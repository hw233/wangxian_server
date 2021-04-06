package com.fy.gamegateway.mieshi.server.pub;

import java.util.HashMap;
import java.util.Map;

import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndex;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

/**
 * 这个类用来存储在游戏服那边vip用户填写的信息
 *
 */
@SimpleEntity(name="ServerPubRecord")
@SimpleIndices({
	@SimpleIndex(members={"serverName"},name="SERVERPUB_INDX_SERVERNAME"),
	@SimpleIndex(members={"createTime"},name="SERVERPUB_INDX_CREATETIME"),
	@SimpleIndex(members={"openServerTime"},name="SERVERPUB_INDX_OPENTIME")
})
public class ServerPubRecord {
	// Fields
	@SimpleId
	private long id;
	
	@SimpleVersion
	private int version;
	
	private String serverName;
	
	private String openServerTime;
	
	private String serverType;
	
	private Map<Integer,Long> fengyinTimeInfo = new HashMap<Integer, Long>();
	
	private long createTime;
	
	
	
	
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




	public String getServerName() {
		return serverName;
	}




	public void setServerName(String serverName) {
		this.serverName = serverName;
	}




	public String getOpenServerTime() {
		return openServerTime;
	}




	public void setOpenServerTime(String openServerTime) {
		this.openServerTime = openServerTime;
	}




	public String getServerType() {
		return serverType;
	}




	public void setServerType(String serverType) {
		this.serverType = serverType;
	}




	public Map<Integer, Long> getFengyinTimeInfo() {
		return fengyinTimeInfo;
	}




	public void setFengyinTimeInfo(Map<Integer, Long> fengyinTimeInfo) {
		this.fengyinTimeInfo = fengyinTimeInfo;
	}




	public long getCreateTime() {
		return createTime;
	}




	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}




	public String getLogString()
	{
		return "["+id+"] ["+version+"] ["+serverName+"] ["+openServerTime+"] ["+serverType+"]";
	}
	
	
}
