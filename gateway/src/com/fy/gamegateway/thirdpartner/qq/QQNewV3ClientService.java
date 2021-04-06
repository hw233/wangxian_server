package com.fy.gamegateway.thirdpartner.qq;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.fy.gamegateway.mieshi.server.MieshiGatewaySubSystem;
import com.fy.gamegateway.mieshi.waigua.LoginEntity;
import com.qqv3.open.ErrorCode;
import com.qqv3.open.OpensnsException;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.text.StringUtil;

public class QQNewV3ClientService {
	
	static Logger logger = Logger.getLogger(MieshiGatewaySubSystem.class);
	public static final String APPID = "1106884290";//"1106696671";//"900000449";
	public static final String APPKEY = "wiKHDdqOWEeTofJv";//"SOsGivZqqB4VfDjD";//"mgwXU2Vy3nqKteTB";
	
	private static QQNewV3ClientService self;
	
	public static final String serverName_msdktest = "ysdktest.qq.com";
	public static final String serverName_msdk = "msdk.qq.com";
	public static String serverName_ysdk = "ysdk.qq.com";
//	【正式环境】http://ysdk.qq.com/auth/qq_check_token 
//	【测试环境】http://ysdktest.qq.com/auth/qq_check_token
	
	public static final String WXSDK_APPID = "wx8f7469c7e346be7b";
	public static final String WXSDK_APPKEY = "d0784594f7780dc8eda8b7f301f88844";//"139de98860ab887258b9a2edb197cff6";
	
	
//	APP ID1106884290
//	APP KEYwiKHDdqOWEeTofJv
	
	public static boolean TEST_WX = false;
	public static final String WX_URIAGRS = "/auth/check_token"; 
	
	public static QQNewV3ClientService getInstance() {
		if(self == null) {
			self = new QQNewV3ClientService();
		}
		return self;
	}
	
	public static String getCodeDescription(int code){
		if(code == 0){
			return "登录成功";
		}
		if(code == 1){
			return "登录失败";
		}
		return "未知响应码";
	}
	
