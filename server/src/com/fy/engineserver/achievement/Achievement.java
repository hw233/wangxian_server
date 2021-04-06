package com.fy.engineserver.achievement;

public class Achievement {
	 
	private int id;
	private int level;// 评级
	private RecordAction action;
	private String maimType;
	private String subType;
	private long num;
	private String name;
	private String des;
	private String fullDes;
	private String prizeArticle;
	private String prizeTitle;
	private long prizeAchievementNum;
	private String icon;
	private boolean showSchedule;

	public Achievement() {

	}

	public Achievement(int id, int level, RecordAction action, String maimType, String subType, long num, String name, String des, String fullDes, String prizeArticle, String prizeTitle, long prizeAchievementNum, String icon, boolean showSchedule) {
		this.id = id;
		this.level = level;
		this.action = action;
		this.maimType = maimType;
		this.subType = subType;
		this.num = num;
		this.name = name;
		this.des = des;
		this.fullDes = fullDes;
		this.prizeArticle = prizeArticle;
		this.prizeTitle = prizeTitle;
		this.prizeAchievementNum = prizeAchievementNum;
		this.icon = icon;
		this.showSchedule = showSchedule;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public RecordAction getAction() {
		return action;
	}

	public void setAction(RecordAction action) {
		this.action = action;
	}

	public String getMaimType() {
		return maimType;
	}

	public void setMaimType(String maimType) {
		this.maimType = maimType;
	}

	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}

	public long getNum() {
		return num;
	}

	public void setNum(long num) {
		this.num = num;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public String getFullDes() {
		return fullDes;
	}

	public void setFullDes(String fullDes) {
		this.fullDes = fullDes;
	}

	public String getPrizeArticle() {
		return prizeArticle;
	}

	public void setPrizeArticle(String prizeArticle) {
		this.prizeArticle = prizeArticle;
	}

	public String getPrizeTitle() {
		return prizeTitle;
	}

	public void setPrizeTitle(String prizeTitle) {
		this.prizeTitle = prizeTitle;
	}

	public long getPrizeAchievementNum() {
		return prizeAchievementNum;
	}

	public void setPrizeAchievementNum(long prizeAchievementNum) {
		this.prizeAchievementNum = prizeAchievementNum;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public boolean isShowSchedule() {
		return showSchedule;
	}

	public void setShowSchedule(boolean showSchedule) {
		this.showSchedule = showSchedule;
	}

	@Override
	public String toString() {
		return "Achievement [id=" + id + ", level=" + level + ", action=" + action + ", maimType=" + maimType + ", subType=" + subType + ", num=" + num + ", name=" + name + ", des=" + des + ", fullDes=" + fullDes + ", prizeArticle=" + prizeArticle + ", prizeTitle=" + prizeTitle + ", prizeAchievementNum=" + prizeAchievementNum + ", icon=" + icon + ", showSchedule=" + showSchedule + "]";
	}
}
