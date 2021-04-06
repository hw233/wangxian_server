package com.fy.engineserver.articleEnchant.model;

import java.util.Arrays;

public class EnchantModel {
	/** 配表id */
	private int id;
	/** 附魔名称 */
	private String enchatName;
	/** 附魔装备等级限制 */
	private int equiptLevelLimit;
	/** 初始耐久度 */
	private int durable;
	/** 增加属性类型(1数值，类似于x的   2被动触发buff) */
	private byte type;
	/** 被动触发类型（1主动攻击   2受到控制类技能  3被攻击） */
	private byte passiveType;
	/** 对应可附魔装备类型 */
	private int equiptmentType;
	/** 增加属性类型 1：外攻
	2：内攻
	3：气血
	4：暴击
	5：命中
	6：精准
	7：破甲 */
	private int[] addAttrTypes;
	/** 分档随机概率 */
	private int[] firstProb;
	/** 数值上下限（分档位，数值应比概率多一位） */
	private int[] attrNums;
	/** 概率类型   1 配表概率   2数据库记录概率 */
	private int probType;
	/** 被动触发几率 */
	private int probabbly;
	/** 触发buff名 */
	private String buffName;
	/** buff等级 */
	private int buffLevel;
	/** buff持续时间 */
	private long lastTime;
	/** 描述 */
	private String des;
	/** 内置cd */
	private long cd;
	/** 颜色(拼泡泡用) */
	private int colorType;
	
	@Override
	public String toString() {
		return "EnchantModel [id=" + id + ", enchatName=" + enchatName + ", equiptLevelLimit=" + equiptLevelLimit + ", durable=" + durable + ", type=" + type + ", passiveType=" + passiveType + ", equiptmentType=" + equiptmentType + ", addAttrTypes=" + Arrays.toString(addAttrTypes) + ", firstProb=" + Arrays.toString(firstProb) + ", attrNums=" + Arrays.toString(attrNums) + ", probType=" + probType + ", probabbly=" + probabbly + ", buffName=" + buffName + ", buffLevel=" + buffLevel + ", lastTime=" + lastTime + ", des=" + des + ", cd=" + cd + ", colorType=" + colorType + "]";
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getDurable() {
		return durable;
	}
	public void setDurable(int durable) {
		this.durable = durable;
	}
	public byte getType() {
		return type;
	}
	public void setType(byte type) {
		this.type = type;
	}
	public int[] getAttrNums() {
		return attrNums;
	}
	public void setAttrNums(int[] attrNums) {
		this.attrNums = attrNums;
	}
	public String getBuffName() {
		return buffName;
	}
	public void setBuffName(String buffName) {
		this.buffName = buffName;
	}
	public int getBuffLevel() {
		return buffLevel;
	}
	public void setBuffLevel(int buffLevel) {
		this.buffLevel = buffLevel;
	}
	public int getEquiptmentType() {
		return equiptmentType;
	}
	public void setEquiptmentType(int equiptmentType) {
		this.equiptmentType = equiptmentType;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	public int getProbabbly() {
		return probabbly;
	}
	public void setProbabbly(int probabbly) {
		this.probabbly = probabbly;
	}
	public int[] getAddAttrTypes() {
		return addAttrTypes;
	}
	public void setAddAttrTypes(int[] addAttrTypes) {
		this.addAttrTypes = addAttrTypes;
	}
	public long getLastTime() {
		return lastTime;
	}
	public void setLastTime(long lastTime) {
		this.lastTime = lastTime;
	}
	public byte getPassiveType() {
		return passiveType;
	}
	public void setPassiveType(byte passiveType) {
		this.passiveType = passiveType;
	}
	public int getEquiptLevelLimit() {
		return equiptLevelLimit;
	}
	public void setEquiptLevelLimit(int equiptLevelLimit) {
		this.equiptLevelLimit = equiptLevelLimit;
	}
	public int getProbType() {
		return probType;
	}
	public void setProbType(int probType) {
		this.probType = probType;
	}
	public int[] getFirstProb() {
		return firstProb;
	}
	public void setFirstProb(int[] firstProb) {
		this.firstProb = firstProb;
	}
	public String getEnchatName() {
		return enchatName;
	}
	public void setEnchatName(String enchatName) {
		this.enchatName = enchatName;
	}
	public long getCd() {
		return cd;
	}
	public void setCd(long cd) {
		this.cd = cd;
	}
	public int getColorType() {
		return colorType;
	}
	public void setColorType(int colorType) {
		this.colorType = colorType;
	}
	
	
}
