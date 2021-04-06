package com.fy.boss.gm.faq;
public class FaqRecord {

	protected String gamename;
	
	protected String modulename;
	
	protected String recorder;
	
	protected String recordtime;

	protected String title;
	
	protected String content;
	
	protected boolean isrepeat;
	 
	public String getGamename() {
		return gamename;
	}

	public void setGamename(String gamename) {
		this.gamename = gamename;
	}

	public String getModulename() {
		return modulename;
	}

	public void setModulename(String modulename) {
		this.modulename = modulename;
	}

	public String getRecorder() {
		return recorder;
	}

	public void setRecorder(String recorder) {
		this.recorder = recorder;
	}

	public String getRecordtime() {
		return recordtime;
	}

	public void setRecordtime(String recordtime) {
		this.recordtime = recordtime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isIsrepeat() {
		return isrepeat;
	}

	public void setIsrepeat(boolean isrepeat) {
		this.isrepeat = isrepeat;
	}

}
