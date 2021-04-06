package com.fy.boss.platform.oppo;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.fy.boss.tools.JacksonManager;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.text.DateUtil;

public class OppoSavingManager {

	protected static OppoSavingManager m_self = null;
	
	public void initialize() throws Exception {
		m_self = this;
		System.out.println("[" + this.getClass().getName() + "] [initialized]");
	}
	
	public final String getSignCode(Map map, String appSecret) throws Exception {
		String queryString = concatMap(map);
		String signText = queryString + ":" + appSecret;
		String serverSignCode = getMD5Str(signText);
		return serverSignCode;
	}
	public static final String concatMap(Map<String, String> params) throws UnsupportedEncodingException {
		List<String> keysSet = new ArrayList<String>();
		keysSet.addAll(params.keySet());
		Collections.sort(keysSet);
		StringBuilder sb = new StringBuilder();
		for (String key : keysSet) {
			if (key.equals("sign") || key.equals("sign_type")) {
				continue;
			}
			Object value = params.get(key);
			sb.append("&").append(key).append("=").append(value.toString());
		}

		if (sb.length() > 0 && sb.toString().startsWith("&")) {
			sb.delete(0, 1);
		}
		return sb.toString();
	}
	public static String getMD5Str(String ...str)throws Exception {
		MessageDigest messageDigest = null;
		StringBuffer strB = new StringBuffer();
		for(int i = 0; i< str.length; i++){
			strB.append(str[i]);
		}
		try {
			messageDigest = MessageDigest.getInstance("MD5");

			messageDigest.reset();

			messageDigest.update(strB.toString().getBytes("UTF-8"));
		} catch (Exception e) {
			throw e;
		}

		byte[] byteArray = messageDigest.digest();

		StringBuffer md5StrBuff = new StringBuffer();

		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(
						Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}

		return md5StrBuff.toString();
	}
	

