package com.fy.engineserver.chat;

import com.fy.engineserver.datasource.language.Translate;

public class ChatChannelType {
	//附近
	public static final int NEARBY = 0;
	
	//世界
	public static final int WORLD = 1;
	
	//国家
	public static final byte COUNTRY = 2;
	
	//宗派
	public static final byte ZONG = 3;

	//帮会
	public static final int JIAZU = 4;

	//队伍
	public static final byte TEAM = 5;

	//私聊
	public static final byte PERSONAL = 6;	
	
	//系统
	public static final byte SYSTEM = 7;
	
	//彩世
	public static final byte COLOR_WORLD = 8;

	public static final String DESP[] = new String[]{Translate.周围,Translate.世界,Translate.国家,Translate.宗族,Translate.家族,Translate.队伍,Translate.私聊,Translate.系统,Translate.彩世,Translate.好友,Translate.群组};

	//好友
	public static final int FRIEND = 9;
	//群组
	public static final int GROUP = 10;
	//战斗
//	public static final int BATTLE = 11;

	public static String getTypeDesp(int type) {
		try {
			return DESP[type];
		} catch(Exception e) {
			return Translate.text_1211;
		}
	}
}
