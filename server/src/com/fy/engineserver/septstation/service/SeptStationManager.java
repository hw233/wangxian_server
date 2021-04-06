package com.fy.engineserver.septstation.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.activity.fireActivity.FireActivityNpcEntity;
import com.fy.engineserver.activity.fireActivity.FireManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.JiazuSubSystem;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.homestead.faery.service.QuerySelect;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.sept.exception.NameExistException;
import com.fy.engineserver.sept.exception.SeptNotExistException;
import com.fy.engineserver.sept.exception.StationExistException;
import com.fy.engineserver.septbuilding.entity.SeptBuildingEntity;
import com.fy.engineserver.septbuilding.service.BuildingState;
import com.fy.engineserver.septbuilding.service.SeptBuildingManager;
import com.fy.engineserver.septbuilding.templet.SeptBuildingTemplet;
import com.fy.engineserver.septbuilding.templet.SeptBuildingTemplet.BuildingType;
import com.fy.engineserver.septstation.NpcStation;
import com.fy.engineserver.septstation.SeptStation;
import com.fy.engineserver.septstation.SeptStationBeatHeartThread;
import com.fy.engineserver.septstation.SeptStationMapTemplet;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.sprite.npc.SeptStationNPC;
import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.tools.cache.CacheObject;
import com.xuanzhi.tools.cache.LruMapCache;

/**
 * 家族驻地管理
 */
public class SeptStationManager {

	public static Logger logger = LoggerFactory.getLogger(JiazuSubSystem.class);
	private SeptBuildingManager septBuildingManager;

	protected static SeptStationManager self;
	// private SeptStationDao dao;
	protected boolean running = true;
	public static LruMapCache ssCache;
	/** 拆除(降级)建筑的冷却时间 */
	public static long lvDownCD = 1000 * 60 * 60 * 24l;
	/** 建筑维修间隔 */
//	public static long MaintaiCycle = 1000 * 60 * 60 * 24l;
	public static long MaintaiCycle = 1000 * 60 * 60 * 1l;			//维护时间调整为1小时  
	
	public static int 每次维护费用比率 = 24;
	/** 正常状态 */
	public static final int STATE_NORMAL = 0;
	/** 删除状态 */
	public static final int STATE_DELETE = 1;
	/** 玩家手动销毁驻地 */
	public static final int DEL_ACTION_SELF = 0;
	/** 由于资金不足 系统销毁驻地 */
	public static final int DEL_ACTION_SYS = 1;
	// /** 驻地入口所在地图 */
	// public HashMap<String, List<SeptStation>> septStationGameGrop = new HashMap<String, List<SeptStation>>();// <mapName,SeptStation>
	// /** 驻地入口所处NPC */
	// public HashMap<Long, List<SeptStation>> septStationNPCGrop = new HashMap<Long, List<SeptStation>>();// <NPCID,SeptStation>
	/** 负责心跳的线程 */
	int threadNum = 5;
	//由于合服后家族太多导致驻地内卡，所有对特定服务器增加驻地线程
	public static String[] 需要额外开启家族线程的服务器 = new String[]{"云海仙域","更端测试C"};
	public static int[] 驻地线程数 = new int[]{40, 15};
	public static long RUN_CYCLE = 1000 * 120;
	/** 负责心跳的线程 */
	public List<SeptStationBeatHeartThread> threads = new ArrayList<SeptStationBeatHeartThread>();

	public static String jiazuMapName = "jiazuditu";

	// private SeptStationRunTimeData runTimeData;
	// private SeptStationRunTimeDataDao runTimeDataDao;

	public static final long SECOND = 1000;
	public static final long MINUTE = SECOND * 60;
	public static final long HOUR = MINUTE * 60;
	public static final long DAY = HOUR * 24;
	/** 最大建筑数 */
	public static final int MAX_BUILDING_NUM = 12;
	/** 低维修标准线 （基础维修费乘以这个值） */
	public static final double lowMaintaiLine = 0.6;
	/** 家族降级资金返还比例 */
	public static final double backRate = 0.8;
	
	public static int 家族封印等级  = 20;
	
	public JiazuManager jiazuManager = null;

	public JiazuManager getJiazuManager() {
		return jiazuManager;
	}

	public void setJiazuManager(JiazuManager jiazuManager) {
		this.jiazuManager = jiazuManager;
	}

	public static SeptStationManager getInstance() {
		return self;
	}

	public SeptBuildingManager getSeptBuildingManager() {
		return septBuildingManager;
	}

