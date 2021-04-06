package com.sqage.stat.model;

import java.util.Date;

/**
 * OnlinePlayer entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class OnlinePlayer implements java.io.Serializable {

	// Fields

	private Long id;
	private Long onlinenum;
	private Long serverid;
	private Long mapid;
	private Long polcampid;
	private Long careerid;
	private Long channelid;
	private Long channelitemid;
	private Date stattime;

	// Constructors

	/** default constructor */
	public OnlinePlayer() {
	}

	/** full constructor */
	public OnlinePlayer(Long onlinenum, Long serverid, Date stattime) {
		this.onlinenum = onlinenum;
		this.serverid = serverid;
		this.stattime = stattime;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOnlinenum() {
		return this.onlinenum;
	}

	public void setOnlinenum(Long onlinenum) {
		this.onlinenum = onlinenum;
	}

	public Long getServerid() {
		return this.serverid;
	}

	public void setServerid(Long serverid) {
		this.serverid = serverid;
	}

	public Date getStattime() {
		return this.stattime;
	}

	public void setStattime(Date stattime) {
		this.stattime = stattime;
	}

	public Long getMapid() {
		return mapid;
	}

	public void setMapid(Long mapid) {
		this.mapid = mapid;
	}

	public Long getPolcampid() {
		return polcampid;
	}

	public void setPolcampid(Long polcampid) {
		this.polcampid = polcampid;
	}

	public Long getCareerid() {
		return careerid;
	}

	public void setCareerid(Long careerid) {
		this.careerid = careerid;
	}

	public Long getChannelid() {
		return channelid;
	}

	public void setChannelid(Long channelid) {
		this.channelid = channelid;
	}

	public Long getChannelitemid() {
		return channelitemid;
	}

	public void setChannelitemid(Long channelitemid) {
		this.channelitemid = channelitemid;
	}

}
