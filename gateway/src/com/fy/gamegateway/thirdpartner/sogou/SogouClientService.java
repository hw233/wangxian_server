package com.fy.gamegateway.thirdpartner.sogou;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.fy.gamegateway.mieshi.server.MieshiGatewaySubSystem;
import com.fy.gamegateway.tools.JacksonManager;
import com.fy.boss.platform.xiaomi.common.HmacSHA1Encryption;
import com.fy.boss.platform.xiaomi.common.ParamEntry;
import com.fy.boss.platform.xiaomi.common.ParamUtil;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.text.StringUtil;



public class SogouClientService {
	
	static Logger logger = Logger.getLogger(MieshiGatewaySubSystem.class);

	public static boolean isTest = false;
	
	public static String testUrl = "http://dev.app.wan.sogou.com/api/v1/login/verify";
	
	public static String url = "http://api.app.wan.sogou.com/api/v1/login/verify";
	
	private static SogouClientService self;
	
	public static String gameid = "98";
	
	public static String app_secret = "020a5ec20547641201d64371b84bca8cdc1915e9085737402da8bd6ba1e8088e";
	
	public static SogouClientService getInstance() {
		if(self == null) {
			self = new SogouClientService();
		}
		return self;
	}
	
	public static String getCodeDescription(int code){
		if(code == 200){
			return "验证正确";
		}
		if(code == 1){
			return "参数错误(缺少必要参数)";
		}
		if(code == 5){
			return "uid错误";
		}
		if(code == 1520){
			return "session 错误";
		}
		if(code == 2001){
			return "无效的应用";
		}
		
		if(code == -1){
			return "未知错误";
		}
		
		return "未知响应码";
	}
	
	/**
	 * 验证sessionId有效性
	 * @param uin
	 * @param sessionId
	 * @return
	 */
	public boolean validate(String uin, String sessionId){
		long startTime = System.currentTimeMillis();
		
		if(uin.startsWith("SOGOUUSER_")) {
			uin = uin.split("_")[1];
		}
		String visiturl = url;
		
		if(isTest)
		{
			visiturl = testUrl;
		}
		
		List<ParamEntry> paras = new ArrayList<ParamEntry>();
		paras.add(new ParamEntry("gid",gameid));
		paras.add(new ParamEntry("user_id",uin));
		paras.add(new ParamEntry("session_key",sessionId));
		String returnString = null;
		String sign = null;
		try {
			returnString = ParamUtil.getSortQueryString(paras);
			sign =  StringUtil.hash(returnString+"&"+app_secret).toLowerCase();
		} catch (Exception e1) {
			logger.info("[调用登录接口] [失败] [出现异常] [url:"+url+"] [待签名字符串:"+returnString+"] [sign:--] [uin:"+uin+"] [sid:"+sessionId+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			return false;
		}
		String queryString = "";
		for(ParamEntry k : paras) {
			String v = k.getValue();
			queryString += k.getKey() + "=" + v  + "&";
		}
		queryString += "auth=" + sign;
		HashMap headers = new HashMap();
		String content =  "";
		try {
			byte bytes[] = HttpUtils.webPost(new java.net.URL(visiturl), queryString.getBytes("utf-8"), headers, 5000, 10000);
			String encoding = (String)headers.get(HttpUtils.ENCODING);
			Integer code = (Integer)headers.get(HttpUtils.Response_Code);
			String message = (String)headers.get(HttpUtils.Response_Message);
			content = new String(bytes,encoding);
			logger.info("[调用登录接口] [成功] ["+visiturl+"] [code:"+code+"] ["+message+"] ["+queryString+"] ["+content+"] [uin:"+uin+"] [sid:"+sessionId+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			logger.info("[调用登录接口] [失败] ["+visiturl+"] [code:--] [--] ["+queryString+"] ["+content+"] [uin:"+uin+"] [sid:"+sessionId+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
			return false;
		}
		JacksonManager jm = JacksonManager.getInstance();
		try {
			Map map =(Map)jm.jsonDecodeObject(content);
			String code = "50000";
			String desp = "";
			if(map.get("result") != null)
			{
				Object result = map.get("result");
				
				if(result instanceof String)
				{
					if("true".equals((String)result))
					{
						code = "200";
					}
					else
					{
						code = "1520";
					}
				}
				else if(result instanceof Boolean)
				{
					if((Boolean)result)
					{
						code = "200";
					}
					else
					{
						code = "1520";
					}
				}
			}
			else
			{
				if(map.get("error") != null)
				{
					Map errMap = (Map)map.get("error");
					desp  = (String)errMap.get("msg");
					code = (String)errMap.get("code");
				}
			}
			
			
			
			logger.info("[解析登录返回数据成功] [结果:"+(code.equals("200")?"验证合法":"验证失败")+"] [uin:"+uin+"] [sessionId:"+sessionId+"] [return_code:"+code+"] [return_desp:"+desp+"] [codeDesp:"+getCodeDescription(Integer.valueOf(code))+"] ["+visiturl+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			if(code.equals("200")) {
				return true;
			} 
		}catch (Exception e) {
			logger.info("[解析登录返回数据失败] [uin:"+uin+"] [sessionId:"+sessionId+"] [return:"+content+"] ["+visiturl+"] ["+(System.currentTimeMillis()-startTime)+"ms]", e);
		}
		return false;
	}
}
