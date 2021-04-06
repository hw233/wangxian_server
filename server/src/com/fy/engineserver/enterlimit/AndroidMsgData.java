package com.fy.engineserver.enterlimit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class AndroidMsgData {
	//REQ   info  clientID,platform,rPlatform,fullGpu,channel,mac
	private ArrayList<String> reqInfos = new ArrayList<String>();
	
	private HashMap<String, Long> sendTimes = new HashMap<String, Long>();
	
	private long pID;
	
	//mac地址
	private String mac;
	//进程
	private String[] processNames;
	//电池温度
	private int batteryTemperature;
	//电池毫安数
	private int batteryMA;
	//电池容量  先进先出
	private int[] batteryValue = new int[10];
	//netType
	private int netType;
	//netName
	private String netName;
	//wifi名字		SSID
	private String wifiName;
	//wifi信号强度 先进先出
	private int[] wifiRssi = new int[10];
	//OS arch
	private String arch;
	//包的签名
	private String android_rsa;
	


	public AndroidMsgData() {
		netType = -1000;
		batteryTemperature = -1000;
		batteryMA = -1000;
		netName = "没";
		wifiName = "没";
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer("");
		sb.append("[").append(pID).append("] ");
		sb.append("[").append(reqInfos).append("] ");
		sb.append("[进程:").append(Arrays.toString(processNames)).append("] ");
		sb.append("[电池温度:").append(batteryTemperature).append("] ");
		sb.append("[电池毫安:").append(batteryMA).append("] ");
		sb.append("[电池容量:").append(Arrays.toString(batteryValue)).append("] ");
		sb.append("[联网类型:").append(netType).append("] ");
		sb.append("[名字:").append(netName).append("] ");
		sb.append("[wifi名字:").append(wifiName).append("] ");
		sb.append("[wifi信号:").append(Arrays.toString(wifiRssi)).append("] ");
		return sb.toString();
	}
	
	public void setBatteryTemperature(int batteryTemperature) {
		this.batteryTemperature = batteryTemperature;
	}
	public int getBatteryTemperature() {
		return batteryTemperature;
	}
	public void setBatteryMA(int batteryMA) {
		this.batteryMA = batteryMA;
	}
	public int getBatteryMA() {
		return batteryMA;
	}
	public void setBatteryValue(int[] batteryValue) {
		this.batteryValue = batteryValue;
	}
	public int[] getBatteryValue() {
		return batteryValue;
	}
	public void addBatteryValue(int v) {
		for (int i = 0; i < batteryValue.length; i++) {
			if (batteryValue[i] == 0) {
				batteryValue[i] = v;
				return;
			}
		}
		int[] cp = new int[batteryValue.length];
		for (int i = 0; i < batteryValue.length-1; i++) {
			cp[i] = batteryValue[i+1];
		}
		cp[cp.length-1] = v;
		batteryValue = cp;
	}
	public void setProcessNames(String[] processNames) {
		this.processNames = processNames;
	}
	public String[] getProcessNames() {
		return processNames;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getMac() {
		return mac;
	}
	public void setNetType(int netType) {
		this.netType = netType;
	}
	public int getNetType() {
		return netType;
	}
	public void setNetName(String netName) {
		this.netName = netName;
	}
	public String getNetName() {
		return netName;
	}
	public void setWifiName(String wifiName) {
		this.wifiName = wifiName;
	}
	public String getWifiName() {
		return wifiName;
	}
	public void setWifiRssi(int[] wifiRssi) {
		this.wifiRssi = wifiRssi;
	}
	public int[] getWifiRssi() {
		return wifiRssi;
	}
	public String getAndroid_rsa() {
		return android_rsa;
	}

	public void setAndroid_rsa(String android_rsa) {
		this.android_rsa = android_rsa;
	}
	public void addWifiRssi(int v) {
		for (int i = 0; i < wifiRssi.length; i++) {
			if (wifiRssi[i] == 0) {
				wifiRssi[i] = v;
				return;
			}
		}
		int[] cp = new int[wifiRssi.length];
		for (int i = 0; i < wifiRssi.length-1; i++) {
			cp[i] = wifiRssi[i+1];
		}
		cp[cp.length-1] = v;
		wifiRssi = cp;
	}
	
	public void setReqInfos(ArrayList<String> reqInfos) {
		this.reqInfos = reqInfos;
	}
	public ArrayList<String> getReqInfos() {
		return reqInfos;
	}

	public void setpID(long pID) {
		this.pID = pID;
	}

	public long getpID() {
		return pID;
	}

	public void setSendTimes(HashMap<String, Long> sendTimes) {
		this.sendTimes = sendTimes;
	}

	public HashMap<String, Long> getSendTimes() {
		return sendTimes;
	}

	public void setArch(String arch) {
		this.arch = arch;
	}

	public String getArch() {
		return arch;
	}
	
}