	public void setSeptBuildingManager(SeptBuildingManager septBuildingManager) {
		this.septBuildingManager = septBuildingManager;
	}

	/**
	 * 发布任务的类型:建造，升级，降级，摧毁，养蚕，祝福果树
	 * 
	 * 
	 */
	public enum ActionType {
		BUILD,
		VLUP,
		LVDOWN,
		DESTROY;
	}

	/**
	 * 把驻地放入心跳列表
	 * 2011-4-29
	 * 
	 * @param station
	 *            void
	 * 
	 */
	public void addToBeatHeart(SeptStation station) {
		synchronized (threads) {
			if (threads != null && threads.size() > 0) {
				Collections.sort(threads);
				threads.get(0).addToNewList(station);
			}
		}
	}

	/**
	 * 根据家族ID,建筑类型获得驻地内建筑
	 * 2011-4-21
	 * 
	 * @param septId
	 * @param type
	 * @return SeptBuildingEntity
	 * 
	 */
	public SeptBuildingEntity getSeptBuildBySeptId(long septId, BuildingType type) {
		SeptStation septStation = getSeptStationBySeptId(septId);// 通过 septid 获得驻地
		if (septStation.isNull()) {// NULLOBJECT 稍后处理
			return null;
		}
		return septStation.getSeptBuild(type);
	}

	/**
	 * 根据家族ID获得驻地内建筑列表
	 * 2011-4-21
	 * 
	 * @param septId
	 * @return List<SeptBuildingEntity>
	 * 
	 */
	public List<SeptBuildingEntity> getSeptBuildBySeptId(long septId) {
		SeptStation septStation = getSeptStationBySeptId(septId);// 通过 septid 获得驻地
		if (septStation == null) {// NULLOBJECT 稍后处理
			return null;
		}
		List<SeptBuildingEntity> list = new ArrayList<SeptBuildingEntity>();
		list.addAll(septStation.getBuildings().values());
		return list;
	}

	/**
	 * 根据驻地ID,建筑类型获得驻地内建筑
	 * 2011-4-21
	 * 
	 * @param ssId
	 * @param type
	 * @return SeptBuildingEntity
	 * 
	 */
	public SeptBuildingEntity getSeptBuildBySSId(long ssId, BuildingType type) {
		SeptStation septStation = getSeptStationById(ssId);
		if (septStation == null) {
			return null;
		}
		return septStation.getSeptBuild(type);
	}

