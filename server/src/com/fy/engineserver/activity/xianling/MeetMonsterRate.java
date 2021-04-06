package com.fy.engineserver.activity.xianling;

import com.fy.engineserver.util.SimpleKey;

public class MeetMonsterRate {
	@SimpleKey
	private int buffId;
	private String name;
	private String des;
	private int rate;
	private String icon;
	private int[] meetMonsterRates;

	public MeetMonsterRate() {
	}

	public MeetMonsterRate(int buffId, String name, String des, int rate, String icon, int[] meetMonsterRates) {
		this.buffId = buffId;
		this.name = name;
		this.des = des;
		this.rate = rate;
		this.icon = icon;
		this.meetMonsterRates = meetMonsterRates;
	}

	public int getBuffId() {
		return buffId;
	}

	public void setBuffId(int buffId) {
		this.buffId = buffId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public int[] getMeetMonsterRates() {
		return meetMonsterRates;
	}

	public void setMeetMonsterRates(int[] meetMonsterRates) {
		this.meetMonsterRates = meetMonsterRates;
	}

}
