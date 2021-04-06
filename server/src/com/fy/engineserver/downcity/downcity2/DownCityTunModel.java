package com.fy.engineserver.downcity.downcity2;

import java.util.ArrayList;
import java.util.List;

public class DownCityTunModel {
	/** 副本地图名 */
	private String mapName;
	/** 物品列表 */
	private List<PropModel> props = new ArrayList<PropModel>();
	
	@Override
	public String toString() {
		return "DownCityTunModel [mapName=" + mapName + ", props=" + props + "]";
	}
	public String getMapName() {
		return mapName;
	}
	public void setMapName(String mapName) {
		this.mapName = mapName;
	}
	public List<PropModel> getProps() {
		return props;
	}
	public void setProps(List<PropModel> props) {
		this.props = props;
	}
	
	
}
