package com.fy.engineserver.septstation;

/**
 * 驻地相关信息
 * (优化时考虑)
 * 
 * 
 */
public class SeptStationInfo {
	/** 资金上限 */
	private long MaxCoin;
	/** 繁荣度 */
	private int prosper;
	/** 当前基础建筑的总等级 */
	private int currBaseTotalLevel;
	/** 基础建筑的总等级 */
	private int baseTotalLevel;
	/** 隐藏资源比例(地窖) */
	private double hiddenResProportion;
	/** 维修标准(聚仙殿 聚宝庄) */
	private long baseMaintai;
	/** 当前的维修费用 (所有建筑) */
	private long currMaintai;
	/** 低维修费用 */
	private long lowMaintai;
	/** 正常维修费用 */
	private long normalMaintai;
	/** 人口上限 */
	private int maxMember;
	/** 商圈的个数 (物宝集) */
	private int tradeRound;
	/** 商线的个数 (物宝集) */
	private int tradeLineNum;
	/** 灵脉值上限 */
	private long maxSprint;
	// /** 当前箭塔数量 */
	// private int currArrowTowerNum;
	// /** 当前可建造箭塔数量 */
	// private int maxArrowTowerNum;
	/** 当前已经建造了的标志像的数量 */
	private int currentBiaozhixiangNum;
	/** 当前可出售的防具的等级 */
	private int maxGradeOfFangJu;
	/** 当前可出售的武器的等级 */
	private int maxGradeOfWuQi;
	/** 当前可造战车数量 */
	private int maxTankNum;
	/** 护法阵的最大血量 */
	private int huFaZhenMaxHp;
	/** 护法阵的防御 */
	private int huFaZhenDefend;
	/** 家族大旗的最大血量 */
	private int jiaZuDaQiMaxHp;
	/** 聚宝庄修炼的最大血量 */
	private int juBaoZhuangMaxHpAdd;;
	/** 聚宝庄修炼的最大蓝量 */
	private int juBaoZhuangMaxMpAdd;
	/** 特殊物品的个数 （龙图阁） */
	private int maxSpecialGoodsNum;
	/** 幽冥幻域层数 */
	private int tongTianTaTopFloors;

	public long getMaxCoin() {
		return MaxCoin;
	}

	public void setMaxCoin(long maxCoin) {
		MaxCoin = maxCoin;
	}

	public int getProsper() {
		return prosper;
	}

	public void setProsper(int prosper) {
		this.prosper = prosper;
	}

	public int getCurrBaseTotalLevel() {
		return currBaseTotalLevel;
	}

	public void setCurrBaseTotalLevel(int currBaseTotalLevel) {
		this.currBaseTotalLevel = currBaseTotalLevel;
	}

	public int getBaseTotalLevel() {
		return baseTotalLevel;
	}

	public void setBaseTotalLevel(int baseTotalLevel) {
		this.baseTotalLevel = baseTotalLevel;
	}

	public double getHiddenResProportion() {
		return hiddenResProportion;
	}

	public void setHiddenResProportion(double hiddenResProportion) {
		this.hiddenResProportion = hiddenResProportion;
	}

	public long getBaseMaintai() {
		return baseMaintai;
	}

	public void setBaseMaintai(long baseMaintai) {
		this.baseMaintai = baseMaintai;
	}

	public long getCurrMaintai() {
		return currMaintai;
	}

	public void setCurrMaintai(long currMaintai) {
		this.currMaintai = currMaintai;
	}

	public int getMaxMember() {
		return maxMember;
	}

	public void setMaxMember(int maxMember) {
		this.maxMember = maxMember;
	}

	public int getTradeRound() {
		return tradeRound;
	}

	public void setTradeRound(int tradeRound) {
		this.tradeRound = tradeRound;
	}

	public int getTradeLineNum() {
		return tradeLineNum;
	}

	public void setTradeLineNum(int tradeLineNum) {
		this.tradeLineNum = tradeLineNum;
	}

	public long getMaxSprint() {
		return maxSprint;
	}

	public void setMaxSprint(long maxSprint) {
		this.maxSprint = maxSprint;
	}

	public int getCurrentBiaozhixiangNum() {
		return currentBiaozhixiangNum;
	}

	public void setCurrentBiaozhixiangNum(int currentBiaozhixiangNum) {
		this.currentBiaozhixiangNum = currentBiaozhixiangNum;
	}

	public int getMaxGradeOfFangJu() {
		return maxGradeOfFangJu;
	}

	public void setMaxGradeOfFangJu(int maxGradeOfFangJu) {
		this.maxGradeOfFangJu = maxGradeOfFangJu;
	}

	public int getMaxGradeOfWuQi() {
		return maxGradeOfWuQi;
	}

	public void setMaxGradeOfWuQi(int maxGradeOfWuQi) {
		this.maxGradeOfWuQi = maxGradeOfWuQi;
	}

	public int getMaxTankNum() {
		return maxTankNum;
	}

	public void setMaxTankNum(int maxTankNum) {
		this.maxTankNum = maxTankNum;
	}

	public int getHuFaZhenMaxHp() {
		return huFaZhenMaxHp;
	}

	public void setHuFaZhenMaxHp(int huFaZhenMaxHp) {
		this.huFaZhenMaxHp = huFaZhenMaxHp;
	}

	public int getHuFaZhenDefend() {
		return huFaZhenDefend;
	}

	public void setHuFaZhenDefend(int huFaZhenDefend) {
		this.huFaZhenDefend = huFaZhenDefend;
	}

	public int getJiaZuDaQiMaxHp() {
		return jiaZuDaQiMaxHp;
	}

	public void setJiaZuDaQiMaxHp(int jiaZuDaQiMaxHp) {
		this.jiaZuDaQiMaxHp = jiaZuDaQiMaxHp;
	}

	public int getJuBaoZhuangMaxHpAdd() {
		return juBaoZhuangMaxHpAdd;
	}

	public void setJuBaoZhuangMaxHpAdd(int juBaoZhuangMaxHpAdd) {
		this.juBaoZhuangMaxHpAdd = juBaoZhuangMaxHpAdd;
	}

	public int getJuBaoZhuangMaxMpAdd() {
		return juBaoZhuangMaxMpAdd;
	}

	public void setJuBaoZhuangMaxMpAdd(int juBaoZhuangMaxMpAdd) {
		this.juBaoZhuangMaxMpAdd = juBaoZhuangMaxMpAdd;
	}

	public int getMaxSpecialGoodsNum() {
		return maxSpecialGoodsNum;
	}

	public void setMaxSpecialGoodsNum(int maxSpecialGoodsNum) {
		this.maxSpecialGoodsNum = maxSpecialGoodsNum;
	}

	public int getTongTianTaTopFloors() {
		return tongTianTaTopFloors;
	}

	public void setTongTianTaTopFloors(int tongTianTaTopFloors) {
		this.tongTianTaTopFloors = tongTianTaTopFloors;
	}

	public long getLowMaintai() {
		return lowMaintai;
	}

	public void setLowMaintai(long lowMaintai) {
		this.lowMaintai = lowMaintai;
	}

	public long getNormalMaintai() {
		return normalMaintai;
	}

	public void setNormalMaintai(long normalMaintai) {
		this.normalMaintai = normalMaintai;
	}
}
