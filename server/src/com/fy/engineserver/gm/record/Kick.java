package com.fy.engineserver.gm.record;

public class Kick {

	private String username;
	private String gmname;
	private String result;
	private String date;
	private String times = "1";
	private String type;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getGmname() {
		return gmname;
	}
	public void setGmname(String gmname) {
		this.gmname = gmname;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTimes() {
		return times;
	}
	public void setTimes(String times) {
		this.times = times;
	}
}