	public boolean validateWXYSDK(String uin, String password,String ip,LoginEntity entity){
		long startTime = System.currentTimeMillis();
		if(uin.startsWith("QQUSER_") && uin.contains("_@@wx##_")) {
			uin = uin.split("_@@wx##_")[1];
		}
		String[] strs = password.split("@L#@");
        
        long timestamp = System.currentTimeMillis()/1000;
		String openkey = strs[0];
        String sign  = StringUtil.hash(WXSDK_APPKEY+timestamp).toLowerCase();
        String paramstr = "?timestamp="+timestamp+"&appid="+WXSDK_APPID+"&sig="+sign+"&openid="+uin+"&openkey="+openkey+"&userip="+ip;
        
        String urlStr = serverName_ysdk;
        if(TEST_WX){
        	urlStr = serverName_msdktest;
        }
        
        String scriptName = "/auth/wx_check_token";
        String url = "http://"+urlStr+scriptName+paramstr;
        
        HashMap headers = new HashMap();
        String resp = "";
    	try {
			byte bytes[] = HttpUtils.webGet(new java.net.URL(url),  headers, 60000, 10000);
			String encoding = (String)headers.get(HttpUtils.ENCODING);
			Integer code = (Integer)headers.get(HttpUtils.Response_Code);
			String message = (String)headers.get(HttpUtils.Response_Message);
			resp = new String(bytes,encoding);
			logger.info("[调用微信登录接口2] [返回数据] [成功] ["+url+"] [code:"+code+"] ["+message+"] [resp:"+resp+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
            try {
            	JSONObject jo = new JSONObject(resp);
            	//返回错误码，请参见：
            	//http://wiki.open.qq.com/wiki/%E5%85%AC%E5%85%B1%E8%BF%94%E5%9B%9E%E7%A0%81%E8%AF%B4%E6%98%8E#OpenAPI_V3.0_.E8.BF.94.E5.9B.9E.E7.A0.81
            	 int rc = jo.optInt("ret", 0);
                 if(rc == 0 ){
                	logger.info("[调用微信登录接口2] [返回数据] [成功] [返回数据:"+resp+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
                	return true;
                 } else{
                	 logger.info("[调用微信登录接口2] [返回数据] [失败] [返回数据:"+resp+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
                 }
            } catch (JSONException e) {
                throw new OpensnsException(ErrorCode.RESPONSE_DATA_INVALID, e); 
            } 
    	} catch (Exception e) {
    		e.printStackTrace();
			logger.error("[调用微信登录接口2] [失败] ["+url+"] [code:--] [--] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		return false;
	}
	
	/**
    	微信验证
		将要接口需要的参数，组装成数组，然后转换成json格式，放在http body中，通过post发送到指定的url中。
		http 协议的url 必须带有 appid,sig, timestamp,encode opendid等参数。接口中所有的参数都是utf8编码
	*/
	public boolean validateWX(String uin, String password,String ip,LoginEntity entity){
		long startTime = System.currentTimeMillis();
		if(uin.startsWith("QQUSER_") && uin.contains("_@@wx##_")) {
			uin = uin.split("_@@wx##_")[1];
		}
		String[] strs = password.split("@L#@");
		String openkey = strs[0];
		StringBuffer jsonBuffer = new StringBuffer();
        jsonBuffer.append("{");
        jsonBuffer.append("\"accessToken\":");
        jsonBuffer.append("\""+openkey+"\"");
        jsonBuffer.append(",");
        jsonBuffer.append("\"openid\":");
        jsonBuffer.append("\""+uin+"\"");
        jsonBuffer.append("}");
        
        long timestamp = System.currentTimeMillis()/1000;
        String sign  = StringUtil.hash(WXSDK_APPKEY+timestamp).toLowerCase();
        String paramstr = "/?timestamp="+timestamp+"&appid="+WXSDK_APPID+"&sig="+sign+"&openid="+uin+"&encode=1";
        String urlStr = serverName_msdk;
        if(TEST_WX){
        	urlStr = serverName_msdktest;
        }
        
        String content = jsonBuffer.toString();
        String url = "http://"+urlStr+WX_URIAGRS+paramstr;
        if(entity != null && entity.channel.toLowerCase().contains("qqysdk_xunxian")){
        	String scriptName = "/auth/wx_check_token";
        	url = "http://"+serverName_ysdk+scriptName+paramstr;
        }
        HashMap headers = new HashMap();
        String resp = "";
    	try {
			byte bytes[] = HttpUtils.webPost(new java.net.URL(url), content.getBytes(), headers, 60000, 10000);
			String encoding = (String)headers.get(HttpUtils.ENCODING);
			Integer code = (Integer)headers.get(HttpUtils.Response_Code);
			String message = (String)headers.get(HttpUtils.Response_Message);
			resp = new String(bytes,encoding);
			logger.info("[调用微信登录接口] [返回数据] [成功] ["+url+"] [code:"+code+"] ["+message+"] ["+content+"] [resp:"+resp+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
            try {
            	JSONObject jo = new JSONObject(resp);
            	//返回错误码，请参见：
            	//http://wiki.open.qq.com/wiki/%E5%85%AC%E5%85%B1%E8%BF%94%E5%9B%9E%E7%A0%81%E8%AF%B4%E6%98%8E#OpenAPI_V3.0_.E8.BF.94.E5.9B.9E.E7.A0.81
            	 int rc = jo.optInt("ret", 0);
                 if(rc == 0 ){
                	logger.info("[调用微信登录接口] [返回数据] [成功] [返回数据:"+resp+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
                	return true;
                 } else{
                	 logger.info("[调用微信登录接口] [返回数据] [失败] [返回数据:"+resp+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
                 }
            } catch (JSONException e) {
                throw new OpensnsException(ErrorCode.RESPONSE_DATA_INVALID, e); 
            } 
    	} catch (Exception e) {
    		e.printStackTrace();
			logger.error("[调用微信登录接口] [失败] ["+url+"] [code:--] [--] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		return false;
	}
	
	public boolean validateYsdk(String uin, String password,String ip,LoginEntity entity){
		long startTime = System.currentTimeMillis();
		if(uin.startsWith("QQUSER_")) {
			uin = uin.split("_")[1];
		}
		
		
		if(ip != null)
		{
			ip = ip.replaceAll("[/]", "");
			ip = ip.replaceAll(":(.)+", "");
		}
		
		//分割password 分隔符 @L#@
		String[] strs = password.split("@L#@");
		String openkey = strs[0];
		boolean isvalid = false;
		try {
			//最新地址：http://wiki.open.qq.com/wiki/YSDK%E5%90%8E%E5%8F%B0%E6%8E%A5%E5%8F%A3
		        // OpenAPI的服务器IP 
		        // 最新的API服务器地址请参考wiki文档: http://wiki.open.qq.com/wiki/API3.0%E6%96%87%E6%A1%A3 
		        // 所要访问的平台, pf的其他取值参考wiki文档: http://wiki.open.qq.com/wiki/API3.0%E6%96%87%E6%A1%A3
		        long timestamp = System.currentTimeMillis()/1000;
		        String sign  = StringUtil.hash(APPKEY+timestamp).toLowerCase();
		        
		        String paramstr = "?timestamp="+timestamp+"&appid="+APPID+"&sig="+sign+"&openid="+uin+"&openkey="+openkey+"&userip="+ip;
		        
		        String scriptName = "/auth/qq_check_token";
		        
		        String urlStr = serverName_ysdk;
		        if(TEST_WX){
		        	urlStr = serverName_msdktest;
		        }
		        
		        String url = "http://"+urlStr+scriptName+paramstr;
		        HashMap headers = new HashMap();
		        String resp = "";
		    	try {
					byte bytes[] = HttpUtils.webGet(new java.net.URL(url),  headers, 60000, 10000);
					String encoding = (String)headers.get(HttpUtils.ENCODING);
					Integer code = (Integer)headers.get(HttpUtils.Response_Code);
					String message = (String)headers.get(HttpUtils.Response_Message);
					resp = new String(bytes,encoding);
					MieshiGatewaySubSystem.logger.info("[调用QQ登录接口2] [成功] ["+url+"] [code:"+code+"] ["+message+"] [gameid:"+APPID+"] [secretkey:"+APPKEY+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
				} catch (Exception e) {
					logger.error("[调用QQ登录接口2] [失败] ["+url+"] [code:--] [--] [gameid:"+APPID+"] [secretkey:"+APPKEY+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
					return false;
				}
		        
		    	JSONObject jo = null;
	            try 
	            {
	                jo = new JSONObject(resp);
	            } 
	            catch (JSONException e) 
	            {
	                throw new OpensnsException(ErrorCode.RESPONSE_DATA_INVALID, e); 
	            } 
	            
	            int rc = jo.optInt("ret", 0);
	            
	            if(rc == 0 )
	            {
	            	isvalid = true;
	            }
	            else
	            {
	            	logger.error("[腾讯QZONE登陆验证2] [失败:获取用户信息失败] [uid:"+uin+"] [openkey:"+openkey+"] [resp:"+resp+"] [错误码:"+rc+"] [错误信息:"+jo.optString("msg", "未知错误")+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
	            	isvalid = false;
	            }
		        
		        if(isvalid)
		        {
		        	
		        	if(logger.isInfoEnabled())
		        		logger.info("[腾讯QZONE登陆验证2] [成功] [isvalid:"+isvalid+"] [uid:"+uin+"] [openkey:"+openkey+"] [resp:"+resp+"] [appkey:"+APPKEY+"] [appid:"+APPID+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
		        }
		        else
		        {
		        	logger.info("[腾讯QZONE登陆验证2] [失败] [isvalid:"+isvalid+"] [uid:"+uin+"] [openkey:"+openkey+"] [resp:--] [appkey:"+APPKEY+"] [appid:"+APPID+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
		        }
		} catch (Exception e) {
			logger.error("[腾讯QZONE登陆验证2] [失败:出现异常] [isvalid:"+isvalid+"] [uid:"+uin+"] [openkey:"+openkey+"] [pf:--] [resp:--] [appkey:"+APPKEY+"] [appid:"+APPID+"] [sdk.server:--] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		return isvalid;
	}
	
}
