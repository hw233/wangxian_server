package com.fy.gamegateway.thirdpartner.xmwan;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.fy.boss.authorize.exception.UserNameInValidException;
import com.fy.boss.authorize.exception.UsernameAlreadyExistsException;
import com.fy.boss.authorize.model.Passport;
import com.fy.gamegateway.mieshi.server.MieshiGatewaySubSystem;
import com.fy.gamegateway.mieshi.waigua.LoginEntity;
import com.fy.gamegateway.tools.JacksonManager;
import com.xuanzhi.tools.servlet.HttpUtils;

public class XmWanClientService {
	
	public static Logger logger = Logger.getLogger(XmWanClientService.class);
	
	private static XmWanClientService self;
	
	public static XmWanClientService getInstance() {
		if(self == null) {
			self = new XmWanClientService();
		}
		return self;
	}

	private String client_id = "275b468748cfb0c1a30032ea8d08f2fd";
	private String client_secret = "b4c47c87dbc8d46532aefc43a9afdd59";
	private String grant_type = "authorization_code";
	private	String access_token_url = "http://open.xmwan.com/v2/oauth2/access_token";
	private String user_info_url = "http://open.xmwan.com/v2/users/me";
	
	
	public String get_access_token(String username){
		LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();
		String guid = username.split("XMWANSDKUSER_")[1];
		params.put("client_id",client_id);
		params.put("client_secret", client_secret);
		params.put("grant_type",grant_type);
		params.put("code",guid);
		
		String encoding = "";
		Integer code = -1;
		String message = "";
		String result = "";
		String content = "";
		HashMap headers = new HashMap();
		try {
			content = buildParams(params);
			byte bytes[] = HttpUtils.webPost(new java.net.URL(access_token_url), content.getBytes(), headers, 5000, 10000);
			
			encoding = (String)headers.get(HttpUtils.ENCODING);
			code = (Integer)headers.get(HttpUtils.Response_Code);
			message = (String)headers.get(HttpUtils.Response_Message);
			result = new String(bytes,encoding);
			
			JacksonManager jm = JacksonManager.getInstance();
			Map map =(Map)jm.jsonDecodeObject(result);
			
			String access_token = (String)map.get("access_token");
			String refresh_token = (String)map.get("refresh_token");
			Integer expires_in = (Integer)map.get("expires_in");
			String xmw_open_id = (String)map.get("xmw_open_id");
			
			if(logger.isInfoEnabled()){
				logger.warn("[熊猫玩获取access_token] [成功] [xmw_open_id:"+xmw_open_id+"] [access_token:"+access_token+"] [refresh_token:"+refresh_token+"] [expires_in:"+expires_in.intValue()+"] [encoding:"+(encoding==null?"":encoding)+"] [code:"+(code==null?"":code)+"] [message:"+(message==null?"":message)+"] [content:"+(content==null?"":content)+"] [result:"+(result==null?"":result)+"]");
			}
			return xmw_open_id;
		} catch (Exception e){
			e.printStackTrace();
			 encoding = (String)headers.get(HttpUtils.ENCODING);
			 code = (Integer)headers.get(HttpUtils.Response_Code);
			 message = (String)headers.get(HttpUtils.Response_Message);
		}
		logger.warn("[熊猫玩获取access_token] [错误] [encoding:"+(encoding==null?"":encoding)+"] [code:"+(code==null?"":code)+"] [message:"+(message==null?"":message)+"] [content:"+(content==null?"":content)+"] [result:"+(result==null?"":result)+"]");
		return null;
	}
	
