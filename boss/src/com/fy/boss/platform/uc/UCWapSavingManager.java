package com.fy.boss.platform.uc;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.node.ObjectNode;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.fy.boss.tools.JacksonManager;
import com.fy.boss.platform.uc.UCWapSavingManager;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.text.StringUtil;

public class UCWapSavingManager {
	
	public static String publicKey = "K3JA0eJpt4TYO4OMM5jDtxIP1FPRuNpMCNt7id80SsngmRThbzDuG5BpjVwMZiwa";
	public static String payType_支付宝 = "201";
	public static String GoodsID_IOS = "141954";
	public static String GoodsID_ANDROID = "141953";
	public static String callBackUrl = "http://wxb.sqgames.net/mieshi_game_boss/ucWapResult";
	public static String returnBackUrl = "http://wxb.sqgames.net/mieshi_game_boss/ucback.jsp";
	public static String service = "uc.pay.wap.order.create";
	public static String version  = "1.0";
	public static String storeID = "1419";
	public static String tokenRetrieveUrl = "https://paycenter.uc.cn/api.htm";
	
	public static UCWapSavingManager self;

	public void initialize(){
		UCWapSavingManager.self=this;
		System.out.println("["+this.getClass().getName()+"] [initialized]");
		PlatformSavingCenter.logger.info("["+this.getClass().getName()+"] [initialized]");	
	}
	
