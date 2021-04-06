package com.fy.engineserver.hotspot;


import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndex;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

//玩家的热点数据，id是玩家playerID

@SimpleEntity
@SimpleIndices({
	@SimpleIndex(members={"playerID"})
})
public class HotspotInfo {
	
	@SimpleId
	private long id;
	
	@SimpleVersion
	private int versionHot;
	
	private long playerID;
	
	private int hotspotID;
	//是否看过，0为未开启，1为未看过，2为看过
	private int[] isSee;
	
	private boolean isOver;
	
	public HotspotInfo(){}
	
	public HotspotInfo(long id, long playerID, int hotspotID){
		this.id = id;
		this.playerID = playerID;
		this.hotspotID = hotspotID;
		isOver = false;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	public long getId() {
		return id;
	}
	public void setHotspotID(int hotspotID) {
		this.hotspotID = hotspotID;
	}
	public int getHotspotID() {
		return hotspotID;
	}
	public void setOver(boolean isOver) {
		this.isOver = isOver;
		HotspotManager.getInstance().em.notifyFieldChange(this, "isOver");
	}
	public boolean isOver() {
		return isOver;
	}
	public void setSee(int[] isSee) {
		this.isSee = isSee;
	}
	public int[] isSee() {
		return isSee;
	}
	
	public void setRealSee(){
		if (isSee == null) {
			return;
		}
		for (int i = 0; i < isSee.length; i++) {
			if (isSee[i] == 1) {
				isSee[i] = 2;
			}
		}
		HotspotManager.getInstance().em.notifyFieldChange(this, "isSee");
	}
	
	public boolean isRealSee(){
		if (isSee == null) {
			return false;
		}
		for (int i = 0; i < isSee.length; i++) {
			if (isSee[i] == 1) {
				return false;
			}
		}
		return true;
	}
	
	public void setPlayerID(long playerID) {
		this.playerID = playerID;
	}

	public long getPlayerID() {
		return playerID;
	}

	public void setVersionHot(int versionHot) {
		this.versionHot = versionHot;
	}

	public int getVersionHot() {
		return versionHot;
	}
}
