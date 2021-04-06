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
import com.fy.boss.platform.duoku.DuokuAlipaySavingManager;
import com.fy.boss.platform.duoku.DuokuSavingManager;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.text.StringUtil;

public class DuokuAlipaySavingManager {
	static DuokuAlipaySavingManager self;
	
	
//	public static String chargeUrl = "http://duokoo.baidu.com/openapi/alisdk/gensign";
	public static String chargeUrl = "http://api.m.duoku.com/openapi/alipay/recharge";

	
	public void initialize(){
		DuokuAlipaySavingManager.self=this;
		System.out.println("["+this.getClass().getName()+"] [initialized]");
		PlatformSavingCenter.logger.info("["+this.getClass().getName()+"] [initialized]");	
	}
	
	
	public String cardSaving(Passport passport, String cardtype, int payAmount, String cardno, String cardpass,
			 String server,String mobileOs,String userChannel){
		try
		{
			String chargeType = "多酷支付宝";
			
			long startTime = System.currentTimeMillis();
			String accessToken = "";
			if(cardtype.indexOf("@") > -1)
			{
				String[] strs = cardtype.split("@");
				cardtype = strs[0];
				if(strs.length >= 2)
					accessToken = strs[1];
			}
			
			
			
			//过滤重复充值
			OrderFormManager orderFormManager = OrderFormManager.getInstance();
			//貌似需要改成同步
			//生成订单
			OrderForm order = new OrderForm();
			order.setCreateTime(new Date().getTime());
			//设置充值平台
			order.setSavingPlatform("百度多酷");
			//设置充值介质
			order.setSavingMedium(chargeType);
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
			order.setOrderId("WANGXIAN_BAIDU_"+DateUtil.formatDate(new Date(), "yyyyMMdd") + "-"+ order.getId());
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
		params.put("appkey", DuokuSavingManager.client_id);
		params.put("orderid", order.getOrderId());
		params.put("token", accessToken);
		params.put("amount",order.getPayMoney()/100.0d+"");
		params.put("sign", sign(params));
		
		params.put("goodsname", "飘渺寻仙曲银两");
		params.put("aid",  order.getOrderId());
		
		
		

		URL url = null;
		try {
			url = new URL(chargeUrl);
		} catch (Exception e) {
			PlatformSavingCenter.logger.error("[百度多酷充值订单生成] [调用接口失败] [失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [url:"+chargeUrl+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
			return "";
		}
		Map headers = new HashMap();
		String content = buildParams(params);
//		params.clear();
//		params.put("data", URLEncoder.encode(content,"utf-8"));
//		params.put("client_id", DuokuSavingManager.client_id);
//		params.put("aid", order.getOrderId());
		content = buildParams(params);
		
		if(PlatformSavingCenter.logger.isDebugEnabled())
		{
			PlatformSavingCenter.logger.debug("[百度多酷充值订单生成] [构造参数] [参数串:"+content+"] [clientId:"+DuokuSavingManager.client_id+"] [gameid:"+DuokuSavingManager.game_id+"] [sessionScret:"+DuokuSavingManager.session_secret+"] [chargeUrl:"+chargeUrl+"]");
		}
		
		try
			{
				byte bytes[] = HttpUtils.webPost(url,content.getBytes(), headers, 50000, 60000);
				String encoding = (String)headers.get(HttpUtils.ENCODING);
				Integer code = (Integer)headers.get(HttpUtils.Response_Code);
				String duoku_message = (String)headers.get(HttpUtils.Response_Message);
				
				content = new String(bytes,encoding);
				order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
				order.setHandleResultDesp("调用多酷接口成功");
				orderFormManager.update(order);
				if(PlatformSavingCenter.logger.isInfoEnabled())
					PlatformSavingCenter.logger.info("[百度多酷充值] [调用接口] [成功] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [orderForm ID:"+order.getId()+"] [orderId:"+order.getOrderId()+"] [content:"+content+"] [clientId:"+DuokuSavingManager.client_id+"] [gameid:"+DuokuSavingManager.game_id+"] [sessionScret:--] [chargeUrl:"+chargeUrl+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
				return order.getOrderId() + "@@" + content;
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
				PlatformSavingCenter.logger.error("[百度多酷充值] [调用接口] [失败] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [orderForm ID:"+order.getId()+"] [orderId:"+order.getOrderId()+"] [content:"+content+"] [clientId:"+DuokuSavingManager.client_id+"] [gameid:"+DuokuSavingManager.game_id+"] [sessionScret:"+DuokuSavingManager.session_secret+"] [chargeUrl:"+chargeUrl+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
				return "";//infoMap.get(6010000);
			}
		}
		catch(Exception e)
		{
			PlatformSavingCenter.logger.error("[百度多酷充值] [调用接口] [失败:出现未知异常] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [clientId:"+DuokuSavingManager.client_id+"] [gameid:"+DuokuSavingManager.game_id+"] [sessionScret:"+DuokuSavingManager.session_secret+"] [chargeUrl:"+chargeUrl+"]",e);
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
	
	public static String buildParams2(Map<String,String> params)
	{
		String[] key = params.keySet().toArray(new String[params.size()]);
		
		StringBuffer sb = new StringBuffer();
		int i=0;
		for (String k : key) {
			sb.append(k);
			sb.append("=");
			sb.append(params.get(k));
			sb.append("&");
			i++;
		}
		
		
		return sb.toString().substring(0, sb.length());
	}
	
	public static String sign(LinkedHashMap<String,String> params){
		String values[] = params.values().toArray(new String[0]);
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < values.length ; i++){
			sb.append(values[i]);
		}
		String md5Str = sb.toString()+DuokuSavingManager.session_secret;
		String sign = StringUtil.hash(md5Str).toLowerCase();
		
		if(PlatformSavingCenter.logger.isDebugEnabled())
		{
			PlatformSavingCenter.logger.debug("[百度多酷充值] [生成签名串] [签名前:"+md5Str+"] [签名后:"+sign+"] [session_secret:"+DuokuSavingManager.session_secret+"]");
		}
		
		return sign;
	}
			
	public static DuokuAlipaySavingManager getInstance(){
		if(self == null)
		{
			self = new DuokuAlipaySavingManager();
		}
		return self;
	}
	
	
	public String cardSaving(Passport passport, String cardtype, int payAmount, String cardno, String cardpass,
			 String server,String mobileOs,String userChannel,String[]others){
		long t = System.currentTimeMillis();
		
		String orderId =  cardSaving(passport, cardtype, payAmount, cardno, cardpass, server, mobileOs, userChannel);
		
		if(others == null || others.length <= 0)
		{
			OrderForm order = OrderFormManager.getInstance().getOrderForm(orderId);
			if(order != null)
			{
				order.setMemo1(others[0]);
				if(others.length > 1){
					order.setChargeValue(others[1]);
				}
				if(others.length > 2){
					order.setChargeType(others[2]);
				}
				try {
					OrderFormManager.getInstance().update(order);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					PlatformSavingCenter.logger.error("[百度多酷充值] [失败] [更新订单] [失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [角色名:"+order.getMemo1()+"] [costs:"+(System.currentTimeMillis()-t)+"]",e);
				}
				
				PlatformSavingCenter.logger.warn("[百度多酷充值] [成功] [无角色id传入] [id:"+order.getId()+"] [orderID："+orderId+"] [充值类型："+cardtype+"] [paymoney:"+0+"] [用户:"+passport.getUserName()+"] [角色:"+order.getMemo1()+"] ["+(System.currentTimeMillis()-t)+"ms]");
			}
			else
			{
				PlatformSavingCenter.logger.warn("[百度多酷充值] [失败] [根据订单id没有查到订单] [无角色id传入] [orderid:"+orderId+"]");
			}
			return orderId;
		}
		else if(!StringUtils.isEmpty(orderId))
		{
			String temporderId = "";
			String aliOrderInfo = "";
			if(orderId.indexOf("@@") > -1)
			{
				temporderId = orderId.split("@@")[0];
				aliOrderInfo = orderId.split("@@")[1];
			}
			OrderForm order = OrderFormManager.getInstance().getOrderForm(temporderId);
			if(order != null)
			{
				order.setMemo1(others[0]);
				try {
					OrderFormManager.getInstance().update(order);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					PlatformSavingCenter.logger.error("[百度多酷充值] [失败：更新订单出现异常] [id:"+order.getId()+"] [orderID："+orderId+"] [充值类型："+cardtype+"] [paymoney:"+0+"] [用户:"+passport.getUserName()+"] [角色:"+order.getMemo1()+"] ["+(System.currentTimeMillis()-t)+"ms]", e);
				}
				
				PlatformSavingCenter.logger.warn("[百度多酷充值] [成功] [无角色id传入] [id:"+order.getId()+"] [orderID："+orderId+"] [充值类型："+cardtype+"] [paymoney:"+0+"] [用户:"+passport.getUserName()+"] [角色:"+order.getMemo1()+"] ["+(System.currentTimeMillis()-t)+"ms]");
			}
			else
			{
				PlatformSavingCenter.logger.warn("[百度多酷充值] [失败] [根据订单id没有查到订单] [无角色id传入] [orderid:"+orderId+"]");
			}
			return aliOrderInfo;
		}
		else
		{
			return "";
		}
		
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