	public String wapSaving(Passport passport, int payAmount, String server,String mobileOs, String userChannel) {
		long startTime = System.currentTimeMillis();
		
		OrderFormManager orderFormManager = OrderFormManager.getInstance();
		//生成订单
		OrderForm order = new OrderForm();
		order.setCreateTime(new Date().getTime());
		//设置充值平台
		order.setSavingPlatform("UCWAP支付");
		//设置充值介质
		order.setSavingMedium("UCWap支付");
		order.setMediumInfo("");
		//设置充值人
		order.setPassportId(passport.getId());
		//设置商品数量
		order.setGoodsCount(0);
		//设置消费金额
		order.setPayMoney(payAmount);
		//设置serverName
		order.setServerName(server);
		order.setHandleResultDesp("");
		//设置订单responseResult
		order.setResponseResult(OrderForm.RESPONSE_NOBACK);
		order.setResponseDesp("");
		//设置是否通知游戏服状态 设为false
		order.setNotified(false);
		//设置通知游戏服是否成功 设为false
		order.setNotifySucc(false);
		//在备注中存入充值平台
		order.setMemo1(mobileOs);
		order.setUserChannel(userChannel);
			
		//先存入到数据库中
		order = orderFormManager.createOrderForm(order);
		
		order.setOrderId(DateUtil.formatDate(new Date(), "yyyyMMdd") + "-"+storeID+"-"+ order.getId());
		
		try {
			orderFormManager.update(order);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			PlatformSavingCenter.logger.warn("[生成UCWap订单失败] [更新订单失败] [orderId:"+order.getOrderId()+"] [username:"+passport.getUserName()+"] [channel:"+userChannel+"] [payAmount:"+payAmount+"] [server:"+server+"] [os:"+mobileOs+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			return "0@@failed";
		}
			
		String token = getPayToken(order.getOrderId(), payAmount, mobileOs);
		
		if(token != null) {
			order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
			try {
				orderFormManager.update(order);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				PlatformSavingCenter.logger.warn("[生成UCWap订单失败] [更新订单失败] [orderId:"+order.getOrderId()+"] [username:"+passport.getUserName()+"] [channel:"+userChannel+"] [payAmount:"+payAmount+"] [server:"+server+"] [os:"+mobileOs+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
				return "0@@failed";
			}
			PlatformSavingCenter.logger.info("[生成UCWap订单成功] [orderId:"+order.getOrderId()+"] [token:"+token+"] [username:"+passport.getUserName()+"] [channel:"+userChannel+"] [payAmount:"+payAmount+"] [server:"+server+"] [os:"+mobileOs+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			return order.getId() +"@@https://paycenter.uc.cn/pay.htm?token=" + token;
		} else {
			PlatformSavingCenter.logger.warn("[生成UCWap订单失败] [token获取失败] [orderId:"+order.getOrderId()+"] [username:"+passport.getUserName()+"] [channel:"+userChannel+"] [payAmount:"+payAmount+"] [server:"+server+"] [os:"+mobileOs+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			return order.getId() +"@@failed";
		}
	}
	
	public String wapSaving(Passport passport, int payAmount, String server,String mobileOs, String userChannel,String[]others) {
		long startTime = System.currentTimeMillis();
		
		OrderFormManager orderFormManager = OrderFormManager.getInstance();
		//生成订单
		OrderForm order = new OrderForm();
		order.setCreateTime(new Date().getTime());
		//设置充值平台
		order.setSavingPlatform("UCWAP支付");
		//设置充值介质
		order.setSavingMedium("UCWap支付");
		order.setMediumInfo("");
		//设置充值人
		order.setPassportId(passport.getId());
		//设置商品数量
		order.setGoodsCount(0);
		//设置消费金额
		order.setPayMoney(payAmount);
		//设置serverName
		order.setServerName(server);
		order.setHandleResultDesp("");
		//设置订单responseResult
		order.setResponseResult(OrderForm.RESPONSE_NOBACK);
		order.setResponseDesp("");
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
		
		order.setOrderId(DateUtil.formatDate(new Date(), "yyyyMMdd") + "-"+storeID+"-"+ order.getId());
		
		try {
			orderFormManager.update(order);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			PlatformSavingCenter.logger.warn("[生成UCWap订单失败] [更新订单失败] [orderId:"+order.getOrderId()+"] [username:"+passport.getUserName()+"] [channel:"+userChannel+"] [payAmount:"+payAmount+"] [server:"+server+"] [os:"+mobileOs+"] [角色id:"+others[0]+"] [others:"+(others==null?"nul":Arrays.toString(others))+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			return "0@@failed";
		}
			
		String token = getPayToken(order.getOrderId(), payAmount, mobileOs);
		
		if(token != null) {
			order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
			try {
				orderFormManager.update(order);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				PlatformSavingCenter.logger.warn("[生成UCWap订单失败] [更新订单失败] [orderId:"+order.getOrderId()+"] [username:"+passport.getUserName()+"] [channel:"+userChannel+"] [payAmount:"+payAmount+"] [server:"+server+"] [os:"+mobileOs+"] [角色id:"+others[0]+"] [others:"+(others==null?"nul":Arrays.toString(others))+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
				return "0@@failed";
			}
			PlatformSavingCenter.logger.info("[生成UCWap订单成功] [orderId:"+order.getOrderId()+"] [token:"+token+"] [username:"+passport.getUserName()+"] [channel:"+userChannel+"] [payAmount:"+payAmount+"] [server:"+server+"] [os:"+mobileOs+"] [角色id:"+others[0]+"] [others:"+(others==null?"nul":Arrays.toString(others))+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			return order.getId() +"@@https://paycenter.uc.cn/pay.htm?token=" + token;
		} else {
			PlatformSavingCenter.logger.warn("[生成UCWap订单失败] [token获取失败] [orderId:"+order.getOrderId()+"] [username:"+passport.getUserName()+"] [channel:"+userChannel+"] [payAmount:"+payAmount+"] [server:"+server+"] [os:"+mobileOs+"] [角色id:"+others[0]+"] [others:"+(others==null?"nul":Arrays.toString(others))+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			return order.getId() +"@@failed";
		}
	}
	
	
	
	public String getPayToken(String orderId, int payAmount, String mobileOs) {
		long startTime = System.currentTimeMillis();
		JacksonManager jm = JacksonManager.getInstance();
		ObjectNode node = jm.createObjectNode();
		node.put("pay_type", payType_支付宝);
		node.put("order_id", orderId);
		node.put("order_amt", payAmount/100 +".00");
		if(mobileOs.equalsIgnoreCase("ios")) {
			node.put("goods_id", GoodsID_IOS);
		} else {
			node.put("goods_id", GoodsID_ANDROID);
		}
		node.put("notify_url", callBackUrl);
		node.put("back_url", returnBackUrl);
		String req_data = jm.toJson(node);
		HashMap<String, String> map = new HashMap<String,String>();
		map.put("service", service);
		map.put("version", version);
		map.put("store_id", storeID);
		map.put("secure_mode", "MD5");
		map.put("req_data", req_data);
		String signedUrl = signedUrl(map, publicKey);
		signedUrl = tokenRetrieveUrl + "?" + signedUrl;
		//进行请求
		HashMap headers = new HashMap();
		String content =  "";
		try {
			byte bytes[] = HttpUtils.webGet(new java.net.URL(signedUrl), headers, 60000, 60000);
			String encoding = (String)headers.get(HttpUtils.ENCODING);
			Integer code = (Integer)headers.get(HttpUtils.Response_Code);
			String message = (String)headers.get(HttpUtils.Response_Message);
			content = new String(bytes,encoding);
		} catch (Exception e) {
			PlatformSavingCenter.logger.info("[调用UCWap支付生成订单接口] [失败:请求时发生异常] ["+signedUrl+"] ["+(System.currentTimeMillis()-startTime)+"ms]", e);
			return null;
		}
		//对content进行解析
		String rsp_data = null;
		String token = null;
		try {
			Map map2 =(Map)jm.jsonDecodeObject(content);
			String status=  (String)map2.get("status");
			if(!status.equals("ok")) {
				PlatformSavingCenter.logger.info("[调用UCWap支付生成订单接口] [失败:获得token失败] [rsp_data:"+rsp_data+"] ["+signedUrl+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
				return null;
			}
			rsp_data = (String)map2.get("rsp_data");
			rsp_data = rsp_data.replaceAll("\\\\", "");
			Map map3 =(Map)jm.jsonDecodeObject(rsp_data);	
			token = (String)map3.get("token");
		} catch(Exception e) {
			PlatformSavingCenter.logger.info("[调用UCWap支付生成订单接口] [失败:解析rspdata时出错] [rsp_data:"+rsp_data+"] ["+signedUrl+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]", e);
			return null;
		}
		if(token == null) {
			PlatformSavingCenter.logger.info("[调用UCWap支付生成订单接口] [失败:解析rspdata获得token失败] [rsp_data:"+rsp_data+"] ["+signedUrl+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
		}
		return token;
	}
	
	private String signedUrl(HashMap<String,String> params,String secretkey){
		
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
		
		sb = new StringBuffer();
		for(int i = 0 ; i < keys.length ; i++){
			String v = params.get(keys[i]);
			sb.append(keys[i]+"="+java.net.URLEncoder.encode(v)+"&");
		}
		sb.append("sign="+sign);
		return sb.toString();
	}
	
	public static UCWapSavingManager getInstance(){
		return UCWapSavingManager.self;
	}
}
