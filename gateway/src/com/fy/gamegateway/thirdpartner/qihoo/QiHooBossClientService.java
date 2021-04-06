package com.fy.gamegateway.thirdpartner.qihoo;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.fy.gamegateway.mieshi.server.MieshiGatewaySubSystem;
import com.fy.gamegateway.tools.JacksonManager;
import com.xuanzhi.tools.servlet.HttpsUtils;



public class QiHooBossClientService {
	
	static Logger logger = Logger.getLogger(MieshiGatewaySubSystem.class);

	public static final String CHAR_SET = "UTF-8";
	public static String baseURL = "https://openapi.360.cn/";
	public static String authorizationURL = baseURL + "oauth2/authorize";
	public static String accessTokenURL = baseURL + "oauth2/access_token";
	public static String getInfoURL = baseURL + "user/me";
	public static String format = "json";
	public static  String appId = "200120241";
	public static  String consumerKey = "455ff7448d782ec75a5ada99b2296cdc";
	public static  String consumerSecret = "b45e94711e0232d7f66c1f916a0c755b";
	public static String redirectUri = null;
	
	
	private static QiHooBossClientService self;
	public static QiHooBossClientService getInstance(){
		if(self != null){
			return self;
		}
		self = new QiHooBossClientService();
		return self;
	}
	
	
	public QihooLoginResult login(String platform, String uid, String password,String channel){
		long startTime = System.currentTimeMillis();
		
		if(!StringUtils.isEmpty(channel))
		{
			String oldAppid = "200120241";
			String oldConsumerKey = "455ff7448d782ec75a5ada99b2296cdc";
			String oldConsumerSecret = "b45e94711e0232d7f66c1f916a0c755b";
			
			if(channel.toLowerCase().contains("360newsdk"))
			{
				appId = "200244826";
				consumerKey = "27d0f6f656017c38ae8df11b3b66a2b4";
				consumerSecret = "e13fc0b7eade2270747973c5248fb24c";
			}
			else
			{
				appId = oldAppid;
				consumerKey = oldConsumerKey;
				consumerSecret = oldConsumerSecret;
			}
		}
		
		
		//拿accesstoken 新的客户端跳过这一步
		StringBuffer subfferUrl = new StringBuffer(accessTokenURL).append("?client_id=").append(consumerKey)
				.append("&client_secret=").append(consumerSecret).append("&grant_type=").append("authorization_code")
				.append("&code=").append(password).append("&redirect_uri=oob");
		if(!StringUtils.isEmpty(channel))
		{
			if(channel.toLowerCase().contains("360newsdk"))
			{
				subfferUrl.append("&scope=basic");
			}
		}
				
				Map headers = new HashMap();
				try {
					URL url = new URL(subfferUrl.toString());
					byte[] resContent = new byte[0];
					String resEncoding = "";
					String content = "";
					Object obj  = null;
					if(!channel.toLowerCase().contains("360newsdk"))
					{
						resContent = HttpsUtils.webGet(url, headers, 30000, 30000);
						resEncoding = (String)headers.get("Encoding");
						content = new String(resContent,resEncoding);
						obj = JacksonManager.getInstance().jsonDecodeObject(content);
					}
					else
					{
						obj = password;
					}
						
				
					if(obj != null ) {
						String accessToken = "";
						if(obj instanceof Map)
						{
							Map map = (Map) obj;
							accessToken = (String)map.get("access_token");
						}
						else
						{
							accessToken = password;
						}
						
						if( accessToken!= null) {
							//去拿userinfo
							if(logger.isInfoEnabled())
							{
								logger.info("[360SDK登陆] [获取token] [成功] [platform:"+platform+"] [uid:"+uid+"] [code:"+password+"] [url:"+subfferUrl.toString()+"] [resEncoding:"+resEncoding+"] [content:"+content+"] [appid:"+appId+"] [consumerKey:"+consumerKey+"] [consumerSecret:"+consumerSecret+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms] ");
							}
							
							String username = null;
							if((username = getQiHoo360UserInfo(accessToken,channel)) != null)
							{
								QihooLoginResult qr = new QihooLoginResult();
								qr.username = username;
								qr.password = accessToken;
								if(logger.isInfoEnabled())
									logger.info("[360SDK登陆] [成功] [platform:"+platform+"] [uid:"+uid+"] [code:"+password+"] [url:"+subfferUrl.toString()+"] [resEncoding:"+resEncoding+"] [content:"+content+"] [appid:"+appId+"] [consumerKey:"+consumerKey+"] [consumerSecret:"+consumerSecret+"] [username:"+username+"] [qr.password:"+qr.password+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms] ");
								return qr;
							}
							else
							{
								QihooLoginResult qr = new QihooLoginResult();
								qr.username = username;
								qr.password = accessToken;
								if(logger.isInfoEnabled())
									logger.info("[360SDK登陆] [失败:没有获得username] [platform:"+platform+"] [uid:"+uid+"] [code:"+password+"] [url:"+subfferUrl.toString()+"] [resEncoding:"+resEncoding+"] [content:"+content+"] [appid:"+appId+"] [consumerKey:"+consumerKey+"] [consumerSecret:"+consumerSecret+"] [username:"+username+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms] ");
								return qr;
							}
							
						
						}
						else
						{
							QihooLoginResult qr = new QihooLoginResult();
							qr.username = null;
							if(logger.isInfoEnabled())
								logger.info("[360SDK登陆] [失败:没有获得token] [platform:"+platform+"] [uid:"+uid+"] [code:"+password+"] [url:"+subfferUrl.toString()+"] [resEncoding:"+resEncoding+"] [content:"+content+"] [appid:"+appId+"] [consumerKey:"+consumerKey+"] [consumerSecret:"+consumerSecret+"] [username:--] [cost:"+(System.currentTimeMillis()-startTime)+"ms] ");
							return qr;
						}
					}
					else
					{
						QihooLoginResult qr = new QihooLoginResult();
						qr.username = null;
						if(logger.isInfoEnabled())
							logger.info("[360SDK登陆] [失败:无法成功解析数据] [platform:"+platform+"] [uid:"+uid+"] [code:"+password+"] [url:"+subfferUrl.toString()+"] [resEncoding:"+resEncoding+"] [content:"+content+"] [appid:"+appId+"] [consumerKey:"+consumerKey+"] [consumerSecret:"+consumerSecret+"] [username:--] [cost:"+(System.currentTimeMillis()-startTime)+"ms] ");
						return qr;
					}
				} catch (Exception e) {
					QihooLoginResult qr = new QihooLoginResult();
					qr.username = null;
					logger.error("[360SDK登陆] [失败:出现异常] [platform:"+platform+"] [uid:"+uid+"] [code:"+password+"] [url:"+subfferUrl.toString()+"] [resEncoding:--] [content:--] [appid:"+appId+"] [consumerKey:"+consumerKey+"] [consumerSecret:"+consumerSecret+"] [username:--] [cost:"+(System.currentTimeMillis()-startTime)+"ms] ",e);
					return qr;
				}
	}
	
	
	
