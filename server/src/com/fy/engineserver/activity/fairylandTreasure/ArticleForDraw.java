package com.fy.engineserver.activity.fairylandTreasure;

public class ArticleForDraw {
	private int id;
	private long tempArticleId;
	private String name;
	private String nameStat;
	private int color;
	private int num;
	private boolean bind;
	private boolean worth;
	private boolean broadcast;
	private int[] rates = new int[3];

	public ArticleForDraw(int id, String name, String nameStat, int color, int num, boolean bind, boolean worth, boolean broadcast, int[] rates) {
		super();
		this.id = id;
		this.name = name;
		this.nameStat = nameStat;
		this.color = color;
		this.num = num;
		this.bind = bind;
		this.worth = worth;
		this.broadcast = broadcast;
		this.rates = rates;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getTempArticleId() {
		return tempArticleId;
	}

	public void setTempArticleId(long tempArticleId) {
		this.tempArticleId = tempArticleId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameStat() {
		return nameStat;
	}

	public void setNameStat(String nameStat) {
		this.nameStat = nameStat;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public boolean isBind() {
		return bind;
	}

	public void setBind(boolean bind) {
		this.bind = bind;
	}

	public boolean isWorth() {
		return worth;
	}

	public void setWorth(boolean worth) {
		this.worth = worth;
	}

	public boolean isBroadcast() {
		return broadcast;
	}

	public void setBroadcast(boolean broadcast) {
		this.broadcast = broadcast;
	}

	public int[] getRates() {
		return rates;
	}

	public void setRates(int[] rates) {
		this.rates = rates;
	}

}
