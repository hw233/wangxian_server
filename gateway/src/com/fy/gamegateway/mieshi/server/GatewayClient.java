package com.fy.gamegateway.mieshi.server;

import java.util.concurrent.ConcurrentHashMap;

import com.xuanzhi.tools.transport.Connection;
import java.util.*;

public class GatewayClient {
	private String clientId = "";
	
	//private Connection connection;
	
	private int sendNum;
	
	private int receiveNum;
	
	private long lastReceiveTime;
	
	private long lastSendTime;
	
	private String step = "";
	
	private int stepTotal;
	
	private int stepNow;
	
	private String passport  = "";
	
	/**
	 * 渠道
	 */
	private String channel = "";
	

	private String platform = "";
	private String gpu = "";
	private String clientCurrentProgramVersion = "";
	private String clientCurrentResourceVersion = "";
	private String phoneType = "";
	
	private String gpuOtherName = "";
	
	private String networkMode = "";
	
	String uuid = "";
	String deviceId ="";
	String imsi = "";
	String macAddress ="";

	
	public String getNetworkMode() {
		return networkMode;
	}

	public void setNetworkMode(String networkMode) {
		this.networkMode = networkMode;
	}

	public String getLogStr(){
		return "[client:"+clientId+"] [username:"+passport+"] [channel:"+channel+"] [platform:"+platform+"] [gpu:"+gpu+"] [uuid:"+uuid+"] [deviceId:"+deviceId+"] [macaddress:"+this.macAddress+"] [imsi:"+imsi+"] [pv:"+clientCurrentProgramVersion+"] [rv:"+clientCurrentResourceVersion+"] [phoneType:"+phoneType+"] [gpuOtherName:"+gpuOtherName+"]";
	}
	
	public String getGpuOtherName() {
		return gpuOtherName;
	}

	public void setGpuOtherName(String gpuOtherName) {
		this.gpuOtherName = gpuOtherName;
	}

	public GatewayClient(String clientId) {
		this.clientId = clientId;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	//public Connection getConnection() {
	//	return connection;
	//}

	//public void setConnection(Connection connection) {
	//	this.connection = connection;
	//}

	public int getSendNum() {
		return sendNum;
	}

	public void setSendNum(int sendNum) {
		this.sendNum = sendNum;
	}

	public int getReceiveNum() {
		return receiveNum;
	}

	public void setReceiveNum(int receiveNum) {
		this.receiveNum = receiveNum;
	}

	public long getLastReceiveTime() {
		return lastReceiveTime;
	}

	public void setLastReceiveTime(long lastReceiveTime) {
		this.lastReceiveTime = lastReceiveTime;
	}

	public long getLastSendTime() {
		return lastSendTime;
	}

	public void setLastSendTime(long lastSendTime) {
		this.lastSendTime = lastSendTime;
	}

	public String getStep() {
		return step;
	}

	public void setStep(String step) {
		this.step = step;
	}

	public int getStepTotal() {
		return stepTotal;
	}

	public void setStepTotal(int stepTotal) {
		this.stepTotal = stepTotal;
	}

	public int getStepNow() {
		return stepNow;
	}

	public void setStepNow(int stepNow) {
		this.stepNow = stepNow;
	}

	public String getPassport() {
		return passport;
	}

	public void setPassport(String passport) {
		this.passport = passport;
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

	public String getGpu() {
		return gpu;
	}

	public void setGpu(String gpu) {
		this.gpu = gpu;
	}

	public String getClientCurrentProgramVersion() {
		return clientCurrentProgramVersion;
	}

	public void setClientCurrentProgramVersion(String clientCurrentProgramVersion) {
		this.clientCurrentProgramVersion = clientCurrentProgramVersion;
	}

	public String getClientCurrentResourceVersion() {
		return clientCurrentResourceVersion;
	}

	public void setClientCurrentResourceVersion(String clientCurrentResourceVersion) {
		this.clientCurrentResourceVersion = clientCurrentResourceVersion;
	}

	public String getPhoneType() {
		return phoneType;
	}

	public void setPhoneType(String phoneType) {
		this.phoneType = phoneType;
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
}
