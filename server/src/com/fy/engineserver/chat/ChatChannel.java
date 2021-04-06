package com.fy.engineserver.chat;

import java.util.Hashtable;

/**
 * 聊天频道
 *
 */
public class ChatChannel {
	
	protected int type;
	
	protected String name;
	
	protected String description;
	//最小发送间隔
	private long minSendPeriod;
	//玩家上次发送消息的时间
	public transient Hashtable<Long, Long> playerLastSendTimeMap;
	
	public ChatChannel(){}
	
	public ChatChannel(int type, String name, long minSendPeriod, String description) {
		this.name = name;
		this.type = type;
		this.minSendPeriod = minSendPeriod;
		this.description = description;
		this.playerLastSendTimeMap = new Hashtable<Long, Long>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	/**
	 * 得到最小发送间隔
	 * @return
	 */
	public long getMinSendPeriod() {
		return minSendPeriod;
	}

	public void setMinSendPeriod(long minSendPeriod) {
		this.minSendPeriod = minSendPeriod;
	}

	/**
	 * 设置玩家上次发送时间
	 * @param playerId
	 * @param sendTime
	 */
	public void setLastSendTime(long playerId, long sendTime) {
		playerLastSendTimeMap.put(playerId, sendTime);
	}
	
	/**
	 * 得到玩家上次发送时间
	 * @param playerId
	 * @return
	 */
	public long getLastSendTime(long playerId) {
	 	 Long time = playerLastSendTimeMap.get(playerId);
	 	 if(time != null) {
	 		 return time;
	 	 } else {
	 		 return 0;
	 	 }
	}
	
	/**
	 * 用户是否符合发送间隔的要求
	 * @param playerId
	 * @param sendTime
	 * @return
	 */
	public byte isSendPeriodValid(long playerId, long sendTime) {
		long lastTime = getLastSendTime(playerId);
		if(sendTime - lastTime >= minSendPeriod) {
			return ChatMessageService.CHAT_ERROR_检查通过;
		}
		return ChatMessageService.CHAT_ERROR_TYPE_发言频率过快;
	}
	
	/**
	 * 用户是否可以发送
	 * @param playerId 用户id
	 * @param level 等级
	 * @param sendTime 发送时间
	 * @return
	 */
	public byte isPlayerAllowSend(long playerId, int level, long sendTime) {
		return isSendPeriodValid(playerId, sendTime);
	}
	
	/**
	 * 用户一个发送动作，给频道进行记录
	 * @param playerId
	 * @param sendTime
	 */
	public void addPlayerSendAction(long playerId, long sendTime) {
		setLastSendTime(playerId, sendTime);
	}
	
	/**
	 * 把玩家从这个频道中删除
	 * @param playerId
	 */
	public void removePlayer(long playerId) {
		playerLastSendTimeMap.remove(playerId);
	}
	
	/**
	 * 玩家在此频道是否禁言
	 * @param playerId
	 * @return
	 */
	public boolean isPlayerBaned(long playerId) {
		return false;
	}
}
