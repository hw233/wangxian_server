package com.fy.engineserver.tournament.data;

import java.util.ArrayList;

import com.fy.engineserver.tournament.manager.TournamentManager;
import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

/**
 * 比武数据
 * 
 *
 */
@SimpleEntity
public class TournamentData {
	
	@SimpleId
	public long id = 1;
	
	@SimpleVersion
	protected int version2;

	/**
	 * 当前报名表
	 */
	@SimpleColumn(length=500000)
	public ArrayList<Long> currentSignUpList = new ArrayList<Long>();

	public ArrayList<Long> getCurrentSignUpList() {
		return currentSignUpList;
	}

	public void setCurrentSignUpList(ArrayList<Long> currentSignUpList) {
		this.currentSignUpList = currentSignUpList;
		setProperty("currentSignUpList");
	}

	public void setProperty(String property){
		TournamentManager tm = TournamentManager.getInstance();
		if(tm != null && tm.em != null){
			tm.em.notifyFieldChange(this, property);
		}
	}
}
