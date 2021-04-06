package com.sqage.stat.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.sqage.stat.model.DaoJuFlow;
import com.xuanzhi.tools.queue.AdvancedFilePersistentQueue;

public class DaoJuMoHuService_merge implements Runnable {

	AdvancedFilePersistentQueue daoJu_mohu_queue=null;
	static Logger logger = Logger.getLogger(DaoJuMoHuService_merge.class);
	Thread thread1;
	static DaoJuMoHuService_merge self;
	SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");

	public static DaoJuMoHuService_merge getInstance() {
		return self;
	}

	public void init() throws Exception {
		self = this;
	}

	private void handle(ArrayList<DaoJuFlow> messageList) {
		HashMap map=new HashMap();
		for (DaoJuFlow flow : messageList) {
			if(flow!=null){
				    //DAOJU_LOG_REQ req = (DAOJU_LOG_REQ) message;
					//DaoJuFlow flow = req.getDaoJuFlow();
				if(logger.isDebugEnabled()){
					logger.debug("[道具购买消耗信息上报] " + flow.toString() + " [队列:" + daoJu_mohu_queue.elementNum() + "] ");
				}
					
					Long createDate=flow.getCreateDate();
					String date=sf.format(new Date(createDate));
					Long danJia=flow.getDanJia();
					String daoJuName=flow.getDaoJuName();
					Long daoJuNum=flow.getDaoJuNum();
					
					String fenQu=flow.getFenQu();
					String gameLevel=flow.getGameLevel();
					String getType=flow.getGetType();
					String huoBiType=flow.getHuoBiType();
					String userName=flow.getUserName();
					String position=flow.getPosition();
					
					 String daoJuColor=flow.getDaoJuColor();
					 String daoJuLevel=flow.getDaoJuLevel();
					 String bindType=flow.getBindType();
					 String jixing=flow.getJixing();
					 Long vip=flow.getVip();
					 String guojia=flow.getGuojia();
					
					 String key=date+daoJuName+fenQu+getType+danJia;
					DaoJuFlow flowtemp=null;
					if(map.get(key)!=null){
						flowtemp=(DaoJuFlow)map.get(key); 
						flowtemp.setCreateDate(createDate);
						flowtemp.setDaoJuNum(flowtemp.getDaoJuNum()+daoJuNum);
						//flowtemp.setDanJia(danJia);
						map.put(key,flowtemp);
					}
					else{
						map.put(key, flow);
					}
		   }else{
			   logger.info("DaoJuFlow排重时发现有null对象");
		   }
	}
					   Iterator<Entry<String,DaoJuFlow>> entrySetIterator=map.entrySet().iterator();
					   while(entrySetIterator.hasNext()){
					      Entry<String,DaoJuFlow> entry=entrySetIterator.next();
					      DaoJuFlow daoJuFlow= entry.getValue();
					       daoJu_mohu_queue.push(daoJuFlow);
		               }
					   if(logger.isDebugEnabled()){
				 logger.debug("[游戏中道具使用排重] [排重前数量:"+new Integer(messageList.size()).toString()+"] " +
						   		"[排重后数量:"+new Integer(map.size()).toString()+"] [排重数量:"+new Integer(messageList.size()-map.size()).toString()+"]" );
	   }
	}


	@Override
	public void run() {
		if(daoJu_mohu_queue==null){daoJu_mohu_queue=StatServerService.daoJu_mohu_queue;}
		while (Thread.currentThread().isInterrupted() == false) {
			ArrayList<DaoJuFlow> al=new ArrayList(); 
			long num=daoJu_mohu_queue.pushNum()-daoJu_mohu_queue.popNum();
			if(num>300){
			try {
				while(!daoJu_mohu_queue.isEmpty()) {
					DaoJuFlow flow =(DaoJuFlow)daoJu_mohu_queue.pop();
						if(flow!=null){al.add(flow);}
						if (al.size()>300) {
							handle(al);
							al.clear();
							Thread.sleep(60000);
					}
					}
				
				if(al.size()>0){
					   handle(al);
					al.clear();
					 }
					 synchronized(this){
					 wait(600000);
					}
			} catch (Exception e) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				logger.info("[reqest处理错误] " +e.getMessage() + " [队列:" + daoJu_mohu_queue.elementNum() + "] ",e);
			}
			}else{
				 synchronized(this){
					 try {
						wait(60000);
					} catch (InterruptedException e) {
						logger.info("[reqest处理错误] " +e.getMessage() + " [队列:" + daoJu_mohu_queue.elementNum() + "] ",e);
					}
					 }
			}
			
		}
		logger.info("[reqest处理完毕] [处理线程退出] [队列:" + daoJu_mohu_queue.elementNum() + "]");
	}

	public AdvancedFilePersistentQueue getDaoJu_mohu_queue() {
		return daoJu_mohu_queue;
	}

	public void setDaoJu_mohu_queue(AdvancedFilePersistentQueue daoJuMohuQueue) {
		daoJu_mohu_queue = daoJuMohuQueue;
	}

}