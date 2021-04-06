package com.fy.engineserver.datasource.article.data.magicweapon.model;

import com.fy.engineserver.datasource.article.data.magicweapon.MagicWeaponConstant;
import com.fy.engineserver.datasource.article.data.magicweapon.MagicWeaponManager;
/**
 * 附加技能配置
 * @author Administrator
 *
 */
public class AdditiveAttrModel {
	/** 索引id */
	private int id;
	/** 增加属性类型 */
	private String attrType;
	/** 具体属性值 */
	private int attrNum;
	/** 自动激活需要法宝等级 */
	private int needLevel;
	/** 技能描述 */
	private String descreption;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setAttrType(String attrType) {
		this.attrType = attrType;
	}
	public String getAttrType() {
		return this.attrType;
	}
	public int getAttrNum() {
		return this.attrNum;
	}
	public void setAttrNum(int attrNum) {
		this.attrNum = attrNum;
	}
	public int getNeedLevel() {
		return needLevel;
	}
	public void setNeedLevel(int needLevel) {
		this.needLevel = needLevel;
	}
	/**
	 * 获取增加属性类型（具体见magicweaponconstant）
	 * @return
	 */
	public int getBaseAttrNumByIndex() {
		if(MagicWeaponConstant.mappingValueKey.get(attrType) != null) {
			return MagicWeaponConstant.mappingValueKey.get(attrType);
		} else {
			MagicWeaponManager.logger.error("[法宝获取增加类型错误] [attrType:" + attrType + "]");
		}
		return 0;
	}
	
	/**
	 * 获取添加属性加持类型
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public static int getAttrAddType(int type) throws Exception {
		int result = -1;
		boolean flag = true;
		String tempType = MagicWeaponConstant.mappingKeyValue.get(type);
		for(String str : MagicWeaponConstant.addtiveSkill) {
			if(str.equals(tempType)) {
				result = MagicWeaponConstant.add_typePercent4MagicWeapon;
				flag = false;
				break;
			}
		}
		if(flag) {
			result = MagicWeaponConstant.add_typePercent4Person;
		}
		return result;
	}
	
	/**
	 * 根据类型
	 * @param type
	 * @return  result[0] = 增加类型    result[1]=具体数值（需要做随机）
	 * @throws Exception 
	 */
	public int[] getAttrNumByType(int type) throws Exception {
		int[] result = new int[2];
		boolean flag = true;
		for(String str : MagicWeaponConstant.addtiveSkill) {
			if(str.equals(attrType)) {
				result[0] = MagicWeaponConstant.add_typePercent4MagicWeapon;
				flag = false;
				break;
			}
		}
		if(flag) {
			result[0] = MagicWeaponConstant.add_typePercent4Person;
		}
		result[1] = this.attrNum;
		return result;
	}
	
	
	public String getDescreption() {
		return descreption;
	}
	public void setDescreption(String descreption) {
		this.descreption = descreption;
	}
	@Override
	public String toString() {
		return "AdditiveAttrModel [id=" + id + ", attrType=" + attrType + ", attrNum=" + attrNum + ", needLevel=" + needLevel + ", descreption=" + descreption + "]";
	}
	
}
