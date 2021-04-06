package com.fy.engineserver.activity.clifford.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.activity.activeness.ActivenessManager;
import com.fy.engineserver.activity.activeness.ActivenessType;
import com.fy.engineserver.activity.clifford.CliffordData;
import com.fy.engineserver.activity.newChongZhiActivity.NewChongZhiActivityManager;
import com.fy.engineserver.activity.newChongZhiActivity.NewXiaoFeiActivity;
import com.fy.engineserver.activity.vipExpActivity.VipExpActivityManager;
import com.fy.engineserver.chat.ChatChannelType;
import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.core.CoreSubSystem;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.props.ArticleProperty;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.green.GreenServerManager;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.clifford.Option_CliffordConfirm;
import com.fy.engineserver.menu.trade.Option_Clifford_Choose;
import com.fy.engineserver.message.CLIFFORD_START_REQ;
import com.fy.engineserver.message.CLIFFORD_START_RES;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.stat.ArticleStatManager;
import com.fy.engineserver.trade.TradeManager;
import com.fy.engineserver.util.ProbabilityUtils;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.config.ConfigServiceManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;

public class CliffordManager implements Runnable {

	private static CliffordManager self;

	public static Logger logger = LoggerFactory.getLogger(CliffordManager.class.getName());

	private CliffordManager() {

	}

	public boolean running = false;
	
	public SimpleEntityManager<CliffordData> em;

	public SimpleEntityManager<CliffordData> getEm() {
		return em;
	}

	public List<NeewNoticeArticle> noticeArticles = new ArrayList<NeewNoticeArticle>();
	
	public static int 每天可以祈福的次数 = 8;

	// public static int 每天可以祈福的次数 = 13;

	public void destory() {
		if (data != null && data.isDirty()) {
			try {
				em.flush(data);
				em.destroy();
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("[祈福保存失败]", e);
			}
			data.setDirty(false);
			logger.warn("[祈福保存成功]");
		}
	}

	public void init() throws Exception {
		

		em = SimpleEntityManagerFactory.getSimpleEntityManager(CliffordData.class);

//		System.out.println("--------------------------初始化CliffordManager--------------------------");
		long startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if (fileName != null && fileName.isFile() && fileName.exists()) {
			fileName = new File(ConfigServiceManager.getInstance().getFilePath(fileName));
			initFile(fileName);
		} else {
			if (logger.isWarnEnabled()) logger.warn("[配置文件不错在] [{}]", new Object[] { fileName.getPath() });
		}

		long[] ids = em.queryIds(CliffordData.class, "");
		if (ids != null && ids.length > 0) {
			data = em.find(ids[ids.length - 1]);
		}
		if (data == null) {
			data = new CliffordData();
			long id = em.nextId();
			data.id = id;
			em.save(data);
		}

		Thread thread = new Thread(this, "CliffordManager");
		running = true;
		thread.start();
		self = this;
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
		int date = calendar.get(Calendar.DATE);
		this.date = date;
		ServiceStartRecord.startLog(this);
	}

	public static CliffordManager getInstance() {
		return self;
	}

	/**
	 * 祈福概率文件
	 */
	public File fileName;

	public File getFileName() {
		return fileName;
	}

	public void setFileName(File fileName) {
		this.fileName = fileName;
	}

	/**
	 * 每天免费祈福次数
	 */
	public static int EVERY_DAY_CLIFFORD_NUM = 0;

	public static final String 祈福所需物品 = Translate.福缘石;

	public static int CLIFFORD_NEED_YINZI = 100000;

	public static int ONCE_CLIFFORD_MAX_NUM = 8;

	public CliffordData data;

	private Random random = new Random();

	/**
	 * 用于实际获得物品的概率
	 * 第一维为级别
	 */
	public ArticleProperty[][] 开启祈福获得物品概率配置;

	/**
	 * 用于用户点查看余下部分的概率
	 */
	public ArticleProperty[][] 查看祈福物品概率配置;

	/**
	 * 对应概率数组的第一维，概率数组的第一维下标和这个数组的第一维下标一致
	 * 第二维表示玩家级别数组，最小级别，最大级别，包含最小最大值
	 */
	public int[][] 玩家级别配置 = new int[][] { { 0, 20 }, { 21, 40 }, { 41, 60 }, { 61, 80 }, { 81, 100 }, { 101, 120 }, { 121, 140 }, { 141, 160 }, { 161, 180 }, { 181, 200 }, { 201, 220 }, { 221, 240 }, { 241, 260 }, { 261, 280 }, { 281, 300 } };
	public static final int 数组长度 = 15;

	public ArticleProperty[] 根据级别得到获得物品概率数组(int level) {
		int index = 0;
		for (int i = 0; i < 玩家级别配置.length; i++) {
			if (玩家级别配置[i][0] <= level && 玩家级别配置[i][1] >= level) {
				index = i;
				break;
			}
		}
		if (开启祈福获得物品概率配置 != null && 开启祈福获得物品概率配置.length > 0) {
			if (开启祈福获得物品概率配置.length > index) {
				return 开启祈福获得物品概率配置[index];
			} else {
				return 开启祈福获得物品概率配置[0];
			}
		}
		return null;
	}

	public ArticleProperty[] 根据级别得到查看物品概率数组(int level) {
		int index = 0;
		for (int i = 0; i < 玩家级别配置.length; i++) {
			if (玩家级别配置[i][0] <= level && 玩家级别配置[i][1] >= level) {
				index = i;
				break;
			}
		}
		if (查看祈福物品概率配置 != null && 查看祈福物品概率配置.length > 0) {
			if (查看祈福物品概率配置.length > index) {
				return 查看祈福物品概率配置[index];
			} else {
				return 查看祈福物品概率配置[0];
			}
		}
		return null;
	}

