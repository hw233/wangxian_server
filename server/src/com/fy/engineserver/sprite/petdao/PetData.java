package com.fy.engineserver.sprite.petdao;

public class PetData {
	
	private int level;			//等级
	
	private int daotype;		//迷城类型(0:普通  1:豪华  2:至尊)
	
	private String petname[];	//宠物名
	
	private String petnames[];	//客户端图鉴需要的
	
	private String iconname[];	//宠物icon名字，一一对应宠物名
	
	private long costsilver;	//进入所花费银子数
	
	private String desc;		//描述

	private String mapname;		//地图
	
	private boolean isopen;		//当前副本是否开放
	
	public int getDaotype() {
		return daotype;
	}

	public String[] getPetnames() {
		return petnames;
	}

	public void setPetnames(String[] petnames) {
		this.petnames = petnames;
	}

	public void setDaotype(int daotype) {
		this.daotype = daotype;
	}

	public String[] getPetname() {
		return petname;
	}

	public void setPetname(String[] petname) {
		this.petname = petname;
	}

	public long getCostsilver() {
		return costsilver;
	}

	public void setCostsilver(long costsilver) {
		this.costsilver = costsilver;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getMapname() {
		return mapname;
	}

	public void setMapname(String mapname) {
		this.mapname = mapname;
	}

	public String[] getIconname() {
		return iconname;
	}

	public void setIconname(String[] iconname) {
		this.iconname = iconname;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public boolean isIsopen() {
		return isopen;
	}

	public void setIsopen(boolean isopen) {
		this.isopen = isopen;
	}

}
