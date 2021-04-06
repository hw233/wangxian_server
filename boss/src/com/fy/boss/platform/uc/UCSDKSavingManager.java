package com.fy.boss.platform.uc;

import java.util.Arrays;
import java.util.Date;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.finance.dao.impl.ExceptionOrderFormDAOImpl;
import com.fy.boss.finance.model.ExceptionOrderForm;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.fy.boss.platform.uc.UCSDKSavingManager;
import com.xuanzhi.tools.text.DateUtil;

public class UCSDKSavingManager {
	public static UCSDKSavingManager self;

	public void initialize(){
		UCSDKSavingManager.self=this;
		System.out.println("["+this.getClass().getName()+"] [initialized]");
		PlatformSavingCenter.logger.info("["+this.getClass().getName()+"] [initialized]");	
	}
	
	public String sdkSaving(Passport passport, String ucOrderId, String server,String mobileOs, String userChannel) {
		long startTime = System.currentTimeMillis();
		
		OrderFormManager orderFormManager = OrderFormManager.getInstance();
		//生成订单
		OrderForm order = new OrderForm();
		order.setCreateTime(new Date().getTime());
		//设置充值平台
		order.setSavingPlatform("UCSDK支付");
		//设置充值介质
		order.setSavingMedium("UCSDK充值");
		order.setMediumInfo("");
		//设置充值人
		order.setPassportId(passport.getId());
		//设置商品数量
		order.setGoodsCount(0);
		//设置消费金额
		order.setPayMoney(0);
		//设置serverName
		order.setServerName(server);
		order.setHandleResultDesp("uc充值通知");
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
		order.setChannelOrderId(ucOrderId);
			
		//先存入到数据库中
		order = orderFormManager.createOrderForm(order);
		
		order.setOrderId("uc" + DateUtil.formatDate(new Date(), "yyyyMMdd") +"-"+ order.getId());
		
		try {
			orderFormManager.update(order);
			if(PlatformSavingCenter.logger.isInfoEnabled())
				PlatformSavingCenter.logger.info("[生成UCSDK订单] [创建订单] [成功] [账号:"+passport.getUserName()+"] [orderId:"+order.getOrderId()+"] [username:"+passport.getUserName()+"] [channel:"+userChannel+"] [server:"+server+"] [os:"+mobileOs+"] [渠道订单号："+order.getChannelOrderId()+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			PlatformSavingCenter.logger.warn("[生成UCSDK订单失败] [更新订单失败] [账号:"+passport.getUserName()+"] [orderId:"+order.getOrderId()+"] [username:"+passport.getUserName()+"] [channel:"+userChannel+"] [server:"+server+"] [os:"+mobileOs+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			return "0@@failed";
		}
			
		//查找是否存在充值结果回调先到达的渠道订单
		ExceptionOrderForm exceptionOrderForm = findByChannelOrderId(ucOrderId,userChannel);
		if(exceptionOrderForm != null)
		{
			//同步订单状态
			try {
				order.setResponseTime(exceptionOrderForm.getResponseTime());
		
				order.setResponseResult(exceptionOrderForm.getResponseResult());
				order.setResponseDesp(exceptionOrderForm.getResponseDesp());
				//设置充值平台
				order.setSavingPlatform(exceptionOrderForm.getSavingPlatform());
				
				order.setMediumInfo(exceptionOrderForm.getSavingMedium() +"@"+exceptionOrderForm.getMediumInfo());
				//以返回的充值额度为准
				order.setPayMoney(exceptionOrderForm.getPayMoney());
				orderFormManager.update(order);
				if(PlatformSavingCenter.logger.isInfoEnabled())
					PlatformSavingCenter.logger.info("[生成UCSDK订单] [同步订单状态] [成功] [账号:"+passport.getUserName()+"] [orderId:"+order.getOrderId()+"] [username:"+passport.getUserName()+"] [channel:"+userChannel+"] [server:"+server+"] [os:"+mobileOs+"] [渠道订单号："+exceptionOrderForm.getChannelOrderId()+"] [渠道订单状态："+exceptionOrderForm.getResponseResult()+"] [渠道订单状态描述:"+exceptionOrderForm.getResponseDesp()+"] [订单金额:"+exceptionOrderForm.getPayMoney()+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
				try
				{
					ExceptionOrderFormDAOImpl.getInstance().getEm().remove(exceptionOrderForm);	
				}
				catch(Exception e)
				{
					PlatformSavingCenter.logger.error("[删除异常订单] [失败] [出现异常] [id:"+exceptionOrderForm.getId()+"] [orderid:"+exceptionOrderForm.getOrderId()+"] [handleresult:"+exceptionOrderForm.getHandleResult()+"] [responseresult:"+exceptionOrderForm.getResponseResult()+"] [notified:"+exceptionOrderForm.isNotified()+"] [nofisucc:"+exceptionOrderForm.isNotifySucc()+"] [channel:"+exceptionOrderForm.getUserChannel()+"] [server:"+exceptionOrderForm.getServerName()+"] [用户名:--] [充值媒介:"+exceptionOrderForm.getSavingMedium()+"] [金额:"+exceptionOrderForm.getPayMoney()+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]",e);
				}
				
			} catch (Exception e) {
				PlatformSavingCenter.logger.error("[生成UCSDK订单] [同步订单状态] [失败] [账号:"+passport.getUserName()+"] [orderId:"+order.getOrderId()+"] [username:"+passport.getUserName()+"] [channel:"+userChannel+"] [server:"+server+"] [os:"+mobileOs+"] [渠道订单号："+exceptionOrderForm.getChannelOrderId()+"] [渠道订单状态："+exceptionOrderForm.getResponseResult()+"] [渠道订单状态描述:"+exceptionOrderForm.getResponseDesp()+"] [订单金额:"+exceptionOrderForm.getPayMoney()+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
				return "";
			}
		}
		
		return order.getOrderId();
		
	}
	
	public String sdkSaving(Passport passport, String ucOrderId, String server,String mobileOs, String userChannel,String[]others) {
		long startTime = System.currentTimeMillis();
		
		OrderFormManager orderFormManager = OrderFormManager.getInstance();
		//生成订单
		OrderForm order = new OrderForm();
		order.setCreateTime(new Date().getTime());
		//设置充值平台
		order.setSavingPlatform("UCSDK支付");
		//设置充值介质
		order.setSavingMedium("UCSDK充值");
		order.setMediumInfo("");
		//设置充值人
		order.setPassportId(passport.getId());
		//设置商品数量
		order.setGoodsCount(0);
		//设置消费金额
		order.setPayMoney(0);
		//设置serverName
		order.setServerName(server);
		order.setHandleResultDesp("uc充值通知");
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
		order.setChannelOrderId(ucOrderId);
		if(others.length > 1){
			order.setChargeValue(others[1]);
		}
		if(others.length > 2){
			order.setChargeType(others[2]);
		}
		//先存入到数据库中
		order = orderFormManager.createOrderForm(order);
		
		order.setOrderId("uc" + DateUtil.formatDate(new Date(), "yyyyMMdd") +"-"+ order.getId());
		
		try {
			orderFormManager.update(order);
			if(PlatformSavingCenter.logger.isInfoEnabled())
				PlatformSavingCenter.logger.info("[生成UCSDK订单] [创建订单] [成功] [账号:"+passport.getUserName()+"] [orderId:"+order.getOrderId()+"] [username:"+passport.getUserName()+"] [channel:"+userChannel+"] [server:"+server+"] [os:"+mobileOs+"] [渠道订单号："+order.getChannelOrderId()+"] [角色id:"+others[0]+"] [others:"+(others==null?"nul":Arrays.toString(others))+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			PlatformSavingCenter.logger.warn("[生成UCSDK订单失败] [更新订单失败] [账号:"+passport.getUserName()+"] [orderId:"+order.getOrderId()+"] [username:"+passport.getUserName()+"] [channel:"+userChannel+"] [server:"+server+"] [os:"+mobileOs+"] [角色id:"+others[0]+"] [others:"+(others==null?"nul":Arrays.toString(others))+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			return "0@@failed";
		}
			
		//查找是否存在充值结果回调先到达的渠道订单
		ExceptionOrderForm exceptionOrderForm = findByChannelOrderId(ucOrderId,userChannel);
		if(exceptionOrderForm != null)
		{
			//同步订单状态
			try {
				order.setResponseTime(exceptionOrderForm.getResponseTime());
		
				order.setResponseResult(exceptionOrderForm.getResponseResult());
				order.setResponseDesp(exceptionOrderForm.getResponseDesp());
				//设置充值平台
				order.setSavingPlatform(exceptionOrderForm.getSavingPlatform());
				
				order.setMediumInfo(exceptionOrderForm.getSavingMedium() +"@"+exceptionOrderForm.getMediumInfo());
				//以返回的充值额度为准
				order.setPayMoney(exceptionOrderForm.getPayMoney());
				orderFormManager.update(order);
				if(PlatformSavingCenter.logger.isInfoEnabled())
					PlatformSavingCenter.logger.info("[生成UCSDK订单] [同步订单状态] [成功] [账号:"+passport.getUserName()+"] [orderId:"+order.getOrderId()+"] [username:"+passport.getUserName()+"] [channel:"+userChannel+"] [server:"+server+"] [os:"+mobileOs+"] [渠道订单号："+exceptionOrderForm.getChannelOrderId()+"] [渠道订单状态："+exceptionOrderForm.getResponseResult()+"] [渠道订单状态描述:"+exceptionOrderForm.getResponseDesp()+"] [订单金额:"+exceptionOrderForm.getPayMoney()+"] [角色id:"+others[0]+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
				try
				{
					ExceptionOrderFormDAOImpl.getInstance().getEm().remove(exceptionOrderForm);	
				}
				catch(Exception e)
				{
					PlatformSavingCenter.logger.error("[删除异常订单] [失败] [出现异常] [id:"+exceptionOrderForm.getId()+"] [orderid:"+exceptionOrderForm.getOrderId()+"] [handleresult:"+exceptionOrderForm.getHandleResult()+"] [responseresult:"+exceptionOrderForm.getResponseResult()+"] [notified:"+exceptionOrderForm.isNotified()+"] [nofisucc:"+exceptionOrderForm.isNotifySucc()+"] [channel:"+exceptionOrderForm.getUserChannel()+"] [server:"+exceptionOrderForm.getServerName()+"] [用户名:--] [充值媒介:"+exceptionOrderForm.getSavingMedium()+"] [金额:"+exceptionOrderForm.getPayMoney()+"] [角色id:"+others[0]+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]",e);
				}
				
			} catch (Exception e) {
				PlatformSavingCenter.logger.error("[生成UCSDK订单] [同步订单状态] [失败] [账号:"+passport.getUserName()+"] [orderId:"+order.getOrderId()+"] [username:"+passport.getUserName()+"] [channel:"+userChannel+"] [server:"+server+"] [os:"+mobileOs+"] [渠道订单号："+exceptionOrderForm.getChannelOrderId()+"] [渠道订单状态："+exceptionOrderForm.getResponseResult()+"] [渠道订单状态描述:"+exceptionOrderForm.getResponseDesp()+"] [订单金额:"+exceptionOrderForm.getPayMoney()+"] [角色id:"+others[0]+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
				return "";
			}
		}
		
		return order.getOrderId();
		
	}
	
	
	private ExceptionOrderForm findByChannelOrderId(String channelOrderId,String userChannel)
	{
		ExceptionOrderFormDAOImpl daoImpl = ExceptionOrderFormDAOImpl.getInstance();
		return daoImpl.getByChannelOrderid(channelOrderId,userChannel);
	}
	
	public static UCSDKSavingManager getInstance(){
		return UCSDKSavingManager.self;
	}
}