	/**
	 * 帮派创建时默认一个驻地
	 * 2011-4-21
	 * @param septId
	 * @param mapName
	 * @param name
	 * @param country
	 * @param NPCId入口NPC的ID
	 * @return SeptStation
	 * 
	 * @throws NameExistException
	 * @throws StationExistException
	 * @throws SeptNotExistException
	 */
	public SeptStation createDefaultSeptStation(long septId, String mapName, String name, int country, long NPCId) throws NameExistException, StationExistException, SeptNotExistException {
		// 判断名字是否重复了 name
		SeptStation checkStation = getSeptStationByName(name);
		Jiazu jiazu = JiazuManager.getInstance().getJiazu(septId);
		if (checkStation != null) {
			throw new NameExistException();
		}
		if (jiazu == null) {
			throw new SeptNotExistException();
		}
		checkStation = SeptStationManager.getInstance().getSeptStationBySeptId(septId);
		if (checkStation != null) {
			throw new StationExistException();
		}

		SeptStation septStation = new SeptStation(septId, name, country);
		try {
			long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
			septStation.setDelState(STATE_NORMAL);
			septStation.setLastMaintaiTime(now);
			septStation.setId(JiazuManager.septstationEm.nextId());
			septStation.setCreateTime(now);
			septStation.setSeptId(jiazu.getJiazuID());
			jiazu.setBaseID(septStation.getId());
			SeptStationMapTemplet mapTemplet = SeptStationMapTemplet.getInstance();
			MemoryNPCManager mnm = (MemoryNPCManager) MemoryNPCManager.getNPCManager();
			// 建所有建筑

			String gameName = SeptStationManager.jiazuMapName;
			Game gameTemplet = GameManager.getInstance().getGameByName(gameName, CountryManager.国家A);
			if (gameTemplet == null) {
				gameTemplet = GameManager.getInstance().getGameByName(gameName, CountryManager.中立);
			}

			if (gameTemplet == null) {
				logger.error("[创建家族驻地] [地图不存在:{}]", new Object[] { gameName });
				throw new IllegalStateException("家族地图不存在:" + gameName);
			}
			Game newGame = new Game(GameManager.getInstance(), gameTemplet.getGameInfo());
			newGame.init();
			newGame.country = (byte) country;
			septStation.setGame(newGame);

			HashMap<Integer, NpcStation> npcStationMap = mapTemplet.getSeptNpcStations();
			for (Iterator<Integer> itor = npcStationMap.keySet().iterator(); itor.hasNext();) {
				Integer index = itor.next();
				NpcStation npcStation = npcStationMap.get(index);

				BuildingType buildingType = SeptBuildingManager.getBuildingTypeByIndex(npcStation.getBuilingType());
				SeptStationNPC ssNpc = (SeptStationNPC) mnm.createNPC(npcStation.getNpcTempletId());
				if (ssNpc == null) {
					logger.error("[createDefaultSeptStation :NPC 不存在 ]{}", new Object[] { npcStation.getNpcTempletId() });
					continue;
				}
				SeptBuildingEntity entity = new SeptBuildingEntity(npcStation.getNpcTempletId(), septStation.getId(), npcStation.getIndex(), npcStation.getBuilingType());
				ssNpc.setX(npcStation.getX());
				ssNpc.setY(npcStation.getY());
				ssNpc.setSeptId(septStation.getId());
				ssNpc.setGameNames(septStation.getGame().gi);

				entity.setStation(septStation);
				entity.setNpc(ssNpc);
				entity.setBuildingState(new BuildingState(SeptBuildingManager.getTemplet(buildingType.getIndex()), buildingType.isDefault() ? 1 : 0));
				entity.getBuildingState().getTempletType().initDepend();
				septStation.addNewBuildings(entity);
				septStation.getGame().addSprite(ssNpc);

				if (logger.isInfoEnabled()) {
					logger.info("[为家族[{}]] [创建默认驻地,建造默认建筑:{}] [{}] [NPCID:{}] [buildingstate:{}]", new Object[] { jiazu.getName(), entity.getBuildingState().getTempletType().getName(), npcStation, ssNpc.getId(), entity.getBuildingState() });
				}
			}
			/**
			 * 创建篝火实体 初始化
			 */
			try {
				FireActivityNpcEntity en = FireManager.getInstance().createFireActivityNpc(septStation);
				septStation.setFireActivityNpcEntity(en);
			} catch (Exception e) {
				if (logger.isWarnEnabled()) logger.warn("[创建篝火实体异常 ] [id:"+septStation.getId()+"] ", e);
			}
			jiazu.setBaseID(septStation.getId());

			addToBeatHeart(septStation);
			ssCache.put(septStation.getId(), septStation);

			JiazuManager.septstationEm.flush(septStation);
		} catch (Exception e) {
			logger.error("增加默认的驻地异常:" + jiazu.getName(), e);
		}
		/* 测试 */
		if (logger.isDebugEnabled()) logger.debug("增加默认的驻地成功:{},{}", new Object[] { jiazu.getName(), jiazu.getBaseID() });
		return septStation;
	}

	/**
	 * 通过名字获得驻地
	 * 2011-4-26
	 * 
	 * @param name
	 * @return SeptStation
	 * 
	 */
	public synchronized SeptStation getSeptStationByName(String name) {
		if (name == null || name.equals("")) {
			return null;
		}
		SeptStation septStation = null;
		Object os[] = ssCache.values().toArray(new Object[0]);
		for (Object obj : os) {
			septStation = (SeptStation) (((CacheObject) obj).object);
			String oName = septStation.getName();
			if (oName.equals(name)) {
				return septStation;
			}
		}
		// System.out.println("[stationManager][检查注定名称][" + name + "]" + septStation);
		return null;
	}

	/**
	 * 获得所有家族驻地列表
	 * 2011-5-3
	 * 
	 * @return List<SeptStation>
	 * 
	 */
	public List<SeptStation> getAllStation() {
		List<SeptStation> list = new ArrayList<SeptStation>();
		Object os[] = ssCache.values().toArray(new Object[0]);
		for (int i = 0; i < os.length; i++) {
			list.add((SeptStation) ((CacheObject) os[i]).object);
		}
		return list;
	}

	/**
	 * 摧毁驻地.在解散家族和资源不够支付时调用
	 * 2011-4-21 void
	 * 
	 * 
	 */
	public void destorySeptStation(SeptStation septStation) {
		septStation.setDelState(STATE_DELETE);
	}

