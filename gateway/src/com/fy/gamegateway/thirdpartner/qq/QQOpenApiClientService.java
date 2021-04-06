package com.fy.gamegateway.thirdpartner.qq;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.log4j.Logger;

import com.fy.gamegateway.mieshi.server.MieshiGatewaySubSystem;
import com.fy.gamegateway.thirdpartner.qq.mime.qweibo.HttpRspInfo;
import com.fy.gamegateway.thirdpartner.qq.mime.qweibo.OauthKey;
import com.fy.gamegateway.thirdpartner.qq.mime.qweibo.QParameter;
import com.fy.gamegateway.thirdpartner.qq.mime.qweibo.QWeiboRequest;
import com.fy.boss.client.BossClientService;

public class QQOpenApiClientService {
	
	static Logger logger = Logger.getLogger(MieshiGatewaySubSystem.class);
	
	private static QQOpenApiClientService self;
	
	public static QQOpenApiClientService getInstance() {
		if(self == null) {
			self = new QQOpenApiClientService();
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
		String token = strs[0];
		String tokensecrect = strs[1];
		//sessionid 就是 access token
		OauthKey oauthParam = new OauthKey();
		oauthParam.customKey = AppInfo.APP_KEY ;
		oauthParam.customSecrect = AppInfo.APP_SECRECT;
		oauthParam.tokenKey = token;
		oauthParam.tokenSecrect = tokensecrect ;
		boolean isvalid = false;
		
		try {
			isvalid = verifyUserToken(oauthParam, AppInfo.URL_CONNECT);
			logger.info("[腾讯登陆验证] [成功] [isvalid:"+isvalid+"] [uid:"+uin+"] [tokenkey:"+token+"] [tokenSecrect:"+tokensecrect+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			return isvalid;
		} catch (Exception e) {
			logger.info("[调用登录接口] [失败] [uid:"+uin+"] [tokenkey:"+token+"] [tokenSecrect:"+tokensecrect+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
			return isvalid;
		}
	}
	
	public boolean verifyUserToken(OauthKey key, String url)
	{

		StringBuffer userInfo = new StringBuffer();
		boolean b = connect(key, url, userInfo);
		String str = userInfo.toString();

		// 请注意str格式open_id&oid,open_id一定存在
		String[] userSplit = str.split("&");
		String open_id = userSplit[0];
		if (userSplit.length > 1)
			key.oidKey = userSplit[1];

		key.open_id = open_id;

		if (b && !open_id.isEmpty()) {
			// 合法的token和secrct已经登录过了。
			return true;
		} else
			return false;
	}	
	
	
	public boolean connect(OauthKey authParam, String url, StringBuffer userid) 
	{
		long startTime = System.currentTimeMillis();
			HttpRspInfo rsp = null;
			try {
				QWeiboRequest request = new QWeiboRequest();
				List<QParameter> parameters = new ArrayList<QParameter>();
				rsp = request.syncRequest(url, QWeiboRequest.HTTP_GET, authParam,	parameters, null);
				
				if(rsp == null){
					throw new Exception("syncRequest返回null");
				}
			} catch (Exception e) {
				logger.info("[调用登录接口] [失败] [出现异常] [url:"+url+"] [openid:"+authParam.open_id+"] [customKey:"+authParam.customKey+"] [customSecrect:"+authParam.customSecrect+"] [token:"+authParam.tokenKey+"] [tokenSecrect:"+authParam.tokenSecrect+"] [verify:"+authParam.verify+"] [callbackUrl:"+authParam.callbackUrl+"] [返回码:"+rsp.httpCode+"] [返回数据:"+rsp.responseData+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
			}
			if (rsp.httpCode == HttpStatus.SC_OK) {
				
				if (userid != null && rsp.responseData != null)
				{
					userid.append(rsp.responseData);
				}
				
				logger.info("[调用登录接口] [成功] [OK] [url:"+url+"] [openid:"+authParam.open_id+"] [customKey:"+authParam.customKey+"] [customSecrect:"+authParam.customSecrect+"] [token:"+authParam.tokenKey+"] [tokenSecrect:"+authParam.tokenSecrect+"] [verify:"+authParam.verify+"] [callbackUrl:"+authParam.callbackUrl+"] [返回码:"+rsp.httpCode+"] [返回数据:"+rsp.responseData+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
				
				return true;
			} else {
				logger.info("[调用登录接口] [失败] [状态码不是"+HttpStatus.SC_OK+"] [url:"+url+"] [openid:"+authParam.open_id+"] [customKey:"+authParam.customKey+"] [customSecrect:"+authParam.customSecrect+"] [token:"+authParam.tokenKey+"] [tokenSecrect:"+authParam.tokenSecrect+"] [verify:"+authParam.verify+"] [callbackUrl:"+authParam.callbackUrl+"] [返回码:"+rsp.httpCode+"] [返回数据:"+rsp.responseData+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
				return false;
			}	
	}
	
	
}
