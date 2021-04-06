package com.fy.boss.finance.model;

import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndex;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;


@SimpleEntity(name="UcidToUserName")
@SimpleIndices({
	@SimpleIndex(members={"ucid"},name="UCID_INX"),
	@SimpleIndex(members={"username"},name="USERNAME_INX"),
	@SimpleIndex(members={"createTime"},name="TIME_INX")	
})
public class UcidToUserName implements java.io.Serializable {
	
	//成功状态
	public static final int NORMAL = 0;
	//冻结状态
	public static final int FORBID = -1;

	// Fields
	@SimpleId
	private long id = 0l;
	
	@SimpleVersion
	private int version;
	
	/**
	 * UCID
	 */
	private String ucid;
	
	/**
	 * 游戏账号
	 */
	private String username;
	
	
	/**
	 * 用户充值时所属的渠道
	 */
	private String userChannel;
	

	/**
	 * 创建时间
	 */
	private long createTime;
	
	private int status = NORMAL;
	
	/**
	 * 描述
	 */
	private String memo;
	
	
	/**
	 * 备注字段1 
	 */
	@SimpleColumn(length=4000)
	private String memo1;
	
	/**
	 * 备注字段2
	 */
	@SimpleColumn(length=4000)
	private String memo2;
	
	/**
	 * 备注字段3 
	 */
	@SimpleColumn(length=4000)
	private String memo3;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getUcid() {
		return ucid;
	}

	public void setUcid(String ucid) {
		this.ucid = ucid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserChannel() {
		return userChannel;
	}

	public void setUserChannel(String userChannel) {
		this.userChannel = userChannel;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getMemo1() {
		return memo1;
	}

	public void setMemo1(String memo1) {
		this.memo1 = memo1;
	}

	public String getMemo2() {
		return memo2;
	}

	public void setMemo2(String memo2) {
		this.memo2 = memo2;
	}

	public String getMemo3() {
		return memo3;
	}

	public void setMemo3(String memo3) {
		this.memo3 = memo3;
	}
}
