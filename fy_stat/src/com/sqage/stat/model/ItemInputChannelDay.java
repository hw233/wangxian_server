package com.sqage.stat.model;

/**
 * ItemInputChannelDay entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ItemInputChannelDay implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 78735870305536908L;
	private Long id;
	/**
	 * 月份 YYYYMM
	 */
	private Long inputmonth;
	private Long channelid;
	private Long channelitemid;
	/**
	 * 单价 分
	 */
	private Long amount = 0L;
	
	/**
	 * 结算单价
	 */
	private Long realamount = 0L;

	// Constructors

	/** default constructor */
	public ItemInputChannelDay() {
	}

	/** full constructor */
	public ItemInputChannelDay(Long inputmonth, Long channelid,
			Long channelitemid, Long amount) {
		this.inputmonth = inputmonth;
		this.channelid = channelid;
		this.channelitemid = channelitemid;
		this.amount = amount;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getInputmonth() {
		return this.inputmonth;
	}

	public void setInputmonth(Long inputmonth) {
		this.inputmonth = inputmonth;
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

	public Long getAmount() {
		return this.amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public Long getRealamount() {
		return realamount;
	}

	public void setRealamount(Long realamount) {
		this.realamount = realamount;
	}

}
