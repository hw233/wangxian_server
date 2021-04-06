package com.sqage.stat.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.sqage.stat.commonstat.entity.HuoDonginfo;
import com.sqage.stat.commonstat.manager.Impl.HuoDonginfoManagerImpl;
import com.sqage.stat.message.ACCEPTHUODONGINFO_LOG_REQ;
import com.sqage.stat.model.AcceptHuoDonginfoFlow;
import com.xuanzhi.tools.queue.AdvancedFilePersistentQueue;
import com.xuanzhi.tools.transport.RequestMessage;

public class AcceptHuoDonginfoService implements Runnable {

	 AdvancedFilePersistentQueue queue=null;
	 static Logger logger = Logger.getLogger(AcceptHuoDonginfoService.class);
	 HuoDonginfoManagerImpl huoDonginfoManager;
	 static AcceptHuoDonginfoService self;
//	 SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
	 public static AcceptHuoDonginfoService getInstance() {
		return self;
	  }

	 public void init() throws Exception {
		self = this;
	  }

	 private boolean handle(List<RequestMessage> ls) {
		  
		 List<HuoDonginfo> huoDonginfoList=new ArrayList<HuoDonginfo>();
		 for(int i=0;i<ls.size();i++)
		 {
			
			 RequestMessage message=ls.get(i);
			 if(message!=null){
			 boolean result=false;
				long now = System.currentTimeMillis();
				ACCEPTHUODONGINFO_LOG_REQ req = (ACCEPTHUODONGINFO_LOG_REQ) message;
				AcceptHuoDonginfoFlow flow = req.getAcceptHuoDonginfoFlow();
				
				
				Long createDate=flow.getCreateDate();
				String fenQu=flow.getFenQu();
				String gameLevel=flow.getGameLevel();
				String taskName=flow.getTaskName();
				String taskType=flow.getTaskType();
				String userName=flow.getUserName();
				String jixing=flow.getJixing();
				
				HuoDonginfo huoDonginfo=new HuoDonginfo();
				huoDonginfo.setCreateDate(new Date(createDate));
				huoDonginfo.setFenQu(fenQu);
				huoDonginfo.setGameLevel(gameLevel);
				huoDonginfo.setGetDaoJu(0);
				
				huoDonginfo.setGetWuPin(0);
				huoDonginfo.setGetYOuXiBi(0);
				huoDonginfo.setStatus("接受");
				huoDonginfo.setTaskName(taskName);
				
				huoDonginfo.setTaskType(taskType);
				huoDonginfo.setUserName(userName);
				huoDonginfo.setJixing(jixing);
				huoDonginfoList.add(huoDonginfo);
				logger.info("[接受活动信息上报] " + flow.toString() + " [队列:" + queue.elementNum() + "] [" + (System.currentTimeMillis() - now) + "ms]");
				
				//HuoDonginfo resulthuoDonginfo=huoDonginfoManager.add(huoDonginfo);
		 }else{
			 
			 logger.info("HuoDonginfo信息为null");
		 }
	 }
		 return huoDonginfoManager.addList(huoDonginfoList);
	  }
//	 private boolean  doAcceptHuoDonginfoFlow(RequestMessage message) {
//			boolean result=false;
//			long now = System.currentTimeMillis();
//			ACCEPTHUODONGINFO_LOG_REQ req = (ACCEPTHUODONGINFO_LOG_REQ) message;
//			AcceptHuoDonginfoFlow flow = req.getAcceptHuoDonginfoFlow();
//			if(logger.isDebugEnabled()){
//			logger.debug("[接受活动信息上报] " + flow.toString() + " [队列:" + queue.elementNum() + "] [" + (System.currentTimeMillis() - now) + "ms]");
//			}
//			Long createDate=flow.getCreateDate();
//			String fenQu=flow.getFenQu();
//			String gameLevel=flow.getGameLevel();
//			String taskName=flow.getTaskName();
//			String taskType=flow.getTaskType();
//			String userName=flow.getUserName();
//			String jixing=flow.getJixing();
//			
//			HuoDonginfo huoDonginfo=new HuoDonginfo();
//			huoDonginfo.setCreateDate(new Date(createDate));
//			huoDonginfo.setFenQu(fenQu);
//			huoDonginfo.setGameLevel(gameLevel);
//			huoDonginfo.setGetDaoJu(0);
//			
//			huoDonginfo.setGetWuPin(0);
//			huoDonginfo.setGetYOuXiBi(0);
//			huoDonginfo.setStatus("接受");
//			huoDonginfo.setTaskName(taskName);
//			
//			huoDonginfo.setTaskType(taskType);
//			huoDonginfo.setUserName(userName);
//			huoDonginfo.setJixing(jixing);
//			
//			HuoDonginfo resulthuoDonginfo=huoDonginfoManager.add(huoDonginfo);
//			if(resulthuoDonginfo!=null){ result=true;}
//			return result;
//		}

	 
	@Override
	public void run() {
	    if(queue==null){queue=StatServerService.acceptHuoDonginfoqueue;}
		while (Thread.currentThread().isInterrupted() == false) {
			try {
				List<RequestMessage> ls=new ArrayList();
				while(!queue.isEmpty()) {
					RequestMessage req = (RequestMessage) queue.pop();
					if(req != null){ ls.add(req);}
						
						if(ls.size()>100)
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
				
				if(queue.isEmpty()){
					 synchronized(this){
						 wait(100);
					 }
				}
			} catch (Exception e) {
				logger.error("[reqest处理错误] [队列acceptHuoDonginfoqueue:" + queue.elementNum() + "] ",e);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
		if(logger.isDebugEnabled()){
		logger.debug("[reqest处理完毕] [处理线程退出] [队列:" + queue.elementNum() + "]");
	}
	}

	public HuoDonginfoManagerImpl getHuoDonginfoManager() {
		return huoDonginfoManager;
	}

	public void setHuoDonginfoManager(HuoDonginfoManagerImpl huoDonginfoManager) {
		this.huoDonginfoManager = huoDonginfoManager;
	}

}
