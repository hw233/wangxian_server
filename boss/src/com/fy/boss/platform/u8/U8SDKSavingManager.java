package com.fy.boss.platform.u8;

import java.util.Date;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.xuanzhi.tools.text.DateUtil;

public class U8SDKSavingManager {
	public static U8SDKSavingManager self;

	public void initialize(){
		self=this;
	}
	
	public String sdkSaving(Passport passport, String server,long addAmount, String userChannel,String[]others) {
		long startTime = System.currentTimeMillis();
		
		OrderFormManager orderFormManager = OrderFormManager.getInstance();
		//生成订单
		OrderForm order = new OrderForm();
		order.setCreateTime(new Date().getTime());
		//设置充值平台
		order.setSavingPlatform("U8充值");
		//设置充值介质
		order.setSavingMedium("U8充值");
		order.setMediumInfo("");
		//设置充值人
		order.setPassportId(passport.getId());
		//设置商品数量
		order.setGoodsCount(0);
		//设置消费金额
		order.setPayMoney(addAmount);
		//设置serverName
		order.setServerName(server);
		order.setHandleResultDesp("U8充值通知");
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
		
		order.setOrderId("u8" + DateUtil.formatDate(new Date(), "yyyyMMdd") +"-"+ order.getId());
		
		try {
			orderFormManager.update(order);
			PlatformSavingCenter.logger.info("[生成u8订单] [创建订单] [成功] [账号:"+passport.getUserName()+"] [orderId:"+order.getOrderId()+"] [username:"+passport.getUserName()+"] [channel:"+userChannel+"] [server:"+server+"] [渠道订单号："+order.getChannelOrderId()+"] [角色id:"+others[0]+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			PlatformSavingCenter.logger.warn("[生成u8订单失败] [更新订单失败] [账号:"+passport.getUserName()+"] [orderId:"+order.getOrderId()+"] [username:"+passport.getUserName()+"] [channel:"+userChannel+"] [server:"+server+"] [角色id:"+others[0]+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
		}
		return order.getOrderId();
	}
	
	
	public static U8SDKSavingManager getInstance(){
		if(U8SDKSavingManager.self == null)
		{
			U8SDKSavingManager.self = new U8SDKSavingManager();
		}
		return U8SDKSavingManager.self;
	}
}
