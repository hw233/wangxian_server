package com.fy.gamegateway.thirdpartner.duoku;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.fy.gamegateway.mieshi.server.MieshiGatewaySubSystem;
import com.fy.gamegateway.tools.JacksonManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.text.StringUtil;



public class DuokuBossClientService {
	
	static Logger logger = Logger.getLogger(DuokuBossClientService.class);
	private String client_id = "e3941454403d6a9b8754dc1bf1351eb3";
	private String game_id = "118";
	private String aid = "";
	private String session_secret = "c95b5b9ccfa1c38d022340a12336a6a4";
	
	private static  Map<String,String> tipMap = new ConcurrentHashMap<String, String>();
	
	static
	{
				tipMap.put("1","用户名密码错误");                        
				tipMap.put("2","用户名密码错误");           
				tipMap.put("3","用户名密码错误");                
				tipMap.put("4","用户名密码错误");            
				tipMap.put("5","用户名密码错误");         
				tipMap.put("6","用户名密码错误");         
				tipMap.put("7","用户名密码错误");                          
				tipMap.put("8","用户名密码错误");                            
				tipMap.put("9" ,"用户名密码错误");        
				tipMap.put("10","用户名密码错误");
				tipMap.put("11","用户名密码错误");               
				tipMap.put("12","用户名密码错误");                            
				tipMap.put("13","用户名不存在");                   
				tipMap.put("100","用户名密码错误");                         
				tipMap.put("101","用户名密码错误");                      
				tipMap.put("102","session过期，请重新登陆");                      
				tipMap.put("103","用户名密码错误");                      
				tipMap.put("104","用户名密码错误");                           
				tipMap.put("105","用户名密码错误");             
				tipMap.put("106","用户名已存在，请换一个用户名注册");             
				tipMap.put("107","用户名不合法，请换一个用户名注册");   
				tipMap.put("108","用户名密码错误");  
	}
	
	private static DuokuBossClientService self;
	public static DuokuBossClientService getInstance(){
		if(self != null){
			return self;
		}
		self = new DuokuBossClientService();
		return self;
	}
	
