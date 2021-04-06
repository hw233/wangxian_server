package com.sqage.stat.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import com.sqage.stat.commonstat.entity.YinZiKuCun;
import com.sqage.stat.commonstat.manager.Impl.YinZiKuCunManagerImpl;
import com.sqage.stat.message.YINZIKUCUN_LOG_REQ;
import com.sqage.stat.model.YinZiKuCunFlow;
import com.xuanzhi.tools.queue.AdvancedFilePersistentQueue;
import com.xuanzhi.tools.transport.RequestMessage;

public class YinZiKuCunService implements Runnable {

	 AdvancedFilePersistentQueue yinZiKunCuqueue=null;
	 static Logger logger = Logger.getLogger(YinZiKuCunService.class);
	 YinZiKuCunManagerImpl yinZiKuCunManager;
	 
	 static YinZiKuCunService self;
	 public static int startnum=10;//处理队列中对象的最小数量
	 public static YinZiKuCunService getInstance() {
		return self;
	  }

	 public void init() throws Exception {
		self = this;
	  }

	 private boolean handle(List<RequestMessage> ls) {
		 
		 ArrayList<YinZiKuCun> yinZiKuCunList =new ArrayList<YinZiKuCun>();
		 for(int i=0;i<ls.size();i++)
		 {
			 RequestMessage message=ls.get(i);
			  if(message!=null){
				YINZIKUCUN_LOG_REQ req = (YINZIKUCUN_LOG_REQ) message;
				YinZiKuCunFlow flow = req.getYinZiKuCunFlow();
				if(logger.isDebugEnabled()){  logger.debug("银子库存日志"+flow.toString());}
				String fenQu=flow.getFenQu();
				
				if("百里沃野".equals(fenQu.trim())||fenQu.indexOf("pan")!=-1||fenQu.indexOf("hefu")!=-1){continue;}
				String col1=flow.getColumn1();
				String col2=flow.getColumn2();
				
				String col3=flow.getColumn3();
				String col4=flow.getColumn4();
				String col5=flow.getColumn5();
				
				Long count=flow.getCount();
				Long createDate=flow.getCreateDate();
				if(StatServerService.shetname!=null&&StatServerService.shetname.indexOf("韩国")!=-1){createDate+=60*60*1000;}//如果是韩国，时间加一小时，以平衡时区
				
				YinZiKuCun yinZiKuCun=new YinZiKuCun();
				
				yinZiKuCun.setFenQu(fenQu);
				yinZiKuCun.setColumn1(col1);
				yinZiKuCun.setColumn2(col2);
				
				yinZiKuCun.setColumn3(col3);
				yinZiKuCun.setColumn4(col4);
				yinZiKuCun.setColumn5(col5);
				
				yinZiKuCun.setCount(count);
				yinZiKuCun.setCreateDate(new Date(createDate));
				yinZiKuCunList.add(yinZiKuCun);
				
		 }else{
			 logger.info("YinZiKuCunFlow队列出现null对象");
		 }
			  
	 }
		 yinZiKuCunManager.addList(yinZiKuCunList);
		 return true;
	  }


	@Override
	public void run() {
	    if(yinZiKunCuqueue==null){yinZiKunCuqueue=StatServerService.yinZiKunCuqueue;}
		while (Thread.currentThread().isInterrupted() == false) {
			List<RequestMessage> ls=new ArrayList();
			long num=yinZiKunCuqueue.pushNum()-yinZiKunCuqueue.popNum();
			if(num>startnum){
			try {
				while(!yinZiKunCuqueue.isEmpty()) {
					RequestMessage req = (RequestMessage) yinZiKunCuqueue.pop();
					if(req != null){ ls.add(req);}
						
						if(ls.size()>startnum)
						{
							handle(ls);
							ls.clear();
						}
					
				}
				if(ls.size()>0){
					handle(ls);	
					ls.clear();
					Thread.sleep(60000);
				}
				
				if(yinZiKunCuqueue.isEmpty()){
					 synchronized(this){
						 wait(1200000);
					 }
				}
			} catch (Exception e) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e2) {
					e2.printStackTrace();
				}
				
				logger.error("[reqest处理错误] [队列yinZiKunCuqueue:" + yinZiKunCuqueue.elementNum() + "] ",e);
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
						logger.error("[reqest处理错误] " +e.getMessage() + " [队列:" + yinZiKunCuqueue.elementNum() + "] ",e);
					}
				 }
				}
		}
		if(logger.isDebugEnabled()){
		logger.debug("[reqest处理完毕] [处理线程退出] [队列:" + yinZiKunCuqueue.elementNum() + "]");
	}
	}

	public YinZiKuCunManagerImpl getYinZiKuCunManager() {
		return yinZiKuCunManager;
	}

	public void setYinZiKuCunManager(YinZiKuCunManagerImpl yinZiKuCunManager) {
		this.yinZiKuCunManager = yinZiKuCunManager;
	}

	
}
