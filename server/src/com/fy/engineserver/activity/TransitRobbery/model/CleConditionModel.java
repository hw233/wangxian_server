package com.fy.engineserver.activity.TransitRobbery.model;

import java.util.Iterator;
import java.util.Map;

/**
 * 过关条件model
 *
 */
public class CleConditionModel {
	/** 天劫重数*/
	private int id;
	/** 小关层 */
	private Map<Integer, EachLevelDetal> levelDetails;
	
	public String toString(){
		String msg = "id" + id;
		Iterator<Integer> ite = levelDetails.keySet().iterator();
		while(ite.hasNext()){
			int key = ite.next();
			msg += "  level=" + key + "  detail=" + levelDetails.get(key);
		}
		return msg;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Map<Integer, EachLevelDetal> getLevelDetails() {
		return levelDetails;
	}
	public void setLevelDetails(Map<Integer, EachLevelDetal> levelDetails) {
		this.levelDetails = levelDetails;
	}
	
	
}
