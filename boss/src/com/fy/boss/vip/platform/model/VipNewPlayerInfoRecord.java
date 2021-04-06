package com.fy.boss.vip.platform.model;

import com.fy.boss.vip.platform.model.Descript;
import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndex;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

@SimpleEntity(name="VipNewPlayerInfoRecord")
@SimpleIndices({
	@SimpleIndex(members={"username"},name="INDX_USERNAME_NEW"),
	@SimpleIndex(members={"createTime"},name="INDX_CREATETIME_NEW"),
	@SimpleIndex(members={"realname"},name="INDX_REALNAME_NEW"),
	@SimpleIndex(members={"platform"},name="INDX_PLATFORM_NEW"),
	@SimpleIndex(members={"serverName"},name="INDX_SERVERNAME_NEW"),
	@SimpleIndex(members={"playerName"},name="INDX_PLAYERNAME_NEW"),
	@SimpleIndex(members={"vipLevel"},name="INDX_VIPLEVEL_NEW"),
	@SimpleIndex(members={"playerLevel"},name="INDX_PLAYERLEVEL_NEW"),
	@SimpleIndex(members={"auditor"},name="INDX_AUDITOR_NEW"),
	@SimpleIndex(members={"operator"},name="INDX_OPERATOR_NEW")
})
public class VipNewPlayerInfoRecord {
	// Fields
	@SimpleId
	private long id;
	
	@SimpleVersion
	private int version;
	
	@SimpleColumn(length=100)
	private String realname;
	
	@SimpleColumn(length=100)
	private String phone;
	
	@SimpleColumn(length=100)
	private String qq;
	
	@SimpleColumn(length=100)
	private String birthday;
	
//	private long birthtime;
	
	private int vipLevel;
	
	private String username;
	
	@SimpleColumn(length=100)
	private String platform;
	
	private String playerName;
	
	private int playerLevel;
	
	private int sex;
	@SimpleColumn(length=20)
	private String province;
	
	@SimpleColumn(length=20)
	private String city;
	
	@SimpleColumn(length=100)
	private String favor;
	
	@SimpleColumn(length=100)
	private String manager4vip;
	
	private String gameCountry;
	
	
	private String address;
	
	@SimpleColumn(length=100)
	private String serverName;
	
	private long windowPopTime;
	
	private int infoFilled;
	
	private int windowPopedNum;
	
	@SimpleColumn(length=1000)
	private String contactAddress;
	
	@SimpleColumn(name="descr",length=50000)
	private Descript desc;

	private String memo;

	private String memo1;

	private String memo2;

	private String memo3;
	
	@SimpleColumn(length=100)
	private String auditor;
	
	@SimpleColumn(length=100)
	private String operator;
	
	//分配状态 0 未分配 1 已分配
	private int privValue;
	
	private long createTime;
	
	private long updateTime;
	
	public static int NORMAL_VIP_RECORD = 0;
	
	public static int ORDER_VIP_RECORD = 1;
	
	//类似于vip记录的统一用这个entity 以recordType做区别
	private int recordType = NORMAL_VIP_RECORD;
	

	public int getRecordType() {
		return recordType;
	}

	public void setRecordType(int recordType) {
		this.recordType = recordType;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
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

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public int getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public int getPlayerLevel() {
		return playerLevel;
	}

	public void setPlayerLevel(int playerLevel) {
		this.playerLevel = playerLevel;
	}

	public String getGameCountry() {
		return gameCountry;
	}

	public void setGameCountry(String gameCountry) {
		this.gameCountry = gameCountry;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public long getWindowPopTime() {
		return windowPopTime;
	}

	public void setWindowPopTime(long windowPopTime) {
		this.windowPopTime = windowPopTime;
	}

	public int getInfoFilled() {
		return infoFilled;
	}

	public void setInfoFilled(int infoFilled) {
		this.infoFilled = infoFilled;
	}

	public int getWindowPopedNum() {
		return windowPopedNum;
	}

	public void setWindowPopedNum(int windowPopedNum) {
		this.windowPopedNum = windowPopedNum;
	}

	public String getContactAddress() {
		return contactAddress;
	}

	public void setContactAddress(String contactAddress) {
		this.contactAddress = contactAddress;
	}

	public Descript getDesc() {
		return desc;
	}

	public void setDesc(Descript desc) {
		this.desc = desc;
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

	public String getAuditor() {
		return auditor;
	}

	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public int getPrivValue() {
		return privValue;
	}

	public void setPrivValue(int privValue) {
		this.privValue = privValue;
	}
	
	
	
	
	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getFavor() {
		return favor;
	}

	public void setFavor(String favor) {
		this.favor = favor;
	}

	public String getManager4vip() {
		return manager4vip;
	}

	public void setManager4vip(String manager4vip) {
		this.manager4vip = manager4vip;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	

/*	public long getBirthtime() {
		return birthtime;
	}

	public void setBirthtime(long birthtime) {
		this.birthtime = birthtime;
	}*/

	public String getLogString()
	{
		return "["+id+"] ["+version+"] ["+realname+"] ["+phone+"] ["+qq+"] ["+birthday+"] ["+vipLevel+"] ["+username+"] ["+platform+"] ["+playerName+"] ["+playerLevel+"] ["+serverName+"] ["+gameCountry+"] ["+windowPopedNum+"] ["+windowPopTime+"] ["+infoFilled+"] ["+contactAddress+"] ["+operator+"] ["+auditor+"] ["+privValue+"] ["+sex+"] ["+address+"] ["+city+"] ["+province+"] ["+manager4vip+"] ["+recordType+"]";
	}
	
	
}
