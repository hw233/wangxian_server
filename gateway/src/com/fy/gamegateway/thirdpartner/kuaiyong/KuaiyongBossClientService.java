package com.fy.gamegateway.thirdpartner.kuaiyong;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.fy.gamegateway.tools.JacksonManager;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.text.StringUtil;



public class KuaiyongBossClientService {
	
	static Logger logger = Logger.getLogger(KuaiyongBossClientService.class);
	private String appkey = "4fde25bde9f770728bce79ba9331d0f9";
	
	private static Map<String,String> errinfoMap = new ConcurrentHashMap<String, String>();
	
	static
	{
		errinfoMap.put("1","数错误");
		errinfoMap.put("2","tokenKey 无效");
		errinfoMap.put("3","服务器忙");
		errinfoMap.put("4","应用无效");
		errinfoMap.put("5","sign 无效");
		errinfoMap.put("6","用户不存在");
		errinfoMap.put("100","未知错误");
	}
	
	
	private static KuaiyongBossClientService self;
	public static KuaiyongBossClientService getInstance(){
		if(self != null){
			return self;
		}
		self = new KuaiyongBossClientService();
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
	
	public KuaiyongLoginResult login(String username,String password){
		try
		{
			long startTime = System.currentTimeMillis();
			
			
//			if(username.startsWith("KUAIYONGSDKUSER_")) {
//				username = username.split("_")[1];
//			}
//			

			String url = "http://f_signin.bppstore.com/loginCheck.php";

			LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();

			params.put("tokenKey",password);
			params.put("sign",StringUtil.hash(appkey+password));

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
				KuaiyongLoginResult r = new KuaiyongLoginResult();
				r.code = "404";
				r.message = "调用快用SDK登陆验证接口出现异常";
				r.message = "用户名或者密码错误";
				return r;
			}

			//解析返回结果
			JacksonManager jm = JacksonManager.getInstance();
			KuaiyongLoginResult r = new KuaiyongLoginResult();
			try {
				Map map =(Map)jm.jsonDecodeObject(content);
				if(map.get("code") != null)
				{
					r.code = ""+((Integer)map.get("code")).intValue();
					r.message = (String)map.get("msg");
					
					if(r.code.trim().equals("0"))
					{
						Map dataMap = (Map)map.get("data");
						
						r.uid = (String)dataMap.get("guid");
						r.username = (String)dataMap.get("username");
						try{
							r.refer_type = Integer.valueOf((String)dataMap.get("refer_type")).intValue();
							r.refer_name = (String)dataMap.get("refer_name");
						}catch(Exception e){
							//e.printStackTrace();
						}
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
					logger.info("[快用登陆] ["+(StringUtils.isEmpty(r.uid) ? "失败" : "成功")+"] [字符集:"+(String)headers.get(HttpUtils.ENCODING)+"] [code:"+(Integer)headers.get(HttpUtils.Response_Code)+"] [响应信息:"+(String)headers.get(HttpUtils.Response_Message)+"] [content:"+content+"] [用户名:"+username+"] [密码:"+password+"] ["+r+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
				}
			} catch (Exception e) {
				logger.error("[读取快用登陆接口JSON内容] [失败] [解析响应内容信息] [失败] [字符集:"+(String)headers.get(HttpUtils.ENCODING)+"] [code:"+(Integer)headers.get(HttpUtils.Response_Code)+"] [响应信息:"+(String)headers.get(HttpUtils.Response_Message)+"] [content:"+content+"] [用户名:"+username+"] [密码:"+password+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
				r.code = "500";
				r.message = "读取快用登陆接口内容出现异常";
				r.message = "用戶名或密码錯誤";
				return r;
			}
			return r;
		}
		catch(Exception e)
		{
			KuaiyongLoginResult r = new KuaiyongLoginResult();
			r.code = "500";
			r.message = "读取快用登陆接口内容出现异常";
			r.message = "未知错误";
			
			logger.error("[快用登陆] [失败:出现未知异常]",e);
			return r;
		}

	}
	
	
	

}