	/**
	 * 通过驻地ID获得驻地
	 * 2011-4-21
	 * 
	 * @param ssId
	 * @return SeptStation
	 * 
	 */
	public SeptStation getSeptStationById(long id) {
		if (id == 0) {
			return null;
		}
		SeptStation septStation = (SeptStation) ((ssCache.get(Long.valueOf(id))));// (SeptStation)(ssCache.get(Long.valueOf(id)));//(SeptStation)
		if (logger.isInfoEnabled()) logger.info("{}在cache中拿到的结果:{}", new Object[] { id, septStation });
		if (septStation == null) {
			try {
				septStation = JiazuManager.septstationEm.find(id);// dao.getSeptStation(id);
			} catch (Exception e) {
				logger.error("[通过ID获得家族驻地] [异常] [Id:" + id + "]", e);
			}
		}
		if (septStation != null) {
			ssCache.put(id, septStation);
		}
		return septStation;
	}

	/**
	 * 通过家族ID获得驻地
	 * 2011-4-21
	 * 
	 * @param fId
	 * @return SeptStation
	 * 
	 */
	public SeptStation getSeptStationBySeptId(long septId) {
		Jiazu jiazu = JiazuManager.getInstance().getJiazu(septId);
		if (jiazu != null) {
			if (jiazu.getBaseID() > 0) {
				return SeptStationManager.getInstance().getSeptStationById(jiazu.getBaseID());
			} else {
				logger.error("[通过家族Id获得驻地] [家族ID异常] [id={" + septId + "}] [jiazu.getBaseID():" + jiazu.getBaseID() + "]");
			}
		} else {
			if (logger.isInfoEnabled()) logger.info("通过家族Id获得驻地 [家族不存在] id = {}", new Object[] { septId });
		}
		return null;
	}

	/**
	 * 得到在同地图的帮派驻地
	 * 2011-4-21
	 * 
	 * @param gameName
	 * @return List<SeptStation>
	 * 
	 */
	public List<SeptStation> getSeptStationsAt(String gameName) {
		return null;
	}

	/**
	 * 隐藏家族资源。依赖于地窖等级
	 * 2011-4-24
	 * 
	 * @param num
	 *            void
	 * 
	 */
	public void hiddenRes(int num) {

	}

	public void destroySeptBuilding(SeptStation station, BuildingType buildingType) {

	}

	/*************************************************************/
	int count = 0;

	//
	// public void run() {// 定时保存到DB
	// while (running) {
	// try {
	// Thread.sleep(RUN_CYCLE);
	// count++;
	// saveAll("定时保存");
	// } catch (Throwable e) {
	// logger.error("[家族驻地][定时保存数据][异常]{}", new Object[] { e });
	// }
	// }
	// }

	// private void saveAll(String type) {
	// long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
	// int num = 0;
	// Object os[] = ssCache.values().toArray(new Object[0]);
	// int size = 0;
	// for (int i = 0; i < os.length; i++) {
	// try {
	// Object o = os[i];
	// SeptStation station = (SeptStation) ((CacheObject) o).object;
	// size += station.getSize();
	// if (station.isNew()) {
	// dao.insert(station);
	// num++;
	// } else {
	// if (station.isDirty()) {
	// dao.update(station);
	// num++;
	// }
	// }
	// } catch (Exception e) {
	// logger.error("[家族驻地管理]");
	// }
	// }
	// if (logger.isInfoEnabled()) logger.info("[家族驻地管理]第{}次[{}]保存了[{}]个对象[耗时]{}毫秒", new Object[] { (count), type, num,
	// (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start) });
	// }

