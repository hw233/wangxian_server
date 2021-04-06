package com.fy.gamegateway.getbackpassport;

import java.util.Date;

import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndex;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;



/**
 * 一条密保记录：
 * 
 * 通行证id
 * clientId
 * 当前渠道
 * 手机机型
 * 大区
 * 服务器
 * 角色名
 * 手机号
 * 账户名
 * 密保问题
 * 密保答案
 * 最后一次充值日期
 * 最后一次充值金额
 * 最后一次登录日期
 * 状态
 * 记录提交时间
 * *记录次数值
 * *记录最近一次点击提交的时间
 * 
 * @author wtx
 *
 */
@SimpleEntity
@SimpleIndices({
	@SimpleIndex(members={"passportid"},compress=1),
	@SimpleIndex(members={"name"},compress=1),
	@SimpleIndex(members={"telnumber"}),
	@SimpleIndex(members={"username"}),
	@SimpleIndex(members={"servername"}),
	@SimpleIndex(members={"state"})
})
public class PassportRecord {

	@SimpleId
	protected long id;
	
	@SimpleVersion
	protected int version;
	
	protected long passportid;
	
	protected String clientId;
	
	protected String mobiletype;
	
	protected String areaname;
	
	protected String name;
	
	protected String servername;
	
	protected String telnumber;
	
	protected String username;
	
	protected String passportquestion;
	
	protected String passportanswer;
	
	protected String lastchargedate;
	
	protected String lastchargeamount;
	
	protected String lastlogindate;
	
	protected String state;
	
	protected Date committime;
	
	protected String channel;
	
	
	public long getPassportid() {
		return passportid;
	}

	public void setPassportid(long passportid) {
		this.passportid = passportid;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getMobiletype() {
		return mobiletype;
	}

	public void setMobiletype(String mobiletype) {
		this.mobiletype = mobiletype;
	}

	public String getAreaname() {
		return areaname;
	}

	public void setAreaname(String areaname) {
		this.areaname = areaname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getServername() {
		return servername;
	}

	public void setServername(String servername) {
		this.servername = servername;
	}

	public String getTelnumber() {
		return telnumber;
	}

	public void setTelnumber(String telnumber) {
		this.telnumber = telnumber;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassportquestion() {
		return passportquestion;
	}

	public void setPassportquestion(String passportquestion) {
		this.passportquestion = passportquestion;
	}

	public String getPassportanswer() {
		return passportanswer;
	}

	public void setPassportanswer(String passportanswer) {
		this.passportanswer = passportanswer;
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

	public String getLastchargedate() {
		return lastchargedate;
	}

	public void setLastchargedate(String lastchargedate) {
		this.lastchargedate = lastchargedate;
	}

	public String getLastchargeamount() {
		return lastchargeamount;
	}

	public void setLastchargeamount(String lastchargeamount) {
		this.lastchargeamount = lastchargeamount;
	}

	public String getLastlogindate() {
		return lastlogindate;
	}

	public void setLastlogindate(String lastlogindate) {
		this.lastlogindate = lastlogindate;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Date getCommittime() {
		return committime;
	}

	public void setCommittime(Date committime) {
		this.committime = committime;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}
	
	
}
