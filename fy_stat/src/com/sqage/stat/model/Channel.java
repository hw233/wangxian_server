package com.sqage.stat.model;

/**
 * Channel entity.
 * 渠道实体
 *
 * @author MyEclipse Persistence Tools
 */

public class Channel implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 6894576750580537639L;
	private Long id;
	private String name;
	private String key;

	// Constructors

	/** default constructor */
	public Channel() {
	}

	/** full constructor */
	public Channel(String name, String key, String description) {
		this.name = name;
		this.key = key;
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

}
