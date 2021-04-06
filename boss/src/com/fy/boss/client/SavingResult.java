package com.fy.boss.client;

public class SavingResult {
	private String savingRecord[];
	
	private int totalnum;
	
	private int pagecount;

	public String[] getSavingRecord() {
		return savingRecord;
	}

	public void setSavingRecord(String[] savingRecord) {
		this.savingRecord = savingRecord;
	}

	public int getTotalnum() {
		return totalnum;
	}

	public void setTotalnum(int totalnum) {
		this.totalnum = totalnum;
	}

	public int getPagecount() {
		return pagecount;
	}

	public void setPagecount(int pagecount) {
		this.pagecount = pagecount;
	}
	
}
