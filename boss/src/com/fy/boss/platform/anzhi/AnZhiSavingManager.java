package com.fy.boss.platform.anzhi;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.fy.boss.finance.service.platform.Base64;
import com.fy.boss.platform.anzhi.AnZhiSavingManager;
import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.text.StringUtil;

public class AnZhiSavingManager {
	static AnZhiSavingManager self;
	
//原来的	public static final String RECHARGE_URL = "http://game.lewan.cn/merchant/payment/receiveconversion";
	public static final String RECHARGE_URL = "http://www.baoruan.com/nested/account/login/";

	
	
	public static Map<String, Integer> cardTypeMap = new HashMap<String,Integer>();
	
	
	public void initialize(){
		AnZhiSavingManager.self=this;
		System.out.println("["+this.getClass().getName()+"] [initialized]");
		PlatformSavingCenter.logger.info("["+this.getClass().getName()+"] [initialized]");	
	}
	
	
	
	public String cardSaving(Passport passport, String cardtype, int payAmount, String cardno, String cardpass,
			 String server,String mobileOs,String userChannel){
		long startTime = System.currentTimeMillis();
		String savingMedium = "安智充值";
		OrderFormManager orderFormManager = OrderFormManager.getInstance();
		//生成订单
		OrderForm order = new OrderForm();
		order.setCreateTime(new Date().getTime());
		//设置充值平台
		order.setSavingPlatform("安智");
		//设置充值介质
		order.setSavingMedium(savingMedium);
		//设置充值人
		order.setPassportId(passport.getId());
		//设置商品数量
		order.setGoodsCount(1);
		//设置消费金额
		order.setPayMoney(payAmount);
		//设置serverName
		order.setServerName(server);
		order.setHandleResult(OrderForm.HANDLE_RESULT_NOBACK);
		order.setHandleResultDesp("安智新生成订单");
		//设置订单responseResult
		order.setResponseResult(OrderForm.RESPONSE_NOBACK);
		order.setResponseDesp("安智新生成订单");
		//设置是否通知游戏服状态 设为false
		order.setNotified(false);
		//设置通知游戏服是否成功 设为false
		order.setNotifySucc(false);
		//在备注中存入充值平台
		order.setMemo1(mobileOs);
		order.setUserChannel(userChannel);
		//先存入到数据库中
		order = orderFormManager.createOrderForm(order);
		//设置订单号
		order.setOrderId("az-"+DateUtil.formatDate(new Date(), "yyyyMMdd") + "-"+ order.getId());

		
		try
		{
			orderFormManager.updateOrderForm(order, "orderId");
			PlatformSavingCenter.logger.info("[安智充值订单生成] [成功] [更新订单信息] [成功] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [订单的ID:"+order.getId()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
		}
		catch(Exception e)
		{
			PlatformSavingCenter.logger.error("[安智充值订单生成] [失败] [更新订单信息] [失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [订单的ID:"+order.getId()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
			return "";
		}

		
		return order.getOrderId();
	}
	
	public String cardSaving(Passport passport, String cardtype, int payAmount, String cardno, String cardpass,
			 String server,String mobileOs,String userChannel,String[] others){
		
		long t = System.currentTimeMillis();
		
		String orderId =  cardSaving(passport, cardtype, payAmount, cardno, cardpass, server, mobileOs, userChannel);
		
		if(others == null || others.length <= 0)
		{
			
			OrderForm order = OrderFormManager.getInstance().getOrderForm(orderId);
			if(order != null)
			{
				if(others.length > 1){
					order.setChargeValue(others[1]);
				}
				if(others.length > 2){
					order.setChargeType(others[2]);
				}
				order.setMemo1("");
				try {
					OrderFormManager.getInstance().update(order);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					PlatformSavingCenter.logger.error("[安智充值订单生成] [失败] [更新订单] [失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [角色名:"+order.getMemo1()+"] [costs:"+(System.currentTimeMillis()-t)+"]",e);
				}
				
				PlatformSavingCenter.logger.warn("[安智充值订单生成] [成功] [无角色id传入] [id:"+order.getId()+"] [orderID："+orderId+"] [充值类型："+cardtype+"] [paymoney:"+0+"] [用户:"+passport.getUserName()+"] [角色:"+order.getMemo1()+"] ["+(System.currentTimeMillis()-t)+"ms]");
			}
			else
			{
				PlatformSavingCenter.logger.warn("[安智充值订单生成] [失败] [根据订单id没有查到订单] [无角色id传入] [orderid:"+orderId+"]");
			}
			return orderId;
		}
		else if(!StringUtils.isEmpty(orderId))
		{
			OrderForm order = OrderFormManager.getInstance().getOrderForm(orderId);
			if(order != null)
			{
				order.setMemo1(others[0]);
				try {
					OrderFormManager.getInstance().update(order);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					PlatformSavingCenter.logger.error("[安智充值订单生成] [失败：更新订单出现异常] [id:"+order.getId()+"] [orderID："+orderId+"] [充值类型："+cardtype+"] [paymoney:"+0+"] [用户:"+passport.getUserName()+"] [角色:"+order.getMemo1()+"] [角色id:"+others[0]+"] ["+(System.currentTimeMillis()-t)+"ms]", e);
				}
				
				PlatformSavingCenter.logger.warn("[安智充值订单生成] [成功] [无角色id传入] [id:"+order.getId()+"] [orderID："+orderId+"] [充值类型："+cardtype+"] [paymoney:"+0+"] [用户:"+passport.getUserName()+"] [角色:"+order.getMemo1()+"] [角色id:"+others[0]+"] ["+(System.currentTimeMillis()-t)+"ms]");
			}
			else
			{
				PlatformSavingCenter.logger.warn("[安智充值订单生成] [失败] [根据订单id没有查到订单] [无角色id传入] [orderid:"+orderId+"] [角色id:"+others[0]+"]");
			}
			return orderId;
		}
		else
		{
			return "";
		}
		
	}
	
	

	
	
	public static AnZhiSavingManager getInstance(){
		if(self == null)
		{
			self = new AnZhiSavingManager();
		}
		return AnZhiSavingManager.self;
	}
}
