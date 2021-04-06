package com.fy.gamegateway.thirdpartner.u8;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.fy.gamegateway.mieshi.waigua.NewLoginHandler;
import com.fy.gamegateway.tools.JacksonManager;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.text.StringUtil;

public class U8ClientService {

	private String checkUrl = "http://tysdk.maowan.com/user/verifyAccount";
	private String AppSecret = "d78a9de975a42676ebe5bdc18ede97dc";
	Logger logger = NewLoginHandler.logger;
	private static U8ClientService self;
	public static U8ClientService getInstance(){
		if(self == null){
			self = new U8ClientService();
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
			String uid = username.split("U8USER_")[1];
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
				logger.info("[u8调用登录接口] [成功] [username:"+username+"] [queryStr:"+queryStr+"] [code:"+code+"] ["+message+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			} catch (Exception e) {
				logger.info("[u8调用登录接口] [失败] [username:"+username+"] [queryStr:"+queryStr+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
				return false;
			}
	
			JacksonManager jm = JacksonManager.getInstance();
			try {
				Map map =(Map)jm.jsonDecodeObject(content);

				Integer resultState = (Integer)map.get("state");
				
				if(resultState == null){
					logger.info("[u8解析登录返回数据] [失败:code is null] [queryStr:"+queryStr+"] [message:"+content+"] [username:"+username+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
					return false;
				}
				
				if(resultState != 1){
					logger.info("[u8解析登录返回数据] [失败:code状态码不是成功] [queryStr:"+queryStr+"] [message:"+content+"] [username:"+username+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
					return false;
				}
				logger.info("[u8最后验证] [成功] [username:"+username+"] [token:"+pwd+"] [content:"+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
				return true;
			}catch (Exception e) {
				logger.error("[u8解析登录返回数据] [失败] [username:"+username+"] [token:"+pwd+"] [content:"+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
				return false;
			}
		}catch(Throwable t){
			logger.error("[u8调用登陆接口] [失败:未知异常] [username："+username+"] [token:"+pwd+"]",t);
			return false;
		} 
	}
}
