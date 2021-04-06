package com.fy.gamegateway.thirdpartner.lenovo;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.fy.gamegateway.tools.JacksonManager;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.text.StringUtil;



public class LenovoClientService {
	
	public static Logger logger = Logger.getLogger(LenovoClientService.class);

	public static String GAMEID = "10154";
	public static String SECRETKEY = "8c55352dd2efe601637a";
	
	private static LenovoClientService self;
	
	public static LenovoClientService getInstance() {
		if(self == null) {
			self = new LenovoClientService();
		}
		return self;
	}
	
	/**
	 * 验证sessionId有效性
	 * @param uin
	 * @param sessionId
	 * @return
	 */
	public boolean validate(String uin, String sessionId){
		long startTime = System.currentTimeMillis();
		if(uin.startsWith("LENOVOUSER_")) {
			uin = uin.split("_")[1];
		}
		
		GAMEID = "10154";
		SECRETKEY = "8c55352dd2efe601637a";
		
		String url = "http://ly.feed.uu.cc/account/verify_osid";

		LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();
		params.put("game_id",GAMEID);
		params.put("open_id", uin);
		params.put("sessionid", sessionId);
		String sign = sign(params);
		String queryString = "";
		String keys[] = params.keySet().toArray(new String[0]);
		for(String k : keys) {
			String v = params.get(k);
			queryString += k + "=" + v  + "&";
		}
		queryString += "sign=" + sign;
		url = url + "?" + queryString;
		HashMap headers = new HashMap();
		String content =  "";
		try {
			byte bytes[] = HttpUtils.webPost(new java.net.URL(url), content.getBytes(), headers, 60000, 10000);
			String encoding = (String)headers.get(HttpUtils.ENCODING);
			Integer code = (Integer)headers.get(HttpUtils.Response_Code);
			String message = (String)headers.get(HttpUtils.Response_Message);
			content = new String(bytes,encoding);
			logger.info("[调用LENOVO登录接口] [成功] ["+url+"] [code:"+code+"] ["+message+"] ["+content+"] [gameid:"+GAMEID+"] [secretkey:"+SECRETKEY+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			logger.error("[调用LENOVO登录接口] [失败] ["+url+"] [code:--] [--] ["+content+"] [gameid:"+GAMEID+"] [secretkey:"+SECRETKEY+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
			return false;
		}
		
		if(content.indexOf("ok") >= 0)
		{
			if(logger.isInfoEnabled())
			{
				logger.info("[验证LENOVO用户] [成功] ["+url+"] ["+content+"] [gameid:"+GAMEID+"] [secretkey:"+SECRETKEY+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			}
			return true;
		}
		logger.warn("[验证LENOVO用户] [失败] [uin:"+uin+"] [sid:"+sessionId+"] ["+url+"] ["+content+"] [gameid:"+GAMEID+"] [secretkey:"+SECRETKEY+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
		return false;
	}
	
	
	public boolean validate(String uin, String sessionId,String channel)
	{
		GAMEID = "10154";
		SECRETKEY = "8c55352dd2efe601637a";
		String url = "http://ly.feed.uu.cc/account/verify_osid";
		if(isLedouChannel(channel))
		{
			GAMEID = "10264";
			SECRETKEY = "f0c1c53ed192ac06a8b5";
			long startTime = System.currentTimeMillis();
			if(uin.startsWith("LENOVOUSER_")) {
				uin = uin.split("_")[1];
			}
			url = "http://ly.feed.uu.cc/account/verify_osid";
			LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();
			params.put("game_id",GAMEID);
			params.put("open_id", uin);
			params.put("sessionid", sessionId);
			
			LinkedHashMap<String,String> signparams = new LinkedHashMap<String,String>();
			//signparams.put("game_id",GAMEID);
			signparams.put("openid", uin);
			signparams.put("sessionid", sessionId);
			String sign = sign(signparams);
			String queryString = "";
			String keys[] = params.keySet().toArray(new String[0]);
			for(String k : keys) {
				String v = params.get(k);
				queryString += k + "=" + v  + "&";
			}
			queryString += "sign=" + sign;
			url = url + "?" + queryString;
			HashMap headers = new HashMap();
			String content =  "";
			try {
				byte bytes[] = HttpUtils.webPost(new java.net.URL(url), content.getBytes(), headers, 60000, 10000);
				String encoding = (String)headers.get(HttpUtils.ENCODING);
				Integer code = (Integer)headers.get(HttpUtils.Response_Code);
				String message = (String)headers.get(HttpUtils.Response_Message);
				content = new String(bytes,encoding);
				logger.info("[调用LENOVO登录接口] [成功] ["+url+"] [code:"+code+"] ["+message+"] ["+content+"] [gameid:"+GAMEID+"] [secretkey:"+SECRETKEY+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			} catch (Exception e) {
				logger.error("[调用LENOVO登录接口] [失败] ["+url+"] [code:--] [--] ["+content+"] [gameid:"+GAMEID+"] [secretkey:"+SECRETKEY+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
				return false;
			}
			
			if(content.indexOf("ok") >= 0)
			{
				if(logger.isInfoEnabled())
				{
					logger.info("[验证LENOVO用户] [成功] ["+url+"] ["+content+"] [gameid:"+GAMEID+"] [secretkey:"+SECRETKEY+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
				}
				return true;
			}
			logger.warn("[验证LENOVO用户] [失败] [uin:"+uin+"] [sid:"+sessionId+"] ["+url+"] ["+content+"] [gameid:"+GAMEID+"] [secretkey:"+SECRETKEY+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			return false;
		}
		
		return validate(uin, sessionId);
	}
	
	public boolean isLedouChannel(String channel)
	{	
		if(!StringUtils.isEmpty(channel))
		{
			if(channel.toLowerCase().contains("ledou"))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}
	
	public static String sign(LinkedHashMap<String,String> params){
		String keys[] = params.keySet().toArray(new String[0]);
		java.util.Arrays.sort(keys);
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < keys.length ; i++){
			if(i == 0)
			{
				String v = params.get(keys[i]);
				sb.append(keys[i]+"="+v);
			}
			else
			{
				sb.append("&");
				String v = params.get(keys[i]);
				sb.append(keys[i]+"="+v);
			}
		}
		String md5Str = sb.toString() +"&secret=" + SECRETKEY;
		if(logger.isInfoEnabled())
			logger.info("[验证LENOVO用户] [生成签名串] [params:"+params+"] [待签名串:"+md5Str+"]");
		md5Str = StringUtil.hash(md5Str);
		
		return md5Str;
	}

	
}
