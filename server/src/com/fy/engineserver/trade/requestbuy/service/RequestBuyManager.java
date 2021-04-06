package com.fy.engineserver.trade.requestbuy.service;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.boss.authorize.exception.BillFailedException;
import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.activity.newChongZhiActivity.NewChongZhiActivityManager;
import com.fy.engineserver.activity.newChongZhiActivity.NewXiaoFeiActivity;
import com.fy.engineserver.articleProtect.ArticleProtectDataValues;
import com.fy.engineserver.articleProtect.ArticleProtectManager;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.core.RequestBuySubSystem;
import com.fy.engineserver.dajing.DajingStudioManager;
import com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.EquipmentEntity;
import com.fy.engineserver.datasource.article.data.equipments.Equipment;
import com.fy.engineserver.datasource.article.data.props.Cell;
import com.fy.engineserver.datasource.article.data.props.Knapsack;
import com.fy.engineserver.datasource.article.data.props.PetEggProps;
import com.fy.engineserver.datasource.article.data.props.Props;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.economic.SavingFailedException;
import com.fy.engineserver.economic.SavingReasonType;
import com.fy.engineserver.economic.thirdpart.migu.entity.ArticleTradeRecord;
import com.fy.engineserver.economic.thirdpart.migu.entity.ArticleTradeRecordManager;
import com.fy.engineserver.enterlimit.EnterLimitManager;
import com.fy.engineserver.enterlimit.EnterLimitManager.PlayerRecordType;
import com.fy.engineserver.green.GreenServerManager;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.Option_TradeConfirm4CancleRB;
import com.fy.engineserver.menu.Option_TradeConfirm4SaleRB;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.REQUESTBUY_CANCEL_SELF_RES;
import com.fy.engineserver.message.REQUESTBUY_RELEASE_RES;
import com.fy.engineserver.message.REQUESTBUY_SALE_RES;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.PlayerSimpleInfo;
import com.fy.engineserver.sprite.PlayerSimpleInfoManager;
import com.fy.engineserver.stat.ArticleStatManager;
import com.fy.engineserver.trade.TradeManager;
import com.fy.engineserver.trade.boothsale.TradeItem;
import com.fy.engineserver.trade.exceptions.EntityNotFoundException;
import com.fy.engineserver.trade.exceptions.NotSelfRequestBuyException;
import com.fy.engineserver.trade.requestbuy.RequestBuy;
import com.fy.engineserver.trade.requestbuy.RequestBuyBuConfig;
import com.fy.engineserver.trade.requestbuy.RequestBuyInfo4Client;
import com.fy.engineserver.trade.requestbuy.RequestBuyRule;
import com.fy.engineserver.trade.requestbuy.RequestOption;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.StringTool;
import com.fy.engineserver.validate.OtherValidateManager;
import com.fy.engineserver.validate.ValidateAsk;
import com.fy.engineserver.validate.ValidateManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;

public class RequestBuyManager implements Runnable {

	// public static Logger logger = Logger.getLogger(RequestBuySubSystem.class);
	public static Logger logger = LoggerFactory.getLogger(RequestBuySubSystem.class);

	private String file;

	private String configFile; // 补求购的配置文件
	private ArrayList<RequestBuyBuConfig> buConfigs = new ArrayList<RequestBuyBuConfig>();
	private ArrayList<RequestBuyBuConfig> removeConfigs = new ArrayList<RequestBuyBuConfig>();
	private RequestBuyBuConfig tempConfig;
	private long writeTime;
	public boolean isOpen = true;

	public Hashtable<String, ArticleEntity> tempEntity;

	public ArticleEntity getTempEntity(Article article, int colorType) {
		ArticleEntity entity = tempEntity.get(article.getName() + colorType);
		if (entity == null) {
			try {
				entity = ArticleEntityManager.getInstance().createTempEntity(article, false, DefaultArticleEntityManager.CREATE_REASON_REQUESTBUY, null, colorType);
				tempEntity.put(article.getName() + colorType, entity);
			} catch (Exception ex) {
				logger.error("求购取临时Entity出错:", ex);
			}
		}
		return entity;
	}

	// public static LruMapCache rbCache;

	private static RequestBuyManager instance;

	public SimpleEntityManager<RequestBuy> em;

	public boolean running = true;
	private int sept = 1;

	private final long MAX_PRICE = 99999999L;

	/** 热卖检查条数 */
	public static int REMAI = 5;
	/** 求购收费 */
	public int feeMoney = 10;

	public static int REQUESTBUY_MAX_NUM = 15;

	/** 求购价格下限限制 */
	public static final double perPriceLessLimit = 0.5;
	/** 规则管理 */
	private List<RequestBuyRule> requestBuyRule = new ArrayList<RequestBuyRule>();
	/** 同一求购规则下的求购信息列表,保证每个LIST的第一个是最高价 */
	private Hashtable<RequestBuyRule, List<RequestBuy>> underRuleMap = new Hashtable<RequestBuyRule, List<RequestBuy>>();
	/***/
	private Hashtable<Long, List<RequestBuy>> playerRuleMap = new Hashtable<Long, List<RequestBuy>>();
	/* 是否是热卖 只要是热卖就在这个表里 */
	private Hashtable<String, Boolean> isReMaiMap = new Hashtable<String, Boolean>();
	public boolean isOpenReMaiMap = true;
	// /** 装备求购等级阶 (0~20],(20,30]....... */
	// public static int[] RBLVStep = { 20, 30, 40, 60, 70, 80, 90, 110, 130, 200 };
	/** 求购持续时间 */
	public static final long requestBuyLast = 1000 * 60 * 60 * 24;
	/** 求购列表 */
	private Hashtable<Long, RequestBuy> requestBuyMap = new Hashtable<Long, RequestBuy>();

	public static final String[] COLOR_TYPES = new String[] { Translate.白, Translate.绿, Translate.蓝, Translate.紫 };

	private static final String LEVEL_SHOW_CHAR = "-"; // 等级范围中间那个分隔符
	private static final int[][] LEVEL_SECTION = new int[][] { { 1, 20 }, { 21, 40 }, { 41, 60 }, { 61, 80 }, { 81, 100 }, { 101, 120 }, { 121, 140 }, { 141, 160 }, { 161, 180 }, { 181, 200 }, { 201, 220 }, { 221, 240 }, { 241, 260 }, { 261, 280 }, { 281, 300 } };
	public static String[] LEVEL_STRINGS;

	public long startMoney; // 系统启动时求购中的钱
	public long allMoney; // 求购中的钱
	public long requestMoney; // 发布的钱
	public long saleMoney; // 出售的钱
	public long cancleMoney; // 取消的钱
	public long taxMoney; // 税
	public long timeOverMoney; // 到期的钱

	public static RequestOption[] option;
	static {
		int[] first = new int[] { ArticleManager.物品一级分类_角色装备类,
				ArticleManager.物品一级分类_马匹装备类,
				ArticleManager.物品一级分类_宝石类, 
				ArticleManager.物品一级分类_人物消耗品类,
				ArticleManager.物品一级分类_宠物消耗品,
				ArticleManager.物品一级分类_材料类,
				ArticleManager.物品一级分类_角色技能书类 };
		option = new RequestOption[first.length];
		for (int i = 0; i < option.length; i++) {
			if (i == 0 || i == 1) {
				option[i] = new RequestOption();
				option[i].setFirst(ArticleManager.物品一级分类类名[first[i]]);
				option[i].setSecond(new String[] {});
				continue;
			} else if (i == 3) {
				option[i] = new RequestOption();
				option[i].setFirst(ArticleManager.物品一级分类类名[first[i]]);
				option[i].setSecond(new String[] { ArticleManager.物品二级分类类名_人物消耗品类[0], ArticleManager.物品二级分类类名_人物消耗品类[1], ArticleManager.物品二级分类类名_人物消耗品类[2] });
				continue;
			} else if (i == 5) {
				option[i] = new RequestOption();
				option[i].setFirst(ArticleManager.物品一级分类类名[first[i]]);
				option[i].setSecond(new String[] {Translate.圣物, Translate.蚕丝});
				continue;
			} else if (i == 2) {
				option[i] = new RequestOption();
				option[i].setFirst(ArticleManager.物品一级分类类名[first[i]]);
				option[i].setSecond(new String[] {Translate.绿宝石, Translate.橙宝石, Translate.橙晶石, Translate.黑金石, Translate.蓝宝石, Translate.红宝石, Translate.白晶石, Translate.黄宝石, Translate.紫晶石});
				continue;
			}
			option[i] = new RequestOption();
			option[i].setFirst(ArticleManager.物品一级分类类名[first[i]]);
			option[i].setSecond(ArticleManager.物品二级分类类名[first[i]]);
		}
	}

