package com.fy.boss.gm.gmpagestat.event;


import java.io.Serializable;
import java.text.SimpleDateFormat;

import com.fy.boss.gm.gmpagestat.RecordEvent;
import com.fy.boss.gm.gmpagestat.EventForJson.EventType;
import com.xuanzhi.boss.game.GameConstants;
public class PlayerPropertyChangeEvent extends RecordEvent implements Serializable{
	
	private String propertyName;
	
	private long oldValue;
	
	private long newValue;
	
	public PlayerPropertyChangeEvent(){}
	
	public PlayerPropertyChangeEvent(String propertyName,long oldValue,long newValue,String playername,String ip,String recorder){
		this.propertyName = propertyName;
		this.oldValue = oldValue;
		this.newValue = newValue;
		this.username = username;
		this.pid = pid;
		setPlayername(playername);
		setEventtype(EventType.属性修改);
		setIpaddress(ip);
		setRecorder(recorder);
		setRecordtime(System.currentTimeMillis());
		setServername(GameConstants.getInstance().getServerName());
	}
	
	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public long getOldValue() {
		return oldValue;
	}

	public void setOldValue(long oldValue) {
		this.oldValue = oldValue;
	}

	public long getNewValue() {
		return newValue;
	}

	public void setNewValue(long newValue) {
		this.newValue = newValue;
	}
	@Override
	public String toString() {
		return "<td>修改属性</td><td>"+servername+"</td><td>"+username+"</td><td>"+playername+"</td><td>"+pid+"</td><td>"+ipaddress+"</td>"+"<td>"+propertyName+"</td>"+"<td>"+oldValue+"</td>"+"<td>"+newValue+"</td>"+"<td>"+recorder+"</td>"+"<td>"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(recordtime)+"</td>";
	}
}
