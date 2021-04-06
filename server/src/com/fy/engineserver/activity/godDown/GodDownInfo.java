package com.fy.engineserver.activity.godDown;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;

public class GodDownInfo implements Serializable{

	private static final long serialVersionUID = 1L;

	//上次领取时间
	public long lastReceiveTime;
	
	public List<Integer> receiveNpcList = new Vector<Integer>();
	
	
	
	
	public long getLastReceiveTime() {
		return lastReceiveTime;
	}

	public void setLastReceiveTime(long lastReceiveTime) {
		this.lastReceiveTime = lastReceiveTime;
	}

	public List<Integer> getReceiveNpcList() {
		return receiveNpcList;
	}

	public void setReceiveNpcList(List<Integer> receiveNpcList) {
		this.receiveNpcList = receiveNpcList;
	}

	public String getLogString(){
		StringBuffer sb = new StringBuffer();
		for(Integer i : receiveNpcList){
			sb.append(" id:"+i);
		}
		return sb.toString();
	}
	
}