	/**
	 * 初始化物品
	 */
	public void initFile(File file) throws Exception {
		if (file != null && file.isFile() && file.exists()) {
			InputStream is = new FileInputStream(file);
			POIFSFileSystem pss = new POIFSFileSystem(is);
			HSSFWorkbook workbook = new HSSFWorkbook(pss);

			HSSFSheet sheet = workbook.getSheetAt(0);
			int rows = sheet.getPhysicalNumberOfRows();

			ArticleProperty[][] 开启祈福获得物品概率配置 = new ArticleProperty[数组长度][];
			ArticleProperty[][] 查看祈福物品概率配置 = new ArticleProperty[数组长度][];
			for (int i = 0; i < 数组长度; i++) {
				开启祈福获得物品概率配置[i] = new ArticleProperty[rows - 1];
				查看祈福物品概率配置[i] = new ArticleProperty[rows - 1];
			}
			for (int r = 1; r < rows; r++) {
				HSSFRow row = sheet.getRow(r);
				if (row != null) {
					for (int i = 0; i < 数组长度; i++) {
						try {
							HSSFCell cell = row.getCell(i * 5 + 0);
							String articleName = (cell.getStringCellValue().trim());

							开启祈福获得物品概率配置[i][r - 1] = new ArticleProperty();
							查看祈福物品概率配置[i][r - 1] = new ArticleProperty();

							开启祈福获得物品概率配置[i][r - 1].setArticleName(articleName);
							查看祈福物品概率配置[i][r - 1].setArticleName(articleName);

							cell = row.getCell(i * 5 + 1);
							int getProb = (int) cell.getNumericCellValue();
							开启祈福获得物品概率配置[i][r - 1].setProb(getProb);

							cell = row.getCell(i * 5 + 2);
							int seeProb = (int) cell.getNumericCellValue();
							查看祈福物品概率配置[i][r - 1].setProb(seeProb);

							cell = row.getCell(i * 5 + 3);
							int color = (int) cell.getNumericCellValue();
							开启祈福获得物品概率配置[i][r - 1].setColor(color);
							查看祈福物品概率配置[i][r - 1].setColor(color);

							cell = row.getCell(i * 5 + 4);
							int count = (int) cell.getNumericCellValue();
							开启祈福获得物品概率配置[i][r - 1].setCount(count);
							查看祈福物品概率配置[i][r - 1].setCount(count);

						} catch (Exception ex) {

						}
					}
				}
			}
			for (int i = 0; i < 开启祈福获得物品概率配置.length; i++) {
				ArrayList<ArticleProperty> apList = new ArrayList<ArticleProperty>();
				ArrayList<ArticleProperty> apList2 = new ArrayList<ArticleProperty>();
				for (int j = 0; j < 开启祈福获得物品概率配置[i].length; j++) {
					if (开启祈福获得物品概率配置[i][j] != null && 查看祈福物品概率配置[i][j] != null) {
						apList.add(开启祈福获得物品概率配置[i][j]);
						apList2.add(查看祈福物品概率配置[i][j]);
					}
				}
				开启祈福获得物品概率配置[i] = apList.toArray(new ArticleProperty[0]);
				查看祈福物品概率配置[i] = apList2.toArray(new ArticleProperty[0]);
			}
			this.开启祈福获得物品概率配置 = 开启祈福获得物品概率配置;
			this.查看祈福物品概率配置 = 查看祈福物品概率配置;
			
			{//祈福开出某个物品广播
				try{
					sheet = workbook.getSheetAt(1);
					rows = sheet.getPhysicalNumberOfRows();
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {
							int nameindex = 0;
							int colorindex = 1;
							HSSFCell cell = row.getCell(nameindex);
							String articleName = (cell.getStringCellValue().trim());
							cell = row.getCell(colorindex);
							int articlecolor = (int) cell.getNumericCellValue();
							NeewNoticeArticle nna = new NeewNoticeArticle(articleName, articlecolor);
							noticeArticles.add(nna);
							if(Game.logger.isWarnEnabled()){
								Game.logger.warn("[祈福开出物品广播] [加载完成] [总数："+noticeArticles.size()+"] [行："+r+"] [articleName:"+(articleName==null?"":articleName)+"] [articlecolor:"+articlecolor+"]");
							}
						}
					}
				}catch(Exception e){
					e.printStackTrace();
					throw new Exception("[祈福开出物品广播] [加载异常] "+e);
				}
			}
			
			if (is != null) {
				is.close();
			}
		}
	}
	
	public boolean isNotice(String name,int color){
		for(NeewNoticeArticle a : noticeArticles){
			Game.logger.warn("[name:"+name+"] [name2:"+a.articlename+"] [color:"+color+"] [color2:"+a.colortype+"] ["+(a.articlename.equals(name))+"] ["+(a.colortype==color)+"]");
			if(a.articlename.equals(name) && a.colortype==color){
				return true;
			}
		}
		return false;
	}
	
	class NeewNoticeArticle{
		private String articlename;
		private int colortype;
		public NeewNoticeArticle(String articlename,int colortype){
			this.articlename = articlename;
			this.colortype = colortype;
		}
	}

	public synchronized void 祈福(int index, byte cliffordType, Player player, boolean 弹框提醒) {
		if (player == null) {
			return;
		}
		if (CoreSubSystem.beCareful) {
			player.sendError("功能优化中，敬请期待！");
			logger.warn(player.getLogString() + " [打开祈福界面] [返回未开放]");
			return;
		}
		synchronized (player.tradeKey) {
			String result = 祈福正确性判断(index, cliffordType, player);
			if (result != null) {
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, result);
				player.addMessageToRightBag(hreq);
				if (logger.isInfoEnabled()) logger.info("[祈福] [失败] [{}] [{}] [{}] [{}]", new Object[] { player.getUsername(), player.getId(), player.getName(), result });
				return;
			}
	
			boolean isGreenBind = false;
			if (GreenServerManager.isBindYinZiServer()) {
				Integer moneyType = greenCliffordChoose.get(player.getId());
				if (moneyType == null) {
					player.sendError("请选择祈福使用的货币类型。");
					return;
				}
				if (moneyType.intValue() == 1) {
					isGreenBind = true;
				}
			}
	
			// 单次祈福
			if (cliffordType == 0) {
				int count = 0;
				if (data.getTodayCliffordCountMap().get(player.getId()) != null) {
					count = data.getTodayCliffordCountMap().get(player.getId());
				}
				boolean binded = true;
				boolean statMoneyFlag = false;
				if (count >= EVERY_DAY_CLIFFORD_NUM) {
	
					// 扣钱或物品
					ArticleEntity ae = null;
					if (GreenServerManager.isBindYinZiServer()) {
						ae = player.getArticleEntity(祈福所需物品, isGreenBind);
						binded = isGreenBind;
					} else {
						ae = player.getArticleEntity(祈福所需物品);
						binded = false;
					}
					if (弹框提醒) {
						if (ae == null && !player.cliffordNotifyFlag) {
							WindowManager wm = WindowManager.getInstance();
							MenuWindow mw = wm.createTempMenuWindow(600);
							mw.setTitle(Translate.祈福消耗银子提示);
							if (GreenServerManager.isBindYinZiServer()) {
								if (isGreenBind) {
									mw.setDescriptionInUUB(Translate.香火不足是否消耗100两银票进行祈福);
								} else {
									mw.setDescriptionInUUB(Translate.香火不足是否消耗100两银子进行祈福);
								}
							} else {
								mw.setDescriptionInUUB(Translate.香火不足是否消耗100两银子进行祈福);
							}
							Option_CliffordConfirm option = new Option_CliffordConfirm();
							option.setText(Translate.确定);
							option.setColor(0xFFFFFF);
							option.cliffordType = cliffordType;
							option.index = index;
							Option_Cancel cancel = new Option_Cancel();
							cancel.setText(Translate.取消);
							cancel.setColor(0xFFFFFF);
							mw.setOptions(new Option[] { option, cancel });
							CONFIRM_WINDOW_REQ req = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), mw.getOptions());
							player.addMessageToRightBag(req);
							return;
						}
					}
					if (ae != null) {
						ArticleEntity aee = player.removeArticleEntityFromKnapsackByArticleId(ae.getId(), "祈福删除", true);
						if (aee == null) {
							result = Translate.删除物品失败;
							HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, result);
							player.addMessageToRightBag(hreq);
							if (logger.isInfoEnabled()) logger.info("[祈福] [失败] [{}] [{}] [{}] [{}]", new Object[] { player.getUsername(), player.getId(), player.getName(), result });
							return;
						}
	
						// 统计
						ArticleStatManager.addToArticleStat(player, null, aee, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "祈福删除", null);
					} else {
						// 扣钱
						BillingCenter bc = BillingCenter.getInstance();
						try {
							if (GreenServerManager.isBindYinZiServer()) {
								if (isGreenBind) {
									bc.playerExpense(player, CLIFFORD_NEED_YINZI, CurrencyType.SHOPYINZI, ExpenseReasonType.CLIFFORD, "祈福");
									NewChongZhiActivityManager.instance.doXiaoFeiActivity(player, CLIFFORD_NEED_YINZI, NewXiaoFeiActivity.XIAOFEI_TYPE_SHOP);
									binded = true;
								} else {
									bc.playerExpense(player, CLIFFORD_NEED_YINZI, CurrencyType.YINZI, ExpenseReasonType.CLIFFORD, "祈福", VipExpActivityManager.qifu_expends_activity);
									NewChongZhiActivityManager.instance.doXiaoFeiActivity(player, CLIFFORD_NEED_YINZI, NewXiaoFeiActivity.XIAOFEI_TYPE_SHOP);
									binded = false;
								}
							} else {
								bc.playerExpense(player, CLIFFORD_NEED_YINZI, CurrencyType.YINZI, ExpenseReasonType.CLIFFORD, "祈福", VipExpActivityManager.qifu_expends_activity);
								NewChongZhiActivityManager.instance.doXiaoFeiActivity(player, CLIFFORD_NEED_YINZI, NewXiaoFeiActivity.XIAOFEI_TYPE_SHOP);
								binded = false;
							}
						} catch (Exception ex) {
							result = Translate.扣费失败;
							HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, result);
							player.addMessageToRightBag(hreq);
							if (logger.isInfoEnabled()) logger.info("[祈福] [失败] [{}] [{}] [{}] [{}]", new Object[] { player.getUsername(), player.getId(), player.getName(), result }, ex);
							return;
						}
						statMoneyFlag = true;
					}
				}
	
				// 给物品
				ArticleEntityManager aem = ArticleEntityManager.getInstance();
				ArticleManager am = ArticleManager.getInstance();
				if (aem != null && am != null) {
					ArticleProperty[] aps = 根据级别得到获得物品概率数组(player.getLevel());
					if (aps != null && aps.length > 0) {
	
						double[] probabilitys = new double[aps.length];
						double countValue = 0;
						for (int i = 0; i < aps.length; i++) {
							if (aps[i] != null) {
								countValue += aps[i].getProb();
							}
						}
						for (int i = 0; i < aps.length; i++) {
							if (aps[i] != null) {
								probabilitys[i] = aps[i].getProb() * 1.0 / countValue;
							}
						}
						Random r = null;
						if (player != null) {
							r = player.random;
						} else {
							r = random;
						}
	
						int randomIndex = ProbabilityUtils.randomProbability(r, probabilitys);
	
						if (randomIndex >= aps.length) {
							randomIndex = 0;
						}
						ArticleProperty ap = aps[randomIndex];
						if (ap != null) {
							Article a = am.getArticle(ap.getArticleName());
							if (a != null) {
								try {
									ArticleEntity ae = aem.createEntity(a, binded, ArticleEntityManager.CREATE_REASON_CLIFFORD, player, ap.getColor(), ap.getCount(), true);
									if (ae != null) {
										if (!player.putToKnapsacks(ae, ap.getCount(), "祈福获得")) {
	
											MailManager mm = MailManager.getInstance();
											if (mm != null) {
												ArticleEntity[] entities = new ArticleEntity[] { ae };
												int[] counts = new int[] { ap.getCount() };
												String title = Translate.系统邮件提示;
												String content = Translate.由于您的背包已满您得到的部分物品通过邮件发送;
												try {
													mm.sendMail(player.getId(), entities, counts, title, content, 0, 0, 0, "祈福");
													HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, content);
													player.addMessageToRightBag(hreq);
													if (logger.isWarnEnabled()) logger.warn("[邮件] [成功] [{}] [{}] [{}]", new Object[] { player.getUsername(), player.getId(), player.getName() });
												} catch (Exception e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
													if (MailManager.logger.isWarnEnabled()) MailManager.logger.warn("[邮件] [异常] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "]", e);
													if (logger.isWarnEnabled()) logger.warn("[邮件] [异常] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "]", e);
													return;
												}
											}
										} else {
											if(isNotice(ae.getArticleName(),ae.getColorType())){
												ChatMessageService cm = ChatMessageService.getInstance();
												ChatMessage msg = new ChatMessage();
												msg.setSort(ChatChannelType.SYSTEM);
												String des = Translate.translateString(Translate.祈福广播开出物品, new String[][] { { Translate.STRING_1, "<f color='"+ArticleManager.color_article[3]+"'>"+CountryManager.得到国家名(player.getCountry()) +"</f>"},{ Translate.STRING_2, "【"+player.getName()+"】" },{ Translate.STRING_3, "<f color='"+ArticleManager.color_article[ae.getColorType()]+"'>" },{ Translate.STRING_4, ae.getArticleName() }});
												msg.setMessageText(des);
												cm.sendMessageToWorld(msg);
												cm.sendMessageToSystem(msg);
											}
											String des = Translate.translateString(Translate.祈福得到物品, new String[][] { { Translate.STRING_1, ae.getArticleName() }, { Translate.COUNT_1, ap.getCount() + "" } });
											HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, des);
											player.addMessageToRightBag(hreq);
										}
										long[] ids = data.openedCliffordArticleIdMap.get(player.getId());
										int[] counts = data.openedCliffordArticleNumMap.get(player.getId());
										ids[index] = ae.getId();
										counts[index] = ap.getCount();
	
										player.updateClifford(System.currentTimeMillis(), 1);
										if (data.getTodayCliffordCountMap().get(player.getId()) == null) {
											data.getTodayCliffordCountMap().put(player.getId(), 0);
										}
										data.setDirty(true);
										data.getTodayCliffordCountMap().put(player.getId(), (data.getTodayCliffordCountMap().get(player.getId()) + 1));
										int has = player.getArticleEntityNum(祈福所需物品);
										int hasFreeCliffordNum = EVERY_DAY_CLIFFORD_NUM;
										if (data.getTodayCliffordCountMap().get(player.getId()) != null) {
											hasFreeCliffordNum -= data.getTodayCliffordCountMap().get(player.getId());
											if (hasFreeCliffordNum < 0) {
												hasFreeCliffordNum = 0;
											}
										}
	
										CLIFFORD_START_RES res = new CLIFFORD_START_RES(GameMessageFactory.nextSequnceNum(), ids, counts, hasFreeCliffordNum, new int[0], 祈福所需物品, has, player.得到可以祈福的剩余祈福的次数(System.currentTimeMillis()));
										player.addMessageToRightBag(res);
	
										// 统计
										if (statMoneyFlag) {
											ArticleStatManager.addToArticleStat(player, null, ae, ArticleStatManager.OPERATION_物品获得和消耗, CLIFFORD_NEED_YINZI, ArticleStatManager.YINZI, ap.getCount(), "祈福获得", null);
										} else {
											ArticleStatManager.addToArticleStat(player, null, ae, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, ap.getCount(), "祈福获得", null);
										}
										if (AchievementManager.getInstance() != null) {
											AchievementManager.getInstance().record(player, RecordAction.累计祈福次数, 1);
										}
										// 活跃度统计
										if(ActivenessManager.getInstance()!=null){
											ActivenessManager.getInstance().record(player, ActivenessType.祈福);
										}
										if(logger.isInfoEnabled())
										logger.info("[祈福] [成功] [{}] [{}] [{}] [articleId:{}] [articleName:{}] [color:{}] [{}]",new Object[]{player.getUsername(),player.getId(),player.getName(),ae.getId(),ae.getArticleName(),ae.getColorType(),ap.getCount()});
										return;
									} else {
										if (logger.isInfoEnabled()) logger.info("[祈福] [失败] [{}] [{}] [{}] [ae==null]", new Object[] { player.getUsername(), player.getId(), player.getName() });
										return;
									}
								} catch (Exception ex) {
									ex.printStackTrace();
								}
							}
	
						}
	
					}
				}
	
			} else {
	
				// 全部祈福
				int count = 0;
				long[] ids = data.openedCliffordArticleIdMap.get(player.getId());
				for (int i = 0; i < ids.length; i++) {
					if (ids[i] < 0) {
						count++;
					}
				}
	
				// 扣钱或物品
				int hasNum = 0;
				if (GreenServerManager.isBindYinZiServer()) {
					hasNum = player.countArticleInKnapsacksByName(祈福所需物品, isGreenBind);
				} else {
					hasNum = player.getArticleEntityNum(祈福所需物品);
				}
				if (hasNum >= count) {
					int removeCount = 0;
					for (int i = 0; i < count; i++) {
						ArticleEntity ae = null;
						if (GreenServerManager.isBindYinZiServer()) {
							ae = player.getArticleEntity(祈福所需物品, isGreenBind);
						} else {
							ae = player.getArticleEntity(祈福所需物品);
						}
						ArticleEntity removeAe = player.removeArticleEntityFromKnapsackByArticleId(ae.getId(), "祈福删除", true);
						if (removeAe == null) {
							result = Translate.删除物品失败;
							HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, result);
							player.addMessageToRightBag(hreq);
							if (logger.isInfoEnabled()) logger.info("[祈福] [失败] [{}] [{}] [{}] [{}] [{}]", new Object[] { player.getUsername(), player.getId(), player.getName(), result, removeCount });
							return;
						} else {
							removeCount++;
	
							// 统计
							ArticleStatManager.addToArticleStat(player, null, removeAe, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "祈福删除", null);
	
						}
					}
				} else {
					// 扣钱
					int cost = CLIFFORD_NEED_YINZI * (count - hasNum);
					if (弹框提醒) {
						if (!player.cliffordNotifyFlag) {
							WindowManager wm = WindowManager.getInstance();
							MenuWindow mw = wm.createTempMenuWindow(600);
							mw.setTitle(Translate.祈福消耗银子提示);
							if (hasNum > 0) {
								String des = Translate.translateString(Translate.香火不足是否消耗100两银子进行祈福详细提示1, new String[][] { { Translate.COUNT_1, hasNum + "" }, { Translate.STRING_1, BillingCenter.得到带单位的银两(cost) } });
								mw.setDescriptionInUUB(des);
							} else {
								String des = Translate.translateString(Translate.香火不足是否消耗100两银子进行祈福详细提示2, new String[][] { { Translate.STRING_1, BillingCenter.得到带单位的银两(cost) } });
								mw.setDescriptionInUUB(des);
							}
							Option_CliffordConfirm option = new Option_CliffordConfirm();
							option.setText(Translate.确定);
							option.setColor(0xFFFFFF);
							option.cliffordType = cliffordType;
							option.index = index;
							Option_Cancel cancel = new Option_Cancel();
							cancel.setText(Translate.取消);
							cancel.setColor(0xFFFFFF);
							mw.setOptions(new Option[] { option, cancel });
							CONFIRM_WINDOW_REQ req = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), mw.getOptions());
							player.addMessageToRightBag(req);
							return;
						}
					}
					// 删除背包中的物品，并且扣费
					int removeCount = 0;
					for (int i = 0; i < hasNum; i++) {
						ArticleEntity ae = null;
						if (GreenServerManager.isBindYinZiServer()) {
							ae = player.getArticleEntity(祈福所需物品, isGreenBind);
						} else {
							ae = player.getArticleEntity(祈福所需物品);
						}
						ArticleEntity removeAe = player.removeArticleEntityFromKnapsackByArticleId(ae.getId(), "祈福删除", true);
						if (removeAe == null) {
							result = Translate.删除物品失败;
							HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, result);
							player.addMessageToRightBag(hreq);
							if (logger.isInfoEnabled()) logger.info("[祈福] [失败] [{}] [{}] [{}] [{}] [{}]", new Object[] { player.getUsername(), player.getId(), player.getName(), result, removeCount });
							return;
						} else {
							removeCount++;
	
							// 统计
							ArticleStatManager.addToArticleStat(player, null, removeAe, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "祈福删除", null);
	
						}
					}
	
					if (cost > 0) {
						BillingCenter bc = BillingCenter.getInstance();
						try {
							if (GreenServerManager.isBindYinZiServer()) {
								if (isGreenBind) {
									bc.playerExpense(player, cost, CurrencyType.SHOPYINZI, ExpenseReasonType.CLIFFORD, Translate.祈福);
									NewChongZhiActivityManager.instance.doXiaoFeiActivity(player, cost, NewXiaoFeiActivity.XIAOFEI_TYPE_SHOP);
								} else {
									bc.playerExpense(player, cost, CurrencyType.YINZI, ExpenseReasonType.CLIFFORD, Translate.祈福, VipExpActivityManager.qifu_expends_activity);
									NewChongZhiActivityManager.instance.doXiaoFeiActivity(player, cost, NewXiaoFeiActivity.XIAOFEI_TYPE_SHOP);
								}
							} else {
								bc.playerExpense(player, cost, CurrencyType.YINZI, ExpenseReasonType.CLIFFORD, Translate.祈福, VipExpActivityManager.qifu_expends_activity);
								NewChongZhiActivityManager.instance.doXiaoFeiActivity(player, cost, NewXiaoFeiActivity.XIAOFEI_TYPE_SHOP);
							}
						} catch (Exception ex) {
							result = Translate.扣费失败;
							HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, result);
							player.addMessageToRightBag(hreq);
							if (logger.isInfoEnabled()) logger.info("[祈福] [失败] [{}] [{}] [{}] [{}] [{}]", new Object[] { player.getUsername(), player.getId(), player.getName(), result, cost }, ex);
							return;
						}
					}
				}
	
				// 给物品
				ArticleEntityManager aem = ArticleEntityManager.getInstance();
				ArticleManager am = ArticleManager.getInstance();
				if (aem != null && am != null) {
					Set<ArticleEntity> rewards = new HashSet<ArticleEntity>();
					ArticleProperty[] aps = 根据级别得到获得物品概率数组(player.getLevel());
					if (aps != null && aps.length > 0) {
	
						double[] probabilitys = new double[aps.length];
						double countValue = 0;
						for (int i = 0; i < aps.length; i++) {
							if (aps[i] != null) {
								countValue += aps[i].getProb();
							}
						}
						for (int i = 0; i < aps.length; i++) {
							if (aps[i] != null) {
								probabilitys[i] = aps[i].getProb() * 1.0 / countValue;
							}
						}
						Random r = player.random;
						MailManager mm = MailManager.getInstance();
						for (int i = 0; i < count; i++) {
							int randomIndex = ProbabilityUtils.randomProbability(r, probabilitys);
	
							if (randomIndex >= aps.length) {
								randomIndex = 0;
							}
							ArticleProperty ap = aps[randomIndex];
							if (ap != null) {
								Article a = am.getArticle(ap.getArticleName());
								if (a != null) {
									try {
										ArticleEntity ae = aem.createEntity(a, isGreenBind, ArticleEntityManager.CREATE_REASON_CLIFFORD, player, ap.getColor(), ap.getCount(), true);
										if (ae != null) {
											if (!player.putToKnapsacks(ae, ap.getCount(), "祈福获得")) {
	
												if (mm != null) {
													ArticleEntity[] entities = new ArticleEntity[] { ae };
													int[] counts = new int[] { ap.getCount() };
													String title = Translate.系统邮件提示;
													String content = Translate.由于您的背包已满您得到的部分物品通过邮件发送;
													try {
														mm.sendMail(player.getId(), entities, counts, title, content, 0, 0, 0, "祈福");
														rewards.add(ae);
														HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, content);
														player.addMessageToRightBag(hreq);
														if (logger.isWarnEnabled()) logger.warn("[邮件] [成功] [{}] [{}] [{}]", new Object[] { player.getUsername(), player.getId(), player.getName() });
													} catch (Exception e) {
														e.printStackTrace();
														if (MailManager.logger.isWarnEnabled()) MailManager.logger.warn("[邮件] [异常] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "]", e);
														if (logger.isWarnEnabled()) logger.warn("[邮件] [异常] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "]", e);
														return;
													}
												}
											} else {
												rewards.add(ae);
												String des = Translate.translateString(Translate.祈福得到物品, new String[][] { { Translate.STRING_1, ae.getArticleName() }, { Translate.COUNT_1, ap.getCount() + "" } });
												HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, des);
												player.addMessageToRightBag(hreq);
											}
											ids = data.openedCliffordArticleIdMap.get(player.getId());
											int[] counts = data.openedCliffordArticleNumMap.get(player.getId());
	
											if (logger.isWarnEnabled()) logger.warn("[祈福] [成功] [{}] [{}] [{}] [articleId:{}] [articleName:{}] [color:{}] [{}]", new Object[] { player.getUsername(), player.getId(), player.getName(), ae.getId(), ae.getArticleName(), ae.getColorType(), ap.getCount() });
											for (int j = 0; j < ids.length; j++) {
												if (ids[j] < 0) {
													ids[j] = ae.getId();
													counts[j] = ap.getCount();
													break;
												}
											}
											data.setDirty(true);
	
											// 统计
											if (i < hasNum) {
												ArticleStatManager.addToArticleStat(player, null, ae, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, ap.getCount(), "祈福获得", null);
											} else {
												ArticleStatManager.addToArticleStat(player, null, ae, ArticleStatManager.OPERATION_物品获得和消耗, CLIFFORD_NEED_YINZI, ArticleStatManager.YINZI, ap.getCount(), "祈福获得", null);
											}
	
										} else {
											if (logger.isInfoEnabled()) logger.error(player.getLogString() + "[祈福失败] [创建entity失败] [" + ap.getArticleName() + "]");
											return;
										}
									} catch (Exception ex) {
										ex.printStackTrace();
									}
								} else {
									logger.error(player.getLogString() + " [祈福失败] [含有不存在的物品:" + ap.getArticleName() + "]");
								}
							} else {
								logger.error(player.getLogString() + " [祈福失败] [含有不存在的物品,randomIndex:" + randomIndex + "]");
							}
						}
	
						if(rewards.size()>0){
							StringBuffer sbb = new StringBuffer();
							for(ArticleEntity ae:rewards){
								if(isNotice(ae.getArticleName(),ae.getColorType())){
									sbb.append("<f color='"+ArticleManager.color_article[ae.getColorType()]+"'>"+ae.getArticleName()+"</f>.");
								}
							}
							if(sbb.length()>0){
								Game.logger.warn("[name2] [rewards:"+rewards.size()+"] [sbb:"+sbb.toString()+"]");
								ChatMessageService cm = ChatMessageService.getInstance();
								ChatMessage msg = new ChatMessage();
								msg.setSort(ChatChannelType.SYSTEM);
								String des = Translate.translateString(Translate.祈福广播开出物品2, new String[][] { { Translate.STRING_1, "<f color='"+ArticleManager.color_article[3]+"'>"+CountryManager.得到国家名(player.getCountry()) +"</f>"},{ Translate.STRING_2, "【"+player.getName()+"】" },{ Translate.STRING_3, sbb.toString() }});
								msg.setMessageText(des);
								try {
									cm.sendMessageToWorld(msg);
									cm.sendMessageToSystem(msg);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							
							
						}	
						player.updateClifford(System.currentTimeMillis(), count);
						ids = data.openedCliffordArticleIdMap.get(player.getId());
						int[] counts = data.openedCliffordArticleNumMap.get(player.getId());
						int has = player.getArticleEntityNum(祈福所需物品);
						int hasFreeCliffordNum = EVERY_DAY_CLIFFORD_NUM;
						if (data.getTodayCliffordCountMap().get(player.getId()) != null) {
							hasFreeCliffordNum -= data.getTodayCliffordCountMap().get(player.getId());
							if (hasFreeCliffordNum < 0) {
								hasFreeCliffordNum = 0;
							}
						}
						CLIFFORD_START_RES res = new CLIFFORD_START_RES(GameMessageFactory.nextSequnceNum(), ids, counts, hasFreeCliffordNum, new int[0], 祈福所需物品, has, player.得到可以祈福的剩余祈福的次数(System.currentTimeMillis()));
						player.addMessageToRightBag(res);
						if (AchievementManager.getInstance() != null && count >= 8) {
							AchievementManager.getInstance().record(player, RecordAction.祈福全开次数, 1);
						}
						// 活跃度统计
						if(ActivenessManager.getInstance()!=null){
							ActivenessManager.getInstance().record(player, ActivenessType.祈福,8);
						}
						return;
	
					}
				}
			}
		}
	}

	public String 祈福正确性判断(int index, byte cliffordType, Player player) {
		if (cliffordType == 0) {
			if (index < 0 || index >= ONCE_CLIFFORD_MAX_NUM) {
				return Translate.请遵守游戏规则不要使用外挂;
			}
		}

		if (data != null) {

			// 判断
			int count = 0;
			if (data.getTodayCliffordCountMap().get(player.getId()) != null) {
				count = data.getTodayCliffordCountMap().get(player.getId());
			}
			if (count >= EVERY_DAY_CLIFFORD_NUM) {
				boolean isGreenBind = false;
				if (GreenServerManager.isBindYinZiServer()) {
					Integer moneyType = greenCliffordChoose.get(player.getId());
					if (moneyType == null) {
						return "请选择祈福使用的货币类型。";
					}
					if (moneyType.intValue() == 1) {
						isGreenBind = true;
					}
					if (isGreenBind) {
						if (player.getShopSilver() + player.getSilver() < CLIFFORD_NEED_YINZI) {
							if (player.countArticleInKnapsacksByName(祈福所需物品, true) <= 0) {
								return Translate.translateString(Translate.祈福需要石头, new String[][] { { Translate.STRING_1, 祈福所需物品 } });
							}
						}
					} else {
						if (player.getSilver() < CLIFFORD_NEED_YINZI) {
							if (player.countArticleInKnapsacksByName(祈福所需物品, false) <= 0) {
								return Translate.translateString(Translate.祈福需要石头, new String[][] { { Translate.STRING_1, 祈福所需物品 } });
							}
						}
					}
				} else {
					if (player.getSilver() < CLIFFORD_NEED_YINZI) {
						if (player.getArticleEntityNum(祈福所需物品) <= 0) {
							return Translate.translateString(Translate.祈福需要石头, new String[][] { { Translate.STRING_1, 祈福所需物品 } });
						}
					}
				}
			}

			long[] ids = data.openedCliffordArticleIdMap.get(player.getId());
			int[] counts = data.openedCliffordArticleNumMap.get(player.getId());
			if (ids == null) {
				ids = new long[ONCE_CLIFFORD_MAX_NUM];
				counts = new int[ONCE_CLIFFORD_MAX_NUM];
				for (int i = 0; i < ONCE_CLIFFORD_MAX_NUM; i++) {
					ids[i] = -1;
				}
				data.openedCliffordArticleIdMap.put(player.getId(), ids);
				data.openedCliffordArticleNumMap.put(player.getId(), counts);
			}

			boolean has = false;
			for (int i = 0; i < ids.length; i++) {
				if (ids[i] == -1) {
					has = true;
					break;
				}
			}
			if (!has) {
				return Translate.请选择重新祈福;
			}

			if (cliffordType == 0) {
				if (index >= ids.length) {
					return Translate.服务器出现错误;
				}

				if (ids[index] > 0) {
					return Translate.不能选择查看过的位置;
				}

				if (player.得到可以祈福的剩余祈福的次数(System.currentTimeMillis()) <= 0) {
					return Translate.您今天祈福次数已满;
				}
			}

			// 如果是全部祈福，那么判断银子或物品够不够
			if (cliffordType != 0) {
				int leftCount = 0;
				for (int i = 0; i < ids.length; i++) {
					if (ids[i] == -1) {
						leftCount++;
					}
				}

				if (player.得到可以祈福的剩余祈福的次数(System.currentTimeMillis()) < leftCount) {
					return Translate.您今天剩余祈福次数不足;
				}

				if (GreenServerManager.isBindYinZiServer()) {
					boolean isGreenBind = false;
					Integer moneyType = greenCliffordChoose.get(player.getId());
					if (moneyType == null) {
						return "请选择祈福使用的货币类型。";
					}
					if (moneyType.intValue() == 1) {
						isGreenBind = true;
					}
					if (isGreenBind) {
						int hasNum = player.countArticleInKnapsacksByName(祈福所需物品, true);
						if (hasNum < leftCount) {
							int cost = CLIFFORD_NEED_YINZI * (leftCount - hasNum);
							if (player.getSilver() + player.getShopSilver() < cost) {
								return Translate.translateString(Translate.您没有足够的石头或银子, new String[][] { { Translate.STRING_1, 祈福所需物品 } });
							}
						}
					} else {
						int hasNum = player.countArticleInKnapsacksByName(祈福所需物品, false);
						if (hasNum < leftCount) {
							int cost = CLIFFORD_NEED_YINZI * (leftCount - hasNum);
							if (player.getSilver() < cost) {
								return Translate.translateString(Translate.您没有足够的石头或银子, new String[][] { { Translate.STRING_1, 祈福所需物品 } });
							}
						}
					}
				} else {
					int hasNum = player.getArticleEntityNum(祈福所需物品);
					if (hasNum < leftCount) {
						int cost = CLIFFORD_NEED_YINZI * (leftCount - hasNum);
						if (player.getSilver() < cost) {
							return Translate.translateString(Translate.您没有足够的石头或银子, new String[][] { { Translate.STRING_1, 祈福所需物品 } });
						}
					}
				}
			}

		} else {
			if (logger.isInfoEnabled()) logger.info("[祈福] [失败] [{}] [{}] [{}] [data==null]", new Object[] { player.getUsername(), player.getId(), player.getName() });
			return Translate.服务器出现错误;
		}

		return null;
	}

	public synchronized void 查看剩余祈福(Player player) {
		if (player == null) {
			return;
		}
		String result = 查看剩余祈福合法性判断(player);
		if (result != null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, result);
			player.addMessageToRightBag(hreq);
			if (logger.isInfoEnabled()) logger.info("[祈福查看] [失败] [{}] [{}] [{}] [{}]", new Object[] { player.getUsername(), player.getId(), player.getName(), result });
			return;
		}

		int count = 0;
		long[] ids = data.openedCliffordArticleIdMap.get(player.getId());
		for (int i = 0; i < ids.length; i++) {
			if (ids[i] < 0) {
				count++;
			}
		}
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		ArticleManager am = ArticleManager.getInstance();
		if (aem != null && am != null) {
			int[] indexs = new int[count];
			ArticleProperty[] aps = 根据级别得到查看物品概率数组(player.getLevel());
			if (aps != null && aps.length > 0) {

				double[] probabilitys = new double[aps.length];
				double countValue = 0;
				for (int i = 0; i < aps.length; i++) {
					if (aps[i] != null) {
						countValue += aps[i].getProb();
					}
				}
				for (int i = 0; i < aps.length; i++) {
					if (aps[i] != null) {
						probabilitys[i] = aps[i].getProb() * 1.0 / countValue;
					}
				}
				Random r = null;
				if (player != null) {
					r = player.random;
				} else {
					r = random;
				}

				for (int i = 0; i < count; i++) {
					int randomIndex = ProbabilityUtils.randomProbability(r, probabilitys);

					if (randomIndex >= aps.length) {
						randomIndex = 0;
					}
					ArticleProperty ap = aps[randomIndex];
					if (ap != null) {
						Article a = am.getArticle(ap.getArticleName());
						if (a != null) {
							if (logger.isInfoEnabled()) logger.info("[祈福查看] [成功] [{}] [{}] [{}] [{}] [{}] [count:{}] [{}] [{}]", new Object[] { player.getUsername(), player.getId(), player.getName(), a.getName(), ap.getCount(), count, i, a, getClass() });
							try {
								ArticleEntity ae = aem.createTempEntity(a, false, ArticleEntityManager.CREATE_REASON_CLIFFORD, player, ap.getColor());
								if (ae != null) {
									ids = data.openedCliffordArticleIdMap.get(player.getId());
									int[] counts = data.openedCliffordArticleNumMap.get(player.getId());
									if (logger.isInfoEnabled()) logger.info("[祈福查看] [成功] [{}] [{}] [{}] [{}] [{}] [count:{}] [{}]", new Object[] { player.getUsername(), player.getId(), player.getName(), ae.getId(), ap.getCount(), count, i });
									for (int j = 0; j < ids.length; j++) {
										if (ids[j] < 0) {
											ids[j] = ae.getId();
											counts[j] = ap.getCount();
											indexs[i] = j;
											break;
										}
									}
									data.setDirty(true);
								} else {
									if (logger.isWarnEnabled()) logger.warn("[祈福查看] [失败] [{}] [{}] [{}] [ae==null]", new Object[] { player.getUsername(), player.getId(), player.getName() });
									return;
								}
							} catch (Exception ex) {
								if (logger.isWarnEnabled()) logger.warn("[祈福查看] [异常]", ex);
							}
						}

					}
				}

				ids = data.openedCliffordArticleIdMap.get(player.getId());
				int[] counts = data.openedCliffordArticleNumMap.get(player.getId());
				int has = player.getArticleEntityNum(祈福所需物品);
				int hasFreeCliffordNum = EVERY_DAY_CLIFFORD_NUM;
				if (data.getTodayCliffordCountMap().get(player.getId()) != null) {
					hasFreeCliffordNum -= data.getTodayCliffordCountMap().get(player.getId());
					if (hasFreeCliffordNum < 0) {
						hasFreeCliffordNum = 0;
					}
				}
				CLIFFORD_START_RES res = new CLIFFORD_START_RES(GameMessageFactory.nextSequnceNum(), ids, counts, hasFreeCliffordNum, indexs, 祈福所需物品, has, player.得到可以祈福的剩余祈福的次数(System.currentTimeMillis()));
				player.addMessageToRightBag(res);
				if (logger.isInfoEnabled()) logger.info("[祈福查看] [成功] [{}] [{}] [{}] [剩余数量:{}]", new Object[] { player.getUsername(), player.getId(), player.getName(), count });
				return;

			}
		}
	}

	public String 查看剩余祈福合法性判断(Player player) {
		if (data != null) {
			long[] ids = data.openedCliffordArticleIdMap.get(player.getId());
			if (ids == null || ids.length == 0) {
				return Translate.最少进行一次祈福才可以查看;
			}
			boolean hasClifford = false;
			boolean leftClifford = false;
			for (int i = 0; i < ids.length; i++) {
				if (ids[i] > 0) {
					hasClifford = true;
				} else {
					leftClifford = true;
				}
			}
			if (!hasClifford) {
				return Translate.最少进行一次祈福才可以查看;
			}
			if (!leftClifford) {
				return Translate.不能进行查看;
			}
		} else {
			if (logger.isInfoEnabled()) logger.info("[查看祈福] [失败] [{}] [{}] [{}] [data==null]", new Object[] { player.getUsername(), player.getId(), player.getName() });
			return Translate.服务器出现错误;
		}
		return null;
	}

	/**
	 * 分为重新开始祈福和继续祈福两种
	 */

	public HashMap<Long, Integer> greenCliffordChoose = new HashMap<Long, Integer>();

	public synchronized void 打开祈福界面(CLIFFORD_START_REQ req, Player player, boolean isMenu) {
		if (player == null) {
			return;
		}
		if (CoreSubSystem.beCareful) {
			player.sendError("功能优化中，敬请期待！");
			logger.warn(player.getLogString() + " [打开祈福界面] [返回未开放]");
			return;
		}
		
		byte cliffordType = req.getCliffordType();
		if (cliffordType == 0) {
			// 如果是第一次进祈福界面，如果是绿色服2 先选择使用绑定不绑定 祈福
			if (GreenServerManager.isBindYinZiServer() && isMenu) {
				greenCliffordChoose.remove(player.getId());
				Option_Clifford_Choose silver = new Option_Clifford_Choose(req, 0);
				silver.setText(Translate.使用银子);
				Option_Clifford_Choose shopsilver = new Option_Clifford_Choose(req, 1);
				shopsilver.setText(Translate.使用银票);
				MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
				mw.setOptions(new Option[] { silver, shopsilver });
				mw.setTitle(Translate.选择祈福使用的的货币);
				mw.setDescriptionInUUB(Translate.祈福选货币提示 + Translate.银票 + ":" + TradeManager.putMoneyToMyText(player.getShopSilver()) + "\n银子:" + TradeManager.putMoneyToMyText(player.getSilver()));
				QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
				player.addMessageToRightBag(res);
				return;
			}
		}
		Integer moneyType = null;
		if (GreenServerManager.isBindYinZiServer()) {
			moneyType = greenCliffordChoose.get(player.getId());
			if (moneyType == null) {
				player.sendError("请选择货币类型。");
				return;
			}
			if (data != null) {
				CLIFFORD_START_RES res = null;
				long[] ids = new long[ONCE_CLIFFORD_MAX_NUM];
				int[] counts = new int[ONCE_CLIFFORD_MAX_NUM];
				for (int i = 0; i < ONCE_CLIFFORD_MAX_NUM; i++) {
					ids[i] = -1;
				}
				data.openedCliffordArticleIdMap.put(player.getId(), ids);
				data.openedCliffordArticleNumMap.put(player.getId(), counts);
				data.setDirty(true);
				int has = 0;
				if (moneyType.intValue() == 0) {
					has = player.countArticleInKnapsacksByName(祈福所需物品, false);
				} else if (moneyType.intValue() == 1) {
					has = player.countArticleInKnapsacksByName(祈福所需物品, true);
				}
				int hasFreeCliffordNum = EVERY_DAY_CLIFFORD_NUM;
				if (data.getTodayCliffordCountMap().get(player.getId()) != null) {
					hasFreeCliffordNum -= data.getTodayCliffordCountMap().get(player.getId());
					if (hasFreeCliffordNum < 0) {
						hasFreeCliffordNum = 0;
					}
				}
				res = new CLIFFORD_START_RES(GameMessageFactory.nextSequnceNum(), ids, counts, hasFreeCliffordNum, new int[0], 祈福所需物品, has, player.得到可以祈福的剩余祈福的次数(System.currentTimeMillis()));
				player.addMessageToRightBag(res);
				if (logger.isInfoEnabled()) logger.info("[打开祈福界面] [成功,类型{}] [{}] [{}] [{}]", new Object[] { cliffordType, player.getUsername(), player.getId(), player.getName() });
			} else {
				if (logger.isInfoEnabled()) logger.info("[打开祈福界面] [失败data==null] [{}] [{}] [{}]", new Object[] { player.getUsername(), player.getId(), player.getName() });
			}
		} else {
			if (data != null) {
				CLIFFORD_START_RES res = null;
				long[] ids = new long[ONCE_CLIFFORD_MAX_NUM];
				int[] counts = new int[ONCE_CLIFFORD_MAX_NUM];
				for (int i = 0; i < ONCE_CLIFFORD_MAX_NUM; i++) {
					ids[i] = -1;
				}
				data.openedCliffordArticleIdMap.put(player.getId(), ids);
				data.openedCliffordArticleNumMap.put(player.getId(), counts);
				data.setDirty(true);
				int has = player.getArticleEntityNum(祈福所需物品);
				int hasFreeCliffordNum = EVERY_DAY_CLIFFORD_NUM;
				if (data.getTodayCliffordCountMap().get(player.getId()) != null) {
					hasFreeCliffordNum -= data.getTodayCliffordCountMap().get(player.getId());
					if (hasFreeCliffordNum < 0) {
						hasFreeCliffordNum = 0;
					}
				}
				res = new CLIFFORD_START_RES(req.getSequnceNum(), ids, counts, hasFreeCliffordNum, new int[0], 祈福所需物品, has, player.得到可以祈福的剩余祈福的次数(System.currentTimeMillis()));
				player.addMessageToRightBag(res);
				if (logger.isInfoEnabled()) logger.info("[打开祈福界面] [成功,类型{}] [{}] [{}] [{}]", new Object[] { cliffordType, player.getUsername(), player.getId(), player.getName() });
			} else {
				if (logger.isInfoEnabled()) logger.info("[打开祈福界面] [失败data==null] [{}] [{}] [{}]", new Object[] { player.getUsername(), player.getId(), player.getName() });
			}
		}
	}

	int date;

	@Override
	public void run() {
		Calendar calendar = Calendar.getInstance();
		while (running) {
			try {
				Thread.sleep(3600000);
				if (!com.fy.engineserver.util.ContextLoadFinishedListener.isLoadFinished()) {
					continue;
				}
				if (data != null && data.isDirty()) {
					em.flush(data);
					data.setDirty(false);
				}

				calendar.setTimeInMillis(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
				int date = calendar.get(Calendar.DATE);
				if (this.date != date) {
					int hour = calendar.get(Calendar.HOUR_OF_DAY);
					long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
					if (hour == 0) {
						if (每天更新()) {
							if (logger.isInfoEnabled()) logger.info("[祈福系统维护] [{}ms]", new Object[] { (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) });
						} else {
							if (logger.isInfoEnabled()) logger.info("[祈福系统维护失败] [{}ms]", new Object[] { (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) });
						}
					}
					this.date = date;
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				if (logger.isWarnEnabled()) logger.warn("[祈福管理器] [异常]", ex);
			}
		}
	}

	public synchronized boolean 每天更新() {
		if (data != null) {
			data.openedCliffordArticleIdMap.clear();
			data.todayCliffordCountMap.clear();
			data.openedCliffordArticleNumMap.clear();
			data.setDirty(true);
			return true;
		}
		return false;
	}
}
