package com.fy.engineserver.chat;

/**
 * 消息类型
 * 组成结构: 1 + ChannelType + 编号1~99
 *
 */
public class ChatMessageType {
	public static final int WORLD_NORMAL = 1001;
	
	public static final int WORLD_WARN = 1002; 
	
	public static final int WORLD_ANNOUNCE = 1003;
	
	public static final int WORLD_PLAYER = 1004;
	
	public static final int TEAM_NORMAL = 1101;
	
	public static final int GANG_NORMAL = 1201;
	
	public static final int SYSTEM_NORMAL = 1301;
	
	public static final int PERSONAL_NORMAL = 1401;
	
	public static final int NEARBY_NORMAL = 1501;
}
