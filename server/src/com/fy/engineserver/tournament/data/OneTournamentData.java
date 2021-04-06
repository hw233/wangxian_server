package com.fy.engineserver.tournament.data;

import com.fy.engineserver.tournament.manager.TournamentManager;
import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndex;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

/**
 * 比武数据
 * 
 *
 */
@SimpleEntity
@SimpleIndices({
	@SimpleIndex(name="OneTournamentData_C",members={"currentTournamentPoint"}),
	@SimpleIndex(name="OneTournamentData_L",members={"lastWeekTournamentPoint"}),
	@SimpleIndex(name="OneTournamentData_CW",members={"currentWeek"},compress=1),
	@SimpleIndex(name="OneTournamentData_LW",members={"lastWeek"},compress=1),
	@SimpleIndex(name="OneTournamentData_CC",members={"currentWeek","currentTournamentPoint"},compress=1),
	@SimpleIndex(name="OneTournamentData_LL",members={"lastWeek","lastWeekTournamentPoint"},compress=1)
})
public class OneTournamentData {
	
	@SimpleId
	public long playerId;
	
	@SimpleVersion
	protected int version2;

	@SimpleColumn(name="ctp")
	public int currentTournamentPoint;

	@SimpleColumn(name="ltp")
	public int lastWeekTournamentPoint;
	
	public int currentWeek;
	
	public int lastWeek;

	public int currentWinCount;
	
	public int currentLostCount;
	
	public int lastWeekWinCount;
	
	public int lastWeekLostCount;
	
	public boolean pickReward = true;

	public OneTournamentData(){
		
	}
	
	public OneTournamentData(long playerId, int currentWeek){
		this.playerId = playerId;
		this.currentWeek = currentWeek;
		TournamentManager tm = TournamentManager.getInstance();
		if(tm != null && tm.emPlayer != null){
			tm.emPlayer.notifyNewObject(this);
		}
	}
	
	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
		setProperty("playerId");
	}

	public int getVersion2() {
		return version2;
	}

	public void setVersion2(int version2) {
		this.version2 = version2;
	}

	public int getCurrentTournamentPoint() {
		return currentTournamentPoint;
	}

	public void setCurrentTournamentPoint(int currentTournamentPoint) {
		this.currentTournamentPoint = currentTournamentPoint;
		setProperty("currentTournamentPoint");
	}

	public int getLastWeekTournamentPoint() {
		return lastWeekTournamentPoint;
	}

	public void setLastWeekTournamentPoint(int lastWeekTournamentPoint) {
		this.lastWeekTournamentPoint = lastWeekTournamentPoint;
		setProperty("lastWeekTournamentPoint");
	}

	public int getCurrentWinCount() {
		return currentWinCount;
	}

	public void setCurrentWinCount(int currentWinCount) {
		this.currentWinCount = currentWinCount;
		setProperty("currentWinCount");
	}

	public int getCurrentLostCount() {
		return currentLostCount;
	}

	public void setCurrentLostCount(int currentLostCount) {
		this.currentLostCount = currentLostCount;
		setProperty("currentLostCount");
	}

	public int getLastWeekWinCount() {
		return lastWeekWinCount;
	}

	public void setLastWeekWinCount(int lastWeekWinCount) {
		this.lastWeekWinCount = lastWeekWinCount;
		setProperty("lastWeekWinCount");
	}

	public int getLastWeekLostCount() {
		return lastWeekLostCount;
	}

	public void setLastWeekLostCount(int lastWeekLostCount) {
		this.lastWeekLostCount = lastWeekLostCount;
		setProperty("lastWeekLostCount");
	}
	
	public int getCurrentWeek() {
		return currentWeek;
	}

	public void setCurrentWeek(int currentWeek) {
		this.currentWeek = currentWeek;
		setProperty("currentWeek");
	}

	public int getLastWeek() {
		return lastWeek;
	}

	public void setLastWeek(int lastWeek) {
		this.lastWeek = lastWeek;
		setProperty("lastWeek");
	}

	public void setProperty(String property){
		TournamentManager tm = TournamentManager.getInstance();
		if(tm != null && tm.emPlayer != null){
			tm.emPlayer.notifyFieldChange(this, property);
		}
	}

	public boolean isPickReward() {
		return pickReward;
	}

	public void setPickReward(boolean pickReward) {
		this.pickReward = pickReward;
		setProperty("pickReward");
	}
}
