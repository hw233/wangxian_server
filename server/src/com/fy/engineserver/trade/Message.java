package com.fy.engineserver.trade;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.trade.boothsale.MessageBoardInfo4Client;

/**
 * 消息体
 * 
 * 
 */
public class Message {

	public final static int TYPE_LEAVE_WORD = 0;
	public final static int TYPE_TRADE_RECORD = 1;
	public final static String[] messageTypes = { Translate.text_boothsale_001,  Translate.text_boothsale_002};
	public final static String[] messageColors = { "#FFFFFF", "#FF0000" };

	/** 消息来源 */
	private String fromName;
	/** 消息目标 */
	private String toName;
	/** 消息发送时间 */
	private long time;
	/** 消息类型 */
	private int type;
	/** 消息内容 */
	private String content;

	public Message() {
		// TODO Auto-generated constructor stub
	}

	public Message(String fromName, String toName, String content, int type) {
		this.fromName = fromName;
		this.toName = toName;
		this.content = content;
		this.type = type;
		this.time = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public String getToName() {
		return toName;
	}

	public void setToName(String toName) {
		this.toName = toName;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * 创建一条留言信息
	 * @param from
	 * @param to
	 * @param content
	 * @return
	 */
	public static Message createTalkMessage(Player from, Player to, String content) {
		return new Message(from.getName(), to.getName(), content, TYPE_LEAVE_WORD);
	}

	/**
	 * 创建一条交易信息
	 * @param from
	 * @param to
	 * @param content
	 * @return
	 */
	public static Message createTradeMessage(Player from, Player to, String content) {
		return new Message(from.getName(), to.getName(), content, TYPE_TRADE_RECORD);
	}

	public MessageBoardInfo4Client getInfo() {
		MessageBoardInfo4Client info4Client = new MessageBoardInfo4Client();
		info4Client.setContent(this.getContent());
		info4Client.setColor(messageColors[this.getType()]);
		info4Client.setName(this.getFromName());
		return info4Client;
	}
}
