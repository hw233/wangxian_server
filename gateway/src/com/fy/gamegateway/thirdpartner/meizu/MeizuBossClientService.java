package com.fy.gamegateway.thirdpartner.meizu;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.fy.gamegateway.tools.JacksonManager;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.text.StringUtil;



public class MeizuBossClientService {
	
	static Logger logger = Logger.getLogger(MeizuBossClientService.class);

	public static String APPID = "A544";
	public static String APPKEY = "7a875ddc676e4bce0a6abc6e8947e4f8";
	
	public static String NEW_APPID = "3198169";
	public static String NEW_APPKEY = "21a19e0d38d44cdf888bef8259305187";
	public static String NEW_appSecre = "gKqv28YXHjGk5ip8FZQuEdCc7ZgEBrXc";
	
	
	
	
	
	
	private static MeizuBossClientService self;
	public static MeizuBossClientService getInstance(){
		if(self != null){
			return self;
		}
		self = new MeizuBossClientService();
		return self;
	}
	
	public MeizuLoginResult newSdkLogin(String username, String password){
		try{
			long startTime = System.currentTimeMillis();
			String url = "https://api.game.meizu.com/game/security/checksession";
			
			String uid = username;
			if(username.contains("MEIZUUSER_")){
				uid = username.split("MEIZUUSER_")[1];
			}
			
			LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();
			params.put("app_id",NEW_APPID);
			params.put("uid",uid);
			params.put("session_id",password);
			params.put("ts",startTime+"");
			String sign = getSignCode(params, NEW_appSecre);
			params.put("sign_type","md5");
			params.put("sign",sign);
			
			String paramString = buildParams(params);
			HashMap headers = new HashMap();
			String content =  "";
			
			try {
				byte bytes[] = HttpUtils.webPost(new java.net.URL(url), paramString.getBytes("utf-8"), headers, 60000, 60000);
				String encoding = (String)headers.get(HttpUtils.ENCODING);
				Integer code = (Integer)headers.get(HttpUtils.Response_Code);
				String message = (String)headers.get(HttpUtils.Response_Message);
				content = new String(bytes,encoding);
				logger.info("[魅族newsdk调用登录接口] [成功] ["+url+"] [paramString:"+paramString+"] [code:"+code+"] ["+message+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			} catch (Exception e) {
				logger.info("[魅族newsdk调用登录接口] [失败] ["+url+"] [paramString:"+paramString+"] [url:"+url+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
				MeizuLoginResult r = new MeizuLoginResult();
				r.status = "调用接口出现异常";
				return r;
			}
	
			JacksonManager jm = JacksonManager.getInstance();
			try {
				Map map =(Map)jm.jsonDecodeObject(content);
				MeizuLoginResult r = new MeizuLoginResult();

				Integer resultState = (Integer)map.get("code");
				
				if(resultState == null){
					r.status = "cod is null";
					logger.info("[魅族newsdk解析登录返回数据] [失败:code is null] [paramString:"+paramString+"] [status:"+r.status+"] [message:"+content+"] [username:"+username+"] [accessToken:"+password+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
					return r;
				}
				
				if(resultState != 200){
					r.status = "code状态码不是成功";
					logger.info("[魅族newsdk解析登录返回数据] [失败:code状态码不是成功] [paramString:"+paramString+"] [status:"+r.status+"] [message:"+content+"] [username:"+username+"] [accessToken:"+password+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
					return r;
				}
				r.username = "MEIZUUSER_"+uid;
				r.status = "200";
				logger.info("[魅族newsdk最后验证] ["+(resultState==200?"成功":"失败")+"] [username:"+username+"] [r.username:"+r.username+"] [code:"+r.status+"] ["+url+"] [status:"+r.status+"] [message:"+content+"] [accessToken:"+password+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
				return r;
				
//				params.clear();
//				params.put("app_id", NEW_APPID);
//				params.put("flyme_uid", uid);
//				params.put("sign",getSignCode(params, NEW_appSecre));
//				
//				paramString = buildParams(params);
//				HashMap headers2 = new HashMap();
//				content =  "";
//				url = "https://api.game.meizu.com/game/sdk37/transform";
//				try {
//					byte bytes[] = HttpUtils.webPost(new java.net.URL(url), paramString.getBytes("utf-8"), headers2, 60000, 60000);
//					String encoding = (String)headers2.get(HttpUtils.ENCODING);
//					Integer code = (Integer)headers2.get(HttpUtils.Response_Code);
//					String message = (String)headers2.get(HttpUtils.Response_Message);
//					content = new String(bytes,encoding);
//					logger.info("[魅族newsdk调用获取37UID] [成功] ["+url+"] [paramString:"+paramString+"] [code:"+code+"] ["+message+"] encoding:"+encoding+"[] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
//				} catch (Exception e) {
//					logger.info("[魅族newsdk调用获取37UID] [失败] ["+url+"] [paramString:"+paramString+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
//					MeizuLoginResult r2 = new MeizuLoginResult();
//					r2.status = "魅族newsdk调用获取37UID";
//					return r2;
//				}
//				
//				map =(Map)jm.jsonDecodeObject(content);
//				resultState = (Integer)map.get("code");
//				
//				if(resultState == null){
//					r.status = "cod is null";
//					logger.info("[魅族newsdk解析获取37UID数据] [失败:code is null] [paramString:"+paramString+"] [status:"+r.status+"] [message:"+content+"] [username:"+username+"] [accessToken:"+password+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
//					return r;
//				}
//				
//				if(resultState != 200){
//					r.status = "code状态码不是成功";
//					logger.info("[魅族newsdk解析获取37UID数据] [失败:code状态码不是成功] [paramString:"+paramString+"] [status:"+r.status+"] [message:"+content+"] [username:"+username+"] [accessToken:"+password+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
//					return r;
//				}
//				
//				if(map.get("value") == null || map.get("value").toString().equals("null") ){
//					r.username = username;
//				}else{
//					HashMap valueStr = (HashMap)(map.get("value"));
//					if(!valueStr.containsKey("ext_uid")){
//						r.status = "ext_uid is null";
//						logger.error("[魅族2用户登录获取37UID] [失败:ext_uid is null]  [paramString:"+paramString+"] [status:"+r.status+"] [message:"+content+"] [username:"+username+"] [accessToken:"+password+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
//						return r;
//					}
//					String ext_uid = valueStr.get("ext_uid").toString();
//					r.username = "MEIZUUSER_"+ext_uid;
//				}
//				if(NewLoginHandler.errorUsers.get(username) != null){
//					r.username = NewLoginHandler.errorUsers.get(username);
//				}
//				r.status = "200";
//				logger.info("[魅族newsdk最后验证] ["+(resultState==200?"成功":"失败")+"] [username:"+username+"] [r.username:"+r.username+"] [code:"+r.status+"] ["+url+"] [status:"+r.status+"] [message:"+content+"] [accessToken:"+password+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
//				return r;
			}catch (Exception e) {
				MeizuLoginResult r = new MeizuLoginResult();
				r.status = "404";
				r.message = "解析JSON串出现异常";
				logger.error("[魅族newsdk解析登录返回数据] [失败] ["+url+"] [status:"+r.status+"] [message:"+r.message+"] [accessToken:"+password+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
				return r;
			}
		}catch(Throwable t){
			MeizuLoginResult r = new MeizuLoginResult();
			r.status = "404";
			r.message = "出现未知异常";
			logger.error("[魅族newsdk调用登陆接口] [失败:未知异常] [--] [status：--] [message:--] [username："+username+"] [password:"+password+"]",t);
			return r;
		}

	}
	
	private  String sign(LinkedHashMap<String,String> params,String scertStr){
		String values[] = params.values().toArray(new String[0]);
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < values.length ; i++){
			sb.append(values[i]);
		}
		String md5Str = sb.toString() + scertStr;
		String sign = StringUtil.hash(md5Str);
		
		if(logger.isDebugEnabled()){
			logger.debug("[调用登陆接口] [拼装签名字符串] [签名前:"+md5Str+"] [签名后:"+sign+"]");
		}
		
		return sign;
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
	
	public static String toJsonStr(HashMap<String,String> params){
		StringBuffer sb = new StringBuffer();
		String keys[] = params.keySet().toArray(new String[0]);
		for(int i = 0 ; i < keys.length ; i++){
			String v = params.get(keys[i]);
			sb.append("\""+keys[i]+"\":\""+v+"\"");
			if(i < keys.length -1){
				sb.append(",");
			}
		}
		return sb.toString();
	}
	
	public static String sign(int cpId,HashMap<String,String> params,String secretkey){
		
		String keys[] = params.keySet().toArray(new String[0]);
		java.util.Arrays.sort(keys);
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < keys.length ; i++){
			String v = params.get(keys[i]);
			sb.append(keys[i]+"="+v);
		}
		String md5Str =cpId + sb.toString() + secretkey;
		
		return md5Str;
	}
	
	public static final String getSignCode(Map map, String appSecret) throws Exception {
		String queryString = concatMap(map);
		String signText = queryString + ":" + appSecret;
		String serverSignCode = getMD5Str(signText);
		return serverSignCode;
	}
	public static final String concatMap(Map<String, String> params) throws UnsupportedEncodingException {
		List<String> keysSet = new ArrayList<String>();
		keysSet.addAll(params.keySet());
		Collections.sort(keysSet);
		StringBuilder sb = new StringBuilder();
		for (String key : keysSet) {
			if (key.equals("sign") || key.equals("sign_type")) {
				continue;
			}
			Object value = params.get(key);
			sb.append("&").append(key).append("=").append(value.toString());
		}

		if (sb.length() > 0 && sb.toString().startsWith("&")) {
			sb.delete(0, 1);
		}
		return sb.toString();
	}
	public static String getMD5Str(String ...str)throws Exception {
		MessageDigest messageDigest = null;
		StringBuffer strB = new StringBuffer();
		for(int i = 0; i< str.length; i++){
			strB.append(str[i]);
		}
		try {
			messageDigest = MessageDigest.getInstance("MD5");

			messageDigest.reset();

			messageDigest.update(strB.toString().getBytes("UTF-8"));
		} catch (Exception e) {
			throw e;
		}

		byte[] byteArray = messageDigest.digest();

		StringBuffer md5StrBuff = new StringBuffer();

		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(
						Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}

		return md5StrBuff.toString();
	}
}
