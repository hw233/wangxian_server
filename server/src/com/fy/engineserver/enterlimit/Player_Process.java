package com.fy.engineserver.enterlimit;

public class Player_Process {

	private long playerID;

	private String platform;

	private String clientID;

	private String otherGPU;

	private String realPlatform;

	private String[] androidProcesss;

	private boolean isMoniqi;

	public static String[] moniqiArr = {
		"bluestacks", 
		"cn.xiaolin", 
		"com.sdkkdgjk2.dkdgjkgjk3", 
		"com.djkjdg45g.ckkbnvmg", 
		"net.aisence.Touchelper", 
		"com.xl.wxconfig", 
		"com.touchscripts.wxhanhua", 
		"org.evan.evan",
		"org.evan.elf",
		"com.wwangxianjb.evan_config", 
		"org.onaips.vnc",
		"com.cih.game_cih",
		"com.cih.gamecih2",
		"com.mh.bmsq",
		"org.sbtools.gamehack",
		"cn.mc1.sq",
		"idv.aqua.bulldog",
		"cn.mc.sq",
		"com.android.xxx",
		"com.saitesoft.gamecheater",
		"com.muzhiwan.gamehelper.memorymanager",
		"com.company.appname",
		};

	public void modifyMoniqi() {
		isMoniqi = false;
		if (androidProcesss == null) {
			return;
		}
		int isHave = 0;
		if (!platform.equals("Android")) {
			isHave = 1;
		}
		for (String processName : androidProcesss) {
			if (processName.indexOf("如花") >= 0) {
				isHave = isHave + 1;
			}
		}
		for (String moniqi : moniqiArr) {
			for (String processName : androidProcesss) {
				if (processName.indexOf(moniqi) >= 0) {
					isMoniqi = true;
					return;
				}
			}
		}
		if (isHave == 0 || isHave >= 3){
			isMoniqi = true;
		}
	}

	public String getLogString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[id=").append(playerID).append("] ");
		sb.append("[pf=").append(platform).append("] ");
		sb.append("[cid=").append(clientID).append("] ");
		sb.append("[GPU=").append(otherGPU).append("] ");
		sb.append("[RPF=").append(realPlatform).append("] ");
		sb.append("[isMoniqi=").append(isMoniqi).append("] ");
		sb.append("[processs=");
		if (androidProcesss != null) {
			sb.append(androidProcesss.length).append(" ");
			for (int i = 0; i < androidProcesss.length; i++) {
				sb.append(androidProcesss[i]).append(";");
			}
		} else {
			sb.append("null");
		}
		sb.append("]");
		return sb.toString();
	}

	public void setPlayerID(long playerID) {
		this.playerID = playerID;
	}

	public long getPlayerID() {
		return playerID;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getPlatform() {
		return platform;
	}

	public void setOtherGPU(String otherGPU) {
		this.otherGPU = otherGPU;
	}

	public String getOtherGPU() {
		return otherGPU;
	}

	public void setRealPlatform(String realPlatform) {
		this.realPlatform = realPlatform;
	}

	public String getRealPlatform() {
		return realPlatform;
	}

	public void setAndroidProcesss(String[] androidProcesss) {
		this.androidProcesss = androidProcesss;
	}

	public String[] getAndroidProcesss() {
		return androidProcesss;
	}

	public void setClientID(String clientID) {
		this.clientID = clientID;
	}

	public String getClientID() {
		return clientID;
	}

	public boolean isMoniqi() {
		return isMoniqi;
	}

	public void setMoniqi(boolean isMoniqi) {
		this.isMoniqi = isMoniqi;
	}

}
