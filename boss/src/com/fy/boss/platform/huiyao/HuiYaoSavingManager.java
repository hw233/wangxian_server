package com.fy.boss.platform.huiyao;

import java.util.Date;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.xuanzhi.tools.text.DateUtil;

public class HuiYaoSavingManager {

	private static HuiYaoSavingManager self;
	
	public static HuiYaoSavingManager getInstance() {
	  	if(self == null){
    		self = new HuiYaoSavingManager();
    	}
		return self;
	}
	
	public void init(){
		self = this;
		System.out.println("["+this.getClass().getName()+"] [initialized]");
		PlatformSavingCenter.logger.info("["+this.getClass().getName()+"] [initialized]");
	}
	
	public String cardSaving(Passport passport, String channel, String servername, int addAmount, String os,String[]others) {
		long startTime = System.currentTimeMillis();
		String savingMediumName = "其乐充值";
		try {
			OrderFormManager orderFormManager = OrderFormManager.getInstance();
			//生成订单
			OrderForm order = new OrderForm();
			order.setCreateTime(new Date().getTime());
			order.setSavingPlatform("辉耀SDK");
			order.setSavingMedium(savingMediumName);
			order.setPassportId(passport.getId());
			order.setPayMoney( new Long(addAmount));
			order.setServerName(servername);
			order.setHandleResult(OrderForm.HANDLE_RESULT_NOBACK);
			order.setHandleResultDesp("新生成订单");
			order.setResponseResult(OrderForm.RESPONSE_NOBACK);
			order.setResponseDesp("新生成订单");
			order.setNotified(false);
			order.setNotifySucc(false);
			order.setUserChannel(channel);
			order.setMemo1(others[0]);
			if(others.length > 1){
				order.setChargeValue(others[1]);
			}
			if(others.length > 2){
				order.setChargeType(others[2]);
			}
			order = orderFormManager.createOrderForm(order);
			String orderid =  "huiyao" + DateUtil.formatDate(new Date(), "yyyyMMdd") +"-"+ order.getId();
			order.setOrderId(orderid);
			orderFormManager.updateOrderForm(order, "orderId");
		
			if(PlatformSavingCenter.logger.isInfoEnabled()){
				PlatformSavingCenter.logger.info("[辉耀充值订单生成] [成功] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值金额:"+addAmount+"] [服务器:"+servername+"] [手机平台:"+os+"] [订单 的ID主键:"+order.getId()+" ] [订单Id:"+order.getOrderId()+"] [channel:"+channel+"] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			return orderid;
		}catch(Exception e) {
			PlatformSavingCenter.logger.error("[辉耀充值订单生成] [失败] [生成订单出现异常] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值金额:"+addAmount+"] [服务器:"+servername+"] [手机平台:"+os+"] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			return null;
		} 
	}
	
}
