package com.fy.boss.platform.feiliu;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.fy.boss.platform.feiliu.FeiLiuAliPaySavingManager;
import com.fy.boss.platform.feiliu.FeiLiuSavingManager;
import com.fy.boss.platform.feiliu.RsaMessage;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.text.XmlUtil;

public class FeiLiuAliPaySavingManager {
	static FeiLiuAliPaySavingManager self;
	
	public static final String PRODUCT_ID = "100004";
	public static final String COMPANY_ID = "100005";
	public static final String CHANNEL_ID = "100012";
	
	
	public static final String RECHARGE_URL = "http://pay.feiliu.com/order/billinterface";
	
	public static Map<String, Integer> cardTypeMap = new HashMap<String,Integer>();
	
	
	public void initialize(){
		FeiLiuAliPaySavingManager.self=this;
		System.out.println("["+this.getClass().getName()+"] [initialized]");
		PlatformSavingCenter.logger.info("["+this.getClass().getName()+"] [initialized]");	
	}
	
	static {
		cardTypeMap.put("支付宝",116 );
	}
	
	
	public String aliSaving(Passport passport, int payAmount,  String server,String mobileOs,String userChannel){
		long startTime = System.currentTimeMillis();
		OrderFormManager orderFormManager = OrderFormManager.getInstance();
		//生成订单
		OrderForm order = new OrderForm();
		order.setCreateTime(new Date().getTime());
		//设置充值平台
		order.setSavingPlatform("飞流");
		//设置充值介质
		order.setSavingMedium("支付宝");
		//设置充值人
		order.setPassportId(passport.getId());
		//设置消费金额
		order.setPayMoney(payAmount);
		//设置serverName
		order.setServerName(server);
		order.setHandleResult(OrderForm.HANDLE_RESULT_NOBACK);
		//设置订单responseResult
		order.setResponseResult(OrderForm.HANDLE_RESULT_NOBACK);
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
		order.setOrderId("fl-"+DateUtil.formatDate(new Date(), "yyyyMMdd") + "-"+ order.getId());
		try
		{
			orderFormManager.updateOrderForm(order, "orderId");
		}
		catch(Exception e)
		{
			PlatformSavingCenter.logger.error("[飞流充值订单生成] [失败] [更新订单信息] [失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:支付宝] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [订单的ID:"+order.getId()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
			return "";
		}
		
		//拼装数据格式
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		sb.append("<Request>");
		sb.append("<OrderId>"+order.getOrderId()+"</OrderId>");
		sb.append("<ProductId>"+PRODUCT_ID+"</ProductId>");
		sb.append("<CompanyId>"+COMPANY_ID+"</CompanyId>");
		sb.append("<ChannelId>"+FeiLiuSavingManager.channelMap.get(order.getUserChannel())+"</ChannelId>");
		sb.append("<OrderType>"+cardTypeMap.get("支付宝")+"</OrderType>");
		sb.append("<Denomination></Denomination>");
		sb.append("<CardNO></CardNO>");
		sb.append("<CardPwd></CardPwd>");
		sb.append("<Amount>"+payAmount+"</Amount>");
		sb.append("<MerPriv>"+""+"</MerPriv>");
		
		
		String mingwen = order.getOrderId()+"|"+PRODUCT_ID+"|"+COMPANY_ID+"|"+FeiLiuSavingManager.channelMap.get(order.getUserChannel())+"|"+cardTypeMap.get("支付宝")+"||||"+payAmount+"|"+"" ;
		RsaMessage rm = new RsaMessage();
		URL urlPath = null; 
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		if(classLoader != null) {
			urlPath = classLoader.getResource("feiliu_SignKey.pub");
		}
		RSAPublicKey publicKey = RsaMessage.initPublicKey(urlPath.getPath());
		String miwen = null;
		try {
			miwen = rm.encrypt(mingwen, publicKey);
		} catch (Exception e) {
			PlatformSavingCenter.logger.error("[飞流充值订单生成] [失败 :生成加密信息失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:支付宝] [充值金额:"+payAmount+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [明文:"+mingwen+"] [密文:"+miwen+"] [公钥寻址路径:"+urlPath.getPath()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			return "";
		}
		sb.append("<VerifyString>"+miwen+"</VerifyString>");
		sb.append("</Request>");				
		//发送消息 
		URL url = null;
		try {
			url = new URL(RECHARGE_URL);
		} catch (MalformedURLException e1) {
			PlatformSavingCenter.logger.error("[飞流充值订单生成] [失败] [生成链接] [失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:支付宝] [充值金额:"+payAmount+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [充值地址:"+RECHARGE_URL+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e1);
			return "";
		}
		Map headers = new HashMap();
		headers.put("Content-Type", "text/xml; charset=utf-8");
		byte[] bytes;
		try {
			bytes = HttpUtils.webPost(url, sb.toString().getBytes(), headers, 120000, 120000);
		} catch (IOException e) {
			PlatformSavingCenter.logger.error("[飞流充值订单生成] [失败] [调用飞流充值接口] [失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:支付宝] [充值金额:"+payAmount+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [充值地址:"+RECHARGE_URL+"] [请求内容:"+sb.toString()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
			return "";
		}
		
		
		//得到响应信息
		String encoding = (String)headers.get(HttpUtils.ENCODING);
		Integer code = (Integer)headers.get(HttpUtils.Response_Code);
		String feiliu_message = (String)headers.get(HttpUtils.Response_Message);
		String content;
		try {
			content = new String(bytes,encoding);
		} catch (UnsupportedEncodingException e) {
			PlatformSavingCenter.logger.error("[飞流充值订单生成] [失败] [调用飞流充值接口] [失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:支付宝] [充值金额:"+payAmount+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [充值地址:"+RECHARGE_URL+"] [请求内容:"+sb.toString()+"] [字符集:"+encoding+"] [code:"+code+"] [响应信息:"+feiliu_message+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
			return "";
		}
		
		Document dom = null;
		try {
			dom = XmlUtil.loadString(content);
		} catch (Exception e) {
			PlatformSavingCenter.logger.error("[飞流充值订单生成] [失败] [解析响应内容信息] [失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:支付宝] [充值金额:"+payAmount+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [充值地址:"+RECHARGE_URL+"] [请求内容:"+sb.toString()+"] [字符集:"+encoding+"] [code:"+code+"] [响应信息:"+feiliu_message+"] [content:"+content+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
			return "";
		}
		Element root = dom.getDocumentElement();
		
		int ret = XmlUtil.getValueAsInteger(XmlUtil.getChildByName(root, "Ret"), -1);
		String retMsg = XmlUtil.getValueAsString(XmlUtil.getChildByName(root, "RetMsg"), "", null);
		//ret 0为成功 非0 为失败
		if(ret == 0)
		{
			// 调用充值平台的结果，-1-未返回, 0-失败,1-成功  代表发送请求的响应
			order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
			order.setHandleResultDesp(retMsg);
			try {
				orderFormManager.update(order);
			} catch (Exception e) {
				PlatformSavingCenter.logger.error("[飞流充值订单生成] [失败] [更新订单] [失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:支付宝] [充值金额:"+payAmount+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [充值地址:"+RECHARGE_URL+"] [请求内容:"+sb.toString()+"] [字符集:"+encoding+"] [code:"+code+"] [响应信息:"+feiliu_message+"] [content:"+content+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
				return "";
			}
			if(PlatformSavingCenter.logger.isInfoEnabled())
				PlatformSavingCenter.logger.info("[飞流充值订单生成] [调用飞流接口成功] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值类型:支付宝] [充值金额:"+payAmount+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [orderForm ID:"+order.getId()+"] [orderId:"+order.getOrderId()+"] [请求内容:"+sb.toString()+"] [retMsg:"+retMsg+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			try {
				retMsg = URLDecoder.decode(retMsg,"utf-8");
			} catch (UnsupportedEncodingException e) {
				if(PlatformSavingCenter.logger.isInfoEnabled())
					PlatformSavingCenter.logger.info("[飞流充值订单生成] [调用飞流接口] [解析返回url] [失败] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值类型:支付宝] [充值金额:"+payAmount+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [orderForm ID:"+order.getId()+"] [orderId:"+order.getOrderId()+"] [请求内容:"+sb.toString()+"] [retMsg:"+retMsg+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			}
			
			return order.getOrderId() + "@@" + retMsg;
		}
		else
		{
			// 调用充值平台的结果，-1-未返回, 0-失败,1-成功  代表发送请求的响应
			order.setHandleResult(OrderForm.HANDLE_RESULT_FAILED);
			order.setHandleResultDesp(retMsg);
			try {
				orderFormManager.update(order);
			} catch (Exception e) {
				PlatformSavingCenter.logger.error("[飞流充值订单生成] [失败] [更新订单] [失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:支付宝] [充值金额:"+payAmount+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [充值地址:"+RECHARGE_URL+"] [请求内容:"+sb.toString()+"] [字符集:"+encoding+"] [code:"+code+"] [响应信息:"+feiliu_message+"] [content:"+content+"] [返回码:"+ret+"] [返回消息:"+retMsg+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
				return "";
			}
			if(PlatformSavingCenter.logger.isInfoEnabled())
				PlatformSavingCenter.logger.error("[飞流充值订单生成] [成功] [调用飞流接口失败] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值类型:支付宝] [充值金额:"+payAmount+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [orderForm ID:"+order.getId()+"] [orderId:"+order.getOrderId()+"] [飞流响应码:"+ret+"] [飞流响应信息:"+retMsg+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			return "";
		}
		//处理
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
					PlatformSavingCenter.logger.error("[飞流充值订单生成] [失败] [更新订单] [失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:支付宝] [充值金额:"+payAmount+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [充值地址:"+RECHARGE_URL+"] [角色名:"+order.getMemo1()+"] [costs:"+(System.currentTimeMillis()-t)+"]",e);
				}
				
				PlatformSavingCenter.logger.warn("[飞流充值订单生成] [成功] [无角色id传入] [id:"+order.getId()+"] [orderID："+orderId+"] [充值类型：支付宝] [paymoney:"+0+"] [用户:"+passport.getUserName()+"] [角色:"+order.getMemo1()+"] ["+(System.currentTimeMillis()-t)+"ms]");
			}
			else
			{
				PlatformSavingCenter.logger.warn("[飞流充值订单生成] [失败] [根据订单id没有查到订单] [无角色id传入] [orderid:"+orderId+"]");
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
					PlatformSavingCenter.logger.error("[飞流充值订单生成] [失败：更新订单出现异常] [id:"+order.getId()+"] [orderID："+orderId+"] [充值类型：支付宝] [paymoney:"+0+"] [用户:"+passport.getUserName()+"] [角色:"+order.getMemo1()+"] ["+(System.currentTimeMillis()-t)+"ms]", e);
				}
				
				PlatformSavingCenter.logger.warn("[飞流充值订单生成] [成功] [无角色id传入] [id:"+order.getId()+"] [orderID："+orderId+"] [充值类型：支付宝] [paymoney:"+0+"] [用户:"+passport.getUserName()+"] [角色:"+order.getMemo1()+"] ["+(System.currentTimeMillis()-t)+"ms]");
			}
			else
			{
				PlatformSavingCenter.logger.warn("[飞流充值订单生成] [失败] [根据订单id没有查到订单] [无角色id传入] [orderid:"+orderId+"]");
			}
			return orderId;
		}
		else
		{
			return "";
		}
		
	}
	
	public static FeiLiuAliPaySavingManager getInstance(){
		if(self == null) {
			self = new FeiLiuAliPaySavingManager();
		}
		return self;
	}
}
