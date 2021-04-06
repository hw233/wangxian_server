package com.sqage.stat.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.sqage.stat.commonstat.entity.Transaction;
import com.sqage.stat.commonstat.manager.Impl.TransactionManagerImpl;
import com.sqage.stat.message.TRANSACTION_LOG_REQ;
import com.sqage.stat.model.TransactionFlow;
import com.xuanzhi.tools.queue.AdvancedFilePersistentQueue;
import com.xuanzhi.tools.transport.RequestMessage;

public class TransactionService implements Runnable {

	 AdvancedFilePersistentQueue queue=null;
	 static Logger logger = Logger.getLogger(TransactionService.class);
	 TransactionManagerImpl transactionManager;
	 static TransactionService self;
	 public static int startnum=1000;//处理队列中对象的最小数量
	 SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
	 public static TransactionService getInstance() { return self;  }

	 public void init() throws Exception {	self = this;  }

	 private boolean handle(List<RequestMessage> ls) {
		 boolean result=true;
		 ArrayList<Transaction> transLs=new ArrayList<Transaction>();
		 for(int i=0;i<ls.size();i++)
		 {
			 RequestMessage message=ls.get(i);
				
				long now = System.currentTimeMillis();
				TRANSACTION_LOG_REQ req = (TRANSACTION_LOG_REQ) message;
				TransactionFlow flow = req.getTransactionFlow();
				
				Long createDate=flow.getCreateDate();
				
				 if(StatServerService.shetname!=null&&StatServerService.shetname.indexOf("韩国")!=-1){createDate+=60*60*1000;}//如果是韩国，时间加一小时，以平衡时区
					
				Integer danJia=flow.getDanjia();
				String daoJuName=flow.getDaoJuName();
				if(daoJuName.indexOf("角色装备")!=-1||daoJuName.indexOf("坐骑装备")!=-1)  continue;
				
				Integer daoJuNum=flow.getDaojunum();
				String fenQu=flow.getFenQu();
				
				String fgameLevel=flow.getFgameLevel();
				String fuserName=flow.getFuserName();
				String toGameLevel=flow.getToGameLevel();
				String toUserName=flow.getToUserName();
				String transactionType=flow.getTransactionType();
				
				 String daoJuColor=flow.getDaoJuColor();
				 String daoJuLevel=flow.getDaoJuLevel();
				 String bindType=flow.getBindType();
				 String jixing=flow.getJixing();
				 
				 String fvip=flow.getFvip();
				 String tovip=flow.getTovip();
				 String fguoJia=flow.getFguoJia();
				 String toguoJia=flow.getToguoJia();
				 
				 
				//保存道具交换信息
				Transaction transaction=new Transaction();
				transaction.setCreateDate(new Date(createDate));
				transaction.setDanjia(danJia);
				transaction.setDaoJuName(daoJuName);
				transaction.setDaojunum(daoJuNum);
				transaction.setFenQu(fenQu);
				transaction.setFgameLevel(fgameLevel);
				transaction.setFuserName(fuserName);
				transaction.setToGameLevel(toGameLevel);
				
				transaction.setToUserName(toUserName);
				transaction.setTransactionType(transactionType);
				
				transaction.setBindType(bindType);
				transaction.setDaoJuColor(daoJuColor);
				transaction.setDaoJuLevel(daoJuLevel);
				transaction.setJixing(jixing);
				
				transaction.setFvip(fvip);
				transaction.setFguoJia(fguoJia);
				transaction.setTovip(tovip);
				transaction.setToguoJia(toguoJia);
				
				transLs.add(transaction);
				//Transaction returntransaction=transactionManager.add(transaction);
				
				if(logger.isDebugEnabled()){	
				logger.debug("[道具交换信息上报] " + flow.toString() + " [队列:" + queue.elementNum() + "] [" + (System.currentTimeMillis() - now) + "ms]");
				}
//				if(returntransaction!=null){
//					result=true;
//				}else{
//					logger.debug("[道具交换信息上报入数据库失败] " + flow.toString() + " [队列:" + queue.elementNum() + "] [" + (System.currentTimeMillis() - now) + "ms]");
//				}
				
				 }
		 
		 transactionManager.addList(transLs);
		 
		 return true;
	  }

	 
//	 private boolean doTransaction(RequestMessage message) {
//			boolean result=false;
//			long now = System.currentTimeMillis();
//			TRANSACTION_LOG_REQ req = (TRANSACTION_LOG_REQ) message;
//			TransactionFlow flow = req.getTransactionFlow();
//			if(logger.isDebugEnabled()){
//			logger.debug("[道具交换信息上报] " + flow.toString() + " [队列:" + queue.elementNum() + "] [" + (System.currentTimeMillis() - now) + "ms]");
//			}
//			Long createDate=flow.getCreateDate();
//			Integer danJia=flow.getDanjia();
//			String daoJuName=flow.getDaoJuName();
//			Integer daoJuNum=flow.getDaojunum();
//			String fenQu=flow.getFenQu();
//			
//			String fgameLevel=flow.getFgameLevel();
//			String fuserName=flow.getFuserName();
//			String toGameLevel=flow.getToGameLevel();
//			String toUserName=flow.getToUserName();
//			String transactionType=flow.getTransactionType();
//			
//			 String daoJuColor=flow.getDaoJuColor();
//			 String daoJuLevel=flow.getDaoJuLevel();
//			 String bindType=flow.getBindType();
//			 String jixing=flow.getJixing();
//			 
//			//保存道具交换信息
//			Transaction transaction=new Transaction();
//			transaction.setCreateDate(new Date(createDate));
//			transaction.setDanjia(danJia);
//			transaction.setDaoJuName(daoJuName);
//			transaction.setDaojunum(daoJuNum);
//			transaction.setFenQu(fenQu);
//			transaction.setFgameLevel(fgameLevel);
//			transaction.setFuserName(fuserName);
//			transaction.setToGameLevel(toGameLevel);
//			
//			transaction.setToUserName(toUserName);
//			transaction.setTransactionType(transactionType);
//			
//			transaction.setBindType(bindType);
//			transaction.setDaoJuColor(daoJuColor);
//			transaction.setDaoJuLevel(daoJuLevel);
//			transaction.setJixing(jixing);
//			
//			Transaction returntransaction=transactionManager.add(transaction);
//			if(returntransaction!=null){result=true;}
//			
//			return result;
//		}
	 
