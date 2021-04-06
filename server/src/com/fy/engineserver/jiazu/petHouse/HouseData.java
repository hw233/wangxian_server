package com.fy.engineserver.jiazu.petHouse;

import java.util.ArrayList;
import java.util.List;

import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndex;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

@SimpleEntity
@SimpleIndices({ @SimpleIndex(members = { "playerId" ,"jiaZuId"})})
public class HouseData {
	@SimpleId
	private long petId;
	@SimpleVersion
	private int version;
	private long playerId;
	private long jiaZuId;
	private String playerName;
	private String petName;
	private String petIcon;
	private int petLevel;
	private int blessCount;
	private long startStoreTime;
	private List<Long> blessIds = new ArrayList<Long>();
	private boolean isCallBack;
	
	public HouseData(){}
	
	public HouseData(long playerId, long petId, String petName,long jiaZuId,String playerName, String petIcon,int petLevel) {
		this.playerId = playerId;
		this.petId = petId;
		this.petName = petName;
		this.jiaZuId = jiaZuId;
		this.playerName = playerName;
		this.petIcon = petIcon;
		this.petLevel = petLevel;
	}
	
	public boolean isCallBack() {
		return isCallBack;
	}

	public void setCallBack(boolean isCallBack) {
		this.isCallBack = isCallBack;
		PetHouseManager.petEm.notifyFieldChange(this, "isCallBack");
	}

	public List<Long> getBlessIds() {
		return blessIds;
	}

	public void setBlessIds(List<Long> blessIds) {
		this.blessIds = blessIds;
		PetHouseManager.petEm.notifyFieldChange(this, "blessIds");
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getPetLevel() {
		return petLevel;
	}

	public void setPetLevel(int petLevel) {
		this.petLevel = petLevel;
		PetHouseManager.petEm.notifyFieldChange(this, "petLevel");
	}

	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
		PetHouseManager.petEm.notifyFieldChange(this, "playerId");
	}
	public long getPetId() {
		return petId;
	}
	public void setPetId(long petId) {
		this.petId = petId;
		PetHouseManager.petEm.notifyFieldChange(this, "petId");
	}
	public long getJiaZuId() {
		return jiaZuId;
	}
	public void setJiaZuId(long jiaZuId) {
		this.jiaZuId = jiaZuId;
		PetHouseManager.petEm.notifyFieldChange(this, "jiaZuId");
	}
	public String getPlayerName() {
		return playerName;
	}
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
		PetHouseManager.petEm.notifyFieldChange(this, "playerName");
	}
	public String getPetIcon() {
		return petIcon;
	}
	public void setPetIcon(String petIcon) {
		this.petIcon = petIcon;
		PetHouseManager.petEm.notifyFieldChange(this, "petIcon");
	}
	public int getBlessCount() {
		return blessCount;
	}
	public void setBlessCount(int blessCount) {
		this.blessCount = blessCount;
		PetHouseManager.petEm.notifyFieldChange(this, "blessCount");
	}
	public long getStartStoreTime() {
		return startStoreTime;
	}
	public void setStartStoreTime(long startStoreTime) {
		this.startStoreTime = startStoreTime;
		PetHouseManager.petEm.notifyFieldChange(this, "startStoreTime");
	}

	public String getPetName() {
		return petName;
	}

	public void setPetName(String petName) {
		this.petName = petName;
		PetHouseManager.petEm.notifyFieldChange(this, "petName");
	}

	@Override
	public String toString() {
		return " [blessCount=" + blessCount + ", blessIds=" + blessIds
				+ ", jiaZuId=" + jiaZuId + ", petIcon=" + petIcon + ", petId="
				+ petId + ", petLevel=" + petLevel + ", petName=" + petName
				+ ", playerId=" + playerId + ", playerName=" + playerName
				+ ", startStoreTime=" + startStoreTime + ", version=" + version
				+ "]";
	}

	
}
