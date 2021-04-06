package com.fy.gamegateway.thirdpartner.kunlun;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.fy.gamegateway.mieshi.server.MieshiGatewaySubSystem;
import com.fy.gamegateway.tools.JacksonManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.sun.mail.handlers.message_rfc822;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.text.StringUtil;



public class KunLunBossClientService {
	
	static Logger logger = Logger.getLogger(MieshiGatewaySubSystem.class);
	
	public static  String TAIWAN_REG_URL = "http://login.kunlun.tw/?act=user.phoneregist&lang=tw";
	public static  String MALAI_REG_URL = "http://login.koramgame.com.my/?act=user.phoneregist&lang=zh";
	public static  String KOREA_REG_URL = "http://login.kunlun.tw/?act=user.phoneregist&lang=kr";
	
	public static  String TAIWAN_LOGIN_URL = "http://login.kunlun.tw/?act=user.phonelogin&lang=tw"; 
	public static  String TAIWAN_VERIFY_SESSION_URL = "http://login.kimi.com.tw/verifyklsso.php?klsso=";
	public static  String TAIWAN_SHIWAN_URL = "http://login.kimi.com.tw/?act=user.gettryplayaccount&user_name=";
	public static  String MALAI_LOGIN_URL = "http://login.koramgame.com.my/?act=user.phonelogin&lang=zh";
	public static  String KOREA_VERIFY_SESSION_URL = "http://login.kunlun.tw/?act=user.phonelogin&lang=kr";
	public static  String KOREA_LOGIN_URL = "http://login.kr.koramgame.com/verifyklsso.php?klsso=";
	public static  String KOREA_SHIWAN_URL = "http://kr.login.koramgame.com/?act=user.gettryplayaccount&user_name=";
	public static  String CHANGE_PASS_URL = "http://login.koramgame.com/?act=user.phonechangepwd&v=2.0";
	
	
	private static KunLunBossClientService self;
	public static KunLunBossClientService getInstance(){
		if(self != null){
			return self;
		}
		self = new KunLunBossClientService();
		return self;
	}
	
