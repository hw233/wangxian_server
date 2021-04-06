package com.fy.gamegateway.thirdpartner.uc;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.uc.g.sdk.cp.model.SDKException;
import cn.uc.g.sdk.cp.model.SessionInfo;
import cn.uc.g.sdk.cp.service.SDKServerService;

import com.fy.gamegateway.tools.JacksonManager;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.text.StringUtil;



public class UcBossClientServiceForNew {
	
	static Logger logger = Logger.getLogger(UcBossClientServiceForNew.class);

	public static int CPID = 88217;
	public static int ANDROID_GAMEID = 885649;
	public static int ANDROID_ServerID = 907;
	public static int ANDDROID_CHANNELID = 2;
	public static int ANDDROID_APPID = 10024;
	//public static String ANDDROID_Secretkey = "2b6f740373c5893c8b798798fbdda077";
	public static String ANDDROID_Secretkey = "1b7bbe8bff2e825c03f2cc42301dec92";
	
	public static int IOS_GAMEID = 63586;
	public static int IOS_ServerID = 908 ;
	public static int IOS_CHANNELID = 2;
	public static int IOS_APPID = 10025;
	public static String IOS_Secretkey = "1b31d9c43c8d8d7ebf655b6ab96f5c09";
	
	
/*	*//**
	 * 环境	CPID/GameID/ServerID/ChannelID	apikey
测试	149/43201/845/2	1b31d9c43c8d8d7ebf655b6ab96f5c09
isDebug=1	sdk.test2.g.uc.cn
http://sdk.test4.g.uc.cn/ss
	 *//*
	public static int CPID = 149;
	public static int ANDROID_GAMEID = 43201;
	public static int ANDROID_ServerID = 845;
	public static int ANDDROID_CHANNELID = 2;
	public static int ANDDROID_APPID = 10024;
	//public static String ANDDROID_Secretkey = "2b6f740373c5893c8b798798fbdda077";
	public static String ANDDROID_Secretkey = "1b31d9c43c8d8d7ebf655b6ab96f5c09";
	
	
	*//**
	 * ios(越狱)	测试	149/43202/846/2	1b31d9c43c8d8d7ebf655b6ab96f5c09	isDebug=1	sdk.test2.g.uc.cn
	 *//*
	public static int IOS_GAMEID = 43202;
	public static int IOS_ServerID = 846 ;
	public static int IOS_CHANNELID = 2;
	public static int IOS_APPID = 10025;
	public static String IOS_Secretkey = "1b31d9c43c8d8d7ebf655b6ab96f5c09";*/
	
	//http://sdk.test4.g.uc.cn/ss 测试
	//http://sdk.g.uc.cn/ss/ 正式
	//public static final String URL_PRE = "http://sdk.test4.g.uc.cn/ss";
	public static final String URL_PRE = "http://sdk.g.uc.cn/ss";
	
	public static boolean OPEN_TEST_URL = false;
	public String SID_URL = "http://sdk.9game.cn/cp/account.verifySession";
	public String SID_TEST_URL = "http://sdk.test4.9game.cn/cp/account.verifySession";
	
	
	
	private static UcBossClientServiceForNew self;
	public static UcBossClientServiceForNew getInstance(){
		if(self != null){
			return self;
		}
		self = new UcBossClientServiceForNew();
		return self;
	}
	
	public UCLoginResultNew getUcName(String password){
		 try {
			SessionInfo sessionInfo = SDKServerService.verifySession(password);
			UCLoginResultNew r = new UCLoginResultNew();
			r.username = sessionInfo.getAccountId();
			r.code = 1;
			logger.warn("[获取UC登录验证接口成功] [password:"+password+"] [AccountId:"+sessionInfo.getAccountId()+"] [Creator:"+sessionInfo.getCreator()+"] [NickName:"+sessionInfo.getNickName()+"]");
			return r;
		 } catch (SDKException e) {
			e.printStackTrace();
			logger.warn("获取UC登录验证接口异常"+e.getErrorCode());
		}
		return null;
	}
	
	
	public static String toJsonStr(HashMap<String,String> params){
		StringBuffer sb = new StringBuffer();
		String keys[] = params.keySet().toArray(new String[0]);
		for(int i = 0 ; i < keys.length ; i++){
			String v = params.get(keys[i]);
			sb.append("\""+keys[i]+"\":\""+v+"\"");
			if(i < keys.length -1){
				sb.append(",");
			}
		}
		return sb.toString();
	}
	
