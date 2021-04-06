package com.fy.boss.gm.newfeedback;

import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndex;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

@SimpleEntity
@SimpleIndices({
	@SimpleIndex(members={"name"})
})
public class GmTalk{

	@SimpleId
	private long id;

	@SimpleVersion
	private int version;
	@SimpleColumn(saveInterval=1) 
	String name;
	@SimpleColumn(saveInterval=1) 
	private long feedbackid;
	@SimpleColumn(saveInterval=1) 
	long sendDate;
	@SimpleColumn(saveInterval=1,length = 1024) 
	String talkcontent;

	
	public long getFeedbackid() {
		return feedbackid;
	}

	public void setFeedbackid(long feedbackid) {
		this.feedbackid = feedbackid;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

}
