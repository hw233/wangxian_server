package com.fy.engineserver.gm.record;

public class MailRecord {
	private String gmname;
	private String mid;
	private String title;
	private String qcontent;
	private String rescontent;
	private String items;
	private String resdate;
	private String username;
	private String playername;
	private long playerid;
	private String cdate;

	public String getGmname() {    
		return gmname;
	}

	public void setGmname(String gmname) {
		this.gmname = gmname;  
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}
    
	public long getPlayerid() {
		return playerid;
	}

	public void setPlayerid(long playerid) {
		this.playerid = playerid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getQcontent() {
		return qcontent;
	}

	public void setQcontent(String qcontent) {
		this.qcontent = qcontent;
	}

	public String getRescontent() {
		return rescontent;
	}

	public void setRescontent(String rescontent) {
		this.rescontent = rescontent;
	}

	public String getItems() {
		return items;
	}

	public void setItems(String items) {
		this.items = items;
	}

	public String getResdate() {
		return resdate;
	}

	public void setResdate(String resdate) {
		this.resdate = resdate;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPlayername() {
		return playername;
	}

	public void setPlayername(String playername) {
		this.playername = playername;
	}

	public String getCdate() {
		return cdate;
	}

	public void setCdate(String cdate) {
		this.cdate = cdate;
	}
    
}