	public RegisterResultNew register(String platform, String username,String mobile,String password,String channel){
		try
		{
			if(channel.toLowerCase().contains("baiduyy"))
			{
				client_id = "e3941454403d6a9b8754dc1bf13baidu";
				game_id = "136";
				session_secret = "c95b5b9ccfa1c38d022340a1233baidu";
			}
			else
			{
				client_id = "e3941454403d6a9b8754dc1bf1351eb3";
				game_id = "118";
				session_secret = "c95b5b9ccfa1c38d022340a12336a6a4";
			}
			
			long startTime = System.currentTimeMillis();
			
//			String registerUrl  = "http://duokoo.baidu.com/openapi/client/register";
	//		String registerUrl  = "http://sdk.m.duoku.com/openapi/client/register";
			String registerUrl  = "http://dsdk.m.duoku.com/openapi/client/register";
			
			LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();
	
			params.put("client_id",client_id);
			params.put("game_id", game_id);
			params.put("aid",aid);
			params.put("user_name",username);
			params.put("user_pwd", password);
			params.put("client_secret",sign(params).toLowerCase());
			
			HashMap headers = new HashMap();
	
			String content =  "";
			content = buildParams(params);
			if(logger.isDebugEnabled())
			{
				logger.debug("[调用注册接口] [拼装参数] ["+content+"] [platform:"+platform+"] [username:"+username+"] [mobile:"+mobile+"] [password:"+password+"] [client_id:"+client_id+"] [game_id:"+game_id+"] [session_secret:"+session_secret+"]");
			}
			
			try {
				byte bytes[] = HttpUtils.webPost(new java.net.URL(registerUrl), content.getBytes(), headers, 5000, 10000);
				
				String encoding = (String)headers.get(HttpUtils.ENCODING);
				Integer code = (Integer)headers.get(HttpUtils.Response_Code);
				String message = (String)headers.get(HttpUtils.Response_Message);
				content = new String(bytes,encoding);
				if(logger.isInfoEnabled())
					logger.info("[调用注册接口] ["+platform+"] [成功] ["+registerUrl+"] [code:"+code+"] ["+message+"] ["+content+"] [用户名:"+username+"] [密码:"+password+"] [mobile:"+mobile+"] [client_id:"+client_id+"] [game_id:"+game_id+"] [session_secret:"+session_secret+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			} catch (Exception e) {
				logger.error("[调用注册接口] ["+platform+"] [失败] ["+registerUrl+"] [code:--] [--] [sign:"+params.get("verifystring")+"] ["+content+"] [用户名:"+username+"] [密码:"+password+"] [mobile:"+mobile+"] [client_id:"+client_id+"] [game_id:"+game_id+"] [session_secret:"+session_secret+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
				RegisterResultNew r = new RegisterResultNew();
				r.code = "404";
				r.message = "调用多酷注册接口出现异常";
				r.message = "用户名或者密码错误";
				return r;
			}
			
			//解析返回结果
			JacksonManager jm = JacksonManager.getInstance();
			RegisterResultNew r = new RegisterResultNew();
			try {
				Map map =(Map)jm.jsonDecodeObject(content);
				if(!StringUtils.isEmpty((String)map.get("error_code")))
				{
					r.code = (String)map.get("error_code");
					String tip = (String)map.get("error_msg");
					r.message = tipMap.get(r.code);
					if(StringUtils.isEmpty(r.message))
					{
						r.message = tip;
					}
				}
				
				r.uid = (String)map.get("uid");
				r.accessToken= (String)map.get("access_token");
				r.clientSecret = (String)map.get("client_secret");
				
	/*			boolean isSucc = false;
				if(!StringUtils.isEmpty(r.clientSecret))
				{
					if(r.clientSecret.equalsIgnoreCase(StringUtil.hash(r.uid)))
				}*/
				
				if(logger.isInfoEnabled())
				{
					logger.info("[多酷注册] ["+(StringUtils.isEmpty(r.uid) ? "失败" : "成功")+"] [字符集:"+(String)headers.get(HttpUtils.ENCODING)+"] [code:"+(Integer)headers.get(HttpUtils.Response_Code)+"] [响应信息:"+(String)headers.get(HttpUtils.Response_Message)+"] [content:"+content+"] [用户名:"+username+"] [密码:"+password+"] [client_id:"+client_id+"] [game_id:"+game_id+"] [session_secret:"+session_secret+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
				}
			} catch (Exception e) {
				logger.error("[读取多酷注册接口JSON内容] [失败] [解析响应内容信息] [失败] [字符集:"+(String)headers.get(HttpUtils.ENCODING)+"] [code:"+(Integer)headers.get(HttpUtils.Response_Code)+"] [响应信息:"+(String)headers.get(HttpUtils.Response_Message)+"] [content:"+content+"] [用户名:"+username+"] [密码:"+password+"] [client_id:"+client_id+"] [game_id:"+game_id+"] [session_secret:"+session_secret+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
				r.code = "500";
				r.message = "读取多酷注册接口内容出现异常";
				return r;
			}
			return r;
		}
		catch(Exception e)
		{
			RegisterResultNew r = new RegisterResultNew();
			r.code = "500";
			r.message = "读取多酷注册接口内容出现异常";
			logger.error("[去多酷注册] [失败:出现未知异常] [platform:"+platform+"] [username:"+username+"] [password:"+password+"] [mobile:"+mobile+"] [client_id:"+client_id+"] [game_id:"+game_id+"] [session_secret:"+session_secret+"]",e);
			return r;
		}
	}
	
	private String buildParams(Map<String,String> params)
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
	
	public DuokuLoginResult login(String username,String password,String channel){
		try
		{
			if(channel.toLowerCase().contains("baiduyy"))
			{
				client_id = "e3941454403d6a9b8754dc1bf13baidu";
				game_id = "136";
				session_secret = "c95b5b9ccfa1c38d022340a1233baidu";
			}
			else
			{
				client_id = "e3941454403d6a9b8754dc1bf1351eb3";
				game_id = "118";
				session_secret = "c95b5b9ccfa1c38d022340a12336a6a4";
			}
			
			
			long startTime = System.currentTimeMillis();

//			String url = "http://duokoo.baidu.com/openapi/client/login";
			String url = "http://dsdk.m.duoku.com/openapi/client/login";

			LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();

			params.put("client_id",client_id);
			params.put("game_id", game_id);
			params.put("aid",aid);
			params.put("user_name",username);

			//参与签名的字符串
			String paramPass = Authcode.Encode(password, "zssghfwldk");
			String signPass = URLDecoder.decode(paramPass,"utf-8");


			params.put("user_pwd", signPass);
			params.put("client_secret",sign(params).toLowerCase());
			params.put("user_pwd", paramPass);


			HashMap headers = new HashMap();

			String content =  "";
			content = buildParams(params);
			if(logger.isDebugEnabled())
			{
				logger.debug("[调用登陆接口] [拼装参数] ["+content+"] [client_id:"+client_id+"] [game_id:"+game_id+"] [session_secret:"+session_secret+"] [参与签名的密码字符串:"+signPass+"] [参与传参的密码字符串:"+paramPass+"]");
			}

			try {
				byte bytes[] = HttpUtils.webPost(new java.net.URL(url), content.getBytes(), headers, 5000, 10000);

				String encoding = (String)headers.get(HttpUtils.ENCODING);
				Integer code = (Integer)headers.get(HttpUtils.Response_Code);
				String message = (String)headers.get(HttpUtils.Response_Message);
				content = new String(bytes,encoding);
				if(logger.isInfoEnabled())
					logger.info("[调用登陆接口] [成功] ["+url+"] [code:"+code+"] ["+message+"] ["+content+"] [用户名:"+username+"] [密码:"+password+"] [client_id:"+client_id+"] [game_id:"+game_id+"] [session_secret:"+session_secret+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			} catch (Exception e) {
				logger.error("[调用登陆接口] [失败] ["+url+"] [code:--] [--] [sign:"+params.get("verifystring")+"] ["+content+"] [用户名:"+username+"] [密码:"+password+"] [client_id:"+client_id+"] [game_id:"+game_id+"] [session_secret:"+session_secret+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
				DuokuLoginResult r = new DuokuLoginResult();
				r.code = "404";
				r.message = "调用多酷登陆接口出现异常";
				r.message = "用户名或者密码错误";
				return r;
			}

			//解析返回结果
			JacksonManager jm = JacksonManager.getInstance();
			DuokuLoginResult r = new DuokuLoginResult();
			try {
				Map map =(Map)jm.jsonDecodeObject(content);
				if(!StringUtils.isEmpty((String)map.get("error_code")))
				{
					r.code = (String)map.get("error_code");
					String tip = (String)map.get("error_msg");
					r.message = tipMap.get(r.code);
					if(StringUtils.isEmpty(r.message))
					{
						r.message = tip;
					}
				}

				r.uid = (String)map.get("uid");
				r.accessToken= (String)map.get("access_token");
				r.clientSecret = (String)map.get("client_secret");

				/*			boolean isSucc = false;
				if(!StringUtils.isEmpty(r.clientSecret))
				{
					if(r.clientSecret.equalsIgnoreCase(StringUtil.hash(r.uid)))
				}*/

				if(logger.isInfoEnabled())
				{
					logger.info("[多酷登陆] ["+(StringUtils.isEmpty(r.uid) ? "失败" : "成功")+"] [字符集:"+(String)headers.get(HttpUtils.ENCODING)+"] [code:"+(Integer)headers.get(HttpUtils.Response_Code)+"] [响应信息:"+(String)headers.get(HttpUtils.Response_Message)+"] [content:"+content+"] [用户名:"+username+"] [密码:"+password+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
				}
			} catch (Exception e) {
				logger.error("[读取多酷登陆接口JSON内容] [失败] [解析响应内容信息] [失败] [字符集:"+(String)headers.get(HttpUtils.ENCODING)+"] [code:"+(Integer)headers.get(HttpUtils.Response_Code)+"] [响应信息:"+(String)headers.get(HttpUtils.Response_Message)+"] [content:"+content+"] [用户名:"+username+"] [密码:"+password+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
				logger.error("[读取多酷登陆接口JSON内容] [失败] [解析响应内容信息] [失败] [字符集:"+(String)headers.get(HttpUtils.ENCODING)+"] [code:"+(Integer)headers.get(HttpUtils.Response_Code)+"] [响应信息:"+(String)headers.get(HttpUtils.Response_Message)+"] [content:"+content+"] [用户名:"+username+"] [密码:"+password+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
				r.code = "500";
				r.message = "读取多酷登陆接口内容出现异常";
				r.message = "用戶名或密码錯誤";
				return r;
			}
			return r;
		}
		catch(Exception e)
		{
			DuokuLoginResult r = new DuokuLoginResult();
			r.code = "500";
			r.message = "读取多酷登陆接口内容出现异常";
			r.message = "未知错误";
			
			logger.error("[多酷登陆] [失败:出现未知异常]",e);
			return r;
		}

	}
	
	
	private  String sign(LinkedHashMap<String,String> params){
		String values[] = params.values().toArray(new String[0]);
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < values.length ; i++){
			sb.append(values[i]);
		}
		String md5Str = sb.toString()+session_secret;
		String sign = StringUtil.hash(md5Str);
		
		if(logger.isDebugEnabled())
		{
			logger.debug("[调用登陆接口] [拼装签名字符串] [签名前:"+md5Str+"] [签名后:"+sign+"] [client_id:"+client_id+"] [game_id:"+game_id+"] [session_secret:"+session_secret+"]");
		}
		
		return sign;
	}
	

}
