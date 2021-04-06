package com.fy.boss.platform.sina;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.hibernate.criterion.Order;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.fy.boss.tools.DES;
import com.fy.boss.tools.JacksonManager;
import com.fy.boss.platform.sina.SinaWYXSavingManager;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.text.StringUtil;
import com.xuanzhi.tools.text.XmlUtil;

public class SinaWYXSavingManager {
	static SinaWYXSavingManager self;
	
	private static final String APPKEY = "3467194209";
	public static final String	MSGFROM= "6056";
	private static final String SECRETKEY = "084b6fbb10729ed4da8c3d3f5a3ae7c9e80d62e09a6638f818e0767091389babd41d8cd98f00b204e9800998ecf8427ef5c24371ae2aed82ec2f23c8bfb72e36";
	
	
	public static final String RECHARGE_URL = "http://ota.pay.mobile.sina.cn/kjava/order.php";
	
	public static Map<String, Integer> cardTypeMap = new HashMap<String,Integer>();
	public static Map<String, Integer> payTypeMap = new HashMap<String,Integer>();
	
	
	public void initialize(){
		SinaWYXSavingManager.self=this;
		System.out.println("["+this.getClass().getName()+"] [initialized]");
		PlatformSavingCenter.logger.info("["+this.getClass().getName()+"] [initialized]");	
	}
	
	static {
		cardTypeMap.put("移动充值卡",1);
		cardTypeMap.put("联通一卡付",2);
		cardTypeMap.put("电信充值卡",3);
	}
	
