package com.fy.engineserver.marriage;

/**
 * 婚礼级别
 * 
 *
 */
public class MarriageLevel {

	private String name;
	
	private int playerNum;				//可邀请人数
	
	private int needMoneyType;				//需要金钱类型
	private int needMoney;					//需要金钱
	
	private int rewardsNum;					//给嘉宾奖励次数
	
	private String[] propName;				//奖励物品
	private int[] propNum;					//奖励数目
	private String icon;					//图标
	
	private String cityName;				//场景名称
	private String homeName;				//场景名称
	
	private String nanRing;					//男的戒指
	private String nvRing;					//女的戒指
	private String[] equpmentRing;			//给的装备戒指，按职业
	private int colorType;					//颜色
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPlayerNum() {
		return playerNum;
	}
	public void setPlayerNum(int playerNum) {
		this.playerNum = playerNum;
	}
	public int getNeedMoneyType() {
		return needMoneyType;
	}
	public void setNeedMoneyType(int needMoneyType) {
		this.needMoneyType = needMoneyType;
	}
	public int getNeedMoney() {
		return needMoney;
	}
	public void setNeedMoney(int needMoney) {
		this.needMoney = needMoney;
	}
	public int getRewardsNum() {
		return rewardsNum;
	}
	public void setRewardsNum(int rewardsNum) {
		this.rewardsNum = rewardsNum;
	}
	public String[] getPropName() {
		return propName;
	}
	public void setPropName(String[] propName) {
		this.propName = propName;
	}
	public int[] getPropNum() {
		return propNum;
	}
	public void setPropNum(int[] propNum) {
		this.propNum = propNum;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getIcon() {
		return icon;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getCityName() {
		return cityName;
	}
	public void setHomeName(String homeName) {
		this.homeName = homeName;
	}
	public String getHomeName() {
		return homeName;
	}
	public void setNanRing(String nanRing) {
		this.nanRing = nanRing;
	}
	public String getNanRing() {
		return nanRing;
	}
	public void setNvRing(String nvRing) {
		this.nvRing = nvRing;
	}
	public String getNvRing() {
		return nvRing;
	}
	public void setEqupmentRing(String[] equpmentRing) {
		this.equpmentRing = equpmentRing;
	}
	public String[] getEqupmentRing() {
		return equpmentRing;
	}
	public void setColorType(int colorType) {
		this.colorType = colorType;
	}
	public int getColorType() {
		return colorType;
	}
	
	@Override
	public String toString() {
		return name+"-"+playerNum+"-"+nanRing+"~"+nvRing+"~";
	}
}