	/**
	 * 新增一个求购信息
	 * @param requestBuy
	 */
	public synchronized void addNewRequestBuy(RequestBuy requestBuy) {
		// 增加后排序 和第一个比较 ,如果价格低,放到最后一位,直到第一个被卖光的时候,做整体排序
		if (!getUnderRuleMap().containsKey(requestBuy.getRule())) {
			getUnderRuleMap().put(requestBuy.getRule(), new ArrayList<RequestBuy>());
		}
		List<RequestBuy> temp = getUnderRuleMap().get(requestBuy.getRule());
		if (temp.size() > 0 && temp.get(0).compareTo(requestBuy) > 0) {
			temp.add(0, requestBuy);
		} else {
			temp.add(requestBuy);
		}
		getUnderRuleMap().put(requestBuy.getRule(), temp);
		addToReMai(requestBuy.getRule());
		getRequestBuyMap().put(requestBuy.getId(), requestBuy);
	}

	/**
	 * 查询符合条件的最高价的求购信息
	 * @param entityId
	 * @return
	 */
	public RequestBuy getAccordRequestBuy(long entityId) {
		return getAccordRequestBuy(entityId, -1);
	}

	public RequestBuy getAccordRequestBuy(long entityId, long rbId) {
		RequestBuyRule buyRule = getBelongsRule(entityId);
		if (buyRule == null) {
			return null;
		}
		List<RequestBuy> fitList = getUnderRuleMap().get(buyRule);
		if (fitList == null || fitList.size() == 0) {
			return null;
		}
		if (rbId == -1) {
			int i = 0;
			while(i < fitList.size() && ChatMessageService.getInstance().isSilence(fitList.get(i).getReleaserId()) >= 2) {
				i++;
			}
			if (i < fitList.size()) {
				return fitList.get(i);
			}else {
				return null;
			}
		} else {
			for (RequestBuy buy : fitList) {
				if (buy.getId() == rbId) {
					return buy;
				}
			}
			return null;
		}
	}

	private Hashtable<String, String[]> propNames = new Hashtable<String, String[]>();

	// 拿分类下的物品名称
	public synchronized CompoundReturn msg_getProp(String first, String second) {
		String[] names = propNames.get(first + second);
		// int[] colors = propColors.get(first+second);
		if (names == null) {
			names = ArticleManager.getInstance().根据物品一级二级分类得到物品名称列表(first, second);
			propNames.put(first + second, names);
		}
		// Article article = ArticleManager.getInstance().getArticle(names[0]);
		// ArticleEntityManager.getInstance().createTempEntity(article, false, reason, player, color)
		// ArticleManager.getInstance().t
		// article.
		int[] colors = new int[names.length];
		int colorDELETE = 0;
		for (int i = 0; i < names.length; i++) {
			Integer color = special_color.get(names[i]);
			if (color != null) {
				if (color.intValue() <= 3) {
					colors[i] = color.intValue();
				} else {
					names[i] = null;
					colorDELETE++;
				}
			} else {
				colors[i] = -1;
			}
		}
		if (colorDELETE > 0) {
			String[] nameTemp = new String[names.length - colorDELETE];
			int[] colorsTemp = new int[names.length - colorDELETE];
			for (int i = 0, j = 0; i < names.length; i++) {
				if (names[i] == null) {
					j++;
				} else {
					nameTemp[i - j] = names[i];
					colorsTemp[i - j] = colors[i];
				}
			}
			propNames.put(first + second, nameTemp);
			names = nameTemp;
			colors = colorsTemp;
		}
		CompoundReturn compoundReturn = new CompoundReturn();
		compoundReturn.setStringValues(names);
		compoundReturn.setIntValues(colors);
		return compoundReturn;
	}

	public synchronized void releaseRequestSale(Player player, int index, long entityId, int saleNum, long rbId, boolean isAskBefore) {
		synchronized (player.tradeKey) {
			Knapsack knapsack = player.getKnapsack_common();
			if (knapsack == null) {
				return;
			}
			Cell cell = knapsack.getCell(index);
			if ((cell == null || cell.isEmpty())) {
				player.sendError(Translate.物品不存在);
				return;
			}
			if (cell.getEntityId() != entityId) {
				player.sendError(Translate.物品不存在);
				return;
			}
			ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(entityId);
			if (ae == null) {
				player.sendError(Translate.text_requestbuy_009 + entityId);
				return;
			}
			if (ae.isBinded()) {
				player.sendError(Translate.text_requestbuy_010);
				return;
			}

			if (!GreenServerManager.canToOtherPlayer(ae)) {
				player.sendError(Translate.text_trade_023);
				return;
			}

			if (ae.getEndTime() > 0 && ae.getEndTime() - System.currentTimeMillis() < 24 * 60 * 60 * 1000) {
				player.sendError(Translate.text_auction_117);
				return;
			}
			if (saleNum > cell.getCount() || saleNum <= 0) {
				player.sendError(Translate.text_requestbuy_011);
				return;
			}
			if (!ArticleProtectManager.instance.isCanDo(player, ArticleProtectDataValues.ArticleProtect_Common, ae.getId())) {
				player.sendError(Translate.锁魂物品不能做此操作);
				return;
			}

			RequestBuy requestBuy = RequestBuyManager.getInstance().getAccordRequestBuy(entityId, rbId);
			if (requestBuy == null || requestBuy.getItem().getEntityNum() <= 0 || requestBuy.isBeOver() || requestBuy.isTimeOut()) {
				player.sendError(Translate.text_requestbuy_002);
				return;
			}
			Player releaser;
			try {
				releaser = PlayerManager.getInstance().getPlayer(requestBuy.getReleaserId());
			} catch (Exception e) {
				player.sendError(Translate.text_requestbuy_012);
				logger.error("求购人不见了", e);
				return;
			}
			if (isAskBefore) {
				int state = OtherValidateManager.getInstance().getValidateState(player, OtherValidateManager.ASK_TYPE_REQUESTBUY);
				if (state == ValidateManager.VALIDATE_STATE_需要答题) {
					ValidateAsk ask = OtherValidateManager.getInstance().sendValidateAsk(player, OtherValidateManager.ASK_TYPE_REQUESTBUY);
					ask.setAskFormParameters(new Object[] { index, entityId, saleNum, rbId });
					return;
				}
			}

			int relNum = requestBuy.getItem().getEntityNum() > saleNum ? saleNum : requestBuy.getItem().getEntityNum();

			try {
				BillingCenter.getInstance().playerSaving(player, requestBuy.getItem().getPerPrice() * relNum, CurrencyType.SHOPYINZI, SavingReasonType.REQUESTBUY, "求购出售物品");
				saleMoney += requestBuy.getItem().getPerPrice() * relNum;
				allMoney -= requestBuy.getItem().getPerPrice() * relNum;
				if (logger.isWarnEnabled()) logger.warn("[求购出售] [成功] [钱={}] [{}] [{}]", new Object[] { requestBuy.getItem().getPerPrice() * relNum, player.getLogString(), requestBuy.getLogString() });
			} catch (SavingFailedException e) {
				if (logger.isErrorEnabled()) logger.error("[求购出售物品，给卖家钱出错:] PID=[{}] EID=[{}] EName=[{}]", new Object[] { player.getId(), entityId, ae.getArticleName() });
				return;
			}
			try {
				MailManager.getInstance().sendMail(requestBuy.getReleaserId(), new ArticleEntity[] { ArticleEntityManager.getInstance().getEntity(entityId) }, new int[] { relNum }, Translate.text_requestbuy_004, Translate.text_requestbuy_005, 0, 0, 0, "给求购者发物品");
			
				//给求购打标记
				
				{
					try
					{
						ArticleTradeRecord articleTradeRecord =  new ArticleTradeRecord();
						articleTradeRecord.setSellPlayerId(player.getId());
						articleTradeRecord.setBuyPlayerId(requestBuy.getReleaserId());
						
						long[] articleIds = new long[1];
						articleIds[0] = ae.getId();
						articleTradeRecord.setArticleIds(articleIds);
						
						articleTradeRecord.setDesc("求购/快速出售");
						articleTradeRecord.setTradeTime(System.currentTimeMillis());
						
						ArticleTradeRecordManager.getInstance().notifyNew(articleTradeRecord);
					}
					catch(Exception e)
					{
						ArticleTradeRecordManager.logger.error("[创建竞拍交易记录] [失败] [出现未知异常] [sell:"+player.getId()+"] [buyer:"+releaser.getId()+"]",e);
					}
				}
				
				if (logger.isWarnEnabled()) logger.warn("[求购出售] [邮件成功] [entityID={}] [relNum={}] [{}] [{}]", new Object[] { entityId, relNum, player.getLogString(), requestBuy.getLogString() });
			} catch (Exception e) {
				if (logger.isWarnEnabled()) logger.warn("[求购出售物品，给求购者邮寄物品:] PID=[{}] EID=[{}] EName=[{}]", new Object[] { player.getId(), entityId, ae.getArticleName() });
				return;
			}
			chanagerSaleTime(requestBuy);
			if (logger.isWarnEnabled()) logger.warn("[求购出售] [真正成功] [entityID={}] [relNum={}] [{}] [{}]", new Object[] { entityId, relNum, player.getLogString(), requestBuy.getLogString() });
			DajingStudioManager.getInstance().notify_快速出售银锭(player, requestBuy.getItem().getPerPrice() * relNum);
			ArticleStatManager.addToArticleStat(player, releaser, ae, ArticleStatManager.OPERATION_交换, requestBuy.getItem().getPerPrice(), ArticleStatManager.YINZI, relNum, "快速出售", null);
			if (GreenServerManager.isBindYinZiServer()) {
				player.send_HINT_REQ(Translate.text_requestbuy_027 + TradeManager.putMoneyToMyText(requestBuy.getItem().getPerPrice() * relNum), (byte) 5);
			} else {
				player.send_HINT_REQ(Translate.text_requestbuy_013 + TradeManager.putMoneyToMyText(requestBuy.getItem().getPerPrice() * relNum), (byte) 5);
			}
			knapsack.clearCell(index, relNum, "快速出售", false);
			requestBuy.getItem().subNum(requestBuy, relNum);

			AchievementManager.getInstance().record(releaser, RecordAction.求购次数);
			AchievementManager.getInstance().record(player, RecordAction.快速出售次数);

			REQUESTBUY_SALE_RES res = new REQUESTBUY_SALE_RES(GameMessageFactory.nextSequnceNum(), relNum, requestBuy.getItem().getPerPrice() * relNum);
			player.addMessageToRightBag(res);

			EnterLimitManager.setValues(player, PlayerRecordType.快速出售次数);
		}
	}

