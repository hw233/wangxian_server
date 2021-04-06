package com.fy.gamegateway.thirdpartner.mumayi;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.fy.gamegateway.mieshi.server.MieshiGatewaySubSystem;
import com.fy.gamegateway.tools.JacksonManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.text.StringUtil;



public class MumayiBossClientService {

	static Logger logger = Logger.getLogger(MumayiBossClientService.class);
	private String appkey = "81a2e83029d33495JkqMve99116IcR0Vjyqh0GpSqAWGEt29nsLyQmcDKxitM0c";



	private static MumayiBossClientService self;
	public static MumayiBossClientService getInstance(){
		if(self != null){
			return self;
		}
		self = new MumayiBossClientService();
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

	public MumayiLoginResult login(String username,String password){
		try
		{
			long startTime = System.currentTimeMillis();


			if(username.startsWith("MUMAYIUSER_")) {
				username = username.split("_")[1];
			}

			String url = "http://pay.mumayi.com/user/index/validation";

			LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();

			params.put("uid",username);
			params.put("token",password);

			HashMap headers = new HashMap();

			String content =  "";
			content = buildParams(params);
			if(logger.isDebugEnabled())
			{
				logger.debug("[调用登陆接口] [拼装参数] ["+content+"] [appkey:"+appkey+"]");
			}

			try {
				byte bytes[] = HttpUtils.webPost(new java.net.URL(url), content.getBytes(), headers, 5000, 10000);

				String encoding = (String)headers.get(HttpUtils.ENCODING);
				Integer code = (Integer)headers.get(HttpUtils.Response_Code);
				String message = (String)headers.get(HttpUtils.Response_Message);
				content = new String(bytes,encoding);
				if(logger.isInfoEnabled())
					logger.info("[调用登陆接口] [成功] ["+url+"] [code:"+code+"] ["+message+"] ["+content+"] [用户名:"+username+"] [密码:"+password+"] [appkey:"+appkey+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			} catch (Exception e) {
				logger.error("[调用登陆接口] [失败] ["+url+"] [code:--] [--] [sign:"+params.get("verifystring")+"] ["+content+"] [用户名:"+username+"] [密码:"+password+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
				MumayiLoginResult r = new MumayiLoginResult();
				r.code = "404";
				r.message = "调用木蚂蚁SDK登陆验证接口出现异常";
				r.message = "用户名或者密码错误";
				return r;
			}

			//解析返回结果
			MumayiLoginResult r = new MumayiLoginResult();
			if("success".equalsIgnoreCase(content))
			{
				r.code = "0";
				r.uid = username;
			}
			else
			{
				r.code = "-1";
			}


			/*			boolean isSucc = false;
				if(!StringUtils.isEmpty(r.clientSecret))
				{
					if(r.clientSecret.equalsIgnoreCase(StringUtil.hash(r.uid)))
				}*/

			if(logger.isInfoEnabled())
			{
				logger.info("[木蚂蚁登陆] ["+(StringUtils.isEmpty(r.uid) ? "失败" : "成功")+"] [字符集:"+(String)headers.get(HttpUtils.ENCODING)+"] [code:"+(Integer)headers.get(HttpUtils.Response_Code)+"] [响应信息:"+(String)headers.get(HttpUtils.Response_Message)+"] [content:"+content+"] [用户名:"+username+"] [密码:"+password+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
			}
			return r;
		}
		catch(Exception e)
		{
			MumayiLoginResult r = new MumayiLoginResult();
			r.code = "500";
			r.message = "读取木蚂蚁登陆接口内容出现异常";
			r.message = "未知错误";

			logger.error("[木蚂蚁登陆] [失败:出现未知异常]",e);
			return r;
		}
	}


}

