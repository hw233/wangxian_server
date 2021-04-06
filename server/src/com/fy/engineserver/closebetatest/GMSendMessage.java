package com.fy.engineserver.closebetatest;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.chat.ChatChannelType;
import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageItem;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.chat.ChatMessageTask;
import com.xuanzhi.tools.text.StringUtil;

public class GMSendMessage implements Runnable{
//	protected static final Log logger = LogFactory.getLog(AutoSendMail.class.getName());
public	static Logger logger = LoggerFactory.getLogger(AutoSendMail.class.getName());
	private static GMSendMessage self;
	public static long sleepingTime = 600000;
	public static String message = "";
	public static int startTime;
	public static int endTime;
	public static GMSendMessage getInstance(){
		if(self == null){
			self = new GMSendMessage();
		}
		return self;
	}
	private GMSendMessage(){
		
	}
	volatile boolean running = false;
	private Thread localThread;
	public void start() {
		if(!running) {
			running = true;
			localThread = new Thread(this);
			localThread.start();
		}
	}
	
	public void stop() {
		running = false;
		if(localThread != null){
			localThread.interrupt();
		}
	}
	
	public void run() {
		// TODO Auto-generated method stub
		while(running){
			try {
				Thread.sleep(sleepingTime);
				String hourTime = "";
		        Calendar cal = Calendar.getInstance();
				if(running) {
					hourTime = new SimpleDateFormat("HH")
	                .format(cal.getTime());
					int hour = Integer.parseInt(hourTime);
					if(hour >= startTime && hour <= endTime){
						sendMessage();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
//				logger.info("[系统自动发消息异常] ["+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis())+"ms e:]"+StringUtil.getStackTrace(e));
				if(logger.isInfoEnabled())
					logger.info("[系统自动发消息异常] [{}ms e:]{}", new Object[]{(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()),StringUtil.getStackTrace(e)});
			}
		}
	}
	private void sendMessage() throws Exception{
		ChatMessageService cm = ChatMessageService.getInstance();
		ChatMessage msg = new ChatMessage();
		msg.setSort(ChatChannelType.SYSTEM);
		msg.setMessageText(message);
		msg.setAccessoryItem(new ChatMessageItem());
		msg.setAccessoryTask(new ChatMessageTask());
		msg.setChatTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
		cm.sendMessageToSystem(msg);
	}
}
