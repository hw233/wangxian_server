package com.fy.engineserver.gm.newfeedback;

public class GmTalk {

	private long id;

	String name;
	
	long sendDate;
	
	String talkcontent;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getSendDate() {
		return sendDate;
	}

	public void setSendDate(long sendDate) {
		this.sendDate = sendDate;
	}

	public String getTalkcontent() {
		return talkcontent;
	}

	public void setTalkcontent(String talkcontent) {
		this.talkcontent = talkcontent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
}
