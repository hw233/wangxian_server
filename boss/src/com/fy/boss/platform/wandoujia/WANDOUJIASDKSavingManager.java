package com.fy.boss.platform.wandoujia;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.node.ObjectNode;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.finance.dao.ExceptionOrderFormDAO;
import com.fy.boss.finance.dao.impl.ExceptionOrderFormDAOImpl;
import com.fy.boss.finance.model.ExceptionOrderForm;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.fy.boss.tools.JacksonManager;
import com.fy.boss.platform.wandoujia.WANDOUJIASDKSavingManager;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.text.StringUtil;

public class WANDOUJIASDKSavingManager {
	public static WANDOUJIASDKSavingManager self;

	public void initialize(){
		WANDOUJIASDKSavingManager.self=this;
		System.out.println("["+this.getClass().getName()+"] [initialized]");
		PlatformSavingCenter.logger.info("["+this.getClass().getName()+"] [initialized]");	
	}
	
	public String sdkSaving(Passport passport, String server,String mobileOs, String userChannel) {
		long startTime = System.currentTimeMillis();
		
		OrderFormManager orderFormManager = OrderFormManager.getInstance();
		//生成订单
		OrderForm order = new OrderForm();
		order.setCreateTime(new Date().getTime());
		//设置充值平台
		order.setSavingPlatform("豌豆荚支付");
		//设置充值介质
		order.setSavingMedium("豌豆荚支付");
		order.setMediumInfo("");
		//设置充值人
		order.setPassportId(passport.getId());
		//设置商品数量
		order.setGoodsCount(0);
		//设置消费金额
		order.setPayMoney(0);
		//设置serverName
		order.setServerName(server);
		order.setHandleResultDesp("豌豆荚充值通知");
		order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
		//设置订单responseResult
		order.setResponseResult(OrderForm.RESPONSE_NOBACK);
		order.setResponseDesp("");
		//设置是否通知游戏服状态 设为false
		order.setNotified(false);
		//设置通知游戏服是否成功 设为false
		order.setNotifySucc(false);
		//在备注中存入充值平台
		order.setMemo1(mobileOs);
		order.setUserChannel(userChannel);
			
		//先存入到数据库中
		order = orderFormManager.createOrderForm(order);
		
		order.setOrderId("wdsdk" + DateUtil.formatDate(new Date(), "yyyyMMdd") +"-"+ order.getId());
		
		try {
			orderFormManager.update(order);
			if(PlatformSavingCenter.logger.isInfoEnabled())
				PlatformSavingCenter.logger.info("[生成WANDOUJIASDK订单] [创建订单] [成功] [账号:"+passport.getUserName()+"] [orderId:"+order.getOrderId()+"] [username:"+passport.getUserName()+"] [channel:"+userChannel+"] [server:"+server+"] [os:"+mobileOs+"] [渠道订单号："+order.getChannelOrderId()+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			PlatformSavingCenter.logger.warn("[生成WANDOUJIASDK订单失败] [更新订单失败] [账号:"+passport.getUserName()+"] [orderId:"+order.getOrderId()+"] [username:"+passport.getUserName()+"] [channel:"+userChannel+"] [server:"+server+"] [os:"+mobileOs+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			return "0@@failed";
		}
		
		return order.getOrderId();
		
	}
	
	
	public String sdkSaving(Passport passport, String server,String mobileOs, String userChannel,String[]others) {
		long startTime = System.currentTimeMillis();
		
		OrderFormManager orderFormManager = OrderFormManager.getInstance();
		//生成订单
		OrderForm order = new OrderForm();
		order.setCreateTime(new Date().getTime());
		//设置充值平台
		order.setSavingPlatform("豌豆荚支付");
		//设置充值介质
		order.setSavingMedium("豌豆荚支付");
		order.setMediumInfo("");
		//设置充值人
		order.setPassportId(passport.getId());
		//设置商品数量
		order.setGoodsCount(0);
		//设置消费金额
		order.setPayMoney(0);
		//设置serverName
		order.setServerName(server);
		order.setHandleResultDesp("豌豆荚充值通知");
		order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
		//设置订单responseResult
		order.setResponseResult(OrderForm.RESPONSE_NOBACK);
		order.setResponseDesp("");
		//设置是否通知游戏服状态 设为false
		order.setNotified(false);
		//设置通知游戏服是否成功 设为false
		order.setNotifySucc(false);
		//在备注中存入充值平台
		order.setMemo1(others[0]);
		order.setUserChannel(userChannel);
		if(others.length > 1){
			order.setChargeValue(others[1]);
		}
		if(others.length > 2){
			order.setChargeType(others[2]);
		}
		//先存入到数据库中
		order = orderFormManager.createOrderForm(order);
		
		order.setOrderId("wdsdk" + DateUtil.formatDate(new Date(), "yyyyMMdd") +"-"+ order.getId());
		
		try {
			orderFormManager.update(order);
			if(PlatformSavingCenter.logger.isInfoEnabled())
				PlatformSavingCenter.logger.info("[生成WANDOUJIASDK订单] [创建订单] [成功] [账号:"+passport.getUserName()+"] [orderId:"+order.getOrderId()+"] [username:"+passport.getUserName()+"] [channel:"+userChannel+"] [server:"+server+"] [os:"+mobileOs+"] [渠道订单号："+order.getChannelOrderId()+"] [角色id:"+others[0]+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			PlatformSavingCenter.logger.warn("[生成WANDOUJIASDK订单失败] [更新订单失败] [账号:"+passport.getUserName()+"] [orderId:"+order.getOrderId()+"] [username:"+passport.getUserName()+"] [channel:"+userChannel+"] [server:"+server+"] [os:"+mobileOs+"] [角色id:"+others[0]+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			return "0@@failed";
		}
		
		return order.getOrderId();
		
	}
	
	
	public static WANDOUJIASDKSavingManager getInstance(){
		if(WANDOUJIASDKSavingManager.self == null)
		{
			WANDOUJIASDKSavingManager.self = new WANDOUJIASDKSavingManager();
		}
		return WANDOUJIASDKSavingManager.self;
	}
}
