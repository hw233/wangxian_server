package com.fy.engineserver.sprite;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.client.BossClientService;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.core.CoreSubSystem;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.PlayerMessagePair;
import com.fy.engineserver.core.event.LeaveGameEvent;
import com.fy.engineserver.core.event.ReconnectEvent;
import com.fy.engineserver.core.res.ResourceManager;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.career.CareerManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.enterlimit.EnterLimitManager;
import com.fy.engineserver.gateway.GameNetworkFramework;
import com.fy.engineserver.gateway.GameSubSystem;
import com.fy.engineserver.gateway.MieshiGatewayClientService;
import com.fy.engineserver.green.GreenServerManager;
import com.fy.engineserver.lineup.EnterServerAgent;
import com.fy.engineserver.message.*;
import com.fy.engineserver.platform.CloseFunctionManager;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.security.SecuritySubSystem;
import com.fy.engineserver.smith.waigua.WaiguaManager;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.stat.GameStatClientService;
import com.fy.engineserver.uniteserver.UnitServerFunctionManager;
import com.fy.engineserver.uniteserver.UnitedServerManager;
import com.fy.engineserver.uniteserver.UnitServerFunctionManager.Function;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.vip.VipManager;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.stat.model.PlayerActionFlow;
import com.xuanzhi.tools.text.StringUtil;
import com.xuanzhi.tools.text.WordFilter;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;

public class SpriteSubSystem implements GameSubSystem, Runnable {

	public static boolean isUSERENTERSERVER = true;

	public static Logger logger = LoggerFactory.getLogger(SpriteSubSystem.class);

	private static SpriteSubSystem self;

	public static SpriteSubSystem getInstance() {
		return self;
	}

	public static final String UNKNOWN = Translate.text_1211;

	/**
	 * 每个用户最多可以创建几个角色
	 */
	public static final int MAX_PLAYER_PER_USER = 1;

	public static int NOLINEUP_VIPLEVEL = 2;

	protected Thread thread;
	protected boolean running = true;
	protected long waittingTime = 60 * 1000L;

	GameNetworkFramework framework;

	PlayerManager playerManager;
	GameManager gameManager;
	public BossClientService bossService;
	// StatClientService statClientService;
	GameConstants gameConstants;

	protected String privatekey = "sqageoverflow";

	public String prefixforbid[] = new String[] { "gm" };

	public String GM_USERNAMES[] = new String[] { "wtx062","YIJIEUSER2_107483"};

	public static String tagforbid[] = new String[] { "</f>","'","'", "\"", " or ", "μ", "Μ", "М", "\n", "\t", " ", "　", "　", Translate.text_30, Translate.text_5874, Translate.text_5875, "GM", Translate.text_5876, "Gm", "М", "м", "Μ", "GМ", "gм", "Gм", "GΜ", "gM", Translate.text_1218, Translate.text_1490, Translate.text_5877, Translate.text_5878, Translate.text_5879, Translate.text_30, Translate.text_5880, Translate.text_5881, Translate.text_5876, Translate.text_5882, Translate.text_5883, Translate.text_5884, Translate.text_5885, Translate.text_5886, Translate.text_5887, Translate.text_5888, Translate.text_4951, Translate.text_5889, Translate.text_5890, Translate.text_5891, Translate.text_5892, Translate.text_5893, Translate.text_5894, Translate.text_5895, Translate.text_5874, Translate.text_5875, "GM", Translate.text_5876, Translate.text_5896, Translate.text_5897, Translate.text_5898, "GM001", "GM002", "GM003", "GM004", "GM005", "GM006", "GM007", "GM008", "GM009", "GM010", "gm", Translate.text_5899, Translate.text_5900, "gm001", "gm002", "gm003", "gm004", "gm005", "gm006", "gm007", "gm008", "gm009", "gm010", "G.m", "g.m", "G.M", "GM", Translate.text_5901, Translate.text_5902, Translate.text_5903, Translate.text_5904, Translate.text_5887, Translate.text_5905, Translate.text_5906, Translate.text_5907, Translate.text_5908, Translate.text_5909, Translate.text_5910, Translate.text_5911, Translate.text_5912, "Ｇm", "ＧM", "ｇｍ", "gＭ", "GＭ", "ｇm", "ｇM", "Μ", "М", "Ｇ", "м", "ｇ", "£", "￠", "＄", "€", "￥", "¢", "¥" };

	public static void main(String args[]) {
		for (int i = 0; i < tagforbid.length; i++) {
			if (tagforbid[i].equals("M") || tagforbid[i].equals("m")) {
				System.out.println(tagforbid[i] + "===" + i);
			}

		}
	}

	// public static Logger playerInfoLog = Logger.getLogger("PlayerInfo");
	public static Logger playerInfoLog = LoggerFactory.getLogger("PlayerInfo");

	public void setGameNetworkFramework(GameNetworkFramework framework) {
		this.framework = framework;
	}

	public void setPlayerManager(PlayerManager pm) {
		playerManager = pm;
	}

	public PlayerManager getPlayerManager() {
		return playerManager;
	}

	public void setBossService(BossClientService bossService) {
		this.bossService = bossService;
	}

	public void setGameConstants(GameConstants gameConstants) {
		this.gameConstants = gameConstants;
	}

	public void setGameManager(GameManager gameManager) {
		this.gameManager = gameManager;
	}

	public void setPrivatekey(String privatekey) {
		this.privatekey = privatekey;
	}

	// public void setStatClientService(StatClientService statClientService) {
	// this.statClientService = statClientService;
	// }

	public HashSet<String> 老玩家回归活动临时变量 = new HashSet<String>();
	public static String 停止注册提示 = "该服务器暂停创建新角色";
	
	
	public void init() throws Exception {
		
		running = true;
		thread = new Thread(this, "Synchronize_GameTime_Thread");
		thread.start();

		// initSameNamePlayers();

		// ////////////////////////////////////////////////////////
		// 特殊处理
		String fileName = "/home/game/resin/webapps/game_server/WEB-INF/game_runtime_data/old_users.txt";

		File file = new File(fileName);
		if (file.isFile() && file.exists()) {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String s = null;
			// 文件格式：账号,手机号码,....
			while ((s = reader.readLine()) != null) {
				String ss[] = s.split(",");
				if (ss.length > 0) {
					String username = ss[0];
					if (username.trim().length() > 0) {
						老玩家回归活动临时变量.add(username.trim());
					}
				}
			}
			reader.close();
			System.out.println("[老玩家回归活动] [加载用户：" + 老玩家回归活动临时变量.size() + "个] [" + fileName + "]");
		}

		self = this;
		ServiceStartRecord.startLog(this);
	}

	public String[] getInterestedMessageTypes() {
		return new String[] { "ACTIVE_TEST_REQ", "CREATE_PLAYER_REQ", "REMOVE_PLAYER_REQ", "DELETE_PLAYER_REQ", "QUERY_PLAYER_REQ", "TIME_SYNC_RES", "USER_ENTER_SERVER_REQ", "NEW_USER_ENTER_SERVER_REQ", "USER_ENTER_SERVER2_REQ", "PLAYER_UPDATE_REQ", "SET_QUEUE_READYNUM_REQ", "PLAYER_RECONN_REQ", "PLAYER_ENTER_CROSSSERVER_REQ", "LEAVE_LINEUP_REQ", "SET_CLIENT_DISPLAY_FLAG", "SUPER_GM_REQ", "SUPER_GM_SELECT_REQ", "USER_CLIENT_INFO_REQ", "RSA_DATA_REQ", "GET_RSA_DATA_REQ", "CHANGE_PLAYER_NAME_REQ", "NOTIFY_SERVER_TIREN_REQ", "SERVER_HAND_CLIENT_100_RES", "SERVER_HAND_CLIENT_1_RES", "SERVER_HAND_CLIENT_2_RES", "SERVER_HAND_CLIENT_3_RES", "SERVER_HAND_CLIENT_4_RES", "SERVER_HAND_CLIENT_5_RES", "SERVER_HAND_CLIENT_6_RES", "SERVER_HAND_CLIENT_7_RES", "SERVER_HAND_CLIENT_8_RES", "SERVER_HAND_CLIENT_9_RES", "SERVER_HAND_CLIENT_10_RES", "SERVER_HAND_CLIENT_11_RES", "SERVER_HAND_CLIENT_12_RES", "SERVER_HAND_CLIENT_13_RES", "SERVER_HAND_CLIENT_14_RES", "SERVER_HAND_CLIENT_15_RES", "SERVER_HAND_CLIENT_16_RES", "SERVER_HAND_CLIENT_17_RES", "SERVER_HAND_CLIENT_18_RES", "SERVER_HAND_CLIENT_19_RES", "SERVER_HAND_CLIENT_20_RES", "SERVER_HAND_CLIENT_21_RES", "SERVER_HAND_CLIENT_22_RES", "SERVER_HAND_CLIENT_23_RES", "SERVER_HAND_CLIENT_24_RES", "SERVER_HAND_CLIENT_25_RES", "SERVER_HAND_CLIENT_26_RES", "SERVER_HAND_CLIENT_27_RES", "SERVER_HAND_CLIENT_28_RES", "SERVER_HAND_CLIENT_29_RES", "SERVER_HAND_CLIENT_30_RES", "SERVER_HAND_CLIENT_31_RES", "SERVER_HAND_CLIENT_32_RES", "SERVER_HAND_CLIENT_33_RES", "SERVER_HAND_CLIENT_34_RES", "SERVER_HAND_CLIENT_35_RES", "SERVER_HAND_CLIENT_36_RES", "SERVER_HAND_CLIENT_37_RES", "SERVER_HAND_CLIENT_38_RES", "SERVER_HAND_CLIENT_39_RES", "SERVER_HAND_CLIENT_40_RES", "SERVER_HAND_CLIENT_41_RES", "SERVER_HAND_CLIENT_42_RES", "SERVER_HAND_CLIENT_43_RES", "SERVER_HAND_CLIENT_44_RES", "SERVER_HAND_CLIENT_45_RES", "SERVER_HAND_CLIENT_46_RES", "SERVER_HAND_CLIENT_47_RES", "SERVER_HAND_CLIENT_48_RES", "SERVER_HAND_CLIENT_49_RES", "SERVER_HAND_CLIENT_50_RES", "SERVER_HAND_CLIENT_51_RES", "SERVER_HAND_CLIENT_52_RES", "SERVER_HAND_CLIENT_53_RES", "SERVER_HAND_CLIENT_54_RES", "SERVER_HAND_CLIENT_55_RES", "SERVER_HAND_CLIENT_56_RES", "SERVER_HAND_CLIENT_57_RES", "SERVER_HAND_CLIENT_58_RES", "SERVER_HAND_CLIENT_59_RES", "SERVER_HAND_CLIENT_60_RES", "SERVER_HAND_CLIENT_61_RES", "SERVER_HAND_CLIENT_62_RES", "SERVER_HAND_CLIENT_63_RES", "SERVER_HAND_CLIENT_64_RES", "SERVER_HAND_CLIENT_65_RES", "SERVER_HAND_CLIENT_66_RES", "SERVER_HAND_CLIENT_67_RES", "SERVER_HAND_CLIENT_68_RES", "SERVER_HAND_CLIENT_69_RES", "SERVER_HAND_CLIENT_70_RES", "SERVER_HAND_CLIENT_71_RES", "SERVER_HAND_CLIENT_72_RES", "SERVER_HAND_CLIENT_73_RES", "SERVER_HAND_CLIENT_74_RES", "SERVER_HAND_CLIENT_75_RES", "SERVER_HAND_CLIENT_76_RES", "SERVER_HAND_CLIENT_77_RES", "SERVER_HAND_CLIENT_78_RES", "SERVER_HAND_CLIENT_79_RES", "SERVER_HAND_CLIENT_80_RES", "SERVER_HAND_CLIENT_81_RES", "SERVER_HAND_CLIENT_82_RES", "SERVER_HAND_CLIENT_83_RES", "SERVER_HAND_CLIENT_84_RES", "SERVER_HAND_CLIENT_85_RES", "SERVER_HAND_CLIENT_86_RES", "SERVER_HAND_CLIENT_87_RES", "SERVER_HAND_CLIENT_88_RES", "SERVER_HAND_CLIENT_89_RES", "SERVER_HAND_CLIENT_90_RES", "SERVER_HAND_CLIENT_91_RES", "SERVER_HAND_CLIENT_92_RES", "SERVER_HAND_CLIENT_93_RES", "SERVER_HAND_CLIENT_94_RES", "SERVER_HAND_CLIENT_95_RES", "SERVER_HAND_CLIENT_96_RES", "SERVER_HAND_CLIENT_97_RES", "SERVER_HAND_CLIENT_98_RES", "SERVER_HAND_CLIENT_99_RES", "GET_PHONE_INFO_1_RES", "GET_PHONE_INFO_2_RES", "GET_PHONE_INFO_3_RES", "GET_PHONE_INFO_4_RES", "GET_PHONE_INFO_5_RES", "GET_PHONE_INFO_6_RES", "GET_PHONE_INFO_7_RES", "GET_PHONE_INFO_8_RES", "GET_PHONE_INFO_9_RES", "GET_PHONE_INFO_10_RES", "GET_PHONE_INFO_11_RES", "GET_CLIENT_INFO_RES", "GET_CLIENT_INFO_1_RES", "GET_CLIENT_INFO_2_RES", "GET_CLIENT_INFO_3_RES", "GET_CLIENT_INFO_4_RES", "GET_CLIENT_INFO_5_RES", "GET_CLIENT_INFO_6_RES", "GET_CLIENT_INFO_7_RES", "GET_CLIENT_INFO_8_RES", "GET_CLIENT_INFO_9_RES", "TRY_GETPLAYERINFO_RES", "WAIT_FOR_OTHER_RES", "GET_REWARD_2_PLAYER_RES", "REQUESTBUY_GET_ENTITY_INFO_RES", "PLAYER_SOUL_RES", "CARD_TRYSAVING_RES", "GANG_WAREHOUSE_JOURNAL_RES", "GET_WAREHOUSE_RES", "QUERY__GETAUTOBACK_RES", "GET_ZONGPAI_NAME_RES", "TRY_LEAVE_ZONGPAI_RES", "REBEL_EVICT_NEW_RES", "GET_PLAYERTITLE_RES", "MARRIAGE_TRY_BEQSTART_RES", "MARRIAGE_GUESTNEW_OVER_RES", "MARRIAGE_HUNLI_RES", "COUNTRY_COMMENDCANCEL_RES", "COUNTRY_NEWQIUJIN_RES", "GET_COUNTRYJINKU_RES", "CAVE_NEWBUILDING_RES", "CAVE_FIELD_RES", "CAVE_NEW_PET_RES", "CAVE_TRY_SCHEDULE_RES", "CAVE_SEND_COUNTYLIST_RES", "PLAYER_NEW_LEVELUP_RES", "CLEAN_FRIEND_LIST_RES", "DO_ACTIVITY_NEW_RES", "REF_TESK_LIST_RES", "DELTE_PET_INFO_RES", "MARRIAGE_DOACTIVITY_RES", "LA_FRIEND_RES", "TRY_NEWFRIEND_LIST_RES", "QINGQIU_PET_INFO_RES", "REMOVE_ACTIVITY_NEW_RES", "TRY_LEAVE_GAME_RES", "GET_TESK_LIST_RES", "GET_GAME_PALAYERNAME_RES", "GET_ACTIVITY_JOINIDS_RES", "QUERY_GAMENAMES_RES", "GET_PET_NBWINFO_RES", "CLONE_FRIEND_LIST_RES", "QUERY_OTHERPLAYER_PET_MSG_RES", "CSR_GET_PLAYER_RES", "HAVE_OTHERNEW_INFO_RES", "SHANCHU_FRIENDLIST_RES", "QUERY_TESK_LIST_RES", "CL_HORSE_INFO_RES", "CL_NEWPET_MSG_RES", "GET_ACTIVITY_NEW_RES", "DO_SOME_RES", "TY_PET_RES", "EQUIPMENT_GET_MSG_RES", "EQU_NEW_EQUIPMENT_RES", "DELETE_FRIEND_LIST_RES", "DO_PET_EQUIPMENT_RES", "QILING_NEW_INFO_RES", "HORSE_QILING_INFO_RES", "HORSE_EQUIPMENT_QILING_RES", "PET_EQU_QILING_RES", "MARRIAGE_TRYACTIVITY_RES", "PET_TRY_QILING_RES", "PLAYER_CLEAN_QILINGLIST_RES", "DELETE_TESK_LIST_RES", "GET_HEIMINGDAI_NEWLIST_RES", "QUERY_CHOURENLIST_RES", "QINCHU_PLAYER_RES", "REMOVE_FRIEND_LIST_RES", "TRY_REMOVE_CHOUREN_RES", "REMOVE_CHOUREN_RES", "DELETE_TASK_LIST_RES", "PLAYER_TO_PLAYER_DEAL_RES", "AUCTION_NEW_LIST_MSG_RES", "REQUEST_BUY_PLAYER_INFO_RES", "BOOTHER_PLAYER_MSGNAME_RES", "BOOTHER_MSG_CLEAN_RES", "TRY_REQUESTBUY_CLEAN_ALL_RES", "SCHOOL_INFONAMES_RES", "PET_NEW_LEVELUP_RES", "VALIDATE_ASK_NEW_RES", "JINGLIAN_NEW_TRY_RES", "JINGLIAN_NEW_DO_RES", "JINGLIAN_PET_RES", "SEE_NEWFRIEND_LIST_RES", "EQU_PET_HUN_RES", "PET_ADD_HUNPO_RES", "PET_ADD_SHENGMINGVALUE_RES", "HORSE_REMOVE_HUNPO_RES", "PET_REMOVE_HUNPO_RES", "PLAYER_CLEAN_HORSEHUNLIANG_RES", "GET_NEW_LEVELUP_RES", "DO_HOSEE2OTHER_RES", "TRYDELETE_PET_INFO_RES", "HAHA_ACTIVITY_MSG_RES", "VALIDATE_NEW_RES", "VALIDATE_ANDSWER_NEW_RES", "PLAYER_ASK_TO_OTHWE_RES", "GA_GET_SOME_RES", "OTHER_PET_LEVELUP_RES", "MY_OTHER_FRIEDN_RES", "DOSOME_SB_MSG_RES", "IOS_CLIENT_MSG_REQ", "QUERY_NEW_PLAYER_REQ", "VIP_LOTTERY_CLICK_REQ" };
	}

