package com.fy.boss.finance.service;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.authorize.service.PassportManager;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.game.model.Server;
import com.fy.boss.game.service.ServerManager;


public class NotifySavingManager implements Runnable {
	
	public static Logger logger = Logger.getLogger(NotifySavingManager.class);
	
	private static NotifySavingManager self;
	
	private List<OrderForm> notifyOrders;
	
	public boolean running = true;
	
	public static NotifySavingManager getInstance() {
		return self;
	}
	
	public void init() {
		long start = System.currentTimeMillis();
		notifyOrders = new LinkedList<OrderForm>();
		self = this;
		Thread t = new Thread(this, "NotifySavingManager");
		t.start();
		for(int i=0; i<10; i++) {
			Thread tt = new Thread(new SavingNotifier(), "SavingNotifier-" + i);
			tt.start();
		}
//		System.out.println("[充值成功通知服务初始化成功] ["+NotifySavingManager.class.getName()+"] ["+(System.currentTimeMillis()-start)+"ms]");
	}
	
	public synchronized OrderForm popOrderForm() {
		if(notifyOrders.size() > 0) {
			return notifyOrders.remove(0);
		}
		return null;
	}
	
	public void run() {
		while(running) {
			try {
				Thread.sleep(1000);
				OrderFormManager om = OrderFormManager.getInstance();
				List<OrderForm> list = om.getSuccAndUnnotifiedOrderForms();
				//获得成功返回但是没有通知服务器的订单，进行通知
				if(list != null && list.size() > 0) {
					synchronized(this) {
						for(OrderForm order : list) {
			    			//先设置状态，然后再通知
			    			order.setNotified(true);
			    			om.updateOrderForm(order,"notified");
			    			if(!notifyOrders.contains(order)) {
			    				notifyOrders.add(order);
			    			}
						}
					}
				} else {
//					logger.info("[充值通知] [没有新的充值] ["+(System.currentTimeMillis()-l)+"ms]");
				}
			} catch(Throwable e) {
				logger.error("[通知主心跳发生异常]", e);
			}
		}
	}
	
	private static HashSet<Long> notifyingSet = new HashSet<Long>();
	
	public class SavingNotifier implements Runnable {
		
		
		public void run() {
			while(running) {
				try {
					Thread.sleep(500);
					OrderForm order = popOrderForm();
					if(order == null) {
						continue;
					}

					synchronized(notifyingSet) {
						if(notifyingSet.contains(order.getId())) {
							continue;
						}
						notifyingSet.add(order.getId());
					}
			
					OrderFormManager om = OrderFormManager.getInstance();
					PassportManager pm = PassportManager.getInstance();
					ServerManager sm = ServerManager.getInstance();
					long now = System.currentTimeMillis();
		    		Passport passport = pm.getPassport(order.getPassportId());
		    		String desp = order.getMemo1();
					Server server = sm.getServer(order.getServerName());
		    		try {
		    			/**
						 * 做容错判断，事实上会出现偶然多通知一次服务器的情况
						 */
						if(order.isNotifySucc() || (order.getMemo3() != null && order.getMemo3().length() > 0) )
						{
							continue;
						}
		    			boolean succ = SavingNotifyClientService.notifySaving(server.getSavingNotifyUrl(), passport.getUserName(), order.getPayMoney(), order.getUserChannel(), order.getSavingMedium(), server.getName(), order.getSavingPlatform(), order);
		    			if(succ) {
		    				order.setNotifySucc(true);
		    				om.updateOrderForm(order,"notifySucc");
		    			} else {
		    				order.setNotifySucc(false);
		    				order.setMemo3("同步失败");
		    				om.update(order);
		    			}
		    			
		    			logger.warn("[充值通知] [SUCC:"+succ+"] [id:"+order.getId()+"] [orderno:"+order.getOrderId()+"] [server:"+server.getName()+"] [user:"+passport.getUserName()+"] [money:"+order.getPayMoney()+"] [platform:"+order.getSavingPlatform()+"] [medium:"+order.getSavingMedium()+"] [otherInfo:"+desp+"] ["+(System.currentTimeMillis()-now)+"ms]");
		    		} catch(Exception e) {
		    			logger.warn("充值通知失败：更新订单发生异常] [id:"+order.getId()+"] [orderno:"+order.getOrderId()+"] [server:"+order.getServerName()+"] [user:"+passport.getUserName()+"] [money:"+order.getPayMoney()+"] [platform:"+order.getSavingPlatform()+"] [medium:"+order.getSavingMedium()+"] [otherInfo:"+desp+"] ["+(System.currentTimeMillis()-now)+"ms]", e);
					}
		    		synchronized(notifyingSet) {
		    			notifyingSet.remove(order.getId());
		    		}
		    		
				} catch(Throwable e) {
					logger.error("[通知线程的心跳发生异常]", e);
				}
			}
		}
	}
}
