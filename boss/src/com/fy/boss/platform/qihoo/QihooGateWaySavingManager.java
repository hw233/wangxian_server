package com.fy.boss.platform.qihoo;

import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.fy.boss.platform.qihoo.QihooAliPaySavingManager;
import com.fy.boss.platform.qihoo.QihooGateWaySavingManager;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.servlet.HttpsUtils;
import com.xuanzhi.tools.text.DateUtil;

public class QihooGateWaySavingManager {
	static QihooGateWaySavingManager self;
	
	
	private static String VERIFY_STRING = "";
	private static String SERVER_ID = "1";
	private static String NOTIFY_URL = "http://116.213.192.216:9110/mieshi_game_boss/qihoojiekouResult";

	public void initialize(){
		QihooGateWaySavingManager.self=this;
		System.out.println("["+this.getClass().getName()+"] [initialized]");
		PlatformSavingCenter.logger.info("["+this.getClass().getName()+"] [initialized]");	
	}
	
	
	public String cardSaving(Passport passport, String cardtype, int payAmount, String cardno, String cardpass,
			 String server,String mobileOs,String userChannel){
		long startTime = System.currentTimeMillis();
		
		//生成订单
		
		//拼接字符串
		//链接360接口url
		//得到返回结果
		//返回消息信息
		
		try {
			cardno = cardno.replaceAll(" ", "").trim();
			cardpass = cardpass.replaceAll(" ", "").trim();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		//过滤重复充值
		OrderFormManager orderFormManager = OrderFormManager.getInstance();
		//貌似需要改成同步
			//生成订单
		OrderForm order = new OrderForm();
		order.setCreateTime(new Date().getTime());
		//设置充值平台
		order.setSavingPlatform("360接口充值卡");
		//设置充值介质
		order.setSavingMedium(cardtype);
		order.setMediumInfo(cardno);
		//设置充值人
		order.setPassportId(passport.getId());
		//设置商品数量
		order.setGoodsCount(1);
		//设置消费金额
		order.setPayMoney(payAmount);
		//设置serverName
		order.setServerName(server);
		order.setHandleResult(OrderForm.HANDLE_RESULT_NOBACK);
		order.setHandleResultDesp("");
		//设置订单responseResult
		order.setResponseResult(OrderForm.RESPONSE_NOBACK);
		order.setResponseDesp("未返回充值结果");
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
		order.setOrderId("qihoo"+"-"+DateUtil.formatDate(new Date(), "yyyyMMdd")  + "-"+ order.getId());
		try
		{
			orderFormManager.updateOrderForm(order, "orderId");
		}
		catch(Exception e)
		{
			PlatformSavingCenter.logger.error("[360接口充值订单生成] [失败] [更新订单信息] [失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [订单的ID:"+order.getId()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
			return "";
		}

		//调用360的充值接口
		LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();
		
		
		params.put("mer_code", QihooAliPaySavingManager.MERCHANT_ID+"");
		params.put("mer_trade_code", order.getOrderId());
		params.put("trans_service", QihooAliPaySavingManager.SERVICE_NAME);
		params.put("input_cha", "UTF-8");

	
		params.put("notify_url", NOTIFY_URL);
		params.put("return_url", NOTIFY_URL); //直接调用用户的登陆ip 可酌情修改 
		params.put("product_name", QihooAliPaySavingManager.PRODUCT_NAME);
		params.put("rec_amount", Double.valueOf(payAmount/100.0)+"");
		params.put("client_ip", "127.0.0.1");
		params.put("bank_code", QihooAliPaySavingManager.BANK_CODE.get(cardtype));
		params.put("card_number", cardno);
		params.put("card_pwd", cardpass);
		params.put("sign_type", "MD5");
		params.put("sign", QihooAliPaySavingManager.signedUrl(params, QihooAliPaySavingManager.MD5_KEY) );
			
		//拼请求查询字符串
		StringBuffer strbuf = new StringBuffer();
		Iterator<String> it = params.keySet().iterator();
		int i = 0;
		while(it.hasNext())
		{
			String key = it.next();
			if(i == 0)
			{
				strbuf.append(key);
				strbuf.append("=");
				strbuf.append(params.get(key));
			}
			else
			{
				strbuf.append("&");
				strbuf.append(key);
				strbuf.append("=");
				strbuf.append(params.get(key));
			}
			i++;
		}

		String urlStr = QihooAliPaySavingManager.CHARGE_URL + "?" + strbuf.toString();
		URL url = null;
		try {
			url = new URL(urlStr);
		} catch (Exception e) {
			PlatformSavingCenter.logger.error("[360接口充值订单生成] [[调用接口失败] [失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [url:"+urlStr+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
			return "";
		}
		Map headers = new HashMap();
		String content = "";
		try
		{
			byte bytes[] = HttpsUtils.webPost(url,content.getBytes(), headers, 50000, 60000);
			String encoding = (String)headers.get(HttpUtils.ENCODING);
			Integer code = (Integer)headers.get(HttpUtils.Response_Code);
			String qihoo_message = (String)headers.get(HttpUtils.Response_Message);
			
			content = new String(bytes,encoding);
	        // 解释提交订单结果
			//针对充值卡应答
	        String []tmp=content.trim().split(":");
	  
	        
	     //   System.out.println(resMap.get("result")); // 当result=1时提交订单成功，否则是失败的
	        if("success".equalsIgnoreCase(tmp[0]))
	        {
	        	order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
				order.setHandleResultDesp("调用360接口充值接口成功");
				orderFormManager.update(order);
				if(PlatformSavingCenter.logger.isInfoEnabled())
					PlatformSavingCenter.logger.info("[360接口充值订单生成] [成功] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [orderForm ID:"+order.getId()+"] [orderId:"+order.getOrderId()+"] [content:"+content+"] [url:"+urlStr+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
				return order.getOrderId();
	        }
				// 调用充值平台的结果，-1-未返回, 0-失败,1-成功  代表发送请求的响应
			else//下单不成功
			{
				// 调用充值平台的结果，-1-未返回, 0-失败,1-成功  代表发送请求的响应
				order.setHandleResult(OrderForm.HANDLE_RESULT_FAILED);
				if(content != null)
				{
					content = new String(content.getBytes("GBK"),"UTF-8");
				}
				order.setHandleResultDesp("调用失败:" + content);
				try
				{
					orderFormManager.update(order);
				}
				catch(Exception e) {
					PlatformSavingCenter.logger.error(e);
				}
				PlatformSavingCenter.logger.error("[360接口充值订单生成] [失败：返回调用失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [订单的ID:"+order.getId()+"] [url:"+urlStr+"] [content:"+content+"] [加密明文:"+strbuf.toString()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
				return "";
			}
			
		}
		catch(Exception e)
		{
			order.setHandleResult(OrderForm.HANDLE_RESULT_FAILED);
			order.setHandleResultDesp("调用360接口充值接口异常");
			try {
				orderFormManager.update(order);
			} catch (Exception e1) {
				PlatformSavingCenter.logger.error(e1);
			}
			PlatformSavingCenter.logger.error("[360接口充值订单生成] [失败:调用360接口充值接口失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [URL:"+urlStr+"] [content:"+content+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
			return "";//infoMap.get(6010000);
		}
				
	}
	
	public String cardSaving(Passport passport, String cardtype, int payAmount, String cardno, String cardpass,
			 String server,String mobileOs,String userChannel,String[]others){
		long startTime = System.currentTimeMillis();
		
		//生成订单
		
		//拼接字符串
		//链接360接口url
		//得到返回结果
		//返回消息信息
		
		try {
			cardno = cardno.replaceAll(" ", "").trim();
			cardpass = cardpass.replaceAll(" ", "").trim();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		//过滤重复充值
		OrderFormManager orderFormManager = OrderFormManager.getInstance();
		//貌似需要改成同步
			//生成订单
		OrderForm order = new OrderForm();
		order.setCreateTime(new Date().getTime());
		//设置充值平台
		order.setSavingPlatform("360接口充值卡");
		//设置充值介质
		order.setSavingMedium(cardtype);
		order.setMediumInfo(cardno);
		//设置充值人
		order.setPassportId(passport.getId());
		//设置商品数量
		order.setGoodsCount(1);
		//设置消费金额
		order.setPayMoney(payAmount);
		//设置serverName
		order.setServerName(server);
		order.setHandleResult(OrderForm.HANDLE_RESULT_NOBACK);
		order.setHandleResultDesp("");
		//设置订单responseResult
		order.setResponseResult(OrderForm.RESPONSE_NOBACK);
		order.setResponseDesp("未返回充值结果");
		//设置是否通知游戏服状态 设为false
		order.setNotified(false);
		//设置通知游戏服是否成功 设为false
		order.setNotifySucc(false);
		//在备注中存入充值平台
		order.setMemo1(others[0]);
		if(others.length > 1){
			order.setChargeValue(others[1]);
		}
		if(others.length > 2){
			order.setChargeType(others[2]);
		}
		order.setUserChannel(userChannel);
		//先存入到数据库中
		order = orderFormManager.createOrderForm(order);
		//设置订单号
		order.setOrderId("qihoo"+"-"+DateUtil.formatDate(new Date(), "yyyyMMdd")  + "-"+ order.getId());
		try
		{
			orderFormManager.updateOrderForm(order, "orderId");
		}
		catch(Exception e)
		{
			PlatformSavingCenter.logger.error("[360接口充值订单生成] [失败] [更新订单信息] [失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [订单的ID:"+order.getId()+"] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
			return "";
		}

		//调用360的充值接口
		LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();
		
		
		params.put("mer_code", QihooAliPaySavingManager.MERCHANT_ID+"");
		params.put("mer_trade_code", order.getOrderId());
		params.put("trans_service", QihooAliPaySavingManager.SERVICE_NAME);
		params.put("input_cha", "UTF-8");

	
		params.put("notify_url", NOTIFY_URL);
		params.put("return_url", NOTIFY_URL); //直接调用用户的登陆ip 可酌情修改 
		params.put("product_name", QihooAliPaySavingManager.PRODUCT_NAME);
		params.put("rec_amount", Double.valueOf(payAmount/100.0)+"");
		params.put("client_ip", "127.0.0.1");
		params.put("bank_code", QihooAliPaySavingManager.BANK_CODE.get(cardtype));
		params.put("card_number", cardno);
		params.put("card_pwd", cardpass);
		params.put("sign_type", "MD5");
		params.put("sign", QihooAliPaySavingManager.signedUrl(params, QihooAliPaySavingManager.MD5_KEY) );
			
		//拼请求查询字符串
		StringBuffer strbuf = new StringBuffer();
		Iterator<String> it = params.keySet().iterator();
		int i = 0;
		while(it.hasNext())
		{
			String key = it.next();
			if(i == 0)
			{
				strbuf.append(key);
				strbuf.append("=");
				strbuf.append(params.get(key));
			}
			else
			{
				strbuf.append("&");
				strbuf.append(key);
				strbuf.append("=");
				strbuf.append(params.get(key));
			}
			i++;
		}

		String urlStr = QihooAliPaySavingManager.CHARGE_URL + "?" + strbuf.toString();
		URL url = null;
		try {
			url = new URL(urlStr);
		} catch (Exception e) {
			PlatformSavingCenter.logger.error("[360接口充值订单生成] [[调用接口失败] [失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [url:"+urlStr+"] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
			return "";
		}
		Map headers = new HashMap();
		String content = "";
		try
		{
			byte bytes[] = HttpsUtils.webPost(url,content.getBytes(), headers, 50000, 60000);
			String encoding = (String)headers.get(HttpUtils.ENCODING);
			Integer code = (Integer)headers.get(HttpUtils.Response_Code);
			String qihoo_message = (String)headers.get(HttpUtils.Response_Message);
			
			content = new String(bytes,encoding);
	        // 解释提交订单结果
			//针对充值卡应答
	        String []tmp=content.trim().split(":");
	  
	        
	     //   System.out.println(resMap.get("result")); // 当result=1时提交订单成功，否则是失败的
	        if("success".equalsIgnoreCase(tmp[0]))
	        {
	        	order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
				order.setHandleResultDesp("调用360接口充值接口成功");
				orderFormManager.update(order);
				if(PlatformSavingCenter.logger.isInfoEnabled())
					PlatformSavingCenter.logger.info("[360接口充值订单生成] [成功] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [orderForm ID:"+order.getId()+"] [orderId:"+order.getOrderId()+"] [content:"+content+"] [url:"+urlStr+"] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
				return order.getOrderId();
	        }
				// 调用充值平台的结果，-1-未返回, 0-失败,1-成功  代表发送请求的响应
			else//下单不成功
			{
				// 调用充值平台的结果，-1-未返回, 0-失败,1-成功  代表发送请求的响应
				order.setHandleResult(OrderForm.HANDLE_RESULT_FAILED);
				if(content != null)
				{
					content = new String(content.getBytes("GBK"),"UTF-8");
				}
				order.setHandleResultDesp("调用失败:" + content);
				try
				{
					orderFormManager.update(order);
				}
				catch(Exception e) {
					PlatformSavingCenter.logger.error(e);
				}
				PlatformSavingCenter.logger.error("[360接口充值订单生成] [失败：返回调用失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [订单的ID:"+order.getId()+"] [url:"+urlStr+"] [content:"+content+"] [加密明文:"+strbuf.toString()+"] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
				return "";
			}
			
		}
		catch(Exception e)
		{
			order.setHandleResult(OrderForm.HANDLE_RESULT_FAILED);
			order.setHandleResultDesp("调用360接口充值接口异常");
			try {
				orderFormManager.update(order);
			} catch (Exception e1) {
				PlatformSavingCenter.logger.error(e1);
			}
			PlatformSavingCenter.logger.error("[360接口充值订单生成] [失败:调用360接口充值接口失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [URL:"+urlStr+"] [content:"+content+"] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
			return "";//infoMap.get(6010000);
		}
				
	}
	
	
			
	public static QihooGateWaySavingManager getInstance(){
		if(self == null)
		{
			self = new QihooGateWaySavingManager();
		}
		return QihooGateWaySavingManager.self;
	}
	
}
