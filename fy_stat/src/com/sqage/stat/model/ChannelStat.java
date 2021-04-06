package com.sqage.stat.model;

import java.util.Date;

/**
 * ChannelStat entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ChannelStat implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -8813755585386564861L;
	private Long id;
	private Long channelid;
	private Long channelitemid;
	private Date statdate;
	private Long regnum;
	private Float prate;
	private Long income;

	// Constructors

	/** default constructor */
	public ChannelStat() {
	}

	/** full constructor */
	public ChannelStat(Long channelid, Long channelitemid,Date statdate, Long regnum, Float prate) {
		this.channelid = channelid;
		this.channelitemid = channelitemid;
		this.statdate = statdate;
		this.regnum = regnum;
		this.prate = prate;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getChannelid() {
		return this.channelid;
	}

	public void setChannelid(Long channelid) {
		this.channelid = channelid;
	}

	public Date getStatdate() {
		return this.statdate;
	}

	public void setStatdate(Date statdate) {
		this.statdate = statdate;
	}

	public Long getRegnum() {
		return this.regnum;
	}

	public void setRegnum(Long regnum) {
		this.regnum = regnum;
	}

	public Float getPrate() {
		return this.prate;
	}

	public void setPrate(Float prate) {
		this.prate = prate;
	}

	public Long getChannelitemid() {
		return channelitemid;
	}

	public void setChannelitemid(Long channelitemid) {
		this.channelitemid = channelitemid;
	}

	public Long getIncome() {
		return income;
	}

	public void setIncome(Long income) {
		this.income = income;
	}

}
