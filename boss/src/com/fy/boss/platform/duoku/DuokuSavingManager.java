package com.fy.boss.platform.duoku;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.downjoy.common.crypto.MessageDigest;
import com.fy.boss.authorize.model.Passport;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.fy.boss.platform.kunlun.KunlunSavingManager;
import com.fy.boss.tools.JacksonManager;
import com.fy.boss.platform.duoku.DuokuSavingManager;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.text.StringUtil;

public class DuokuSavingManager {
	static DuokuSavingManager self;
	
	
	public static String client_id = "e3941454403d6a9b8754dc1bf1351eb3";
	public static String game_id = "118";
	public static String session_secret = "c95b5b9ccfa1c38d022340a12336a6a4";
	public static Map<String, String> cardNo = new HashMap<String,String>();
//	public static String chargeUrl = "http://duokoo.baidu.com/openapi/client/duokoo_card_recharge";
	public static String chargeUrl = "http://sdk.m.duoku.com/openapi/sdk/duokoo_card_recharge";
		
	static
	{
		/**
		 * 1	移动充值卡
2	联通充值卡
3	电信充值卡
4	骏网一卡通
5	盛大一卡通
6	网易一卡通
7	征途卡
8	搜狐卡
9	完美卡
10	Q币卡
11	久游卡
12	天下一卡通
13	纵游一卡通
14	天宏一卡通
15	商联通卡
16	奥斯卡
17	亿卡通

		 */
		cardNo.put("移动充值卡", "1");
		cardNo.put("联通一卡付", "2");
		cardNo.put("电信充值卡", "3");
		cardNo.put("盛大卡", "5");
		cardNo.put("征途卡", "7");
		cardNo.put("骏网一卡通", "4");
		cardNo.put("久游卡", "11");
		cardNo.put("网易卡", "6");
		cardNo.put("完美卡", "9");
		cardNo.put("搜狐卡", "8");
		cardNo.put("支付宝", "101");
	}
	
