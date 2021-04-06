package com.fy.boss.platform.amazon;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.finance.dao.impl.OrderFormDAOImpl;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.fy.boss.tools.JacksonManager;
import com.fy.boss.platform.amazon.AmazonSavingManager;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.servlet.HttpsUtils;
import com.xuanzhi.tools.text.DateUtil;

	public class AmazonSavingManager {
	static AmazonSavingManager self;
	//正式:https://appstore-sdk.amazon.com/version/2.0/verify/developer/developerSecret/user/userId/purchaseToken/purchaseToken
	public static String RECHARGE_URL = "https://appstore-sdk.amazon.com/version/2.0/verify/";
	//测试: http://192.168.0.1:80/RVSSandbox/version/2.0/verify/...
	public static String SANDBOX_RECHARGE_URL = "http://124.248.40.21:9110/RVSSandbox/version/2.0/verify/";
	public static String DEVELOPER_SECRET = "2:gxp7LMSZwLWgwYsoB1_Tra1OJTt9ydOR6aC1jTibfiCZA6ckIAfck9gXkxQ2jC6w:_BE1Mul3244UehygnG3ZXQ==";
		
	public static Map<String, Long> map = new HashMap<String,Long>();
	
	public void initialize(){
		AmazonSavingManager.self=this;
		System.out.println("["+this.getClass().getName()+"] [initialized]");
		PlatformSavingCenter.logger.info("["+this.getClass().getName()+"] [initialized]");	
	}

	static {//单位人民币分
		map.put("com.sqage.amazon6RMB", 600L);
		map.put("com.sqage.amazon12RMB", 1200L);
		map.put("com.sqage.amazon30RMB", 3000L);
		map.put("com.sqage.amazon60RMB", 6000L);
		map.put("com.sqage.amazon98RMB", 9800L);
		map.put("com.sqage.amazon218RMB", 21800L);
		map.put("com.sqage.amazon318RMB", 31800L);
		map.put("com.sqage.amazon418RMB", 41800L);
		map.put("com.sqage.amazon588RMB", 58800L);
		
	}
	
	public String appStoreSaving(Passport passport, String channel, String servername, String receipt, String deviceCode){
		long t=System.currentTimeMillis();
		OrderForm order = new OrderForm();
		java.util.Date cdate = new java.util.Date();
		order.setCreateTime(t);
		order.setPassportId(passport.getId());
		order.setServerName(servername);
		order.setSavingPlatform("Amazon");
		order.setSavingMedium("亚马逊充值");
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
    		String breakSign = "@L#";
    		String[] recInfo = receipt.split(breakSign);
    		if(recInfo == null || recInfo.length != 2)
    		{
    			PlatformSavingCenter.logger.warn("[应用商店充值订单生成] [失败] [收到验证串不符合规则] [username:"+passport.getUserName()+"] [channel:"+channel+"] [server:"+servername+"] [验证串:"+receipt+"] [devicecode:"+deviceCode+"] [orderid:"+orderID+"] [cost:"+(System.currentTimeMillis()-t)+"ms]");
    			return null;
    		}
    		String userid = recInfo[0];
    		receipt = recInfo[1];
    		
			Map headers = new HashMap();
			String realUrl  = RECHARGE_URL+"developer/"+DEVELOPER_SECRET+"/user/"+userid + "/purchaseToken/" + receipt;
			URL url = new URL(realUrl);
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
			//要改成正式环境 要用https
			byte[] b = null; 
					
			try
			{
				b = HttpsUtils.webGet(url, headers, 60000, 60000);
			}
			catch(Exception e)
			{
				b=null;
			}
			String result = null;
			if(b != null)	
				result = new String(b,"UTF-8");
			Object obj = null;
			if(result != null && result.length() > 0) {
				obj = JacksonManager.getInstance().jsonDecodeObject(result);
			}
			Map dataMap = null;
			Integer code = (Integer)headers.get(HttpUtils.Response_Code);
			boolean isValid = false;
			String skuItem = null;
			if(obj != null && obj instanceof Map) {
				dataMap = (Map) obj;			
				skuItem = (String)dataMap.get("sku");
				isValid = (skuItem != null && map.containsKey(skuItem));			
				PlatformSavingCenter.logger.warn("[应用商店充值订单生成] [正式环境验证] [id:"+order.getId()+"] [orderID："+orderID+"] [充值类型：亚马逊充值] [支付平台:Amazon] [paymoney:--] [用户:"+passport.getUserName()+"] [服务器:"+servername+"] [渠道:"+channel+"] [信息："+result+"] [url:"+realUrl+"] [userid:"+userid+"] [receipt:"+receipt+"] [code:"+(code == null ? new Integer(-1):code.intValue())+"] [是否合法:"+isValid+"] ["+(System.currentTimeMillis()-t)+"ms]");
			} else {
				//如果正式环境不行 就再去沙盒环境验证
				if(!isValid &&  (code != null && code.intValue() != 500))
				{
					realUrl  = SANDBOX_RECHARGE_URL+"developer/"+DEVELOPER_SECRET+"/user/"+userid + "/purchaseToken/" + receipt;
					url = new URL(realUrl);
					headers = new HashMap();
					try
					{
						b = HttpUtils.webGet(url, headers, 60000, 60000);
					}
					catch(Exception e)
					{
						b = null;
					}
					if(b != null)
						result = new String(b,"UTF-8");
					obj = null;
					if(result != null && result.length() > 0) {
						obj = JacksonManager.getInstance().jsonDecodeObject(result);
					}
					dataMap = null;
					code = (Integer)headers.get(HttpUtils.Response_Code);
					
					if(obj != null && obj instanceof Map) {
						dataMap = (Map) obj;			
						skuItem = (String)dataMap.get("sku");
						isValid = (skuItem != null && map.containsKey(skuItem));			
					}
					
					PlatformSavingCenter.logger.warn("[应用商店充值订单生成] [沙盒环境验证] [id:"+order.getId()+"] [orderID："+orderID+"] [充值类型：亚马逊充值] [支付平台:Amazon] [paymoney:--] [用户:"+passport.getUserName()+"] [服务器:"+servername+"] [渠道:"+channel+"] [信息："+result+"] [url:"+realUrl+"] [userid:"+userid+"] [receipt:"+receipt+"] [code:"+(code == null ? new Integer(-1):code.intValue())+"] [是否合法:"+isValid+"] ["+(System.currentTimeMillis()-t)+"ms]");
				}
			}
			
			if(isValid) {
				List<OrderForm> exists = OrderFormDAOImpl.getInstance().queryForWhere(" userChannel = '"+channel.trim()+"' and handleResultDesp = '"+receipt+"'");
			
				//检查订单是否被处理
				if(exists != null && exists.size() > 0) {
					order.setHandleResult(OrderForm.HANDLE_RESULT_FAILED);
					OrderFormManager.getInstance().update(order);
					PlatformSavingCenter.logger.warn("[应用商店充值订单生成] [失败:此交易已经生成过订单] [transactionID："+orderID+"] [充值类型：亚马逊充值] [支付平台:Amazon] [用户:"+passport.getUserName()+"] [服务器:"+servername+"] [渠道:"+channel+"] [信息："+result+"] [url:"+realUrl+"] [userid:"+userid+"] [验证串:"+receipt+"] ["+(System.currentTimeMillis()-t)+"ms]");
					return null;
				} else {
					Long addAmount = map.get(skuItem);
					if(addAmount != null && addAmount > 0) {
						order.setPayMoney(addAmount);
						order.setOrderId(orderID);
						order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
						order.setHandleResultDesp(receipt);
						order.setResponseResult(OrderForm.RESPONSE_SUCC);
						OrderFormManager.getInstance().update(order);
						PlatformSavingCenter.logger.warn("[应用商店充值订单生成] [成功] [id:"+order.getId()+"] [orderID："+orderID+"] [充值类型：亚马逊充值] [支付平台:Amazon] [paymoney:"+addAmount+"] [用户:"+passport.getUserName()+"] [服务器:"+servername+"] [渠道:"+channel+"] [信息："+result+"] [url:"+realUrl+"] [userid:"+userid+"] [receipt:"+receipt+"] ["+(System.currentTimeMillis()-t)+"ms]");
					} else {
						order.setHandleResult(OrderForm.HANDLE_RESULT_FAILED);
						order.setHandleResultDesp(receipt);
						OrderFormManager.getInstance().update(order);
						PlatformSavingCenter.logger.warn("[应用商店充值订单生成] [失败：此交易返回的数据有误（充值额<0）] [id:"+order.getId()+"] [orderID："+orderID+"] [充值类型：亚马逊充值] [支付平台:Amazon] [paymoney:"+addAmount+"] [用户:"+passport.getUserName()+"] [服务器:"+servername+"] [渠道:"+channel+"] [信息："+result+"] [url:"+realUrl+"] [userid:"+userid+"] [receipt:"+receipt+"] ["+(System.currentTimeMillis()-t)+"ms]");
					} 
				}
			}
			else {
				order.setHandleResult(OrderForm.HANDLE_RESULT_FAILED);
				order.setHandleResultDesp(receipt);
				OrderFormManager.getInstance().update(order);
				PlatformSavingCenter.logger.warn("[应用商店充值订单生成] [失败：此交易不合法] [id:"+order.getId()+"] [orderID：NULL] [充值类型：亚马逊充值] [支付平台:Amazon] [paymoney:"+0+"] [用户:"+passport.getUserName()+"] [服务器:"+servername+"] [渠道:"+channel+"] [信息："+result+"] [url:"+realUrl+"] [userid:"+userid+"] [receipt:"+receipt+"] ["+(System.currentTimeMillis()-t)+"ms]");
			}
			
			
		} catch(Exception ex) {
			order.setHandleResult(OrderForm.HANDLE_RESULT_FAILED);
			try {
				OrderFormManager.getInstance().update(order);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				PlatformSavingCenter.logger.error("[应用商店充值订单生成] [失败：更新订单出现异常] [id:"+order.getId()+"] [orderID：NULL] [充值类型：亚马逊充值] [支付平台:Amazon] [paymoney:"+0+"] [用户:"+passport.getUserName()+"] [服务器:"+servername+"] [渠道:"+channel+"] [receipt:"+receipt+"] ["+(System.currentTimeMillis()-t)+"ms]", e);
			}
			PlatformSavingCenter.logger.error("[应用商店充值订单生成] [失败：出现异常] [id:"+order.getId()+"] [orderID：NULL] [充值类型：亚马逊充值] [支付平台:Amazon] [paymoney:"+0+"] [用户:"+passport.getUserName()+"] [服务器:"+servername+"] [渠道:"+channel+"] [receipt:"+receipt+"] ["+(System.currentTimeMillis()-t)+"ms]", ex);
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
	
	public static AmazonSavingManager getInstance(){
		if(self == null) {
			self = new AmazonSavingManager();
		}
		return self;
	}
}
