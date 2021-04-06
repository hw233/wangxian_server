package com.fy.engineserver.septbuilding.entity;

import com.fy.engineserver.septbuilding.templet.SeptBuildingTemplet;
import com.fy.engineserver.septstation.service.SeptStationManager;

/**
 * 客户端查看驻地建筑信息
 * 
 * 
 */
public class SeptBuildingEntity4Client {

	/** NPCID */
	private long npcId;
	/** NPC名字 */
	private String npcName;
	/** 建筑等级 */
	private int grade;
	/** 建筑类型 */
	private int buildType;
	/** 是否在建造 */
	private boolean inBuild;
	/** NPC位置索引 */
	private int index;
	/** 描述 */
	private String des;
	/** 建造前提 */
	private String depend;

	private int dependType;
	private int dependGrade;
	/** X坐标 */
	private int x;
	/** Y坐标 */
	private int y;
	/** 升级所需灵脉值 */
	private long lvUpCostSpirit;
	/** 升级所需金钱 */
	private long lvUpCostCoin;
	/** 当前维护费 */
	private long dailyMaintainCost;
	/** 升级后维护费 */
	private long lvupMaintainCost;
	/** 当前繁荣度 */
	private int currAddProsper;
	/** 升级后繁荣度 */
	private int lvUpAddProsper;
	/** 发布任务数 */
	private int releaseTaskNum;
	/** 图标 */
	private String icon;

	public SeptBuildingEntity4Client() {

	}

	/**
	 * @param entity
	 */
	public SeptBuildingEntity4Client(SeptBuildingEntity entity) {
		this.setNpcId(entity.getNpc().getId());
		this.setNpcName(entity.getNpc().getName());
		this.setIndex(entity.getIndex());
		this.setBuildType(entity.getTempletIndex());
		this.setGrade(entity.getBuildingState().getLevel());
		this.setDes("");
		if (entity.getBuildingState() != null && entity.getBuildingState().getTempletType() != null && entity.getBuildingState().getTempletType().getDepend() != null) {
			this.setDepend(entity.getBuildingState().getTempletType().getDepend().toString());
			this.setDependType(entity.getBuildingState().getTempletType().getDepend().getTempletType().getTypeIndex());
			this.setDependGrade(entity.getBuildingState().getTempletType().getDepend().getLevel());
		} else {
			this.setDepend("");
		}
		this.setIcon(entity.getNpc().getIcon());
		this.setX(entity.getNpc().getX());
		this.setY(entity.getNpc().getY());
		this.setInBuild(entity.isInBuild());

		int level = entity.getBuildingState().getLevel();
		if (level <= 0) {// 直接设置成0
			this.setLvupMaintainCost(entity.getBuildingState().getTempletType().getDailyMaintainCost()[0]/SeptStationManager.每次维护费用比率);
			this.setLvUpCostCoin(entity.getBuildingState().getTempletType().getLvUpCostCoin()[0]);
			this.setLvUpCostSpirit(entity.getBuildingState().getTempletType().getLvUpCostSpirit()[0]);
			this.setLvUpAddProsper(entity.getBuildingState().getTempletType().getLvUpAddProsper()[0]);

			this.setCurrAddProsper(0);
			this.setReleaseTaskNum(entity.getBuildingState().getTempletType().getReleaseTaskNum()[0]);
			this.setDailyMaintainCost(0);
		} else if (level == SeptBuildingTemplet.MAX_LEVEL) {// 满级的
			this.setLvupMaintainCost(entity.getBuildingState().getTempletType().getDailyMaintainCost()[level - 1]/SeptStationManager.每次维护费用比率);
			this.setCurrAddProsper(entity.getBuildingState().getTempletType().getLvUpAddProsper()[level - 1]);
			this.setDailyMaintainCost(entity.getBuildingState().getTempletType().getDailyMaintainCost()[level - 1]/SeptStationManager.每次维护费用比率);
		} else {
			this.setLvupMaintainCost(entity.getBuildingState().getTempletType().getDailyMaintainCost()[level]/SeptStationManager.每次维护费用比率);
			this.setLvUpCostCoin(entity.getBuildingState().getTempletType().getLvUpCostCoin()[level]);
			this.setLvUpCostSpirit(entity.getBuildingState().getTempletType().getLvUpCostSpirit()[level]);
			this.setLvUpAddProsper(entity.getBuildingState().getTempletType().getLvUpAddProsper()[level]);

			this.setCurrAddProsper(entity.getBuildingState().getTempletType().getLvUpAddProsper()[level - 1]);
			this.setDailyMaintainCost(entity.getBuildingState().getTempletType().getDailyMaintainCost()[level - 1]/SeptStationManager.每次维护费用比率);
			this.setReleaseTaskNum(entity.getBuildingState().getTempletType().getReleaseTaskNum()[level - 1]);
		}
	}

