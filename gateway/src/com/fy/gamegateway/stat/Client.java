package com.fy.gamegateway.stat;

import java.util.ArrayList;
import java.util.Date;

import com.xuanzhi.tools.cache.CacheListener;
import com.xuanzhi.tools.cache.Cacheable;
import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndex;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

/**
 * 标识一个客户端
 * 
 * 记录此客户端相关的信息
 * 包括：
 * 
 * 1.客户端ID
 * 2.渠道标识
 * 3.平台标识
 * 4.资源标识
 * 
 * 机型标识
 * GPU标识
 * 
 * 5.首次联网客户端程序版本
 * 6.首次联网客户端资源版本
 * 7.首次联网服务器程序版本
 * 8.首次联网服务器资源版本
 * 9.首次联网时间
 * 
 * 10.是否有注册过
 * 11.首次注册时间
 * 12.是否有登录过
 * 13.首次登录时间
 * 
 * 14.最后一次联网客户端程序版本
 * 15.最后一次联网客户端资源版本
 * 16.最后一次联网服务器程序版本
 * 17.最后一次联网服务器资源版本
 * 18.最后一次联网时间
 * 
 * 19.最后一次登录时间
 * 20.最后一次登录帐号
 * 
 * 22.成功注册过的帐号
 * 21.成功登录过的帐号
 *
 * 23.成功注册过的帐号数
 * 24.成功登录过的帐号数
 * 
 * 25.登录次数
 * 
 * 
 * 增加一些资源更新的状态
 * 
 *
 */
@SimpleEntity
@SimpleIndices({
	@SimpleIndex(members={"channel"},compress=1),
	@SimpleIndex(members={"platform"},compress=1),
	@SimpleIndex(members={"clientId"}),
	@SimpleIndex(members={"timeOfFirstConnected"}),
	@SimpleIndex(members={"timeOfLastConnected"}),
	@SimpleIndex(members={"lastLoginTime"}),
	@SimpleIndex(members={"uuid"}),
	@SimpleIndex(members={"deviceId"}),
	@SimpleIndex(members={"macAddress"}),
	//@SimpleIndex(members={"lastLoginTime"}),
	@SimpleIndex(members={"lastLoginUsername"})
})
public class Client implements Cacheable,CacheListener{

	@SimpleId
	protected long id;
	
	@SimpleVersion
	protected int version;
	
	

	protected String clientId;
	protected String channel;
	protected String platform;

	protected String resourceName;
	
	protected String phoneType;
	protected String gpuType;
	
	protected String packageType;
	
	protected Date timeOfFirstConnected;
	
	protected String networkMo;
	
	@SimpleColumn(name="CPVOFC")
	protected String clientProgramVersionOfFirstConnected;
	
	@SimpleColumn(name="CRVOFC")
	protected String clientResourceVersionOfFirstConnected;
	
	@SimpleColumn(name="SPVOFC")
	protected String serverProgramVersionOfFirstConnected;
	
	@SimpleColumn(name="SRVOFC")
	protected String serverResourceVersionOfFirstConnected;
	
	@SimpleColumn(name="NTOFC")
	protected String networkTypeOfFirstConnected;
	
	protected Date timeOfLastConnected;
	
	@SimpleColumn(name="CPVOLC")
	protected String clientProgramVersionOfLastConnected;
	
	@SimpleColumn(name="CRVOLC")
	protected String clientResourceVersionOfLastConnected;
	
	@SimpleColumn(name="SPVOLC")
	protected String serverProgramVersionOfLastConnected;
	
	@SimpleColumn(name="SRVOLC")
	protected String serverResourceVersionOfLastConnected;

	@SimpleColumn(name="NTOLC")
	protected String networkTypeOfLastConnected;
	
	
	
	
	
	protected boolean hasRegister;
	protected Date firstRegisterTime;
	
	protected boolean hasLogin;
	protected Date firstLoginTime;
	
	
	protected Date lastLoginTime;
	protected String lastLoginUsername;
	
