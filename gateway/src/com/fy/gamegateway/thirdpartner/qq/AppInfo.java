package com.fy.gamegateway.thirdpartner.qq;

public class AppInfo
{
		//================================================================
		// 以下为腾讯平台分配的APP信息，建议把这些信息改为从配置文件里面读取出来，方便以后维护
		//================================================================
		
		//应用id
	public static final int APP_ID = 207;     
		//public static final int APP_ID = 1;     
		
		//平台的聚道标识，1000手机家园，1001手机空间
		//public static final String PLATFORM_ID = "1001"; 
	public static final String PLATFORM_ID = "1003";  //正式
		
		//应用密钥
	//public static final String APP_KEY = "GDdmIQH6jhtmLUypg82g"; 
public static final String APP_KEY = "NBAyR40Mi2BYg288Pz91";  //正式
		
		//应用的key
	//public static final String APP_SECRECT = "MCD8BKwGdgPHvAuvgvz4EQpqDAtx89grbuNMRd7Eh98"; 
public static final String APP_SECRECT = "mgwXU2Vy3nqKteTB";  //正式
		
		//api服务器域名,正式环境http://openapi.3g.qq.com,测试环境http://openapi.sp0309.3g.qq.com
	//public static final String API_URL = "http://openapi.sp0309.3g.qq.com"; 
		public static final String API_URL = "http://openapi.3g.qq.com";  //正式
		
		//api服务domain,正式环境http://openapi.3g.qq.com,测试环境http://openapi.sp0309.3g.qq.com
		//public static final String APP_DOMAIN = "http://openapi.sp0309.3g.qq.com";
		public static final String APP_DOMAIN = "http://openapi.3g.qq.com";//正式
		
		// termialType决定了登录界面的样式
		// 0是andriod，1是ios，2是wap，3是symbin，4是wap2.0 平台默认是wap。		
		public static final String TERMINAL_TYPE  = "2";
		
		//应用的回调domain，用于登录和扣费回调地址。需要换成你APP部署后的真实地址
		public static final String CB_DOMAIN = "http://localhost:80/api";
		
		public static String  URL_CONNECT	 		 = API_URL + "/oauth/connect";
		
		//使用OAuth协议登录时，应用提供的在用户输入密码验证后，跳回应用的url
		public static String  URL_LOGIN_CALLBACK	  = CB_DOMAIN+"/login" ;
			
		public static String  URL_GET_REQUESET_TOKEN  = API_URL + "/oauth/request_token";
		
		public static String  URL_AUTHORIZE  = API_URL+ "/oauth/authorize";
		
		public static String  URL_GET_ACCESS_TOKEN   = API_URL+ "/oauth/access_token";
		
		
}