package com.fy.boss.gm.newfeedback;

import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndex;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

@SimpleEntity
@SimpleIndices({
	@SimpleIndex(members={"playerState"}),@SimpleIndex(members={"username"}),@SimpleIndex(members={"state"})
})
public class NewFeedback {

	@SimpleId
	private long id;

	@SimpleVersion
	private int version;
	
	//等待回复，有新回复，评分
	@SimpleColumn(saveInterval=1) 
	private int playerState;
	
	//排队数量
	@SimpleColumn(saveInterval=1) 
	private int waitCount;
	
	//内容前几个字作为标题
	@SimpleColumn(saveInterval=1) 
	private String title;
	
	//反馈状态，0:玩家删除过的，1：玩家显示的，2：系统删除
	@SimpleColumn(saveInterval=1) 
	private int state;
	
	private String username;

	private String sendtime;
	@SimpleColumn(saveInterval=1) 
	private int viplevel;
	
	private String playername;
	
	private String servername;
	
	//是否在队列中，服务器重启的时候还原队列信息.0:不是：1:是
	@SimpleColumn(saveInterval=1) 
	private int isInQueue;
	
	//是否是正在处理的反馈,服务器重启的时候还原队列信息0:不是：1:是
	@SimpleColumn(saveInterval=1) 
	private int isHandleNow;
	@SimpleColumn(saveInterval=1) 
	private String gmHandler;
	
	//GM状态1：删除,2:挂起
	@SimpleColumn(saveInterval=1) 
	private int gmstat;
	
	//评分状态0：关闭 1：极佳 2：很好 3：一般
	private int scorestate;
	
	//上次挂起时间,挂起时间超过30分钟，系统会自动关闭该反馈
	private long gqtime;
	@SimpleColumn(saveInterval=1) 
	private long lastReplyTime;
	
	private int isnewFeedback;
	
	public int getIsnewFeedback() {
		return isnewFeedback;
	}

	public void setIsnewFeedback(int isnewFeedback) {
		this.isnewFeedback = isnewFeedback;
	}

	public long getLastReplyTime() {
		return lastReplyTime;
	}

	public void setLastReplyTime(long lastReplyTime) {
		this.lastReplyTime = lastReplyTime;
	}

	public long getGqtime() {
		return gqtime;
	}

	public void setGqtime(long gqtime) {
		this.gqtime = gqtime;
	}

	public int getScorestate() {
		return scorestate;
	}

	public void setScorestate(int scorestate) {
		this.scorestate = scorestate;
	}

	public int getGmstat() {
		return gmstat;
	}

	public void setGmstat(int gmstat) {
		this.gmstat = gmstat;
	}

	public String getGmHandler() {
		return gmHandler;
	}

	public void setGmHandler(String gmHandler) {
		this.gmHandler = gmHandler;
	}

	public int getIsInQueue() {
		return isInQueue;
	}

	public void setIsInQueue(int isInQueue) {
		this.isInQueue = isInQueue;
	}

	public int getIsHandleNow() {
		return isHandleNow;
	}

	public void setIsHandleNow(int isHandleNow) {
		this.isHandleNow = isHandleNow;
	}

	public String getServername() {
		return servername;
	}

	public void setServername(String servername) {
		this.servername = servername;
	}

	public String getPlayername() {
		return playername;
	}

	public void setPlayername(String playername) {
		this.playername = playername;
	}

	public long getViplevel() {
		return viplevel;
	}

	public void setViplevel(int viplevel) {
		this.viplevel = viplevel;
	}

	public String getSendtime() {
		return sendtime;
	}

	public void setSendtime(String sendtime) {
		this.sendtime = sendtime;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	
	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getPlayerState() {
		return playerState;
	}

	public void setPlayerState(int playerState) {
		this.playerState = playerState;
	}

	public int getWaitCount() {
		return waitCount;
	}

	public void setWaitCount(int waitCount) {
		this.waitCount = waitCount;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	
	
}
