package com.fy.engineserver.seal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.fy.engineserver.activity.loginActivity.ActivityManagers;
import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameInfo;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.props.Cell;
import com.fy.engineserver.datasource.article.data.props.Knapsack;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.buff.BuffTemplate;
import com.fy.engineserver.datasource.buff.BuffTemplateManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.message.ARTICLE_REMOVE_SUCC_RES;
import com.fy.engineserver.message.COLLECT_MATERIAL_FOR_BOSS_RES;
import com.fy.engineserver.message.CONFIRM_COLLENT_MATERIAL_FOR_BOSS_REQ;
import com.fy.engineserver.message.CONFIRM_COLLENT_MATERIAL_FOR_BOSS_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.SEAL_TASK_INFO_REQ;
import com.fy.engineserver.message.SEAL_TASK_INFO_RES;
import com.fy.engineserver.message.SEAL_TASK_STAT_RES;
import com.fy.engineserver.newtask.service.TaskConfig.ModifyType;
import com.fy.engineserver.seal.data.Seal;
import com.fy.engineserver.seal.data.SealTaskInfo;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.Sprite;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.sprite.monster.BossMonster;
import com.fy.engineserver.sprite.monster.MemoryMonsterManager;
import com.fy.engineserver.sprite.monster.Monster;
import com.fy.engineserver.sprite.monster.SimpleMonster;
import com.fy.engineserver.stat.ArticleStatManager;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.TimeTool;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;
import com.xuanzhi.tools.text.FileUtils;
import com.xuanzhi.tools.text.XmlUtil;

/**
 * 封印系统管理
 * 
 * 
 */
public class SealManager {

	private static SealManager self;
	public static final String 封印物品1 = Translate.封印物品1;
	public static final String 封印物品2 = Translate.封印物品2;
	public static final String 封印物品3 = Translate.封印物品3;

	public static boolean testFalg = false;
	/** 特殊需求，第一个飞升开始计算，260封印一年。(读取xml改变) */
	public static boolean hasUpdateSeal = false;

	public static int takeSealTaskLevel[] = { 70, 110, 150, 190, 220, 260, 300, 1000 };
	public static int 开启下一级破封任务间隔[] = { 10, 10, 30, 50, 50, 50, 50, 50 };
	public static long 封印等级之间的间隔_测试 = 30 * 60 * 1000;
	/**
	 * 封印等级数组
	 */
	public static final int[] SEAL_LEVELS = new int[] { 70, 110, 150, 190, 220, 260, 300, 1000 };

	/**
	 * 解开封印的天数
	 */
	public static int[] SEAL_OPEN_DAYS = new int[] { 3, 25, 50, 60, 180, 180, 180, 1000 };
	
	public static long 一天毫秒数 = 24 * 60 * 60 * 1000;
	public static int bossIds[] = {};
	public static String MAP_NAMES[] = { "fengyintiaozhan", "fengyintiaozhan", "fengyintiaozhan", "fengyintiaozhan", "fengyintiaozhan" };
	public static int BORN_POINTS[][] = { { 550, 664 }, { 550, 664 }, { 550, 664 }, { 550, 664 }, { 550, 664 } };
	public List<SealCityBossInfo> infos = new ArrayList<SealCityBossInfo>();
	public List<SealTaskInfo> tasks = new ArrayList<SealTaskInfo>();
	public static int MAX_THREAD_NUM = 3;
	public static int NEXT_HANDLE_THREAD_INDEX = 0;
	public static int 野外boss_bossId = 20113259;
	public static String 野外boss_地图 = "fenglingdu";
	public static String 野外boss_中文地图 = "风陵渡";
	public static int 野外boss_x = 999;
	public static int 野外boss_y = 722;
	public static long SEAL_STEP_COLSE_LENGTH_190 = 7 * 24 * 60 * 60 * 1000;
	public static long SEAL_STEP_COLSE_LENGTH_220 = 10 * 24 * 60 * 60 * 1000;
	public static long BOSS_REFRESH_TIME_220 = 7 * 24 * 60 * 60 * 1000;
	public long closeTimeLength = 24 * 60 * 60 * 1000; // 关闭破封间隔
	public String KEY_BOSS_DEAD = "KEY_BOSS_DEAD";
	public String KEY_BOSS_DEAD_NUM = "KEY_BOSS_DEAD";
	public int MAX_BOSS_DEAD_NUM = 5;
	public static int ids[] = { 20113263, 20113262, 20113261, 20113260, 20113259, 20113267 };

	public static long BOSS_REFRESH_LENGTH = 7 * 24 * 3600 * 1000;

	public final Random random = new Random();

	public SealThread[] threads = new SealThread[MAX_THREAD_NUM];

	public SimpleEntityManager<SealTaskInfo> em_info;
	
	//正在参加副本的人
	public static Map<Long, Long> playersOfDownCitys = new Hashtable<Long, Long>(); 

	public static boolean isstart;
	public static boolean sealFlag;
	
	public static SealManager getInstance() {
		return self;
	}

	public static class BossDeadInfo implements Serializable {
		public int deadNums;
		public long firstDeadTime;

		public int getDeadNums() {
			return this.deadNums;
		}

		public void setDeadNums(int deadNums) {
			this.deadNums = deadNums;
		}

		public long getFirstDeadTime() {
			return this.firstDeadTime;
		}

		public void setFirstDeadTime(long firstDeadTime) {
			this.firstDeadTime = firstDeadTime;
		}
	}
	
	public boolean isRightBoss(int bossId){
		for(int i=0;i<ids.length;i++){
			if(ids[i]==bossId){
				return true;
			}
		}
		return false;
	}

	public void initBossInfo() {
		SealCityBossInfo info01 = new SealCityBossInfo(20113254, new int[] { 466, 304 }, 110, 1, 6 * 24 * 60 * 60 * 1000L);
		SealCityBossInfo info02 = new SealCityBossInfo(20113255, new int[] { 466, 304 }, 110, 2, 9 * 24 * 60 * 60 * 1000L);
		SealCityBossInfo info03 = new SealCityBossInfo(20113256, new int[] { 466, 304 }, 110, 3, 15 * 24 * 60 * 60 * 1000L);

		SealCityBossInfo info11 = new SealCityBossInfo(20113254, new int[] { 466, 304 }, 150, 1, 6 * 24 * 60 * 60 * 1000L);
		SealCityBossInfo info12 = new SealCityBossInfo(20113255, new int[] { 466, 304 }, 150, 2, 9 * 24 * 60 * 60 * 1000L);
		SealCityBossInfo info13 = new SealCityBossInfo(20113256, new int[] { 466, 304 }, 150, 3, 15 * 24 * 60 * 60 * 1000L);
		info11.buffid = 1767;
		info12.buffid = 1767;
		info13.buffid = 1767;
		info11.title = Translate.封印_TITLE1_150;
		info12.title = Translate.封印_TITLE2_150;
		info13.title = Translate.封印_TITLE3_150;
		info11.content = Translate.封印_CONTENT1_150;
		info12.content = Translate.封印_CONTENT2_150;
		info13.content = Translate.封印_CONTENT3_150;

		SealCityBossInfo info21 = new SealCityBossInfo(20113257, new int[] { 466, 304 }, 190, 1, 15 * 24 * 60 * 60 * 1000L);
		info21.needMaterialName = Translate.玄天灵片;
		info21.resultMess = Translate.捐献材料描述_190;
		info21.allPoints = 100000;
		info21.buffid = 1767;
		SealCityBossInfo info22 = new SealCityBossInfo(20113257, new int[] { 466, 304 }, 190, 2, 25 * 24 * 60 * 60 * 1000L);
		SealCityBossInfo info23 = new SealCityBossInfo(20113258, new int[] { 466, 304 }, 190, 3, 40 * 24 * 60 * 60 * 1000L);
		info23.needMaterialName = Translate.冰晶;
		info23.resultMess = Translate.捐献材料描述_190_2;
		info23.allPoints = 600000;
		info23.buffid = 1767;
		info22.buffid = 1767;
		info21.title = Translate.封印_TITLE1_190;
		info22.title = Translate.封印_TITLE2_190;
		info23.title = Translate.封印_TITLE3_190;
		info21.content = Translate.封印_CONTENT1_190;
		info22.content = Translate.封印_CONTENT2_190;
		info23.content = Translate.封印_CONTENT3_190;
		info21.articleInfo = new SealRewardArticleInfo("寻迹宝录", 1, true);
		info22.articleInfo = new SealRewardArticleInfo("寻迹宝录", 1, true);
		info23.articleInfo = new SealRewardArticleInfo("寻迹宝录", 1, true);

		SealCityBossInfo info31 = new SealCityBossInfo(20113259, new int[] { 466, 304 }, 220, 1, 30 * 24 * 60 * 60 * 1000L);
		info31.needMaterialName = Translate.降灵符;
		info31.resultMess = Translate.捐献材料描述_220;
		info31.allPoints = 180000;
		info31.buffid = 1767;
		info31.bossids = new int[] { 20113263, 20113262, 20113261, 20113260, 20113259, 20113267 };
		SealCityBossInfo info32 = new SealCityBossInfo(20113259, new int[] { 466, 304 }, 220, 2, 50 * 24 * 60 * 60 * 1000L);
		info31.title = Translate.封印_TITLE1_220;
		info32.title = Translate.封印_TITLE2_220;
		info31.content = Translate.封印_CONTENT1_220;
		info32.content = Translate.封印_CONTENT2_220;
		info31.articleInfo = new SealRewardArticleInfo("寻迹宝录", 1, true);
		info31.resultMess = Translate.捐献材料描述_220;
		info32.resultMess = Translate.捐献材料描述_220_2;
		info32.allPoints = 180000;
		infos.add(info11);
		infos.add(info12);
		infos.add(info13);
		infos.add(info21);
		infos.add(info22);
		infos.add(info23);
		infos.add(info31);
		infos.add(info32);
		infos.add(info01);
		infos.add(info02);
		infos.add(info03);
	}

