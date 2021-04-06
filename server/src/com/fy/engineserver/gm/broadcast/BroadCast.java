package com.fy.engineserver.gm.broadcast;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.chat.ChatChannelType;
import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageItem;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.chat.ChatMessageTask;
import com.xuanzhi.tools.text.StringUtil;

	public class BroadCast implements Runnable{
//		protected static final Log logger = LogFactory.getLog("com.fy.engineserver.gm.broadcast.BroadCast");
public	static Logger logger = LoggerFactory.getLogger("com.fy.engineserver.gm.broadcast.BroadCast");
		private  long sleepingTime = 1000;
		private  String message = "";
		private  long startTime;
		private  long endTime;
	
		public BroadCast(String message, long startTime, long endTime,long sleepingTime) {
			super();
			this.message = message;
			this.startTime = startTime;
			this.endTime = endTime;
			this.sleepingTime = sleepingTime;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		

		public long getSleepingTime() {
			return sleepingTime;
		}

		public void setSleepingTime(long sleepingTime) {
			this.sleepingTime = sleepingTime;
		}

		public long getStartTime() {
			return startTime;
		}

		public void setStartTime(long startTime) {
			this.startTime = startTime;
		}

		public long getEndTime() {
			return endTime;
		}

		public void setEndTime(long endTime) {
			this.endTime = endTime;
		}



		volatile boolean running = false;
		private Thread localThread;
		public void start() {
			if(!running) {
//				logger.info("[系统消息序列启动] ["+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()));
				if(logger.isInfoEnabled())
					logger.info("[系统消息序列启动] [{}", new Object[]{(com.fy.engineserver.gametime.SystemTime.currentTimeMillis())});
				running = true;
				localThread = new Thread(this,"broadcast");
				localThread.start();
			}
		}
		
		public void stop() {
			running = false;
			if(localThread != null){
				localThread.interrupt();
			}
		}
		
	/*	public void suspend(){
		    if(localThread!=null){
		    	Thread.yield();
		    }
			
		}*/
		public void run() {
			// TODO Auto-generated method stub
			while(running){
				try { 	
					if(running) {					
						long nowtime = new Date().getTime();
						if(nowtime >= startTime && nowtime <= endTime){
							sendMessage();
						}
					}
					Thread.sleep(sleepingTime);
				} catch (Exception e) {
					e.printStackTrace();
//					logger.info("[系统自动发消息异常] ["+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis())+"ms e:]"+StringUtil.getStackTrace(e));
					if(logger.isInfoEnabled())
						logger.info("[系统自动发消息异常] [{}ms e:]{}", new Object[]{(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()),StringUtil.getStackTrace(e)});
					try {
						Thread.sleep(30000);
					} catch (InterruptedException e1) {
					}
				}
			}
		}
		private void sendMessage() throws Exception{
			ChatMessageService cm = ChatMessageService.getInstance();
			ChatMessage msg = new ChatMessage();
			msg.setSort(ChatChannelType.SYSTEM);
			msg.setMessageText(message);
//			logger.info(message+" startTime"+startTime+" endTime"+endTime+" sleepingTime"+sleepingTime);
			if(logger.isInfoEnabled())
				logger.info("{} startTime{} endTime{} sleepingTime{}", new Object[]{message,startTime,endTime,sleepingTime});
			msg.setAccessoryItem(new ChatMessageItem());
			msg.setAccessoryTask(new ChatMessageTask());
			msg.setChatTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
			cm.sendMessageToSystem(msg);
		}
	}
