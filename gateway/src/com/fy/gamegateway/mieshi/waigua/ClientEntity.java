package com.fy.gamegateway.mieshi.waigua;

import com.xuanzhi.tools.cache.Cacheable;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndex;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

@SimpleEntity
@SimpleIndices({
	@SimpleIndex(members={"clientID"}),
	@SimpleIndex(members={"mac"})
})
public class ClientEntity implements Cacheable {
	
	@SimpleId
	protected long id;
	
	@SimpleVersion
	protected int version;

	private String clientID;					//客户端clientID
	
	private String mac;							//mac地址
	
	private String platform;					//平台
	
	private String phoneType;					//机型
	
	private String gpuType;						//GPU类型
	
	private String channel;						//渠道
	
	private long createTime = System.currentTimeMillis();		//创建时间
	
	private long lastActiveTime = System.currentTimeMillis();		//最后一次使用时间，通过这个时间，可以删除一些最老的对象
	
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
	
	public void setClientID(String clientID) {
		this.clientID = clientID;
	}

	public String getClientID() {
		return clientID;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getMac() {
		return mac;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPhoneType(String phoneType) {
		this.phoneType = phoneType;
	}

	public String getPhoneType() {
		return phoneType;
	}

	public void setGpuType(String gpuType) {
		this.gpuType = gpuType;
	}

	public String getGpuType() {
		return gpuType;
	}
	@Override
	public int getSize() {
		return 1;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public long getCreateTime() {
		return createTime;
	}
	public long getLastActiveTime() {
		return lastActiveTime;
	}
	public void setLastActiveTime(long lastActiveTime) {
		this.lastActiveTime = lastActiveTime;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getChannel() {
		return channel;
	}

}
