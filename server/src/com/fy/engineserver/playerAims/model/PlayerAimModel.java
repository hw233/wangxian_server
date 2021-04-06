package com.fy.engineserver.playerAims.model;

import java.util.List;

import com.fy.engineserver.achievement.RecordAction;

public class PlayerAimModel {
	/** 所属章节名 */
	private String chapterName;
	/** 目标id(唯一，存库用) */
	private int id;
	/** 记录类型 */
	private RecordAction action;
	/** 目标名 */
	private String aimName;
	/** 领取奖励等级限制 */
	private int levelLimit;
	/** 领取vip奖励vip等级限制 */
	private int vipLimit;
	/** vip获得奖励倍数(只能是整数) */
	private int mulReward4Vip;
	/** 描述(目标任务) */
	private String description;
	/** 导航描述 */
	private String navigationDes;
	/** icon */
	private String icon;
	/** 边框颜色 */
	private int frameColor;
	/** 完成目标需要数量 */
	private int num;
	/** 奖励积分 */
	private int score;
	/** 奖励道具List */
	private List<RewordArticle> rewardArticles;
	/** 奖励称号名 */
	private String rewardTitle;
	/** 奖励工资数 */
	private long rewardGongzi;
	/** 奖励绑银数 */
	private long rewardBindYin;
	/** 奖励功勋 */
	private long rewardGongXun;
	/** 是否显示进度 */
	private byte showProgress;
	/** 达成目标前是否显示在目标栏中（隐藏章节中有些探索目标） */
	private boolean display;
	/** 目标级别   大于等于3的将达成的目标发到世界 */
	private int aimLevel;
	
	@Override
	public String toString() {
		return "PlayerAimModel [chapterName=" + chapterName + ", id=" + id + ", action=" + action + ", aimName=" + aimName + ", levelLimit=" + levelLimit + ", vipLimit=" + vipLimit + ", mulReward4Vip=" + mulReward4Vip + ", description=" + description + ", navigationDes=" + navigationDes + ", icon=" + icon + ", frameColor=" + frameColor + ", num=" + num + ", score=" + score + ", rewardArticles=" + rewardArticles + ", rewardTitle=" + rewardTitle + ", rewardGongzi=" + rewardGongzi + ", rewardBindYin=" + rewardBindYin + ", rewardGongXun=" + rewardGongXun + ", showProgress=" + getShowProgress() + ", display=" + display + "]";
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public RecordAction getAction() {
		return action;
	}
	public void setAction(RecordAction action) {
		this.action = action;
	}
	public String getAimName() {
		return aimName;
	}
	public void setAimName(String aimName) {
		this.aimName = aimName;
	}
	public int getLevelLimit() {
		return levelLimit;
	}
	public void setLevelLimit(int levelLimit) {
		this.levelLimit = levelLimit;
	}
	public int getVipLimit() {
		return vipLimit;
	}
	public void setVipLimit(int vipLimit) {
		this.vipLimit = vipLimit;
	}
	public int getMulReward4Vip() {
		return mulReward4Vip;
	}
	public void setMulReward4Vip(int mulReward4Vip) {
		this.mulReward4Vip = mulReward4Vip;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getNavigationDes() {
		return navigationDes;
	}
	public void setNavigationDes(String navigationDes) {
		this.navigationDes = navigationDes;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public List<RewordArticle> getRewardArticles() {
		return rewardArticles;
	}
	public void setRewardArticles(List<RewordArticle> rewardArticles) {
		this.rewardArticles = rewardArticles;
	}
	public String getRewardTitle() {
		return rewardTitle;
	}
	public void setRewardTitle(String rewardTitle) {
		this.rewardTitle = rewardTitle;
	}
	public long getRewardGongzi() {
		return rewardGongzi;
	}
	public void setRewardGongzi(long rewardGongzi) {
		this.rewardGongzi = rewardGongzi;
	}
	public long getRewardBindYin() {
		return rewardBindYin;
	}
	public void setRewardBindYin(long rewardBindYin) {
		this.rewardBindYin = rewardBindYin;
	}
	public long getRewardGongXun() {
		return rewardGongXun;
	}
	public void setRewardGongXun(long rewardGongXun) {
		this.rewardGongXun = rewardGongXun;
	}
	public boolean isDisplay() {
		return display;
	}
	public void setDisplay(boolean display) {
		this.display = display;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int getFrameColor() {
		return frameColor;
	}
	public void setFrameColor(int frameColor) {
		this.frameColor = frameColor;
	}
	public String getChapterName() {
		return chapterName;
	}
	public void setChapterName(String chapterName) {
		this.chapterName = chapterName;
	}
	public byte getShowProgress() {
		return showProgress;
	}
	public void setShowProgress(byte showProgress) {
		this.showProgress = showProgress;
	}
	public int getAimLevel() {
		return aimLevel;
	}
	public void setAimLevel(int aimLevel) {
		this.aimLevel = aimLevel;
	}
	
	
}
