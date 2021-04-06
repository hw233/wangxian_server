package com.fy.gamegateway.thirdpartner.v8;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.fy.gamegateway.mieshi.waigua.NewLoginHandler;
import com.fy.gamegateway.tools.JacksonManager;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.text.StringUtil;

public class V8ClientService {

	private String checkUrl = "http://userver.huashy.cn:8080/user/verifyAccount";
	private String AppSecret = "3db0c2e953da147d26969a10d0dc2bd6";
	Logger logger = NewLoginHandler.logger;
	private static V8ClientService self;
	public static V8ClientService getInstance(){
		if(self == null){
			self = new V8ClientService();
		}
		return self;
	}
	
	public static final String [] keys = {};
	
	public static final String [] values = {};
	
	public static String getChannelStr(String code){
		for(int i=0;i<keys.length;i++){
			if(keys[i].equals(code)){
				return values[i];
			}
		}
		return "错误";
	}
	
	public static void main(String[] args) {
		
	}
	public boolean checkLogin(String username,String pwd){
		try {
			long startTime = System.currentTimeMillis();
			String uid = username.split("V8USER_")[1];
			String signStr = "userID="+uid+"token="+pwd+AppSecret;
			String sign = StringUtil.hash(signStr);
			String queryStr = "userID="+uid+"&token="+pwd+"&sign="+sign;
			String content = "";
			try {
				Map headers = new HashMap();
				byte bytes[] = HttpUtils.webPost(new java.net.URL(checkUrl), queryStr.getBytes("utf-8"), headers, 60000, 60000);
				String encoding = (String)headers.get(HttpUtils.ENCODING);
				Integer code = (Integer)headers.get(HttpUtils.Response_Code);
				String message = (String)headers.get(HttpUtils.Response_Message);
				content = new String(bytes,encoding);
				logger.info("[v8调用登录接口] [成功] [username:"+username+"] [signStr:"+signStr+"] [queryStr:"+queryStr+"] [code:"+code+"] ["+message+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			} catch (Exception e) {
				logger.info("[v8调用登录接口] [失败] [username:"+username+"] [signStr:"+signStr+"] [queryStr:"+queryStr+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
				return false;
			}
	
			JacksonManager jm = JacksonManager.getInstance();
			try {
				Map map =(Map)jm.jsonDecodeObject(content);

				Integer resultState = (Integer)map.get("state");
				
				if(resultState == null){
					logger.info("[v8解析登录返回数据] [失败:code is null] [queryStr:"+queryStr+"] [message:"+content+"] [username:"+username+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
					return false;
				}
				
				if(resultState != 1){
					logger.info("[v8解析登录返回数据] [失败:code状态码不是成功] [queryStr:"+queryStr+"] [message:"+content+"] [username:"+username+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
					return false;
				}
				logger.info("[v8最后验证] [成功] [username:"+username+"] [token:"+pwd+"] [content:"+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
				return true;
			}catch (Exception e) {
				logger.error("[v8解析登录返回数据] [失败] [username:"+username+"] [token:"+pwd+"] [content:"+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
				return false;
			}
		}catch(Throwable t){
			logger.error("[v8调用登陆接口] [失败:未知异常] [username："+username+"] [token:"+pwd+"]",t);
			return false;
		} 
	}
	
	public boolean checkLogin2(String username,String pwd){
		try {
			long startTime = System.currentTimeMillis();
			String uid = username.split("LEHAIHAIUSER_")[1];
			//sign = md5("pid="+pid+"&token="+token+"&username="+username+key)
			String signStr = "pid=14314"+"&token="+pwd+"&username="+uid+"83580300BEB790E69AEC915B330395BE";
			String sign = StringUtil.hash(signStr);
			String queryStr = "username="+uid+"&token="+pwd+"&pid=14314"+"&sign="+sign;
			String content = "";
			try {
				Map headers = new HashMap();
				byte bytes[] = HttpUtils.webPost(new java.net.URL("http://sdk-nscs.btgame01.com/index.php/User/v8"), queryStr.getBytes("utf-8"), headers, 60000, 60000);
				String encoding = (String)headers.get(HttpUtils.ENCODING);
				Integer code = (Integer)headers.get(HttpUtils.Response_Code);
				String message = (String)headers.get(HttpUtils.Response_Message);
				content = new String(bytes,encoding);
				logger.info("[乐嗨嗨调用登录接口] [成功] [username:"+username+"] [signStr:"+signStr+"] [queryStr:"+queryStr+"] [code:"+code+"] ["+message+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			} catch (Exception e) {
				logger.info("[乐嗨嗨调用登录接口] [失败] [username:"+username+"] [signStr:"+signStr+"] [queryStr:"+queryStr+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
				return false;
			}
	
			JacksonManager jm = JacksonManager.getInstance();
			try {
				Map map =(Map)jm.jsonDecodeObject(content);

				Integer resultState = (Integer)map.get("state");
				
				if(resultState == null){
					logger.info("[乐嗨嗨解析登录返回数据] [失败:code is null] [queryStr:"+queryStr+"] [message:"+content+"] [username:"+username+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
					return false;
				}
				
				if(resultState != 1){
					logger.info("[乐嗨嗨解析登录返回数据] [失败:code状态码不是成功] [queryStr:"+queryStr+"] [message:"+content+"] [username:"+username+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
					return false;
				}
				logger.info("[乐嗨嗨最后验证] [成功] [username:"+username+"] [token:"+pwd+"] [content:"+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
				return true;
			}catch (Exception e) {
				logger.error("[乐嗨嗨解析登录返回数据] [失败] [username:"+username+"] [token:"+pwd+"] [content:"+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
				return false;
			}
		}catch(Throwable t){
			logger.error("[乐嗨嗨调用登陆接口] [失败:未知异常] [username："+username+"] [token:"+pwd+"]",t);
			return false;
		} 
	}
	
	public boolean checkLogin5(String username,String pwd){
		try {
			long startTime = System.currentTimeMillis();
			String time = pwd.split("#####")[0];
			String sign = pwd.split("#####")[1];
			String uid = username.split("MAIYOUUSER_")[1];
//			sign=MD5(“username=t315688&appkey=91bac46a9b70bd2db563cc483d443ba3&logintime=1395634100”)
			String signStr = "username="+uid+"&appkey=f8607a402949c5780a04003e0d85dcf0&logintime="+time;
			String mysign = StringUtil.hash(signStr);
			if(!mysign.equals(sign)){
				logger.info("[麦游解析登录返回数据] [失败:sign error] [username:"+username+"] [pwd:"+pwd+"] [signStr:"+signStr+"] [mysign:"+mysign+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
				return false;
			}
			logger.info("[麦游最后验证] [成功] [username:"+username+"] [pwd:"+pwd+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			return true;
		}catch(Throwable t){
			logger.error("[麦游调用登陆接口] [失败:未知异常] [username："+username+"] [token:"+pwd+"]",t);
			return false;
		} 
	}
	
	public boolean checkLogin4(String username,String pwd){
		try {
//			if(true) return false;
			
			long startTime = System.currentTimeMillis();
			String time = pwd.split("#####")[0];
			String sign = pwd.split("#####")[1];
			String uid = username.split("ANJIUUSER_")[1];
//			sign=MD5(“username=t315688&appkey=91bac46a9b70bd2db563cc483d443ba3&logintime=1395634100”)
			String signStr = "username="+uid+"&appkey=10233e9dbc6985ed6d85e9d590bc4795&logintime="+time;
			String mysign = StringUtil.hash(signStr);
			if(!mysign.equals(sign)){
				logger.info("[安久解析登录返回数据] [失败:sign error] [username:"+username+"] [pwd:"+pwd+"] [signStr:"+signStr+"] [mysign:"+mysign+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
				return false;
			}
			logger.info("[安久最后验证] [成功] [username:"+username+"] [pwd:"+pwd+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			return true;
		}catch(Throwable t){
			logger.error("[安久调用登陆接口] [失败:未知异常] [username："+username+"] [token:"+pwd+"]",t);
			return false;
		} 
	}
	
	public String checkLogin3(String username,String pwd){
		try {
			long startTime = System.currentTimeMillis();
			String appkey = "d5d01f6b47bb7e40afa126a1066278c8";
			String sign = StringUtil.hash(appkey+pwd);
			String queryStr = "tokenkey="+pwd+"&sign="+sign;
			String content = "";
			try {
				Map headers = new HashMap();
				byte bytes[] = HttpUtils.webPost(new java.net.URL("http://api.x7sy.com/user/test_check_login"), queryStr.getBytes("utf-8"), headers, 60000, 60000);
				String encoding = (String)headers.get(HttpUtils.ENCODING);
				Integer code = (Integer)headers.get(HttpUtils.Response_Code);
				String message = (String)headers.get(HttpUtils.Response_Message);
				content = new String(bytes,encoding);
				logger.info("[小7调用登录接口] [成功] [username:"+username+"] [queryStr:"+queryStr+"] [code:"+code+"] ["+message+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			} catch (Exception e) {
				logger.info("[小7调用登录接口] [失败] [username:"+username+"] [queryStr:"+queryStr+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
				return null;
			}
	
			JacksonManager jm = JacksonManager.getInstance();
			try {
				Map map =(Map)jm.jsonDecodeObject(content);

				Integer resultState = (Integer)map.get("errorno");
				String mess = (String)map.get("errormsg");
				
				if(resultState == null){
					logger.info("[小7解析登录返回数据] [失败:code is null] [queryStr:"+queryStr+"] [message:"+content+"] [error:"+mess+"] [username:"+username+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
					return null;
				}
//				0：成功；
//				-400：Sign 无效！；
//				-401：内部错误：不存在 guid 或不存在 mid 或不存在游戏 appkey！；
//				-403：内部错误：小号信息不存在！；
//				-404：内部错误：小号信息不对应！；
//				-405：内部错误：用户信息不对应！
//				-406：tokenkey 或者 sign 信息不完整！
				if(resultState != 0){
					logger.info("[小7解析登录返回数据] [失败:code状态码不是成功] [queryStr:"+queryStr+"] [message:"+content+"] [error:"+mess+"] [username:"+username+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
					return null;
				}
				
				HashMap data = (HashMap)map.get("data");
				if(data != null && data.size() > 0){
					String guid = (String)data.get("guid");
					if(guid != null){
						logger.info("[小7最后验证] [成功] [username:"+username+"] [guid:"+guid+"] [token:"+pwd+"] [返回数据:"+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
						return guid;
					}
				}
				
				logger.info("[小7最后验证] [失败] [username:"+username+"] [token:"+pwd+"] [content:"+content+"] [data:"+data+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
				return null;
			}catch (Exception e) {
				logger.error("[小7解析登录返回数据] [失败] [username:"+username+"] [token:"+pwd+"] [content:"+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
				return null;
			}
		}catch(Throwable t){
			logger.error("[小7调用登陆接口] [失败:未知异常] [username："+username+"] [token:"+pwd+"]",t);
			return null;
		} 
	}
	
}
