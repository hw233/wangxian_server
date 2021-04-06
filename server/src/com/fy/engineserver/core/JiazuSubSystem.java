package com.fy.engineserver.core;

import static com.fy.engineserver.datasource.language.Translate.STRING_1;
import static com.fy.engineserver.datasource.language.Translate.translateString;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.activity.TransitRobbery.model.RobberyConstant;
import com.fy.engineserver.activity.doomsday.DoomsdayBossGame;
import com.fy.engineserver.activity.doomsday.DoomsdayManager;
import com.fy.engineserver.activity.silvercar.SilvercarTaskEvent;
import com.fy.engineserver.carbon.devilSquare.DevilSquareManager;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.constants.InitialPlayerConstant;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.downcity.city.CityConfig;
import com.fy.engineserver.downcity.downcity2.DownCityManager2;
import com.fy.engineserver.downcity.downcity2.MapFlop;
import com.fy.engineserver.downcity.downcity3.BossCityManager;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.economic.charge.CardFunction;
import com.fy.engineserver.exception.WrongFormatMailException;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.gateway.SubSystemAdapter;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.JiazuFunction;
import com.fy.engineserver.jiazu.JiazuMember;
import com.fy.engineserver.jiazu.JiazuMember4Client;
import com.fy.engineserver.jiazu.JiazuTitle;
import com.fy.engineserver.jiazu.petHouse.HouseData;
import com.fy.engineserver.jiazu.petHouse.PetHouseManager;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.jiazu2.manager.HuanJingCity;
import com.fy.engineserver.jiazu2.manager.HuanJingInfoForClient;
import com.fy.engineserver.mail.Mail;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.mail.service.concrete.DefaultMailManager;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.Option_JiazuApply_Confirm;
import com.fy.engineserver.menu.Option_JiazuCreate_confirm;
import com.fy.engineserver.menu.Option_JiazuInvite_Confirm;
import com.fy.engineserver.menu.Option_Jiazu_confirm_req_base;
import com.fy.engineserver.menu.Option_Jiazu_dismiss;
import com.fy.engineserver.menu.Option_Jiazu_vicemaster_apply;
import com.fy.engineserver.menu.Option_LeaveJiazu_confirm;
import com.fy.engineserver.menu.Option_UseCancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.jiazu.Option_Jiazu_CallIn;
import com.fy.engineserver.menu.jiazu.Option_Jiazu_contribute;
import com.fy.engineserver.message.CITY_SINGLE_REQ;
import com.fy.engineserver.message.CITY_SINGLE_RES;
import com.fy.engineserver.message.ENTER_CITY_SINGLE_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.JIAZU_APPLY_MASTER_REQ;
import com.fy.engineserver.message.JIAZU_APPLY_REQ;
import com.fy.engineserver.message.JIAZU_APPLY_RES;
import com.fy.engineserver.message.JIAZU_APPOINT_REQ;
import com.fy.engineserver.message.JIAZU_APPOINT_RES;
import com.fy.engineserver.message.JIAZU_APPROVE_APPLY_REQ;
import com.fy.engineserver.message.JIAZU_APPROVE_APPLY_RES;
import com.fy.engineserver.message.JIAZU_BASE_REQ;
import com.fy.engineserver.message.JIAZU_BASE_RES;
import com.fy.engineserver.message.JIAZU_BATCH_SEND_SALARY_RES;
import com.fy.engineserver.message.JIAZU_BEDGE_INFO_RES;
import com.fy.engineserver.message.JIAZU_BUY_BEDGE_REQ;
import com.fy.engineserver.message.JIAZU_BUY_BEDGE_RES;
import com.fy.engineserver.message.JIAZU_CONTRIBUTE_MONEY_REQ;
import com.fy.engineserver.message.JIAZU_CONTRIBUTE_MONEY_RES;
import com.fy.engineserver.message.JIAZU_CREATE_REQ;
import com.fy.engineserver.message.JIAZU_CREATE_RES;
import com.fy.engineserver.message.JIAZU_DISMISS_REQ;
import com.fy.engineserver.message.JIAZU_EXPEL_MEMBER_REQ;
import com.fy.engineserver.message.JIAZU_EXPEL_MEMBER_RES;
import com.fy.engineserver.message.JIAZU_INFO_ADD_TIME_RES;
import com.fy.engineserver.message.JIAZU_INFO_ADD_TIME_RES2;
import com.fy.engineserver.message.JIAZU_INFO_RES;
import com.fy.engineserver.message.JIAZU_INFO_RES2;
import com.fy.engineserver.message.JIAZU_INVITE_REQ;
import com.fy.engineserver.message.JIAZU_LEAVE_RES;
import com.fy.engineserver.message.JIAZU_LIST_REQ;
import com.fy.engineserver.message.JIAZU_LIST_RES;
import com.fy.engineserver.message.JIAZU_LIST_SALARY_RES;
import com.fy.engineserver.message.JIAZU_MASTER_RESIGN_REQ;
import com.fy.engineserver.message.JIAZU_MASTER_RESIGN_RES;
import com.fy.engineserver.message.JIAZU_MODIFY_SLOGAN_REQ;
import com.fy.engineserver.message.JIAZU_MODIFY_SLOGAN_RES;
import com.fy.engineserver.message.JIAZU_P_SALARY_REQ;
import com.fy.engineserver.message.JIAZU_P_SALARY_RES;
import com.fy.engineserver.message.JIAZU_QUERY_APPLY_RES;
import com.fy.engineserver.message.JIAZU_QUERY_BY_ID_REQ;
import com.fy.engineserver.message.JIAZU_QUERY_BY_ID_RES;
import com.fy.engineserver.message.JIAZU_QUERY_MEMBER_SALARY_REQ;
import com.fy.engineserver.message.JIAZU_QUERY_MEMBER_SALARY_RES;
import com.fy.engineserver.message.JIAZU_REPLACE_BEDGE_REQ;
import com.fy.engineserver.message.JIAZU_REPLACE_BEDGE_RES;
import com.fy.engineserver.message.JIAZU_SALARY_CEREMONY_REQ;
import com.fy.engineserver.message.JIAZU_SET_RANK_NAME_REQ;
import com.fy.engineserver.message.JIAZU_SET_RANK_NAME_RES;
import com.fy.engineserver.message.JIAZU_WAREHOUSE_TOPLAYER_REQ;
import com.fy.engineserver.message.JIAZU_WAREHOUSE_TOPLAYER_RES;
import com.fy.engineserver.message.QUERY_JIAZUBOSS_DAMAGE_RES;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.message.SEPTBUILDING_CREATE_REQ;
import com.fy.engineserver.message.SEPTBUILDING_CREATE_RES;
import com.fy.engineserver.message.SEPTBUILDING_CREAT_SEPT_REQ;
import com.fy.engineserver.message.SEPTBUILDING_CREAT_SEPT_RES;
import com.fy.engineserver.message.SEPTBUILDING_DESTROY_REQ;
import com.fy.engineserver.message.SEPTBUILDING_DESTROY_RES;
import com.fy.engineserver.message.SEPTBUILDING_INFO_RES;
import com.fy.engineserver.message.SEPTBUILDING_LVDOWN_REQ;
import com.fy.engineserver.message.SEPTBUILDING_LVDOWN_RES;
import com.fy.engineserver.message.SEPTBUILDING_LVUP_REQ;
import com.fy.engineserver.message.SEPTBUILDING_LVUP_RES;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.TaskEntity;
import com.fy.engineserver.newtask.service.TaskConfig;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.newtask.service.TaskConfig.ModifyType;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sept.exception.ActionIsCDException;
import com.fy.engineserver.sept.exception.BuildingExistException;
import com.fy.engineserver.sept.exception.BuildingNotFoundException;
import com.fy.engineserver.sept.exception.CannotDestoryException;
import com.fy.engineserver.sept.exception.DependBuildingNotLvUpException;
import com.fy.engineserver.sept.exception.IndexHasBuildingException;
import com.fy.engineserver.sept.exception.JiaZuFenyingException;
import com.fy.engineserver.sept.exception.MainBuildNotLvUpException;
import com.fy.engineserver.sept.exception.MaxLvException;
import com.fy.engineserver.sept.exception.NameExistException;
import com.fy.engineserver.sept.exception.OtherBuildingNotLvUpException;
import com.fy.engineserver.sept.exception.OtherInBuildException;
import com.fy.engineserver.sept.exception.ResNotEnoughException;
import com.fy.engineserver.sept.exception.SeptNotExistException;
import com.fy.engineserver.sept.exception.StationExistException;
import com.fy.engineserver.sept.exception.TooManyBiaozhixiangException;
import com.fy.engineserver.sept.exception.WrongLvOnLvDownException;
import com.fy.engineserver.septbuilding.entity.JiazuBedge;
import com.fy.engineserver.septbuilding.entity.SeptBuildingEntity;
import com.fy.engineserver.septbuilding.entity.SeptBuildingEntity4Client;
import com.fy.engineserver.septbuilding.templet.SeptBuildingTemplet;
import com.fy.engineserver.septbuilding.templet.SeptBuildingTemplet.BuildingType;
import com.fy.engineserver.septstation.JiazuBossDamageRecord;
import com.fy.engineserver.septstation.SeptStation;
import com.fy.engineserver.septstation.SeptStationMapTemplet;
import com.fy.engineserver.septstation.service.SeptStationManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.PlayerSimpleInfo;
import com.fy.engineserver.sprite.PlayerSimpleInfoManager;
import com.fy.engineserver.sprite.Sprite;
import com.fy.engineserver.sprite.Team;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.sprite.monster.MemoryMonsterManager;
import com.fy.engineserver.sprite.monster.Monster;
import com.fy.engineserver.sprite.npc.SeptStationNPC;
import com.fy.engineserver.tool.GlobalTool;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.TimeTool;
import com.fy.engineserver.util.TimeTool.TimeDistance;
import com.fy.engineserver.zongzu.manager.ZongPaiManager;
import com.xuanzhi.tools.text.WordFilter;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;

public class JiazuSubSystem extends SubSystemAdapter {

	public static Logger logger = LoggerFactory.getLogger(JiazuSubSystem.class);

	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public String getName() {
		return "JiazuSubSystem";
	}

	public static JiazuSubSystem self = null;
	/**
	 * 创建家族的级别的要求?
	 */
	public static final int CREATE_JIAZU_LEVLE = 20;
	/*
	 * 申请/被邀请 等级
	 */
	public static final int JOINJIAZU_LEVLE = 20;
	/** 家族名字上限 */
	public static final int NAME_MAX = 18;
	/** 宗旨字上限 */
	public static final int SLOGAN_MAX = 60;
	/** 家族boss记录上限 */
	public static final int JIAZUBOSS_LIST_MAX = 10;
	/**
	 * 离开家族的惩罚时间1天，才能加入另一个家族
	 */
	public static long LEAVE_JIAZU_PENALTY_TIME = 24L * 60 * 60 * 1000;
	/**
	 * 重新开启投票为族长的时间
	 */
	public static long REVOTE_MASTER_TIME = 24L * 60 * 60 * 1000;
	/**
	 * 修改家族的称号的花费?
	 */
	public static final int MODIFY_RANK_NAME_COIN = 50;

	public static final int MAX_REQUEST_NUM = 1;
	/**
	 * 修改徽章的花费
	 */
	public static final int MODIFY_ICON_COIN = 50;

	public static JiazuSubSystem getInstance() {
		return self;
	}

	public void init() throws Exception {
		
		self = this;
		ServiceStartRecord.startLog(this);
	}

	public String[] getInterestedMessageTypes() {
		return new String[] { "JIAZU_CREATE_REQ", "JIAZU_LIST_REQ", "JIAZU_APPLY_REQ", "JIAZU_QUERY_APPLY_REQ", 
				"JIAZU_APPROVE_APPLY_REQ", "JIAZU_EXPEL_MEMBER_REQ", "JIAZU_LEAVE_REQ", "JIAZU_QURRY_BASE_LIST_REQ", 
				"JIAZU_BASE_REQ", "JIAZU_MASTER_RESIGN_REQ", "JIAZU_APPLY_MASTER_REQ", "JIAZU_DISMISS_REQ", 
				"JIAZU_MODIFY_SLOGAN_REQ", "JIAZU_APPOINT_REQ", "JIAZU_SET_RANK_NAME_REQ", "JIAZU_SET_ICON_REQ", 
				"JIAZU_LIST_SALARY_REQ", "JIAZU_SALARY_CEREMONY_REQ", "JIAZU_QUERY_MEMBER_SALARY_REQ", "JIAZU_P_SALARY_REQ", 
				"SEPTBUILDING_CREAT_SEPT_REQ", "SEPTBUILDING_QUERY_BUILDING_INFO_REQ", "SEPTBUILDING_QUERY_CANUP_REQ", 
				"SEPTBUILDING_LVDOWN_REQ", "SEPTBUILDING_LVUP_REQ", "SEPTBUILDING_CREATE_REQ", "SEPTBUILDING_DESTROY_REQ", 
				"JIAZU_QURRY_BASE_LIST_RES", "JIAZU_INFO_REQ", "SEPTBUILDING_INFO_REQ", "JIAZU_BEDGE_INFO_REQ", 
				"JIAZU_BUY_BEDGE_REQ", "JIAZU_REPLACE_BEDGE_REQ", "JIAZU_QUERY_BY_ID_REQ", "JIAZU_INVITE_REQ", 
				"JIAZU_CALL_IN_REQ", "JIAZU_BATCH_SEND_SALARY_REQ", "JIAZU_CONTRIBUTE_MONEY_REQ", 
				"JIAZU_WAREHOUSE_TOPLAYER_REQ", "QUERY_JIAZUBOSS_DAMAGE_REQ", "JIAZU_INFO_ADD_TIME_REQ",
				"JIAZU_INFO_REQ2", "JIAZU_INFO_ADD_TIME_REQ2","CITY_SINGLE_REQ","ENTER_CITY_SINGLE_REQ"};
	}

	public String tagforbid[] = new String[] { "'", "\"", " or ", "μ", "Μ", "\n", "\t", " ", "　", "　" };
	public String tagforbid_korea[] = new String[] { "'", "\"", " or ", "μ", "Μ", "\n", "\t", "　", "　" };

	/**
	 * 判断是否含有禁用的标签字符
	 * 
	 * @param name
	 * @return
	 */
	public boolean tagValid(String name) {
		String aname = name.toLowerCase();
		String[] temp = null;
		if (PlatformManager.getInstance().isPlatformOf(Platform.韩国)) {
			temp = tagforbid_korea;
		} else {
			temp = tagforbid;
		}
		for (String s : temp) {
			if (aname.indexOf(s) != -1) {
				return false;
			}
		}
		return true;
	}

	public ResponseMessage handleReqeustMessage(Connection conn, RequestMessage message) throws ConnectionException, Exception {
		Player player = check(conn, message, logger);
		if (useMethodCache) {
			return super.handleReqeustMessage(conn, message, player);
		}

		Class clazz = this.getClass();
		Method m1 = clazz.getDeclaredMethod("handle_" + message.getTypeDescription(), Connection.class, RequestMessage.class, Player.class);
		m1.setAccessible(true);
		return (ResponseMessage) m1.invoke(this, conn, message, player);
	}

	public boolean handleResponseMessage(Connection conn, RequestMessage request, ResponseMessage response) throws ConnectionException, Exception {

		return false;
	}

