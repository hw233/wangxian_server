package com.tencent.java.sdk;

public class AppConfig {
	static public String appid = "900000449";
	static public String appkey= "mgwXU2Vy3nqKteTB";
	
	static public String platformid = "1002";
	static public String terminaltype = "0";
	
	//static public String OpenPlatformURL = "http://openapi.sp0309.3g.qq.com";
	static public String OpenPlatformURL = "http://openapi.3g.qq.com";
	
	//actions
	//验证openid, openkey
	static public String verify = "/v3/mobile/login/verify";
	//查余额
	static public String balance = "/v3/mobile/check_balance";
	//扣费
	static public String charge = "/v3/mobile/charge";
	//魔钻
	static public String magic = "/v3/mobile/people/is_gamevip/@me/@self";
	//超Q
	static public String vip = "/v3/mobile/people/is_sqq/@me/@self";
	//个人信息
	static public String userinfo = "/v3/mobile/people/@me/@self";
	//好友列表
	static public String friends = "/v3/mobile/people/@me/@friends";
}
