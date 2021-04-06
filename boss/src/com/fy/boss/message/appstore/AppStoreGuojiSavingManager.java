package com.fy.boss.message.appstore;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.fy.boss.tools.JacksonManager;
import com.xuanzhi.tools.servlet.HttpsUtils;
import com.xuanzhi.tools.text.DateUtil;

	public class AppStoreGuojiSavingManager {
	static AppStoreGuojiSavingManager self;
		
	public static String RECHARGE_URL = "https://buy.itunes.apple.com/verifyReceipt";
	public static String SANDBOX_RECHARGE_URL = "https://sandbox.itunes.apple.com/verifyReceipt";
		
	public static Map<String, Long> map = new HashMap<String,Long>();
	public static boolean isGoSandBox = false;
	
	public void initialize(){
		AppStoreGuojiSavingManager.self=this;
		System.out.println("["+this.getClass().getName()+"] [initialized]");
		PlatformSavingCenter.logger.info("["+this.getClass().getName()+"] [initialized]");	
	}
	
	static {//单位人民币分
		map.put("com.sqage.wangxiangjb.1lvrmb", 600L);
		map.put("com.sqage.wangxiangjb.2lvrmb", 1200L);
		map.put("com.sqage.wangxiangjb.5lvrmb", 3000L);
		map.put("com.sqage.wangxiangjb.9lvrmb", 6000L);
		map.put("com.sqage.wangxiangjb.15lv", 9800L);
		map.put("com.sqage.wangxiangjb.32lvrmb", 21800L);
		map.put("com.sqage.wangxiangjb.49lvrmb", 31800L);
		map.put("com.sqage.wangxiangjb.53lvrmb", 41800L);
		map.put("com.sqage.wangxiangjb.58lvrmb", 58800L);
		
		map.put("com.sqage.wangxian.1lvrmb", 600L);
		map.put("com.sqage.wangxian.2lvrmb", 1200L);
		map.put("com.sqage.wangxian.5lv", 3000L);
		map.put("com.sqage.wangxian.9lv", 6000L);
		map.put("com.sqage.wangxian.15lv", 9800L);
		map.put("com.sqage.wangxian.32lv", 21800L);
		map.put("com.sqage.wangxian.49lv", 31800L);
		map.put("com.sqage.wangxian.53lv", 41800L);
		map.put("com.sqage.wangxian.58lv", 58800L);
		
		map.put("wangxian01", 600L);
		map.put("wangxian02", 1200L);
		map.put("wangxian05", 3000L);
		map.put("wangxian09", 6000L);
		map.put("wangxian15", 9800L);
		map.put("wangxian32", 21800L);
		map.put("wangxian49", 31800L);
		map.put("wangxian53", 41800L);
		map.put("wangxian58", 58800L);
	}
	
	public String appStoreSaving(Passport passport, String channel, String servername, String receipt, String deviceCode){
		long t=System.currentTimeMillis();
		
		String savingMedium = "AppStoreGuoji";
		OrderForm order = new OrderForm();
		java.util.Date cdate = new java.util.Date();
		order.setCreateTime(t);
		order.setPassportId(passport.getId());
		order.setServerName(servername);
		order.setSavingPlatform("AppStoreGuoji");
		order.setSavingMedium(savingMedium);
		//order.setMediumInfo("receipt:" + receipt);
		order.setPayMoney(0L);
		order.setHandleResult(OrderForm.HANDLE_RESULT_NOBACK);
		order.setResponseResult(OrderForm.RESPONSE_NOBACK);
		order.setUserChannel(channel);
		order.setMemo1("IOS");
		order.setDeviceCode(deviceCode);
		order = OrderFormManager.getInstance().createOrderForm(order);
		String orderID = DateUtil.formatDate(cdate,"yyyyMMdd") + "-" + order.getId();
		order.setOrderId(orderID);
		try {
			OrderFormManager.getInstance().update(order);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			PlatformSavingCenter.logger.error("更新订单出现异常", e1);
		}
    	try {
			Map headers = new HashMap();
			URL url = new URL(RECHARGE_URL);
			headers.put("Content-type", "application/json");
			receipt = "{\"receipt-data\":\"" + receipt + "\"}";
			byte[] b = HttpsUtils.webPost(url, receipt.getBytes(), headers, 60000, 60000);
			String result = new String(b,"UTF-8");
			Object obj = null;
			if(result != null && result.length() > 0) {
				obj = JacksonManager.getInstance().jsonDecodeObject(result);
			}
			Map dataMap = null;
			if(obj != null && obj instanceof Map) {
				dataMap = (Map) obj;
				boolean isValid = false;
				isValid = (dataMap.get("status") != null && (dataMap.get("status") + "").equals("0"));
			//如果正式环境不行 就再去沙盒环境验证
				if(isGoSandBox)
				{
					if(!isValid && dataMap.get("status") != null  && ((dataMap.get("status") + "").equals("21007") ||  (dataMap.get("status") + "").equals("21002")))
					{
						url = new URL(SANDBOX_RECHARGE_URL);
						headers = new HashMap();
						headers.put("Content-type", "application/json");
						b = HttpsUtils.webPost(url, receipt.getBytes(), headers, 60000, 60000);
						result = new String(b,"UTF-8");
						obj = null;
						if(result != null && result.length() > 0) {
							obj = JacksonManager.getInstance().jsonDecodeObject(result);
						}
						
						if(obj != null && obj instanceof Map) {
							dataMap = (Map) obj;
							isValid = (dataMap.get("status") != null && (dataMap.get("status") + "").equals("0"));
						}
						
						PlatformSavingCenter.logger.warn("[应用商店充值订单生成] [用户进入沙盒环境验证] [transactionID："+orderID+"] [充值类型：AppStore] [支付平台:AppStore] [用户:"+passport.getUserName()+"] [服务器:"+servername+"] [渠道:"+channel+"] [信息："+result+"] ["+(System.currentTimeMillis()-t)+"ms]");
					}
				}
				
				if(isValid) {
					Map receiptMap = (Map)dataMap.get("receipt");
					String productId = receiptMap.get("product_id") + "";
					if(receiptMap.get("transaction_id") != null) {
						orderID = receiptMap.get("transaction_id") + "";
						OrderForm exists = OrderFormManager.getInstance().getOrderForm(orderID);
					
						//检查订单是否被处理
						if(exists != null) {
							order.setHandleResult(OrderForm.HANDLE_RESULT_FAILED);
							OrderFormManager.getInstance().update(order);
							PlatformSavingCenter.logger.warn("[应用商店充值订单生成] [失败:此交易已经生成过订单] [transactionID："+orderID+"] [充值类型：AppStore] [支付平台:AppStore] [用户:"+passport.getUserName()+"] [服务器:"+servername+"] [渠道:"+channel+"] [信息："+result+"] ["+(System.currentTimeMillis()-t)+"ms]");
							return null;
						} else {
							Long addAmount = map.get(productId);
							if(addAmount != null && addAmount > 0) {
								order.setPayMoney(addAmount);
								order.setOrderId(orderID);
								order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
								order.setResponseResult(OrderForm.RESPONSE_SUCC);
								OrderFormManager.getInstance().update(order);
								PlatformSavingCenter.logger.warn("[应用商店充值订单生成] [成功] [id:"+order.getId()+"] [orderID："+orderID+"] [充值类型：AppStore] [支付平台:AppStore] [paymoney:"+addAmount+"] [用户:"+passport.getUserName()+"] [服务器:"+servername+"] [渠道:"+channel+"] [信息："+result+"] [url:"+RECHARGE_URL+"] [receipt:"+receipt+"] ["+(System.currentTimeMillis()-t)+"ms]");
							} else {
								order.setHandleResult(OrderForm.HANDLE_RESULT_FAILED);
								OrderFormManager.getInstance().update(order);
								PlatformSavingCenter.logger.warn("[应用商店充值订单生成] [失败：此交易返回的数据有误（充值额<0）] [id:"+order.getId()+"] [orderID："+orderID+"] [充值类型：AppStore] [支付平台:AppStore] [paymoney:"+addAmount+"] [用户:"+passport.getUserName()+"] [服务器:"+servername+"] [渠道:"+channel+"] [信息："+result+"] [receipt:"+receipt+"] ["+(System.currentTimeMillis()-t)+"ms]");
							} 
						}
					} else {
						order.setHandleResult(OrderForm.HANDLE_RESULT_FAILED);
						OrderFormManager.getInstance().update(order);
						PlatformSavingCenter.logger.warn("[应用商店充值订单生成] [失败：此交易返回的数据有误] [id:"+order.getId()+"] [orderID：NULL] [充值类型：AppStore] [支付平台:AppStore] [paymoney:"+0+"] [用户:"+passport.getUserName()+"] [服务器:"+servername+"] [渠道:"+channel+"] [信息："+result+"] [receipt:"+receipt+"] ["+(System.currentTimeMillis()-t)+"ms]");
					}
				}
				else {
					order.setHandleResult(OrderForm.HANDLE_RESULT_FAILED);
					OrderFormManager.getInstance().update(order);
					PlatformSavingCenter.logger.warn("[应用商店充值订单生成] [失败：此交易不合法] [id:"+order.getId()+"] [orderID：NULL] [充值类型：AppStore] [支付平台:AppStore] [paymoney:"+0+"] [用户:"+passport.getUserName()+"] [服务器:"+servername+"] [渠道:"+channel+"] [信息："+result+"] [receipt:"+receipt+"] ["+(System.currentTimeMillis()-t)+"ms]");
				}
			} else {
				order.setHandleResult(OrderForm.HANDLE_RESULT_FAILED);
				OrderFormManager.getInstance().update(order);
				PlatformSavingCenter.logger.warn("[应用商店充值订单生成] [失败：返回数据格式错误] [id:"+order.getId()+"] [orderID：NULL] [充值类型：AppStore] [支付平台:AppStore] [paymoney:"+0+"] [用户:"+passport.getUserName()+"] [服务器:"+servername+"] [渠道:"+channel+"] [信息："+result+"] [receipt:"+receipt+"] ["+(System.currentTimeMillis()-t)+"ms]");
			}
		} catch(Exception ex) {
			order.setHandleResult(OrderForm.HANDLE_RESULT_FAILED);
			try {
				OrderFormManager.getInstance().update(order);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				PlatformSavingCenter.logger.error("[应用商店充值订单生成] [失败：更新订单出现异常] [id:"+order.getId()+"] [orderID：NULL] [充值类型：AppStore] [支付平台:AppStore] [paymoney:"+0+"] [用户:"+passport.getUserName()+"] [服务器:"+servername+"] [渠道:"+channel+"] [receipt:"+receipt+"] ["+(System.currentTimeMillis()-t)+"ms]", e);
			}
			PlatformSavingCenter.logger.error("[应用商店充值订单生成] [失败：出现异常] [id:"+order.getId()+"] [orderID：NULL] [充值类型：AppStore] [支付平台:AppStore] [paymoney:"+0+"] [用户:"+passport.getUserName()+"] [服务器:"+servername+"] [渠道:"+channel+"] [receipt:"+receipt+"] ["+(System.currentTimeMillis()-t)+"ms]", ex);
		}
    	return orderID;
	}
	
	public String appStoreSaving(Passport passport, String channel, String servername, String receipt, String deviceCode,String[] others){
		
		long t = System.currentTimeMillis();
		
		String orderId = appStoreSaving(passport, channel, servername, receipt, deviceCode);
		
		if(others == null || others.length <= 0)
		{
			OrderForm order = OrderFormManager.getInstance().getOrderForm(orderId);
			if(order != null)
			{
				order.setMemo1("");
				try {
					OrderFormManager.getInstance().update(order);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					PlatformSavingCenter.logger.error("[应用商店充值订单生成] [失败：更新订单出现异常] [id:"+order.getId()+"] [orderID："+orderId+"] [充值类型：AppStore] [支付平台:AppStore] [paymoney:"+0+"] [用户:"+passport.getUserName()+"] [服务器:"+servername+"] [渠道:"+channel+"] [receipt:"+receipt+"] [角色:"+order.getMemo1()+"] ["+(System.currentTimeMillis()-t)+"ms]", e);
				}
				
				PlatformSavingCenter.logger.warn("[应用商店充值订单生成] [成功] [无角色id传入] [id:"+order.getId()+"] [orderID："+orderId+"] [充值类型：AppStore] [支付平台:AppStore] [paymoney:"+0+"] [用户:"+passport.getUserName()+"] [服务器:"+servername+"] [渠道:"+channel+"] [receipt:"+receipt+"] [角色:"+order.getMemo1()+"] ["+(System.currentTimeMillis()-t)+"ms]");
			}
			else
			{
				PlatformSavingCenter.logger.warn("[应用商店充值订单生成] [失败] [根据订单id没有查到订单] [无角色id传入] [orderid:"+orderId+"]");
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
					PlatformSavingCenter.logger.error("[应用商店充值订单生成] [失败：更新订单出现异常] [id:"+order.getId()+"] [orderID："+orderId+"] [充值类型：AppStore] [支付平台:AppStore] [paymoney:"+0+"] [用户:"+passport.getUserName()+"] [服务器:"+servername+"] [渠道:"+channel+"] [receipt:"+receipt+"] [角色:"+order.getMemo1()+"] ["+(System.currentTimeMillis()-t)+"ms]", e);
				}
				
				PlatformSavingCenter.logger.warn("[应用商店充值订单生成] [成功] [无角色id传入] [id:"+order.getId()+"] [orderID："+orderId+"] [充值类型：AppStore] [支付平台:AppStore] [paymoney:"+0+"] [用户:"+passport.getUserName()+"] [服务器:"+servername+"] [渠道:"+channel+"] [receipt:"+receipt+"] [角色:"+order.getMemo1()+"] ["+(System.currentTimeMillis()-t)+"ms]");
			}
			else
			{
				PlatformSavingCenter.logger.warn("[应用商店充值订单生成] [失败] [根据订单id没有查到订单] [无角色id传入] [orderid:"+orderId+"]");
			}
			return orderId;
		}
		else
		{
			return "";
		}
	}
	
	
	public static AppStoreGuojiSavingManager getInstance(){
		if(self == null) {
			self = new AppStoreGuojiSavingManager();
		}
		return self;
	}
	
	public static void main(String[] args) {
		try {
			String receipt = 
//				"ewoJInNpZ25hdHVyZSIgPSAiQWt0a2xTTlNaYi9lbzBlYmlnU2N6Z2p1Q25lNlk3WlRySXMvd0I4QXB3bThMUGtkZ1dacVl6eTloUWxidDVuSnBjTndUN3VCVDlDM1pRdVpMcmtPcEJTYVZjZWc5Rlg2aDJ2Wklpbks5SHFxZTJWdUNVV3YxUmkzSHQ1dWljdUErK0M0S3dYMGpEbG9HR0Z4L0ZDQmFobzVwNEY1aGRQZmJ0ZnA5UHdpZlV6bkFBQURWekNDQTFNd2dnSTdvQU1DQVFJQ0NCdXA0K1BBaG0vTE1BMEdDU3FHU0liM0RRRUJCUVVBTUg4eEN6QUpCZ05WQkFZVEFsVlRNUk13RVFZRFZRUUtEQXBCY0hCc1pTQkpibU11TVNZd0pBWURWUVFMREIxQmNIQnNaU0JEWlhKMGFXWnBZMkYwYVc5dUlFRjFkR2h2Y21sMGVURXpNREVHQTFVRUF3d3FRWEJ3YkdVZ2FWUjFibVZ6SUZOMGIzSmxJRU5sY25ScFptbGpZWFJwYjI0Z1FYVjBhRzl5YVhSNU1CNFhEVEUwTURZd056QXdNREl5TVZvWERURTJNRFV4T0RFNE16RXpNRm93WkRFak1DRUdBMVVFQXd3YVVIVnlZMmhoYzJWU1pXTmxhWEIwUTJWeWRHbG1hV05oZEdVeEd6QVpCZ05WQkFzTUVrRndjR3hsSUdsVWRXNWxjeUJUZEc5eVpURVRNQkVHQTFVRUNnd0tRWEJ3YkdVZ1NXNWpMakVMTUFrR0ExVUVCaE1DVlZNd2daOHdEUVlKS29aSWh2Y05BUUVCQlFBRGdZMEFNSUdKQW9HQkFNbVRFdUxnamltTHdSSnh5MW9FZjBlc1VORFZFSWU2d0Rzbm5hbDE0aE5CdDF2MTk1WDZuOTNZTzdnaTNvclBTdXg5RDU1NFNrTXArU2F5Zzg0bFRjMzYyVXRtWUxwV25iMzRucXlHeDlLQlZUeTVPR1Y0bGpFMU93QytvVG5STStRTFJDbWVOeE1iUFpoUzQ3VCtlWnRERWhWQjl1c2szK0pNMkNvZ2Z3bzdBZ01CQUFHamNqQndNQjBHQTFVZERnUVdCQlNKYUVlTnVxOURmNlpmTjY4RmUrSTJ1MjJzc0RBTUJnTlZIUk1CQWY4RUFqQUFNQjhHQTFVZEl3UVlNQmFBRkRZZDZPS2RndElCR0xVeWF3N1hRd3VSV0VNNk1BNEdBMVVkRHdFQi93UUVBd0lIZ0RBUUJnb3Foa2lHOTJOa0JnVUJCQUlGQURBTkJna3Foa2lHOXcwQkFRVUZBQU9DQVFFQWVhSlYyVTUxcnhmY3FBQWU1QzIvZkVXOEtVbDRpTzRsTXV0YTdONlh6UDFwWkl6MU5ra0N0SUl3ZXlOajVVUllISytIalJLU1U5UkxndU5sMG5rZnhxT2JpTWNrd1J1ZEtTcTY5Tkluclp5Q0Q2NlI0Szc3bmI5bE1UQUJTU1lsc0t0OG9OdGxoZ1IvMWtqU1NSUWNIa3RzRGNTaVFHS01ka1NscDRBeVhmN3ZuSFBCZTR5Q3dZVjJQcFNOMDRrYm9pSjNwQmx4c0d3Vi9abEwyNk0ydWVZSEtZQ3VYaGRxRnd4VmdtNTJoM29lSk9PdC92WTRFY1FxN2VxSG02bTAzWjliN1BSellNMktHWEhEbU9Nazd2RHBlTVZsTERQU0dZejErVTNzRHhKemViU3BiYUptVDdpbXpVS2ZnZ0VZN3h4ZjRjemZIMHlqNXdOelNHVE92UT09IjsKCSJwdXJjaGFzZS1pbmZvIiA9ICJld29KSW05eWFXZHBibUZzTFhCMWNtTm9ZWE5sTFdSaGRHVXRjSE4wSWlBOUlDSXlNREUyTFRBMExUQTNJREE1T2pBME9qUTBJRUZ0WlhKcFkyRXZURzl6WDBGdVoyVnNaWE1pT3dvSkluQjFjbU5vWVhObExXUmhkR1V0YlhNaUlEMGdJakUwTmpBd05EVXdPRFF5TlRRaU93b0pJblZ1YVhGMVpTMXBaR1Z1ZEdsbWFXVnlJaUE5SUNJeVpEVXdObVU0TWpCallUZG1Oekl3TmpNMFpESm1OalppT1dZellURmlZMlk1WlRJNVpUQXhJanNLQ1NKdmNtbG5hVzVoYkMxMGNtRnVjMkZqZEdsdmJpMXBaQ0lnUFNBaU1qWXdNREF3TWpBMk9EVTJPVGMxSWpzS0NTSmlkbkp6SWlBOUlDSXlMakl1T1RrdU15STdDZ2tpWVhCd0xXbDBaVzB0YVdRaUlEMGdJalUxTlRVNE9ETXdOaUk3Q2draWRISmhibk5oWTNScGIyNHRhV1FpSUQwZ0lqSTJNREF3TURJd05qZzFOamszTlNJN0Nna2ljWFZoYm5ScGRIa2lJRDBnSWpFaU93b0pJbTl5YVdkcGJtRnNMWEIxY21Ob1lYTmxMV1JoZEdVdGJYTWlJRDBnSWpFME5qQXdORFV3T0RReU5UUWlPd29KSW5WdWFYRjFaUzEyWlc1a2IzSXRhV1JsYm5ScFptbGxjaUlnUFNBaU9ESkZSVUZDUkVNdFJUVkdNaTAwTXpRMkxUazNNRVF0UWpNNU9ERTJOelZEUVRkRElqc0tDU0pwZEdWdExXbGtJaUE5SUNJMU5UazNNakF3TVRjaU93b0pJblpsY25OcGIyNHRaWGgwWlhKdVlXd3RhV1JsYm5ScFptbGxjaUlnUFNBaU9ERTFNemMwTVRrNElqc0tDU0p3Y205a2RXTjBMV2xrSWlBOUlDSjNZVzVuZUdsaGJqRTFJanNLQ1NKd2RYSmphR0Z6WlMxa1lYUmxJaUE5SUNJeU1ERTJMVEEwTFRBM0lERTJPakEwT2pRMElFVjBZeTlIVFZRaU93b0pJbTl5YVdkcGJtRnNMWEIxY21Ob1lYTmxMV1JoZEdVaUlEMGdJakl3TVRZdE1EUXRNRGNnTVRZNk1EUTZORFFnUlhSakwwZE5WQ0k3Q2draVltbGtJaUE5SUNKamIyMHVjM0ZoWjJVdWQyRnVaM2hwWVc1aGNIQnpkRzl5WlRNaU93b0pJbkIxY21Ob1lYTmxMV1JoZEdVdGNITjBJaUE5SUNJeU1ERTJMVEEwTFRBM0lEQTVPakEwT2pRMElFRnRaWEpwWTJFdlRHOXpYMEZ1WjJWc1pYTWlPd3A5IjsKCSJwb2QiID0gIjI2IjsKCSJzaWduaW5nLXN0YXR1cyIgPSAiMCI7Cn0";
//				"ewoJInNpZ25hdHVyZSIgPSAiQXowUGs2dlo5MHFhaXZhbml4MXJlR1d4OGRiUVgzdzdEQkJIeWxiU2hlTVZHVmlmRFhTK2dPTXBmQktHa3pleGRPNnN5b2RWcEhCeWFTRXZjV1ZqejdnMmQ1Mkp6NHgyRUM3OWhPTVczVEp3cmZNKzVEa1dhcGVGT1ZBS3lvdHF1YWpoYnArNjFwSGh3THpKRHNEaXo3YTRQWDhtNXgrTVRUdWNnRnNIcG5vSXpLZmErdStNWlFraHBKUVFrOUxIaTJBdDF2UzErV1dYWnBnQ3NKZk8vTUp4MWpKaHY3dHRKZHU5YXZXMUw2ZXpHalVpOERoUmlFUTlCR2Z1clM3Z3Zxa29hcjZQc1hMZ0NPdkhVWTdQb29lR0NVVmVabExNeUdRZUZGVDY3THU1bU9welFpZ3R3dkRXSFA2cGk3aUlOVDZVc1g5aklmM01CbUcxQlAxMWxyOEFBQVdBTUlJRmZEQ0NCR1NnQXdJQkFnSUlEdXRYaCtlZUNZMHdEUVlKS29aSWh2Y05BUUVGQlFBd2daWXhDekFKQmdOVkJBWVRBbFZUTVJNd0VRWURWUVFLREFwQmNIQnNaU0JKYm1NdU1Td3dLZ1lEVlFRTERDTkJjSEJzWlNCWGIzSnNaSGRwWkdVZ1JHVjJaV3h2Y0dWeUlGSmxiR0YwYVc5dWN6RkVNRUlHQTFVRUF3dzdRWEJ3YkdVZ1YyOXliR1IzYVdSbElFUmxkbVZzYjNCbGNpQlNaV3hoZEdsdmJuTWdRMlZ5ZEdsbWFXTmhkR2x2YmlCQmRYUm9iM0pwZEhrd0hoY05NVFV4TVRFek1ESXhOVEE1V2hjTk1qTXdNakEzTWpFME9EUTNXakNCaVRFM01EVUdBMVVFQXd3dVRXRmpJRUZ3Y0NCVGRHOXlaU0JoYm1RZ2FWUjFibVZ6SUZOMGIzSmxJRkpsWTJWcGNIUWdVMmxuYm1sdVp6RXNNQ29HQTFVRUN3d2pRWEJ3YkdVZ1YyOXliR1IzYVdSbElFUmxkbVZzYjNCbGNpQlNaV3hoZEdsdmJuTXhFekFSQmdOVkJBb01Da0Z3Y0d4bElFbHVZeTR4Q3pBSkJnTlZCQVlUQWxWVE1JSUJJakFOQmdrcWhraUc5dzBCQVFFRkFBT0NBUThBTUlJQkNnS0NBUUVBcGMrQi9TV2lnVnZXaCswajJqTWNqdUlqd0tYRUpzczl4cC9zU2cxVmh2K2tBdGVYeWpsVWJYMS9zbFFZbmNRc1VuR09aSHVDem9tNlNkWUk1YlNJY2M4L1cwWXV4c1FkdUFPcFdLSUVQaUY0MWR1MzBJNFNqWU5NV3lwb041UEM4cjBleE5LaERFcFlVcXNTNCszZEg1Z1ZrRFV0d3N3U3lvMUlnZmRZZUZScjZJd3hOaDlLQmd4SFZQTTNrTGl5a29sOVg2U0ZTdUhBbk9DNnBMdUNsMlAwSzVQQi9UNXZ5c0gxUEttUFVockFKUXAyRHQ3K21mNy93bXYxVzE2c2MxRkpDRmFKekVPUXpJNkJBdENnbDdaY3NhRnBhWWVRRUdnbUpqbTRIUkJ6c0FwZHhYUFEzM1k3MkMzWmlCN2o3QWZQNG83UTAvb21WWUh2NGdOSkl3SURBUUFCbzRJQjF6Q0NBZE13UHdZSUt3WUJCUVVIQVFFRU16QXhNQzhHQ0NzR0FRVUZCekFCaGlOb2RIUndPaTh2YjJOemNDNWhjSEJzWlM1amIyMHZiMk56Y0RBekxYZDNaSEl3TkRBZEJnTlZIUTRFRmdRVWthU2MvTVIydDUrZ2l2Uk45WTgyWGUwckJJVXdEQVlEVlIwVEFRSC9CQUl3QURBZkJnTlZIU01FR0RBV2dCU0lKeGNKcWJZWVlJdnM2N3IyUjFuRlVsU2p0ekNDQVI0R0ExVWRJQVNDQVJVd2dnRVJNSUlCRFFZS0tvWklodmRqWkFVR0FUQ0IvakNCd3dZSUt3WUJCUVVIQWdJd2diWU1nYk5TWld4cFlXNWpaU0J2YmlCMGFHbHpJR05sY25ScFptbGpZWFJsSUdKNUlHRnVlU0J3WVhKMGVTQmhjM04xYldWeklHRmpZMlZ3ZEdGdVkyVWdiMllnZEdobElIUm9aVzRnWVhCd2JHbGpZV0pzWlNCemRHRnVaR0Z5WkNCMFpYSnRjeUJoYm1RZ1kyOXVaR2wwYVc5dWN5QnZaaUIxYzJVc0lHTmxjblJwWm1sallYUmxJSEJ2YkdsamVTQmhibVFnWTJWeWRHbG1hV05oZEdsdmJpQndjbUZqZEdsalpTQnpkR0YwWlcxbGJuUnpMakEyQmdnckJnRUZCUWNDQVJZcWFIUjBjRG92TDNkM2R5NWhjSEJzWlM1amIyMHZZMlZ5ZEdsbWFXTmhkR1ZoZFhSb2IzSnBkSGt2TUE0R0ExVWREd0VCL3dRRUF3SUhnREFRQmdvcWhraUc5Mk5rQmdzQkJBSUZBREFOQmdrcWhraUc5dzBCQVFVRkFBT0NBUUVBRGFZYjB5NDk0MXNyQjI1Q2xtelQ2SXhETUlKZjRGelJqYjY5RDcwYS9DV1MyNHlGdzRCWjMrUGkxeTRGRkt3TjI3YTQvdncxTG56THJSZHJqbjhmNUhlNXNXZVZ0Qk5lcGhtR2R2aGFJSlhuWTR3UGMvem83Y1lmcnBuNFpVaGNvT0FvT3NBUU55MjVvQVE1SDNPNXlBWDk4dDUvR2lvcWJpc0IvS0FnWE5ucmZTZW1NL2oxbU9DK1JOdXhUR2Y4YmdwUHllSUdxTktYODZlT2ExR2lXb1IxWmRFV0JHTGp3Vi8xQ0tuUGFObVNBTW5CakxQNGpRQmt1bGhnd0h5dmozWEthYmxiS3RZZGFHNllRdlZNcHpjWm04dzdISG9aUS9PamJiOUlZQVlNTnBJcjdONFl0UkhhTFNQUWp2eWdhWndYRzU2QWV6bEhSVEJoTDhjVHFBPT0iOwoJInB1cmNoYXNlLWluZm8iID0gImV3b0pJbTl5YVdkcGJtRnNMWEIxY21Ob1lYTmxMV1JoZEdVdGNITjBJaUE5SUNJeU1ERTJMVEEwTFRBM0lESXhPakV3T2pJM0lFRnRaWEpwWTJFdlRHOXpYMEZ1WjJWc1pYTWlPd29KSW5WdWFYRjFaUzFwWkdWdWRHbG1hV1Z5SWlBOUlDSm1ZVGxpTW1RNU1HWmtaRFU1TTJKbVkyRXlNVFF3WVRkbVl6UmlZamhrTlRWbE5UVm1NamsxSWpzS0NTSnZjbWxuYVc1aGJDMTBjbUZ1YzJGamRHbHZiaTFwWkNJZ1BTQWlNVEF3TURBd01ESXdOREkyT0RZeU5TSTdDZ2tpWW5aeWN5SWdQU0FpTVM0eExqRTFMakFpT3dvSkluUnlZVzV6WVdOMGFXOXVMV2xrSWlBOUlDSXhNREF3TURBd01qQTBNalk0TmpJMUlqc0tDU0p4ZFdGdWRHbDBlU0lnUFNBaU1TSTdDZ2tpYjNKcFoybHVZV3d0Y0hWeVkyaGhjMlV0WkdGMFpTMXRjeUlnUFNBaU1UUTJNREE0T0RZeU56VXdNaUk3Q2draWRXNXBjWFZsTFhabGJtUnZjaTFwWkdWdWRHbG1hV1Z5SWlBOUlDSTRSRFEzUVRreVJpMUNRamhETFRSQ01Ea3RRakV3T0MweFFVTXlPVVZHUXprek4wRWlPd29KSW5CeWIyUjFZM1F0YVdRaUlEMGdJbU52YlM1bllXMWxZbTk0WTI5dGNHRnVlVGd1YkdWblpXNWthR1Z5Ynk1MGNta3hJanNLQ1NKcGRHVnRMV2xrSWlBOUlDSXhNRGszTmprNE5UWTFJanNLQ1NKaWFXUWlJRDBnSW1OdmJTNW5ZVzFsWW05NFkyOXRjR0Z1ZVRndWJHVm5aVzVrYUdWeWJ5STdDZ2tpY0hWeVkyaGhjMlV0WkdGMFpTMXRjeUlnUFNBaU1UUTJNREE0T0RZeU56VXdNaUk3Q2draWNIVnlZMmhoYzJVdFpHRjBaU0lnUFNBaU1qQXhOaTB3TkMwd09DQXdORG94TURveU55QkZkR012UjAxVUlqc0tDU0p3ZFhKamFHRnpaUzFrWVhSbExYQnpkQ0lnUFNBaU1qQXhOaTB3TkMwd055QXlNVG94TURveU55QkJiV1Z5YVdOaEwweHZjMTlCYm1kbGJHVnpJanNLQ1NKdmNtbG5hVzVoYkMxd2RYSmphR0Z6WlMxa1lYUmxJaUE5SUNJeU1ERTJMVEEwTFRBNElEQTBPakV3T2pJM0lFVjBZeTlIVFZRaU93cDkiOwoJImVudmlyb25tZW50IiA9ICJTYW5kYm94IjsKCSJwb2QiID0gIjEwMCI7Cgkic2lnbmluZy1zdGF0dXMiID0gIjAiOwp9";
				"ewoJInNpZ25hdHVyZSIgPSAiQTVYZXIxUzRWeHZKWS9TV0w2eTVhcXlXUHJnZWM2enhSUnVYUXZ1THJyK2djaWlaYmR2endXamhYYWJINjZYSXcweWNrQ2RDdW5lVUpRT3FOVWpMYzBvem9Tbm93V1dwb00rTFBibGsyY1BkalU5bXd5SWN6amdUL2pqOHRPZVB0Wmt4K3VMSVBpcW42ZVJwUzkzcVNlY0F0RFZVbi8xbkk2cFBxT1B3Mk9FbzJuY2hUbjVaTVV4U0lNcDl1YVpVWU8vQzFkZUwrZzVVRWhTaG1DRjFhRWZmZlhyZ29vbW1UUGFxVFFDUzViWXNWSUZ3eFEveld6YjZzVjRzZWJ0dll0SWgxRU1ZY1I2N1VZdjArNUdNWXdLbXJvWTQwZkdsZjMyS2pFMENiVGZJbWlMSENjZGFjOTNpa0IvbWt3ZXBhZ04vMnJTM0lFRlhBdGQ3Zjl5VGpzSUFBQVdBTUlJRmZEQ0NCR1NnQXdJQkFnSUlEdXRYaCtlZUNZMHdEUVlKS29aSWh2Y05BUUVGQlFBd2daWXhDekFKQmdOVkJBWVRBbFZUTVJNd0VRWURWUVFLREFwQmNIQnNaU0JKYm1NdU1Td3dLZ1lEVlFRTERDTkJjSEJzWlNCWGIzSnNaSGRwWkdVZ1JHVjJaV3h2Y0dWeUlGSmxiR0YwYVc5dWN6RkVNRUlHQTFVRUF3dzdRWEJ3YkdVZ1YyOXliR1IzYVdSbElFUmxkbVZzYjNCbGNpQlNaV3hoZEdsdmJuTWdRMlZ5ZEdsbWFXTmhkR2x2YmlCQmRYUm9iM0pwZEhrd0hoY05NVFV4TVRFek1ESXhOVEE1V2hjTk1qTXdNakEzTWpFME9EUTNXakNCaVRFM01EVUdBMVVFQXd3dVRXRmpJRUZ3Y0NCVGRHOXlaU0JoYm1RZ2FWUjFibVZ6SUZOMGIzSmxJRkpsWTJWcGNIUWdVMmxuYm1sdVp6RXNNQ29HQTFVRUN3d2pRWEJ3YkdVZ1YyOXliR1IzYVdSbElFUmxkbVZzYjNCbGNpQlNaV3hoZEdsdmJuTXhFekFSQmdOVkJBb01Da0Z3Y0d4bElFbHVZeTR4Q3pBSkJnTlZCQVlUQWxWVE1JSUJJakFOQmdrcWhraUc5dzBCQVFFRkFBT0NBUThBTUlJQkNnS0NBUUVBcGMrQi9TV2lnVnZXaCswajJqTWNqdUlqd0tYRUpzczl4cC9zU2cxVmh2K2tBdGVYeWpsVWJYMS9zbFFZbmNRc1VuR09aSHVDem9tNlNkWUk1YlNJY2M4L1cwWXV4c1FkdUFPcFdLSUVQaUY0MWR1MzBJNFNqWU5NV3lwb041UEM4cjBleE5LaERFcFlVcXNTNCszZEg1Z1ZrRFV0d3N3U3lvMUlnZmRZZUZScjZJd3hOaDlLQmd4SFZQTTNrTGl5a29sOVg2U0ZTdUhBbk9DNnBMdUNsMlAwSzVQQi9UNXZ5c0gxUEttUFVockFKUXAyRHQ3K21mNy93bXYxVzE2c2MxRkpDRmFKekVPUXpJNkJBdENnbDdaY3NhRnBhWWVRRUdnbUpqbTRIUkJ6c0FwZHhYUFEzM1k3MkMzWmlCN2o3QWZQNG83UTAvb21WWUh2NGdOSkl3SURBUUFCbzRJQjF6Q0NBZE13UHdZSUt3WUJCUVVIQVFFRU16QXhNQzhHQ0NzR0FRVUZCekFCaGlOb2RIUndPaTh2YjJOemNDNWhjSEJzWlM1amIyMHZiMk56Y0RBekxYZDNaSEl3TkRBZEJnTlZIUTRFRmdRVWthU2MvTVIydDUrZ2l2Uk45WTgyWGUwckJJVXdEQVlEVlIwVEFRSC9CQUl3QURBZkJnTlZIU01FR0RBV2dCU0lKeGNKcWJZWVlJdnM2N3IyUjFuRlVsU2p0ekNDQVI0R0ExVWRJQVNDQVJVd2dnRVJNSUlCRFFZS0tvWklodmRqWkFVR0FUQ0IvakNCd3dZSUt3WUJCUVVIQWdJd2diWU1nYk5TWld4cFlXNWpaU0J2YmlCMGFHbHpJR05sY25ScFptbGpZWFJsSUdKNUlHRnVlU0J3WVhKMGVTQmhjM04xYldWeklHRmpZMlZ3ZEdGdVkyVWdiMllnZEdobElIUm9aVzRnWVhCd2JHbGpZV0pzWlNCemRHRnVaR0Z5WkNCMFpYSnRjeUJoYm1RZ1kyOXVaR2wwYVc5dWN5QnZaaUIxYzJVc0lHTmxjblJwWm1sallYUmxJSEJ2YkdsamVTQmhibVFnWTJWeWRHbG1hV05oZEdsdmJpQndjbUZqZEdsalpTQnpkR0YwWlcxbGJuUnpMakEyQmdnckJnRUZCUWNDQVJZcWFIUjBjRG92TDNkM2R5NWhjSEJzWlM1amIyMHZZMlZ5ZEdsbWFXTmhkR1ZoZFhSb2IzSnBkSGt2TUE0R0ExVWREd0VCL3dRRUF3SUhnREFRQmdvcWhraUc5Mk5rQmdzQkJBSUZBREFOQmdrcWhraUc5dzBCQVFVRkFBT0NBUUVBRGFZYjB5NDk0MXNyQjI1Q2xtelQ2SXhETUlKZjRGelJqYjY5RDcwYS9DV1MyNHlGdzRCWjMrUGkxeTRGRkt3TjI3YTQvdncxTG56THJSZHJqbjhmNUhlNXNXZVZ0Qk5lcGhtR2R2aGFJSlhuWTR3UGMvem83Y1lmcnBuNFpVaGNvT0FvT3NBUU55MjVvQVE1SDNPNXlBWDk4dDUvR2lvcWJpc0IvS0FnWE5ucmZTZW1NL2oxbU9DK1JOdXhUR2Y4YmdwUHllSUdxTktYODZlT2ExR2lXb1IxWmRFV0JHTGp3Vi8xQ0tuUGFObVNBTW5CakxQNGpRQmt1bGhnd0h5dmozWEthYmxiS3RZZGFHNllRdlZNcHpjWm04dzdISG9aUS9PamJiOUlZQVlNTnBJcjdONFl0UkhhTFNQUWp2eWdhWndYRzU2QWV6bEhSVEJoTDhjVHFBPT0iOwoJInB1cmNoYXNlLWluZm8iID0gImV3b0pJbTl5YVdkcGJtRnNMWEIxY21Ob1lYTmxMV1JoZEdVdGNITjBJaUE5SUNJeU1ERTJMVEEwTFRBM0lESXhPalF5T2pBMklFRnRaWEpwWTJFdlRHOXpYMEZ1WjJWc1pYTWlPd29KSW5WdWFYRjFaUzFwWkdWdWRHbG1hV1Z5SWlBOUlDSTFOR1ExWVRNME1UUTBOR1kxTlRRNU9UWmtZMk01WVdRM01HRTNaVFZoWldReU16WTNPVGRqSWpzS0NTSnZjbWxuYVc1aGJDMTBjbUZ1YzJGamRHbHZiaTFwWkNJZ1BTQWlNVEF3TURBd01ESXdOREkzTURVM09TSTdDZ2tpWW5aeWN5SWdQU0FpTWk0eUxqazVMak1pT3dvSkluUnlZVzV6WVdOMGFXOXVMV2xrSWlBOUlDSXhNREF3TURBd01qQTBNamN3TlRjNUlqc0tDU0p4ZFdGdWRHbDBlU0lnUFNBaU1TSTdDZ2tpYjNKcFoybHVZV3d0Y0hWeVkyaGhjMlV0WkdGMFpTMXRjeUlnUFNBaU1UUTJNREE1TURVeU5qUTJNeUk3Q2draWRXNXBjWFZsTFhabGJtUnZjaTFwWkdWdWRHbG1hV1Z5SWlBOUlDSkJSREkxTUVWR1JDMDRNMEZDTFRSR05EVXRPVEJFTkMwME1qVXhSVEk1UTBNelJrWWlPd29KSW5CeWIyUjFZM1F0YVdRaUlEMGdJbmRoYm1kNGFXRnVNRGtpT3dvSkltbDBaVzB0YVdRaUlEMGdJalUxT1RjeU5ETTBOeUk3Q2draVltbGtJaUE5SUNKamIyMHVjM0ZoWjJVdWQyRnVaM2hwWVc1aGNIQnpkRzl5WlRNaU93b0pJbkIxY21Ob1lYTmxMV1JoZEdVdGJYTWlJRDBnSWpFME5qQXdPVEExTWpZME5qTWlPd29KSW5CMWNtTm9ZWE5sTFdSaGRHVWlJRDBnSWpJd01UWXRNRFF0TURnZ01EUTZOREk2TURZZ1JYUmpMMGROVkNJN0Nna2ljSFZ5WTJoaGMyVXRaR0YwWlMxd2MzUWlJRDBnSWpJd01UWXRNRFF0TURjZ01qRTZOREk2TURZZ1FXMWxjbWxqWVM5TWIzTmZRVzVuWld4bGN5STdDZ2tpYjNKcFoybHVZV3d0Y0hWeVkyaGhjMlV0WkdGMFpTSWdQU0FpTWpBeE5pMHdOQzB3T0NBd05EbzBNam93TmlCRmRHTXZSMDFVSWpzS2ZRPT0iOwoJImVudmlyb25tZW50IiA9ICJTYW5kYm94IjsKCSJwb2QiID0gIjEwMCI7Cgkic2lnbmluZy1zdGF0dXMiID0gIjAiOwp9";
			Map headers = new HashMap();
			URL url = new URL(SANDBOX_RECHARGE_URL);
//			URL url = new URL(RECHARGE_URL);
			headers.put("Content-type", "application/json");
			receipt = "{\"receipt-data\":\"" + receipt + "\"}";
			byte[] b = HttpsUtils.webPost(url, receipt.getBytes(), headers, 60000, 60000);
			String result = new String(b,"UTF-8");
			System.out.println(result);
//			Object obj = null;
//			if(result != null && result.length() > 0) {
//				obj = JacksonManager.getInstance().jsonDecodeObject(result);
//			}
		} catch(Exception e) {
			
		}
	}
}