	/**
	 * 确实发布一个求购
	 * @param player
	 * @param entityId
	 * @param num
	 * @param perPrice
	 */
	public synchronized void relReleaseRequestBuy(Player player, RequestBuyRule buyRule, int num, long perPrice) {
		long startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		long endTime = startTime + requestBuyLast;
		List<RequestBuy> currRBs = getUnderRuleMap().get(buyRule);
		List<RequestBuy> playerBs = getPlayerRuleMap().get(player.getId());
		if (playerBs != null && playerBs.size() >= REQUESTBUY_MAX_NUM) {
			player.sendError(Translate.text_requestbuy_014);
			return;
		}

		if (num > 999 || num <= 0) {
			player.sendError(Translate.text_requestbuy_015);
			logger.error("[求购] [外挂] [p={}] [num={}] [price={}]", new Object[] { player.getLogString(), num, perPrice });
			return;
		}

		if (perPrice <= 0 || perPrice > MAX_PRICE) {
			player.sendError(Translate.text_requestbuy_016);
			logger.error("[求购] [外挂] [p={}] [num={}] [price={}]", new Object[] { player.getLogString(), num, perPrice });
			return;
		}

		// 当前类型的求购
		if (currRBs != null && currRBs.size() > 0) {
			RequestBuy currMax = null;
			int rIndex = 0;
			while(rIndex < currRBs.size() && ChatMessageService.getInstance().isSilence(currRBs.get(rIndex).getReleaserId()) >= 2) {
				rIndex++;
			}
			if (rIndex < currRBs.size()) {
				currMax = currRBs.get(rIndex);
			}
			if (currMax != null) {
				if (currMax.getItem().getPerPrice() > (int) (perPrice / perPriceLessLimit)) {
					HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.text_requestbuy_017);
					player.addMessageToRightBag(req);
					return;
				}
			}
		}

