package com.fy.boss.message.appstore;

import java.net.URL;
import java.util.Arrays;
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

public class AppStoreSavingManager {
	static AppStoreSavingManager self;
		
	public static String RECHARGE_URL = "https://buy.itunes.apple.com/verifyReceipt";
	//public static String RECHARGE_URL = "https://sandbox.itunes.apple.com/verifyReceipt";//
		
	public static Map<String, Long> map = new HashMap<String,Long>();
	public static Map<String, String> map2 = new HashMap<String,String>();
	public static boolean isGoSandBox = false;
	
	public void initialize(){
		AppStoreSavingManager.self=this;
		System.out.println("["+this.getClass().getName()+"] [initialized]");
		PlatformSavingCenter.logger.info("["+this.getClass().getName()+"] [initialized]");	
	}
	
	static {//单位人民币分
		map.put("xunxian01", 600L);
		map.put("xunxian02", 1200L);
		map.put("xunxian03", 3000L);
		map.put("xunxian04", 6000L);
		map.put("xunxian05", 9800L);
		map.put("xunxian06", 21800L);
		map.put("xunxian07", 31800L);
		map.put("xunxian08", 41800L);
		map.put("xunxian09", 58800L);
		map.put("xunxian10", 99800L);
		
		map2.put("xunxian01", "xxy6");
		map2.put("xunxian02", "xxy12");
		map2.put("xunxian03", "xxy30");
		map2.put("xunxian04", "xxy60");
		map2.put("xunxian05", "xxy98");
		map2.put("xunxian06", "xxy218");
		map2.put("xunxian07", "xxy318");
		map2.put("xunxian08", "xxy418");
		map2.put("xunxian09", "xxy588");
		map2.put("newxunxian_yueka12", "xxy588");
		map2.put("newxunxian_yueka18", "xxy588");
		map2.put("newxunxian_yueka30", "xxy588");
		map2.put("xunxian_day1", "xxy588");
		map2.put("xunxian_day3", "xxy588");
		map2.put("xunxian_day6", "xxy588");
		//月卡
		map.put("newxunxian_yueka12", 1200L);
		map.put("newxunxian_yueka18", 1800L);
		map.put("newxunxian_yueka30", 3000L);
		map.put("MQB12", 1200L);
		map.put("MQB18", 1800L);
		map.put("MQB30", 3000L);
		//每日礼包
		map.put("xunxian_day1", 100L);
		map.put("xunxian_day3", 300L);
		map.put("xunxian_day6", 600L);
		
		//九州
		map.put("jiuzhou01", 600L);
		map.put("jiuzhou02", 1200L);
		map.put("jiuzhou03", 3000L);
		map.put("jiuzhou04", 6000L);
		map.put("jiuzhou05", 9800L);
		map.put("jiuzhou06", 21800L);
		map.put("jiuzhou07", 31800L);
		map.put("jiuzhou08", 41800L);
		map.put("jiuzhou09", 58800L);
		map.put("jiuzhou10", 99800L);
		//月卡
		map.put("jiuzhou_yueka12", 1200L);
		map.put("jiuzhou_yueka18", 1800L);
		map.put("jiuzhou_yueka30", 3000L);
		map.put("MQB12", 1200L);
		map.put("MQB18", 1800L);
		map.put("MQB30", 3000L);
		//每日礼包
		map.put("jiuzhou_day1", 100L);
		map.put("jiuzhou_day3", 300L);
		map.put("xunxian_day6", 600L);
	}
	
