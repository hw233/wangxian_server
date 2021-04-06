package com.fy.gamegateway.thirdpartner.v8;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.fy.gamegateway.mieshi.waigua.NewLoginHandler;
import com.fy.gamegateway.tools.JacksonManager;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.text.StringUtil;

public class HaoDongClientService {

	private String checkUrl = "http://eagle.hoho666.com/user/verifyAccount";
	private String AppSecret = "5826287c518d4cba900174b039fc6eb1";
	Logger logger = NewLoginHandler.logger;
	private static HaoDongClientService self;
	public static HaoDongClientService getInstance(){
		if(self == null){
			self = new HaoDongClientService();
		}
		return self;
	}
	
//	-1	未知错误
//	0	请求失败
//	1	请求成功
//	2	游戏不存在
//	3	渠道不存在
//	4	渠道商不存在
//	5	渠道认证失败
//	6	用户不存在
//	8	认证失败
//	9	token 错误
//	10	金额错误
//	11	订单错误
//	12	签名错误
//	13	充值未开放
//	14	渠道游戏不存在
//	15	生成订单号失败
//	16	子渠道不存在
//	17	注册未开放
//	101	参数错误

	public boolean checkLogin(String username,String pwd){
		try {
			long startTime = System.currentTimeMillis();
			String uid = username.split("HAODONGUSER_")[1];
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
				logger.info("[浩动调用登录接口] [成功] [username:"+username+"] [signStr:"+signStr+"] [queryStr:"+queryStr+"] [code:"+code+"] ["+message+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			} catch (Exception e) {
				logger.info("[浩动调用登录接口] [失败] [username:"+username+"] [signStr:"+signStr+"] [queryStr:"+queryStr+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
				return false;
			}
	
			JacksonManager jm = JacksonManager.getInstance();
			try {
				Map map =(Map)jm.jsonDecodeObject(content);

				Integer resultState = (Integer)map.get("state");
				
				if(resultState == null){
					logger.info("[浩动解析登录返回数据] [失败:code is null] [queryStr:"+queryStr+"] [message:"+content+"] [username:"+username+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
					return false;
				}
				
				if(resultState != 1){
					logger.info("[浩动解析登录返回数据] [失败:code状态码不是成功] [queryStr:"+queryStr+"] [message:"+content+"] [username:"+username+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
					return false;
				}
				logger.info("[浩动最后验证] [成功] [username:"+username+"] [token:"+pwd+"] [content:"+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
				return true;
			}catch (Exception e) {
				logger.error("[浩动解析登录返回数据] [失败] [username:"+username+"] [token:"+pwd+"] [content:"+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
				return false;
			}
		}catch(Throwable t){
			logger.error("[浩动调用登陆接口] [失败:未知异常] [username："+username+"] [token:"+pwd+"]",t);
			return false;
		} 
	}
}