	public void initialize(){
		DuokuSavingManager.self=this;
		System.out.println("["+this.getClass().getName()+"] [initialized]");
		PlatformSavingCenter.logger.info("["+this.getClass().getName()+"] [initialized]");	
	}
	
	
	public String cardSaving(Passport passport, String cardtype, int payAmount, String cardno, String cardpass,
			 String server,String mobileOs,String userChannel){
		try
		{
			long startTime = System.currentTimeMillis();
			String accessToken = "";
			if(cardtype.indexOf("@") > -1)
			{
				String[] strs = cardtype.split("@");
				cardtype = strs[0];
				if(strs.length >= 2)
					accessToken = strs[1];
			}
			
			cardno = cardno.replaceAll(" ", "").trim();
			cardpass = cardpass.replaceAll(" ", "").trim();
			//过滤重复充值
			OrderFormManager orderFormManager = OrderFormManager.getInstance();
			//貌似需要改成同步
			//生成订单
			OrderForm order = new OrderForm();
			order.setCreateTime(new Date().getTime());
			//设置充值平台
			order.setSavingPlatform("百度多酷");
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
			order.setOrderId(DateUtil.formatDate(new Date(), "yyyyMMdd") + "-" + game_id + "-"+ order.getId());
			try
			{
				orderFormManager.updateOrderForm(order, "orderId");
			}
			catch(Exception e)
			{
				PlatformSavingCenter.logger.error("[百度多酷充值订单生成] [失败] [更新订单信息] [失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [订单的ID:"+order.getId()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
				return "";
			}
			
			/**
			 * client_id
game_id
Aid
access_token
card_num
card_pwd
card_type
pay_num
client_secret
下列参数+ session_secret的MD5码（字母小写）
access_token值+client_id值+card_num值+ card_pwd 值+ card_type值+ pay_num 值+session_secret值(计算时无+号)
			 */
			
		//调用百度多酷的充值接口
		LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();
		
		params.put("access_token", accessToken);
		params.put("client_id", client_id);
		params.put("card_num", cardno);
		params.put("card_pwd", cardpass); //直接调用用户的登陆ip 可酌情修改 
		params.put("card_type", cardNo.get(cardtype));
		params.put("pay_num", String.valueOf(payAmount/100));
		params.put("client_secret", sign(params));
		params.put("order_id", order.getOrderId());
		params.put("game_id", game_id);
		

		URL url = null;
		try {
			url = new URL(chargeUrl);
		} catch (Exception e) {
			PlatformSavingCenter.logger.error("[百度多酷充值订单生成] [调用接口失败] [失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [url:"+chargeUrl+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
			return "";
		}
		Map headers = new HashMap();
		String content = buildParams(params);
		if(PlatformSavingCenter.logger.isDebugEnabled())
		{
			PlatformSavingCenter.logger.debug("[百度多酷充值订单生成] [构造参数] [参数串:"+content+"] [clientId:"+client_id+"] [gameid:"+game_id+"] [sessionScret:"+session_secret+"] [chargeUrl:"+chargeUrl+"]");
		}
		
		try
			{
				byte bytes[] = HttpUtils.webPost(url,content.getBytes(), headers, 50000, 60000);
				String encoding = (String)headers.get(HttpUtils.ENCODING);
				Integer code = (Integer)headers.get(HttpUtils.Response_Code);
				String duoku_message = (String)headers.get(HttpUtils.Response_Message);
				
				content = new String(bytes,encoding);
				/**
				 * 2.6.4 错误返回格式
参数	含义	备注
error_code	错误码	
error_msg	错误信息	
2.6.5 正确返回格式
参数	含义	备注
order_id	充值订单ID	
client_secret	orderid+ session_secret的MD5码（字母小写）	session_secre预先分配合作方

				 */
				
				JacksonManager jacksonManager = JacksonManager.getInstance();
				Map<String,String> resMap = (Map)jacksonManager.jsonDecodeObject(content);
				
				
				//   System.out.println(resMap.get("result")); // 当result=1时提交订单成功，否则是失败的
		        if(!StringUtils.isEmpty(resMap.get("order_id")))
		        {
		        	order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
					order.setHandleResultDesp("调用百度多酷充值接口成功");
					order.setChannelOrderId(resMap.get("order_id"));
					orderFormManager.update(order);
					if(PlatformSavingCenter.logger.isInfoEnabled())
						PlatformSavingCenter.logger.info("[百度多酷充值] [调用接口] [成功] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [orderForm ID:"+order.getId()+"] [orderId:"+order.getOrderId()+"] [content:"+content+"] [clientId:"+client_id+"] [gameid:"+game_id+"] [sessionScret:"+session_secret+"] [chargeUrl:"+chargeUrl+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
					return order.getOrderId();
		        }
					// 调用充值平台的结果，-1-未返回, 0-失败,1-成功  代表发送请求的响应
				else//下单不成功
				{
					// 调用充值平台的结果，-1-未返回, 0-失败,1-成功  代表发送请求的响应
					order.setHandleResult(OrderForm.HANDLE_RESULT_FAILED);
					order.setHandleResultDesp("调用失败:" + resMap.get("error_msg"));
					try
					{
						orderFormManager.update(order);
					}
					catch(Exception e) {
						PlatformSavingCenter.logger.error(e);
					}
					PlatformSavingCenter.logger.error("[百度多酷充值订单生成] [失败：返回调用失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [订单的ID:"+order.getId()+"] [content:"+content+"] [clientId:"+client_id+"] [gameid:"+game_id+"] [sessionScret:"+session_secret+"] [chargeUrl:"+chargeUrl+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					return "";
				}
				
			}
			catch(Exception e)
			{
				order.setHandleResult(OrderForm.HANDLE_RESULT_FAILED);
				order.setHandleResultDesp("调用百度多酷充值接口异常");
				try {
					orderFormManager.update(order);
				} catch (Exception e1) {
					PlatformSavingCenter.logger.error(e1);
				}
				PlatformSavingCenter.logger.error("[百度多酷充值] [调用接口] [失败] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [orderForm ID:"+order.getId()+"] [orderId:"+order.getOrderId()+"] [content:"+content+"] [clientId:"+client_id+"] [gameid:"+game_id+"] [sessionScret:"+session_secret+"] [chargeUrl:"+chargeUrl+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
				return "";//infoMap.get(6010000);
			}
		}
		catch(Exception e)
		{
			PlatformSavingCenter.logger.error("[百度多酷充值] [调用接口] [失败:出现未知异常] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [clientId:"+client_id+"] [gameid:"+game_id+"] [sessionScret:"+session_secret+"] [chargeUrl:"+chargeUrl+"]",e);
			return "";
		}
	}
	
	
	public String cardSaving(Passport passport, String cardtype, int payAmount, String cardno, String cardpass,
			 String server,String mobileOs,String userChannel,String[]others){
		try
		{
			long startTime = System.currentTimeMillis();
			String accessToken = "";
			if(cardtype.indexOf("@") > -1)
			{
				String[] strs = cardtype.split("@");
				cardtype = strs[0];
				if(strs.length >= 2)
					accessToken = strs[1];
			}
			
			cardno = cardno.replaceAll(" ", "").trim();
			cardpass = cardpass.replaceAll(" ", "").trim();
			//过滤重复充值
			OrderFormManager orderFormManager = OrderFormManager.getInstance();
			//貌似需要改成同步
			//生成订单
			OrderForm order = new OrderForm();
			order.setCreateTime(new Date().getTime());
			//设置充值平台
			order.setSavingPlatform("百度多酷");
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
			order.setUserChannel(userChannel);
			if(others.length > 1){
				order.setChargeValue(others[1]);
			}
			if(others.length > 2){
				order.setChargeType(others[2]);
			}
			//先存入到数据库中
			order = orderFormManager.createOrderForm(order);
			//设置订单号
			order.setOrderId(DateUtil.formatDate(new Date(), "yyyyMMdd") + "-" + game_id + "-"+ order.getId());
			try
			{
				orderFormManager.updateOrderForm(order, "orderId");
			}
			catch(Exception e)
			{
				PlatformSavingCenter.logger.error("[百度多酷充值订单生成] [失败] [更新订单信息] [失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [订单的ID:"+order.getId()+"] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
				return "";
			}
			
			/**
			 * client_id
game_id
Aid
access_token
card_num
card_pwd
card_type
pay_num
client_secret
下列参数+ session_secret的MD5码（字母小写）
access_token值+client_id值+card_num值+ card_pwd 值+ card_type值+ pay_num 值+session_secret值(计算时无+号)
			 */
			
		//调用百度多酷的充值接口
		LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();
		
		params.put("access_token", accessToken);
		params.put("client_id", client_id);
		params.put("card_num", cardno);
		params.put("card_pwd", cardpass); //直接调用用户的登陆ip 可酌情修改 
		params.put("card_type", cardNo.get(cardtype));
		params.put("pay_num", String.valueOf(payAmount/100));
		params.put("client_secret", sign(params));
		params.put("order_id", order.getOrderId());
		params.put("game_id", game_id);
		

		URL url = null;
		try {
			url = new URL(chargeUrl);
		} catch (Exception e) {
			PlatformSavingCenter.logger.error("[百度多酷充值订单生成] [调用接口失败] [失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [url:"+chargeUrl+"] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
			return "";
		}
		Map headers = new HashMap();
		String content = buildParams(params);
		if(PlatformSavingCenter.logger.isDebugEnabled())
		{
			PlatformSavingCenter.logger.debug("[百度多酷充值订单生成] [构造参数] [参数串:"+content+"] [clientId:"+client_id+"] [gameid:"+game_id+"] [sessionScret:"+session_secret+"] [chargeUrl:"+chargeUrl+"] [角色id:"+others[0]+"]");
		}
		
		try
			{
				byte bytes[] = HttpUtils.webPost(url,content.getBytes(), headers, 50000, 60000);
				String encoding = (String)headers.get(HttpUtils.ENCODING);
				Integer code = (Integer)headers.get(HttpUtils.Response_Code);
				String duoku_message = (String)headers.get(HttpUtils.Response_Message);
				
				content = new String(bytes,encoding);
				/**
				 * 2.6.4 错误返回格式
参数	含义	备注
error_code	错误码	
error_msg	错误信息	
2.6.5 正确返回格式
参数	含义	备注
order_id	充值订单ID	
client_secret	orderid+ session_secret的MD5码（字母小写）	session_secre预先分配合作方

				 */
				
				JacksonManager jacksonManager = JacksonManager.getInstance();
				Map<String,String> resMap = (Map)jacksonManager.jsonDecodeObject(content);
				
				
				//   System.out.println(resMap.get("result")); // 当result=1时提交订单成功，否则是失败的
		        if(!StringUtils.isEmpty(resMap.get("order_id")))
		        {
		        	order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
					order.setHandleResultDesp("调用百度多酷充值接口成功");
					order.setChannelOrderId(resMap.get("order_id"));
					orderFormManager.update(order);
					if(PlatformSavingCenter.logger.isInfoEnabled())
						PlatformSavingCenter.logger.info("[百度多酷充值] [调用接口] [成功] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [orderForm ID:"+order.getId()+"] [orderId:"+order.getOrderId()+"] [content:"+content+"] [clientId:"+client_id+"] [gameid:"+game_id+"] [sessionScret:"+session_secret+"] [chargeUrl:"+chargeUrl+"] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
					return order.getOrderId();
		        }
					// 调用充值平台的结果，-1-未返回, 0-失败,1-成功  代表发送请求的响应
				else//下单不成功
				{
					// 调用充值平台的结果，-1-未返回, 0-失败,1-成功  代表发送请求的响应
					order.setHandleResult(OrderForm.HANDLE_RESULT_FAILED);
					order.setHandleResultDesp("调用失败:" + resMap.get("error_msg"));
					try
					{
						orderFormManager.update(order);
					}
					catch(Exception e) {
						PlatformSavingCenter.logger.error(e);
					}
					PlatformSavingCenter.logger.error("[百度多酷充值订单生成] [失败：返回调用失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [订单的ID:"+order.getId()+"] [content:"+content+"] [clientId:"+client_id+"] [gameid:"+game_id+"] [sessionScret:"+session_secret+"] [chargeUrl:"+chargeUrl+"] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
					return "";
				}
				
			}
			catch(Exception e)
			{
				order.setHandleResult(OrderForm.HANDLE_RESULT_FAILED);
				order.setHandleResultDesp("调用百度多酷充值接口异常");
				try {
					orderFormManager.update(order);
				} catch (Exception e1) {
					PlatformSavingCenter.logger.error(e1);
				}
				PlatformSavingCenter.logger.error("[百度多酷充值] [调用接口] [失败] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [orderForm ID:"+order.getId()+"] [orderId:"+order.getOrderId()+"] [content:"+content+"] [clientId:"+client_id+"] [gameid:"+game_id+"] [sessionScret:"+session_secret+"] [chargeUrl:"+chargeUrl+"] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
				return "";//infoMap.get(6010000);
			}
		}
		catch(Exception e)
		{
			PlatformSavingCenter.logger.error("[百度多酷充值] [调用接口] [失败:出现未知异常] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [clientId:"+client_id+"] [gameid:"+game_id+"] [sessionScret:"+session_secret+"] [chargeUrl:"+chargeUrl+"] [角色id:"+others[0]+"]",e);
			return "";
		}
	}
	
	
	
	
	public static String buildParams(Map<String,String> params)
	{
		String[] key = params.keySet().toArray(new String[params.size()]);

		StringBuffer sb = new StringBuffer();
		for (String k : key) {
			sb.append(k);
			sb.append("=");
			sb.append(params.get(k));
			sb.append("&");
		}
		
		
		return sb.toString().substring(0, sb.length() - 1);
	}
	
	public static String sign(LinkedHashMap<String,String> params){
		String values[] = params.values().toArray(new String[0]);
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < values.length ; i++){
			sb.append(values[i]);
		}
		String md5Str = sb.toString()+session_secret;
		String sign = StringUtil.hash(md5Str).toLowerCase();
		
		if(PlatformSavingCenter.logger.isDebugEnabled())
		{
			PlatformSavingCenter.logger.debug("[百度多酷充值] [生成签名串] [签名前:"+md5Str+"] [签名后:"+sign+"] [session_secret:"+session_secret+"]");
		}
		
		return sign;
	}
			
	public static DuokuSavingManager getInstance(){
		if(self == null)
		{
			self = new DuokuSavingManager();
		}
		return self;
	}
	
//	public static void main(String args[]) {
//		//String s = "mid=261&gid=6&sid=1&uif=tom185622&utp=1&uip=116.213.142.183&eif=20120618-261-1100000000000512558&amount=20&timestamp=20120618184124";
//		String s = "mid=261&gid=6&sid=1&uif=%E6%96%AD%E6%9C%A8&utp=1&uip=116.213.142.183&eif=20120618-261-1100000000000512463&amount=20&timestamp=20120618181949";
//		String mkey = "pOa#h|18";
//		String ss[] = s.split("&");
//		StringBuffer sb = new StringBuffer();
//		for(String sss : ss) {
//			String as[] = sss.split("=");
//			try {
//				String v = as[1];
//				if(as[0].equals("uif") || as[0].equals("cardno") || as[0].equals("cardpwd")) {
//					v = java.net.URLDecoder.decode(as[1], "UTF-8");
//				}
//				sb.append(as[0] + "=" + v  + "&");
//			} catch (UnsupportedEncodingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		sb.append("merchantkey=" + mkey);
//		String verstring = StringUtil.hash(sb.toString()).toLowerCase();
//		System.out.println("@@@@@" + sb.toString() + "@@@@");
//		System.out.println(verstring);
//		
//		//System.out.println(DES.encode("", DES_KEY).replaceAll("\r\n", ""));
//	}
}
