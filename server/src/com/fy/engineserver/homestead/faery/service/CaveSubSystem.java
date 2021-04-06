package com.fy.engineserver.homestead.faery.service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.PetPropsEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.enterlimit.EnterLimitManager;
import com.fy.engineserver.enterlimit.EnterLimitManager.PlayerRecordType;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.gateway.SubSystemAdapter;
import com.fy.engineserver.homestead.cave.Cave;
import com.fy.engineserver.homestead.cave.CaveBuilding;
import com.fy.engineserver.homestead.cave.CaveDynamic;
import com.fy.engineserver.homestead.cave.CaveField;
import com.fy.engineserver.homestead.cave.CaveFieldBombData;
import com.fy.engineserver.homestead.cave.CaveSchedule;
import com.fy.engineserver.homestead.cave.CaveScheduleForClient;
import com.fy.engineserver.homestead.cave.PetHookInfo;
import com.fy.engineserver.homestead.cave.ResourceCollection;
import com.fy.engineserver.homestead.cave.resource.PlantCfg;
import com.fy.engineserver.homestead.exceptions.AlreadyHasCaveException;
import com.fy.engineserver.homestead.exceptions.CountyNotSameException;
import com.fy.engineserver.homestead.exceptions.FertyNotFountException;
import com.fy.engineserver.homestead.exceptions.IndexHasBeUsedException;
import com.fy.engineserver.homestead.exceptions.LevelToolowException;
import com.fy.engineserver.homestead.exceptions.OutOfMaxLevelException;
import com.fy.engineserver.homestead.exceptions.PointNotFoundException;
import com.fy.engineserver.homestead.faery.Faery;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_UseCancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.cave.Option_Cave_Accelerate_Schedule;
import com.fy.engineserver.message.CAVE_ACCELERATE_REQ;
import com.fy.engineserver.message.CAVE_ASSART_FIELD_REQ;
import com.fy.engineserver.message.CAVE_CANCEL_SCHEDULE_REQ;
import com.fy.engineserver.message.CAVE_CHECK_PLANT_REQ;
import com.fy.engineserver.message.CAVE_CHECK_PLANT_RES;
import com.fy.engineserver.message.CAVE_CONVERT_PLANT_REQ;
import com.fy.engineserver.message.CAVE_CONVERT_PLANT_RES;
import com.fy.engineserver.message.CAVE_DETAILED_INFO_REQ;
import com.fy.engineserver.message.CAVE_DETAILED_INFO_RES;
import com.fy.engineserver.message.CAVE_DYNAMIC_RES;
import com.fy.engineserver.message.CAVE_FIND_SELFCAVE_RES;
import com.fy.engineserver.message.CAVE_HARVEST_PLANT_REQ;
import com.fy.engineserver.message.CAVE_HOOK_PET_REQ;
import com.fy.engineserver.message.CAVE_HOOK_PET_RES;
import com.fy.engineserver.message.CAVE_INFO_RES;
import com.fy.engineserver.message.CAVE_LEAVE_CAVE_REQ;
import com.fy.engineserver.message.CAVE_LVUP_BUILDING_REQ;
import com.fy.engineserver.message.CAVE_OPTION_CAVE_REQ;
import com.fy.engineserver.message.CAVE_PLANT_REQ;
import com.fy.engineserver.message.CAVE_QUERY_ALL_PLANT_RES;
import com.fy.engineserver.message.CAVE_QUERY_EXCHANGE_REQ;
import com.fy.engineserver.message.CAVE_QUERY_EXCHANGE_RES;
import com.fy.engineserver.message.CAVE_QUERY_FAERYLIST_REQ;
import com.fy.engineserver.message.CAVE_QUERY_FAERYLIST_RES;
import com.fy.engineserver.message.CAVE_QUERY_SCHEDULE_RES;
import com.fy.engineserver.message.CAVE_READ_DYNAMIC_REQ;
import com.fy.engineserver.message.CAVE_SIMPLE_REQ;
import com.fy.engineserver.message.CAVE_SIMPLE_RES;
import com.fy.engineserver.message.CAVE_STORE_PET_REQ;
import com.fy.engineserver.message.CAVE_STORE_PET_RES;
import com.fy.engineserver.message.CAVE_STORE_SIZEUP_REQ;
import com.fy.engineserver.message.CAVE_TAKEOUT_PET_REQ;
import com.fy.engineserver.message.CAVE_TAKEOUT_PET_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.npc.CaveNPC;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.sprite.npc.NPC;
import com.fy.engineserver.sprite.pet.Pet;
import com.fy.engineserver.sprite.pet.PetManager;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.TimeTool;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;

public class CaveSubSystem extends SubSystemAdapter implements FaeryConfig {

	// public static Logger logger = Logger.getLogger(CaveSubSystem.class);
	public static Logger logger = LoggerFactory.getLogger(CaveSubSystem.class);

	private static CaveSubSystem instance;

	public static CaveSubSystem getInstance() {
		return instance;
	}

	@Override
	public String getName() {
		return "CaveSubSystem";
	}

	String[] InterestedMessageTypes = new String[] { "CAVE_LVUP_BUILDING_REQ", "CAVE_QUERY_FAERYLIST_REQ", "CAVE_OPTION_CAVE_REQ", "CAVE_DOOR_OPTION_REQ", "CAVE_ASSART_FIELD_REQ", "CAVE_ASSART_FIELD_REQ", "CAVE_QUERY_ALL_PLANT_REQ", "CAVE_PLANT_REQ", "CAVE_HARVEST_PLANT_REQ", "CAVE_CONVERT_PLANT_REQ", "CAVE_STORE_SIZEUP_REQ", "CAVE_STORE_PET_REQ", "CAVE_TAKEOUT_PET_REQ", "CAVE_CHECK_PLANT_REQ", "CAVE_CANCEL_SCHEDULE_REQ", "CAVE_QUERY_SCHEDULE_REQ", "CAVE_CONVERTARTICLE_REQ", "CAVE_INFO_REQ", "CAVE_HOOK_PET_REQ", "CAVE_FIND_SELFCAVE_REQ", "CAVE_LEAVE_CAVE_REQ", "CAVE_QUERY_EXCHANGE_REQ", "CAVE_ACCELERATE_RES", "CAVE_ACCELERATE_REQ", "CAVE_SIMPLE_REQ", "CAVE_READ_DYNAMIC_REQ", "CAVE_DETAILED_INFO_REQ", "CAVE_DYNAMIC_REQ" };

