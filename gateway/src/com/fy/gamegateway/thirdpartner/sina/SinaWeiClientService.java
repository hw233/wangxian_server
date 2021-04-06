package com.fy.gamegateway.thirdpartner.sina;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.fy.gamegateway.mieshi.server.MieshiGatewaySubSystem;
import com.fy.gamegateway.tools.EncryptTools;
import com.fy.gamegateway.tools.JacksonManager;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.text.StringUtil;



public class SinaWeiClientService {
	
	static Logger logger = Logger.getLogger(MieshiGatewaySubSystem.class);

	//public static final String AppKey=  "112522362359285";
	public static final String AppKey=  "3467194209";
	public static final String	MSGFROM= "1124";
	//public static final String	KEY = "wp9459w3twi4pasie49iw09ti34itiweriijuy7oeaq3wseiwitsw9e4iiwpp9aw";
	public static final String	KEY = "bef5e793a3381d446fd66761418b36ff";
	
	private static SinaWeiClientService self;
	
	public static SinaWeiClientService getInstance() {
		if(self == null) {
			self = new SinaWeiClientService();
		}
		return self;
	}
	
/*	public static String getCodeDescription(int code){
		if(code == 1){
			return "登录成功";
		}
		if(code == 0){
			return "登录失败";
		}
		if(code == 2){
			return "调用91接口，AppId无效";
		}
		if(code == 3){
			return "调用91接口，Act无效";
		}
		if(code == 4){
			return "调用91接口，参数无效";
		}
		if(code == 5){
			return "调用91接口，Sign无效";
		}
		if(code == 11){
			return "调用91接口，SessionId无效";
		}
		return "未知响应码";
	}*/
	
