package com.fy.engineserver.downcity.stat;

import java.io.Serializable;

/**
 * 副本消耗信息
 * 
 *
 */
public class DownCityConsumeInfo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3695378417032761886L;

	/**
	 * 玩家id
	 */
	protected long id;
	
	/**
	 * 人名
	 */
	protected String playerName;
	
	/**
	 * userName
	 */
	protected String userName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * 使用道具名
	 */
	protected String propName;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public String getPropName() {
		return propName;
	}

	public void setPropName(String propName) {
		this.propName = propName;
	}

}
