package com.fy.boss.platform.wapalipay;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.fy.boss.platform.wapalipay.AlipayWapSavingManager;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.text.StringUtil;
import com.xuanzhi.tools.text.XmlUtil;

public class AlipayWapSavingManager {
	
	public static String publicKey = "hblcdvmjli5zpx4srw7wqigxmdoqs5r3";
	public static String subject_name="飘渺寻仙曲银两";
	public static final String sellerID = "guoxiaoying@sqage.com";
	public static final String partnerID = "2088701002800853";
	public static final String notifyUrl = "http://116.213.192.216:9110/mieshi_game_boss/alipayWapResult";
//	public static final String notifyUrl = "http://124.248.40.21:9110/mieshi_game_boss/alipayWapResult";
	public static String service = "alipay.wap.trade.create.direct";
	public static String contentformat = "xml";
	public static String version  = "2.0";
	public static String tokenRetrieveUrl = "http://wappaygw.alipay.com/service/rest.htm";
	public static String payUrl = "http://wappaygw.alipay.com/service/rest.htm";
	
	
	
	
	
	public static AlipayWapSavingManager self;

	public void initialize(){
		AlipayWapSavingManager.self=this;
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
		order.setSavingPlatform("Wap支付宝");
		//设置充值介质
		order.setSavingMedium("Wap支付宝");
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
		
		order.setOrderId(DateUtil.formatDate(new Date(), "yyyyMMdd") + order.getId());
		
		try {
			orderFormManager.update(order);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			PlatformSavingCenter.logger.warn("[生成wap支付宝订单失败] [更新订单失败] [orderId:"+order.getOrderId()+"] [username:"+passport.getUserName()+"] [channel:"+userChannel+"] [payAmount:"+payAmount+"] [server:"+server+"] [os:"+mobileOs+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			return "0@@failed";
		}
			
		String token = null;
		try {
			token = getPayToken(order.getOrderId(), payAmount, mobileOs);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(token != null) {
			order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
			try {
				orderFormManager.update(order);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				PlatformSavingCenter.logger.warn("[生成wap支付宝订单失败] [更新订单失败] [orderId:"+order.getOrderId()+"] [username:"+passport.getUserName()+"] [channel:"+userChannel+"] [payAmount:"+payAmount+"] [server:"+server+"] [os:"+mobileOs+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
				return "0@@failed";
			}
			
			String returnUrl = "";
			try {
				returnUrl = buildPayUrl(token);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				PlatformSavingCenter.logger.warn("[生成wap支付宝订单失败] [构造返回url失败] ["+token+"] ["+returnUrl+"]",e);
				return "";
			}
			
			PlatformSavingCenter.logger.info("[生成wap支付宝订单成功] [orderId:"+order.getOrderId()+"] [token:"+token+"] [username:"+passport.getUserName()+"] [channel:"+userChannel+"] [payAmount:"+payAmount+"] [server:"+server+"] [os:"+mobileOs+"] [returnurl:"+returnUrl+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			return returnUrl;
		} else {
			PlatformSavingCenter.logger.warn("[生成wap支付宝订单失败] [token获取失败] [orderId:"+order.getOrderId()+"] [username:"+passport.getUserName()+"] [channel:"+userChannel+"] [payAmount:"+payAmount+"] [server:"+server+"] [os:"+mobileOs+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
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
		order.setSavingPlatform("Wap支付宝");
		//设置充值介质
		order.setSavingMedium("Wap支付宝");
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
		
		order.setOrderId(DateUtil.formatDate(new Date(), "yyyyMMdd") +"-"+ order.getId());
		
		try {
			orderFormManager.update(order);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			PlatformSavingCenter.logger.warn("[生成wap支付宝订单失败] [更新订单失败] [orderId:"+order.getOrderId()+"] [username:"+passport.getUserName()+"] [channel:"+userChannel+"] [payAmount:"+payAmount+"] [server:"+server+"] [os:"+mobileOs+"] [角色id:"+others[0]+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			return "0@@failed";
		}
			
		String token = null;
		try {
			token = getPayToken(order.getOrderId(), payAmount, mobileOs);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(token != null) {
			order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
			try {
				orderFormManager.update(order);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				PlatformSavingCenter.logger.warn("[生成wap支付宝订单失败] [更新订单失败] [orderId:"+order.getOrderId()+"] [username:"+passport.getUserName()+"] [channel:"+userChannel+"] [payAmount:"+payAmount+"] [server:"+server+"] [os:"+mobileOs+"] [角色id:"+others[0]+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
				return "0@@failed";
			}
			String returnUrl = "";
			try {
				returnUrl = buildPayUrl(token);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				PlatformSavingCenter.logger.warn("[生成wap支付宝订单失败] [构造返回url失败] ["+token+"] ["+returnUrl+"]",e);
				return "";
			}
			
			PlatformSavingCenter.logger.info("[生成wap支付宝订单成功] [orderId:"+order.getOrderId()+"] [token:"+token+"] [username:"+passport.getUserName()+"] [channel:"+userChannel+"] [payAmount:"+payAmount+"] [server:"+server+"] [os:"+mobileOs+"] [角色id:"+others[0]+"] [returnurl:"+returnUrl+"] [notifyurl:"+notifyUrl+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			return returnUrl;
		} else {
			PlatformSavingCenter.logger.warn("[生成wap支付宝订单失败] [token获取失败] [orderId:"+order.getOrderId()+"] [username:"+passport.getUserName()+"] [channel:"+userChannel+"] [payAmount:"+payAmount+"] [server:"+server+"] [os:"+mobileOs+"] [角色id:"+others[0]+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			return order.getId() +"@@failed";
		}
	}
	
	
	/**
	 * 
	 * http://wappaygw.alipay.com/service/rest.htm
	 * 
	 * ?req_data=<direct_trade_create _req><subject>彩票 </subject><out_trade_no>1282889603601</out_trade_no><total_fee>10.01</tot al_fee><seller_account_name>chenf003@yahoo.cn</seller_account_name><call_ back_url>http://www.yoursite.com/waptest0504/servlet/CallBack</call_back_ url><notify_url>http://www.yoursite.com/waptest0504/servlet/NotifyReceive
	 * @param orderId
	 * @param payAmount
	 * @param mobileOs
	 * @return
	 * @throws Exception 
	 */
	
	
	
	public String getPayToken(String orderId, int payAmount, String mobileOs) throws Exception {
		long startTime = System.currentTimeMillis();
		
		//创建一个xml字符串
		DocumentBuilderFactory factory = DocumentBuilderFactory
                .newInstance();
		
		DocumentBuilder builder = factory.newDocumentBuilder();
		
		Document doc = builder.newDocument();
		Element root = doc.createElement("direct_trade_create_req");
		XmlUtil.createChildText(doc, root, "subject", subject_name);
		XmlUtil.createChildText(doc, root, "out_trade_no", orderId);
		XmlUtil.createChildText(doc, root, "total_fee", payAmount/100 +".00" );
		XmlUtil.createChildText(doc, root, "seller_account_name",  sellerID);
		XmlUtil.createChildText(doc, root, "call_back_url", "");
		XmlUtil.createChildText(doc, root, "notify_url", notifyUrl);
		XmlUtil.createChildText(doc, root, "out_user", partnerID);
		XmlUtil.createChildText(doc, root, "merchant_url", "");
		XmlUtil.createChildText(doc, root, "pay_expire", "");
		
		doc.appendChild(root);
		
		String req_data = XmlUtil.toString(doc, "utf-8");
		
		HashMap<String, String> map = new LinkedHashMap<String,String>();
		map.put("service", service);
		map.put("req_data", req_data);
		map.put("partner", partnerID);
		map.put("req_id", System.currentTimeMillis()+"");
		map.put("sec_id", "MD5");
		map.put("format", contentformat);
		map.put("v", version);
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
			//content = URLDecoder.decode(content, "utf-8");
		} catch (Exception e) {
			PlatformSavingCenter.logger.info("[调用wap支付宝支付生成订单接口] [失败:请求时发生异常] ["+signedUrl+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]", e);
			return null;
		}
		//对content进行解析
		String rsp_data = null;
		String token = null;
		try {
			String[] rescontents = content.split("&");
			Map<String,String> params = new HashMap<String, String>();
			
			for(String str :  rescontents)
			{
				String[] keyvalues = str.split("=");
				if(keyvalues != null && keyvalues.length >  1)
				{
					params.put(URLDecoder.decode(keyvalues[0],"utf-8"), URLDecoder.decode(keyvalues[1],"utf-8"));
				}
				else
				{
					PlatformSavingCenter.logger.warn("[调用wap支付宝支付生成订单接口] [出现无法构造参数对的字符串] ["+str+"]");
				}
				
			}
			
			
			String errors=  (String)params.get("res_error");
			if(errors != null) {
				String decodedErrors = URLDecoder.decode(errors,"utf-8");
				PlatformSavingCenter.logger.info("[调用wap支付宝支付生成订单接口] [失败:获得token失败] [errors:"+errors+"] ["+signedUrl+"] ["+content+"] ["+decodedErrors+"] ["+req_data+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
				return null;
			}
			rsp_data = (String)params.get("res_data");
			Document dom = XmlUtil.loadString(rsp_data);
			
			root = dom.getDocumentElement();
			token = XmlUtil.getValueAsString(XmlUtil.getChildByName(root, "request_token"), "", null);
		} catch(Exception e) {
			PlatformSavingCenter.logger.info("[调用wap支付宝支付生成订单接口] [失败:解析rspdata时出错] [rsp_data:"+rsp_data+"] ["+signedUrl+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]", e);
			return null;
		}
		if(token == null) {
			PlatformSavingCenter.logger.info("[调用wap支付宝支付生成订单接口] [失败:解析rspdata获得token失败] [rsp_data:"+rsp_data+"] ["+signedUrl+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
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
			try {
				sb.append(keys[i]+"="+java.net.URLEncoder.encode(v,"utf-8")+"&");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				PlatformSavingCenter.logger.info("[调用wap支付宝支付生成订单接口] [encode字符串出错] ["+sb.toString()+"] ["+i+"] ["+keys[i]+"] ["+v+"]",e);
			}
		}
		sb.append("sign="+sign);
		return sb.toString();
	}
	
	private String buildPayUrl(String token) throws Exception
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory
                .newInstance();
		
		DocumentBuilder builder = factory.newDocumentBuilder();
		
		Document doc = builder.newDocument();
		Element root = doc.createElement("auth_and_execute_req");
		XmlUtil.createChildText(doc, root, "request_token", token);
		doc.appendChild(root);
		String req_data = XmlUtil.toString(doc, "utf-8");
		
		HashMap<String, String> map = new LinkedHashMap<String,String>();
		map.put("service", "alipay.wap.auth.authAndExecute");
		map.put("req_data", req_data);
		map.put("partner", partnerID);
		map.put("sec_id", "MD5");
		map.put("format", contentformat);
		map.put("v", version);
		String signedUrl = signedUrl(map, publicKey);
		signedUrl = payUrl + "?" + signedUrl;
		
		return signedUrl;
		
	}
	
	
	public static AlipayWapSavingManager getInstance(){
    	if(self == null)
    	{
    		self = new AlipayWapSavingManager();
    	}
		return self;
	}
}
