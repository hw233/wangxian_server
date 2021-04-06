package com.fy.engineserver.activity.activeness;


/**
 * 活跃度活动数据
 * 
 * 
 */
public class ActivityForActiveness {

	private int id;
//	private String openTimeDistance;
	private String name;
	private String nameFtr;
	private String startGame;
	private String startNpc;
	private String startNpcFtr;
	private int startX;
	private int startY;
	private String shortdes;
	private String shortdesFtr;
	private String detaildes;
	private String detaildesFtr;
	private int exp;
	private int article;
	private int equipment;
	private int levelLimit;
	private int timesLimit; // 活跃度单日活动次数上限
	private int activityAdd;
	private int tili;

	private ActivenessType type;//活跃度类型
//	private String startGameCnName;
//	private String startGameCnNameFtr;


	public ActivityForActiveness() {
		// TODO Auto-generated constructor stub
	}


	public ActivityForActiveness(int id, String name, String nameFtr, String startGame, String startNpc, String startNpcFtr, int startX, int startY, String shortdes, String shortdesFtr, String detaildes, String detaildesFtr, int exp, int article, int equipment, int levelLimit, int timesLimit, int activityAdd, int tili, ActivenessType type) {
	this.id = id;
	this.name = name;
	this.nameFtr = nameFtr;
	this.startGame = startGame;
	this.startNpc = startNpc;
	this.startNpcFtr = startNpcFtr;
	this.startX = startX;
	this.startY = startY;
	this.shortdes = shortdes;
	this.shortdesFtr = shortdesFtr;
	this.detaildes = detaildes;
	this.detaildesFtr = detaildesFtr;
	this.exp = exp;
	this.article = article;
	this.equipment = equipment;
	this.levelLimit = levelLimit;
	this.timesLimit = timesLimit;
	this.activityAdd = activityAdd;
	this.tili = tili;
	this.type = type;
}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameFtr() {
		return nameFtr;
	}

	public void setNameFtr(String nameFtr) {
		this.nameFtr = nameFtr;
	}

	public String getStartGame() {
		return startGame;
	}

	public void setStartGame(String startGame) {
		this.startGame = startGame;
	}

	public String getStartNpc() {
		return startNpc;
	}

	public void setStartNpc(String startNpc) {
		this.startNpc = startNpc;
	}

	public String getStartNpcFtr() {
		return startNpcFtr;
	}

	public void setStartNpcFtr(String startNpcFtr) {
		this.startNpcFtr = startNpcFtr;
	}

	public int getStartX() {
		return startX;
	}

	public void setStartX(int startX) {
		this.startX = startX;
	}

	public int getStartY() {
		return startY;
	}

	public void setStartY(int startY) {
		this.startY = startY;
	}

	public String getShortdes() {
		return shortdes;
	}

	public void setShortdes(String shortdes) {
		this.shortdes = shortdes;
	}

	public String getShortdesFtr() {
		return shortdesFtr;
	}

	public void setShortdesFtr(String shortdesFtr) {
		this.shortdesFtr = shortdesFtr;
	}

	public String getDetaildes() {
		return detaildes;
	}

	public void setDetaildes(String detaildes) {
		this.detaildes = detaildes;
	}

	public String getDetaildesFtr() {
		return detaildesFtr;
	}

	public void setDetaildesFtr(String detaildesFtr) {
		this.detaildesFtr = detaildesFtr;
	}

	public int getActivityAdd() {
		return activityAdd;
	}

	public void setActivityAdd(int activityAdd) {
		this.activityAdd = activityAdd;
	}

	public int getLevelLimit() {
		return levelLimit;
	}

	public void setLevelLimit(int levelLimit) {
		this.levelLimit = levelLimit;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public int getArticle() {
		return article;
	}

	public void setArticle(int article) {
		this.article = article;
	}

	public int getEquipment() {
		return equipment;
	}

	public void setEquipment(int equipment) {
		this.equipment = equipment;
	}

	public int getTimesLimit() {
		return timesLimit;
	}

	public void setTimesLimit(int timesLimit) {
		this.timesLimit = timesLimit;
	}


	public ActivenessType getType() {
		return type;
	}

	public void setType(ActivenessType type) {
		this.type = type;
	}

	public int getTili() {
		return tili;
	}


	public void setTili(int tili) {
		this.tili = tili;
	}


	@Override
	public String toString() {
		return "ActivityForActiveness [activityAdd=" + activityAdd + ", article=" + article + ", detaildes=" + detaildes + ", detaildesFtr=" + detaildesFtr + ", equipment=" + equipment + ", exp=" + exp + ", id=" + id + ", levelLimit=" + levelLimit + ", name=" + name + ", nameFtr=" + nameFtr + ", shortdes=" + shortdes + ", shortdesFtr=" + shortdesFtr + ", startGame=" + startGame + ", startNpc=" + startNpc + ", startNpcFtr=" + startNpcFtr + ", startX=" + startX + ", startY=" + startY + ", tili=" + tili + ", timesLimit=" + timesLimit + ", type=" + type + "]";
	}


}