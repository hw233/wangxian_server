package com.fy.gamegateway.thirdpartner.kaopu;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.fy.gamegateway.tools.JacksonManager;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.text.StringUtil;

public class KaoPuClientService {

	public static KaoPuClientService self;
	
	public static Logger logger = Logger.getLogger(KaoPuClientService.class);
	
	public String queryLoginUrl = "http://sdk.geturl.kpzs.com/api/UserAuthUrl";
	
	private String KAOPU_APPKEY = "10112";
	
	private String KAOPU_SECRETKEY = "BFCF79B9-B1D4-4E95-B9A2-64DD51E15580";
	
	private String KAOPU_APPID = "10112001"; 
	
	private String splitStr = "@##@@";
	
	private String [] secretKeys = {"18257284-7F5D-348D-AB09-299E5B7DD997","655A957D-157D-7C21-E3A7-9CAAFA835318",
			"F467CA93-D550-346D-6BCB-173995F7C83A","BD32817A-99F9-2E26-5B33-15208F7B360A"};
	
	public static KaoPuClientService getInstance(){
		if(self == null){
			self = new KaoPuClientService();
		}
		return self;
	}
	
	/**
	 * 获取用户身份验证有效性的 url 
	 * @return@author
	 */
	public String getKaoPuLoginUrl(String userName,String pwd){
		long startTime = System.currentTimeMillis();
		String args [] = pwd.split(splitStr);
		if(args != null && args.length == 5){
			String token = args[0];
			String version = args[1];
			String imei = args[2];
			String channelkey = args[3];
			
			LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();
			params.put("appid",KAOPU_APPID);
			params.put("channelkey",channelkey);
			params.put("imei",imei);
			params.put("r","0");
			params.put("tag",KAOPU_APPKEY);
			params.put("tagid",KAOPU_SECRETKEY);
			params.put("version",version);
			params.put("sign",sign(params,secretKeys[0]));
			
			String paramString = buildParams(params);
			String url = queryLoginUrl + "?" + paramString;
			HashMap headers = new HashMap();
			String content =  "";
			try {
				byte bytes[] = HttpUtils.webGet(new java.net.URL(url), headers, 5000, 10000);
				String encoding = (String)headers.get(HttpUtils.ENCODING);
				Integer code = (Integer)headers.get(HttpUtils.Response_Code);
				String message = (String)headers.get(HttpUtils.Response_Message);
				content = new String(bytes,encoding);
				logger.info("[获取靠谱登录验证url] [成功] [userName:"+userName+"] ["+url+"] [code:"+code+"] ["+message+"] ["+content+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("[获取靠谱登录验证url] [失败] [userName:"+userName+"] ["+url+"] ["+content+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]",e);
				return "";
			}
			JacksonManager jm = JacksonManager.getInstance();
			try {
				Map map =(Map)jm.jsonDecodeObject(content);
				int code = (Integer)map.get("code");
				HashMap data = (HashMap)map.get("data");
				if(code == 1 && data != null && data.size() > 0){
					String loginurl = (String)data.get("url");
					if(loginurl != null){
						return loginurl;
					}else{
						logger.info("[解析靠谱获取登录验证url返回数据] [出错] [userName:"+userName+"] [url:"+url+"]  [返回内容:"+content+"] [loginurl:"+loginurl+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
					}
				}
				logger.info("[解析靠谱获取登录验证url返回数据] [结果:"+(code==1?"验证合法":"验证失败")+"] [userName:"+userName+"] [url:"+url+"] [返回内容:"+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			}catch (Exception e) {
				e.printStackTrace();
				logger.info("[解析靠谱获取登录验证url返回数据] [异常] [userName:"+userName+"] [url:"+url+"]  [返回内容:"+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]", e);
			}
		}else{
			logger.warn("[获取靠谱登录验证url] [出错:客户端数据出错] [userName:"+userName+"] [pwd:"+pwd+"]");
		}
		return "";
	}
	
	public String loginCheck(String userName,String pwd){
		long startTime = System.currentTimeMillis();
		String args [] = pwd.split(splitStr);
		if(args != null && args.length == 5){
			String token = args[0];
			String version = args[1];
			String imei = args[2];
			String channelkey = args[3];
			String openid = args[4];
			String loginUrl = getKaoPuLoginUrl(userName, pwd);
			if(loginUrl != null && loginUrl.isEmpty() == false){
				LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();
				params.put("appid",KAOPU_APPID);
				params.put("channelkey",channelkey);
				params.put("devicetype","android");
				params.put("imei",imei);
				params.put("openid",openid);
				params.put("r","0");
				params.put("tag",KAOPU_APPKEY);
				params.put("tagid",KAOPU_SECRETKEY);
				params.put("token",token);
				params.put("sign",sign(params,secretKeys[0]));
				
				String paramString = buildParams(params);
				String url = loginUrl + "?" + paramString;
				HashMap headers = new HashMap();
				String content =  "";
				try {
					byte bytes[] = HttpUtils.webGet(new java.net.URL(url), headers, 5000, 10000);
					String encoding = (String)headers.get(HttpUtils.ENCODING);
					Integer code = (Integer)headers.get(HttpUtils.Response_Code);
					String message = (String)headers.get(HttpUtils.Response_Message);
					content = new String(bytes,encoding);
					logger.info("[靠谱登录验证] [成功] [userName:"+userName+"] ["+url+"] [code:"+code+"] ["+message+"] ["+content+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
				} catch (Exception e) {
					e.printStackTrace();
					logger.info("[靠谱登录验证] [失败] [userName:"+userName+"] ["+url+"] ["+content+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]",e);
					return null;
				}
				JacksonManager jm = JacksonManager.getInstance();
				try {
					Map map =(Map)jm.jsonDecodeObject(content);
					int code = (Integer)map.get("code");
					/**
					 *  0 处理失败
					 *	201 Token 失效(过期)
					 *	202 Token 验证失败
					 */
					String data = (String)map.get("data");
					logger.info("[解析靠谱登录验证返回数据] [结果:"+(code==1?"验证合法":"验证失败")+"] [code:"+code+"] [userName:"+userName+"] [url:"+url+"] [返回内容:"+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
					if(code == 1){
						return token;
					}
				}catch (Exception e) {
					e.printStackTrace();
					logger.info("[解析靠谱登录验证返回数据] [异常] [userName:"+userName+"] [url:"+url+"]  [返回内容:"+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]", e);
				}
			}
		}else{
			logger.warn("[靠谱登录验证] [出错:客户端数据出错] [userName:"+userName+"] [pwd:"+pwd+"]");
		}
		return null;
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
}
