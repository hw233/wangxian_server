package com.fy.engineserver.datasource.article.data.magicweapon.model;

import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 法宝属性配置
 * @author Administrator
 *
 */
@SimpleEmbeddable
public class MagicWeaponAttrModel {
	/** 附加属性类型(气血等)----附加技能特殊，存储的是技能id */
	private int id;
	/** 具体数值---次数会产生随机数，需要存库 */
	private int attrNum;
	/** 属性增加类型（增加具体数值、人物属性百分比、法宝基础属性百分比） */
	private int addType = 0;
	/** 属性是否生效 (基础属性、隐藏只要存在就生效。。附加技能为法宝等级激活)*/
	private transient boolean isAct = false;
	/**  基础属性(强化等级所加属性) ---  附加技能（增加法宝自身属性百分比计算出来的数值）*/
	private transient int extraAddAttr;
	/** 属性描述 */
	private transient String descraption;
	/** 颜色index */
	private transient int colorType;
	/** 具体数值，不存库，通过计算获得 (法宝增加的属性数值使用此值)*/
	private transient int newAttrNum;
	
	public MagicWeaponAttrModel() {
		super();
	}
	
	@Override
	public String toString() {
		return "MagicWeaponAttrModel [id=" + id + ", attrNum=" + attrNum + ", addType=" + addType + ", newAttrNum=" + newAttrNum + "]";
	}

	public MagicWeaponAttrModel(int id, int attrNum, int addType, String descraption) {
		this.id = id;
		this.attrNum = attrNum;
		this.descraption = descraption;
		this.addType = addType;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAttrNum() {
		return attrNum;
	}
	public void setAttrNum(int attrNum) {
		this.attrNum = attrNum;
	}
	public boolean isAct() {
		return isAct;
	}
	public void setAct(boolean isAct) {
		this.isAct = isAct;
	}
	public String getDescraption() {
		return descraption;
	}
	public void setDescraption(String descraption) {
		this.descraption = descraption;
	}
	public int getExtraAddAttr() {
		return extraAddAttr;
	}
	public void setExtraAddAttr(int extraAddAttr) {
		this.extraAddAttr = extraAddAttr;
	}


	public int getAddType() {
		return addType;
	}


	public void setAddType(int addType) {
		this.addType = addType;
	}



	public int getColorType() {
		return colorType;
	}



	public void setColorType(int colorType) {
		this.colorType = colorType;
	}



	public int getNewAttrNum() {
		return newAttrNum;
	}



	public void setNewAttrNum(int newAttrNum) {
		this.newAttrNum = newAttrNum;
	}
}
