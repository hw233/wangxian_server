package com.fy.engineserver.datasource.article.data.magicweapon.model;

import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.datasource.article.data.magicweapon.MagicWeaponConstant;

/**
 * 基础属性model,随机name获取附加的属性类型
 *
 */
public class BasicAttrRanModel {
	/** 前缀名 */
	private String pre_name;
	/** 属性列表 */
	private List<Integer> attrList = new ArrayList<Integer>();
	/** 获得概率，万分比 */
	private int probabbly;
	
	public List<Integer> getAttrList() {
		return attrList;
	} 
	
	public void add2AttrList(String name) throws Exception {
		int index = this.getAttrIdByName(name);
		if(!attrList.contains(index)) {
			attrList.add(index);
		}
	}
	
	private int getAttrIdByName(String name) throws Exception {
		int result = -1;
		if(MagicWeaponConstant.mappingValueKey.get(name) != null) {
			result = MagicWeaponConstant.mappingValueKey.get(name);
		} else {
			throw new Exception("[没有找到mapping映射] [" + name + "]");
		}
		return result;
	}

	public String getPre_name() {
		return pre_name;
	}

	public void setPre_name(String pre_name) {
		this.pre_name = pre_name;
	}

	public int getProbabbly() {
		return probabbly;
	}

	public void setProbabbly(int probabbly) {
		this.probabbly = probabbly;
	}
}
