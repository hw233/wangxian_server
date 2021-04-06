package com.fy.engineserver.activity.base;

import java.util.Calendar;

import com.fy.engineserver.activity.BaseActivityInstance;

public class SendFlowerActivityConfig extends BaseActivityInstance{


	private String stime = "";						//只是用来toString
	private String etime = "";
	private String flowerName= "";					//奖励条件一，花名
	private int flowerNums = 0;						//奖励条件二，花的数量
	private String liZiName = "";					//粒子名
	private String senderRewardArticleName = "";	//赠送者奖励物品名
	private int senderRewardArticleNum = 0;			//奖励数量
	private String receiveRewardArticleName = "";	//接受者奖励物品名
	private int receiveRewardArtilceNum = 0;
	private boolean hasNotice = false;				//是否同时有世界广播
	
	public SendFlowerActivityConfig(String startTime, String endTime, String platForms, String openServerNames, String notOpenServers) throws Exception {
		super(startTime, endTime, platForms, openServerNames, notOpenServers);
		this.stime = startTime;
		this.etime = endTime;
	}
	
	public void setArgs(String flowerName,int flowerNums,String senderRewardArticleName,int senderRewardArticleNum,
			String receiveRewardArticleName,int receiveRewardArtilceNum,String liZiName,boolean hasNotice){
		this.flowerName = flowerName;
		this.flowerNums = flowerNums;
		this.senderRewardArticleName = senderRewardArticleName;
		this.senderRewardArticleNum = senderRewardArticleNum;
		this.receiveRewardArticleName = receiveRewardArticleName;
		this.receiveRewardArtilceNum = receiveRewardArtilceNum;
		this.liZiName = liZiName;
		this.hasNotice = hasNotice;
	}
	
	Calendar calendar = Calendar.getInstance();

	public boolean isEffectActivity(String flowerName,int flowerNum){
		boolean result = false;
		if(this.isThisServerFit() == null){
			if(this.flowerName.equals(flowerName) && flowerNum == flowerNums){
				result = true;
			}
		}
		return result;
	}

	public String getStime() {
		return this.stime;
	}
	public void setStime(String stime) {
		this.stime = stime;
	}
	public String getEtime() {
		return this.etime;
	}
	public void setEtime(String etime) {
		this.etime = etime;
	}
	public String getLiZiName() {
		return this.liZiName;
	}
	public void setLiZiName(String liZiName) {
		this.liZiName = liZiName;
	}
	public String getFlowerName() {
		return this.flowerName;
	}
	public void setFlowerName(String flowerName) {
		this.flowerName = flowerName;
	}
	public int getFlowerNums() {
		return this.flowerNums;
	}
	public void setFlowerNums(int flowerNums) {
		this.flowerNums = flowerNums;
	}
	public String getSenderRewardArticleName() {
		return this.senderRewardArticleName;
	}
	public void setSenderRewardArticleName(String senderRewardArticleName) {
		this.senderRewardArticleName = senderRewardArticleName;
	}
	public int getSenderRewardArticleNum() {
		return this.senderRewardArticleNum;
	}
	public void setSenderRewardArticleNum(int senderRewardArticleNum) {
		this.senderRewardArticleNum = senderRewardArticleNum;
	}
	public String getReceiveRewardArticleName() {
		return this.receiveRewardArticleName;
	}
	public void setReceiveRewardArticleName(String receiveRewardArticleName) {
		this.receiveRewardArticleName = receiveRewardArticleName;
	}
	public int getReceiveRewardArtilceNum() {
		return this.receiveRewardArtilceNum;
	}
	public void setReceiveRewardArtilceNum(int receiveRewardArtilceNum) {
		this.receiveRewardArtilceNum = receiveRewardArtilceNum;
	}
	public boolean isHasNotice() {
		return this.hasNotice;
	}
	public void setHasNotice(boolean hasNotice) {
		this.hasNotice = hasNotice;
	}

	@Override
	public String getInfoShow() {
		return toString();
	}

	@Override
	public String toString() {
		return "SendFlowerActivityConfig [stime=" + this.stime + ", etime=" + this.etime + ", flowerName=" + this.flowerName + ", flowerNums=" + this.flowerNums + ", liZiName=" + this.liZiName + ", senderRewardArticleName=" + this.senderRewardArticleName + ", senderRewardArticleNum=" + this.senderRewardArticleNum + ", receiveRewardArticleName=" + this.receiveRewardArticleName + ", receiveRewardArtilceNum=" + this.receiveRewardArtilceNum + ", hasNotice=" + this.hasNotice + "]";
	}
}
