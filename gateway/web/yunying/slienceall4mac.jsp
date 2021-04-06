<%@page import="com.fy.gamegateway.mieshi.waigua.ClientAuthorization"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.gamegateway.mieshi.waigua.AuthorizeManager"%>
<%@page import="com.fy.gamegateway.mieshi.waigua.ClientEntity"%>
<%@page import="java.util.List"%>
<%@page import="com.xuanzhi.tools.authorize.User"%>
<%@page import="com.xuanzhi.tools.authorize.UserManager"%>
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
		String macs = ParamUtils.getParameter(request, "macs", "");
		
		int hour = ParamUtils.getIntParameter(request, "silencetime", 0);
		int level = ParamUtils.getIntParameter(request, "silencelevel", 2);
		String reason = ParamUtils.getParameter(request, "reason", "");
		
		String[] macarray = macs.split("\r*\n");
		
		for(int i=0; i<macarray.length; i++)
		{
			//获取匹配mac地址的所有用户名
			List<ClientEntity> list = AuthorizeManager.getInstance().em_ce.query(ClientEntity.class, "mac=?", new Object[]{s}, "createTime desc", 1, 2);
			for(ClientEntity c : list)
			{
				List<ClientAuthorization> clst = null; 
				clst = AuthorizeManager.getInstance().em_ca.query(ClientAuthorization.class,"clientID='"+c.getClientID()+"'","authorizeTime desc",1,10000);
				for(ClientAuthorization ca : clst)
				{
					String username = ca.getUsername();
					
					com.xuanzhi.tools.authorize.AuthorizeManager authorizeManager = com.xuanzhi.tools.authorize.AuthorizeManager.getInstance();
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
       沉默用户: <textarea name="macs"  cols="30" rows="10"></textarea>(可输入多个mac地址，一行一个)<br/>
        <input value="true" name="isDo" type="hidden"/><br/>
      沉默时间: <input type="text" name="silencetime" />小时<br/>
      沉默级别  <select name="silencelevel" >
            <option value="1">普通沉默</option>
            <option value="2" selected>2级沉默</option>
      原因:  <input type="text" name="reason" />
        </select><br/>
        <input type="submit" value="提交"/>
    </form>


</body>
</html>