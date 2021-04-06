package com.fy.boss.platform.sina;

import java.net.URLEncoder;
import java.util.Date;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.fy.boss.platform.sina.SinaWYXNewSavingManager;

public class SinaWYXNewSavingManager {
	static SinaWYXNewSavingManager self;
	
	
	public void initialize(){
		SinaWYXNewSavingManager.self=this;
		System.out.println("["+this.getClass().getName()+"] [initialized]");
		PlatformSavingCenter.logger.info("["+this.getClass().getName()+"] [initialized]");	
	}
	
	
	
	public String cardSaving(Passport passport, String cardtype, int payAmount, String cardno, String cardpass,
			 String server,String mobileOs,String userChannel){
		long startTime = System.currentTimeMillis();
		
		boolean isTest = false;
		
		String savingMedium = "微游戏充值";
		
		OrderFormManager orderFormManager = OrderFormManager.getInstance();
		//生成订单
		OrderForm order = new OrderForm();
		order.setCreateTime(new Date().getTime());
		//设置充值平台
		order.setSavingPlatform("新浪微游戏");
		//设置充值介质
		order.setSavingMedium(savingMedium);
		order.setMediumInfo(cardno);
		//设置充值人
		order.setPassportId(passport.getId());
		//设置商品数量
		order.setGoodsCount(1);
		//设置消费金额
		order.setPayMoney(payAmount);
		//设置serverName
		order.setServerName(server);
		order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
		order.setHandleResultDesp("新浪微游戏新生成订单");
		//设置订单responseResult
		order.setResponseResult(OrderForm.RESPONSE_NOBACK);
		order.setResponseDesp("新浪微游戏新生成订单");
		//设置是否通知游戏服状态 设为false
		order.setNotified(false);
		//设置通知游戏服是否成功 设为false
		order.setNotifySucc(false);
		//在备注中存入充值平台
		order.setMemo1(mobileOs);
		
		order.setUserChannel(userChannel);
		//先存入到数据库中
		order = orderFormManager.createOrderForm(order);
		if(PlatformSavingCenter.logger.isInfoEnabled())
			PlatformSavingCenter.logger.info("[新浪微游戏充值订单生成] [成功] [创建新订单] [成功] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [订单的ID:"+order.getId()+"] [channel:"+userChannel+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
		//设置订单号
		//order.setOrderId("sinawei-"+DateUtil.formatDate(new Date(), "yyyyMMdd") + "-"+ order.getId());
		order.setOrderId(cardno);
		try
		{
			String returnString = "";
			
			if(isTest)
			{
				returnString = "http://124.248.40.21:9110/mieshi_game_boss/sinaweiNewResult";
			}
			else
			{
				returnString = "http://116.213.192.216:9110/mieshi_game_boss/sinaweiNewResult";
			}
			orderFormManager.update(order);
			returnString += "?orderNo="+order.getOrderId();
			returnString = URLEncoder.encode(returnString,"utf-8");
			if(PlatformSavingCenter.logger.isInfoEnabled())
				PlatformSavingCenter.logger.error("[新浪微游戏充值订单生成] [成功] [更新订单：添加订单ID] [成功] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [订单的ID:"+order.getId()+"] [订单ID:"+order.getOrderId()+"] ["+returnString+"] [channel:"+userChannel+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			return returnString;
		}
		catch(Exception e)
		{
			PlatformSavingCenter.logger.error("[新浪微游戏充值订单生成] [失败] [更新订单信息] [失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [订单的ID:"+order.getId()+"] [channel:"+userChannel+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
			return "";
		}
	}
	
	
	
	public String cardSaving(Passport passport, String cardtype, int payAmount, String cardno, String cardpass,
			 String server,String mobileOs,String userChannel,String[]others){
		long startTime = System.currentTimeMillis();
		
		boolean isTest = false;
		
		String savingMedium = "微游戏充值";
		
		OrderFormManager orderFormManager = OrderFormManager.getInstance();
		//生成订单
		OrderForm order = new OrderForm();
		order.setCreateTime(new Date().getTime());
		//设置充值平台
		order.setSavingPlatform("新浪微游戏");
		//设置充值介质
		order.setSavingMedium(savingMedium);
		order.setMediumInfo(cardno);
		//设置充值人
		order.setPassportId(passport.getId());
		//设置商品数量
		order.setGoodsCount(1);
		//设置消费金额
		order.setPayMoney(payAmount);
		//设置serverName
		order.setServerName(server);
		order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
		order.setHandleResultDesp("新浪微游戏新生成订单");
		//设置订单responseResult
		order.setResponseResult(OrderForm.RESPONSE_NOBACK);
		order.setResponseDesp("新浪微游戏新生成订单");
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
		if(PlatformSavingCenter.logger.isInfoEnabled())
			PlatformSavingCenter.logger.info("[新浪微游戏充值订单生成] [成功] [创建新订单] [成功] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [订单的ID:"+order.getId()+"] [channel:"+userChannel+"] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
		//设置订单号
		//order.setOrderId("sinawei-"+DateUtil.formatDate(new Date(), "yyyyMMdd") + "-"+ order.getId());
		order.setOrderId(cardno);
		try
		{
			String returnString = "";
			
			if(isTest)
			{
				returnString = "http://124.248.40.21:9110/mieshi_game_boss/sinaweiNewResult";
			}
			else
			{
				returnString = "http://116.213.192.216:9110/mieshi_game_boss/sinaweiNewResult";
			}
			orderFormManager.update(order);
			returnString += "?orderNo="+order.getOrderId();
			returnString = URLEncoder.encode(returnString,"utf-8");
			if(PlatformSavingCenter.logger.isInfoEnabled())
				PlatformSavingCenter.logger.error("[新浪微游戏充值订单生成] [成功] [更新订单：添加订单ID] [成功] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [订单的ID:"+order.getId()+"] [订单ID:"+order.getOrderId()+"] ["+returnString+"] [channel:"+userChannel+"] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			return returnString;
		}
		catch(Exception e)
		{
			PlatformSavingCenter.logger.error("[新浪微游戏充值订单生成] [失败] [更新订单信息] [失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [订单的ID:"+order.getId()+"] [channel:"+userChannel+"] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
			return "";
		}
	}
	
	
	
	public static SinaWYXNewSavingManager getInstance(){
		if(self == null)
		{
			self = new SinaWYXNewSavingManager();
		}
		return SinaWYXNewSavingManager.self;
	}
}
