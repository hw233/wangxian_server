package com.fy.engineserver.util.gmstat;

import com.fy.engineserver.util.TimeTool;
import com.fy.engineserver.util.gmstat.EventForJson.EventType;
/**
 * 基础事件
 * 
 *
 */
public class RecordEvent {

	public String recorder;
	
	public long recordtime;
	
	public String servername;
	
	public String ipaddress;
	
	public EventType eventtype;
	
	public String playername;
	
	public String username;
	public long pid;
	
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public String getRecorder() {
		return recorder;
	}

	public void setRecorder(String recorder) {
		this.recorder = recorder;
	}

	public long getRecordtime() {
		return recordtime;
	}

	public void setRecordtime(long recordtime) {
		this.recordtime = recordtime;
	}

	public String getServername() {
		return servername;
	}

	public void setServername(String servername) {
		this.servername = servername;
	}

	public String getIpaddress() {
		return ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	public EventType getEventtype() {
		return eventtype;
	}

	public void setEventtype(EventType eventtype) {
		this.eventtype = eventtype;
	}

	public String getPlayername() {
		return playername;
	}

	public void setPlayername(String playername) {
		this.playername = playername;
	}

	@Override
	public String toString() {
		return "[服务器:"+servername+"]--[角色名:"+playername+"]--[IP:"+ipaddress+"]"+"--[操作人:"+recorder+"]"+"[操作时间:"+TimeTool.formatter.varChar23.format(recordtime)+"]";
	}
	
}
