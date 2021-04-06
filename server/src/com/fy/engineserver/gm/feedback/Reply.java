package com.fy.engineserver.gm.feedback;

import com.fy.engineserver.gametime.SystemTime;
import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

@SimpleEmbeddable
public class Reply {

	public static int CLOSE = 1;
	
	//为""就是玩家自己发的
	private String gmName;
	
	private long sendDate;
	@SimpleColumn(length=768)
	private String fcontent;
	
	// 回复(0)  还是关闭1
	private int type;
	
	public Reply(){
		super();
		this.gmName = "";
		this.type = 0;
		this.sendDate = SystemTime.currentTimeMillis();
		this.fcontent = "";
	}
	
	
	
	
	public String getGmName() {
		return gmName;
	}
	public void setGmName(String gmName) {
		this.gmName = gmName;
	}
	public long getSendDate() {
		return sendDate;
	}
	public void setSendDate(long sendDate) {
		this.sendDate = sendDate;
	}
	public String getFcontent() {
		return fcontent;
	}

	public void setFcontent(String fcontent) {
		this.fcontent = fcontent;
	}




	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	
}
