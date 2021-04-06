package com.fy.engineserver.sprite.pet2;

import java.util.Arrays;

import org.apache.commons.lang.ArrayUtils;

/**
 * 可进阶的宠物的列表
 * 
 * create on 2013年8月15日
 */
public class GradePet {
	public String name;
	/**
	 * 统计名称。
	 */
	public String progName;
	public String icon;
	public int maxGrade;
	public String lv4Avatar;
	public String lv7Avatar;
	public String lv4Icon;
	public String lv7Icon;
	public String baseAvatar;
	//
	public int[] bornSkill;
	public String[] icons;
	public String[] skDesc;
	public String jiChuJiNengDesc;
	public String tianFuJiNengDesc;
	public String gainFrom;
	public int character;
	public int takeLevel;
	public int rarity;
	public int[] minValues;
	public int[] maxValues;
	/**
	 * 每一阶的缩放，从0开始，0表示2阶，值为物品表里的配置。
	 */
	public int[] scaleArr;
	public String[] partBody;
	public int[] partBodyY;
	public String[] partFoot;
	public int[] partFootY;
	/**
	 * 1代表可飞升
	 */
	public int flyType;
	public String flyAvata;
	public String flyIcon;
	
	public GradePet(){
		minValues = maxValues = new int[5];
		scaleArr = new int[7];//2~8
		
		partBody = new String[9];	Arrays.fill(partBody, "");
		partBodyY = new int[9];	//0~8	

		partFootY = new int[9];	//0~8
		partFoot = new String[9];			Arrays.fill(partFoot, "");
		
		Arrays.fill(scaleArr, 1000);
	}
	
	public String getFlyAvata() {
		return flyAvata;
	}

	public void setFlyAvata(String flyAvata) {
		this.flyAvata = flyAvata;
	}

	//用于显示属性的计算。
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getMaxGrade() {
		return maxGrade;
	}
	public void setMaxGrade(int maxGrade) {
		this.maxGrade = maxGrade;
	}
	public String getLv4Avatar() {
		return lv4Avatar;
	}
	public void setLv4Avatar(String lv4Avatar) {
		this.lv4Avatar = lv4Avatar;
	}
	public String getLv7Avatar() {
		return lv7Avatar;
	}
	public void setLv7Avatar(String lv7Avatar) {
		this.lv7Avatar = lv7Avatar;
	}
	public int[] getBornSkill() {
		return bornSkill;
	}
	public void setBornSkill(int[] bornSkill) {
		this.bornSkill = bornSkill;
	}
	public String getGainFrom() {
		return gainFrom;
	}
	public void setGainFrom(String gainFrom) {
		this.gainFrom = gainFrom;
	}
	public String getProgName() {
		return progName;
	}
	public void setProgName(String progName) {
		this.progName = progName;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String[] getIcons() {
		return icons;
	}
	public void setIcons(String[] icons) {
		this.icons = icons;
	}
	public String getJiChuJiNengDesc() {
		return jiChuJiNengDesc;
	}
	public void setJiChuJiNengDesc(String jiChuJiNengDesc) {
		this.jiChuJiNengDesc = jiChuJiNengDesc;
	}
	public String getTianFuJiNengDesc() {
		return tianFuJiNengDesc;
	}
	public void setTianFuJiNengDesc(String tianFuJiNengDesc) {
		this.tianFuJiNengDesc = tianFuJiNengDesc;
	}
	public String[] getSkDesc() {
		return skDesc;
	}
	public void setSkDesc(String[] sdDesc) {
		this.skDesc = sdDesc;
	}
	public String getBaseAvatar() {
		return baseAvatar;
	}
	public void setBaseAvatar(String baseAvatar) {
		this.baseAvatar = baseAvatar;
	}
	public int getCharacter() {
		return character;
	}
	public void setCharacter(int character) {
		this.character = character;
	}
	public int getTakeLevel() {
		return takeLevel;
	}
	public void setTakeLevel(int takeLevel) {
		this.takeLevel = takeLevel;
	}
	public int getRarity() {
		return rarity;
	}
	public void setRarity(int rarity) {
		this.rarity = rarity;
	}
	public int[] getMinValues() {
		return minValues;
	}
	public void setMinValues(int[] minValues) {
		this.minValues = minValues;
	}
	public int[] getMaxValues() {
		return maxValues;
	}
	public void setMaxValues(int[] maxValues) {
		this.maxValues = maxValues;
	}
	public String[] getPartBody() {
		return partBody;
	}
	public void setPartBody(String[] partBody) {
		this.partBody = partBody;
	}
	public int[] getPartBodyY() {
		return partBodyY;
	}
	public void setPartBodyY(int[] partBodyY) {
		this.partBodyY = partBodyY;
	}
	public String[] getPartFoot() {
		return partFoot;
	}
	public void setPartFoot(String[] partFoot) {
		this.partFoot = partFoot;
	}
	public int[] getPartFootY() {
		return partFootY;
	}
	public void setPartFootY(int[] partFootY) {
		this.partFootY = partFootY;
	}
	public int getFlyType() {
		return flyType;
	}
	public void setFlyType(int flyType) {
		this.flyType = flyType;
	}
	
}