	public String[] handle_xmWan_login(String authorization_code,LoginEntity entity){
		String access_token = get_access_token(authorization_code);
		if(access_token != null){
			HashMap headers = new HashMap();
			String xmw_open_id = "";
			try {
				URL url = new URL(user_info_url+"?access_token="+access_token);
				byte bytes [] =  HttpUtils.webGet(url, headers, 5000, 10000);
				
				String encoding = (String)headers.get(HttpUtils.ENCODING);
				Integer code = (Integer)headers.get(HttpUtils.Response_Code);
				String message = (String)headers.get(HttpUtils.Response_Message);
				String result = new String(bytes,encoding);
				
				JacksonManager jm = JacksonManager.getInstance();
				Map map =(Map)jm.jsonDecodeObject(result);
				
				xmw_open_id = (String)map.get("xmw_open_id");
				String nickname = (String)map.get("nickname");
				String avatar = (String)map.get("avatar");
				String gender  = String.valueOf(map.get("gender"));
				if(xmw_open_id == null || xmw_open_id.isEmpty()){
					if(logger.isInfoEnabled()){
						logger.warn("[熊猫玩处理玩家登录] [错误:xmw_open_id==null] [xmw_open_id:"+xmw_open_id+"] [access_token:"+access_token+"] [nickname:"+nickname+"] [avatar:"+avatar+"] [gender:"+gender+"] [code:"+code+"] [message:"+message+"] [result:"+result+"]");
					}
					return null;
				}else{
					Passport passport = null;
					try{
						passport = MieshiGatewaySubSystem.getInstance().getBossClientService().getPassportByUserName("XMWANSDKUSER_"+xmw_open_id);
					}catch(Exception e){
						e.printStackTrace();
						logger.warn("[熊猫玩处理玩家登录] [错误1:UsernameNOTExistsException] [xmw_open_id:"+xmw_open_id+"] [access_token:"+access_token+"]",e);
					}
					boolean isNewUser = false;
					if(passport == null){
						isNewUser= true;
						passport = new Passport();
						passport.setUserName("XMWANSDKUSER_"+xmw_open_id);
						passport.setPassWd(access_token);
						passport.setFromWhere("熊猫玩SDK");
						passport.setRegisterChannel("YOUXIQUNSDK_MIESHI");
						passport.setLastLoginChannel("YOUXIQUNSDK_MIESHI");
						passport.setLastLoginDate(new Date());
						try {
							passport = MieshiGatewaySubSystem.getInstance().getBossClientService().register("XMWANSDKUSER_"+xmw_open_id, access_token, entity.clientId, "", entity.userType, "YOUXIQUNSDK_MIESHI", "", entity.platform, entity.phoneType, entity.networkMode);
						} catch (UsernameAlreadyExistsException e1) {
							e1.printStackTrace();
						} catch (UserNameInValidException e1) {
							e1.printStackTrace();
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						
						if(passport == null){
							logger.warn("[熊猫玩处理玩家登录] [错误2:注册失败] [xmw_open_id:"+xmw_open_id+"] [access_token:"+access_token+"]");
							return null;
						}
					}else{
						passport.setUserName("XMWANSDKUSER_"+xmw_open_id);
						passport.setLastLoginDate(new Date());
						passport.setPassWd(access_token);
						MieshiGatewaySubSystem.getInstance().getBossClientService().update(passport);
						
						try {
//							MieshiGatewaySubSystem.getInstance().getBossClientService().login("XMWANSDKUSER_"+xmw_open_id, access_token, entity.clientId, entity.channel, entity.platform,entity.phoneType, entity.networkMode);
						} catch (Exception e) {
							logger.error("[登录更新Passport信息时发生异常] ["+passport.getUserName()+"]", e);
						}
					}
					if(logger.isInfoEnabled()){
						logger.warn("[熊猫玩处理玩家登录] [成功] [新用户:"+isNewUser+"] [xmw_open_id:"+xmw_open_id+"] [access_token:"+access_token+"] [nickname:"+nickname+"] [avatar:"+avatar+"] [gender:"+gender+"] [code:"+code+"] [message:"+message+"] [result:"+result+"]");
					}
					return new String[]{access_token,xmw_open_id,(nickname==null?"":nickname),(avatar==null?"":avatar),(gender==null?"":gender)};
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
				logger.warn("[熊猫玩处理玩家登录] [错误:MalformedURLException] [xmw_open_id:"+xmw_open_id+"] [access_token:"+access_token+"]",e);
			} catch (IOException e) {
				e.printStackTrace();
				logger.warn("[熊猫玩处理玩家登录] [错误:IOException] [xmw_open_id:"+xmw_open_id+"] [access_token:"+access_token+"]",e);
			} catch(Exception e){
				e.printStackTrace();
				logger.warn("[熊猫玩处理玩家登录] [错误:Exception] [xmw_open_id:"+xmw_open_id+"] [access_token:"+access_token+"]",e);
			}
		}
		return null;
	}
	
	private String buildParams(Map<String,String> params){
		String[] key = params.keySet().toArray(new String[params.size()]);

		StringBuffer sb = new StringBuffer();
		for (String k : key) {
			sb.append(k);
			sb.append("=");
			sb.append(params.get(k));
			sb.append("&");
		}
		
		return sb.toString().substring(0, sb.length() - 1);
	}
	
}
