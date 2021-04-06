package com.fy.gamegateway.thirdpartner.dcn;

import java.security.NoSuchAlgorithmException;


import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.downjoy.common.crypto.MessageDigest;
import com.downjoy.common.ui.DownjoyOpenAPIManager;
import com.xuanzhi.tools.text.XmlUtil;




public class DcnBossClientService {

	static Logger logger = Logger.getLogger(DcnBossClientService.class);

	
	//APP_ID：127
	public static int APP_ID = 127;
	public static String SECRET_KEY = "a7dI6dKs";
	public static String TEST_ADDRESS = "connect.d.cn";
	
	//SECRET_KEY：a7dI6dKs
	//测试地址：124.193.202.18:8899
	
	private static DcnBossClientService self;
	public static DcnBossClientService getInstance(){
		if(self != null){
			return self;
		}
		self = new DcnBossClientService();
		return self;
	}
	
	private DownjoyOpenAPIManager manager=null; // 当乐开放接口管理器

	private MessageDigest md=null;

	    
	protected DcnBossClientService(){
		manager=new DownjoyOpenAPIManager(APP_ID, SECRET_KEY, TEST_ADDRESS, 10000);
	    try {
			md = MessageDigest.getInstance(MessageDigest.ALGORITHM_SHA_256);
		} catch (NoSuchAlgorithmException e) {
			logger.error("当乐接口初始化失败",e);
		}
	}
	
	
	public static String getResponseDescription(int status){
		if(status == 0) return "验证成功";
		if(status == 101) return "调用当乐接口，未知错误";
		if(status == 201) return "调用当乐接口，call_id错误";
		if(status == 202) return "调用当乐接口，sig计算错误或者密码错误";
		if(status == 203) return "调用当乐接口，api_key错误";
		if(status == 204) return "调用当乐接口，未知参数错误";
		if(status == 302) return "调用当乐接口，mid或username错误（2选1）";
		if(status == 601) return "调用当乐接口，无此用户";
		if(status == 1000) return "当乐接口返回的XML解析出错";
		if(status == 1001) return "调用当乐接口出现异常";
		if(status == 1002) return "登录失败，密码不匹配";

		return "未知响应码";
	}
	
	public DCNLoginResult login(String platform,String username,String password,boolean usernameIsMid){
		 long startTime = System.currentTimeMillis();
		 
		 Long mid=null; // 乐号
		 String userName = null;
		 String sha256Pwd = md.getDigest(password); // 加密后密码
		 
		 if(usernameIsMid){
			 try{
				 mid = Long.parseLong(username.trim());
			 }catch(NumberFormatException e){
				 	DCNLoginResult r = new DCNLoginResult();
					r.status = 1002;
					r.message = getResponseDescription(r.status);
					r.djtk = ""; 
			 		logger.info("[调用登录接口] [usernameIsMid:"+usernameIsMid+"] ["+platform+"] [失败] ["+TEST_ADDRESS+"] [username:"+username+"] [password:"+password+"] [--] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
			 		return r;
			 }
		 }else{
			 userName = username;
		 }
		 
	     String xml = null;
	     try{
	    	 xml = manager.login(mid, userName, sha256Pwd); // 调用登录方法（乐号或用户名任意填写一个，没有就设为null即可）。
	    	 String content = xml.replaceAll("\r\n", "");
	    	 logger.info("[调用登录接口] [usernameIsMid:"+usernameIsMid+"] ["+platform+"] [成功] ["+TEST_ADDRESS+"] [username:"+username+"] [password:"+password+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
		 		
	     }catch(Exception e){
	    	DCNLoginResult r = new DCNLoginResult();
			r.status = 1001;
			r.message = getResponseDescription(r.status);
			r.djtk = ""; 
	 		logger.info("[调用登录接口] [usernameIsMid:"+usernameIsMid+"] ["+platform+"] [失败] ["+TEST_ADDRESS+"] [username:"+username+"] [password:"+password+"] [--] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
	 		return r;
	     }
	     
	     try {
			Document dom = XmlUtil.loadString(xml);
			Element root = dom.getDocumentElement();
			int status = XmlUtil.getAttributeAsInteger(root, "status", 0);
			DCNLoginResult r = new DCNLoginResult();
			if(status == 0){
				Element ele = XmlUtil.getChildByName(root, "user");
				long mmid = XmlUtil.getValueAsLong(XmlUtil.getChildByName(ele, "mid"),0L);
				
				String name = XmlUtil.getValueAsString(XmlUtil.getChildByName(ele, "name"), "",null);
				String nickname = XmlUtil.getValueAsString(XmlUtil.getChildByName(ele, "nickname"), "",null);
				int points = XmlUtil.getValueAsInteger(XmlUtil.getChildByName(ele, "point"), 0);
				int level = XmlUtil.getValueAsInteger(XmlUtil.getChildByName(ele, "level"), 0);
				int gender = XmlUtil.getValueAsInteger(XmlUtil.getChildByName(ele, "gender"), 0);
				String djtk = XmlUtil.getValueAsString(XmlUtil.getChildByName(ele, "djtk"), "",null);
				
				r.status = status;
				r.message = getResponseDescription(status);
				r.mid = mmid;
				r.name = name;
				r.nickname = nickname;
				r.points = points;
				r.level = level;
				r.gender = gender;
				r.djtk = djtk;
			}else{
				
				r.status = status;
				r.message = getResponseDescription(status);
			}
			if(usernameIsMid){
				logger.info("[解析登录返回数据] [usernameIsMid:"+usernameIsMid+"] ["+platform+"] [成功] [username:"+username+"] [password:"+password+"] ["+TEST_ADDRESS+"] [status:"+r.status+"] [message:"+getResponseDescription(r.status)+"] [accessToken:"+r.djtk+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
				return r;
			}else{
				try{
					Long.parseLong(username.trim());
				}catch(Exception e){
					logger.info("[解析登录返回数据] [usernameIsMid:"+usernameIsMid+"] ["+platform+"] [成功] [username:"+username+"] [password:"+password+"] ["+TEST_ADDRESS+"] [status:"+r.status+"] [message:"+getResponseDescription(r.status)+"] [accessToken:"+r.djtk+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
					return r;
				}
				
				if(status == 601 || status == 202){
					logger.info("[解析登录返回数据] [usernameIsMid:"+usernameIsMid+"] ["+platform+"] [尝试用乐号登录] [username:"+username+"] [password:"+password+"] ["+TEST_ADDRESS+"] [status:"+r.status+"] [message:"+getResponseDescription(r.status)+"] [accessToken:"+r.djtk+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
					return login(platform,username,password,true);
				}else{
					logger.info("[解析登录返回数据] [usernameIsMid:"+usernameIsMid+"] ["+platform+"] [成功] [username:"+username+"] [password:"+password+"] ["+TEST_ADDRESS+"] [status:"+r.status+"] [message:"+getResponseDescription(r.status)+"] [accessToken:"+r.djtk+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
					return r;
				}
			}
		} catch (Exception e) {
			DCNLoginResult r = new DCNLoginResult();
			r.status = 1000;
			r.message = getResponseDescription(r.status);
			r.djtk = "";
			
			logger.info("[解析登录返回数据] [usernameIsMid:"+usernameIsMid+"] ["+platform+"] [失败] [username:"+username+"] [password:"+password+"] ["+TEST_ADDRESS+"] [status:"+r.status+"] [message:"+getResponseDescription(r.status)+"] [accessToken:"+r.djtk+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
			return r;
		}

	}
}
