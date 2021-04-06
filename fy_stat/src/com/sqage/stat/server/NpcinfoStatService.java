package com.sqage.stat.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.sqage.stat.commonstat.entity.Npcinfo;
import com.sqage.stat.commonstat.manager.Impl.NpcinfoManagerImpl;
import com.sqage.stat.message.NPC_LOG_REQ;
import com.sqage.stat.model.NpcinfoFlow;
import com.xuanzhi.tools.queue.AdvancedFilePersistentQueue;
import com.xuanzhi.tools.transport.RequestMessage;

public class NpcinfoStatService implements Runnable {

	 AdvancedFilePersistentQueue queue=null;
	 static Logger logger = Logger.getLogger(NpcinfoStatService.class);
	 public static int startnum=1;//处理队列中对象的最小数量
	 NpcinfoManagerImpl npcinfoManager;
	 static NpcinfoStatService self;
	 SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
	 public static NpcinfoStatService getInstance() {
		return self;
	  }

	 public void init() throws Exception {
		self = this;
	  }

	 private void handle(List<RequestMessage> ls) {
		 ArrayList<Npcinfo> npcinfoList   = new ArrayList<Npcinfo>();
		 for(int i=0;i<ls.size();i++)
		 {
			 RequestMessage message=ls.get(i);
				long now = System.currentTimeMillis();
				NPC_LOG_REQ req = (NPC_LOG_REQ) message;
				NpcinfoFlow flow = req.getNpcinfoFlow();
				if(logger.isDebugEnabled()){logger.debug("[NPC信息上报] " + flow.toString() + " [队列:" + queue.elementNum() + "] [" + (System.currentTimeMillis() - now) + "ms]"); }
				
				String award=flow.getAward();
				String career=flow.getCareer();
				long createDate=flow.getCreateDate();
				String fenQu=flow.getFenQu();
				int gameLevel=flow.getGameLevel();
				int getDaoJu=flow.getGetDaoJu();
				int getWuPin=flow.getGetWuPin();
				int getYOuXiBi=flow.getGetYOuXiBi();
				//long id=flow.getId();
				String jixing=flow.getJixing();
				String npcName=flow.getNpcName();
				String taskType=flow.getTaskType();
				String userName=flow.getUserName();
				String column1 =flow.getColumn1();
				String column2 =flow.getColumn2();
				
				Npcinfo npcinfo=new Npcinfo();
				npcinfo.setAward(award);
				npcinfo.setCareer(career);
				npcinfo.setColumn1(column1);
				npcinfo.setColumn2(column2);
				
				npcinfo.setCreateDate(new Date(createDate));
				npcinfo.setFenQu(fenQu);
				npcinfo.setGameLevel(gameLevel);
				npcinfo.setGetDaoJu(getDaoJu);
				
				npcinfo.setGetWuPin(getWuPin);
				npcinfo.setGetYOuXiBi(getYOuXiBi);
				//npcinfo.setId(id);
				npcinfo.setJixing(jixing);
				npcinfo.setNpcName(npcName);
				npcinfo.setTaskType(taskType);
				npcinfo.setUserName(userName);
				npcinfoList.add(npcinfo);
		 }	
		 npcinfoManager.addList(npcinfoList);	
	  }


	@Override
	public void run() {
	    if(queue==null){queue=StatServerService.npcinfoFlowqueue;}
		while (Thread.currentThread().isInterrupted() == false) {
			List<RequestMessage> ls=new ArrayList();
			if(queue.elementNum()>startnum){
			try {
				while(!queue.isEmpty()) {
					RequestMessage req = (RequestMessage) queue.pop();
					if(req != null){
						ls.add(req);
						if(ls.size()>=200)
						{
							handle(ls);
							ls.clear();
						}
					}
				}
				if(ls.size()>0){
					handle(ls);	
					ls.clear();
					Thread.sleep(1100);
				}
			} catch (Exception e) {
				try {
					e.printStackTrace();
					Thread.sleep(5000);
				} catch (InterruptedException e2) {
					e2.printStackTrace();
				}
				logger.error("[reqest处理错误] [队列queue:" + queue.elementNum() + "] ",e);
			}
			} else {
				 synchronized(this){
					 try {
						wait(100000);
					} catch (InterruptedException e) {
						logger.info("[reqest处理npcinfoFlowqueue] " +e.getMessage() + " [队列暂时没有数据，释放线程资源 ] ",e);
					}
					 }
			}
		}
		if(logger.isDebugEnabled()){
		logger.debug("[reqest处理完毕] [处理线程退出] [队列:" + queue.elementNum() + "]");
	}
	}

	public AdvancedFilePersistentQueue getQueue() {
		return queue;
	}

	public void setQueue(AdvancedFilePersistentQueue queue) {
		this.queue = queue;
	}

	public NpcinfoManagerImpl getNpcinfoManager() {
		return npcinfoManager;
	}

	public void setNpcinfoManager(NpcinfoManagerImpl npcinfoManager) {
		this.npcinfoManager = npcinfoManager;
	}

}