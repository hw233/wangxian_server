package com.fy.engineserver.playerAims.model;

import java.util.List;


public class ChapterModel {
	/** 章节中包含的目标 */
	private List<PlayerAimModel> aimsList;
	/** 章节名 */
	private String chapterName;
	/** 副标题 */
	private String subtitle;
	/** 领取奖励总积分限制 */
	private int scoreLimit;
	/** 领取奖励等级限制 */
	private int levelLimit;
	/** 等级上限（区分等级段用） */
	private int maxLevel;
	/** 领取vip奖励vip等级限制 */
	private int vipLimit;
	/** vip获得奖励倍数(只能是整数) */
	private int mulReward4Vip;
	/** 描述 */
	private String description;
	/** icon */
	private String icon;
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
	/** 章节总共可获得的积分 */
	private int totalScore = 0;
	
	@Override
	public String toString() {
		return "ChapterModel [aimsList=" + aimsList + ", chapterName=" + chapterName + ", scoreLimit=" + scoreLimit + ", levelLimit=" + levelLimit + ", maxLevel=" + maxLevel + ", vipLimit=" + vipLimit + ", mulReward4Vip=" + mulReward4Vip + ", description=" + description + ", icon=" + icon + ", rewardArticles=" + rewardArticles + ", rewardTitle=" + rewardTitle + ", rewardGongzi=" + rewardGongzi + ", rewardBindYin=" + rewardBindYin + ", rewardGongXun=" + rewardGongXun + ", totalScore=" + totalScore + "]";
	}
	
	public void updateScore() {
		if(aimsList != null && aimsList.size() > 0 && totalScore <= 0) 
		{		
			for(PlayerAimModel pam : aimsList) {
				totalScore += pam.getScore();	
			}
		}
	}
	public List<PlayerAimModel> getAimsList() {
		return aimsList;
	}
	public void setAimsList(List<PlayerAimModel> aimsList) {
		this.aimsList = aimsList;
	}
	public int getScoreLimit() {
		return scoreLimit;
	}
	public void setScoreLimit(int scoreLimit) {
		this.scoreLimit = scoreLimit;
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
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
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
	public int getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}
	public int getMaxLevel() {
		return maxLevel;
	}
	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}
	public String getChapterName() {
		return chapterName;
	}
	public void setChapterName(String chapterName) {
		this.chapterName = chapterName;
	}
	public String getSubtitle() {
		return subtitle;
	}
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}
	
	
}
