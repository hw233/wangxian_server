package com.sqage.stat.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.sqage.stat.commonstat.entity.Transaction_Face;
import com.sqage.stat.commonstat.manager.Impl.Transaction_FaceManagerImpl;
import com.sqage.stat.message.TRANSACTION_FACE_LOG_REQ;
import com.sqage.stat.model.Transaction_FaceFlow;
import com.xuanzhi.tools.queue.AdvancedFilePersistentQueue;
import com.xuanzhi.tools.transport.RequestMessage;

public class Transaction_FaceService implements Runnable {

	 AdvancedFilePersistentQueue queue=null;
	 static Logger logger = Logger.getLogger(Transaction_FaceService.class);
	 Transaction_FaceManagerImpl transaction_FaceManager;
	 static Transaction_FaceService self;
	 SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
	 public static Transaction_FaceService getInstance() {
		return self;
	  }

	 public void init() throws Exception {
		self = this;
	  }

	 private void handle(List<RequestMessage> ls) {
		 ArrayList<Transaction_Face> transaction_FaceList=new ArrayList();
		 for(int i=0;i<ls.size();i++)
		 {
			 RequestMessage message=ls.get(i);
				long now = System.currentTimeMillis();
				TRANSACTION_FACE_LOG_REQ req = (TRANSACTION_FACE_LOG_REQ) message;
				
				Transaction_FaceFlow flow = req.getTransaction_FaceFlow();
				Long createDate=flow.getCreateDate();
				String zhenYing=flow.getZhenYing();
				
				String fdaoJu=flow.getFdaoJu();
				String fenQu=flow.getFenQu();
				String flevel=flow.getFlevel();
				long fmoney=flow.getFmoney();
				String fquDao=flow.getFquDao();
				String fuserName=flow.getFuserName();
				String fvip=flow.getFvip();
				
				String todaoJu=flow.getTodaoJu();
				String toLevel=flow.getToLevel();
				long toMoney=flow.getToMoney();
				String toquDao=flow.getToquDao();
				String toUserName=flow.getToUserName();
				String toVip=flow.getToVip();

				Transaction_Face transaction_Face=new Transaction_Face();
				
				transaction_Face.setCreateDate(createDate);
				transaction_Face.setZhenYing(zhenYing);
				transaction_Face.setFdaoJu(fdaoJu);
				transaction_Face.setFenQu(fenQu);
				transaction_Face.setFlevel(flevel);
				transaction_Face.setFmoney(fmoney);
				transaction_Face.setFquDao(fquDao);
				transaction_Face.setFuserName(fuserName);
				transaction_Face.setFvip(fvip);
				
				transaction_Face.setTodaoJu(todaoJu);
				transaction_Face.setToLevel(toLevel);
				transaction_Face.setToMoney(toMoney);
				transaction_Face.setToquDao(toquDao);
				transaction_Face.setToUserName(toUserName);
				transaction_Face.setToVip(toVip);
				
				transaction_FaceList.add(transaction_Face);
				if(logger.isDebugEnabled()){logger.debug("[游戏面对面交易信息上报] " + flow.toString() + " [队列:" + queue.elementNum() + "] [" + (System.currentTimeMillis() - now) + "ms]"); }
		 
		 }	
		 transaction_FaceManager.addList(transaction_FaceList);
	  }


	@Override
	public void run() {
	    if(queue==null){queue=StatServerService.transaction_Facequeue;}
		while (Thread.currentThread().isInterrupted() == false) {
			try {
				List<RequestMessage> ls=new ArrayList();
				while(!queue.isEmpty()) {
					RequestMessage req = (RequestMessage) queue.pop();
					if(req != null){
						ls.add(req);
						if(ls.size()>200)
						{
							handle(ls);
							ls.clear();
						}
					}
				}
				if(ls.size()>0){
					handle(ls);	
					ls.clear();
					Thread.sleep(3000);
				}
				
				if(queue.isEmpty()){
					 synchronized(this){
						 wait(1000*60*5);
					 }
				}
			} catch (Exception e) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e2) {
					e2.printStackTrace();
				}
				
				logger.error("[reqest处理错误] [队列queue:" + queue.elementNum() + "] ",e);
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

	public Transaction_FaceManagerImpl getTransaction_FaceManager() {
		return transaction_FaceManager;
	}

	public void setTransaction_FaceManager(Transaction_FaceManagerImpl transactionFaceManager) {
		transaction_FaceManager = transactionFaceManager;
	}

}
