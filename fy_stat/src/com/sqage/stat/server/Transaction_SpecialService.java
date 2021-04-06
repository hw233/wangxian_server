package com.sqage.stat.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.sqage.stat.commonstat.entity.Transaction_Special;
import com.sqage.stat.commonstat.manager.Impl.Transaction_SpecialManagerImpl;
import com.sqage.stat.message.TRANSACTION_SPECIAL_LOG_REQ;
import com.sqage.stat.model.Transaction_SpecialFlow;
import com.xuanzhi.tools.queue.AdvancedFilePersistentQueue;
import com.xuanzhi.tools.transport.RequestMessage;

public class Transaction_SpecialService implements Runnable {

	 AdvancedFilePersistentQueue queue=null;
	 static Logger logger = Logger.getLogger(Transaction_SpecialService.class);
	 Transaction_SpecialManagerImpl transaction_SpecialManager;
	 static Transaction_SpecialService self;
	 SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
	 public static Transaction_SpecialService getInstance() {
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
				
				long now = System.currentTimeMillis();
				TRANSACTION_SPECIAL_LOG_REQ req = (TRANSACTION_SPECIAL_LOG_REQ) message;
				Transaction_SpecialFlow flow = req.getTransaction_SpecialFlow();
				
				Long createDate=flow.getCreateDate();
				if(StatServerService.shetname!=null&&StatServerService.shetname.indexOf("韩国")!=-1){createDate+=60*60*1000;}//如果是韩国，时间加一小时，以平衡时区
					
				String fdaoJuName=flow.getFdaoJuName();
				String fenQu=flow.getFenQu();
				long fjiazi=flow.getFjiazhi();
				String fplayerName=flow.getFplayerName();
				String fquDao=flow.getFquDao();
				String fuserName=flow.getFuserName();
				String fuuid=flow.getFuuId();
				long fyinzi=flow.getFyinzi();
				long id=flow.getId();
				
				String todaoJuName=flow.getTodaoJuName();
				long tojiazi=flow.getTojiazhi();
				String toplayerName=flow.getToPlayername();
				String toquDao=flow.getToquDao();
				String toUserName=flow.getToUserName();
				String toUUid=flow.getTouuId();
				long toYinzi=flow.getToyinzi();
				String transactionType=flow.getTransactionType();
				
				
				long fRegisttime=flow.getFregisttime();
				long fCreatPlayerTime=flow.getFcreatPlayerTime();
				String flevel=flow.getFlevel();
				String fVip=flow.getFvip();
				long fTotalMoney=flow.getFtotalMoney();
				
                long toRegisttime=flow.getToRegisttime();
                long toCreatPlayerTime=flow.getToCreatPlayerTime();
                String toLevel=flow.getToLevel();
                String toVip=flow.getToVip();
				long toTotalMoney=flow.getToTotalMoney();
			
				Transaction_Special transaction_Special=new Transaction_Special();
				
				if(createDate!=null){transaction_Special.setCreateDate(new Date(createDate));}else{transaction_Special.setCreateDate(new Date());}
				transaction_Special.setFdaoJuName(fdaoJuName);
				transaction_Special.setFenQu(fenQu);
				transaction_Special.setFjiazhi(fjiazi);
				transaction_Special.setFplayerName(fplayerName);
				transaction_Special.setFquDao(fquDao);
				transaction_Special.setFuserName(fuserName);
				transaction_Special.setFuuId(fuuid);
				transaction_Special.setFyinzi(fyinzi);
				transaction_Special.setId(id);
				
				transaction_Special.setTodaoJuName(todaoJuName);
				transaction_Special.setTojiazhi(tojiazi);
				transaction_Special.setToPlayername(toplayerName);
				transaction_Special.setToquDao(toquDao);
				transaction_Special.setToUserName(toUserName);
				transaction_Special.setTouuId(toUUid);
				transaction_Special.setToyinzi(toYinzi);
				transaction_Special.setTransactionType(transactionType);
				
				transaction_Special.setfRegisttime(new Date(fRegisttime));
				transaction_Special.setfCreatPlayerTime(new Date(fCreatPlayerTime));
				transaction_Special.setfLevel(flevel);
				transaction_Special.setfVip(fVip);
				transaction_Special.setfTotalMoney(fTotalMoney);

				
				transaction_Special.setToRegisttime(new Date(toRegisttime));
				transaction_Special.setToCreatPlayerTime(new Date(toCreatPlayerTime));
				transaction_Special.setToLevel(toLevel);
				transaction_Special.setToVip(toVip);
				transaction_Special.setToTotalMoney(toTotalMoney);
				
				Transaction_Special returngameChongZhi=transaction_SpecialManager.add(transaction_Special);
				logger.info("[游戏异常交易信息上报] " + flow.toString() + " [队列:" + queue.elementNum() + "] [" + (System.currentTimeMillis() - now) + "ms]");
				if(returngameChongZhi!=null){ result=true; }
				 }
		 return result;
	  }


	 private boolean doTransaction_SpecialFlow(RequestMessage message) {
			
			boolean result=false;
			long now = System.currentTimeMillis();
			TRANSACTION_SPECIAL_LOG_REQ req = (TRANSACTION_SPECIAL_LOG_REQ) message;
			Transaction_SpecialFlow flow = req.getTransaction_SpecialFlow();
			
			Long createDate=flow.getCreateDate();
			String fdaoJuName=flow.getFdaoJuName();
			String fenQu=flow.getFenQu();
			long fjiazi=flow.getFjiazhi();
			String fplayerName=flow.getFplayerName();
			String fquDao=flow.getFquDao();
			String fuserName=flow.getFuserName();
			String fuuid=flow.getFuuId();
			long fyinzi=flow.getFyinzi();
			long id=flow.getId();
			
			String todaoJuName=flow.getTodaoJuName();
			long tojiazi=flow.getTojiazhi();
			String toplayerName=flow.getToPlayername();
			String toquDao=flow.getToquDao();
			String toUserName=flow.getToUserName();
			String toUUid=flow.getTouuId();
			long toYinzi=flow.getToyinzi();
			String transactionType=flow.getTransactionType();
			
			Transaction_Special transaction_Special=new Transaction_Special();
			
			if(createDate!=null){transaction_Special.setCreateDate(new Date(createDate));}else{transaction_Special.setCreateDate(new Date());}
			transaction_Special.setFdaoJuName(fdaoJuName);
			transaction_Special.setFenQu(fenQu);
			transaction_Special.setFjiazhi(fjiazi);
			transaction_Special.setFplayerName(fplayerName);
			transaction_Special.setFquDao(fquDao);
			transaction_Special.setFuserName(fuserName);
			transaction_Special.setFuuId(fuuid);
			transaction_Special.setFyinzi(fyinzi);
			transaction_Special.setId(id);
			
			transaction_Special.setTodaoJuName(todaoJuName);
			transaction_Special.setTojiazhi(tojiazi);
			transaction_Special.setToPlayername(toplayerName);
			transaction_Special.setToquDao(toquDao);
			transaction_Special.setToUserName(toUserName);
			transaction_Special.setTouuId(toUUid);
			transaction_Special.setToyinzi(toYinzi);
			transaction_Special.setTransactionType(transactionType);
			
			Transaction_Special returngameChongZhi=transaction_SpecialManager.add(transaction_Special);
			
			logger.info("[游戏异常交易信息上报] " + flow.toString() + " [队列:" + queue.elementNum() + "] [" + (System.currentTimeMillis() - now) + "ms]");
			if(returngameChongZhi!=null){ result=true;	}
			return true;
		}

	 
	@Override
	public void run() {
	    if(queue==null){queue=StatServerService.transaction_Specialqueue;}
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
//						if(!handle(req))
//						{
//							onLineUsersqueue.push(req);
//						}
					}
				}
				if(ls.size()>0){
					handle(ls);	
					ls.clear();
					Thread.sleep(20000);
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

	public Transaction_SpecialManagerImpl getTransaction_SpecialManager() {
		return transaction_SpecialManager;
	}

	public void setTransaction_SpecialManager(Transaction_SpecialManagerImpl transactionSpecialManager) {
		transaction_SpecialManager = transactionSpecialManager;
	}

}
