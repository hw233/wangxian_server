package com.fy.gamegateway.thirdpartner.nineone;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.fy.gamegateway.thirdpartner.baidusdk.BaiduBossClientService;
import com.fy.gamegateway.tools.JacksonManager;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.text.StringUtil;



public class NineOneClientService {
	
	static Logger logger = Logger.getLogger(NineOneClientService.class);

	public static String APPID = "102110";
	public static String APPKEY = "80ebd0ce0aeb10d428769c5a9498b80f580a007ab104b350";
	public static String IOS_APPID = "101530";
	public static String IOS_APPKEY = "938acf3525aa7269ef510f1e9aaa762ee8e0a9e9e1abe211";
	
	public static String NEW_91_APP_ID = "5317596";//"5953434"; 
	public static String NEW_91_APP_KEY = "m19wvWw2bBWvyvp6EDf1FfRO";//"5xFWyKGGuxYnaGF272n6kmxw"; 
	public static String NEW_91_SECRET_KEY = "rOkevqjspkQCHfntuNca9sDLlYfWsOoH";//"mnaOEobpbBWBQAKdamjphPDaCL6zHoDQ";

	
	private static NineOneClientService self;
	
	public static NineOneClientService getInstance() {
		if(self == null) {
			self = new NineOneClientService();
		}
		return self;
	}
	
	public static String getCodeDescription(int code){
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
	}
	
