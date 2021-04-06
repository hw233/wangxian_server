package com.fy.engineserver.hook;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PlayerHookInfo {

	private long playerID;				//玩家ID
	
	private boolean isinHook;			//开始挂机
	
	private boolean isAutoAtt;			//自动打怪
	
	private boolean isUseYinZi;			//是否使用银子买药
	
	private String playerHpName;			//HP恢复物品名字
	
	private String playerMpName;			//MP恢复物品名字
	
	private long startHookTime;			//开始挂机时间
	
	private long startAutoTime;			//开始自动打怪时间
	
	private long endAutoTime;			//上次结束自动打怪时间
	
	private long lastTime;				//最后访问时间

	public void setPlayerID(long playerID) {
		this.playerID = playerID;
	}

	public long getPlayerID() {
		return playerID;
	}

	public void setIsinHook(boolean isinHook) {
		this.isinHook = isinHook;
	}

	public boolean isIsinHook() {
		return isinHook;
	}

	public void setIsAutoAtt(boolean isAutoAtt) {
		this.isAutoAtt = isAutoAtt;
	}

	public boolean isIsAutoAtt() {
		return isAutoAtt;
	}

	public void setIsUseYinZi(boolean isUseYinZi) {
		this.isUseYinZi = isUseYinZi;
	}

	public boolean isIsUseYinZi() {
		return isUseYinZi;
	}

	public void setPlayerHpName(String playerHpName) {
		this.playerHpName = playerHpName;
	}

	public String getPlayerHpName() {
		return playerHpName;
	}

	public void setPlayerMpName(String playerMpName) {
		this.playerMpName = playerMpName;
	}

	public String getPlayerMpName() {
		return playerMpName;
	}

	public void setStartHookTime(long startHookTime) {
		this.startHookTime = startHookTime;
	}

	public long getStartHookTime() {
		return startHookTime;
	}

	public void setStartAutoTime(long startAutoTime) {
		this.startAutoTime = startAutoTime;
	}

	public long getStartAutoTime() {
		return startAutoTime;
	}

	public void setEndAutoTime(long endAutoTime) {
		this.endAutoTime = endAutoTime;
	}

	public long getEndAutoTime() {
		return endAutoTime;
	}

	public void setLastTime(long lastTime) {
		this.lastTime = lastTime;
	}

	public long getLastTime() {
		return lastTime;
	}
	
	public String getLogString() {
		StringBuffer sb = new StringBuffer("");
		sb.append(" [pid:").append(getPlayerID()).append("]");
		sb.append(" [挂机:").append(isIsinHook()).append("]");
		sb.append(" [自动打怪:").append(isIsAutoAtt()).append("]");
		sb.append(" [绑银不够使用银子:").append(isIsUseYinZi()).append("]");
		sb.append(" [HP:").append(getPlayerHpName()).append("]");
		sb.append(" [MP:").append(getPlayerMpName()).append("]");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sb.append(" [开始:").append(format.format(new Date(getStartHookTime()))).append("]");
		sb.append(" [开始自动打怪:").append(new Date(getStartAutoTime())).append("]");
		sb.append(" [结束自动打怪:").append(new Date(getEndAutoTime())).append("]");
		return sb.toString();
	}
	
}
