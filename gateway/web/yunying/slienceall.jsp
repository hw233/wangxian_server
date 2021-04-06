<%@page import="com.xuanzhi.tools.authorize.User"%>
<%@page import="com.xuanzhi.tools.authorize.UserManager"%>
<%@page import="com.xuanzhi.tools.authorize.AuthorizeManager"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiSlientInfoManager"%>
<%@page import="com.xuanzhi.tools.text.ParamUtils"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiGatewaySubSystem"%>
<%@page import="java.io.File"%>
<%@page import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache"%>
<%@page import="com.xuanzhi.tools.cache.diskcache.DiskCache"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiServerInfoManager"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiServerInfo"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiPlayerInfo"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiPlayerInfoManager"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%
	boolean isDo = ParamUtils.getBooleanParameter(request, "isDo");
	
	if(isDo)
	{
		String usernames = ParamUtils.getParameter(request, "usernames", "");
		int hour = ParamUtils.getIntParameter(request, "silencetime", 0);
		int level = ParamUtils.getIntParameter(request, "silencelevel", 2);
		String reason = ParamUtils.getParameter(request, "reason", "");
		
		String[] userArray = usernames.split("\r*\n");
		
		for(int i=0; i<userArray.length; i++)
		{
			String username = userArray[i];
			
			AuthorizeManager authorizeManager = AuthorizeManager.getInstance();
			UserManager userManager = authorizeManager.getUserManager();
			String curUser = (String)session.getAttribute("authorize.username");
			User user = userManager.getUser(curUser);
			String opuser= user.getName();
			
			
			MieshiSlientInfoManager mieshiSlientInfoManager = MieshiSlientInfoManager.getInstance();
			
			try
			{
				mieshiSlientInfoManager.slienceAll(username, hour, reason, level, opuser);
				out.println("[沉默用户] [成功] [ok] [用户名:"+username+"]<br/>");
			}
			catch(Exception e)
			{
				out.println("[沉默用户] [失败] [出现未知异常] [用户名:"+username+"]<br/>");
				throw e;
				
			}
			
				
		
			
		}
	}

%>

<!DOCTYPE html>
<html>
<head>
    <title></title>
</head>
<body>
    <form action="" method="post">
       沉默用户: <textarea name="usernames"  cols="30" rows="10"></textarea>(可输入多个用户名，一行一个)<br/>
        <input value="true" name="isDo" type="hidden"/><br/>
      沉默时间: <input type="text" name="silencetime" />小时<br/>
      沉默级别  <select name="silencelevel" >
            <option value="1">普通沉默</option>
            <option value="2" selected>2级沉默</option>
        </select><br/>
     	原因:  <input type="text" name="reason" /> 
        <input type="submit" value="提交"/>
    </form>


</body>
</html>