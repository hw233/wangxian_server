package com.fy.engineserver.septstation;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;

import com.fy.engineserver.activity.fireActivity.FireActivityNpcEntity;
import com.fy.engineserver.activity.fireActivity.FireManager;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.JiazuSubSystem;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.core.res.ResourceManager;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.homestead.cave.resource.Point;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.JiazuMember;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.jiazu2.manager.JiazuManager2;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTICE_CLIENT_JIAZUBOSS_REQ;
import com.fy.engineserver.sept.exception.ActionIsCDException;
import com.fy.engineserver.sept.exception.BuildingNotFoundException;
import com.fy.engineserver.sept.exception.WrongLvOnLvDownException;
import com.fy.engineserver.septbuilding.entity.SeptBuildingEntity;
import com.fy.engineserver.septbuilding.service.SeptBuildingManager;
import com.fy.engineserver.septbuilding.templet.FangJuFang;
import com.fy.engineserver.septbuilding.templet.HuanYuZhen;
import com.fy.engineserver.septbuilding.templet.JiaZuQi;
import com.fy.engineserver.septbuilding.templet.JuBaoZhuang;
import com.fy.engineserver.septbuilding.templet.JuXianDian;
import com.fy.engineserver.septbuilding.templet.LongTuGe;
import com.fy.engineserver.septbuilding.templet.SeptBuildingTemplet;
import com.fy.engineserver.septbuilding.templet.WuQiFang;
import com.fy.engineserver.septbuilding.templet.SeptBuildingTemplet.BuildingType;
import com.fy.engineserver.septstation.service.SeptStationManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.sprite.monster.BossMonster;
import com.fy.engineserver.sprite.monster.MemoryMonsterManager;
import com.fy.engineserver.sprite.monster.Monster;
import com.fy.engineserver.sprite.monster.Monster.AttackRecord;
import com.fy.engineserver.sprite.npc.ForLuckFruitNPC;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.sprite.npc.SeptStationNPC;
import com.fy.engineserver.sprite.pet.Pet;
import com.xuanzhi.tools.cache.CacheListener;
import com.xuanzhi.tools.cache.Cacheable;
import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

/**
 * 家族驻地
 */
@SimpleEntity
public class SeptStation implements Cacheable, CacheListener {

	public static Logger logger = JiazuSubSystem.logger;
	public static DecimalFormat df = new DecimalFormat("##0.00%");
	/** ID */
	@SimpleId
	private long id;
	@SimpleVersion
	private int version;
	/** 家族ID */
	private long septId;
	/** 所属国家 */
	private int country;
	/** 家族驻地名字 */
	@SimpleColumn(length = 60)
	private String name;
	/** 地图ID */
	@SimpleColumn(length = 60)
	private String mapName;
	/** 有建筑在建造中 */
	private boolean inBuilding = false;;
	/** 最后一次摧毁/降级 建筑时间 */
	private long lastDestoryTime;
	/** 删除标记 0:正常,1删除 */
	private int delState;
	/** 最后一次修理时间 */
	private long lastMaintaiTime;
	/** 驻地信息集合 */
	private transient SeptStationInfo info = new SeptStationInfo();
	/** 家族内建筑实体 */
	private SeptBuildingEntity[] buildingEntities = new SeptBuildingEntity[SeptStationManager.MAX_BUILDING_NUM];
	/** 驻地建筑 */
	private transient Hashtable<Long, SeptBuildingEntity> buildings = new Hashtable<Long, SeptBuildingEntity>();// <NPCID,SeptBuildingEntity>
	/** 创建时间 */
	private long createTime;
	/** 销毁时间 */
	private long destoryTime;
	/** 销毁动作 0：玩家删除驻地 1：资源不够自动销毁 */
	private int destoryAction;
	/** 家族驻地是否提供福利(当维修费用不足，停止一切福利) */
	private boolean hasBoon = false;

	transient Jiazu jiazu;

	private transient Game game;

	/** 家族Boss是否还活着.召唤和击杀设置 */
	private transient boolean jiazuBossalive = false;
	/** 本次BOSS移除时间[减少判断] */
	private transient Calendar thisBossRemoveTime;
	/** 本次召唤的BOSS */
	private transient BossMonster thisBoss;
	/** 是否使用着 */
	private transient boolean used = true;

	public boolean isUsed() {
		return used;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}

	/**
	 * 篝火活动新加的npc实体对象
	 */
	private FireActivityNpcEntity fireActivityNpcEntity;

	public FireActivityNpcEntity getFireActivityNpcEntity() {
		return fireActivityNpcEntity;
	}

	public void setFireActivityNpcEntity(FireActivityNpcEntity fireActivityNpcEntity) {
		this.fireActivityNpcEntity = fireActivityNpcEntity;
		notifyFieldChange("fireActivityNpcEntity");
	}

	public SeptStation() {

	}
	
	public String getMapName2UpLv(){
		int level = getMainBuildingLevel();
		String name = SeptStationManager.jiazuMapName;
		if(level == 2){
			name = "jiazudituyi";
		}else if(level == 4){
			name = "jiazudituer";
		}else if(level == 6){
			name = "jiazuditusan";
		}else if(level >= 8){
			name = "jiazuditusi";
		}
		return name;
	}
	

	public SeptStation(long septId, String name, int country) {
		this.septId = septId;
		this.name = name;
		this.country = country;
	}

	/**
	 * 通NPCID获得建筑实例
	 * 2011-4-27
	 * 
	 * @param id
	 * @return SeptBuildingEntity
	 * 
	 */
	public SeptBuildingEntity getSeptBuildingByNPCId(long NPCId) {
		return getBuildings().get(NPCId);
	}

	/**
	 * 得到驻地内某个建筑
	 * 2011-4-21
	 * 
	 * @param type
	 * @return SeptBuild
	 * 
	 */
	public SeptBuildingEntity getSeptBuild(BuildingType type) {
		for (Iterator<Long> iterator = buildings.keySet().iterator(); iterator.hasNext();) {
			Long NPCId = (Long) iterator.next();
			if (buildings.get(NPCId).getBuildingState() != null) {
				if (buildings.get(NPCId).getBuildingState().getTempletType().getBuildingType().getIndex() == type.getIndex()) {
					return buildings.get(NPCId);
				}
			}
		}
		return null;
	}

	/**
	 * 得到某个位置上的建筑
	 * 2011-4-28
	 * 
	 * @param index
	 * @return SeptBuildingEntity
	 * 
	 */
	public SeptBuildingEntity getSeptBuildInIndex(int index) {
		for (SeptBuildingEntity entity : buildings.values()) {
			if (entity.getIndex() == index) {
				return entity;
			}
		}
		throw new IllegalStateException();
	}

