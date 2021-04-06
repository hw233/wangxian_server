package com.fy.boss.platform.huiyao;

import java.util.Date;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.xuanzhi.tools.text.DateUtil;
//坚果
public class HuoGameSavingManager {

	private static HuoGameSavingManager self;
	
	public static HuoGameSavingManager getInstance() {
	  	if(self == null){
    		self = new HuoGameSavingManager();
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
		String savingMediumName = "坚果充值";
		try {
			OrderFormManager orderFormManager = OrderFormManager.getInstance();
			//生成订单
			OrderForm order = new OrderForm();
			order.setCreateTime(new Date().getTime());
			order.setSavingPlatform("火游戏SDK");
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
			String orderid =  "huogame" + DateUtil.formatDate(new Date(), "yyyyMMdd") +"-"+ order.getId();
			order.setOrderId(orderid);
			orderFormManager.updateOrderForm(order, "orderId");
		
			if(PlatformSavingCenter.logger.isInfoEnabled()){
				PlatformSavingCenter.logger.info("[火游戏充值订单生成] [成功] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值金额:"+addAmount+"] [服务器:"+servername+"] [手机平台:"+os+"] [订单 的ID主键:"+order.getId()+" ] [订单Id:"+order.getOrderId()+"] [channel:"+channel+"] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			return orderid;
		}catch(Exception e) {
			PlatformSavingCenter.logger.error("[火游戏充值订单生成] [失败] [生成订单出现异常] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值金额:"+addAmount+"] [服务器:"+servername+"] [手机平台:"+os+"] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			return null;
		} 
	}
	
	public String haoDongCardSaving(Passport passport, String channel, String servername, int addAmount, String os,String[]others) {
		long startTime = System.currentTimeMillis();
		String savingMediumName = "浩动充值";
		try {
			OrderFormManager orderFormManager = OrderFormManager.getInstance();
			//生成订单
			OrderForm order = new OrderForm();
			order.setCreateTime(new Date().getTime());
			order.setSavingPlatform("浩动SDK");
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
			String orderid =  "haodong" + DateUtil.formatDate(new Date(), "yyyyMMdd") +"-"+ order.getId();
			order.setOrderId(orderid);
			orderFormManager.updateOrderForm(order, "orderId");
		
			if(PlatformSavingCenter.logger.isInfoEnabled()){
				PlatformSavingCenter.logger.info("[浩动充值订单生成] [成功] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值金额:"+addAmount+"] [服务器:"+servername+"] [手机平台:"+os+"] [订单 的ID主键:"+order.getId()+" ] [订单Id:"+order.getOrderId()+"] [channel:"+channel+"] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			return orderid;
		}catch(Exception e) {
			PlatformSavingCenter.logger.error("[浩动值订单生成] [失败] [生成订单出现异常] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值金额:"+addAmount+"] [服务器:"+servername+"] [手机平台:"+os+"] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			return null;
		} 
	}
	
	public String jiuzhouCardSaving(Passport passport, String channel, String servername, int addAmount, String os,String[]others) {
		long startTime = System.currentTimeMillis();
		String savingMediumName = "九州飘渺充值";
		try {
			OrderFormManager orderFormManager = OrderFormManager.getInstance();
			//生成订单
			OrderForm order = new OrderForm();
			order.setCreateTime(new Date().getTime());
			order.setSavingPlatform("九州SDK");
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
			String orderid =  "jiuzhou" + DateUtil.formatDate(new Date(), "yyyyMMdd") +"-"+ order.getId();
			order.setOrderId(orderid);
			orderFormManager.updateOrderForm(order, "orderId");
		
			if(PlatformSavingCenter.logger.isInfoEnabled()){
				PlatformSavingCenter.logger.info("[九州飘渺充值订单生成] [成功] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值金额:"+addAmount+"] [服务器:"+servername+"] [手机平台:"+os+"] [订单 的ID主键:"+order.getId()+" ] [订单Id:"+order.getOrderId()+"] [channel:"+channel+"] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			return orderid;
		}catch(Exception e) {
			PlatformSavingCenter.logger.error("[九州飘渺充值订单生成] [失败] [生成订单出现异常] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值金额:"+addAmount+"] [服务器:"+servername+"] [手机平台:"+os+"] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			return null;
		} 
	}
	
	public String guopanCardSaving(Passport passport, String channel, String servername, int addAmount, String os,String[]others) {
		long startTime = System.currentTimeMillis();
		String savingMediumName = "果盘充值";
		try {
			OrderFormManager orderFormManager = OrderFormManager.getInstance();
			//生成订单
			OrderForm order = new OrderForm();
			order.setCreateTime(new Date().getTime());
			order.setSavingPlatform("果盘SDK");
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
			String orderid =  "guopan" + DateUtil.formatDate(new Date(), "yyyyMMdd") +"-"+ order.getId();
			order.setOrderId(orderid);
			orderFormManager.updateOrderForm(order, "orderId");
		
			if(PlatformSavingCenter.logger.isInfoEnabled()){
				PlatformSavingCenter.logger.info("[果盘充值订单生成] [成功] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值金额:"+addAmount+"] [服务器:"+servername+"] [手机平台:"+os+"] [订单 的ID主键:"+order.getId()+" ] [订单Id:"+order.getOrderId()+"] [channel:"+channel+"] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			return orderid;
		}catch(Exception e) {
			PlatformSavingCenter.logger.error("[果盘充值订单生成] [失败] [生成订单出现异常] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值金额:"+addAmount+"] [服务器:"+servername+"] [手机平台:"+os+"] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			return null;
		} 
	}
	
}
