package com.fy.gamegateway.thirdpartner.xysdk;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.fy.gamegateway.thirdpartner.uc.RegisterResult;
import com.fy.gamegateway.tools.JacksonManager;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.text.StringUtil;



public class XYBossClientService {
	
	static Logger logger = Logger.getLogger(XYBossClientService.class);
	public static String APPID = "100000307";
	public static String APP_KEY = "sTnFzuQb6iP6W7JjGMsAh7JBwhkwe5WS";

	
	
	private static XYBossClientService self;
	public static XYBossClientService getInstance(){
		if(self != null){
			return self;
		}
		self = new XYBossClientService();
		return self;
	}

	
	
	public XYLoginResult login( String uid, String password){
		long startTime = System.currentTimeMillis();
		
		String url = "http://passport.xyzs.com/checkLogin.php";
		if(uid.startsWith("XYSDKUSER_")) {
			uid = uid.split("_")[1];
		}
		HashMap<String,String> params = new LinkedHashMap<String,String>();

		params.put("uid",uid);
		params.put("appid",APPID);
		params.put("token",password);

		String queryString = "";
		queryString = buildParams(params);
		
		Map headers = new HashMap();

		String content =  "";
		try {
			byte bytes[] = HttpUtils.webPost(new java.net.URL(url), queryString.getBytes(), headers, 10000, 10000);
			
			String encoding = (String)headers.get(HttpUtils.ENCODING);
			Integer code = (Integer)headers.get(HttpUtils.Response_Code);
			String message = (String)headers.get(HttpUtils.Response_Message);
			
			content = new String(bytes,encoding);
			//解析json
			
		} catch (Exception e) {
			logger.error("[调用登录接口] [失败] [调用验证接口出现异常] ["+url+"] [code:--] [--] ["+content+"] [queryString:"+queryString+"] [cpid:"+APPID+"] [username:"+uid+"] [password:"+password+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
			
			XYLoginResult r = new XYLoginResult();
			r.status = "500";
			r.message = "调用验证接口出现异常";
			r.uid = "";
			r.password = "";
			r.accessToken = "";
			r.sign = "";
			r.time = System.currentTimeMillis();
			
			return r;
		}
		
		JacksonManager jm = JacksonManager.getInstance();
		try {
			Map map =(Map)jm.jsonDecodeObject(content);
			XYLoginResult r = new XYLoginResult();
			r.status = (Integer)map.get("ret")+"";
			r.message = (String)map.get("error");
			
			if("0".equals(r.status))
			{
				r.password = password;
				r.uid = uid;
				if(logger.isInfoEnabled())
					logger.info("[调用登录接口] [成功] [ok] ["+url+"] [code:"+r.status+"] [msg:"+r.message+"] ["+content+"] [queryString:"+queryString+"] [cpid:"+APPID+"] [username:"+uid+"] [password:"+password+"] [uid:"+r.uid+"] [返回内容:"+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			}
			else
			{
				logger.warn("[调用登录接口] [失败] [验证失败] ["+url+"] [code:"+r.status+"] [msg:"+r.message+"] ["+content+"] [queryString:"+queryString+"] [cpid:"+APPID+"] [username:"+uid+"] [password:"+password+"] [uid:"+r.uid+"] [返回内容:"+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
				r.message = "验证失败";
			}
			
			return r;
		}catch (Exception e) {
			XYLoginResult r = new XYLoginResult();
			r.status = "500";
			r.message = "解析JSON串出现异常";
			r.uid = "";
			r.password = "";
			r.accessToken = "";
			r.sign = "";
			r.time = System.currentTimeMillis();
			logger.error("[调用登录接口] [失败] [解析json失败] ["+url+"] [code:"+r.status+"] [msg:"+r.message+"] ["+content+"] [queryString:"+queryString+"] [cpid:"+APPID+"] [username:"+uid+"] [password:"+password+"] [uid:"+r.uid+"] [返回内容:"+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			r.message = "验证失败";
			return r;

		}

	}
	

	

	
	public static String buildParams(HashMap<String,String> params){
		String keys[] = params.keySet().toArray(new String[0]);
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < keys.length ; i++){
			String v = params.get(keys[i]);
			sb.append(keys[i]+"="+v);
		}

		
		sb = new StringBuffer();
		for(int i = 0 ; i < keys.length ; i++){
			String v = params.get(keys[i]);
			sb.append(keys[i]+"="+v+"&");
		}
		
		return sb.substring(0,sb.toString().length()-1);
	}

}
