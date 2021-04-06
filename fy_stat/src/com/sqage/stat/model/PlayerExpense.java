package com.sqage.stat.model;

import java.util.Date;

/**
 * PlayerExpense entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class PlayerExpense implements java.io.Serializable {

	// Fields

	private Long id;
	private Long playerid;
	private Long userid;
	private Long nowlevel;
	private Long server;
	private Long career;
	private Long sex;
	private Long polcamp;
	private Long map;
	private Long expensereason;
	private Long currencytype;
	private Long expenseamount;
	private Long channel;
	private Long channelitem;
	private Date uptime;
	private Date regtime;
	private Long firstexpenseyuanbao;

	// Constructors

	/** default constructor */
	public PlayerExpense() {
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPlayerid() {
		return playerid;
	}

	public void setPlayerid(Long playerid) {
		this.playerid = playerid;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public Long getNowlevel() {
		return this.nowlevel;
	}

	public void setNowlevel(Long nowlevel) {
		this.nowlevel = nowlevel;
	}

	public Long getServer() {
		return this.server;
	}

	public void setServer(Long server) {
		this.server = server;
	}

	public Long getCareer() {
		return this.career;
	}

	public void setCareer(Long career) {
		this.career = career;
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

	public Long getMap() {
		return this.map;
	}

	public void setMap(Long map) {
		this.map = map;
	}

	public Long getExpensereason() {
		return this.expensereason;
	}

	public void setExpensereason(Long expensereason) {
		this.expensereason = expensereason;
	}

	public Long getCurrencytype() {
		return this.currencytype;
	}

	public void setCurrencytype(Long currencytype) {
		this.currencytype = currencytype;
	}

	public Long getExpenseamount() {
		return this.expenseamount;
	}

	public void setExpenseamount(Long expenseamount) {
		this.expenseamount = expenseamount;
	}

	public Long getChannel() {
		return this.channel;
	}

	public void setChannel(Long channel) {
		this.channel = channel;
	}

	public Long getChannelitem() {
		return this.channelitem;
	}

	public void setChannelitem(Long channelitem) {
		this.channelitem = channelitem;
	}

	public Date getUptime() {
		return uptime;
	}

	public void setUptime(Date uptime) {
		this.uptime = uptime;
	}

	public Date getRegtime() {
		return regtime;
	}

	public void setRegtime(Date regtime) {
		this.regtime = regtime;
	}

	public Long getFirstexpenseyuanbao() {
		return firstexpenseyuanbao;
	}

	public void setFirstexpenseyuanbao(Long firstexpenseyuanbao) {
		this.firstexpenseyuanbao = firstexpenseyuanbao;
	}
}
