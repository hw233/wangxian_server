package com.fy.gamegateway.thirdpartner.wandoujia;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.fy.gamegateway.mieshi.server.MieshiGatewaySubSystem;
import com.fy.gamegateway.thirdpartner.dcn.DCNLoginResult;
import com.fy.gamegateway.tools.JacksonManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.xuanzhi.tools.text.StringUtil;
import com.xuanzhi.tools.text.XmlUtil;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.servlet.HttpsUtils;



public class WandoujiaClientService {
	
	public static Logger logger = Logger.getLogger(WandoujiaClientService.class);
	
	
	
	
	private static WandoujiaClientService self;
	public static WandoujiaClientService getInstance(){
		if(self != null){
			return self;
		}
		self = new WandoujiaClientService();
		return self;
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
	
	public WandoujiaLoginResult login(String username,String password){
		 long startTime = System.currentTimeMillis();
		 
		 if(username.startsWith("WANDOUJIASDKUSER_")) {
				username = username.split("_")[1];
			}
		 
		 String url = "https://pay.wandoujia.com/api/uid/check";
		 
		 
		LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();

		params.put("uid",username);
		try {
			params.put("token", URLEncoder.encode(password,"utf-8"));
		} catch (UnsupportedEncodingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		url += "?" + buildParams(params);
		URL tokenUrl = null;
		try {
			tokenUrl = new URL(url);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		String content = "";
		
		HashMap headers = new HashMap();
		try {
			byte bytes[] = HttpsUtils.webGet(tokenUrl, headers, 50000, 50000);
			
			String encoding = (String)headers.get(HttpUtils.ENCODING);
			Integer code = (Integer)headers.get(HttpUtils.Response_Code);
			String message = (String)headers.get(HttpUtils.Response_Message);
			
			content = new String(bytes,encoding);
			
			
			logger.info("[调用登陆接口] [豌豆荚] [成功] ["+url+"] [code:"+code+"] ["+message+"] [sign:"+params.get("verifystring")+"] ["+content+"] [用户名:"+username+"] [密码:"+password+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			
			logger.info("[调用登陆接口] [豌豆荚] [失败] ["+url+"] [code:--] [--] [sign:"+params.get("verifystring")+"] ["+content+"] [用户名:"+username+"] [密码:"+password+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
			
			WandoujiaLoginResult r = new WandoujiaLoginResult();
			r.code = 404;
			r.message = "调用豌豆荚登陆接口出现异常";
						
			return r;
		}
		
		boolean loginSucc = false;
		if("true".equalsIgnoreCase(content))
		{
			loginSucc = true;
		}
		
		WandoujiaLoginResult r = new WandoujiaLoginResult();
	
		if(loginSucc)
		{
			r.code = 200;
			r.username = username;
			r.passwd = password;
			r.message = content;
			logger.info("[解析登录返回数据] [豌豆荚] [成功] [去豌豆荚登陆] [成功] ["+url+"] [status:"+r.code+"] [message:"+r.message+"] [用户名:"+username+"] [密码:"+password+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
		}
		else
		{
			r.code = 500;
			r.username = username;
			r.passwd = password;
			r.message = "登陆失败";
		}
			
		
		
		return r;
		
		

	}
	
	

}
