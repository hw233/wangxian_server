package com.fy.gamegateway.mieshi.server;

import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndex;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

/**
 * 玩家角色信息
 * 
 */

@SimpleEntity
@SimpleIndices({
	@SimpleIndex(members={"playerId"}),
	@SimpleIndex(members={"playerName"}),
	@SimpleIndex(members={"userName"}),
	@SimpleIndex(name="sRname", members={"serverRealName"})
})
public class MieshiPlayerInfo implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2862548382330946841L;
	
	@SimpleId
	long id;
	
	@SimpleVersion
	int version;
	
	long playerId;
	
	String serverRealName;
	String userName;
	
	@SimpleColumn(name="level2")
	int level;
	String playerName;
	int career;
	
	int playerRMB;
	
	int playerVIP;
	
	//最后一次更新的时间，是用户上一次进入服务器是需要通知网关
	public long lastAccessTime;
	
	public String getServerRealName() {
		return serverRealName;
	}
	public void setServerRealName(String serverRealName) {
		this.serverRealName = serverRealName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getPlayerName() {
		return playerName;
	}
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	public int getCareer() {
		return career;
	}
	public void setCareer(int career) {
		this.career = career;
	}
	public void setPlayerRMB(int playerRMB) {
		this.playerRMB = playerRMB;
	}
	public int getPlayerRMB() {
		return playerRMB;
	}
	public void setPlayerVIP(int playerVIP) {
		this.playerVIP = playerVIP;
	}
	public int getPlayerVIP() {
		return playerVIP;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	public long getPlayerId() {
		return playerId;
	}
	public long getLastAccessTime() {
		return lastAccessTime;
	}
	public void setLastAccessTime(long lastAccessTime) {
		this.lastAccessTime = lastAccessTime;
	}
	
}
