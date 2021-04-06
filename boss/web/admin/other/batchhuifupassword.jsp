<%@page import="com.fy.boss.transport.BossServerService"%>
<%@page import="com.xuanzhi.tools.text.ParamUtils"%>
<%@page import="com.xuanzhi.tools.text.StringUtil"%>
<%@page import="com.fy.boss.authorize.model.Passport"%>
<%@page import="com.fy.boss.authorize.service.PassportManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<% 
		String usernamesstr = ParamUtils.getParameter(request, "username", "");
		boolean isDo = ParamUtils.getBooleanParameter(request, "isDO");
		
		if(isDo)
		{
			String[] usernames =  usernamesstr.split("\r*\n");
		
			for(String userinfo : usernames)
			{
				String[] users = userinfo.split("[\\s]+");
				String username = users[0];
				if(username != null)
				{
					username = username.trim();
				}
				
				String pass = users[1];
				
				if(pass != null)
				{
					pass = pass.trim();
				}
				
				
				
				PassportManager passportManager = PassportManager.getInstance();
				Passport p = passportManager.getPassport(username);
				if(p != null)
				{
					String opass = p.getPassWd();
					p.setPassWd(pass);
					
					passportManager.update(p);
					BossServerService.logger.warn("[恢复用户密码] [成功] ["+username+"] ["+pass+"] [老密码:"+opass+"]");
					out.println("[更新passport密码] [id:"+p.getId()+"] [username:"+p.getUserName()+"] [老密码:"+opass+"] [新密码:"+p.getPassWd()+"]<br/>");
				}
				else
				{
					BossServerService.logger.warn("[恢复用户密码] [失败] ["+username+"] ["+pass+"]");
					out.println("[更新passport密码] [没有查到对应的passport] [username:"+username+"]<br/>");
				}
			}
		}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>

</head>
<body>
	<h2></h2>
	<form method="post">
		<input type="hidden" name="isDO" value="true" />
		用户名:<textarea rows="20" cols="30" name="username" value="<%=usernamesstr%>"></textarea><br>
		<input type="submit" value="提交">
	</form>
</body>
</html>	