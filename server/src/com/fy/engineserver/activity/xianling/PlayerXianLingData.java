package com.fy.engineserver.activity.xianling;

import java.util.Map;

import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

@SimpleEntity
public class PlayerXianLingData {
	@SimpleId
	private long id;// playerId
	@SimpleVersion
	private int version;

	/** 真气 */
	private int energy;

	/** 当前已经挑战过的最大关卡,从1开始 */
	private int maxLevel;

	/** 限时任务id */
	private int taskId;

	/** 限时任务0-未接取；1-已接取未完成；2-已完成；3-超时失效 */
	private byte taskState;

	/** 限时任务奖励道具信息 */
	private String[] articleCNName;
	private int[] articleColor;
	private int[] articleNum;
	private boolean[] bind;

	/** 是否已领取限时任务奖励 */
	private boolean takePrize;

	/** 下次限时任务可免费刷新的时间 */
	private long nextRefreshTime;

	/** 积分 */
	private int score;

	/** 已购买真气次数 */
	private int bugEntityTimes;

	/** 下次自动恢复真气时间 */
	private long nextResumeEnergy;

	/** 积分奖励领取记录 */
	private Map<Integer, Boolean> takeScorePrizeMap;

	/** 技能cd<技能id, 下次可使用时间> */
	private Map<Integer, Long> skillCDMap;
	/** 技能公cd结束时间 */
	private transient long puclicCDEnd;
	/** 显示技能说明 */
	private boolean shownHelp;
	/** 记录当前正在参与的活动key,如果有变化,则活动结束或开启新活动,要清除玩家数据 */
	private int activityKey;
	/** 上次扣除真气时间，防止掉线情况下玩家点击挑战次数太多导致短时间重新连上后重复扣除真气 */
	private long lastCostEnergyTime;

