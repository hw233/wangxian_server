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
import com.fy.boss.message.appstore.AppStoreKunLunSavingManager;
import com.xuanzhi.tools.servlet.HttpsUtils;
import com.xuanzhi.tools.text.DateUtil;

	public class AppStoreKunLunSavingManager {
	static AppStoreKunLunSavingManager self;
		
	public static String RECHARGE_URL = "https://buy.itunes.apple.com/verifyReceipt";
	public static String SANDBOX_RECHARGE_URL = "https://sandbox.itunes.apple.com/verifyReceipt";
		
	public static Map<String, Long> map = new HashMap<String,Long>();
	public static boolean isGoSandBox = false;
	
	public void initialize(){
		AppStoreKunLunSavingManager.self=this;
		System.out.println("["+this.getClass().getName()+"] [initialized]");
		PlatformSavingCenter.logger.info("["+this.getClass().getName()+"] [initialized]");	
	}
	
	static {//单位人民币分
		map.put("koramgame.wxcht.silver1", 250L);
		map.put("koramgame.wxcht.silver2", 750L);
		map.put("koramgame.wxcht.silver3", 1250L);
		map.put("koramgame.wxcht.silver4", 2500L);
		map.put("koramgame.wxcht.silver5", 7500L);
		map.put("koramgame.wxcht.silver6", 12500L);
		map.put("koramgame.wxcht.silver7", 25000L);
	}
	
	public String appStoreSaving(Passport passport, String channel, String servername, String receipt, String deviceCode){
		long t=System.currentTimeMillis();
		OrderForm order = new OrderForm();
		java.util.Date cdate = new java.util.Date();
		order.setCreateTime(t);
		order.setPassportId(passport.getId());
		order.setServerName(servername);
		order.setSavingPlatform("KunlunAppStore");
		order.setSavingMedium("KunlunAppStore");
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
				boolean isSandBox = false;
				isValid = (dataMap.get("status") != null && (dataMap.get("status") + "").equals("0"));
			//如果正式环境不行 就再去沙盒环境验证
				if(isGoSandBox)
				{
					if(!isValid && dataMap.get("status") != null  && ((dataMap.get("status") + "").equals("21007") || (dataMap.get("status") + "").equals("21002")) )
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
						
						isSandBox = true;
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
							PlatformSavingCenter.logger.warn("[应用商店充值"+(isSandBox?"沙盒":"" )+"订单生成] [失败:此交易已经生成过订单] [transactionID："+orderID+"] [充值类型：AppStore] [支付平台:AppStore] [用户:"+passport.getUserName()+"] [服务器:"+servername+"] [渠道:"+channel+"] [信息："+result+"] ["+(System.currentTimeMillis()-t)+"ms]");
							return null;
						} else {
							Long addAmount = map.get(productId);
							if(addAmount != null && addAmount > 0) {
								order.setPayMoney(addAmount);
								order.setOrderId(orderID);
								order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
								order.setResponseResult(OrderForm.RESPONSE_SUCC);
								OrderFormManager.getInstance().update(order);
								PlatformSavingCenter.logger.warn("[应用商店充值"+(isSandBox?"沙盒":"" )+"订单生成] [成功] [id:"+order.getId()+"] [orderID："+orderID+"] [充值类型：AppStore] [支付平台:AppStore] [paymoney:"+addAmount+"] [用户:"+passport.getUserName()+"] [服务器:"+servername+"] [渠道:"+channel+"] [信息："+result+"] [url:"+RECHARGE_URL+"] [receipt:"+receipt+"] ["+(System.currentTimeMillis()-t)+"ms]");
							} else {
								order.setHandleResult(OrderForm.HANDLE_RESULT_FAILED);
								OrderFormManager.getInstance().update(order);
								PlatformSavingCenter.logger.warn("[应用商店充值"+(isSandBox?"沙盒":"" )+"订单生成] [失败：此交易返回的数据有误（充值额<0）] [id:"+order.getId()+"] [orderID："+orderID+"] [充值类型：AppStore] [支付平台:AppStore] [paymoney:"+addAmount+"] [用户:"+passport.getUserName()+"] [服务器:"+servername+"] [渠道:"+channel+"] [信息："+result+"] [receipt:"+receipt+"] ["+(System.currentTimeMillis()-t)+"ms]");
							} 
						}
					} else {
						order.setHandleResult(OrderForm.HANDLE_RESULT_FAILED);
						OrderFormManager.getInstance().update(order);
						PlatformSavingCenter.logger.warn("[应用商店充值"+(isSandBox?"沙盒":"" )+"订单生成] [失败：此交易返回的数据有误] [id:"+order.getId()+"] [orderID：NULL] [充值类型：AppStore] [支付平台:AppStore] [paymoney:"+0+"] [用户:"+passport.getUserName()+"] [服务器:"+servername+"] [渠道:"+channel+"] [信息："+result+"] [receipt:"+receipt+"] ["+(System.currentTimeMillis()-t)+"ms]");
					}
				}
				else {
					order.setHandleResult(OrderForm.HANDLE_RESULT_FAILED);
					OrderFormManager.getInstance().update(order);
					PlatformSavingCenter.logger.warn("[应用商店充值"+(isSandBox?"沙盒":"" )+"订单生成] [失败：此交易不合法] [id:"+order.getId()+"] [orderID：NULL] [充值类型：AppStore] [支付平台:AppStore] [paymoney:"+0+"] [用户:"+passport.getUserName()+"] [服务器:"+servername+"] [渠道:"+channel+"] [信息："+result+"] [receipt:"+receipt+"] ["+(System.currentTimeMillis()-t)+"ms]");
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
	
	public static AppStoreKunLunSavingManager getInstance(){
		if(self == null) {
			self = new AppStoreKunLunSavingManager();
		}
		return self;
	}
}
