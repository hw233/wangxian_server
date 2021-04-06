package com.fy.boss.platform.googleplay;

import java.net.URL;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.finance.dao.impl.OrderFormDAOImpl;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.fy.boss.platform.googleplay.util.Security;
import com.fy.boss.tools.JacksonManager;
import com.fy.boss.platform.googleplay.GooglePlaySavingManager;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.servlet.HttpsUtils;
import com.xuanzhi.tools.text.DateUtil;

	public class GooglePlaySavingManager {
	static GooglePlaySavingManager self;
	
	public static String TEST_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqhDo97aZexvnVmtXf5sY4PAAVsbjlm0Kq0JO6iztUYLV90oNfHRUQhOaDIFJq3TcIE84yzu9/jzhC5o+jR5dEzxS+6F0zrAYq2cUBLbPqLNXnVUi3C2oyQVCiauwUOz4cI0FKY8Ddvbl9xXgymAl2utlw6UFsokQ1VNlsacA4qHrdphYX/g1DJ4QXjKa8YQv2vgISY22TgY4rsSvMcm1twlGa5ETPjOxA5kNVaAlYjqAuWA/542o6MH6mpzwTMmJAMgniuLmEgYtzu+x707rdDKkdQIXXNcSEZdiDmLfTN+foMeu9wdLypkfLMy55SIzKnOY43+D68MKQaBZryAYnwIDAQAB";
	public static String PUBLIC_KEY = "";	
	
	public static boolean IS_TEST = true;
	 
	public static Map<String, Long> map = new HashMap<String,Long>();
	
    public static final int BILLING_RESPONSE_RESULT_OK = 0;
    public static final int BILLING_RESPONSE_RESULT_USER_CANCELED = 1;
    public static final int BILLING_RESPONSE_RESULT_BILLING_UNAVAILABLE = 3;
    public static final int BILLING_RESPONSE_RESULT_ITEM_UNAVAILABLE = 4;
    public static final int BILLING_RESPONSE_RESULT_DEVELOPER_ERROR = 5;
    public static final int BILLING_RESPONSE_RESULT_ERROR = 6;
    public static final int BILLING_RESPONSE_RESULT_ITEM_ALREADY_OWNED = 7;
    public static final int BILLING_RESPONSE_RESULT_ITEM_NOT_OWNED = 8;
	
	
	public void initialize(){
		GooglePlaySavingManager.self=this;
		System.out.println("["+this.getClass().getName()+"] [initialized]");
		PlatformSavingCenter.logger.info("["+this.getClass().getName()+"] [initialized]");	
	}

	static {//单位人民币分
		map.put("com.sqage.wangxiantest", 400L);
		map.put("com.sqage.wangxian1lv", 3000L);
		map.put("com.sqage.wangxian2lv", 6000L);
		map.put("com.sqage.wangxian3lv", 9800L);
		map.put("com.sqage.wangxian4lv", 21800L);
		map.put("com.sqage.wangxian5lv", 31800L);
		map.put("com.sqage.wangxian6lv", 41800L);
		map.put("com.sqage.wangxian7lv", 58800L);
		
	}
	
	public String appStoreSaving(Passport passport, String channel, String servername, String receipt, String deviceCode){
		long t=System.currentTimeMillis();
		String publickey = TEST_PUBLIC_KEY;
		
		if(!IS_TEST)
		{
			publickey = PUBLIC_KEY;
		}
		
		OrderForm order = new OrderForm();
		java.util.Date cdate = new java.util.Date();
		order.setCreateTime(t);
		order.setPassportId(passport.getId());
		order.setServerName(servername);
		order.setSavingPlatform("googleplay");
		order.setSavingMedium("GOOGLEINAPPBILLING");
		//order.setMediumInfo("receipt:" + receipt);
		order.setPayMoney(0L);
		order.setHandleResult(OrderForm.HANDLE_RESULT_NOBACK);
		order.setResponseResult(OrderForm.RESPONSE_NOBACK);
		order.setUserChannel(channel);
		order.setMemo1("Android");
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
    		String breakSign = "@L#";
    		String[] recInfo = receipt.split(breakSign);
    		if(recInfo == null || recInfo.length != 2)
    		{
    			PlatformSavingCenter.logger.warn("[应用商店充值订单生成] [失败] [收到验证串不符合规则] [username:"+passport.getUserName()+"] [channel:"+channel+"] [server:"+servername+"] [验证串:"+receipt+"] [devicecode:"+deviceCode+"] [orderid:"+orderID+"] [cost:"+(System.currentTimeMillis()-t)+"ms]");
    			return null;
    		}
    		receipt = recInfo[0];
    		String sign = recInfo[1];
    		
    		
    		//验证签名
//			headers.put("Content-type", "application/json");
//			receipt = "{\"receipt-data\":\"" + receipt + "\"}";
			
			/**
			 * 
			 * { "itemType":"ENTITLED",
"startDate":null,
"endDate":null,
"sku":"myapp.entitlement.sku",
"purchaseToken":"2:FlrXSsmgOBKXoBbf6BtIrBtmbZLNr92laKjtTMTlz9tQyYUXl-vuEsdl1Hr8g0xxsQIa8JP3uIqNfmatmSRnOamsrYWGlpKFTrKb0IWXPlYlXhY4EH0ufJYuWzoOicNXCm6BBH9seKczkQ_I-QObpjCuHnlZk4pXgl3g_VggJZGpWBtuvYAqOVXYfcMjf268BaMjVX7plTQ_MPvzLrRNGQ==:qsy5n5MMZM4u-LlDrqGp5Q==" }
			 */
			Object obj = null;
			if(receipt != null && receipt.length() > 0) {
				obj = JacksonManager.getInstance().jsonDecodeObject(receipt);
			}
			Map dataMap = null;
			boolean isValid = false;
			if(sign != null && sign.length() > 0)
			{
				
				//验证签名
				isValid  = Security.verifyPurchase(publickey, receipt, sign);
			}
			
			String skuItem = null;
			String channelOrderId = null;
			String token = null;
			int orderStatus = -1;
			
			if(obj != null && obj instanceof Map) {
				dataMap = (Map) obj;			
				skuItem = (String)dataMap.get("productId");
				channelOrderId = (String)dataMap.get("orderId");
				orderStatus = ((Integer)dataMap.get("purchaseState")).intValue();
				PlatformSavingCenter.logger.warn("[应用商店充值订单生成] ["+(IS_TEST ? "测试":"正式")+"环境验证] [id:"+order.getId()+"] [orderID："+orderID+"] [充值类型："+order.getSavingMedium()+"] [支付平台:googleplay] [paymoney:--] [用户:"+passport.getUserName()+"] [服务器:"+servername+"] [渠道:"+channel+"] [信息："+receipt+"] [sign:"+sign+"] [是否合法:"+isValid+"] ["+(System.currentTimeMillis()-t)+"ms]");
			} else {
				PlatformSavingCenter.logger.warn("[应用商店充值订单生成] ["+(IS_TEST ? "测试":"正式")+"环境验证] [失败] [出现非法的json串] [id:"+order.getId()+"] [orderID："+orderID+"] [充值类型："+order.getSavingMedium()+"] [支付平台:googleplay] [paymoney:--] [用户:"+passport.getUserName()+"] [服务器:"+servername+"] [渠道:"+channel+"] [信息："+receipt+"] [sign:"+sign+"] [是否合法:"+isValid+"] ["+(System.currentTimeMillis()-t)+"ms]");
				return null;
			}
			
			if(isValid) {
				List<OrderForm> exists = OrderFormDAOImpl.getInstance().queryForWhere(" userChannel = '"+channel.trim()+"' and channelOrderId = '"+channelOrderId+"'");
			
				//检查订单是否被处理
				if(exists != null && exists.size() > 0) {
					order.setHandleResult(OrderForm.HANDLE_RESULT_FAILED);
					OrderFormManager.getInstance().update(order);
					PlatformSavingCenter.logger.warn("[应用商店充值订单生成] [失败:此交易已经生成过订单] [transactionID："+orderID+"] [充值类型："+order.getSavingMedium()+"] [支付平台:googleplay] [用户:"+passport.getUserName()+"] [服务器:"+servername+"] [渠道:"+channel+"] [信息："+receipt+"] [sign:"+sign+"] ["+(System.currentTimeMillis()-t)+"ms]");
					return null;
				} else {
					Long addAmount = map.get(skuItem);
					if(addAmount != null && addAmount > 0 && orderStatus == 0) {
						order.setPayMoney(addAmount);
						order.setOrderId(orderID);
						order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
						order.setHandleResultDesp(receipt);
						order.setResponseResult(OrderForm.RESPONSE_SUCC);
						order.setChannelOrderId(channelOrderId);
						OrderFormManager.getInstance().update(order);
						PlatformSavingCenter.logger.warn("[应用商店充值订单生成] [成功] [id:"+order.getId()+"] [orderID："+orderID+"] [充值类型："+order.getSavingMedium()+"] [支付平台:googleplay] [paymoney:"+addAmount+"] [用户:"+passport.getUserName()+"] [服务器:"+servername+"] [渠道:"+channel+"] [信息："+receipt+"] [sign:"+sign+"] ["+(System.currentTimeMillis()-t)+"ms]");
					} else {
						order.setHandleResult(OrderForm.HANDLE_RESULT_FAILED);
						order.setHandleResultDesp(receipt);
						order.setChannelOrderId(channelOrderId);
						OrderFormManager.getInstance().update(order);
						PlatformSavingCenter.logger.warn("[应用商店充值订单生成] [失败] [id:"+order.getId()+"] [orderID："+orderID+"] [充值类型："+order.getSavingMedium()+"] [支付平台:googleplay] [paymoney:"+addAmount+"] [用户:"+passport.getUserName()+"] [服务器:"+servername+"] [渠道:"+channel+"] [信息："+receipt+"] [sign:"+sign+"] ["+(System.currentTimeMillis()-t)+"ms]");
					} 
				}
			}
			else {
				order.setHandleResult(OrderForm.HANDLE_RESULT_FAILED);
				order.setHandleResultDesp(receipt);
				order.setChannelOrderId(channelOrderId);
				OrderFormManager.getInstance().update(order);
				PlatformSavingCenter.logger.warn("[应用商店充值订单生成] [失败：此交易不合法] [id:"+order.getId()+"] [orderID：NULL] [充值类型："+order.getSavingMedium()+"] [支付平台:googleplay] [paymoney:"+0+"] [用户:"+passport.getUserName()+"] [服务器:"+servername+"] [渠道:"+channel+"] [信息："+receipt+"] [sign:"+sign+"] ["+(System.currentTimeMillis()-t)+"ms]");
			}
			
			
		} catch(Exception ex) {
			order.setHandleResult(OrderForm.HANDLE_RESULT_FAILED);
			try {
				OrderFormManager.getInstance().update(order);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				PlatformSavingCenter.logger.error("[应用商店充值订单生成] [失败：更新订单出现异常] [id:"+order.getId()+"] [orderID：NULL] [充值类型："+order.getSavingMedium()+"] [支付平台:googleplay] [paymoney:"+0+"] [用户:"+passport.getUserName()+"] [服务器:"+servername+"] [渠道:"+channel+"] [receipt:"+receipt+"] ["+(System.currentTimeMillis()-t)+"ms]", e);
			}
			PlatformSavingCenter.logger.error("[应用商店充值订单生成] [失败：出现异常] [id:"+order.getId()+"] [orderID：NULL] [充值类型："+order.getSavingMedium()+"] [支付平台:googleplay] [paymoney:"+0+"] [用户:"+passport.getUserName()+"] [服务器:"+servername+"] [渠道:"+channel+"] [receipt:"+receipt+"] ["+(System.currentTimeMillis()-t)+"ms]", ex);
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
	
	public static GooglePlaySavingManager getInstance(){
		if(self == null) {
			self = new GooglePlaySavingManager();
		}
		return self;
	}
}
