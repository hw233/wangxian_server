package com.fy.boss.platform.qq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.fy.boss.platform.qq.QQInfoService;
import com.fy.boss.platform.qq.QQUserInfo;
import com.mime.qweibo.HttpRspInfo;
import com.mime.qweibo.OauthKey;
import com.mime.qweibo.QParameter;
import com.mime.qweibo.QWeiboRequest;
import com.tencent.java.sdk.AppConfig;
import com.tencent.java.sdk.BackEndSDK;
import com.xuanzhi.ms.server.logic.provision.unicom.SyncNotifyServlet;


public class QQInfoService {
	public static Logger logger = Logger.getLogger(QQInfoService.class);
	
	public static final String QZONEAPPID = "900000449";
	public static final String QZONEAPPKEY = "mgwXU2Vy3nqKteTB";
	public static final int OPENAPIAPPID = 207;
	public static final String OPENAPIAPPKEY = "NBAyR40Mi2BYg288Pz91";
	public static final String OPENAPISECRET = "mgwXU2Vy3nqKteTB";
	public static final String TESTQZONEAPPID = "900000106";
	public static final String TESTQZONEAPPKEY = "OUjiz2QqjnYziQkJ";
	public static final String serverName = "openapi.3g.qq.com";
	
	private static QQInfoService qqInfoService = null;
	
