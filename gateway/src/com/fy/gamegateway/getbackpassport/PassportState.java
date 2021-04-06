package com.fy.gamegateway.getbackpassport;

import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndex;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

/**
 * 玩家对找回密码的操作记录
 * @author wtx
 *
 */
@SimpleEntity
@SimpleIndices({
	@SimpleIndex(members={"value"},compress=1),
	@SimpleIndex(members={"lastUpdateTime"},compress=1)
})
public class PassportState {
	
	@SimpleId
	protected long id;
	
	@SimpleVersion
	protected int version;
	
	private String username;
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	private long value;
	
	private long lastUpdateTime;
	
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
	
	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}

	public long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

}
