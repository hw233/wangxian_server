package com.fy.engineserver.septbuilding.templet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.fy.engineserver.core.JiazuSubSystem;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.jiazu2.manager.JiazuManager2;
import com.fy.engineserver.sept.exception.ActionIsCDException;
import com.fy.engineserver.sept.exception.BuildingExistException;
import com.fy.engineserver.sept.exception.BuildingNotFoundException;
import com.fy.engineserver.sept.exception.CannotDestoryException;
import com.fy.engineserver.sept.exception.DependBuildingNotLvUpException;
import com.fy.engineserver.sept.exception.IndexHasBuildingException;
import com.fy.engineserver.sept.exception.JiaZuFenyingException;
import com.fy.engineserver.sept.exception.MainBuildNotLvUpException;
import com.fy.engineserver.sept.exception.MaxLvException;
import com.fy.engineserver.sept.exception.OtherBuildingNotLvUpException;
import com.fy.engineserver.sept.exception.OtherInBuildException;
import com.fy.engineserver.sept.exception.ResNotEnoughException;
import com.fy.engineserver.sept.exception.TooManyBiaozhixiangException;
import com.fy.engineserver.sept.exception.WrongLvOnLvDownException;
import com.fy.engineserver.septbuilding.entity.SeptBuildingEntity;
import com.fy.engineserver.septbuilding.service.BuildAble;
import com.fy.engineserver.septbuilding.service.BuildingState;
import com.fy.engineserver.septbuilding.service.SeptBuildingManager;
import com.fy.engineserver.septstation.NpcStation;
import com.fy.engineserver.septstation.SeptStation;
import com.fy.engineserver.septstation.SeptStationMapTemplet;
import com.fy.engineserver.septstation.service.SeptStationManager;
import com.fy.engineserver.septstation.service.SeptStationManager.ActionType;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.sprite.npc.SeptStationNPC;
import com.fy.engineserver.util.StringTool;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;
import com.xuanzhi.tools.text.XmlUtil;

/**
 * 建筑模板超类
 * 
 * 
 */
/**
 * 
 * 
 */
@SimpleEmbeddable
public class SeptBuildingTemplet implements BuildAble {

	private static final long serialVersionUID = 1L;

	public static Logger logger = LoggerFactory.getLogger(JiazuSubSystem.class);

	public static final int MAX_LEVEL = 30;

	private String root;

	private static String confRoot;

	/** 名字 */
	private String name;
	/** 依赖的建筑状态 */
	private transient BuildingState depend;
	/** 升级增加繁荣度 */
	private transient int[] lvUpAddProsper = new int[MAX_LEVEL];
	/** 日常维修费 */
	private transient long[] dailyMaintainCost = new long[MAX_LEVEL];
	/** 升级建筑消耗的钱 */
	private transient long[] lvUpCostCoin = new long[MAX_LEVEL];
	/** 升级建筑消耗的灵脉值 */
	private transient long[] lvUpCostSpirit = new long[MAX_LEVEL];
	/** 发布任务数 */
	private transient int[] releaseTaskNum = new int[MAX_LEVEL];
	/** 外观(NPC形象) */
	private transient String[] outShows = new String[MAX_LEVEL];

	protected String[] avataRaces = new String[MAX_LEVEL];
	protected String[] avataSexes = new String[MAX_LEVEL];

	/** 类型 */
	protected transient BuildingType buildingType;
	/** 类型ID */
	protected int typeIndex;
	/** 默认NPCId */
	private transient int defaultNpcId;

	/**
	 * 加载基础模板
	 * @throws Exception
	 */
	void load() throws Exception {
		try {
			String path = getConfPath();
			java.io.File file = new java.io.File(path);
			Document doc = null;
			doc = XmlUtil.load(file.toString());
			Element root = doc.getDocumentElement();
			loadBaseAttribute(root);
			loadeSpecially(root);
			if (logger.isInfoEnabled()) {
				logger.info("[系统初始化] [初始化家族建筑模板 >>>{}<<< 完毕 ] [{}]", new Object[] { getBuildingType().getName(), toString() });
			}
		} catch (Exception e) {
			logger.error("[家族建筑模板加载][ERROR]", e);
			throw e;
		}
	}

