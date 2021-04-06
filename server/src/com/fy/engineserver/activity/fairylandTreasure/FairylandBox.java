package com.fy.engineserver.activity.fairylandTreasure;

public class FairylandBox {
	private int npcId;
	private String boxName;
	private String boxNameStat;
	private int boxType; // 宝箱类型
	private long needSilver; // 没有钥匙的情况下开启宝箱需要的银两,单位文
	private int num; // 每次刷新产生的个数
	private int[] rates; // 各种类型的概率(顺序:人物经验,神兽碎片,宠物经验,道具)
	private int[] prayRates; // 祈福对各种类型增加的概率(顺序:人物经验,神兽碎片,宠物经验,道具)
	private String boxKeyName; // 开箱子对应的钥匙名

	public FairylandBox(int npcId, String boxName, String boxNameStat, int boxType, long needSilver, int num, int[] rates, int[] prayRates, String boxKeyName) {
		this.npcId = npcId;
		this.boxName = boxName;
		this.boxNameStat = boxNameStat;
		this.boxType = boxType;
		this.needSilver = needSilver;
		this.num = num;
		this.rates = rates;
		this.prayRates = prayRates;
		this.boxKeyName = boxKeyName;
	}

	public int getNpcId() {
		return npcId;
	}

	public void setNpcId(int npcId) {
		this.npcId = npcId;
	}

	public String getBoxName() {
		return boxName;
	}

	public void setBoxName(String boxName) {
		this.boxName = boxName;
	}

	public String getBoxNameStat() {
		return boxNameStat;
	}

	public void setBoxNameStat(String boxNameStat) {
		this.boxNameStat = boxNameStat;
	}

	public int getBoxType() {
		return boxType;
	}

	public void setBoxType(int boxType) {
		this.boxType = boxType;
	}

	public long getNeedSilver() {
		return needSilver;
	}

	public void setNeedSilver(long needSilver) {
		this.needSilver = needSilver;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int[] getRates() {
		return rates;
	}

	public void setRates(int[] rates) {
		this.rates = rates;
	}

	public int[] getPrayRates() {
		return prayRates;
	}

	public void setPrayRates(int[] prayRates) {
		this.prayRates = prayRates;
	}

	public String getBoxKeyName() {
		return boxKeyName;
	}

	public void setBoxKeyName(String boxKeyName) {
		this.boxKeyName = boxKeyName;
	}

}
