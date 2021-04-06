package com.fy.engineserver.society;

public class Player_RelatinInfo {
	
	private byte relationShip;
	private long id;
	private String icon;
	private String name;
	private boolean isonline;
	private String mood;
	private byte career;
	private int tx_gamelevel = -1;
	
	public Player_RelatinInfo() {
		super();
	}
	public Player_RelatinInfo(byte relationShip, long id,byte career, String icon,
			String name, boolean online, String mood) {
		super();
		this.relationShip = relationShip;
		this.id = id;
		this.career = career;
		this.icon = icon;
		this.name = name;
		this.isonline = online;
		this.mood = mood;
	}
	
	public Player_RelatinInfo(byte relationShip, long id,byte career, String icon,
			String name, boolean online, String mood, int tx_gamelevel) {
		super();
		this.relationShip = relationShip;
		this.id = id;
		this.career = career;
		this.icon = icon;
		this.name = name;
		this.isonline = online;
		this.mood = mood;
		this.tx_gamelevel = tx_gamelevel;
	}
	
	public byte getRelationShip() {
		return relationShip;
	}
	public void setRelationShip(byte relationShip) {
		this.relationShip = relationShip;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public boolean isIsonline() {
		return isonline;
	}
	public void setIsonline(boolean isonline) {
		this.isonline = isonline;
	}
	public String getMood() {
		return mood;
	}
	public void setMood(String mood) {
		this.mood = mood;
	}
	public byte getCareer() {
		return career;
	}
	public void setCareer(byte career) {
		this.career = career;
	}
	public void setTx_gamelevel(int tx_gamelevel) {
		this.tx_gamelevel = tx_gamelevel;
	}
	public int getTx_gamelevel() {
		return tx_gamelevel;
	}

}