		long needMoney = perPrice * num;
		long tax = needMoney * feeMoney / 100;
		if (tax <= 0) {
			tax = 1;
		}
		long needMoney2 = needMoney;
		needMoney = needMoney + tax;
		if (player.getSilver() < needMoney || needMoney < 0 || tax < 0) {
			player.sendError(Translate.text_requestbuy_006);
			return;
		}
		try {
			if (PlatformManager.getInstance().isPlatformOf(Platform.韩国)) {
				BillingCenter.getInstance().playerExpense(player, needMoney2, CurrencyType.YINZI, ExpenseReasonType.REQUESTBUY, "求购预存费用和手续费", -1);
				BillingCenter.getInstance().playerExpense(player, tax, CurrencyType.YINZI, ExpenseReasonType.REQUESTBUY, "求购预存费用和手续费");
			} else {
				BillingCenter.getInstance().playerExpense(player, needMoney, CurrencyType.YINZI, ExpenseReasonType.REQUESTBUY, "求购预存费用和手续费");
			}
			requestMoney += perPrice * num;
			allMoney += perPrice * num;
			taxMoney += tax;
			NewChongZhiActivityManager.instance.doXiaoFeiActivity(player, tax, NewXiaoFeiActivity.XIAOFEI_TYPE_TAX);
			// ChongZhiActivity.getInstance().doXiaoFeiActivity(player, tax, XiaoFeiServerConfig.XIAOFEI_TONGDAO_JIAOYISHUI);
			// ChongZhiActivity.getInstance().doXiaoFeiMoneyActivity(player, tax, MoneyBillBoardActivity.XIAOFEI_TONGDAO_JIAOYISHUI);
			if (logger.isWarnEnabled()) logger.warn("[发布求购] [扣钱] [{}] [{}]", new Object[] { player.getLogString(), needMoney });
		} catch (com.fy.boss.authorize.exception.NoEnoughMoneyException e) {
			if (logger.isErrorEnabled()) logger.error("求购预存费用扣除出错：Player=[{}] RName1=[{}] RName2=[{}] needMoney=[{}]", new Object[] { player.getId(), buyRule.getMainType(), buyRule.getSubType(), needMoney });
			return;
		} catch (BillFailedException e) {
			if (logger.isErrorEnabled()) logger.error("求购预存费用扣除出错：Player=[{}] RName1=[{}] RName2=[{}] needMoney=[{}]", new Object[] { player.getId(), buyRule.getMainType(), buyRule.getSubType(), needMoney });
			return;
		}
		TradeItem item = new TradeItem(buyRule.getArticleName(), num, perPrice, startTime, endTime);
		item.setStartNum(num);
		RequestBuy buy = RequestBuy.createRequestBuy().init(player.getId(), player.getName(), item, buyRule);
		if (buy == null) {
			player.sendError(Translate.text_requestbuy_018);
			return;
		}
		if (getPlayerRuleMap().get(player.getId()) == null) {
			getPlayerRuleMap().put(player.getId(), new ArrayList<RequestBuy>());
		}
		getPlayerRuleMap().get(player.getId()).add(buy);
		em.notifyNewObject(buy);
		addNewRequestBuy(buy);
		if (ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_角色装备类].equals(buy.getRule().getMainType()) || ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_马匹装备类].equals(buy.getRule().getMainType())) {
			ArticleStatManager.addToArticleStat(player, null, 0, null, null, 0, buy.getRule().getMainType(), buy.getRule().getGrade(), COLOR_TYPES[buy.getRule().getColor()], false, ArticleStatManager.OPERATION_交换, tax, ArticleStatManager.YINZI, num, "发布求购", null);
		} else {
			ArticleStatManager.addToArticleStat(player, null, 0, null, null, 0, buy.getRule().getArticleName(), buy.getRule().getGrade(), COLOR_TYPES[buy.getRule().getColor()], false, ArticleStatManager.OPERATION_交换, tax, ArticleStatManager.YINZI, num, "发布求购", null);
		}
		if (logger.isWarnEnabled()) logger.warn("[发布求购] [成功] [{}] [{}] [共花费{}]", new Object[] { player.getLogString(), buy.getLogString(), needMoney });
		REQUESTBUY_RELEASE_RES res = new REQUESTBUY_RELEASE_RES(GameMessageFactory.nextSequnceNum());
		player.addMessageToRightBag(res);
		HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 2, Translate.text_requestbuy_019);
		player.addMessageToRightBag(req);

		EnterLimitManager.setValues(player, PlayerRecordType.求购次数);
	}

	// /**
	// * 获得价格支出
	// * @param num
	// * @param perPrice
	// * @return
	// */
	// public CompoundReturn getCost(int num, int perPrice) {
	// int price = num * perPrice;// 物品总价格
	// int duty = feeMoney;// 税
	// int handlingCharge = getHandingCharge(num, price);// 手续费
	// return new CompoundReturn().setIntValues(new int[] { price, duty, handlingCharge }).setIntValue(price + duty + handlingCharge);
	// }
	//
	// /**
	// * 计算手续费
	// * @param num
	// * @return
	// */
	// public int getHandingCharge(int num, int price) {
	// return 0;
	// }

	/**
	 * 通过实ID得到所属的模板
	 * @param entityId
	 * @return
	 */
	public RequestBuyRule getBelongsRule(long entityId) {
		ArticleEntity articleEntity = ArticleEntityManager.getInstance().getEntity(entityId);
		if (articleEntity == null) {
			// logger.error("entityId对应对象不存在["+entityId+"]");
			if (logger.isWarnEnabled()) logger.warn("entityId对应对象不存在[{}]", new Object[] { entityId });
			return null;
		}
		Article article = ArticleManager.getInstance().getArticle(articleEntity.getArticleName());
		if (article == null) {
			// logger.error("模板数据不存在["+entityId+"]");
			if (logger.isWarnEnabled()) logger.warn("模板数据不存在[{}]", new Object[] { entityId });
			return null;
		}
		if (article instanceof Equipment) {
			Equipment entity = (Equipment) article;
			int level = entity.getPlayerLevelLimit();
			String levelName = null;
			for (int i = 0; i < LEVEL_SECTION.length; i++) {
				if (level <= LEVEL_SECTION[i][1] && level >= LEVEL_SECTION[i][0]) {
					if (LEVEL_SECTION[i][0] > 220) {
						levelName = Translate.仙 + (LEVEL_SECTION[i][0] - 220) + LEVEL_SHOW_CHAR + Translate.仙 + (LEVEL_SECTION[i][1] - 220);
					} else {
						levelName = LEVEL_SECTION[i][0] + LEVEL_SHOW_CHAR + LEVEL_SECTION[i][1];
					}
					break;
				}
			}
			logger.warn("~~~~~~~~~~~~   level=[" + level + "]   [levelName=" + levelName + "]");
			return getRequestBuyRule(article.get物品一级分类(), "", levelName, articleEntity.getColorType());
		} else {
			return getRequestBuyRule(article.get物品一级分类(), article.get物品二级分类(), articleEntity.getArticleName(), articleEntity.getColorType());
		}
	}

	/**
	 * 拿到物品对应的列表
	 * @param entityId
	 * @return
	 */
	public List<RequestBuy> getBelongsList(long entityId) {
		RequestBuyRule buyRule = getBelongsRule(entityId);
		return getUnderRuleMap().get(buyRule);
	}

	/**
	 * 取消求购
	 * @param player
	 * @param rbId
	 * @return
	 * @throws EntityNotFoundException
	 * @throws NotSelfRequestBuyException
	 */
	public synchronized CompoundReturn msg_cancel(Player player, long rbId) {
		MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);

		mw.setTitle(Translate.text_requestbuy_020);
		mw.setDescriptionInUUB(Translate.text_requestbuy_021);
		Option_TradeConfirm4CancleRB cancleRB = new Option_TradeConfirm4CancleRB(rbId);
		Option_Cancel cancel = new Option_Cancel();
		cancleRB.setText(Translate.确定);
		cancel.setText(Translate.取消);
		mw.setOptions(new Option[] { cancleRB, cancel });
		if (logger.isInfoEnabled()) logger.info("[求购] [尝试取消求购] [pid={}] [pName={}] [rbid={}]", new Object[] { player.getId(), player.getName(), rbId });
		CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), mw.getOptions());
		player.addMessageToRightBag(creq);
		return null;
	}

	/**
	 * 真正取消求购
	 * @param player
	 * @param rbId
	 */
	public synchronized void relCancel(Player player, long rbId) {
		RequestBuy requestBuy = getRequestBuyById(rbId);
		boolean canCancel = true;
		String result = "";
		if (requestBuy == null) {
			canCancel = false;
			result = Translate.text_requestbuy_002;
			player.sendError(result);
			return;
		}
		if (requestBuy.isBeOver() || requestBuy.isTimeOut()) {
			canCancel = false;
			result = Translate.text_requestbuy_002;
			player.sendError(result);
			return;
		}
		if (player.getId() != requestBuy.getReleaserId()) {
			canCancel = false;
			result = Translate.text_requestbuy_001;
			player.sendError(result);
			return;
		}
		if (canCancel) {
			try {
				BillingCenter.getInstance().playerSaving(player, requestBuy.getItem().getPerPrice() * requestBuy.getItem().getEntityNum(), CurrencyType.YINZI, SavingReasonType.REQUEST_CANCLE, "求购取消退预存费用");
				cancleMoney += requestBuy.getItem().getPerPrice() * requestBuy.getItem().getEntityNum();
				allMoney -= requestBuy.getItem().getPerPrice() * requestBuy.getItem().getEntityNum();
				if (logger.isWarnEnabled()) logger.warn("[取消求购] [退钱={}] [{}] [{}]", new Object[] { requestBuy.getItem().getPerPrice() * requestBuy.getItem().getEntityNum(), player.getLogString(), requestBuy.getLogString() });
			} catch (SavingFailedException e) {
				if (logger.isErrorEnabled()) logger.error("[给取消的玩家退钱出错:] RID=[{}] RName=[{}] PID=[{}] Money=[{}]", new Object[] { requestBuy.getId(), requestBuy.getReleaserName(), requestBuy.getReleaserId(), requestBuy.getItem().getEntityNum() * requestBuy.getItem().getPerPrice() });
				return;
			}
			player.send_HINT_REQ(Translate.text_requestbuy_022 + TradeManager.putMoneyToMyText(requestBuy.getItem().getPerPrice() * requestBuy.getItem().getEntityNum()), (byte) 5);
			requestBuy.setBeOver(true);
			if (logger.isWarnEnabled()) logger.warn("[取消求购] [真正成功] [{}] [{}]", new Object[] { player.getLogString(), requestBuy.getLogString() });
		}
		if (ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_角色装备类].equals(requestBuy.getRule().getMainType()) || ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_马匹装备类].equals(requestBuy.getRule().getMainType())) {
			ArticleStatManager.addToArticleStat(player, null, 0, null, null, 0, requestBuy.getRule().getMainType(), requestBuy.getRule().getGrade(), COLOR_TYPES[requestBuy.getRule().getColor()], false, ArticleStatManager.OPERATION_交换, 0, ArticleStatManager.YINZI, requestBuy.getItem().getEntityNum(), "求购取消", null);
		} else {
			ArticleStatManager.addToArticleStat(player, null, 0, null, null, 0, requestBuy.getRule().getArticleName(), requestBuy.getRule().getGrade(), COLOR_TYPES[requestBuy.getRule().getColor()], false, ArticleStatManager.OPERATION_交换, 0, ArticleStatManager.YINZI, requestBuy.getItem().getEntityNum(), "求购取消", null);
		}
		REQUESTBUY_CANCEL_SELF_RES res = new REQUESTBUY_CANCEL_SELF_RES(GameMessageFactory.nextSequnceNum(), canCancel ? (byte) 0 : 1, result);
		player.addMessageToRightBag(res);
	}

	public synchronized CompoundReturn msg_lookOtherRequestBuys(Player p, String mainType, String subType, String articleName, int color, String level) {
		try {
			List<RequestBuyInfo4Client> buys = new ArrayList<RequestBuyInfo4Client>();
			List<RequestBuyRule> rules = new ArrayList<RequestBuyRule>();
			for (int i = 0; i < requestBuyRule.size(); i++) {
				RequestBuyRule buyRule = requestBuyRule.get(i);
				if (!mainType.equals("")) {
					if (!mainType.equals(buyRule.getMainType())) {
						continue;
					}
				}
				if (!subType.equals("")) {
					if (!subType.equals(buyRule.getSubType())) {
						continue;
					}
				}
				if (!articleName.equals("")) {
					if (!articleName.equals(buyRule.getArticleName())) {
						continue;
					}
				}
				if (color >= 0) {
					if (color != buyRule.getColor()) {
						continue;
					}
				}
				
				if (ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_角色装备类].equals(mainType) || ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_马匹装备类].equals(mainType)) {
					if (!"".equals(level)) {
						String[] grades = null;
						boolean isXian = false;
						if (level.indexOf(Translate.仙) >= 0) {
							isXian = true;
							grades = level.split(LEVEL_SHOW_CHAR);
							grades[0] = grades[0].substring(Translate.仙.length());
							grades[1] = grades[1].substring(Translate.仙.length());
						} else {
							grades = level.split(LEVEL_SHOW_CHAR);
						}
						int g = Integer.parseInt(grades[0]) + (isXian ? 220 : 0);
						int mg = Integer.parseInt(grades[1]) + (isXian ? 220 : 0);
						logger.warn("~~~~~~~~~~~~   level=[" + level + "][" + grades[0] + "][" + grades[0] + "][g=" + g + "][mg=" + mg + "]");
						if (buyRule.getGrade() != g || buyRule.getMaxGrade() != mg) {
							continue;
						}
					}
				}
				rules.add(buyRule);
			}
			for (int i = 0; i < rules.size(); i++) {
				List<RequestBuy> list = getUnderRuleMap().get(rules.get(i));
				if (list != null) {
					for (int j = 0; j < list.size(); j++) {
						RequestBuy buy = list.get(j);
						if (buy.getReleaserId() != p.getId() && ChatMessageService.getInstance().isSilence(buy.getReleaserId()) >= 2) {
							continue;
						}
						buys.add(buy.getRequestBuyInfo4Client());
					}
				}
			}
			CompoundReturn compoundReturn = new CompoundReturn();
			compoundReturn.setObjValue(buys);
			if (logger.isInfoEnabled()) logger.info("[查询他人求购] [成功] [条件{},{},{},{},{}] [size={}]", new Object[] { mainType, subType, articleName, color, level, buys.size() });
			return compoundReturn;
		} catch (Exception e) {
			if (logger.isWarnEnabled()) logger.warn("msg_lookOtherRequestBuys出错===", e);
		}
		return null;
	}

	/**
	 * 查看自己的求购信息
	 * @param player
	 * @return List<RequestBuy> Object
	 */
	public CompoundReturn msg_lookUpSelfRequestBuys(Player player) {
		List<RequestBuy> ids = getPlayerRuleMap().get(player.getId());
		List<RequestBuyInfo4Client> list = new ArrayList<RequestBuyInfo4Client>();
		if (ids == null) {
			return CompoundReturn.createCompoundReturn().setObjValue(list);
		}
		for (RequestBuy buy : ids) {
			if (buy != null && !buy.isBeOver() && !buy.isTimeOut()) {
				list.add(buy.getRequestBuyInfo4Client());
			}
		}
		if (logger.isInfoEnabled()) logger.info("[查询自己求购] [成功] [pid={}] [pName={}] [size={}]", new Object[] { player.getId(), player.getName(), list.size() });
		return CompoundReturn.createCompoundReturn().setObjValue(list);
	}

	// /**
	// * 查询某物品的最高价求购信息
	// * @param entityId
	// * @return RequestBuy Object
	// */
	// public CompoundReturn msg_queryMaxRequestBuy(long entityId) {
	// RequestBuy requestBuy = getAccordRequestBuy(entityId);
	// return CompoundReturn.createCompoundReturn().setObjValue(requestBuy);
	// }

	public RequestBuy queryMaxRequestBuy(String mainType, String subType, String articleName, int color) {
		RequestBuyRule buyRule = getRequestBuyRule(mainType, subType, articleName, color);
		if (buyRule == null) {
			return null;
		}
		List<RequestBuy> fitList = getUnderRuleMap().get(buyRule);
		if (fitList == null || fitList.size() == 0) {
			return null;
		}
		return fitList.get(0);
	}

	public synchronized CompoundReturn msg_getFastBuy(Player player) {
		CompoundReturn compoundReturn = new CompoundReturn();
		try {
			int level = player.getLevel();
			String[] propnames = null;
			for (int i = 0; i < levels.length; i++) {
				if (level <= levels[i][1] && level >= levels[i][0]) {
					propnames = levelpropNames[i];
					break;
				}
			}
			if (propnames != null) {
				RequestBuyInfo4Client[] buyInfo4Clients = new RequestBuyInfo4Client[propnames.length];
				StringBuffer sb = new StringBuffer("");
				for (int i = 0; i < propnames.length; i++) {
					sb.append(propnames[i]).append(",");
					buyInfo4Clients[i] = new RequestBuyInfo4Client();
					Article article = ArticleManager.getInstance().getArticle(propnames[i]);
					if (article == null) {
						// logger.error("录入快速求购物品不存在" + propnames[i]);
						if (logger.isWarnEnabled()) logger.warn("录入快速求购物品不存在{}", new Object[] { propnames[i] });
						compoundReturn.setObjVlues(new RequestBuyInfo4Client[0]);
						return compoundReturn;
					}
					ArticleEntity articleEntity = getTempEntity(article, 0);

					buyInfo4Clients[i].setMainType(article.get物品一级分类());
					buyInfo4Clients[i].setSubType(article.get物品二级分类());
					buyInfo4Clients[i].setEntityId(articleEntity.getId());
					buyInfo4Clients[i].setIcon(article.getIconId());
					buyInfo4Clients[i].setArticleName(article.getName());
					if (special_color.get(propnames[i]) != null) {
						buyInfo4Clients[i].setColorType(special_color.get(propnames[i]));
					} else {
						buyInfo4Clients[i].setColorType(-1);
					}
				}
				if (logger.isInfoEnabled()) logger.info("[求购] [查询快速求购] [{}] [物品={}]", new Object[] { player.getLogString(), sb.toString() });
				compoundReturn.setObjVlues(buyInfo4Clients);
			} else {
				compoundReturn.setObjVlues(new RequestBuyInfo4Client[0]);
			}
		} catch (Exception e) {
			if (logger.isWarnEnabled()) logger.warn("快速求购列表", e);
			compoundReturn.setObjVlues(new RequestBuyInfo4Client[0]);
		}
		return compoundReturn;
	}

	/**
	 * 出售物品
	 * @param player
	 * @param reqId
	 * @param entityId
	 * @param pakType
	 * @param index
	 * @param saleNum
	 * @param rbId
	 * @return
	 */
	public synchronized CompoundReturn msg_sale(Player player, long entityId, int index, int saleNum, long rbId) {
		Knapsack knapsack = player.getKnapsack_common();
		if (knapsack == null) {
			return null;
		}
		if (ChatMessageService.getInstance().isSilence(player.getId()) >= 2) {
			return null;
		}
		Cell cell = knapsack.getCell(index);
		if (cell == null || cell.isEmpty()) {
			player.sendError(Translate.text_requestbuy_009);
			return null;
		}
		if (cell.getEntityId() != entityId) {
			player.sendError(Translate.text_requestbuy_009);
			return null;
		}
		ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(entityId);
		if (ae.isBinded()) {
			player.sendError(Translate.text_requestbuy_010);
			return null;
		}
		if (saleNum > cell.getCount()) {
			player.sendError(Translate.text_requestbuy_011);
			return null;
		}
		RequestBuy requestBuy = getAccordRequestBuy(entityId, rbId);
		if (requestBuy == null || requestBuy.getItem().getEntityNum() <= 0) {
			player.sendError(Translate.text_requestbuy_002);
			return null;
		}
		if (logger.isInfoEnabled()) logger.info("[求购出售] [尝试出售] [{}] [entityId={}] [index={}] [saleNum={}] [{}]", new Object[] { player.getLogString(), entityId, index, saleNum, requestBuy.getLogString() });
		MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
		mw.setTitle(Translate.text_requestbuy_024);
		mw.setDescriptionInUUB(Translate.text_requestbuy_025);
		Option_Cancel cancel = new Option_Cancel();
		Option_TradeConfirm4SaleRB confirm4SaleRB = new Option_TradeConfirm4SaleRB(entityId, index, saleNum, rbId);
		confirm4SaleRB.setText(Translate.确定);
		mw.setOptions(new Option[] { confirm4SaleRB, cancel });
		CONFIRM_WINDOW_REQ req = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), mw.getOptions());
		player.addMessageToRightBag(req);
		return CompoundReturn.createCompoundReturn();
	}

	/**
	 * 请求快速购物
	 * @param player
	 * @return <背包索引,在求购列表的物品的索引>
	 */
	public synchronized CompoundReturn msg_ask4FastSale(Player player) {
		try {
			ArrayList<Long> list = new ArrayList<Long>();
			Knapsack[] Knapsacks = player.getKnapsacks_common();
			ArrayList<Long> temp = getIndexsInRequestBuy(Knapsacks[0]);
			list.addAll(temp);
			// Knapsack knapsacks = player.getKnapsacks_fangBao();
			// if (knapsacks != null) {
			// temp = getIndexsInRequestBuy(knapsacks);
			// list.addAll(temp);
			// }
			return CompoundReturn.createCompoundReturn().setObjValue(list);
		} catch (Exception e) {
			if (logger.isWarnEnabled()) logger.warn("msg_ask4FastSale请求快速购物:", e);
		}
		return null;
	}

	/**
	 * 得到某个背包中在求购列表中的物品的索引
	 * @param player
	 * @param pakIndex
	 * @return
	 */
	public ArrayList<Long> getIndexsInRequestBuy(Knapsack knapsack) {
		ArrayList<Long> list = new ArrayList<Long>();
		for (byte i = (byte) (knapsack.getCells().length - 1); i >= 0; i--) {
			Cell cell = knapsack.getCells()[i];
			if (cell != null && !cell.isEmpty()) {
				long entityId = cell.getEntityId();
				ArticleEntity articleEntity = ArticleEntityManager.getInstance().getEntity(entityId);
				if (articleEntity == null) {
					// logger.debug("取不到对应的entity对象："+entityId);
					if (logger.isWarnEnabled()) logger.warn("取不到对应的entity对象：{}", new Object[] { entityId });
					continue;
				}
				if (articleEntity.isBinded()) {
					continue;
				}
				Article article = ArticleManager.getInstance().getArticle(articleEntity.getArticleName());

				if (article == null) {
					if (logger.isWarnEnabled()) logger.warn("取不到对应的Article对象：id = {} name = {}", new Object[] { entityId }, articleEntity.getArticleName());
					continue;
				}

				if (articleEntity instanceof EquipmentEntity) {
					if (isOpenReMaiMap) {
						Equipment entity = (Equipment) article;
						int level = entity.getPlayerLevelLimit();
						String levelName = null;
						for (int j = 0; j < LEVEL_SECTION.length; j++) {
							if (level <= LEVEL_SECTION[j][1] && level >= LEVEL_SECTION[j][0]) {
								if (LEVEL_SECTION[j][0] > 220) {
									levelName = Translate.仙 + (LEVEL_SECTION[j][0] - 220) + LEVEL_SHOW_CHAR + Translate.仙 + (LEVEL_SECTION[j][1] - 220);
								} else {
									levelName = LEVEL_SECTION[j][0] + LEVEL_SHOW_CHAR + LEVEL_SECTION[j][1];
								}
								break;
							}
						}
						logger.warn("~~~~~~~~~~~~   level=[" + level + "][" + levelName + "]");
						Boolean isReMai = isReMaiMap.get(levelName + articleEntity.getColorType());
						if (isReMai != null) {
							list.add(entityId);
						}
					} else {
						Equipment entity = (Equipment) article;
						int level = entity.getPlayerLevelLimit();
						String levelName = null;
						for (int j = 0; j < LEVEL_SECTION.length; j++) {
							if (level <= LEVEL_SECTION[j][1] && level >= LEVEL_SECTION[j][0]) {
								if (LEVEL_SECTION[j][0] > 220) {
									levelName = Translate.仙 + (LEVEL_SECTION[j][0] - 220) + LEVEL_SHOW_CHAR + Translate.仙 + (LEVEL_SECTION[j][1] - 220);
								} else {
									levelName = LEVEL_SECTION[j][0] + LEVEL_SHOW_CHAR + LEVEL_SECTION[j][1];
								}
								break;
							}
						}
						logger.warn("~~~~~~~~~~~~   level=[" + level + "][" + levelName + "]");
						RequestBuyRule buyRule = getRequestBuyRule(article.get物品一级分类(), "", levelName, articleEntity.getColorType());
						if (buyRule == null) {
							// logger.debug("对象没有分类:"+article.getName());
							if (logger.isInfoEnabled()) logger.info("对象没有分类:[{}] [{}]", new Object[] { article.getName(), articleEntity.getColorType() });
							continue;
						}
						List<RequestBuy> list2 = getUnderRuleMap().get(buyRule);
						if (list2 != null && list2.size() >= REMAI) {
							list.add(entityId);
						}
					}
				} else {
					if (isOpenReMaiMap) {
						Boolean isReMai = isReMaiMap.get(article.getName() + articleEntity.getColorType());
						if (isReMai != null) {
							list.add(entityId);
						}
					} else {
						RequestBuyRule buyRule = getRequestBuyRule(article.get物品一级分类(), article.get物品二级分类(), article.getName(), articleEntity.getColorType());
						if (buyRule != null) {
							List<RequestBuy> list2 = getUnderRuleMap().get(buyRule);
							if (list2 != null && list2.size() >= REMAI) {
								list.add(entityId);
							}
						}
					}
				}
			}
		}
		return list;
	}

	/**
	 * 在角色身上移除求购信息/考虑放到player
	 * @param player
	 * @param itemId
	 * @return
	 */
	public boolean removePlayerRequestBuy(long playerId, RequestBuy requestBuy) {
		if (getPlayerRuleMap().get(playerId) != null) {
			return getPlayerRuleMap().get(playerId).remove(requestBuy);
		} else {
			if (logger.isDebugEnabled()) logger.debug("此角色不存在此求购");
			return false;
		}
	}

	/**
	 * 在列表里移除
	 * @param requestBuy
	 * @return
	 */
	public boolean removeTotalRequestBuy(RequestBuy requestBuy) {
		List<RequestBuy> list = getUnderRuleMap().get(requestBuy.getRule());
		if (list == null || list.size() == 0) {
			return false;
		}
		boolean isFirst = list.get(0).equals(requestBuy);
		getRequestBuyMap().remove(requestBuy.getId());
		if (isFirst) {
			list.remove(0);
			removeToReMai(requestBuy.getRule());
			Collections.sort(list);
			getUnderRuleMap().put(requestBuy.getRule(), list);
			return true;
		}
		boolean isRmove = list.remove(requestBuy);
		removeToReMai(requestBuy.getRule());
		return isRmove;
	}

	/**
	 * 移除一个求购信息
	 * @param player
	 * @param rbId
	 */
	public void removeRequestBuy(long playerId, long rbId) {
		RequestBuy requestBuy = getRequestBuyById(rbId);
		if (requestBuy == null) {
			// logger.error("[求购管理][移除求购异常][记录不存在 rbId=" + rbId + "]");
			if (logger.isWarnEnabled()) logger.warn("[求购管理][移除求购异常][记录不存在 rbId={}]", new Object[] { rbId });
			return;
		}
		boolean succ;
		succ = removePlayerRequestBuy(playerId, requestBuy);
		if (!succ) {
			// logger.error("[求购管理][移除求购异常][从角色身上移除][角色ID：" + player.getId() + "][求购ID:" + requestBuy.getId() + "]");
			if (logger.isWarnEnabled()) logger.warn("[求购管理][移除求购异常][从角色身上移除][角色ID：{}][求购ID:{}]", new Object[] { playerId, requestBuy.getId() });
		}
		succ = removeTotalRequestBuy(requestBuy);
		if (!succ) {
			// logger.error("[求购管理][移除求购异常][在列表中移除][求购ID:" + requestBuy.getId() + "]");
			if (logger.isWarnEnabled()) logger.warn("[求购管理][移除求购异常][在列表中移除][求购ID:{}]", new Object[] { requestBuy.getId() });
		}
		// requestBuy.setBeOver(true);
	}

	public RequestBuy getRequestBuyById(long id) {
		return getRequestBuyMap().get(id);
	}

	public Hashtable<Long, RequestBuy> getRequestBuyMap() {
		return requestBuyMap;
	}

	public void setRequestBuyMap(Hashtable<Long, RequestBuy> requestBuyMap) {
		this.requestBuyMap = requestBuyMap;
	}

	public Hashtable<RequestBuyRule, List<RequestBuy>> getUnderRuleMap() {
		return underRuleMap;
	}

	public void setUnderRuleMap(Hashtable<RequestBuyRule, List<RequestBuy>> underRuleList) {
		this.underRuleMap = underRuleList;
	}

	private void loadAllRequestBuyFromDB() {
		List<RequestBuy> list = new ArrayList<RequestBuy>();
		try {
			long count = em.count();
			long queryIndex = 1;
			while (count > 5000) {
				list.addAll(em.query(RequestBuy.class, "", new Object[] {}, "", queryIndex, queryIndex + 5000));
				count -= 5000;
				queryIndex += 5000;
			}
			if (count > 0) {
				list.addAll(em.query(RequestBuy.class, "", new Object[] {}, "", queryIndex, queryIndex + count));
			}
		} catch (Exception e) {
			logger.error("loadAllRequestBuyFromDB出错,很严重", e);
			return;
		}
		if (logger.isWarnEnabled()) logger.warn("从数据库载入所有拍卖纪录，一共:" + list.size());
		for (RequestBuy requestBuy : list) {
			getRequestBuyMap().put(requestBuy.getId(), requestBuy);

			RequestBuyRule buyRule = requestBuy.getRule();
			// if(buyRule!=null&&requestBuyRule.size()>0){
			int index = requestBuyRule.indexOf(buyRule);
			if (index >= 0) {
				requestBuy.setRule(requestBuyRule.get(index));
				buyRule = requestBuyRule.get(index);
			} else {
				requestBuyRule.add(buyRule);
			}
			// }
			if (!getUnderRuleMap().containsKey(buyRule)) {
				getUnderRuleMap().put(buyRule, new ArrayList<RequestBuy>());
			}
			getUnderRuleMap().get(buyRule).add(requestBuy);
			addToReMai(buyRule);
			Long playerId = requestBuy.getReleaserId();
			if (!getPlayerRuleMap().containsKey(playerId)) {
				getPlayerRuleMap().put(playerId, new ArrayList<RequestBuy>());
			}
			getPlayerRuleMap().get(playerId).add(requestBuy);
		}
		for (Iterator<RequestBuyRule> it = getUnderRuleMap().keySet().iterator(); it.hasNext();) {
			Collections.sort(getUnderRuleMap().get(it.next()));
		}
	}

	public void destroy() {
		try {
			File f = new File(getConfigFile());
			DataOutputStream dos = new DataOutputStream(new FileOutputStream(f));
			dos.writeInt(getBuConfigs().size());
			RequestBuyBuConfig[] buConfigs = getBuConfigs().toArray(new RequestBuyBuConfig[0]);
			for (int i = 0; i < buConfigs.length; i++) {
				buConfigs[i].save(dos);
			}
			dos.close();
		} catch (Exception e) {
			RequestBuyBuConfig.logger.error("保存补求购出现写错误", e);
		}

		List<RequestBuy> deleteList = new ArrayList<RequestBuy>();
		for (Iterator<Long> it = getRequestBuyMap().keySet().iterator(); it.hasNext();) {
			long id = it.next();
			RequestBuy buy = getRequestBuyMap().get(id);
			if (buy.isBeOver()) {
				deleteList.add(buy);
			}
		}
		for (int i = 0; i < deleteList.size(); i++) {
			RequestBuy buy = deleteList.get(i);
			try {
				em.remove(buy);
			} catch (Exception e) {
				logger.error("destroy中删除过期的和取消的:" + buy.getId(), e);
			}
		}
		em.destroy();
	}

	public void init() {
		
		logger.warn("求购初始化开始----------");
		LEVEL_STRINGS = new String[] { "1-20", "21-40", "41-60", "61-80", "81-100", "101-120", "121-140", "141-160", "161-180", "181-200", "201-220", Translate.仙 + "1-" + Translate.仙 + "20", Translate.仙 + "21-" + Translate.仙 + "40", Translate.仙 + "41-" + Translate.仙 + "60", Translate.仙 + "61-" + Translate.仙 + "80" };
		startMoney = 0;
		allMoney = 0;
		requestMoney = 0;
		saleMoney = 0;
		cancleMoney = 0;
		taxMoney = 0;
		timeOverMoney = 0;
		em = SimpleEntityManagerFactory.getSimpleEntityManager(RequestBuy.class);
		TradeItem.setEM(em);
		instance = this;
		tempEntity = new Hashtable<String, ArticleEntity>();
		instance.loadFile();
		instance.loadBuRequestBuyFile();
		loadAllRequestBuyFromDB();
		for (RequestBuy requestBuy : getRequestBuyMap().values()) {
			startMoney += (requestBuy.getItem().getPerPrice() * requestBuy.getItem().getEntityNum());
		}
		allMoney = startMoney;
		logger.warn("当前服务器剩余求购金额:" + startMoney);
		Thread t = new Thread(instance, "RequestBuy");
		t.start();
		logger.warn("求购初始化结束----------");
		ServiceStartRecord.startLog(this);
	}

	@Override
	public void run() {
		/** 要删除的列表 */
		List<RequestBuy> deleteList = new ArrayList<RequestBuy>();
		// 处理超时的求购
		while (running) {
			try {
				Thread.sleep(10000 / sept);
				if (!com.fy.engineserver.util.ContextLoadFinishedListener.isLoadFinished()) {
					continue;
				}
				RequestBuy[] requestBuys = getRequestBuyMap().values().toArray(new RequestBuy[0]);
				for (RequestBuy buy : requestBuys) {
					if (buy.isBeOver()) {
						deleteList.add(buy);
					} else if (buy.isTimeOut()) {// 不是新的且超时的做 ,退钱处理 且 删除
						if (buy.getItem().getEntityNum() > 0 && !buy.isBeOver()) {

							buy.setBeOver(true);
							PlayerSimpleInfo player = PlayerSimpleInfoManager.getInstance().getInfoById(buy.getReleaserId());

							try {
								MailManager.getInstance().sendMail(buy.getReleaserId(), null, null, Translate.text_requestbuy_007, Translate.text_requestbuy_008, buy.getItem().getEntityNum() * buy.getItem().getPerPrice(), 0, 0, "求购到期退钱");
								timeOverMoney += buy.getItem().getEntityNum() * buy.getItem().getPerPrice();
								allMoney -= buy.getItem().getEntityNum() * buy.getItem().getPerPrice();
								deleteList.add(buy);
								if (logger.isWarnEnabled()) logger.warn("[求购run] [退钱={}] [{}]", new Object[] { buy.getItem().getEntityNum() * buy.getItem().getPerPrice(), buy.getLogString() });
							} catch (Exception e) {
								if (logger.isWarnEnabled()) logger.warn("[求购管理器] [run给过期的玩家退钱发邮件出错:] RID=[{}] RName=[{}] PID=[{}] Money=[{}]", new Object[] { buy.getId(), buy.getReleaserName(), buy.getReleaserId(), buy.getItem().getEntityNum() * buy.getItem().getPerPrice() });
							}
							if (ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_角色装备类].equals(buy.getRule().getMainType()) || ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_马匹装备类].equals(buy.getRule().getMainType())) {
								ArticleStatManager.addToArticleStat(null, player.getUsername(), player.getLevel(), null, null, 0, buy.getRule().getMainType(), buy.getRule().getGrade(), COLOR_TYPES[buy.getRule().getColor()], false, ArticleStatManager.OPERATION_交换, 0, ArticleStatManager.YINZI, buy.getItem().getEntityNum(), "求购过期", null);
							} else {
								ArticleStatManager.addToArticleStat(null, player.getUsername(), player.getLevel(), null, null, 0, buy.getRule().getArticleName(), buy.getRule().getGrade(), COLOR_TYPES[buy.getRule().getColor()], false, ArticleStatManager.OPERATION_交换, 0, ArticleStatManager.YINZI, buy.getItem().getEntityNum(), "求购过期", null);
							}
						} else {
							deleteList.add(buy);
						}
					}
				}

				for (int i = 0; i < deleteList.size(); i++) {
					RequestBuy buy = deleteList.get(i);
					removeRequestBuy(buy.getReleaserId(), buy.getId());
					try {
						em.remove(buy);
					} catch (Exception e) {
						if (logger.isInfoEnabled()) logger.info("[求购管理器] [em.remove]", e);
					}
				}
				deleteList.clear();
			} catch (InterruptedException e) {
				if (logger.isWarnEnabled()) logger.warn("[求购管理器] [run 异常]", e);
			} catch (Exception e) {
				if (logger.isWarnEnabled()) logger.warn("[求购管理器] [run 异常]", e);
			}
			if (isOpen) {
				try {
					RequestBuyBuConfig[] removes = getRemoveConfigs().toArray(new RequestBuyBuConfig[0]);
					getRemoveConfigs().clear();
					for (int i = 0; i < removes.length; i++) {
						getBuConfigs().remove(removes[i]);
					}
					RequestBuyBuConfig[] buConfigs = getBuConfigs().toArray(new RequestBuyBuConfig[0]);
					for (int i = 0; i < buConfigs.length; i++) {
						if (buConfigs[i] == null) {
							RequestBuyBuConfig.logger.error("放了个空的？");
							continue;
						}
						buConfigs[i].isRefBuConfig();
						RequestBuyRule rule = buConfigs[i].getRule();
						if (rule == null) {
							continue;
						}
						List<RequestBuy> requestBuys = getUnderRuleMap().get(rule);
						if (requestBuys != null && requestBuys.size() > 0) {
							buConfigs[i].addRequestBuy(requestBuys.get(0));
						}
						buConfigs[i].doBuHuo();
					}
					if (System.currentTimeMillis() - writeTime > 5 * 60 * 1000) {
						try {
							File f = new File(getConfigFile());
							DataOutputStream dos = new DataOutputStream(new FileOutputStream(f));
							dos.writeInt(getBuConfigs().size());
							buConfigs = getBuConfigs().toArray(new RequestBuyBuConfig[0]);
							for (int i = 0; i < buConfigs.length; i++) {
								buConfigs[i].save(dos);
							}
							dos.close();
						} catch (Exception e) {
							RequestBuyBuConfig.logger.error("保存补求购出现写错误", e);
						}
						writeTime = System.currentTimeMillis();
					}
				} catch (Exception e) {
					RequestBuyBuConfig.logger.error("补求购心跳出现异常", e);
				}
			}
		}
	}

	public static RequestBuyManager getInstance() {
		return instance;
	}

	/**
	 * 给角色返还钱
	 * @param player
	 * @param moneyType
	 * @param num
	 */
	public void sendBackMoney2Player(Player player, int moneyType, int num, String reason) {

	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getFile() {
		return file;
	}

	private Hashtable<String, Integer> special_color; // 求购中一些特殊品质颜色的物品
	private int[][] levels; // 快速求购的物品等级范围
	private String[][] levelpropNames; // 快速求购的物品名称

	public void loadFile() {
		try {
			special_color = new Hashtable<String, Integer>();
			File file = new File(getFile());
			if (!file.exists()) {
				// logger.fatal("任务配置文件不存在");
				return;
			}
			InputStream is = new FileInputStream(file);
			POIFSFileSystem pss = new POIFSFileSystem(is);
			HSSFWorkbook workbook = new HSSFWorkbook(pss);

			// 特殊品质颜色的
			HSSFSheet sheet1 = workbook.getSheetAt(0);
			for (int i = 1, n = sheet1.getPhysicalNumberOfRows(); i < n; i++) {
				HSSFRow row = sheet1.getRow(i);
				if (row.getPhysicalNumberOfCells() <= 0) {
					continue;
				}
				special_color.put(row.getCell(0).getStringCellValue(), StringTool.getCellValue(row.getCell(1), Integer.class));
			}
			// 快速求购等级段的
			HSSFSheet sheet2 = workbook.getSheetAt(1);
			levels = new int[sheet2.getPhysicalNumberOfRows()][2];
			levelpropNames = new String[sheet2.getPhysicalNumberOfRows()][];
			for (int i = 1, n = sheet2.getPhysicalNumberOfRows(); i < n; i++) {
				HSSFRow row = sheet2.getRow(i);
				levels[i - 1][0] = StringTool.getCellValue(row.getCell(0), Integer.class);
				levels[i - 1][1] = StringTool.getCellValue(row.getCell(1), Integer.class);
				String[] names = (row.getCell(2).getStringCellValue()).split(",");
				levelpropNames[i - 1] = names;
			}
		} catch (Exception e) {
			if (logger.isWarnEnabled()) logger.warn("loadFile取求购配置文件出错====", e);
		}
	}

	// 读补求购的配置
	public void loadBuRequestBuyFile() {
		try {
			RequestBuyBuConfig.logger.warn("初始化开始");
			File file = new File(getConfigFile());
			if (!file.exists()) {
				// logger.fatal("任务配置文件不存在");
				return;
			}
			FileInputStream fis = new FileInputStream(file);
			DataInputStream dis = new DataInputStream(fis);
			int num = dis.readInt();
			for (int i = 0; i < num; i++) {
				RequestBuyBuConfig config = RequestBuyBuConfig.creat(dis);
				getBuConfigs().add(config);
			}
			writeTime = System.currentTimeMillis();
			RequestBuyBuConfig.logger.warn("初始化RequestBuyBuConfig个数:" + getBuConfigs().size() + "~" + num);
		} catch (Exception e) {
			RequestBuyBuConfig.logger.error("读补求购配置出错", e);
		}
	}

	// public static void main(String[] args) {
	// RequestBuyManager buyManager = new RequestBuyManager();
	// buyManager.setFile("E:/工作相关文档/RequestBuyConfig.xls");
	// buyManager.loadFile();
	// buyManager.msg_getFastBuy();
	// }

	public void setPlayerRuleMap(Hashtable<Long, List<RequestBuy>> playerRuleMap) {
		this.playerRuleMap = playerRuleMap;
	}

	public Hashtable<Long, List<RequestBuy>> getPlayerRuleMap() {
		return playerRuleMap;
	}

	// 拿求购规则，如果没有就新建一个
	public RequestBuyRule getRequestBuyRule(String mainType, String subType, String artName, int color) {
		if (mainType == null) {
			return null;
		}
		if (color >= RequestBuyManager.COLOR_TYPES.length) {
			// 求购只有4种颜色
			return null;
		}
		if (artName == null) {
			return null;
		}
		for (RequestBuyRule buyRule : requestBuyRule) {
			if (buyRule.getMainType().equals(mainType) && buyRule.getArticleName().equals(artName) && buyRule.getColor() == color) {
				if (buyRule.getSubType() == null && subType == null) {
					return buyRule;
				} else if (buyRule.getSubType() != null && subType != null && buyRule.getSubType().equals(subType)) {
					return buyRule;
				}
				break;
			}
		}
		RequestBuyRule requestBuy = new RequestBuyRule();
		requestBuy.setMainType(mainType);
		requestBuy.setSubType(subType);
		requestBuy.setColor(color);
		if (ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_角色装备类].equals(mainType) || ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_马匹装备类].equals(mainType)) {
			if (logger.isDebugEnabled()) logger.debug("求购装备需要设置特殊");
			requestBuy.setSubType("");
			requestBuy.setOutshowName(RequestBuyManager.COLOR_TYPES[color] + mainType);
			requestBuy.setArticleName(artName);
			String[] grades = null;
			boolean isXian = false;
			if (artName.indexOf(Translate.仙) >= 0) {
				isXian = true;
				grades = artName.split(LEVEL_SHOW_CHAR);
				grades[0] = grades[0].substring(Translate.仙.length());
				grades[1] = grades[1].substring(Translate.仙.length());
			} else {
				grades = artName.split(LEVEL_SHOW_CHAR);
			}
			logger.warn("~~~~~~~~~~~~   artName=[" + artName + "][" + grades[0] + "] [" + grades[1] + "]");
			requestBuy.setGrade(Integer.parseInt(grades[0]) + (isXian ? 220 : 0));
			requestBuy.setMaxGrade(Integer.parseInt(grades[1]) + (isXian ? 220 : 0));
		} else {
			Article article = ArticleManager.getInstance().getArticle(artName);
			requestBuy.setOutshowName(artName);
			requestBuy.setArticleName(artName);
			if (article instanceof Props) {
				if (article instanceof PetEggProps) {
					requestBuy.setGrade(((PetEggProps) article).getArticleLevel());
				}
				requestBuy.setGrade(((Props) article).getLevelLimit());
			} else {
				requestBuy.setGrade(1);
			}
		}
		requestBuyRule.add(requestBuy);
		return requestBuy;
	}

	public static String timeDistanceLong2String(long time) {
		int hour = (int) time / 1000 / 60 / 60;
		if (hour > 0) {
			return hour + Translate.text_小时;
		}
		return Translate.text_不足1小时;
	}

	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}

	public String getConfigFile() {
		return configFile;
	}

	public void setTempConfig(RequestBuyBuConfig tempConfig) {
		this.tempConfig = tempConfig;
	}

	public RequestBuyBuConfig getTempConfig() {
		return tempConfig;
	}

	public void setBuConfigs(ArrayList<RequestBuyBuConfig> buConfigs) {
		this.buConfigs = buConfigs;
	}

	public ArrayList<RequestBuyBuConfig> getBuConfigs() {
		return buConfigs;
	}

	private void chanagerSaleTime(RequestBuy buy) {
		if (isOpen) {
			for (int i = 0; i < getBuConfigs().size(); i++) {
				if (getBuConfigs().get(i).getRequestID() == buy.getId()) {
					getBuConfigs().get(i).setLastSaleTime(System.currentTimeMillis());
					break;
				}
			}
		}
	}

	public void setWriteTime(long writeTime) {
		this.writeTime = writeTime;
	}

	public long getWriteTime() {
		return writeTime;
	}

	public void setRemoveConfigs(ArrayList<RequestBuyBuConfig> removeConfigs) {
		this.removeConfigs = removeConfigs;
	}

	public ArrayList<RequestBuyBuConfig> getRemoveConfigs() {
		return removeConfigs;
	}

	private void addToReMai(RequestBuyRule buyRule) {
		if (isOpenReMaiMap) {
			try {
				if (getUnderRuleMap().get(buyRule) != null && getUnderRuleMap().get(buyRule).size() >= REMAI) {
					String key = "";
					if (ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_角色装备类].equals(buyRule.getMainType()) || ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_马匹装备类].equals(buyRule.getMainType())) {
						key = buyRule.getArticleName() + buyRule.getColor();
					} else {
						key = buyRule.getArticleName() + buyRule.getColor();
					}
					isReMaiMap.put(key, true);
					logger.warn("[进入热卖:] [{}] [{}] [{}] [{}]", new Object[] { key, buyRule.getMainType(), buyRule.getArticleName(), buyRule.getColor() });
				}
			} catch (Exception e) {
				logger.error("addToReMai出错", e);
			}
		}
	}

	private void removeToReMai(RequestBuyRule buyRule) {
		if (isOpenReMaiMap) {
			try {
				if (getUnderRuleMap().get(buyRule) == null || getUnderRuleMap().get(buyRule).size() < REMAI) {
					String key = "";
					if (ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_角色装备类].equals(buyRule.getMainType()) || ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_马匹装备类].equals(buyRule.getMainType())) {
						key = buyRule.getArticleName() + buyRule.getColor();
					} else {
						key = buyRule.getArticleName() + buyRule.getColor();
					}
					isReMaiMap.remove(key);
					logger.warn("[移除热卖:] [{}] [{}] [{}] [{}]", new Object[] { key, buyRule.getMainType(), buyRule.getArticleName(), buyRule.getColor() });
				}
			} catch (Exception e) {
				logger.error("removeToReMai出错", e);
			}
		}
	}
}
