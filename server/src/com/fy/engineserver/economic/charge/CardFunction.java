package com.fy.engineserver.economic.charge;

public enum CardFunction {

	开放离线摆摊功能(1,"开放离线摆摊功能"),//
	提升绑银使用上限1锭(2,"提升绑银使用上限1锭"),//
	每日免费进出福地洞天(3,"每日免费进出福地洞天"),//
	每日绑银500两(4,"每日绑银50两"),//
	每日2张彩世仙符(5,"每日2张彩世仙符"),//
	每日可购买一个3元专属礼包(6,"每日可购买一个3元专属礼包"),//
	坐骑进阶形象额外加1(7,"坐骑进阶形象额外加1"),//
	每日免费一次圣兽阁普通(8,"每日免费一次圣兽阁普通"),//
	每日免费原地复活5次(9,"每日免费原地复活5次"),
	每日彩世飘花一次(10,"每日彩世飘花一次"),//
	每日蓝色封魔录两张(11,"每日蓝色封魔录两张"),//
	每日可购买一个6元专属礼包(12,"每日可购买一个6元专属礼包"),//
	副本百分200掉落次数每周增加5次(13,"每周增加5次副本次数"),
	世界boss不死buff(14,"全民boss不死buff"),//
	每日蓝酒两瓶(15,"每日蓝酒两瓶"),//
	每日体力丹3颗(16,"每日活力丹3颗"),//
	每日常青草2颗(17,"每日坐骑培养膏2颗"),//
	仙兽房增加一个挂机位(18,"仙兽房增加一个挂机位");//

	private int id;
	private String nameString;
	
	private CardFunction(int id,String name){
		this.id = id;
		this.nameString = name;
	}
	
	public int getId(){
		return id;
	}
	public String getName(){
		return nameString;
	}
}
