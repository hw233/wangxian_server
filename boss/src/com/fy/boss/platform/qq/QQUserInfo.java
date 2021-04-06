package com.fy.boss.platform.qq;

public class QQUserInfo 
{
	private boolean mozuan = false;
	private int mozuanDengji = 0;
	
	public boolean isMozuan() {
		return mozuan;
	}
	public void setMozuan(boolean mozuan) {
		this.mozuan = mozuan;
	}
	public int getMozuanDengji() {
		return mozuanDengji;
	}
	public void setMozuanDengji(int mozuanDengji) {
		this.mozuanDengji = mozuanDengji;
	}
	
	
	public String getLogStr()
	{
		return "["+mozuan+"] ["+mozuanDengji+"]";
	}
	
	
}