	/**
	 * 加载所有的家族驻地
	 */
	public void init() {
		
		try {
			if (logger.isInfoEnabled()) logger.info("----------加载驻地-------------");
			self = this;
			long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();

			ssCache = new LruMapCache(32 * 1024 * 1024, 8L * 24 * 60 * 60 * 1000);

			QuerySelect<SeptStation> querySelect = new QuerySelect<SeptStation>();

			List<SeptStation> allSeptStations = querySelect.selectAll(SeptStation.class, null, new Object[]{},null, JiazuManager.septstationEm);
			for (SeptStation septStation : allSeptStations) {
				try{
					boolean success = septStation.init();
					if (!success) {
						logger.error("[加载家族驻地异常!] [init返回false] [{}]", new Object[] { septStation.getId(), septStation.getMapName()});
						continue;
					}
					ssCache.put(septStation.getId(), septStation);
				}catch(Exception e){
					e.printStackTrace();
					logger.error("[加载家族驻地异常22] [name:"+septStation.getName()+"] [map:"+septStation.getMapName()+"]", e);
				}
			}
			// 心跳线程初始化
			int ttNum = threadNum;
			String sverName = "";
			if (GameConstants.getInstance() != null) {
				sverName = GameConstants.getInstance().getServerName();
				for (int i=0; i<需要额外开启家族线程的服务器.length; i++) {
					if (需要额外开启家族线程的服务器[i].equals(sverName)) {
						ttNum = 驻地线程数[i];
						break;
					}
				}
			}
			if (logger.isWarnEnabled()) {
				logger.warn("[服务器启动驻地管理线程] [开启线程个数:" + ttNum + "] [sverName:" + sverName + "]");
			}
			for (int i = 0; i < ttNum; i++) {
				threads.add(new SeptStationBeatHeartThread());
			}
			int index = 0;
			for (SeptStationBeatHeartThread thread : threads) {
				thread.setName(Translate.text_6316 + (++index));
				thread.setBeatheart(10);
			}
			// 平均一个线程需要处理的任务数(平均分到各个线程)
			int avg = (ssCache.values().size() + threads.size()) / threads.size();
			Object[] objects = ssCache.values().toArray(new Object[0]);

			int threadSize = threads.size();
			for (int i = 0; i < objects.length; i++) {
				SeptStation septStation = (SeptStation) ((CacheObject) objects[i]).object;
				threads.get(threadSize - 1 - (i / avg)).addToNewList(septStation);
			}
			for (SeptStationBeatHeartThread t : threads) {
				t.start();
			}
		} catch (Exception e) {
			logger.error("[加载家族驻地异常]", e);
			return;
		}
		ServiceStartRecord.startLog(this);
	}

	public void destroy() {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		logger.error("[系统关闭调用][SSManaget]开始");
		JiazuManager.septstationEm.destroy();
		logger.error("[系统关闭调用][SSManaget]结束,耗时[{}毫秒]", new Object[] { (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start) });
	}

	/**
	 * 为某个驻地创建一个建筑
	 * 2011-4-26
	 * 
	 * @param buildingType
	 * @param septStation
	 * @param index
	 *            建造在某个位置
	 * @return SeptBuildingEntity
	 * 
	 */
	public static SeptBuildingEntity createEntityForStation(BuildingType buildingType, SeptStation septStation, int index) {
		SeptBuildingTemplet templet = SeptBuildingManager.getTemplet(buildingType.getIndex());

		NpcStation npcStation = SeptStationMapTemplet.getInstance().getSeptNpcStations().get(index);

		MemoryNPCManager mnm = (MemoryNPCManager) MemoryNPCManager.getNPCManager();
		// 位置信息
		int realNpcId = mnm.getNPCTempalteByCategoryName(buildingType.getName()).NPCCategoryId;
		SeptStationNPC npc = (SeptStationNPC) mnm.createNPC(realNpcId);
		SeptBuildingEntity buildingEntity = new SeptBuildingEntity(realNpcId, septStation.getId(), index, npcStation.getBuilingType());
		buildingEntity.setStation(septStation);
		buildingEntity.setTempletIndex(buildingType.getIndex());
		buildingEntity.setBuildingState(new BuildingState(templet, 1));
		buildingEntity.setNpc(npc);

		npc.setX(npcStation.getX());
		npc.setY(npcStation.getY());
		npc.setGrade(1);
		npc.setSeptId(septStation.getId());

		buildingEntity.setIndex(npcStation.getIndex());
		septStation.addNewBuildings(buildingEntity);
		septStation.getGame().addSprite(npc);
		return buildingEntity;
	}

	/**
	 * 移除一个驻地
	 * 2011-4-28
	 * @param station
	 *            void
	 * 
	 */
	public void removeSeptStation(SeptStation station) {
		station.setDelState(STATE_DELETE);
		ssCache.remove(station.getId());
	}

	/**
	 * 将long型的时间间隔转成String的时间间隔
	 * 2011-5-3
	 * 
	 * @param timeDistance
	 * @return String
	 * 
	 */
	public static String timeDistanceLong2String(long timeDistance) {
		long day = timeDistance / DAY;
		timeDistance = timeDistance % DAY;
		long hour = timeDistance / HOUR;
		timeDistance = timeDistance % HOUR;
		long minute = timeDistance / MINUTE;
		timeDistance = timeDistance % MINUTE;
		long second = timeDistance / SECOND;
		StringBuffer sbf = new StringBuffer();
		sbf.append(day).append(Translate.text_6341).append(hour).append(Translate.text_6342).append(minute).append(Translate.text_6343).append(second).append(Translate.text_6344);
		return sbf.toString();
	}

}