	/**
	 * 得到驻地内类型建筑列表
	 * 2011-4-21
	 * 
	 * @param type
	 * @return SeptBuild
	 * 
	 */
	public List<SeptBuildingEntity> getSeptBuildList(BuildingType type) {
		List<SeptBuildingEntity> result = new ArrayList<SeptBuildingEntity>();
		for (Iterator<Long> iterator = buildings.keySet().iterator(); iterator.hasNext();) {
			Long NPCId = (Long) iterator.next();
			SeptBuildingEntity entity = buildings.get(NPCId);
			if (entity.getBuildingState().getTempletType().getBuildingType() == type) {
				result.add(entity);
			}
		}
		return result;
	}

	/**
	 * 2011-4-26
	 * 
	 * @return int
	 * 
	 */
	public int getMaxArrowTowerNum() {
		return HuanYuZhen.getInstance().getMaxArrowTowerNum()[this.getSeptBuild(BuildingType.仙兽房).getBuildingState().getLevel()];
	}

	/**
	 * 驻地新增一个建筑
	 * 2011-4-26
	 * 
	 * @param buildingEntityKey
	 * @param buildingEntity
	 *            void
	 * 
	 */
	public void addNewBuildings(SeptBuildingEntity buildingEntity) {
		synchronized (buildings) {
			if (!buildings.containsKey(buildingEntity.getNpc().getId())) {
				buildings.put(buildingEntity.getNpc().getId(), buildingEntity);
				buildingEntities[buildingEntity.getIndex()] = buildingEntity;
				notifyFieldChange("buildingEntities");
			} else {
				throw new IllegalStateException();
			}
		}
	}

	/**
	 * 重新计算属性值
	 * 2011-4-21
	 * 
	 * @return SeptStationInfo
	 * 
	 */
	public void initInfo() {
		// 有待优化
		SeptStationInfo info = new SeptStationInfo();
		int prosper = 0;// 繁荣度
		int maintai = 0;// 维修费
		int baseMaintai = 0; // 基础维修费
		int currentBiaozhixiangNum = 0; // 标志像数量
		for (Iterator<Long> iterator = buildings.keySet().iterator(); iterator.hasNext();) {
			Long id = iterator.next();
			SeptBuildingEntity entity = buildings.get(id);
			if (entity == null) {
				continue;
			}
			if (entity.getNpc() == null || ((SeptStationNPC) entity.getNpc()).getLevel() <= 0 || entity.getBuildingState() == null) {
				continue;
			}
			int level = entity.getBuildingState().getLevel() - 1;
			if (level < 0) {
				continue;
			}
			SeptBuildingTemplet templet = entity.getBuildingState().getTempletType();

			prosper += templet.getLvUpAddProsper()[level];
			maintai += templet.getDailyMaintainCost()[level] / SeptStationManager.每次维护费用比率;
			BuildingType type = templet.getBuildingType();

			switch (type) {
			case 聚仙殿:
//				info.setMaxCoin((JuXianDian.getInstance().getMaxCoin()[level]));
				baseMaintai += JuXianDian.getInstance().getBaseMaintainCost()[level];
				break;
			case 聚宝庄:
				info.setMaxCoin((JuBaoZhuang.getInstance().getMaxCoin()[level]));
				info.setJuBaoZhuangMaxHpAdd(JuBaoZhuang.getInstance().getGradeforMaxHp()[level]);
				info.setJuBaoZhuangMaxMpAdd(JuBaoZhuang.getInstance().getGradeforMaxMp()[level]);
				baseMaintai += JuBaoZhuang.getInstance().getBaseMaintainCost()[level];
				break;
			case 仙灵洞:
				info.setMaxMember(jiazu.maxMember());
				break;
			case 龙图阁:
				info.setMaxSprint(LongTuGe.getInstance().getMaxSpirit()[level]);
				info.setMaxSpecialGoodsNum(LongTuGe.getInstance().getMaxSpecialGoodsNum()[level]);
				break;
			case 仙兽房:
				info.setMaxTankNum(HuanYuZhen.getInstance().getMaxTankNum()[level]);
				break;
			case 武器坊:
				info.setMaxGradeOfWuQi(WuQiFang.getInstance().getMaxGradeOfWuQi()[level]);
				break;
			case 防具坊:
				info.setMaxGradeOfFangJu(FangJuFang.getInstance().getMaxGradeOfFangJu()[level]);
				break;
			case 家族大旗:
				info.setJiaZuDaQiMaxHp(JiaZuQi.getInstance().getHp()[level]);
				break;
			case 标志像朱雀:
			case 标志像玄武:
			case 标志像青龙:
			case 标志像白虎:
				currentBiaozhixiangNum++;
				break;
			default:
				break;
			}

		}
		info.setBaseMaintai(baseMaintai);
		info.setProsper(prosper);
		info.setCurrMaintai(maintai);//2014年8月28日18:13:31   维护费就是所有的加和
//		info.setCurrMaintai(maintai + info.getBaseMaintai());
		info.setLowMaintai(info.getBaseMaintai() / 200);
		info.setCurrentBiaozhixiangNum(currentBiaozhixiangNum);
		setInfo(info);
		Jiazu jiazu = JiazuManager.getInstance().getJiazu(getSeptId());
		if (jiazu != null) {

			jiazu.setProsperity(prosper);
		} else {
			JiazuSubSystem.logger.error("[计算家族数值] [异常] [家族不存在] [家族ID:" + getSeptId() + "]", new Exception());
		}
	}

	/**
	 * 主建筑(聚仙殿)等级
	 * 2011-4-24
	 * 
	 * @return int
	 * 
	 */
	public int getMainBuildingLevel() {
		return getBuildingLevel(BuildingType.聚仙殿);
	}

	/**
	 * 得到各个建筑的等级
	 * 2011-4-24
	 * 
	 * @param buildingType
	 * @return int
	 * 
	 */
	public int getBuildingLevel(BuildingType buildingType) {
		SeptBuildingEntity buildingEntity = this.getSeptBuild(buildingType);
		if (buildingEntity != null) {
			return buildingEntity.getBuildingState().getLevel();
		}
		return -1;
	}

	public boolean isNull() {
		return false;
	}

	public void remove(int arg0) {

	}

	public int getSize() {
		return 0;
	}

	/**
	 * 是否到了该修理的时间
	 * 2011-4-25
	 * 
	 * @return boolean
	 * 
	 */
	public boolean needMaintai() {
		return com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - this.getLastMaintaiTime() >= SeptStationManager.MaintaiCycle;
	}

	/**
	 * 钱是否够修的
	 * 2011-4-25
	 * 
	 * @return boolean
	 * 
	 */
	public boolean enoughToMaintai() {
		return true;
	}

	/**
	 * 所有建筑的维修费
	 * 2011-4-25
	 * 
	 * @return int
	 * 
	 */
	public int getTotalMaintaiCost() {
		int cost = 0;
		for (SeptBuildingEntity entity : buildings.values()) {
			cost += entity.getDailyMaintaiCost();
		}
		return cost;
	}

