package com.fy.engineserver.carbon.devilSquare.model;

public class DevelSquareBaseConf {
	private int level;
	
	private String plays;
	
	private String bcstorys;
	
	private long costprops;
	
	private int costNum;
	
	private long[] dropprops;
	/** 掉率 */
	private String[] dropprobabbly;
	
	private String mapName;

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getPlays() {
		return plays;
	}

	public void setPlays(String plays) {
		this.plays = plays;
	}

	public String getBcstorys() {
		return bcstorys;
	}

	public void setBcstorys(String bcstorys) {
		this.bcstorys = bcstorys;
	}

	public long getCostprops() {
		return costprops;
	}

	public void setCostprops(long costprops) {
		this.costprops = costprops;
	}

	public int getCostNum() {
		return costNum;
	}

	public void setCostNum(int costNum) {
		this.costNum = costNum;
	}

	public long[] getDropprops() {
		return dropprops;
	}

	public void setDropprops(long[] dropprops) {
		this.dropprops = dropprops;
	}

	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	public String[] getDropprobabbly() {
		return dropprobabbly;
	}

	public void setDropprobabbly(String[] dropprobabbly) {
		this.dropprobabbly = dropprobabbly;
	}
	
	
}
