package com.sqage.stat.model;

import java.util.Date;

/**
 * PlayerLogin entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class PlayerLogin implements java.io.Serializable {

	// Fields

	private Long id;
	private Long userid;
	private Long playerid;
	private Long playerlevel;
	private Long serverid;
	private Long careerid;
	private Long sex;
	private Long polcamp;
	private Long channelid;
	private Long channelitemid;
	private Date logintime;

	// Constructors

	/** default constructor */
	public PlayerLogin() {
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserid() {
		return this.userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public Long getPlayerid() {
		return this.playerid;
	}

	public void setPlayerid(Long playerid) {
		this.playerid = playerid;
	}

	public Long getPlayerlevel() {
		return this.playerlevel;
	}

	public void setPlayerlevel(Long playerlevel) {
		this.playerlevel = playerlevel;
	}

	public Long getServerid() {
		return this.serverid;
	}

	public void setServerid(Long serverid) {
		this.serverid = serverid;
	}

	public Long getCareerid() {
		return this.careerid;
	}

	public void setCareerid(Long careerid) {
		this.careerid = careerid;
	}

	public Long getSex() {
		return this.sex;
	}

	public void setSex(Long sex) {
		this.sex = sex;
	}

	public Long getPolcamp() {
		return this.polcamp;
	}

	public void setPolcamp(Long polcamp) {
		this.polcamp = polcamp;
	}

	public Long getChannelid() {
		return this.channelid;
	}

	public void setChannelid(Long channelid) {
		this.channelid = channelid;
	}

	public Long getChannelitemid() {
		return this.channelitemid;
	}

	public void setChannelitemid(Long channelitemid) {
		this.channelitemid = channelitemid;
	}

	public Date getLogintime() {
		return logintime;
	}

	public void setLogintime(Date logintime) {
		this.logintime = logintime;
	}
}