	@Override
	public void run() {
		if (queue == null) {
			queue = StatServerService.transactionqueue;
		}
		while (Thread.currentThread().isInterrupted() == false) {
			List<RequestMessage> ls = new ArrayList();
			long num = queue.pushNum() - queue.popNum();
			if (num > startnum) {
				try {
					while (!queue.isEmpty()) {
						RequestMessage req = (RequestMessage) queue.pop();
						if (req != null) {
							ls.add(req);
							if (ls.size() > startnum) {
								handle(ls);
								ls.clear();
							}
						}
					}
					if (ls.size() > 0) {
						handle(ls);
						ls.clear();
						Thread.sleep(100);
					}
				} catch (Exception e) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e2) {
						e2.printStackTrace();
					}
					logger.error("[reqest处理错误] [队列queue:" + queue.elementNum() + "] ", e);
				}
			} else {
				synchronized (this) {
					try {
						wait(2000);
					} catch (InterruptedException e) {
						logger.info("[reqest处理错误] " + e.getMessage() + " [队列:" + queue.elementNum() + "] ", e);
					}
				}
			}

		}
		if (logger.isDebugEnabled()) {
			logger.debug("[reqest处理完毕] [处理线程退出] [队列:" + queue.elementNum() + "]");
		}
	}

	public TransactionManagerImpl getTransactionManager() {
		return transactionManager;
	}

	public void setTransactionManager(TransactionManagerImpl transactionManager) {
		this.transactionManager = transactionManager;
	}

}
