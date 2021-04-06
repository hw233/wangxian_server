package com.fy.boss.client;

import com.fy.boss.client.SavingRecord;

public class SavingPageRecord {
	
	private SavingRecord records[];
	
	private long totalRecordNum;
	
	private long totalPageCount;

	public SavingRecord[] getRecords() {
		return records;
	}

	public void setRecords(SavingRecord[] records) {
		this.records = records;
	}

	public long getTotalRecordNum() {
		return totalRecordNum;
	}

	public void setTotalRecordNum(long totalRecordNum) {
		this.totalRecordNum = totalRecordNum;
	}

	public long getTotalPageCount() {
		return totalPageCount;
	}

	public void setTotalPageCount(long totalPageCount) {
		this.totalPageCount = totalPageCount;
	}
	
	
	
}