	public String getConfPath() {
		return "";
	}

	public void loadeSpecially(Element root) throws Exception {

	}

	/** 加载依赖关系 */
	public void initDepend() {

	};

	protected void loadBaseAttribute(Element root) {
		Element[] es;
		// 基础属性
		// es = XmlUtil.getChildrenByName(root, "name");
		// setName(XmlUtil.getAttributeAsString(es[0], "value", null));

		es = XmlUtil.getChildrenByName(root, "lvUpAddProsper");
		setLvUpAddProsper(StringTool.string2IntArr(XmlUtil.getAttributeAsString(es[0], "value", null), ","));

		es = XmlUtil.getChildrenByName(root, "dailyMaintainCost");
		setDailyMaintainCost(StringTool.string2LongArr(XmlUtil.getAttributeAsString(es[0], "value", null), ","));

		es = XmlUtil.getChildrenByName(root, "lvUpCostCoin");
		setLvUpCostCoin(StringTool.string2LongArr(XmlUtil.getAttributeAsString(es[0], "value", null), ","));

		es = XmlUtil.getChildrenByName(root, "lvUpCostSpirit");
		setLvUpCostSpirit(StringTool.string2LongArr(XmlUtil.getAttributeAsString(es[0], "value", null), ","));

		es = XmlUtil.getChildrenByName(root, "releaseTaskNum");
		setReleaseTaskNum(StringTool.string2IntArr(XmlUtil.getAttributeAsString(es[0], "value", null), ","));

		es = XmlUtil.getChildrenByName(root, "outShows");
		setOutShows((StringTool.string2StringArr(XmlUtil.getAttributeAsString(es[0], "value", null), ",")));

		es = XmlUtil.getChildrenByName(root, "defaultNpcId");
		setDefaultNpcId(XmlUtil.getAttributeAsInteger(es[0], "value"));
	}

	/**
	 * 基础建筑 且非 聚仙殿的默认实现，其他建筑要在各自实体内实现此方法 否则抛出IllegalStateException()[改为 只有聚仙特殊处理，其他建筑 处理方式一样]
	 * 
	 * @throws MainBuildNotLvUpException
	 * @throws ResNotEnoughException
	 * @throws OtherBuildingNotLvUpException
	 * @throws BuildingNotFoundException
	 * @throws MaxLvException
	 */
	@Override
	public boolean levelUp(SeptStation station, long NPCId) throws MainBuildNotLvUpException, ResNotEnoughException, OtherBuildingNotLvUpException, BuildingNotFoundException, MaxLvException {
		int mainLevel = station.getMainBuildingLevel();
		int thisLevel = station.getBuildingLevel(this.getBuildingType());
		if (thisLevel >= MAX_LEVEL) {
			throw new MaxLvException();
		}
		if (thisLevel >= mainLevel) {
			throw new MainBuildNotLvUpException();
		}
		if (!resEnough(station)) {
			throw new ResNotEnoughException();
		}
		SeptBuildingEntity thisEntity = station.getSeptBuild(this.getBuildingType());

		// 设置任务数量
		thisEntity.setInBuild(true);

		thisEntity.releaseTask(ActionType.VLUP, station.getGame());
		return true;
	}

