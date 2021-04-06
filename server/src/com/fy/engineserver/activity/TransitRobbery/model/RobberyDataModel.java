package com.fy.engineserver.activity.TransitRobbery.model;


public class RobberyDataModel {
	private int id;
	/** 天劫名 */
	private String robberyName;
	/** 场景名 */
	private String sceneName;
	/** 等级限制 */
	private int levelLimit;
	/** 是否元神挑战*/
	private boolean soulChallenge;
	/** 胜利条件 */
	private String cleCondition;				//会有多个，需要在渡劫的时候检测达到了第几小关，根据小关判断是否渡劫成功（进入下一小关）
//	/** boss刷出坐标 */
//	private String bossPoint;
	/** 散仙出现概率 */
	private int immortalProbability;
	/** 散仙最大个数 */
	private int maxImmortalAmount;
	/** 劫兽出现概率 */
	private int beastProbability;
	/** 劫兽刷出最大个数 */
	private int maxBeastAmount;
	/** 散仙刷出坐标 */
	private String immortalPoint;
	/** 劫兽刷出坐标 */
	private String beastPoint;
	/** 劫兽id */
	private String beastId;
	/** 玩家进入场景后的默认坐标 */
	private String initPoint;
	/** 提示信息 */
	private String tips;
	/** 进入天劫后的提示信息 */
	private String tips2;
	/** 胜利后弹窗内容 */
	private String tips3;
	
	public String toString(){
		return "id=" + id + " robberyName=" + robberyName + " levelLimit=" + levelLimit + " soulchallenge=" + soulChallenge
				+ "  bosspoint=" + "immortalProbability=" + immortalProbability + " beastProbability=" + beastProbability
				+ " immortalPoint=" + immortalPoint + " beastPoint=" + beastPoint + " beastId=" + beastId ;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getRobberyName() {
		return robberyName;
	}
	public void setRobberyName(String robberyName) {
		this.robberyName = robberyName;
	}
	public int getLevelLimit() {
		return levelLimit;
	}
	public void setLevelLimit(int levelLimit) {
		this.levelLimit = levelLimit;
	}
	public boolean getSoulChallenge() {
		return soulChallenge;
	}
	public void setSoulChallenge(boolean soulChallenge) {
		this.soulChallenge = soulChallenge;
	}
	public int getImmortalProbability() {
		return immortalProbability;
	}
	public void setImmortalProbability(int immortalProbability) {
		this.immortalProbability = immortalProbability;
	}
	public int getBeastProbability() {
		return beastProbability;
	}
	public void setBeastProbability(int beastProbability) {
		this.beastProbability = beastProbability;
	}
	public String getImmortalPoint() {
		return immortalPoint;
	}
	public void setImmortalPoint(String immortalPoint) {
		this.immortalPoint = immortalPoint;
	}
	public String getBeastPoint() {
		return beastPoint;
	}
	public void setBeastPoint(String beastPoint) {
		this.beastPoint = beastPoint;
	}
	public String getBeastId() {
		return beastId;
	}
	public void setBeastId(String beastId) {
		this.beastId = beastId;
	}
	public String getImmortalId() {
		return immortalId;
	}
	public void setImmortalId(String immortalId) {
		this.immortalId = immortalId;
	}
	public String getAward() {
		return award;
	}
	public void setAward(String award) {
		this.award = award;
	}
	public String getSceneName() {
		return sceneName;
	}

	public void setSceneName(String sceneName) {
		this.sceneName = sceneName;
	}
	public int getMaxImmortalAmount() {
		return maxImmortalAmount;
	}

	public void setMaxImmortalAmount(int maxImmortalAmount) {
		this.maxImmortalAmount = maxImmortalAmount;
	}
	public int getMaxBeastAmount() {
		return maxBeastAmount;
	}

	public void setMaxBeastAmount(int maxBeastAmount) {
		this.maxBeastAmount = maxBeastAmount;
	}
	public String getInitPoint() {
		return initPoint;
	}

	public void setInitPoint(String initPoint) {
		this.initPoint = initPoint;
	}
	public String getTips() {
		return tips;
	}

	public void setTips(String tips) {
		this.tips = tips;
	}
	public String getCleCondition() {
		return cleCondition;
	}

	public void setCleCondition(String cleCondition) {
		this.cleCondition = cleCondition;
	}
	public String getTips2() {
		return tips2;
	}

	public void setTips2(String tips2) {
		this.tips2 = tips2;
	}
	public String getTips3() {
		return tips3;
	}

	public void setTips3(String tips3) {
		this.tips3 = tips3;
	}
	/** 散仙id */
	private String immortalId;
	/** 通关后奖励 */
	private String award;
}
