package com.fy.engineserver.chat;


/**
	聊天类的设计
	
	聊天的内容，包含所有的文本信息。		(表情是否可以用 字符直接显示)
	/-1 标示物品的位置
	/-2 标示任务的位置
	其他 /0 /1 /2 /3 标示表情
	绘图类可以直接解析ChatMessage数据格式
	
	保存消息在主游戏画面中显示的时间和进度，绘图用puding 和绘制系统消息使用的变量 滚动进度
	
	记录玩家当前的频道位置，记录玩家当前的偏移量，当前选择的光标 6[] 
	
	清理机制，理论上应该不清理，当某种类型的消息，队列存储20条消息
	
	保存玩家曾经发言的信息 队列存储20条 在玩家输入的时，可通过按键选择
 * @author Administrator
 *
 */

public class ChatMessage {

	/**
	 * 消息的类型，见ChatChannelType
	 */
	private int sort = 0;	
	
	/**
	 * 消息所包含的文本 /-1代表物品的位置 /-2代表任务的位置 /0~11代表表情
	 * 
	 */
	private String messageText = "";
	
	/**
	 * 消息类别，同一个频道内不同类别的消息可能显示要做区分
	 */
	private int chatType = 0;
	
	/**
	 * 消息所包含的物品附件
	 */
	private ChatMessageItem accessoryItem = new ChatMessageItem();

	/**
	 * 消息所包含的任务附件
	 */
	private ChatMessageTask accessoryTask = new ChatMessageTask();
	
	/**
	 * 发送时间
	 */
	private long chatTime = 0;
	
	/**
	 * 发送者的名字
	 */
	private String senderName = "";
	private long senderId = -1;
	
	// 发送者要发送的组的id
	private long groupId = -1;  
	private int campId = 0;
	
	/**
	 * 接收者的名字
	 */
	private String receiverName = "";
	private long receiverId = -1;
	
	public void setSort(int type){
		this.sort = type;
	}
	public int getSort(){
		return sort;
	}
	
	public void setMessageText(String messageText){
		if(messageText != null) {
			this.messageText = messageText.replaceAll("\n", "").replaceAll("\r", "").replaceAll(" {2,}", " ");
		}
	}
	public String getMessageText(){
		return messageText;
	}
	
	public void setSenderId(long senderId){
		this.senderId = senderId;
	}		
	public long getSenderId(){
		return senderId;
	}	
	
	public void setSenderName(String senderName){
		this.senderName = senderName;
	}		
	public String getSenderName(){
		return senderName;
	}		
	
	public ChatMessageItem getAccessoryItem(){
		return accessoryItem;
	}
	public void setAccessoryItem(ChatMessageItem accessoryItem){
		this.accessoryItem = accessoryItem;
	}	
	public ChatMessageTask getAccessoryTask(){
		return accessoryTask;
	}	
	public void setAccessoryTask(ChatMessageTask accessoryTask){
		this.accessoryTask = accessoryTask;
	}		
	
	public void setReceiverId(long receiverId){
		this.receiverId = receiverId;
	}		
	public long getReceiverId(){
		return receiverId;
	}	
	
	public void setReceiverName(String receiverName){
		this.receiverName = receiverName;
	}		
	public String getReceiverName(){
		return receiverName;
	}
	public long getChatTime() {
		return chatTime;
	}
	public void setChatTime(long chatTime) {
		this.chatTime = chatTime;
	}
	public int getChatType() {
		return chatType;
	}
	public void setChatType(int chatType) {
		this.chatType = chatType;
	}		
	
	public int getCampId() {
		return campId;
	}
	public void setCampId(int campId) {
		this.campId = campId;
	}
	public ChatMessage clone() {
		ChatMessage mes = new ChatMessage();
		mes.setAccessoryItem(this.accessoryItem);
		mes.setAccessoryTask(this.accessoryTask);
		mes.setChatTime(this.chatTime);
		mes.setChatType(this.chatType);
		mes.setMessageText(this.messageText);
		mes.setReceiverId(this.receiverId);
		mes.setReceiverName(this.receiverName);
		mes.setSenderId(this.senderId);
		mes.setSenderName(this.senderName);
		mes.setCampId(this.getCampId());
		mes.setSort(this.sort);
		return mes;
	}
	public long getGroupId() {
		return groupId;
	}
	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}
}
