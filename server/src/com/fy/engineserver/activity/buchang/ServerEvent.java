package com.fy.engineserver.activity.buchang;

public class ServerEvent {
	
	EventType type;
	EventExec entity;

	public ServerEvent(){}
	
	public ServerEvent(EventType type,EventExec event){
		this.type = type;
		this.entity = event;
	}

	@Override
	public String toString() {
		return "ServerEvent [type=" + this.type + "]";
	}
	
}
