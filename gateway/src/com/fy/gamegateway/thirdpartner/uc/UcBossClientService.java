package com.fy.gamegateway.thirdpartner.uc;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;

import com.fy.gamegateway.tools.JacksonManager;
import com.xuanzhi.tools.text.StringUtil;
import com.xuanzhi.tools.servlet.HttpUtils;



public class UcBossClientService {
	
	static Logger logger = Logger.getLogger(UcBossClientService.class);

	public static int CPID = 149;
	public static int ANDROID_GAMEID = 63371;
	public static int ANDROID_ServerID = 907;
	public static int ANDDROID_CHANNELID = 2;
	public static int ANDDROID_APPID = 10024;
	public static String ANDDROID_Secretkey = "2b6f740373c5893c8b798798fbdda077";
	
	public static int IOS_GAMEID = 63586;
	public static int IOS_ServerID = 908 ;
	public static int IOS_CHANNELID = 2;
	public static int IOS_APPID = 10025;
	public static String IOS_Secretkey = "1c18543e919f196a46d3d65332c86b68";
	
	
	private static UcBossClientService self;
	public static UcBossClientService getInstance(){
		if(self != null){
			return self;
		}
		self = new UcBossClientService();
		return self;
	}
	
	public static String getResponseDescription(int status){
		if(status == 20101100){
			return "登录成功";
		}
		if(status == 50101101){
			return "调用UC接口，帐号不存在";
		}
		if(status == 50101102){
			return "调用UC接口，密码错误";
		}
		if(status == 50101103){
			return "调用UC接口，帐号被锁定";
		}
		if(status == 50101104){
			return "调用UC接口，验证接口异常";
		}
		if(status == 20101200){
			return "调用UC接口，同步数据成功";
		}
		if(status == 50101201){
			return "调用UC接口，游戏信息接口异常";
		}
		if(status == 50101202){
			return "调用UC接口，没有该游戏信息";
		}
		if(status == 20101400){
			return "注册成功";
		}
		if(status == 50101401){
			return "注册失败,手机号或密码为空";
		}
		if(status == 50101403){
			return "注册失败,手机号码不合法";
		}
		if(status == 50101402){
			return "注册失败，签名数据不一致，较验失败";
		}
		if(status == 50101498){
			return "您的手机号码已经注册过UC号，请用UC号直接登录！";
		}
		
		if(status == 50101400){
			return "注册失败，未知错误";
		}
		return "未知响应码";
	}
	
	
	public UCLoginResult login(String platform, String uid, String password){
		long startTime = System.currentTimeMillis();
		
		String url = "http://api.g.uc.cn/login";
		
		HashMap<String,String> params = new HashMap<String,String>();

		params.put("uid",String.valueOf(uid));
		params.put("password", password);
		params.put("cpId", String.valueOf(CPID));
		
		if(platform.equalsIgnoreCase("ios")){
			params.put("appId", String.valueOf(IOS_APPID));
			params.put("gameId", String.valueOf(IOS_GAMEID));
			
		}else{
			params.put("appId", String.valueOf(ANDDROID_APPID));
			params.put("gameId", String.valueOf(ANDROID_GAMEID));
		}
		
		params.put("requestId", "1" + StringUtil.randomIntegerString(7));
		params.put("authType", "0");
		
		String queryString = "";
		if(platform.equalsIgnoreCase("ios")){
			queryString = sign(params,IOS_Secretkey);
		}else{
			queryString = sign(params,ANDDROID_Secretkey);
		}
		
		url = url + "?" + queryString;
		HashMap headers = new HashMap();

		String content =  "";
		try {
			byte bytes[] = HttpUtils.webGet(new java.net.URL(url), headers, 5000, 10000);
			
			String encoding = (String)headers.get(HttpUtils.ENCODING);
			Integer code = (Integer)headers.get(HttpUtils.Response_Code);
			String message = (String)headers.get(HttpUtils.Response_Message);
			
			content = new String(bytes,encoding);
			
			
			logger.info("[调用登录接口] ["+platform+"] [成功] ["+url+"] [code:"+code+"] ["+message+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			logger.info("[调用登录接口] ["+platform+"] [失败] ["+url+"] [code:--] [--] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
			
			UCLoginResult r = new UCLoginResult();
			r.status = 50101104;
			r.message = "调用UC接口出现异常";
			r.uid = "";
			r.password = "";
			r.accessToken = "";
			r.sign = "";
			r.time = System.currentTimeMillis();
			
			return r;
		}
		
		JacksonManager jm = JacksonManager.getInstance();
		try {
			Map map =(Map)jm.jsonDecodeObject(content);
			UCLoginResult r = new UCLoginResult();
			r.status = ((Number)map.get("status")).intValue();
			r.message = (String)map.get("message");
			r.message = URLDecoder.decode(r.message, "utf-8");
			if(r.message.equals("请求参数不符，请参考API文档")) {
				r.message = "请使用数字UC帐号登录";
			}
			
			//如果从uc中返回的uid和传入的uid不符合
			if(!(String.valueOf ((((Map)map.get("data")).get("uid")))).equals(uid) )
			{
				//返回的作为用户名
				//传入的作为别名
				r.uid = (String.valueOf ((((Map)map.get("data")).get("uid"))));
				//r.ucName = (String.valueOf ((((Map)map.get("data")).get("uid")))) ;
			
			}
			else
			{
				//r.ucName = (String.valueOf ((((Map)map.get("data")).get("uid"))));
				r.uid = uid;
			}
			
			r.password = (String)(((Map)map.get("data")).get("password"));
			r.accessToken = (String)(((Map)map.get("data")).get("accessToken"));
			r.sign = (String)map.get("sign");
			r.time = (((Number)map.get("time"))).longValue();
			
			logger.info("[解析登录返回数据] ["+platform+"] [成功] ["+url+"] [status:"+r.status+"] ["+getResponseDescription(r.status)+"] [message:"+r.message+"] [password:"+r.password+"] [accessToken:"+r.accessToken+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			
			
			return r;
		}catch (Exception e) {
			UCLoginResult r = new UCLoginResult();
			r.status = 50101104;
			r.message = "解析JSON串出现异常";
			r.uid = "";
			r.password = "";
			r.accessToken = "";
			r.sign = "";
			r.time = System.currentTimeMillis();

			logger.info("[解析登录返回数据] ["+platform+"] [失败] ["+url+"] [status:"+r.status+"] ["+getResponseDescription(r.status)+"] [message:"+r.message+"] [password:"+r.password+"] [accessToken:"+r.accessToken+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
			
			return r;

		}

	}
	
	
	public void sendGameInfo(String platform, int uid, String actionCode,
			String gameServerInfo,String gameArea,String accessToken){
		long startTime = System.currentTimeMillis();
		
		String url = "http://api.g.uc.cn/sendGameInfo";
		
		HashMap<String,String> params = new HashMap<String,String>();

		params.put("uid",String.valueOf(uid));
		params.put("actionCode", actionCode);
		params.put("cpId", String.valueOf(CPID));
		
		if(platform.equalsIgnoreCase("ios")){
			params.put("appId", String.valueOf(IOS_APPID));
			params.put("gameId", String.valueOf(IOS_GAMEID));
			
		}else{
			params.put("appId", String.valueOf(ANDDROID_APPID));
			params.put("gameId", String.valueOf(ANDROID_GAMEID));
		}
		
		params.put("requestId", "1" + StringUtil.randomIntegerString(7));
		
		params.put("gameServerInfo", gameServerInfo);
		params.put("gameArea", gameArea);
		params.put("accessToken", accessToken);
		
		String queryString = "";
		if(platform.equalsIgnoreCase("ios")){
			queryString = sign(params,IOS_Secretkey);
		}else{
			queryString = sign(params,ANDDROID_Secretkey);
		}
		
		url = url + "?" + queryString;
		HashMap headers = new HashMap();

		String content =  "";
		try {
			byte bytes[] = HttpUtils.webGet(new java.net.URL(url), headers, 5000, 10000);
			
			String encoding = (String)headers.get(HttpUtils.ENCODING);
			Integer code = (Integer)headers.get(HttpUtils.Response_Code);
			String message = (String)headers.get(HttpUtils.Response_Message);
			
			content = new String(bytes,encoding);
			
			
			logger.info("[调用信息同步接口] ["+platform+"] [成功] ["+url+"] [code:"+code+"] ["+message+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			logger.info("[调用信息同步接口] ["+platform+"] [失败] ["+url+"] [code:--] [--] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
			return ;
		}
		
		JacksonManager jm = JacksonManager.getInstance();
		try {
			Map map =(Map)jm.jsonDecodeObject(content);
	
			int status = ((Number)map.get("status")).intValue();
			String message = (String)map.get("message");
			message = URLDecoder.decode(message, "utf-8");
			
			logger.info("[解析同步返回数据] ["+platform+"] [成功] ["+url+"] [status:"+status+"] ["+getResponseDescription(status)+"] [message:"+message+"] ["+(System.currentTimeMillis()-startTime)+"ms]");

		}catch (Exception e) {
		
			int status = 50101104;
			String message = "解析JSON串出现异常";
		
			logger.info("[解析登录返回数据] ["+platform+"] [失败] ["+url+"] [status:"+status+"] ["+getResponseDescription(status)+"] [message:"+message+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
		}

	}
	
	
	public RegisterResult register(String platform, String mobile,String password){
		long startTime = System.currentTimeMillis();
		
		String url = "http://api.g.uc.cn/register";
		
		HashMap<String,String> params = new HashMap<String,String>();

		params.put("mobile",mobile);
		params.put("password", password);
		params.put("cpId", String.valueOf(CPID));
		
		if(platform.equalsIgnoreCase("ios")){
			params.put("appId", String.valueOf(IOS_APPID));
			
		}else{
			params.put("appId", String.valueOf(ANDDROID_APPID));
		}
		
		params.put("requestId", "1" + StringUtil.randomIntegerString(7));
		
		String queryString = "";
		if(platform.equalsIgnoreCase("ios")){
			queryString = sign(params,IOS_Secretkey);
		}else{
			queryString = sign(params,ANDDROID_Secretkey);
		}
		
		url = url + "?" + queryString;
		HashMap headers = new HashMap();

		String content =  "";

		try {
			byte bytes[] = HttpUtils.webGet(new java.net.URL(url), headers, 5000, 10000);
			
			String encoding = (String)headers.get(HttpUtils.ENCODING);
			Integer code = (Integer)headers.get(HttpUtils.Response_Code);
			String message = (String)headers.get(HttpUtils.Response_Message);
			
			content = new String(bytes,encoding);
			
			
			logger.info("[调用注册接口] ["+platform+"] [成功] ["+url+"] [code:"+code+"] ["+message+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			logger.info("[调用注册接口] ["+platform+"] [失败] ["+url+"] [code:--] [--] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
			
			RegisterResult r = new RegisterResult();
			r.status = 50101499;
			r.message = "注册出现异常";
			r.uid = "";
			r.sign = "";
			return r;
		}
		
		JacksonManager jm = JacksonManager.getInstance();
		try {
			Map map =(Map)jm.jsonDecodeObject(content);
	
			
			RegisterResult r = new RegisterResult();
			r.status = ((Number)map.get("status")).intValue();
			r.message = (String)map.get("message");
			r.message = URLDecoder.decode(r.message, "utf-8");
			
			if(r.status == 20101400){
				r.uid = String.valueOf(((Map)map.get("data")).get("uid"));
			}
			
			r.sign = (String)map.get("sign");
			r.time = ((Long)map.get("time")).longValue();
			
			logger.info("[解析注册返回数据] ["+platform+"] [成功] ["+url+"] [status:"+r.status+"] ["+getResponseDescription(r.status)+"] [message:"+r.message+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			return r;
		}catch (Exception e) {
		
			RegisterResult r = new RegisterResult();
			r.status = 50101499;
			r.message = "解析JSON串出现异常";
			r.uid = "";
			r.sign = "";
			
			logger.info("[解析登录返回数据] ["+platform+"] [失败] ["+url+"] [status:"+r.status+"] ["+getResponseDescription(r.status)+"] [message:"+r.message+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
			
			return r;
		}

	}
	
	public static String sign(HashMap<String,String> params,String secretkey){
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

}