	/**
	 * 当前建造的标志像实体
	 * 没有则返回NULL
	 * @return
	 */
	public SeptBuildingEntity getCurrentBiaozhixiang() {
		for (SeptBuildingEntity entity : buildings.values()) {
			if (entity.getBuildingState().getLevel() > 0) {
				if (entity.getBuildingState().getTempletType().getBuildingType().isBiaozhixiang()) {
					return entity;
				}
			}
		}
		return null;
	}

	/***************************************** Getters Setters **************************************/

	public long getId() {
		return id;
	}

	public SeptBuildingEntity[] getBuildingEntities() {
		return buildingEntities;
	}

	public void setBuildingEntities(SeptBuildingEntity[] buildingEntities) {
		this.buildingEntities = buildingEntities;
		notifyFieldChange("buildingEntities");
	}

	public Jiazu getJiazu() {
		return jiazu;
	}

	public void setJiazu(Jiazu jiazu) {
		this.jiazu = jiazu;
	}

	public boolean isHasBoon() {
		return hasBoon;
	}

	public void setHasBoon(boolean hasBoon) {
		this.hasBoon = hasBoon;
		notifyFieldChange("hasBoon");
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
		notifyFieldChange("createTime");
	}

	public long getDestoryTime() {
		return destoryTime;
	}

	public void setDestoryTime(long destoryTime) {
		this.destoryTime = destoryTime;
		notifyFieldChange("destoryTime");
	}

	public int getDestoryAction() {
		return destoryAction;
	}

	public void setDestoryAction(int destoryAction) {
		this.destoryAction = destoryAction;
		notifyFieldChange("destoryAction");
	}

	public int getCountry() {
		return country;
	}

	public void setCountry(int country) {
		this.country = country;
		notifyFieldChange("country");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		notifyFieldChange("name");
	}

	public long getLastMaintaiTime() {
		return lastMaintaiTime;
	}

	public void setLastMaintaiTime(long lastMaintaiTime) {
		this.lastMaintaiTime = lastMaintaiTime;
		notifyFieldChange("lastMaintaiTime");
	}

	public int getDelState() {
		return delState;
	}

	public void setDelState(int delState) {
		this.delState = delState;
		notifyFieldChange("delState");
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getSeptId() {
		return septId;
	}

	public void setSeptId(long septId) {
		this.septId = septId;
	}

	// public String getBelondMapName() {
	// return belondMapName;
	// }
	//
	// public void setBelondMapName(String belondMapName) {
	// this.belondMapName = belondMapName;
	// }

	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
		notifyFieldChange("mapName");
	}

	public boolean isInBuilding() {
		return inBuilding;
	}

	public void setInBuilding(boolean inBuilding) {
		this.inBuilding = inBuilding;
		notifyFieldChange("inBuilding");
	}

	public long getLastDestoryTime() {
		return lastDestoryTime;
	}

	public void setLastDestoryTime(long lastDestoryTime) {
		this.lastDestoryTime = lastDestoryTime;
		notifyFieldChange("lastDestoryTime");
	}

	// public boolean isDirty() {
	// return dirty;
	// }
	//
	// public void setDirty(boolean dirty) {
	// this.dirty = dirty;
	// }

	public Hashtable<Long, SeptBuildingEntity> getBuildings() {
		return buildings;
	}

	public void setBuildings(Hashtable<Long, SeptBuildingEntity> buildings) {
		this.buildings = buildings;
	}

	public SeptStationInfo getInfo() {
		return info;
	}

	public void setInfo(SeptStationInfo info) {
		this.info = info;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public void heartbeat() {
		try {
			if (!used || jiazu == null) {
				return;
			}
			Calendar now = Calendar.getInstance();
			// 维护
			maintai(now);
			// 处理boss
//			dealBoss(now);

			if (this.fireActivityNpcEntity != null) {
				fireActivityNpcEntity.heartbeat();
			}
			try {
				{
					// 家族的祝福果树心跳
					ForLuckFruitNPC[] forluckNpcs = jiazu.getFruitNPCs();
					if (forluckNpcs != null && forluckNpcs.length > 0) {
						for (ForLuckFruitNPC forluck : forluckNpcs) {
							if (forluck != null) {
								if (!forluck.isInService()) {// 非可摘取状态
									if (now.getTimeInMillis() >= forluck.getGrowupTime()) {// 成熟了,可以收获
										forluck.setInService(true);// 可收获状态;
									}
									forluck.setTitle(forluck.getCurrentTitle());
								} else {// 在摘取状态.判断是否该死亡了
									if (now.getTimeInMillis() >= forluck.getDeadTime()) {// 成熟了,可以收获
										forluck.onDead();
									}
								}
							}

						}
					}
				}
				
				{
					if(this.getInBuildBuilding() != null){
						this.getInBuildBuilding().realLevelUp();
					}
				}
			} catch (Throwable e) {
				logger.error("[家族驻地] [心跳异常]", e);
			}
			game.heartbeat();
		} catch (Throwable e) {
			logger.error("[家族驻地] [心跳异常]", e);

		}
	}

	/* *
	 * 处理召唤boss相关
	 * @param now
	 */
	private void dealBoss(Calendar now) {
		// 先判断家族等级是否符合召唤.5级内不开放
		if (jiazu == null) {
			// logger.error("[驻地心跳] [家族不存在] [家族ID:" + septId + "] [驻地ID:" + id + "]");
			return;
		}
		if (!jiazuBossalive && jiazu.getLevel() < 5) {
			// logger.error(jiazu.getName() + "[到了召唤boss时间] [家族等级太低,没有召唤] [当前家族等级:" + jiazu.getLevel() + "]");
			return;
		}
		if (jiazuBossalive) { // 如果boss活着,判断是否需要移除BOSS
			if (now.after(thisBossRemoveTime)) {// 应该移除boss,但是没有被击杀,移除BOSS
				String bossName = thisBoss.getName();
				// 移除BOSS
				if (thisBoss == null) {
					logger.error("[驻地心跳] [处理家族BOSS] [异常] [boss不存在] [家族:" + jiazu.getName() + "]");
				} else {
					getGame().removeSprite(thisBoss);
					logger.error("[驻地心跳] [处理家族BOSS] [击杀期限到了,boss还活着] [直接移除boss:" + thisBoss.getName() + "(" + thisBoss.getHp() + "/" + thisBoss.getMaxHP() + ")] [家族:" + jiazu.getName() + "]");
				}
				{
					thisBoss = null;
					jiazuBossalive = false;// boss活着的状态设置为false
					// 通知所有人击杀完毕,取消按钮
					for (LivingObject lo : getGame().getLivingObjects()) {
						if (lo instanceof Player) {
							noticeJiazuBossButton((Player) lo, false);
						}
					}
					// 发系统消息
					ChatMessageService.getInstance().sendMessageToJiazu(jiazu, bossName + "收到异界的召唤，回到了异界！很遗憾,未能成功击杀异界" + bossName + "！", "");
				}
			}
		} else { // 如果boss没有在活着状态,判断时间是否要刷新一个boss
			if (SeptStationMapTemplet.getInstance().isInbossActivityTime(now)) {// 如果在活动期间
				// 如果最后一次杀死时间和现在时间不是同一个活动周期.则召唤
				Calendar lastCallBoss = Calendar.getInstance();
				lastCallBoss.setTimeInMillis(getJiazu().getLastCallbossTime());
				if (!inSameDay(now, lastCallBoss)) {// 在活动期间,而和上次召唤时间不是同一天,则召唤
					logger.warn("[驻地心跳] [处理家族BOSS] [需要召唤] [家族:" + jiazu.getName() + "]");
					callJiazuboss(now);
					logger.warn("[驻地心跳] [处理家族BOSS] [召唤成功] [家族:" + jiazu.getName() + "]");
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("[驻地心跳] [处理家族BOSS] [和上次召唤是同一天] [不召唤] [家族:" + jiazu.getName() + "] [时间:" + getJiazu().getLastCallbossTime() + "] [" + getJiazu().hashCode() + "]");
					}
				}
			}
		}
	}

