package com.fy.engineserver.datasource.article.data.magicweapon.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 法宝吞噬升级相关配置---被吞噬获取经验写死计算公式
 * @author Administrator
 *
 */
public class DevourModel {
	/** 法宝当前等级 */
	private int level;				
	/** 升级 吞噬消耗相关配置 */
	private List<Color4DevourModel> colorList = new ArrayList<Color4DevourModel>();
	
	public void addColorList(Color4DevourModel cm) {
		if(!colorList.contains(cm)) {
			colorList.add(cm);
		}
	}
	
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public List<Color4DevourModel> getColorList() {
		return colorList;
	}

}
