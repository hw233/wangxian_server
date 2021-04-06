package com.fy.gamegateway.mieshi.waigua;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.xuanzhi.tools.cache.Cacheable;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndex;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;


/**
 * 
 *	自动授权：  1.当没有授权。
 *			  2.已经授权 全部失效 （最后一次登录时间超过30天）。
 *			  3.mac地址与已授权clientEntity中的mac地址一致。
 *	手动授权: 
 */
@SimpleEntity
@SimpleIndices({
	@SimpleIndex(members={"username"}),
	@SimpleIndex(name="clientID", members={"clientID"}),
	@SimpleIndex(name="lastLoginTime", members={"lastLoginTime"})
})
public class ClientAuthorization implements Cacheable {
	
	@SimpleId
	protected long id;
	
	@SimpleVersion
	protected int version;
	
	public static final int AUTHORIZE_STATE_DENGDAI = 0;				//等待授权
	public static final int AUTHORIZE_STATE_ZHENGCHANG = 1;				//正常
	public static final int AUTHORIZE_STATE_BEITIDAI = 2;				//代表此授权被其他授权应该替代
	public static final int AUTHORIZE_STATE_YICHANG = 3;				//应该被替代的授权又使用了(异常情况)
	public static final int AUTHORIZE_STATE_JUJUE = 4;					//被拒绝授权
	public static final int AUTHORIZE_STATE_DENGDAI_GUOQI = 5;			//等待过期
	
	private String username;					//用户名
	
	private String authorizeClientId;			//授权clientID
	
	private String clientID;					//
	
	private long authorizeTime;					//授权时间
	
	private long lastLoginTime;					//最后一次登录时间
	
	private int authorizeType;					//授权方式   0是自动授权   1是手动授权
	
	//授权状态  0等待授权，1代表正常，2代表此授权被其他授权应该替代 ，3应该被替代的授权又使用了
	private int authorizeState;
	//等待授权的时间
	private long waitAuthorizeTime;
	//请求等待授权次数
	private int queryNum;
	
	//成功登录次数
	private int loginNum;
	
	private String ip;
	
	public String getLogString () {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		StringBuffer sb = new StringBuffer("");
		sb.append("[").append(id).append("]");
		sb.append(" [").append(username).append("]");
		sb.append(" [fID=").append(authorizeClientId).append("]");
		sb.append(" [").append(clientID).append("]");
		sb.append(" [").append(authorizeType).append("]");
		sb.append(" [").append(authorizeState).append("]");
		sb.append(" [").append(queryNum).append("]");
		sb.append(" [").append(ip).append("]");
		sb.append(" [").append(format.format(new Date(authorizeTime))).append("]");
		return sb.toString();
	}
	
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

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setAuthorizeTime(long authorizeTime) {
		this.authorizeTime = authorizeTime;
	}

	public long getAuthorizeTime() {
		return authorizeTime;
	}

	public void setLastLoginTime(long lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public long getLastLoginTime() {
		return lastLoginTime;
	}

	public void setAuthorizeType(int authorizeType) {
		this.authorizeType = authorizeType;
	}

	public int getAuthorizeType() {
		return authorizeType;
	}

	public void setAuthorizeState(int authorizeState) {
		this.authorizeState = authorizeState;
	}

	public int getAuthorizeState() {
		return authorizeState;
	}
	@Override
	public int getSize() {
		return 1;
	}
	public void setClientID(String clientID) {
		this.clientID = clientID;
	}
	public String getClientID() {
		return clientID;
	}
	
	public boolean isAuthorized(){
		return authorizeState == ClientAuthorization.AUTHORIZE_STATE_ZHENGCHANG || 
		authorizeState == ClientAuthorization.AUTHORIZE_STATE_BEITIDAI || 
		authorizeState == ClientAuthorization.AUTHORIZE_STATE_YICHANG;
	}
	public void setQueryNum(int queryNum) {
		this.queryNum = queryNum;
	}
	public int getQueryNum() {
		return queryNum;
	}

	public void setWaitAuthorizeTime(long waitAuthorizeTime) {
		this.waitAuthorizeTime = waitAuthorizeTime;
	}

	public long getWaitAuthorizeTime() {
		return waitAuthorizeTime;
	}

	public void setAuthorizeClientId(String authorizeClientId) {
		this.authorizeClientId = authorizeClientId;
	}

	public String getAuthorizeClientId() {
		return authorizeClientId;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getIp() {
		return ip;
	}

	public int getLoginNum() {
		return loginNum;
	}

	public void setLoginNum(int loginNum) {
		this.loginNum = loginNum;
	}
}
