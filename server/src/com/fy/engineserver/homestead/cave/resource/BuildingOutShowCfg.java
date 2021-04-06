package com.fy.engineserver.homestead.cave.resource;

/**
 * 建筑形象配置
 * 
 * 
 */
public class BuildingOutShowCfg {

	private int defaultNpcId;

	public BuildingOutShowCfg() {

	}

	public BuildingOutShowCfg(int defaultNpcId) {
		super();
		this.defaultNpcId = defaultNpcId;
	}

	public int getDefaultNpcId() {
		return defaultNpcId;
	}

	public void setDefaultNpcId(int defaultNpcId) {
		this.defaultNpcId = defaultNpcId;
	}

	@Override
	public String toString() {
		return "BuildingOutShowCfg [defaultNpcId=" + defaultNpcId + "]";
	}

}
