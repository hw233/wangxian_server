package com.fy.engineserver.chat;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.datasource.language.MultiLanguageTranslateManager;
import com.fy.engineserver.datasource.language.TransferLanguage;
import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.tools.configuration.Configuration;
import com.xuanzhi.tools.configuration.DefaultConfigurationBuilder;

public class ChatChannelManager implements Runnable  {

//	protected static Logger logger = Logger.getLogger(ChatChannelManager.class);
public	static Logger logger = LoggerFactory.getLogger(ChatChannelManager.class);

	private static ChatChannelManager mself;
	
	protected String xmlConfigFile;
	
	protected List<ChatChannel> channelList;

	public static ChatChannelManager getPlayerManager() {
		return mself;
	}
	
	public void initialize() throws Exception {
		
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		configure(new File(xmlConfigFile));
		mself = this;
		Thread t = new Thread(this, "ChatChannelManager");
		t.start();
		ServiceStartRecord.startLog(this);
	}
	
	public void run() {
		while(true) {
			try {
				Thread.sleep(5000);
				for(ChatChannel channel : channelList) {
					Hashtable<Long, Long> sendMap = channel.playerLastSendTimeMap;
					Long keys[] = sendMap.keySet().toArray(new Long[0]);
					for(Long key : keys) {
						Long sendTime = sendMap.get(key);
						if(sendTime != null && com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-sendTime > 60*60*1000) {
							sendMap.remove(key);
						}
					}
				}
			} catch(Throwable e) {
				logger.error("[聊天频道处理] [异常]", e);
			}
		}
	}
	
	private void configure(File file) throws Exception {
		Configuration config = new DefaultConfigurationBuilder().buildFromFile(file);
		Configuration channels[] = config.getChildren("channel");
		List<ChatChannel> clist = new ArrayList<ChatChannel>();
		for(Configuration channelConf : channels) {
			String name = TransferLanguage.transferString(channelConf.getAttribute("name",""));
			name = MultiLanguageTranslateManager.languageTranslate(name);
			
			long minSendPeriod = channelConf.getChild("minSendPeriod").getValueAsLong(1000);
			String description = TransferLanguage.transferString(channelConf.getChild("description").getValue(""));
			int type = channelConf.getAttributeAsInteger("type", -1);
			if(type == -1) {
				throw new Exception("ChatChannel intialize failed for id or type value invalid.");
			}
			ChatChannel channel = null;
			switch(type) {
				case	ChatChannelType.WORLD :
					int minAuthorizedLevel = channelConf.getChild("minAuthorizedLevel").getValueAsInteger(1);
					channel = new WorldChatChannel(name, minSendPeriod, description, minAuthorizedLevel);
//					Configuration fconfs[] = channelConf.getChild("frequency-limits").getChildren();
//					for(Configuration fconf : fconfs) {
//						int level = fconf.getAttributeAsInteger("level", 0);
//						long period = fconf.getAttributeAsLong("period",0);
//						long count = fconf.getAttributeAsLong("count",0);
//						((WorldChatChannel)channel).addSendFrequency(level, period, count);
//					}
					clist.add(channel);
//					System.out.println("[加载世界频道] ["+type+"]");
					break;
				case ChatChannelType.COLOR_WORLD :
					channel = new ChatChannel(type, name, minSendPeriod, description);
					clist.add(channel);
//					System.out.println("[加载彩世频道] ["+type+"]");
					break;
				case	ChatChannelType.COUNTRY :
					minAuthorizedLevel = channelConf.getChild("minAuthorizedLevel").getValueAsInteger(1);
					channel = new PolcampChatChannel(name, minSendPeriod, description, minAuthorizedLevel);
//					fconfs = channelConf.getChild("frequency-limits").getChildren();
//					for(Configuration fconf : fconfs) {
//						int level = fconf.getAttributeAsInteger("level", 0);
//						long period = fconf.getAttributeAsLong("period",0);
//						long count = fconf.getAttributeAsLong("count",0);
//						((PolcampChatChannel)channel).addSendFrequency(level, period, count);
//					}
					clist.add(channel);
//					System.out.println("[加载阵营频道] ["+type+"]");
					break;
				case ChatChannelType.TEAM :
					channel = new ChatChannel(type, name, minSendPeriod, description);
					clist.add(channel);
//					System.out.println("[加载队伍频道] ["+type+"]");
					break;
				
				case ChatChannelType.SYSTEM :
					channel = new ChatChannel(type, name, minSendPeriod, description);
					clist.add(channel);
//					System.out.println("[加载系统频道] ["+type+"]");
					break;
				case ChatChannelType.PERSONAL :
					channel = new ChatChannel(type, name, minSendPeriod, description);
					clist.add(channel);
//					System.out.println("[加载私聊频道] ["+type+"]");
					break;
				case ChatChannelType.JIAZU :
					channel = new ChatChannel(type, name, minSendPeriod, description);
					clist.add(channel);
//					System.out.println("[加载家族频道] ["+type+"]");
					break;
				case ChatChannelType.ZONG :
					channel = new ChatChannel(type, name, minSendPeriod, description);
					clist.add(channel);
//					System.out.println("[加载宗派频道] ["+type+"]");
					break;
				case ChatChannelType.NEARBY :
					channel = new ChatChannel(type, name, minSendPeriod, description);
					clist.add(channel);
//					System.out.println("[加载附近频道] ["+type+"]");
					break;
				case ChatChannelType.FRIEND :
					channel = new ChatChannel(type, name, minSendPeriod, description);
					clist.add(channel);
//					System.out.println("[加载好友频道] ["+type+"]");
					break;
				case ChatChannelType.GROUP :
					channel = new ChatChannel(type, name, minSendPeriod, description);
					clist.add(channel);
//					System.out.println("[加载群组频道] ["+type+"]");
					break;
				default : 
					//do nothing;
					break;
			}
		}
		this.channelList = clist;
	}

	public String getXmlConfigFile() {
		return xmlConfigFile;
	}

	public void setXmlConfigFile(String xmlConfigFile) {
		this.xmlConfigFile = xmlConfigFile;
	}
	
	/**
	 * 通过频道类型获取频道
	 * @param chatChannelType
	 * @return
	 */
	public ChatChannel getChatChannel(int channelType) {
		for(ChatChannel channel : channelList) {
			if(channel.getType() == channelType) {
				return channel;
			}
		}
		return null;
	}


}
