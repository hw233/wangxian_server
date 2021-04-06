package com.fy.gamegateway.thirdpartner.huiyao;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fy.gamegateway.mieshi.waigua.NewLoginHandler;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.text.StringUtil;


public class GuoPanClientService {
	private static GuoPanClientService self;
	private final static String url = "http://userapi.guopan.cn/gamesdk/verify/";
	private final static String appId = "117258";
	private final static String appId2 = "117260";
	private final static String SERVER_KEY = "HGHGS124382H2STGY0TW2M2CQQCUADBET7XSLXTGL8AG91F60PEHLJSNYLFXU15S";
	private final static String SERVER_KEY2 = "SQF995DH2YKSCXOMGHHYFFU1JC26B6QAZXIJD2I83LOJRV538NFVSUGZS1Y0XOWQ";
	
	public static GuoPanClientService getInstance(){
		if(self == null){
			self = new GuoPanClientService();
		}
		return self;
	}
	
	public boolean checkLogin2(String username,String pwd){
		long startTime = System.currentTimeMillis();
		String guid = username.split("GUOPANSDKUSER_")[1];
		String t = String.valueOf(startTime/1000);
		LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();
		params.put("game_uin",guid);
		params.put("appid",appId2);
		params.put("token",pwd);
		params.put("t",t);
		
		//sign=md5(game_uin+appid+t+SERVER_KEY) 
		String mysign = StringUtil.hash(guid+appId2+t+SERVER_KEY2);
		params.put("sign",mysign);
		
		
		String paramString = buildParams(params);
		HashMap headers = new HashMap();
		String content =  "";
		
		try {
			java.net.URL uuu = new java.net.URL(url+"?"+paramString); 
			byte bytes[] = HttpUtils.webGet(uuu, headers, 5000, 5000);
			String encoding = (String)headers.get(HttpUtils.ENCODING);
			Integer code = (Integer)headers.get(HttpUtils.Response_Code);
			String message = (String)headers.get(HttpUtils.Response_Message);
			content = new String(bytes,encoding);
	    	 if(content != null && content.equals("true")){
	        	NewLoginHandler.logger.info("[果盘2调用登录接口] [成功] [uuu:"+uuu+"] [code:"+code+"] ["+message+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
	        	return true;
	         } else{
	        	 NewLoginHandler.logger.info("[果盘2调用登录接口] [失败] [uuu:"+uuu+"] [code:"+code+"] ["+message+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
	         }
		} catch (Exception e) {
			NewLoginHandler.logger.info("[果盘2调用登录接口] [异常] [guid:"+guid+"] ["+pwd+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		return false;
	}
	
	public boolean checkLogin(String username,String pwd){
		long startTime = System.currentTimeMillis();
		String guid = username.split("GUOPANSDKUSER_")[1];
		String t = String.valueOf(startTime/1000);
		LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();
		params.put("game_uin",guid);
		params.put("appid",appId);
		params.put("token",pwd);
		params.put("t",t);
		
		//sign=md5(game_uin+appid+t+SERVER_KEY) 
		String mysign = StringUtil.hash(guid+appId+t+SERVER_KEY);
		params.put("sign",mysign);
		
		
		String paramString = buildParams(params);
		HashMap headers = new HashMap();
		String content =  "";
		
		try {
			java.net.URL uuu = new java.net.URL(url+"?"+paramString); 
			byte bytes[] = HttpUtils.webGet(uuu, headers, 5000, 5000);
			String encoding = (String)headers.get(HttpUtils.ENCODING);
			Integer code = (Integer)headers.get(HttpUtils.Response_Code);
			String message = (String)headers.get(HttpUtils.Response_Message);
			content = new String(bytes,encoding);
	    	 if(content != null && content.equals("true")){
	        	NewLoginHandler.logger.info("[果盘调用登录接口] [成功] [uuu:"+uuu+"] [code:"+code+"] ["+message+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
	        	return true;
	         } else{
	        	 NewLoginHandler.logger.info("[果盘调用登录接口] [失败] [uuu:"+uuu+"] [code:"+code+"] ["+message+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
	         }
		} catch (Exception e) {
			NewLoginHandler.logger.info("[果盘调用登录接口] [异常] [guid:"+guid+"] ["+pwd+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
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