	public long getNpcId() {
		return npcId;
	}

	public void setNpcId(long npcId) {
		this.npcId = npcId;
	}

	public String getNpcName() {
		return npcName;
	}

	public void setNpcName(String npcName) {
		this.npcName = npcName;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public String getDepend() {
		return depend;
	}

	public void setDepend(String depend) {
		this.depend = depend;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public long getLvUpCostSpirit() {
		return lvUpCostSpirit;
	}

	public void setLvUpCostSpirit(long lvUpCostSpirit) {
		this.lvUpCostSpirit = lvUpCostSpirit;
	}

	public long getLvUpCostCoin() {
		return lvUpCostCoin;
	}

	public void setLvUpCostCoin(long lvUpCostCoin) {
		this.lvUpCostCoin = lvUpCostCoin;
	}

	public long getDailyMaintainCost() {
		return dailyMaintainCost;
	}

	public void setDailyMaintainCost(long dailyMaintainCost) {
		this.dailyMaintainCost = dailyMaintainCost;
	}

	public long getLvupMaintainCost() {
		return lvupMaintainCost;
	}

	public void setLvupMaintainCost(long lvupMaintainCost) {
		this.lvupMaintainCost = lvupMaintainCost;
	}

	public int getCurrAddProsper() {
		return currAddProsper;
	}

	public void setCurrAddProsper(int currAddProsper) {
		this.currAddProsper = currAddProsper;
	}

	public int getLvUpAddProsper() {
		return lvUpAddProsper;
	}

	public void setLvUpAddProsper(int lvUpAddProsper) {
		this.lvUpAddProsper = lvUpAddProsper;
	}

	public boolean isInBuild() {
		return inBuild;
	}

	public void setInBuild(boolean inBuild) {
		this.inBuild = inBuild;
	}

	public int getBuildType() {
		return buildType;
	}

	public void setBuildType(int buildType) {
		this.buildType = buildType;
	}

	public int getDependType() {
		return dependType;
	}

	public void setDependType(int dependType) {
		this.dependType = dependType;
	}

	public int getDependGrade() {
		return dependGrade;
	}

	public void setDependGrade(int dependGrade) {
		this.dependGrade = dependGrade;
	}

	public int getReleaseTaskNum() {
		return releaseTaskNum;
	}

	public void setReleaseTaskNum(int releaseTaskNum) {
		this.releaseTaskNum = releaseTaskNum;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	@Override
	public String toString() {
		return "SeptBuildingEntity4Client [npcId=" + npcId + ", npcName=" + npcName + ", grade=" + grade + ", buildType=" + buildType + ", inBuild=" + inBuild + ", index=" + index + ", des=" + des + ", depend=" + depend + ", dependType=" + dependType + ", dependGrade=" + dependGrade + ", x=" + x + ", y=" + y + ", lvUpCostSpirit=" + lvUpCostSpirit + ", lvUpCostCoin=" + lvUpCostCoin + ", dailyMaintainCost=" + dailyMaintainCost + ", lvupMaintainCost=" + lvupMaintainCost + ", currAddProsper=" + currAddProsper + ", lvUpAddProsper=" + lvUpAddProsper + ", releaseTaskNum=" + releaseTaskNum + ", icon=" + icon + "]";
	}
}
