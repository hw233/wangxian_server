package com.sqage.stat.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.sqage.stat.commonstat.entity.Libao;
import com.sqage.stat.commonstat.manager.Impl.LibaoManagerImpl;
import com.sqage.stat.message.LIBAO_LOG_REQ;
import com.sqage.stat.model.LibaoFlow;
import com.xuanzhi.tools.queue.AdvancedFilePersistentQueue;
import com.xuanzhi.tools.transport.RequestMessage;

public class LiBaoqueueService implements Runnable {

	 AdvancedFilePersistentQueue liBaoqueue=null;
	 static Logger logger = Logger.getLogger(LiBaoqueueService.class);
	 LibaoManagerImpl libaoManager;
	 static LiBaoqueueService self;
	 public static int startnum=20;//处理队列中对象的最小数量
	 SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
	 public static LiBaoqueueService getInstance() {
		return self;
	  }

	 public void init() throws Exception {
		self = this;
	  }

	 private boolean handle(List<RequestMessage> ls) {
		  
		 ArrayList<Libao> libaoList =new ArrayList<Libao>();
		 boolean result=false;
		 for(int i=0;i<ls.size();i++)
		 {
			 RequestMessage message=ls.get(i);
			  if(message!=null){
				
				long now = System.currentTimeMillis();
				LIBAO_LOG_REQ req = (LIBAO_LOG_REQ) message;
				LibaoFlow flow = req.getLibaoFlow();
				
				long createDate=flow.getCreateDate();
				if(StatServerService.shetname!=null&&StatServerService.shetname.indexOf("韩国")!=-1){createDate+=60*60*1000;}//如果是韩国，时间加一小时，以平衡时区
					
				String column1=flow.getColumn1();
				String column2=flow.getColumn2();
				long count=flow.getCount();
				long danjia=flow.getDanjia();
				String daoJuName=flow.getDaoJuName();
				String fenqu=flow.getFenQu();
				int type=flow.getType();
				
				
				
				Libao libao=new Libao();
				
				libao.setColumn1(column1);
				libao.setColumn2(column2);
				libao.setCount(count);
				libao.setCreateDate(new Date(createDate));
				libao.setDanjia(danjia);
				libao.setDaoJuName(daoJuName);
				libao.setFenQu(fenqu);
				libao.setType(type);

				libaoList.add(libao);				
				
				logger.debug("[玩家在线人数上报] " + flow.toString() + " [队列:" + liBaoqueue.elementNum() + "] [" + (System.currentTimeMillis() - now) + "ms]");
		 }else{
			 logger.info("LibaoFlow队列出现null对象");
		 }
	 }
		 result=libaoManager.addList(libaoList);
		 return result;
	  }

	@Override
	public void run() {
	    if(liBaoqueue==null){liBaoqueue=StatServerService.liBaoqueue;}
		while (Thread.currentThread().isInterrupted() == false) {
			List<RequestMessage> ls=new ArrayList();
			long num=liBaoqueue.pushNum()-liBaoqueue.popNum();
			if(num>startnum){
			try {
				while(!liBaoqueue.isEmpty()) {
					RequestMessage req = (RequestMessage) liBaoqueue.pop();
					if(req != null){ ls.add(req);}
						
						if(ls.size()>startnum)
						{
							handle(ls);
							ls.clear();
						}
//						if(!handle(req))
//						{
//							liBaoqueue.push(req);
//						}
					
				}
				if(ls.size()>0){
					handle(ls);	
					ls.clear();
					Thread.sleep(100);
				}
				
				if(liBaoqueue.isEmpty()){
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
				
				logger.error("[reqest处理错误] [队列liBaoqueue:" + liBaoqueue.elementNum() + "] ",e);
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
						logger.info("[reqest处理错误] " +e.getMessage() + " [队列:" + liBaoqueue.elementNum() + "] ",e);
					}
				 }
				}
		}
		if(logger.isDebugEnabled()){
		logger.debug("[reqest处理完毕] [处理线程退出] [队列:" + liBaoqueue.elementNum() + "]");
	}
	}

	public LibaoManagerImpl getLibaoManager() {
		return libaoManager;
	}

	public void setLibaoManager(LibaoManagerImpl libaoManager) {
		this.libaoManager = libaoManager;
	}

	
}
