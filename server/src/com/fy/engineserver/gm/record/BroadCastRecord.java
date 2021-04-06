package com.fy.engineserver.gm.record;

public class BroadCastRecord {
	//广播记录
    private String message;
    private String startDate;
    private String endDate;
    private String cycletime;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getCycletime() {
		return cycletime;
	}
	public void setCycletime(String cycletime) {
		this.cycletime = cycletime;
	}
    
}
