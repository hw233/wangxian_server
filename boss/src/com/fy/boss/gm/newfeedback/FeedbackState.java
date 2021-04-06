package com.fy.boss.gm.newfeedback;

import com.fy.boss.gm.newfeedback.service.NewFeedbackStateManager;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndex;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

@SimpleEntity
@SimpleIndices({
	@SimpleIndex(members={"gmnum"})
})
public class FeedbackState {

	@SimpleId
	private long id;

	@SimpleVersion
	private int version;
	
	private String gmnum;
	
	private String servername;
	
	private long playerSendTime;
	
	private String username;
	
	private String playername;
	
	private String title;
	
	private String content;
	
	private int viplevel;
	
	//领取反馈时间，处理时间
	private long getFeedbackTime;
	
	//关闭反馈时间
	private long closeFeedbackTime;
	
	//服务评价
	private int serviceState;
	
	private int manyi;
	
	private int yiban;
	
	private int bumanyi;
	
	//交互条数
	private int talknum;
	
	//挂起时间
	private long guaqiTime;
	
	//暂离时间
	private long leaveTime;
	
	//开始工作时间
	private long startWorkTime;
	
	//注销时间
	private long zhuxiaoTime;
	
	//记录一天中的多次注销时间
	private String zhuxiaoTimestr;
	
	private long fbid;
	
	private long fankuiTime;
	
	//统计生成的时间
	private long recordTime;
	
	private String stateid;
	
	public String getStateid() {
		return stateid;
	}

	public int getManyi() {
		return manyi;
	}

	public String getZhuxiaoTimestr() {
		return zhuxiaoTimestr;
	}

	public void setZhuxiaoTimestr(String zhuxiaoTimestr) {
		this.zhuxiaoTimestr = zhuxiaoTimestr;
		NewFeedbackStateManager.getInstance().sem.notifyFieldChange(this, "zhuxiaoTimestr");
	}

	public void setManyi(int manyi) {
		this.manyi = manyi;
		NewFeedbackStateManager.getInstance().sem.notifyFieldChange(this, "manyi");
	}

	public int getYiban() {
		return yiban;
	}

	public void setYiban(int yiban) {
		this.yiban = yiban;
		NewFeedbackStateManager.getInstance().sem.notifyFieldChange(this, "yiban");
	}

	public int getBumanyi() {
		return bumanyi;
	}

	public void setBumanyi(int bumanyi) {
		this.bumanyi = bumanyi;
		NewFeedbackStateManager.getInstance().sem.notifyFieldChange(this, "bumanyi");
	}

	public void setStateid(String stateid) {
		this.stateid = stateid;
		NewFeedbackStateManager.getInstance().sem.notifyFieldChange(this, "stateid");
	}

	public long getRecordTime() {
		return recordTime;
	}

	public void setRecordTime(long recordTime) {
		this.recordTime = recordTime;
		NewFeedbackStateManager.getInstance().sem.notifyFieldChange(this, "recordTime");
	}

	public long getFankuiTime() {
		return fankuiTime;
	}

	public void setFankuiTime(long fankuiTime) {
		this.fankuiTime = fankuiTime;
		NewFeedbackStateManager.getInstance().sem.notifyFieldChange(this, "fankuiTime");
	}

	public long getFbid() {
		return fbid;
	}

	public void setFbid(long fbid) {
		this.fbid = fbid;
		NewFeedbackStateManager.getInstance().sem.notifyFieldChange(this, "fbid");
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

	public String getGmnum() {
		return gmnum;
	}

	public void setGmnum(String gmnum) {
		this.gmnum = gmnum;
		NewFeedbackStateManager.getInstance().sem.notifyFieldChange(this, "gmnum");
	}

	public String getServername() {
		return servername;
	}

	public void setServername(String servername) {
		this.servername = servername;
		NewFeedbackStateManager.getInstance().sem.notifyFieldChange(this, "servername");
	}

	public long getPlayerSendTime() {
		return playerSendTime;
	}

	public void setPlayerSendTime(long playerSendTime) {
		this.playerSendTime = playerSendTime;
		NewFeedbackStateManager.getInstance().sem.notifyFieldChange(this, "playerSendTime");
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
		NewFeedbackStateManager.getInstance().sem.notifyFieldChange(this, "username");
	}

	public String getPlayername() {
		return playername;
	}

	public void setPlayername(String playername) {
		this.playername = playername;
		NewFeedbackStateManager.getInstance().sem.notifyFieldChange(this, "playername");
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
		NewFeedbackStateManager.getInstance().sem.notifyFieldChange(this, "title");
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
		NewFeedbackStateManager.getInstance().sem.notifyFieldChange(this, "content");
	}

	public int getViplevel() {
		return viplevel;
	}

	public void setViplevel(int viplevel) {
		this.viplevel = viplevel;
		NewFeedbackStateManager.getInstance().sem.notifyFieldChange(this, "viplevel");
	}

	public long getGetFeedbackTime() {
		return getFeedbackTime;
	}

	public void setGetFeedbackTime(long getFeedbackTime) {
		this.getFeedbackTime = getFeedbackTime;
		NewFeedbackStateManager.getInstance().sem.notifyFieldChange(this, "getFeedbackTime");
	}

	public long getCloseFeedbackTime() {
		return closeFeedbackTime;
	}

	public void setCloseFeedbackTime(long closeFeedbackTime) {
		this.closeFeedbackTime = closeFeedbackTime;
		NewFeedbackStateManager.getInstance().sem.notifyFieldChange(this, "closeFeedbackTime");
	}

	public int getServiceState() {
		return serviceState;
	}

	public void setServiceState(int serviceState) {
		this.serviceState = serviceState;
		NewFeedbackStateManager.getInstance().sem.notifyFieldChange(this, "serviceState");
	}

	public int getTalknum() {
		return talknum;
	}

	public void setTalknum(int talknum) {
		this.talknum = talknum;
		NewFeedbackStateManager.getInstance().sem.notifyFieldChange(this, "talknum");
	}

	public long getGuaqiTime() {
		return guaqiTime;
	}

	public void setGuaqiTime(long guaqiTime) {
		this.guaqiTime = guaqiTime;
		NewFeedbackStateManager.getInstance().sem.notifyFieldChange(this, "guaqiTime");
	}

	public long getLeaveTime() {
		return leaveTime;
	}

	public void setLeaveTime(long leaveTime) {
		this.leaveTime = leaveTime;
		NewFeedbackStateManager.getInstance().sem.notifyFieldChange(this, "leaveTime");
	}

	public long getStartWorkTime() {
		return startWorkTime;
	}

	public void setStartWorkTime(long startWorkTime) {
		this.startWorkTime = startWorkTime;
		NewFeedbackStateManager.getInstance().sem.notifyFieldChange(this, "startWorkTime");
	}

	public long getZhuxiaoTime() {
		return zhuxiaoTime;
	}

	public void setZhuxiaoTime(long zhuxiaoTime) {
		this.zhuxiaoTime = zhuxiaoTime;
		NewFeedbackStateManager.getInstance().sem.notifyFieldChange(this, "zhuxiaoTime");
	}

} 
