package com.sqage.stat.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.sqage.stat.commonstat.entity.HuoDonginfo;
import com.sqage.stat.commonstat.manager.Impl.HuoDonginfoManagerImpl;
import com.sqage.stat.message.FINISHHUODONGINFO_LOG_REQ;
import com.sqage.stat.model.FinishHuoDonginfoFlow;
import com.xuanzhi.tools.queue.AdvancedFilePersistentQueue;
import com.xuanzhi.tools.transport.RequestMessage;

public class FinishHuoDonginfoService implements Runnable {

	 AdvancedFilePersistentQueue finishHuoDongqueue=null;
	 static Logger logger = Logger.getLogger(FinishHuoDonginfoService.class);
	 HuoDonginfoManagerImpl huoDonginfoManager;
	 static FinishHuoDonginfoService self;
	 SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
	 public static FinishHuoDonginfoService getInstance() {
		return self;
	  }

	 public void init() throws Exception {
		self = this;
	  }

	 private boolean handle(List<RequestMessage> ls) {
		 boolean result=true;
		 for(int i=0;i<ls.size();i++)
		 {
			 RequestMessage message=ls.get(i);
				if(message!=null){
				long now = System.currentTimeMillis();
				FINISHHUODONGINFO_LOG_REQ req = (FINISHHUODONGINFO_LOG_REQ) message;
				FinishHuoDonginfoFlow flow = req.getFinishHuoDonginfoFlow();
				String fenQu=flow.getFenQu();
				Integer getDaoJu=flow.getGetDaoJu();
				Integer getWuPin=flow.getGetWuPin();
				Integer getYouXiBi=flow.getGetYOuXiBi();
				String status=flow.getStatus();
				String taskName=flow.getTaskName();
				String userName=flow.getUserName();
				String award=flow.getAward();
				String jixing=flow.getJixing();
				
				String sql="select * from stat_huodong_info t where t.username='"+userName+"' and t.taskname='"+taskName+"'";
				List<HuoDonginfo> HuoDonginfoList=huoDonginfoManager.getBySql(sql);
				if(HuoDonginfoList!=null&&HuoDonginfoList.size()>0){
					HuoDonginfo huoDonginfo= HuoDonginfoList.get(0);
					huoDonginfo.setStatus(status);
					huoDonginfo.setGetDaoJu(getDaoJu);
					huoDonginfo.setGetWuPin(getWuPin);
					huoDonginfo.setGetYOuXiBi(getYouXiBi);
					huoDonginfo.setAward(award);
					huoDonginfo.setJixing(jixing);
					result=huoDonginfoManager.update(huoDonginfo);
				}else{
					result=true;
				}
				logger.info("[完成活动信息上报] " + flow.toString() + " [队列:" + finishHuoDongqueue.elementNum() + "] [" + (System.currentTimeMillis() - now) + "ms]");
		 }else{
			 logger.info("FinishHuoDonginfoFlow队列出现null对象");
		 }
	 }
		 return result;
	  }

//	 private boolean doFinishHuoDonginfoFlow(RequestMessage message) {
//			boolean result=false;
//			long now = System.currentTimeMillis();
//			FINISHHUODONGINFO_LOG_REQ req = (FINISHHUODONGINFO_LOG_REQ) message;
//			FinishHuoDonginfoFlow flow = req.getFinishHuoDonginfoFlow();
//			if(logger.isDebugEnabled()){
//			logger.info("[完成活动信息上报] " + flow.toString() + " [队列:" + queue.elementNum() + "] [" + (System.currentTimeMillis() - now) + "ms]");
//			}
//			String fenQu=flow.getFenQu();
//			Integer getDaoJu=flow.getGetDaoJu();
//			Integer getWuPin=flow.getGetWuPin();
//			Integer getYouXiBi=flow.getGetYOuXiBi();
//			String status=flow.getStatus();
//			String taskName=flow.getTaskName();
//			String userName=flow.getUserName();
//			String award=flow.getAward();
//			String jixing=flow.getJixing();
//			
//			String sql="select * from stat_huodong_info t " +
//					" where t.username='"+userName+"' and t.taskname='"+taskName+"'";
//			List<HuoDonginfo> HuoDonginfoList=huoDonginfoManager.getBySql(sql);
//			if(HuoDonginfoList!=null&&HuoDonginfoList.size()>0){
//				HuoDonginfo huoDonginfo= HuoDonginfoList.get(0);
//				huoDonginfo.setStatus(status);
//				huoDonginfo.setGetDaoJu(getDaoJu);
//				huoDonginfo.setGetWuPin(getWuPin);
//				huoDonginfo.setGetYOuXiBi(getYouXiBi);
//				huoDonginfo.setAward(award);
//				huoDonginfo.setJixing(jixing);
//				result=huoDonginfoManager.update(huoDonginfo);
//			}else{
//				result=true;
//			}
//			return result;
//		}
	 
	@Override
	public void run() {
	    if(finishHuoDongqueue==null){finishHuoDongqueue=StatServerService.finishHuoDonginfoqueue;}
		while (Thread.currentThread().isInterrupted() == false) {
			try {
				List<RequestMessage> ls=new ArrayList();
				while(!finishHuoDongqueue.isEmpty()) {
					RequestMessage req = (RequestMessage) finishHuoDongqueue.pop();
					if(req != null){
						ls.add(req);
						
						if(ls.size()>10000)
						{
							handle(ls);
							ls.clear();
						}
//						if(!handle(req))
//						{
//							onLineUsersqueue.push(req);
//						}
					}
				}
				if(ls.size()>0){
					handle(ls);	
					ls.clear();
					Thread.sleep(100);
				}
				
				if(finishHuoDongqueue.isEmpty()){
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
				
				logger.error("[reqest处理错误] [队列queue:" + finishHuoDongqueue.elementNum() + "] ",e);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
		if(logger.isDebugEnabled()){
		logger.debug("[reqest处理完毕] [处理线程退出] [队列:" + finishHuoDongqueue.elementNum() + "]");
	}
	}

	public HuoDonginfoManagerImpl getHuoDonginfoManager() {
		return huoDonginfoManager;
	}

	public void setHuoDonginfoManager(HuoDonginfoManagerImpl huoDonginfoManager) {
		this.huoDonginfoManager = huoDonginfoManager;
	}

}