	@Override
	public boolean levelDown(SeptStation station, long NPCId, boolean confirm, Player p) throws BuildingNotFoundException, WrongLvOnLvDownException, ActionIsCDException {
		long timeDistance = com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - station.getLastDestoryTime();
		if (timeDistance < SeptStationManager.lvDownCD) {
			throw new ActionIsCDException(SeptStationManager.timeDistanceLong2String(SeptStationManager.lvDownCD - timeDistance));
		}
		if (!confirm) {
			JiazuManager2.popConfirmLevelDown(p, station, NPCId, this, this.getName());
			return false;
		}
		BuildingType buildingType = this.getBuildingType();

		SeptBuildingEntity thisEntity = station.getSeptBuild(buildingType);
		if (thisEntity.getBuildingState().getLevel() <= 1) {
			throw new WrongLvOnLvDownException();
		} else {
			thisEntity.getBuildingState().setLevel(thisEntity.getBuildingState().getLevel() - 1);
			thisEntity.modifyNPCOutShow();
			station.setLastDestoryTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
			station.initInfo();
			station.notifyFieldChange("buildingEntities");
			if (logger.isWarnEnabled()) {
				logger.warn("[家族:{}] [降级建筑:{}] [降级后建筑等级:{}]", new Object[] { station.getJiazu().getName(), station.getName(), thisEntity.getBuildingState().getLevel() });
			}
			return true;
		}
	}

	@Override
	public boolean destroy(SeptStation station, long NPCId) throws CannotDestoryException, BuildingNotFoundException {
		try {
			// TODO 除基础建筑外都可拆除
			if (getBuildingType().isBase()) {
				throw new CannotDestoryException();
			}
			SeptBuildingEntity thisEntity = station.getSeptBuildingByNPCId(NPCId);
			if (thisEntity == null) {
				throw new BuildingNotFoundException();
			}
			station.getBuildings().remove(NPCId);
			station.getGame().removeSprite(thisEntity.getNpc());

			BuildingType buildingType = this.getBuildingType();
			// 拆除建筑要把当前建筑移除 并且 要再加入一个默认的NPC
			int index = thisEntity.getIndex();

			NpcStation npcStation = SeptStationMapTemplet.getInstance().getNpcStation(index);
			if (npcStation != null) {
				SeptStationNPC ssNpc = (SeptStationNPC) MemoryNPCManager.getNPCManager().createNPC(npcStation.getNpcTempletId());
				SeptBuildingEntity entity = new SeptBuildingEntity(npcStation.getNpcTempletId(), station.getId(), npcStation.getIndex(), npcStation.getBuilingType());
				ssNpc.setX(npcStation.getX());
				ssNpc.setY(npcStation.getY());
				ssNpc.setSeptId(station.getId());

				entity.setNpc(ssNpc);
				entity.setBuildingState(new BuildingState(SeptBuildingManager.getTemplet(buildingType.getIndex()), buildingType.isDefault() ? 1 : 0));
				entity.setStation(station);
				entity.getBuildingState().getTempletType().initDepend();
				station.addNewBuildings(entity);
				station.getGame().addSprite(ssNpc);
			}
		} catch (Exception e) {
			logger.error("[拆除建筑] [异常]", e);

		}
		return false;
	}

