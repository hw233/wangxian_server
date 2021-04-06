package com.fy.gamegateway.thirdpartner.baidusdk;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.fy.gamegateway.thirdpartner.nineone.Base64;
import com.fy.gamegateway.thirdpartner.qq.mime.qweibo.utils.Base64Encoder;
import com.fy.gamegateway.tools.JacksonManager;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.text.StringUtil;



public class BaiduBossClientService {
	
	static Logger logger = Logger.getLogger(BaiduBossClientService.class);
	private String appid = "1670";
	private String appkey = "f8fa7b2a60d2236b119ca003bbc12e5c";
	private String session_secret = "2f68ed90d5cd79851858fbe83f723c61";
	
	private static String NEW_APP_ID = "5179344"; 
	private static String NEW_APP_KEY = "sL7MReEdeBWl498aoM3cXkWF";
	private static String NEW_SECRET_KEY = "rw1j0wjI9hWKoKACyRhGdF6BPI6Sg3wb";

	public String newBaiduUrl = "http://querysdkapi.baidu.com/query/cploginstatequery";
	private static Map<String,String> errinfoMap = new ConcurrentHashMap<String, String>();
	
	static
	{
		errinfoMap.put("1","未知错误");
		errinfoMap.put("2","服务暂时不可用");
		errinfoMap.put("3","不支持的API办法");
		errinfoMap.put("4","达到最大连接数");
		errinfoMap.put("5","客户端IP地址未授权");
		errinfoMap.put("6","无权访问用户数据");
		errinfoMap.put("7","操作失败");
		errinfoMap.put("8","无效的客户端");
		errinfoMap.put("9","平台不支持的API");
		errinfoMap.put("10","通讯错误");
		errinfoMap.put("11","平台未认证");
		errinfoMap.put("12","密码错误");
		errinfoMap.put("13","用户名不存在");
		errinfoMap.put("100","无效的参数");
		errinfoMap.put("101","token无效");
		errinfoMap.put("102","Access token 超时");
		errinfoMap.put("103","签名错误");
		errinfoMap.put("104","无效的API key");
		errinfoMap.put("105","余额不足");
		errinfoMap.put("106","用户名已注册");
		errinfoMap.put("107","用户名不符合规则");
		errinfoMap.put("108","密码不符合规则");
		errinfoMap.put("109","未知错误");
	}
	
	
	private static BaiduBossClientService self;
	public static BaiduBossClientService getInstance(){
		if(self != null){
			return self;
		}
		self = new BaiduBossClientService();
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
	
	public BaiduLoginResult loginForNewBaiDu2(String uin, String sessionId){
		long startTime = System.currentTimeMillis();
		String loginVaildataUrl = newBaiduUrl;
		String appid = NEW_APP_ID;
		String appkey = NEW_APP_KEY;
		BaiduLoginResult r = new BaiduLoginResult();
		LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();
		params.put("AppID",String.valueOf(appid));
		params.put("AccessToken", sessionId);
		String sign = sign(params,NEW_SECRET_KEY);
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
			logger.info("[调用登录接口] [newbaidu2] [成功] ["+uin+"] [queryURI:"+queryURI+"] [code:"+code+"] [encoding:"+encoding+"] [message:"+message+"] [content:"+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			String encoding = (String)headers.get(HttpUtils.ENCODING);
			Integer code = (Integer)headers.get(HttpUtils.Response_Code);
			String message = (String)headers.get(HttpUtils.Response_Message);
			logger.info("[调用登录接口] [newbaidu2] [失败] ["+uin+"] [queryURI:"+queryURI+"] [code:"+code+"] [encoding:"+encoding+"] [message:"+message+"] [content:"+content+"]  ["+(System.currentTimeMillis()-startTime)+"ms]",e);
			r.code = "404";
			r.message = "调用百度SDK登陆验证接口出现异常";
			r.message = "用户名或者密码错误";
			return r;
		}
		
		JacksonManager jm = JacksonManager.getInstance();
		try {
			Map map =(Map)jm.jsonDecodeObject(content);
			Integer code = (Integer)map.get("ResultCode");
			String rAppid = "";
			try{
				Integer IAppid = (Integer)map.get("AppID");
				rAppid = IAppid+"";
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
			String selfSign = sign(rParams,NEW_SECRET_KEY);
			
			logger.info("[解析登录返回数据成功] [newbaidu2] [结果:"+(code==1?"验证合法":"验证失败")+"] [验证结果:"+(selfSign.equals(rSign)?"验证合法":"验证失败")+"] [uin:"+uin+"] [rAppid:"+rAppid+"] [sessionId:"+sessionId+"] [return_code:"+code+"] [return_desp:"+result+"] [rSign:"+rSign+"] [rContent:"+rContent+"] ["+URLDecoder.decode(rContent)+"] ["+Base64Encoder.encode(URLDecoder.decode(rContent).getBytes())+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			r.code = String.valueOf(code);
			r.message = result;
			if(code.intValue() == 1 && selfSign.equals(rSign)){
				r.uid = uin;
				r.accessToken = sessionId;
			}else{
				r.message = errinfoMap.get(r.code);
			}
		}catch (Exception e) {
			logger.info("[解析登录返回数据失败] [newbaidu2] [uin:"+uin+"] [sessionId:"+sessionId+"] [return:"+content+"] [queryURI:"+queryURI+"] ["+(System.currentTimeMillis()-startTime)+"ms]", e);
			r.code = "500";
			r.message = "读取多酷登陆接口内容出现异常";
			r.message = "用戶名或密码錯誤";
		}
		return r;
	}
	
	public BaiduLoginResult loginForNewBaiDu(String uin, String sessionId){
		long startTime = System.currentTimeMillis();
		String loginVaildataUrl = "http://querysdkapi.91.com/CpLoginStateQuery.ashx";
		String appid = NEW_APP_ID;
		String appkey = NEW_APP_KEY;
		BaiduLoginResult r = new BaiduLoginResult();
		LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();
		params.put("AppID",String.valueOf(appid));
		params.put("AccessToken", sessionId);
		String sign = sign(params,NEW_SECRET_KEY);
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
			logger.info("[调用登录接口] [newbaidu] [成功] ["+uin+"] [queryURI:"+queryURI+"] [code:"+code+"] [encoding:"+encoding+"] [message:"+message+"] [content:"+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			String encoding = (String)headers.get(HttpUtils.ENCODING);
			Integer code = (Integer)headers.get(HttpUtils.Response_Code);
			String message = (String)headers.get(HttpUtils.Response_Message);
			logger.info("[调用登录接口] [newbaidu] [失败] ["+uin+"] [queryURI:"+queryURI+"] [code:"+code+"] [encoding:"+encoding+"] [message:"+message+"] [content:"+content+"]  ["+(System.currentTimeMillis()-startTime)+"ms]",e);
			r.code = "404";
			r.message = "调用百度SDK登陆验证接口出现异常";
			r.message = "用户名或者密码错误";
			return r;
		}
		
		JacksonManager jm = JacksonManager.getInstance();
		try {
			Map map =(Map)jm.jsonDecodeObject(content);
			Integer code = (Integer)map.get("ResultCode");
			String rAppid = "";
			try{
				Integer IAppid = (Integer)map.get("AppID");
				rAppid = IAppid+"";
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
			String selfSign = sign(rParams,NEW_SECRET_KEY);
			
			logger.info("[解析登录返回数据成功] [newbaidu] [结果:"+(code==1?"验证合法":"验证失败")+"] [验证结果:"+(selfSign.equals(rSign)?"验证合法":"验证失败")+"] [uin:"+uin+"] [rAppid:"+rAppid+"] [sessionId:"+sessionId+"] [return_code:"+code+"] [return_desp:"+result+"] [rSign:"+rSign+"] [rContent:"+rContent+"] ["+URLDecoder.decode(rContent)+"] ["+Base64Encoder.encode(URLDecoder.decode(rContent).getBytes())+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			r.code = String.valueOf(code);
			r.message = result;
			if(code.intValue() == 1 && selfSign.equals(rSign)){
				r.uid = uin;
				r.accessToken = sessionId;
			}else{
				r.message = errinfoMap.get(r.code);
			}
		}catch (Exception e) {
			logger.info("[解析登录返回数据失败] [newbaidu] [uin:"+uin+"] [sessionId:"+sessionId+"] [return:"+content+"] [queryURI:"+queryURI+"] ["+(System.currentTimeMillis()-startTime)+"ms]", e);
			r.code = "500";
			r.message = "读取多酷登陆接口内容出现异常";
			r.message = "用戶名或密码錯誤";
		}
		return r;
	}
	
	public BaiduLoginResult login(String username,String password){
		try
		{
			long startTime = System.currentTimeMillis();
			
			
			if(username.startsWith("BAIDUSDKUSER_")) {
				username = username.split("_")[1];
			}
			

			String url = "http://sdk.m.duoku.com/openapi/sdk/checksession";

			LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();

			params.put("appid",appid);
			params.put("appkey",appkey);
			params.put("uid",username);
			params.put("sessionid", password);

			params.put("clientsecret",sign(params).toLowerCase());


			HashMap headers = new HashMap();

			String content =  "";
			content = buildParams(params);
			if(logger.isDebugEnabled())
			{
				logger.debug("[调用登陆接口] [拼装参数] ["+content+"] [appid:"+appid+"] [appkey:"+appkey+"] [session_secret:"+session_secret+"]");
			}

			try {
				byte bytes[] = HttpUtils.webPost(new java.net.URL(url), content.getBytes(), headers, 5000, 10000);

				String encoding = (String)headers.get(HttpUtils.ENCODING);
				Integer code = (Integer)headers.get(HttpUtils.Response_Code);
				String message = (String)headers.get(HttpUtils.Response_Message);
				content = new String(bytes,encoding);
				if(logger.isInfoEnabled())
					logger.info("[调用登陆接口] [成功] ["+url+"] [code:"+code+"] ["+message+"] ["+content+"] [用户名:"+username+"] [密码:"+password+"] [cli[appid:"+appid+"] [appkey:"+appkey+"] [session_secret:"+session_secret+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			} catch (Exception e) {
				logger.error("[调用登陆接口] [失败] ["+url+"] [code:--] [--] [sign:"+params.get("verifystring")+"] ["+content+"] [用户名:"+username+"] [密码:"+password+"] [appid:"+appid+"] [session_secret:"+session_secret+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
				BaiduLoginResult r = new BaiduLoginResult();
				r.code = "404";
				r.message = "调用百度SDK登陆验证接口出现异常";
				r.message = "用户名或者密码错误";
				return r;
			}

			//解析返回结果
			JacksonManager jm = JacksonManager.getInstance();
			BaiduLoginResult r = new BaiduLoginResult();
			try {
				Map map =(Map)jm.jsonDecodeObject(content);
				if(!StringUtils.isEmpty((String)map.get("error_code")))
				{
					r.code = (String)map.get("error_code");
					r.message = (String)map.get("error_msg");
					
					if(r.code.trim().equals("0"))
					{
						r.uid = username;
						r.accessToken = password;
					}
					else
					{
						r.message = errinfoMap.get(r.code);
					}
				}


				/*			boolean isSucc = false;
				if(!StringUtils.isEmpty(r.clientSecret))
				{
					if(r.clientSecret.equalsIgnoreCase(StringUtil.hash(r.uid)))
				}*/

				if(logger.isInfoEnabled())
				{
					logger.info("[多酷登陆] ["+(StringUtils.isEmpty(r.uid) ? "失败" : "成功")+"] [字符集:"+(String)headers.get(HttpUtils.ENCODING)+"] [code:"+(Integer)headers.get(HttpUtils.Response_Code)+"] [响应信息:"+(String)headers.get(HttpUtils.Response_Message)+"] [content:"+content+"] [用户名:"+username+"] [密码:"+password+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
				}
			} catch (Exception e) {
				logger.error("[读取多酷登陆接口JSON内容] [失败] [解析响应内容信息] [失败] [字符集:"+(String)headers.get(HttpUtils.ENCODING)+"] [code:"+(Integer)headers.get(HttpUtils.Response_Code)+"] [响应信息:"+(String)headers.get(HttpUtils.Response_Message)+"] [content:"+content+"] [用户名:"+username+"] [密码:"+password+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
				logger.error("[读取多酷登陆接口JSON内容] [失败] [解析响应内容信息] [失败] [字符集:"+(String)headers.get(HttpUtils.ENCODING)+"] [code:"+(Integer)headers.get(HttpUtils.Response_Code)+"] [响应信息:"+(String)headers.get(HttpUtils.Response_Message)+"] [content:"+content+"] [用户名:"+username+"] [密码:"+password+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
				r.code = "500";
				r.message = "读取多酷登陆接口内容出现异常";
				r.message = "用戶名或密码錯誤";
				return r;
			}
			return r;
		}
		catch(Exception e)
		{
			BaiduLoginResult r = new BaiduLoginResult();
			r.code = "500";
			r.message = "读取多酷登陆接口内容出现异常";
			r.message = "未知错误";
			
			logger.error("[多酷登陆] [失败:出现未知异常]",e);
			return r;
		}

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
	
	private  String sign(LinkedHashMap<String,String> params){
		String values[] = params.values().toArray(new String[0]);
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < values.length ; i++){
			sb.append(values[i]);
		}
		String md5Str = sb.toString()+session_secret;
		String sign = StringUtil.hash(md5Str);
		
		if(logger.isDebugEnabled())
		{
			logger.debug("[调用登陆接口] [拼装签名字符串] [签名前:"+md5Str+"] [签名后:"+sign+"] [appid:"+appid+"] [session_secret:"+session_secret+"]");
		}
		
		return sign;
	}
	

}
