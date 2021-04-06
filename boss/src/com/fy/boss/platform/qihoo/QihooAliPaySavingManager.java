package com.fy.boss.platform.qihoo;

import java.net.URL;
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
import com.fy.boss.tools.JacksonManager;
import com.fy.boss.platform.qihoo.QihooAliPaySavingManager;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.servlet.HttpsUtils;
import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.text.StringUtil;

public class QihooAliPaySavingManager {
	static QihooAliPaySavingManager self;
	
	
	private static String VERIFY_STRING = "";
	//private static String MERCHANT_ID = "20111117360"; //测试用
	public static String MERCHANT_ID = "9090100018"; //正式
	private static String SERVER_ID = "1";
	public static String MD5_KEY = "356c54699b29e468a41241bfe6e58aed";
	public static String SERVICE_NAME = "direct_pay";
	private static String NOTIFY_URL = "http://116.213.192.216:9110/mieshi_game_boss/qihooalipayResult";
	public static String PRODUCT_NAME = "飘渺寻仙曲OL";
	public  static String CHARGE_URL = "https://mpay.360.cn/gateway/do";
	public static Map<String,String> BANK_CODE = new HashMap<String, String>();
	
	
	static
	{
		BANK_CODE.put("移动充值卡", "SZX_CARD");
		BANK_CODE.put("联通一卡付", "LT_CARD");
		BANK_CODE.put("电信充值卡", "DX_CARD");
		BANK_CODE.put("骏网一卡通", "JCARD");
		BANK_CODE.put("支付宝", "MOBILE_ZFB");
	}
	
	
		
