package com.fy.gamegateway.thirdpartner.qq;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.fy.gamegateway.mieshi.server.MieshiGatewaySubSystem;
import com.qqv3.open.ErrorCode;
import com.qqv3.open.OpenApiV3;
import com.qqv3.open.OpensnsException;

public class QQZoneClientService {
	
	static Logger logger = Logger.getLogger(MieshiGatewaySubSystem.class);
	//public static final String APPID = "1000000429";
	public static final String APPID = "900000449";
//	public static final String APPID = "207";
	
	//public static final String APPKEY = "mgwXU2Vy3nqKteTB";
//	public static final String APPKEY = "urB63l40Ir092fjU";
	public static final String APPKEY = "mgwXU2Vy3nqKteTB";
	public static final String TESTAPPID = "900000106";
	public static final String TESTAPPKEY = "OUjiz2QqjnYziQkJ";
    public static final String testServerName = "119.147.19.43";
   // public static final String serverName = "openapi.tencentyun.com";
//    public static final String serverName = "openapigate.3g.qq.com";
    public static final String serverName = "openapi.3g.qq.com";
	private static QQZoneClientService self;
	
	
	public static QQZoneClientService getInstance() {
		if(self == null) {
			self = new QQZoneClientService();
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
	
	/**
	 * 验证sessionId有效性
	 * @param uin
	 * @param sessionId
	 * @return
	 */
	public boolean validate(String uin, String password){
		long startTime = System.currentTimeMillis();
		if(uin.startsWith("QQUSER_")) {
			uin = uin.split("_")[1];
		}
		
		//分割password 分隔符 @L#@
		String[] strs = password.split("@L#@");
		String openkey = strs[0];
		boolean isvalid = false;
		try {
		        // OpenAPI的服务器IP 
		        // 最新的API服务器地址请参考wiki文档: http://wiki.open.qq.com/wiki/API3.0%E6%96%87%E6%A1%A3 
		        // 所要访问的平台, pf的其他取值参考wiki文档: http://wiki.open.qq.com/wiki/API3.0%E6%96%87%E6%A1%A3
		        String pf = "qzone";
		        OpenApiV3 sdk = new OpenApiV3(APPID, APPKEY);
		        String sn = serverName;
		        sdk.setServerName(sn);
		        String resp = getUserInfo(sdk, uin, openkey, pf);
		        if(resp != null)
		        {
		        	isvalid = true;
		        	if(logger.isInfoEnabled())
		        		logger.info("[腾讯QZONE登陆验证] [成功] [isvalid:"+isvalid+"] [uid:"+uin+"] [openkey:"+openkey+"] [pf:"+pf+"] [resp:"+resp+"] [appkey:"+APPKEY+"] [appid:"+APPID+"] [sdk.server:"+sn+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
		        }
		        else
		        {
		        	logger.info("[腾讯QZONE登陆验证] [失败] [isvalid:"+isvalid+"] [uid:"+uin+"] [openkey:"+openkey+"] [pf:"+pf+"] [resp:--] [appkey:"+APPKEY+"] [appid:"+APPID+"] [sdk.server:"+sn+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
		        }
		        
		
		} catch (Exception e) {
			logger.error("[腾讯QZONE登陆验证] [失败:出现异常] [isvalid:"+isvalid+"] [uid:"+uin+"] [openkey:"+openkey+"] [pf:--] [resp:--] [appkey:"+APPKEY+"] [appid:"+APPID+"] [sdk.server:--] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		
		return isvalid;
	}
		
	
	  public static String getUserInfo(OpenApiV3 sdk, String openid, String openkey, String pf)
	   {
		  	long startTime = System.currentTimeMillis();
	        // 指定OpenApi Cgi名字 
	        String scriptName = "/v3/user/is_login";
	        // 指定HTTP请求协议类型
	        String protocol = "http";

	        // 填充URL请求参数
	        HashMap<String,String> params = new HashMap<String, String>();
	        params.put("openid", openid);
	        params.put("openkey", openkey);
	        params.put("pf", pf);
	        String resp = null;
	        
	        try
	        {
	            resp = sdk.api(scriptName, params, protocol);
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
	            	return resp;
	            }
	            else
	            {
	            	logger.error("[腾讯QZONE登陆验证] [失败:获取用户信息失败] [uid:"+openid+"] [openkey:"+openkey+"] [resp:"+resp+"] [错误码:"+rc+"] [错误信息:"+jo.optString("msg", "未知错误")+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
	            	return null;
	            }
	        }
	        catch (OpensnsException e)
	        {
	        	logger.error("[腾讯QZONE登陆验证] [失败:获取用户信息失败] [uid:"+openid+"] [openkey:"+openkey+"] [resp:--] [错误码:"+e.getErrorCode()+"] [错误信息:"+e.getMessage()+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
	        }
	        
	        return resp;
	    }
	
	
	
}
