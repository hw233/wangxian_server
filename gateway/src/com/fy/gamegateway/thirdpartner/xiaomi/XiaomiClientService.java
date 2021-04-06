package com.fy.gamegateway.thirdpartner.xiaomi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.fy.boss.platform.xiaomi.common.HmacSHA1Encryption;
import com.fy.boss.platform.xiaomi.common.ParamEntry;
import com.fy.boss.platform.xiaomi.common.ParamUtil;
import com.fy.gamegateway.mieshi.server.MieshiGatewaySubSystem;
import com.fy.gamegateway.tools.JacksonManager;
import com.xuanzhi.tools.servlet.HttpUtils;



public class XiaomiClientService {
	
	static Logger logger = Logger.getLogger(MieshiGatewaySubSystem.class);

	public static String APPID = "2882303761517739782";//"2882303761517739244";//"3087";
	public static String APPKEY = "5581773990782";//"5561773994244";//"512e79c0-a81e-c2af-38b7-505af006f12f";
	public static String AppSecret = "f6JGesLqklrDfsoZPKUn5A==";//"Rj572AZx7gSt7NbHj/ZMWw==";//"512e79c0-a81e-c2af-38b7-505af006f12f";
	public static String SESSION_CHECK = "http://mis.migc.xiaomi.com/api/biz/service/verifySession.do";
	
	
//	AppID：2882303761517739782	AppKEY：5581773990782	AppSecret：f6JGesLqklrDfsoZPKUn5A==

	
	private static XiaomiClientService self;
	
	public static XiaomiClientService getInstance() {
		if(self == null) {
			self = new XiaomiClientService();
		}
		return self;
	}
	
	public static String getCodeDescription(int code){
		if(code == 200){
			return "验证正确";
		}
		if(code == 1515){
			return "appId错误";
		}
		if(code == 1516){
			return "uid错误";
		}
		if(code == 1520){
			return "session 错误";
		}
		if(code == 1525){
			return "signature 错误";
		}
		
		return "未知响应码";
	}
	
