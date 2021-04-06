package com.fy.engineserver.septbuilding.service;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.septbuilding.templet.SeptBuildingTemplet;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 建筑状态
 * 
 * 
 */
@SimpleEmbeddable
public class BuildingState {

	private transient SeptBuildingTemplet templetType;
	private int level;

	public BuildingState() {

	}

	public BuildingState(SeptBuildingTemplet templetType, int level) {
		this.templetType = templetType;
		this.level = level;
	}

	public SeptBuildingTemplet getTempletType() {
		return templetType;
	}

	public void setTempletType(SeptBuildingTemplet type) {
		this.templetType = type;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	@Override
	public String toString() {
		return templetType.getBuildingType().getName() + Translate.text_6337 + level + Translate.text_6338;
	}
}