	public void initialize(){
		QihooAliPaySavingManager.self=this;
		System.out.println("["+this.getClass().getName()+"] [initialized]");
		PlatformSavingCenter.logger.info("["+this.getClass().getName()+"] [initialized]");	
	}
	
	
	public String aliSaving(Passport passport, int payAmount, 
			 String server,String mobileOs,String userChannel){
		long startTime = System.currentTimeMillis();
		
		//生成订单
		
		//拼接字符串
		//链接360接口url
		//得到返回结果
		//返回消息信息
		
		
		//过滤重复充值
		OrderFormManager orderFormManager = OrderFormManager.getInstance();
		//貌似需要改成同步
			//生成订单
		OrderForm order = new OrderForm();
		order.setCreateTime(new Date().getTime());
		//设置充值平台
		order.setSavingPlatform("360接口支付宝");
		//设置充值介质
		order.setSavingMedium("支付宝");
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
			PlatformSavingCenter.logger.error("[360接口充值订单生成] [失败] [更新订单信息] [失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:支付宝] [充值金额:"+payAmount+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [订单的ID:"+order.getId()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
			return "";
		}
		
		//调用360的充值接口
		LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();
		
		
		params.put("mer_code", MERCHANT_ID+"");
		params.put("mer_trade_code", order.getOrderId());
		params.put("trans_service", SERVICE_NAME);
		params.put("input_cha", "UTF-8");

	
		params.put("notify_url", NOTIFY_URL);
		params.put("return_url", NOTIFY_URL); 
		params.put("product_name", PRODUCT_NAME);
		params.put("rec_amount", Double.valueOf(payAmount/100.0)+"");
		params.put("client_ip", "127.0.0.1");
		params.put("bank_code", QihooAliPaySavingManager.BANK_CODE.get("支付宝"));
		
		
		params.put("sign_type", "MD5");
		params.put("sign", signedUrl(params, MD5_KEY) );
			
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

		String urlStr = CHARGE_URL + "?" + strbuf.toString();
		URL url = null;
		try {
			url = new URL(urlStr);
		} catch (Exception e) {
			PlatformSavingCenter.logger.error("[360接口充值订单生成] [[调用接口失败] [失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:支付宝] [充值金额:"+payAmount+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [url:"+urlStr+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
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
			//针对手机支付
			JacksonManager jk = JacksonManager.getInstance();
			Map info = (Map)jk.jsonDecodeObject(content);
			
			String resCode = (String)info.get("code");
			String paydata = (String)info.get("paydata");
			/*String resCode = (String)info.get("code");
			String resCode = (String)info.get("code");*/
	  
	        
	     //   System.out.println(resMap.get("result")); // 当result=1时提交订单成功，否则是失败的
	        if("success".equalsIgnoreCase(resCode))
	        {
	        	order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
				order.setHandleResultDesp("调用360接口充值接口成功");
				orderFormManager.update(order);
				if(PlatformSavingCenter.logger.isInfoEnabled())
					PlatformSavingCenter.logger.info("[360接口充值订单生成] [成功] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值类型:支付宝] [充值金额:"+payAmount+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [orderForm ID:"+order.getId()+"] [orderId:"+order.getOrderId()+"] [content:"+content+"] [url:"+urlStr+"] [rescode:"+resCode+"] [paydata:"+paydata+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
				return order.getOrderId()+"@@"+paydata;
	        }
				// 调用充值平台的结果，-1-未返回, 0-失败,1-成功  代表发送请求的响应
			else//下单不成功
			{
				// 调用充值平台的结果，-1-未返回, 0-失败,1-成功  代表发送请求的响应
				order.setHandleResult(OrderForm.HANDLE_RESULT_FAILED);
				order.setHandleResultDesp("调用失败:" + content);
				try
				{
					orderFormManager.update(order);
				}
				catch(Exception e) {
					PlatformSavingCenter.logger.error(e);
				}
				PlatformSavingCenter.logger.error("[360接口充值订单生成] [失败：返回调用失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:支付宝] [充值金额:"+payAmount+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [订单的ID:"+order.getId()+"] [url:"+urlStr+"] [content:"+content+"] [加密明文:"+strbuf.toString()+"] [rescode:"+resCode+"] [paydata:"+paydata+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
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
			PlatformSavingCenter.logger.error("[360接口充值订单生成] [失败:调用360接口充值接口失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:支付宝] [充值金额:"+payAmount+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [URL:"+urlStr+"] [content:"+content+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
			return "";//infoMap.get(6010000);
		}
				
	}
	
	public String aliSaving(Passport passport, int payAmount,  String server,String mobileOs,String userChannel,String[]others){
		long t = System.currentTimeMillis();
		
		String orderId = aliSaving(passport, payAmount, server, mobileOs, userChannel);
		
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
				order.setMemo1(others[0]);
				try {
					OrderFormManager.getInstance().update(order);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					PlatformSavingCenter.logger.error("[360接口充值订单生成] [失败] [更新订单] [失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:支付宝] [充值金额:"+payAmount+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [角色名:"+order.getMemo1()+"] [costs:"+(System.currentTimeMillis()-t)+"]",e);
				}
				
				PlatformSavingCenter.logger.warn("[360接口充值订单生成] [成功] [无角色id传入] [id:"+order.getId()+"] [orderID："+orderId+"] [充值类型：支付宝] [paymoney:"+0+"] [用户:"+passport.getUserName()+"] [角色:"+order.getMemo1()+"] ["+(System.currentTimeMillis()-t)+"ms]");
			}
			else
			{
				PlatformSavingCenter.logger.warn("[360接口充值订单生成] [失败] [根据订单id没有查到订单] [无角色id传入] [orderid:"+orderId+"]");
			}
			return orderId;
		}
		else if(!StringUtils.isEmpty(orderId))
		{
			String temporderId = "";
			if(orderId.indexOf("@@") > -1)
			{
				temporderId = orderId.split("@@")[0];
			}
			OrderForm order = OrderFormManager.getInstance().getOrderForm(temporderId);
			
			if(order != null)
			{
				order.setMemo1(others[0]);
				try {
					OrderFormManager.getInstance().update(order);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					PlatformSavingCenter.logger.error("[360接口充值订单生成] [失败：更新订单出现异常] [id:"+order.getId()+"] [orderID："+orderId+"] [充值类型：支付宝] [paymoney:"+0+"] [用户:"+passport.getUserName()+"] [角色:"+order.getMemo1()+"] ["+(System.currentTimeMillis()-t)+"ms]", e);
				}
				
				PlatformSavingCenter.logger.warn("[360接口充值订单生成] [成功] [无角色id传入] [id:"+order.getId()+"] [orderID："+orderId+"] [充值类型：支付宝] [paymoney:"+0+"] [用户:"+passport.getUserName()+"] [角色:"+order.getMemo1()+"] ["+(System.currentTimeMillis()-t)+"ms]");
			}
			else
			{
				PlatformSavingCenter.logger.warn("[360接口充值订单生成] [失败] [根据订单id没有查到订单] [无角色id传入] [orderid:"+orderId+"]");
			}
			return orderId;
		}
		else
		{
			return "";
		}
		
	}
	
	
	
	
	public static String signedUrl(HashMap<String,String> params,String secretkey){
		
		String keys[] = params.keySet().toArray(new String[0]);
		java.util.Arrays.sort(keys);
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < keys.length ; i++){
			String v = params.get(keys[i]);
			sb.append(keys[i]+"="+v + "&");
		}
		String md5Str = sb.toString();
		if(md5Str.length() > 0) {
			md5Str = md5Str.substring(0, md5Str.length()-1);
		}
		md5Str = md5Str + secretkey;
		
		String sign = StringUtil.hash(md5Str);
		
		return sign;
	}
			
	public static QihooAliPaySavingManager getInstance(){
		if(self == null)
		{
			self = new QihooAliPaySavingManager();
		}
		return QihooAliPaySavingManager.self;
	}
	
}
