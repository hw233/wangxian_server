package com.sqage.stat.commonstat.entity;

/**
 * 银子监控
 * 
 *
 */
public class Transaction_yinzi {

	  private String createDate;
	  private String fenQu;
	  private String action;
	  private String reasonType;
	  private long money;
	  
	@Override
	public String toString() {
		return "Transaction_yinzi [action:" + action + "] [createDate:" + createDate + "] [fenQu:" + fenQu + "] [money:" + money + "] [reasonType:"
				+ reasonType + "]";
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getFenQu() {
		return fenQu;
	}
	public void setFenQu(String fenQu) {
		this.fenQu = fenQu;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getReasonType() {
		return reasonType;
	}
	public void setReasonType(String reasonType) {
		this.reasonType = reasonType;
	}
	public long getMoney() {
		return money;
	}
	public void setMoney(long money) {
		this.money = money;
	}
}