	/**
	 * 验证sessionId有效性
	 * @param uin
	 * @param sessionId
	 * @return
	 */
	public boolean validate(String uin, String sessionId){
		long startTime = System.currentTimeMillis();
		if(uin.startsWith("SINAWEIUSER_")) {
			uin = uin.split("_")[1];
		}
		String url = "http://api.weibo.com/game/1/user/show.json";

		
		LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();
		params.put("source",AppKey);
		params.put("timestamp", System.currentTimeMillis()+"");
		params.put("session_key", sessionId);
		params.put("uid", uin);
		//params.put("signature", sign+"");
		String paramString = buildParamString(params);
		url = url + "?" + paramString;
		HashMap headers = new HashMap();
		String content =  "";
		try {
			byte bytes[] = HttpUtils.webGet(new java.net.URL(url), headers, 5000, 10000);
			String encoding = (String)headers.get(HttpUtils.ENCODING);
			Integer code = (Integer)headers.get(HttpUtils.Response_Code);
			String message = (String)headers.get(HttpUtils.Response_Message);
			content = new String(bytes,encoding);
			logger.info("[调用SINAWEI登录接口] [成功] ["+url+"] [code:"+code+"] ["+message+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			logger.info("[调用SINAWEI登录接口] [失败] ["+url+"] [code:--] [--] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
			return false;
		}
		JacksonManager jm = JacksonManager.getInstance();
		try {
			Map map =(Map)jm.jsonDecodeObject(content);
			String uid = (String)map.get("idstr");
			logger.info("[解析SINAWEI登录返回数据成功] [结果:"+(uin.equalsIgnoreCase(uid)?"验证合法":"验证失败")+"] [uin:"+uin+"] [新浪uid:"+uid+"] [SESSION_KEY:"+sessionId+"] ["+url+"] [新浪返回内容:"+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			if(uin.equalsIgnoreCase(uid)) {
				return true;
			} 
		}catch (Exception e) {
			logger.info("[解析SINAWEI登录返回数据失败] [uin:"+uin+"] [SESSION_KEY:"+sessionId+"] [return:"+content+"] ["+url+"] ["+(System.currentTimeMillis()-startTime)+"ms]", e);
		}
		return false;
	}
	
	
	public boolean validateNew(String uin, String sessionId){
		
		try
		{
			if(validateSSO(uin, sessionId))
			{
				return true;
			}
		}
		catch(Exception e)
		{
			
		}
			
		
		
		long startTime = System.currentTimeMillis();
		if(uin.startsWith("SINAWEIUSER_")) {
			uin = uin.split("_")[1];
		}
		String url = "http://api.weibo.com/game/1/user/show.json";
		String appkey = "2199740082"; 
		String msgFrom = "";
		String key = "75aa7ff521e383aa2c2fb2f60e3746d1";
		
		LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();
		params.put("source",appkey);
		params.put("timestamp", System.currentTimeMillis()+"");
		params.put("session_key", sessionId);
		params.put("uid", uin);
		//params.put("signature", sign+"");
		String paramString = buildParamNewString(params,key);
		url = url + "?" + paramString;
		HashMap headers = new HashMap();
		String content =  "";
		try {
			byte bytes[] = HttpUtils.webGet(new java.net.URL(url), headers, 5000, 10000);
			String encoding = (String)headers.get(HttpUtils.ENCODING);
			Integer code = (Integer)headers.get(HttpUtils.Response_Code);
			String message = (String)headers.get(HttpUtils.Response_Message);
			content = new String(bytes,encoding);
			logger.info("[调用SINAWEI登录接口] [成功] ["+url+"] [code:"+code+"] ["+message+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			logger.info("[调用SINAWEI登录接口] [失败] ["+url+"] [code:--] [--] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
			return false;
		}
		JacksonManager jm = JacksonManager.getInstance();
		try {
			Map map =(Map)jm.jsonDecodeObject(content);
			String uid = (String)map.get("idstr");
			logger.info("[解析SINAWEI登录返回数据成功] [结果:"+(uin.equalsIgnoreCase(uid)?"验证合法":"验证失败")+"] [uin:"+uin+"] [新浪uid:"+uid+"] [SESSION_KEY:"+sessionId+"] ["+url+"] [新浪返回内容:"+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			if(uin.equalsIgnoreCase(uid)) {
				return true;
			} 
		}catch (Exception e) {
			logger.info("[解析SINAWEI登录返回数据失败] [uin:"+uin+"] [SESSION_KEY:"+sessionId+"] [return:"+content+"] ["+url+"] ["+(System.currentTimeMillis()-startTime)+"ms]", e);
		}
		return false;
	}
	
	
	private boolean validateSSO(String uin, String sessionId)
	{
		long startTime = System.currentTimeMillis();
		if(uin.startsWith("SINAWEIUSER_")) {
			uin = uin.split("_")[1];
		}
		String url = "http://game.weibo.cn/sso.php";
		String i = "chktoken"; 
		String msgFrom = "";
		String key = "75aa7ff521e383aa2c2fb2f60e3746d1";
		
		String[] infos = sessionId.split("@@");
		String token = infos[0];
		String machineid = infos[1];
		String ip = infos[2];
		
		String p = 	"d940a1ab0429ce60292187d9d70bae34";
		String m = StringUtil.hash(token+p);
		
		LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();
		params.put("i",i);
		params.put("uid", uin);
		params.put("token", token);
		params.put("machineid", machineid);
		params.put("ip", ip);
		params.put("m", m);
		//params.put("signature", sign+"");
		String paramString = buildBaseString(params);
		url = url + "?" + paramString;
		HashMap headers = new HashMap();
		String content =  "";
		try {
			byte bytes[] = HttpUtils.webGet(new java.net.URL(url), headers, 5000, 10000);
			String encoding = (String)headers.get(HttpUtils.ENCODING);
			Integer code = (Integer)headers.get(HttpUtils.Response_Code);
			String message = (String)headers.get(HttpUtils.Response_Message);
			content = new String(bytes,encoding);
			logger.info("[调用SINAWEI登录接口] [成功] ["+url+"] [code:"+code+"] ["+message+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			logger.info("[调用SINAWEI登录接口] [失败] ["+url+"] [code:--] [--] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
			return false;
		}
		JacksonManager jm = JacksonManager.getInstance();
		try {
			Map map =(Map)jm.jsonDecodeObject(content);
			Map dataMap = (Map)map.get("data");
			String uid = (String)dataMap.get("uid");
			logger.info("[解析SINAWEI登录返回数据成功] [结果:"+(uin.equalsIgnoreCase(uid)?"验证合法":"验证失败")+"] [uin:"+uin+"] [新浪uid:"+uid+"] [SESSION_KEY:"+sessionId+"] ["+url+"] [新浪返回内容:"+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			if(uin.equalsIgnoreCase(uid)) {
				return true;
			}
			else
			{
				String code  = (String)dataMap.get("code");
				String mess  = (String)dataMap.get("message");
				logger.warn("[解析SINAWEI登录返回数据成功] [结果:"+(uin.equalsIgnoreCase(uid)?"验证合法":"验证失败")+"] [uin:"+uin+"] [新浪uid:"+uid+"] [SESSION_KEY:"+sessionId+"] ["+url+"] [新浪返回内容:"+content+"] [code:"+new String(code)+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
				
			}
		}catch (Exception e) {
			logger.info("[解析SINAWEI登录返回数据失败] [uin:"+uin+"] [SESSION_KEY:"+sessionId+"] [return:"+content+"] ["+url+"] ["+(System.currentTimeMillis()-startTime)+"ms]", e);
		}
		return false;
	}
	
	
	private String buildBaseString(Map<String,String> paramMap)
	{
		Map<String, String> map = new HashMap<String, String>();
		for (Map.Entry<String, String> entry : paramMap.entrySet()) {
			map.put(this.urlencodeRfc3986(entry.getKey()), this
					.urlencodeRfc3986(entry.getValue().toString()));
		}
		String[] key = map.keySet().toArray(new String[map.size()]);
		Arrays.sort(key, null);

		StringBuffer sb = new StringBuffer();
		for (String k : key) {
			sb.append(k);
			sb.append("=");
			sb.append(map.get(k));
			sb.append("&");
		}
		
		return sb.toString().substring(0, sb.length() - 1);
	}
	
	private String buildParamString(Map<String,String> paramMap)
	{
		String baseString = buildBaseString(paramMap);
		String signature = EncryptTools.makeSha1(baseString + KEY);
		baseString += "&signature=" + signature;

		return baseString;
	}
	
	private String buildParamNewString(Map<String,String> paramMap,String key)
	{
		String baseString = buildBaseString(paramMap);
		String signature = EncryptTools.makeSha1(baseString + key);
		baseString += "&signature=" + signature;

		return baseString;
	}

	
	public String urlencodeRfc3986(String input) {
		String encodeInput = null;
		try {
			encodeInput = URLEncoder.encode(input, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		encodeInput = encodeInput.replace("+", " ");

		return encodeInput.replace("%7E", "~");
	}
	
	public static void main(String args[]) {
		String str = "10211042857230444d4d79a0ee7a7345486cbccc4e91a8d880ebd0ce0aeb10d428769c5a9498b80f580a007ab104b350";
		System.out.println(StringUtil.hash(str));
	}
	
}
