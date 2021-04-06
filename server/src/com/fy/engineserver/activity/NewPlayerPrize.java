package com.fy.engineserver.activity;

import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.activity.shop.ActivityProp;
import com.fy.engineserver.platform.PlatformManager.Platform;

/**
 * 新服奖励
 * 
 */
public class NewPlayerPrize {

	private Platform platform;

	private String serverName;

	private List<ActivityProp> prizeList = new ArrayList<ActivityProp>();

	private String prizeGroupName;

	private String mailTitle;

	private String mailContent;

	public NewPlayerPrize(Platform platform, String serverName, List<ActivityProp> prizeList, String prizeGroupName, String mailTitle, String mailContent) {
		super();
		this.platform = platform;
		this.serverName = serverName;
		this.prizeList = prizeList;
		this.prizeGroupName = prizeGroupName;
		this.mailTitle = mailTitle;
		this.mailContent = mailContent;
	}

	public Platform getPlatform() {
		return platform;
	}

	public void setPlatform(Platform platform) {
		this.platform = platform;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public List<ActivityProp> getPrizeList() {
		return prizeList;
	}

	public void setPrizeList(List<ActivityProp> prizeList) {
		this.prizeList = prizeList;
	}

	public String getPrizeGroupName() {
		return prizeGroupName;
	}

	public void setPrizeGroupName(String prizeGroupName) {
		this.prizeGroupName = prizeGroupName;
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

	@Override
	public String toString() {
		return "NewPlayerPrize [mailContent=" + mailContent + ", mailTitle=" + mailTitle + ", platform=" + platform + ", prizeGroupName=" + prizeGroupName + ", prizeList=" + prizeList + ", serverName=" + serverName + "]";
	}

}
