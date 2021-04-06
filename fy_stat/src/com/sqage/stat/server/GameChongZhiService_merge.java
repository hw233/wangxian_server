package com.sqage.stat.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.sqage.stat.model.GameChongZhiFlow;
import com.xuanzhi.tools.queue.AdvancedFilePersistentQueue;

public class GameChongZhiService_merge implements Runnable {

	AdvancedFilePersistentQueue gameChongZhiqueue=StatServerService.gameChongZhiqueue;
	static Logger logger = Logger.getLogger(GameChongZhiService_merge.class);
	Thread thread1;

	static GameChongZhiService_merge self;
	
	 SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");

	public static GameChongZhiService_merge getInstance() {
		return self;
	}

	public void init() throws Exception {
		if(gameChongZhiqueue==null){gameChongZhiqueue=StatServerService.gameChongZhiqueue;}
		self = this;
	}

	private void handle(ArrayList<GameChongZhiFlow> messageList) {
		HashMap map=new HashMap();
		for (GameChongZhiFlow flow : messageList) {
			if(flow!=null){
				//GAMECHONGZHI_LOG_REQ req = (GAMECHONGZHI_LOG_REQ) message;
				//GameChongZhiFlow flow = req.getGameChongZhiFlow();
				
					int action=flow.getAction();
					String currencyType=flow.getCurrencyType();
					String fenQu=flow.getFenQu();
					String game=flow.getGame();
					
					String gameLevel=flow.getGameLevel();
					Long money=flow.getMoney();
					String quDao=flow.getQuDao();
					String reasonType=flow.getReasonType();
					
					Long time=flow.getTime();
					String date=sf.format(new Date(time));
					String userName=flow.getUserName();
					String jixing=flow.getJixing();
					
					//String key=currencyType+fenQu+gameLevel+quDao+reasonType+date+userName;
					String key=currencyType+fenQu+quDao+reasonType+date;
					
					//  logger.info("............key:"+key );

					
					GameChongZhiFlow flowtemp=null;
					if(map.get(key)!=null){
						flowtemp=(GameChongZhiFlow)map.get(key); 
						flowtemp.setTime(time);
						flowtemp.setMoney(flowtemp.getMoney()+money);
						map.put(key,flowtemp);
					}
					else{
						map.put(key, flow);
					}
		}else{
			logger.info("GameChongZhiFlow排重时出现null对象");
		}
	}
					//HashMap<String,String> entrySetMap=new HashMap<String,String>();
					   Iterator<Entry<String,GameChongZhiFlow>> entrySetIterator=map.entrySet().iterator();
					   boolean result=false;
					   while(entrySetIterator.hasNext()){
					      Entry<String,GameChongZhiFlow> entry=entrySetIterator.next();
					      GameChongZhiFlow gameChongZhiFlow= entry.getValue();
					      
					      gameChongZhiqueue.push(gameChongZhiFlow);
		               }
					   logger.info("[游戏中的货币充值排重] [排重前数量:"+new Integer(messageList.size()).toString()+"] " +
					   		"[排重后数量:"+new Integer(map.size()).toString()+"] [排重数量:"+new Integer(messageList.size()-map.size()).toString()+"]" );
	       }

	@Override
	public void run() {
		if(gameChongZhiqueue==null){gameChongZhiqueue=StatServerService.gameChongZhiqueue;}
		while (Thread.currentThread().isInterrupted() == false) {
			ArrayList<GameChongZhiFlow> al=new ArrayList(); 
			long num=gameChongZhiqueue.pushNum()-gameChongZhiqueue.popNum();
			logger.info("...........................................gameChongZhiqueue队列元素数:"+num);
			if(num>3000){
			try {
				while(!gameChongZhiqueue.isEmpty()) {
					//RequestMessage req = (RequestMessage) gameChongZhiqueue.pop();
					//GAMECHONGZHI_LOG_REQ req = (GAMECHONGZHI_LOG_REQ) gameChongZhiqueue.pop();
					GameChongZhiFlow flow =(GameChongZhiFlow) gameChongZhiqueue.pop();
						if(flow!=null){al.add(flow);}
				   if (al.size()>= 400) {
							handle(al);
							al.clear();
							Thread.sleep(1000);
					}
				}
				if(al.size()>0){
					handle(al);
					al.clear();
					}
				synchronized(this){
					 wait(1000);
					 }
			} catch (Exception e) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				logger.info("[reqest处理错误] " +e.getMessage() + " [队列:" + gameChongZhiqueue.elementNum() + "] ",e);
			}
			}else{
				synchronized(this){
				 try {
					wait(3*60000);
				} catch (InterruptedException e) {
					logger.info("[reqest处理错误] " +e.getMessage() + " [队列:" + gameChongZhiqueue.elementNum() + "] ",e);
				}
			 }
			}
		}
		//logger.info("[reqest处理完毕] [处理线程退出] [队列:" + gameChongZhiqueue.elementNum() + "]");
	}

	public AdvancedFilePersistentQueue getGameChongZhiqueue() {
		return gameChongZhiqueue;
	}

	public void setGameChongZhiqueue(AdvancedFilePersistentQueue gameChongZhiqueue) {
		this.gameChongZhiqueue = gameChongZhiqueue;
	}


}