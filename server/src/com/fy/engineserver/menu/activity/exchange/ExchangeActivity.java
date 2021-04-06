package com.fy.engineserver.menu.activity.exchange;

import java.util.Arrays;

public class ExchangeActivity {
	private String menuName;// 菜单名称
	private boolean tipYN;// 是否有二次弹框
	private String tip;// 二次弹框提示
	// // 消耗物品
	// private String costArticleNameStr;
	// private String costArticleCNNameStr;
	// private String costArticleColorStr;
	// private String costArticleNumStr;
	// // 兑换得到物品
	// private String gainArticleNames;
	// private String gainArticleCNNames;
	// private String gainArticleColors;
	// private String gainArticleNums;
	// private String gainArticleBinds;
	//
	// private String mailTitle;// 邮件标题
	// private String mailContent;// 邮件内容
	/** 兑换消耗物品 */
	private String[] costArticleNameArr;
	private String[] costArticleCNNameArr;
	private Integer[] costArticleColorArr;
	private Integer[] costArticleNumArr;

	/** 兑换得到物品 */
	private String[] gainArticleNameArr;
	private String[] gainArticleCNNameArr;
	private Integer[] gainArticleColorArr;
	private Integer[] gainArticleNumArr;
	private Boolean[] gainArticleBindArr;
	private int rewardType;
	private int rewardNum;
	String peifangName;
	String peifangName_stat;
	long startTime;
	long endTime;

	/** 邮件标题和内容 */
	private String mailTitle;
	private String mailContent;

	public ExchangeActivity(String peifangName, String peifangName_stat, String[] costArticleNameArr, String[] costArticleCNNameArr, Integer[] costArticleColorArr, Integer[] costArticleNumArr, String[] gainArticleNameArr, String[] gainArticleCNNameArr, Integer[] gainArticleColorArr, Integer[] gainArticleNumArr, Boolean[] gainArticleBindArr, String mailTitle, String mailContent) {
		this.peifangName_stat = peifangName_stat;
		this.peifangName = peifangName;
		this.costArticleNameArr = costArticleNameArr;
		this.costArticleCNNameArr = costArticleCNNameArr;
		this.costArticleColorArr = costArticleColorArr;
		this.costArticleNumArr = costArticleNumArr;
		this.gainArticleNameArr = gainArticleNameArr;
		this.gainArticleCNNameArr = gainArticleCNNameArr;
		this.gainArticleColorArr = gainArticleColorArr;
		this.gainArticleNumArr = gainArticleNumArr;
		this.gainArticleBindArr = gainArticleBindArr;
		this.mailTitle = mailTitle;
		this.mailContent = mailContent;
	}

	public ExchangeActivity(String menuName, boolean tipYN, String tip, String[] costArticleNameArr, String[] costArticleCNNameArr, Integer[] costArticleColorArr, Integer[] costArticleNumArr, String[] gainArticleNameArr, String[] gainArticleCNNameArr, Integer[] gainArticleColorArr, Integer[] gainArticleNumArr, Boolean[] gainArticleBindArr, String mailTitle, String mailContent) {
		this.menuName = menuName;
		this.tipYN = tipYN;
		this.tip = tip;
		this.costArticleNameArr = costArticleNameArr;
		this.costArticleCNNameArr = costArticleCNNameArr;
		this.costArticleColorArr = costArticleColorArr;
		this.costArticleNumArr = costArticleNumArr;
		this.gainArticleNameArr = gainArticleNameArr;
		this.gainArticleCNNameArr = gainArticleCNNameArr;
		this.gainArticleColorArr = gainArticleColorArr;
		this.gainArticleNumArr = gainArticleNumArr;
		this.gainArticleBindArr = gainArticleBindArr;
		this.mailTitle = mailTitle;
		this.mailContent = mailContent;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public boolean getTipYN() {
		return tipYN;
	}

	public void setTipYN(boolean tipYN) {
		this.tipYN = tipYN;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public String[] getCostArticleNameArr() {
		return costArticleNameArr;
	}

	public void setCostArticleNameArr(String[] costArticleNameArr) {
		this.costArticleNameArr = costArticleNameArr;
	}

	public String[] getCostArticleCNNameArr() {
		return costArticleCNNameArr;
	}

	public void setCostArticleCNNameArr(String[] costArticleCNNameArr) {
		this.costArticleCNNameArr = costArticleCNNameArr;
	}

	public Integer[] getCostArticleColorArr() {
		return costArticleColorArr;
	}

	public void setCostArticleColorArr(Integer[] costArticleColorArr) {
		this.costArticleColorArr = costArticleColorArr;
	}

	public Integer[] getCostArticleNumArr() {
		return costArticleNumArr;
	}

	public void setCostArticleNumArr(Integer[] costArticleNumArr) {
		this.costArticleNumArr = costArticleNumArr;
	}

	public String[] getGainArticleNameArr() {
		return gainArticleNameArr;
	}

	public void setGainArticleNameArr(String[] gainArticleNameArr) {
		this.gainArticleNameArr = gainArticleNameArr;
	}

	public String[] getGainArticleCNNameArr() {
		return gainArticleCNNameArr;
	}

	public void setGainArticleCNNameArr(String[] gainArticleCNNameArr) {
		this.gainArticleCNNameArr = gainArticleCNNameArr;
	}

	public Integer[] getGainArticleColorArr() {
		return gainArticleColorArr;
	}

	public void setGainArticleColorArr(Integer[] gainArticleColorArr) {
		this.gainArticleColorArr = gainArticleColorArr;
	}

	public Integer[] getGainArticleNumArr() {
		return gainArticleNumArr;
	}

	public void setGainArticleNumArr(Integer[] gainArticleNumArr) {
		this.gainArticleNumArr = gainArticleNumArr;
	}

	public Boolean[] getGainArticleBindArr() {
		return gainArticleBindArr;
	}

	public void setGainArticleBindArr(Boolean[] gainArticleBindArr) {
		this.gainArticleBindArr = gainArticleBindArr;
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

	public int getRewardType() {
		return rewardType;
	}

	public void setRewardType(int rewardType) {
		this.rewardType = rewardType;
	}

	public int getRewardNum() {
		return rewardNum;
	}

	public void setRewardNum(int rewardNum) {
		this.rewardNum = rewardNum;
	}

	public String getPeifangName() {
		return peifangName;
	}

	public void setPeifangName(String peifangName) {
		this.peifangName = peifangName;
	}

	public String getPeifangName_stat() {
		return peifangName_stat;
	}

	public void setPeifangName_stat(String peifangName_stat) {
		this.peifangName_stat = peifangName_stat;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	@Override
	public String toString() {
		return "ExchangeActivity [costArticleCNNameArr=" + Arrays.toString(costArticleCNNameArr) + ", costArticleColorArr=" + Arrays.toString(costArticleColorArr) + ", costArticleNameArr=" + Arrays.toString(costArticleNameArr) + ", costArticleNumArr=" + Arrays.toString(costArticleNumArr) + ", gainArticleBindArr=" + Arrays.toString(gainArticleBindArr) + ", gainArticleCNNameArr=" + Arrays.toString(gainArticleCNNameArr) + ", gainArticleColorArr=" + Arrays.toString(gainArticleColorArr) + ", gainArticleNameArr=" + Arrays.toString(gainArticleNameArr) + ", gainArticleNumArr=" + Arrays.toString(gainArticleNumArr) + ", mailContent=" + mailContent + ", mailTitle=" + mailTitle + ", menuName=" + menuName + "]";
	}

}