	private String appid = "dc470e100a28ae4b9de7428279f36029";
	private String appkey = "2c645a6dba5a97d79a816eabc330a0e3";
	private String cpid = "dfc4b3a950f56f96f08f";
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	
	private String buildParams(Map<String,String> params){
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
	
	public String VIVOSaving(Passport passport, String channel, String servername, int addAmount, String os,String[]others) {
		long startTime = System.currentTimeMillis();
		String savingMediumName = "VIVO充值";
		try {
			OrderFormManager orderFormManager = OrderFormManager.getInstance();
			//生成订单
			OrderForm order = new OrderForm();
			order.setCreateTime(new Date().getTime());
			//设置充值平台
			order.setSavingPlatform("VIVO");
			order.setSavingMedium(savingMediumName);
			//设置充值人
			order.setPassportId(passport.getId());
			//设置消费金额
			order.setPayMoney( new Long(addAmount));
			//设置serverid
			order.setServerName(servername);
			order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
			order.setHandleResultDesp("新生成订单");
			//设置订单responseResult
			order.setResponseResult(OrderForm.RESPONSE_NOBACK);
			order.setResponseDesp("新生成订单");
			//设置是否通知游戏服状态 设为false
			order.setNotified(false);
			//设置通知游戏服是否成功 设为false
			order.setNotifySucc(false);
			order.setUserChannel(channel);
			order.setMemo1(others[0]);
			if(others.length > 1){
				order.setChargeValue(others[1]);
			}
			if(others.length > 2){
				order.setChargeType(others[2]);
			}
			//先存入到数据库中
			order = orderFormManager.createOrderForm(order);
			//设置订单号
			order.setOrderId("op"+"-"+DateUtil.formatDate(new Date(), "yyyyMMdd")  + "-"+ order.getId());
			//orderFormManager.getEm().notifyFieldChange(order, "orderId");
			//orderFormManager.updateOrderForm(order);
			orderFormManager.updateOrderForm(order, "orderId");
			if (order.getId() > 0 && order.getOrderId() != null) 
			{
				if(PlatformSavingCenter.logger.isInfoEnabled())
					PlatformSavingCenter.logger.info("[VIVO充值订单生成] [成功] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值金额:"+addAmount+"] [渠道:"+channel+"] [游戏服务器名称:"+servername+"] [手机平台:"+os+"] [充值平台:MEIZUSDK] [订单 的ID主键:"+order.getId()+" ] [订单Id:"+order.getOrderId()+"] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
//				return order.getOrderId();
			}
			else
			{
				PlatformSavingCenter.logger.error("[VIVO充值订单生成] [失败] [订单格式不合法] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值金额:"+addAmount+"] [渠道:"+channel+"] [游戏服务器名称:"+servername+"] [手机平台:"+os+"] [充值平台:MEIZUSDK] [订单 的ID主键:"+order.getId()+" ] [订单Id:"+order.getOrderId()+"] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
				return null;
			}
			
			try {
				String paramString = "";
				String content =  "";
				Map<String,String> para = new HashMap<String, String>();
				para.put("version", "1.0.0");
				
				para.put("cpId", cpid);
				para.put("appId", appid);
				para.put("cpOrderNumber", order.getOrderId());
				para.put("notifyUrl", "http://119.254.154.201:12111/vivoResult");
				
				para.put("orderTime", sdf.format(startTime));
				para.put("orderAmount", addAmount+"");
				para.put("orderTitle", "银子");
				para.put("orderDesc", "充值银子");
				para.put("extInfo", "vivo");
				
				String mySign = VivoSignUtils.getVivoSign(para, appkey);
				para.put("signMethod", "MD5");
				para.put("signature", mySign);
				
				paramString = buildParams(para);
				HashMap headers = new HashMap();
				
				byte bytes[] = HttpUtils.webPost(new java.net.URL("https://pay.vivo.com.cn/vcoin/trade"), paramString.getBytes("utf-8"), headers, 60000, 60000);
				String encoding = (String)headers.get(HttpUtils.ENCODING);
				Integer code = (Integer)headers.get(HttpUtils.Response_Code);
				String message = (String)headers.get(HttpUtils.Response_Message);
				content = new String(bytes,encoding);
				
				JacksonManager jm = JacksonManager.getInstance();
				Map map =(Map)jm.jsonDecodeObject(content);
				String respCode = (String) map.get("respCode");
				String respMsg = (String) map.get("respMsg");
				if(respCode.equals("200")){
					String accessKey = (String) map.get("accessKey");
					String orderNumber = (String) map.get("orderNumber");
					PlatformSavingCenter.logger.info("[vivo订单推送] [成功] [orderId:"+order.getOrderId()+"] [accessKey:"+accessKey+"] [orderNumber:"+orderNumber+"] [请求内容:"+paramString+"] [返回内容:"+content+"] [code:"+code+"] ["+message+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
					return order.getOrderId() + "####" + accessKey + "####" + orderNumber;
				}else{
					PlatformSavingCenter.logger.info("[vivo订单推送] [失败:响应码错误] [错误码:"+respCode+"] [错误信息:"+respMsg+"] [请求内容:"+paramString+"] [返回内容:"+content+"] [code:"+code+"] ["+message+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
				}
			} catch (Exception e) {
				PlatformSavingCenter.logger.info("[vivo订单推送] [异常] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
			}
		}catch(Exception e){
			PlatformSavingCenter.logger.error("[VIVO充值订单生成] [失败] [生成订单出现异常] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值金额:"+addAmount+"] [渠道:"+channel+"] [游戏服务器名称:"+servername+"] [手机平台:"+os+"] [充值平台:MEIZUSDK] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		} 
		return null;
	}
	
	//vivo错误码
//	200	成功
//	失败
//	400	参数为空或者格式不正确
//	402	商户id非法，请检查
//	403	验签失败，请检查
//	405	cp订单号不唯一
//	406	appId非法，请检查
//	500	服务器内部错误，请稍后再试

	public String OPPOSaving(Passport passport, String channel, String servername, int addAmount, String os,String[]others) {
		long startTime = System.currentTimeMillis();
		String savingMediumName = "OPPO充值";
		try {
			OrderFormManager orderFormManager = OrderFormManager.getInstance();
			//生成订单
			OrderForm order = new OrderForm();
			order.setCreateTime(new Date().getTime());
			//设置充值平台
			order.setSavingPlatform("OPPO");
			order.setSavingMedium(savingMediumName);
			//设置充值人
			order.setPassportId(passport.getId());
			//设置消费金额
			order.setPayMoney( new Long(addAmount));
			//设置serverid
			order.setServerName(servername);
			order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
			order.setHandleResultDesp("新生成订单");
			//设置订单responseResult
			order.setResponseResult(OrderForm.RESPONSE_NOBACK);
			order.setResponseDesp("新生成订单");
			//设置是否通知游戏服状态 设为false
			order.setNotified(false);
			//设置通知游戏服是否成功 设为false
			order.setNotifySucc(false);
			order.setUserChannel(channel);
			order.setMemo1(others[0]);
			if(others.length > 1){
				order.setChargeValue(others[1]);
			}
			if(others.length > 2){
				order.setChargeType(others[2]);
			}
			//先存入到数据库中
			order = orderFormManager.createOrderForm(order);
			//设置订单号
			order.setOrderId("op"+"-"+DateUtil.formatDate(new Date(), "yyyyMMdd")  + "-"+ order.getId());
			//orderFormManager.getEm().notifyFieldChange(order, "orderId");
			//orderFormManager.updateOrderForm(order);
			orderFormManager.updateOrderForm(order, "orderId");
			if (order.getId() > 0 && order.getOrderId() != null) 
			{
				if(PlatformSavingCenter.logger.isInfoEnabled())
					PlatformSavingCenter.logger.info("[OPPO充值订单生成] [成功] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值金额:"+addAmount+"] [渠道:"+channel+"] [游戏服务器名称:"+servername+"] [手机平台:"+os+"] [充值平台:MEIZUSDK] [订单 的ID主键:"+order.getId()+" ] [订单Id:"+order.getOrderId()+"] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
				return order.getOrderId();
			}
			else
			{
				PlatformSavingCenter.logger.error("[OPPO充值订单生成] [失败] [订单格式不合法] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值金额:"+addAmount+"] [渠道:"+channel+"] [游戏服务器名称:"+servername+"] [手机平台:"+os+"] [充值平台:MEIZUSDK] [订单 的ID主键:"+order.getId()+" ] [订单Id:"+order.getOrderId()+"] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
				return null;
			}
		
		}
		catch(Exception e)
		{
			PlatformSavingCenter.logger.error("[OPPO充值订单生成] [失败] [生成订单出现异常] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值金额:"+addAmount+"] [渠道:"+channel+"] [游戏服务器名称:"+servername+"] [手机平台:"+os+"] [充值平台:MEIZUSDK] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			return null;
		} 
	}

	public static OppoSavingManager getInstance() {
		
		if(m_self == null)
		{
			m_self = new OppoSavingManager();
		}
		
		return m_self;
	}
}
