package com.fy.engineserver.datasource.article.data.magicweapon.model;

import com.fy.engineserver.datasource.article.data.magicweapon.MagicWeaponConstant;

/**
 * 隐藏属性随机概率
 * @author Administrator
 *
 */
public class HiddenRanProbModel {
	/** 附加属性类型，需要转换成int使用 */
	private String attrStr;
	/** 得到概率 */
	private int probabbly;
	
	@Override
	public String toString() {
		return "HiddenRanProbModel [attrStr=" + attrStr + ", probabbly=" + probabbly + "]";
	}
	/**
	 * 属性类型（气血等）
	 * @return
	 * @throws Exception 
	 */
	public int getAttrType() throws Exception {
		int result = -1;
		if(MagicWeaponConstant.mappingValueKey.get(attrStr) != null) {
			result = MagicWeaponConstant.mappingValueKey.get(attrStr);
		} else {
			throw new Exception("[没有找到mapping映射] [" + attrStr + "]");
		}
		return result;
	}
	
	public void setAttrStr(String attrStr) {
		this.attrStr = attrStr;
	}
	public int getProbabbly() {
		return probabbly;
	}
	public void setProbabbly(int probabbly) {
		this.probabbly = probabbly;
	}
	
	
}