	/**
	 * 2个时间是否在同一个boss活动周期
	 * 前提是
	 * @param c1
	 * @param c2
	 * @return
	 */
	private boolean inSameDay(Calendar c1, Calendar c2) {
		if (c1.get(Calendar.YEAR) != c2.get(Calendar.YEAR)) {
			return false;
		}
		if (c1.get(Calendar.DAY_OF_YEAR) != c2.get(Calendar.DAY_OF_YEAR)) {
			return false;
		}
		return true;
	}

	public void noticeBossKilled(BossMonster bossMonster) {
		if (bossMonster != thisBoss) {
			logger.error("[严重错误] [boss被击杀与家族记录的boss不同] [家族boss:" + thisBoss + "] [被杀死的boss:" + bossMonster + "] [boss生存状态:" + jiazuBossalive + "]");
			jiazuBossalive = false;
			return;
		}
		List<Long> alreadyPrized = new ArrayList<Long>();
		if (bossMonster.attackRecordList != null) {
			StringBuffer backBuffer = new StringBuffer("●");
			for (AttackRecord ar : bossMonster.attackRecordList) {
				if (ar != null) {
					LivingObject lo = ar.living;
					if (lo != null) {
						if (lo instanceof Player && !alreadyPrized.contains(lo.getId())) {// 此处是单线程．不许考虑
							Player attacker = (Player) lo;
							JiazuSubSystem.logger.warn("[攻击列表里有人" + attacker.getJiazuLogString() + "]");
							if (attacker.getJiazuId() == bossMonster.getJiazuId()) {
								JiazuMember jiazuMember = JiazuManager.getInstance().getJiazuMember(attacker.getId(), bossMonster.getJiazuId());
								if (jiazuMember == null) {
									JiazuSubSystem.logger.error("[家族:" + jiazu.getName() + "] [家族boss:" + bossMonster.getName() + "被击杀] [成员不存在:{" + attacker.toString() + "}]");
									continue;
								}
								alreadyPrized.add(attacker.getId());
								if (bossMonster.getLastAttacker() != null && bossMonster.getLastAttacker().getId() == attacker.getId()) {// 是最后一击者
									try {
										jiazuMember.setCurrentWeekContribution(jiazuMember.getCurrentWeekContribution() + JiazuManager.CONTRIBUTION_ADD_BY_KILL_JIAZUBOSS);
										if (JiazuSubSystem.logger.isWarnEnabled()) {
											JiazuSubSystem.logger.warn(jiazuMember.getPlayerID() + "[获得贡献度] [击杀家族BOSS] [增加贡献度:" + JiazuManager.CONTRIBUTION_ADD_BY_KILL_JIAZUBOSS + "] [增加后贡献度:" + jiazuMember.getCurrentWeekContribution() + "]");
										}
										attacker.sendError("恭喜你成功击杀" + bossMonster.getName() + "获得家族贡献:" + JiazuManager.CONTRIBUTION_ADD_BY_KILL_JIAZUBOSS);

										ChatMessageService.getInstance().sendMessageToJiazu(jiazu, "家族boss:" + bossMonster.getName() + "轰然倒下,勇猛无敌的" + attacker.getName() + "技高一筹,一个华丽的500连击,完成了致命一击,整个宇宙都为之震颤!", "");

										Article article = ArticleManager.getInstance().getArticle(SeptStationMapTemplet.getInstance().getPrizes()[3]);
										if (article != null) {
											Player player = GamePlayerManager.getInstance().getPlayer(attacker.getId());
											if (player != null) {
												ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(article, true, ArticleEntityManager.CREATE_REASON_JIAZUBOSS, player, article.getColorType(), 1, true);
												MailManager.getInstance().sendMail(attacker.getId(), new ArticleEntity[] { ae }, "家族邮件", "", 0, 0, 0, "家族boss击杀");
												backBuffer.append("<f>").append(attacker.getName()).append(" 在本次活动中表现耀眼，获得</f><f onclick='articleEntity' onclickType='2' color='").append(ArticleManager.color_article[ae.getColorType()]).append("' entityId='").append(ae.getId()).append("'>").append(SeptStationMapTemplet.getInstance().getPrizes()[3]).append("</f>●");
											} else {
												logger.error("[家族boss被击杀:" + thisBoss.getName() + "] [最后一击boss] [角色不存在:" + bossMonster.getLastAttacker() + "]");
											}
										} else {
											logger.error("[家族boss被击杀:" + thisBoss.getName() + "] [最后一击boss] [奖励物品不存在]");
										}
									} catch (Exception e) {
										logger.error("[家族boss被击杀:" + thisBoss.getName() + "] [最后一击boss] [奖励发放异常]", e);
									}

									JiazuSubSystem.logger.error("[家族:" + jiazu.getName() + "] [家族boss:" + bossMonster.getName() + "被击杀] [最后一击:" + jiazuMember.getPlayerID() + "]");
								} else {
									jiazuMember.setCurrentWeekContribution(jiazuMember.getCurrentWeekContribution() + JiazuManager.CONTRIBUTION_ADD_BY_ATTACK_JIAZUBOSS);
									if (JiazuSubSystem.logger.isWarnEnabled()) {
										JiazuSubSystem.logger.warn(jiazuMember.getPlayerID() + "[获得贡献度] [参与家族BOSS战] [增加贡献度:" + JiazuManager.CONTRIBUTION_ADD_BY_ATTACK_JIAZUBOSS + "] [增加后贡献度:" + jiazuMember.getCurrentWeekContribution() + "]");
									}
									attacker.sendError("恭喜你成功击杀" + bossMonster.getName() + "获得家族贡献:" + JiazuManager.CONTRIBUTION_ADD_BY_ATTACK_JIAZUBOSS);
									JiazuSubSystem.logger.error("[家族:" + jiazu.getName() + "] [家族boss:" + bossMonster.getName() + "被击杀] [参与成员:" + jiazuMember.getPlayerID() + "]");
								}
							} else {
								JiazuSubSystem.logger.error("[家族:" + jiazu.getName() + "] [家族boss:" + bossMonster.getName() + "被击杀] [攻击列表null]");
							}
						}
					}
				}
			}
			jiazu.initMember4Client();
			// 根据伤害给其他奖励
			List<JiazuBossDamageRecord> list = getBossDamageRecords();
			if (list.size() > 0) {
				try {
					StringBuffer frontBuffer = new StringBuffer("<f>本次击退异族BOSS伤害最高前几名:</f>");// 提示消息
					for (int i = 0; i < list.size(); i++) {
						JiazuBossDamageRecord dr = list.get(i);
						if (i == 0) { // 第一名
							frontBuffer.append("<f color='").append(ArticleManager.COLOR_ORANGE).append("'>第一名:").append(dr.getPlayerName()).append(" 伤害 ").append(dr.getDamage()).append(" 百分比 ").append(df.format(((double) dr.getDamage()) / thisBoss.getMaxHP())).append("●</f>");
							// 发送奖励
							Article article = ArticleManager.getInstance().getArticle(SeptStationMapTemplet.getInstance().getPrizes()[i]);
							if (article != null) {
								Player player = GamePlayerManager.getInstance().getPlayer(dr.getPlayerName());
								if (player != null) {
									ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(article, true, ArticleEntityManager.CREATE_REASON_JIAZUBOSS, player, article.getColorType(), 1, true);
									MailManager.getInstance().sendMail(dr.getPlayerId(), new ArticleEntity[] { ae }, "家族邮件", "", 0, 0, 0, "家族boss击杀");
									backBuffer.append("<f>").append(dr.getPlayerName()).append(" 在本次活动中表现耀眼，获得</f><f onclick='articleEntity' onclickType='2' color='").append(ArticleManager.color_article[ae.getColorType()]).append("' entityId='").append(ae.getId()).append("'>").append(SeptStationMapTemplet.getInstance().getPrizes()[i]).append("</f>●");
								} else {
									logger.error("[家族boss被击杀:" + thisBoss.getName() + "] [第" + (i + 1) + "名:" + dr.getPlayerName() + "] [角色不存在:" + dr.getPlayerName() + "]");
								}
							} else {
								logger.error("[家族boss被击杀:" + thisBoss.getName() + "] [第" + (i + 1) + "名:" + dr.getPlayerName() + "] [物品不存在:" + SeptStationMapTemplet.getInstance().getPrizes()[i] + "]");
							}
						} else if (i == 1) {// 第二名
							frontBuffer.append("<f color='").append(ArticleManager.COLOR_PURPLE).append("'>第二名:").append(dr.getPlayerName()).append(" 伤害 ").append(dr.getDamage()).append(" 百分比 ").append(df.format(((double) dr.getDamage()) / thisBoss.getMaxHP())).append("●</f>");
							// 发送奖励
							Article article = ArticleManager.getInstance().getArticle(SeptStationMapTemplet.getInstance().getPrizes()[i]);
							if (article != null) {
								Player player = GamePlayerManager.getInstance().getPlayer(dr.getPlayerName());
								if (player != null) {
									ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(article, true, ArticleEntityManager.CREATE_REASON_JIAZUBOSS, player, article.getColorType(), 1, true);
									MailManager.getInstance().sendMail(dr.getPlayerId(), new ArticleEntity[] { ae }, "家族邮件", "", 0, 0, 0, "家族boss击杀");
									backBuffer.append("<f>").append(dr.getPlayerName()).append(" 在本次活动中表现耀眼，获得</f><f onclick='articleEntity' onclickType='2' color='").append(ArticleManager.color_article[ae.getColorType()]).append("' entityId='").append(ae.getId()).append("'>").append(SeptStationMapTemplet.getInstance().getPrizes()[i]).append("</f>●");
								} else {
									logger.error("[家族boss被击杀:" + thisBoss.getName() + "] [第" + (i + 1) + "名:" + dr.getPlayerName() + "] [角色不存在:" + dr.getPlayerName() + "]");
								}
							} else {
								logger.error("[家族boss被击杀:" + thisBoss.getName() + "] [第" + (i + 1) + "名:" + dr.getPlayerName() + "] [物品不存在:" + SeptStationMapTemplet.getInstance().getPrizes()[i] + "]");
							}
						} else if (i == 2) {// 第三名
							frontBuffer.append("<f color='").append(ArticleManager.COLOR_BLUE).append("'>第三名:").append(dr.getPlayerName()).append(" 伤害 ").append(dr.getDamage()).append(" 百分比 ").append(df.format(((double) dr.getDamage()) / thisBoss.getMaxHP())).append("●</f>");
							// 发送奖励
							Article article = ArticleManager.getInstance().getArticle(SeptStationMapTemplet.getInstance().getPrizes()[i]);
							if (article != null) {
								Player player = GamePlayerManager.getInstance().getPlayer(dr.getPlayerName());
								if (player != null) {
									ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(article, true, ArticleEntityManager.CREATE_REASON_JIAZUBOSS, player, article.getColorType(), 1, true);
									MailManager.getInstance().sendMail(dr.getPlayerId(), new ArticleEntity[] { ae }, "家族邮件", "", 0, 0, 0, "家族boss击杀");
									backBuffer.append("<f>").append(dr.getPlayerName()).append(" 在本次活动中表现耀眼，获得</f><f onclick='articleEntity' onclickType='2' color='").append(ArticleManager.color_article[ae.getColorType()]).append("' entityId='").append(ae.getId()).append("'>").append(SeptStationMapTemplet.getInstance().getPrizes()[i]).append("</f>●");
								} else {
									logger.error("[家族boss被击杀:" + thisBoss.getName() + "] [第" + (i + 1) + "名:" + dr.getPlayerName() + "] [角色不存在:" + dr.getPlayerName() + "]");
								}
							} else {
								logger.error("[家族boss被击杀:" + thisBoss.getName() + "] [第" + (i + 1) + "名:" + dr.getPlayerName() + "] [物品不存在:" + SeptStationMapTemplet.getInstance().getPrizes()[i] + "]");
							}
						} else {// 其他人
							// 发送奖励
							// Article article = ArticleManager.getInstance().getArticle(SeptStationMapTemplet.getInstance().getPrizes()[3]);
							// if (article != null) {
							// Player player = GamePlayerManager.getInstance().getPlayer(dr.getPlayerName());
							// if (player != null) {
							// ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(article, true, ArticleEntityManager.CREATE_REASON_JIAZUBOSS, player,
							// article.getColorType(), true);
							// MailManager.getInstance().sendMail(dr.getPlayerId(), new ArticleEntity[] { ae }, "家族邮件", "", 0, 0, 0);
							// backBuffer.append("<f>").append(dr.getPlayerName()).append(" 在本次活动中表现耀眼，获得</f><f onclick='articleEntity' onclickType='2' color='").append(ArticleManager.color_article[ae.getColorType()]).append("' entityId='").append(ae.getId()).append("'>").append(SeptStationMapTemplet.getInstance().getPrizes()[i]).append("</f>●");
							// } else {
							// logger.error("[家族boss被击杀:" + thisBoss.getName() + "] [第" + (i + 1) + "名:" + dr.getPlayerName() + "] [角色不存在:" + dr.getPlayerName() + "]");
							// }
							// } else {
							// logger.error("[家族boss被击杀:" + thisBoss.getName() + "] [第" + (i + 1) + "名:" + dr.getPlayerName() + "] [物品不存在:" +
							// SeptStationMapTemplet.getInstance().getPrizes()[i] + "]");
							// }
						}
					}
					String notice = frontBuffer.toString() + backBuffer.toString();
					ChatMessageService.getInstance().sendMessageToJiazu(jiazu, notice, "");
					if (JiazuSubSystem.logger.isWarnEnabled()) {
						JiazuSubSystem.logger.warn("[成功击杀家族BOSS] [家族:" + getJiazu().getName() + "]" + notice);
					}
					{
						thisBoss = null;
						jiazuBossalive = false;

						// 通知所有人击杀完毕,取消按钮
						for (LivingObject lo : getGame().getLivingObjects()) {
							if (lo instanceof Player) {
								noticeJiazuBossButton((Player) lo, false);
							}
						}
					}
				} catch (Exception e) {
					JiazuSubSystem.logger.error("[家族:" + jiazu.getName() + "] [家族boss:" + bossMonster.getName() + "被击杀] [出现异常]", e);
				} finally {
					thisBoss = null;
					jiazuBossalive = false;
				}
			} else {
				JiazuSubSystem.logger.error("[家族:" + jiazu.getName() + "] [家族boss:" + bossMonster.getName() + "被击杀] [计算出的伤害列表空]");
			}
			thisBoss = null;
			jiazuBossalive = false;
		} else {
			JiazuSubSystem.logger.error("[家族:" + jiazu.getName() + "] [家族boss:" + bossMonster.getName() + "被击杀] [攻击列表null]");
		}
	}

