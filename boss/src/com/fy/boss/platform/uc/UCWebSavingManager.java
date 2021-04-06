package com.fy.boss.platform.uc;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.fy.boss.tools.JacksonManager;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.text.StringUtil;

public class UCWebSavingManager {
	static UCWebSavingManager self;
	public static int UC_CPID = 149;
	//private static int UC_ANDROID_GAMEID = 63371;
	public static int UC_ANDROID_GAMEID = 43201; //测试用GAMEID
	//private static int UC_ANDROID_ServerID = 907;
	public static int UC_ANDROID_ServerID = 845; //测试用服务器id
	public static int UC_ANDDROID_CHANNELID = 2;
	public static int UC_ANDDROID_APPID = 10024;
	public static String UC_ANDDROID_Secretkey = "2b6f740373c5893c8b798798fbdda077";
	
	//private static int UC_IOS_GAMEID = 63586;
	public static int UC_IOS_GAMEID = 43202;
	//private static int UC_IOS_ServerID = 908 ;
	public static int UC_IOS_ServerID = 846;
	public static int UC_IOS_CHANNELID = 2;
	public static int UC_IOS_APPID = 10025;
	public static String UC_IOS_Secretkey = "1c18543e919f196a46d3d65332c86b68";
	
	public static String UC_VALID_URL = "http://api.g.uc.cn/login";
	
	public static final String RECHARGE_URL = "http://api.g.uc.cn/pay";
	
	private OrderFormManager orderFormManager;
	public static Map<String, Integer> cardTypeMap = new HashMap<String,Integer>();
//public static Map<Integer,String> infoMap = new HashMap<Integer, String>();
	
	
	public void initialize(){
		UCWebSavingManager.self=this;
		System.out.println("["+this.getClass().getName()+"] [initialized]");
		PlatformSavingCenter.logger.info("["+this.getClass().getName()+"] [initialized]");	
	}
	
	static {
		cardTypeMap.put("神州行",1001);
		cardTypeMap.put("联通卡",1002);
		cardTypeMap.put("电信卡",1003);
		cardTypeMap.put("骏网一卡通",1004);
		cardTypeMap.put("盛大卡",1005);
		cardTypeMap.put( "征途卡",1006);
		cardTypeMap.put("Q币卡",1007);
		cardTypeMap.put("久游卡",1008);
		cardTypeMap.put( "易宝e卡通",1009);
		cardTypeMap.put("网易卡",1010);
		cardTypeMap.put("完美卡",1011 );
		cardTypeMap.put("搜狐卡",1012 );
		cardTypeMap.put("纵游一卡通",1013 );
		cardTypeMap.put("天下一卡通",1014 );
		cardTypeMap.put("天宏一卡通",1015 );
	}
	
	static{
	/*	//自定义错误信息 
		infoMap.put(6010000, "调用接口错误");
		//infoMap.put(6010001, "调用支付接口错误");
		infoMap.put(6010001, "订单生成失败");
		
		//验证响应码
		infoMap.put(20101100, "验证成功");
		
		//充值相关响应码
		infoMap.put(20100100, "OK,下单成功");
		infoMap.put(50100101, "签名较验失败或未知错误");
		infoMap.put(50100102, "卡密成功处理过或者提交卡号过于频繁");
		infoMap.put(50100103, "卡数量过多，目前最多支持10张卡");
		infoMap.put(50100104, "订单号重复");
		infoMap.put(50100105, "支付金额有误");
		infoMap.put(50100106, "支付方式未开通");
		infoMap.put(50100107, "业务状态不可用，未开通此类卡业务");
		infoMap.put(50100108, "卡面额组填写错误");
		infoMap.put(50100109, "卡号密码为空或者数量不相等（使用组合支付时）");
		infoMap.put(50100110, "无效的通知地址");
		infoMap.put(50100111, "无效的用户ID");
		infoMap.put(50100112, "无效的商品编号");
		infoMap.put(50100199, "其他错误");
		infoMap.put(20100200, "支付成功");
		infoMap.put(50100201, "卡号卡密或卡面额不符合规则");
		infoMap.put(50100202, "卡无效（不存在或过期或者已注销）");
		infoMap.put(50100203, "卡内余额不足");
		infoMap.put(50100204, "无法连接支付中心或支付系统错误");
		infoMap.put(50100299, "其他错误");
		infoMap.put(20100300, "查询成功");
		infoMap.put(50100301, "查询的订单数过多，不能超过100个");
		infoMap.put(50100302, "查询失败");
		infoMap.put(20100400, "同步成功");
		infoMap.put(50100401, "同步失败或没有记录需要同步");
		infoMap.put(20100500, "查询成功");
		infoMap.put(50100501, "查询失败");*/
	}
	
