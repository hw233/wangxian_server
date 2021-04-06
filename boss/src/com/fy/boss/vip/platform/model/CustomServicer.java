package com.fy.boss.vip.platform.model;

import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

@SimpleEntity(name="CustomServicer")
public class CustomServicer {
	@SimpleId
	private long id;
	
	@SimpleVersion
	private int version;
	
	
	private String nickname;
	
	private String loginName;
	
	private String realName;
	
	private String memo;
	private String memo1;
	private String memo2;
	private String memo3;
	private String memo4;
	private String memo5;
	private String memo6;
	private String memo7;
	private String memo8;
	
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
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
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
	public String getMemo4() {
		return memo4;
	}
	public void setMemo4(String memo4) {
		this.memo4 = memo4;
	}
	public String getMemo5() {
		return memo5;
	}
	public void setMemo5(String memo5) {
		this.memo5 = memo5;
	}
	public String getMemo6() {
		return memo6;
	}
	public void setMemo6(String memo6) {
		this.memo6 = memo6;
	}
	public String getMemo7() {
		return memo7;
	}
	public void setMemo7(String memo7) {
		this.memo7 = memo7;
	}
	public String getMemo8() {
		return memo8;
	}
	public void setMemo8(String memo8) {
		this.memo8 = memo8;
	}
	
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public String getLogString()
	{
		return "["+id+"] ["+version+"] ["+nickname+"] ["+loginName+"] ["+(realName == null ? "" : realName)+"]";
	}
	

}