	protected int loginTimes;
	
	protected int successLoginUserAmount;
	protected int successRegisterUserAmount;
	
	protected ArrayList<String> successLoginUsernames  = new ArrayList<String>();
	protected ArrayList<String> successRegisterUsernames  = new ArrayList<String>();
	
	@SimpleColumn(length=128)
	String uuid;
	@SimpleColumn(length=128)
	String deviceId;
	@SimpleColumn(length=128)
	String imsi;
	@SimpleColumn(length=128)
	String macAddress;

	
	public static String 客户端更新状态列表[] = new String[]{"版本检查","无需更新","开始更新","更新完成"};
	
	@SimpleColumn(name="KHDGXZT")
	protected String 客户端更新状态 = "未知状态";
	
	@SimpleColumn(name="KHDGXBB")
	protected String 客户端更新版本;
	
	@SimpleColumn(name="KHDGXSC")
	protected long 客户端更新时长;
	
	
	public static String 资源拷贝状态列表[] = new String[]{"未知状态","无需拷贝","开始拷贝","拷贝完成"};
	
	@SimpleColumn(name="ZYKBZT")
	protected String 资源拷贝状态 = "未知状态";
	@SimpleColumn(name="ZYKBSC")
	protected long 资源拷贝时长;
	
	public static String 资源包下载状态列表[] = new String[]{"未知状态","无需下载","开始下载","下载完成"};
	
	@SimpleColumn(name="ZYBXZZT")
	protected String 资源包下载状态 = "未知状态";
	@SimpleColumn(name="ZYBXZBB")
	protected String 资源包下载版本;
	@SimpleColumn(name="ZYBXZDX")
	protected long 资源包下载大小;
	@SimpleColumn(name="ZYBXZSC")
	protected long 资源包下载时长;
	
	
	public static String 资源包解压状态列表[] = new String[]{"未知状态","无需解压","开始解压","解压完成","解压失败"};
	
	@SimpleColumn(name="ZYBJYZT")
	protected String 资源包解压状态 = "未知状态";
	@SimpleColumn(name="ZYBJYSC")
	protected long 资源包解压时长;
	
	
	public static String 逐个下载资源状态列表[] = new String[]{"未知状态","无需下载","开始下载","下载完成","下载失败"};
	
	@SimpleColumn(name="ZGXZZYZT")
	protected String 逐个下载资源状态 = "未知状态";
	@SimpleColumn(name="ZGXZZYSC")
	protected long 逐个下载资源已用时长;
	
	
	
