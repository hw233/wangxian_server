
<%@page import="com.fy.gamegateway.mieshi.waigua.ClientAuthorization"%><%@page import="com.fy.boss.authorize.model.Passport"%>
<%@page import="com.fy.boss.client.BossClientService"%>
<%@page import="com.fy.gamegateway.mieshi.waigua.AuthorizeManager"%>
<%@page import="com.fy.gamegateway.mieshi.waigua.ClientEntity"%>
<%@page import="net.sf.json.JSONArray"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiPlayerInfo"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiPlayerInfoManager"%>
<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%
MieshiPlayerInfoManager mpm = MieshiPlayerInfoManager.getInstance();
String username = request.getParameter("userName");
Map result = new HashMap();
if(username != null && !username.isEmpty()){
	Passport passport = BossClientService.getInstance().getPassportByUserName(username);
	if(passport != null){
		ClientEntity clientEntity = AuthorizeManager.getInstance().getClientEntity(passport.getLastLoginClientId());
		String ip = "";
		try{
			ip = passport.getLastLoginIp().replace("/","");
			ip = ip.split(":")[0];
		}catch(Exception e){
		}
		//boss获取ip为空，再通过授权表里查
		//优先查当前clientID登录的授权信息,其次上次登录时间最久的授权信息
		if(ip == null || ip.trim().length()==0 || ip.contains("UUID")){
			try {
				List<ClientAuthorization>  caList = AuthorizeManager.getInstance().getUsernameClientAuthorization(username);
				ClientAuthorization oldLoginAuthor = null;
				ClientAuthorization currAuthor = null;
				for (ClientAuthorization ca : caList) {
					if(ca == null) continue;
					if (ca.getAuthorizeType() == 0) {
						if(oldLoginAuthor == null){
							oldLoginAuthor = ca;
						}else if(oldLoginAuthor.getLastLoginTime() > ca.getLastLoginTime()){
							oldLoginAuthor = ca;
						}
					}
					if(ca.getClientID().equals(passport.getLastLoginClientId())){
						currAuthor = ca;
					}
				}
				
				if(oldLoginAuthor != null){
					ip = oldLoginAuthor.getIp();
				}
				if(currAuthor != null){
					ip = currAuthor.getIp();
				}
				
				try{
					if(ip != null && ip.trim().length() > 0){
						ip = ip.replace("/","");
						ip = ip.split(":")[0];
					}
				}catch(Exception e){
				}
			} catch (Exception e) {
				out.print("[获取授权信息] [异常] ["+username+"]"+e);
			}
		}
		result.put("ip",ip);
		if(clientEntity != null){
			result.put("macId",clientEntity.getMac());
		}
	}else{
		out.print("passport不存在，请输入正确的账号信息");
	}
}else{
	out.print("账号不能为空");
}
if(result.size() > 0){
	out.print(JSONArray.fromObject(result).toString());
}
%>

