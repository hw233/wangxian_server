package com.fy.gamegateway.stat;

import java.util.Date;

import com.xuanzhi.tools.cache.Cacheable;
import com.xuanzhi.tools.cache.CacheListener;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndex;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

/**
 * 1.clientid
 * 2.手机号 mobilenum
 * 3.用户名
 * 4.os
 * 5.上次登陆手机号
 * 6.上次登陆os
 * 7.上次登陆用户名
 * 8.上次登陆clientid
 * 9.memo1
 * 10.memo2
 * 11.memo3
 * 12.attr1
 * 13.attr2
 * 14.attr3
 * 15.createtime
 * 16.channel
 * 17.lastloginchannel
 * 
 *
 */
@SimpleEntity
@SimpleIndices({
	@SimpleIndex(members={"username"}),
	@SimpleIndex(members={"clientId"}),
	@SimpleIndex(members={"createtime"}),
	@SimpleIndex(members={"mobilenum"}),
	@SimpleIndex(members={"channel"}),
	@SimpleIndex(members={"os"})
})
public class ClientExtendInfo{
	@SimpleId
	protected long id;
	
	@SimpleVersion
	protected int version;
	
	protected String clientId;
	protected String mobilenum;
	protected String username;
	protected String os;
	protected String channel;
	
	protected Date createtime = new Date();
	
	protected String lastLoginClientId="";
	protected String lastLoginMobilenum="";
	protected String lastLoginUsername="";
	protected String lastLoginOs="";
	protected String lastLoginChannel="";
	
	
	protected String memo1="";
	protected String memo2="";
	protected String memo3="";
	protected String attr1="";
	protected String attr2="";
	protected String attr3="";
	
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

	
	
	
	
	public String getMobilenum() {
		return mobilenum;
	}

	public void setMobilenum(String mobilenum) {
		this.mobilenum = mobilenum;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getLastLoginClientId() {
		return lastLoginClientId;
	}

	public void setLastLoginClientId(String lastLoginClientId) {
		this.lastLoginClientId = lastLoginClientId;
	}

	public String getLastLoginMobilenum() {
		return lastLoginMobilenum;
	}

	public void setLastLoginMobilenum(String lastLoginMobilenum) {
		this.lastLoginMobilenum = lastLoginMobilenum;
	}

	public String getLastLoginUsername() {
		return lastLoginUsername;
	}

	public void setLastLoginUsername(String lastLoginUsername) {
		this.lastLoginUsername = lastLoginUsername;
	}

	public String getLastLoginOs() {
		return lastLoginOs;
	}

	public void setLastLoginOs(String lastLoginOs) {
		this.lastLoginOs = lastLoginOs;
	}

	public String getLastLoginChannel() {
		return lastLoginChannel;
	}

	public void setLastLoginChannel(String lastLoginChannel) {
		this.lastLoginChannel = lastLoginChannel;
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

	public String getAttr1() {
		return attr1;
	}

	public void setAttr1(String attr1) {
		this.attr1 = attr1;
	}

	public String getAttr2() {
		return attr2;
	}

	public void setAttr2(String attr2) {
		this.attr2 = attr2;
	}

	public String getAttr3() {
		return attr3;
	}

	public void setAttr3(String attr3) {
		this.attr3 = attr3;
	}

	
	public String getLogString()
	{
		return "["+id+"] ["+clientId+"] ["+mobilenum+"] ["+username+"] ["+os+"] ["+channel+"]";
	}
	
/*	public void save() {
		try {
			StatManager.getInstance().em4ExtendInfo.save(this);
		} catch (Exception e) {
			StatManager.logger.error("[储存扩展信息] [出现异常] "+getLogString(),e);
		}
		
	}*/
}
