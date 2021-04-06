package com.sqage.stat.model;

import java.util.Date;

/**
 * SavingYuanbao entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SavingYuanbao implements java.io.Serializable {

	// Fields

	private Long id;
	private Long userid;
	private Long playerid;
	private Long provinceid;
	private Long cityid;
	private Long nowlevel;
	private Long serverid;
	private Long sexid;
	private Long polcampid;
	private Long careerid;
	private Long mapid;
	private Long channelid;
	private Long channelitemid;
	private Long platformid;
	private Long mediumid;
	private Long firstsaving;
	private Long yuanbao;
	private Long rmb;
	private Long succ;
	private Long cost;
	private String orderno;
	private Date createtime;
	private Date regtime;
	private Long purermb;

	// Constructors

	/** default constructor */
	public SavingYuanbao() {
	}

	/** full constructor */
	public SavingYuanbao(Long userid, Long playerid, Long provinceid,
			Long cityid, Long nowlevel, Long serverid, Long sexid,
			Long polcampid, Long careerid, Long mapid, Long channelid,
			Long channelitemid, Long platformid, Long mediumid,
			Long firstsaving, Long yuanbao, Long rmb, Long succ, Long cost,
			String orderno, Date createtime) {
		this.userid = userid;
		this.playerid = playerid;
		this.provinceid = provinceid;
		this.cityid = cityid;
		this.nowlevel = nowlevel;
		this.serverid = serverid;
		this.sexid = sexid;
		this.polcampid = polcampid;
		this.careerid = careerid;
		this.mapid = mapid;
		this.channelid = channelid;
		this.channelitemid = channelitemid;
		this.platformid = platformid;
		this.mediumid = mediumid;
		this.firstsaving = firstsaving;
		this.yuanbao = yuanbao;
		this.rmb = rmb;
		this.succ = succ;
		this.cost = cost;
		this.orderno = orderno;
		this.createtime = createtime;
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

	public Long getProvinceid() {
		return this.provinceid;
	}

	public void setProvinceid(Long provinceid) {
		this.provinceid = provinceid;
	}

	public Long getCityid() {
		return this.cityid;
	}

	public void setCityid(Long cityid) {
		this.cityid = cityid;
	}

	public Long getNowlevel() {
		return this.nowlevel;
	}

	public void setNowlevel(Long nowlevel) {
		this.nowlevel = nowlevel;
	}

	public Long getServerid() {
		return this.serverid;
	}

	public void setServerid(Long serverid) {
		this.serverid = serverid;
	}

	public Long getSexid() {
		return this.sexid;
	}

	public void setSexid(Long sexid) {
		this.sexid = sexid;
	}

	public Long getPolcampid() {
		return this.polcampid;
	}

	public void setPolcampid(Long polcampid) {
		this.polcampid = polcampid;
	}

	public Long getCareerid() {
		return this.careerid;
	}

	public void setCareerid(Long careerid) {
		this.careerid = careerid;
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

	public Long getPlatformid() {
		return this.platformid;
	}

	public void setPlatformid(Long platformid) {
		this.platformid = platformid;
	}

	public Long getMediumid() {
		return this.mediumid;
	}

	public void setMediumid(Long mediumid) {
		this.mediumid = mediumid;
	}

	public Long getFirstsaving() {
		return this.firstsaving;
	}

	public void setFirstsaving(Long firstsaving) {
		this.firstsaving = firstsaving;
	}

	public Long getYuanbao() {
		return this.yuanbao;
	}

	public void setYuanbao(Long yuanbao) {
		this.yuanbao = yuanbao;
	}

	public Long getRmb() {
		return this.rmb;
	}

	public void setRmb(Long rmb) {
		this.rmb = rmb;
	}

	public Long getSucc() {
		return this.succ;
	}

	public void setSucc(Long succ) {
		this.succ = succ;
	}

	public Long getCost() {
		return this.cost;
	}

	public void setCost(Long cost) {
		this.cost = cost;
	}

	public String getOrderno() {
		return this.orderno;
	}

	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}

	public Date getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Date getRegtime() {
		return regtime;
	}

	public void setRegtime(Date regtime) {
		this.regtime = regtime;
	}

	public Long getPurermb() {
		return purermb;
	}

	public void setPurermb(Long purermb) {
		this.purermb = purermb;
	}

}
