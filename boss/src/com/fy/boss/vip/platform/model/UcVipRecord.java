package com.fy.boss.vip.platform.model;

import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndex;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

@SimpleEntity(name="UcVipRecord")
@SimpleIndices({
	@SimpleIndex(members={"username"},name="INDX_UCVIP_USERNAME"),
	@SimpleIndex(members={"createTime"},name="INDX_UCVIP_CREATETIME"),
	@SimpleIndex(members={"playerId"},name="INDX_UCVIP_PLAYERID")
})
public class UcVipRecord {
	// Fields
	@SimpleId
	private long id;
	
	@SimpleVersion
	private int version;
	
	private String username;
	
	private long playerId;
	
	private String playerName;
	
	private String serverName;
	
//	private long birthtime;
	
	private int playerLevel;
	
	private int  vipLevel;
	
	
	
	
	
	
	
	
	private long createTime;
	
	private long updateTime;
	

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}


	public int getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}


	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public int getPlayerLevel() {
		return playerLevel;
	}

	public void setPlayerLevel(int playerLevel) {
		this.playerLevel = playerLevel;
	}


	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}



	
	
	
	
	

/*	public long getBirthtime() {
		return birthtime;
	}

	public void setBirthtime(long birthtime) {
		this.birthtime = birthtime;
	}*/

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public String getLogString()
	{
		return "["+id+"] ["+version+"] ["+vipLevel+"] ["+username+"] ["+playerName+"] ["+playerLevel+"] ["+serverName+"] ["+playerId+"]";
	}
	
	
}
