package com.sqage.stat.model;

import java.util.Date;

/**
 * PlayerLogout entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class PlayerLogout implements java.io.Serializable {

	// Fields

	private Long id;
	private Long userid;
	private Long playerid;
	private Long playerlevel;
	private Long serverid;
	private Long careerid;
	private Long sex;
	private Long polcampid;
	private Long mapid;
	private Long channelid;
	private Long channelitemid;
	private Date logintime;
	private Date loginouttime;
	private Long onlinetime;

	// Constructors

	/** default constructor */
	public PlayerLogout() {
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

	public Long getPolcampid() {
		return this.polcampid;
	}

	public void setPolcampid(Long polcampid) {
		this.polcampid = polcampid;
	}

	public Long getMapid() {
		return this.mapid;
	}

	public void setMapid(Long mapid) {
		this.mapid = mapid;
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

	public Date getLoginouttime() {
		return loginouttime;
	}

	public void setLoginouttime(Date loginouttime) {
		this.loginouttime = loginouttime;
	}

	public Long getOnlinetime() {
		return this.onlinetime;
	}

	public void setOnlinetime(Long onlinetime) {
		this.onlinetime = onlinetime;
	}

}