	   /**
  * 随机1个数。从begin开始到end结束。包括begin。不包括end
  * @param begin
  * @param end
  * @return
  */
 public static int getRandomInt(int begin,int end) {
 	Random random = new Random();
 	int i = random.nextInt(end);
 	if(i < begin){
 		i = getRandomInt(begin,end);
 	}
 	return i;
	}
 
	public static String rundomten() {
		String password = "";
		for(int i = 0;i < 10;i++) {
			password += getRandomInt(0,10);
		}
//		System.out.println(password);
		return password;
	}
	
	public static String getQiHoo360UserInfo(String access_token,String channel) {
		long startTime = System.currentTimeMillis();
		
		StringBuffer subfferUrl = new StringBuffer(getInfoURL).append("?access_token=").append(access_token);
		Map headers = new HashMap();
		try {
			URL url = new URL(subfferUrl.toString());
			byte[] resContent =HttpsUtils.webGet(url, headers, 30000, 30000);
			String resEncoding = (String)headers.get("Encoding");
			String content = new String(resContent,resEncoding);
			Object obj = JacksonManager.getInstance().jsonDecodeObject(content);
			if(obj != null ){
				Map rMap = (Map)obj;
				if(rMap.get("id") != null) {
					if(logger.isInfoEnabled())
						logger.info("[360SDK登陆] [获取用户信息] [成功] [url:"+subfferUrl.toString()+"] [resEncoding:"+resEncoding+"] [content:"+content+"] [appid:"+appId+"] [consumerKey:"+consumerKey+"] [consumerSecret:"+consumerSecret+"] [accessToken:"+access_token+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms] ");
					return rMap.get("id") + "";
				} else {
					if(logger.isInfoEnabled())
						logger.info("[360SDK登陆] [获取用户信息] [失败:无法获取userid] [url:"+subfferUrl.toString()+"] [resEncoding:"+resEncoding+"] [content:"+content+"] [appid:"+appId+"] [consumerKey:"+consumerKey+"] [consumerSecret:"+consumerSecret+"] [accessToken:"+access_token+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms] ");
				}
			} else {
				if(logger.isInfoEnabled())
					logger.info("[360SDK登陆] [获取用户信息] [失败:无法解析返回数据] [url:"+subfferUrl.toString()+"] [resEncoding:"+resEncoding+"] [content:"+content+"] [appid:"+appId+"] [consumerKey:"+consumerKey+"] [consumerSecret:"+consumerSecret+"] [accessToken:"+access_token+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms] ");
			}
		} catch (Exception e) {
			logger.error("[360SDK登陆] [获取用户信息] [失败:出现异常] [url:"+subfferUrl.toString()+"] [resEncoding:--] [content:--] [appid:"+appId+"] [consumerKey:"+consumerKey+"] [consumerSecret:"+consumerSecret+"] [accessToken:"+access_token+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		return null;
	}
}