	public List<JiazuBossDamageRecord> getBossDamageRecords() {
		List<JiazuBossDamageRecord> list = new ArrayList<JiazuBossDamageRecord>();
		HashMap<Long, JiazuBossDamageRecord> map = new HashMap<Long, JiazuBossDamageRecord>();
		if (jiazuBossalive && thisBoss != null) {
			AttackRecord[] arArr = thisBoss.attackRecordList.toArray(new AttackRecord[0]);
			for (int i = 0; i < arArr.length; i++) {
				AttackRecord ar = arArr[i];
				if (ar != null) {
					LivingObject lo = ar.living;
					if (lo != null) {
						if (lo instanceof Player) {
							Player player = (Player) lo;
							if (!map.containsKey(player.getId())) {
								map.put(player.getId(), new JiazuBossDamageRecord(player.getId(), player.getName(), 0L));
							}
							JiazuBossDamageRecord dr = map.get(player.getId());
							dr.setDamage(dr.getDamage() + ar.damage);
						} else if (lo instanceof Pet) {
							Pet pet = (Pet) lo;
							long ownerId = pet.getOwnerId();
							Player player = null;
							try {
								player = GamePlayerManager.getInstance().getPlayer(ownerId);
							} catch (Exception e) {
								logger.error("", e);
							}
							if (player != null) {
								if (!map.containsKey(player.getId())) {
									map.put(player.getId(), new JiazuBossDamageRecord(player.getId(), player.getName(), 0L));
								}
								JiazuBossDamageRecord dr = map.get(player.getId());
								dr.setDamage(dr.getDamage() + ar.damage);
							}
						}
					}
				} else {
					if (logger.isWarnEnabled()) {
						logger.warn("[查询家族boss伤害列表] [异常] [伤害主体不存在]");
					}
				}
			}
			for (Iterator<Long> itor = map.keySet().iterator(); itor.hasNext();) {
				long id = itor.next();
				JiazuBossDamageRecord dr = map.get(id);
				list.add(dr);
			}
		} else {
			if (logger.isWarnEnabled()) {
				logger.warn("[查询家族boss伤害列表] [失败] [boss状态不符] [jiazuBossalive:" + jiazuBossalive + "] [thisBoss:" + thisBoss + "]");
			}
		}
		Collections.sort(list);
		return list;
	}

