package com.sqage.stat.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.sqage.stat.commonstat.entity.OnLineUsersCount;
import com.sqage.stat.commonstat.manager.Impl.OnLineUsersCountManagerImpl;
import com.sqage.stat.message.ONLINEUSERSCOUNT_LOG_REQ;
import com.sqage.stat.model.OnLineUsersCountFlow;
import com.xuanzhi.tools.queue.AdvancedFilePersistentQueue;
import com.xuanzhi.tools.transport.RequestMessage;

public class OnLineUsersService implements Runnable {

	 AdvancedFilePersistentQueue onLineUsersqueue=null;
	 static Logger logger = Logger.getLogger(OnLineUsersService.class);
	 OnLineUsersCountManagerImpl onLineUsersCountManager;
	 static OnLineUsersService self;
	 public static int startnum=500;//处理队列中对象的最小数量
	 SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
	 public static OnLineUsersService getInstance() {
		return self;
	  }

	 public void init() throws Exception {
		self = this;
	  }

	 private boolean handle(List<RequestMessage> ls) {
		  
		 ArrayList<OnLineUsersCount> onLineUsersCountList =new ArrayList<OnLineUsersCount>();
		 boolean result=false;
		 for(int i=0;i<ls.size();i++)
		 {
			 RequestMessage message=ls.get(i);
			  if(message!=null){
				
				long now = System.currentTimeMillis();
				ONLINEUSERSCOUNT_LOG_REQ req = (ONLINEUSERSCOUNT_LOG_REQ) message;
				OnLineUsersCountFlow flow = req.getOnLineUsersCountFlow();
				
				String fenQu=flow.getFenQu();
				String game=flow.getGame();
				Long onlineTime=flow.getOnlineTime();

				 if(StatServerService.shetname!=null&&StatServerService.shetname.indexOf("韩国")!=-1){onlineTime+=60*60*1000;}//如果是韩国，时间加一小时，以平衡时区
					
				String quDao=flow.getQuDao();
				Long userCount=flow.getUserCount();
				
				OnLineUsersCount onLineUsersCount=new OnLineUsersCount();
				
				onLineUsersCount.setFenQu(fenQu);
				onLineUsersCount.setGame(game);
				if(onlineTime!=null){onLineUsersCount.setOnlineTime(new Date(onlineTime));}
				onLineUsersCount.setQuDao(quDao);
				if(userCount!=null){onLineUsersCount.setUserCount(userCount);}
				SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
				if(onlineTime!=null){ 
					String dateStr=sf.format(new Date(onlineTime)).substring(15);
					if("0".equals(dateStr)){
				     logger.debug("[玩家在线人数上报] " + flow.toString() + " [队列:" + onLineUsersqueue.elementNum() + "] [" + (System.currentTimeMillis() - now) + "ms]");
					onLineUsersCountList.add(onLineUsersCount);
					}
				}
				//OnLineUsersCount returnonLineUsersCount=onLineUsersCountManager.add(onLineUsersCount);
				
		 }else{
			 logger.info("OnLineUsersCountFlow队列出现null对象");
		 }
	 }
		 result=onLineUsersCountManager.addList(onLineUsersCountList);
		 return result;
	  }

	private boolean doOnLineUserCount(RequestMessage message) {
		boolean result=false;
		long now = System.currentTimeMillis();
		ONLINEUSERSCOUNT_LOG_REQ req = (ONLINEUSERSCOUNT_LOG_REQ) message;
		OnLineUsersCountFlow flow = req.getOnLineUsersCountFlow();
		if(logger.isDebugEnabled()){
		logger.info("[玩家在线人数上报] " + flow.toString() + " [队列:" + onLineUsersqueue.elementNum() + "] [" + (System.currentTimeMillis() - now) + "ms]");
		}
		String fenQu=flow.getFenQu();
		String game=flow.getGame();
		Long onlineTime=flow.getOnlineTime();
		String quDao=flow.getQuDao();
		Long userCount=flow.getUserCount();
		
		OnLineUsersCount onLineUsersCount=new OnLineUsersCount();
		
		onLineUsersCount.setFenQu(fenQu);
		onLineUsersCount.setGame(game);
		if(onlineTime!=null){onLineUsersCount.setOnlineTime(new Date(onlineTime));}
		onLineUsersCount.setQuDao(quDao);
		if(userCount!=null){onLineUsersCount.setUserCount(userCount);}
		
		//addOnLineUsersCountList.add(onLineUsersCount);
		OnLineUsersCount returnonLineUsersCount=onLineUsersCountManager.add(onLineUsersCount);
		if(returnonLineUsersCount!=null){result=true;}
		return result;
	}

	@Override
	public void run() {
	    if(onLineUsersqueue==null){onLineUsersqueue=StatServerService.onLineUsersqueue;}
		while (Thread.currentThread().isInterrupted() == false) {
			List<RequestMessage> ls=new ArrayList();
			long num=onLineUsersqueue.pushNum()-onLineUsersqueue.popNum();
			if(num>startnum){
			try {
				while(!onLineUsersqueue.isEmpty()) {
					RequestMessage req = (RequestMessage) onLineUsersqueue.pop();
					if(req != null){ ls.add(req);}
						
						if(ls.size()>startnum)
						{
							handle(ls);
							ls.clear();
						}
//						if(!handle(req))
//						{
//							onLineUsersqueue.push(req);
//						}
					
				}
				if(ls.size()>0){
					handle(ls);	
					ls.clear();
					Thread.sleep(100);
				}
				
				if(onLineUsersqueue.isEmpty()){
					 synchronized(this){
						 wait(100);
					 }
				}
			} catch (Exception e) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e2) {
					e2.printStackTrace();
				}
				
				logger.error("[reqest处理错误] [队列onLineUsersqueue:" + onLineUsersqueue.elementNum() + "] ",e);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
			} else{
				synchronized(this){
					 try {
						wait(1000);
					} catch (InterruptedException e) {
						logger.info("[reqest处理错误] " +e.getMessage() + " [队列:" + onLineUsersqueue.elementNum() + "] ",e);
					}
				 }
				}
		}
		if(logger.isDebugEnabled()){
		logger.debug("[reqest处理完毕] [处理线程退出] [队列:" + onLineUsersqueue.elementNum() + "]");
	}
	}

	public void setOnLineUsersCountManager(OnLineUsersCountManagerImpl onLineUsersCountManager) {
		this.onLineUsersCountManager = onLineUsersCountManager;
	}
}