	@Override
	public String[] getInterestedMessageTypes() {
		// CAVE_DYNAMIC_NOTICE_REQ
		return InterestedMessageTypes;
	}

	public void init() {
		
		instance = this;
		ServiceStartRecord.startLog(this);
	}

	@Override
	public ResponseMessage handleReqeustMessage(Connection conn, RequestMessage message) throws ConnectionException, Exception {
		Player player = check(conn, message, logger);

		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if (useMethodCache) {
			ResponseMessage ret = super.handleReqeustMessage(conn, message, player);
			if (logger.isInfoEnabled()) logger.info("[角色:{}][{}][耗时:{}ms]", new Object[] { player.getName(), message.getTypeDescription(), (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start) });
			return ret;
		}
		ResponseMessage response = null;
		Class<?> clazz = this.getClass();
		Method m1 = clazz.getDeclaredMethod("handle_" + message.getTypeDescription(), Connection.class, RequestMessage.class, Player.class);
		try {
			response = (ResponseMessage) m1.invoke(this, conn, message, player);
		} catch (Exception e) {
			logger.error("[角色:" + player.getName() + "] [异常] [" + m1.getName() + "]", e);
		}
		if (logger.isInfoEnabled()) logger.info("[角色:{}][{}][耗时:{}ms]", new Object[] { player.getName(), m1.getName(), (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start) });
		return response;
	}

	@Override
	public boolean handleResponseMessage(Connection conn, RequestMessage request, ResponseMessage response) throws ConnectionException, Exception {
		return false;
	}

	public ResponseMessage handle_CAVE_DYNAMIC_REQ(Connection conn, RequestMessage message, Player player) {

		Cave cave = FaeryManager.getInstance().getCave(player);

		if (cave == null) {
			player.sendError(Translate.text_cave_007);
			return null;
		}

		if (cave.getStatus() == CAVE_STATUS_KHATAM) {
			player.sendError(Translate.text_cave_048);
			return null;
		}

		List<CaveDynamic> readMsgList = new ArrayList<CaveDynamic>();
		List<CaveDynamic> unreadMsgList = new ArrayList<CaveDynamic>();
		if (cave.getRecordList() != null) {
			for (CaveDynamic cd : cave.getRecordList()) {
				if (cd.getTime() <= cave.getOwnerLastSeeDynamic()) {
					readMsgList.add(cd);
				} else {
					unreadMsgList.add(cd);
				}
			}
		}

		CAVE_DYNAMIC_RES res = new CAVE_DYNAMIC_RES(message.getSequnceNum(), readMsgList.toArray(new CaveDynamic[0]), unreadMsgList.toArray(new CaveDynamic[0]));

		return res;
	}

	public ResponseMessage handle_CAVE_DETAILED_INFO_REQ(Connection conn, RequestMessage message, Player player) {
		return getCave_DETAILED_INFO_RES(player, (CAVE_DETAILED_INFO_REQ) message);
	}

	public CAVE_DETAILED_INFO_RES getCave_DETAILED_INFO_RES(Player player, CAVE_DETAILED_INFO_REQ req) {
		Cave cave = FaeryManager.getInstance().getCave(player);
		boolean caveValid = true;
		if (cave == null || FaeryManager.getInstance().getKhatamMap().containsKey(player.getId())) {
			caveValid = false;
			return new CAVE_DETAILED_INFO_RES(req == null ? GameMessageFactory.nextSequnceNum() : req.getSequnceNum(), caveValid, 0, 0, 0, new CaveScheduleForClient[0], new ResourceCollection(0, 0, 0), new ResourceCollection(0, 0, 0));
		}
		if (cave.getStatus() == CAVE_STATUS_KHATAM) {
			player.sendError(Translate.text_cave_048);
			return null;
		}
		long leftIncTime = cave.getIncreaseScheduleLast() + cave.getIncreaseScheduleStart() - SystemTime.currentTimeMillis();
		leftIncTime = leftIncTime <= 0 ? 0 : leftIncTime;
		List<CaveScheduleForClient> list = new ArrayList<CaveScheduleForClient>();
		for (CaveBuilding cb : cave.getBuildings().values()) {
			List<CaveSchedule> schedules = cb.getSchedules();
			if (schedules != null && schedules.size() > 0) {
				for (CaveSchedule cs : schedules) {
					CaveScheduleForClient csForClient = new CaveScheduleForClient(cs);
					if (cs.getOptionType() == OPTION_PLANT) {// 是种植
						CaveField cf = (CaveField) cb;
						CaveFieldBombData bombData = cf.getCaveFieldBombData();
						if (bombData != null && bombData.isValid()) {
							csForClient.setScheduleIcon(bombData.getBombConfig().getShowIcon());
							csForClient.setScheduleDescription("<f color='" + ArticleManager.color_article[bombData.getBombArticleColor()] + "'>" + Translate.translateString(Translate.已经埋好了炸弹, new String[][] { { Translate.STRING_1, bombData.getBombArticleName() } }) + "</f>");
						}
					}
					list.add(csForClient);
				}
			}
		}
		CAVE_DETAILED_INFO_RES res = new CAVE_DETAILED_INFO_RES(req == null ? GameMessageFactory.nextSequnceNum() : req.getSequnceNum(), caveValid, cave.getSchedules().size(), cave.getCurrMaxScheduleNum(), leftIncTime, list.toArray(new CaveScheduleForClient[list.size()]), cave.getCurrRes(), cave.getCurrMaxResource());
		return res;
	}

	public ResponseMessage handle_CAVE_READ_DYNAMIC_REQ(Connection conn, RequestMessage message, Player player) {
		CAVE_READ_DYNAMIC_REQ req = (CAVE_READ_DYNAMIC_REQ) message;
		long lasetDynameicTime = req.getLasetDynameicTime();
		Cave cave = FaeryManager.getInstance().getCave(player);
		if (cave != null) {
			if (lasetDynameicTime <= SystemTime.currentTimeMillis()) {
				cave.setOwnerLastSeeDynamic(lasetDynameicTime);
			}
		}
		return null;
	}

