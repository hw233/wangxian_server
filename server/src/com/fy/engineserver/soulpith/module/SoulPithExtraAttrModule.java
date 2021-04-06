package com.fy.engineserver.soulpith.module;

import java.util.Map;

import com.fy.engineserver.soulpith.SoulPithTypes;
import com.fy.engineserver.soulpith.property.AddPropertyTypes;
import com.fy.engineserver.soulpith.property.Propertys;
import com.fy.engineserver.util.NeedBuildExtraData;

/**
 * 灵髓属性
 * 
 * @date on create 2016年3月16日 下午3:01:51
 */
public class SoulPithExtraAttrModule implements NeedBuildExtraData{
	/** 灵根名 */
	private String name;
	/** 激活此属性需要的灵髓类型 */
	private int[] soulPithTypes;
	/** 对应需要的灵髓点数 */
	private int[] needSoulNum;
	/** 增加类型(数值或百分比) */
	private int[] addTypes;
	/** 增加属性类型 */
	private int[] attrTypes;
	/** 对应数值 */
	private int[] attrNums;
	/** 属性描述 */
	private String[] attrDes;
	
	@Override
	public void buildExtraData() throws Exception {
		// TODO Auto-generated method stub
		attrDes = new String[attrTypes.length];
		for (int i=0; i<attrTypes.length; i++) {
			String temp = attrNums[i] + "";
			if (addTypes[i] == AddPropertyTypes.ADD_C_NUM.getIndex()) {
				temp = ((float)attrNums[i] / 100f) + "%";
			}
			attrDes[i] = Propertys.valueOf(attrTypes[i]).getName() + " +" + temp;
		}
	}
	
	/**
	 * 此属性是否激活
	 * @param soulNums
	 * @return
	 */
	public boolean canAdd(Map<SoulPithTypes, Integer> soulNums) {
		for (int i=0; i<soulPithTypes.length; i++) {
			SoulPithTypes t = SoulPithTypes.valueOf(soulPithTypes[i]);
			if (!soulNums.containsKey(t)) {
				return false;
			}
			int a = soulNums.get(t);
			if (a < needSoulNum[i]) {
				return false;
			}
		}
		return true;
	}
	
	public boolean canAdd(int[] soulNums) {
		for (int i=0; i<soulPithTypes.length; i++) {
			if (soulNums[soulPithTypes[i]] < needSoulNum[i]) {
				return false;
			}
		}
		return true;
	}
	
	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String[] getAttrDes() {
		return attrDes;
	}



	public void setAttrDes(String[] attrDes) {
		this.attrDes = attrDes;
	}



	public int[] getNeedSoulNum() {
		return needSoulNum;
	}
	public void setNeedSoulNum(int[] needSoulNum) {
		this.needSoulNum = needSoulNum;
	}
	public int[] getAddTypes() {
		return addTypes;
	}
	public void setAddTypes(int[] addTypes) {
		this.addTypes = addTypes;
	}
	public int[] getAttrTypes() {
		return attrTypes;
	}
	public void setAttrTypes(int[] attrTypes) {
		this.attrTypes = attrTypes;
	}
	public int[] getAttrNums() {
		return attrNums;
	}
	public void setAttrNums(int[] attrNums) {
		this.attrNums = attrNums;
	}

	public int[] getSoulPithTypes() {
		return soulPithTypes;
	}

	public void setSoulPithTypes(int[] soulPithTypes) {
		this.soulPithTypes = soulPithTypes;
	}
}
