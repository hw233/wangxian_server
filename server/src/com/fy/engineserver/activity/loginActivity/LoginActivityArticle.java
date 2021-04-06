package com.fy.engineserver.activity.loginActivity;


public class LoginActivityArticle {
	
	private String days;
	
	private String clieckMess;
	
	private String showMess;
	
	private String iconid;
	
	private int rewardColor;
	
	private int rewardNum;
	
	private String articleName;
	
	private String rewardMess;
	
	private int status = 2;
	
	private int pnums;
	
	public LoginActivityArticle(){}
	
	public LoginActivityArticle(String days,String clieckMess,String showMess,String rewardMess,String iconid,int rewardColor,int rewardNum,String articleName,int status,int pnums){
		this.days = days;
		this.clieckMess = clieckMess;
		this.showMess = showMess;
		this.rewardMess = rewardMess;
		this.iconid = iconid;
		this.rewardColor = rewardColor;
		this.rewardNum = rewardNum;
		this.articleName = articleName;
		this.status = status;
		this.pnums = pnums;
	}
	
	public int getPnums() {
		return pnums;
	}

	public void setPnums(int pnums) {
		this.pnums = pnums;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getRewardMess() {
		return rewardMess;
	}

	public void setRewardMess(String rewardMess) {
		this.rewardMess = rewardMess;
	}

	public String getArticleName() {
		return articleName;
	}

	public void setArticleName(String articleName) {
		this.articleName = articleName;
	}


	public String getIconid() {
		return iconid;
	}

	public void setIconid(String iconid) {
		this.iconid = iconid;
	}

	public int getRewardColor() {
		return rewardColor;
	}

	public void setRewardColor(int rewardColor) {
		this.rewardColor = rewardColor;
	}

	public int getRewardNum() {
		return rewardNum;
	}

	public void setRewardNum(int rewardNum) {
		this.rewardNum = rewardNum;
	}

	public String getDays() {
		return days;
	}

	public void setDays(String days) {
		this.days = days;
	}

	public String getClieckMess() {
		return clieckMess;
	}

	public void setClieckMess(String clieckMess) {
		this.clieckMess = clieckMess;
	}

	public String getShowMess() {
		return showMess;
	}

	public void setShowMess(String showMess) {
		this.showMess = showMess;
	}

	@Override
	public String toString() {
		return "LoginActivityArticle [days=" + days + ", clieckMess="
				+ clieckMess + ", showMess=" + showMess + ", iconid=" + iconid
				+ ", rewardColor=" + rewardColor + ", rewardNum=" + rewardNum
				+ ", articleName=" + articleName + ", rewardMess=" + rewardMess
				+ ", status=" + status + ", pnums=" + pnums + "]";
	}

}
