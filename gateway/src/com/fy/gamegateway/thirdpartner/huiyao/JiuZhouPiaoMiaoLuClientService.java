package com.fy.gamegateway.thirdpartner.huiyao;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.fy.gamegateway.mieshi.waigua.NewLoginHandler;
import com.fy.gamegateway.tools.JacksonManager;
import com.xuanzhi.tools.servlet.HttpUtils;


public class JiuZhouPiaoMiaoLuClientService {
	private static JiuZhouPiaoMiaoLuClientService self;
	private final static String url = "http://c7sdk.c7game.com/sdk.php/LoginNotify/login_verify";
	
	public static JiuZhouPiaoMiaoLuClientService getInstance(){
		if(self == null){
			self = new JiuZhouPiaoMiaoLuClientService();
		}
		return self;
	}
	
	
	public boolean checkLogin(String username,String pwd){
		long startTime = System.currentTimeMillis();
		String guid = username.split("JIUZHOUPIAOMIAOUSER_")[1];
		
		LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();
		params.put("user_id",guid);
		params.put("token",pwd);
		
		JacksonManager jm = new JacksonManager();
		
//		String paramString = buildParams(params);
		HashMap headers = new HashMap();
		String content =  "";
		
		try {
			String jsonStr = jm.toJsonString(params);
			byte bytes[] = HttpUtils.webPost(new java.net.URL(url), jsonStr.getBytes("utf-8"), headers, 60000, 60000);
			String encoding = (String)headers.get(HttpUtils.ENCODING);
			Integer code = (Integer)headers.get(HttpUtils.Response_Code);
			String message = (String)headers.get(HttpUtils.Response_Message);
			content = new String(bytes,encoding);
			
	    	JSONObject jo = new JSONObject(content);
	    	 int status = jo.optInt("status", 0);
	    	 if(status == 1){
	        	NewLoginHandler.logger.info("[九州飘渺曲调用登录接口] [成功] [paramString:"+jsonStr+"] [code:"+code+"] ["+message+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
	        	return true;
	         } else{
	        	 NewLoginHandler.logger.info("[九州飘渺曲调用登录接口] [失败] [paramString:"+jsonStr+"] [code:"+code+"] ["+message+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
	         }
		} catch (Exception e) {
			NewLoginHandler.logger.info("[九州飘渺曲调用登录接口] [异常] [url:"+url+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		return false;
	}
	
	public boolean checkLogin2(String username,String pwd){
		long startTime = System.currentTimeMillis();
		String guid = username.split("XUNXIANJUEUSER_")[1];
		
		LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();
		params.put("user_id",guid);
		params.put("token",pwd);
		
		JacksonManager jm = new JacksonManager();
		
//		String paramString = buildParams(params);
		HashMap headers = new HashMap();
		String content =  "";
		
		try {
			String jsonStr = jm.toJsonString(params);
			byte bytes[] = HttpUtils.webPost(new java.net.URL(url), jsonStr.getBytes("utf-8"), headers, 60000, 60000);
			String encoding = (String)headers.get(HttpUtils.ENCODING);
			Integer code = (Integer)headers.get(HttpUtils.Response_Code);
			String message = (String)headers.get(HttpUtils.Response_Message);
			content = new String(bytes,encoding);
			
	    	JSONObject jo = new JSONObject(content);
	    	 int status = jo.optInt("status", 0);
	    	 if(status == 1){
	        	NewLoginHandler.logger.info("[九州飘渺曲调用登录接口] [成功] [paramString:"+jsonStr+"] [code:"+code+"] ["+message+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
	        	return true;
	         } else{
	        	 NewLoginHandler.logger.info("[九州飘渺曲调用登录接口] [失败] [paramString:"+jsonStr+"] [code:"+code+"] ["+message+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
	         }
		} catch (Exception e) {
			NewLoginHandler.logger.info("[九州飘渺曲调用登录接口] [异常] [url:"+url+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		return false;
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
}