	static {
		payTypeMap.put("新浪支付宝",1);
		payTypeMap.put("移动充值卡",4);
		payTypeMap.put("联通一卡付",4);
		payTypeMap.put("电信充值卡",4);
	}
	
	
	public String cardSaving(Passport passport, String cardtype, int payAmount, String cardno, String cardpass,
			 String server,String mobileOs,String userChannel){
		long startTime = System.currentTimeMillis();
		OrderFormManager orderFormManager = OrderFormManager.getInstance();
		//生成订单
		OrderForm order = new OrderForm();
		order.setCreateTime(new Date().getTime());
		//设置充值平台
		order.setSavingPlatform("新浪微游戏");
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
		order.setHandleResultDesp("新浪微游戏新生成订单");
		//设置订单responseResult
		order.setResponseResult(OrderForm.RESPONSE_NOBACK);
		order.setResponseDesp("新浪微游戏新生成订单");
		//设置是否通知游戏服状态 设为false
		order.setNotified(false);
		//设置通知游戏服是否成功 设为false
		order.setNotifySucc(false);
		//在备注中存入充值平台
		order.setMemo1(mobileOs);
		order.setUserChannel(userChannel);
		//先存入到数据库中
		order = orderFormManager.createOrderForm(order);
		if(PlatformSavingCenter.logger.isInfoEnabled())
			PlatformSavingCenter.logger.error("[新浪微游戏充值订单生成] [成功] [创建新订单] [成功] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [订单的ID:"+order.getId()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
		//设置订单号
		order.setOrderId("sinawei-"+DateUtil.formatDate(new Date(), "yyyyMMdd") + "-"+ order.getId());
		try
		{
			orderFormManager.updateOrderForm(order, "orderId");
			if(PlatformSavingCenter.logger.isInfoEnabled())
				PlatformSavingCenter.logger.error("[新浪微游戏充值订单生成] [成功] [更新订单：添加订单ID] [成功] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [订单的ID:"+order.getId()+"] [订单ID:"+order.getOrderId()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
		}
		catch(Exception e)
		{
			PlatformSavingCenter.logger.error("[新浪微游戏充值订单生成] [失败] [更新订单信息] [失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [订单的ID:"+order.getId()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
		}
		Map<String,String> paramMap = new LinkedHashMap<String, String>();
		//放入参数
		paramMap.put("AppKey", APPKEY);
		paramMap.put("orderNo", order.getOrderId());
		try {
			paramMap.put("desc", URLEncoder.encode("飘渺寻仙曲充值","utf-8"));
		} catch (UnsupportedEncodingException e2) {
			PlatformSavingCenter.logger.error("[新浪微游戏充值订单生成] [失败] [URLEncoder encode] [失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [订单的ID:"+order.getId()+"] [使用方法 :URLEncoder.encode(\"飘渺寻仙曲充值\",\"utf-8\")] [costs:"+(System.currentTimeMillis()-startTime)+"]",e2);
		}
		paramMap.put("fee", String.valueOf(payAmount/100));
		paramMap.put("pay_type", payTypeMap.get(cardtype)+"");
		paramMap.put("mobile", "");
	/*	try {
			paramMap.put("cardNO", DES.encode(cardno,SECRETKEY));
			paramMap.put("cardPsw", DES.encode(cardpass,SECRETKEY));
		} catch (Exception e2) {
			PlatformSavingCenter.logger.error("[新浪微游戏充值订单生成] [失败] [加密卡号和卡密] [失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [订单的ID:"+order.getId()+"] [订单ID:"+order.getOrderId()+"] [密钥:"+SECRETKEY+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e2);
		}*/
		paramMap.put("cardNO",cardno);
		paramMap.put("cardPsw", cardpass);
		paramMap.put("msgfrom", MSGFROM);	
		paramMap.put("card_type", cardTypeMap.get(cardtype) == null ? "" : cardTypeMap.get(cardtype)+"");
		paramMap.put("ru", "wap.sina.com.cn");
		paramMap.put("order_uid", passport.getUserName().split("_")[1]);
		
		//拼装数据格式
		String params = pinzhuangcanshu(paramMap);
		//发送消息 
		URL url = null;
		try {
			url = new URL(RECHARGE_URL);
		} catch (MalformedURLException e1) {
			PlatformSavingCenter.logger.error("[新浪微游戏充值订单生成] [失败] [生成链接] [失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [充值地址:"+RECHARGE_URL+"] [订单的ID:"+order.getId()+"] [订单ID:"+order.getOrderId()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e1);
			order.setHandleResult(OrderForm.HANDLE_RESULT_FAILED);
			order.setHandleResultDesp("新浪微游戏充值订单生成 生成链接 失败");
			try {
				orderFormManager.update(order);
				PlatformSavingCenter.logger.error("[新浪微游戏充值订单生成] [失败]  [更新订单信息成功] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [充值地址:"+RECHARGE_URL+"] [请求内容:"+""+"] [订单的ID:"+order.getId()+"] [订单ID:"+order.getOrderId()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
			} catch (Exception e) {
				PlatformSavingCenter.logger.error("[新浪微游戏充值订单生成] [失败] [更新订单信息失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [充值地址:"+RECHARGE_URL+"] [请求内容:"+""+"] [订单的ID:"+order.getId()+"] [订单ID:"+order.getOrderId()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
			}
		}
		
		Map headers = new HashMap();
		byte[] bytes= null;
		try {
			bytes = HttpUtils.webPost(url, params.getBytes(), headers, 120000, 120000);
		} catch (IOException e) {
			PlatformSavingCenter.logger.error("[新浪微游戏充值订单生成] [失败] [调用新浪微游戏充值接口] [失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [充值地址:"+RECHARGE_URL+"] [订单的ID:"+order.getId()+"] [订单ID:"+order.getOrderId()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
			order.setHandleResult(OrderForm.HANDLE_RESULT_FAILED);
			order.setHandleResultDesp("新浪微游戏充值订单生成 调用新浪微游戏充值接口 失败");
			try {
				orderFormManager.update(order);
				PlatformSavingCenter.logger.error("[新浪微游戏充值订单生成] [失败]  [更新订单信息成功] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [充值地址:"+RECHARGE_URL+"] [请求内容:"+""+"] [订单的ID:"+order.getId()+"] [订单ID:"+order.getOrderId()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
			} catch (Exception e1) {
				PlatformSavingCenter.logger.error("[新浪微游戏充值订单生成] [失败] [更新订单信息失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [充值地址:"+RECHARGE_URL+"] [请求内容:"+""+"] [订单的ID:"+order.getId()+"] [订单ID:"+order.getOrderId()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e1);
			}
		}
		
		//得到响应信息
		String encoding = (String)headers.get(HttpUtils.ENCODING);
		Integer code = (Integer)headers.get(HttpUtils.Response_Code);
		String sinawei_message = (String)headers.get(HttpUtils.Response_Message);
		String content=null;
		try {
			content = new String(bytes,encoding);
			Map dataMap = new HashMap();
			//处理
			 dataMap = parseData(content);
			
			//继续解析数据
			/**
			 * error_code	返回错误码	是	Max(20)	00:成功
	其余，详见附录三
	
			 */
			Map payinfoMap = new HashMap();
			String errorCode = (String)dataMap.get("error_code");
			String errorMessage = null; 
				errorMessage = URLDecoder.decode(((String)dataMap.get("error_message")),"utf-8");
			payinfoMap = (Map)dataMap.get("pay_info");
			//dataMap.get("fee");
			//如果成功 
			if("00".equalsIgnoreCase(errorCode))
			{
				//如果是卡密 判断pay_info的string
				if(payTypeMap.get(cardtype).intValue() == 4) 
				{
					if("Up_Complete".equalsIgnoreCase(((String)payinfoMap.get("String"))))
					{
						order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
						order.setHandleResultDesp("新浪微游戏新生成订单@卡密充值方式@调用接口成功@充值卡号:"+cardno);
						try {
							orderFormManager.update(order);
							PlatformSavingCenter.logger.info("[新浪微游戏充值订单生成] [成功] [解析新浪返回json数据] [成功] [调用充值接口成功] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [充值地址:"+RECHARGE_URL+"] [请求内容:"+""+"] [字符集:"+encoding+"] [code:"+code+"] [响应信息:"+sinawei_message+"] [订单的ID:"+order.getId()+"] [订单ID:"+order.getOrderId()+"] [新浪返回信息:"+content+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
							return order.getOrderId();
						} catch (Exception e) {
							PlatformSavingCenter.logger.error("[新浪微游戏充值订单生成] [成功] [解析新浪返回json数据] [成功] [更新订单信息失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [充值地址:"+RECHARGE_URL+"] [请求内容:"+""+"] [字符集:"+encoding+"] [code:"+code+"] [响应信息:"+sinawei_message+"] [订单的ID:"+order.getId()+"] [订单ID:"+order.getOrderId()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
							return "";
						}
					}
				}
				//如果是支付宝 则返回url
				if(payTypeMap.get(cardtype).intValue() == 1)
				{
					order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
					order.setHandleResultDesp("新浪微游戏新生成订单@支付宝充值方式@调用接口成功@充值卡号:"+cardno);
					try {
						orderFormManager.update(order);
						PlatformSavingCenter.logger.error("[新浪微游戏充值订单生成] [成功] [调用充值接口成功] [充值信息提交成功] [更新订单信息成功] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [充值地址:"+RECHARGE_URL+"] [请求内容:"+""+"] [字符集:"+encoding+"] [code:"+code+"] [响应信息:"+sinawei_message+"] [订单的ID:"+order.getId()+"] [订单ID:"+order.getOrderId()+"] [返回值："+payinfoMap.get("Url")+"] [新浪返回信息:"+content+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
						return (String)payinfoMap.get("Url");
					} catch (Exception e) {
						PlatformSavingCenter.logger.error("[新浪微游戏充值订单生成] [成功] [调用充值接口成功] [充值信息提交成功] [更新订单信息失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [充值地址:"+RECHARGE_URL+"] [请求内容:"+""+"] [字符集:"+encoding+"] [code:"+code+"] [响应信息:"+sinawei_message+"] [订单的ID:"+order.getId()+"] [订单ID:"+order.getOrderId()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
						return "";
					}
				}
			}
			
			order.setHandleResult(OrderForm.HANDLE_RESULT_FAILED);
			order.setHandleResultDesp("错误码："+errorCode+"@错误信息:"+errorMessage);
			try {
				orderFormManager.update(order);
				PlatformSavingCenter.logger.error("[新浪微游戏充值订单生成] [成功] [调用充值接口成功] [充值信息提交失败] [更新订单信息成功] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [充值地址:"+RECHARGE_URL+"?" + params+"] [请求内容:"+""+"] [字符集:"+encoding+"] [code:"+code+"] [响应信息:"+sinawei_message+"] [订单的ID:"+order.getId()+"] [订单ID:"+order.getOrderId()+"] [新浪返回信息:"+content+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
				return "";
			} catch (Exception e) {
				PlatformSavingCenter.logger.error("[新浪微游戏充值订单生成] [成功] [调用充值接口成功] [充值信息提交失败] [更新订单信息失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [充值地址:"+RECHARGE_URL+"?" + params+"] [请求内容:"+""+"] [字符集:"+encoding+"] [code:"+code+"] [响应信息:"+sinawei_message+"] [订单的ID:"+order.getId()+"] [订单ID:"+order.getOrderId()+"] [新浪返回信息:"+content+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
				return "";
			}
		}
		catch (Exception e) {
			order.setHandleResult(OrderForm.HANDLE_RESULT_FAILED);
			order.setHandleResultDesp("调用接口时出现异常");
			try {
				orderFormManager.update(order);
				PlatformSavingCenter.logger.error("[新浪微游戏充值订单生成] [成功] [调用充值接口失败：出现异常] [更新订单信息成功]  [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [充值地址:"+RECHARGE_URL+"?" + params+"] [请求内容:"+""+"] [字符集:"+encoding+"] [code:"+code+"] [响应信息:"+sinawei_message+"] [订单的ID:"+order.getId()+"] [订单ID:"+order.getOrderId()+"] [新浪返回信息:"+content+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
				return "";
			} catch (Exception e1) {
				PlatformSavingCenter.logger.error("[新浪微游戏充值订单生成] [成功] [调用充值接口失败：出现异常] [更新订单信息失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [充值地址:"+RECHARGE_URL+"?" + params+"] [请求内容:"+""+"] [字符集:"+encoding+"] [code:"+code+"] [响应信息:"+sinawei_message+"] [订单的ID:"+order.getId()+"] [订单ID:"+order.getOrderId()+"] [出现异常信息:"+e+"] [新浪返回信息:"+content+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e1);
				return "";
			}
		}
		
	}
	
	
	public String cardSaving(Passport passport, String cardtype, int payAmount, String cardno, String cardpass,
			 String server,String mobileOs,String userChannel,String[]others){
		long startTime = System.currentTimeMillis();
		OrderFormManager orderFormManager = OrderFormManager.getInstance();
		//生成订单
		OrderForm order = new OrderForm();
		order.setCreateTime(new Date().getTime());
		//设置充值平台
		order.setSavingPlatform("新浪微游戏");
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
		order.setHandleResultDesp("新浪微游戏新生成订单");
		//设置订单responseResult
		order.setResponseResult(OrderForm.RESPONSE_NOBACK);
		order.setResponseDesp("新浪微游戏新生成订单");
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
		if(PlatformSavingCenter.logger.isInfoEnabled())
			PlatformSavingCenter.logger.error("[新浪微游戏充值订单生成] [成功] [创建新订单] [成功] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [订单的ID:"+order.getId()+"] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
		//设置订单号
		order.setOrderId("sinawei-"+DateUtil.formatDate(new Date(), "yyyyMMdd") + "-"+ order.getId());
		try
		{
			orderFormManager.updateOrderForm(order, "orderId");
			if(PlatformSavingCenter.logger.isInfoEnabled())
				PlatformSavingCenter.logger.error("[新浪微游戏充值订单生成] [成功] [更新订单：添加订单ID] [成功] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [订单的ID:"+order.getId()+"] [订单ID:"+order.getOrderId()+"] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
		}
		catch(Exception e)
		{
			PlatformSavingCenter.logger.error("[新浪微游戏充值订单生成] [失败] [更新订单信息] [失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [订单的ID:"+order.getId()+"] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
		}
		Map<String,String> paramMap = new LinkedHashMap<String, String>();
		//放入参数
		paramMap.put("AppKey", APPKEY);
		paramMap.put("orderNo", order.getOrderId());
		try {
			paramMap.put("desc", URLEncoder.encode("飘渺寻仙曲充值","utf-8"));
		} catch (UnsupportedEncodingException e2) {
			PlatformSavingCenter.logger.error("[新浪微游戏充值订单生成] [失败] [URLEncoder encode] [失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [订单的ID:"+order.getId()+"] [使用方法 :URLEncoder.encode(\"飘渺寻仙曲充值\",\"utf-8\")] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e2);
		}
		paramMap.put("fee", String.valueOf(payAmount/100));
		paramMap.put("pay_type", payTypeMap.get(cardtype)+"");
		paramMap.put("mobile", "");
	/*	try {
			paramMap.put("cardNO", DES.encode(cardno,SECRETKEY));
			paramMap.put("cardPsw", DES.encode(cardpass,SECRETKEY));
		} catch (Exception e2) {
			PlatformSavingCenter.logger.error("[新浪微游戏充值订单生成] [失败] [加密卡号和卡密] [失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [订单的ID:"+order.getId()+"] [订单ID:"+order.getOrderId()+"] [密钥:"+SECRETKEY+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e2);
		}*/
		paramMap.put("cardNO",cardno);
		paramMap.put("cardPsw", cardpass);
		paramMap.put("msgfrom", MSGFROM);	
		paramMap.put("card_type", cardTypeMap.get(cardtype) == null ? "" : cardTypeMap.get(cardtype)+"");
		paramMap.put("ru", "wap.sina.com.cn");
		paramMap.put("order_uid", passport.getUserName().split("_")[1]);
		
		//拼装数据格式
		String params = pinzhuangcanshu(paramMap);
		//发送消息 
		URL url = null;
		try {
			url = new URL(RECHARGE_URL);
		} catch (MalformedURLException e1) {
			PlatformSavingCenter.logger.error("[新浪微游戏充值订单生成] [失败] [生成链接] [失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [充值地址:"+RECHARGE_URL+"] [订单的ID:"+order.getId()+"] [订单ID:"+order.getOrderId()+"] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e1);
			order.setHandleResult(OrderForm.HANDLE_RESULT_FAILED);
			order.setHandleResultDesp("新浪微游戏充值订单生成 生成链接 失败");
			try {
				orderFormManager.update(order);
				PlatformSavingCenter.logger.error("[新浪微游戏充值订单生成] [失败]  [更新订单信息成功] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [充值地址:"+RECHARGE_URL+"] [请求内容:"+""+"] [订单的ID:"+order.getId()+"] [订单ID:"+order.getOrderId()+"] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
			} catch (Exception e) {
				PlatformSavingCenter.logger.error("[新浪微游戏充值订单生成] [失败] [更新订单信息失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [充值地址:"+RECHARGE_URL+"] [请求内容:"+""+"] [订单的ID:"+order.getId()+"] [订单ID:"+order.getOrderId()+"] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
			}
		}
		
		Map headers = new HashMap();
		byte[] bytes= null;
		try {
			bytes = HttpUtils.webPost(url, params.getBytes(), headers, 120000, 120000);
		} catch (IOException e) {
			PlatformSavingCenter.logger.error("[新浪微游戏充值订单生成] [失败] [调用新浪微游戏充值接口] [失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [充值地址:"+RECHARGE_URL+"] [订单的ID:"+order.getId()+"] [订单ID:"+order.getOrderId()+"] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
			order.setHandleResult(OrderForm.HANDLE_RESULT_FAILED);
			order.setHandleResultDesp("新浪微游戏充值订单生成 调用新浪微游戏充值接口 失败");
			try {
				orderFormManager.update(order);
				PlatformSavingCenter.logger.error("[新浪微游戏充值订单生成] [失败]  [更新订单信息成功] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [充值地址:"+RECHARGE_URL+"] [请求内容:"+""+"] [订单的ID:"+order.getId()+"] [订单ID:"+order.getOrderId()+"] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
			} catch (Exception e1) {
				PlatformSavingCenter.logger.error("[新浪微游戏充值订单生成] [失败] [更新订单信息失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [充值地址:"+RECHARGE_URL+"] [请求内容:"+""+"] [订单的ID:"+order.getId()+"] [订单ID:"+order.getOrderId()+"] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e1);
			}
		}
		
		//得到响应信息
		String encoding = (String)headers.get(HttpUtils.ENCODING);
		Integer code = (Integer)headers.get(HttpUtils.Response_Code);
		String sinawei_message = (String)headers.get(HttpUtils.Response_Message);
		String content=null;
		try {
			content = new String(bytes,encoding);
			Map dataMap = new HashMap();
			//处理
			 dataMap = parseData(content);
			
			//继续解析数据
			/**
			 * error_code	返回错误码	是	Max(20)	00:成功
	其余，详见附录三
	
			 */
			Map payinfoMap = new HashMap();
			String errorCode = (String)dataMap.get("error_code");
			String errorMessage = null; 
				errorMessage = URLDecoder.decode(((String)dataMap.get("error_message")),"utf-8");
			payinfoMap = (Map)dataMap.get("pay_info");
			//dataMap.get("fee");
			//如果成功 
			if("00".equalsIgnoreCase(errorCode))
			{
				//如果是卡密 判断pay_info的string
				if(payTypeMap.get(cardtype).intValue() == 4) 
				{
					if("Up_Complete".equalsIgnoreCase(((String)payinfoMap.get("String"))))
					{
						order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
						order.setHandleResultDesp("新浪微游戏新生成订单@卡密充值方式@调用接口成功@充值卡号:"+cardno);
						try {
							orderFormManager.update(order);
							PlatformSavingCenter.logger.info("[新浪微游戏充值订单生成] [成功] [解析新浪返回json数据] [成功] [调用充值接口成功] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [充值地址:"+RECHARGE_URL+"] [请求内容:"+""+"] [字符集:"+encoding+"] [code:"+code+"] [响应信息:"+sinawei_message+"] [订单的ID:"+order.getId()+"] [订单ID:"+order.getOrderId()+"] [新浪返回信息:"+content+"] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
							return order.getOrderId();
						} catch (Exception e) {
							PlatformSavingCenter.logger.error("[新浪微游戏充值订单生成] [成功] [解析新浪返回json数据] [成功] [更新订单信息失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [充值地址:"+RECHARGE_URL+"] [请求内容:"+""+"] [字符集:"+encoding+"] [code:"+code+"] [响应信息:"+sinawei_message+"] [订单的ID:"+order.getId()+"] [订单ID:"+order.getOrderId()+"] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
							return "";
						}
					}
				}
				//如果是支付宝 则返回url
				if(payTypeMap.get(cardtype).intValue() == 1)
				{
					order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
					order.setHandleResultDesp("新浪微游戏新生成订单@支付宝充值方式@调用接口成功@充值卡号:"+cardno);
					try {
						orderFormManager.update(order);
						PlatformSavingCenter.logger.error("[新浪微游戏充值订单生成] [成功] [调用充值接口成功] [充值信息提交成功] [更新订单信息成功] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [充值地址:"+RECHARGE_URL+"] [请求内容:"+""+"] [字符集:"+encoding+"] [code:"+code+"] [响应信息:"+sinawei_message+"] [订单的ID:"+order.getId()+"] [订单ID:"+order.getOrderId()+"] [返回值："+payinfoMap.get("Url")+"] [新浪返回信息:"+content+"] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
						return (String)payinfoMap.get("Url");
					} catch (Exception e) {
						PlatformSavingCenter.logger.error("[新浪微游戏充值订单生成] [成功] [调用充值接口成功] [充值信息提交成功] [更新订单信息失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [充值地址:"+RECHARGE_URL+"] [请求内容:"+""+"] [字符集:"+encoding+"] [code:"+code+"] [响应信息:"+sinawei_message+"] [订单的ID:"+order.getId()+"] [订单ID:"+order.getOrderId()+"] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
						return "";
					}
				}
			}
			
			order.setHandleResult(OrderForm.HANDLE_RESULT_FAILED);
			order.setHandleResultDesp("错误码："+errorCode+"@错误信息:"+errorMessage);
			try {
				orderFormManager.update(order);
				PlatformSavingCenter.logger.error("[新浪微游戏充值订单生成] [成功] [调用充值接口成功] [充值信息提交失败] [更新订单信息成功] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [充值地址:"+RECHARGE_URL+"?" + params+"] [请求内容:"+""+"] [字符集:"+encoding+"] [code:"+code+"] [响应信息:"+sinawei_message+"] [订单的ID:"+order.getId()+"] [订单ID:"+order.getOrderId()+"] [新浪返回信息:"+content+"] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
				return "";
			} catch (Exception e) {
				PlatformSavingCenter.logger.error("[新浪微游戏充值订单生成] [成功] [调用充值接口成功] [充值信息提交失败] [更新订单信息失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [充值地址:"+RECHARGE_URL+"?" + params+"] [请求内容:"+""+"] [字符集:"+encoding+"] [code:"+code+"] [响应信息:"+sinawei_message+"] [订单的ID:"+order.getId()+"] [订单ID:"+order.getOrderId()+"] [新浪返回信息:"+content+"] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
				return "";
			}
		}
		catch (Exception e) {
			order.setHandleResult(OrderForm.HANDLE_RESULT_FAILED);
			order.setHandleResultDesp("调用接口时出现异常");
			try {
				orderFormManager.update(order);
				PlatformSavingCenter.logger.error("[新浪微游戏充值订单生成] [成功] [调用充值接口失败：出现异常] [更新订单信息成功]  [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [充值地址:"+RECHARGE_URL+"?" + params+"] [请求内容:"+""+"] [字符集:"+encoding+"] [code:"+code+"] [响应信息:"+sinawei_message+"] [订单的ID:"+order.getId()+"] [订单ID:"+order.getOrderId()+"] [新浪返回信息:"+content+"] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
				return "";
			} catch (Exception e1) {
				PlatformSavingCenter.logger.error("[新浪微游戏充值订单生成] [成功] [调用充值接口失败：出现异常] [更新订单信息失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [充值地址:"+RECHARGE_URL+"?" + params+"] [请求内容:"+""+"] [字符集:"+encoding+"] [code:"+code+"] [响应信息:"+sinawei_message+"] [订单的ID:"+order.getId()+"] [订单ID:"+order.getOrderId()+"] [出现异常信息:"+e+"] [新浪返回信息:"+content+"] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e1);
				return "";
			}
		}
		
	}
	
	
	//拼参数串
	private String pinzhuangcanshu(Map<String,String> paramMap)
	{
		StringBuffer strbuf = new StringBuffer();
		Iterator<String> it = paramMap.keySet().iterator();
		int i = 0;
		while(it.hasNext())
		{
			String key = it.next();
			if(i == 0)
			{
				strbuf.append(key);
				strbuf.append("=");
				strbuf.append(paramMap.get(key));
			}
			else
			{
				strbuf.append("&");
				strbuf.append(key);
				strbuf.append("=");
				strbuf.append(paramMap.get(key));
			}
			i++;
		}
		return strbuf.toString() + "&sign=" + sign(paramMap);
	}
	
	//签名
	private String sign(Map<String,String> paramMap)
	{
		String keys[] = paramMap.keySet().toArray(new String[0]);
		java.util.Arrays.sort(keys);
		StringBuffer sb = new StringBuffer();
		
		for(int i = 0 ; i < keys.length ; i++){
			String v = paramMap.get(keys[i]);
			sb.append(keys[i]+"="+v+"&");
		}
		sb.append("key="+SECRETKEY);
		String sign = StringUtil.hash(sb.toString());

		return sign;
	}
	
	private Map parseData(String content) throws JsonParseException, IOException
	{
		JacksonManager jm = JacksonManager.getInstance();
		Map dataMap = (Map)jm.jsonDecodeObject(content);
		return dataMap;
	
	}
	
	public static SinaWYXSavingManager getInstance(){
		return SinaWYXSavingManager.self;
	}
}