	public String getName() {
		return "SpriteSubSystem";
	}

	public ResponseMessage handleReqeustMessage(Connection conn, RequestMessage message) throws ConnectionException, Exception {
		long startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		Player player = (Player) conn.getAttachmentData("Player");
		if (player != null) {

			// qq9月5,6,7活动，小于当前封印30级，三天不上线
			player.setLastRequestTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());

			try {
				WaiguaManager wm = WaiguaManager.getInstance();
				if (wm.isOpenning()) {
					wm.notifyPlayerMessage(player, message.getTypeDescription(), message.getSequnceNum());
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		String username = (String) conn.getAttachmentData(SecuritySubSystem.USERNAME);

		
		if (message instanceof SUPER_GM_REQ) {

			for (int i = 0; i < GM_USERNAMES.length; i++) {
				if (GM_USERNAMES[i].equals(username)) {
					return new SUPER_GM_RES(message.getSequnceNum(), new String[] { "禁言", "封号" });

				}
			}

		} else if (message instanceof SUPER_GM_SELECT_REQ) {
			SUPER_GM_SELECT_REQ req = (SUPER_GM_SELECT_REQ) message;
			long pid = req.getPlayerId();
			String o = req.getOptions();
			boolean isGM = false;
			for (int i = 0; i < GM_USERNAMES.length; i++) {
				if (GM_USERNAMES[i].equals(username)) {
					isGM = true;
				}
			}
			if (isGM) {
				Player oPlayer = PlayerManager.getInstance().getPlayer(pid);
				if (oPlayer != null) {
					if (o != null && o.equals("禁言")) {
						ChatMessageService.getInstance().banPlayer(oPlayer.getId(), 2);

						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, oPlayer.getName() + "已被禁言2个小时！");
						player.addMessageToRightBag(hreq);
					} else if (o != null && o.equals("封号")) {
						DENY_USER_REQ req2 = new DENY_USER_REQ(GameMessageFactory.nextSequnceNum(), "", oPlayer.getUsername(), "被GM手动封号", username + "通过客户端封号", false, true, false, 0, 24);
						MieshiGatewayClientService.getInstance().sendMessageToGateway(req2);

						if (oPlayer.getConn() != null) oPlayer.getConn().close();

						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, oPlayer.getName() + "已被封号2个小时！");
						player.addMessageToRightBag(hreq);
					} else {
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, "操作不能识别！");
						player.addMessageToRightBag(hreq);
					}
				}
			} else {
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, "您不是GM，不能进行此操作！");
				player.addMessageToRightBag(hreq);
			}

		} else if (message instanceof SET_CLIENT_DISPLAY_FLAG) {
			SET_CLIENT_DISPLAY_FLAG flag = (SET_CLIENT_DISPLAY_FLAG) message;
			if (player != null) {
				if (player.getCurrentGame() == null) {
					if (Game.isHiddenOpen) {
						if (flag.getShowPlayer() == false) player.hiddenAllPlayer = true;
						else player.hiddenAllPlayer = false;

						if (flag.getShowSameCountryPlayer() == false) player.hiddenSameCountryPlayer = true;
						else player.hiddenSameCountryPlayer = false;

						if (flag.getShowChat() == false) player.hiddenChatMessage = true;
						else player.hiddenChatMessage = false;

						if (player.hiddenAllPlayer || player.hiddenSameCountryPlayer || player.hiddenChatMessage) {
							player.needToNotifyAboutHidden = true;
						}
					} else {
						player.hiddenAllPlayer = false;
						player.hiddenSameCountryPlayer = false;
						player.hiddenChatMessage = false;
					}
				} else {
					player.getCurrentGame().messageQueue.push(new PlayerMessagePair(player, message, ""));
				}
			} else {
				if (Game.logger.isDebugEnabled()) {
					Game.logger.debug("[收到玩家请求屏蔽功能] [服务器屏蔽功能未开启]");
				}
			}
		} else if (message instanceof SET_QUEUE_READYNUM_REQ) {
		} else if (message instanceof ACTIVE_TEST_REQ) {
			ACTIVE_TEST_REQ req = (ACTIVE_TEST_REQ) message;
			return new ACTIVE_TEST_RES(req.getSequnceNum());
		} else if (message instanceof CREATE_PLAYER_REQ) {
			CREATE_PLAYER_REQ req = (CREATE_PLAYER_REQ) message;
			CREATE_PLAYER_RES res = null;
			if (UnitServerFunctionManager.needCloseFunctuin(Function.创建角色)) {
				res = new CREATE_PLAYER_RES(req.getSequnceNum(), (byte) 3, Translate.合服功能关闭提示, false);
				return res;
			}
			if (GamePlayerManager.LimitCreateNewPlayer) {
				res = new CREATE_PLAYER_RES(req.getSequnceNum(), (byte) 3, 停止注册提示, false);
				return res;
			}
			if(GamePlayerManager.limitCreateServers.contains(GameConstants.getInstance().getServerName())){
				res = new CREATE_PLAYER_RES(req.getSequnceNum(), (byte) 3, 停止注册提示, false);
				return res;
			}
			try {
				if (CloseFunctionManager.isCloseRegisterPlayer()) {
					res = new CREATE_PLAYER_RES(req.getSequnceNum(), (byte) 3, "該伺服器已滿，停止創建角色，請您在最新服進行遊戲。", false);
					return res;
				}
			} catch (Exception e1) {
				logger.error("判断是否可以创建角色异常", e1);
			}
			{
				// 创建新加的代码.原则:1在预创建的状态,2在预创建时间内,3创建的国家未满
				if (PlatformManager.getInstance().isPlatformOf(Platform.官方)) {
					if (!CoreSubSystem.getInstance().judgeCanEnterServer()) {
						// 是预创建的服务器
						if (!CoreSubSystem.getInstance().timeCanBeforehandCreate(startTime)) {

							CoreSubSystem.logger.warn("是预创建的服务器,但是时间已经过了,不能创建角色");

							res = new CREATE_PLAYER_RES(req.getSequnceNum(), (byte) 30, "亲爱的仙友，预创建时间已结束，无法进行预创建操作，服务器开启时间请留意官网与论坛公告!", false);
							return res;
						}

						int countryMaxCreateNum = CoreSubSystem.getInstance().getBeforehandCreateCountryNum()[req.getCountry()];
						long countryCurrCreateNum = ((GamePlayerManager) GamePlayerManager.getInstance()).em.count(Player.class, "country = ?", new Object[] { req.getCountry() });
						CoreSubSystem.logger.warn("[预创建角色] [选择国家:" + req.getCountry() + "] [人数上限:" + countryMaxCreateNum + "] [已经创建角色:" + countryCurrCreateNum + "]");
						if (countryCurrCreateNum >= countryMaxCreateNum) {// 预创建的人数满了
							res = new CREATE_PLAYER_RES(req.getSequnceNum(), (byte) 3, "亲爱的仙友,您选择的当前国家" + CountryManager.得到国家名(req.getCountry()) + "已满,请选择其他国家创建或到服务器开放时再进行创建!", false);
							return res;
						}
					}
				}

			}
			String name = req.getName().trim();
			if (username != null) {
				// 判断角色名是否合法
				WordFilter filter = WordFilter.getInstance();
				boolean valid = false;
				if (name != null && name.length() > 0) {
					// 角色名,对于0级词汇,完全过滤;对于1级词汇,等于过滤
					valid = filter.cvalid(name, 0) && filter.evalid(name, 1) && prefixValid(name) && tagValid(name) && gmValid(name);
				}
				if (!valid) {
					res = new CREATE_PLAYER_RES(req.getSequnceNum(), (byte) 3, Translate.角色名称不合法, false);
				} else {
					int num = 0;
					try {
						num = playerManager.getAmountOfPlayers(username);
					} catch (Exception ex) {
						System.out.println(ex.getMessage());
					}
					if (num >= MAX_PLAYER_PER_USER) {
						res = new CREATE_PLAYER_RES(req.getSequnceNum(), (byte) 2, Translate.您已经有了一个角色, false);
					} else {
						byte careerId = req.getCareer();
						byte sex = req.getSex();
						if (req.getSex() != 0 && req.getSex() != 1) {
							res = new CREATE_PLAYER_RES(req.getSequnceNum(), (byte) 4, Translate.text_5915, false);
						} else {
							try {
								Player p = playerManager.createPlayer(username, name, req.getRace(), req.getSex(), req.getCountry(), (byte) 1, req.getCareer());
								if (p != null) {
									Passport passport = (Passport) conn.getAttachment();
									p.setPassport(passport);

									GameStatClientService.statPlayerCreate(p);

									{
										if (p.getPassport() != null && p.getPassport().getRegisterChannel() != null) {
											Game.logger.warn("[玩家注册角色1] [" + p.getLogString() + "] [" + p.getPassport().getLastLoginChannel() + "]");
										} else {
											Game.logger.warn("[玩家注册角色2] [" + p.getLogString() + "]");
										}

										try {
											if (p.getPassport() != null && p.getPassport().getRegisterChannel() != null) {
												ArticleManager am = ArticleManager.getInstance();
												Game.logger.warn("[玩家注册角色] [" + p.getLogString() + "] [registerChannel:" + p.getPassport().getRegisterChannel() + "] [lastLoginChannel:" + p.getPassport().getLastLoginChannel() + "]");
												if (am != null) {
													if (p.getPassport().getRegisterChannel().toLowerCase().indexOf("360zhushou_mieshi") == 0) {
														Article a = am.getArticle("360特权礼包");
														if (a != null) {
															ArticleEntityManager aem = ArticleEntityManager.getInstance();
															ArticleEntity ae = aem.createEntity(a, true, ArticleEntityManager.CREATE_REASON_huodong_libao, p, a.getColorType(), 1, true);
															if (ae != null) {
																p.putToKnapsacks(ae, "360特权礼包");
															}
														}
													}

													if (p.getPassport().getRegisterChannel().toLowerCase().indexOf("360sdk_mieshi") == 0) {
														Article a = am.getArticle("360特权礼包");
														if (a != null) {
															ArticleEntityManager aem = ArticleEntityManager.getInstance();
															ArticleEntity ae = aem.createEntity(a, true, ArticleEntityManager.CREATE_REASON_huodong_libao, p, a.getColorType(), 1, true);
															if (ae != null) {
																p.putToKnapsacks(ae, "360特权礼包");
															}
														}
													}

													if (p.getPassport().getRegisterChannel().toLowerCase().indexOf("360jiekou_mieshi") == 0) {
														Article a = am.getArticle("360特权礼包");
														if (a != null) {
															ArticleEntityManager aem = ArticleEntityManager.getInstance();
															ArticleEntity ae = aem.createEntity(a, true, ArticleEntityManager.CREATE_REASON_huodong_libao, p, a.getColorType(), 1, true);
															if (ae != null) {
																p.putToKnapsacks(ae, "360特权礼包");
															}
														}
													}
												}
											}
										} catch (Exception ex) {
											ex.printStackTrace();
										}

										try {
											if (p.getPassport() != null && p.getPassport().getRegisterChannel() != null) {
												ArticleManager am = ArticleManager.getInstance();
												if (am != null) {
													if (p.getPassport().getRegisterChannel().toLowerCase().indexOf("lenovo_mieshi") == 0) {
														Article a = am.getArticle("乐享其成");
														if (a != null) {
															ArticleEntityManager aem = ArticleEntityManager.getInstance();
															ArticleEntity ae = aem.createEntity(a, true, ArticleEntityManager.CREATE_REASON_huodong_libao, p, a.getColorType(), 1, true);
															if (ae != null) {
																p.putToKnapsacks(ae, "乐享其成");
															}
														}
													}
												}
											}
										} catch (Exception ex) {
											ex.printStackTrace();
										}

										try {
											GameConstants gc = GameConstants.getInstance();
											if (gc != null) {
												if (gc.getServerName().equals("北冥佛光")) {
													if (p.getPassport() != null && p.getPassport().getRegisterChannel() != null) {
														ArticleManager am = ArticleManager.getInstance();
														if (am != null) {
															if (p.getPassport().getRegisterChannel().toLowerCase().indexOf("189store") == 0) {
																Article a = am.getArticle("天翼仙囊");
																if (a != null) {
																	ArticleEntityManager aem = ArticleEntityManager.getInstance();
																	ArticleEntity ae = aem.createEntity(a, true, ArticleEntityManager.CREATE_REASON_huodong_libao, p, a.getColorType(), 1, true);
																	if (ae != null) {
																		p.putToKnapsacks(ae, "天翼仙囊活动礼包");
																	}
																}
															} else if (p.getPassport().getRegisterChannel().toLowerCase().indexOf("dcn") == 0) {
																Article a = am.getArticle("萌小熊礼包");
																if (a != null) {
																	ArticleEntityManager aem = ArticleEntityManager.getInstance();
																	ArticleEntity ae = aem.createEntity(a, true, ArticleEntityManager.CREATE_REASON_huodong_libao, p, a.getColorType(), 1, true);
																	if (ae != null) {
																		p.putToKnapsacks(ae, "dcn活动礼包");
																	}
																}
															} else if (p.getPassport().getRegisterChannel().equals("PPZHUSHOU_MIESHI")) {
																Article a = am.getArticle("助手锦囊");
																if (a != null) {
																	ArticleEntityManager aem = ArticleEntityManager.getInstance();
																	ArticleEntity ae = aem.createEntity(a, true, ArticleEntityManager.CREATE_REASON_huodong_libao, p, a.getColorType(), 1, true);
																	if (ae != null) {
																		p.putToKnapsacks(ae, "PP活动礼包");
																	}
																}
															} else if (p.getPassport().getRegisterChannel().equals("TBTSDK_MIESHI")) {
																Article a = am.getArticle("步步高升锦囊");
																if (a != null) {
																	ArticleEntityManager aem = ArticleEntityManager.getInstance();
																	ArticleEntity ae = aem.createEntity(a, true, ArticleEntityManager.CREATE_REASON_huodong_libao, p, a.getColorType(), 1, true);
																	if (ae != null) {
																		p.putToKnapsacks(ae, "TBT活动礼包");
																	}
																}
															}
														}
													}
												}
											}
										} catch (Exception ex) {
											ex.printStackTrace();
										}

										try {
											GameConstants gc = GameConstants.getInstance();
											if (gc != null) {
												if (gc.getServerName().equals("明空梵天")) {
													if (p.getPassport() != null && p.getPassport().getRegisterChannel() != null) {
														ArticleManager am = ArticleManager.getInstance();
														if (am != null) {
															if (p.getPassport().getRegisterChannel().toLowerCase().indexOf("dcn") == 0) {
																Article a = am.getArticle("萌小熊礼包");
																if (a != null) {
																	ArticleEntityManager aem = ArticleEntityManager.getInstance();
																	ArticleEntity ae = aem.createEntity(a, true, ArticleEntityManager.CREATE_REASON_huodong_libao, p, a.getColorType(), 1, true);
																	if (ae != null) {
																		p.putToKnapsacks(ae, "dcn活动礼包");
																	}
																}
															} else if (p.getPassport().getRegisterChannel().toLowerCase().indexOf("xiaomiyx") == 0) {
																Article a = am.getArticle("步步高升锦囊");
																if (a != null) {
																	ArticleEntityManager aem = ArticleEntityManager.getInstance();
																	ArticleEntity ae = aem.createEntity(a, true, ArticleEntityManager.CREATE_REASON_huodong_libao, p, a.getColorType(), 1, true);
																	if (ae != null) {
																		p.putToKnapsacks(ae, "xiaomiyx活动礼包");
																	}
																}
															}
														}
													}
												}
											}
										} catch (Exception ex) {
											ex.printStackTrace();
										}

										try {
											GameConstants gc = GameConstants.getInstance();
											if (gc != null) {
												if (gc.getServerName().equals("七宝莲台")) {
													if (p.getPassport() != null && p.getPassport().getRegisterChannel() != null) {
														ArticleManager am = ArticleManager.getInstance();
														if (am != null) {
															if (p.getPassport().getRegisterChannel().toLowerCase().indexOf("dcn") == 0) {
																Article a = am.getArticle("萌小熊礼包");
																if (a != null) {
																	ArticleEntityManager aem = ArticleEntityManager.getInstance();
																	ArticleEntity ae = aem.createEntity(a, true, ArticleEntityManager.CREATE_REASON_huodong_libao, p, a.getColorType(), 1, true);
																	if (ae != null) {
																		p.putToKnapsacks(ae, "dcn活动礼包");
																	}
																}
															} else if (p.getPassport().getRegisterChannel().equals("PPZHUSHOU_MIESHI")) {
																Article a = am.getArticle("助手锦囊");
																if (a != null) {
																	ArticleEntityManager aem = ArticleEntityManager.getInstance();
																	ArticleEntity ae = aem.createEntity(a, true, ArticleEntityManager.CREATE_REASON_huodong_libao, p, a.getColorType(), 1, true);
																	if (ae != null) {
																		p.putToKnapsacks(ae, "PP活动礼包");
																	}
																}
															} else if (p.getPassport().getRegisterChannel().equals("TBTSDK_MIESHI")) {
																Article a = am.getArticle("步步高升锦囊");
																if (a != null) {
																	ArticleEntityManager aem = ArticleEntityManager.getInstance();
																	ArticleEntity ae = aem.createEntity(a, true, ArticleEntityManager.CREATE_REASON_huodong_libao, p, a.getColorType(), 1, true);
																	if (ae != null) {
																		p.putToKnapsacks(ae, "TBT活动礼包");
																	}
																}
															}
														}
													}
												}
											}
										} catch (Exception ex) {
											ex.printStackTrace();
										}

										try {
											GameConstants gc = GameConstants.getInstance();
											if (gc != null) {
												if (gc.getServerName().equals("冲霄云楼") || gc.getServerName().equals("王者之域") || gc.getServerName().equals("独战群神") || gc.getServerName().equals("傲剑凌云") || gc.getServerName().equals("百花仙谷") || gc.getServerName().equals("金蛇圣殿") || gc.getServerName().equals("仙界至尊") || gc.getServerName().equals("雄霸天下")) {
													if (p.getPassport() != null && p.getPassport().getRegisterChannel() != null) {
														ArticleManager am = ArticleManager.getInstance();
														if (am != null) {
															if (p.getPassport().getRegisterChannel().toLowerCase().indexOf("dcn") == 0) {
																Article a = am.getArticle("萌小熊礼包");
																if (a != null) {
																	ArticleEntityManager aem = ArticleEntityManager.getInstance();
																	ArticleEntity ae = aem.createEntity(a, true, ArticleEntityManager.CREATE_REASON_huodong_libao, p, a.getColorType(), 1, true);
																	if (ae != null) {
																		p.putToKnapsacks(ae, "dcn活动礼包");
																	}
																}
															} else if (p.getPassport().getRegisterChannel().toLowerCase().indexOf("189store") == 0) {
																Article a = am.getArticle("天翼仙囊");
																if (a != null) {
																	ArticleEntityManager aem = ArticleEntityManager.getInstance();
																	ArticleEntity ae = aem.createEntity(a, true, ArticleEntityManager.CREATE_REASON_huodong_libao, p, a.getColorType(), 1, true);
																	if (ae != null) {
																		p.putToKnapsacks(ae, "天翼仙囊活动礼包");
																	}
																}
															}
														}
													}
												}
											}
										} catch (Exception ex) {
											ex.printStackTrace();
										}
									}

									{
										if (GreenServerManager.isBindYinZiServer()) {
											if (GreenServerManager.isUseBindProp) {
												Article a = ArticleManager.getInstance().getArticle(Translate.银票);
												if (a != null) {
													ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.绿色服, p, a.getColorType(), 1, true);
													if (ae != null) {
														p.putToKnapsacks(ae, "绿色服银票");
													}
												}
											}
										}
									}

									if (ArticleManager.logger.isWarnEnabled()) ArticleManager.logger.warn("[{}] [channeItemKey:{}] [{}] [{}] [{}]", new Object[] { GameConstants.getInstance().getServerName(), (passport != null ? passport.getRegisterChannel() : ""), p.getUsername(), p.getId(), p.getName() });

									// 发送通知给boss
									// Object[] result = bossService.playerCreated(p.getUsername(), p.getName(), p.getCountry(), new Date(), req.getRecommendid());

									boolean needBind = false;
									//
									// if (result.length >= 4) {
									// boolean recommend = (Boolean) result[0];
									// String recServer = (String) result[1];
									// String recplayername = (String) result[2];
									// boolean mobileBinded = (Boolean) result[3];
									// needBind = recommend && !mobileBinded;
									// if (recplayername.length() > 0 && gameConstants.getServerName().equals(recServer)) {
									// Player recplayer = playerManager.getPlayer(recplayername);
									// if (recplayer != null) {
									// if (recplayer.getCountry() == p.getCountry()) {
									// SocialManager.getInstance().addFriend(recplayer, p);
									// SocialManager.getInstance().addFriend(p, recplayer);
									// }
									// }
									// }
									// }
									res = new CREATE_PLAYER_RES(req.getSequnceNum(), (byte) 0, Translate.text_5919, needBind);

									if (logger.isWarnEnabled()) logger.warn("[{}] [create_player] [success] [{}] [{}] [{}] [needBindMobile:{}] [{}ms]", new Object[] { getName(), username, name, p.getId(), needBind, (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime) });

									// 设置第一次进入游戏标记
									p.setRecentlyCreatedButNotEnterGameFlag(true);
									String channelName = "";
									String channelKey = "";
									if (passport != null) {
										channelName = passport.getRegisterChannel();
										channelKey = passport.getRegisterChannel();
									}
									if (playerInfoLog.isInfoEnabled()) {
										// playerInfoLog.info("[创建角色] [帐号：" + p.getUsername() + "] [渠道：" + channelName + "] [渠道ID：" + channelKey + "] [姓名：" + p.getName() + "] [ID："
										// + p.getId() + "] [性别：" + (p.getSex() == 0 ? "男" : "女") + "] [门派：" + CareerManager.careerNames[p.getCareer()] + "]");
										if (playerInfoLog.isInfoEnabled()) playerInfoLog.info("[创建角色] [帐号：{}] [渠道：{}] [渠道ID：{}] [姓名：{}] [ID：{}] [性别：{}] [门派：{}]", new Object[] { p.getUsername(), channelName, channelKey, p.getName(), p.getId(), (p.getSex() == 0 ? "男" : "女"), CareerManager.careerNames[p.getCareer()] });
									}

									return res;
								} else {
									res = new CREATE_PLAYER_RES(req.getSequnceNum(), (byte) 4, Translate.text_5920, false);
								}
							} catch (Exception e) {
								if (logger.isWarnEnabled()) logger.warn("[" + getName() + "] [create_player] [fail] [" + username + "] [" + name + "] [--] [" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime) + "ms]", e);
								res = new CREATE_PLAYER_RES(req.getSequnceNum(), (byte) 4, Translate.text_5921, false);
							}
						}
					}
				}
			} else {
				res = new CREATE_PLAYER_RES(req.getSequnceNum(), (byte) 3, Translate.text_5922, false);
			}
			if (res.getResult() > 0) {
				if (logger.isWarnEnabled()) logger.warn("[{}] [create_player] [failed] [{}] [{}] [{}] [{}ms]", new Object[] { getName(), res.getResultString(), username, name, (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime) });
			}
			return res;
		} else if (message instanceof PLAYER_UPDATE_REQ) {
			PLAYER_UPDATE_REQ req = (PLAYER_UPDATE_REQ) message;
			PLAYER_UPDATE_RES res = null;
			if (username != null) {
				String name = req.getPlayername();
				long id = req.getPlayer();
				Player pp = playerManager.getPlayer(id);
				String oldname = pp.getName();
				try {
					playerManager.userModifyPlayer(pp, name, req.getSex(), req.getCareer());
					res = new PLAYER_UPDATE_RES(req.getSequnceNum(), (byte) 0, "");
				} catch (Exception e) {
					res = new PLAYER_UPDATE_RES(req.getSequnceNum(), (byte) 1, e.getMessage());
				}
				if (res.getResult() == 0) {
				}
				if (logger.isWarnEnabled()) logger.warn("[{}] [modify_player] [{}] [{}] [{}] [{}] [oldname:{}] [{}ms]", new Object[] { getName(), (res.getResult() == 0 ? "SUCC" : "FAILED"), res.getDescription(), username, name, oldname, (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime) });
				return res;
			} else {
				res = new PLAYER_UPDATE_RES(req.getSequnceNum(), (byte) 1, Translate.text_5922);
			}
		} else if (message instanceof REMOVE_PLAYER_REQ) {
			REMOVE_PLAYER_REQ req = (REMOVE_PLAYER_REQ) message;
			if (username != null) {
				Passport pp = BossClientService.getInstance().getPassportByUserName(username);
				if (pp != null) {
					if (pp.getStatus() == Passport.STATUS_PAUSED) {
						REMOVE_PLAYER_RES res = new REMOVE_PLAYER_RES(req.getSequnceNum(), (byte) 1, Translate.text_5923);
						return res;
					}
				}
			}
			String name = req.getName();
			if (username == null) {
				// logger.warn("[删除角色] [失败] [" + username + "] [" + name + "] [" + gameConstants.getServerName() + "] [用户还没有登录]");
				if (logger.isWarnEnabled()) logger.warn("[删除角色] [失败] [{}] [{}] [{}] [用户还没有登录]", new Object[] { username, name, gameConstants.getServerName() });

				REMOVE_PLAYER_RES res = new REMOVE_PLAYER_RES(req.getSequnceNum(), (byte) 1, Translate.text_5924);
				return res;
			}

			Player removePlayer = null;
			int level = 0;
			byte career = 0;
			try {
				removePlayer = playerManager.getPlayer(name);
				level = removePlayer.getLevel();
				career = removePlayer.getCareer();
			} catch (Exception e) {
				if (logger.isWarnEnabled()) logger.warn("[删除角色] [获取角色] [失败] [" + conn.getName() + "] [" + name + "]", e);
			}

			if (removePlayer == null) {

				// logger.warn("[删除角色] [失败] [" + username + "] [" + name + "] [" + gameConstants.getServerName() + "] [角色不存在]");
				if (logger.isWarnEnabled()) logger.warn("[删除角色] [失败] [{}] [{}] [{}] [角色不存在]", new Object[] { username, name, gameConstants.getServerName() });

				REMOVE_PLAYER_RES res = new REMOVE_PLAYER_RES(req.getSequnceNum(), (byte) 1, Translate.text_5925 + name + Translate.text_5926);
				return res;
			} else if (removePlayer.getUsername().equals(username) == false) {

				// logger.warn("[删除角色] [失败] [登录用户：" + username + "] [角色用户：" + removePlayer.getUsername() + "] [" + name + "] [" + gameConstants.getServerName() +
				// "] [可能是外挂，用户名不匹配]");
				if (logger.isWarnEnabled()) logger.warn("[删除角色] [失败] [登录用户：{}] [角色用户：{}] [{}] [{}] [可能是外挂，用户名不匹配]", new Object[] { username, removePlayer.getUsername(), name, gameConstants.getServerName() });

				REMOVE_PLAYER_RES res = new REMOVE_PLAYER_RES(req.getSequnceNum(), (byte) 1, Translate.text_5927);
				return res;
			}

			if (removePlayer.getConn() != null) {
				removePlayer.getConn().close();
			}

			playerManager.removePlayer(name);

			// try {
			// bossService.notifyRemovePlayer(username, name);
			// if (logger.isWarnEnabled()) logger.warn("[删除角色] [{}] [{}] [{}]", new Object[] { username, name, gameConstants.getServerName() });
			// } catch (Exception e) {
			// e.printStackTrace();
			// if (logger.isWarnEnabled()) logger.warn("[删除角色] [异常] [" + username + "] [" + name + "] [" + gameConstants.getServerName() + "]", e);
			// }
			// if (statClientService != null) {
			// PlayerCreateFlow flow = new PlayerCreateFlow();
			// flow.setDeleted(1);
			// flow.setPlayername(removePlayer.getName());
			// flow.setServer(gameConstants.getServerName());
			// flow.setUsername(removePlayer.getUsername());
			// statClientService.sendPlayerCreateFlow(flow);
			// }
			// 给网关服务器发送消息，更新用户信息
			REMOVE_PLAYER_RES res = new REMOVE_PLAYER_RES(req.getSequnceNum(), (byte) 0, Translate.text_5928 + name + Translate.text_5929);
			return res;

		} else if (message instanceof LEAVE_LINEUP_REQ) {
			LEAVE_LINEUP_REQ req = (LEAVE_LINEUP_REQ) message;
			if (username == null) {
				if (logger.isInfoEnabled()) logger.info("[离开排队] [失败] [--] [用户名为null] [用户还没有登录]");
			} else if (username.equals(req.getUsername()) == false) {
				if (logger.isInfoEnabled()) logger.info("[离开排队] [失败] [--] [用户名不匹配:(" + username + "/" + req.getUsername() + ")]");
			} else {
				EnterServerAgent agent = EnterServerAgent.getInstance();
				agent.cancelLineup(username);
				if (logger.isInfoEnabled()) logger.info("[离开排队] [成功] [" + username + "]");
			}
		} else if (message instanceof QUERY_PLAYER_REQ) {
			QUERY_PLAYER_REQ req = (QUERY_PLAYER_REQ) message;
			QUERY_PLAYER_RES res = null;
			try {
				// System.out.println("dao1");

				if (username == null) {
					// System.out.println("dao2");
					if (logger.isInfoEnabled()) logger.info("[获取玩家列表] [失败] [--] [用户名为null] [用户还没有登录]");

					res = new QUERY_PLAYER_RES(req.getSequnceNum(), new Player[0]);

				} else if (username.equals(req.getUsername()) == false) {
					// System.out.println("dao3");
					// logger.info("[获取玩家列表] [失败] [登录用户：" + username + "] [获取角色：" + req.getUsername() + "] [可能是外挂，获取其他用户的角色列表]");
					if (logger.isInfoEnabled()) logger.info("[获取玩家列表] [失败] [登录用户：{}] [获取角色：{}] [可能是外挂，获取其他用户的角色列表]", new Object[] { username, req.getUsername() });

					res = new QUERY_PLAYER_RES(req.getSequnceNum(), new Player[0]);

				} else {
					try {
						// System.out.println("username1:"+username+"--u2:"+req.getUsername());
						Passport passport = bossService.getPassportByUserName(username);
						// System.out.println("username:"+username+"--u2:"+req.getUsername()+"--passport:"+passport);
						if (passport != null && passport.getLastLoginChannel().contains("KUAIYONGSDK")) {
							username = passport.getUserName();
						}
						// System.out.println("username3:"+username+"--u2:"+req.getUsername()+"--passport:"+passport+"--nickname:"+(passport==null?"nul":passport.getNickName()));
					} catch (Exception e) {
						e.printStackTrace();
					}
					// System.out.println("dao4");
					Player[] ps = playerManager.getPlayerByUser(username);
					// System.out.println("dao5");

					if (ps != null && ps.length > 1) {
						Arrays.sort(ps, new Comparator<Player>() {

							@Override
							public int compare(Player o1, Player o2) {
								// TODO Auto-generated method stub
								if (o1.getQuitGameTime() > o2.getQuitGameTime()) {
									return -1;
								} else if (o1.getQuitGameTime() == o2.getQuitGameTime()) {
									return o2.getLevel() - o1.getLevel();
								} else {
									return 1;
								}

							}
						});
					}

					if (!CoreSubSystem.getInstance().judgeCanEnterServer()) {
						// System.out.println("dao6");
						if (ps.length == 0) {
							String description = "<f color='#FFFFFF'>欢迎您来到飘渺寻仙曲—</f><f color='#FF00'>" + gameConstants.getServerName() + "</f><f color='#FFFFFF'>，此服务器暂时未开放，只开放预创建功能，请仙友们快点抢注自己的名字吧！</f>";
							HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 20, description);
							GameNetworkFramework.getInstance().sendMessage(conn, hreq, description);
							// System.out.println("dao7");
						}
					}
					// System.out.println("dao8");
					String pnames[] = new String[ps.length];
					byte polcamps[] = new byte[ps.length];
					for (int i = 0; i < ps.length; i++) {
						pnames[i] = ps[i].getName();
						polcamps[i] = ps[i].getCountry();
					}
					// System.out.println("dao9");
					WordFilter filter = WordFilter.getInstance();
					if (pnames.length > 0) {
						// System.out.println("dao10");
						boolean needChangeName = false;
						List<Long> changeIds = new ArrayList<Long>();
						StringBuffer changeNameNotice = new StringBuffer("您的角色名与其他玩家重复,需要修改角色名再进入游戏,需要修改的角色名为:");
						// System.out.println("dao11");
						for (Player p : ps) {
							boolean valid = false;
							String name = p.getName();
							if (name != null && name.length() > 0) {// 这里是名字在屏蔽字里的 需要改名
								// 角色名,对于0级词汇,完全过滤;对于1级词汇,等于过滤
								valid = filter.cvalid(name, 0) && filter.evalid(name, 1) && prefixValid(name) && tagValid(name) && gmValid(name);
								logger.warn(p.getLogString() + "[角色名字过滤] [结果:" + valid + "]");
							}
							
							// System.out.println("dao12");
							if (!valid) {
								changeNameNotice = new StringBuffer("您的角色名存在非法字符，为了您能正常体验游戏请您修改角色名，需要修改的角色名为：");
								System.out.println("[角色名字过滤] ["+p.getLogString()+"] [valid:"+valid+"] [name:"+name+"]");
							}
							// System.out.println("dao13");
							if (UnitedServerManager.getInstance().needChangeName(p.getId()) || !valid) {
								changeNameNotice.append(p.getName()).append(".");
								needChangeName = true;
								changeIds.add(p.getId());
							}
							// System.out.println("dao14");
						}
						if (needChangeName) {// 需要修改用户
							// System.out.println("dao15");
							Long[] idArr = changeIds.toArray(new Long[0]);
							long[] send = new long[idArr.length];
							for (int i = 0; i < idArr.length; i++) {
								send[i] = idArr[i];
							}
							// System.out.println("dao16");
							NEED_CHANGE_NAME_REQ notice_req = new NEED_CHANGE_NAME_REQ(GameMessageFactory.nextSequnceNum(), changeNameNotice.toString(), send);
							// TODO how to send to client?
							GameNetworkFramework.getInstance().sendMessage(conn, notice_req, "");
							// conn.writeMessage(notice_req);
							logger.warn("[通知用户修改重名角色] [username:" + username + "] [要改名的角色:" + Arrays.toString(send) + "]");
						}
					}
					// System.out.println("dao17");
					Player players[] = ps;
					StringBuffer sb = new StringBuffer();
					for (int i = 0; i < players.length; i++) {
						if (players[i] != null) {
							String[] avatas = players[i].getAvata();
							boolean isChange = false;
							if (avatas != null) {
								for (String s : avatas) {
									if (s != null && (s.contains("chongwu_shihunxuelang04") || s.contains("xinchongwu_xiaoyang"))) {
										isChange = true;
									}
								}
							}
							if (isChange) {
								ResourceManager.getInstance().getAvata(players[i]);
								players[i].setHold(false);
							}
						}
						if (players[i].getLastGame() == null || players[i].getLastGame().length() == 0) {
							players[i].setGame(gameManager.getRandomGame().getGameInfo().getName());
						}
						if (players[i].getGame() == null || players[i].getGame().length() == 0) {
							players[i].setGame(players[i].getLastGame());
						}
						sb.append(players[i].getId() + "/" + players[i].getName());
						if (players[i].getAvata() != null) {
							sb.append("/").append(Arrays.toString(players[i].getAvata()));
						}
						if (i < players.length - 1) {
							sb.append(",");
						}
					}
					// System.out.println("dao18");
					if (logger.isInfoEnabled()) logger.info("[获取玩家列表] [成功] [{}] [players:{}]", new Object[] { username, sb.toString() });
					res = new QUERY_PLAYER_RES(req.getSequnceNum(), players);
				}
				return res;
			} catch (Throwable e) {
				// System.out.println("dao19");
				logger.error("[获取玩家列表] [异常] [" + username + "]", e);
				res = new QUERY_PLAYER_RES(req.getSequnceNum(), new Player[0]);
			}

		} else if (message instanceof QUERY_NEW_PLAYER_REQ) {
			QUERY_NEW_PLAYER_REQ req = (QUERY_NEW_PLAYER_REQ) message;
			QUERY_NEW_PLAYER_RES res = null;
			try {
				// System.out.println("dao1");

				if (username == null) {
					// System.out.println("dao2");
					if (logger.isInfoEnabled()) logger.info("[获取玩家列表1] [失败] [--] [用户名为null] [用户还没有登录]");

					res = new QUERY_NEW_PLAYER_RES(req.getSequnceNum(), new Player[0]);

				} else if (username.equals(req.getUsername()) == false) {
					// System.out.println("dao3");
					// logger.info("[获取玩家列表] [失败] [登录用户：" + username + "] [获取角色：" + req.getUsername() + "] [可能是外挂，获取其他用户的角色列表]");
					if (logger.isInfoEnabled()) logger.info("[获取玩家列表1] [失败] [登录用户：{}] [获取角色：{}] [可能是外挂，获取其他用户的角色列表]", new Object[] { username, req.getUsername() });

					res = new QUERY_NEW_PLAYER_RES(req.getSequnceNum(), new Player[0]);

				} else {
					// System.out.println("dao4");
					Player[] ps = playerManager.getPlayerByUser(username);
					// System.out.println("dao5");
					if (!CoreSubSystem.getInstance().judgeCanEnterServer()) {
						// System.out.println("dao6");
						if (ps.length == 0) {
							String description = "<f color='#FFFFFF'>欢迎您来到飘渺寻仙曲—</f><f color='#FF00'>" + gameConstants.getServerName() + "</f><f color='#FFFFFF'>，此服务器暂时未开放，只开放预创建功能，请仙友们快点抢注自己的名字吧！</f>";
							HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 20, description);
							GameNetworkFramework.getInstance().sendMessage(conn, hreq, description);
							// System.out.println("dao7");
						}
					}
					// System.out.println("dao8");
					String pnames[] = new String[ps.length];
					byte polcamps[] = new byte[ps.length];
					for (int i = 0; i < ps.length; i++) {
						pnames[i] = ps[i].getName();
						polcamps[i] = ps[i].getCountry();
					}
					// System.out.println("dao9");
					WordFilter filter = WordFilter.getInstance();
					if (pnames.length > 0) {
						// System.out.println("dao10");
						boolean needChangeName = false;
						List<Long> changeIds = new ArrayList<Long>();
						StringBuffer changeNameNotice = new StringBuffer("您的角色名与其他玩家重复,需要修改角色名再进入游戏,需要修改的角色名为:");
						// System.out.println("dao11");
						for (Player p : ps) {
							boolean valid = false;
							String name = p.getName();
							if (name != null && name.length() > 0) {// 这里是名字在屏蔽字里的 需要改名
								// 角色名,对于0级词汇,完全过滤;对于1级词汇,等于过滤
								valid = filter.cvalid(name, 0) && filter.evalid(name, 1) && prefixValid(name) && tagValid(name) && gmValid(name);
								logger.warn(p.getLogString() + "[角色名字过滤1] [结果:" + valid + "]");
							}
							// System.out.println("dao12");
							if (!valid) {
								changeNameNotice = new StringBuffer("您的角色名存在非法字符，为了您能正常体验游戏请您修改角色名，需要修改的角色名为：");
								System.out.println("[角色名字过滤1] ["+p.getLogString()+"] [valid:"+valid+"] [name:"+name+"]");
							}
							logger.warn(p.getLogString() + "[角色名字过滤1] [name:"+name+"] [结果:" + valid + "] [ps:"+ps.length+"] [result:"+UnitedServerManager.getInstance().needChangeName(p.getId())+"] ["+p.getLogString()+"]");
							// System.out.println("dao13");
							if (UnitedServerManager.getInstance().needChangeName(p.getId()) || !valid) {
								changeNameNotice.append(p.getName()).append(".");
								needChangeName = true;
								changeIds.add(p.getId());
							}
							// System.out.println("dao14");
						}
						if (needChangeName) {// 需要修改用户
							// System.out.println("dao15");
							Long[] idArr = changeIds.toArray(new Long[0]);
							long[] send = new long[idArr.length];
							for (int i = 0; i < idArr.length; i++) {
								send[i] = idArr[i];
							}
							// System.out.println("dao16");
							NEED_CHANGE_NAME_REQ notice_req = new NEED_CHANGE_NAME_REQ(GameMessageFactory.nextSequnceNum(), changeNameNotice.toString(), send);
							// TODO how to send to client?
							GameNetworkFramework.getInstance().sendMessage(conn, notice_req, "");
							// conn.writeMessage(notice_req);
							logger.warn("[通知用户修改重名角色1] [username:" + username + "] [要改名的角色:" + Arrays.toString(send) + "]");
						}
					}
					// System.out.println("dao17");
					Player players[] = ps;
					StringBuffer sb = new StringBuffer();
					for (int i = 0; i < players.length; i++) {
						if (players[i].getLastGame() == null || players[i].getLastGame().length() == 0) {
							players[i].setGame(gameManager.getRandomGame().getGameInfo().getName());
						}
						if (players[i].getGame() == null || players[i].getGame().length() == 0) {
							players[i].setGame(players[i].getLastGame());
						}
						sb.append(players[i].getId() + "/" + players[i].getName());
						if (i < players.length - 1) {
							sb.append(",");
						}
					}
					// System.out.println("dao18");
					if (logger.isInfoEnabled()) logger.info("[获取玩家列表1] [成功] [{}] [players:{}]", new Object[] { username, sb.toString() });
					res = new QUERY_NEW_PLAYER_RES(req.getSequnceNum(), players);
				}
				return res;
			} catch (Throwable e) {
				// System.out.println("dao19");
				logger.error("[获取玩家列表1] [异常] [" + username + "]", e);
				res = new QUERY_NEW_PLAYER_RES(req.getSequnceNum(), new Player[0]);
			}

		} else if (message instanceof USER_ENTER_SERVER_REQ) {
			USER_ENTER_SERVER_REQ req = (USER_ENTER_SERVER_REQ) message;
			String userName = req.getUsername();
			//isRobot
//			if (isUSERENTERSERVER && (PlatformManager.getInstance().isPlatformOf(Platform.官方) || PlatformManager.getInstance().isPlatformOf(Platform.台湾))) {
//				logger.warn("[老协议应该是外挂] [{}] [{}]", new Object[] { conn.getName(), userName });
//				return null;
//			}
			String password = req.getPassword();
			String authCode = req.getAuthCode();
			//isRobot
//			if (authenticated(userName, password, authCode)) {
				try {
					EnterServerAgent.getInstance().cancelLineup(userName);
					Passport passport = bossService.getPassportByUserName(userName);

					// String md5ed = StringUtil.hash(password);
					// if (!passport.getPassWd().equals(md5ed)) {// 密码校验
					// USER_ENTER_SERVER_RES res = new USER_ENTER_SERVER_RES(req.getSequnceNum(), (byte) 2, 0L, 0L, 0L);
					// logger.error("[用户进入服务器] [密码校验失败] [不能进入] [userName:" + userName + "] [inputPass:"+password+"] [inputMd5:"+md5ed+"] [passportMd5:"+passport.getPassWd()+"]");
					// return res;
					// }
					//isRobot
					String md5ed = StringUtil.hash(password);
//					if (!passport.getPassWd().equals(md5ed)) {// 密码校验
//						boolean isUC = passport.getRegisterChannel().toLowerCase().contains("uc_mieshi") || passport.getRegisterChannel().toLowerCase().matches("uc\\d+_mieshi") || passport.getRegisterChannel().toLowerCase().contains("ucsdk");
//						if (isUC && password.equals(passport.getUcPassword())) {
//							logger.warn("[用户进入服务器] [成功] [匹配UCPWD] [userName:" + userName + "] [inputPass:" + password + "] [inputMd5:" + md5ed + "] [passportMd5:" + passport.getPassWd() + "]");
//						} else {
//							USER_ENTER_SERVER_RES res = new USER_ENTER_SERVER_RES(req.getSequnceNum(), (byte) 2, 0L, 0L, 0L);
//							logger.error("[用户进入服务器] [密码校验失败] [不能进入] [userName:" + userName + "] [inputPass:" + password + "] [inputMd5:" + md5ed + "] [passportMd5:" + passport.getPassWd() + "]");
//							return res;
//						}
//					}
					//isRobot
					// 登陆限制
//					{
//						if (EnterLimitManager.LIMIT && EnterLimitManager.getInstance() != null && (EnterLimitManager.getInstance().inEnterLimit(passport.getUserName(), conn) || (EnterLimitManager.isLimit_nickname && EnterLimitManager.getInstance().inEnterLimit(passport.getNickName(), conn)))) {
//							conn.close();
//							TaskManager.logger.warn("[疑似外挂] [阻止登陆] [" + passport.getUserName() + "]");
//							return null;
//						}
//					}
					// 登录成功，检查是否有这个用户的玩家在线，如果有，全部踢掉
					Player ps[] = playerManager.getOnlinePlayerByUser(userName);
					for (Player p : ps) {
						if (p.isOnline()) {
							if (p.getCurrentGame() != null) {
								p.getCurrentGame().getQueue().push(new LeaveGameEvent(p));
							}
							p.leaveServer();
							p.getConn().close();
							// logger.warn("[用户进入服务器] [已有此用户的玩家在线] [踢掉此用户] [用户:" + userName + "] [玩家:" + p.getName() + "]");
							if (logger.isWarnEnabled()) logger.warn("[用户进入服务器] [已有此用户的玩家在线] [踢掉此用户] [用户:{}] [玩家:{}]", new Object[] { userName, p.getName() });
						}
					}

					conn.setAttachment(passport);
					conn.setAttachmentData(SecuritySubSystem.USERNAME, userName);

					// 检查排队
					EnterServerAgent agent = EnterServerAgent.getInstance();
					boolean canEnter = agent.canEnterDirectly(userName, conn);
					boolean vipEnter = false;
					if (!canEnter) {
						// 检查这个账号的玩家是否有角色是vip2以上，如果有则不需要排队
						Player pps[] = playerManager.getPlayerByUser(userName);
						for (Player p : pps) {
							if (p.getVipLevel() >= NOLINEUP_VIPLEVEL) {
								vipEnter = true;
								break;
							}
						}
					}

					{
						Object o = conn.getAttachmentData("USER_CLIENT_INFO_REQ");
						if (o instanceof USER_CLIENT_INFO_REQ) {
							USER_CLIENT_INFO_REQ req1 = (USER_CLIENT_INFO_REQ) o;
							String clientId = req1.getClientId();
							String channel = req1.getChannel();
							String clientPlatform = req1.getClientPlatform();
							String clientFull = req1.getClientFull();
							String clientProgramVersion = req1.getClientProgramVersion();
							String clientResourceVersion = req1.getClientResourceVersion();
							String phoneType = req1.getPhoneType();
							String network = req1.getNetwork();
							String gpu = req1.getGpu();
							String gpuOtherName = req1.getGpuOtherName();
							String UUID = req1.getUUID();
							String DEVICEID = req1.getDEVICEID();
							String IMSI = req1.getIMSI();
							String MACADDRESS = req1.getMACADDRESS();
							boolean isExistOtherServer = req1.getIsExistOtherServer();
							boolean isStartServerSucess = req1.getIsStartServerSucess();
							boolean isStartServerBindFail = req1.getIsStartServerBindFail();
							GamePlayerManager.logger.warn("[USER_CLIENT_INFO_REQ] [userName:" + userName + "] [isExistOtherServer:" + isExistOtherServer + "] [isStartServerSucess:" + isStartServerSucess + "] [isStartServerBindFail:" + isStartServerBindFail + "] [clientId:" + clientId + "] [channel:" + channel + "] [clientPlatform:" + clientPlatform + "] [clientFull:" + clientFull + "] [clientProgramVersion:" + clientProgramVersion + "] [clientResourceVersion:" + clientResourceVersion + "] [phoneType:" + phoneType + "] [network:" + network + "] [gpu:" + gpu + "] [gpuOtherName:" + gpuOtherName + "] [UUID:" + UUID + "] [DEVICEID:" + DEVICEID + "] [IMSI:" + IMSI + "] [MACADDRESS:" + MACADDRESS + "]");
						}
					}
//					TengXunDataManager.instance.putTXLoginInfo(userName, password);
					if (canEnter || vipEnter) {
						if (vipEnter) {
							conn.setAttachmentData("vipEnter", Boolean.TRUE);
						}
						if (GamePlayerManager.logger.isInfoEnabled()) GamePlayerManager.logger.info("[用户进入服务器握手] [成功:直接进入] [vipEnter:" + vipEnter + "] [用户:{}] [channel:{}] [authCode:{}]", new Object[] { userName, passport != null ? passport.getRegisterChannel() : "", authCode });
						USER_ENTER_SERVER_RES res = new USER_ENTER_SERVER_RES(req.getSequnceNum(), (byte) 0, 0L, 0L, 0L);
						return res;
					} else {
						agent.doLineup(userName, conn);
						int stat[] = agent.getLineupStatus(userName);
						int pos = agent.getNowPosition(userName);
						long etime = agent.getEstimateTime(pos);
						if (GamePlayerManager.logger.isInfoEnabled()) GamePlayerManager.logger.info("[用户进入服务器握手] [成功：需要排队] [用户:{}] [channel:{}] [位置:" + pos + "] [前面在线/离线:" + stat[0] + "/" + stat[1] + "] [预计进入时长:" + etime + "ms] [authCode:{}]", new Object[] { userName, passport != null ? passport.getRegisterChannel() : "", authCode });
						USER_ENTER_SERVER_RES res = new USER_ENTER_SERVER_RES(req.getSequnceNum(), (byte) 1, stat[0], stat[1], etime);
						return res;
					}

				} catch (Exception e) {
					e.printStackTrace();
					GamePlayerManager.logger.error("[用户进入服务器握手] [失败:获取账号失败] [用户:" + userName + "] [channel:] [authCode:" + authCode + "]", e);
					USER_ENTER_SERVER_RES res = new USER_ENTER_SERVER_RES(req.getSequnceNum(), (byte) 2, 0L, 0L, 0L);
					return res;
				}
//		}
//		else {
//				GamePlayerManager.logger.error("[用户进入服务器握手] [失败:校验失败的连接,关闭连接] [用户:{}] [authCode:{}]", new Object[] { userName, authCode });
//				conn.close();
//				return null;
//			}
		} else if (message instanceof IOS_CLIENT_MSG_REQ) {
			conn.setAttachmentData("IOS_CLIENT_MSG_REQ", message);
			IOS_CLIENT_MSG_REQ req = (IOS_CLIENT_MSG_REQ) message;
			logger.warn("[收到IOS特殊协议] [" + Arrays.toString(((IOS_CLIENT_MSG_REQ) req).getMsgs()) + "]");
		} else if (message instanceof NEW_USER_ENTER_SERVER_REQ) {
			NewUserEnterServerService.handle_NEW_USER_ENTER_SERVER_REQ(conn, message);
		} else if (message instanceof NOTIFY_SERVER_TIREN_REQ) {
			NewUserEnterServerService.handle_NOTIFY_SERVER_TIREN_REQ(conn, message);
		} else if (message instanceof USER_ENTER_SERVER2_REQ) {
			USER_ENTER_SERVER2_REQ req = (USER_ENTER_SERVER2_REQ) message;
			String userName = req.getUsername();
			String password = req.getPassword();
			String authCode = req.getAuthCode();

			String clientId = req.getClientId();
			String channel = req.getChannel();
			String clientPlatform = req.getClientPlatform();
			String clientFull = req.getClientFull();
			String clientProgramVersion = req.getClientProgramVersion();
			String clientResourceVersion = req.getClientResourceVersion();
			String phoneType = req.getPhoneType();
			String network = req.getNetwork();
			String gpu = req.getGpu();
			String gpuOtherName = req.getGpuOtherName();
			String UUID = req.getUUID();
			String DEVICEID = req.getDEVICEID();
			String IMSI = req.getIMSI();
			String MACADDRESS = req.getMACADDRESS();
			boolean isExistOtherServer = req.getIsExistOtherServer();
			boolean isStartServerSucess = req.getIsStartServerSucess();
			boolean isStartServerBindFail = req.getIsStartServerBindFail();
			GamePlayerManager.logger.warn("[USER_ENTER_SERVER2_REQ] [userName:" + userName + "] [authCode:" + authCode + "] [isExistOtherServer:" + isExistOtherServer + "] [isStartServerSucess:" + isStartServerSucess + "] [isStartServerBindFail:" + isStartServerBindFail + "] [clientId:" + clientId + "] [channel:" + channel + "] [clientPlatform:" + clientPlatform + "] [clientFull:" + clientFull + "] [clientProgramVersion:" + clientProgramVersion + "] [clientResourceVersion:" + clientResourceVersion + "] [phoneType:" + phoneType + "] [network:" + network + "] [gpu:" + gpu + "] [gpuOtherName:" + gpuOtherName + "] [UUID:" + UUID + "] [DEVICEID:" + DEVICEID + "] [IMSI:" + IMSI + "] [MACADDRESS:" + MACADDRESS + "]");
			if (authenticated(userName, password, authCode)) {
				try {
					EnterServerAgent.getInstance().cancelLineup(userName);
					Passport passport = bossService.getPassportByUserName(userName);
					String md5ed = StringUtil.hash(password);
					if (!passport.getPassWd().equals(md5ed)) {// 密码校验
						USER_ENTER_SERVER_RES res = new USER_ENTER_SERVER_RES(req.getSequnceNum(), (byte) 2, 0L, 0L, 0L);
						logger.error("[用户进入服务器] [密码校验失败] [不能进入] [userName:" + userName + "] [inputPass:" + password + "] [inputMd5:" + md5ed + "] [passportMd5:" + passport.getPassWd() + "]");
						return res;
					}
					// 登录成功，检查是否有这个用户的玩家在线，如果有，全部踢掉
					Player ps[] = playerManager.getOnlinePlayerByUser(username);
					for (Player p : ps) {
						if (p.isOnline()) {
							if (p.getCurrentGame() != null) {
								p.getCurrentGame().getQueue().push(new LeaveGameEvent(p));
							}
							p.leaveServer();
							p.getConn().close();
							// logger.warn("[用户进入服务器] [已有此用户的玩家在线] [踢掉此用户] [用户:" + userName + "] [玩家:" + p.getName() + "]");
							if (logger.isWarnEnabled()) logger.warn("[用户进入服务器] [已有此用户的玩家在线] [踢掉此用户] [用户:{}] [玩家:{}]", new Object[] { userName, p.getName() });
						}
					}
					conn.setAttachment(passport);
					conn.setAttachmentData(SecuritySubSystem.USERNAME, userName);
					conn.setAttachmentData("USER_ENTER_SERVER2_REQ", req);
					// 检查排队
					EnterServerAgent agent = EnterServerAgent.getInstance();
					boolean canEnter = agent.canEnterDirectly(userName, conn);
					if (canEnter) {
						if (GamePlayerManager.logger.isInfoEnabled()) GamePlayerManager.logger.info("[用户进入服务器握手] [成功:直接进入] [用户:{}] [channel:{}] [authCode:{}]", new Object[] { userName, passport != null ? passport.getRegisterChannel() : "", authCode });
						USER_ENTER_SERVER_RES res = new USER_ENTER_SERVER_RES(req.getSequnceNum(), (byte) 0, 0L, 0L, 0L);
						return res;
					} else {
						agent.doLineup(userName, conn);
						int stat[] = agent.getLineupStatus(userName);
						int pos = agent.getNowPosition(userName);
						long etime = agent.getEstimateTime(pos);
						if (GamePlayerManager.logger.isInfoEnabled()) GamePlayerManager.logger.info("[用户进入服务器握手] [成功：需要排队] [用户:{}] [channel:{}] [位置:" + pos + "] [前面在线/离线:" + stat[0] + "/" + stat[1] + "] [预计进入时长:" + etime + "ms] [authCode:{}]", new Object[] { userName, passport != null ? passport.getRegisterChannel() : "", authCode });
						USER_ENTER_SERVER_RES res = new USER_ENTER_SERVER_RES(req.getSequnceNum(), (byte) 1, stat[0], stat[1], etime);
						return res;
					}
				} catch (Exception e) {
					e.printStackTrace();
					GamePlayerManager.logger.error("[用户进入服务器握手] [失败:获取账号失败] [用户:" + userName + "] [channel:] [authCode:" + authCode + "]", e);
					USER_ENTER_SERVER_RES res = new USER_ENTER_SERVER_RES(req.getSequnceNum(), (byte) 2, 0L, 0L, 0L);
					return res;
				}
			} else {
				GamePlayerManager.logger.error("[用户进入服务器握手] [失败:校验失败的连接,关闭连接] [用户:{}] [authCode:{}]", new Object[] { userName, authCode });
				conn.close();
				return null;
			}
		} else if (message instanceof USER_CLIENT_INFO_REQ) {
			USER_CLIENT_INFO_REQ req = (USER_CLIENT_INFO_REQ) message;
			String clientId = req.getClientId();
			String channel = req.getChannel();
			String clientPlatform = req.getClientPlatform();
			String clientFull = req.getClientFull();
			String clientProgramVersion = req.getClientProgramVersion();
			String clientResourceVersion = req.getClientResourceVersion();
			String phoneType = req.getPhoneType();
			String network = req.getNetwork();
			String gpu = req.getGpu();
			String gpuOtherName = req.getGpuOtherName();
			String UUID = req.getUUID();
			String DEVICEID = req.getDEVICEID();
			String IMSI = req.getIMSI();
			String MACADDRESS = req.getMACADDRESS();
			boolean isExistOtherServer = req.getIsExistOtherServer();
			boolean isStartServerSucess = req.getIsStartServerSucess();
			boolean isStartServerBindFail = req.getIsStartServerBindFail();
			conn.setAttachmentData("USER_CLIENT_INFO_REQ", req);
			GamePlayerManager.logger.warn("[USER_CLIENT_INFO_REQ] [userName:" + username + "] [isExistOtherServer:" + isExistOtherServer + "] [isStartServerSucess:" + isStartServerSucess + "] [isStartServerBindFail:" + isStartServerBindFail + "] [clientId:" + clientId + "] [channel:" + channel + "] [clientPlatform:" + clientPlatform + "] [clientFull:" + clientFull + "] [clientProgramVersion:" + clientProgramVersion + "] [clientResourceVersion:" + clientResourceVersion + "] [phoneType:" + phoneType + "] [network:" + network + "] [gpu:" + gpu + "] [gpuOtherName:" + gpuOtherName + "] [UUID:" + UUID + "] [DEVICEID:" + DEVICEID + "] [IMSI:" + IMSI + "] [MACADDRESS:" + MACADDRESS + "]");
		} else if (message instanceof RSA_DATA_REQ) {
			if (EnterLimitManager.isCompareRSA) {
				RSA_DATA_REQ req = (RSA_DATA_REQ) message;
				String clientId = req.getClientID();
				String clientProgramVersion = req.getClientVersion();
				String platform = req.getPlatform();
				String realPlatform = req.getRealPlatform();
				String gpu = req.getGPU();
				String otherGpu = req.getOtherGPU();
				byte[] rsa_data = req.getRsa_data();
				// conn.setAttachmentData("RSA_DATA_REQ", req);
				EnterLimitManager.logger.warn("[RSA_DATA_REQ] [{}] [{}] [{}] [{}] [{}] [{}] [{}]", new Object[] { clientId, clientProgramVersion, platform, realPlatform, gpu, otherGpu, rsa_data.length });
			}
		} else if (message instanceof GET_RSA_DATA_REQ) {
			if (EnterLimitManager.isCompareRSA) {
				GET_RSA_DATA_REQ req = (GET_RSA_DATA_REQ) message;
				String clientId = req.getClientID();
				String clientProgramVersion = req.getClientVersion();
				String platform = req.getPlatform();
				String realPlatform = req.getRealPlatform();
				String gpu = req.getGPU();
				String otherGpu = req.getOtherGPU();
				byte[] rsa_data = req.getRsa_data();
				String playerLog = "无";
				if (player != null) {
					playerLog = player.getLogString();
				}
				EnterLimitManager.logger.warn("[GET_RSA_DATA_REQ] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}]", new Object[] { playerLog, clientId, clientProgramVersion, platform, realPlatform, gpu, otherGpu, rsa_data.length });
				conn.setAttachmentData("GET_RSA_DATA_REQ", req);
			}
		} else if (message instanceof PLAYER_ENTER_CROSSSERVER_REQ) {

		} else if (message instanceof PLAYER_RECONN_REQ) {
			PLAYER_RECONN_REQ req = (PLAYER_RECONN_REQ) message;
			String userName = req.getUsername();
			String password = req.getPassword();
			String authCode = req.getAuthCode();
			if (authenticated(userName, password, authCode)) {

				try {
					Passport passport = bossService.getPassportByUserName(userName);// login(userName, password);
					// 登录成功，检查是否有这个用户的玩家在线，如果有，全部踢掉

					Player p = playerManager.getPlayer(req.getPlayer());
					if (p != null) {

						// 检查是否有外挂
						if (passport.getUserName().equals(p.getUsername()) == false && passport.getNickName().equals(p.getUsername()) == false) {

							if (logger.isWarnEnabled()) logger.warn("[处理PLAYER_RECONN_REQ] [出现外挂] [{}] [登录用户：{}] [角色用户：{}] [{}] [角色上的帐号名与连接上的帐号名不匹配]", new Object[] { conn.getName(), passport.getUserName(), p.getUsername(), p.getName() });
							if (GameNetworkFramework.logger.isWarnEnabled()) GameNetworkFramework.logger.warn("[处理PLAYER_ENTER_REQ] [出现外挂] [{}] [登录用户：{}] [角色用户：{}] [{}] [角色上的帐号名与连接上的帐号名不匹配]", new Object[] { conn.getName(), passport.getUserName(), p.getUsername(), p.getName() });
							conn.close();
							return null;
						}

						String oldConnName = "-";
						if (p.getConn() != null) {
							oldConnName = p.getConn().getName();
							p.getConn().close();
						}
						conn.setAttachment(passport);
						conn.setAttachmentData(SecuritySubSystem.USERNAME, userName);

						p.setPassport(passport);
						conn.setAttachmentData("Player", p);
						p.setConn(conn);

						if (p.getCurrentGame() != null) {
							p.getCurrentGame().messageQueue.push(new ReconnectEvent(p));
						}
						// 断网重连后，可能客户端上自己的某些属性状态，周围的怪...没有和服务器同步
						// 不能在这里修改，这个线程和game心跳是不同的线程

						logger.error("[用户重连服务器] [成功] [用户:{}] [authCode:{}] [{}] [old:{}] [new:{}]", new Object[] { userName, authCode, p.getName(), oldConnName, conn.getName() });
						Player.sendPlayerAction(p, PlayerActionFlow.行为类型_断网重连, Translate.text_5930, 0, new java.util.Date(), GamePlayerManager.isAllowActionStat());
						return new PLAYER_RECONN_RES(req.getSequnceNum(), (byte) 0, Translate.text_5931);
					} else {
						conn.close();
						logger.error("[用户重连失败] [失败:获取角色失败] [用户:{}] [authCode:{}]", new Object[] { userName, authCode });
					}

				} catch (Exception e) {
					conn.close();
					logger.error("[用户重连失败] [失败:获取账号失败] [用户:" + userName + "] [authCode:" + authCode + "]", e);
				}
			} else {
				logger.error("[用户重连失败] [失败:校验失败的连接,关闭连接] [用户:{}] [authCode:{}]", new Object[] { userName, authCode });
				conn.close();
				return null;
			}
		} else if (message instanceof CHANGE_PLAYER_NAME_REQ) {
			CHANGE_PLAYER_NAME_REQ req = (CHANGE_PLAYER_NAME_REQ) message;
			logger.warn("[角色改名]" + req.getPlayerId());
			long playerId = req.getPlayerId();
			Player tempPlayer = null;
			try {
				tempPlayer = GamePlayerManager.getInstance().getPlayer(playerId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (tempPlayer == null) {
				logger.warn("[角色改名] [角色不存在:" + playerId + "]");
				return null;
			}
			String newName = req.getNewName();
			String oldName = tempPlayer.getName();

			WordFilter filter = WordFilter.getInstance();
			boolean validForOldName = false;
			String name = tempPlayer.getName();
			if (name != null && name.length() > 0) {
				// 角色名,对于0级词汇,完全过滤;对于1级词汇,等于过滤
				validForOldName = filter.cvalid(name, 0) && filter.evalid(name, 1) && prefixValid(name) && tagValid(name) && gmValid(name);
			}

			if (!UnitedServerManager.getInstance().needChangeName(playerId) && validForOldName) {
				logger.warn("[角色改名] [" + tempPlayer.getLogString() + "] [oldName:" + oldName + "] [newName:" + newName + "] 你不需要改名");
				return new CHANGE_PLAYER_NAME_RES(message.getSequnceNum(), tempPlayer.getId(), oldName, 1, "你不需要改名");
			}
			logger.warn("[角色改名] [" + tempPlayer.getLogString() + "] [oldName:" + oldName + "] [newName:" + newName + "]");
			boolean valid = false;
			newName = newName.replaceAll("'", "");
			if (newName != null && newName.length() > 0) {
				// 角色名,对于0级词汇,完全过滤;对于1级词汇,等于过滤
				valid = filter.cvalid(newName, 0) && filter.evalid(newName, 1) && prefixValid(newName) && tagValid(newName) && gmValid(newName);
			}
			if (!valid) {
				logger.warn("[角色改名] [" + tempPlayer.getLogString() + "] [oldName:" + oldName + "] [newName:" + newName + "]" + Translate.角色名称不合法);
				return new CHANGE_PLAYER_NAME_RES(message.getSequnceNum(), tempPlayer.getId(), oldName, 1, Translate.角色名称不合法);
			}
			GamePlayerManager gpm = ((GamePlayerManager) GamePlayerManager.getInstance());
			synchronized (gpm.getCreate_lock()) {
				if (gpm.creatingPlayerNameSet.contains(newName)) {
					throw new Exception("角色名正在被其他玩家创建");
				}
				gpm.creatingPlayerNameSet.add(newName);
			}
			try {
				boolean exists = gpm.isExists(newName);
				if (!exists) {
					tempPlayer.setName(newName);
					gpm.em.flush(tempPlayer);
//					JJCManager.getInstance().changePlayerNameForJJCBillboardData(JJCManager.em_stat, oldName, newName);
//					JJCManager.getInstance().changePLayerNameForCross(GameConstants.getInstance().getServerName(), oldName, newName);
					GamePlayerManager.logger.warn("[角色改名] [成功] [" + oldName + ">" + newName + "]");

					// TODO
					{
						// 改名导致的一些操作修改
						// 1.仙府.
						// 2.各种称号
						UnitedServerManager.getInstance().notifyPlayerChanageName(tempPlayer, oldName);
					}
					{
						// 修改名字成功后 再通知一次 需要修改的角色用户名
						Player[] ps = playerManager.getPlayerByUser(username);
						String pnames[] = new String[ps.length];
						byte polcamps[] = new byte[ps.length];
						for (int i = 0; i < ps.length; i++) {
							pnames[i] = ps[i].getName();
							polcamps[i] = ps[i].getCountry();
						}
						if (pnames.length > 0) {
							boolean needChangeName = false;
							List<Long> changeIds = new ArrayList<Long>();
							StringBuffer changeNameNotice = new StringBuffer("您的角色名与其他玩家重复,需要修改角色名再进入游戏,需要修改的角色名为:");
							for (Player p : ps) {
								if (UnitedServerManager.getInstance().needChangeName(p.getId())) {
									changeNameNotice.append(p.getName()).append(".");
									needChangeName = true;
									changeIds.add(p.getId());
								}
							}
							if (needChangeName) {// 需要修改用户
								Long[] idArr = changeIds.toArray(new Long[0]);
								long[] send = new long[idArr.length];
								for (int i = 0; i < idArr.length; i++) {
									send[i] = idArr[i];
								}
								NEED_CHANGE_NAME_REQ notice_req = new NEED_CHANGE_NAME_REQ(GameMessageFactory.nextSequnceNum(), changeNameNotice.toString(), send);
								// TODO how to send to client?
								GameNetworkFramework.getInstance().sendMessage(conn, notice_req, "");
								// conn.writeMessage(notice_req);
								logger.warn("[通知用户修改重名角色] [username:" + username + "] [要改名的角色:" + Arrays.toString(send) + "]");
							}
						}
					}
					return new CHANGE_PLAYER_NAME_RES(message.getSequnceNum(), playerId, newName, 0, "修改角色名成功,祝您游戏愉快");
				} else {
					logger.warn("[角色改名] [" + tempPlayer.getLogString() + "] [oldName:" + oldName + "] [newName:" + newName + "]" + "角色名已经被使用");
					return new CHANGE_PLAYER_NAME_RES(message.getSequnceNum(), tempPlayer.getId(), oldName, 1, "角色名已经被使用");
					// throw new Exception("角色名已经被使用");
				}
			} finally {
				synchronized (gpm.create_lock) {
					gpm.creatingPlayerNameSet.remove(newName);
				}
			}
		} else if (message instanceof VIP_LOTTERY_CLICK_REQ) {
			if (logger.isInfoEnabled()) {
				logger.info("[VIP转盘] [用户点击转盘] [" + player.getLogString() + "]");
			}
			VipManager.getInstance().getLotteryGame().play(player);

		}
		return null;
	}

	/**
	 * 判断角色名是否开头允许
	 * @param name
	 * @return
	 */
	public boolean prefixValid(String name) {
		String aname = name.toLowerCase();
		for (String s : prefixforbid) {
			if (aname.startsWith(s)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断是否含有禁用的标签字符
	 * @param name
	 * @return
	 */
	public boolean tagValid(String name) {
		String aname = name.toLowerCase();
		for (String s : tagforbid) {
			if (aname.indexOf(s) != -1) {
				return false;
			}
		}
		return true;
	}

	public boolean gmValid(String name) {
		String aname = name.toLowerCase().trim();
		aname = aname.replaceAll(" ", "").trim();
		aname = aname.replaceAll("　", "").trim();
		aname = aname.replaceAll("　", "").trim();
		if (aname.matches("gm\\d*")) {
			return false;
		}
		return true;
	}

	public String encrypt(String username, String password) {
		return StringUtil.hash(username + "|" + password + "|" + privatekey);
	}

	public boolean authenticated(String username, String password, String authCode) {
		if (username == null || authCode == null) {
			return false;
		}
		String code = encrypt(username, password);
		if (code.equals(authCode)) {
			return true;
		}
		// logger.warn("[玩家登录校验][失败][" + username + "][" + password + "][" + authCode + "][:" + code + "]");
		if (logger.isWarnEnabled()) logger.warn("[玩家登录校验][失败][{}][{}][{}][:{}]", new Object[] { username, password, authCode, code });
		return false;
	}

	public boolean handleResponseMessage(Connection conn, RequestMessage req, ResponseMessage message) throws ConnectionException, Exception {
		Player player = (Player) conn.getAttachmentData("Player");
		if (message instanceof TIME_SYNC_RES) {
			TIME_SYNC_RES res = (TIME_SYNC_RES) message;
			long delay = (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - res.getCurrentTime()) / 2;

			conn.setAttachmentData("network.delay", delay);

			if (framework != null) {
				framework.sendMessage(conn, new TIME_SETTING_REQ(GameMessageFactory.nextSequnceNum(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis(), delay), "setting_time");
			}
		} else if (message instanceof SERVER_HAND_CLIENT_1_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_1_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_2_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_2_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_3_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_3_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_4_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_4_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_5_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_5_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_6_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_6_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_7_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_7_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_8_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_8_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_9_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_9_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_10_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_10_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_11_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_11_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_12_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_12_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_13_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_13_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_14_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_14_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_15_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_15_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_16_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_16_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_17_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_17_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_18_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_18_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_19_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_19_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_20_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_20_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_21_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_21_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_22_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_22_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_23_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_23_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_24_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_24_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_25_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_25_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_26_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_26_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_27_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_27_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_28_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_28_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_29_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_29_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_30_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_30_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_31_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_31_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_32_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_32_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_33_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_33_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_34_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_34_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_35_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_35_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_36_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_36_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_37_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_37_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_38_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_38_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_39_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_39_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_40_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_40_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_41_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_41_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_42_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_42_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_43_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_43_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_44_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_44_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_45_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_45_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_46_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_46_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_47_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_47_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_48_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_48_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_49_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_49_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_50_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_50_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_51_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_51_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_52_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_52_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_53_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_53_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_54_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_54_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_55_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_55_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_56_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_56_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_57_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_57_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_58_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_58_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_59_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_59_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_60_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_60_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_61_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_61_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_62_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_62_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_63_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_63_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_64_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_64_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_65_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_65_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_66_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_66_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_67_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_67_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_68_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_68_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_69_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_69_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_70_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_70_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_71_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_71_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_72_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_72_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_73_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_73_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_74_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_74_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_75_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_75_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_76_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_76_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_77_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_77_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_78_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_78_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_79_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_79_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_80_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_80_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_81_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_81_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_82_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_82_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_83_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_83_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_84_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_84_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_85_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_85_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_86_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_86_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_87_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_87_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_88_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_88_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_89_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_89_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_90_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_90_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_91_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_91_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_92_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_92_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_93_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_93_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_94_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_94_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_95_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_95_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_96_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_96_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_97_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_97_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_98_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_98_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_99_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_99_RES(conn, message);
		} else if (message instanceof SERVER_HAND_CLIENT_100_RES) {
			NewUserEnterServerService.handle_SERVER_HAND_CLIENT_100_RES(conn, message);
		} else if (message instanceof GET_PHONE_INFO_1_RES) {
			NewUserEnterServerService.handle_GET_PHONE_INFO_1_RES(conn, message, player);
		} else if (message instanceof GET_PHONE_INFO_2_RES) {
			NewUserEnterServerService.handle_GET_PHONE_INFO_2_RES(conn, message, player);
		} else if (message instanceof GET_PHONE_INFO_3_RES) {
			NewUserEnterServerService.handle_GET_PHONE_INFO_3_RES(conn, message, player);
		} else if (message instanceof GET_PHONE_INFO_4_RES) {
			NewUserEnterServerService.handle_GET_PHONE_INFO_4_RES(conn, message, player);
		} else if (message instanceof GET_PHONE_INFO_5_RES) {
			NewUserEnterServerService.handle_GET_PHONE_INFO_5_RES(conn, message, player);
		} else if (message instanceof GET_PHONE_INFO_6_RES) {
			NewUserEnterServerService.handle_GET_PHONE_INFO_6_RES(conn, message, player);
		} else if (message instanceof GET_PHONE_INFO_7_RES) {
			NewUserEnterServerService.handle_GET_PHONE_INFO_7_RES(conn, message, player);
		} else if (message instanceof GET_PHONE_INFO_8_RES) {
			NewUserEnterServerService.handle_GET_PHONE_INFO_8_RES(conn, message, player);
		} else if (message instanceof GET_PHONE_INFO_9_RES) {
			NewUserEnterServerService.handle_GET_PHONE_INFO_9_RES(conn, message, player);
		} else if (message instanceof GET_PHONE_INFO_10_RES) {
			NewUserEnterServerService.handle_GET_PHONE_INFO_10_RES(conn, message, player);
		} else if (message instanceof GET_PHONE_INFO_11_RES) {
			NewUserEnterServerService.handle_GET_PHONE_INFO_11_RES(conn, message, player);
		} else if (message instanceof GET_CLIENT_INFO_RES) {
			NewUserEnterServerService.handle_GET_CLIENT_INFO_RES(conn, message, player);
		} else if (message instanceof GET_CLIENT_INFO_1_RES) {
			NewUserEnterServerService.handle_GET_CLIENT_INFO_1_RES(conn, message, player);
		} else if (message instanceof GET_CLIENT_INFO_2_RES) {
			NewUserEnterServerService.handle_GET_CLIENT_INFO_2_RES(conn, message, player);
		} else if (message instanceof GET_CLIENT_INFO_3_RES) {
			NewUserEnterServerService.handle_GET_CLIENT_INFO_3_RES(conn, message, player);
		} else if (message instanceof GET_CLIENT_INFO_4_RES) {
			NewUserEnterServerService.handle_GET_CLIENT_INFO_4_RES(conn, message, player);
		} else if (message instanceof GET_CLIENT_INFO_5_RES) {
			NewUserEnterServerService.handle_GET_CLIENT_INFO_5_RES(conn, message, player);
		} else if (message instanceof GET_CLIENT_INFO_6_RES) {
			NewUserEnterServerService.handle_GET_CLIENT_INFO_6_RES(conn, message, player);
		} else if (message instanceof GET_CLIENT_INFO_7_RES) {
			NewUserEnterServerService.handle_GET_CLIENT_INFO_7_RES(conn, message, player);
		} else if (message instanceof GET_CLIENT_INFO_8_RES) {
			NewUserEnterServerService.handle_GET_CLIENT_INFO_8_RES(conn, message, player);
		} else if (message instanceof GET_CLIENT_INFO_9_RES) {
			NewUserEnterServerService.handle_GET_CLIENT_INFO_9_RES(conn, message, player);
		} else if (message instanceof TRY_GETPLAYERINFO_RES) {
			NewUserEnterServerService.handle_TRY_GETPLAYERINFO_RES(conn, message);
		} else if (message instanceof WAIT_FOR_OTHER_RES) {
			NewUserEnterServerService.handle_WAIT_FOR_OTHER_RES(conn, message);
		} else if (message instanceof GET_REWARD_2_PLAYER_RES) {
			NewUserEnterServerService.handle_GET_REWARD_2_PLAYER_RES(conn, message);
		} else if (message instanceof REQUESTBUY_GET_ENTITY_INFO_RES) {
			NewUserEnterServerService.handle_REQUESTBUY_GET_ENTITY_INFO_RES(conn, message);
		} else if (message instanceof PLAYER_SOUL_RES) {
			NewUserEnterServerService.handle_PLAYER_SOUL_RES(conn, message);
		} else if (message instanceof CARD_TRYSAVING_RES) {
			NewUserEnterServerService.handle_CARD_TRYSAVING_RES(conn, message);
		} else if (message instanceof GANG_WAREHOUSE_JOURNAL_RES) {
			NewUserEnterServerService.handle_GANG_WAREHOUSE_JOURNAL_RES(conn, message);
		} else if (message instanceof GET_WAREHOUSE_RES) {
			NewUserEnterServerService.handle_GET_WAREHOUSE_RES(conn, message);
		} else if (message instanceof QUERY__GETAUTOBACK_RES) {
			NewUserEnterServerService.handle_QUERY__GETAUTOBACK_RES(conn, message);
		} else if (message instanceof GET_ZONGPAI_NAME_RES) {
			NewUserEnterServerService.handle_GET_ZONGPAI_NAME_RES(conn, message);
		} else if (message instanceof TRY_LEAVE_ZONGPAI_RES) {
			NewUserEnterServerService.handle_TRY_LEAVE_ZONGPAI_RES(conn, message);
		} else if (message instanceof REBEL_EVICT_NEW_RES) {
			NewUserEnterServerService.handle_REBEL_EVICT_NEW_RES(conn, message);
		} else if (message instanceof GET_PLAYERTITLE_RES) {
			NewUserEnterServerService.handle_GET_PLAYERTITLE_RES(conn, message);
		} else if (message instanceof MARRIAGE_TRY_BEQSTART_RES) {
			NewUserEnterServerService.handle_MARRIAGE_TRY_BEQSTART_RES(conn, message);
		} else if (message instanceof MARRIAGE_GUESTNEW_OVER_RES) {
			NewUserEnterServerService.handle_MARRIAGE_GUESTNEW_OVER_RES(conn, message);
		} else if (message instanceof MARRIAGE_HUNLI_RES) {
			NewUserEnterServerService.handle_MARRIAGE_HUNLI_RES(conn, message);
		} else if (message instanceof COUNTRY_COMMENDCANCEL_RES) {
			NewUserEnterServerService.handle_COUNTRY_COMMENDCANCEL_RES(conn, message);
		} else if (message instanceof COUNTRY_NEWQIUJIN_RES) {
			NewUserEnterServerService.handle_COUNTRY_NEWQIUJIN_RES(conn, message);
		} else if (message instanceof GET_COUNTRYJINKU_RES) {
			NewUserEnterServerService.handle_GET_COUNTRYJINKU_RES(conn, message);
		} else if (message instanceof CAVE_NEWBUILDING_RES) {
			NewUserEnterServerService.handle_CAVE_NEWBUILDING_RES(conn, message);
		} else if (message instanceof CAVE_FIELD_RES) {
			NewUserEnterServerService.handle_CAVE_FIELD_RES(conn, message);
		} else if (message instanceof CAVE_NEW_PET_RES) {
			NewUserEnterServerService.handle_CAVE_NEW_PET_RES(conn, message);
		} else if (message instanceof CAVE_TRY_SCHEDULE_RES) {
			NewUserEnterServerService.handle_CAVE_TRY_SCHEDULE_RES(conn, message);
		} else if (message instanceof CAVE_SEND_COUNTYLIST_RES) {
			NewUserEnterServerService.handle_CAVE_SEND_COUNTYLIST_RES(conn, message);
		} else if (message instanceof PLAYER_NEW_LEVELUP_RES) {
			NewUserEnterServerService.handle_PLAYER_NEW_LEVELUP_RES(conn, message);
		} else if (message instanceof CLEAN_FRIEND_LIST_RES) {
			NewUserEnterServerService.handle_CLEAN_FRIEND_LIST_RES(conn, message);
		} else if (message instanceof DO_ACTIVITY_NEW_RES) {
			NewUserEnterServerService.handle_DO_ACTIVITY_NEW_RES(conn, message);
		} else if (message instanceof REF_TESK_LIST_RES) {
			NewUserEnterServerService.handle_REF_TESK_LIST_RES(conn, message);
		} else if (message instanceof DELTE_PET_INFO_RES) {
			NewUserEnterServerService.handle_DELTE_PET_INFO_RES(conn, message);
		} else if (message instanceof MARRIAGE_DOACTIVITY_RES) {
			NewUserEnterServerService.handle_MARRIAGE_DOACTIVITY_RES(conn, message);
		} else if (message instanceof LA_FRIEND_RES) {
			NewUserEnterServerService.handle_LA_FRIEND_RES(conn, message);
		} else if (message instanceof TRY_NEWFRIEND_LIST_RES) {
			NewUserEnterServerService.handle_TRY_NEWFRIEND_LIST_RES(conn, message);
		} else if (message instanceof QINGQIU_PET_INFO_RES) {
			NewUserEnterServerService.handle_QINGQIU_PET_INFO_RES(conn, message);
		} else if (message instanceof REMOVE_ACTIVITY_NEW_RES) {
			NewUserEnterServerService.handle_REMOVE_ACTIVITY_NEW_RES(conn, message);
		} else if (message instanceof TRY_LEAVE_GAME_RES) {
			NewUserEnterServerService.handle_TRY_LEAVE_GAME_RES(conn, message);
		} else if (message instanceof GET_TESK_LIST_RES) {
			NewUserEnterServerService.handle_GET_TESK_LIST_RES(conn, message);
		} else if (message instanceof GET_GAME_PALAYERNAME_RES) {
			NewUserEnterServerService.handle_GET_GAME_PALAYERNAME_RES(conn, message);
		} else if (message instanceof GET_ACTIVITY_JOINIDS_RES) {
			NewUserEnterServerService.handle_GET_ACTIVITY_JOINIDS_RES(conn, message);
		} else if (message instanceof QUERY_GAMENAMES_RES) {
			NewUserEnterServerService.handle_QUERY_GAMENAMES_RES(conn, message);
		} else if (message instanceof GET_PET_NBWINFO_RES) {
			NewUserEnterServerService.handle_GET_PET_NBWINFO_RES(conn, message);
		} else if (message instanceof CLONE_FRIEND_LIST_RES) {
			NewUserEnterServerService.handle_CLONE_FRIEND_LIST_RES(conn, message);
		} else if (message instanceof QUERY_OTHERPLAYER_PET_MSG_RES) {
			NewUserEnterServerService.handle_QUERY_OTHERPLAYER_PET_MSG_RES(conn, message);
		} else if (message instanceof CSR_GET_PLAYER_RES) {
			NewUserEnterServerService.handle_CSR_GET_PLAYER_RES(conn, message);
		} else if (message instanceof HAVE_OTHERNEW_INFO_RES) {
			NewUserEnterServerService.handle_HAVE_OTHERNEW_INFO_RES(conn, message);
		} else if (message instanceof SHANCHU_FRIENDLIST_RES) {
			NewUserEnterServerService.handle_SHANCHU_FRIENDLIST_RES(conn, message);
		} else if (message instanceof QUERY_TESK_LIST_RES) {
			NewUserEnterServerService.handle_QUERY_TESK_LIST_RES(conn, message);
		} else if (message instanceof CL_HORSE_INFO_RES) {
			NewUserEnterServerService.handle_CL_HORSE_INFO_RES(conn, message);
		} else if (message instanceof CL_NEWPET_MSG_RES) {
			NewUserEnterServerService.handle_CL_NEWPET_MSG_RES(conn, message);
		} else if (message instanceof GET_ACTIVITY_NEW_RES) {
			NewUserEnterServerService.handle_GET_ACTIVITY_NEW_RES(conn, message);
		} else if (message instanceof DO_SOME_RES) {
			NewUserEnterServerService.handle_DO_SOME_RES(conn, message);
		} else if (message instanceof TY_PET_RES) {
			NewUserEnterServerService.handle_TY_PET_RES(conn, message);
		} else if (message instanceof EQUIPMENT_GET_MSG_RES) {
			NewUserEnterServerService.handle_EQUIPMENT_GET_MSG_RES(conn, message);
		} else if (message instanceof EQU_NEW_EQUIPMENT_RES) {
			NewUserEnterServerService.handle_EQU_NEW_EQUIPMENT_RES(conn, message);
		} else if (message instanceof DELETE_FRIEND_LIST_RES) {
			NewUserEnterServerService.handle_DELETE_FRIEND_LIST_RES(conn, message);
		} else if (message instanceof DO_PET_EQUIPMENT_RES) {
			NewUserEnterServerService.handle_DO_PET_EQUIPMENT_RES(conn, message);
		} else if (message instanceof QILING_NEW_INFO_RES) {
			NewUserEnterServerService.handle_QILING_NEW_INFO_RES(conn, message);
		} else if (message instanceof HORSE_QILING_INFO_RES) {
			NewUserEnterServerService.handle_HORSE_QILING_INFO_RES(conn, message);
		} else if (message instanceof HORSE_EQUIPMENT_QILING_RES) {
			NewUserEnterServerService.handle_HORSE_EQUIPMENT_QILING_RES(conn, message);
		} else if (message instanceof PET_EQU_QILING_RES) {
			NewUserEnterServerService.handle_PET_EQU_QILING_RES(conn, message);
		} else if (message instanceof MARRIAGE_TRYACTIVITY_RES) {
			NewUserEnterServerService.handle_MARRIAGE_TRYACTIVITY_RES(conn, message);
		} else if (message instanceof PET_TRY_QILING_RES) {
			NewUserEnterServerService.handle_PET_TRY_QILING_RES(conn, message);
		} else if (message instanceof PLAYER_CLEAN_QILINGLIST_RES) {
			NewUserEnterServerService.handle_PLAYER_CLEAN_QILINGLIST_RES(conn, message);
		} else if (message instanceof DELETE_TESK_LIST_RES) {
			NewUserEnterServerService.handle_DELETE_TESK_LIST_RES(conn, message);
		} else if (message instanceof GET_HEIMINGDAI_NEWLIST_RES) {
			NewUserEnterServerService.handle_GET_HEIMINGDAI_NEWLIST_RES(conn, message);
		} else if (message instanceof QUERY_CHOURENLIST_RES) {
			NewUserEnterServerService.handle_QUERY_CHOURENLIST_RES(conn, message);
		} else if (message instanceof QINCHU_PLAYER_RES) {
			NewUserEnterServerService.handle_QINCHU_PLAYER_RES(conn, message);
		} else if (message instanceof REMOVE_FRIEND_LIST_RES) {
			NewUserEnterServerService.handle_REMOVE_FRIEND_LIST_RES(conn, message);
		} else if (message instanceof TRY_REMOVE_CHOUREN_RES) {
			NewUserEnterServerService.handle_TRY_REMOVE_CHOUREN_RES(conn, message);
		} else if (message instanceof REMOVE_CHOUREN_RES) {
			NewUserEnterServerService.handle_REMOVE_CHOUREN_RES(conn, message);
		} else if (message instanceof DELETE_TASK_LIST_RES) {
			NewUserEnterServerService.handle_DELETE_TASK_LIST_RES(conn, message);
		} else if (message instanceof PLAYER_TO_PLAYER_DEAL_RES) {
			NewUserEnterServerService.handle_PLAYER_TO_PLAYER_DEAL_RES(conn, message);
		} else if (message instanceof AUCTION_NEW_LIST_MSG_RES) {
			NewUserEnterServerService.handle_AUCTION_NEW_LIST_MSG_RES(conn, message);
		} else if (message instanceof REQUEST_BUY_PLAYER_INFO_RES) {
			NewUserEnterServerService.handle_REQUEST_BUY_PLAYER_INFO_RES(conn, message);
		} else if (message instanceof BOOTHER_PLAYER_MSGNAME_RES) {
			NewUserEnterServerService.handle_BOOTHER_PLAYER_MSGNAME_RES(conn, message);
		} else if (message instanceof BOOTHER_MSG_CLEAN_RES) {
			NewUserEnterServerService.handle_BOOTHER_MSG_CLEAN_RES(conn, message);
		} else if (message instanceof TRY_REQUESTBUY_CLEAN_ALL_RES) {
			NewUserEnterServerService.handle_TRY_REQUESTBUY_CLEAN_ALL_RES(conn, message);
		} else if (message instanceof SCHOOL_INFONAMES_RES) {
			NewUserEnterServerService.handle_SCHOOL_INFONAMES_RES(conn, message);
		} else if (message instanceof PET_NEW_LEVELUP_RES) {
			NewUserEnterServerService.handle_PET_NEW_LEVELUP_RES(conn, message);
		} else if (message instanceof VALIDATE_ASK_NEW_RES) {
			NewUserEnterServerService.handle_VALIDATE_ASK_NEW_RES(conn, message);
		} else if (message instanceof JINGLIAN_NEW_TRY_RES) {
			NewUserEnterServerService.handle_JINGLIAN_NEW_TRY_RES(conn, message);
		} else if (message instanceof JINGLIAN_NEW_DO_RES) {
			NewUserEnterServerService.handle_JINGLIAN_NEW_DO_RES(conn, message);
		} else if (message instanceof JINGLIAN_PET_RES) {
			NewUserEnterServerService.handle_JINGLIAN_PET_RES(conn, message);
		} else if (message instanceof SEE_NEWFRIEND_LIST_RES) {
			NewUserEnterServerService.handle_SEE_NEWFRIEND_LIST_RES(conn, message);
		} else if (message instanceof EQU_PET_HUN_RES) {
			NewUserEnterServerService.handle_EQU_PET_HUN_RES(conn, message);
		} else if (message instanceof PET_ADD_HUNPO_RES) {
			NewUserEnterServerService.handle_PET_ADD_HUNPO_RES(conn, message);
		} else if (message instanceof PET_ADD_SHENGMINGVALUE_RES) {
			NewUserEnterServerService.handle_PET_ADD_SHENGMINGVALUE_RES(conn, message);
		} else if (message instanceof HORSE_REMOVE_HUNPO_RES) {
			NewUserEnterServerService.handle_HORSE_REMOVE_HUNPO_RES(conn, message);
		} else if (message instanceof PET_REMOVE_HUNPO_RES) {
			NewUserEnterServerService.handle_PET_REMOVE_HUNPO_RES(conn, message);
		} else if (message instanceof PLAYER_CLEAN_HORSEHUNLIANG_RES) {
			NewUserEnterServerService.handle_PLAYER_CLEAN_HORSEHUNLIANG_RES(conn, message);
		} else if (message instanceof GET_NEW_LEVELUP_RES) {
			NewUserEnterServerService.handle_GET_NEW_LEVELUP_RES(conn, message);
		} else if (message instanceof DO_HOSEE2OTHER_RES) {
			NewUserEnterServerService.handle_DO_HOSEE2OTHER_RES(conn, message);
		} else if (message instanceof TRYDELETE_PET_INFO_RES) {
			NewUserEnterServerService.handle_TRYDELETE_PET_INFO_RES(conn, message);
		} else if (message instanceof HAHA_ACTIVITY_MSG_RES) {
			NewUserEnterServerService.handle_HAHA_ACTIVITY_MSG_RES(conn, message);
		} else if (message instanceof VALIDATE_NEW_RES) {
			NewUserEnterServerService.handle_VALIDATE_NEW_RES(conn, message);
		} else if (message instanceof VALIDATE_ANDSWER_NEW_RES) {
			NewUserEnterServerService.handle_VALIDATE_ANDSWER_NEW_RES(conn, message);
		} else if (message instanceof PLAYER_ASK_TO_OTHWE_RES) {
			NewUserEnterServerService.handle_PLAYER_ASK_TO_OTHWE_RES(conn, message);
		} else if (message instanceof GA_GET_SOME_RES) {
			NewUserEnterServerService.handle_GA_GET_SOME_RES(conn, message);
		} else if (message instanceof OTHER_PET_LEVELUP_RES) {
			NewUserEnterServerService.handle_OTHER_PET_LEVELUP_RES(conn, message);
		} else if (message instanceof MY_OTHER_FRIEDN_RES) {
			NewUserEnterServerService.handle_MY_OTHER_FRIEDN_RES(conn, message);
		} else if (message instanceof DOSOME_SB_MSG_RES) {
			NewUserEnterServerService.handle_DOSOME_SB_MSG_RES(conn, message);
		}
		return true;
	}

	public void run() {
		while (running) {
			try {
				synchronized (this) {
					wait(waittingTime);
				}
				Player players[] = playerManager.getOnlinePlayers();
				for (int i = 0; i < players.length; i++) {
					if (players[i].getConn() != null && players[i].getConn().getState() != Connection.CONN_STATE_CLOSE) {
						TIME_SYNC_REQ req = new TIME_SYNC_REQ(GameMessageFactory.nextSequnceNum(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
						framework.sendMessage(players[i].getConn(), req, "synchronize_game_time");
					}
				}
			} catch (Exception e) {
				if (logger.isWarnEnabled()) logger.warn("catch exception in run method:", e);
			}
		}
	}
}
