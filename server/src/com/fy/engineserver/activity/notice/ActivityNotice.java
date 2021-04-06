package com.fy.engineserver.activity.notice;

/*
 * 活动提示的数据
 */
public class ActivityNotice {

	private int activityId;
	private String activityName;
	private String iconName;
	private boolean needSignup;
	private String des;

	public ActivityNotice() {

	}

	public ActivityNotice(int activityId, String activityName, String iconName, boolean needSignup, String des) {
		this.activityId = activityId;
		this.activityName = activityName;
		this.iconName = iconName;
		this.needSignup = needSignup;
		this.des = des;
	}

	public int getActivityId() {
		return activityId;
	}

	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getIconName() {
		return iconName;
	}

	public void setIconName(String iconName) {
		this.iconName = iconName;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public boolean isNeedSignup() {
		return needSignup;
	}

	public void setNeedSignup(boolean needSignup) {
		this.needSignup = needSignup;
	}

	@Override
	public String toString() {
		return "ActivityNotice [Id=" + activityId + ", Name=" + activityName + ", icon=" + iconName + ", des=" + des + "]";
	}

}
