package com.fy.engineserver.trade;

import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.trade.boothsale.MessageBoardInfo4Client;

/**
 * 消息面板
 * 
 * 
 */
public class MessageBoard {

	/** 消息队列 */
	private List<Message> messages = new ArrayList<Message>();
	private long createTime;

	public MessageBoard(long createTime) {
		this.createTime = createTime;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	public void clear() {
		messages.clear();
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public static MessageBoard createMessageBoard() {
		return new MessageBoard(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
	}

	public void add(Message message) {
		getMessages().add(message);
	}

	public MessageBoardInfo4Client[] getInfo() {
		MessageBoardInfo4Client[] boardInfo4Clients = new MessageBoardInfo4Client[getMessages().size()];
		for (int i = 0; i < getMessages().size(); i++) {
			boardInfo4Clients[i] = getMessages().get(i).getInfo();
		}
		return boardInfo4Clients;
	}
}
