package com.fy.engineserver.septbuilding.service;

import java.util.HashMap;

import com.fy.engineserver.septbuilding.templet.SeptBuildingTemplet;
import com.fy.engineserver.septbuilding.templet.SeptBuildingTemplet.BuildingType;
import com.fy.engineserver.util.ServiceStartRecord;

/*
 * 家族建筑模板管理
 */
public class SeptBuildingManager {

	private static SeptBuildingManager self;

	public static SeptBuildingManager getInstance() {
		return self;
	}

	private String res_base_path;

	public String getRes_base_path() {
		return res_base_path;
	}

	public void setRes_base_path(String res_base_path) {
		this.res_base_path = res_base_path;
	}

	public static HashMap<BuildingType, SeptBuildingTemplet> type_templet_map = new HashMap<BuildingType, SeptBuildingTemplet>();

	/**
	 * 通过索引获得建筑类型
	 * 2011-4-25
	 * 
	 * @param index
	 * @return BuildingType
	 * 
	 */
	public static BuildingType getBuildingTypeByIndex(int index) {
		for (BuildingType buildingType : BuildingType.values()) {
			if (buildingType.getIndex() == index) {
				return buildingType;
			}
		}
		throw new IllegalStateException("[SeptBuildingManager] 非法调用 [getBuildingTypeByIndex] index = " + index);
	}

	public static SeptBuildingTemplet getTemplet(int index) {
		return type_templet_map.get(getBuildingTypeByIndex(index));
	}

	/**
	 * 通过类型得到模板类
	 * 2011-4-25
	 * 
	 * @param buildingType
	 * @return SeptBuilding
	 * 
	 */
	public static SeptBuildingTemplet getTemplet(BuildingType buildingType) {
		return type_templet_map.get(buildingType);
	}

	/**
	 * 加载模板资源
	 * 2011-4-25 void
	 * 
	 * 
	 */
	public void init() {
		
		self = this;
		ServiceStartRecord.startLog(this);
	}
}
