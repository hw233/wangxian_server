package com.fy.engineserver.chat;

import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * 阵营频道
 *
 */
public class PolcampChatChannel extends ChatChannel {
	//要求玩家的最低等级
	private long minAuthorizedLevel;
	//禁言玩家
	private Hashtable<Long, Long[]> bannedPlayerMap;
	/*发送频率表 level:[period,count] ; level指代用户level<=level适用value内的规则*/
	private Hashtable<Integer, Long[]> sendFrequencyMap;
	//发送数量表 player:[count,lastTime] list内每个元素代表一分钟的发送 只保留最近reserveHour小时
	private Hashtable<Long, List<Long[]>> sendCountMap;
	//保留发送记录的时间(小时)
	private int reserveHour = 24;
	
	private Hashtable<Long, List<ChatMessage>> messageMap;
	
	public PolcampChatChannel() {
		super();
		this.type  = ChatChannelType.COUNTRY;
		this.bannedPlayerMap = new Hashtable<Long, Long[]>();
		this.sendFrequencyMap = new Hashtable<Integer, Long[]>();
		this.sendCountMap = new Hashtable<Long, List<Long[]>>();
		this.messageMap = new Hashtable<Long, List<ChatMessage>>();
	}
	
	public PolcampChatChannel(String name, long minSendPeriod, String description, long minAuthorizedLevel) {
		super(ChatChannelType.COUNTRY,name,minSendPeriod,description);
		this.minAuthorizedLevel = minAuthorizedLevel;
		this.bannedPlayerMap = new Hashtable<Long, Long[]>();
		this.sendFrequencyMap = new Hashtable<Integer, Long[]>();
		this.sendCountMap = new Hashtable<Long, List<Long[]>>();
		this.messageMap = new Hashtable<Long, List<ChatMessage>>();
	}

	public long getMinAuthorizedLevel() {
		return minAuthorizedLevel;
	}

	public void setMinAuthorizedLevel(long minAuthorizedLevel) {
		this.minAuthorizedLevel = minAuthorizedLevel;
	}
	
	public Hashtable<Integer, Long[]> getSendFrequencyMap() {
		return sendFrequencyMap;
	}

	public void setSendFrequencyMap(Hashtable<Integer, Long[]> sendFrequencyMap) {
		this.sendFrequencyMap = sendFrequencyMap;
	}

	public Hashtable<Long, Long[]> getBannedPlayerMap() {
		return bannedPlayerMap;
	}

	public Hashtable<Long, List<Long[]>> getSendCountMap() {
		return sendCountMap;
	}
	
	public void addMessage(long playerId, ChatMessage message) {
		List<ChatMessage> list = messageMap.get(playerId);
		if(list == null) {
			list = Collections.synchronizedList(new LinkedList<ChatMessage>());
			list.add(message);
			messageMap.put(playerId, list);
		} else {
			list.add(0, message);
		}
	}
	
	public List<ChatMessage> popPlayerMessages(long playerId) {
		return messageMap.remove(playerId);
	}
	
	/**
	 * @param playerId 
	 * @param startTime 开始禁言时间(ms)
	 * @param banTime 禁言时长(s)
	 */
	public void addBanPlayer(long playerId, Long startTime, Long banTime) {
		bannedPlayerMap.put(playerId, new Long[]{startTime, banTime});
	}
	
