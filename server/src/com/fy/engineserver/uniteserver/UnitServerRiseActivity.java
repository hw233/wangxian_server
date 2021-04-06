package com.fy.engineserver.uniteserver;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.shop.ActivityProp;
import com.fy.engineserver.util.TimeTool;
import com.fy.engineserver.util.config.ServerFit4Activity;

public class UnitServerRiseActivity {
	private String keyId;
	private String platForm;
	private String serverName;
	private byte countryId;
	private String startTime;
	private String endTime;
	
	private int levelLimit;
	
	private ActivityProp[] props;
	private String mailTitle;
	private String mailContent;
	
	public UnitServerRiseActivity(String keyId, String platForm, String serverName, byte countryId, String startTime, String endTime, int levelLimit, ActivityProp[] props, String mailTitle, String mailContent) {
		this.keyId = keyId;
		this.platForm = platForm;
		this.serverName = serverName;
		this.countryId = countryId;
		this.startTime = startTime;
		this.endTime = endTime;
		this.levelLimit = levelLimit;
		this.props = props;
		this.mailTitle = mailTitle;
		this.mailContent = mailContent;
	}
	
	public boolean isOpen(){
		try {
			ServerFit4Activity sf4a=new ServerFit4Activity(platForm, serverName, "");
			if(sf4a.thiserverFit()){
				ActivitySubSystem.logger.error("[合服活动] [崛起专属奖励] [服务器正确]");
				long now=System.currentTimeMillis();
				long start=TimeTool.formatter.varChar19.parse(startTime);
				long end=TimeTool.formatter.varChar19.parse(endTime);
				if("".equals(startTime)||"".equals(endTime)||(start<now&&now<end)){
					ActivitySubSystem.logger.error("[合服活动] [崛起专属奖励] [时间正确]");
					return true;
				}
			}
		} catch (Exception e) {
			ActivitySubSystem.logger.error("[合服活动] [崛起专属奖励] [判断是否开放出错] ["+e+"]");
			e.printStackTrace();
		}
		return false;
	}
	public String getKeyId() {
		return keyId;
	}
	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}
	public String getPlatForm() {
		return platForm;
	}
	public void setPlatForm(String platForm) {
		this.platForm = platForm;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public byte getCountryId() {
		return countryId;
	}
	public void setCountryId(byte countryId) {
		this.countryId = countryId;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public int getLevelLimit() {
		return levelLimit;
	}
	public void setLevelLimit(int levelLimit) {
		this.levelLimit = levelLimit;
	}
	public ActivityProp[] getProps() {
		return props;
	}
	public void setProps(ActivityProp[] props) {
		this.props = props;
	}
	public String getMailTitle() {
		return mailTitle;
	}
	public void setMailTitle(String mailTitle) {
		this.mailTitle = mailTitle;
	}
	public String getMailContent() {
		return mailContent;
	}
	public void setMailContent(String mailContent) {
		this.mailContent = mailContent;
	}
	
	
	
}