	public void init() throws Exception {
		
		long now = System.currentTimeMillis();
		self = this;
		em_info = SimpleEntityManagerFactory.getSimpleEntityManager(SealTaskInfo.class);
		File f = new File(file);
		if (f != null && f.isFile() && f.length() > 0) {
			read(f);
			//System.out.println("[封印管理器] [读取已有文件] [成功] [" + file + "] [耗时:" + (System.currentTimeMillis() - now) + "ms]");
		} else {
			Seal seal = new Seal();
			seal.setSealLevel(SEAL_LEVELS[0]);
			seal.setSealCanOpenTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + SEAL_OPEN_DAYS[0] * 1l * 24 * 3600 * 1000,"init");
			if(GameConstants.getInstance().getServerName().equals("仙尊降世")){
				seal.setSealCanOpenTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + 封印等级之间的间隔_测试,"initTest");
			}
			seal.setFirstPlayerIdInCountry(new long[3]);
			this.seal = seal;
			sealMap.put(seal.sealLevel, seal);
			saveSeal();
			System.out.println("[封印管理器] [创建新文件] [成功] [" + file + "] [耗时:" + (System.currentTimeMillis() - now) + "ms]");
		}

		for (int i = 0; i < MAX_THREAD_NUM; i++) {
			threads[i] = new SealThread();
			threads[i].start("封印副本线程--" + i);
		}
		initBossInfo();
		ServiceStartRecord.startLog(this);
	}

	private SealManager() {

	}

	String file;
	public Seal seal;

	public static long SEAL_TASK_ID = 100000;
	public static int 发布一次封印任务的基本费用 = 50000;

	public long getSealTaskId(int country){
		if(country == 1){
			return 100000L;
		}else if(country == 2){
			return 66666L;
		}else if(country == 3){
			return 66667L;
		}
		return SEAL_TASK_ID;
	}
	
	public static int 得到发布封印任务的费用(int max, int leftCount) {
		int cost = ((max - leftCount) / 2 + 1) * SealManager.发布一次封印任务的基本费用;
		if (cost <= 0) {
			cost = SealManager.发布一次封印任务的基本费用;
		}
		return cost;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public Seal getSeal() {
		return seal;
	}

	public void setSeal(Seal seal) {
		this.seal = seal;
	}

	public int getSealLevel() {
		if (seal != null) {
			return seal.sealLevel;
		}
		return 0;
	}

	public Hashtable<Integer, Seal> sealMap = new Hashtable<Integer, Seal>();

	public static final int SEAL_MAX_LEVEL = 300;

	/**
	 * 国王开启封印
	 */
	public synchronized void openSeal(Player player) {
		CountryManager cm = CountryManager.getInstance();
		if (cm != null) {
			if (cm.官职(player.getCountry(), player.getId()) != CountryManager.国王) {
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.只有国王才能解开封印);
				player.addMessageToRightBag(hreq);
				return;
			}
			if (seal != null) {
				if (seal.sealLevel > SEAL_MAX_LEVEL) {
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.已经解开最大封印);
					player.addMessageToRightBag(hreq);
					return;
				}
				if (seal.sealCanOpenTime > com.fy.engineserver.gametime.SystemTime.currentTimeMillis()) {
					Calendar calendar = Calendar.getInstance();
					calendar.setTimeInMillis(seal.sealCanOpenTime);
					StringBuffer sb = new StringBuffer();
					sb.append(calendar.get(Calendar.YEAR) + Translate.年);
					sb.append((calendar.get(Calendar.MONTH) + 1) + Translate.月);
					sb.append(calendar.get(Calendar.DAY_OF_MONTH) + Translate.日);
					sb.append(calendar.get(Calendar.HOUR_OF_DAY) + Translate.时);
					sb.append(calendar.get(Calendar.MINUTE) + Translate.分);
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.translateString(Translate.解开封印最早日期, new String[][] { { Translate.STRING_1, sb.toString() } }));
					player.addMessageToRightBag(hreq);
					return;
				}

				if(sealFlag){
					if (SealManager.getInstance().openSeal() == false) {
						Calendar calendar = Calendar.getInstance();
						calendar.setTimeInMillis(seal.sealCanOpenTime);
						StringBuffer sb = new StringBuffer();
						sb.append(calendar.get(Calendar.YEAR) + Translate.年);
						sb.append((calendar.get(Calendar.MONTH) + 1) + Translate.月);
						sb.append(calendar.get(Calendar.DAY_OF_MONTH) + Translate.日);
						sb.append(calendar.get(Calendar.HOUR_OF_DAY) + Translate.时);
						sb.append(calendar.get(Calendar.MINUTE) + Translate.分);
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.translateString(Translate.解开封印最早日期, new String[][] { { Translate.STRING_1, sb.toString() } }));
						player.addMessageToRightBag(hreq);
						return;
					}
				}

				if (seal.firstPlayerIdInCountry[player.getCountry() - 1] <= 0) {
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.您的国家现在还没有玩家满足解开封印的等级限制);
					player.addMessageToRightBag(hreq);
					return;
				}
				for (int i = 0; i < SEAL_LEVELS.length; i++) {
					int level = SEAL_LEVELS[i];

					if (level == 220) {
						SealTaskInfo taskInfo = SealManager.getInstance().getTakInfo(220, 0, 0);

						if (taskInfo.getSealTaskStartTime() == 0) {
							taskInfo.setSealTaskStartTime(System.currentTimeMillis());
							try {
								SealManager.getInstance().saveInfo(taskInfo);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}

					if (level > seal.sealLevel) {

						String playerName = Translate.无名氏;
						try {
							playerName = PlayerManager.getInstance().getPlayer(seal.firstPlayerIdInCountry[player.getCountry() - 1]).getName();
						} catch (Exception ex) {

						}
						long lastCanOpenTime = seal.getSealCanOpenTime();
						Seal s = new Seal();
						s.setSealLevel(SEAL_LEVELS[i]);
						s.setSealCanOpenTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + SEAL_OPEN_DAYS[i] * 1l * 24 * 3600 * 1000,"openSeal");
						if(GameConstants.getInstance().getServerName().equals("仙尊降世")){
							s.setSealCanOpenTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + 封印等级之间的间隔_测试,"openTest");
						}
						s.setFirstPlayerIdInCountry(new long[3]);
						sealMap.put(s.sealLevel, s);
						try {
							if (saveSeal()) {
								this.seal = s;

								String description = Translate.translateString(Translate.开启封印广播, new String[][] { { Translate.PLAYER_NAME_1, playerName }, { Translate.STRING_1, CountryManager.得到国家名(player.getCountry()) }, { Translate.STRING_2, CountryManager.得到官职名(cm.官职(player.getCountry(), player.getId())) }, { Translate.PLAYER_NAME_2, player.getName() } });
								try {
									ChatMessageService cms = ChatMessageService.getInstance();
									ChatMessage msg = new ChatMessage();
									msg.setMessageText("<f color='0x14ff00'>" + description + "</f>");
									cms.sendRoolMessageToSystem(msg);
									cms.sendRoolMessageToSystem(msg);
									cms.sendRoolMessageToSystem(msg);
								} catch (Exception ex) {

								}
								Game.logger.warn("[国王开启封印] [成功] [封印等级:{}] [封印阶段:{}] [上一封印阶段解封时间:{}] [解封时间:{}] [开启玩家:{}]",new Object[]{seal.getSealLevel(),seal.getSealStep(),TimeTool.formatter.varChar23.format(lastCanOpenTime),TimeTool.formatter.varChar23.format(seal.getSealCanOpenTime()),player.getLogString()});
								return;
							}
						} catch (Exception ex) {
							ex.printStackTrace();
							Game.logger.error("[开启封印] [异常]", ex);
						}
					}
				}
			}
		}
	}
	
	public boolean saveSeal() throws Exception {
		return saveSeal(false);
	}

	public boolean saveSeal(boolean saveNewFlag) throws Exception {
		FileOutputStream pos = null;
		boolean success = false;
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("<?xml version='1.0' encoding='utf-8'?>");
			sb.append("<sealmanager>");
			for (Integer key : sealMap.keySet()) {
				Seal s = sealMap.get(key);
				if (s != null) {
					sb.append("<seal sealLevel='" + s.getSealLevel() + "' sealStep='" + s.getSealStep() + "'  sealCanOpenTime='" + s.sealCanOpenTime + "' playerId1='" + s.firstPlayerIdInCountry[0] + "' playerId2='" + s.firstPlayerIdInCountry[1] + "' playerId3='" + s.firstPlayerIdInCountry[2] + "' />");
				}
			}
			if (saveNewFlag) {
				sb.append("<newSale hassave='true'/>");
			}
			sb.append("</sealmanager>");
			File newFile = new File(file);
			if (!newFile.exists()) {
				FileUtils.chkFolder(file);
			}
			pos = new FileOutputStream(newFile);
			pos.write(sb.toString().getBytes("UTF-8"));
			success = true;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		} finally {
			if (pos != null) {
				try {
					pos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return success;
	}

	public SealTaskInfo getTakInfo(int sealLevel, int sealStep, int sealType) {
		long now = System.currentTimeMillis();
		for (SealTaskInfo info : tasks) {
			if (info.getSealLevel() == sealLevel && info.getSealStep() == sealStep && info.getSealType() == sealType) {
				return info;
			}
		}

		List<SealTaskInfo> list = new ArrayList<SealTaskInfo>();
		try {
			list = em_info.query(SealTaskInfo.class, " sealLevel = " + sealLevel + " AND sealStep = " + sealStep + " AND sealType = " + sealType, "sealStep desc", 1, 10);
			if (list != null && list.size() > 0) {
				tasks.add(list.get(0));
				if (Game.logger.isWarnEnabled()) {
					Game.logger.info("[通过封印等级和阶段查询封印信息] [成功] [封印等级:{}] [封印阶段:{}] [类型：{}] [数量:{}] [耗时:{}ms]", new Object[] { sealLevel, sealStep, sealType, (list == null ? "0" : list.size()), (System.currentTimeMillis() - now) });
				}
				return list.get(0);
			} else {
				SealTaskInfo taskinfo = new SealTaskInfo();
				taskinfo.setSealLevel(sealLevel);
				taskinfo.setSealStep(sealStep);
				taskinfo.setBuffLevel(0);
				taskinfo.setSealType(sealType);
				tasks.add(taskinfo);
				if (Game.logger.isWarnEnabled()) {
					Game.logger.info("[通过封印等级和阶段查询封印信息] [创建新信息] [封印等级:{}] [封印阶段:{}] [类型：{}] [数量:{}] [耗时:{}ms]", new Object[] { sealLevel, sealStep, sealType, (tasks == null ? "0" : tasks.size()), (System.currentTimeMillis() - now) });
				}
				return taskinfo;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Game.logger.info("[通过封印等级和阶段查询封印信息] [异常] [封印等级:{}] [封印阶段:{}] [类型：{}] [数量:{}] [异常:{}]", new Object[] { sealLevel, sealStep, sealType, (tasks == null ? "0" : tasks.size()), e });
		}

		return null;
	}

	/**
	 * 需要通知内存中的玩家封印状态变化，且刷新封印任务
	 */
	public void 开启封印后的操作() {
		try {
			PlayerManager pm = PlayerManager.getInstance();
			Player[] ps = pm.getOnlinePlayers();
			for (Player player : ps) {
				if (player != null) {
					player.checkFunctionNPCModify(ModifyType.GRADE_UP);
				}
			}
			if (pm instanceof GamePlayerManager) {
				Hashtable<String, Player> namePlayerMap = ((GamePlayerManager) pm).namePlayerMap;
				for (Player player : namePlayerMap.values()) {
					if (player != null) {
						player.flushSealState();
					}
				}
			}
		} catch (Exception ex) {

		}
	}

	public void read(File file) throws Exception {
		FileInputStream fis = null;
		boolean needSave = false;
		try {
			fis = new FileInputStream(file);
			Document dom = XmlUtil.load(fis, "utf-8");
			Element root = dom.getDocumentElement();
			Element sealEles[] = XmlUtil.getChildrenByName(root, "seal");
			Hashtable<Integer, Seal> sealMap = new Hashtable<Integer, Seal>();
			int maxLevel = 0;
			for (int i = 0; i < sealEles.length; i++) {
				Element e = sealEles[i];
				if (e != null) {
					Seal seal = new Seal();
					int sealLevel = XmlUtil.getAttributeAsInteger(e, "sealLevel", 70);
					int sealStep = XmlUtil.getAttributeAsInteger(e, "sealStep", 0);
					long sealCanOpenTime = XmlUtil.getAttributeAsLong(e, "sealCanOpenTime", 0);
					long playerId1 = XmlUtil.getAttributeAsLong(e, "playerId1", 0);
					long playerId2 = XmlUtil.getAttributeAsLong(e, "playerId2", 0);
					long playerId3 = XmlUtil.getAttributeAsLong(e, "playerId3", 0);
					seal.setSealLevel(sealLevel);
					seal.setSealStep(sealStep);
					seal.setSealCanOpenTime(sealCanOpenTime,"read");
					seal.setFirstPlayerIdInCountry(new long[] { playerId1, playerId2, playerId3 });
					sealMap.put(seal.sealLevel, seal);
					if (maxLevel < seal.sealLevel) {
						maxLevel = seal.sealLevel;
					}
				}
			}
			Element hasChange[] = XmlUtil.getChildrenByName(root, "newSale");
			if (hasChange == null || hasChange.length <= 0) {
				hasUpdateSeal = false;
//				needSave = true;
//				if (maxLevel == 260) {
//					sealMap.get(maxLevel).setSealCanOpenTime(System.currentTimeMillis() + 365 * 24 * 60 * 60 * 1000L);
//				}
			} else {
				hasUpdateSeal = true;
			}
			
			this.sealMap = sealMap;
			this.seal = sealMap.get(maxLevel);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
//		if (needSave) {
//			saveSeal();
//		}
	}

	/**
	 * 获取可以开启下一级别破封任务的间隔
	 * @param level
	 * @return
	 */
	public int getNextSealDays(int level) {
		int index = takeSealTaskLevel[takeSealTaskLevel.length - 1];
		for (int i = 0; i < takeSealTaskLevel.length; i++) {
			if (takeSealTaskLevel[i] == level) {
				index = i;
				break;
			}
		}
		return 开启下一级破封任务间隔[index];
	}

	public int getIndex(int level) {
		int index = 0;
		for (int i = 0; i < takeSealTaskLevel.length; i++) {
			if (takeSealTaskLevel[i] == level) {
				index = i;
				break;
			}
		}
		return index;
	}

	public boolean isSealDownCity(String mapname) {
		for (String str : MAP_NAMES) {
			if (str.equals(mapname)) {
				return true;
			}
		}
		return false;
	}

	public void addSealTask(SealDownCity city) {
		if (NEXT_HANDLE_THREAD_INDEX >= MAX_THREAD_NUM) {
			NEXT_HANDLE_THREAD_INDEX = 0;
		}
		threads[NEXT_HANDLE_THREAD_INDEX].addTask(city);
		NEXT_HANDLE_THREAD_INDEX++;
		if (Game.logger.isWarnEnabled()) {
			Game.logger.warn("[封印副本] [线程队列] [线程--" + NEXT_HANDLE_THREAD_INDEX + "] [任务数量:" + threads[NEXT_HANDLE_THREAD_INDEX - 1].tasks.size() + "]");
		}
	}

	public void refreshMonster(Game game, Player p) {
		try {
			SealCityBossInfo bossinfo = null;
			for (SealCityBossInfo info : infos) {
				if (info.sealLevel == SealManager.getInstance().getSealLevel() && info.sealLayer == p.optionSealStep) {
					bossinfo = info;
				}
			}
			if (bossinfo != null) {
				Sprite sprite = MemoryMonsterManager.getMonsterManager().createMonster(bossinfo.bossId);
				if (sprite != null && sprite instanceof Monster) {
					Buff buff = null;
					sprite.setX(bossinfo.xyPoints[0]);
					sprite.setY(bossinfo.xyPoints[1]);
					game.addSprite(sprite);

					GameInfo gi = game.getGameInfo();
					if (Game.logger.isWarnEnabled()) {
						Game.logger.warn("[查看破封任务] [刷怪物] [成功] [immunity:{}] [buffs:{}] [optionSealStep:{}] [x:{}] [y:{}] [giname:{}] [sprite:{}] [buff:{}] [info:{}]", new Object[] { sprite.isImmunity(), sprite.getBuffs(), p.optionSealStep, sprite.getX(), sprite.getY(), (gi == null ? "null" : gi.name), sprite.getName(), (buff == null ? "null" : buff.getTemplateName() + "--" + (buff == null ? "0" : buff.getLevel())), (bossinfo != null ? bossinfo.toString() : "null") });
					}
				} else {
					if (Game.logger.isWarnEnabled()) {
						Game.logger.warn("[查看破封任务] [刷怪物] [出错:获取精灵出错] [optionSealStep:" + p.optionSealStep + "] [info:{}]", new Object[] { (bossinfo != null ? bossinfo.toString() : "null") });
					}
				}
			} else {
				if (Game.logger.isWarnEnabled()) {
					Game.logger.warn("[查看破封任务] [刷怪物] [出错:boss信息配置错误] [optionSealStep:" + p.optionSealStep + "] [seallevel:{}] [sealStep:{}]", new Object[] { SealManager.getInstance().getSealLevel(), SealManager.getInstance().getSeal().getSealStep() });
				}
			}
		} catch (Exception e) {
			Game.logger.warn("[查看破封任务] [刷怪物] [异常] ", e);
		}
	}

	public void saveInfo(SealTaskInfo info) {
		try {
			long now = System.currentTimeMillis();
			long id = em_info.nextId();
			if (info.getId() <= 0) {
				info.setId(id);
			}
			em_info.save(info);
			if (Game.logger.isWarnEnabled()) {
				Game.logger.info("[保存封印信息] [成功] [信息:{}] [耗时:{}ms]", new Object[] { info, (System.currentTimeMillis() - now) });
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void handle_SEAL_TASK_INFO_REQ(SEAL_TASK_INFO_REQ req, Player p) {
		int sealLevel = SealManager.getInstance().getSealLevel();
		int sealStep = SealManager.getInstance().getSeal().getSealStep();
		String title = "";
		String content = "";
		if (sealStep >= 2) {
			sealStep = 2;
		}
		for (SealCityBossInfo info : infos) {
			if (info.sealLevel == sealLevel && info.sealLayer == sealStep + 1) {
				title = info.title;
				content = info.content;
			}
		}
		int x = 1917;
		int y = 1420;
		String mapname = "miemoshenyu";
		String npcname = Translate.盘古;
		if (sealLevel == 220 && sealStep == 1) {
			x = 野外boss_x;
			y = 野外boss_y;
			mapname = 野外boss_地图;
			npcname = "";
		}
		SEAL_TASK_INFO_RES res = new SEAL_TASK_INFO_RES(req.getSequnceNum(), title, content, x, y, npcname, mapname);
		p.addMessageToRightBag(res);
		if (Game.logger.isInfoEnabled()) {
			Game.logger.info("[获取破封印任务提示信息] [成功] [sealLevel:{}] [sealStep:{}] [npcname:{}] [mapname:{}]", new Object[] { sealLevel, sealStep, npcname, mapname });
		}
	}

	public void handle_CONFIRM_COLLENT_MATERIAL_FOR_BOSS_REQ(CONFIRM_COLLENT_MATERIAL_FOR_BOSS_REQ req, Player player) {
		long materialIds[] = req.getMaterialIds();
		int materialNums[] = req.getMaterialNums();
		int bossType = req.getBossType(); // 1:A boss 2:B boss

		int sealLevel = SealManager.getInstance().getSealLevel();
		int sealStep = SealManager.getInstance().getSeal().getSealStep();
		int allMaterialNum = 0;

		for (int num : materialNums) {
			allMaterialNum += num;
		}

		if (materialIds == null || materialIds.length == 0 || materialNums == null || materialNums.length == 0 || allMaterialNum == 0) {
			player.sendError(Translate.请放入破封材料);
			player.addMessageToRightBag(new CONFIRM_COLLENT_MATERIAL_FOR_BOSS_RES(req.getSequnceNum(), 0));
			return;
		}

		Knapsack k = player.getKnapsack_common();
		if (k == null) {
			player.sendError(Translate.破封材料不存在);
			player.addMessageToRightBag(new CONFIRM_COLLENT_MATERIAL_FOR_BOSS_RES(req.getSequnceNum(), 0));
			return;
		}

		SealCityBossInfo binfo = null;
		List<SealCityBossInfo> infos = SealManager.getInstance().infos;
		for (SealCityBossInfo info : infos) {
			if (info.sealLevel == sealLevel && info.sealLayer == sealStep + 1) {
				binfo = info;
				break;
			}
		}

		if (binfo == null) {
			player.sendError(Translate.破封任务配置错误);
			player.addMessageToRightBag(new CONFIRM_COLLENT_MATERIAL_FOR_BOSS_RES(req.getSequnceNum(), 0));
			return;
		}

		for (long id : materialIds) {
			boolean hasinknapsack = false;
			ArticleEntity ae = DefaultArticleEntityManager.getInstance().getEntity(id);
			if (ae == null) {
				player.sendError(Translate.破封材料不存在 + "!");
				player.addMessageToRightBag(new CONFIRM_COLLENT_MATERIAL_FOR_BOSS_RES(req.getSequnceNum(), 0));
				return;
			}
			if (ae.getArticleName().equals(binfo.needMaterialName) == false) {
				player.sendError(Translate.请放入破封材料);
				return;
			}
			for (Cell c : k.getCells()) {
				if (c != null && c.entityId > 0) {
					if (c.entityId == id) {
						hasinknapsack = true;
					}
				}
			}
			if (hasinknapsack == false) {
				player.sendError(Translate.破封材料不存在 + ".");
				player.addMessageToRightBag(new CONFIRM_COLLENT_MATERIAL_FOR_BOSS_RES(req.getSequnceNum(), 0));
				return;
			}
		}

		if (sealLevel == 190 && sealStep == 1) {
			player.sendError(Translate.该破封阶段没有捐献);
			player.addMessageToRightBag(new CONFIRM_COLLENT_MATERIAL_FOR_BOSS_RES(req.getSequnceNum(), 0));
			return;
		}

		if (sealLevel == 220 && sealStep != 0) {
			player.sendError(Translate.该破封阶段没有捐献);
			player.addMessageToRightBag(new CONFIRM_COLLENT_MATERIAL_FOR_BOSS_RES(req.getSequnceNum(), 0));
			return;
		}

		SealTaskInfo taskInfo = SealManager.getInstance().getTakInfo(sealLevel, sealStep, bossType);
		if (taskInfo == null) {
			player.sendError(Translate.破封任务配置错误 + "!!");
			player.addMessageToRightBag(new CONFIRM_COLLENT_MATERIAL_FOR_BOSS_RES(req.getSequnceNum(), 0));
			return;
		}

		if (sealLevel == 220 || sealLevel == 190) {
			int changeBossId = 0;
			int changeBuffLevel = 0; // 获取buff级别
			int bufflevel = taskInfo.getBuffLevel();
			long allHasPoints = taskInfo.getPoints() + allMaterialNum * 10;
			long allPoints = binfo.allPoints;

			if (allHasPoints >= allPoints) {
				changeBuffLevel = 5;
			} else if (allHasPoints >= ((int) allPoints * 4 / 5)) {
				changeBuffLevel = 4;
			} else if (allHasPoints >= ((int) allPoints * 3 / 5)) {
				changeBuffLevel = 3;
			} else if (allHasPoints >= ((int) allPoints * 2 / 5)) {
				changeBuffLevel = 2;
			} else if (allHasPoints >= ((int) allPoints * 1 / 5)) {
				changeBuffLevel = 1;
			}

			if (sealLevel == 220) {
				int bossids[] = binfo.bossids;
				if (bossids == null) {
					if (Game.logger.isWarnEnabled()) {
						Game.logger.warn("[破封任务捐献确认] [220级封印挑战boss配置错误] [boss技能，bossBuff] [bossid:{}] [bufflevel:{}] [pname:{}]", new Object[] { (taskInfo.getBossid() + "-->" + changeBossId), (bufflevel + "-->" + changeBuffLevel), player.getName() });
					}
					return;
				}

				changeBossId = bossids[changeBuffLevel];
			}

			long oldPoints = taskInfo.getPoints();

			if (oldPoints >= allPoints) {
				player.sendError(Translate.捐献已到上线);
				return;
			}

			end: for (int i = 0; i < materialIds.length; i++) {
				long id = materialIds[i];
				if (id > 0) {
					int nums = materialNums[i];
					for (int j = 0; j < nums; j++) {
						ArticleEntity aee = player.removeArticleEntityFromKnapsackByArticleId(id, "破封任务捐献删除", true);
						if (aee == null) {
							String description = Translate.删除物品失败;
							HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
							player.addMessageToRightBag(hreq);
							player.addMessageToRightBag(new CONFIRM_COLLENT_MATERIAL_FOR_BOSS_RES(req.getSequnceNum(), 0));
							if (Game.logger.isWarnEnabled()) {
								Game.logger.warn("[破封任务捐献确认] [删除物品不成功] [id:{}] [boss技能，bossBuff] [bossid:{}] [bufflevel:{}] [pname:{}]", new Object[] { id, (taskInfo.getBossid() + "-->" + changeBossId), (bufflevel + "-->" + changeBuffLevel), player.getName() });
							}
							ARTICLE_REMOVE_SUCC_RES res = new ARTICLE_REMOVE_SUCC_RES(GameMessageFactory.nextSequnceNum(), 1);
							player.addMessageToRightBag(res);
							return;
						} else {
							oldPoints += 10;
							taskInfo.setPoints(oldPoints);
							taskInfo.setSealType(bossType);
							ArticleStatManager.addToArticleStat(player, null, aee, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "破封任务捐献删除", null);
							if (Game.logger.isWarnEnabled()) {
								Game.logger.warn("[破封任务捐献确认] [成功] [bossType:{}] [sealLevel:{}] [sealStep:{}] [id:{}] [boss技能，bossBuff] [bossid:{}] [bufflevel:{}] [pname:{}] [points:{}]", new Object[] { bossType, sealLevel, sealStep, id, (taskInfo.getBossid() + "-->" + changeBossId), (bufflevel + "-->" + changeBuffLevel), player.getName(), (oldPoints + "-->" + allPoints) });
							}
							ARTICLE_REMOVE_SUCC_RES res = new ARTICLE_REMOVE_SUCC_RES(GameMessageFactory.nextSequnceNum(), 0);
							player.addMessageToRightBag(res);
						}
						if (oldPoints >= allPoints) {
							if (Game.logger.isWarnEnabled()) {
								Game.logger.warn("[破封任务捐献确认] [收集以达上限] [sealLevel:{}] [sealStep:{}] [id:{}] [buffid:{}] [boss技能，bossBuff] [bossid:{}] [bufflevel:{}] [pname:{}]", new Object[] { sealLevel, sealStep, id, (binfo.buffid), (taskInfo.getBossid() + "-->" + changeBossId), (bufflevel + "-->" + changeBuffLevel), player.getName() });
							}
							taskInfo.setPoints(allPoints);
							if (sealLevel == 220) {

							} else {
								if (sealLevel == 190 && sealStep == 2) {

								} else {
									SealManager.getInstance().getSeal().setSealStep(sealStep + 1);
									taskInfo.setSealStep(sealStep + 1);
									long newSealTime = System.currentTimeMillis();
									long oldSealTime = SealManager.getInstance().getSeal().getSealCanOpenTime();
									if (newSealTime + binfo.cutSealTime < oldSealTime) {
										newSealTime = oldSealTime - binfo.cutSealTime;
									}
									SealManager.getInstance().getSeal().setSealCanOpenTime(newSealTime,"捐献");
									String days = "";
									if (newSealTime > System.currentTimeMillis()) {
										days = (newSealTime - System.currentTimeMillis()) / (24 * 60 * 60 * 1000) + "";
									} else {
										days = 0 + "";
									}
									String mess = Translate.translateString(Translate.捐献提示, new String[][] { { Translate.STRING_1, 190 + "" }, { Translate.STRING_2, (sealStep + 1) + "" }, { Translate.STRING_3, (binfo.cutSealTime / 一天毫秒数) + "" }, { Translate.STRING_4, days } });
									ChatMessage msg = new ChatMessage();
									msg.setMessageText(mess);
									try {
										ChatMessageService.getInstance().sendColorMessageToWorld(msg);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}
							break end;
						}
					}
				}

			}
			// 捐献成功 bossid,bufflevel
			if (sealLevel == 190 && sealStep == 2) {
				taskInfo.setBossid(taskInfo.getBossid());
			} else {
				taskInfo.setBossid(changeBossId);
			}
			taskInfo.setBuffLevel(changeBuffLevel);

			try {
				saveInfo(taskInfo);
				sendReward(binfo.articleInfo, player, allMaterialNum);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			player.sendError(Translate.捐献成功);
			player.addMessageToRightBag(new CONFIRM_COLLENT_MATERIAL_FOR_BOSS_RES(req.getSequnceNum(), 1));

			{
				try {
					int stype = 0;
					if (sealLevel == 220) {
						stype = 1;
					}
					if (sealLevel == 190 && sealStep == 2) {
						stype = 1;
					}
					int buffId = binfo.buffid;
					BuffTemplate bt = BuffTemplateManager.getInstance().getBuffTemplateById(buffId);
					Buff buff = bt.createBuff(changeBuffLevel);
					String bossBuffInfo = buff.getDescription();
					if (buff.getLevel() == 0) {
						bossBuffInfo = Translate.捐献buff级别是0;
					}
					String bossBuffName = buff.getTemplateName();
					Article a = ArticleManager.getInstance().getArticle(binfo.needMaterialName);
					if(a==null){
						Game.logger.warn("[破封任务捐献确认] [失败：物品不存在] ["+binfo.needMaterialName+"]");
						return;
					}
					ArticleEntity ae = null;
					ae = DefaultArticleEntityManager.getInstance().getEntity(binfo.needMaterialName, true, a.getColorType());
					if (ae == null) {
						try {
							ae = DefaultArticleEntityManager.getInstance().createTempEntity(a, true, ArticleEntityManager.封印捐献物品, player, a.getColorType());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					String resuleMess = binfo.resultMess;
					if (sealLevel == 220 && bossType == 1) {
						resuleMess = Translate.捐献材料描述_220;
					} else if (sealLevel == 220 && bossType == 2) {
						resuleMess = Translate.捐献材料描述_220_2;
					}
					COLLECT_MATERIAL_FOR_BOSS_RES res = new COLLECT_MATERIAL_FOR_BOSS_RES(GameMessageFactory.nextSequnceNum(), resuleMess, (ae == null ? -2 : ae.getId()), bossBuffName, buff.getIconId(), bossBuffInfo, oldPoints, allPoints, stype, bossType, (ae == null ? "" : ae.getArticleName()));
					player.addMessageToRightBag(res);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		}

	}

	public void sendReward(SealRewardArticleInfo info, Player p, int nums) {
		if (info != null && nums > 0) {
			Article a = ArticleManager.getInstance().getArticleByCNname(info.name);
			if (a == null) {
				if (Game.logger.isWarnEnabled()) {
					Game.logger.warn("[破封任务捐献奖励] [失败-物品不存在:" + info.name + "] [总数：" + nums + "] [" + info.toString() + "] [" + p.getLogString() + "]");
				}
				return;
			}
			try {
				ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, info.isbind, ArticleEntityManager.封印捐献奖励, p, info.color, nums, true);
				ArticleEntity aes[] = new ArticleEntity[nums];
				Arrays.fill(aes, ae);
				if (ae != null) {
					if (p.putAll(aes, "破封任务捐献奖励")) {
						p.sendError(Translate.封印奖励_title);
					} else {
						MailManager.getInstance().sendMail(p.getId(), aes, new int[] { nums }, Translate.封印奖励_title, Translate.封印奖励_title, 0, 0, 0, "破封任务捐献奖励");
						p.sendError(Translate.封印奖励_title);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				if (Game.logger.isWarnEnabled()) {
					Game.logger.warn("[破封任务捐献奖励] [失败-创建物品异常:" + info.name + "] [总数：" + nums + "] [" + info.toString() + "] [" + p.getLogString() + "]", e);
				}
			}
		}
	}

	public void noticeSealTaskStat(Player p) {
		if (seal != null) {
			try {
				int sealStat = 1;
				
				int currSealLevel = SealManager.getInstance().getSealLevel();
				int currSealStep = SealManager.getInstance().getSeal().getSealStep();

				int index = SealManager.getInstance().getIndex(currSealLevel);
				if (index <= 0) {
					sealStat = 0;
				}

				if (p.getSoulLevel() < currSealLevel) {
					sealStat = 0;
				}

				if (currSealLevel == 70 || currSealLevel == 110 || currSealLevel == 260 || currSealLevel == 300) {
					sealStat = 0;
				}
				
				if (sealStat == 1) {
					if (!SealManager.getInstance().isStartSealTask()) {
						sealStat = 0;
					} else {
						Long sealTaskStartTime = (Long) ActivityManagers.getInstance().getDdc().get("破封任务开启提示" + currSealLevel);
						if (sealTaskStartTime == null) {
							String mess = Translate.translateString(Translate.破封任务开启, new String[][] { { Translate.STRING_1, currSealLevel + "" } });
							ChatMessage msg = new ChatMessage();
							msg.setMessageText(mess);
							try {
								ChatMessageService.getInstance().sendHintMessageToSystem(msg);
							} catch (Exception e) {
								e.printStackTrace();
							}

							ActivityManagers.getInstance().getDdc().put("破封任务开启提示" + currSealLevel, new Long(System.currentTimeMillis()));
						}
					}
				}

				if (sealStat == 1 && isCloseCurrSeal()) {
					if (currSealLevel == 190 && currSealStep == 2) {
						SealManager.getInstance().getSeal().setSealStep(3);
						SealManager.getInstance().saveSeal();
						SealTaskInfo taskInfo = SealManager.getInstance().getTakInfo(currSealLevel, currSealStep, 0);
						taskInfo.setSealStep(3);
					}
					sealStat = 0;
				}

				if (sealStat == 1 &&currSealLevel == 220 && currSealStep == 1) {
					refreshWorldBoss();
				}
				if (sealStat == 1 &&currSealLevel == 220 && currSealStep == 0) {
					long now = System.currentTimeMillis();
					SealTaskInfo taskInfo = SealManager.getInstance().getTakInfo(currSealLevel, currSealStep, 0);

					if (taskInfo == null) {
						taskInfo = new SealTaskInfo();
					}
					if (taskInfo.getSealTaskStartTime() > 0) {
						if (now - taskInfo.getSealTaskStartTime() > SEAL_STEP_COLSE_LENGTH_220) {
							SealManager.getInstance().getSeal().setSealStep(1);
							taskInfo.setSealStep(1);
							SealCityBossInfo sealinfo = null;
							long newSealTime = System.currentTimeMillis();
							long oldSealTime = SealManager.getInstance().getSeal().getSealCanOpenTime();

							for (SealCityBossInfo info : infos) {
								if (info.sealLayer == currSealStep + 1 && info.sealLevel == SealManager.getInstance().getSealLevel()) {
									sealinfo = info;
									break;
								}
							}

							if (sealinfo == null) {
								if (Game.logger.isInfoEnabled()) {
									Game.logger.info("[通知开启破封任务] [出错:sealinfo==null] [220自动开启第2阶段boss挑战任务] [捐献时间到] [currSealLevel:{}] [currSealStep:{}] [玩家信息:{}]", new Object[] { currSealLevel, currSealStep, p.getLogString() });
								}
								return;
							}
							if (newSealTime + sealinfo.cutSealTime < oldSealTime) {
								newSealTime = oldSealTime - sealinfo.cutSealTime;
							}
							String days = "";
							if (newSealTime > System.currentTimeMillis()) {
								days = (newSealTime - System.currentTimeMillis()) / (24 * 60 * 60 * 1000) + "";
							} else {
								days = 0 + "";
							}
							SealManager.getInstance().getSeal().setSealCanOpenTime(newSealTime,"开启破封任务");
							SealManager.getInstance().saveSeal();
							String mess = Translate.translateString(Translate.破封野外boss公告, new String[][] { { Translate.STRING_1, Translate.暴炎邪帝 }, { Translate.STRING_2, SealManager.野外boss_中文地图 } });
							ChatMessage msg = new ChatMessage();
							msg.setMessageText(mess);
							try {
								ChatMessageService.getInstance().sendColorMessageToWorld(msg);
							} catch (Exception e) {
								e.printStackTrace();
							}
							{
								String mess2 = Translate.translateString(Translate.第一阶段自动提示_220, new String[][] { { Translate.STRING_1, 30 + "" }, { Translate.STRING_1, days } });
								ChatMessage msg2 = new ChatMessage();
								msg2.setMessageText(mess2);
								try {
									ChatMessageService.getInstance().sendColorMessageToWorld(msg2);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							refreshWorldBoss();
							if (Game.logger.isInfoEnabled()) {
								Game.logger.info("[通知开启破封任务] [成功] [220自动开启第2阶段boss挑战任务] [捐献时间到] [玩家信息:{}] [sealStat:{}]", new Object[] { p.getLogString(),sealStat });
							}
						}
					}
				}
				
				if (Game.logger.isDebugEnabled()) {
					Game.logger.debug("[通知开启破封任务] [debug测试] [成功] [玩家信息:{}] [sealStat:{}] [pstat:{}] [currSealLevel:{}]", new Object[] { p.getLogString(),sealStat,p.sealStat,currSealLevel });
				}
				
				if(p.sealStat!=sealStat){
					p.sealStat = sealStat;
				}else{
					return;
				}
				
				if (Game.logger.isInfoEnabled()) {
					Game.logger.info("[通知开启破封任务] [成功] [玩家信息:{}] [sealStat:{}] [pstat:{}] [currSealLevel:{}]", new Object[] { p.getLogString(),sealStat,p.sealStat,currSealLevel });
				}
				
				SEAL_TASK_STAT_RES res = new SEAL_TASK_STAT_RES(GameMessageFactory.nextSequnceNum(), sealStat);
				p.addMessageToRightBag(res);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 关闭条件
	 * 1.每天00点
	 * 2.每次缩短封印时间的时候
	 * --
	 * 1.打开按钮的时候
	 * 2.圆的按钮不显示
	 * 3.野外boss不刷新
	 * @return
	 */
	public boolean isCloseCurrSeal() {
		if (testFalg) {
			return false;
		}
		int sealLevel = SealManager.getInstance().getSealLevel();
		int sealStep = SealManager.getInstance().getSeal().getSealStep();
		long currSealTime = SealManager.getInstance().getSeal().getSealCanOpenTime();
		long now = System.currentTimeMillis();
		long cutTime = 0;

		SealCityBossInfo bossinfo = null;
		for (SealCityBossInfo info : infos) {
			if (info.sealLevel == sealLevel && info.sealLayer == sealStep) {
				bossinfo = info;
			}
		}

		if (bossinfo != null) {
			cutTime = bossinfo.cutSealTime;
		}
		if ((sealLevel == 150 || sealLevel == 190) && sealStep == 2) {
			cutTime = 0;
		}
		if (sealLevel == 220 && sealStep == 1) {
			cutTime = 0;
		}

		if (now + cutTime + closeTimeLength >= currSealTime) {
			// if(Game.logger.isInfoEnabled()){
			// Game.logger.info("[关闭条件] [关闭成功] [sealLevel:"+sealLevel+"] [sealStep:"+sealStep+"] [解封时间："+TimeTool.formatter.varChar23.format(currSealTime)+"] [缩短时间:"+cutTime+"-ms] [差值:"+(now+cutTime+closeTimeLength-currSealTime)+"-ms]");
			// }
			return true;
		}

		return false;
	}

	/**
	 * 解封条件,封印保护间隔
	 * 本阶段开启时间-上阶段开启时间≥上阶段原封印时间-上阶段总共缩短的封印天数
	 */
	public boolean openSeal() {
		int currSealLevel = SealManager.getInstance().getSealLevel();
		if (testFalg || currSealLevel==70) {
			testFalg = false;
			return true;
		}
		
		long currOpenSealTime = System.currentTimeMillis();
		int currSealStep = SealManager.getInstance().getSeal().getSealStep();

		int currSealLength = 0;
		for (int i = 0; i < SEAL_LEVELS.length; i++) {
			if (SEAL_LEVELS[i] == currSealLevel) {
				currSealLength = SEAL_OPEN_DAYS[i];
			}
		}

		long lastStepAllCutTime = 0;
		for (SealCityBossInfo info : infos) {
			if (info.sealLevel == currSealLevel) {
				if(info.sealLayer <= currSealStep){
					lastStepAllCutTime += info.cutSealTime;
				}
			}
		}

		int index = SealManager.getInstance().getIndex(currSealLevel);
		index = index - 1;
		if (index < 0) {
			index = 0;
		}
		long lastOpenSealTime = 0;
		int lastSealLevel = SealManager.takeSealTaskLevel[index];
		Seal seal = SealManager.getInstance().sealMap.get(lastSealLevel);
		if (seal != null) {
			lastOpenSealTime = seal.getSealCanOpenTime();
		}

		if (lastOpenSealTime <= 0) {
			if (Game.logger.isInfoEnabled()) {
				Game.logger.info("[解封条件] [失败:获取上一阶段解封时间出错] [sealLevel:" + currSealLevel + "] [sealStep:" + currSealStep + "] [当前解封时间：" + TimeTool.formatter.varChar23.format(currOpenSealTime) + "] [lastOpenSealTime:" + lastOpenSealTime + "] [lastSealLevel:" + lastSealLevel + "]");
			}
			return false;
		}

		long value = (currOpenSealTime - lastOpenSealTime) - (currSealLength * 一天毫秒数 - lastStepAllCutTime);

		if (Game.logger.isInfoEnabled()) {
			Game.logger.info("[解封条件] [value:" + (value / (一天毫秒数)) + "] [sealLevel:" + currSealLevel + "] [sealStep:" + currSealStep + "] [value:" + value + "] [一天毫秒数:" + 一天毫秒数 + "] [本阶段原封印间隔:" + currSealLength + "] [本阶段总共缩减时间:" + lastStepAllCutTime + "] [当前解封时间：" + TimeTool.formatter.varChar23.format(currOpenSealTime) + "] [上一阶段解封时间:" + TimeTool.formatter.varChar23.format(lastOpenSealTime) + "] [lastSealLevel:" + lastSealLevel + "]");
		}

		if (value >= 0) {
			return true;
		}

		return false;
	}

	/**
	 * 封印道具产出
	 * @param color
	 */
	public void sealArticleOutPut(Player p, int color) {
		int rannum = random.nextInt(100);
		boolean isoutput = false;
		if (color == 0) {
			if (rannum <= 5) {
				isoutput = true;
			}
		} else if (color == 1) {
			if (rannum <= 25) {
				isoutput = true;
			}
		} else if (color == 2) {
			if (rannum <= 50) {
				isoutput = true;
			}
		} else if (color == 3) {
			if (rannum <= 70) {
				isoutput = true;
			}
		} else if (color == 4) {
			if (rannum <= 85) {
				isoutput = true;
			}
		}

		int currSealLevel = SealManager.getInstance().getSealLevel();
		int currSealStep = SealManager.getInstance().getSeal().getSealStep();

		if (isoutput == false) {
			return;
		}

		if (!SealManager.getInstance().isStartSealTask()) {
			return;
		}

		if (Game.logger.isInfoEnabled()) {
			Game.logger.info("[封印道具产出] [" + isoutput + "] [currSealLevel:" + currSealLevel + "] [currSealStep:" + currSealStep + "] [rannum:" + rannum + "] [color:" + color + "] [" + p.getName() + "]");
		}

		if (currSealLevel == 190 && (currSealStep == 0 || currSealStep == 2) || (currSealLevel == 220 && currSealStep == 0)) {
			SealCityBossInfo binfo = null;
			for (SealCityBossInfo info : infos) {
				if (info.sealLevel == currSealLevel && currSealStep + 1 == info.sealLayer) {
					binfo = info;
				}
			}
			if (binfo == null) {
				if (Game.logger.isInfoEnabled()) {
					Game.logger.info("[封印道具产出] [出错:获取配置信息出错] [currSealLevel:" + currSealLevel + "] [currSealStep:" + currSealStep + "]  [" + p.getName() + "]");
				}
			} else {
				String rewardName = binfo.needMaterialName;
				if (rewardName == null) {
					if (Game.logger.isInfoEnabled()) {
						Game.logger.info("[封印道具产出] [出错:获取奖励物品出错] [currSealLevel:" + currSealLevel + "] [currSealStep:" + currSealStep + "]  [" + p.getName() + "]");
					}
				} else {
					if(currSealLevel == 190 && currSealStep == 2){
						SealTaskInfo info = SealManager.getInstance().getTakInfo(currSealLevel, currSealStep, 0);
						if(info.getPoints()>=binfo.allPoints){
							if (Game.logger.isInfoEnabled()) {
								Game.logger.info("[封印道具产出] [捐献已满] [points:"+info.getPoints()+"] [currSealLevel:" + currSealLevel + "] [currSealStep:" + currSealStep + "]  [" + p.getName() + "]");
							}
							return;
						}
					}else if(currSealLevel == 220 && currSealStep == 0){
						String collectMess = "";
						SealTaskInfo info = SealManager.getInstance().getTakInfo(currSealLevel, currSealStep, 1);
						if(info.getPoints()>=binfo.allPoints){
							collectMess = "第1阶段满";
						}
						if(collectMess.length()>=0){
							SealTaskInfo info2 = SealManager.getInstance().getTakInfo(currSealLevel, currSealStep, 2);
							if(info2.getPoints()>=binfo.allPoints){
								collectMess = "第2阶段满";
							}
						}
						if (Game.logger.isInfoEnabled()) {
							Game.logger.info("[封印道具产出] [collectMess:"+collectMess+"] [points:"+info.getPoints()+"] [currSealLevel:" + currSealLevel + "] [currSealStep:" + currSealStep + "]  [" + p.getName() + "]");
						}
						if(collectMess.equals("第2阶段满")){
							return;
						}
					}
					Article a = ArticleManager.getInstance().getArticle(rewardName);
					if (a == null) {
						if (Game.logger.isInfoEnabled()) {
							Game.logger.info("[封印道具产出] [出错:物品不存在 " + rewardName + "] [currSealLevel:" + currSealLevel + "] [currSealStep:" + currSealStep + "]  [" + p.getName() + "]");
						}
					} else {
						try {
							ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.采集产出封印物品, p, 3, 1, true);
							if (ae != null) {
								if (p.putToKnapsacks(ae, "采集产出封印材料")) {
									p.sendError(Translate.translateString(Translate.恭喜您获得了, new String[][] { { Translate.STRING_1, rewardName }, { Translate.COUNT_1, 1 + "" } }));
								} else {
									p.sendError(Translate.text_trade_015);
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}

	}

	public void changeSealTime() {
		if (seal != null) {
			sealMap.put(seal.getSealLevel(), seal);
			try {
				saveSeal();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 处理野外boss死亡
	 * @param monster
	 */
	public void handleBossDead(SimpleMonster boss) {
		if(!SealManager.getInstance().isRightBoss(boss.getSpriteCategoryId())){
			return;
		}
		
		SealTaskInfo taskinfo = SealManager.getInstance().getTakInfo(SealManager.getInstance().getSealLevel(), 1, 0);
		if (taskinfo == null) {
			taskinfo = new SealTaskInfo();
			SealManager.getInstance().saveInfo(taskinfo);
		}

		if (taskinfo.getBossid() == 0) {
			taskinfo.setBossid(boss.getSpriteCategoryId());
			taskinfo.setSealStep(1);
			SealManager.getInstance().saveInfo(taskinfo);
			SealCityBossInfo sealinfo = null;
			long newSealTime = System.currentTimeMillis();
			long oldSealTime = SealManager.getInstance().getSeal().getSealCanOpenTime();

			for (SealCityBossInfo info : infos) {
				if (info.sealLayer == 2 && info.sealLevel == SealManager.getInstance().getSealLevel()) {
					sealinfo = info;
					break;
				}
			}

			if (sealinfo != null) {
				if (newSealTime + sealinfo.cutSealTime < oldSealTime) {
					newSealTime = oldSealTime - sealinfo.cutSealTime;
				}

				SealManager.getInstance().getSeal().setSealCanOpenTime(newSealTime,"boss死亡");
				SealManager.getInstance().getSeal().setSealStep(1);
				String days = "";
				if (newSealTime > System.currentTimeMillis()) {
					days = (newSealTime - System.currentTimeMillis()) / (24 * 60 * 60 * 1000) + "";
				} else {
					days = 0 + "";
				}
				String mess = Translate.translateString(Translate.破封成功, new String[][] { { Translate.STRING_1, (sealinfo.cutSealTime / (一天毫秒数)) + "" }, { Translate.STRING_2, days } });
				ChatMessage msg = new ChatMessage();
				msg.setMessageText(mess);
				try {
					ChatMessageService.getInstance().sendColorMessageToWorld(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			Game.logger.warn("[野外boss死亡] [首次击杀] [monster:" + boss + "] [id:" + boss.getSpriteCategoryId() + "] [oldSealTime:"+oldSealTime+"] [newSealTime:"+newSealTime+"] [cutSealTime:" + (sealinfo == null ? "0" : sealinfo.cutSealTime) + "]");
		} else {
			Game.logger.warn("[野外boss死亡] [已经击杀过] [monster:" + boss + "] [id:" + boss.getSpriteCategoryId() + "]");
		}

	}

	/**
	 * 是否开启破封任务
	 * @return
	 */
	public boolean isStartSealTask() {
		if (testFalg) {
			return true;
		}
		long now = System.currentTimeMillis();
		int currSealLevel = SealManager.getInstance().getSealLevel();
		int currSealStep = SealManager.getInstance().getSeal().getSealStep();
		int index = SealManager.getInstance().getIndex(currSealLevel);
		if (index <= 0) {
			if (Game.logger.isInfoEnabled()) {
				Game.logger.info("[查看破封任务] [表的封印等级配置错误] [当前等级:{}] [当前阶段:{}]", new Object[] { currSealLevel, currSealStep });
			}
			return false;
		}

		int nextDayLength = SealManager.开启下一级破封任务间隔[index];

		long openSealDays = SEAL_OPEN_DAYS[index];

		long kingOpenSealTime = SealManager.getInstance().getSeal().getSealCanOpenTime() - openSealDays * SealManager.一天毫秒数;

		if (now - kingOpenSealTime <= nextDayLength * SealManager.一天毫秒数) {
//			if (Game.logger.isInfoEnabled()) {
//				Game.logger.info("[查看破封任务] [失败:不满足国王解封后间隔限制] [解封后几天后才能开启:{}] [封印等级:{}] [封印阶段:{}] [国王开启封印的时间：{}] [openSealDays:{}]", new Object[] { nextDayLength, currSealLevel, currSealStep, TimeTool.formatter.varChar23.format(kingOpenSealTime), openSealDays });
//			}
			return false;
		}

		return true;

	}

	int buffnums = 0;

	public void refreshWorldBoss() {
		try {
			Long date = (Long) ActivityManagers.getInstance().getDdc().get(KEY_BOSS_DEAD);
			if (date == null) {
				ActivityManagers.getInstance().getDdc().put(KEY_BOSS_DEAD, new Long(0L));
				date = (Long) ActivityManagers.getInstance().getDdc().get(KEY_BOSS_DEAD);
			}

			if (System.currentTimeMillis() - date.longValue() < BOSS_REFRESH_LENGTH) {
				return;
			}
			// 野外boss
			int currSealLevel = SealManager.getInstance().getSealLevel();
			int currSealStep = SealManager.getInstance().getSeal().getSealStep();
			if (currSealLevel == 220 && currSealStep == 1) {
				Game game = GameManager.getInstance().getGameByName(野外boss_地图, 0);
				if (game != null) {
					LivingObject livingObjectArray[] = game.getLivingObjects();
					BossMonster bossmonster = null;
					int ids[] = { 20113263, 20113262, 20113261, 20113260, 20113259, 20113267 };
					if (livingObjectArray != null && livingObjectArray.length > 0) {
						end: for (LivingObject o : livingObjectArray) {
							if (o != null && o instanceof BossMonster) {
								BossMonster boss = (BossMonster) o;
								for (int id : ids) {
									if (boss.getSpriteCategoryId() == id) {
										bossmonster = boss;
										break end;
									}
								}
							}
						}
					}

					// 加buff
					if (bossmonster != null && bossmonster.getHp() < bossmonster.getMaxHP() && buffnums == 0) {
						BuffTemplate bt = BuffTemplateManager.getInstance().getBuffTemplateById(1767);
						if (bt != null) {
							Buff buff = bt.createBuff(1);
							buff.setStartTime(SystemTime.currentTimeMillis());
							buff.setInvalidTime(buff.getStartTime() + 100 * 24 * 60 * 60 * 1000);
							if (buff != null) {
								bossmonster.placeBuff(buff);
								if (Game.logger.isWarnEnabled()) {
									Game.logger.warn("[通知开启破封任务] [刷新野外boss]  [种植buff成功] [buff:{}] [boss:{}] [玩家:{}]", new Object[] { buff.getTemplateName() + "--" + buff.getLevel(), bossmonster.getName() });
								}
							}
						}
						buffnums = 1;
					}

					if (bossmonster == null) { // 刷boss
						if (Game.logger.isInfoEnabled()) {
							Game.logger.info("[通知开启破封任务] [刷新野外boss] [心跳] [currSealLevel:{}] [currSealStep:{}] [bossmonster:{}] [boss是否开启：{}]", new Object[] { currSealLevel, currSealStep, (bossmonster == null ? "null" : bossmonster.getName()), (bossmonster == null ? "null" : bossmonster.isTurnOnFlag()) });
						}
						SealTaskInfo taskInfo = SealManager.getInstance().getTakInfo(currSealLevel, 0, 1);
						int bossid = taskInfo.getBossid();
						if (bossid == 0) {
							bossid = 野外boss_bossId;
						}

						Sprite sprite = MemoryMonsterManager.getMonsterManager().createMonster(bossid);
						if (sprite != null && game != null) {
							sprite.setX(SealManager.野外boss_x);
							sprite.setY(SealManager.野外boss_y);
							sprite.setBornPoint(new com.fy.engineserver.core.g2d.Point(SealManager.野外boss_x, SealManager.野外boss_y));
							game.addSprite(sprite);
							buffnums = 0;
							ActivityManagers.getInstance().getDdc().put(KEY_BOSS_DEAD, new Long(System.currentTimeMillis()));
							if (Game.logger.isInfoEnabled()) {
								Game.logger.info("[通知开启破封任务] [刷新野外boss] [成功] [boss:{}] [bossid:{}]", new Object[] { sprite.getName(), bossid });
							}
						} else {
							if (Game.logger.isInfoEnabled()) {
								Game.logger.info("[通知开启破封任务] [刷新野外boss] [失败:{}] [currSealLevel:{}] [currSealStep:{}] [bossmonster:{}][bossid:{}]", new Object[] { (sprite == null ? "sprite==null" : "game==null"), bossid });
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
