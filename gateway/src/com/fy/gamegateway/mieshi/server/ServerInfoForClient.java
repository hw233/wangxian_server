package com.fy.gamegateway.mieshi.server;

/**
 * 
 * 
 *
 */
public class ServerInfoForClient {


	//分区
	private String category ;
	
	//显示名字
	private String name = "";
	
	private String ip = "";
	
	private int port;
	
	private byte clientid;
	
	private byte serverid;
	
	private String description = "";
	
	private int onlinePlayerNum;
	
	private int status;
	private String statusName = "";
	private boolean hasPlayer;
	
	private int career;
	
	private int level;
	
	private String playerName = "";
	
	//此字段不发送给客户端
	public long lastLoginTime;
	
	
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * 服务器名称
	 * @return
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	/**
	 * IP地址
	 * @return
	 */
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * 端口
	 * @return
	 */
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * 描述
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	

	public int getOnlinePlayerNum() {
		return onlinePlayerNum;
	}

	public void setOnlinePlayerNum(int onlinePlayerNum) {
		this.onlinePlayerNum = onlinePlayerNum;
	}
	
	public byte getServerid() {
		return serverid;
	}

	public void setServerid(byte serverid) {
		this.serverid = serverid;
	}

	public byte getClientid() {
		return clientid;
	}

	public void setClientid(byte clientid) {
		this.clientid = clientid;
	}

	public boolean isHasPlayer() {
		return hasPlayer;
	}

	public void setHasPlayer(boolean hasPlayer) {
		this.hasPlayer = hasPlayer;
	}

	public int getCareer() {
		return career;
	}

	public void setCareer(int career) {
		this.career = career;
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

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	
}
