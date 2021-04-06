package com.fy.gamegateway.thirdpartner.j6533;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.fy.gamegateway.mieshi.waigua.NewLoginHandler;
import com.fy.gamegateway.tools.JacksonManager;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.text.StringUtil;

public class T6533ClientService {
	
	public static Logger logger = NewLoginHandler.logger;
	
	private static T6533ClientService self;
	
	public static T6533ClientService getInstance() {
		if(self == null) {
			self = new T6533ClientService();
		}
		return self;
	}

	private String key = "8a95c501f17f9e06b0a85ea9d172d439";
	private String key2 = "613614ebaaa869d571a0b653c50a2ed1";
	private	String access_token_url = "http://dev.185sy.com/user/verify";
	private	String access_token_url2 = "http://dev.185sy.com/user/verify";
	
	
	public boolean checkLogin(String username,String token){
		LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();
		String guid = username.split("6533USER_")[1];
		params.put("userID",guid);
		params.put("token", token);
//		md5("userID="+userID+"&token="+token+"&key="+RH_AppSecret)
		String signstr = "userID="+guid+"&token="+token+"&key="+key;
		String sign = StringUtil.hash(signstr);
		params.put("sign", sign);
		
		String encoding = "";
		Integer code = -1;
		String message = "";
		String result = "";
		String content = "";
		HashMap headers = new HashMap();
		try {
			content = buildParams(params);
			byte bytes[] = HttpUtils.webPost(new java.net.URL(access_token_url), content.getBytes(), headers, 5000, 10000);
			
			encoding = (String)headers.get(HttpUtils.ENCODING);
			code = (Integer)headers.get(HttpUtils.Response_Code);
			message = (String)headers.get(HttpUtils.Response_Message);
			result = new String(bytes,encoding);
			
			JacksonManager jm = JacksonManager.getInstance();
			Map map =(Map)jm.jsonDecodeObject(result);
			Integer state = (Integer)map.get("state");
			
			logger.warn("[6533登录验证] [成功] [state:"+state+"] [result:"+result+"] [signstr:"+signstr+"] [encoding:"+(encoding==null?"":encoding)+"] [code:"+(code==null?"":code)+"] [message:"+(message==null?"":message)+"] [content:"+(content==null?"":content)+"] [result:"+(result==null?"":result)+"]");
			
			return state==1;
		} catch (Exception e){
			e.printStackTrace();
			 encoding = (String)headers.get(HttpUtils.ENCODING);
			 code = (Integer)headers.get(HttpUtils.Response_Code);
			 message = (String)headers.get(HttpUtils.Response_Message);
		}
		logger.warn("[6533登录验证] [错误] [encoding:"+(encoding==null?"":encoding)+"] [code:"+(code==null?"":code)+"] [message:"+(message==null?"":message)+"] [content:"+(content==null?"":content)+"] [result:"+(result==null?"":result)+"]");
		return false;
	}
	
	public boolean checkLogin_android(String username,String token){
		LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();
		String guid = username.split("185USER_")[1];
		params.put("userID",guid);
		params.put("token", token);
//		md5("userID="+userID+"&token="+token+"&key="+RH_AppSecret)
		String signstr = "userID="+guid+"&token="+token+"&key="+key2;
		String sign = StringUtil.hash(signstr);
		params.put("sign", sign);
		
		String encoding = "";
		Integer code = -1;
		String message = "";
		String result = "";
		String content = "";
		HashMap headers = new HashMap();
		try {
			content = buildParams(params);
			byte bytes[] = HttpUtils.webPost(new java.net.URL(access_token_url2), content.getBytes(), headers, 5000, 10000);
			
			encoding = (String)headers.get(HttpUtils.ENCODING);
			code = (Integer)headers.get(HttpUtils.Response_Code);
			message = (String)headers.get(HttpUtils.Response_Message);
			result = new String(bytes,encoding);
			
			JacksonManager jm = JacksonManager.getInstance();
			Map map =(Map)jm.jsonDecodeObject(result);
			Integer state = (Integer)map.get("state");
			
			logger.warn("[185登录验证] [成功] [state:"+state+"] [result:"+result+"] [signstr:"+signstr+"] [encoding:"+(encoding==null?"":encoding)+"] [code:"+(code==null?"":code)+"] [message:"+(message==null?"":message)+"] [content:"+(content==null?"":content)+"] [result:"+(result==null?"":result)+"]");
			
			return state==1;
		} catch (Exception e){
			e.printStackTrace();
			 encoding = (String)headers.get(HttpUtils.ENCODING);
			 code = (Integer)headers.get(HttpUtils.Response_Code);
			 message = (String)headers.get(HttpUtils.Response_Message);
		}
		logger.warn("[185登录验证] [错误] [encoding:"+(encoding==null?"":encoding)+"] [code:"+(code==null?"":code)+"] [message:"+(message==null?"":message)+"] [content:"+(content==null?"":content)+"] [result:"+(result==null?"":result)+"]");
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