	@Override
	public boolean build(SeptStation station, long npcId, int buildType) throws DependBuildingNotLvUpException, TooManyBiaozhixiangException, BuildingExistException, ResNotEnoughException, IndexHasBuildingException, OtherInBuildException, JiaZuFenyingException {
		SeptBuildingEntity inBuildingEntity = station.getInBuildBuilding();
		if (inBuildingEntity != null) {
			throw new OtherInBuildException(inBuildingEntity.getNpc().getName());
		}
		// 非基础建筑才会建造
		BuildingState dependBuildingState = getDepend();
		if (dependBuildingState == null) {
			throw new BuildingExistException();
		}

		int level = station.getBuildingLevel(dependBuildingState.getTempletType().getBuildingType());
		if (level < dependBuildingState.getLevel()) {
			// 依赖建筑没达到等级
			throw new DependBuildingNotLvUpException(dependBuildingState.toString());
		}
		SeptBuildingEntity be = station.getBuildings().get(npcId);

		if (((SeptStationNPC) be.getNpc()).getGrade() > 0) {
			throw new IndexHasBuildingException();
		}

		boolean isBiaozhixiang = getBuildingType().isBiaozhixiang();// 标志像要特殊处理
		boolean canBuildBiaozhixiang = true;
		if (isBiaozhixiang) {
			int currentBiaozhixiangNum = station.getInfo().getCurrentBiaozhixiangNum();// 当前的标志像个数
			canBuildBiaozhixiang = currentBiaozhixiangNum < 1;
			if (logger.isInfoEnabled()) {
				logger.info("[家族:{}] [建造标志像] [已有数量:{}] [是否可以建造:{}]", new Object[] { station.getJiazu().getName(), currentBiaozhixiangNum, canBuildBiaozhixiang });
			}
		}
		if (isBiaozhixiang && !canBuildBiaozhixiang) {// 是箭塔且不能建造（到达上限）
			throw new TooManyBiaozhixiangException();
		}
		if (station.getJiazu().getJiazuStatus() == JiazuManager2.封印家族功能) {
			throw new JiaZuFenyingException();
		}

		if (!this.resEnough(station, true)) {// 资源足够建造
			throw new ResNotEnoughException();
		}

		// 建筑类型
		BuildingType type = SeptBuildingManager.getBuildingTypeByIndex(buildType);
		//移除原有的NPC
		station.getBuildings().remove(be.getNpc().getId());
		SeptBuildingEntity newEntity = SeptStationManager.createEntityForStation(type, station, be.getIndex());
		newEntity.getBuildingState().setLevel(0);
		((SeptStationNPC) newEntity.getNpc()).setGrade(0);
		newEntity.setInBuild(true);
		newEntity.releaseTask(ActionType.BUILD, station.getGame());
		
		station.getGame().removeSprite(be.getNpc());

		station.initInfo();
		return true;
	}

	/**
	 * 升级时对应的操作<BR/>
	 * 真实升级的时候调用<BR/>
	 */
	public void onLevelUp(SeptStation station) {

	}

	/**
	 * 降级时对应操作
	 */
	public void onLevelDown(SeptStation station) {

	}

	/**
	 * 家族资源是否足够升级本类型建筑
	 * 2011-4-25
	 * 
	 * @param station
	 * @return boolean
	 * 
	 */
	public boolean resEnough(SeptStation station) {
		return resEnough(station, false);
	}

	public boolean resEnough(SeptStation station, boolean isBuild) {
		int currLevel = 0;
		if (!isBuild) {
			SeptBuildingEntity buildingEntity = station.getSeptBuild(this.getBuildingType());
			currLevel = buildingEntity.getBuildingState().getLevel();
		}
		long coinCost = this.getLvUpCostCoin()[currLevel];// 消耗的钱
		long spiritCost = this.getLvUpCostSpirit()[currLevel];// 消耗的灵脉值

		if (coinCost > station.getJiazu().getJiazuMoney()) {
			JiazuSubSystem.logger.warn("[{}] [家族升级/建造建筑] [资金不足] [需要:{}] [已有:{}] [建筑:{}]", new Object[] { station.getJiazu().getName(), coinCost, station.getJiazu().getJiazuMoney(), getBuildingType().getName() });
			return false;
		}
		if (spiritCost > station.getJiazu().getConstructionConsume()) {
			JiazuSubSystem.logger.warn("[{}] [家族升级/建造建筑] [灵脉值不足] [需要:{}] [已有:{}] [建筑:{}]", new Object[] { station.getJiazu().getName(), spiritCost, station.getJiazu().getConstructionConsume(), getBuildingType().getName() });
			return false;
		}
		if (station.getJiazu().getJiazuStatus() == JiazuManager2.封印家族功能) {
			JiazuSubSystem.logger.warn("[{}] [家族升级/建造建筑] [家族封印] [需要:{}] [已有:{}] [建筑:{}]", new Object[] { station.getJiazu().getName(), spiritCost, station.getJiazu().getConstructionConsume(), getBuildingType().getName() });
//			player.sendError(Translate.家族资金不足封印);
			return false;
		}
		synchronized (station.getJiazu()) {
			station.getJiazu().setJiazuMoney(station.getJiazu().getJiazuMoney() - coinCost);
			station.getJiazu().setConstructionConsume(station.getJiazu().getConstructionConsume() - spiritCost);
			try {
				if (JiazuManager2.instance.noticeNum.get(station.getJiazu().getLevel()) != null) {
					int num = JiazuManager2.instance.noticeNum.get(station.getJiazu().getLevel());
					if (station.getJiazu().getJiazuMoney() < num) {
						JiazuManager2.instance.noticeJizuMondeyNotEnough(station.getJiazu());
					}
				}
			} catch (Exception e) {
				JiazuManager2.logger.error("[新家族] [发送家族资金不足提醒] [异常] [" + station.getJiazu().getLogString() + "]", e);
			}
		}
		if (JiazuSubSystem.logger.isWarnEnabled()) {
			JiazuSubSystem.logger.warn("[{}] [家族升级/建造建筑] [OK] [扣除资金:{}] [扣除灵脉值:{}] [剩余资金:{}] [剩余灵脉值:{}]", new Object[] {station.getJiazu().getLogString() , coinCost, spiritCost, station.getJiazu().getJiazuMoney(), station.getJiazu().getConstructionConsume()});
		}
		return true;
	}