	/**
	 * 玩家是否被禁言
	 * @param playerId
	 * @return
	 */
	public boolean isPlayerBaned(long playerId, long sendTime) {      
		Long[] banSet = bannedPlayerMap.get(playerId);
		if(banSet != null) {
			if(sendTime - banSet[0] <= banSet[1]*1000) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 添加一个发送频率限制
	 * @param level 等级
	 * @param period 周期(分)
	 * @param times 发送次数
	 */
	public void addSendFrequency(int level, long period, long times) {
		sendFrequencyMap.put(level, new Long[]{period, times});
	}

	/**
	 * 添加用户的发送
	 * 主键为用户，value是一个List，存放了每分钟的发送次数
	 * @param playerId
	 * @param sendTime
	 * @param times
	 */
	public void addPlayerSend(long playerId, long sendTime) {
		List<Long[]> sendList = sendCountMap.get(playerId);
		if(sendList == null || sendList.size() == 0) {
			sendList = new LinkedList<Long[]>();
			sendList.add(new Long[]{1L, sendTime});
			//System.out.println("null_new_add");
		} else {
			Long lastSend[] = sendList.get(sendList.size()-1);
			long lastSendTime = lastSend[1];
			long minCount = minCount(lastSendTime, sendTime);
			if(minCount == 0) {
				sendList.set(sendList.size()-1, new Long[]{lastSend[0]+1, sendTime});
				//System.out.println("add_same ["+DateUtil.formatDate(new Date(lastSendTime), "yyMMdd HH:mm:ss")+"] ["+DateUtil.formatDate(new Date(sendTime), "yyMMdd HH:mm:ss")+"]");
			} else {				
				//System.out.println("add_diff ["+DateUtil.formatDate(new Date(lastSendTime), "yyMMdd HH:mm:ss")+"] ["+DateUtil.formatDate(new Date(sendTime), "yyMMdd HH:mm:ss")+"]");
				sendList.add(new Long[]{1L, sendTime});
			}
			long startTime = sendTime - reserveHour*60*60*1000;
			while(sendList.size() > 0 && sendList.get(0)[1] < startTime) {
				sendList.remove(0);
			}
		}
		sendCountMap.put(playerId, sendList);
	}
	
	/**
	 * 获得用户某周期内的发送次数
	 * @param player
	 * @param period (分) 从当前开始算到之前一段时间内这个周期
	 * @return
	 */
	public int getPlayerSendCount(long playerId, long period, long sendTime) {
		List<Long[]> sendList = sendCountMap.get(playerId);
		if(sendList == null) {
			return 0;
		}
		int sendCount = 0;
		for(int i=sendList.size()-1; i>= 0 ; i--) {
			Long sendSet[] = sendList.get(i);
			if(sendSet[1] + period*60*1000 > sendTime) {
				sendCount += sendSet[0];
			}
		}
		return sendCount;
	}
	
	/**
	 * 通过发送频率条件检查用户是否允许发送
	 * @param playerId
	 * @return
	 */
	public boolean isPlayerAllowSendByFrequency(long playerId, int level, long sendTime) {
		Long fitSet[] = null;
		Integer levels[] = sendFrequencyMap.keySet().toArray(new Integer[0]);
		Arrays.sort(levels);
		for(int i=0; i<levels.length; i++) {
			if(level <= levels[i]) {
				fitSet = sendFrequencyMap.get(levels[i]);
				break;
			}
		}
		if(fitSet == null) {
			return true;
		}
		long period = fitSet[0];
		long acount = fitSet[1];
		int trueSend = getPlayerSendCount(playerId, period, sendTime);
		if(trueSend < acount) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 用户是否可以发送
	 */
	public byte isPlayerAllowSend(long playerId, int level, long sendTime) {
		if(level < minAuthorizedLevel){
			return ChatMessageService.CHAT_ERROR_TYPE_级别不足;
		}
		if(isPlayerBaned(playerId,sendTime) ){
			return ChatMessageService.CHAT_ERROR_TYPE_被禁言;
		}
		if(!isPlayerAllowSendByFrequency(playerId, level, sendTime)){
			return ChatMessageService.CHAT_ERROR_TYPE_发言频率过快;
		}
		return super.isPlayerAllowSend(playerId, level, sendTime);
	}
	
	/**
	 * rewrite this method
	 */
	public void addPlayerSendAction(long playerId, long sendTime) {
		super.addPlayerSendAction(playerId, sendTime);
		addPlayerSend(playerId, sendTime);
	}
	
	/**
	 * 把玩家从这个频道中删除
	 * @param playerId
	 */
	public void removePlayer(long playerId) {
		super.removePlayer(playerId);
		bannedPlayerMap.remove(playerId);
		sendCountMap.remove(playerId);
	}
	
	public boolean isPlayerBaned(long playerId) {
		return bannedPlayerMap.get(playerId) != null;
	}
	
	/**
	 * 结算两个时间点相差的分钟数
	 * @param timeStart
	 * @param timeEnd
	 * @return
	 */
	public long minCount(long timeStart, long timeEnd) {
		long minStart = timeStart/(1000*60);
		long minEnd = timeEnd/(1000*60);
		return minEnd - minStart;
	}
	
	public static void main(String args[]) {
		PolcampChatChannel channel = new PolcampChatChannel();
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		int testCount = 1000;
		long sendTime = now;
		for(int i=0; i<testCount; i++) {
			sendTime = sendTime - (new Random().nextInt(30)+1)*1000;
			channel.addPlayerSend(0, sendTime);
		}
		sendTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		channel.addPlayerSend(0, sendTime);
		long elap = com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-sendTime;
		int scount = channel.getPlayerSendCount(0, (testCount*30)/60, sendTime);
	}
	
}