	private void callJiazuboss(Calendar now) {
		int level = jiazu.getLevel();
		int bossId = SeptStationMapTemplet.getInstance().getJiazuBossIds()[level - 1];
		Point point = SeptStationMapTemplet.getInstance().getBossPoint();
		if (point == null) {
			logger.error("[驻地心跳] [处理家族BOSS] [异常] [boss位置不存在] [家族:" + jiazu.getName() + "]");
			return;
		}
		jiazu.setCurrentWeekCallbossTimes(jiazu.getCurrentWeekCallbossTimes() + 1);
		jiazu.setLastCallbossTime(now.getTimeInMillis());
		Monster monster = MemoryMonsterManager.getMonsterManager().createMonster(bossId);
		if (monster != null && monster instanceof BossMonster) {
			monster.setX(point.getX());
			monster.setY(point.getY());
			((BossMonster) monster).setJiazuId(jiazu.getJiazuID());
			monster.setGameNames(getGame().gi);
			getGame().addSprite(monster);
			((BossMonster) monster).setBornPoint(new com.fy.engineserver.core.g2d.Point(point.getX(), point.getY()));
			thisBossRemoveTime = Calendar.getInstance();
			thisBossRemoveTime.add(Calendar.MINUTE, SeptStationMapTemplet.getInstance().getContinuance());

			thisBoss = (BossMonster) monster;
			jiazuBossalive = true;

			ChatMessageService.getInstance().sendMessageToJiazu(jiazu, " 家族受到异族入侵，请各位家族成员赶往家族地图击退入侵异族!", "");

			{
				// 通知所有人出现boss
				for (LivingObject lo : getGame().getLivingObjects()) {
					if (lo instanceof Player) {
						noticeJiazuBossButton((Player) lo, false);
					}
				}
			}

			if (logger.isWarnEnabled()) {
				logger.warn("[驻地心跳] [处理家族BOSS] [成功] [家族:" + jiazu.getName() + "] [召唤家族BOSS] [成功] [BOSS:" + monster.getName() + "] [位置:" + point.toString() + "]");
			}
		} else {
			JiazuSubSystem.logger.error("[家族:" + jiazu.getName() + "] [召唤家族BOSS] [失败] [BOSS不存在:" + bossId + "]");
		}
	}