	public PlayerXianLingData() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PlayerXianLingData(long id, int energy, int maxLevel, int taskId, byte taskState, boolean takePrize, long nextRefreshTime, int score, int bugEntityTimes, long nextResumeEnergy, Map<Integer, Boolean> takeScorePrizeMap, Map<Integer, Long> skillCDMap) {
		this.id = id;
		this.energy = energy;
		this.maxLevel = maxLevel;
		this.taskId = taskId;
		this.taskState = taskState;
		this.takePrize = takePrize;
		this.nextRefreshTime = nextRefreshTime;
		this.score = score;
		this.bugEntityTimes = bugEntityTimes;
		this.nextResumeEnergy = nextResumeEnergy;
		this.takeScorePrizeMap = takeScorePrizeMap;
		this.skillCDMap = skillCDMap;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getEnergy() {
		return energy;
	}

	public void setEnergy(int energy) {
		this.energy = energy;
		XianLingManager.xianlingEm.notifyFieldChange(this, "energy");
	}

	public int getMaxLevel() {
		return maxLevel;
	}

	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
		XianLingManager.xianlingEm.notifyFieldChange(this, "maxLevel");
	}

	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
		XianLingManager.xianlingEm.notifyFieldChange(this, "taskId");
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
		XianLingManager.xianlingEm.notifyFieldChange(this, "score");
	}

	public byte getTaskState() {
		return taskState;
	}

	public void setTaskState(byte taskState) {
		this.taskState = taskState;
		XianLingManager.xianlingEm.notifyFieldChange(this, "taskState");
	}

	public long getNextRefreshTime() {
		return nextRefreshTime;
	}

	public void setNextRefreshTime(long nextRefreshTime) {
		this.nextRefreshTime = nextRefreshTime;
		XianLingManager.xianlingEm.notifyFieldChange(this, "nextRefreshTime");
	}

	public boolean isTakePrize() {
		return takePrize;
	}

	public void setTakePrize(boolean takePrize) {
		this.takePrize = takePrize;
		XianLingManager.xianlingEm.notifyFieldChange(this, "takePrize");
	}

	public int getBugEntityTimes() {
		return bugEntityTimes;
	}

	public void setBugEntityTimes(int bugEntityTimes) {
		this.bugEntityTimes = bugEntityTimes;
		XianLingManager.xianlingEm.notifyFieldChange(this, "bugEntityTimes");
	}

	public long getNextResumeEnergy() {
		return nextResumeEnergy;
	}

	public void setNextResumeEnergy(long nextResumeEnergy) {
		this.nextResumeEnergy = nextResumeEnergy;
		XianLingManager.xianlingEm.notifyFieldChange(this, "nextResumeEnergy");
	}

	public String[] getArticleCNName() {
		return articleCNName;
	}

	public void setArticleCNName(String[] articleCNName) {
		this.articleCNName = articleCNName;
		XianLingManager.xianlingEm.notifyFieldChange(this, "articleCNName");
	}

	public int[] getArticleColor() {
		return articleColor;
	}

	public void setArticleColor(int[] articleColor) {
		this.articleColor = articleColor;
		XianLingManager.xianlingEm.notifyFieldChange(this, "articleColor");
	}

	public int[] getArticleNum() {
		return articleNum;
	}

	public void setArticleNum(int[] articleNum) {
		this.articleNum = articleNum;
		XianLingManager.xianlingEm.notifyFieldChange(this, "articleNum");
	}

	public boolean[] getBind() {
		return bind;
	}

	public void setBind(boolean[] bind) {
		this.bind = bind;
		XianLingManager.xianlingEm.notifyFieldChange(this, "bind");
	}

	public Map<Integer, Boolean> getTakeScorePrizeMap() {
		return takeScorePrizeMap;
	}

	public void setTakeScorePrizeMap(Map<Integer, Boolean> takeScorePrizeMap) {
		this.takeScorePrizeMap = takeScorePrizeMap;
		XianLingManager.xianlingEm.notifyFieldChange(this, "takeScorePrizeMap");
	}

	public Map<Integer, Long> getSkillCDMap() {
		return skillCDMap;
	}

	public void setSkillCDMap(Map<Integer, Long> skillCDMap) {
		this.skillCDMap = skillCDMap;
		XianLingManager.xianlingEm.notifyFieldChange(this, "skillCDMap");
	}

	public long getPuclicCDEnd() {
		return puclicCDEnd;
	}

	public void setPuclicCDEnd(long puclicCDEnd) {
		this.puclicCDEnd = puclicCDEnd;
	}

	public boolean isShownHelp() {
		return shownHelp;
	}

	public void setShownHelp(boolean shownHelp) {
		this.shownHelp = shownHelp;
		XianLingManager.xianlingEm.notifyFieldChange(this, "shownHelp");
	}

	public int getActivityKey() {
		return activityKey;
	}

	public void setActivityKey(int activityKey) {
		this.activityKey = activityKey;
		XianLingManager.xianlingEm.notifyFieldChange(this, "activityKey");
	}

	public long getLastCostEnergyTime() {
		return lastCostEnergyTime;
	}

	public void setLastCostEnergyTime(long lastCostEnergyTime) {
		this.lastCostEnergyTime = lastCostEnergyTime;
		XianLingManager.xianlingEm.notifyFieldChange(this, "lastCostEnergyTime");
	}

	@Override
	public String toString() {
		return "[PlayerXianLingData:playerId=" + id + ",score=" + score + ",energy=" + energy + ",bugEntityTimes=" + bugEntityTimes + ",nextResumeEnergy=" + nextResumeEnergy + ",maxLevel=" + maxLevel + ",taskId" + taskId + ",taskState" + taskState + ",takePrize=" + takePrize + ",nextRefreshTime=" + nextRefreshTime + ",shownHelp=" + shownHelp + ",activityKey=" + activityKey + ",lastCostEnergyTime=" + lastCostEnergyTime + "]";
	}
}
