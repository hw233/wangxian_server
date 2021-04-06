package com.sqage.stat.commonstat.entity;

import java.util.Date;

/**
 * 每个人的功勋
 * 每天每人发送一次统计信息
 * 
 */
public class Battle_PlayerStat {
	
	private static final long serialVersionUID = -9203088531016193193L;

	private String		type="";       //	模式	
	private String		fenqu="";	    //	分区
	private String		gongxun="";	//	功勋值
	private long		PlayerCount;//	玩家人数
	private long createDate=System.currentTimeMillis();
	
	  private String column1=""; // 备用项
	  private String column2=""; //备用项
	
	@Override
	public String toString() {
		return "Battle_PlayerStat [PlayerCount:" + PlayerCount + "] [column1:" + column1 + "] [column2:" + column2 + "] [createDate:" + createDate
				+ "] [fenqu:" + fenqu + "] [gongxun:" + gongxun + "] [type:" + type + "]";
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFenqu() {
		return fenqu;
	}
	public void setFenqu(String fenqu) {
		this.fenqu = fenqu;
	}
	public String getGongxun() {
		return gongxun;
	}
	public void setGongxun(String gongxun) {
		this.gongxun = gongxun;
	}
	public long getPlayerCount() {
		return PlayerCount;
	}
	public void setPlayerCount(long playerCount) {
		PlayerCount = playerCount;
	}
	public String getColumn1() {
		return column1;
	}
	public void setColumn1(String column1) {
		this.column1 = column1;
	}
	public String getColumn2() {
		return column2;
	}
	public void setColumn2(String column2) {
		this.column2 = column2;
	}
	public long getCreateDate() {
		return createDate;
	}
	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}
	 
}
