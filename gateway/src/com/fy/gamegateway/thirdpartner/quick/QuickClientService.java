package com.fy.gamegateway.thirdpartner.quick;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.fy.gamegateway.mieshi.server.MieshiGatewaySubSystem;
import com.xuanzhi.tools.servlet.HttpUtils;



public class QuickClientService {
	
	static Logger logger = Logger.getLogger(MieshiGatewaySubSystem.class);

	public static String APPID = "2882303761517739782";//"2882303761517739244";//"3087";
	public static String APPKEY = "5581773990782";//"5561773994244";//"512e79c0-a81e-c2af-38b7-505af006f12f";
	public static String AppSecret = "f6JGesLqklrDfsoZPKUn5A==";//"Rj572AZx7gSt7NbHj/ZMWw==";//"512e79c0-a81e-c2af-38b7-505af006f12f";
	public static String SESSION_CHECK = "http://mis.migc.xiaomi.com/api/biz/service/verifySession.do";
	public static String url = "http://checkuser.sdk.quicksdk.net/v2/checkUserInfo";
	
	private static QuickClientService self;
	
	public static QuickClientService getInstance() {
		if(self == null) {
			self = new QuickClientService();
		}
		return self;
	}
	
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
	
	public boolean login(String username,String password){
		try{
			long startTime = System.currentTimeMillis();
			String guid = username.split("QUICKUSER_")[1];
			LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();

			params.put("token",password);
			params.put("product_code","73886996385349016330751853648825");
			params.put("uid",guid);

			HashMap headers = new HashMap();

			String content = buildParams(params);
			if(logger.isDebugEnabled()){
				logger.debug("[调用quick登陆接口] [拼装参数] [username:"+username+"] ["+password+"] ["+content+"]");
			}

			try {
				byte bytes[] = HttpUtils.webPost(new java.net.URL(url), content.getBytes(), headers, 5000, 10000);

				String encoding = (String)headers.get(HttpUtils.ENCODING);
				Integer code = (Integer)headers.get(HttpUtils.Response_Code);
				String message = (String)headers.get(HttpUtils.Response_Message);
				content = new String(bytes,encoding);
				if(logger.isInfoEnabled())
					logger.info("[调用quick登陆接口] [成功] ["+url+"] [code:"+code+"] ["+message+"] ["+content+"] [用户名:"+username+"] [密码:"+password+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			} catch (Exception e) {
				logger.error("[调用quick登陆接口] [失败] ["+url+"] [code:--] [--] [sign:"+params.get("verifystring")+"] ["+content+"] [用户名:"+username+"] [密码:"+password+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
				return false;
			}
			
			if(content == null || !content.equals("1")){
				logger.info("[调用quick登陆接口] [成功] ["+url+"] [content:"+content+"] [用户名:"+username+"] ["+password+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
				return false;
			}

			return true;
		}catch(Exception e){
			return false;
		}

	}
	
	public boolean login2(String username,String password){
		try{
			long startTime = System.currentTimeMillis();
			String guid = username.split("QUICKUSER2_")[1];
			LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();

			params.put("token",password);
			params.put("product_code","99432701385976645187949420265805");
			params.put("uid",guid);

			HashMap headers = new HashMap();

			String content = buildParams(params);
			if(logger.isDebugEnabled()){
				logger.debug("[调用quick2登陆接口] [拼装参数] [username:"+username+"] ["+password+"] ["+content+"]");
			}

			try {
				byte bytes[] = HttpUtils.webPost(new java.net.URL(url), content.getBytes(), headers, 5000, 10000);

				String encoding = (String)headers.get(HttpUtils.ENCODING);
				Integer code = (Integer)headers.get(HttpUtils.Response_Code);
				String message = (String)headers.get(HttpUtils.Response_Message);
				content = new String(bytes,encoding);
				if(logger.isInfoEnabled())
					logger.info("[调用quick2登陆接口] [成功] ["+url+"] [code:"+code+"] ["+message+"] ["+content+"] [用户名:"+username+"] [密码:"+password+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			} catch (Exception e) {
				logger.error("[调用quick2登陆接口] [失败] ["+url+"] [code:--] [--] [sign:"+params.get("verifystring")+"] ["+content+"] [用户名:"+username+"] [密码:"+password+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
				return false;
			}
			
			if(content == null || !content.equals("1")){
				logger.info("[调用quick2登陆接口] [成功] ["+url+"] [content:"+content+"] [用户名:"+username+"] ["+password+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
				return false;
			}

			return true;
		}catch(Exception e){
			return false;
		}

	}
	
	public boolean login3(String username,String password){
		try{
			long startTime = System.currentTimeMillis();
			String guid = username.split("QUICKUSER3_")[1];
			LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();

			params.put("token",password);
			params.put("product_code","25932106876645476420383659946888");
			params.put("uid",guid);

			HashMap headers = new HashMap();

			String content = buildParams(params);
			if(logger.isDebugEnabled()){
				logger.debug("[调用quick3登陆接口] [拼装参数] [username:"+username+"] ["+password+"] ["+content+"]");
			}

			try {
				byte bytes[] = HttpUtils.webPost(new java.net.URL(url), content.getBytes(), headers, 5000, 10000);

				String encoding = (String)headers.get(HttpUtils.ENCODING);
				Integer code = (Integer)headers.get(HttpUtils.Response_Code);
				String message = (String)headers.get(HttpUtils.Response_Message);
				content = new String(bytes,encoding);
				if(logger.isInfoEnabled())
					logger.info("[调用quick3登陆接口] [成功] ["+url+"] [code:"+code+"] ["+message+"] ["+content+"] [用户名:"+username+"] [密码:"+password+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			} catch (Exception e) {
				logger.error("[调用quick3登陆接口] [失败] ["+url+"] [code:--] [--] [sign:"+params.get("verifystring")+"] ["+content+"] [用户名:"+username+"] [密码:"+password+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
				return false;
			}
			
			if(content == null || !content.equals("1")){
				logger.info("[调用quick3登陆接口] [成功] ["+url+"] [content:"+content+"] [用户名:"+username+"] ["+password+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
				return false;
			}

			return true;
		}catch(Exception e){
			return false;
		}

	}
	
}