	public String appStoreSaving(Passport passport, String channel, String servername, String receipt, String deviceCode){
		long t=System.currentTimeMillis();
		OrderForm order = new OrderForm();
		java.util.Date cdate = new java.util.Date();
		order.setCreateTime(t);
		order.setPassportId(passport.getId());
		order.setServerName(servername);
		order.setSavingPlatform("AppStore");
		order.setSavingMedium("AppStore");
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
				
				if(isGoSandBox)
				{
					if(!isValid && dataMap.get("status") != null  && ((dataMap.get("status") + "").equals("21007") || (dataMap.get("status") + "").equals("21002")) )
					{
						url = new URL(AppStoreGuojiSavingManager.SANDBOX_RECHARGE_URL);
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
					String chargeId = "";
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
								if(channel != null && channel.equals("APPSTORE_XUNXIAN")){
									chargeId = map2.get(productId);
									order.setChargeValue(chargeId);
								}
								//appstore没有透传，是apple返回的productId来区分function
								if(productId.equals("jiuzhou_yueka12") || productId.equals("jiuzhou_yueka18") || productId.equals("jiuzhou_yueka30") || productId.equals("newxunxian_yueka12") || productId.equals("newxunxian_yueka18") || productId.equals("newxunxian_yueka30")){
									order.setChargeType("购买月卡");
									order.setChargeValue(productId);
								}else if(productId.equals("jiuzhou_day1") || productId.equals("jiuzhou_day3") || productId.equals("jiuzhou_day6") || productId.equals("xunxian_day1") || productId.equals("xunxian_day3") || productId.equals("xunxian_day6")){
									order.setChargeType("每日礼包");
									order.setChargeValue(productId);
								}

								
								order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
								order.setResponseResult(OrderForm.RESPONSE_SUCC);
								OrderFormManager.getInstance().update(order);
								PlatformSavingCenter.logger.warn("[应用商店充值"+(isSandBox?"沙盒":"" )+"订单生成] [成功] [id:"+order.getId()+"] [valuett:"+order.getChargeValue()+"] [type:"+order.getChargeType()+"] [orderID："+orderID+"] [充值类型：AppStore] [支付平台:AppStore] [paymoney:"+addAmount+"] [用户:"+passport.getUserName()+"] [服务器:"+servername+"] [渠道:"+channel+"] [productId:"+productId+"] [chargeId:"+chargeId+"] ["+(System.currentTimeMillis()-t)+"ms]");
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
				} else {
					order.setHandleResult(OrderForm.HANDLE_RESULT_FAILED);
					OrderFormManager.getInstance().update(order);
					PlatformSavingCenter.logger.warn("[应用商店充值"+(isSandBox?"沙盒":"" )+"订单生成] [失败：此交易不合法] [id:"+order.getId()+"] [orderID："+orderID+"] [充值类型：AppStore] [支付平台:AppStore] [paymoney:"+0+"] [用户:"+passport.getUserName()+"] [服务器:"+servername+"] [渠道:"+channel+"] [信息："+result+"] [receipt:"+receipt+"] ["+(System.currentTimeMillis()-t)+"ms]");
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
					PlatformSavingCenter.logger.error("[应用商店充值订单生成2] [失败：更新订单出现异常] [id:"+order.getId()+"] [orderID："+orderId+"] [充值类型：AppStore] [支付平台:AppStore] [paymoney:"+order.getPayMoney()+"] [用户:"+passport.getUserName()+"] [服务器:"+servername+"] [渠道:"+channel+"] [receipt:"+receipt+"] [角色:"+order.getMemo1()+"] ["+(System.currentTimeMillis()-t)+"ms]", e);
				}
				
				PlatformSavingCenter.logger.warn("[应用商店充值订单生成2] [成功] [无角色id传入] [id:"+order.getId()+"] [orderID："+orderId+"] [充值类型：AppStore] [支付平台:AppStore] [paymoney:"+order.getPayMoney()+"] [用户:"+passport.getUserName()+"] [服务器:"+servername+"] [渠道:"+channel+"] [角色:"+order.getMemo1()+"] ["+Arrays.toString(others)+"] ["+(System.currentTimeMillis()-t)+"ms]");
			}
			else
			{
				PlatformSavingCenter.logger.warn("[应用商店充值订单生成2] [失败] [根据订单id没有查到订单] [无角色id传入] [orderid:"+orderId+"]");
			}
			return orderId;
		}
		else
		{
			return "";
		}
	}
	
	public static AppStoreSavingManager getInstance(){
		if(self == null) {
			self = new AppStoreSavingManager();
		}
		return self;
	}
}