	public static String sign(int cpId,HashMap<String,String> params,String secretkey){
		
		String keys[] = params.keySet().toArray(new String[0]);
		java.util.Arrays.sort(keys);
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < keys.length ; i++){
			String v = params.get(keys[i]);
			sb.append(keys[i]+"="+v);
		}
		String md5Str =cpId + sb.toString() + secretkey;
		
		return md5Str;
	}
	
	public UCLoginResultNew sidValidate(String sid){
		if(sid == null){
			logger.warn("[调用新的用户会话验证接口] [错误: sid is null]");
			return null;
		}
		long startTime = System.currentTimeMillis();
		String url = SID_URL;
		if(OPEN_TEST_URL){
			url = SID_TEST_URL;
		}
		String bSignStr = "sid="+sid+ANDDROID_Secretkey;
		String sign = StringUtil.hash(bSignStr);
		
		HashMap<String,String> params = new HashMap<String,String>();
		params.put("sid",sid);
		HashMap<String,String> params2 = new HashMap<String,String>();
		params2.put("gameId",ANDROID_GAMEID+"");
		
		StringBuffer jsonStr = new StringBuffer();
		jsonStr.append("{\n");
		jsonStr.append("\"id\":"+System.currentTimeMillis()+",\n");
		jsonStr.append("\"data\":{"+toJsonStr(params)+"},\n");
		jsonStr.append("\"game\":{"+toJsonStr(params2)+"},\n");
		jsonStr.append("\"sign\":\""+sign+"\"\n");
		jsonStr.append("}");
		String logStr = jsonStr.toString().replaceAll("\n", "");
		
		HashMap headers = new HashMap();
		String content =  "";
		try {
			byte bytes[] = HttpUtils.webPost(new java.net.URL(url), jsonStr.toString().getBytes("UTF-8"), headers, 5000, 10000);
			
			String encoding = (String)headers.get(HttpUtils.ENCODING);
			Integer code = (Integer)headers.get(HttpUtils.Response_Code);
			String message = (String)headers.get(HttpUtils.Response_Message);
			
			content = new String(bytes,encoding);
			
			logger.info("[调用新的用户会话验证接口] [成功] ["+url+"] [code:"+code+"] ["+message+"] [签名前:"+bSignStr+"] [签名后:"+sign+"] ["+logStr+"] ["+content+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			logger.info("[调用新的用户会话验证接口] [失败] ["+url+"] [签名前:"+bSignStr+"] [签名后:"+sign+"] ["+logStr+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			UCLoginResultNew r = new UCLoginResultNew();
			r.code = 404;
			r.message = "调用新的UC接口出现异常";
			return null;
		}
		
		
		try {
			JacksonManager jm = JacksonManager.getInstance();
			Map map =(Map)jm.jsonDecodeObject(content);
			UCLoginResultNew r = new UCLoginResultNew();

			r.code = ((Number)((Map)map.get("state")).get("code")).intValue();
			r.message = ((String)((Map)map.get("state")).get("msg")).toString();
			r.message = URLDecoder.decode(r.message, "utf-8");
		
			if(r.code == 1){
				r.username = ((String)((Map)map.get("data")).get("accountId")).toString();
				r.creator =  ((String)((Map)map.get("data")).get("creator")).toString();
				r.nickName = ((String)((Map)map.get("data")).get("nickName")).toString();
				logger.info("[解析新的用户会话验证接口返回数据] [成功] ["+url+"] [status:"+r.code+"] [message:"+r.message+"] [ucid:"+r.ucid+"] [userName:"+r.username+"] [nickName:"+r.nickName+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
				return r;
			}
			logger.info("[解析新的用户会话验证接口返回数据] [失败] ["+url+"] [status:"+r.code+"] [message:"+r.message+"] [ucid:"+r.ucid+"] [userName:"+r.username+"] [nickName:"+r.nickName+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
		}catch (Exception e) {
			UCLoginResultNew r = new UCLoginResultNew();
			r.code = 404;
			r.message = "解析新的JSON串出现异常";
			logger.info("[解析新的用户会话验证接口返回数据] [失败] ["+url+"] [status:"+r.code+"] [message:"+r.message+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		return null;
	}
	
}
