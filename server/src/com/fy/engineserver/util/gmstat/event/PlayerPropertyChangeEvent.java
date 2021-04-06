package com.fy.engineserver.util.gmstat.event;

import com.fy.engineserver.util.TimeTool;
import com.fy.engineserver.util.gmstat.RecordEvent;
import com.fy.engineserver.util.gmstat.EventForJson.EventType;
import com.xuanzhi.boss.game.GameConstants;
/**
 * 玩家属性改变事件
 * 
 *
 */
public class PlayerPropertyChangeEvent extends RecordEvent{
	
	private String propertyName;
	
	private long oldValue;
	
	private long newValue;
	
	public PlayerPropertyChangeEvent(){}
	
	public PlayerPropertyChangeEvent(String propertyName,long oldValue,long newValue,String playername,String ip,String recorder){
		this.propertyName = propertyName;
		this.oldValue = oldValue;
		this.newValue = newValue;
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
		return "【属性修改】 [服务器:"+servername+"]--[角色名:"+playername+"]--[IP:"+ipaddress+"]"+"[属性："+propertyName+"]"+"[老值："+oldValue+"]"+"[新值："+newValue+"]"+"--[操作人:"+recorder+"]"+"--[操作时间:"+TimeTool.formatter.varChar23.format(recordtime)+"]";
	}
}