	/**************************************** Getters Setters ****************************************/

	public int[] getReleaseTaskNum() {
		return releaseTaskNum;
	}

	public int getDefaultNpcId() {
		return defaultNpcId;
	}

	public void setDefaultNpcId(int defaultNpcId) {
		this.defaultNpcId = defaultNpcId;
	}

	public void setReleaseTaskNum(int[] releaseTaskNum) {
		this.releaseTaskNum = releaseTaskNum;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BuildingState getDepend() {
		return depend;
	}

	public void setDepend(BuildingState depend) {
		this.depend = depend;
	}

	public int[] getLvUpAddProsper() {
		return lvUpAddProsper;
	}

	public void setLvUpAddProsper(int[] lvUpAddProsper) {
		this.lvUpAddProsper = lvUpAddProsper;
	}

	public long[] getDailyMaintainCost() {
		return dailyMaintainCost;
	}

	public void setDailyMaintainCost(long[] dailyMaintainCost) {
		this.dailyMaintainCost = dailyMaintainCost;
	}

	public long[] getLvUpCostCoin() {
		return lvUpCostCoin;
	}

	public void setLvUpCostCoin(long[] lvUpCostCoin) {
		this.lvUpCostCoin = lvUpCostCoin;
	}

	public long[] getLvUpCostSpirit() {
		return lvUpCostSpirit;
	}

	public void setLvUpCostSpirit(long[] lvUpCostSpirit) {
		this.lvUpCostSpirit = lvUpCostSpirit;
	}

	public int getTypeIndex() {
		return typeIndex;
	}

	public void setTypeIndex(int typeIndex) {
		this.typeIndex = typeIndex;
	}

	public BuildingType getBuildingType() {
		if (buildingType == null) {
			buildingType = SeptBuildingManager.getBuildingTypeByIndex(getTypeIndex());
		}
		return buildingType;
	}

	public void setBuildingType(BuildingType buildingType) {
		this.buildingType = buildingType;
		setTypeIndex(buildingType.getIndex());
	}

	public int getMAX_LEVEL() {
		return MAX_LEVEL;
	}

	public String[] getOutShows() {
		return outShows;
	}

	public void setOutShows(String[] outShows) {
		this.outShows = outShows;
	}

	public static final int TYPE_BASE = 0;// 基础建筑
	public static final int TYPE_EXTEND = 1;// 扩展建筑

	public enum BuildingType {
		聚仙殿(1, "JuXianDian.xml", TYPE_BASE, Translate.聚仙殿, true),
		聚宝庄(2, "JuBaoZhuang.xml", TYPE_BASE, Translate.聚宝庄, true),
		仙灵洞(3, "XianLingDong.xml", TYPE_BASE, Translate.仙灵洞, true),
		龙图阁(5, "LongTuGe.xml", TYPE_BASE, Translate.龙图阁, true),
		仙兽房(6, "HuanYuZhen.xml", TYPE_EXTEND, Translate.仙兽房, true),
		武器坊(7, "WuQiFang.xml", TYPE_EXTEND, Translate.武器坊),
		防具坊(8, "FangJuFang.xml", TYPE_EXTEND, Translate.防具坊),
		家族大旗(10, "JiaZuQi.xml", TYPE_EXTEND, Translate.家族大旗, true),
		标志像青龙(11, "BiaoZhiXiangQL.xml", TYPE_EXTEND, Translate.标志像青龙),
		标志像白虎(12, "BiaoZhiXiangBH.xml", TYPE_EXTEND, Translate.标志像白虎),
		标志像朱雀(13, "BiaoZhiXiangZQ.xml", TYPE_EXTEND, Translate.标志像朱雀),
		标志像玄武(14, "BiaoZhiXiangXW.xml", TYPE_EXTEND, Translate.标志像玄武), ;

		BuildingType(int index, String resName, int type, String name) {
			this(index, resName, type, name, false);
		}

		BuildingType(int index, String resName, int type, String name, boolean isDefault) {
			this.index = index;
			this.path = confRoot + resName;
			this.resName = resName;
			this.name = name;
			this.isDefault = isDefault;
			this.type = type;
		}

		private int index;

		private String name;// 名字
		private String path;// 资源路径
		private String resName;// 资源名字
		private int type;// 基础建筑/非基础建筑
		private boolean isDefault;// 是不是创建家族驻地时的默认建筑

		@Override
		public String toString() {
			return "[BuildingType] name = " + name + ", path = " + path + " , isBase :" + isBase() + " , isDefault " + isDefault();
		}

		public boolean isDefault() {
			return isDefault;
		}

		public void setDefault(boolean isDefault) {
			this.isDefault = isDefault;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		public String getResName() {
			return resName;
		}

		public void setResName(String resName) {
			this.resName = resName;
		}

		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public String getPath() {
			return this.path;
		}

		/** 是否是基础建筑 */
		public boolean isBase() {
			return type == TYPE_BASE;
		}

		// /** 是否是箭塔 */
		// public boolean isJianTa() {
		// return name.indexOf("箭楼") > -1;
		// }

		/** 是否是标志像 */
		public boolean isBiaozhixiang() {
			return name.indexOf(Translate.标志像) > -1;
		}
	}

	public String getRoot() {
		return root;
	}

	public void setRoot(String root) {
		this.root = root;
	}

	@Override
	public String toString() {
		StringBuffer sbf = new StringBuffer();
		sbf.append("<font color=red>[模板:]").append(getClass().getName()).append(",").append(getName()).append("</font><BR/>");
		sbf.append("[依赖建筑]").append(depend == null ? "无" : depend.toString()).append("<BR/>");
		sbf.append("需要繁荣度[").append(StringTool.intArr2String(getLvUpAddProsper(), ",")).append("]<BR/>");
		sbf.append("日常维修费[").append(StringTool.longArr2String(getDailyMaintainCost(), ",")).append("]<BR/>");
		sbf.append("升级建筑消耗的钱[").append(StringTool.longArr2String(getLvUpCostCoin(), ",")).append("]<BR/>");
		sbf.append("升级建筑消耗的灵脉值[").append(StringTool.longArr2String(getLvUpCostSpirit(), ",")).append("]<BR/>");
		sbf.append("发布任务数[").append(StringTool.intArr2String(getReleaseTaskNum(), ",")).append("]<BR/>");
		return sbf.toString();
	}

	@Override
	public boolean canLvUp(SeptStation station) {
		return false;
	}

	public void loadPath() {
		confRoot = root;
		if (logger.isInfoEnabled()) logger.info("root========" + root);
		if (logger.isInfoEnabled()) logger.info("confRoot========" + confRoot);
	}
}
