package com.fy.gamegateway.thirdpartner.baoruan;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.fy.gamegateway.mieshi.server.MieshiGatewaySubSystem;
import com.fy.gamegateway.thirdpartner.dcn.DCNLoginResult;
import com.fy.gamegateway.tools.JacksonManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.xuanzhi.tools.text.StringUtil;
import com.xuanzhi.tools.text.XmlUtil;
import com.xuanzhi.tools.servlet.HttpUtils;



public class BaoRuanBossClientService {
	
	static Logger logger = Logger.getLogger(BaoRuanBossClientService.class);

	
	//appid=135833496863804037 cid=135830493976591001 uniquekey=D9iB5BsD8KuYbtuxornbQQuDzkRQDgnh financekey=hXyRXqLR2IoRFtBYFpq4wnQVuFTs6U8m
	private String appid = "135833496863804037";
	private String cid = "135830493976591001";
	private String uniquekey = "D9iB5BsD8KuYbtuxornbQQuDzkRQDgnh";
	private String financekey = "hXyRXqLR2IoRFtBYFpq4wnQVuFTs6U8m";
	
	
	
	private static BaoRuanBossClientService self;
	public static BaoRuanBossClientService getInstance(){
		if(self != null){
			return self;
		}
		self = new BaoRuanBossClientService();
		return self;
	}
	