	/**
	 * 查看家族BOSS伤害统计
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_QUERY_JIAZUBOSS_DAMAGE_REQ(Connection conn, RequestMessage message, Player player) {
		if (player.getCurrentGame() != null) {
			DoomsdayBossGame bossGame = null;
			if (DoomsdayManager.boss1Mapname.equals(player.getCurrentGame().gi.name)) {
				bossGame = DoomsdayManager.getInstance().boss1Games[player.getCountry() - 1];
			} else if (DoomsdayManager.boss2Mapname.equals(player.getCurrentGame().gi.name)) {
				bossGame = DoomsdayManager.getInstance().boss2Games[player.getCountry() - 1];
			}
			if (bossGame != null) {
				long selfDamage = 0;
				int num = bossGame.getBossDamages().size();
				if (num > JIAZUBOSS_LIST_MAX) {
					num = JIAZUBOSS_LIST_MAX;
				}
				JiazuBossDamageRecord[] damageRecords = new JiazuBossDamageRecord[num];
				for (int i = 0; i < bossGame.getBossDamages().size(); i++) {
					if (bossGame.getBossDamages().get(i).getPlayerId() == player.getId()) {
						selfDamage = bossGame.getBossDamages().get(i).getDamage();
					}
					if (i < damageRecords.length) {
						damageRecords[i] = bossGame.getBossDamages().get(i);
					}
				}
				DoomsdayManager.logger.warn("[{}] [取伤害排行] [{}] [{}] [{}] [{}] [{}]", new Object[] { DoomsdayManager.doomsdayLoggerHead, Arrays.toString(damageRecords), player.getLogString(), selfDamage, bossGame.getGame().gi.name, bossGame.getMonster().getName() });
				QUERY_JIAZUBOSS_DAMAGE_RES res = new QUERY_JIAZUBOSS_DAMAGE_RES(message.getSequnceNum(), (byte) 0, bossGame.getMonster().getMaxHP(), selfDamage, damageRecords);
				return res;
			}
		}
		long startTime = SystemTime.currentTimeMillis();
		if (!player.inSelfSeptStation()) {
			player.sendError(Translate.text_jiazu_111);
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[查看家族boss伤害列表] [失败] [不在家族驻地] [jiazuId:" + player.getJiazuId() + "] [当前地图:" + player.getMapName() + "]");
			}
			return new QUERY_JIAZUBOSS_DAMAGE_RES(message.getSequnceNum(), (byte) 1, 0, 0, new JiazuBossDamageRecord[0]);
		}
		Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
		SeptStation septStation = SeptStationManager.getInstance().getSeptStationBySeptId(jiazu.getJiazuID());

		if (!septStation.isJiazuBossalive() || septStation.getThisBoss() == null) {
			player.sendError(Translate.text_jiazu_112);
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[查看家族boss伤害列表] [失败] [家族BOSS不存在] [jiazuId:" + player.getJiazuId() + "] [septStation.isJiazuBossalive():" + septStation.isJiazuBossalive() + "] [septStation.getThisBoss():" + septStation.getThisBoss() + "]");
			}
			return new QUERY_JIAZUBOSS_DAMAGE_RES(message.getSequnceNum(), (byte) 1, 0, 0, new JiazuBossDamageRecord[0]);
		}
		long maxHp = septStation.getThisBoss().getMaxHP();// 此处有风险.boss不存在

		List<JiazuBossDamageRecord> bossDamageRecords = septStation.getBossDamageRecords();
		List<JiazuBossDamageRecord> rerurns = new ArrayList<JiazuBossDamageRecord>(JIAZUBOSS_LIST_MAX);
		JiazuBossDamageRecord selfDR = new JiazuBossDamageRecord();

		boolean findSelf = false;
		for (int i = 0; i < bossDamageRecords.size(); i++) {
			if (findSelf && i >= JIAZUBOSS_LIST_MAX) {// 没找到自己的或者数量小于最大值
				break;
			}
			if (i < JIAZUBOSS_LIST_MAX) {
				rerurns.add(bossDamageRecords.get(i));
			}
			if (!findSelf) {
				if (bossDamageRecords.get(i).getPlayerId() == player.getId()) {
					findSelf = true;
					selfDR = bossDamageRecords.get(i);
				}
			}
		}
		if (logger.isWarnEnabled()) {
			logger.warn(player.getJiazuLogString() + "[查看boss伤害] [成功] [自己伤害" + selfDR.getDamage() + "] [最大血量:" + maxHp + "] [列表长度:" + rerurns.size() + "] [耗时:" + (SystemTime.currentTimeMillis() - startTime) + "ms]");
		}
		return new QUERY_JIAZUBOSS_DAMAGE_RES(message.getSequnceNum(), (byte) 0, maxHp, selfDR.getDamage(), rerurns.toArray(new JiazuBossDamageRecord[0]));
	}

	/**
	 * 查看家族信息
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_JIAZU_INFO_REQ(Connection conn, RequestMessage message, Player player) {
		JIAZU_INFO_RES res;
		try {
			long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
			if (player.isInBattleField() && player.getDuelFieldState() > 0) {
				HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_060);
				player.addMessageToRightBag(nreq);
				return null;
			}
			if (player.isLocked()) {
				HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_061);
				player.addMessageToRightBag(nreq);
				return null;
			}
			if (player.getJiazuId() <= 0) {
				if (logger.isWarnEnabled()) {
					logger.warn(player.getJiazuLogString() + "[查看家族信息] [失败] [没有加入家族] [{}] [{}ms]", new Object[] { player.getJiazuName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				}
				return null;
			}
			Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
			if (jiazu == null) {
				HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_040);
				if (logger.isWarnEnabled()) {
					logger.warn(player.getJiazuLogString() + "[查看家族信息] [失败] [家族不存在] [{}] [{}ms]", new Object[] { player.getJiazuName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				}
				player.addMessageToRightBag(nreq);
				return null;
			}
			Integer[] ids = (jiazu.getBedges() == null ? new ArrayList<Integer>() : jiazu.getBedges()).toArray(new Integer[0]);
			int[] hadId = new int[ids.length];
			for (int i = 0; i < ids.length; i++) {
				hadId[i] = ids[i];
			}
			SeptStation septStation = SeptStationManager.getInstance().getSeptStationById(jiazu.getBaseID());
			long maintai = septStation == null ? 0 : septStation.getInfo().getCurrMaintai();
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[查询家族信息] [成功] [家族成员总数:{}]", new Object[] { jiazu.getMember4Clients().size() });
			}
			GamePlayerManager gpm = (GamePlayerManager) GamePlayerManager.getInstance();
			for (JiazuMember4Client jiazuMember4Client : jiazu.getMember4Clients()) {
				if (gpm.isOnline(jiazuMember4Client.getPlayerId())) {
					Player p = gpm.getPlayer(jiazuMember4Client.getPlayerId());
					jiazuMember4Client.setOnLine(true);
					jiazuMember4Client.setPlayerLevel(p.getLevel());
				} else {
					jiazuMember4Client.setOnLine(false);
				}
			}
			res = new JIAZU_INFO_RES(message.getSequnceNum(), jiazu.getJiazuID(), jiazu.getName(), jiazu.getLevel(), jiazu.getSlogan(), jiazu.getUsedBedge(), (int)jiazu.getConstructionConsume(), jiazu.getTitleAlias(), jiazu.getMember4Clients().toArray(new JiazuMember4Client[0]), SeptStationMapTemplet.getInstance().getBedgeList().toArray(new JiazuBedge[0]), hadId, JiazuBedge.switchCost, jiazu.getBaseID(), maintai, (int)jiazu.getJiazuMoney(), jiazu.getWarScore(), jiazu.getProsperity());
			return res;
		} catch (Exception e) {
			logger.error("[家族信息异常]", e);
		}
		return null;
	}
	public ResponseMessage handle_JIAZU_INFO_REQ2(Connection conn, RequestMessage message, Player player) {
		JIAZU_INFO_RES2 res;
		try {
			long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
			if (player.isInBattleField() && player.getDuelFieldState() > 0) {
				HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_060);
				player.addMessageToRightBag(nreq);
				return null;
			}
			if (player.isLocked()) {
				HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_061);
				player.addMessageToRightBag(nreq);
				return null;
			}
			if (player.getJiazuId() <= 0) {
				if (logger.isWarnEnabled()) {
					logger.warn(player.getJiazuLogString() + "[查看家族信息] [失败] [没有加入家族] [{}] [{}ms]", new Object[] { player.getJiazuName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				}
				return null;
			}
			Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
			if (jiazu == null) {
				HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_040);
				if (logger.isWarnEnabled()) {
					logger.warn(player.getJiazuLogString() + "[查看家族信息] [失败] [家族不存在] [{}] [{}ms]", new Object[] { player.getJiazuName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				}
				player.addMessageToRightBag(nreq);
				return null;
			}
			Integer[] ids = (jiazu.getBedges() == null ? new ArrayList<Integer>() : jiazu.getBedges()).toArray(new Integer[0]);
			int[] hadId = new int[ids.length];
			for (int i = 0; i < ids.length; i++) {
				hadId[i] = ids[i];
			}
			SeptStation septStation = SeptStationManager.getInstance().getSeptStationById(jiazu.getBaseID());
			long maintai = septStation == null ? 0 : septStation.getInfo().getCurrMaintai();
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[查询家族信息] [成功] [家族成员总数:{}]", new Object[] { jiazu.getMember4Clients().size() });
			}
			GamePlayerManager gpm = (GamePlayerManager) GamePlayerManager.getInstance();
			for (JiazuMember4Client jiazuMember4Client : jiazu.getMember4Clients()) {
				if (gpm.isOnline(jiazuMember4Client.getPlayerId())) {
					Player p = gpm.getPlayer(jiazuMember4Client.getPlayerId());
					jiazuMember4Client.setOnLine(true);
					jiazuMember4Client.setPlayerLevel(p.getLevel());
				} else {
					jiazuMember4Client.setOnLine(false);
				}
			}
			res = new JIAZU_INFO_RES2(message.getSequnceNum(), jiazu.getJiazuID(), jiazu.getName(), jiazu.getLevel(), jiazu.getSlogan(), jiazu.getUsedBedge(), jiazu.getConstructionConsume(), jiazu.getTitleAlias(), jiazu.getMember4Clients().toArray(new JiazuMember4Client[0]), SeptStationMapTemplet.getInstance().getBedgeList().toArray(new JiazuBedge[0]), hadId, JiazuBedge.switchCost, jiazu.getBaseID(), maintai, jiazu.getJiazuMoney(), jiazu.getWarScore(), jiazu.getProsperity());
			return res;
		} catch (Exception e) {
			logger.error("[家族信息异常]", e);
		}
		return null;
	}

	public ResponseMessage handle_JIAZU_INFO_ADD_TIME_REQ(Connection conn, RequestMessage message, Player player) {
		JIAZU_INFO_ADD_TIME_RES res;
		try {
			long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
			if (player.isInBattleField() && player.getDuelFieldState() > 0) {
				HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_060);
				player.addMessageToRightBag(nreq);
				return null;
			}
			if (player.isLocked()) {
				HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_061);
				player.addMessageToRightBag(nreq);
				return null;
			}
			if (player.getJiazuId() <= 0) {
				if (logger.isWarnEnabled()) {
					logger.warn(player.getJiazuLogString() + "[查看家族信息] [失败] [没有加入家族] [{}] [{}ms]", new Object[] { player.getJiazuName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				}
				return null;
			}
			Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
			if (jiazu == null) {
				HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_040);
				if (logger.isWarnEnabled()) {
					logger.warn(player.getJiazuLogString() + "[查看家族信息] [失败] [家族不存在] [{}] [{}ms]", new Object[] { player.getJiazuName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				}
				player.addMessageToRightBag(nreq);
				return null;
			}
			Integer[] ids = (jiazu.getBedges() == null ? new ArrayList<Integer>() : jiazu.getBedges()).toArray(new Integer[0]);
			int[] hadId = new int[ids.length];
			for (int i = 0; i < ids.length; i++) {
				hadId[i] = ids[i];
			}
			SeptStation septStation = SeptStationManager.getInstance().getSeptStationById(jiazu.getBaseID());
			long maintai = septStation == null ? 0 : septStation.getInfo().getCurrMaintai();
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[查询家族信息] [成功] [家族成员总数:{}]", new Object[] { jiazu.getMember4Clients().size() });
			}
			GamePlayerManager gpm = (GamePlayerManager) GamePlayerManager.getInstance();
			String[] lastOfflineTime = new String[jiazu.getMember4Clients().size()];
			for (int i = 0; i < jiazu.getMember4Clients().size(); i++) {
				JiazuMember4Client jiazuMember4Client = jiazu.getMember4Clients().get(i);
				if (gpm.isOnline(jiazuMember4Client.getPlayerId())) {
					Player p = gpm.getPlayer(jiazuMember4Client.getPlayerId());
					jiazuMember4Client.setOnLine(true);
					jiazuMember4Client.setPlayerLevel(p.getLevel());
				} else {
					jiazuMember4Client.setOnLine(false);
				}
				long offlineTime = jiazuMember4Client.getJiazuMember().getLastOffLineTime();
				lastOfflineTime[i] = modifyOfflinetime(offlineTime, start);
			}
			res = new JIAZU_INFO_ADD_TIME_RES(message.getSequnceNum(), jiazu.getJiazuID(), jiazu.getName(), jiazu.getLevel(), jiazu.getSlogan(), jiazu.getUsedBedge(), (int)jiazu.getConstructionConsume(), jiazu.getTitleAlias(), jiazu.getMember4Clients().toArray(new JiazuMember4Client[0]), lastOfflineTime, SeptStationMapTemplet.getInstance().getBedgeList().toArray(new JiazuBedge[0]), hadId, JiazuBedge.switchCost, jiazu.getBaseID(), maintai, (int)jiazu.getJiazuMoney(), jiazu.getWarScore(), jiazu.getProsperity());
			return res;
		} catch (Exception e) {
			logger.error("[家族信息异常]", e);
		}
		return null;
	}
	
	public ResponseMessage handle_CITY_SINGLE_REQ(Connection conn, RequestMessage message, Player player) {
		CITY_SINGLE_REQ req = (CITY_SINGLE_REQ)message;
		int cityType = req.getCityType();
		logger.warn("[请求副本界面] [cityType:"+cityType+"] ["+player.getLogString()+"]");
		
		if(cityType == 0){
			int [] cityEnterLimitNum = player.getCityEnterLimitNum();
			long [] cityEnterLimitDate = player.getCityEnterLimitDate();
			int monthCardAddNum = 0;
			int weekNum = 15;
			if(player.ownMonthCard(CardFunction.副本百分200掉落次数每周增加5次)){
				monthCardAddNum = 5;
			}
			if(!TimeTool.instance.isSame(SystemTime.currentTimeMillis(), cityEnterLimitDate[0], TimeDistance.DAY)){
				cityEnterLimitNum[0] = 0;
				cityEnterLimitDate[0] = SystemTime.currentTimeMillis();
			}
			if(!TimeTool.instance.isSameWeek(SystemTime.currentTimeMillis(), cityEnterLimitDate[1])){
				cityEnterLimitNum[1] = 0;
				cityEnterLimitDate[1] = SystemTime.currentTimeMillis();
			}
			player.setCityEnterLimitDate(cityEnterLimitDate);
			player.setCityEnterLimitNum(cityEnterLimitNum);
			cityEnterLimitNum = player.getCityEnterLimitNum();
			
			List<HuanJingInfoForClient> list = new ArrayList<HuanJingInfoForClient>();
			for(CityConfig cityConfig : DownCityManager2.instance.cityConfigs){
				if(cityConfig != null && cityConfig.getJoinType() == 2){
					CityConfig info = cityConfig;
					HuanJingInfoForClient cInfo = new HuanJingInfoForClient();
					cInfo.setBossIcon(info.getBossAvata());
					cInfo.setBossName(info.getBossName());
					cInfo.setBossId(info.getBossId());
					cInfo.setIds(info.getIds());
					cInfo.setNums(info.getNums());
					cInfo.setPlayerLevel(info.getMinLevelLimit());
					cInfo.setJiazuLevel(info.getJiaZuLevelLimit());
					cInfo.setCityName(info.getCityName());
					cInfo.setEnterTimes(cityEnterLimitNum[1]);
					cInfo.setTotleTimes(weekNum + monthCardAddNum);
					list.add(cInfo);
				}
			}
			
			CITY_SINGLE_RES res = new CITY_SINGLE_RES(message.getSequnceNum(),cityType, list.toArray(new HuanJingInfoForClient[0]));
			return res;
		}else if(cityType == 1){
			List<HuanJingInfoForClient> list = new ArrayList<HuanJingInfoForClient>();
			for(CityConfig cityConfig : DownCityManager2.instance.cityConfigs){
				if(cityConfig != null && cityConfig.getJoinType() == 1){
					CityConfig info = cityConfig;
					HuanJingInfoForClient cInfo = new HuanJingInfoForClient();
					cInfo.setBossIcon(info.getBossAvata());
					cInfo.setBossName(info.getBossName());
					cInfo.setBossId(info.getBossId());
					cInfo.setIds(info.getIds());
					cInfo.setNums(info.getNums());
					cInfo.setPlayerLevel(info.getMinLevelLimit());
					cInfo.setJiazuLevel(info.getJiaZuLevelLimit());
					cInfo.setCityName(info.getCityName());
					list.add(cInfo);
				}
			}
			CITY_SINGLE_RES res = new CITY_SINGLE_RES(message.getSequnceNum(),cityType, list.toArray(new HuanJingInfoForClient[0]));
			return res;
		}else if(cityType == 2){
			BossCityManager.getInstance().handleCityPage(0,player);
		}
		player.sendError(Translate.副本类型错误);
		return null;
	}
	public static boolean openTimeLimit = true;
	public String [] allowEnterMaps = {"kunhuagucheng","shangzhouxianjing","wanfayiji","kunlunshengdian","jiuzhoutiancheng","wanfazhiyuan","miemoshenyu"};
	public ResponseMessage handle_ENTER_CITY_SINGLE_REQ(Connection conn, RequestMessage message, Player player) {
		ENTER_CITY_SINGLE_REQ req = (ENTER_CITY_SINGLE_REQ)message;
		String cityName = req.getCityName();
		List<CityConfig> configs = DownCityManager2.instance.cityConfigs;
		CityConfig config = null;
		for(CityConfig c : configs){
			if(c != null && c.getCityName().equals(cityName)){
				config = c;
			}
		}
		if(config == null){
			player.sendError(Translate.服务器配置错误);
			DownCityManager2.logger.warn("[进入副本] [失败:副本信息不存在] [cityName:"+cityName+"] ["+player.getLogString()+"]");
			return null;
		}
		
		if(player.getCurrentGame() == null || player.getCurrentGame().gi == null){
			player.sendError(Translate.当前地图不允许进入副本);
			return null;
		}
		if(!Arrays.asList(allowEnterMaps).contains(player.getCurrentGame().gi.name)){
			player.sendError(Translate.当前地图不允许进入副本+"!");
			return null;
		}
		
		if (player.isFighting()) {
			player.sendError(Translate.战斗状态不能进入);
			return null;
		}
		
		if(config.getJoinType() == 2){
			DownCityManager2.instance.enterCity(cityName,player);
			return null;
		}
		
		Team team = player.getTeam();
		if(team != null){
			player.sendError(Translate.你在组队状态);
			return null;
		}
		Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
		if(jiazu == null){
			player.sendError(Translate.家族不存在);
			return null;
		}
		
		if(player.getLevel() < config.getMinLevelLimit() || player.getLevel() > config.getMaxLevelLimit()){
			player.sendError(Translate.角色等级不满足);
			return null;
		}
		
		if(jiazu.getLevel() < config.getJiaZuLevelLimit()){
			player.sendError(Translate.家族等级不满足);
			return null;
		}
		
		if(openTimeLimit && isSameDay(SystemTime.currentTimeMillis(),player.getLastCityTime())){
			player.sendError(Translate.幻境副本一天只能参加一次);
			return null;
		}
		
		Game newGame = createGame(config.getMapName());
		if(newGame == null){
			player.sendError(Translate.远征活动配置错误);
			DownCityManager2.logger.warn("[进入单人副本] [失败:创建game错误] [cityName:"+cityName+"] [config:"+config+"] ["+player.getLogString()+"]");
			return null;
		}
		
		Sprite boss = refreshBoss(config,newGame);
		if(boss == null){
			player.sendError(Translate.远征活动配置错误);
			return null;
		}
		
		List<MapFlop> flop = DownCityManager2.instance.mapFlops.get(config.getMapName());
		if(flop == null){
			player.sendError(Translate.远征活动副本配置错误);
			return null;
		}
		
		HuanJingCity city = new HuanJingCity(newGame,(Monster)boss,player,flop,config);
		city.id = JiazuManager.getInstance().nextId();
		city.pMap().put(player.getId(), player);
		DownCityManager2.instance.cityMap.put(player.getId(),city);
		DownCityManager2.instance.addCityTask(city);
		player.getCurrentGame().transferGame(player, new TransportData(0, 0, 0, 0, config.getMapName(), config.getPlayerXY()[0], config.getPlayerXY()[1]));
		player.setLastCityTime(SystemTime.currentTimeMillis());
		DownCityManager2.logger.warn("[进入单人副本] [成功] [副本id:{}] [country:{}] [{}] [{}]",new Object[]{city.id, newGame.country, config, player.getLogString()});
		return null;
	}
	Random random = new Random();
	
	public Sprite refreshBoss(CityConfig config,Game game){
		Sprite boss = createBoss(config.getBossId(),config.getBossXY()[0],config.getBossXY()[1]);
		if(boss == null){
			DownCityManager2.logger.warn("[进入单人副本] [失败:创建boss错误] [config:"+config+"]");
			return null;
		}
		
		int [][] areas = config.getAreaXYs();
		int [][] monsterIds = config.getMonsterIds();
		for(int i=0;i<monsterIds.length;i++){
			int [] ids = monsterIds[i];
			int [] xy = areas[i];
			for(int j=0;j<ids.length;j++){
				Sprite monster = createBoss(ids[j],xy[0]+random.nextInt(300),xy[1]+random.nextInt(300));
				if(monster == null){
					DownCityManager2.logger.warn("[进入单人副本] [刷新boss成功:"+boss.getId()+"/"+boss.getName()+"] [刷新小怪失败:"+ids[j]+"]");
					return null;
				}
				game.addSprite(monster);
			}
		}
		game.addSprite(boss);
		DownCityManager2.logger.warn("[进入单人副本] [刷新boss成功:"+boss.getId()+"/"+boss.getName()+"]");
		return boss;
	}
	
	public boolean isSameDay(long time1, long time2) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String str1 = sdf.format(time1);
		String str2 = sdf.format(time2);
		return str1.equals(str2);
	}
	
	public Game createGame(String mapName){
		GameManager gameManager = GameManager.getInstance();
		GameInfo gi = gameManager.getGameInfo(mapName);
		if(gi == null){
			JiazuSubSystem.logger.warn("[家族幻境] [newGame] [失败:对应的地图信息不存在] [{}]", new Object[]{mapName});
			return null;
		}
		try {
			Game newGame = new Game(gameManager,gi);
			newGame.init();
			return newGame;
		} catch (Exception e) {
			e.printStackTrace();
			JiazuSubSystem.logger.warn("[家族幻境] [newGame] [失败:game初始化失败] [{}] [{}]", new Object[]{mapName,e});
			return null;
		}
	}
	
	public Sprite createBoss(int bossId,int x,int y){
		try{
			Sprite sprite = MemoryMonsterManager.getMonsterManager().createMonster(bossId);
			sprite.setX(x);
			sprite.setY(y);
			sprite.setBornPoint(new com.fy.engineserver.core.g2d.Point(x, y));
			return sprite;
		}catch (Exception e) {
			e.printStackTrace();
			JiazuSubSystem.logger.warn("[家族幻境] [副本结束:刷boss异常] [bossId:{}] [{}]",new Object[]{bossId,e});
		}
		return null;
	}
	
	public ResponseMessage handle_JIAZU_INFO_ADD_TIME_REQ2(Connection conn, RequestMessage message, Player player) {
		JIAZU_INFO_ADD_TIME_RES2 res;
		try {
			long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
			if (player.isInBattleField() && player.getDuelFieldState() > 0) {
				HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_060);
				player.addMessageToRightBag(nreq);
				return null;
			}
			if (player.isLocked()) {
				HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_061);
				player.addMessageToRightBag(nreq);
				return null;
			}
			if (player.getJiazuId() <= 0) {
				if (logger.isWarnEnabled()) {
					logger.warn(player.getJiazuLogString() + "[查看家族信息] [失败] [没有加入家族] [{}] [{}ms]", new Object[] { player.getJiazuName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				}
				return null;
			}
			Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
			if (jiazu == null) {
				HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_040);
				if (logger.isWarnEnabled()) {
					logger.warn(player.getJiazuLogString() + "[查看家族信息] [失败] [家族不存在] [{}] [{}ms]", new Object[] { player.getJiazuName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				}
				player.addMessageToRightBag(nreq);
				return null;
			}
			Integer[] ids = (jiazu.getBedges() == null ? new ArrayList<Integer>() : jiazu.getBedges()).toArray(new Integer[0]);
			int[] hadId = new int[ids.length];
			for (int i = 0; i < ids.length; i++) {
				hadId[i] = ids[i];
			}
			SeptStation septStation = SeptStationManager.getInstance().getSeptStationById(jiazu.getBaseID());
			long maintai = septStation == null ? 0 : septStation.getInfo().getCurrMaintai();
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[查询家族信息] [成功] [家族成员总数:{}]", new Object[] { jiazu.getMember4Clients().size() });
			}
			GamePlayerManager gpm = (GamePlayerManager) GamePlayerManager.getInstance();
			String[] lastOfflineTime = new String[jiazu.getMember4Clients().size()];
			for (int i = 0; i < jiazu.getMember4Clients().size(); i++) {
				JiazuMember4Client jiazuMember4Client = jiazu.getMember4Clients().get(i);
				if (gpm.isOnline(jiazuMember4Client.getPlayerId())) {
					Player p = gpm.getPlayer(jiazuMember4Client.getPlayerId());
					jiazuMember4Client.setOnLine(true);
					jiazuMember4Client.setPlayerLevel(p.getLevel());
				} else {
					jiazuMember4Client.setOnLine(false);
				}
				long offlineTime = jiazuMember4Client.getJiazuMember().getLastOffLineTime();
				lastOfflineTime[i] = modifyOfflinetime(offlineTime, start);
			}
			res = new JIAZU_INFO_ADD_TIME_RES2(message.getSequnceNum(), jiazu.getJiazuID(), jiazu.getName(), jiazu.getLevel(), jiazu.getSlogan(), jiazu.getUsedBedge(), jiazu.getConstructionConsume(), jiazu.getTitleAlias(), jiazu.getMember4Clients().toArray(new JiazuMember4Client[0]), lastOfflineTime, SeptStationMapTemplet.getInstance().getBedgeList().toArray(new JiazuBedge[0]), hadId, JiazuBedge.switchCost, jiazu.getBaseID(), maintai, jiazu.getJiazuMoney(), jiazu.getWarScore(), jiazu.getProsperity());
			return res;
		} catch (Exception e) {
			logger.error("[家族信息异常]", e);
		}
		return null;
	}

	public static long OFFTIME_DISTANCE = 1000L * 60 * 60 * 24;
	public static long OFFTIME_HOUR_DISTANCE = 1000L * 60 * 24;
	public static long OFFTIME_MINT_DISTANCE = 1000L * 60 ;
	Random ran = new Random(SystemTime.currentTimeMillis());

	private String modifyOfflinetime(long offlineTime, long now) {
		long timeOff = now - offlineTime;
		if (timeOff <= 0) {
			return Translate.text_jiazu_113;
		}
		if (offlineTime <= 0) {
			return (ran.nextInt(59)+ 1) + Translate.分;
//			return Translate.text_jiazu_113;
		}
		try {
			int offDay = (int) (timeOff / OFFTIME_DISTANCE);
			if (offDay > 0) {
				return offDay + Translate.text_jiazu_114;
			} else {
				int offHour =  (int) (timeOff / OFFTIME_HOUR_DISTANCE);
				if (offHour <= 0) {
					int minut = (int) (timeOff / OFFTIME_MINT_DISTANCE);
					if (minut <= 0) {
						return Translate.text_jiazu_113;
					}
					return minut + Translate.分;
				}
				return offHour + Translate.小时;
			}
		} catch (Exception e) {
			return Translate.text_jiazu_113;
		}
//		switch (offDay) {
//		case 0:
//			return Translate.text_jiazu_113;
//		case 1:
//			return offDay + Translate.text_jiazu_114;
//		case 2:
//			return offDay + Translate.text_jiazu_114;
//		case 3:
//			return offDay + Translate.text_jiazu_114;
//		case 4:
//			return offDay + Translate.text_jiazu_114;
//		case 5:
//			return offDay + Translate.text_jiazu_114;
//		case 6:
//			return offDay + Translate.text_jiazu_114;
//		case 7:
//			return offDay + Translate.text_jiazu_114;
//		default:
//			return "N" + Translate.text_jiazu_114;
//		}
	}

	public ResponseMessage handle_JIAZU_INVITE_REQ(Connection conn, RequestMessage message, Player player) {

		try {
			long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
			if (player.isInBattleField() && player.getDuelFieldState() > 0) {
				HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_060);
				player.addMessageToRightBag(nreq);
				return null;
			}
			if (player.isLocked()) {
				HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_061);
				player.addMessageToRightBag(nreq);
				return null;
			}

			JIAZU_INVITE_REQ req = (JIAZU_INVITE_REQ) message;
			long jiazuId = req.getJizuId();
			long pId = req.getPlayerId();

			if (jiazuId == 0 || jiazuId != player.getJiazuId()) {
				if (logger.isWarnEnabled()) {
					logger.warn(player.getJiazuLogString() + "[查看家族信息] [失败] [没有加入家族] [{}]", new Object[] { player.getJiazuName() });
				}
				return null;
			}
			JiazuMember member = JiazuManager.getInstance().getJiazuMember(player.getId(), jiazuId);
			if (!JiazuTitle.hasPermission(member.getTitle(), JiazuFunction.handle_join_request)) {
				player.sendError(Translate.text_jiazu_047);
				return null;
			}
			Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
			if (jiazu == null) {
				player.sendError(Translate.text_jiazu_040);
				if (logger.isWarnEnabled()) {
					logger.warn(player.getJiazuLogString() + "[查看家族信息] [失败] [家族不存在] [{}] [{}ms]", new Object[] { player.getJiazuName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				}
				return null;
			}
			Player target = null;
			try {
				target = PlayerManager.getInstance().getPlayer(pId);
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (target == null) {
				player.sendError(Translate.text_jiazu_051);
				return null;
			}
			if (target.getJiazuId() != 0) {
				player.sendError(Translate.text_jiazu_062);
				return null;
			}
			if (target.getCountry() != player.getCountry()) {
				player.sendError(Translate.text_jiazu_063);
				return null;
			}

			if (target.getLevel() < JOINJIAZU_LEVLE) {
				player.sendError(Translate.text_jiazu_076);
				return null;
			}
			
			if ((com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - target.getLeaveJiazuTime()) < JiazuSubSystem.LEAVE_JIAZU_PENALTY_TIME) {
				player.sendError(Translate.text_newjiazu_001);
				return null;
			}

			MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(36000);
			mw.setDescriptionInUUB(Translate.translateString(Translate.text_jiazu_064, new String[][] { { Translate.STRING_1, player.getName() }, { Translate.STRING_2, jiazu.getName() } }));
			mw.setTitle(Translate.text_jiazu_054);
			Option_UseCancel oc = new Option_UseCancel();
			oc.setText(Translate.取消);
			oc.setColor(0xffffff);

			Option_JiazuInvite_Confirm confirm = new Option_JiazuInvite_Confirm(jiazuId);
			confirm.setText(Translate.确定);
			oc.setColor(0xffffff);
			mw.setOptions(new Option[] { confirm, oc });
			QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
			target.addMessageToRightBag(res);
		} catch (Exception e) {
			logger.error(player.getJiazuLogString() + "[邀请加入家族] [异常]", e);
		}

		return null;
	}

	/**
	 * 查看驻地内建筑
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_SEPTBUILDING_INFO_REQ(Connection conn, RequestMessage message, Player player) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if (player.isInBattleField() && player.getDuelFieldState() > 0) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_060);
			player.addMessageToRightBag(nreq);
			return null;
		}
		if (player.isLocked()) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_061);
			player.addMessageToRightBag(nreq);
			return null;
		}

		if (player.getJiazuId() <= 0) {
			if (logger.isWarnEnabled()) {
				logger.warn("[查看所有建筑] [失败] [家族不存在] [{}] [jiazuName:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_040);
			player.addMessageToRightBag(nreq);
			return null;
		}

		Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
		if (jiazu == null) {
			if (logger.isWarnEnabled()) {
				logger.warn("[查看所有建筑] [失败] [家族不存在] [{}] [jiazuName:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_041);
			player.addMessageToRightBag(nreq);
			return null;
		}

		JiazuMember member = JiazuManager.getInstance().getJiazuMember(player.getId(), jiazu.getJiazuID());
		if (member == null) {
			if (logger.isWarnEnabled()) {
				logger.warn("[查看所有建筑] [失败] [家族成员不存在] [{}] [jiazuName:{}] [jiazuID:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), jiazu.getJiazuID(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_065);
			player.addMessageToRightBag(nreq);
			return null;
		}
		SeptStation septStation = SeptStationManager.getInstance().getSeptStationById(jiazu.getBaseID());
		if (septStation == null) {
			return null;
		}
		SeptBuildingEntity4Client[] buildingEntity4Clients = new SeptBuildingEntity4Client[septStation.getBuildingEntities().length];
		try {
			for (int i = 0; i < septStation.getBuildingEntities().length; i++) {
				if (septStation.getBuildingEntities()[i] == null) {
					if (logger.isWarnEnabled()) {
						logger.warn(player.getJiazuLogString() + "[查看所有建筑] [异常] [建筑不存在] [{}/{}] 家族:{}", new Object[] { i, septStation.getBuildingEntities().length, jiazu.getName() });
					}
					continue;
				}
				if (septStation.getBuildingEntities()[i].getNpc() == null) {
					if (logger.isWarnEnabled()) {
						logger.warn(player.getJiazuLogString() + "[查看所有建筑] [异常] [建筑npc null] 家族:{},建筑:{}", new Object[] { jiazu.getName(), septStation.getBuildingEntities()[i].getBuildingState().getTempletType() });
					}
					continue;
				}
				buildingEntity4Clients[i] = new SeptBuildingEntity4Client(septStation.getBuildingEntities()[i]);
			}
		} catch (Exception e) {
			logger.error(player.getJiazuLogString() + "[查看家族建筑异常]", e);
		}
		SEPTBUILDING_INFO_RES res = new SEPTBUILDING_INFO_RES(message.getSequnceNum(), buildingEntity4Clients);
		return res;
	}

	/**
	 * 申请加入家族
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_JIAZU_APPLY_REQ(Connection conn, RequestMessage message, Player player) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if (player.isInBattleField() && player.getDuelFieldState() > 0) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_060);
			player.addMessageToRightBag(nreq);
			return null;
		}
		if (player.isLocked()) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_061);
			player.addMessageToRightBag(nreq);
			return null;
		}
		JiazuManager jiazuManager = JiazuManager.getInstance();
		JIAZU_APPLY_REQ req = (JIAZU_APPLY_REQ) message;
		// 用户级别不能小于20
		if (player.getLevel() < JOINJIAZU_LEVLE) {
			JIAZU_APPLY_RES res = new JIAZU_APPLY_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.translateString(Translate.text_jiazu_066, new String[][] { { Translate.COUNT_1, String.valueOf(JOINJIAZU_LEVLE) } }));
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[申请加入家族] [失败] [level<20] [{}] [{}] [{}ms]", new Object[] { player.getName(), player.getLevel(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			return res;
		}

		if (req.getName() == null || req.getName().length() == 0) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_067);
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[申请加入家族] [失败] [家族不存在] [{}] [{}ms]", new Object[] { player.getJiazuName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			player.addMessageToRightBag(nreq);
			return null;
		}
		Jiazu jiazu = jiazuManager.getJiazu(req.getName());
		if (jiazu == null) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_041);
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[申请加入家族] [失败] [家族不存在] [{}] [{}ms]", new Object[] { player.getJiazuName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			player.addMessageToRightBag(nreq);
			return null;
		}
		if (jiazu.getCountry() != player.getCountry()) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_077);
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[申请加入家族] [失败] [国家不符] [家族国家:{}] [角色国家:{}] [{}ms]", new Object[] { jiazu.getCountry(), player.getCountry(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			player.addMessageToRightBag(nreq);
			return null;
		}
		// 用户不能在另外一个家族之中
		if (player.getJiazuName() != null && player.getJiazuName().length() > 0) {
			JIAZU_APPLY_RES res = new JIAZU_APPLY_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.translateString(Translate.text_jiazu_068, new String[][] { { Translate.STRING_1, player.getJiazuName() } }));
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[申请加入家族] [失败] [用户在另一个家族之中] [{}] [{}] [{}] [{}ms]", new Object[] { player.getName(), player.getLevel(), player.getJiazuName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			return res;
		}
		// 离开家族24小时之内不允许加入新的家族
		if ((com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - player.getLeaveJiazuTime()) < LEAVE_JIAZU_PENALTY_TIME) {
			JIAZU_APPLY_RES res = new JIAZU_APPLY_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_jiazu_069);
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[申请加入家族] [失败] [位于离开家族惩罚时间] [{}] [{}] [{}] [leave time] [{}] [{}ms]", new Object[] { player.getName(), player.getLevel(), player.getJiazuName(), (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - player.getLeaveJiazuTime() / 1000), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			return res;
		}

		// 如果玩家有申请的记录，那么要玩家确认是否替换家族
		else if (player.getRequestJiazuName() != null && player.getRequestJiazuName().length() > 0) {
			MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(36000);
			mw.setTitle(Translate.text_jiazu_070);
			mw.setDescriptionInUUB(Translate.translateString(Translate.text_jiazu_071, new String[][] { { Translate.STRING_1, player.getRequestJiazuName() }, { Translate.STRING_2, req.getName() } }));
			Option_UseCancel oc = new Option_UseCancel();
			oc.setText(Translate.取消);
			oc.setColor(0xffffff);
			Option_JiazuApply_Confirm confirm = new Option_JiazuApply_Confirm(req);
			confirm.setText(Translate.确定);
			oc.setColor(0xffffff);
			mw.setOptions(new Option[] { oc, confirm });
			QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
			player.addMessageToRightBag(res);
		}
		// 用户没有申请的记录，直接加入家族的申请列表
		else {
			// 用户的申请的家族的名称
			player.setRequestJiazuName(jiazu.getName());
			jiazu.addRequest(player.getId());
			JIAZU_APPLY_RES res = new JIAZU_APPLY_RES(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.translateString(Translate.text_jiazu_142, new String[][] { { Translate.STRING_1, req.getName() } }));
			player.addMessageToRightBag(res);
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[申请加入家族] [成功] [{}] [{}] [申请加入家族名:{}] [{}ms]", new Object[] { player.getName(), player.getLevel(), req.getName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
		}
		return null;
	}

	public ResponseMessage handle_JIAZU_QUERY_APPLY_REQ(Connection conn, RequestMessage message, Player player) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if (player.isInBattleField() && player.getDuelFieldState() > 0) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_060);
			player.addMessageToRightBag(nreq);
			return null;
		}
		if (player.isLocked()) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_061);
			player.addMessageToRightBag(nreq);
			return null;
		}
		JiazuManager jiazuManager = JiazuManager.getInstance();
		if (player.getJiazuId() <= 0) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_040);
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[请求申请列表] [失败] [家族不存在] [{}] [{}ms]", new Object[] { player.getJiazuName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			player.addMessageToRightBag(nreq);
			return null;
		}
		Jiazu jiazu = jiazuManager.getJiazu(player.getJiazuId());
		if (jiazu == null) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_041);
			if (logger.isWarnEnabled()) {
				logger.warn(player.getLoginServerTime() + "[请求申请列表] [失败] [家族不存在] [{}] [{}ms]", new Object[] { player.getJiazuName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			player.addMessageToRightBag(nreq);
			return null;
		} else {
			JiazuMember member = jiazuManager.getJiazuMember(player.getId(), jiazu.getJiazuID());
			if (member == null) {
				if (logger.isWarnEnabled()) {
					logger.warn(player.getJiazuLogString() + "[请求申请列表] [失败] [家族成员不存在] [{}] [jiazuName:{}] [jiazuID:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), jiazu.getJiazuID(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				}
				HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_051);
				player.addMessageToRightBag(nreq);
				return null;
			}
			if (!JiazuTitle.hasPermission(member.getTitle(), JiazuFunction.handle_join_request)) {
				if (logger.isWarnEnabled()) {
					logger.warn(player.getJiazuLogString() + "[请求申请列表] [失败] [权限不足] [{}] [jiazuName:{}] [jiazuID:{}] [title:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), jiazu.getJiazuID(), member.getTitle(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				}
				HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_047);
				player.addMessageToRightBag(nreq);
			} else {
				Map<Long, Long> requestIDSet = jiazu.getRequestMap();
				PlayerManager playerManager = PlayerManager.getInstance();
				Set<Player> players = new HashSet<Player>();
				Set<Long> nullIds = new HashSet<Long>();
				for (Long id : requestIDSet.keySet()) {
					Player reqplayer = null;
					try {
						reqplayer = playerManager.getPlayer(id);
					} catch (Exception e) {
						nullIds.add(id);
						if (logger.isWarnEnabled()) {
							logger.warn(player.getJiazuLogString() + "[请求申请列表] [失败] [玩家不存在] [" + id + "]" + e.getMessage(), e);
						}
						continue;
					}
					if (reqplayer != null) {
						players.add(reqplayer);
					}
				}
				for (Long nullId : nullIds) {
					jiazu.removeRequest(nullId);
				}
				long[] playerID = new long[players.size()];
				String[] playerName = new String[players.size()];
				String[] applyTime = new String[players.size()];// 申请时间
				int[] playerLevels = new int[players.size()];
				int[] playerCareer = new int[players.size()];
				int index = 0;
				for (Player p : players) {
					playerID[index] = p.getId();
					playerName[index] = p.getName();
					playerLevels[index] = p.getLevel();
					playerCareer[index] = p.getCareer();
					applyTime[index] = TimeTool.formatter.varChar19.format(jiazu.getRequestMap().get(p.getId()));
					index++;
				}

				if (logger.isWarnEnabled()) {
					logger.warn(player.getJiazuLogString() + "[请求申请列表] [成功] [{}] [jiazuName:{}] [jiazuID:{}] [requestSet size={}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), jiazu.getJiazuID(), requestIDSet.size(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				}
				JIAZU_QUERY_APPLY_RES res = new JIAZU_QUERY_APPLY_RES(GameMessageFactory.nextSequnceNum(), playerName, playerID, playerLevels, playerCareer, applyTime);
				return res;
			}

		}

		return null;
	}

	/**
	 * 接收成员
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_JIAZU_APPROVE_APPLY_REQ(Connection conn, RequestMessage message, Player player) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if (player.isInBattleField() && player.getDuelFieldState() > 0) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_060);
			player.addMessageToRightBag(nreq);
			return null;
		}
		if (player.isLocked()) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_061);
			player.addMessageToRightBag(nreq);
			return null;
		}
		JiazuManager jiazuManager = JiazuManager.getInstance();
		MailManager mailManager = DefaultMailManager.getInstance();
		JIAZU_APPROVE_APPLY_REQ req = (JIAZU_APPROVE_APPLY_REQ) message;
		if (player.getJiazuId() <= 0) {
			if (logger.isWarnEnabled()) {
				logger.warn("[处理加入家族申请] [失败] [家族不存在] [{}] [jiazuName:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_040);
			player.addMessageToRightBag(nreq);
			return null;
		}
		Jiazu jiazu = jiazuManager.getJiazu(player.getJiazuId());
		if (jiazu == null) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[处理加入家族申请] [失败] [家族不存在] [{}] [jiazuName:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_041);
			player.addMessageToRightBag(nreq);
			return null;
		}
		JiazuMember member = jiazuManager.getJiazuMember(player.getId(), jiazu.getJiazuID());
		if (member == null) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[处理加入家族申请] [失败] [家族成员不存在] [{}] [jiazuName:{}] [jiazuID:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), jiazu.getJiazuID(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_051);
			player.addMessageToRightBag(nreq);
			return null;
		}
		if (!JiazuTitle.hasPermission(member.getTitle(), JiazuFunction.handle_join_request)) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[处理加入家族申请] [失败] [没有权限] [{}] [jiazuName:{}] [jiazuID:{}] [title:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), jiazu.getJiazuID(), member.getTitle().toString(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			JIAZU_APPROVE_APPLY_RES res = new JIAZU_APPROVE_APPLY_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_jiazu_047);
			return res;
		}
		boolean isApprove = req.getApprove();
		long playerId = req.getPlayerid();
		Player reqPlayer = null;
		try {
			reqPlayer = PlayerManager.getInstance().getPlayer(playerId);
		} catch (Exception e) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[[请求申请列表] [失败] [玩家不存在] [{}]{}", new Object[] { playerId, e.getMessage(), start }, e);
			}
			jiazu.removeRequest(playerId);
			jiazu.notifyFieldChange("requestMap");
			JIAZU_APPROVE_APPLY_RES res = new JIAZU_APPROVE_APPLY_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_jiazu_051);
			return res;
		}
		if (reqPlayer != null) {
			if (reqPlayer.getJiazuId() > 0) {
				JIAZU_APPROVE_APPLY_RES res = new JIAZU_APPROVE_APPLY_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_jiazu_101);
				jiazu.removeRequest(playerId);
				jiazu.notifyFieldChange("requestMap");
				return res;
			}
		}
		// 拒绝申请
		if (!isApprove) {
			JIAZU_APPROVE_APPLY_RES res = new JIAZU_APPROVE_APPLY_RES(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.translateString(Translate.text_jiazu_052, new String[][] { { Translate.STRING_1, reqPlayer.getName() } }));

			// 删除这个玩家的申请
			jiazu.removeRequest(playerId);
			// 清除这个申请的家族的名称
			reqPlayer.setRequestJiazuName(null);
			if (reqPlayer.isOnline()) {
				HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.translateString(Translate.text_jiazu_053, new String[][] { { Translate.STRING_1, jiazu.getName() } }));
				reqPlayer.addMessageToRightBag(nreq);
			} else {
				if (mailManager != null) {
					// 发送邮件
					Mail mail = new Mail();
					mail.setPoster(-1);
					mail.setContent(Translate.translateString(Translate.text_jiazu_053, new String[][] { { Translate.STRING_1, jiazu.getName() } }));
					mail.setReceiver(reqPlayer.getId());
					mail.setTitle(Translate.text_jiazu_054);
					try {
						mail = mailManager.createMail(mail);
						if (MailManager.logger.isWarnEnabled()) {
							MailManager.logger.warn("[创建邮件] [用户加入家族邮件] [成功] [邮件id:{}]  [接受人:{}/{}/{}] [{}] [{}] [{}ms]", new Object[] { mail.getId(), reqPlayer.getId(), reqPlayer.getName(), reqPlayer.getUsername(), mail.getTitle(), mail.getContent(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						}
					} catch (WrongFormatMailException e) {
						mail.setCoins(0);
						mail.setLastModifyDate(new java.util.Date());
						MailManager.logger.error("[创建邮件] [用户加入家族创建邮件] [异常,中断群发] [邮件id:" + mail.getId() + "]  [接收人:" + reqPlayer.getId() + "/" + reqPlayer.getName() + "/" + reqPlayer.getUsername() + "]", e);
						String error = e.getMessage();
						if (error == null) {
							error = Translate.text_jiazu_055;
						}
						HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, error);
						player.addMessageToRightBag(hint);

					}
				}
			}
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[处理加入家族申请] [成功] [拒绝加入] [{}] [jiazuName:{}] [jiazuID:{}] [approve:{}] [被拒玩家name:{}] [是否在线:{}] [level: {}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), jiazu.getJiazuID(), isApprove, reqPlayer.getName(), reqPlayer.isOnline(), jiazu.getLevel(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			return res;
		} else {
			// 批准加入

			if (jiazu.getCountry() != reqPlayer.getCountry()) {
				HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_077);
				if (logger.isWarnEnabled()) {
					logger.warn(player.getJiazuLogString() + "[批准加入家族] [失败] [国家不符] [家族国家:{}] [角色国家:{}] [{}ms]", new Object[] { jiazu.getCountry(), reqPlayer.getCountry(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				}
				jiazu.removeRequest(playerId);
				player.addMessageToRightBag(nreq);
				return null;
			}
			int maxNum = jiazu.maxMember();
			jiazu.initMember4Client();
			if (jiazu.getMember4Clients().size() >= maxNum) {
				if (logger.isWarnEnabled()) {
					logger.warn(player.getJiazuLogString() + "[处理加入家族申请] [失败] [超过人数上限] [{}] [jiazuName:{}] [jiazuID:{}] [maxNum:{}] [level: {}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), jiazu.getJiazuID(), maxNum, jiazu.getLevel(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				}
				JIAZU_APPROVE_APPLY_RES res = new JIAZU_APPROVE_APPLY_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_jiazu_056);
				return res;
			}

			if (!jiazu.getRequestMap().containsKey(playerId)) {
				if (logger.isWarnEnabled()) {
					logger.warn(player.getJiazuLogString() + "[处理加入家族申请] [失败] [用户已经申请其它家族] [{}] [jiazuName:{}] [jiazuID:{}] [maxNum:{}] [level: {}] [req_playerID:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), jiazu.getJiazuID(), maxNum, jiazu.getLevel(), playerId, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				}
				JIAZU_APPROVE_APPLY_RES res = new JIAZU_APPROVE_APPLY_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_jiazu_181);
				return res;
			} else {
				JiazuMember newmember = null;
				try {
					newmember = jiazuManager.createJiauMember(jiazu, reqPlayer, JiazuTitle.civilian);
				} catch (Exception e1) {
					JIAZU_APPROVE_APPLY_RES res = new JIAZU_APPROVE_APPLY_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_jiazu_082);
					logger.error(player.getJiazuLogString() + "[协议] [创建家族成员失败]", e1);
					return res;
				}
				jiazu.addMemberID(newmember.getJiazuMemID());
				jiazu.setMemberModify(true);
				jiazu.getMembers().add(newmember);

				JIAZU_APPROVE_APPLY_RES res = new JIAZU_APPROVE_APPLY_RES(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.translateString(Translate.text_jiazu_057, new String[][] { { Translate.STRING_1, reqPlayer.getName() } }));
				jiazu.removeRequest(playerId);
				reqPlayer.setRequestJiazuName(null);
				reqPlayer.setJiazuName(jiazu.getName());
				reqPlayer.initJiazuTitleAndIcon();
				reqPlayer.initZongPaiName();

				ChatMessageService.getInstance().sendMessageToJiazu(jiazu, Translate.translateString(Translate.text_jiazu_058, new String[][] { { Translate.STRING_1, reqPlayer.getName() } }), Translate.系统);

				// 在线直接发送欢迎消息
				if (reqPlayer.isOnline()) {
					HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.translateString(Translate.text_jiazu_058, new String[][] { { Translate.STRING_1, reqPlayer.getName() } }));
					reqPlayer.addMessageToRightBag(nreq);
				} else {
					if (mailManager != null) {
						// 发送邮件
						Mail mail = new Mail();
						mail.setPoster(-1);
						mail.setContent(Translate.translateString(Translate.text_jiazu_058, new String[][] { { Translate.STRING_1, reqPlayer.getName() } }));
						mail.setReceiver(reqPlayer.getId());

						mail.setTitle(Translate.text_jiazu_054);
						try {
							mail = mailManager.createMail(mail);
							if (MailManager.logger.isWarnEnabled()) {
								MailManager.logger.warn("[创建邮件] [用户加入家族邮件] [成功] [邮件id:{}]  [接受人:{}/{}/{}] [{}] [{}]", new Object[] { mail.getId(), reqPlayer.getId(), reqPlayer.getName(), reqPlayer.getUsername(), mail.getTitle(), mail.getContent() });
							}
						} catch (WrongFormatMailException e) {
							mail.setCoins(0);
							mail.setLastModifyDate(new java.util.Date());
							MailManager.logger.error("[创建邮件] [用户加入家族创建邮件] [异常,中断群发] [邮件id:" + mail.getId() + "]  [接收人:" + reqPlayer.getId() + "/" + reqPlayer.getName() + "/" + reqPlayer.getUsername() + "]", e);
							String error = e.getMessage();
							if (error == null) {
								error = Translate.text_jiazu_055;
							}
							HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, error);
							player.addMessageToRightBag(hint);

						}
					}
				}
				if (reqPlayer.isOnline()) {
					reqPlayer.checkFunctionNPCModify(ModifyType.JIAZU_GOT);
					reqPlayer.checkFunctionNPCModify(ModifyType.JIAZU_TITLE_MODIFY);
				}
				if (logger.isWarnEnabled()) {
					logger.warn(player.getJiazuLogString() + "[处理加入家族申请] [成功] [{}] [jiazuName:{}] [jiazuID:{}] [approve:{}] [加入玩家name:{}] [是否在线:{}] [level: {}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), jiazu.getJiazuID(), isApprove, reqPlayer.getName(), reqPlayer.isOnline(), jiazu.getLevel(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				}
				return res;
			}
		}

	}

	/**
	 * 开除成员
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_JIAZU_EXPEL_MEMBER_REQ(Connection conn, RequestMessage message, Player player) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if (player.isInBattleField() && player.getDuelFieldState() > 0) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_060);
			player.addMessageToRightBag(nreq);
			return null;
		}
		if (player.isLocked()) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_061);
			player.addMessageToRightBag(nreq);
			return null;
		}
		JiazuManager jiazuManager = JiazuManager.getInstance();
		JIAZU_EXPEL_MEMBER_REQ req = (JIAZU_EXPEL_MEMBER_REQ) message;
		long expelPlayerId = req.getPlayerid();
		if (player.getJiazuId() <= 0) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[处理逐出成员] [失败] [家族不存在] [{}] [jiazuName:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_040);
			player.addMessageToRightBag(nreq);
			return null;
		}
		Jiazu jiazu = jiazuManager.getJiazu(player.getJiazuId());
		if (jiazu == null) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[处理逐出成员] [失败] [家族不存在] [{}] [jiazuName:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_041);
			player.addMessageToRightBag(nreq);
			return null;
		}

		JiazuMember targetMember = jiazuManager.getJiazuMember(expelPlayerId, player.getJiazuId());

		if (targetMember == null) {
			JIAZU_EXPEL_MEMBER_RES res = new JIAZU_EXPEL_MEMBER_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_jiazu_051);
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[处理逐出成员] [失败] [家族不存在这个成员] [{}] [jiazuName:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			return res;
		}
		if (player.getId() == req.getPlayerid()) {
			JIAZU_EXPEL_MEMBER_RES res = new JIAZU_EXPEL_MEMBER_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_jiazu_059);
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[处理逐出成员] [失败] [不能逐出自己] [{}] [jiazuName:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			return res;
		}
		JiazuMember jiazuMember = jiazuManager.getJiazuMember(player.getId(), jiazu.getJiazuID());
		if (jiazuMember == null) {
			if (logger.isWarnEnabled()) {
			}
			if (logger.isWarnEnabled()) logger.warn(player.getJiazuLogString() + "[处理逐出成员] [失败] [家族成员不存在] [{}] [jiazuName:{}] [jiazuID:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), jiazu.getJiazuID(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_051);
			player.addMessageToRightBag(nreq);
			return null;
		}
		if (!JiazuTitle.hasPermission(jiazuMember.getTitle(), JiazuFunction.expel)) {
			JIAZU_EXPEL_MEMBER_RES res = new JIAZU_EXPEL_MEMBER_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_jiazu_047);
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[处理逐出成员] [失败] [没有权限] [{}] [jiazuName:{}] [JiazuTitle:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), jiazuMember.getTitle().toString(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			return res;
		}
		
		List<HouseData> pDatas = PetHouseManager.getInstance().getPlayerData(expelPlayerId);
		if(pDatas != null && pDatas.size() > 0){
			JiazuManager.logger.warn("[维护] [处理逐出成员] [失败] [" + jiazu.getLogString() + "] [jiazuMoney:" + jiazu.getJiazuMoney() + "] [level:" + jiazu.getLevel() + "] [" + player.getLogString() + "]");
			return new JIAZU_MASTER_RESIGN_RES(message.getSequnceNum(), (byte) 1, Translate.有宠物正在挂机不能操作);
		}
		
		if (jiazuMember.getTitle().ordinal() > targetMember.getTitle().ordinal()) {
			JIAZU_EXPEL_MEMBER_RES res = new JIAZU_EXPEL_MEMBER_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_jiazu_074);
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[处理逐出成员] [失败] [目标比自己的职位高] [{}] [jiazuName:{}] [JiazuTitle:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), jiazuMember.getTitle().toString(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			return res;
		} else {
			Player expelPlayer = null;
			try {
				expelPlayer = PlayerManager.getInstance().getPlayer(expelPlayerId);
			} catch (Exception e) {
				logger.error(player.getJiazuLogString() + "[处理逐出成员] [失败] [玩家不存在] [{}]{}", new Object[] { expelPlayerId, e.getMessage(), start }, e);
				JIAZU_APPROVE_APPLY_RES res = new JIAZU_APPROVE_APPLY_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_jiazu_051);
				return res;

			}

			boolean suc = false;
			try {
				suc = jiazuManager.removeMember(expelPlayerId, jiazu);
				if (suc) {
					jiazu.setMemberModify(true);
					if (logger.isWarnEnabled()) {
						logger.warn(player.getJiazuLogString() + "[处理逐出成员] [成功] [{}] [expel_player_name:{}] [{}ms]", new Object[] { expelPlayerId, expelPlayer.getName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
					}
					expelPlayer.setJiazuName(null);
					expelPlayer.setJiazuId(0);
					expelPlayer.setLeaveJiazuTime(System.currentTimeMillis());
					expelPlayer.initJiazuTitleAndIcon();
					expelPlayer.setGangContribution(0);
					jiazu.getMembers().remove(jiazuMember);
					// 通知家族的人

					ChatMessageService.getInstance().sendMessageToJiazu(jiazu, Translate.translateString(Translate.text_jiazu_023, new String[][] { { Translate.STRING_1, expelPlayer.getName() }, { Translate.STRING_2, player.getName() } }), "");

					if (logger.isWarnEnabled()) {
						logger.warn(player.getJiazuLogString() + "[开除成员] [成功] [家族:{}] [目标:{}]", new Object[] { jiazu.getName(), expelPlayer.getName() });
					}
					if (expelPlayer.isOnline()) {
						HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.translateString(Translate.text_jiazu_018, new String[][] { { Translate.STRING_1, jiazu.getName() }, { Translate.STRING_2, jiazu.getName() } }));
						expelPlayer.addMessageToRightBag(nreq);

						expelPlayer.checkFunctionNPCModify(ModifyType.JIAZU_LOST);
						if (false) {// 未做优化之前此处无意义
							expelPlayer.checkFunctionNPCModify(ModifyType.JIAZU_TITLE_MODIFY_CURRENT);
						}
					} else {
						MailManager mailManager = DefaultMailManager.getInstance();
						if (mailManager != null) {
							// 发送邮件
							Mail mail = new Mail();
							mail.setPoster(-1);
							mail.setContent(Translate.translateString(Translate.text_jiazu_072, new String[][] { { Translate.STRING_1, jiazu.getName() } }));
							mail.setReceiver(expelPlayer.getId());

							mail.setTitle(Translate.text_jiazu_054);
							try {
								mail = mailManager.createMail(mail);
								if (MailManager.logger.isWarnEnabled()) {
									MailManager.logger.warn("[创建邮件] [驱逐用户出家族邮件] [成功] [邮件id:{}]  [接受人:{}/{}/{}] [{}] [{}]", new Object[] { mail.getId(), expelPlayer.getId(), expelPlayer.getName(), expelPlayer.getUsername(), mail.getTitle(), mail.getContent() });
								}
							} catch (WrongFormatMailException e) {
								mail.setCoins(0);
								mail.setLastModifyDate(new java.util.Date());
								MailManager.logger.error("[创建邮件] [驱逐用户出家族邮件] [异常,中断群发] [邮件id:" + mail.getId() + "]  [接收人:" + expelPlayer.getId() + "/" + expelPlayer.getName() + "/" + expelPlayer.getUsername() + "]", e);
								String error = e.getMessage();
								if (error == null) {
									error = Translate.text_jiazu_055;
								}
								HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, error);
								player.addMessageToRightBag(hint);

							}
						}
					}

					JIAZU_EXPEL_MEMBER_RES res = new JIAZU_EXPEL_MEMBER_RES(req.getSequnceNum(), (byte) 0, Translate.translateString(Translate.text_jiazu_115, new String[][] { { Translate.STRING_1, expelPlayer.getName() } }));
					return res;
				} else {
					if (logger.isWarnEnabled()) {
						logger.warn(player.getJiazuLogString() + "[处理逐出成员] [失败] [{}] [expel_player_name:{}] [{}ms]", new Object[] { expelPlayerId, expelPlayer.getName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
					}
					return null;
				}
			} catch (Exception e) {
				logger.error("[处理逐出成员] [成功] [未知错误] [{}] [expel_player_name:{}] [message:{}] [{}ms]", new Object[] { expelPlayerId, expelPlayer.getName(), e.getMessage(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start }, e);
				return null;
			}

		}

	}

	public ResponseMessage handle_JIAZU_LIST_REQ(Connection conn, RequestMessage message, Player player) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if (player.isInBattleField() && player.getDuelFieldState() > 0) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_060);
			player.addMessageToRightBag(nreq);
			return null;
		}
		if (player.isLocked()) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_061);
			player.addMessageToRightBag(nreq);
			return null;
		}
		JiazuManager jiazuManager = JiazuManager.getInstance();
		JIAZU_LIST_REQ req = (JIAZU_LIST_REQ) message;
		String keyword = req.getKeyword();
		// 关键字的模糊查询
		List<Jiazu> jiazuList = jiazuManager.getJiazuByOrder(keyword, req.getPageindex(), req.getPagenum(), req.getOrderby(), req.getIsasc(), player.getCountry());
		if (jiazuList.size() == 0) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[列表家族] [失败] [size=0] [{}] [{}] [keyword] [{}] [pageIndex] [{}] [pageSize] [{}] [{}ms]", new Object[] { player.getName(), player.getLevel(), req.getKeyword(), req.getPageindex(), req.getPagenum(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_143);
			player.addMessageToRightBag(nreq);
			return null;
		}
		String jiazunames[] = new String[jiazuList.size()];
		String jiazuSlogans[] = new String[jiazuList.size()];
		String jiazuMasters[] = new String[jiazuList.size()];
		boolean jiazuFull[] = new boolean[jiazuList.size()];
		int jiazulevels[] = new int[jiazuList.size()];
		int jiazumembers[] = new int[jiazuList.size()];
		int maxmembers[] = new int[jiazuList.size()];
		for (int i = 0; i < jiazuList.size(); i++) {
			Jiazu jiazu = jiazuList.get(i);
			jiazunames[i] = jiazu.getName();
			jiazulevels[i] = jiazu.getLevel();
			jiazumembers[i] = jiazu.getMemberNum();
			jiazuSlogans[i] = jiazu.getSlogan();
			PlayerSimpleInfo masterPlayer = null;
			try {
				masterPlayer = PlayerSimpleInfoManager.getInstance().getInfoById(JiazuManager.getInstance().getJiazuMember(jiazu.getJiazuID(), JiazuTitle.master).get(0).getPlayerID());
				// master = GamePlayerManager.getInstance().getPlayer(JiazuManager.getInstance().getJiazuMember(jiazu.getJiazuID(), JiazuTitle.master).get(0).getPlayerID());
			} catch (Exception e) {
				logger.error(player.getJiazuLogString() + "[查询家族] [查询玩家异常] [族长不存在]");
			}
			if (masterPlayer != null) {
				jiazuMasters[i] = masterPlayer.getName();
			} else {
				jiazuMasters[i] = "";
			}
			jiazuFull[i] = jiazu.isFull();
			maxmembers[i] = jiazu.maxMember();
		}
		JIAZU_LIST_RES res = new JIAZU_LIST_RES(GameMessageFactory.nextSequnceNum(), jiazunames, jiazulevels, jiazuMasters, jiazuFull, jiazuSlogans, jiazumembers, maxmembers, player.getRequestJiazuName() == null ? "" : player.getRequestJiazuName());
		if (logger.isWarnEnabled()) {
			logger.warn(player.getJiazuLogString() + "[列表家族] [成功] [{}] [{}] [keyword] [{}] [pageIndex] [{}] [pageSize] [{}] [size:{}] [{}ms]", new Object[] { player.getName(), player.getLevel(), req.getKeyword(), req.getPageindex(), req.getPagenum(), jiazuList.size(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
		}
		return res;
	}

	public ResponseMessage handle_JIAZU_LEAVE_REQ(Connection conn, RequestMessage message, Player player) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if (player.isInBattleField() && player.getDuelFieldState() > 0) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_060);
			player.addMessageToRightBag(nreq);
			return null;
		}
		if (player.isLocked()) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_061);
			player.addMessageToRightBag(nreq);
			return null;
		}
		JiazuManager jiazuManager = JiazuManager.getInstance();
		if (player.getJiazuId() <= 0) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[离开家族] [失败] [家族不存在] [{}] [jiazuName:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_040);
			player.addMessageToRightBag(nreq);
			player.setJiazuName(null);
			return null;
		}
		Jiazu jiazu = jiazuManager.getJiazu(player.getJiazuId());
		if (jiazu == null) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[离开家族] [失败] [家族不存在] [{}] [jiazuName:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_041);
			player.addMessageToRightBag(nreq);
			player.setJiazuName(null);
			return null;
		}
		
		
		List<HouseData> pDatas = PetHouseManager.getInstance().getPlayerData(player.getId());
		if(pDatas != null && pDatas.size() > 0){
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.有宠物正在挂机不能操作);
			player.addMessageToRightBag(nreq);
			JiazuManager.logger.warn("[维护] [有宠物正在挂机不能离开] [离开家族] [" + jiazu.getLogString() + "] [jiazuMoney:" + jiazu.getJiazuMoney() + "] [level:" + jiazu.getLevel() + "] [" + player.getLogString() + "]");
			return null;
		}
		
		JiazuMember member = jiazuManager.getJiazuMember(player.getId(), jiazu.getJiazuID());
		if (member == null) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[离开家族] [失败] [家族成员不存在] [{}] [jiazuName:{}] [jiazuID:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), jiazu.getJiazuID(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			JIAZU_LEAVE_RES res = new JIAZU_LEAVE_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_jiazu_125);
			return res;
		}
		JiazuTitle title = member.getTitle();
		if (title == JiazuTitle.master) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[离开家族] [失败] [族长要先禅让才能离开家族] [{}] [jiazuName:{}] [jiazuID:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), jiazu.getJiazuID(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			JIAZU_LEAVE_RES res = new JIAZU_LEAVE_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_jiazu_144);
			return res;
		} else {
			MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(36000);
			mw.setTitle(Translate.text_jiazu_116);
			mw.setDescriptionInUUB(Translate.translateString(Translate.text_jiazu_117, new String[][] { { Translate.STRING_1, jiazu.getName() } }));
			Option_UseCancel oc = new Option_UseCancel();
			oc.setText(Translate.取消);
			oc.setColor(0xffffff);
			Option_LeaveJiazu_confirm confirm = new Option_LeaveJiazu_confirm();
			confirm.setText(Translate.确定);
			oc.setColor(0xffffff);
			mw.setOptions(new Option[] { confirm, oc });
			QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
			return res;
		}
	}

	public ResponseMessage handle_JIAZU_BASE_REQ(Connection conn, RequestMessage message, Player player) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if (player.isInBattleField() && player.getDuelFieldState() > 0) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_060);
			player.addMessageToRightBag(nreq);
			return null;
		}
		if (player.isLocked()) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_061);
			player.addMessageToRightBag(nreq);
			return null;
		}
		JIAZU_BASE_REQ req = (JIAZU_BASE_REQ) message;

		String jiazuPassword = req.getJiazuPassword();
		String jiazuPasswordNotice = req.getJiazuPasswordNotice();
		if (jiazuPassword.getBytes().length > 20) {
			JIAZU_BASE_RES res = new JIAZU_BASE_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_jiazu_084, 0);
			return res;
		}
		if (jiazuPasswordNotice.getBytes().length > 80) {
			JIAZU_BASE_RES res = new JIAZU_BASE_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_jiazu_085, 0);
			return res;
		}
		WordFilter filter = WordFilter.getInstance();
		boolean isTooLong = req.getBaseName().getBytes().length > 20;
		boolean valid = filter.cvalid(req.getBaseName(), 0) && filter.evalid(req.getBaseName(), 1) && tagValid(req.getBaseName());
		JiazuManager jiazuManager = JiazuManager.getInstance();
		if (player.getJiazuId() <= 0) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[申请驻地] [失败] [家族不存在] [{}] [jiazuName:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}

			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_040);
			player.addMessageToRightBag(nreq);
			player.setJiazuName(null);
			return null;
		}
		Jiazu jiazu = jiazuManager.getJiazu(player.getJiazuId());

		if (jiazu == null) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[申请驻地] [失败] [家族不存在] [{}] [jiazuName:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_041);
			player.addMessageToRightBag(nreq);
			player.setJiazuName(null);
			return null;
		}
		if (jiazu.getBaseID() > 0) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[申请驻地] [失败] [已经有驻地 不能重复申请] [{}] [jiazuName:{}] [jiazuID:{}]{}", new Object[] { player.getName(), player.getJiazuName(), jiazu.getJiazuID(), jiazu.getBaseID(), start });
			}
			JIAZU_BASE_RES res = new JIAZU_BASE_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_jiazu_083, 0);
			return res;
		}
		Set<JiazuMember> jiazuMember = jiazuManager.getJiazuMember(jiazu.getJiazuID());
		if (req.getBaseName() == null || req.getBaseName().length() == 0) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[申请驻地] [失败] [申请驻地名称为空] [{}] [jiazuName:{}] [jiazuID:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), jiazu.getJiazuID(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			JIAZU_BASE_RES res = new JIAZU_BASE_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_jiazu_145, 0);
			return res;
		} else if (isTooLong) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[申请驻地] [失败] [驻地名称太长] [{}] [jiazuName:{}] [jiazuID:{}] [req name:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), jiazu.getJiazuID(), req.getBaseName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			JIAZU_BASE_RES res = new JIAZU_BASE_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_jiazu_146, 0);
			return res;
		} else if (!valid) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[申请驻地] [失败] [驻地名称非法] [{}] [jiazuName:{}] [jiazuID:{}] [req name:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), jiazu.getJiazuID(), req.getBaseName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			JIAZU_BASE_RES res = new JIAZU_BASE_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_jiazu_147, 0);
			return res;
		} else if (SeptStationManager.getInstance().getSeptStationByName(req.getBaseName()) != null) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[申请驻地] [失败] [驻地名已存在] [{}] [jiazuName:{}] [jiazuID:{}] [req name:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), jiazu.getJiazuID(), req.getBaseName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			JIAZU_BASE_RES res = new JIAZU_BASE_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_jiazu_148, 0);
			return res;
		} else if (jiazuMember.size() < Jiazu.CREATE_BASE_LESS_NUM) {
			JIAZU_BASE_RES res = new JIAZU_BASE_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.translateString(Translate.text_jiazu_019, new String[][] { { Translate.STRING_1, String.valueOf(Jiazu.CREATE_BASE_LESS_NUM) } }), 0);
			return res;
		} else {
			MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(36000);
			mw.setTitle(Translate.text_jiazu_118);
			mw.setDescriptionInUUB(Translate.translateString(Translate.text_jiazu_007, new String[][] { { Translate.STRING_1, BillingCenter.得到带单位的银两(Jiazu.REQUEST_BASE_MONEY) }, { Translate.STRING_2, Jiazu.REQUEST_BASE_ARTICLE } }));
			Option_UseCancel oc = new Option_UseCancel();
			oc.setText(Translate.取消);
			oc.setColor(0xffffff);

			Option_Jiazu_confirm_req_base confirm = new Option_Jiazu_confirm_req_base(req);
			confirm.setText(Translate.确定);
			oc.setColor(0xffffff);
			mw.setOptions(new Option[] { confirm, oc });
			QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
			player.addMessageToRightBag(res);
		}
		return null;

	}

	public ResponseMessage handle_JIAZU_MASTER_RESIGN_REQ(Connection conn, RequestMessage message, Player player) {
		final long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();

		final JIAZU_MASTER_RESIGN_RES[] ress = new JIAZU_MASTER_RESIGN_RES[1];
		if (player.isInBattleField() && player.getDuelFieldState() > 0) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_060);
			player.addMessageToRightBag(nreq);
			return null;
		}
		if (player.isLocked()) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_061);
			player.addMessageToRightBag(nreq);
			return null;
		}
		JIAZU_MASTER_RESIGN_REQ req = (JIAZU_MASTER_RESIGN_REQ) message;
		JiazuManager jiazuManager = JiazuManager.getInstance();
		final long targetId = req.getPlayerId();

		if (player.getJiazuId() <= 0) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[族长禅让] [失败] [家族不存在] [{}] [jiazuName:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_040);
			player.addMessageToRightBag(nreq);
			player.setJiazuName(null);
			return null;
		}
		{
			// 如果接了押镖任务，不让禅让
			String[] jiazuSilvercarTask = SilvercarTaskEvent.getInstance().getJiazuSilvercarTask();
			boolean hasJiazuCarTask = false;
			for (String taskName : jiazuSilvercarTask) {
				Task task = TaskManager.getInstance().getTask(taskName,player.getCountry());
				if (task == null) continue;
				TaskEntity te = player.getTaskEntity(task.getId());
				if (te == null) continue;
				int status = te.getStatus();
				if (status == TaskConfig.TASK_STATUS_GET || status == TaskConfig.TASK_STATUS_COMPLETE) {
					hasJiazuCarTask = true;
					break;
				}
			}
			if (hasJiazuCarTask) {
				player.sendError(Translate.接取了家族押镖不能离开家族);
				return null;
			}
		}
		Jiazu jiazu = jiazuManager.getJiazu(player.getJiazuId());

		if (jiazu == null) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[族长禅让] [失败] [家族不存在] [{}] [jiazuName:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_041);
			player.addMessageToRightBag(nreq);
			player.setJiazuName(null);
			player.setJiazuId(0L);
			player.setLeaveJiazuTime(System.currentTimeMillis());
			return null;
		}

		JiazuMember member = jiazuManager.getJiazuMember(player.getId(), jiazu.getJiazuID());
		JiazuMember memberTarget = jiazuManager.getJiazuMember(targetId, jiazu.getJiazuID());
		if (player.getCountryPosition() == CountryManager.国王) {
			player.send_HINT_REQ(Translate.text_jiazu_119);
			return null;
		}
		if (member == null) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[族长禅让] [失败] [家族成员不存在] [targetId:{}] [族长:{}] [jiazuName:{}] [jiazuID:{}] [{}ms]", new Object[] { targetId, player.getName(), player.getJiazuName(), jiazu.getJiazuID(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			return new JIAZU_MASTER_RESIGN_RES(message.getSequnceNum(), (byte) 1, Translate.text_jiazu_125);
		}
		
		List<HouseData> pDatas = PetHouseManager.getInstance().getPlayerData(player.getId());
		if(pDatas != null && pDatas.size() > 0){
			JiazuManager.logger.warn("[维护] [失败：有宠物正在挂机不能禅让] [禅让族长] [" + jiazu.getLogString() + "] [jiazuMoney:" + jiazu.getJiazuMoney() + "] [level:" + jiazu.getLevel() + "] [" + player.getLogString() + "]");
			return new JIAZU_MASTER_RESIGN_RES(message.getSequnceNum(), (byte) 1, Translate.有宠物正在挂机不能操作);
		}
		
		if (logger.isWarnEnabled()) {
			logger.warn(player.getJiazuLogString() + "[禅让族长] [家族:{}] [职位:{}] [目标:{}]", new Object[] { jiazu.getName(), member.getTitle(), req.getPlayerId() });
		}
		if (member.getTitle() != JiazuTitle.master) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[族长禅让] [失败] [不是族长] [{}] [jiazuName:{}] [jiazuID:{}] [title:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), jiazu.getJiazuID(), member.getTitle().toString(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			return new JIAZU_MASTER_RESIGN_RES(req.getSequnceNum(), (byte) 1, Translate.text_jiazu_047);
		} else if (memberTarget == null || memberTarget.getTitle() != JiazuTitle.vice_master) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[族长禅让] [失败] [目标不是副族长] [{}] [jiazuName:{}] [jiazuID:{}] [targetTitle:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), jiazu.getJiazuID(), memberTarget.getTitle().toString(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			return new JIAZU_MASTER_RESIGN_RES(req.getSequnceNum(), (byte) 1, Translate.text_jiazu_149);
		} else {
			String password = req.getGiazuPassword();
			if (!password.equals(jiazu.getJiazuPassword())) {
				if (logger.isWarnEnabled()) {
					logger.warn(player.getJiazuLogString() + "[族长禅让] [失败] [密码不正确] [{}] [jiazuName:{}] [jiazuID:{}] [title:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), jiazu.getJiazuID(), member.getTitle().toString(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				}
				return new JIAZU_MASTER_RESIGN_RES(req.getSequnceNum(), (byte) 1, Translate.text_jiazu_136);
			} else {
				Set<JiazuMember> list = jiazuManager.getJiazuMember(jiazu.getJiazuID());
				if (list == null || list.size() == 0) {
					if (logger.isWarnEnabled()) {
						logger.warn(player.getJiazuLogString() + "[族长禅让] [失败] [没有族长] [{}] [jiazuName:{}] [jiazuID:{}] [title:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), jiazu.getJiazuID(), member.getTitle().toString(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
					}
					return new JIAZU_MASTER_RESIGN_RES(req.getSequnceNum(), (byte) 1, Translate.text_jiazu_120);
				} else {
					JiazuMember viceMember = memberTarget;
					PlayerManager playerManager = PlayerManager.getInstance();
					try {
						Player vicePlayer = playerManager.getPlayer(viceMember.getPlayerID());
						jiazuManager.resignMaster(member, viceMember, jiazu.getJiazuID());

						vicePlayer.initJiazuTitleAndIcon();
						player.initJiazuTitleAndIcon();
						jiazu.setMemberModify(true);

						ZongPaiManager.getInstance().setPlayerAsZongPaiMaster(player, vicePlayer);

						ress[0] = new JIAZU_MASTER_RESIGN_RES(req.getSequnceNum(), (byte) 0, Translate.text_jiazu_121);
						if (vicePlayer.isOnline()) {
							HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_122);
							vicePlayer.addMessageToRightBag(nreq);
						} else {
							MailManager mailManager = DefaultMailManager.getInstance();
							// 发送邮件
							Mail mail = new Mail();
							mail.setPoster(-1);
							mail.setContent(Translate.text_jiazu_122);
							mail.setReceiver(vicePlayer.getId());

							mail.setTitle(Translate.text_jiazu_054);
							try {
								mail = mailManager.createMail(mail);
								if (MailManager.logger.isWarnEnabled()) {
									MailManager.logger.warn("[创建邮件] [禅让邮件] [成功] [邮件id:{}]  [接受人:{}/{}/{}] [{}] [{}]", new Object[] { mail.getId(), vicePlayer.getId(), vicePlayer.getName(), vicePlayer.getUsername(), mail.getTitle(), mail.getContent() });
								}
							} catch (WrongFormatMailException e) {
								mail.setCoins(0);
								mail.setLastModifyDate(new java.util.Date());
								MailManager.logger.error("[创建邮件] [禅让邮件] [异常,中断群发] [邮件id:" + mail.getId() + "]  [接收人:" + vicePlayer.getId() + "/" + vicePlayer.getName() + "/" + vicePlayer.getUsername() + "]", e);
								String error = e.getMessage();
								if (error == null) {
									error = Translate.text_jiazu_055;
								}
								HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, error);
								player.addMessageToRightBag(hint);

							}
						}
						if (logger.isWarnEnabled()) {
							logger.warn(player.getJiazuLogString() + "[族长禅让] [成功] [{}] [jiazuName:{}] [jiazuID:{}] [title:{}] [size={}] [vicePlayerID:{}] [vicePlayerName:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), jiazu.getJiazuID(), member.getTitle().toString(), list.size(), viceMember.getPlayerID(), vicePlayer.getName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						}

					} catch (Exception e) {
						logger.error(player.getJiazuLogString() + "[族长禅让] [失败] [找不到副族长的Player] [" + player.getName() + "] [jiazuName:" + player.getJiazuName() + "] [jiazuID:" + jiazu.getJiazuID() + "] [title:" + member.getTitle().toString() + "] [size=" + list.size() + "] [vicePlayerID:" + viceMember.getPlayerID() + "]", e);
						return new JIAZU_MASTER_RESIGN_RES(req.getSequnceNum(), (byte) 1, Translate.text_jiazu_051);
					}

				}

			}
		}
		return ress[0];
	}

	public ResponseMessage handle_JIAZU_DISMISS_REQ(Connection conn, RequestMessage message, Player player) {
		final long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if (player.isInBattleField() && player.getDuelFieldState() > 0) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_060);
			player.addMessageToRightBag(nreq);
			return null;
		}
		if (player.isLocked()) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_061);
			player.addMessageToRightBag(nreq);
			return null;
		}
		JIAZU_DISMISS_REQ req = (JIAZU_DISMISS_REQ) message;
		JiazuManager jiazuManager = JiazuManager.getInstance();
		if (player.getJiazuId() <= 0) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[解散家族] [失败] [家族不存在] [{}] [jiazuName:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_040);
			player.addMessageToRightBag(nreq);
			player.setJiazuName(null);
			return null;
		}

		Jiazu jiazu = jiazuManager.getJiazu(player.getJiazuId());
		if (jiazu == null) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[解散家族] [失败] [家族不存在] [{}] [jiazuName:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_041);
			player.addMessageToRightBag(nreq);
			player.setJiazuName(null);
			return null;
		}
//		if (SiFangManager.getInstance().isJiaZuInSiFang(player.getJiazuId())) {
//			if (logger.isWarnEnabled()) {
//				logger.warn(player.getJiazuLogString() + "[解散家族] [失败] [家族正在四方神兽活动中] [{}] [jiazuName:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
//			}
//			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, "家族正在五方圣兽活动中，不能解散");
//			player.addMessageToRightBag(nreq);
//			return null;
//		}
		
		List<HouseData> jiazuDatas = PetHouseManager.getInstance().getJiazuDatas(jiazu.getJiazuID());
		if(jiazuDatas != null && jiazuDatas.size() > 0){
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.家族中有宠物正在挂机不能解散);
			player.addMessageToRightBag(nreq);
			return null;
		}
		
		JiazuMember member = jiazuManager.getJiazuMember(player.getId(), jiazu.getJiazuID());
		if (member == null) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[解散家族] [失败] [家族成员不存在] [{}] [jiazuName:{}] [jiazuID:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), jiazu.getJiazuID(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_125);
			player.addMessageToRightBag(nreq);

		} else if (member.getTitle() != JiazuTitle.master) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[解散家族] [失败] [不是族长] [{}] [jiazuName:{}] [jiazuID:{}] [title:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), jiazu.getJiazuID(), member.getTitle().toString(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			HINT_REQ res = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_047);
			player.addMessageToRightBag(res);
		} else {
			String password = req.getGiazuPassword();
			if (jiazu.getLevel() > 0 && !password.equals(jiazu.getJiazuPassword())) {
				if (logger.isWarnEnabled()) {
					logger.warn(player.getJiazuLogString() + "[解散家族] [失败] [密码不正确] [{}] [jiazuName:{}] [jiazuID:{}] [title:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), jiazu.getJiazuID(), member.getTitle().toString(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				}
				JIAZU_MASTER_RESIGN_RES res = new JIAZU_MASTER_RESIGN_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_jiazu_136);
				player.addMessageToRightBag(res);
			} else {
				if (player.getCountryPosition() == CountryManager.国王) {
					player.send_HINT_REQ(Translate.国王不能解散家族);
					return null;
				}
				// 确认解散家族
				MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(36000);
				mw.setTitle(Translate.text_jiazu_123);
				mw.setDescriptionInUUB(Translate.translateString(Translate.text_jiazu_124, new String[][] { { Translate.STRING_1, jiazu.getName() } }));
				Option_UseCancel oc = new Option_UseCancel();
				oc.setText(Translate.取消);
				oc.setColor(0xffffff);

				Option_Jiazu_dismiss confirm = new Option_Jiazu_dismiss();
				confirm.setText(Translate.确定);
				oc.setColor(0xffffff);
				mw.setOptions(new Option[] { oc, confirm });
				QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());

				if (logger.isWarnEnabled()) {
					logger.warn(player.getJiazuLogString() + "[解散家族] [待族长确认窗口] [{}] [jiazuName:{}] [jiazuID:{}] [title:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), jiazu.getJiazuID(), member.getTitle().toString(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				}
				player.addMessageToRightBag(res);
			}
		}
		return null;

	}

	/**
	 * 申请成为族长
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_JIAZU_APPLY_MASTER_REQ(Connection conn, RequestMessage message, Player player) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if (player.isInBattleField() && player.getDuelFieldState() > 0) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_060);
			player.addMessageToRightBag(nreq);
			return null;
		}
		if (player.isLocked()) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_061);
			player.addMessageToRightBag(nreq);
			return null;
		}
		JIAZU_APPLY_MASTER_REQ req = (JIAZU_APPLY_MASTER_REQ) message;
		JiazuManager jiazuManager = JiazuManager.getInstance();
		if (player.getJiazuId() == 0) {
			if (logger.isWarnEnabled()) {
				logger.warn("[副族长申请为族长] [失败] [尚未加入家族] [{}] [jiazuName:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_040);
			player.addMessageToRightBag(nreq);
			player.setJiazuName(null);
			return null;
		}
		Jiazu jiazu = jiazuManager.getJiazu(player.getJiazuName());
		if (jiazu == null) {
			if (logger.isWarnEnabled()) {
				logger.warn("[副族长申请为族长] [失败] [家族不存在] [{}] [jiazuName:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_041);
			player.addMessageToRightBag(nreq);
			player.setJiazuName(null);
			return null;
		}
		if (logger.isWarnEnabled()) {
			logger.warn(player.getJiazuLogString() + "[申请成为族长]");
		}
		JiazuMember member = jiazuManager.getJiazuMember(player.getId(), jiazu.getJiazuID());
		if (member == null) {
			if (logger.isWarnEnabled()) {
				logger.warn("[副族长申请为族长] [失败] [家族成员不存在] [{}] [jiazuName:{}] [jiazuID:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), jiazu.getJiazuID(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_040);
			player.addMessageToRightBag(nreq);
			return null;
		} else if (member.getTitle() != JiazuTitle.vice_master) {
			if (logger.isWarnEnabled()) {
				logger.warn("[副族长申请为族长] [失败] [不是副族长] [{}] [jiazuName:{}] [jiazuID:{}] [title:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), jiazu.getJiazuID(), member.getTitle().toString(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			JIAZU_MASTER_RESIGN_RES res = new JIAZU_MASTER_RESIGN_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_jiazu_042);
			return res;
		} else {
			if ((com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - jiazu.getLastVoteTime()) < REVOTE_MASTER_TIME) {
				JIAZU_MASTER_RESIGN_RES res = new JIAZU_MASTER_RESIGN_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_jiazu_043);
				if (logger.isWarnEnabled()) {
					logger.warn("[副族长申请为族长] [失败] [未到重新开启投票的时间] [{}] [jiazuName:{}] [jiazuID:{}] [title:{}] [lastVotetime:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), jiazu.getJiazuID(), member.getTitle().toString(), (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - jiazu.getLastVoteTime()), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				}
				return res;
			} else {
				// 发起确定的投票
				MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(36000);
				mw.setTitle(Translate.text_jiazu_044);
				mw.setDescriptionInUUB(Translate.text_jiazu_045);
				Option_UseCancel oc = new Option_UseCancel();
				oc.setText(Translate.取消);
				oc.setColor(0xffffff);
				Option_Jiazu_vicemaster_apply confirm = new Option_Jiazu_vicemaster_apply();
				confirm.setText(Translate.确定);
				oc.setColor(0xffffff);
				mw.setOptions(new Option[] { oc, confirm });
				QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
				player.addMessageToRightBag(res);
				if (logger.isWarnEnabled()) {
					logger.warn("[副族长申请为族长] [待副族长确认窗口] [{}] [jiazuName:{}] [jiazuID:{}] [title:{}] [lastVotetime:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), jiazu.getJiazuID(), member.getTitle().toString(), (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - jiazu.getLastVoteTime()), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				}
			}

		}
		return null;
	}

	/**
	 * 修改家族标语
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_JIAZU_MODIFY_SLOGAN_REQ(Connection conn, RequestMessage message, Player player) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if (player.isInBattleField() && player.getDuelFieldState() > 0) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_060);
			player.addMessageToRightBag(nreq);
			return null;
		}
		if (player.isLocked()) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_061);
			player.addMessageToRightBag(nreq);
			return null;
		}
		JIAZU_MODIFY_SLOGAN_REQ req = (JIAZU_MODIFY_SLOGAN_REQ) message;
		JiazuManager jiazuManager = JiazuManager.getInstance();

		if (player.getJiazuId() <= 0) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[修改家族宗旨] [失败] [玩家尚未加入家族] [{}] [jiazuName:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_040);
			player.addMessageToRightBag(nreq);
			player.setJiazuName(null);
			return null;
		}
		Jiazu jiazu = jiazuManager.getJiazu(player.getJiazuName());
		if (jiazu == null) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[修改家族宗旨] [失败] [家族不存在] [{}] [jiazuName:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_041);
			player.addMessageToRightBag(nreq);
			player.setJiazuName(null);
			return null;
		}

		if (logger.isWarnEnabled()) {
			logger.warn(player.getJiazuLogString() + "[修改标语] [旧的标语:{}]", new Object[] { jiazu.getSlogan() });
		}
		JiazuMember member = jiazuManager.getJiazuMember(player.getId(), jiazu.getJiazuID());
		if (member == null) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[修改家族宗旨] [失败] [家族成员不存在] [{}] [jiazuName:{}] [jiazuID:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), jiazu.getJiazuID(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_125);
			player.addMessageToRightBag(nreq);
			return null;
		} else if (!JiazuTitle.hasPermission(member.getTitle(), JiazuFunction.modify_slogan)) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[修改家族宗旨] [失败] [不是副族长或族长] [{}] [jiazuName:{}] [jiazuID:{}] [title:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), jiazu.getJiazuID(), member.getTitle().toString(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			JIAZU_MODIFY_SLOGAN_RES res = new JIAZU_MODIFY_SLOGAN_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, "", Translate.text_jiazu_047);
			return res;
		} else {
			WordFilter filter = WordFilter.getInstance();
			boolean valid = req.getSlogan().getBytes().length <= 60 && filter.cvalid(req.getSlogan(), 0) && filter.evalid(req.getSlogan(), 1) && tagValid(req.getSlogan());
			if (!valid) {
				if (logger.isWarnEnabled()) {
					logger.warn(player.getJiazuLogString() + "[修改家族宗旨] [失败] [有非法字符或过长] [{}] [jiazuName:{}] [jiazuID:{}] [title:{}] [slogan:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), jiazu.getJiazuID(), member.getTitle().toString(), req.getSlogan(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				}
				JIAZU_MODIFY_SLOGAN_RES res = new JIAZU_MODIFY_SLOGAN_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, "", Translate.translateString(Translate.text_jiazu_087, new String[][] { { Translate.COUNT_1, String.valueOf(30) } }));
				return res;
			} else {
				jiazu.setSlogan(req.getSlogan() == null ? "" : req.getSlogan());
				if (logger.isWarnEnabled()) {
					logger.warn(player.getJiazuLogString() + "[修改家族宗旨] [成功] [{}] [jiazuName:{}] [jiazuID:{}] [title:{}] [slogan:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), jiazu.getJiazuID(), member.getTitle().toString(), req.getSlogan(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				}
				ChatMessageService.getInstance().sendMessageToJiazu(jiazu, jiazu.getSlogan(), "");
				JIAZU_MODIFY_SLOGAN_RES res = new JIAZU_MODIFY_SLOGAN_RES(GameMessageFactory.nextSequnceNum(), (byte) 0, jiazu.getSlogan(), "");
				return res;
			}

		}
	}

	/**
	 * 家族任命
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_JIAZU_APPOINT_REQ(Connection conn, RequestMessage message, Player player) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if (player.isInBattleField() && player.getDuelFieldState() > 0) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_060);
			player.addMessageToRightBag(nreq);
			return null;
		}
		if (player.isLocked()) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_061);
			player.addMessageToRightBag(nreq);
			return null;
		}
		JIAZU_APPOINT_REQ req = (JIAZU_APPOINT_REQ) message;
		JiazuManager jiazuManager = JiazuManager.getInstance();
		if (player.getJiazuId() <= 0) {
			if (logger.isWarnEnabled()) {
				logger.warn("[家族任命] [失败] [玩家尚未加入家族] [{}] [jiazuName:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_040);
			player.addMessageToRightBag(nreq);
			return null;
		}
		Jiazu jiazu = jiazuManager.getJiazu(player.getJiazuName());
		if (jiazu == null) {
			if (logger.isWarnEnabled()) {
				logger.warn("[家族任命] [失败] [家族不存在] [{}] [jiazuName:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_041);
			player.addMessageToRightBag(nreq);
			player.setJiazuName(null);
			return null;
		}

		JiazuMember selfMember = jiazuManager.getJiazuMember(player.getId(), jiazu.getJiazuID());
		if (selfMember == null) {
			if (logger.isWarnEnabled()) {
				if (logger.isWarnEnabled()) {
					logger.warn(player.getJiazuLogString() + "[家族任命] [失败] [家族成员不存在] [{}] [jiazuName:{}] [jiazuID:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), jiazu.getJiazuID(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				}
			}
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_040);
			player.addMessageToRightBag(nreq);
			return null;
		}

		long appointPlayerID = req.getAppointPlayerID();
		if (appointPlayerID == player.getId()) {
			JIAZU_APPOINT_RES res = new JIAZU_APPOINT_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_jiazu_046);
			if (logger.isWarnEnabled()) {
				logger.warn("[家族任命] [失败] [不可以任命自已为其它职位] [{}] [appointPlayerID:{}] [title:{}] [{}ms]", new Object[] { player.getName(), appointPlayerID, JiazuTitle.values()[req.getTitleID()].toString(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			return res;
		} else if (selfMember.getTitle() == JiazuTitle.vice_master && req.getTitleID() <= 1) {
			if (logger.isWarnEnabled()) {
				logger.warn("[家族任命] [失败] [副族长任命自已为族长或副族长] [{}] [appointPlayerID:{}] [title:{}] [{}ms]", new Object[] { player.getName(), appointPlayerID, JiazuTitle.values()[req.getTitleID()].toString(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			JIAZU_APPOINT_RES res = new JIAZU_APPOINT_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_jiazu_047);
			return res;
		} else if (selfMember.getTitle() == JiazuTitle.law_elder && req.getTitleID() <= 2) {
			if (logger.isWarnEnabled()) {
				logger.warn("[家族任命] [失败] [执法长老任命自已为族长/副族长/执法长老] [{}] [appointPlayerID:{}] [title:{}] [{}ms]", new Object[] { player.getName(), appointPlayerID, JiazuTitle.values()[req.getTitleID()].toString(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			JIAZU_APPOINT_RES res = new JIAZU_APPOINT_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_jiazu_047);
			return res;
		} else if (selfMember.getTitle() == JiazuTitle.master && req.getTitleID() == 1) {
			if(jiazu.getLevel()==0){
				JIAZU_APPOINT_RES res = new JIAZU_APPOINT_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.家族等级不足);
				return res;
			}
			if (!req.getJiazupassword().equals(jiazu.getJiazuPassword())) {
				if (logger.isWarnEnabled()) {
					logger.warn("[家族任命] [失败] [族长任命副族长，密码不正确] [{}] [appointPlayerID:{}] [title:{}] [{}ms]", new Object[] { player.getName(), appointPlayerID, JiazuTitle.values()[req.getTitleID()].toString(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				}
				JIAZU_APPOINT_RES res = new JIAZU_APPOINT_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_jiazu_048);
				return res;
			}

		}
		if (req.getTitleID() > JiazuTitle.values().length - 1) {
			JIAZU_APPOINT_RES res = new JIAZU_APPOINT_RES(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_075);
			logger.error(player.getJiazuLogString() + "[家族任命] [异常] [疑似外挂] [申请职位索引:{}] [实际最大职位索引:{}]", new Object[] { req.getTitleID(), JiazuTitle.values().length });
			return res;
		}
		PlayerManager playerManager = PlayerManager.getInstance();
		// 族长,副族长，执法长老，只能把赋比自已小的权力
		if (selfMember.getTitle().ordinal() <= 2 && selfMember.getTitle().ordinal() < req.getTitleID()) {
			if (appointPlayerID == selfMember.getPlayerID() && (selfMember.getTitle() == JiazuTitle.vice_master || selfMember.getTitle() == JiazuTitle.law_elder)) {
				int leftCount = jiazuManager.getTitleLeft(jiazu, JiazuTitle.values()[req.getTitleID()]);
				if (leftCount == 0) {
					if (logger.isWarnEnabled()) {
						logger.warn("[家族任命] [失败] [副族长，执法长老任命自已为其它职位，职位已经没有空余] [{}] [appointPlayerID:{}] [title:{}] [{}ms]", new Object[] { player.getName(), appointPlayerID, JiazuTitle.values()[req.getTitleID()].toString(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
					}
					JIAZU_APPOINT_RES res = new JIAZU_APPOINT_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_jiazu_049);
					return res;
				} else {
					selfMember.setTitle(JiazuTitle.values()[req.getTitleID()]);
					if (logger.isWarnEnabled()) {
						logger.warn("[家族任命] [成功] [{}] [appointPlayerID:{}] [title:{}] [{}ms]", new Object[] { player.getName(), appointPlayerID, JiazuTitle.values()[req.getTitleID()].toString(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
					}
					jiazu.initMember();
					// jiazu.setMemberModify(true);
					JIAZU_APPOINT_RES res = new JIAZU_APPOINT_RES(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_050);
					return res;
				}
			} else {
				int leftCount = jiazuManager.getTitleLeft(jiazu, JiazuTitle.values()[req.getTitleID()]);
				try {
					if (leftCount > 0) {
						Player appplayer = playerManager.getPlayer(appointPlayerID);
						JiazuMember appMember = jiazuManager.getJiazuMember(appplayer.getId(), jiazu.getJiazuID());
						if (appMember == null) {
							JIAZU_APPOINT_RES res = new JIAZU_APPOINT_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_jiazu_030);
							return res;
						}
						if (appMember.getTitle().ordinal() <= selfMember.getTitle().ordinal()) {// 不能任命比自己职务高的人
							JIAZU_APPOINT_RES res = new JIAZU_APPOINT_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_jiazu_102);
							return res;
						}
						if (logger.isWarnEnabled()) {
							logger.warn(player.getJiazuLogString() + "[任命成员] [成员:{}] [旧的职务:{}]", new Object[] { req.getAppointPlayerID(), appMember.getTitleIndex() });
						}
						if (appMember.getTitleIndex() == req.getTitleID()) {
							return new JIAZU_APPOINT_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_jiazu_086);
						}
						appMember.setTitle(JiazuTitle.values()[req.getTitleID()]);
						appplayer.initJiazuTitleAndIcon();
						// 给授权者返回任命成功
						JIAZU_APPOINT_RES res = new JIAZU_APPOINT_RES(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_137);
						jiazu.setMemberModify(true);
						if (appplayer.isOnline()) {
							// 给被授权者返回你的家族权限发生改变
							HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_050);
							appplayer.addMessageToRightBag(nreq);
							appplayer.noticeJiazuTitleChange(jiazu);
							appplayer.checkFunctionNPCModify(ModifyType.JIAZU_TITLE_MODIFY);
							appplayer.checkFunctionNPCModify(ModifyType.JIAZU_TITLE_MODIFY_CURRENT);
						}
						if (logger.isWarnEnabled()) {
							logger.warn("[家族任命] [成功] [{}] [appointPlayerID:{}] [title:{}] [{}ms]", new Object[] { player.getName(), appointPlayerID, JiazuTitle.values()[req.getTitleID()].toString(), SystemTime.currentTimeMillis() - start });
						}
						jiazu.initMember4Client();
						if (logger.isWarnEnabled()) {
							logger.warn(player.getJiazuLogString() + "[任命成员] [成功] [成员:{}] [新的职务:{}]", new Object[] { req.getAppointPlayerID(), appMember.getTitleIndex() });
						}
						return res;
					} else {
						if (logger.isWarnEnabled()) {
							logger.warn("[家族任命] [失败] [职位已没有空余] [{}] [appointPlayerID:{}] [title:{}] [leftCount={}] [{}ms]", new Object[] { player.getName(), appointPlayerID, JiazuTitle.values()[req.getTitleID()].toString(), leftCount, SystemTime.currentTimeMillis() - start });
						}
						JIAZU_APPOINT_RES res = new JIAZU_APPOINT_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_jiazu_049);
						return res;
					}

				} catch (Exception e) {
					JIAZU_APPOINT_RES res = new JIAZU_APPOINT_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_jiazu_051);
					logger.error(player.getJiazuLogString() + "[家族任命] [失败] [任命的玩家不存在] [" + player.getName() + "] [appointPlayerID:" + appointPlayerID + "] [title:" + JiazuTitle.values()[req.getTitleID()].toString() + "]", e);
					return res;

				}
			}

		} else {
			if (logger.isWarnEnabled()) {
				logger.warn("[家族任命] [失败] [没有权限任命] [{}] [appointPlayerID:{}] [title:{}] [{}ms]", new Object[] { player.getName(), appointPlayerID, JiazuTitle.values()[req.getTitleID()].toString(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			JIAZU_APPOINT_RES res = new JIAZU_APPOINT_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_jiazu_047);
			return res;
		}

	}

	// boolean isWordValid(String word, int length) {
	// WordFilter filter = WordFilter.getInstance();
	// boolean valid = word != null && word.length() > 0 && word.length() <= length && filter.cvalid(word, 0) && filter.evalid(word, 1) && tagValid(word);
	// return valid;
	// }

	/**
	 * 修改家族称号
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_JIAZU_SET_RANK_NAME_REQ(Connection conn, RequestMessage message, Player player) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if (player.isInBattleField() && player.getDuelFieldState() > 0) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_060);
			player.addMessageToRightBag(nreq);
			return null;
		}
		if (player.isLocked()) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_061);
			player.addMessageToRightBag(nreq);
			return null;
		}
		JIAZU_SET_RANK_NAME_REQ req = (JIAZU_SET_RANK_NAME_REQ) message;
		JiazuManager jiazuManager = JiazuManager.getInstance();

		if (player.getJiazuId() <= 0) {
			if (logger.isWarnEnabled()) {
				logger.warn("[设立家族称号] [失败] [玩家尚未加入家族] [{}] [jiazuName:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_040);
			player.addMessageToRightBag(nreq);
			return null;
		}
		Jiazu jiazu = jiazuManager.getJiazu(player.getJiazuName());
		if (jiazu == null) {
			if (logger.isWarnEnabled()) {
				logger.warn("[设立家族称号] [失败] [家族不存在] [{}] [jiazuName:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_041);
			player.addMessageToRightBag(nreq);
			player.setJiazuName(null);
			return null;
		}
		if (logger.isWarnEnabled()) {
			logger.warn(player.getJiazuLogString() + "[修改家族称号] [旧的称号:{}]", new Object[] { Arrays.toString(jiazu.getTitleAlias()) });
		}
		JiazuMember member = jiazuManager.getJiazuMember(player.getId(), jiazu.getJiazuID());
		if (member == null) {
			if (logger.isWarnEnabled()) {
				logger.warn("[设立家族称号] [失败] [家族成员不存在] [{}] [jiazuName:{}] [jiazuID:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), jiazu.getJiazuID(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_125);
			player.addMessageToRightBag(nreq);
			return null;
		}
		if (!JiazuTitle.hasPermission(member.getTitle(), JiazuFunction.set_rank_name)) {
			JIAZU_SET_RANK_NAME_RES res = new JIAZU_SET_RANK_NAME_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_jiazu_047);
			return res;
		}
		SeptStation station = null;
		if (jiazu.getBaseID() != 0) {
			station = SeptStationManager.getInstance().getSeptStationById(jiazu.getBaseID());
		}
		if (station == null || jiazu.getBaseID() == 0) {
			JIAZU_SET_RANK_NAME_RES res = new JIAZU_SET_RANK_NAME_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_jiazu_170);
			return res;
		}

		int level = station.getBuildingLevel(BuildingType.龙图阁);
		if (level < 2) {
			JIAZU_SET_RANK_NAME_RES res = new JIAZU_SET_RANK_NAME_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_jiazu_171);
			return res;
		}
		String str[] = new String[7];
		String master_alias = req.getMaster_alias();
		String vice_master_alias = req.getVice_master_alias();
		String law_alias = req.getLaw_elder_alias();
		String fight_alias = req.getFight_elder_alias();
		String research_alias = req.getResearch_elder_alias();
		String elite_alias = req.getElite_alias();
		String civilian_alias = req.getCivilian_alias();
		str[0] = master_alias;
		str[1] = vice_master_alias;
		str[2] = law_alias;
		str[3] = fight_alias;
		str[4] = research_alias;
		str[5] = elite_alias;
		str[6] = civilian_alias;
		boolean isValid = true;
		WordFilter filter = WordFilter.getInstance();
		for (String s : str) {
			s = filter.nvalidAndReplace(s, 0, "@#\\$%^\\&\\*");
		}
		if (!isValid) {
			if (logger.isWarnEnabled()) {
				logger.warn("[设立家族称号] [失败] [含有非法字符或者长度大于12] [{}] [jiazuName:{}] [jiazuID:{}] [master:{}] [vice:{}] [law：{}] [fight:{}] [reserch:{}] [elite:{}] [civilian:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), jiazu.getJiazuID(), master_alias, str[1], str[2], str[3], str[4], str[5], str[6], com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			JIAZU_SET_RANK_NAME_RES res = new JIAZU_SET_RANK_NAME_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_jiazu_173);
			return res;
		} else {
			jiazuManager.addTitleAlias(str, jiazu);
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[设立家族称号] [成功] [{}] [jiazuName:{}] [jiazuID:{}] [master:{}] [vice:{}] [law：{}] [fight:{}] [reserch:{}] [elite:{}] [civilian:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), jiazu.getJiazuID(), master_alias, str[1], str[2], str[3], str[4], str[5], str[6], com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}

			Player[] players = PlayerManager.getInstance().getOnlinePlayerByJiazu(jiazu);
			for (Player p : players) {
				p.noticeJiazuTitleChange(jiazu);
				if (logger.isWarnEnabled()) {
					logger.warn(player.getJiazuLogString() + "[设立家族称号] [成功] [通知玩家更改]" + player.getJiazuLogString());
				}
			}
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[修改家族称号] [成功] [新的称号:{}]", new Object[] { Arrays.toString(jiazu.getTitleAlias()) });
			}
			JIAZU_SET_RANK_NAME_RES res = new JIAZU_SET_RANK_NAME_RES(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_174);
			return res;
		}
	}

	public ResponseMessage handle_JIAZU_LIST_SALARY_REQ(Connection conn, RequestMessage message, Player player) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if (player.isInBattleField() && player.getDuelFieldState() > 0) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_060);
			player.addMessageToRightBag(nreq);
			return null;
		}
		if (player.isLocked()) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_061);
			player.addMessageToRightBag(nreq);
			return null;
		}
		JiazuManager jiazuManager = JiazuManager.getInstance();
		if (player.getJiazuId() <= 0) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[取出家族工资列表] [失败] [玩家尚未加入家族] [{}] [jiazuName:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_040);
			player.addMessageToRightBag(nreq);
			return null;
		}
		Jiazu jiazu = jiazuManager.getJiazu(player.getJiazuName());
		if (jiazu == null) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[取出家族工资列表] [失败] [家族不存在] [{}] [jiazuName:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_041);
			player.addMessageToRightBag(nreq);
			player.setJiazuName(null);
			return null;
		}
		JiazuMember member = jiazuManager.getJiazuMember(player.getId(), jiazu.getJiazuID());
		if (member == null) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[取出家族工资列表] [失败] [家族成员不存在] [{}] [jiazuName:{}] [jiazuID:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), jiazu.getJiazuID(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_125);
			player.addMessageToRightBag(nreq);
			return null;
		} else {
			long salarySum = jiazu.getSalarySum();
			boolean hasPopedom = member.getTitle() == JiazuTitle.master;
			String des = Translate.text_jiazu_028;
			String batchDes = Translate.text_jiazu_029;
			long salaryLeft = jiazu.getSalaryLeft();
			Set<JiazuMember> memberList = jiazuManager.getJiazuMember(jiazu.getJiazuID());

			String[] playerName = new String[memberList.size()];
			long[] playerId = new long[memberList.size()];
			long[] salary = new long[memberList.size()];
			long[] lastWeekSalary = new long[memberList.size()];
			boolean[] hasPaid = new boolean[memberList.size()];
			long[] paidSalary = new long[memberList.size()];

			// PlayerManager playerManager = PlayerManager.getInstance();
			int i = 0;
			for (JiazuMember tempMem : memberList) {
				try {
					// Player p = playerManager.getPlayer(tempMem.getPlayerID());
					PlayerSimpleInfo p = PlayerSimpleInfoManager.getInstance().getInfoById(tempMem.getPlayerID());
					playerName[i] = p.getName();
					playerId[i] = p.getId();
					salary[i] = jiazu.getSinglePlayerSalary(p.getId());
					lastWeekSalary[i] = 0;// tempMem.getLastSalary();
					// hasPaid[i] = tempMem.isPaidthisweek();
					hasPaid[i] = (tempMem.getCurrentSalary() >= jiazu.getSinglePlayerSalary(p.getId()));
					paidSalary[i] = 0;// tempMem.getCurrentSalary();
				} catch (Exception e) {
					playerName[i] = "";
					playerId[i] = 0;
					salary[i] = 0;
					lastWeekSalary[i] = 0;
					paidSalary[i] = 0;
					// hasPaid[i] = true;
					hasPaid[i] = false;
					JiazuSubSystem.logger.error(player.getJiazuLogString() + "[查询工资异常] [tempMemId:{}]", new Object[] { tempMem.getJiazuMemID() }, e);
					continue;
				}
			}
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[取出家族工资列表] [成功] [{}] [jiazuName:{}] [jiazuID:{}] [salarySum:{}] [salaryLeft:{}] [memberSize:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), jiazu.getJiazuID(), salarySum, salaryLeft, memberList.size(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			JIAZU_LIST_SALARY_RES res = new JIAZU_LIST_SALARY_RES(GameMessageFactory.nextSequnceNum(), salarySum, salaryLeft, hasPopedom, des, batchDes, playerName, playerId, salary, paidSalary, lastWeekSalary, hasPaid);
			return res;
		}
	}

	/**
	 * 举行工资仪式
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_JIAZU_SALARY_CEREMONY_REQ(Connection conn, RequestMessage message, Player player) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if (player.isInBattleField() && player.getDuelFieldState() > 0) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_060);
			player.addMessageToRightBag(nreq);
			return null;
		}
		if (player.isLocked()) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_061);
			player.addMessageToRightBag(nreq);
			return null;
		}
		JIAZU_SALARY_CEREMONY_REQ req = (JIAZU_SALARY_CEREMONY_REQ) message;
		JiazuManager jiazuManager = JiazuManager.getInstance();
		Jiazu jiazu = jiazuManager.getJiazu(player.getJiazuName());

		if (logger.isWarnEnabled()) {
			logger.warn(player.getJiazuLogString() + "[举行工资仪式]");
		}

		if (player.getJiazuId() <= 0) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[举行工资发送仪式] [失败] [家族不存在] [{}] [jiazuName:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_040);
			player.addMessageToRightBag(nreq);
			return null;
		}
		if (jiazu == null) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[举行工资发送仪式] [失败] [家族不存在] [{}] [jiazuName:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_041);
			player.addMessageToRightBag(nreq);
			player.setJiazuName(null);
			return null;
		}
		JiazuMember member = jiazuManager.getJiazuMember(player.getId(), jiazu.getJiazuID());
		if (member == null) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[举行工资发送仪式] [失败] [家族成员不存在] [{}] [jiazuName:{}] [jiazuID:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), jiazu.getJiazuID(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_125);
			player.addMessageToRightBag(nreq);
			return null;
		} else if (!(member.getTitle() == JiazuTitle.master)) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[举行工资发送仪式] [失败] [不是族长] [{}] [jiazuName:{}] [jiazuID:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), jiazu.getJiazuID(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_047);
			player.addMessageToRightBag(nreq);
		} else {

			// 判断工资仪式次数

			if (TimeTool.instance.isSame(jiazu.getLastSalaryCeremonyTime(), start, TimeDistance.DAY)) {// 是同一周期的
				if (jiazu.getCycleSalaryCeremonyTimes() >= Jiazu.DAILY_SALAEY_MAX) {// 当天超过上限
					player.sendError(Translate.text_jiazu_088);
					if (logger.isWarnEnabled()) {
						logger.warn(player.getJiazuLogString() + "[发布工资仪式] [召唤次数太多了]");
					}
					return null;
				} else {// 记录时间
					jiazu.setCycleSalaryCeremonyTimes(jiazu.getCycleSalaryCeremonyTimes() + 1);
				}
			} else {
				jiazu.setCycleSalaryCeremonyTimes(1);
			}
			jiazu.setLastSalaryCeremonyTime(start);

			List<Player> onlines = jiazu.getOnLineMembers();
			for (Player mem : onlines) {
				JiazuMember tempMem = jiazuManager.getJiazuMember(mem.getId(), jiazu.getJiazuID());
				if (tempMem == null) {
					if (logger.isWarnEnabled()) {
						logger.warn(player.getJiazuLogString() + "[举行工资仪式] [异常] [家族成员不存在] [{}]", mem.getLogString());
					}
					continue;
				}
				if (mem.getId() != player.getId() && !tempMem.isPaidthisweek()) {
					if (GlobalTool.verifyMapTrans(mem.getId()) != null) {
						continue;
					}
					MenuWindow wm = WindowManager.getInstance().createTempMenuWindow((int) TimeTool.TimeDistance.MINUTE.getTimeMillis());
					wm.setDescriptionInUUB(Translate.translateString(Translate.text_jiazu_025, new String[][] { { Translate.STRING_1, tempMem.getTitle().getName() }, { Translate.STRING_2, sdf.format(new Date()) } }));
					wm.setTitle(Translate.text_jiazu_002);
					Option_Cancel cancel = new Option_Cancel();
					cancel.setText(Translate.text_jiazu_004);
					Option_Jiazu_CallIn callIn = new Option_Jiazu_CallIn(player.getCurrentGame(), player);
					callIn.setText(Translate.text_jiazu_003);
					wm.setOptions(new Option[] { callIn, cancel });
					mem.addMessageToRightBag(new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), wm, wm.getOptions()));
				}
			}
			ChatMessageService.getInstance().sendMessageToJiazu(jiazu, player.getName() + Translate.text_jiazu_026, "");
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[举行工资发送仪式] [成功] [{}] [jiazuName:{}] [jiazuID:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), jiazu.getJiazuID(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
		}
		return null;
	}

	/**
	 * 查询玩家工资范围
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_JIAZU_QUERY_MEMBER_SALARY_REQ(Connection conn, RequestMessage message, Player player) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if (player.isInBattleField() && player.getDuelFieldState() > 0) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_060);
			player.addMessageToRightBag(nreq);
			return null;
		}
		if (player.isLocked()) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_061);
			player.addMessageToRightBag(nreq);
			return null;
		}
		JIAZU_QUERY_MEMBER_SALARY_REQ req = (JIAZU_QUERY_MEMBER_SALARY_REQ) message;
		long playerId = req.getPlayerid();
		PlayerManager playerManager = PlayerManager.getInstance();
		JiazuManager jiazuManager = JiazuManager.getInstance();
		Jiazu jiazu = jiazuManager.getJiazu(player.getJiazuName());
		Player targetPlayer = null;
		try {
			targetPlayer = playerManager.getPlayer(playerId);
		} catch (Exception e) {
			logger.error(player.getJiazuLogString() + "[查询单个玩家工资额度] [失败] [玩家不存在] [{}] [jiazuName:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start }, e);
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_051);
			player.addMessageToRightBag(nreq);
			return null;
		}
		if (player.getJiazuId() <= 0) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[查询单个玩家工资额度] [失败] [家族不存在] [{}] [jiazuName:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}

			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_040);
			player.addMessageToRightBag(nreq);
			return null;
		}
		if (jiazu == null) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[查询单个玩家工资额度] [失败] [家族不存在] [{}] [jiazuName:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_041);
			player.addMessageToRightBag(nreq);
			player.setJiazuName(null);
			return null;
		}
		JiazuMember member = jiazuManager.getJiazuMember(player.getId(), jiazu.getJiazuID());
		JiazuMember targetMember = jiazuManager.getJiazuMember(targetPlayer.getId(), jiazu.getJiazuID());
		if (member == null) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[查询单个玩家工资额度] [失败] [家族成员不存在] [{}] [jiazuName:{}] [jiazuID:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), jiazu.getJiazuID(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_125);
			player.addMessageToRightBag(nreq);
			return null;
		} else if (!(member.getTitle() == JiazuTitle.master)) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[查询单个玩家工资额度] [失败] [不是族长] [{}] [jiazuName:{}] [jiazuID:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), jiazu.getJiazuID(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_047);
			player.addMessageToRightBag(nreq);
			return null;
		} else if (targetMember == null) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[查询单个玩家工资额度] [失败] [目标家族成员不存在] [{}] [jiazuName:{}] [jiazuID:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), jiazu.getJiazuID(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_125);
			player.addMessageToRightBag(nreq);
			return null;

		} else {
			if (logger.isWarnEnabled()) {
				logger.warn("[查询单个玩家工资额度] [成功] [{}] [jiazuName:{}] [jiazuID:{}] [targetPlayer:{}] [targetPlayerID:{}] [targetSalary:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), jiazu.getJiazuID(), targetPlayer.getName(), targetPlayer.getId(), targetMember.getCurrentWeekMaxSalary(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}

			JIAZU_QUERY_MEMBER_SALARY_RES res = new JIAZU_QUERY_MEMBER_SALARY_RES(message.getSequnceNum(), req.getPlayerid(), jiazu.getSalaryLeft(), 0, targetMember.getCurrentWeekMaxSalary());
			return res;
		}

	}

	/**
	 * 发工资
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_JIAZU_P_SALARY_REQ(Connection conn, RequestMessage message, Player player) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if (player.isInBattleField() && player.getDuelFieldState() > 0) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_060);
			player.addMessageToRightBag(nreq);
			return null;
		}
		if (player.isLocked()) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_061);
			player.addMessageToRightBag(nreq);
			return null;
		}
		JIAZU_P_SALARY_REQ req = (JIAZU_P_SALARY_REQ) message;
		long playerId = req.getPlayerid();
		long paySalary = req.getSalary();

		if (logger.isWarnEnabled()) {
			logger.warn(player.getJiazuLogString() + "[申请给成员发工资] [成员playerId:{}] [工资:{}]", new Object[] { playerId, paySalary });
		}

		PlayerManager playerManager = PlayerManager.getInstance();
		JiazuManager jiazuManager = JiazuManager.getInstance();
		Jiazu jiazu = jiazuManager.getJiazu(player.getJiazuName());
		Player targetPlayer = null;
		try {
			targetPlayer = playerManager.getPlayer(playerId);
		} catch (Exception e) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[查询单个玩家工资额度] [失败] [玩家不存在] [{}] [jiazuName:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start }, e);
			}
			JIAZU_P_SALARY_RES res = new JIAZU_P_SALARY_RES(message.getSequnceNum(), (byte) 1, Translate.text_jiazu_030, playerId, paySalary, 0);
			return res;
		}
		if (player.getJiazuId() <= 0) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[查询单个玩家工资额度] [失败] [家族不存在] [{}] [jiazuName:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			JIAZU_P_SALARY_RES res = new JIAZU_P_SALARY_RES(message.getSequnceNum(), (byte) 1, Translate.text_jiazu_031, playerId, paySalary, 0);
			return res;
		}
		if (jiazu == null) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[查询单个玩家工资额度] [失败] [家族不存在] [{}] [jiazuName:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			player.setJiazuName(null);
			JIAZU_P_SALARY_RES res = new JIAZU_P_SALARY_RES(message.getSequnceNum(), (byte) 1, Translate.text_jiazu_031, playerId, paySalary, 0);
			return res;
		}
		JiazuMember member = jiazuManager.getJiazuMember(player.getId(), jiazu.getJiazuID());
		JiazuMember targetMember = jiazuManager.getJiazuMember(targetPlayer.getId(), jiazu.getJiazuID());
		if (member == null) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[发工资] [失败] [家族成员不存在] [{}] [jiazuName:{}] [jiazuID:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), jiazu.getJiazuID(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			JIAZU_P_SALARY_RES res = new JIAZU_P_SALARY_RES(message.getSequnceNum(), (byte) 1, Translate.text_jiazu_031, playerId, paySalary, 0);
			return res;
		} else if (!(member.getTitle() == JiazuTitle.master)) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[发工资] [失败] [不是族长] [{}] [jiazuName:{}] [jiazuID:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), jiazu.getJiazuID(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			JIAZU_P_SALARY_RES res = new JIAZU_P_SALARY_RES(message.getSequnceNum(), (byte) 1, Translate.text_jiazu_032, playerId, paySalary, 0);
			return res;
		} else if (targetMember == null) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[发工资] [失败] [目标家族成员不存在] [{}] [jiazuName:{}] [jiazuID:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), jiazu.getJiazuID(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			JIAZU_P_SALARY_RES res = new JIAZU_P_SALARY_RES(message.getSequnceNum(), (byte) 1, Translate.text_jiazu_030, playerId, paySalary, 0);
			return res;

		} else if (jiazu.getSalaryLeft() < paySalary) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[发工资] [失败] [剩余的工资数小于发送总额] [{}] [jiazuName:{}] [jiazuID:{}] [paySalary:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), jiazu.getJiazuID(), paySalary, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			JIAZU_P_SALARY_RES res = new JIAZU_P_SALARY_RES(message.getSequnceNum(), (byte) 1, Translate.text_jiazu_033, playerId, paySalary, 0);
			return res;
			// } else if (targetMember.isPaidthisweek()) {
			// if (logger.isWarnEnabled()) {
			// logger.warn(player.getJiazuLogString() + "[发工资] [失败] [已经发送过了] [{}] [jiazuName:{}] [jiazuID:{}] [paySalary:{}] [{}ms]", new Object[] { player.getName(),
			// player.getJiazuName(), jiazu.getJiazuID(), paySalary, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			// }
			// JIAZU_P_SALARY_RES res = new JIAZU_P_SALARY_RES(message.getSequnceNum(), (byte) 1, Translate.text_jiazu_034, playerId, paySalary, 0);
			// return res;
		} else if (!targetPlayer.inSelfSeptStation()) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[发工资] [失败] [只能给在驻地的成员发送工资] [{}] [jiazuName:{}] [jiazuID:{}] [paySalary:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), jiazu.getJiazuID(), paySalary, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			JIAZU_P_SALARY_RES res = new JIAZU_P_SALARY_RES(message.getSequnceNum(), (byte) 1, Translate.text_jiazu_038, playerId, paySalary, 0);
			return res;
		} else {
			long maxNum = jiazu.getSinglePlayerSalary(playerId);
			if (paySalary < 0 || (targetMember.getCurrentSalary() + paySalary) > maxNum) {
				if (logger.isWarnEnabled()) {
					logger.warn(player.getJiazuLogString() + "[发工资] [失败] [工资数目不对，超出最大限制] [{}] [jiazuName:{}] [jiazuID:{}] [paySalary:{}] [maxNum:{}] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), jiazu.getJiazuID(), paySalary, maxNum, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				}
				JIAZU_P_SALARY_RES res = new JIAZU_P_SALARY_RES(message.getSequnceNum(), (byte) 1, Translate.text_jiazu_035, playerId, paySalary, 0);
				return res;
			}
			targetMember.addSalary(paySalary);
			jiazu.setSalaryLeft(jiazu.getSalaryLeft() - paySalary);
			targetPlayer.sendError(Translate.translateString(Translate.text_jiazu_039, new String[][] { { Translate.STRING_1, BillingCenter.得到带单位的银两(paySalary) } }));

			JIAZU_P_SALARY_RES res = new JIAZU_P_SALARY_RES(message.getSequnceNum(), (byte) 0, Translate.text_jiazu_036, playerId, paySalary, jiazu.getSalaryLeft());
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[发工资] [成功] [{}] [jiazuName:{}] [targetMemberID:{}] [jiazuID:{}] [paySalary:{}] [maxNum:{}] [家族剩余工资:" + jiazu.getSalaryLeft() + "] [{}ms]", new Object[] { player.getName(), player.getJiazuName(), targetMember.getJiazuMemID(), jiazu.getJiazuID(), paySalary, maxNum, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			return res;
		}
	}

	/**
	 * 批量发工资
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_JIAZU_BATCH_SEND_SALARY_REQ(Connection conn, RequestMessage message, Player player) {
		if (player.isInBattleField() && player.getDuelFieldState() > 0) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_060);
			player.addMessageToRightBag(nreq);
			return null;
		}
		if (player.isLocked()) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_061);
			player.addMessageToRightBag(nreq);
			return null;
		}
		JiazuManager jiazuManager = JiazuManager.getInstance();
		Jiazu jiazu = jiazuManager.getJiazu(player.getJiazuName());

		if (logger.isWarnEnabled()) {
			logger.warn(player.getJiazuLogString() + "[申请批量发工资]");
		}

		if (jiazu == null) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[批量发送工资] [失败] [家族不存在]");
			}
			JIAZU_BATCH_SEND_SALARY_RES res = new JIAZU_BATCH_SEND_SALARY_RES(message.getSequnceNum(), new long[0], new long[0], 0, Translate.text_jiazu_031);
			return res;
		}
		JiazuMember jiazuMember = jiazuManager.getJiazuMember(player.getId(), jiazu.getJiazuID());
		if (jiazuMember == null) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[批量发送工资] [失败] [jiazuMember不存在]");
			}
			JIAZU_BATCH_SEND_SALARY_RES res = new JIAZU_BATCH_SEND_SALARY_RES(message.getSequnceNum(), new long[0], new long[0], 0, Translate.text_jiazu_031);
			return res;
		}
		if (jiazuMember.getTitle().ordinal() != JiazuTitle.master.ordinal()) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[批量发送工资] [失败] [权限不足] [玩家权限:{}]", new Object[] { jiazuMember.getTitle() });
			}
			JIAZU_BATCH_SEND_SALARY_RES res = new JIAZU_BATCH_SEND_SALARY_RES(message.getSequnceNum(), new long[0], new long[0], 0, Translate.text_jiazu_032);
			return res;
		}

		List<Long> playerIds = new ArrayList<Long>();
		List<Long> salarys = new ArrayList<Long>();
		Set<JiazuMember> memberList = jiazuManager.getJiazuMember(jiazu.getJiazuID());
		PlayerManager playerManager = GamePlayerManager.getInstance();
		for (JiazuMember member : memberList) {
			try {
				if (playerManager.isOnline(member.getPlayerID())) {// 在线
					if (!member.isPaidthisweek() && member.getCurrentSalary() == 0) {// 且本周没发过工资
						Player targetPlayer = playerManager.getPlayer(member.getPlayerID());
						if (targetPlayer.inSelfSeptStation()) {// 且在家族驻地的
							long needSalary = jiazu.getSinglePlayerSalary(member.getPlayerID());// 需要给这个人发的工资数
							if (jiazu.getSalaryLeft() >= needSalary) {// 家族工资够发
								member.addSalary(needSalary);// 给玩家发工资
								jiazu.setSalaryLeft(jiazu.getSalaryLeft() - needSalary);
								playerIds.add(targetPlayer.getId());
								salarys.add(needSalary);
								targetPlayer.sendError(Translate.translateString(Translate.text_jiazu_039, new String[][] { { Translate.STRING_1, BillingCenter.得到带单位的银两(needSalary) } }));
								if (logger.isWarnEnabled()) {
									logger.warn(player.getJiazuLogString() + " [批量发送工资] [接受人:" + targetPlayer.getJiazuLogString() + "] [发给工资:" + needSalary + "] [发完后目标工资:" + targetPlayer.getWage() + "] [家族剩余工资:" + jiazu.getSalaryLeft() + "]");
								}
							} else {
								if (logger.isWarnEnabled()) {
									logger.warn(player.getJiazuLogString() + " [批量发送工资] [接受人:" + targetPlayer.getJiazuLogString() + "] [要发给工资:" + needSalary + "] [发完后目标工资:" + targetPlayer.getWage() + "] [家族剩余工资:" + jiazu.getSalaryLeft() + "] [工资不够发了]");
								}
							}
						}
					}
				}
			} catch (Exception e) {
				logger.error(player.getJiazuLogString() + "[批量发送工资] [异常]", e);
			}
		}
		long[] playerId = new long[playerIds.size()];
		long[] salary = new long[playerIds.size()];
		for (int i = 0; i < playerId.length; i++) {
			playerId[i] = playerIds.get(i);
		}
		for (int i = 0; i < salary.length; i++) {
			salary[i] = salarys.get(i);
		}
		if (logger.isWarnEnabled()) {
			logger.warn(player.getJiazuLogString() + "[批量发工资] [成功] [发送给了:{}] [工资:{}]", new Object[] { Arrays.toString(playerId), Arrays.toString(salary) });
		}
		return new JIAZU_BATCH_SEND_SALARY_RES(message.getSequnceNum(), playerId, salary, jiazu.getSalaryLeft(), Translate.text_jiazu_037);
	}

	/**
	 * 创建家族
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_JIAZU_CREATE_REQ(Connection conn, RequestMessage message, Player player) {
		try {
			long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
			if (player.isInBattleField() && player.getDuelFieldState() > 0) {
				HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_060);
				player.addMessageToRightBag(nreq);
				return null;
			}
			if(player.getVipLevel() < 11){
				HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, "需要vip等级达到11级以上才可以创建");
				player.addMessageToRightBag(nreq);
				return null;
			}
			if (player.isLocked()) {
				HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_061);
				player.addMessageToRightBag(nreq);
				return null;
			}
			JIAZU_CREATE_REQ req = (JIAZU_CREATE_REQ) message;
			String slogan = req.getSlogan();

			String jiazuName = player.getJiazuName();

			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[申请创建家族] [家族名字:{}] [标语:{}]", new Object[] { jiazuName, slogan });
			}
			// 离开家族24小时之内不允许加入新的家族
			if ((com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - player.getLeaveJiazuTime()) < LEAVE_JIAZU_PENALTY_TIME) {
				JIAZU_APPLY_RES res = new JIAZU_APPLY_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_newjiazu_069);
				if (logger.isWarnEnabled()) {
					logger.warn(player.getJiazuLogString() + "[申请加入家族] [失败] [位于离开家族惩罚时间] [{}] [{}] [{}] [leave time] [{}] [{}ms]", new Object[] { player.getName(), player.getLevel(), player.getJiazuName(), (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - player.getLeaveJiazuTime() / 1000), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				}
				return res;
			}
			if (jiazuName != null && jiazuName.trim().length() > 0) {
				JIAZU_CREATE_RES res = new JIAZU_CREATE_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_jiazu_126);
				if (logger.isWarnEnabled()) {
					logger.warn(player.getJiazuLogString() + "[创建家族] [失败] [您已经在家族中了，不能创建！] [{}] [{}] [{}ms]", new Object[] { player.getName(), jiazuName, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				}
				return res;
			}
			if (player.getLevel() < CREATE_JIAZU_LEVLE) {
				JIAZU_CREATE_RES res = new JIAZU_CREATE_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_jiazu_127);
				if (logger.isWarnEnabled()) {
					logger.warn(player.getJiazuLogString() + "[创建家族] [失败] [您的级别不够{}级，不能创建！] [{}] [{}] [{}ms]", new Object[] { CREATE_JIAZU_LEVLE, player.getName(), player.getLevel(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				}
				return res;
			}
			if (req.getJiazuname() == null || req.getJiazuname().length() == 0) {
				JIAZU_CREATE_RES res = new JIAZU_CREATE_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_jiazu_128);
				if (logger.isWarnEnabled()) {
					logger.warn(player.getJiazuLogString() + "[创建家族] [失败] [你没有输入家族名！] [{}] [{}] [{}ms]", new Object[] { player.getName(), player.getLevel(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				}
				return res;
			}
			if (player.getRequestJiazuName() != null && !"".equals(player.getRequestJiazuName())) {
				Jiazu requestJiazu = JiazuManager.getInstance().getJiazu(player.getRequestJiazuName());
				if (requestJiazu != null) {
					requestJiazu.removeRequest(player.getId());
					requestJiazu.notifyFieldChange("requestMap");
					if (logger.isWarnEnabled()) {
						logger.warn(player.getJiazuLogString() + "[申请创建家族] [在已申请的家族的申请列表移除] [申请的家族:{}]", new Object[] { player.getRequestJiazuName() });
					}
				}
				player.setRequestJiazuName("");
			}

			WordFilter filter = WordFilter.getInstance();
			boolean validName = req.getJiazuname().getBytes().length <= NAME_MAX && filter.cvalid(req.getJiazuname(), 0) && filter.evalid(req.getJiazuname(), 1) && tagValid(req.getJiazuname());
			boolean validSlogan = slogan.getBytes().length <= SLOGAN_MAX && filter.cvalid(slogan, 0) && filter.evalid(slogan, 1) && tagValid(slogan);
			// 重名的？
			if (!validName) {
				JIAZU_CREATE_RES res = new JIAZU_CREATE_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_jiazu_129);
				if (logger.isWarnEnabled()) {
					logger.warn(player.getJiazuLogString() + "[创建家族] [失败] [您输入家族名称含有禁用字符或敏感字符或过长(要求小于{}个字符)，请重新输入！] [{}] [{}] [{}ms]", new Object[] { NAME_MAX, player.getName(), req.getJiazuname(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				}
				return res;
			}
			if (!validSlogan) {
				JIAZU_CREATE_RES res = new JIAZU_CREATE_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_jiazu_130);
				if (logger.isWarnEnabled()) {
					logger.warn(player.getJiazuLogString() + "[创建家族] [失败] [您输入家族宗旨含有禁用字符或敏感字符或过长(要求小于{}个字符 )，请重新输入!] [{}] [{}] [{}ms]", new Object[] { SLOGAN_MAX, player.getName(), slogan, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				}
				return res;
			}
			
			if (player.getSilver() < ZongPaiManager.创建宗派花费) {
				String description = translateString(Translate.创建家族需要xx您银子不足, new String[][] { { STRING_1, ZongPaiManager.创建宗派花费锭 } });

				BillingCenter.银子不足时弹出充值确认框(player, description);
				ZongPaiManager.logger.error("[创建宗派银子不足] [" + player.getLogString() + "] [" + player.getSilver() + "]");
				return null;
			}
			
//			if (!player.bindSilverEnough(InitialPlayerConstant.JAIZU_CREATE_EXPENSE)) {
//				JIAZU_CREATE_RES res = new JIAZU_CREATE_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.translateString(Translate.text_jiazu_014, new String[][] { { Translate.STRING_1, BillingCenter.得到带单位的银两(InitialPlayerConstant.JAIZU_CREATE_EXPENSE) } }));
//				BillingCenter.银子不足时弹出充值确认框(player, Translate.translateString(Translate.你的银子不足是否去充值_金额, new String[][] { { Translate.STRING_1, BillingCenter.得到带单位的银两(InitialPlayerConstant.JAIZU_CREATE_EXPENSE) } }));
//				if (logger.isWarnEnabled()) {
//					logger.warn(player.getJiazuLogString() + "[创建家族] [失败] [您的金币不够去付创建家族！] [{}] [money] [{}] [expense] [{}] [{}ms]", new Object[] { player.getName(), player.getBindSilver(), InitialPlayerConstant.JAIZU_CREATE_EXPENSE, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
//				}
//				return res;
//			}
			MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(36000);
			mw.setTitle(Translate.text_jiazu_131);
			mw.setDescriptionInUUB(Translate.translateString(Translate.text_jiazu_012, new String[][] { { Translate.STRING_1, req.getJiazuname() }, { Translate.STRING_2, BillingCenter.得到带单位的银两(InitialPlayerConstant.JAIZU_CREATE_EXPENSE) } }));
			Option_UseCancel oc = new Option_UseCancel();
			oc.setText(Translate.取消);
			oc.setColor(0xffffff);

			Option_JiazuCreate_confirm confirm = new Option_JiazuCreate_confirm(req);
			confirm.setText(Translate.确定);
			oc.setColor(0xffffff);
			mw.setOptions(new Option[] { confirm, oc });
			QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[创建家族] [发送窗口确认] [{}] [Player money:{}] [expense:{}] [jiazuName:{}] [{}ms]", new Object[] { player.getName(), player.getBindSilver(), InitialPlayerConstant.JAIZU_CREATE_EXPENSE, req.getJiazuname(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			player.addMessageToRightBag(res);
		} catch (Exception e) {
			logger.error(player.getJiazuLogString() + "[创建家族异常]", e);
		}
		return null;
	}

	/**
	 * 建造建筑物 (升级)2011-4-27
	 * 
	 * @param conn
	 * @param message
	 * @param player
	 * @return ResponseMessage
	 * 
	 */
	public ResponseMessage handle_SEPTBUILDING_CREATE_REQ(Connection conn, RequestMessage message, Player player) {
		if (player.isInBattleField() && player.getDuelFieldState() > 0) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_060);
			player.addMessageToRightBag(nreq);
			return null;
		}
		if (player.isLocked()) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_061);
			player.addMessageToRightBag(nreq);
			return null;
		}

		SEPTBUILDING_CREATE_REQ req = (SEPTBUILDING_CREATE_REQ) message;
		SEPTBUILDING_CREATE_RES res = null;

		long septId = req.getSeptId();
		int buildType = req.getBuildType();
		long npcId = req.getNpcId();

		if (logger.isWarnEnabled()) {
			logger.warn(player.getJiazuLogString() + "[建造建筑] [septId={}] [buildType={}] [npcId={}]", new Object[] { septId, buildType, npcId });
		}

		JiazuManager manager = JiazuManager.getInstance();
		Jiazu jiazu = manager.getJiazu(septId);

		if (jiazu == null) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[建造建筑] [家族不存在]");
			}
			res = new SEPTBUILDING_CREATE_RES(req.getSequnceNum(), (byte) 1, Translate.text_jiazu_041);
			return res;
		}

		if (!jiazu.isContainPlayer(player.getId())) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[建造建筑] [不是该家族成员] [角色家族:" + player.getJiazuId() + "] " + jiazu.getLogString());
			}
			res = new SEPTBUILDING_CREATE_RES(req.getSequnceNum(), (byte) 1, Translate.text_jiazu_132);
			return res;
		}
		if (jiazu.getBaseID() <= 0) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[建造建筑] [驻地不存在]" + jiazu.getLogString());
			}
			res = new SEPTBUILDING_CREATE_RES(req.getSequnceNum(), (byte) 1, Translate.text_jiazu_132);
			return res;
		}
		
		SeptStation station = SeptStationManager.getInstance().getSeptStationById(jiazu.getBaseID());
		SeptBuildingEntity entity = station.getBuildings().get(npcId);
		
		SeptBuildingTemplet buildingTemplet = entity.getBuildingState().getTempletType();
		if (buildingTemplet.getBuildingType().equals(BuildingType.聚仙殿) && jiazu.getLevel() >= SeptStationManager.家族封印等级) {
			res = new SEPTBUILDING_CREATE_RES(req.getSequnceNum(), (byte) 1, Translate.家族封印等级);
			return res;
		}
		// 权限
		JiazuMember member = manager.getJiazuMember(player.getId(), septId);
		boolean hasPermission = JiazuTitle.hasPermission(member.getTitle(), JiazuFunction.upgrade_or_repair_construction);
		if (!hasPermission) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[建造建筑] [权限不足:{}]" + jiazu.getLogString(), new Object[] { member.getTitle().getName() });
			}
			res = new SEPTBUILDING_CREATE_RES(req.getSequnceNum(), (byte) 1, Translate.text_jiazu_133);
			return res;
		}
		if (logger.isWarnEnabled()) {
			logger.warn(player.getJiazuLogString() + "[建造建筑] [开始建造:{}]", new Object[] { buildType });
		}
		// 执行建造
		byte result = 0;
		String failreason = Translate.text_jiazu_073;
		try {
			try {
				if (entity == null) {
					return null;
				}
				if (logger.isWarnEnabled()) {
					logger.warn(player.getJiazuLogString() + "[家族:{}] [建造:{}]" + jiazu.getLogString(), new Object[] { jiazu.getName(), entity.getClass() });
				}
				entity.getBuildingState().getTempletType().build(station, npcId, buildType);
				station.initInfo();
//				{
//					// 召唤
//					List<Player> onlines = station.getJiazu().getOnLineMembers();
//					for (Player p : onlines) {
//						if (p.getId() != player.getId()) {
//							MenuWindow wm = WindowManager.getInstance().createTempMenuWindow((int) TimeTool.TimeDistance.MINUTE.getTimeMillis());
//							wm.setDescriptionInUUB(player.getName() + "刚刚发布建设任务" + entity.getNpc().getName() + " " + (entity.getBuildingState().getLevel() + 1) + "级.是否前往!");
//							wm.setTitle("建设任务开启");
//							Option_Cancel cancel = new Option_Cancel();
//							cancel.setText(Translate.text_jiazu_004);
//							Option_Jiazu_CallIn callIn = new Option_Jiazu_CallIn(player.getCurrentGame(), player);
//							callIn.setText(Translate.text_jiazu_003);
//							wm.setOptions(new Option[] { callIn, cancel });
//							p.addMessageToRightBag(new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), wm, wm.getOptions()));
//							if (logger.isWarnEnabled()) {
//								logger.warn(player.getJiazuLogString() + jiazu.getLogString() + "[建造:{" + entity.getNpc().getName() + "}] [发布任务成功] [召唤成员]");
//							}
//						}
//					}
//				}

			} catch (DependBuildingNotLvUpException e) {
				result = 1;
				failreason = Translate.translateString(Translate.需要, new String[][] { { Translate.STRING_1, e.getMsg() } });// "需要:" + e.getMsg();
			} catch (TooManyBiaozhixiangException e) {
				result = 1;
				failreason = Translate.text_jiazu_160;
			} catch (BuildingExistException e) {
				result = 1;
				failreason = Translate.text_jiazu_161;
			} catch (ResNotEnoughException e) {
				result = 1;
				failreason = Translate.text_jiazu_162;
			} catch (IndexHasBuildingException e) {
				result = 1;
				failreason = Translate.text_jiazu_158;
			} catch (OtherInBuildException e) {
				result = 1;
				failreason = Translate.translateString(Translate.text_jiazu_163, new String[][] { { Translate.STRING_1, e.getMsg() } });
			} catch (JiaZuFenyingException e) {
				result = 1;
				failreason = Translate.家族资金不足封印;
			}
		} catch (Exception e) {
			logger.error("建造时出现问题:", e);
		}
		if (logger.isWarnEnabled()) {
			logger.warn(player.getJiazuLogString() + jiazu.getLogString() + " [建造:{}] [结果:" + failreason + "] [召唤成员]", new Object[] { entity.getNpc().getName() });
		}
		return new SEPTBUILDING_CREATE_RES(req.getSequnceNum(), result, failreason);
	}

	/**
	 * 升级建筑 2011-4-27
	 * 
	 * @param conn
	 * @param message
	 * @param player
	 * @return ResponseMessage
	 * 
	 */
	public ResponseMessage handle_SEPTBUILDING_LVUP_REQ(Connection conn, RequestMessage message, Player player) {
		if (player.isInBattleField() && player.getDuelFieldState() > 0) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_060);
			player.addMessageToRightBag(nreq);
			return null;
		}
		if (player.isLocked()) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_061);
			player.addMessageToRightBag(nreq);
			return null;
		}

		SEPTBUILDING_LVUP_REQ req = (SEPTBUILDING_LVUP_REQ) message;
		SEPTBUILDING_LVUP_RES res = null;

		long septId = req.getSeptId();
		long NPCId = req.getNpcId();

		JiazuManager manager = JiazuManager.getInstance();
		Jiazu jiazu = manager.getJiazu(septId);
		if (jiazu == null) {
			res = new SEPTBUILDING_LVUP_RES(req.getSequnceNum(), (byte) 1, Translate.text_jiazu_041);
			return res;
		}

		SeptStation station = SeptStationManager.getInstance().getSeptStationBySeptId(septId);

		if (station == null) {
			res = new SEPTBUILDING_LVUP_RES(req.getSequnceNum(), (byte) 1, Translate.text_jiazu_065);
			return res;
		}
		SeptBuildingEntity entity = station.getSeptBuildingByNPCId(NPCId);
		if (entity == null || ((SeptStationNPC) entity.getNpc()).getGrade() < 0) {
			res = new SEPTBUILDING_LVUP_RES(req.getSequnceNum(), (byte) 1, Translate.text_jiazu_134);
			return res;
		}
		SeptBuildingTemplet buildingTemplet = entity.getBuildingState().getTempletType();
		if (buildingTemplet.getBuildingType().equals(BuildingType.聚仙殿) && jiazu.getLevel() >= SeptStationManager.家族封印等级) {
			res = new SEPTBUILDING_LVUP_RES(req.getSequnceNum(), (byte) 1, Translate.家族封印等级);
			return res;
		}

		if (logger.isWarnEnabled()) {
			logger.warn(player.getJiazuLogString() + "[升级驻地建筑] [{}] [当前等级:{}]", new Object[] { entity.getNpc().getName(), entity.getBuildingState().getLevel() });
		}

		if (!jiazu.isContainPlayer(player.getId())) {
			res = new SEPTBUILDING_LVUP_RES(req.getSequnceNum(), (byte) 1, Translate.text_jiazu_164);
			return res;
		}
		// 权限
		JiazuMember member = manager.getJiazuMember(player.getId(), septId);
		boolean hasPermission = JiazuTitle.hasPermission(member.getTitle(), JiazuFunction.upgrade_or_repair_construction);
		if (!hasPermission) {
			res = new SEPTBUILDING_LVUP_RES(req.getSequnceNum(), (byte) 1, Translate.text_jiazu_133);
			return res;
		}
		SeptBuildingEntity se = station.getInBuildBuilding();
		if (se != null) {
			res = new SEPTBUILDING_LVUP_RES(req.getSequnceNum(), (byte) 1, Translate.translateString(Translate.text_jiazu_094, new String[][] { { Translate.STRING_1, se.getNpc().getName() } }));
			return res;
		}
		byte result = 0;
		String failreason = Translate.text_jiazu_158;
		try {
			try {
				buildingTemplet.levelUp(station, NPCId);
//				{
//					// 召唤
//					List<Player> onlines = station.getJiazu().getOnLineMembers();
//					for (Player p : onlines) {
//						if (p.getId() != player.getId()) {
//							MenuWindow wm = WindowManager.getInstance().createTempMenuWindow((int) TimeTool.TimeDistance.MINUTE.getTimeMillis());
//							// wm.setDescriptionInUUB(player.getName() + "刚刚发布建设任务" + entity.getNpc().getName() + " " + (entity.getBuildingState().getLevel() + 1) + "级.是否前往!");
//							wm.setDescriptionInUUB(Translate.translateString(Translate.text_jiazu_166, new String[][] { { Translate.STRING_1, player.getName() }, { Translate.STRING_2, entity.getNpc().getName() }, { Translate.STRING_3, String.valueOf((entity.getBuildingState().getLevel() + 1)) } }));
//							wm.setTitle(Translate.text_jiazu_165);
//							Option_Cancel cancel = new Option_Cancel();
//							cancel.setText(Translate.text_jiazu_004);
//							Option_Jiazu_CallIn callIn = new Option_Jiazu_CallIn(player.getCurrentGame(), player);
//							callIn.setText(Translate.text_jiazu_003);
//							wm.setOptions(new Option[] { callIn, cancel });
//							p.addMessageToRightBag(new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), wm, wm.getOptions()));
//						}
//					}
//				}
				if (logger.isWarnEnabled()) {
					logger.warn(player.getJiazuLogString() + "[升级驻地建筑] [成功] [{}] [当前等级:{}]", new Object[] { entity.getNpc().getName(), entity.getBuildingState().getLevel() });
				}
			} catch (MainBuildNotLvUpException e) {
				result = 1;
				failreason = Translate.text_jiazu_167;
			} catch (ResNotEnoughException e) {
				result = 1;
				failreason = Translate.text_jiazu_162;
			} catch (OtherBuildingNotLvUpException e) {
				result = 1;
				failreason = Translate.text_jiazu_168;
			} catch (BuildingNotFoundException e) {
				result = 1;
				failreason = Translate.text_jiazu_134;
			} catch (MaxLvException e) {
				result = 1;
				failreason = Translate.text_jiazu_169;
			} 
		} catch (Exception e) {
			logger.error(jiazu.getName() + "[升级建筑异常]", e);
		}
		return new SEPTBUILDING_LVUP_RES(req.getSequnceNum(), result, failreason);
	}

	/**
	 * 降级建筑 2011-4-27
	 * 
	 * @param conn
	 * @param message
	 * @param player
	 * @return ResponseMessage
	 * 
	 */
	public ResponseMessage handle_SEPTBUILDING_LVDOWN_REQ(Connection conn, RequestMessage message, Player player) {
		if (player.isInBattleField() && player.getDuelFieldState() > 0) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_060);
			player.addMessageToRightBag(nreq);
			return null;
		}
		if (player.isLocked()) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_061);
			player.addMessageToRightBag(nreq);
			return null;
		}

		SEPTBUILDING_LVDOWN_REQ req = (SEPTBUILDING_LVDOWN_REQ) message;
		SEPTBUILDING_LVDOWN_RES res = null;

		long septId = req.getSeptId();
		long NPCId = req.getNpcId();
		JiazuManager manager = JiazuManager.getInstance();
		Jiazu jiazu = manager.getJiazu(septId);
		if (jiazu == null) {
			res = new SEPTBUILDING_LVDOWN_RES(req.getSequnceNum(), (byte) 1, Translate.text_jiazu_041 + septId);
			return res;
		}
		SeptStation station = SeptStationManager.getInstance().getSeptStationBySeptId(septId);

		if (station == null) {
			res = new SEPTBUILDING_LVDOWN_RES(req.getSequnceNum(), (byte) 1, Translate.text_jiazu_065);
			return res;
		}
		SeptBuildingEntity entity = station.getSeptBuildingByNPCId(NPCId);
		if (entity == null || ((SeptStationNPC) entity.getNpc()).getGrade() < 0) {
			res = new SEPTBUILDING_LVDOWN_RES(req.getSequnceNum(), (byte) 1, Translate.text_jiazu_134);
			return res;
		}

		if (entity.isInBuild()) {
			res = new SEPTBUILDING_LVDOWN_RES(req.getSequnceNum(), (byte) 1, Translate.text_jiazu_095);
			return res;
		}
		if (logger.isWarnEnabled()) {
			logger.warn(player.getJiazuLogString() + "[降级驻地建筑] [{}] [当前等级:{}]", new Object[] { entity.getNpc().getName(), entity.getBuildingState().getLevel() });
		}
		SeptBuildingTemplet buildingTemplet = entity.getBuildingState().getTempletType();

		if (!jiazu.isContainPlayer(player.getId())) {
			res = new SEPTBUILDING_LVDOWN_RES(req.getSequnceNum(), (byte) 1, Translate.text_jiazu_132);
			return res;
		}
		// 权限
		JiazuMember member = manager.getJiazuMember(player.getId(), septId);
		boolean hasPermission = JiazuTitle.hasPermission(member.getTitle(), JiazuFunction.downgrade_or_destroy_construction);
		if (!hasPermission) {
			res = new SEPTBUILDING_LVDOWN_RES(req.getSequnceNum(), (byte) 1, Translate.text_jiazu_133);
			return res;
		}

		// 执行降级
		try {
			try {
				buildingTemplet.levelDown(station, NPCId, false, player);

				if (logger.isWarnEnabled()) {
					logger.warn(player.getJiazuLogString() + "[降级驻地建筑] [未确认] [成功] [{}] [当前等级:{}]", new Object[] { entity.getNpc().getName(), entity.getBuildingState().getLevel() });
				}
				return null;
			} catch (BuildingNotFoundException e) {
				res = new SEPTBUILDING_LVDOWN_RES(req.getSequnceNum(), (byte) 1, Translate.text_jiazu_134);
				return res;
			} catch (WrongLvOnLvDownException e) {
				res = new SEPTBUILDING_LVDOWN_RES(req.getSequnceNum(), (byte) 1, Translate.text_jiazu_152);
				return res;
			} catch (ActionIsCDException e) {
				res = new SEPTBUILDING_LVDOWN_RES(req.getSequnceNum(), (byte) 1, Translate.text_jiazu_153 + e.getMsg());
				return res;
			}
		} catch (Exception e) {
			logger.error(player.getJiazuLogString() + "[建筑降级] [异常] ", e);
		}
		res = new SEPTBUILDING_LVDOWN_RES(req.getSequnceNum(), (byte) 0, Translate.text_jiazu_154);
		return res;
	}

	/**
	 * 玩家申请家族驻地 2011-4-28
	 * 
	 * @param conn
	 * @param message
	 * @param player
	 * @return ResponseMessage
	 * 
	 */
	public ResponseMessage handle_SEPTBUILDING_CREAT_SEPT_REQ(Connection conn, RequestMessage message, Player player) {
		if (player.isInBattleField() && player.getDuelFieldState() > 0) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_060);
			player.addMessageToRightBag(nreq);
			return null;
		}
		if (player.isLocked()) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_061);
			player.addMessageToRightBag(nreq);
			return null;
		}
		SEPTBUILDING_CREAT_SEPT_REQ req = (SEPTBUILDING_CREAT_SEPT_REQ) message;
		SEPTBUILDING_CREAT_SEPT_RES res = null;

		long jiazuId = req.getSeptId();// 家族ID
		String mapName = req.getMapName();// 驻地所在地图
		String name = req.getName();// 驻地名字
		// TODO 角色所属国家 player.getCountry();
		long NPCId = req.getNPCId();
		int country = 0;

		try {
			SeptStation station = SeptStationManager.getInstance().createDefaultSeptStation(jiazuId, mapName, name, country, NPCId);
			station.initInfo();
		} catch (NameExistException e) {
			res = new SEPTBUILDING_CREAT_SEPT_RES(req.getSequnceNum(), (byte) 1, Translate.text_jiazu_155);
			return res;
		} catch (StationExistException e) {
			res = new SEPTBUILDING_CREAT_SEPT_RES(req.getSequnceNum(), (byte) 1, Translate.text_jiazu_156);
			return res;
		} catch (SeptNotExistException e) {
			logger.error(player.getJiazuLogString() + "[申请驻地异常]", e);
		}
		return res = new SEPTBUILDING_CREAT_SEPT_RES(req.getSequnceNum(), (byte) 0, Translate.text_jiazu_157);
	}

	/**
	 * 摧毁建筑 2011-5-4
	 * 
	 * @param conn
	 * @param message
	 * @param player
	 * @return ResponseMessage
	 * 
	 */
	public ResponseMessage handle_SEPTBUILDING_DESTROY_REQ(Connection conn, RequestMessage message, Player player) {
		if (player.isInBattleField() && player.getDuelFieldState() > 0) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_060);
			player.addMessageToRightBag(nreq);
			return null;
		}
		if (player.isLocked()) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_061);
			player.addMessageToRightBag(nreq);
			return null;
		}
		SEPTBUILDING_DESTROY_REQ req = (SEPTBUILDING_DESTROY_REQ) message;
		SEPTBUILDING_DESTROY_RES res = null;

		long NPCId = req.getNpcId();

		byte result = 0;
		String failreason = Translate.text_jiazu_158;

		JiazuManager manager = JiazuManager.getInstance();
		String jiazuName = player.getJiazuName();
		if (jiazuName == null || "".equals(jiazuName)) {
			result = 1;
			failreason = Translate.text_jiazu_040;
		}
		Jiazu jiazu = manager.getJiazu(jiazuName);
		if (jiazu == null) {
			result = 1;
			failreason = Translate.text_jiazu_041;
		}
		// 权限
		JiazuMember member = manager.getJiazuMember(player.getId(), jiazu.getJiazuID());
		boolean hasPermission = JiazuTitle.hasPermission(member.getTitle(), JiazuFunction.downgrade_or_destroy_construction);
		if (!hasPermission) {
			failreason = Translate.text_jiazu_159;
			result = 1;
			return new SEPTBUILDING_DESTROY_RES(req.getSequnceNum(), result, failreason);
		}
		SeptStation station = SeptStationManager.getInstance().getSeptStationBySeptId(jiazu.getJiazuID());
		SeptBuildingEntity entity = station.getBuildings().get(NPCId);
		if (entity == null || entity.getBuildingState().getLevel() <= 0) {
			failreason = Translate.text_jiazu_134;
			result = 1;
			return new SEPTBUILDING_DESTROY_RES(req.getSequnceNum(), result, failreason);
		}
		try {
			entity.getBuildingState().getTempletType().destroy(station, NPCId);
		} catch (CannotDestoryException e) {
			failreason = Translate.text_jiazu_135;
			result = 1;
		} catch (BuildingNotFoundException e) {
			failreason = Translate.text_jiazu_134;
			result = 1;
		}
		return new SEPTBUILDING_DESTROY_RES(req.getSequnceNum(), result, failreason);
	}

	/**
	 * 查看家族徽章信息
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_JIAZU_BEDGE_INFO_REQ(Connection conn, RequestMessage message, Player player) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if (player.isInBattleField() && player.getDuelFieldState() > 0) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_060);
			player.addMessageToRightBag(nreq);
			return null;
		}
		if (player.isLocked()) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_061);
			player.addMessageToRightBag(nreq);
			return null;
		}
		if (player.getJiazuId() <= 0) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_040);
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[查看家族徽章信息] [失败] [没有加入家族] [{}] [{}ms]", new Object[] { player.getJiazuName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			player.addMessageToRightBag(nreq);
			return null;
		}
		Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
		if (jiazu == null) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_040);
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[查看家族徽章信息] [失败] [家族不存在] [{}] [{}ms]", new Object[] { player.getJiazuName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			player.addMessageToRightBag(nreq);
			return null;
		}
		List<JiazuBedge> allBedge = new ArrayList<JiazuBedge>();

		for (Iterator<Integer> itor = SeptStationMapTemplet.getInstance().getBedges().keySet().iterator(); itor.hasNext();) {
			allBedge.addAll(SeptStationMapTemplet.getInstance().getBedges().get(itor));
		}
		if (jiazu.getBedges() == null) {
			jiazu.setBedges(new ArrayList<Integer>());
		}
		int[] hasIds = new int[jiazu.getBedges().size()];
		for (int i = 0; i < jiazu.getBedges().size(); i++) {
			hasIds[i] = jiazu.getBedges().get(i);
		}
		JIAZU_BEDGE_INFO_RES res = new JIAZU_BEDGE_INFO_RES(message.getSequnceNum(), allBedge.toArray(new JiazuBedge[0]), hasIds, JiazuBedge.switchCost);
		return res;
	}

	/**
	 * 购买家族徽章
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_JIAZU_BUY_BEDGE_REQ(Connection conn, RequestMessage message, Player player) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if (player.isInBattleField() && player.getDuelFieldState() > 0) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_060);
			player.addMessageToRightBag(nreq);
			return null;
		}
		if (player.isLocked()) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_061);
			player.addMessageToRightBag(nreq);
			return null;
		}
		if (player.getJiazuId() <= 0) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_040);
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[查看家族徽章信息] [失败] [没有加入家族] [{}] [{}ms]", new Object[] { player.getJiazuName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			player.addMessageToRightBag(nreq);
			return null;
		}
		Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
		if (jiazu == null) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_040);
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[查看家族徽章信息] [失败] [家族不存在] [{}] [{}ms]", new Object[] { player.getJiazuName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			player.addMessageToRightBag(nreq);
			return null;
		}

		JIAZU_BUY_BEDGE_REQ req = (JIAZU_BUY_BEDGE_REQ) message;
		int bedgeId = req.getBedgeId();
		if (jiazu.getBedges().contains(bedgeId)) {
			JIAZU_BUY_BEDGE_RES res = new JIAZU_BUY_BEDGE_RES(GameMessageFactory.nextSequnceNum(), 0, (byte) 1, Translate.text_jiazu_138);
			return res;
		}

		SeptStation septStation = SeptStationManager.getInstance().getSeptStationById(jiazu.getBaseID());
		if (septStation == null) {
			player.sendError(Translate.text_jiazu_065);
			return null;
		}

		int level = septStation.getBuildingLevel(BuildingType.仙灵洞);

		JiazuBedge bedge = SeptStationMapTemplet.getInstance().getBedge(bedgeId);
		if (bedge == null) {
			JIAZU_BUY_BEDGE_RES res = new JIAZU_BUY_BEDGE_RES(GameMessageFactory.nextSequnceNum(), 0, (byte) 1, Translate.text_jiazu_139);
			return res;
		}

		if (bedge.getLevelLimit() > level) {
			JIAZU_REPLACE_BEDGE_RES res = new JIAZU_REPLACE_BEDGE_RES(req.getSequnceNum(), 0, (byte) 1, Translate.text_jiazu_151);
			return res;
		}
		// 钱不够
		long money = bedge.getPrice();
		if (player.getSilver() + player.getShopSilver() < money) {
			JIAZU_BUY_BEDGE_RES res = new JIAZU_BUY_BEDGE_RES(GameMessageFactory.nextSequnceNum(), 0, (byte) 1, Translate.text_jiazu_099);
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[想要买家族徽章] [失败] [银子不足] [家族 :{}] [徽章:{}] [需要银子:{}] [已有银子:{}]", new Object[] { jiazu.getName(), bedgeId, bedge.getPrice(), player.getSilver() });
			}
			return res;
		}
		try {
			BillingCenter.getInstance().playerExpense(player, money, CurrencyType.SHOPYINZI, ExpenseReasonType.JIAZU_BEDGE, "购买家族徽章");
		} catch (Exception e) {
			logger.warn(player.getJiazuLogString() + "[想要买家族徽章] [扣除银子异常] [家族 :{}] [徽章:{}] [需要银子:{}] [已有银子:{}]", new Object[] { jiazu.getName(), bedgeId, bedge.getPrice(), player.getSilver() }, e);
			JIAZU_BUY_BEDGE_RES res = new JIAZU_BUY_BEDGE_RES(GameMessageFactory.nextSequnceNum(), 0, (byte) 1, Translate.text_jiazu_099);
			return res;
		}
		jiazu.getBedges().add(bedgeId);
		jiazu.notifyFieldChange("bedges");
		if (logger.isWarnEnabled()) {
			logger.warn(player.getJiazuLogString() + "[购买了家族徽章] [成功] [家族:{}] [{}]", new Object[] { jiazu.getName(), bedgeId });
		}
		return new JIAZU_BUY_BEDGE_RES(GameMessageFactory.nextSequnceNum(), bedgeId, (byte) 0, Translate.text_jiazu_150);
	}

	public ResponseMessage handle_JIAZU_REPLACE_BEDGE_REQ(Connection conn, RequestMessage message, Player player) {
		JIAZU_REPLACE_BEDGE_REQ req = (JIAZU_REPLACE_BEDGE_REQ) message;
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if (player.isInBattleField() && player.getDuelFieldState() > 0) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_060);
			player.addMessageToRightBag(nreq);
			return null;
		}
		if (player.isLocked()) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_061);
			player.addMessageToRightBag(nreq);
			return null;
		}
		if (player.getJiazuId() <= 0) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_040);
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[查看家族徽章信息] [失败] [没有加入家族] [{}] [{}ms]", new Object[] { player.getJiazuName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			player.addMessageToRightBag(nreq);
			return null;
		}
		Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
		if (jiazu == null) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_040);
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[查看家族徽章信息] [失败] [家族不存在] [{}] [{}ms]", new Object[] { player.getJiazuName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
			player.addMessageToRightBag(nreq);
			return null;
		}
		int bedgeId = req.getBedgeId();
		JiazuBedge bedge = SeptStationMapTemplet.getInstance().getBedge(bedgeId);
		if (jiazu.getBedges() == null || !jiazu.getBedges().contains(bedgeId) || bedge == null) {
			JIAZU_REPLACE_BEDGE_RES res = new JIAZU_REPLACE_BEDGE_RES(req.getSequnceNum(), 0, (byte) 1, Translate.text_jiazu_139);
			return res;
		}

		SeptStation septStation = SeptStationManager.getInstance().getSeptStationBySeptId(jiazu.getJiazuID());
		if (septStation == null) {
			player.sendError(Translate.text_jiazu_065);
			return null;
		}

		int level = septStation.getBuildingLevel(BuildingType.仙灵洞);

		if (bedge.getLevelLimit() > level) {
			JIAZU_REPLACE_BEDGE_RES res = new JIAZU_REPLACE_BEDGE_RES(req.getSequnceNum(), 0, (byte) 1, Translate.text_jiazu_151);
			return res;
		}
		jiazu.setUsedBedge(bedgeId);
		jiazu.notifyFieldChange("usedBedge");
		Player ps[] = PlayerManager.getInstance().getOnlinePlayerByJiazu(jiazu);
		for (Player p : ps) {
			p.initJiazuTitleAndIcon();
		}
		if (logger.isWarnEnabled()) {
			logger.warn(player.getJiazuLogString() + "[更换了家族徽章] [成功] [家族:{}] [{}]", new Object[] { jiazu.getName(), bedgeId });
		}
		return new JIAZU_REPLACE_BEDGE_RES(req.getSequnceNum(), bedgeId, (byte) 0, Translate.text_jiazu_158);
	}

	/**
	 * 通过ID获得家族信息
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_JIAZU_QUERY_BY_ID_REQ(Connection conn, RequestMessage message, Player player) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if (player.isInBattleField() && player.getDuelFieldState() > 0) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_060);
			player.addMessageToRightBag(nreq);
			return null;
		}
		if (player.isLocked()) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_061);
			player.addMessageToRightBag(nreq);
			return null;
		}

		JIAZU_QUERY_BY_ID_REQ req = (JIAZU_QUERY_BY_ID_REQ) message;
		long jiazuId = req.getJizuId();

		Jiazu selfJiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
		if (selfJiazu == null) {
			return null;
		}
		if (selfJiazu.getZongPaiId() <= 0) {
			return null;
		}
		Jiazu queryJiazu = JiazuManager.getInstance().getJiazu(jiazuId);

		if (selfJiazu.getZongPaiId() != queryJiazu.getZongPaiId()) {// 不是同一宗派不允许查看
			HINT_REQ hint_REQ = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, com.fy.engineserver.datasource.language.Translate.不是同一宗派不能操作);
			player.addMessageToRightBag(hint_REQ);
			return null;
		}

		if (queryJiazu.getMember4Clients() == null || queryJiazu.getMember4Clients().size() == 0) {
			queryJiazu.setMemberModify(true);
		}
		GamePlayerManager gpm = (GamePlayerManager) GamePlayerManager.getInstance();
		for (JiazuMember4Client jiazuMember4Client : queryJiazu.getMember4Clients()) {
			try {
				if (gpm.isOnline(jiazuMember4Client.getPlayerId())) {
					Player p = gpm.getPlayer(jiazuMember4Client.getPlayerId());
					jiazuMember4Client.setOnLine(true);
					jiazuMember4Client.setPlayerLevel(p.getLevel());
				} else {
					jiazuMember4Client.setOnLine(false);
				}
			} catch (Exception e) {
				logger.error(player.getJiazuLogString() + "[查询家族异常]", e);
			}
		}

		return new JIAZU_QUERY_BY_ID_RES(message.getSequnceNum(), jiazuId, queryJiazu.getName(), queryJiazu.getLevel(), queryJiazu.getSlogan(), queryJiazu.getUsedBedge(), queryJiazu.getTitleAlias(), queryJiazu.getMember4Clients().toArray(new JiazuMember4Client[0]), queryJiazu.getWarScore(), queryJiazu.getProsperity());
	}

	/**
	 * 捐钱增加贡献度
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	static int CONTRIBUTE_MONEY_NEED_VIPLEVEL = 5;

	public ResponseMessage handle_JIAZU_CONTRIBUTE_MONEY_REQ(Connection conn, RequestMessage message, Player player) {
		JIAZU_CONTRIBUTE_MONEY_REQ req = (JIAZU_CONTRIBUTE_MONEY_REQ) message;
		long money = req.getMoney();

		if (player.getVipLevel() < CONTRIBUTE_MONEY_NEED_VIPLEVEL) {
			return new JIAZU_CONTRIBUTE_MONEY_RES(message.getSequnceNum(), (byte) 1, Translate.translateString(Translate.text_jiazu_110, new String[][] { { Translate.COUNT_1, String.valueOf(CONTRIBUTE_MONEY_NEED_VIPLEVEL) } }));
		}
		if (player.getSilver() + player.getShopSilver() < money || money <= 0) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[捐献家族资金] [失败] [银子不足] [输入了银子:{}] [实际有银子:{}]", new Object[] { money, player.getSilver() });
			}
			BillingCenter.银子不足时弹出充值确认框(player, Translate.你的银子不足是否去充值);
			return new JIAZU_CONTRIBUTE_MONEY_RES(message.getSequnceNum(), (byte) 1, Translate.text_jiazu_103);
		}
		if (player.getJiazuId() <= 0) {
			return new JIAZU_CONTRIBUTE_MONEY_RES(message.getSequnceNum(), (byte) 1, Translate.text_jiazu_031);
		}
		Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
		if (jiazu == null) {
			return new JIAZU_CONTRIBUTE_MONEY_RES(message.getSequnceNum(), (byte) 1, Translate.text_jiazu_031);
		}
		JiazuMember member = JiazuManager.getInstance().getJiazuMember(player.getId(), player.getJiazuId());
		if (member == null) {
			return new JIAZU_CONTRIBUTE_MONEY_RES(message.getSequnceNum(), (byte) 1, Translate.text_jiazu_031);
		}

		if (player.getSilver() < money) {
			return new JIAZU_CONTRIBUTE_MONEY_RES(message.getSequnceNum(), (byte) 1, Translate.text_jiazu_103);
		}
		int contribute = (int) (money / JiazuManager.moneyExchangeContribution);
		MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(36000);
		mw.setTitle(Translate.text_jiazu_141);
		mw.setDescriptionInUUB(Translate.translateString(Translate.text_jiazu_104, new String[][] { { Translate.STRING_1, BillingCenter.得到带单位的银两(money) }, { Translate.STRING_2, String.valueOf(contribute) } }));
		Option_UseCancel oc = new Option_UseCancel();
		oc.setText(Translate.text_jiazu_106);
		oc.setColor(0xffffff);
		Option_Jiazu_contribute confirm = new Option_Jiazu_contribute(money);
		confirm.setText(Translate.text_jiazu_105);
		confirm.setColor(0xffffff);
		mw.setOptions(new Option[] { confirm, oc });
		QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
		player.addMessageToRightBag(res);
		return null;
	}

	/**
	 * 召集成员
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_JIAZU_CALL_IN_REQ(Connection conn, RequestMessage message, Player player) {
		zuzhangCallin(player, false);
		return null;
	}

	public boolean zuzhangCallin(Player player) {
		return zuzhangCallin(player, true);
	}

	public boolean zuzhangCallin(Player player, boolean isPorp) {

		long now = SystemTime.currentTimeMillis();
		Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
		if (jiazu == null) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[召集家族成员] [家族不存在]");
			}
			return false;
		}
		JiazuMember jiazuMember = JiazuManager.getInstance().getJiazuMember(player.getId(), player.getJiazuId());
		if (!JiazuTitle.hasPermission(jiazuMember.getTitle(), JiazuFunction.release_callIn)) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[家族召唤] [没有权限] [{}] [jiazuName:{}] [jiazuID:{}] [title:{}]", new Object[] { player.getName(), player.getJiazuName(), jiazu.getJiazuID(), jiazuMember.getTitle().toString() });
			}
			player.sendError(Translate.text_jiazu_047);
			return false;
		}
		if (!isPorp && player.getCountry() != player.getCurrentGameCountry()) {
			player.sendError(Translate.text_jiazu_175);
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[召集家族成员] [不在自己国家] [角色国家:{}] [当前国家:{}]", new Object[] { player.getCountry(), player.getCurrentGameCountry() });
			}
			return false;
		}
		boolean isXianjie = false;
		try {
			String mapname = player.getCurrentGame().gi.name;
			isXianjie = RobberyConstant.没飞升玩家不可进入的地图.contains(mapname);
		} catch (Exception e) {
			
		}
		if (!isPorp) {
			if (TimeTool.instance.isSame(jiazu.getLastCallinTime(), now, TimeDistance.DAY)) {// 是同一周期的
				if (jiazu.getCycleCallinTimes() >= Jiazu.DAILY_CALLIN_MAX) {// 当天超过上限
					player.sendError(Translate.text_jiazu_088);
					if (logger.isWarnEnabled()) {
						logger.warn(player.getJiazuLogString() + "[召集家族成员] [召唤次数太多了]");
					}
					return false;
				} else {// 记录时间
					jiazu.setCycleCallinTimes(jiazu.getCycleCallinTimes() + 1);
				}
			} else {
				jiazu.setCycleCallinTimes(1);
			}
			jiazu.setLastCallinTime(now);
		}

		List<Player> onlines = jiazu.getOnLineMembers();
		DevilSquareManager inst = DevilSquareManager.instance;
		for (Player member : onlines) {
			if(inst != null && inst.isPlayerInDevilSquare(member)) {		//恶魔广场不弹框
				continue;
			}
			if(GlobalTool.verifyTransByOther(member.getId(),player.getCurrentGame()) != null) {
				continue;
			}
			if (isXianjie && member.getLevel() <= 220) {
				continue;
			}
			if (member.getId() != player.getId()) {
				MenuWindow wm = WindowManager.getInstance().createTempMenuWindow((int) TimeTool.TimeDistance.MINUTE.getTimeMillis());
				wm.setDescriptionInUUB(Translate.translateString(Translate.text_jiazu_022, new String[][] { { Translate.STRING_1, jiazuMember.getTitle().getName() }, { Translate.STRING_2, sdf.format(new Date()) } }));
				wm.setTitle(Translate.text_jiazu_002); 
				Option_Cancel cancel = new Option_Cancel();
				cancel.setText(Translate.text_jiazu_004);
				Option_Jiazu_CallIn callIn = new Option_Jiazu_CallIn(player.getCurrentGame(), player);
				callIn.setText(Translate.text_jiazu_003);
				wm.setOptions(new Option[] { callIn, cancel });
				member.addMessageToRightBag(new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), wm, wm.getOptions()));
			}
		}
		ChatMessageService.getInstance().sendMessageToJiazu(jiazu, player.getName() + Translate.召唤大家去他身边, "");
		if (logger.isWarnEnabled()) {
			logger.warn(player.getJiazuLogString() + "[族长召唤] [成功]");
		}
		return true;
	}

	public ResponseMessage handle_JIAZU_WAREHOUSE_TOPLAYER_REQ(Connection conn, RequestMessage message, Player player) {
		JIAZU_WAREHOUSE_TOPLAYER_REQ req = (JIAZU_WAREHOUSE_TOPLAYER_REQ) message;
		boolean is = JiazuManager.getInstance().msg_putEntity2Player(player, req.getWareHouseIndex(), req.getEntityID(), req.getEntityNum(), req.getPName());
		if (is) {
			JIAZU_WAREHOUSE_TOPLAYER_RES res = new JIAZU_WAREHOUSE_TOPLAYER_RES(req.getSequnceNum(), true, req.getEntityID(), req.getEntityNum(), req.getWareHouseIndex());
			return res;
		}
		return null;
	}

	public static void main(String[] args) {
		System.out.println(JiazuTitle.hasPermission(JiazuTitle.vice_master, JiazuFunction.handle_join_request));
	}
}