	/**
	 * 验证sessionId有效性
	 * @param uin
	 * @param sessionId
	 * @return
	 */
	public boolean validate(String platform,  String uin, String sessionId){
		long startTime = System.currentTimeMillis();
		if(uin.startsWith("91USER_")) {
			uin = uin.split("_")[1];
		}
		
		String appid = APPID;
		String appkey = APPKEY;
		if(platform.toLowerCase().contains("ios"))
		{
			appid = IOS_APPID;
			appkey = IOS_APPKEY;
		}
		
		String url = "http://service.sj.91.com/usercenter/AP.aspx";
		LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();
		params.put("AppId",String.valueOf(appid));
		params.put("Act", "4");
		params.put("Uin", uin);
		params.put("SessionId", sessionId);
		String sign = sign(params,appkey);
		String queryString = "";
		String keys[] = params.keySet().toArray(new String[0]);
		for(String k : keys) {
			String v = params.get(k);
			queryString += k + "=" + v  + "&";
		}
		queryString += "Sign=" + sign;
		url = url + "?" + queryString;
		HashMap headers = new HashMap();
		String content =  "";
		try {
			byte bytes[] = HttpUtils.webGet(new java.net.URL(url), headers, 5000, 10000);
			String encoding = (String)headers.get(HttpUtils.ENCODING);
			Integer code = (Integer)headers.get(HttpUtils.Response_Code);
			String message = (String)headers.get(HttpUtils.Response_Message);
			content = new String(bytes,encoding);
			logger.info("[调用登录接口] [成功] ["+url+"] [code:"+code+"] ["+message+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			logger.info("[调用登录接口] [失败] ["+url+"] [code:--] [--] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
			return false;
		}
		JacksonManager jm = JacksonManager.getInstance();
		try {
			Map map =(Map)jm.jsonDecodeObject(content);
			String code = (String)map.get("ErrorCode");
			String desp = (String)map.get("ErrorDesc");
			logger.info("[解析登录返回数据成功] [结果:"+(code.equals("1")?"验证合法":"验证失败")+"] [uin:"+uin+"] [sessionId:"+sessionId+"] [return_code:"+code+"] [return_desp:"+desp+"] [codeDesp:"+getCodeDescription(Integer.valueOf(code))+"] ["+url+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			if(code.equals("1")) {
				return true;
			} 
		}catch (Exception e) {
			logger.info("[解析登录返回数据失败] [uin:"+uin+"] [sessionId:"+sessionId+"] [return:"+content+"] ["+url+"] ["+(System.currentTimeMillis()-startTime)+"ms]", e);
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
	
	public boolean validateNEW912(String platform,String uin, String sessionId){
		long startTime = System.currentTimeMillis();
		String loginVaildataUrl = BaiduBossClientService.getInstance().newBaiduUrl;
		String appid = NEW_91_APP_ID;
		String appkey = NEW_91_APP_KEY;
		if(platform.toLowerCase().contains("ios")){
			appid = IOS_APPID;
			appkey = IOS_APPKEY;
		}
		LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();
		params.put("AppID",String.valueOf(appid));
		params.put("AccessToken", sessionId);
		String sign = sign(params,NEW_91_SECRET_KEY);
		params.put("Sign", sign);
		String queryURI = buildParams(params);
		String content =  "";
		Map headers = new HashMap();
		try {
			byte bytes[] = HttpUtils.webPost(new java.net.URL(loginVaildataUrl),queryURI.getBytes(), headers, 10000, 10000);
			String encoding = (String)headers.get(HttpUtils.ENCODING);
			Integer code = (Integer)headers.get(HttpUtils.Response_Code);
			String message = (String)headers.get(HttpUtils.Response_Message);
			content = new String(bytes,encoding);
			logger.info("[调用登录接口] [new912] [成功] ["+uin+"] [queryURI:"+queryURI+"] [code:"+code+"] [encoding:"+encoding+"] [message:"+message+"] [content:"+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			String encoding = (String)headers.get(HttpUtils.ENCODING);
			Integer code = (Integer)headers.get(HttpUtils.Response_Code);
			String message = (String)headers.get(HttpUtils.Response_Message);
			logger.info("[调用登录接口] [new912] [失败] ["+uin+"] [queryURI:"+queryURI+"] [code:"+code+"] [encoding:"+encoding+"] [message:"+message+"] [content:"+content+"]  ["+(System.currentTimeMillis()-startTime)+"ms]",e);
			return false;
		}
		
		JacksonManager jm = JacksonManager.getInstance();
		try {
			Map map =(Map)jm.jsonDecodeObject(content);
			Integer code = (Integer)map.get("ResultCode");
			String rAppid = "";
			try{
				Integer iAppid = (Integer)map.get("AppID");
				rAppid = iAppid+"";
			}catch(Exception e){
				rAppid = (String)map.get("AppID");
			}
			String result = (String)map.get("ResultMsg");
			String rSign = (String)map.get("Sign");
			String rContent = (String)map.get("Content");
			String handleContent = Base64.decode(URLDecoder.decode(rContent));
			
			LinkedHashMap<String,String> rParams = new LinkedHashMap<String,String>();
			rParams.put("AppID",rAppid);
			rParams.put("ResultCode",String.valueOf(code));
			rParams.put("Content",Base64.encode(handleContent));
			String selfSign = sign(rParams,NEW_91_SECRET_KEY);
			
			logger.info("[解析登录返回数据成功] [new912] [结果:"+(code==1?"验证合法":"验证失败")+"] [验证结果:"+(selfSign.equals(rSign)?"验证合法":"验证失败")+"] [uin:"+uin+"] [rAppid:"+rAppid+"] [sessionId:"+sessionId+"] [return_code:"+code+"] [return_desp:"+result+"] [codeDesp:"+getCodeDescription(Integer.valueOf(code))+"] [selfSign:"+selfSign+"] [rSign:"+rSign+"] [rContent:"+rContent+"] ["+URLDecoder.decode(rContent)+"] [handleContent:"+handleContent+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			if(code.intValue() == 1 && selfSign.equals(rSign)) {
				return true;
			} 
		}catch (Exception e) {
			logger.info("[解析登录返回数据失败] [new912] [uin:"+uin+"] [sessionId:"+sessionId+"] [return:"+content+"] [queryURI:"+queryURI+"] ["+(System.currentTimeMillis()-startTime)+"ms]", e);
		}
		return false;
	}
	
	public boolean validateNEW91(String platform,String uin, String sessionId){
		long startTime = System.currentTimeMillis();
		String loginVaildataUrl = "http://querysdkapi.91.com/CpLoginStateQuery.ashx";
		String appid = NEW_91_APP_ID;
		String appkey = NEW_91_APP_KEY;
		if(platform.toLowerCase().contains("ios")){
			appid = IOS_APPID;
			appkey = IOS_APPKEY;
		}
		LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();
		params.put("AppID",String.valueOf(appid));
		params.put("AccessToken", sessionId);
		String sign = sign(params,NEW_91_SECRET_KEY);
		params.put("Sign", sign);
		String queryURI = buildParams(params);
		String content =  "";
		Map headers = new HashMap();
		try {
			byte bytes[] = HttpUtils.webPost(new java.net.URL(loginVaildataUrl),queryURI.getBytes(), headers, 10000, 10000);
			String encoding = (String)headers.get(HttpUtils.ENCODING);
			Integer code = (Integer)headers.get(HttpUtils.Response_Code);
			String message = (String)headers.get(HttpUtils.Response_Message);
			content = new String(bytes,encoding);
			logger.info("[调用登录接口] [new91] [成功] ["+uin+"] [queryURI:"+queryURI+"] [code:"+code+"] [encoding:"+encoding+"] [message:"+message+"] [content:"+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			String encoding = (String)headers.get(HttpUtils.ENCODING);
			Integer code = (Integer)headers.get(HttpUtils.Response_Code);
			String message = (String)headers.get(HttpUtils.Response_Message);
			logger.info("[调用登录接口] [new91] [失败] ["+uin+"] [queryURI:"+queryURI+"] [code:"+code+"] [encoding:"+encoding+"] [message:"+message+"] [content:"+content+"]  ["+(System.currentTimeMillis()-startTime)+"ms]",e);
			return false;
		}
		
		JacksonManager jm = JacksonManager.getInstance();
		try {
			Map map =(Map)jm.jsonDecodeObject(content);
			Integer code = (Integer)map.get("ResultCode");
			String rAppid = "";
			try{
				Integer iAppid = (Integer)map.get("AppID");
				rAppid = iAppid+"";
			}catch(Exception e){
				rAppid = (String)map.get("AppID");
			}
			String result = (String)map.get("ResultMsg");
			String rSign = (String)map.get("Sign");
			String rContent = (String)map.get("Content");
			String handleContent = Base64.decode(URLDecoder.decode(rContent));
			
			LinkedHashMap<String,String> rParams = new LinkedHashMap<String,String>();
			rParams.put("AppID",rAppid);
			rParams.put("ResultCode",String.valueOf(code));
			rParams.put("Content",Base64.encode(handleContent));
			String selfSign = sign(rParams,NEW_91_SECRET_KEY);
			
			logger.info("[解析登录返回数据成功] [new91] [结果:"+(code==1?"验证合法":"验证失败")+"] [验证结果:"+(selfSign.equals(rSign)?"验证合法":"验证失败")+"] [uin:"+uin+"] [rAppid:"+rAppid+"] [sessionId:"+sessionId+"] [return_code:"+code+"] [return_desp:"+result+"] [codeDesp:"+getCodeDescription(Integer.valueOf(code))+"] [selfSign:"+selfSign+"] [rSign:"+rSign+"] [rContent:"+rContent+"] ["+URLDecoder.decode(rContent)+"] [handleContent:"+handleContent+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			if(code.intValue() == 1 && selfSign.equals(rSign)) {
				return true;
			} 
		}catch (Exception e) {
			logger.info("[解析登录返回数据失败] [new91] [uin:"+uin+"] [sessionId:"+sessionId+"] [return:"+content+"] [queryURI:"+queryURI+"] ["+(System.currentTimeMillis()-startTime)+"ms]", e);
		}
		return false;
	}
	
	public static String sign(LinkedHashMap<String,String> params,String appkey){
		String values[] = params.values().toArray(new String[0]);
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < values.length ; i++){
			sb.append(values[i]);
		}
		String md5Str = sb.toString()+appkey;
		String sign = StringUtil.hash(md5Str);
		return sign.toLowerCase();
	}

	public static void main(String args[]) {
////		String str = "10211042857230444d4d79a0ee7a7345486cbccc4e91a8d880ebd0ce0aeb10d428769c5a9498b80f580a007ab104b350";
////		System.out.println("签名:"+StringUtil.hash(str));
//		String str = "100001100001 测试签名 1D375C1E-F59A-4f9e-BF57-D471681C7774";
//		//签名方式    MD5(AppID+ResultCode+Content+SecretKey)
//		String content = "eyJVSUQiOjU5ODI3NTQxOX0=";
//		String handleContent = Base64.decode(URLDecoder.decode(content));
////		String signStr = "59534341"+handleContent +"mnaOEobpbBWBQAKdamjphPDaCL6zHoDQ";
////		signStr = StringUtil.hash(signStr);
////			String signStr2 = "";
////			try {
////				byte[] bytes = signStr.getBytes("utf-8");
////				MessageDigest md5 = MessageDigest.getInstance("MD5"); md5.update(bytes);
////				byte[] md5Byte = md5.digest();
////				if(md5Byte != null){
////				signStr2 = HexBin.encode(md5Byte); }
////				} catch (NoSuchAlgorithmException e) { e.printStackTrace();
////				} catch (UnsupportedEncodingException e) { e.printStackTrace();
////				}
//		
//		LinkedHashMap<String,String> rParams = new LinkedHashMap<String,String>();
//		rParams.put("AppID",String.valueOf(5953434));
//		rParams.put("ResultCode",String.valueOf(1));
//		rParams.put("Content",Base64.encode(handleContent));
//		String selfSign = sign(rParams,NEW_91_SECRET_KEY);
//		System.out.println("正确签名:7b668cd99d331c74d32cf520d55c66b9\n"+"my 签名:"+selfSign.toLowerCase());
		String content = "eyJVSUQiOjU5ODI3NTQxOSwiTWVyY2hhbmRpc2VOYW1lIjoi6ZO25LikIiwiT3JkZXJNb25leSI6IjEwLjAwIiwiU3RhcnREYXRlVGltZSI6IjIwMTUtMDUtMTkgMTE6MjQ6NDciLCJCYW5rRGF0ZVRpbWUiOiIyMDE1LTA1LTE5IDExOjI1OjQxIiwiT3JkZXJTdGF0dXMiOjEsIlN0YXR1c01zZyI6IuaIkOWKnyIsIkV4dEluZm8iOiIiLCJWb3VjaGVyTW9uZXkiOjB9";
		String handleContent = Base64.decode(URLDecoder.decode(content));
		System.out.println("content:"+handleContent);
		String rightSign = "46674312b5cfc9077b29bee555d038fe";
		LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();
		params.put("AppID", "5953434");
		params.put("OrderSerial", "d4e14c9c4aabbf75_01001_2015051911_000000");
		params.put("CooperatorOrderSerial", "wx-2015051911-1100000000000144402");
		params.put("Content", content);
		String mySign = sign(params,"mnaOEobpbBWBQAKdamjphPDaCL6zHoDQ");
		System.out.println("正确签名:"+rightSign);
		System.out.println("我的签名:"+mySign);
	}
	
}