	public RegisterResultNew register(String platform, String username,String mobile,String password){
		
		
		long startTime = System.currentTimeMillis();
		
		String registerUrl  = "http://www.lewan.cn/api/account/register/vt/3g";
		
		LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();

		params.put("username",username);
		params.put("password", password);
		params.put("email", "");
		params.put("mobilenumber", "");
		params.put("appid", appid);
		params.put("uniquekey", uniquekey);
		params.put("verifystring", sign(username,password));
	//	params.put("json", "false");

		//registerUrl += "?" + buildParams(params);
		
		HashMap headers = new HashMap();

		String content =  "";
		content = buildParams(params);
		try {
			byte bytes[] = HttpUtils.webPost(new java.net.URL(registerUrl), content.getBytes(), headers, 5000, 10000);
			
			String encoding = (String)headers.get(HttpUtils.ENCODING);
			Integer code = (Integer)headers.get(HttpUtils.Response_Code);
			String message = (String)headers.get(HttpUtils.Response_Message);
			
			content = new String(bytes,encoding);
			
			
			logger.info("[调用注册接口] ["+platform+"] [成功] ["+registerUrl+"] [code:"+code+"] ["+message+"] [sign:"+params.get("verifystring")+"] ["+content+"] [用户名:"+username+"] [密码:"+password+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			
			logger.info("[调用注册接口] ["+platform+"] [失败] ["+registerUrl+"] [code:--] [--] [sign:"+params.get("verifystring")+"] ["+content+"] [用户名:"+username+"] [密码:"+password+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
			
			RegisterResultNew r = new RegisterResultNew();
			r.code = 404;
			r.message = "调用宝软注册接口出现异常";
						
			return r;
		}
		
		Document dom = null;
		try {
			dom = XmlUtil.loadString(content);
		} catch (Exception e) {
			PlatformSavingCenter.logger.error("[读取宝软注册接口XML内容] [失败] [解析响应内容信息] [失败] [字符集:"+(String)headers.get(HttpUtils.ENCODING)+"] [code:"+(Integer)headers.get(HttpUtils.Response_Code)+"] [响应信息:"+(String)headers.get(HttpUtils.Response_Message)+"] [content:"+content+"] [用户名:"+username+"] [密码:"+password+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
			RegisterResultNew r = new RegisterResultNew();
			r.code = 500;
			r.message = "读取宝软注册接口XML内容出现异常";
			return r;
		}
		Element root = dom.getDocumentElement();
		
		long uid = XmlUtil.getValueAsLong(XmlUtil.getChildByName(root, "uid"), -1);
		String returnUsername = XmlUtil.getValueAsString(XmlUtil.getChildByName(root, "username"), "", null);
		String returnPassword = XmlUtil.getValueAsString(XmlUtil.getChildByName(root, "password"), "", null);
		String returnVerifyString = XmlUtil.getValueAsString(XmlUtil.getChildByName(root, "verifystring"), "", null);
		RegisterResultNew r = new RegisterResultNew();
		if(uid != -1 && !StringUtils.isEmpty(returnUsername))
		{
			r.code = 200;
			r.username = returnUsername;
			r.passwd = returnPassword;
			r.uid = uid;
			logger.info("[解析注册返回数据] ["+platform+"] [成功] [去宝软注册] [成功] ["+registerUrl+"] [status:"+r.code+"] [message:"+r.message+"] [用户名:"+username+"] [密码:"+password+"] [返回用户名:"+returnUsername+"] [返回密码："+returnPassword+"] [宝软uid:"+uid+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
		}
		else
		{
			String errorMessage = XmlUtil.getValueAsString(XmlUtil.getChildByName(root, "error"), "", null);
			r.code = 500;
			r.message = errorMessage;
			logger.info("[解析注册返回数据] ["+platform+"] [成功] [去宝软注册] [失败] ["+registerUrl+"] [status:"+r.code+"] [message:"+r.message+"] [用户名:"+username+"] [密码:"+password+"] [错误信息:"+errorMessage+"] [返回内容:"+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
		}
		return r;
	}
	
	private  String sign(String username,String passwd)
	{
		String md5Str = StringUtil.hash(appid+username+passwd+uniquekey);
		return md5Str;
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
	
	public BAORUANLoginResult login(String username,String password){
		 long startTime = System.currentTimeMillis();
		 
		 String url = "http://www.lewan.cn/api/account/login/vt/3g";
		 
	/*	 username string 是 Lewan 系统用户名
		 password string 是 对应的用户密码
		 appid int 是 应用的 id
		 uniquekey string 是 验证摘要串 见表底
		 json boolean 否 要求返回 json
		 verifystring = md5( appid + user name + p asswor d + uniquekey )
		*/
		 
		LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();

		params.put("username",username);
		params.put("password", password);
		params.put("appid", appid);
		params.put("uniquekey", uniquekey);
	//	params.put("json", "false");
		params.put("verifystring", sign(username,password));
		
		
	//	url += "?" + buildParams(params);
		HashMap headers = new HashMap();
	//	headers.putAll(params);
		String content =  "";
		content = buildParams(params);
		try {
			byte bytes[] = HttpUtils.webPost(new java.net.URL(url), content.getBytes(), headers, 5000, 10000);
			
			String encoding = (String)headers.get(HttpUtils.ENCODING);
			Integer code = (Integer)headers.get(HttpUtils.Response_Code);
			String message = (String)headers.get(HttpUtils.Response_Message);
			
			content = new String(bytes,encoding);
			
			
			logger.info("[调用登陆接口] [宝软（乐玩）] [成功] ["+url+"] [code:"+code+"] ["+message+"] [sign:"+params.get("verifystring")+"] ["+content+"] [用户名:"+username+"] [密码:"+password+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			
			logger.info("[调用登陆接口] [宝软（乐玩）] [失败] ["+url+"] [code:--] [--] [sign:"+params.get("verifystring")+"] ["+content+"] [用户名:"+username+"] [密码:"+password+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
			
			BAORUANLoginResult r = new BAORUANLoginResult();
			r.code = 404;
			r.message = "调用宝软登陆接口出现异常";
						
			return r;
		}
		
		Document dom = null;
		try {
			dom = XmlUtil.loadString(content);
		} catch (Exception e) {
			PlatformSavingCenter.logger.error("[读取宝软登陆接口XML内容] [失败] [解析响应内容信息] [失败] [字符集:"+(String)headers.get(HttpUtils.ENCODING)+"] [code:"+(Integer)headers.get(HttpUtils.Response_Code)+"] [响应信息:"+(String)headers.get(HttpUtils.Response_Message)+"] [content:"+content+"] [用户名:"+username+"] [密码:"+password+"] [costs:"+(System.currentTimeMillis()-startTime)+"]",e);
			BAORUANLoginResult r = new BAORUANLoginResult();
			r.code = 500;
			r.message = "读取宝软登陆接口XML内容出现异常";
			return r;
		}
		Element root = dom.getDocumentElement();
		
		long uid = XmlUtil.getValueAsLong(XmlUtil.getChildByName(root, "uid"), -1);
		String returnUsername = XmlUtil.getValueAsString(XmlUtil.getChildByName(root, "username"), "", null);
		String returnPassword = XmlUtil.getValueAsString(XmlUtil.getChildByName(root, "password"), "", null);
		String avatar = XmlUtil.getValueAsString(XmlUtil.getChildByName(root, "avatar"), "", null);
		String gender = XmlUtil.getValueAsString(XmlUtil.getChildByName(root, "gender"), "", null);
		String returnVerifyString = XmlUtil.getValueAsString(XmlUtil.getChildByName(root, "verifystring"), "", null);
		BAORUANLoginResult r = new BAORUANLoginResult();
	
			if(!StringUtils.isEmpty(returnUsername))
			{
				if(sign(returnUsername,returnPassword).equalsIgnoreCase(returnVerifyString))
				{
					
					r.uid = uid;
					r.code = 200;
					r.username = returnUsername;
					r.passwd = returnPassword;
					r.avatar = avatar;
					r.gender = gender;
					logger.info("[解析登录返回数据] [宝软（乐玩）] [成功] [去宝软登陆] [成功] ["+url+"] [status:"+r.code+"] [message:"+r.message+"] [uid:"+uid+"] [用户名:"+username+"] [密码:"+password+"] [返回用户名:"+returnUsername+"] [返回密码："+returnPassword+"] [宝软用户头像url:"+r.avatar+"] [宝软用户性别:"+r.gender+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
				}
				else
				{
					String errorMessage = "调用宝软登录接口签名验证失败";
					r.code = 500;
					r.message = errorMessage;
					logger.info("[解析登录返回数据] [宝软（乐玩）] [成功] [去宝软登陆] [失败] [签名串不一致] ["+url+"] [status:"+r.code+"] [message:"+r.message+"] [用户名:"+username+"] [密码:"+password+"] [错误信息:"+errorMessage+"] [传入用户名:"+returnUsername+"] [传入密码："+returnPassword+"] [传入签名："+returnVerifyString+"] [生成签名串："+sign(returnUsername,returnPassword)+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
				}
			}
			else
			{
				String errorMessage = XmlUtil.getValueAsString(XmlUtil.getChildByName(root, "error"), "", null);
				r.code = 500;
				r.message = errorMessage;
				logger.info("[解析登录返回数据] [宝软（乐玩）] [成功] [去宝软登陆] [失败] ["+url+"] [status:"+r.code+"] [message:"+r.message+"] [用户名:"+username+"] [密码:"+password+"] [错误信息:"+errorMessage+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			}
		
		
		return r;
		
		

	}
	
	

}
