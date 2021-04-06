package com.fy.boss.gm.gmuser;


/**
 * Authority entity. @author MyEclipse Persistence Tools
 */

public class Authority implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer userid;
	private String roleid;

	// Constructors

	/** default constructor */
	public Authority() {
	}

	/** full constructor */
	

	public String getRoleid() {
		return this.roleid;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}

}
