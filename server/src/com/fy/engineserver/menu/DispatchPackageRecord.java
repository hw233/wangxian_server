package com.fy.engineserver.menu;

import java.io.Serializable;
import java.util.Date;
import java.util.Hashtable;
import java.util.Set;

public class DispatchPackageRecord implements Serializable{
	private static final long serialVersionUID = 1L;
	long playerId;
	Hashtable<String , Date> dailyRecords = new Hashtable<String, Date>();
	Hashtable<String , Set<Integer>> levelRecords = new Hashtable<String, Set<Integer>>();
	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
}