	public String get客户端更新状态() {
		return 客户端更新状态;
	}
	public void set客户端更新状态(String 客户端更新状态) {
		this.客户端更新状态 = 客户端更新状态;
	}
	public String get客户端更新版本() {
		return 客户端更新版本;
	}
	public void set客户端更新版本(String 客户端更新版本) {
		this.客户端更新版本 = 客户端更新版本;
	}
	public long get客户端更新时长() {
		return 客户端更新时长;
	}
	public void set客户端更新时长(long 客户端更新时长) {
		this.客户端更新时长 = 客户端更新时长;
	}
	public String get资源拷贝状态() {
		return 资源拷贝状态;
	}
	public void set资源拷贝状态(String 资源拷贝状态) {
		this.资源拷贝状态 = 资源拷贝状态;
	}
	public long get资源拷贝时长() {
		return 资源拷贝时长;
	}
	public void set资源拷贝时长(long 资源拷贝时长) {
		this.资源拷贝时长 = 资源拷贝时长;
	}
	public String get资源包下载状态() {
		return 资源包下载状态;
	}
	public void set资源包下载状态(String 资源包下载状态) {
		this.资源包下载状态 = 资源包下载状态;
	}
	public String get资源包下载版本() {
		return 资源包下载版本;
	}
	public void set资源包下载版本(String 资源包下载版本) {
		this.资源包下载版本 = 资源包下载版本;
	}
	public long get资源包下载大小() {
		return 资源包下载大小;
	}
	public void set资源包下载大小(long 资源包下载大小) {
		this.资源包下载大小 = 资源包下载大小;
	}
	public long get资源包下载时长() {
		return 资源包下载时长;
	}
	public void set资源包下载时长(long 资源包下载时长) {
		this.资源包下载时长 = 资源包下载时长;
	}
	public String get资源包解压状态() {
		return 资源包解压状态;
	}
	public void set资源包解压状态(String 资源包解压状态) {
		this.资源包解压状态 = 资源包解压状态;
	}
	public long get资源包解压时长() {
		return 资源包解压时长;
	}
	public void set资源包解压时长(long 资源包解压时长) {
		this.资源包解压时长 = 资源包解压时长;
	}
	public String get逐个下载资源状态() {
		return 逐个下载资源状态;
	}
	public void set逐个下载资源状态(String 逐个下载资源状态) {
		this.逐个下载资源状态 = 逐个下载资源状态;
	}
	public long get逐个下载资源已用时长() {
		return 逐个下载资源已用时长;
	}
	public void set逐个下载资源已用时长(long 逐个下载资源已用时长) {
		this.逐个下载资源已用时长 = 逐个下载资源已用时长;
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
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public Date getTimeOfFirstConnected() {
		return timeOfFirstConnected;
	}
	public void setTimeOfFirstConnected(Date timeOfFirstConnected) {
		this.timeOfFirstConnected = timeOfFirstConnected;
	}
	public String getClientProgramVersionOfFirstConnected() {
		return clientProgramVersionOfFirstConnected;
	}
	public void setClientProgramVersionOfFirstConnected(
			String clientProgramVersionOfFirstConnected) {
		this.clientProgramVersionOfFirstConnected = clientProgramVersionOfFirstConnected;
	}
	public String getClientResourceVersionOfFirstConnected() {
		return clientResourceVersionOfFirstConnected;
	}
	public void setClientResourceVersionOfFirstConnected(
			String clientResourceVersionOfFirstConnected) {
		this.clientResourceVersionOfFirstConnected = clientResourceVersionOfFirstConnected;
	}
	public String getServerProgramVersionOfFirstConnected() {
		return serverProgramVersionOfFirstConnected;
	}
	public void setServerProgramVersionOfFirstConnected(
			String serverProgramVersionOfFirstConnected) {
		this.serverProgramVersionOfFirstConnected = serverProgramVersionOfFirstConnected;
	}
	public String getServerResourceVersionOfFirstConnected() {
		return serverResourceVersionOfFirstConnected;
	}
	public void setServerResourceVersionOfFirstConnected(
			String serverResourceVersionOfFirstConnected) {
		this.serverResourceVersionOfFirstConnected = serverResourceVersionOfFirstConnected;
	}
	public Date getTimeOfLastConnected() {
		return timeOfLastConnected;
	}
	public void setTimeOfLastConnected(Date timeOfLastConnected) {
		this.timeOfLastConnected = timeOfLastConnected;
	}
	public String getClientProgramVersionOfLastConnected() {
		return clientProgramVersionOfLastConnected;
	}
	public void setClientProgramVersionOfLastConnected(
			String clientProgramVersionOfLastConnected) {
		this.clientProgramVersionOfLastConnected = clientProgramVersionOfLastConnected;
	}
	public String getClientResourceVersionOfLastConnected() {
		return clientResourceVersionOfLastConnected;
	}
	public void setClientResourceVersionOfLastConnected(
			String clientResourceVersionOfLastConnected) {
		this.clientResourceVersionOfLastConnected = clientResourceVersionOfLastConnected;
	}
	public String getServerProgramVersionOfLastConnected() {
		return serverProgramVersionOfLastConnected;
	}
	public void setServerProgramVersionOfLastConnected(
			String serverProgramVersionOfLastConnected) {
		this.serverProgramVersionOfLastConnected = serverProgramVersionOfLastConnected;
	}
	public String getServerResourceVersionOfLastConnected() {
		return serverResourceVersionOfLastConnected;
	}
	public void setServerResourceVersionOfLastConnected(
			String serverResourceVersionOfLastConnected) {
		this.serverResourceVersionOfLastConnected = serverResourceVersionOfLastConnected;
	}
	public boolean isHasRegister() {
		return hasRegister;
	}
	public void setHasRegister(boolean hasRegister) {
		this.hasRegister = hasRegister;
	}
	public Date getFirstRegisterTime() {
		return firstRegisterTime;
	}
	public void setFirstRegisterTime(Date firstRegisterTime) {
		this.firstRegisterTime = firstRegisterTime;
	}
	public boolean isHasLogin() {
		return hasLogin;
	}
	public void setHasLogin(boolean hasLogin) {
		this.hasLogin = hasLogin;
	}
	public Date getFirstLoginTime() {
		return firstLoginTime;
	}
	public void setFirstLoginTime(Date firstLoginTime) {
		this.firstLoginTime = firstLoginTime;
	}
	public Date getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public String getLastLoginUsername() {
		return lastLoginUsername;
	}
	public void setLastLoginUsername(String lastLoginUsername) {
		this.lastLoginUsername = lastLoginUsername;
	}
	public int getLoginTimes() {
		return loginTimes;
	}
	public void setLoginTimes(int loginTimes) {
		this.loginTimes = loginTimes;
	}
	public int getSuccessLoginUserAmount() {
		return successLoginUserAmount;
	}
	public void setSuccessLoginUserAmount(int successLoginUserAmount) {
		this.successLoginUserAmount = successLoginUserAmount;
	}
	public int getSuccessRegisterUserAmount() {
		return successRegisterUserAmount;
	}
	public void setSuccessRegisterUserAmount(int successRegisterUserAmount) {
		this.successRegisterUserAmount = successRegisterUserAmount;
	}
	public ArrayList<String> getSuccessLoginUsernames() {
		return successLoginUsernames;
	}
	public void setSuccessLoginUsernames(ArrayList<String> successLoginUsernames) {
		this.successLoginUsernames = successLoginUsernames;
	}
	public ArrayList<String> getSuccessRegisterUsernames() {
		return successRegisterUsernames;
	}
	public void setSuccessRegisterUsernames(
			ArrayList<String> successRegisterUsernames) {
		this.successRegisterUsernames = successRegisterUsernames;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public String getPhoneType() {
		return phoneType;
	}
	public void setPhoneType(String phoneType) {
		this.phoneType = phoneType;
	}
	public String getGpuType() {
		return gpuType;
	}
	public void setGpuType(String gpuType) {
		this.gpuType = gpuType;
	}
	public String getPackageType() {
		return packageType;
	}
	public void setPackageType(String packageType) {
		this.packageType = packageType;
	}
	public String getNetworkTypeOfFirstConnected() {
		return networkTypeOfFirstConnected;
	}
	public void setNetworkTypeOfFirstConnected(String networkTypeOfFirstConnected) {
		this.networkTypeOfFirstConnected = networkTypeOfFirstConnected;
	}
	public String getNetworkTypeOfLastConnected() {
		return networkTypeOfLastConnected;
	}
	public void setNetworkTypeOfLastConnected(String networkTypeOfLastConnected) {
		this.networkTypeOfLastConnected = networkTypeOfLastConnected;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getImsi() {
		return imsi;
	}
	public void setImsi(String imsi) {
		this.imsi = imsi;
	}
	public String getMacAddress() {
		return macAddress;
	}
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}
	
	public int getSize(){
		return 1;
	}

	public void remove(int type) {
		
//		try {
//			StatManager.getInstance().em4Client.save(this);
//		} catch (Exception e) {
//			StatManager.logger.error("[处理Client从Cache中移除] [出现异常] [clientId:"+clientId+"]",e);
//		}
		
	}
	
	
	
	
}
