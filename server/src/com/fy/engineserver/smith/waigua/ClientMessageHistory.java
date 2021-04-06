package com.fy.engineserver.smith.waigua;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 
 * @version 创建时间：Oct 17, 2013 11:29:58 AM
 * 
 */
public class ClientMessageHistory {
	
	private long playerId;
	
	private List<String> messageList;
	
	public ClientMessageHistory(long playerId) {
		this.playerId = playerId;
		this.messageList = new ArrayList<String>();
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public List<String> getMessageList() {
		return messageList;
	}

	public void setMessageList(List<String> messageList) {
		this.messageList = messageList;
	}
	
	public void addMessage(String message) {
		messageList.add(message);
	}
}