	/**
	 * 维护
	 */
	public synchronized void maintai(Calendar now) {
		if (now.getTimeInMillis() - lastMaintaiTime >= SeptStationManager.MaintaiCycle) {
			// 修理 规则有待策划提供 // 如果是一小时的话 维护后基本就要修了。。。
			// 如果钱不够处理方式 等策划提供
			if (JiazuSubSystem.logger.isWarnEnabled()) {
				JiazuSubSystem.logger.warn("[家族:" + jiazu.getName() + "] [维护] [开始执行维护]");
			}
			this.initInfo();
			long baseCoin = this.getInfo().getBaseMaintai();// 基础维护费（聚仙殿+聚宝庄）
			long currMaintaiCost = this.getInfo().getCurrMaintai();// 各个建筑的维护费之和
			long jiazuMoney = jiazu.getJiazuMoney();

			if (JiazuSubSystem.logger.isWarnEnabled()) {
				JiazuSubSystem.logger.warn("[家族:" + jiazu.getName() + "] [维护] [基础维护费:" + baseCoin + "] [各个建筑维护费之和:" + currMaintaiCost + "] [家族资金" + jiazuMoney + "]");
			}
			/**
			 * 当家族的资金小于基本维修费用，家族主建筑聚仙殿降级
			 * 当主建筑1级，维修后家族资金<=10，驻地回收
			 */

			boolean needForceToLvDown = jiazuMoney < currMaintaiCost;

			if (needForceToLvDown) {// 降级			2014年8月14日  修改  资金不足调整为限制家族功能
				JiazuManager2.instance.noticeJizuMaint(jiazu, Translate.家族降级通知, Translate.家族降级标题);
				JiazuManager2.logger.warn("[新家族] [家族维护] [维护资金不足] [家族降级] [" + jiazu.getLogString() + "]");
				boolean succ = false;
				try {
					SeptBuildingEntity JXDEntity = this.getSeptBuild(BuildingType.聚仙殿);
					if (jiazu.getJiazuMaster() > 0) {
						Player p = GamePlayerManager.getInstance().getPlayer(jiazu.getJiazuMaster());
						try {
							((JuXianDian)JXDEntity.getBuildingState().getTempletType()).leverDownByServer = true;
							succ = JXDEntity.getBuildingState().getTempletType().levelDown(this, 0, true, p);
						} catch (BuildingNotFoundException e1) {
								// TODO Auto-generated catch block
							JiazuSubSystem.logger.error("[家族维护] [资金不足降级] [异常] [" + jiazu.getLogString() + "]", e1);
						} catch (WrongLvOnLvDownException e3) {
								
						} catch (ActionIsCDException e4) {
								
						}
					}
				} catch (Exception e) {
					JiazuSubSystem.logger.error("[家族维护] [资金不足降级] [异常2] [" + jiazu.getLogString() + "]", e);
				}
				if (succ) {
					JiazuManager2.instance.noticeJizuLevelDown(jiazu);
				}
			} else {// 正常维修
				if (!jiazu.consumeJiazuMoney(currMaintaiCost)) {
					if (logger.isInfoEnabled()) logger.info("[家族驻地] [扣除正常修费] [异常] [家族资金不足] 家族 : [{}]", new Object[] { jiazu.getName() });
				}
				jiazu.setJiazuStatus(JiazuManager2.家族功能正常);
				if(currMaintaiCost > 0){
					JiazuManager2.instance.noticeJizuMaint(jiazu, Translate.家族维护标题, String.format(Translate.家族维护通知, BillingCenter.得到带单位的银两(currMaintaiCost)));
				}
				if (JiazuSubSystem.logger.isWarnEnabled()) {
					JiazuSubSystem.logger.warn("[家族:" + jiazu.getName() + "] [维护] [扣除维护费:"+currMaintaiCost+"] [剩余家族资金:" + jiazu.getJiazuMoney() + "]");
				}
			}
			setLastMaintaiTime(now.getTimeInMillis());
			if (JiazuSubSystem.logger.isWarnEnabled()) {
				JiazuSubSystem.logger.warn("[家族:" + jiazu.getName() + "] [维护] [完成维护] [耗时:" + (SystemTime.currentTimeMillis() - now.getTimeInMillis()) + "ms]");
			}
		}
	}

	/**
	 * 是否要执行维修
	 * 2011-5-3
	 * 
	 * @param calendar
	 * @return boolean
	 * 
	 */
	private boolean needMaintai(Calendar calendar) {
		if (calendar.get(Calendar.MINUTE) == 10) {
			return true;
		}
		return false;
	}

	/**
	 * 确实给驻地的某个位置增加一个建筑
	 * 2011-4-29
	 * 
	 * @param index
	 * @param entity
	 *            void
	 * 
	 */
	// public void addBuildEntity(SeptBuildingEntity buildingEntity) {
	// MemoryNPCManager mnm = (MemoryNPCManager) MemoryNPCManager.getNPCManager();
	//
	// /**
	// * 真实代码
	// */
	// NPC defaultNpc = mnm.getNPC(buildingEntity.getBuildingState().getTempletType().getDefaultNpcId());
	// System.out.println("创建默认驻地defaultNpc:" + defaultNpc);
	// NPC npc = mnm.createNPC(defaultNpc.getNPCCategoryId());
	// System.out.println("创建默认驻地npc:" + npc);
	// NpcStation npcStation = SeptStationMapTemplet.getInstance().getMap().get(buildingEntity.getIndex());
	// System.out.println("创建默认驻地 npcStation :" + (npcStation == null ? " null " : npcStation.toString()));
	// npc.setX(npcStation.getX());
	// npc.setY(npcStation.getY());
	// buildingEntity.setIndex(npcStation.getIndex());
	//
	// this.addNewBuildings(buildingEntity);
	// this.getGame().addSprite(npc);
	//
	// /** 测试代码 */
	// // NpcStation npcStation = SeptStationMapTemplet.getInstance().getMap().get(buildingEntity.getIndex());
	// // buildingEntity.setIndex(npcStation.getIndex());
	// }

	/**
	 * 获得正在建造的建筑
	 * 2011-5-3
	 * 
	 * @return SeptBuildingEntity
	 * 
	 */
	public SeptBuildingEntity getInBuildBuilding() {
		for (SeptBuildingEntity entity : buildings.values()) {
			if (entity.isInBuild()) {
				return entity;
			}
		}
		return null;
	}