	/**
	 * 查询仙府
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_CAVE_SIMPLE_REQ(Connection conn, RequestMessage message, Player player) {
		CAVE_SIMPLE_REQ req = (CAVE_SIMPLE_REQ) message;
		return FaeryManager.getInstance().caveSimple(player, req);
	}

	public ResponseMessage handle_CAVE_ACCELERATE_REQ(Connection conn, RequestMessage message, Player player) {
		CAVE_ACCELERATE_REQ req = (CAVE_ACCELERATE_REQ) message;
		int buildingType = req.getBuildingType();
		int optionType = req.getOptionType();
		Cave cave = FaeryManager.getInstance().getCave(player);

		if (cave == null) {
			player.sendError(Translate.text_cave_007);
			return null;
		}

		if (player.getVipLevel() < FaeryConfig.ACCELERATE_SCHEDULE_NEED_VIPLEVEL) {
			player.sendError(Translate.translateString(Translate.text_cave_108, new String[][] { { Translate.COUNT_1, String.valueOf(FaeryConfig.ACCELERATE_SCHEDULE_NEED_VIPLEVEL) } }));
			return null;
		}

		CaveBuilding cBuilding = cave.getCaveBuilding(buildingType);
		if (cBuilding == null) {
			logger.error("[加速进度] [建筑没找到][player:{}] [buildingType={}] [optionType={}", new Object[] { player.getName(), buildingType, optionType });
			return null;
		}
		CaveSchedule cSchedule = cBuilding.getScheduleByOptType(optionType);
		if (logger.isWarnEnabled()) {
			logger.warn(player.getLogString() + "[加速进度] [player:{}] [buildingType={}] [optionType={}] [进度:{}]", new Object[] { player.getName(), buildingType, optionType, cSchedule });
		}

		if (cSchedule != null) {
			ArticleEntity ae = player.getArticleEntity(FaeryManager.accelerateArticleName);
			if (ae == null) {

				player.sendError(Translate.translateString(Translate.text_cave_101, new String[][] { { Translate.STRING_1, FaeryManager.accelerateArticleName } }));
				if (CaveSubSystem.logger.isWarnEnabled()) {
					CaveSubSystem.logger.warn(player.getLogString() + "[加速] [没有道具:" + FaeryManager.accelerateArticleName + "]");
				}
				return null;
			} else {
				MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(36000);
				mw.setDescriptionInUUB(Translate.translateString(Translate.text_cave_103, new String[][] { { Translate.STRING_1, FaeryManager.accelerateArticleName }, { Translate.STRING_2, TimeTool.instance.getShowTime(FaeryManager.accelerateTime) } }));
				mw.setTitle(Translate.text_cave_102);
				Option_UseCancel oc = new Option_UseCancel();
				oc.setText(Translate.取消);
				oc.setColor(0xffffff);

				Option_Cave_Accelerate_Schedule option_Schedule = new Option_Cave_Accelerate_Schedule(buildingType, optionType);
				option_Schedule.setText(Translate.确定);
				oc.setColor(0xffffff);
				mw.setOptions(new Option[] { option_Schedule, oc });
				QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
				player.addMessageToRightBag(res);
			}
		} else {
			player.sendError(Translate.text_cave_100);
		}
		return null;
	}

	public ResponseMessage handle_CAVE_QUERY_EXCHANGE_REQ(Connection conn, RequestMessage message, Player player) {

		CAVE_QUERY_EXCHANGE_REQ req = (CAVE_QUERY_EXCHANGE_REQ) message;
		long[] ids = req.getRequestIds();
		List<Long> responseIdList = new ArrayList<Long>();
		List<ResourceCollection> resources = new ArrayList<ResourceCollection>();
		for (long articleEntityId : ids) {
			ArticleEntity articleEntity = player.getArticleEntity(articleEntityId);
			if (articleEntity == null) {
				continue;
			}
			Article article = ArticleManager.getInstance().getArticle(articleEntity.getArticleName());
			if (article == null) {
				continue;
			}
			PlantCfg cfg = FaeryManager.getInstance().getPlantCfg(articleEntity.getArticleName());
			if (cfg == null || !cfg.outputIsConvertFruit()) {// 不是庄园果实类型
				continue;
			}
			responseIdList.add(articleEntityId);
			resources.add(new ResourceCollection(cfg.getType(), Cave.getResNum(articleEntity.getColorType(), cfg, 1)));
		}

		long[] responseIds = new long[responseIdList.size()];
		for (int i = 0; i < responseIds.length; i++) {
			responseIds[i] = responseIdList.get(i);
		}
		return new CAVE_QUERY_EXCHANGE_RES(message.getSequnceNum(), responseIds, resources.toArray(new ResourceCollection[0]));
	}

	/**
	 * 离开仙府
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_CAVE_LEAVE_CAVE_REQ(Connection conn, RequestMessage message, Player player) {
		CAVE_LEAVE_CAVE_REQ req = (CAVE_LEAVE_CAVE_REQ) message;
		if (player.getLastTransferGame() == null || "".equals(player.getLastTransferGame())) {
			if (logger.isInfoEnabled()) {
				logger.info(player.getLogString() + "[离开庄园] [上次传送地图为空] [LastTransferGame:{}] [X:{}] [Y:{}]", new Object[] { player.getLastTransferGame(), player.getLastX(), player.getLastY() });
			}
			player.setLastTransferGame(player.getResurrectionMapName());
			player.setLastX(player.getResurrectionX());
			player.setLastY(player.getResurrectionY());
		}
		if (logger.isInfoEnabled()) {
			logger.info(player.getLogString() + "[离开庄园] [LastTransferGame:{}] [X:{}] [Y:{}]", new Object[] { player.getLastTransferGame(), player.getLastX(), player.getLastY() });
		}
		player.transferGameCountry = player.getCountry();
		if (player.getCurrentGame() != null) {
			player.getCurrentGame().transferGame(player, new TransportData(0, 0, 0, 0, player.getLastTransferGame(), player.getLastX(), player.getLastY()));
			logger.error(player.getLogString() + "[离开庄园] [失败] [LastTransferGame:{}] [X:{}] [Y:{}]", new Object[] { player.getLastTransferGame(), player.getLastX(), player.getLastY() });
		} else {
			logger.error(player.getLogString() + "[离开庄园] [异常] [当前地图(player.getCurrentGame())null] [LastTransferGame:{}] [X:{}] [Y:{}]", new Object[] { player.getLastTransferGame(), player.getLastX(), player.getLastY() });
		}
		return null;
	}

	/**
	 * 查询自己家园所在位置
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_CAVE_FIND_SELFCAVE_REQ(Connection conn, RequestMessage message, Player player) {

		Cave cave = FaeryManager.getInstance().getCave(player);

		if (cave == null) {
			player.sendError(Translate.text_cave_007);
			return null;
		}

		if (cave.getStatus() == CAVE_STATUS_KHATAM) {
			player.sendError(Translate.text_cave_048);
			return null;
		}
		if (!cave.getFaery().getGame().contains(player)) {
			player.sendError(Translate.text_cave_054);
			return null;
		}
		return new CAVE_FIND_SELFCAVE_RES(message.getSequnceNum(), cave.getDoorplate().getNpc().getX(), cave.getDoorplate().getNpc().getY());
	}

	/**
	 * 宠物挂机
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_CAVE_HOOK_PET_REQ(Connection conn, RequestMessage message, Player player) {
		CAVE_HOOK_PET_REQ req = (CAVE_HOOK_PET_REQ) message;
		byte action = req.getAction();
		long petId = req.getPetId();
		long npcId = req.getNPCId();
		int index = req.getIndex();

		String failreason = Translate.text_cave_001;

		boolean isSelfCave = FaeryManager.isSelfCave(player, npcId);
		if (!isSelfCave && action == 1) {
			CaveNPC npc = (CaveNPC) MemoryNPCManager.getNPCManager().getNPC(npcId);
			if (npc != null) {
				Cave cave = npc.getCave();
				try {
					CompoundReturn cr = npc.getCave().hookPet(player, petId, index);
					if (!cr.getBooleanValue()) {
						switch (cr.getIntValue()) {
						case 1:
							failreason = Translate.text_cave_038;
							break;
						case 2:
							failreason = Translate.text_cave_034;
							break;
						case 3:
							failreason = Translate.text_cave_035;
							break;
						case 4:
							failreason = Translate.text_cave_037;
							break;
						case 5:
							failreason = Translate.text_cave_092;
							break;
						case 6:
							failreason = Translate.text_cave_085;
							break;
						case 7:
							failreason = Translate.text_cave_086;
							break;
						case 8:
							failreason = Translate.text_cave_091;
							break;
						default:
							break;
						}
						player.sendError(failreason);
						return null;
					} else {
						PetHookInfo hookInfo = (PetHookInfo) cr.getObjValue();
						if (hookInfo != null) {
							hookInfo.setExp(cave.getHookExp(hookInfo));
							CAVE_HOOK_PET_RES res = new CAVE_HOOK_PET_RES(message.getSequnceNum(), action, index, hookInfo);
							return res;
						}
					}
				} catch (Exception e) {
					logger.error(player.getLogString() + "[宠物挂机] [异常]", e);
				}
			} else {
				failreason = Translate.text_cave_033;
			}
		} else {
			failreason = Translate.text_cave_061;
			player.sendError(failreason);
			return null;
		}
		return null;
	}

	/**
	 * 查看庄园信息
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_CAVE_INFO_REQ(Connection conn, RequestMessage message, Player player) {

		Cave cave = FaeryManager.getInstance().getCave(player);
		if (cave == null) {
			player.sendError(Translate.text_cave_007);
			return null;
		}

		if (cave.getStatus() == CAVE_STATUS_KHATAM) {
			player.sendError(Translate.text_cave_048);
			return null;
		}
		String[] buildName = new String[cave.getBuildings().size()];
		String[] depend = new String[cave.getBuildings().size()];
		int[] types = new int[cave.getBuildings().size()];
		int[] grades = new int[cave.getBuildings().size()];
		int index = 0;

		ResourceCollection[] lvupCost = new ResourceCollection[cave.getBuildings().size()];

		List<CaveBuilding> order = new ArrayList<CaveBuilding>();

		for (Iterator<Long> itor = cave.getBuildings().keySet().iterator(); itor.hasNext();) {
			long id = itor.next();
			CaveBuilding building = cave.getBuildings().get(id);
			order.add(building);
		}

		Collections.sort(order);
		try {
			for (CaveBuilding building : order) {
				buildName[index] = CAVE_BUILDING_NAMES[building.getType()];
				grades[index] = building.getGrade();
				types[index] = building.getType();
				CompoundReturn info = FaeryManager.getInstance().getLvUpInfo(building.getType(), building.getGrade());
				lvupCost[index] = (ResourceCollection) info.getObjValue();
				depend[index] = info.getStringValue();
				index++;
			}
		} catch (OutOfMaxLevelException e) {
			logger.error(player.getLogString() + "[handle_CAVE_INFO_REQ]", e);
		} catch (Exception e) {
			logger.error(player.getLogString() + "[handle_CAVE_INFO_REQ]", e);
		}
		CAVE_INFO_RES res = new CAVE_INFO_RES(message.getSequnceNum(), cave.getCurrRes(), cave.getMaintenanceCost(), cave.getCurrMaxResource(), buildName, types, grades, lvupCost, depend);
		return res;
	}

	/**
	 * 
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_CAVE_QUERY_SCHEDULE_REQ(Connection conn, RequestMessage message, Player player) {

		Cave cave = FaeryManager.getInstance().getCave(player);

		if (cave == null) {
			player.sendError(Translate.text_cave_010);
			return null;
		}
		if (cave.getStatus() == CAVE_STATUS_KHATAM) {
			player.sendError(Translate.text_cave_048);
			return null;
		}

		return new CAVE_QUERY_SCHEDULE_RES(message.getSequnceNum(), cave.getSchedules().size(), cave.getCurrMaxScheduleNum(), cave.getSchedules().toArray(new CaveSchedule[0]), cave.getCurrRes(), cave.getCurrMaxResource());
	}

	/**
	 * 取消进度
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_CAVE_CANCEL_SCHEDULE_REQ(Connection conn, RequestMessage message, Player player) {

		CAVE_CANCEL_SCHEDULE_REQ req = (CAVE_CANCEL_SCHEDULE_REQ) message;
		int buildingType = req.getBuildingType();
		int optionType = req.getOptionType();

		Cave cave = FaeryManager.getInstance().getCave(player);

		if (cave == null) {
			player.sendError(Translate.text_cave_007);
			return null;
		}

		CaveBuilding cBuilding = cave.getCaveBuilding(buildingType);
		if (cBuilding == null) {
			logger.error("[取消进度][建筑没找到][player:{}][buildingType={}][optionType={}", new Object[] { player.getName(), buildingType, optionType });
			return null;
		}
		CaveSchedule cSchedule = cBuilding.getScheduleByOptType(optionType);
		logger.error(player.getLogString() + "[取消进度] [player:{}] [buildingType={}] [optionType={}] [进度:{}]", new Object[] { player.getName(), buildingType, optionType, cSchedule });

		if (cSchedule != null) { // 确实有这个进度
			cBuilding.removeScheduleForCancel(optionType);
			player.addMessageToRightBag(CaveSubSystem.getInstance().getCave_DETAILED_INFO_RES(player, null));
			
		}
		return null;
	}

	/**
	 * 查看果实成熟时间
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_CAVE_CHECK_PLANT_REQ(Connection conn, RequestMessage message, Player player) {
		CAVE_CHECK_PLANT_REQ req = (CAVE_CHECK_PLANT_REQ) message;
		long NPCId = req.getNpcId();

		long NPCIds[] = new long[0];
		long harvestTime[] = new long[0];

		try {
			CaveNPC npc = (CaveNPC) MemoryNPCManager.getNPCManager().getNPC(NPCId);
			if (npc == null) {
				if (logger.isInfoEnabled()) {
					logger.info(player.getLogString() + "[查看作物时间] [NPC==null] [NPCId:{}]", new Object[] { NPCId });
				}
				HINT_REQ hint_REQ = new HINT_REQ(message.getSequnceNum(), (byte) 0, Translate.text_cave_042);
				player.addMessageToRightBag(hint_REQ);
				return null;
			} else {
				Cave cave = npc.getCave();
				if (cave == null) {
					HINT_REQ hint_REQ = new HINT_REQ(message.getSequnceNum(), (byte) 0, Translate.text_cave_042);
					player.addMessageToRightBag(hint_REQ);
					if (logger.isInfoEnabled()) {
						logger.info(player.getLogString() + "[查看作物时间] [cave==null] [NPCId:{}]", new Object[] { NPCId });
					}
					return null;
				} else {
					List<Long> idList = new ArrayList<Long>();
					List<Long> harvestTimeList = new ArrayList<Long>();
					for (CaveField field : cave.getFields()) {
						if (field.getPlantStatus() != null) {// 有作物
							idList.add(field.getNpc().getId());
							harvestTimeList.add(field.getPlantStatus().getHarvestTime());
						}
					}
					NPCIds = new long[idList.size()];
					harvestTime = new long[harvestTimeList.size()];
					for (int i = 0; i < idList.size(); i++) {
						NPCIds[i] = idList.get(i);
					}
					for (int i = 0; i < harvestTimeList.size(); i++) {
						harvestTime[i] = harvestTimeList.get(i);
					}
				}
			}
		} catch (Exception e) {
			logger.error(player.getName() + "[查看植物成熟时间异常]", e);
		}
		return new CAVE_CHECK_PLANT_RES(message.getSequnceNum(), NPCIds, harvestTime);
	}

	/**
	 * 取出宠物(取出 存放 ,取出挂机,赶走挂机)
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_CAVE_TAKEOUT_PET_REQ(Connection conn, RequestMessage message, Player player) {
		CAVE_TAKEOUT_PET_REQ req = (CAVE_TAKEOUT_PET_REQ) message;
		byte action = req.getAction();
		long articleId = req.getPetId();
		long NPCId = req.getNPCId();

		byte result = 0;
		String failreason = Translate.text_cave_001;
		// ACTION 0 仓库 1挂机
		try {
			boolean isSelfCave = FaeryManager.isSelfCave(player, NPCId);
			if (isSelfCave && action == 1) {
				result = 1;
				failreason = Translate.text_cave_026;
			} else if (!isSelfCave && (action == 0 || action == 2)) {
				result = 1;
				failreason = Translate.text_cave_026;
			} else {
				if (isSelfCave) {
					Cave cave = FaeryManager.getInstance().getCave(player);
					if (cave.isLocked()) {
						player.sendError(Translate.仙府被锁定);
						return null;
					}
					if (action == 0) {// 取出宠物
						CompoundReturn cr = cave.takeOutStorePet(player, articleId);
						if (!cr.getBooleanValue()) {
							switch (cr.getIntValue()) {
							case 1:
								player.sendError(Translate.text_cave_089);
								return new CAVE_TAKEOUT_PET_RES(message.getSequnceNum(), action, 0L);
							case 2:
								player.sendError(Translate.text_cave_088);
								return new CAVE_TAKEOUT_PET_RES(message.getSequnceNum(), action, 0L);
							case 3:
								player.sendError(Translate.text_cave_090);
								return new CAVE_TAKEOUT_PET_RES(message.getSequnceNum(), action, 0L);
							default:
								break;
							}
						}
					} else {// 赶走宠物
						ArticleEntity articleEntity = ArticleEntityManager.getInstance().getEntity(articleId);
						if (articleEntity == null) {
							result = 1;
							failreason = Translate.text_cave_039;
						} else {
							if (articleEntity instanceof PetPropsEntity) {
								PetPropsEntity ppe = (PetPropsEntity) articleEntity;
								long realPetId = ppe.getPetId();
								Pet pet = PetManager.getInstance().getPet(realPetId);
								if (pet == null) {
									result = 1;
									failreason = Translate.text_cave_039;
								} else {
									Player petOwner = null;
									try {
										petOwner = PlayerManager.getInstance().getPlayer(pet.getOwnerId());
									} catch (Exception e) {
										e.printStackTrace();
									}
									CompoundReturn cr = cave.takeOutHookPet(petOwner, articleId, true);
									if (!cr.getBooleanValue()) {
										result = 1;
										switch (cr.getIntValue()) {
										case 1:
											failreason = Translate.text_cave_040;
											break;
										case 2:
											failreason = Translate.text_cave_041;
											break;
										default:
											break;
										}
									}
								}
							}
						}
					}
				} else {
					// 取回挂机宠物
					CaveNPC npc = (CaveNPC) MemoryNPCManager.getNPCManager().getNPC(NPCId);
					Cave cave = npc.getCave();
					CompoundReturn cr = cave.takeOutHookPet(player, articleId, false);
					if (!cr.getBooleanValue()) {
						result = 1;
						switch (cr.getIntValue()) {
						case 1:
							failreason = Translate.text_cave_040;
							break;
						case 2:
							failreason = Translate.text_cave_041;
							break;
						default:
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error(player.getLogString() + "[取回宠物异常]", e);
		}
		player.sendError(failreason);
		return new CAVE_TAKEOUT_PET_RES(message.getSequnceNum(), action, articleId);
	}

	/**
	 * 存放宠物
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_CAVE_STORE_PET_REQ(Connection conn, RequestMessage message, Player player) {

		CAVE_STORE_PET_REQ req = (CAVE_STORE_PET_REQ) message;
		byte action = req.getAction();
		long petId = req.getPetId();
		long npcId = req.getNPCId();
		int index = req.getIndex();

		String failreason = Translate.text_cave_001;

		boolean isSelfCave = FaeryManager.isSelfCave(player, npcId);
		if (isSelfCave && action == 0) {// 存放宠物
			try {
				Cave cave = FaeryManager.getInstance().getCave(player);
				if (cave.isLocked()) {
					player.sendError(Translate.仙府被锁定);
					return null;
				}
				CompoundReturn cr = FaeryManager.getInstance().getCave(player).storePet(player, petId, index);
				if (!cr.getBooleanValue()) {
					switch (cr.getIntValue()) {
					case 1:
						break;
					case 2:
						failreason = Translate.text_cave_034;
						break;
					case 3:
						failreason = Translate.text_cave_035;
						break;
					case 4:
						failreason = Translate.text_cave_036;
						break;
					case 5:
						failreason = Translate.text_cave_038;
						break;
					case 6:
						failreason = Translate.text_cave_085;
						break;
					case 7:
						failreason = Translate.text_cave_091;
						break;
					default:
						break;
					}
					player.sendError(failreason);
					return null;
				} else {
					return new CAVE_STORE_PET_RES(message.getSequnceNum(), action, petId, index);
				}
			} catch (Exception e) {
				logger.error(player.getLogString() + "[存放宠物]", e);
			}
		} else if (!isSelfCave && action == 1) {// 挂机
			CaveNPC npc = (CaveNPC) MemoryNPCManager.getNPCManager().getNPC(npcId);
			if (npc != null) {
				try {
					CompoundReturn cr = npc.getCave().hookPet(player, petId, index);
					if (!cr.getBooleanValue()) {
						switch (cr.getIntValue()) {
						case 1:
							failreason = Translate.text_cave_038;
							break;
						case 2:
							failreason = Translate.text_cave_034;
							break;
						case 3:
							failreason = Translate.text_cave_035;
							break;
						case 4:
							failreason = Translate.text_cave_037;
							break;
						case 5:
							failreason = Translate.text_cave_092;
							break;
						case 6:
							failreason = Translate.text_cave_085;
							break;
						case 7:
							failreason = Translate.text_cave_086;
							break;
						case 8:
							failreason = Translate.text_cave_091;
							break;
						default:
							break;
						}
						player.sendError(failreason);
						return null;
					}
				} catch (Exception e) {
					if (logger.isInfoEnabled()) logger.info("宠物挂机", e);
				}
			}
		} else {
			failreason = Translate.text_cave_033;
		}
		player.sendError(failreason);
		return new CAVE_STORE_PET_RES(message.getSequnceNum(), action, petId, index);
	}

	/**
	 * 提升资源等级 //TODO
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_CAVE_STORE_SIZEUP_REQ(Connection conn, RequestMessage message, Player player) {
		CAVE_STORE_SIZEUP_REQ req = (CAVE_STORE_SIZEUP_REQ) message;

		int resouceType = req.getResType();

		Cave cave = FaeryManager.getInstance().getCave(player);
		byte result = 0;
		String failreason = Translate.text_cave_001;

		if (cave == null) {
			result = 1;
			failreason = Translate.text_cave_007;
		} else {
			if (cave.isLocked()) {
				player.sendError(Translate.仙府被锁定);
				return null;
			}
			CompoundReturn cr = cave.storeSizeUp(resouceType,player);
			if (!cr.getBooleanValue()) {
				result = 1;
				switch (cr.getIntValue()) {
				case 1:
					// 判断仓库是否达到了最高等级,给予用户 不同的提示
					int storeHouseGrade = cave.getStorehouse().getGrade();
					if (storeHouseGrade >= BUILDING_MAX_LEVEL) {
						failreason = Translate.text_cave_031;
					} else {
						failreason = Translate.text_cave_063;
					}
					break;
				case 2:
					failreason = Translate.text_cave_013;
					break;
				case 3:
					failreason = Translate.text_cave_032;
					break;
				case 4:
					failreason = Translate.text_cave_033;
					break;
				default:
					break;
				}
			}
		}
		player.sendError(failreason);
		return null;
	}

	/**
	 * 兑换资源
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_CAVE_CONVERT_PLANT_REQ(Connection conn, RequestMessage message, Player player) {
		CAVE_CONVERT_PLANT_REQ req = (CAVE_CONVERT_PLANT_REQ) message;
		long articleEntityId = req.getArticleEntityId();
		int num = req.getNum();

		Cave cave = FaeryManager.getInstance().getCave(player);
		String failreason = Translate.text_cave_001;

		if (cave == null) {
			failreason = Translate.text_cave_007;
		} else {
			CompoundReturn cr = cave.exchangeRes(articleEntityId, num);
			if (!cr.getBooleanValue()) {
				switch (cr.getIntValue()) {
				case 1:
					failreason = Translate.text_cave_027;
					break;
				case 2:
					failreason = Translate.text_cave_028;
					break;
				case 3:
					failreason = Translate.text_cave_029;
					break;
				case 4:
					failreason = Translate.text_cave_030;
					break;
				case 5:
					failreason = Translate.text_cave_057;
					break;

				default:
					break;
				}
			}
		}
		CAVE_CONVERT_PLANT_RES res = new CAVE_CONVERT_PLANT_RES(GameMessageFactory.nextSequnceNum(), Translate.text_cave_001.equals(failreason) ? (byte) 0 : (byte) 1, failreason);
		return res;
	}

	/**
	 * 收获
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_CAVE_HARVEST_PLANT_REQ(Connection conn, RequestMessage message, Player player) {
		CAVE_HARVEST_PLANT_REQ req = (CAVE_HARVEST_PLANT_REQ) message;
		long NPCId = req.getNpcId();

		/** 是否是自己家的果实 */
		boolean selfHavest = false;