	/**
	 * 验证sessionId有效性
	 * @param uin
	 * @param sessionId
	 * @return
	 */
	public boolean validate(String uin, String sessionId){
		long startTime = System.currentTimeMillis();
		
		String url = "http://mis.migc.xiaomi.com/api/biz/service/verifySession.do";
		
		if(uin.startsWith("XIAOMIUSER_")) {
			uin = uin.split("_")[1];
		}
		List<ParamEntry> paras = new ArrayList<ParamEntry>();
		paras.add(new ParamEntry("appId",String.valueOf(APPID)));
		paras.add(new ParamEntry("session",sessionId));
		paras.add(new ParamEntry("uid",uin));
		String returnString = null;
		String sign = null;
		try {
			returnString = ParamUtil.getSortQueryString(paras);
			sign = HmacSHA1Encryption.hmacSHA1Encrypt(returnString,AppSecret);
		} catch (Exception e1) {
			logger.info("[调用登录接口] [失败] [出现异常] [url:"+url+"] [待签名字符串:"+returnString+"] [sign:--] [uin:"+uin+"] [sid:"+sessionId+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			return false;
		}
		String queryString = "";
		for(ParamEntry k : paras) {
			String v = k.getValue();
			queryString += k.getKey() + "=" + v  + "&";
		}
		queryString += "signature=" + sign;
		url = url + "?" + queryString;
		HashMap headers = new HashMap();
		String content =  "";
		try {
			byte bytes[] = HttpUtils.webGet(new java.net.URL(url), headers, 5000, 10000);
			String encoding = (String)headers.get(HttpUtils.ENCODING);
			Integer code = (Integer)headers.get(HttpUtils.Response_Code);
			String message = (String)headers.get(HttpUtils.Response_Message);
			content = new String(bytes,encoding);
			logger.info("[调用登录接口] [成功] ["+url+"] [code:"+code+"] ["+message+"] ["+content+"] [uin:"+uin+"] [sid:"+sessionId+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			logger.info("[调用登录接口] [失败] ["+url+"] [code:--] [--] ["+content+"] [uin:"+uin+"] [sid:"+sessionId+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
			return false;
		}
		JacksonManager jm = JacksonManager.getInstance();
		try {
			Map map =(Map)jm.jsonDecodeObject(content);
			String code = (map.get("errcode") instanceof String ? (String)map.get("errcode") : ((Integer)map.get("errcode")).intValue()+"");
			String desp = (String)map.get("errMsg");
			logger.info("[解析登录返回数据成功] [结果:"+(code.equals("200")?"验证合法":"验证失败")+"] [uin:"+uin+"] [sessionId:"+sessionId+"] [return_code:"+code+"] [return_desp:"+desp+"] [codeDesp:"+getCodeDescription(Integer.valueOf(code))+"] ["+url+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			if(code.equals("200")) {
				return true;
			} 
		}catch (Exception e) {
			logger.info("[解析登录返回数据失败] [uin:"+uin+"] [sessionId:"+sessionId+"] [return:"+content+"] ["+url+"] ["+(System.currentTimeMillis()-startTime)+"ms]", e);
		}
		return false;
	}
	
	public boolean validate2(String uin, String sessionId){
		long startTime = System.currentTimeMillis();
		
		String url = "http://mis.migc.xiaomi.com/api/biz/service/verifySession.do";
		
		if(uin.startsWith("XIAOMIUSER2_")) {
			uin = uin.split("_")[1];
		}
		List<ParamEntry> paras = new ArrayList<ParamEntry>();
		paras.add(new ParamEntry("appId","2882303761517847798"));
		paras.add(new ParamEntry("session",sessionId));
		paras.add(new ParamEntry("uid",uin));
		String returnString = null;
		String sign = null;
		try {
			returnString = ParamUtil.getSortQueryString(paras);
			sign = HmacSHA1Encryption.hmacSHA1Encrypt(returnString,"JdBHg+XmZ7ZnONdVFReFKg==");
		} catch (Exception e1) {
			logger.info("[调用登录接口] [失败] [出现异常] [url:"+url+"] [待签名字符串:"+returnString+"] [sign:--] [uin:"+uin+"] [sid:"+sessionId+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			return false;
		}
		String queryString = "";
		for(ParamEntry k : paras) {
			String v = k.getValue();
			queryString += k.getKey() + "=" + v  + "&";
		}
		queryString += "signature=" + sign;
		url = url + "?" + queryString;
		HashMap headers = new HashMap();
		String content =  "";
		try {
			byte bytes[] = HttpUtils.webGet(new java.net.URL(url), headers, 5000, 10000);
			String encoding = (String)headers.get(HttpUtils.ENCODING);
			Integer code = (Integer)headers.get(HttpUtils.Response_Code);
			String message = (String)headers.get(HttpUtils.Response_Message);
			content = new String(bytes,encoding);
			logger.info("[调用登录接口] [成功] ["+url+"] [code:"+code+"] ["+message+"] ["+content+"] [uin:"+uin+"] [sid:"+sessionId+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			logger.info("[调用登录接口] [失败] ["+url+"] [code:--] [--] ["+content+"] [uin:"+uin+"] [sid:"+sessionId+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
			return false;
		}
		JacksonManager jm = JacksonManager.getInstance();
		try {
			Map map =(Map)jm.jsonDecodeObject(content);
			String code = (map.get("errcode") instanceof String ? (String)map.get("errcode") : ((Integer)map.get("errcode")).intValue()+"");
			String desp = (String)map.get("errMsg");
			logger.info("[解析登录返回数据成功] [结果:"+(code.equals("200")?"验证合法":"验证失败")+"] [uin:"+uin+"] [sessionId:"+sessionId+"] [return_code:"+code+"] [return_desp:"+desp+"] [codeDesp:"+getCodeDescription(Integer.valueOf(code))+"] ["+url+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			if(code.equals("200")) {
				return true;
			} 
		}catch (Exception e) {
			logger.info("[解析登录返回数据失败] [uin:"+uin+"] [sessionId:"+sessionId+"] [return:"+content+"] ["+url+"] ["+(System.currentTimeMillis()-startTime)+"ms]", e);
		}
		return false;
	}
	
	public boolean validate(String uin, String sessionId,String channel){
		long startTime = System.currentTimeMillis();
		
		boolean isWali = false;
		if(channel.toLowerCase().contains("walisdk"))
		{
			isWali = true;
			if(uin.startsWith("WALIUSER")) {
				uin = uin.split("_")[1];
			}
		}
		else
		{
			if(uin.startsWith("XIAOMIUSER_")) {
				uin = uin.split("_")[1];
			}
		}
		
		
		String url = "http://mis.migc.xiaomi.com/api/biz/service/verifySession.do";
		if(isWali)
		{
			url = "http://gamebiz.wali.com/api/biz/service/verifySession.do";
			APPID = "7761";
			APPKEY = "6feeea8b-d27d-e05f-2c12-516bb33784fa";
		}
		else
		{
			url = "http://mis.migc.xiaomi.com/api/biz/service/verifySession.do";
			APPID = "3087";
			APPKEY = "512e79c0-a81e-c2af-38b7-505af006f12f";
		}
		List<ParamEntry> paras = new ArrayList<ParamEntry>();
		paras.add(new ParamEntry("appId",String.valueOf(APPID)));
		paras.add(new ParamEntry("session",sessionId));
		paras.add(new ParamEntry("uid",uin));
		String returnString = null;
		String sign = null;
		try {
			returnString = ParamUtil.getSortQueryString(paras);
			sign = HmacSHA1Encryption.hmacSHA1Encrypt(returnString,APPKEY);
		} catch (Exception e1) {
			logger.info("[调用登录接口] [失败] [出现异常] [url:"+url+"] [待签名字符串:"+returnString+"] [sign:--] [uin:"+uin+"] [sid:"+sessionId+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			return false;
		}
		String queryString = "";
		for(ParamEntry k : paras) {
			String v = k.getValue();
			queryString += k.getKey() + "=" + v  + "&";
		}
		queryString += "signature=" + sign;
		url = url + "?" + queryString;
		HashMap headers = new HashMap();
		String content =  "";
		try {
			byte bytes[] = HttpUtils.webGet(new java.net.URL(url), headers, 5000, 10000);
			String encoding = (String)headers.get(HttpUtils.ENCODING);
			Integer code = (Integer)headers.get(HttpUtils.Response_Code);
			String message = (String)headers.get(HttpUtils.Response_Message);
			content = new String(bytes,encoding);
			logger.info("[调用登录接口] [成功] ["+url+"] [code:"+code+"] ["+message+"] ["+content+"] [uin:"+uin+"] [sid:"+sessionId+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			logger.info("[调用登录接口] [失败] ["+url+"] [code:--] [--] ["+content+"] [uin:"+uin+"] [sid:"+sessionId+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
			return false;
		}
		JacksonManager jm = JacksonManager.getInstance();
		try {
			Map map =(Map)jm.jsonDecodeObject(content);
			String code = (map.get("errcode") instanceof String ? (String)map.get("errcode") : ((Integer)map.get("errcode")).intValue()+"");
			String desp = (String)map.get("errMsg");
			logger.info("[解析登录返回数据成功] [结果:"+(code.equals("200")?"验证合法":"验证失败")+"] [uin:"+uin+"] [sessionId:"+sessionId+"] [return_code:"+code+"] [return_desp:"+desp+"] [codeDesp:"+getCodeDescription(Integer.valueOf(code))+"] ["+url+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			if(code.equals("200")) {
				return true;
			} 
		}catch (Exception e) {
			logger.info("[解析登录返回数据失败] [uin:"+uin+"] [sessionId:"+sessionId+"] [return:"+content+"] ["+url+"] ["+(System.currentTimeMillis()-startTime)+"ms]", e);
		}
		return false;
	}
	
	
}