	/**
	 * 驻地初始化,加载地图数据,NPC数据
	 * 2011-4-28 void
	 * 
	 * 
	 */
	public boolean init() {
		jiazu = JiazuManager.getInstance().getJiazu(septId);
		if (jiazu == null) {
			logger.error("[加载家住驻地] [失败] [家族不存在] 驻地名称 : [{}] [家族id:{}]", new Object[] { getName(), septId });
			return false;
		}

		MemoryNPCManager mnm = (MemoryNPCManager) MemoryNPCManager.getNPCManager();
		Game gameTemplet = null;
		if(getMapName() != null && getMapName().indexOf("_") > -1){
			gameTemplet = GameManager.getInstance().getGameByName(getMapName().substring(0, getMapName().indexOf("_")), CountryManager.国家A);
			if (gameTemplet == null) {
				gameTemplet = GameManager.getInstance().getGameByName(getMapName().substring(0, getMapName().indexOf("_")), CountryManager.中立);
			}
		}else{
			gameTemplet = GameManager.getInstance().getGameByName(getMapName(), CountryManager.国家A);
			if (gameTemplet == null) {
				gameTemplet = GameManager.getInstance().getGameByName(getMapName(), CountryManager.中立);
			}
		}
		SeptStationMapTemplet mapTemplet = SeptStationMapTemplet.getInstance();

		if (gameTemplet == null) {
			return false;
		}
		if (logger.isInfoEnabled()) logger.info("[加载家住驻地] [gameTemplet:{}] [gameinfo:{}] [驻地地图不存在] 家族名称: [{}] 地图名称 :{}", 
				new Object[] {gameTemplet, (gameTemplet!=null?gameTemplet.getGameInfo():"null") ,jiazu.getName(), getMapName() });
		Game newGame = new Game(GameManager.getInstance(), gameTemplet.getGameInfo());

		try {
			newGame.init();
		} catch (Exception e) {
			SeptStationManager.logger.error("加载家族地图异常::"+this.getMapName(), e);
			return false;
		}
		try {
			setGame(newGame);// 必须在加载NPC前设置地图
			for (int i = 0; i < buildingEntities.length; i++) {
				SeptBuildingEntity entity = buildingEntities[i];
				SeptStationNPC ssNpc = null;
				NpcStation npcStation = null;
				if (entity != null) {
					entity.setStation(this);
					ssNpc = (SeptStationNPC) mnm.createNPC(entity.getNpcTempletId());
					if (ssNpc == null || entity.getBuildingState() == null) {
						if (logger.isInfoEnabled()) logger.info("[加载驻地时 出现NULL:{}] [Name:{}] [NPC:{}] [STAT:{}] [NPCID:{}]", new Object[] { getId(), getName(), ssNpc, entity.getBuildingState(), entity.getNpcTempletId() });
						continue;
					}
					npcStation = mapTemplet.getNpcStation(i);
					ssNpc.setSeptId(this.getId());
					ssNpc.setGrade(entity.getBuildingState() == null ? 0 : entity.getBuildingState().getLevel());
					ssNpc.setGameNames(getGame().gi);
					if (npcStation == null) {
						logger.error("[加载家住驻地] [失败] [建筑索引异常] [家族名称:{}] [地图名称 :{}] [索引:{}] [ssNpc:{}", new Object[] { jiazu.getName(), getMapName(), i, ssNpc });
						continue;
					}
					if (entity.getTempletIndex() != 0) {

						SeptBuildingTemplet sbt = SeptBuildingManager.getTemplet(entity.getTempletIndex());
						if (sbt != null) {
							entity.getBuildingState().setTempletType(sbt);
						} else {
							logger.error(ssNpc.getName() + "模板不存在");
						}
					} else {
						logger.error(ssNpc.getName() + "模板ID=0,驻地ID:" + this.id);
					}
					ResourceManager.getInstance().setAvata(ssNpc);
					ssNpc.setX(npcStation.getX());
					ssNpc.setY(npcStation.getY());
					ssNpc.setSeptId(getId());

					entity.setNpc(ssNpc);
					if (entity.getBuildingState() != null && entity.getBuildingState().getTempletType() != null) {
						entity.getBuildingState().getTempletType().initDepend();
					}

					addNewBuildings(entity);
					newGame.addSprite(ssNpc);
					logger.warn("[家族驻地init...] [家族Id:{}] [名字:{}] [等级:{}] [国家:{}] [驻地Id:{}] [名字:{}] [建筑名字:{}] [等级:{}] [avata:{}]", 
							new Object[] { jiazu.getJiazuID(),jiazu.getName(),jiazu.getLevel(),jiazu.getCountry(), getId(),getName() ,entity.getNpc().getName(),ssNpc.getGrade(),(ssNpc.getAvata()!=null?Arrays.toString(ssNpc.getAvata()):"nul")});
				}
			}
			if (jiazu == null) {
				if (logger.isInfoEnabled()) logger.info("[ERROR] [家族驻地的时候家族不存在] [septStationId:{}] [家族ID：{}", new Object[] { getId(), getSeptId() });
				return false;
			}
			game = newGame;
			game.country = jiazu.getCountry();
			this.initInfo();

			if (this.fireActivityNpcEntity == null) {
				setFireActivityNpcEntity(FireManager.getInstance().createFireActivityNpc(this));
				if (FireManager.logger.isDebugEnabled()) FireManager.logger.debug("[初始化家族篝火实体] [篝火实体null] [" + this.fireActivityNpcEntity.getTemplate() + "]");
			} else {
				fireActivityNpcEntity.init(this);
				if (FireManager.logger.isWarnEnabled()) {
					FireManager.logger.warn("[初始化家族篝火实体成功] [" + this.fireActivityNpcEntity.getTemplate().getFireName() + "] [" + this.getName() + "] [驻地Id:" + this.getId() + "]");
				}
			}
			setUsed(true);
			if (logger.isWarnEnabled()) {
				logger.warn("[家族驻地init完成] [家族Id:{}] [名字:{}] [等级:{}] [国家:{}] [驻地ID:{}] [名字:{}]", new Object[] { jiazu.getJiazuID(),jiazu.getName(),jiazu.getLevel(),jiazu.getCountry(), getId() ,getName()});
			}
			return true;
		} catch (Exception e) {
			logger.error("[家族驻地init异常] [家族Id:{}]", new Object[] { jiazu.getJiazuID() }, e);
		}
		return false;
	}

	public void noticeJiazuBossButton(Player player, boolean isEnter) {
		boolean show = thisBoss != null && jiazuBossalive;
		if (!show && isEnter) {
			return;
		} else {
			NOTICE_CLIENT_JIAZUBOSS_REQ req = new NOTICE_CLIENT_JIAZUBOSS_REQ(GameMessageFactory.nextSequnceNum(), show);
			player.addMessageToRightBag(req);
			if (logger.isWarnEnabled()) {
				logger.warn("[通知角色] [显示/取消按钮:" + show + "] [是否是进入地图:" + isEnter + "]");
			}
		}
	}

	public int getAllbuildingTotalLevel() {
		int totalLevel = 0;
		for (Iterator<Long> itor = buildings.keySet().iterator(); itor.hasNext();) {
			long id = itor.next();
			totalLevel += buildings.get(id).getBuildingState().getLevel();
		}
		return totalLevel;
	}

	public void notifyFieldChange(String fieldName) {
		JiazuManager.septstationEm.notifyFieldChange(this, fieldName);
	}

	public boolean isJiazuBossalive() {
		return jiazuBossalive;
	}

	public void setJiazuBossalive(boolean jiazuBossalive) {
		this.jiazuBossalive = jiazuBossalive;
	}

	public BossMonster getThisBoss() {
		return thisBoss;
	}

	public void setThisBoss(BossMonster thisBoss) {
		this.thisBoss = thisBoss;
	}
}