		Cave selfCave = FaeryManager.getInstance().getCave(player);
		CaveBuilding cBuilding = null;
		if (selfCave != null) {
			cBuilding = selfCave.getCaveBuildingByNPCId(NPCId);
			if (cBuilding != null) {// 自己庄园的植物
				selfHavest = true;
			}
		} else {
			NPC npc = MemoryNPCManager.getNPCManager().getNPC(NPCId);
			if (npc == null || !(npc instanceof CaveNPC)) {
				HINT_REQ hint_REQ = new HINT_REQ(message.getSequnceNum(), (byte) 0, Translate.text_cave_026);
				player.addMessageToRightBag(hint_REQ);
				return null;
			}
			CaveNPC cavePlantNPC = (CaveNPC) npc;
			Cave cave = cavePlantNPC.getCave();
			cBuilding = cave.getCaveBuildingByNPCId(NPCId);
		}

		if (cBuilding == null) {
			HINT_REQ hint_REQ = new HINT_REQ(message.getSequnceNum(), (byte) 0, Translate.text_cave_026);
			logger.error("[摘取果实异常] [建筑不存在] [{}][目标是否是自己庄园的:{}]", new Object[] { NPCId, selfHavest });
			player.addMessageToRightBag(hint_REQ);
			return null;
		}
		return null;
	}

	/**
	 * 种植
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_CAVE_PLANT_REQ(Connection conn, RequestMessage message, Player player) {
		if (logger.isInfoEnabled()) {
		}
		CAVE_PLANT_REQ req = (CAVE_PLANT_REQ) message;

		long NPCId = req.getNpcId();
		int plantId = req.getPlantId();

		logger.info(player.getLogString() + "[申请种植] [NPCId:{}] [plantId:{}]", new Object[] { NPCId, plantId });
		Cave cave = FaeryManager.getInstance().getCave(player);

		if (cave == null) {
			HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_cave_007);
			player.addMessageToRightBag(hint);
			return null;
		}
		if (cave.isLocked()) {
			player.sendError(Translate.仙府被锁定);
			return null;
		}
		byte result = 0;
		String failreason = Translate.text_cave_001;

		CompoundReturn cr = cave.planting(player, plantId, NPCId);
		if (!cr.getBooleanValue()) {
			result = 1;
			switch (cr.getIntValue()) {
			case 1:
				failreason = Translate.text_cave_021;
				break;
			case 2:
				failreason = Translate.text_cave_022;
				break;
			case 3:
				failreason = Translate.text_cave_015;
				break;
			case 4:
				failreason = Translate.text_cave_023;
				break;
			case 5:
				failreason = Translate.text_cave_024;
				break;
			case 6:
				failreason = Translate.text_cave_025;
				break;
			case 7:
				failreason = Translate.text_cave_013;
				break;
			case 8:
				failreason = Translate.text_cave_032;
				break;
			case 9:
				failreason = Translate.text_cave_058;
				break;
			case 10:
				failreason = Translate.text_cave_094;
				break;
			case 11:
				failreason = Translate.银子不足不能种植;
				break;
			default:
				break;
			}
		}
		if (Translate.text_cave_001.equals(failreason)) {
			player.sendNotice(failreason);
			EnterLimitManager.setValues(player, PlayerRecordType.种植次数);
		} else {
			player.sendError(failreason);
		}
		return null;
	}

	/**
	 * 查看种植列表
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_CAVE_QUERY_ALL_PLANT_REQ(Connection conn, RequestMessage message, Player player) {

		Cave cave = FaeryManager.getInstance().getCave(player);

		if (cave == null) {
			HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_cave_007);
			player.addMessageToRightBag(hint);
			return null;
		}

		int mainGrade = cave.getMainBuilding().getGrade();
		List<PlantCfg> fitPlant = new ArrayList<PlantCfg>();
		List<ResourceCollection> collections = new ArrayList<ResourceCollection>();
		for (Iterator<Integer> itor = FaeryManager.getInstance().getPlantGradeMap().keySet().iterator(); itor.hasNext();) {
			int grade = itor.next();
			if (grade <= mainGrade) {
				for (PlantCfg cfg : FaeryManager.getInstance().getPlantGradeMap().get(grade)) {
					fitPlant.add(cfg);
					collections.add(cfg.getCost());
				}
			}
		}
		return new CAVE_QUERY_ALL_PLANT_RES(message.getSequnceNum(), -1, fitPlant.toArray(new PlantCfg[0]), collections.toArray(new ResourceCollection[0]));
	}

	/**
	 * 开垦田地
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_CAVE_ASSART_FIELD_REQ(Connection conn, RequestMessage message, Player player) {

		CAVE_ASSART_FIELD_REQ req = (CAVE_ASSART_FIELD_REQ) message;

		long NPCId = req.getNpcId();

		Cave cave = FaeryManager.getInstance().getCave(player);
		if (cave == null) {
			player.sendError(Translate.text_cave_007);
			return null;
		}
		if (cave.isLocked()) {
			player.sendError(Translate.仙府被锁定);
			return null;
		}
		CaveBuilding building = cave.getCaveBuildingByNPCId(NPCId);
		if (building == null) {
			player.sendError(Translate.text_cave_016);
			return null;
		}
		byte result = 0;
		String failreason = Translate.text_cave_001;

		CompoundReturn cr = cave.assartField(building.getType(), player);
		if (!cr.getBooleanValue()) {
			result = 1;
			switch (cr.getIntValue()) {
			case 1:
				failreason = Translate.text_cave_017;
				break;
			case 2:
				failreason = Translate.text_cave_018;
				break;
			case 3:
				// 提示用户开放更多田地开垦需要庄园的等级
				int nextGrade = 0;
				int currentMainGrade = cave.getMainBuilding().getGrade();// 当前主建筑等级
				int fieldLimit = FaeryManager.getInstance().getMainCfgs()[currentMainGrade - 1].getFieldNumLimit();
				for (int i = currentMainGrade - 1; i < FaeryManager.getInstance().getMainCfgs().length; i++) {
					if (fieldLimit < FaeryManager.getInstance().getMainCfgs()[i].getFieldNumLimit()) {
						nextGrade = i + 1;
						break;
					}
				}
				if (nextGrade == 0) {
					failreason = Translate.text_cave_062;
					logger.error(player.getLogString() + "[开垦田地][异常][拿不到更高级别的建筑开放开垦][现在主建筑等级]", new Object[] { currentMainGrade });
				} else {
					failreason = Translate.translateString(Translate.text_cave_019, new String[][] { { Translate.STRING_1, String.valueOf(nextGrade) } });
				}
				break;
			case 4:
				failreason = Translate.text_cave_020;
				break;
			default:
				break;
			}
		}
		player.sendError(failreason);
		return null;
	}

	/**
	 * 建筑升级
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_CAVE_LVUP_BUILDING_REQ(Connection conn, RequestMessage message, Player player) {
		CAVE_LVUP_BUILDING_REQ req = (CAVE_LVUP_BUILDING_REQ) message;
		long npcId = req.getNpcId();

		Cave cave = FaeryManager.getInstance().getCave(player);

		if (cave == null) {
			player.sendError(Translate.text_cave_007);
			return null;
		}

		if (cave.isLocked()) {
			player.sendError(Translate.仙府被锁定);
			return null;
		}
		CaveBuilding cbBuilding = cave.getCaveBuildingByNPCId(npcId);
		CompoundReturn cr = cave.lvUp(cbBuilding, player);
		byte result = 0;
		String failreason = Translate.text_cave_001;
		if (!cr.getBooleanValue()) {
			result = 1;
			switch (cr.getIntValue()) {
			case 1:
				failreason = Translate.text_cave_010 + "[npcId:" + npcId + "]";
				logger.error(player.getLogString() + "[升级建筑不是自己家的]" + npcId);
				break;
			case 2:
				failreason = Translate.text_cave_011;
				break;
			case 3:
				failreason = Translate.text_cave_012;
				break;
			case 4:
				failreason = Translate.text_cave_013;
				break;
			case 5:
				failreason = Translate.translateString(Translate.text_cave_014, new String[][] { { Translate.STRING_1, String.valueOf(cave.getMainBuilding().getGrade() + 1) } });
				break;
			case 6:
				failreason = Translate.text_cave_015;
				break;
			case 7:
				failreason = Translate.text_cave_032;
				break;
			case 8:
				failreason = Translate.text_cave_056;
				break;
			case 9:
				failreason = Translate.text_cave_011;
				break;
			default:
				break;
			}
		}
		if (Translate.text_cave_001.equals(failreason)) {
			player.sendNotice(failreason);
		} else {
			player.sendError(failreason);
		}
		return null;
	}

	/**
	 * 对仙府的操作
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_CAVE_OPTION_CAVE_REQ(Connection conn, RequestMessage message, Player player) {
		CAVE_OPTION_CAVE_REQ req = (CAVE_OPTION_CAVE_REQ) message;

		int action = req.getAction();
		FaeryManager manager = FaeryManager.getInstance();
		String failreason = Translate.text_cave_001;
		switch (action) {
		case 0:// 创建仙府
			try {
				Cave cave = manager.createDefaultCave(player);
				if (cave == null) {
					failreason = Translate.text_cave_084;
				}
			} catch (PointNotFoundException e) {
				logger.error(player.getLogString() + "[坐标信息没找到]" + e.getMsg(), e);
				failreason = Translate.text_cave_002;
			} catch (FertyNotFountException e) {
				logger.error(player.getLogString() + "[Faery不存在]" + e.getMsg(), e);
				failreason = Translate.text_cave_003;
			} catch (CountyNotSameException e) {
				logger.error(player.getLogString() + "[国家不符]" + e.getMsg(), e);
				failreason = Translate.text_cave_004;
			} catch (IndexHasBeUsedException e) {
				logger.error(player.getLogString() + "[该位置已经被占用了]" + e.getMsg(), e);
				failreason = Translate.text_cave_005;
			} catch (AlreadyHasCaveException e) {
				logger.error(player.getLogString() + "[已经有仙府了]" + e.getMsg(), e);
				failreason = Translate.text_cave_006;
			} catch (LevelToolowException e) {
				logger.error(player.getLogString() + "[等级太低]" + e.getMsg(), e);
				failreason = Translate.translateString(Translate.text_cave_093, new String[][] { { Translate.COUNT_1, "20" } });
			} catch (Exception e) {
				logger.error("[严重错误] [仙府创建ID失败]", e);
				failreason = Translate.text_cave_095;
			}
			break;
		case 1:// 解封仙府
				// Cave cave = FaeryManager.getInstance().getCave(player);
				// if (cave != null) {
				// CompoundReturn cr = cave.notifyCave();
				// if (cr.getBooleanValue()) {
				// Object obj = cr.getObjValue();
				// if (obj != null) {
				// CavePosition position = (CavePosition) obj;
				// // 是否要通知玩家新的位置????
				// } else {
				//
				// }
				// } else {
				// int errorCode = cr.getIntValue();
				// if (errorCode == 1) {
				// failreason = Translate.text_cave_008;
				// } else if (errorCode == 2) {
				// failreason = Translate.text_cave_009;
				// }
				// }
				// } else {
				// failreason = Translate.text_cave_007;
				// }
			break;
		case 2:// 迁移仙府

			break;

		default:
			break;
		}
		player.sendError(failreason);
		return null;
	}

	/**
	 * 查看所有仙府列表
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_CAVE_QUERY_FAERYLIST_REQ(Connection conn, RequestMessage message, Player player) {

		CAVE_QUERY_FAERYLIST_REQ req = (CAVE_QUERY_FAERYLIST_REQ) message;
		byte county = req.getCounty();
		FaeryManager faeryManager = FaeryManager.getInstance();
		List<Faery> countyList = faeryManager.getCountryMap().get(Integer.valueOf(county));
		if (logger.isInfoEnabled()) {
			logger.info("国家:{},仙府个数:{}", new Object[] { county, (countyList == null ? "NULL" : countyList.size()) });
		}
		if (countyList == null) {
			if (logger.isInfoEnabled()) {
				logger.info("{}[查看所有仙府列表][列表不存在][国家:{}]", new Object[] { player.getName(), county });
			}
			HINT_REQ hint_REQ = new HINT_REQ(message.getSequnceNum(), (byte) 1, Translate.国家不存在);
			player.addMessageToRightBag(hint_REQ);
			return null;
		}
//		String[] faeryNames = new String[countyList.size()];
//		String[] mapNames = new String[countyList.size()];
		List<String> faeryNames = new ArrayList<String>();
		List<String> mapNames = new ArrayList<String>();
		List<Long> caveWherePetin = player.getHookPetCaveId();
		for (int i = 0; i < countyList.size(); i++) {
			Faery faery = countyList.get(i);
			//由于合服导致仙境列表过长，协议发送失败，无法查看某个国家的仙境列表，暂时如此处理，仙境里有仙府的才出现在列表里
			boolean hascave = false;
			for (Cave cave : faery.getCaves()) {
				if (cave != null && cave.getId() > 0) {
					hascave = true;
				}
			}
			if (!hascave) {
				continue;
			}
//			faeryNames[i] = faery.getName();
			String faeryName = faery.getName();
			if (player.getFaeryId() == faery.getId()) {
//				faeryNames[i] += Translate.自己;
				faeryName += Translate.自己;
			}
			if (faery.getCountry() == player.getCountry()) {// 只能在本国的仙府挂机
				for (int in = 0; in < faery.getCaveIds().length; in++) {
					if (caveWherePetin.contains(faery.getCaveIds()[in])) {// 有挂机的宠物在这幅地图.
//						faeryNames[i] += Translate.宠物挂机中;
						faeryName += Translate.宠物挂机中;
						break;
					}
				}
			}
			faeryNames.add(faeryName);
			mapNames.add(faery.getMemoryMapName());
//			mapNames[i] = faery.getMemoryMapName();
		}
		return new CAVE_QUERY_FAERYLIST_RES(message.getSequnceNum(), county, GAME_NAME, faeryNames.toArray(new String[0]), mapNames.toArray(new String[0]));
	}
}
