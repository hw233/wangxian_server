package com.sqage.stat.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.sqage.stat.commonstat.entity.FuMo;
import com.sqage.stat.commonstat.manager.Impl.FuMoManagerImpl;
import com.sqage.stat.message.FUMO_LOG_REQ;
import com.sqage.stat.model.FuMoFlow;
import com.xuanzhi.tools.queue.AdvancedFilePersistentQueue;
import com.xuanzhi.tools.transport.RequestMessage;

public class FuMoStatService implements Runnable {

	 AdvancedFilePersistentQueue queue=null;
	 static Logger logger = Logger.getLogger(FuMoStatService.class);
	 public static int startnum=1;//处理队列中对象的最小数量
	 FuMoManagerImpl fuMoManager;
	 static FuMoStatService self;
	 SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
	 public static FuMoStatService getInstance() {
		return self;
	  }

	 public void init() throws Exception {
		self = this;
	  }

	 private void handle(List<RequestMessage> ls) {
		 ArrayList<FuMo> fuMoList1   = new ArrayList<FuMo>();
		 ArrayList<FuMo> fuMoList2   = new ArrayList<FuMo>();
		 
		 for(int i=0;i<ls.size();i++)
		 {
			 RequestMessage message=ls.get(i);
				long now = System.currentTimeMillis();
				FUMO_LOG_REQ req = (FUMO_LOG_REQ) message;
				FuMoFlow flow = req.getFuMoFlow();
				if(logger.isDebugEnabled()){logger.debug("[佛魔信息上报] " + flow.toString() + " [队列:" + queue.elementNum() + "] [" + (System.currentTimeMillis() - now) + "ms]"); }
				
				String column1 =flow.getColumn1();
				String column2 =flow.getColumn2();
				long createDate=flow.getCreateTime();
				String type=flow.getType();
				String fenQu=flow.getFenQu();
				String foMoWuPinName=flow.getFoMoWuPinName();
				String userName=flow.getUserName();
				
				FuMo fuMo=new FuMo();
				fuMo.setColumn1(column1);
				fuMo.setColumn2(column2);
				fuMo.setCreateTime(new Date(createDate));
				fuMo.setFenQu(fenQu);
				fuMo.setFoMoWuPinName(foMoWuPinName);
				fuMo.setType(type);
				//fuMo.setUsedTime(usedTime);
				fuMo.setUserName(userName);
				if("1".equals(type)){fuMoList1.add(fuMo);}
				if("2".equals(type)){fuMoList2.add(fuMo);}
				
		 }	
		 if(fuMoList1.size()>0)  { fuMoManager.addUseFoMoList(fuMoList1);	}
		 if(fuMoList2.size()>0)  { fuMoManager.addFinishFuMoList(fuMoList2);	}
	  }


	@Override
	public void run() {
	    if(queue==null){queue=StatServerService.fuMoFlowqueue;}
		while (Thread.currentThread().isInterrupted() == false) {
			try {
				List<RequestMessage> ls=new ArrayList();
				while(!queue.isEmpty()) {
					RequestMessage req = (RequestMessage) queue.pop();
					if(req != null){
						ls.add(req);
						if(ls.size()>=startnum)
						{
							handle(ls);
							ls.clear();
						}
					}
				}
				if(ls.size()>0){
					handle(ls);	
					ls.clear();
					Thread.sleep(110000);
				}
				
				if(queue.isEmpty()){
					 synchronized(this){
						 wait(160000);
					 }
				}
			} catch (Exception e) {
				try {
					Thread.sleep(11100);
				} catch (InterruptedException e2) {
					e2.printStackTrace();
				}
				
				logger.error("[reqest处理错误] [队列queue:" + queue.elementNum() + "] ",e);
				try {
					Thread.sleep(100000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
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

	public FuMoManagerImpl getFuMoManager() {
		return fuMoManager;
	}

	public void setFuMoManager(FuMoManagerImpl fuMoManager) {
		this.fuMoManager = fuMoManager;
	}

}