	public String cardSaving(Passport passport, String cardtype, int payAmount, String cardno, String cardpass,
			 String server,String mobileOs){
		long startTime = System.currentTimeMillis();
		//先进行uc验证接口调用
		UcValidResult ucV = validUcInterface(passport, mobileOs);
		
		if(ucV.status != 20101100)
		{
			PlatformSavingCenter.logger.error("[UC充值订单生成] [失败] [UC接口验证] [失败] [响应吗:"+ucV.status+"] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
			return "";
		}
		else//验证成功
		{
			//过滤重复充值
			orderFormManager = OrderFormManager.getInstance();
			//貌似需要改成同步
			//生成订单
			OrderForm order = new OrderForm();
			order.setCreateTime(new Date().getTime());
			//设置充值平台
			order.setSavingPlatform("UC");
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
			order.setHandleResultDesp("UC新生成订单");
			//设置订单responseResult
			order.setResponseResult(-1);
			order.setResponseDesp("UC新生成订单");
			//设置是否通知游戏服状态 设为false
			order.setNotified(false);
			//设置通知游戏服是否成功 设为false
			order.setNotifySucc(false);
			//在备注中存入充值平台
			order.setMemo1(mobileOs);
			order.setUserChannel("UC_MIESHI");
			
			//先存入到数据库中
			order = orderFormManager.createOrderForm(order);
			if(PlatformSavingCenter.logger.isInfoEnabled())
				PlatformSavingCenter.logger.info("[UC充值订单生成] [生成订单] [插入新订单成功] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [订单的ID:"+order.getId()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			//设置订单号
			if(mobileOs.equalsIgnoreCase("ios"))
				order.setOrderId(DateUtil.formatDate(new Date(), "yyyy-MM-dd") + "-" + UC_IOS_APPID + "-"+ order.getId());
			else
				order.setOrderId(DateUtil.formatDate(new Date(), "yyyy-MM-dd") + "-" + UC_ANDDROID_APPID + "-"+ order.getId());
			try
			{
				orderFormManager.updateOrderForm(order, "orderId");
			}
			catch(Exception e)
			{
				PlatformSavingCenter.logger.error("[UC充值订单生成] [失败] [更新订单信息] [失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [订单的ID:"+order.getId()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
				return "";
			}
			
			if(PlatformSavingCenter.logger.isInfoEnabled())
				PlatformSavingCenter.logger.info("[UC充值订单生成] [生成订单] [更新订单信息成功] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [orderForm ID:"+order.getId()+"] [orderId:"+order.getOrderId()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			
			//调用uc的充值接口
			HashMap<String,String> params = new HashMap<String,String>();

			params.put("uid",passport.getUserName());
			
			if(mobileOs.equalsIgnoreCase("ios")){
				params.put("appId", String.valueOf(UC_IOS_APPID));
				
			}else{
				params.put("appId", String.valueOf(UC_ANDDROID_APPID));
			}
			
			//accessToken
			params.put("accessToken", ucV.accessToken);
			params.put("v", "1.0");
			params.put("format", "JSON");
			params.put("requestId", "1" + StringUtil.randomIntegerString(7));
			
			if(mobileOs.equalsIgnoreCase("ios")){
				params.put("sign", uc_sign(params, UC_IOS_Secretkey));
			}else{
				params.put("sign", uc_sign(params, UC_ANDDROID_Secretkey));
			}
			
			HashMap<String,String> dataMap = new HashMap<String, String>();
			dataMap.put("payType", cardTypeMap.get(cardtype)+"");
			dataMap.put("cardNo", cardno);
			dataMap.put("cardPassword", cardpass);
			dataMap.put("payAmount", (payAmount*1.0)/100+"");
			dataMap.put("uid", passport.getUserName());
			dataMap.put("cpId", UC_CPID+"");
			if(mobileOs.equalsIgnoreCase("ios"))
			{
				dataMap.put("gameId", UC_IOS_GAMEID+"");
				dataMap.put("serverId", UC_IOS_ServerID+"");
				dataMap.put("channelId", UC_IOS_CHANNELID+"");
			}
			else
			{
				dataMap.put("gameId", UC_ANDROID_GAMEID+"");
				dataMap.put("serverId", UC_ANDROID_ServerID+"");
				dataMap.put("channelId", UC_ANDDROID_CHANNELID+"");
			}
			
			dataMap.put("cpOrderId", order.getOrderId()+"");
			dataMap.put("customerParam", "");
			
			JacksonManager j = JacksonManager.getInstance();
			try {
				params.put("data", URLEncoder.encode(j.toJsonString(dataMap),"utf-8"));
			} catch (UnsupportedEncodingException e2) {
				PlatformSavingCenter.logger.error("[UC充值订单生成] [失败] [对data数据进行urlEncode] [失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [data:"+j.toJsonString(dataMap)+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e2);
				return "";
			}
			
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
			
			//拼接url和查询字符串
			String urlStr = RECHARGE_URL + "?" + strbuf.toString();
			URL url = null;
			try {
				url = new URL(urlStr);
			} catch (MalformedURLException e1) {
				PlatformSavingCenter.logger.error("[UC充值订单生成] [失败] [生成链接] [失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [url:"+urlStr+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e1);
				return "";
			}
			Map headers = new HashMap();
			String content = "";
			try
			{
				byte bytes[] = HttpUtils.webGet(url,headers, 60000, 60000);
				
				String encoding = (String)headers.get(HttpUtils.ENCODING);
				Integer code = (Integer)headers.get(HttpUtils.Response_Code);
				String uc_message = (String)headers.get(HttpUtils.Response_Message);
				
				content = new String(bytes,encoding);
				
				Map map =(Map)j.jsonDecodeObject(content);
				//下单成功
				int status = ((Number)map.get("status")).intValue();
				String message = (String)map.get("message");
				String sign = (String)map.get("sign");
				long time = (((Number)map.get("time"))).longValue();
				String cpid = (String.valueOf ((((Map)map.get("data")).get("cpId")))) ;
				String uc_orderid = (String)(((Map)map.get("data")).get("orderId"));
				
				if(status == 20100100)
				{
					//更新订单
					// 调用充值平台的结果，-1-未返回, 0-失败,1-成功  代表发送请求的响应
					order.setHandleResult(1);
					order.setHandleResultDesp("调用uc充值接口成功");
					orderFormManager.update(order);
					if(PlatformSavingCenter.logger.isInfoEnabled())
						PlatformSavingCenter.logger.info("[UC充值订单生成] [成功] [调用uc充值接口返回] [成功] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [orderForm ID:"+order.getId()+"] [orderId:"+order.getOrderId()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
					return order.getOrderId();
				}
				else//下单不成功
				{
					// 调用充值平台的结果，-1-未返回, 0-失败,1-成功  代表发送请求的响应
					order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
					order.setHandleResultDesp("调用uc充值接口成功");
					try
					{
						orderFormManager.update(order);
					}
					catch(Exception e)
					{
						PlatformSavingCenter.logger.error("[UC充值订单生成] [失败] [更新订单信息] [失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [订单的ID:"+order.getId()+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
					}
					
				/*	String errormessage = infoMap.get(status);
					if(errormessage == null)
						errormessage = infoMap.get(6010001);
					return errormessage;*/
					return order.getOrderId();
				}
				
			}
			catch(Exception e)
			{
				order.setHandleResult(OrderForm.HANDLE_RESULT_FAILED);
				order.setHandleResultDesp("调用uc充值接口失败");
				try {
					orderFormManager.update(order);
				} catch (Exception e1) {
					PlatformSavingCenter.logger.error("[UC充值订单生成] [失败] [更新订单信息] [失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [URL:"+urlStr+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e1);
				}
				PlatformSavingCenter.logger.error("[UC充值订单生成] [失败] [调用uc充值接口] [失败] [用户id:"+passport.getId()+"] [用户名："+passport.getUserName()+"] [充值类型:"+cardtype+"] [充值金额:"+payAmount+"] [充值卡号:"+cardno+"] [充值卡密码:"+cardpass+"] [游戏服务器名称:"+server+"] [手机平台:"+mobileOs+"] [URL:"+urlStr+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
				return "";
			}
				
			
		}
		
	}
	
	
	
	private class UcValidResult
	{
		public int status;
		public String message;
		public long time;
		public String uid;
		public String password;
		public String accessToken;
		public String sign;
	}
	
	public UcValidResult validUcInterface(Passport user,String mobileOs)
	{
		long startTime = System.currentTimeMillis();
		//调用uc的验证接口验证合法性
		HashMap<String,String> params = new HashMap<String,String>();

		params.put("uid",user.getUserName());
		params.put("password", user.getUcPassword());
		params.put("cpId", String.valueOf(UC_CPID));
		
		if(mobileOs.equalsIgnoreCase("ios")){
			params.put("appId", String.valueOf(UC_IOS_APPID));
			params.put("gameId", String.valueOf(UC_IOS_GAMEID));
			
		}else{
			params.put("appId", String.valueOf(UC_ANDDROID_APPID));
			params.put("gameId", String.valueOf(UC_ANDROID_GAMEID));
		}
		params.put("authType", "1");
		params.put("requestId", "1" + StringUtil.randomIntegerString(7));
	
		
		String queryString = "";
		if(mobileOs.equalsIgnoreCase("ios")){
			queryString = uc_sign(params,UC_IOS_Secretkey);
		}else{
			queryString = uc_sign(params,UC_ANDDROID_Secretkey);
		}
		
		String url = UC_VALID_URL + "?" + queryString;
		HashMap headers = new HashMap();

		String content =  "";
		try {
			byte bytes[] = HttpUtils.webGet(new java.net.URL(url), headers, 60000, 60000);
			
			String encoding = (String)headers.get(HttpUtils.ENCODING);
			Integer code = (Integer)headers.get(HttpUtils.Response_Code);
			String uc_message = (String)headers.get(HttpUtils.Response_Message);
			
			content = new String(bytes,encoding);
			
			if(PlatformSavingCenter.logger.isInfoEnabled())
				PlatformSavingCenter.logger.info("[UC充值订单生成] [调用UC验证接口] [成功] [用户id:"+user.getId()+"] [userName:" + user.getUserName() + "] " +
												"[手机平台:"+mobileOs+"] [URL:"+url+"] [code:"+code+"] [uc Message:"+uc_message+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			UcValidResult ucV = new UcValidResult();
			ucV.status = 6010000;
			PlatformSavingCenter.logger.error("[UC充值订单生成] [调用UC验证接口] [失败] [用户id:"+user.getId()+"] [userName:" + user.getUserName() + "] " +
					"[手机平台:"+user.getLastLoginMobileOs()+"] [URL:"+url+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
			
			return ucV;
		}
		
		
		JacksonManager jm = JacksonManager.getInstance();
		try {
			Map map =(Map)jm.jsonDecodeObject(content);
			UcValidResult ucV = new UcValidResult();
			ucV.status = ((Number)map.get("status")).intValue();
			ucV.message = (String)map.get("message");
			ucV.message = URLDecoder.decode(ucV.message, "utf-8");
			ucV.uid = (String.valueOf ((((Map)map.get("data")).get("uid")))) ;
			
			ucV.password = (String)(((Map)map.get("data")).get("password"));
			ucV.accessToken = (String)(((Map)map.get("data")).get("accessToken"));
			ucV.sign = (String)map.get("sign");
			ucV.time = (((Number)map.get("time"))).longValue();
			if(PlatformSavingCenter.logger.isInfoEnabled())
				PlatformSavingCenter.logger.info("[UC充值订单生成] [解析验证接口响应数据] [成功] [用户id:"+user.getId()+"] [userName:" + user.getUserName() + "] " +
					"[手机平台:"+mobileOs+"] [URL:"+url+"] ["+content+"] [status:"+ucV.status+"] [accesstoken:"+ucV.accessToken+"] [sign:"+ucV.sign+"] [time:"+ucV.time+"] [UC_UID:"+ucV.uid+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			
			return ucV;
		}catch (Exception e) {
			UcValidResult ucV = new UcValidResult();
			ucV.status = 6010000;
			PlatformSavingCenter.logger.error("[UC充值订单生成] [解析验证接口响应数据] [失败] [用户id:"+user.getId()+"] [userName:" + user.getUserName() + "] " +
					"[手机平台:"+mobileOs+"] [URL:"+url+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);	
			return ucV;
		}
	}
	
	
	private String uc_sign(HashMap<String,String> params,String secretkey){
		String appId = params.get("appId");
		String requestId = params.get("requestId");
		
		String keys[] = params.keySet().toArray(new String[0]);
		java.util.Arrays.sort(keys);
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < keys.length ; i++){
			String v = params.get(keys[i]);
			sb.append(keys[i]+"="+v);
		}
		String md5Str = appId+secretkey+requestId+sb.toString();
		
		String sign = StringUtil.hash(md5Str);
		
		sb = new StringBuffer();
		for(int i = 0 ; i < keys.length ; i++){
			String v = params.get(keys[i]);
			sb.append(keys[i]+"="+v+"&");
		}
		sb.append("sign="+sign);
		
		return sb.toString();
	}
	
	public OrderFormManager getOrderFormManager() {
		return orderFormManager;
	}

	public void setOrderFormManager(OrderFormManager orderFormManager) {
		this.orderFormManager = orderFormManager;
	}
	
	public static UCWebSavingManager getInstance(){
		return UCWebSavingManager.self;
	}
}
