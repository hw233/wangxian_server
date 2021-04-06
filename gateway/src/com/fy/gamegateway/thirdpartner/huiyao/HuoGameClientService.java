package com.fy.gamegateway.thirdpartner.huiyao;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.fy.gamegateway.mieshi.waigua.NewLoginHandler;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.text.StringUtil;


public class HuoGameClientService {
	private static HuoGameClientService self;
	private final static String url = "http://union.huoyx.cn/api/cp/user/check";
	public static final String app_key = "9828c666aff04ee10f32b212f6eddcec";
	public static final String app_key2 = "3b88f7c2f902288eff2d041436045941";
	
	public static HuoGameClientService getInstance(){
		if(self == null){
			self = new HuoGameClientService();
		}
		return self;
	}
	
	
	public boolean checkLogin(String username,String pwd){
		long startTime = System.currentTimeMillis();
		String mem_id = username.split("HUOGAMEUSER_")[1];
		
		LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();
		params.put("app_id",60183+"");
		params.put("mem_id",mem_id);
		params.put("user_token",pwd);
//		/sign 的签名规则：md5(app_id=...&mem_id=...&user_token=...&app_key=...)
		String mysign = StringUtil.hash("app_id="+60183+"&mem_id="+mem_id+"&user_token="+pwd+"&app_key="+app_key);
		params.put("sign",mysign);
		
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
	    	 if(status == 1 ){
	        	NewLoginHandler.logger.info("[火游戏调用登录接口] [成功] [paramString:"+paramString+"] [code:"+code+"] ["+message+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
	        	return true;
	         } else{
	        	 NewLoginHandler.logger.info("[火游戏调用登录接口] [失败] [paramString:"+paramString+"] [code:"+code+"] ["+message+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
	         }
		} catch (Exception e) {
			NewLoginHandler.logger.info("[火游戏调用登录接口] [异常] [paramString:"+paramString+"] [url:"+url+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		return false;
	}
	
	int para_id = 60100190;
	int app_id = 12499;
	public boolean checkLogin3(String username,String pwd){
		
//		long startTime = System.currentTimeMillis();
//		String user_id = username.split("HUOGAMEUSER3_")[1];
//		String child_para_id = "";
//		LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();
//		params.put("para_id",para_id+"");
//		params.put("app_id",app_id+"");
//		params.put("child_para_id",mem_id);
//		params.put("user_id",user_id);
//		//加密校验值MD5(para_id+app_id+child_para_id+sign)小写
//		String signstr = ""+para_id+""+app_id+child_para_id;
//		params.put("tn",signstr.toLowerCase());
//		String mysign = StringUtil.hash(signstr);
//		params.put("sign",mysign);
//		
//		String paramString = buildParams(params);
//		HashMap headers = new HashMap();
//		String content =  "";
//		
//		try {
//			byte bytes[] = HttpUtils.webPost(new java.net.URL(url), paramString.getBytes("utf-8"), headers, 60000, 60000);
//			String encoding = (String)headers.get(HttpUtils.ENCODING);
//			Integer code = (Integer)headers.get(HttpUtils.Response_Code);
//			String message = (String)headers.get(HttpUtils.Response_Message);
//			content = new String(bytes,encoding);
//			
//	    	JSONObject jo = new JSONObject(content);
//	    	 int status = jo.optInt("status", 0);
//	    	 if(status == 1 ){
//	        	NewLoginHandler.logger.info("[火游戏2调用登录接口] [成功] [paramString:"+paramString+"] [code:"+code+"] ["+message+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
//	        	return true;
//	         } else{
//	        	 NewLoginHandler.logger.info("[火游戏2调用登录接口] [失败] [paramString:"+paramString+"] [code:"+code+"] ["+message+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
//	         }
//		} catch (Exception e) {
//			NewLoginHandler.logger.info("[火游戏2调用登录接口] [异常] [paramString:"+paramString+"] [url:"+url+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
//		}
		return false;
	
	}
	
	public boolean checkLogin2(String username,String pwd){
		long startTime = System.currentTimeMillis();
		String mem_id = username.split("HUOGAMEUSER2_")[1];
		
		LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();
		params.put("app_id",60182+"");
		params.put("mem_id",mem_id);
		params.put("user_token",pwd);
//		/sign 的签名规则：md5(app_id=...&mem_id=...&user_token=...&app_key=...)
		String mysign = StringUtil.hash("app_id="+60182+"&mem_id="+mem_id+"&user_token="+pwd+"&app_key="+app_key2);
		params.put("sign",mysign);
		
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
	    	 if(status == 1 ){
	        	NewLoginHandler.logger.info("[火游戏2调用登录接口] [成功] [paramString:"+paramString+"] [code:"+code+"] ["+message+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
	        	return true;
	         } else{
	        	 NewLoginHandler.logger.info("[火游戏2调用登录接口] [失败] [paramString:"+paramString+"] [code:"+code+"] ["+message+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
	         }
		} catch (Exception e) {
			NewLoginHandler.logger.info("[火游戏2调用登录接口] [异常] [paramString:"+paramString+"] [url:"+url+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		return false;
	}
	
	//一剑诛仙
	public boolean checkLogin4(String username,String pwd){
		long startTime = System.currentTimeMillis();
		String mem_id = username.split("HUOGAMEUSER4_")[1];
		
		LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();
		params.put("app_id",60214+"");
		params.put("mem_id",mem_id);
		params.put("user_token",pwd);
//		/sign 的签名规则：md5(app_id=...&mem_id=...&user_token=...&app_key=...)
		String mysign = StringUtil.hash("app_id="+60214+"&mem_id="+mem_id+"&user_token="+pwd+"&app_key=6833a245ca5098f2d8ae9d844bb6ff2e");
		params.put("sign",mysign);
		
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
	    	 if(status == 1 ){
	        	NewLoginHandler.logger.info("[一剑诛仙调用登录接口] [成功] [paramString:"+paramString+"] [code:"+code+"] ["+message+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
	        	return true;
	         } else{
	        	 NewLoginHandler.logger.info("[一剑诛仙调用登录接口] [失败] [paramString:"+paramString+"] [code:"+code+"] ["+message+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
	         }
		} catch (Exception e) {
			NewLoginHandler.logger.info("[一剑诛仙调用登录接口] [异常] [paramString:"+paramString+"] [url:"+url+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		return false;
	}
	
	//三界飘渺
	public boolean checkLogin9(String username,String pwd){
		long startTime = System.currentTimeMillis();
		String mem_id = username.split("HUOGAMEUSER5_")[1];
		
		LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();
		params.put("app_id",60213+"");
		params.put("mem_id",mem_id);
		params.put("user_token",pwd);
//		/sign 的签名规则：md5(app_id=...&mem_id=...&user_token=...&app_key=...)5acba75718c9c95cecd223c50a0a40b3
		String mysign = StringUtil.hash("app_id="+60213+"&mem_id="+mem_id+"&user_token="+pwd+"&app_key=5acba75718c9c95cecd223c50a0a40b3");
		params.put("sign",mysign);
		
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
	    	 if(status == 1 ){
	        	NewLoginHandler.logger.info("[三界飘渺调用登录接口] [成功] [username:"+username+"] [paramString:"+paramString+"] [code:"+code+"] ["+message+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
	        	return true;
	         } else{
	        	 NewLoginHandler.logger.info("[三界飘渺调用登录接口] [失败] [username:"+username+"] [paramString:"+paramString+"] [code:"+code+"] ["+message+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
	         }
		} catch (Exception e) {
			NewLoginHandler.logger.info("[火游戏2调用登录接口] [异常] [username:"+username+"] [paramString:"+paramString+"] [url:"+url+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		return false;
	}
	
	public boolean checkLogin5(String username,String pwd){
		long startTime = System.currentTimeMillis();
		String mem_id = username.split("AIWANUSER_")[1];
		
		LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();
		params.put("app_id",6308+"");
		params.put("mem_id",mem_id);
		params.put("user_token",pwd);
//		/sign 的签名规则：md5(app_id=...&mem_id=...&user_token=...&app_key=...)
		String mysign = StringUtil.hash("app_id="+6308+"&mem_id="+mem_id+"&user_token="+pwd+"&app_key=ef00253243f359c47b940d8a924bd170");
		params.put("sign",mysign);
		
		String paramString = buildParams(params);
		HashMap headers = new HashMap();
		String content =  "";
		
		try {
			byte bytes[] = HttpUtils.webPost(new java.net.URL("http://api.igame001.cn/api/cp/user/check"), paramString.getBytes("utf-8"), headers, 60000, 60000);
			String encoding = (String)headers.get(HttpUtils.ENCODING);
			Integer code = (Integer)headers.get(HttpUtils.Response_Code);
			String message = (String)headers.get(HttpUtils.Response_Message);
			content = new String(bytes,encoding);
			
	    	JSONObject jo = new JSONObject(content);
	    	 int status = jo.optInt("status", 0);
	    	 if(status == 1 ){
	        	NewLoginHandler.logger.info("[爱玩调用登录接口] [成功] [paramString:"+paramString+"] [code:"+code+"] ["+message+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
	        	return true;
	         } else{
	        	 NewLoginHandler.logger.info("[爱玩调用登录接口] [失败] [paramString:"+paramString+"] [code:"+code+"] ["+message+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
	         }
		} catch (Exception e) {
			NewLoginHandler.logger.info("[爱玩调用登录接口] [异常] [paramString:"+paramString+"] [url:"+url+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		return false;
	}
	
	public boolean checkLogin6(String username,String pwd){
		long startTime = System.currentTimeMillis();
		String mem_id = username.split("AIWAN2USER_")[1];
		
		LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();
		params.put("app_id",6309+"");
		params.put("mem_id",mem_id);
		params.put("user_token",pwd);
//		/sign 的签名规则：md5(app_id=...&mem_id=...&user_token=...&app_key=...)
		String mysign = StringUtil.hash("app_id="+6309+"&mem_id="+mem_id+"&user_token="+pwd+"&app_key=7d243ab6a4c46dda16fe7da2fb736abd");
		params.put("sign",mysign);
		
		String paramString = buildParams(params);
		HashMap headers = new HashMap();
		String content =  "";
		
		try {
			byte bytes[] = HttpUtils.webPost(new java.net.URL("http://api.igame001.cn/api/cp/user/check"), paramString.getBytes("utf-8"), headers, 60000, 60000);
			String encoding = (String)headers.get(HttpUtils.ENCODING);
			Integer code = (Integer)headers.get(HttpUtils.Response_Code);
			String message = (String)headers.get(HttpUtils.Response_Message);
			content = new String(bytes,encoding);
			
	    	JSONObject jo = new JSONObject(content);
	    	 int status = jo.optInt("status", 0);
	    	 if(status == 1 ){
	        	NewLoginHandler.logger.info("[爱玩2调用登录接口] [成功] [paramString:"+paramString+"] [code:"+code+"] ["+message+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
	        	return true;
	         } else{
	        	 NewLoginHandler.logger.info("[爱玩2调用登录接口] [失败] [paramString:"+paramString+"] [code:"+code+"] ["+message+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
	         }
		} catch (Exception e) {
			NewLoginHandler.logger.info("[爱玩2调用登录接口] [异常] [paramString:"+paramString+"] [url:"+url+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
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
