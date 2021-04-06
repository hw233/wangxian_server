package com.fy.gamegateway.thirdpartner.huiyao;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.fy.gamegateway.mieshi.waigua.NewLoginHandler;
import com.xuanzhi.tools.servlet.HttpUtils;


public class HuiYaoClientService {
	private static HuiYaoClientService self;
	private final static String url = "http://api.hygame.cc/user/checkToken";
	
	public static HuiYaoClientService getInstance(){
		if(self == null){
			self = new HuiYaoClientService();
		}
		return self;
	}
	
	
	public boolean checkLogin(String username,String pwd){
		long startTime = System.currentTimeMillis();
		String guid = username.split("HUIYAOUSER_")[1];
		
		LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();
		params.put("guid",guid);
		params.put("token",pwd);
		
		String paramString = buildParams(params);
		HashMap headers = new HashMap();
		String content =  "";
		
		try {
			byte bytes[] = HttpUtils.webPost(new java.net.URL(url), paramString.getBytes("utf-8"), headers, 60000, 60000);
			String encoding = (String)headers.get(HttpUtils.ENCODING);
			Integer code = (Integer)headers.get(HttpUtils.Response_Code);
			String message = (String)headers.get(HttpUtils.Response_Message);
			content = new String(bytes,encoding);
			
	    	JSONObject jo = new JSONObject(content);
	    	 int status = jo.optInt("status", 0);
	    	 if(status == 0 ){
	        	NewLoginHandler.logger.info("[辉耀调用登录接口] [成功] [paramString:"+paramString+"] [code:"+code+"] ["+message+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
	        	return true;
	         } else{
	        	 NewLoginHandler.logger.info("[辉耀调用登录接口] [失败] [paramString:"+paramString+"] [code:"+code+"] ["+message+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
	         }
		} catch (Exception e) {
			NewLoginHandler.logger.info("[辉耀调用登录接口] [异常] [paramString:"+paramString+"] [url:"+url+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
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
