package com.fy.engineserver.activity.unitedserver;

import java.util.HashSet;
import java.util.Set;

import com.fy.engineserver.activity.shop.ActivityProp;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Player;

/**
 * 每日登录活动
 * 
 */
public class DailyLoginActivity {

	/** 活动ID(不可重复,作为) */
	private long id;
	/** 平台 */
	private Platform platform;
	/** 服务器列表 */
	private Set<String> servers = new HashSet<String>();
	/** 日期 */
	private String day;
	/** 活动奖励物品 */
	private ActivityProp activityProp;
	/** 邮件标题 */
	private String mailTitle;
	/** 邮件内容 */
	private String mailContent;

	public DailyLoginActivity(long id, Platform platform, Set<String> servers, String day, ActivityProp activityProp, String mailTitle, String mailContent) {
		super();
		this.id = id;
		this.platform = platform;
		this.servers = servers;
		this.day = day;
		this.activityProp = activityProp;
		this.mailTitle = mailTitle;
		this.mailContent = mailContent;
	}

	/**
	 * 判断时间和服务器是否参加当前活动
	 * @param day
	 * @param serverName
	 * @return
	 */
	public boolean timeAndSeverFit(String day, String serverName) {
		if (PlatformManager.getInstance().isPlatformOf(this.platform)) {
			if (this.day.equals(day)) {
				if (this.servers.contains(serverName)) {
					return true;
				}
			}
		}
		return false;
	}

	public void doPrize(Player player) {
		this.activityProp.sendMailToPlayer(player, mailTitle, mailContent, "登录活动");
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Platform getPlatform() {
		return platform;
	}

	public void setPlatform(Platform platform) {
		this.platform = platform;
	}

	public Set<String> getServers() {
		return servers;
	}

	public void setServers(Set<String> servers) {
		this.servers = servers;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public ActivityProp getActivityProp() {
		return activityProp;
	}

	public void setActivityProp(ActivityProp activityProp) {
		this.activityProp = activityProp;
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

	public String getLogString() {
		return "DailyLoginActivity [id=" + id + ", day=" + day + ", activityProp=" + activityProp + ", mailTitle=" + mailTitle + ", mailContent=" + mailContent + "]";
	}

	@Override
	public String toString() {
		return "DailyLoginActivity [id=" + id + ", platform=" + platform + ", servers=" + servers + ", day=" + day + ", activityProp=" + activityProp + ", mailTitle=" + mailTitle + ", mailContent=" + mailContent + "]";
	}

}