	public  QQUserInfo getQQUserInfoByUserName(String username,String clientString,String channel)
	{
		try
		{
			
			/*
			 * 访问qq用户消息接口
			 */
	
		  	long startTime = System.currentTimeMillis();
		  	
		  	QQUserInfo qqUserInfo = null;
		  	
		  	if(username.startsWith("QQUSER_")) {
				username = username.split("_")[1];
			}

		  	if(channel.toLowerCase().contains("qq")) //qzone
		  	{
		      //分割password 分隔符 @L#@
				String[] strs = clientString.split("@L#@");
				String openkey = strs[0];
		        // 指定HTTP请求协议类型
		        // 填充URL请求参数
		        String resp = null;
		        
		        try
		        {
		        	if(channel.toLowerCase().contains("qqzone"))
		        		resp = BackEndSDK.getMagic(openkey, username, QZONEAPPID, QZONEAPPKEY, AppConfig.platformid, AppConfig.terminaltype);
		        	else
		        		resp = BackEndSDK.getMagic(openkey, username, OPENAPIAPPID+"", OPENAPISECRET, AppConfig.platformid, AppConfig.terminaltype);
		        	JSONObject jo = null;
		            try 
		            {
		                jo = new JSONObject(resp);
		                qqUserInfo = parseJson(jo);
		                if(QQInfoService.logger.isInfoEnabled())
		                {
		                	QQInfoService.logger.info("[获取qq用户魔钻信息] [成功] [ok] [username:"+username+"] [openkey:"+clientString +"] [channel:"+channel+"] [请求的api:"+AppConfig.magic+"] [协议:--] [请求url:"+AppConfig.OpenPlatformURL+"] [appid:"+QZONEAPPID+"] [appkey:"+QZONEAPPKEY+"] [serverName:"+serverName+"] [返回值:"+resp+"] [解析后的json对象为:"+jo.toString()+"]");
		                }
		                
		            } 
		            catch (Exception e) 
		            {
		            	QQInfoService.logger.error("[获取qq用户魔钻信息] [失败] [出现异常:解析json失败] [username:"+username+"] [openkey:"+clientString +"] [channel:"+channel+"] [请求的api:"+AppConfig.magic+"] [协议:--] [请求url:"+AppConfig.OpenPlatformURL+"] [appid:"+QZONEAPPID+"] [appkey:"+QZONEAPPKEY+"] [serverName:"+serverName+"] [返回值:"+resp+"] [解析后的json对象为:--]",e);
		            } 
		            
		        }
		        catch (Exception e)
		        {
		        	QQInfoService.logger.error("[获取qq用户魔钻信息] [失败] [出现异常:获取返回信息失败] [username:"+username+"] [openkey:"+clientString+"] [channel:"+channel+"] [请求的api:"+AppConfig.magic+"] [协议:--] [请求url:"+AppConfig.OpenPlatformURL+"] [appid:"+QZONEAPPID+"] [appkey:"+QZONEAPPKEY+"] [serverName:"+serverName+"] [返回值:"+resp+"] [解析后的json对象为:--]",e);
		        }
		  	}
		  	
		  	if(qqUserInfo == null)
		  	{
		  		
		  		//分割password 分隔符 @L#@
				String[] strs = clientString.split("@L#@");
				String token = strs[0];
				String tokensecrect = strs[1];
			
				
				OauthKey oauthParam = new OauthKey();
				oauthParam.customKey = OPENAPIAPPKEY;
				oauthParam.customSecrect = OPENAPISECRET;
				oauthParam.tokenKey = token;
				oauthParam.tokenSecrect = tokensecrect ;
				String url = "http://"+serverName+"/people/@me/@self";
				JSONObject jo = null;
				
				HttpRspInfo rsp = null;
				
				try {
					
					
					QWeiboRequest request = new QWeiboRequest();
					List<QParameter> parameters = new ArrayList<QParameter>();
					parameters.add(new QParameter("fields", "id,gamevip,gamevipLevel"));
					
					rsp = request.syncRequest(url, QWeiboRequest.HTTP_GET, oauthParam,	parameters, null);
					
					
					if(rsp == null){
						throw new Exception("syncRequest返回null");
					}
					
					try
					{
						jo = new JSONObject(rsp.responseData);
		                qqUserInfo = parseJson(jo);
		                if(QQInfoService.logger.isInfoEnabled())
		                {
		                	QQInfoService.logger.info("[获取qq用户魔钻信息] [成功] [ok] [username:"+username+"] [openkey:"+clientString +"] [channel:"+channel+"] [请求的api:"+url+"] [协议:http] [请求内容:"+oauthParam+"] [appid:"+OPENAPIAPPID+"] [appkey:"+OPENAPIAPPKEY+"("+OPENAPISECRET+")] [serverName:"+serverName+"] [返回值:"+rsp.responseData+"("+rsp.httpCode+")] [解析后的json对象为:"+jo.toString()+"]");
		                }
					}
					catch(Exception e)
					{
						QQInfoService.logger.error("[获取qq用户魔钻信息] [失败] [出现异常:解析json失败] [username:"+username+"] [openkey:"+clientString +"] [channel:"+channel+"] [请求的api:"+url+"] [协议:http] [请求内容:"+oauthParam+"] [appid:"+OPENAPIAPPID+"] [appkey:"+OPENAPIAPPKEY+"("+OPENAPISECRET+")] [serverName:"+serverName+"] [返回值:"+rsp.responseData+"("+rsp.httpCode+")] [解析后的json对象为:--]",e);
					}
	                
				} catch (Exception e) {
					QQInfoService.logger.error("[获取qq用户魔钻信息] [失败] [出现异常:获取返回信息失败] [username:"+username+"] [openkey:"+clientString +"] [channel:"+channel+"] [请求的api:"+url+"] [协议:http] [请求内容:"+oauthParam+"] [appid:"+OPENAPIAPPID+"] [appkey:"+OPENAPIAPPKEY+"("+OPENAPISECRET+")] [serverName:"+serverName+"] [返回值:--] [解析后的json对象为:--]",e);
				}
		  	}
	        
		    return qqUserInfo;
	      
		}
		catch(Exception e)
		{
			QQInfoService.logger.error("[获取qq用户魔钻信息] [失败] [出现异常:获取返回信息失败] [username:"+username+"] [openkey:"+clientString +"] [channel:"+channel+"] [请求的api:--] [协议:http] [请求内容:--] [appid:--] [appkey:--] [serverName:"+serverName+"] [返回值:--] [解析后的json对象为:--]",e);
			return null;
		}

	}
	
	private QQUserInfo parseJson(JSONObject jo) throws Exception
	{
		//解析json
		/**
		 * 2.7 返回结果
参数名称	类型及范围	是否必选	说明
totalResults	int	true	返回条目的总数。
itemsPerPage	int	true	分页时请求时每页包括的数据数量，如果请求有count参数与它一致。
entry	string	true	数组对象标签，如果返回包括多个对象，则是数组形式，否则是单个对象形式。
以下是魔钻信息字段
gamevip	string	true	是否开通腾讯的魔钻业务。
gamevipLevel	string	true	魔钻等级。
		 */
		JSONObject joo = jo.getJSONObject("entry");
		QQUserInfo q = new QQUserInfo();
		q.setMozuan(joo.getBoolean("gamevip"));
		q.setMozuanDengji(joo.getInt("gamevipLevel"));
		
		return q;
	}
	

	
	
	
	public static synchronized  QQInfoService getInstance()
	{
		if(qqInfoService == null)
		{
			qqInfoService = new QQInfoService();
		}
		return qqInfoService;
	}
	
	
}
