package com.fy.engineserver.activity.xianling;

public class XianLingLevelData {
	private int level; // 关卡等级
	private String name; // 关卡名字
	private int type; // 关卡类型：0-普通，1-高级
	private String gameName; // 对应地图名
	private String[] monsterCategoryIds; // 关卡中可能出现的怪物
	private int[] rates; // 上述怪物出现的几率

	public XianLingLevelData() {
		super();
		// TODO Auto-generated constructor stub
	}

	public XianLingLevelData(int level, String name, int type, String gameName, String[] monsterCategoryIds, int[] rates) {
		super();
		this.level = level;
		this.name = name;
		this.type = type;
		this.gameName = gameName;
		this.monsterCategoryIds = monsterCategoryIds;
		this.rates = rates;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getMonsterCategoryIds() {
		return monsterCategoryIds;
	}

	public void setMonsterCategoryIds(String[] monsterCategoryIds) {
		this.monsterCategoryIds = monsterCategoryIds;
	}

	public int[] getRates() {
		return rates;
	}

	public void setRates(int[] rates) {
		this.rates = rates;
	}

}
