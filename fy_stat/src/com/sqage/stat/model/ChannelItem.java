package com.sqage.stat.model;

/**
 * ChannelItem entity.
 * 子渠道实体，从属于父渠道
 * @author MyEclipse Persistence Tools
 */

public class ChannelItem implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -6733310408678905391L;
	private Long id;
	private String name;
	private String key;
	private Long channelid;
	private Float prate = 1f;
	private Long cmode = 0L;

	// Constructors

	/** default constructor */
	public ChannelItem() {
	}

	/** full constructor */
	public ChannelItem(String name, String key, Long channelid) {
		this.name = name;
		this.key = key;
		this.channelid = channelid;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Long getChannelid() {
		return this.channelid;
	}

	public void setChannelid(Long channelid) {
		this.channelid = channelid;
	}

	public Float getPrate() {
		return prate;
	}

	public void setPrate(Float prate) {
		this.prate = prate;
	}

	public Long getCmode() {
		return cmode;
	}

	public void setCmode(Long cmode) {
		this.cmode = cmode;
	}
}
