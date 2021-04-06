package com.fy.gamegateway.stat;

import java.util.Date;

import com.xuanzhi.tools.cache.CacheListener;
import com.xuanzhi.tools.cache.Cacheable;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndex;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

/**
 * 标识一个帐号和Client
 * 
 * 包含如下信息：
 * 1. 帐号
 * 2. Client
 * 3. 是否成功注册
 * 4. 注册时间
 * 5. 是否成功登录
 * 6. 首次登录时间
 * 7. 最后一次登录时间
 * 8. 登录次数
 * 9. 用户来源标识
 * 
 *
 */
@SimpleEntity
@SimpleIndices({
	@SimpleIndex(members={"username"}),
	@SimpleIndex(members={"clientId"}),
	@SimpleIndex(members={"registerTime"}),
	@SimpleIndex(members={"loginTimeForLast"}),
	@SimpleIndex(members={"userSource"},compress=1)
})
public class ClientAccount implements CacheListener,Cacheable{
	@SimpleId
	protected long id;
	
	@SimpleVersion
	protected int version;
	
	protected String username;
	protected String clientId;
	
	protected boolean hasSuccessRegister;
	
	protected Date registerTime;
	
	protected boolean hasSuccessLogin;
	
	protected Date loginTimeForFirst;
	protected Date loginTimeForLast;
	
	protected int loginTimes;
	
	protected String userSource;

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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public boolean isHasSuccessRegister() {
		return hasSuccessRegister;
	}

	public void setHasSuccessRegister(boolean hasSuccessRegister) {
		this.hasSuccessRegister = hasSuccessRegister;
	}

	public Date getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}

	public boolean isHasSuccessLogin() {
		return hasSuccessLogin;
	}

	public void setHasSuccessLogin(boolean hasSuccessLogin) {
		this.hasSuccessLogin = hasSuccessLogin;
	}

	public Date getLoginTimeForFirst() {
		return loginTimeForFirst;
	}

	public void setLoginTimeForFirst(Date loginTimeForFirst) {
		this.loginTimeForFirst = loginTimeForFirst;
	}

	public Date getLoginTimeForLast() {
		return loginTimeForLast;
	}

	public void setLoginTimeForLast(Date loginTimeForLast) {
		this.loginTimeForLast = loginTimeForLast;
	}

	public int getLoginTimes() {
		return loginTimes;
	}

	public void setLoginTimes(int loginTimes) {
		this.loginTimes = loginTimes;
	}

	public String getUserSource() {
		return userSource;
	}

	public void setUserSource(String userSource) {
		this.userSource = userSource;
	}
	
	public int getSize(){
		return 1;
	}
	
	public void remove(int type) {
		
		try {
//			StatManager.getInstance().em4Account.save(this);
		} catch (Exception e) {
			StatManager.logger.error("[处理ClientAcount从Cache中移除] [出现异常] [clientId:"+clientId+"] [username:"+username+"]",e);
		}
		
	}
}