	public RegisterResultNew register(String platform, String username,String mobile,String password){
		long startTime = System.currentTimeMillis();
		
		String registerUrl  = TAIWAN_REG_URL;
		
		LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();

		params.put("user_name",username);
		params.put("password", StringUtil.hash(password));
		
		HashMap headers = new HashMap();

		String content =  "";
		content = buildParams(params);
		try {
			byte bytes[] = HttpUtils.webPost(new java.net.URL(registerUrl), content.getBytes(), headers, 5000, 10000);
			
			String encoding = (String)headers.get(HttpUtils.ENCODING);
			Integer code = (Integer)headers.get(HttpUtils.Response_Code);
			String message = (String)headers.get(HttpUtils.Response_Message);
			content = new String(bytes,encoding);
			logger.info("[调用注册接口] ["+platform+"] [成功] ["+registerUrl+"] [code:"+code+"] ["+message+"] ["+content+"] [用户名:"+username+"] [密码:"+password+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			logger.info("[调用注册接口] ["+platform+"] [失败] ["+registerUrl+"] [code:--] [--] [sign:"+params.get("verifystring")+"] ["+content+"] [用户名:"+username+"] [密码:"+password+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
			RegisterResultNew r = new RegisterResultNew();
			r.code = 404;
			r.message = "调用昆仑注册接口出现异常";
			r.message = "用戶名或密碼錯誤";
			return r;
		}
		
		//解析返回结果
		JacksonManager jm = JacksonManager.getInstance();
		RegisterResultNew r = new RegisterResultNew();
		try {
			Map map =(Map)jm.jsonDecodeObject(content);
			r.code = ((Number)map.get("retcode")).intValue();
			r.message = (String)map.get("retmsg");
			
			if(logger.isInfoEnabled()){
				logger.info("[读取昆仑注册接口JSON内容] [成功] [解析响应内容信息] [成功] [字符集:"+(String)headers.get(HttpUtils.ENCODING)+"] [code:"+(Integer)headers.get(HttpUtils.Response_Code)+"] [响应信息:"+(String)headers.get(HttpUtils.Response_Message)+"] [content:"+content+"] [用户名:"+username+"] [密码:"+password+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
			}
		} catch (Exception e) {
			logger.error("[读取昆仑注册接口JSON内容] [失败] [解析响应内容信息] [失败] [字符集:"+(String)headers.get(HttpUtils.ENCODING)+"] [code:"+(Integer)headers.get(HttpUtils.Response_Code)+"] [响应信息:"+(String)headers.get(HttpUtils.Response_Message)+"] [content:"+content+"] [用户名:"+username+"] [密码:"+password+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
			r.code = 500;
			r.message = "读取昆仑注册接口内容出现异常";
			r.message = "用戶名或密碼錯誤";
			return r;
		}
		return r;
	}
	
	
	public RegisterResultNew register(String platform, String username,String mobile,String password,String channel){
		long startTime = System.currentTimeMillis();
		
//		String registerUrl  = "http://login.kunlun.tw/?act=user.phoneregist&lang=tw";
		String registerUrl  = TAIWAN_REG_URL;
		boolean isSimpleChinese = false;
		boolean isKorea = false;
		
		if(channel.toLowerCase().contains("malai"))
		{
			isSimpleChinese = true;
			registerUrl = MALAI_REG_URL;
		}
		
		if(channel.toLowerCase().contains("korea"))
		{
			isKorea = true;
			//registerUrl = "http://login.koramgame.com/?act=user.phoneregist&lang=kr";
			registerUrl = KOREA_REG_URL;
		}
		
		
		LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();

		params.put("user_name",username);
		params.put("password", StringUtil.hash(password));
		
		HashMap headers = new HashMap();

		String content =  "";
		content = buildParams(params);
		try {
			byte bytes[] = HttpUtils.webPost(new java.net.URL(registerUrl), content.getBytes(), headers, 5000, 10000);
			
			String encoding = (String)headers.get(HttpUtils.ENCODING);
			Integer code = (Integer)headers.get(HttpUtils.Response_Code);
			String message = (String)headers.get(HttpUtils.Response_Message);
			content = new String(bytes,encoding);
			if(logger.isInfoEnabled())
				logger.info("[调用注册接口] ["+platform+"] [成功] ["+registerUrl+"] [code:"+code+"] ["+message+"] ["+content+"] [用户名:"+username+"] [密码:"+password+"] [渠道:"+channel+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			if(logger.isInfoEnabled())
				logger.info("[调用注册接口] ["+platform+"] [失败] ["+registerUrl+"] [code:--] [--] [sign:"+params.get("verifystring")+"] ["+content+"] [用户名:"+username+"] [密码:"+password+"] [渠道:"+channel+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
			RegisterResultNew r = new RegisterResultNew();
			r.code = 404;
			r.message = "调用昆仑注册接口出现异常";
			r.message = "用戶名或密碼錯誤";
			if(isSimpleChinese)
			{
				r.message = "用户名或密码错误";
			}
			
			if(isKorea)
			{
				r.message = "계정 또는 비밀번호 오류";
			}
			return r;
		}
		
		//解析返回结果
		JacksonManager jm = JacksonManager.getInstance();
		RegisterResultNew r = new RegisterResultNew();
		try {
			Map map =(Map)jm.jsonDecodeObject(content);
			r.code = ((Number)map.get("retcode")).intValue();
			r.message = (String)map.get("retmsg");
			
			if(logger.isInfoEnabled()){
				logger.info("[读取昆仑注册接口JSON内容] [成功] [解析响应内容信息] [成功] [字符集:"+(String)headers.get(HttpUtils.ENCODING)+"] [code:"+(Integer)headers.get(HttpUtils.Response_Code)+"] [响应信息:"+(String)headers.get(HttpUtils.Response_Message)+"] [content:"+content+"] [用户名:"+username+"] [密码:"+password+"] [渠道:"+channel+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
			}
		} catch (Exception e) {
			logger.error("[读取昆仑注册接口JSON内容] [失败] [解析响应内容信息] [失败] [字符集:"+(String)headers.get(HttpUtils.ENCODING)+"] [code:"+(Integer)headers.get(HttpUtils.Response_Code)+"] [响应信息:"+(String)headers.get(HttpUtils.Response_Message)+"] [content:"+content+"] [用户名:"+username+"] [密码:"+password+"] [渠道:"+channel+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
			r.code = 500;
			r.message = "读取昆仑注册接口内容出现异常";
			r.message = "用戶名或密碼錯誤";
			if(isSimpleChinese)
			{
				r.message = "用户名或密码错误";
			}
			if(isKorea)
			{
				r.message = "계정 또는 비밀번호 오류";
			}
			return r;
		}
		return r;
	}
	
	
	public String buildParams(Map<String,String> params)
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
	
	public KunLunLoginResult login(String username,String password){
		 long startTime = System.currentTimeMillis();
		 
		 String url = TAIWAN_LOGIN_URL;
		 
	/*	 username:                 例如：maohangjun@gmail.com
userpass:md5(password);   例如：md5("123456");
"username=maohangjun@gmail.com&userpass=e10adc3949ba59abbe56e057f20f883e" "http://login.kunlun.tw/?act=user.phonelogin&lang=tw"
返回结果：JSON格式
{"retcode":0,"retmsg":"success","data":{"uid":"2","ipv4":"60.28.208.203","indulge":0,"uname":"maohangjun@gmail.com","KL_SSO":"m5Kqk4Dmag6_mVm3Ij2IIYs7bG3KKvlEbvBz5RSixDCXsBvSIfOKuMU8CdLYOIJK2BSnKqSDa39yIjRk.uM3qCP_ReQ-4dkOoxr8PhRsj1Wan5qSo.274MfqYpTIhLZhapZNE9.s3bzX-72mlh32nF5KJ-k_iY1.H2iJ-tx7Rzo0","KL_PERSON":"KhdY9p6l6A5Th5nYg9HUMDOBQr.57OpPqQIypGGEJyMbhAJWY2SKJIiiObcHeVwusSRivoGnaSG5NSkN4Et7cTFv-X3nikx4me.Lr-4eMuNC4A8.A9ILDOMd8j1pW9.cNJFgwTXxYF4j2U9jJbCV6LrGTGE3Lg97pf6TkdHH4G6ZpFyW1yKZ9V8qjzjl9NJstZp7NUFyTTChs--aiXL1N_aoS9vw4trBq.8b1XpttDL5UrS_pu7X2WjpR9tV1DOGE62S22nOc4oI..t1Dmt4Ag00"}}

		*/
		 
		LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();

		params.put("username",username);
		params.put("userpass", StringUtil.hash(password));
		
		HashMap headers = new HashMap();
		String content =  "";
		content = buildParams(params);
		try {
			byte bytes[] = HttpUtils.webPost(new java.net.URL(url), content.getBytes(), headers, 5000, 10000);
			
			String encoding = (String)headers.get(HttpUtils.ENCODING);
			Integer code = (Integer)headers.get(HttpUtils.Response_Code);
			String message = (String)headers.get(HttpUtils.Response_Message);
			
			content = new String(bytes,encoding);
			logger.info("[调用登陆接口] [昆仑] [成功] ["+url+"] [code:"+code+"] ["+message+"] ["+content+"] [用户名:"+username+"] [密码:"+password+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			
			logger.info("[调用登陆接口] [昆仑] [失败] ["+url+"] [code:--] [--] ["+content+"] [用户名:"+username+"] [密码:"+password+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
			
			KunLunLoginResult r = new KunLunLoginResult();
			r.code = 404;
			r.message = "调用昆仑登陆接口出现异常";
			r.message = "用戶名或密碼錯誤";
			return r;
		}
		
		JacksonManager jm = JacksonManager.getInstance();
		KunLunLoginResult r = new KunLunLoginResult();
		try {
			Map map =(Map)jm.jsonDecodeObject(content);
			r.code = ((Number)(map.get("retcode"))).intValue();
			r.message = (String)map.get("retmsg");
			Map data = (Map)map.get("data");
			r.uid = Long.parseLong((String)data.get("uid"));
			r.sso = (String)data.get("KL_SSO");
			if(logger.isInfoEnabled()){
				logger.info("[读取昆仑登陆接口JSON内容] [成功] [字符集:"+(String)headers.get(HttpUtils.ENCODING)+"] [code:"+(Integer)headers.get(HttpUtils.Response_Code)+"] [响应信息:"+(String)headers.get(HttpUtils.Response_Message)+"] [content:"+content+"] [用户名:"+username+"] [密码:"+password+"] [uid:"+r.uid+"] [sso:"+r.sso+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
			}
		} catch (Exception e) {
			logger.error("[读取昆仑登陆接口JSON内容] [失败] [解析响应内容信息] [失败] [字符集:"+(String)headers.get(HttpUtils.ENCODING)+"] [code:"+(Integer)headers.get(HttpUtils.Response_Code)+"] [响应信息:"+(String)headers.get(HttpUtils.Response_Message)+"] [content:"+content+"] [用户名:"+username+"] [密码:"+password+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
			r.code = 500;
			r.message = "读取昆仑登陆接口JSON内容出现异常";
			r.message = "用戶名或密碼錯誤";
			return r;
		}
		return r;

	}
	
	
	public KunLunLoginResult login(String username,String password,String channel){
		 long startTime = System.currentTimeMillis();
		 
		 String url = TAIWAN_LOGIN_URL;
		 
	/*	 username:                 例如：maohangjun@gmail.com
userpass:md5(password);   例如：md5("123456");
"username=maohangjun@gmail.com&userpass=e10adc3949ba59abbe56e057f20f883e" "http://login.kunlun.tw/?act=user.phonelogin&lang=tw"
返回结果：JSON格式
{"retcode":0,"retmsg":"success","data":{"uid":"2","ipv4":"60.28.208.203","indulge":0,"uname":"maohangjun@gmail.com","KL_SSO":"m5Kqk4Dmag6_mVm3Ij2IIYs7bG3KKvlEbvBz5RSixDCXsBvSIfOKuMU8CdLYOIJK2BSnKqSDa39yIjRk.uM3qCP_ReQ-4dkOoxr8PhRsj1Wan5qSo.274MfqYpTIhLZhapZNE9.s3bzX-72mlh32nF5KJ-k_iY1.H2iJ-tx7Rzo0","KL_PERSON":"KhdY9p6l6A5Th5nYg9HUMDOBQr.57OpPqQIypGGEJyMbhAJWY2SKJIiiObcHeVwusSRivoGnaSG5NSkN4Et7cTFv-X3nikx4me.Lr-4eMuNC4A8.A9ILDOMd8j1pW9.cNJFgwTXxYF4j2U9jJbCV6LrGTGE3Lg97pf6TkdHH4G6ZpFyW1yKZ9V8qjzjl9NJstZp7NUFyTTChs--aiXL1N_aoS9vw4trBq.8b1XpttDL5UrS_pu7X2WjpR9tV1DOGE62S22nOc4oI..t1Dmt4Ag00"}}

		*/
		 boolean isSimpleChinese = false;
		 boolean isKorea = false;
		if(channel.toLowerCase().contains("malai"))
		{
			isSimpleChinese = true;
			url = MALAI_LOGIN_URL;
		}
		
		if(channel.toLowerCase().contains("korea"))
		{
			isKorea = true;
			//url = "http://login.koramgame.com/?act=user.phonelogin&lang=kr";
			url = KOREA_LOGIN_URL;
		}
		
		
		LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();

		params.put("username",username);
		params.put("userpass", StringUtil.hash(password));
		
		HashMap headers = new HashMap();
		String content =  "";
		content = buildParams(params);
		try {
			byte bytes[] = HttpUtils.webPost(new java.net.URL(url), content.getBytes(), headers, 5000, 10000);
			
			String encoding = (String)headers.get(HttpUtils.ENCODING);
			Integer code = (Integer)headers.get(HttpUtils.Response_Code);
			String message = (String)headers.get(HttpUtils.Response_Message);
			
			content = new String(bytes,encoding);
			if(logger.isInfoEnabled())
				logger.info("[调用登陆接口] [昆仑] [成功] ["+url+"] [code:"+code+"] ["+message+"] ["+content+"] [用户名:"+username+"] [密码:"+password+"] [渠道:"+channel+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			if(logger.isInfoEnabled())
				logger.info("[调用登陆接口] [昆仑] [失败] ["+url+"] [code:--] [--] ["+content+"] [用户名:"+username+"] [密码:"+password+"] [渠道:"+channel+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
			
			KunLunLoginResult r = new KunLunLoginResult();
			r.code = 404;
			r.message = "调用昆仑登陆接口出现异常";
			r.message = "用戶名或密碼錯誤";
			if(isSimpleChinese)
			{
				r.message = "用户名或密码错误";
			}
			
			if(isKorea)
			{
				r.message = "계정 또는 비밀번호 오류";
			}
			return r;
		}
		
		JacksonManager jm = JacksonManager.getInstance();
		KunLunLoginResult r = new KunLunLoginResult();
		try {
			Map map =(Map)jm.jsonDecodeObject(content);
			r.code = ((Number)(map.get("retcode"))).intValue();
			r.message = (String)map.get("retmsg");
			Map data = (Map)map.get("data");
			r.uid = Long.parseLong((String)data.get("uid"));
			r.sso = (String)data.get("KL_SSO");
			try
			{
				if(isKorea)
				{
					long time = System.currentTimeMillis()/1000;
//					String shiwanUrl = "http://login.koramgame.com/?act=user.gettryplayaccount"+"&user_name="+username+"&time="+time+"&sign="+StringUtil.hash(username+"c5c4322e89085"+time);
					String shiwanUrl = KOREA_SHIWAN_URL+username+"&time="+time+"&sign="+StringUtil.hash(username+"c5c4322e89085"+time);
					
					byte bytes[] = HttpUtils.webGet(new java.net.URL(shiwanUrl),  headers, 5000, 10000);
					
					String encoding = (String)headers.get(HttpUtils.ENCODING);
					Integer code = (Integer)headers.get(HttpUtils.Response_Code);
					String message = (String)headers.get(HttpUtils.Response_Message);
					
					String returnContent = new String(bytes,encoding);
					
					JacksonManager jmm = JacksonManager.getInstance();
					Map shiyongmap =(Map)jmm.jsonDecodeObject(returnContent);
					Map shiyongdata = (Map)shiyongmap.get("data");
					if(((Number)(shiyongmap.get("retcode"))).intValue() == 0)
					{
						r.username = (String)shiyongdata.get("uname");
						if(logger.isInfoEnabled())
							logger.info("[昆仑试玩接口] [成功] [需要转换用户名] ["+username+"] ["+r.username+"] ["+r.uid+"] ["+r.code+"] ["+r.message+"] ["+password+"] ["+channel+"]");
					}
					else
					{
						if(logger.isInfoEnabled())
							logger.info("[昆仑试玩接口] [成功] [不需要转换用户名] ["+username+"] ["+r.username+"] ["+r.uid+"] ["+r.code+"] ["+r.message+"] ["+password+"] ["+channel+"]");
					}
				}
				else
				{
					if(logger.isInfoEnabled())
					{
						logger.info("[昆仑试玩接口] [不是韩国用户] [不需要转换用户名] ["+username+"] ["+r.username+"] ["+r.uid+"] ["+r.code+"] ["+r.message+"] ["+password+"] ["+channel+"]");
					}
				}
			}
			catch(Exception e)
			{
				logger.error("[昆仑试玩接口] [错误] [出现未知异常] [不做转换] ["+username+"] ["+password+"] ["+channel+"]",e);
			}
			
			if(logger.isInfoEnabled()){
				logger.info("[读取昆仑登陆接口JSON内容] [成功] [字符集:"+(String)headers.get(HttpUtils.ENCODING)+"] [code:"+(Integer)headers.get(HttpUtils.Response_Code)+"] [响应信息:"+(String)headers.get(HttpUtils.Response_Message)+"] [content:"+content+"] [用户名:"+username+"] [密码:"+password+"] [uid:"+r.uid+"] [sso:"+r.sso+"] [渠道:"+channel+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
			}
			
			
			
		} catch (Exception e) {
			if(logger.isInfoEnabled())
				logger.error("[读取昆仑登陆接口JSON内容] [失败] [解析响应内容信息] [失败] [字符集:"+(String)headers.get(HttpUtils.ENCODING)+"] [code:"+(Integer)headers.get(HttpUtils.Response_Code)+"] [响应信息:"+(String)headers.get(HttpUtils.Response_Message)+"] [content:"+content+"] [用户名:"+username+"] [密码:"+password+"] [渠道:"+channel+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
			r.code = 500;
			r.message = "读取昆仑登陆接口JSON内容出现异常";
			r.message = "用戶名或密碼錯誤";
			if(isSimpleChinese)
			{
				r.message = "用户名或密码错误";
			}
			
			if(isKorea)
			{
				r.message = "계정 또는 비밀번호 오류";
			}
			return r;
		}
		return r;

	}
	
	
	
	
	
	public KunLunLoginResult validate(String username,String password,String channel){
		 long startTime = System.currentTimeMillis();
		 
		 String url = "";
		 boolean isTaiwan = channel.contains("kunlunsdk");
		 if(isTaiwan)
		 {
			 url = TAIWAN_VERIFY_SESSION_URL;
		 }
		 else
		 {
			 url = KOREA_LOGIN_URL;
		 }
		 
		 
	/*	 username:                 例如：maohangjun@gmail.com
userpass:md5(password);   例如：md5("123456");
"username=maohangjun@gmail.com&userpass=e10adc3949ba59abbe56e057f20f883e" "http://login.kunlun.tw/?act=user.phonelogin&lang=tw"
返回结果：JSON格式
{"retcode":0,"retmsg":"success","data":{"uid":"2","ipv4":"60.28.208.203","indulge":0,"uname":"maohangjun@gmail.com","KL_SSO":"m5Kqk4Dmag6_mVm3Ij2IIYs7bG3KKvlEbvBz5RSixDCXsBvSIfOKuMU8CdLYOIJK2BSnKqSDa39yIjRk.uM3qCP_ReQ-4dkOoxr8PhRsj1Wan5qSo.274MfqYpTIhLZhapZNE9.s3bzX-72mlh32nF5KJ-k_iY1.H2iJ-tx7Rzo0","KL_PERSON":"KhdY9p6l6A5Th5nYg9HUMDOBQr.57OpPqQIypGGEJyMbhAJWY2SKJIiiObcHeVwusSRivoGnaSG5NSkN4Et7cTFv-X3nikx4me.Lr-4eMuNC4A8.A9ILDOMd8j1pW9.cNJFgwTXxYF4j2U9jJbCV6LrGTGE3Lg97pf6TkdHH4G6ZpFyW1yKZ9V8qjzjl9NJstZp7NUFyTTChs--aiXL1N_aoS9vw4trBq.8b1XpttDL5UrS_pu7X2WjpR9tV1DOGE62S22nOc4oI..t1Dmt4Ag00"}}

		*/
		 HashMap headers = new HashMap();
		String content =  "";
		
		url = url += password;
		try {
			byte bytes[] = HttpUtils.webPost(new java.net.URL(url), content.getBytes(), headers, 5000, 10000);
			
			String encoding = (String)headers.get(HttpUtils.ENCODING);
			Integer code = (Integer)headers.get(HttpUtils.Response_Code);
			String message = (String)headers.get(HttpUtils.Response_Message);
			
			content = new String(bytes,encoding);
			if(logger.isInfoEnabled())
				logger.info("[调用登陆接口] [昆仑] [成功] ["+url+"] [code:"+code+"] ["+message+"] ["+content+"] [用户名:"+username+"] [密码:"+password+"] [渠道:"+channel+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			if(logger.isInfoEnabled())
				logger.info("[调用登陆接口] [昆仑] [失败] ["+url+"] [code:--] [--] ["+content+"] [用户名:"+username+"] [密码:"+password+"] [渠道:"+channel+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
			
			KunLunLoginResult r = new KunLunLoginResult();
			r.code = 404;
			r.message = getMessage(channel, r.code);
			return r;
		}
		
		JacksonManager jm = JacksonManager.getInstance();
		KunLunLoginResult r = new KunLunLoginResult();
		try {
			Map map =(Map)jm.jsonDecodeObject(content);
			r.code = ((Number)(map.get("retcode"))).intValue();
			r.message = (String)map.get("retmsg");
			Map data = (Map)map.get("data");
			logger.info("koreatest] [data:"+(data==null?"null":data)+"]");
			r.uid = Long.parseLong((String)data.get("uid"));
			r.sso = (String)data.get("KL_SSO");
			
			
			try
			{
				 boolean isKorea = false;
				if(channel.toLowerCase().contains("korea"))
				{
					isKorea = true;
				}
				
				if(isKorea)
				{
					long time = System.currentTimeMillis()/1000;
					String shiwanUrl = "http://login.koramgame.com/?act=user.gettryplayaccount"+"&user_name="+username+"&time="+time+"&sign="+StringUtil.hash(username+"c5c4322e89085"+time);
					headers =  new HashMap();
					byte bytes[] = HttpUtils.webGet(new java.net.URL(shiwanUrl),  headers, 5000, 10000);
					
					String encoding = (String)headers.get(HttpUtils.ENCODING);
					Integer code = (Integer)headers.get(HttpUtils.Response_Code);
					String message = (String)headers.get(HttpUtils.Response_Message);
					
					String returnContent = new String(bytes,encoding);
					
					JacksonManager jmm = JacksonManager.getInstance();
					Map shiyongmap =(Map)jmm.jsonDecodeObject(returnContent);
					Map shiyongdata = (Map)shiyongmap.get("data");
					if(((Number)(shiyongmap.get("retcode"))).intValue() == 0)
					{
						r.username = (String)shiyongdata.get("uname");
						if(logger.isInfoEnabled())
							logger.info("[昆仑试玩接口] [成功] [需要转换用户名] ["+username+"] ["+r.username+"] ["+r.uid+"] ["+r.code+"] ["+r.message+"] ["+password+"] ["+channel+"]");
					}
					else
					{
						if(logger.isInfoEnabled())
							logger.info("[昆仑试玩接口] [成功] [不需要转换用户名] ["+username+"] ["+r.username+"] ["+r.uid+"] ["+r.code+"] ["+r.message+"] ["+password+"] ["+channel+"]");
					}
				}
				else
				{
					long time = System.currentTimeMillis()/1000;
					String shiwanUrl = TAIWAN_SHIWAN_URL+username+"&time="+time+"&sign="+StringUtil.hash(username+"c5c4322e89085"+time);
					headers =  new HashMap();
					byte bytes[] = HttpUtils.webGet(new java.net.URL(shiwanUrl),  headers, 5000, 10000);
					
					String encoding = (String)headers.get(HttpUtils.ENCODING);
					Integer code = (Integer)headers.get(HttpUtils.Response_Code);
					String message = (String)headers.get(HttpUtils.Response_Message);
					
					String returnContent = new String(bytes,encoding);
					
					JacksonManager jmm = JacksonManager.getInstance();
					Map shiyongmap =(Map)jmm.jsonDecodeObject(returnContent);
					Map shiyongdata = (Map)shiyongmap.get("data");
					if(((Number)(shiyongmap.get("retcode"))).intValue() == 0)
					{
						r.username = (String)shiyongdata.get("uname");
						if(logger.isInfoEnabled())
							logger.info("[昆仑试玩接口] [成功] [需要转换用户名] ["+username+"] ["+r.username+"] ["+r.uid+"] ["+r.code+"] ["+r.message+"] ["+password+"] ["+channel+"]");
					}
					else
					{
						if(logger.isInfoEnabled())
							logger.info("[昆仑试玩接口] [成功] [不需要转换用户名] ["+username+"] ["+r.username+"] ["+r.uid+"] ["+r.code+"] ["+r.message+"] ["+password+"] ["+channel+"]");
					}
				}
			}
			catch(Exception e)
			{
				logger.error("[昆仑试玩接口] [错误] [出现未知异常] [不做转换] ["+username+"] ["+password+"] ["+channel+"]",e);
			}
			
			
			
			if(logger.isInfoEnabled()){
				logger.info("[读取昆仑登陆接口JSON内容] [成功] [字符集:"+(String)headers.get(HttpUtils.ENCODING)+"] [code:"+(Integer)headers.get(HttpUtils.Response_Code)+"] [响应信息:"+(String)headers.get(HttpUtils.Response_Message)+"] [content:"+content+"] [用户名:"+username+"] [密码:"+password+"] [uid:"+r.uid+"] [sso:"+r.sso+"] [渠道:"+channel+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
			}
		} catch (Exception e) {
			if(logger.isInfoEnabled())
				logger.error("[读取昆仑登陆接口JSON内容] [失败] [解析响应内容信息] [失败] [字符集:"+(String)headers.get(HttpUtils.ENCODING)+"] [code:"+(Integer)headers.get(HttpUtils.Response_Code)+"] [响应信息:"+(String)headers.get(HttpUtils.Response_Message)+"] [content:"+content+"] [用户名:"+username+"] [密码:"+password+"] [渠道:"+channel+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
			r.code = 500;
			r.message = "读取昆仑登陆接口JSON内容出现异常";
			r.message = "用戶名或密碼錯誤";
			
			r.message = getMessage(channel, r.code);
			return r;
		}
		return r;

	}
	
	public String getMessage(String channel,int code)
	{
		boolean isTaiwan = 	channel.contains("kunlunsdk");
		String message = "";
		
		if(code == 404 && !isTaiwan)
		{
			message ="계정 또는 비밀번호 오류";
		}
		else
		{
			message ="用戶名或密碼錯誤";
		}
		
		if(code == 500 && !isTaiwan)
		{
			message ="계정 또는 비밀번호 오류";
		}
		else
		{
			message ="用戶名或密碼錯誤";
		}
		
		return message;
	}
	
	
	public int updatePass(String username,String oldpass,String newpass,String channel)
	{
		long startTime = System.currentTimeMillis();
		String url = CHANGE_PASS_URL;
	
		/* 
	     * $_POST['user_name'] 原用户名
	     * $_POST['password'] 旧密码 MD5值
	     * $_POST['new_password'] 新密码 MD5值*/
				 
		LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();

		params.put("user_name",username);
		params.put("password", StringUtil.hash(oldpass));
		params.put("new_password", StringUtil.hash(newpass));

				
		HashMap headers = new HashMap();
		String content =  "";
		content = buildParams(params);
		try {
			byte bytes[] = HttpUtils.webPost(new java.net.URL(url), content.getBytes(), headers, 5000, 10000);
			
			String encoding = (String)headers.get(HttpUtils.ENCODING);
			Integer code = (Integer)headers.get(HttpUtils.Response_Code);
			String message = (String)headers.get(HttpUtils.Response_Message);
			
			content = new String(bytes,encoding);
			if(logger.isInfoEnabled())
				logger.info("[调用修改密码接口] [昆仑] [成功] ["+url+"] [code:"+code+"] ["+message+"] ["+content+"] [用户名:"+username+"] [老密码:"+oldpass+"] [新密码:"+newpass+"]");
		} catch (Exception e) {
			if(logger.isInfoEnabled())
				logger.info("[调用修改密码接口] [昆仑] [失败] ["+url+"] [code:--] [--] ["+content+"] [用户名:"+username+"] [老密码:"+oldpass+"] [新密码:"+newpass+"]",e);
			return -1;
		}
		
		JacksonManager jm = JacksonManager.getInstance();
	
		try {
			Map map =(Map)jm.jsonDecodeObject(content);
			int code = ((Number)(map.get("retcode"))).intValue();
			String message = (String)map.get("retmsg");
			Map data = (Map)map.get("data");
			long uid = Long.parseLong((String)data.get("uid"));
			String sso = (String)data.get("KL_SSO");
			if(logger.isInfoEnabled()){
				logger.info("[读取昆仑修改密码接口JSON内容] [成功] [字符集:"+(String)headers.get(HttpUtils.ENCODING)+"] [code:"+(Integer)headers.get(HttpUtils.Response_Code)+"] [响应信息:"+(String)headers.get(HttpUtils.Response_Message)+"] [content:"+content+"] [用户名:"+username+"] [老密码:"+oldpass+"] [新密码:"+newpass+"] [uid:"+uid+"] [sso:"+sso+"] [渠道:"+channel+"] [costs:"+(System.currentTimeMillis()-startTime)+"]");
			}
		} catch (Exception e) {
			if(logger.isInfoEnabled())
				logger.error("[读取昆仑修改密码接口JSON内容] [失败] [解析响应内容信息] [失败] [字符集:"+(String)headers.get(HttpUtils.ENCODING)+"] [code:"+(Integer)headers.get(HttpUtils.Response_Code)+"] [响应信息:"+(String)headers.get(HttpUtils.Response_Message)+"] [content:"+content+"] [用户名:"+username+"] [老密码:"+oldpass+"] [新密码:"+newpass+"] [渠道:"+channel+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
			return -1;
		}
		return 0;
	}

}
