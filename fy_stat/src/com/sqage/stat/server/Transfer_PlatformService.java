package com.sqage.stat.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.sqage.stat.commonstat.entity.Transfer_Platform;
import com.sqage.stat.commonstat.manager.Impl.Transaction_FaceManagerImpl;
import com.sqage.stat.message.TRANSFER_PLATFORM_LOG_REQ;
import com.sqage.stat.model.Transfer_PlatformFlow;
import com.xuanzhi.tools.queue.AdvancedFilePersistentQueue;
import com.xuanzhi.tools.transport.RequestMessage;

public class Transfer_PlatformService implements Runnable {

	 AdvancedFilePersistentQueue queue=null;
	 static Logger logger = Logger.getLogger(Transfer_PlatformService.class);
	 Transaction_FaceManagerImpl transaction_FaceManager;
	 static Transfer_PlatformService self;
	 SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
	 public static Transfer_PlatformService getInstance() {
		return self;
	  }

	 public void init() throws Exception {
		self = this;
	  }

	 private void handle(List<RequestMessage> ls) {
		 ArrayList<Transfer_Platform> transfer_PlatformList   = new ArrayList();//购买成功和提前撤单以外的消息入数据库的处理
		 ArrayList<Transfer_Platform> transfer_PlatformList_2 = new ArrayList();//购买成功时的消息入数据库的处理
		 ArrayList<Transfer_Platform> transfer_PlatformList_3 = new ArrayList();//提前撤单时的消息入数据库的处理
		 
		 
		 for(int i=0;i<ls.size();i++)
		 {
			 RequestMessage message=ls.get(i);
				long now = System.currentTimeMillis();
				TRANSFER_PLATFORM_LOG_REQ req = (TRANSFER_PLATFORM_LOG_REQ) message;
				Transfer_PlatformFlow flow = req.getTransfer_PlatformFlow();
				if(logger.isDebugEnabled()){logger.debug("[平台交易信息上报] " + flow.toString() + " [队列:" + queue.elementNum() + "] [" + (System.currentTimeMillis() - now) + "ms]"); }
				
				String articleColor	=flow.getArticleColor();
				String articleId	=flow.getArticleId();
				String aticleName	=flow.getArticleName();
				String buyPassPortChannel	=flow.getBuyPassportChannel();
				String buyPassPortId	=flow.getBuyPassportId();
				String buyPassPortName	=flow.getBuyPassportName();
				String buyPlayerId	=flow.getBuyPlayerId();
				String buyPlayerLevel	=flow.getBuyPlayerLevel();
				String buyPlayerName	=flow.getBuyPlayerName();
				String buyPlayerVipLevel	=flow.getBuyPlayerVipLevel();
				
				
				String callIndex	=flow.getCellIndex();
				String column1	=flow.getColumn1();
				String column2	=flow.getColumn2();
				String column3	=flow.getColumn3();
				String column4	=flow.getColumn4();
				
				long createTime	=flow.getCreateTime();
				String goodsCount	=flow.getGoodsCount();
				String id	=flow.getId();
				long payMoney	=flow.getPayMoney();
				String responseResult	=flow.getResponseResult();
				
				String sellPassPortChannel	=flow.getSellPassportChannel();
				String sellPassPortId	=flow.getSellPassportId();
				String sellPassPortName	=flow.getSellPassportName();
				String sellPlayerId	=flow.getSellPlayerId();
				String sellPlayerLevel	=flow.getSellPlayerLevel();
				
				String sellPlayerName	=flow.getSellPlayerName();
				String sellVipLevel	=flow.getSellVipLevel();
				String serverName	=flow.getServerName();
				long tradeMoney	=flow.getTradeMoney();
				String tradeType	=flow.getTradeType();
				
				String articleSalePaySilver=flow.getArticleSalePaySilver();
				String   cancelSaleSilver=flow.getCancelSaleSilver(); 
				
			Transfer_Platform tform=new Transfer_Platform();
				
				 tform.setArticleColor(articleColor);
	  			 tform.setArticleId(articleId);
	  			 tform.setArticleName(aticleName);
	  			 tform.setBuyPassportChannel(buyPassPortChannel);
	  			 tform.setBuyPassportId(buyPassPortId);
	  			 tform.setBuyPassportName(buyPassPortName);
	  			 tform.setBuyPlayerId(buyPlayerId);
	  			 tform.setBuyPlayerLevel(buyPlayerLevel);
	  			 tform.setBuyPlayerName(buyPlayerName);
	  			 tform.setBuyPlayerVipLevel(buyPlayerVipLevel);
	  			 tform.setCellIndex(callIndex);
	  			 tform.setColumn1(column1);
	  			 tform.setColumn2(column2);
	  			 tform.setColumn3(column3);
	  			 tform.setColumn4(column4);
	  			
	  			 tform.setCreateTime(createTime);
	  			 tform.setGoodsCount(goodsCount);
	  			 tform.setId(id);
	  			 tform.setPayMoney(payMoney);
	  			 tform.setResponseResult(responseResult);
	  			 tform.setSellPassportChannel(sellPassPortChannel);
	  			 tform.setSellPassportId(sellPassPortId);
	  			 tform.setSellPassportName(sellPassPortName);
	  			 tform.setSellPlayerId(sellPlayerId);
	  			 tform.setSellPlayerLevel(sellPlayerLevel);
	  			 tform.setSellPlayerName(sellPlayerName);
	  			 tform.setSellVipLevel(sellVipLevel);
	  			 tform.setServerName(serverName);
	  			 tform.setTradeMoney(tradeMoney);
	  			 tform.setTradeType(tradeType);
	  			 tform.setArticleSalePaySilver(articleSalePaySilver);
	  			 tform.setCancelSaleSilver(cancelSaleSilver);
	  			if("1".endsWith(responseResult)){ 
				    transfer_PlatformList_2.add(tform);
	  			}else if("2".equals(responseResult)){
	  				transfer_PlatformList_3.add(tform);
	  			}else{
	  				transfer_PlatformList.add(tform);
	  			}
		 }	
		 if(transfer_PlatformList.size()>0)  { transaction_FaceManager.addTransferFormList(transfer_PlatformList);		 }
		 if(transfer_PlatformList_2.size()>0){ transaction_FaceManager.addTransferFormList_2(transfer_PlatformList_2);}
		 if(transfer_PlatformList_3.size()>0){ transaction_FaceManager.addTransferFormList_3(transfer_PlatformList_3);}
		 
		 
	  }


	@Override
	public void run() {
	    if(queue==null){queue=StatServerService.transfer_PlatFormqueue;}
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
					Thread.sleep(100);
				}
				
				if(queue.isEmpty()){
					 synchronized(this){
						 wait(1000);
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
