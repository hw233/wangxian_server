<%@ page contentType="text/html;charset=utf-8" import="java.util.*,
com.xuanzhi.tools.text.*,
com.xuanzhi.tools.transport.*,java.nio.channels.*,com.fy.gamegateway.mieshi.server.*"%>
<%@page import="com.xuanzhi.tools.text.ParamUtils;"%><%

	boolean isDo = ParamUtils.getBooleanParameter(request, "isDo");
	if(isDo)
	{
		String userName =  ParamUtils.getParameter(request, "username");
		
		MieshiServerInfoManager mieshiServerInfoManager = MieshiServerInfoManager.getInstance();
	
		boolean isTest = mieshiServerInfoManager.isTestUser(userName);
		
		if(isTest)
		{
			out.print("["+userName+"]是测试用户");
		}
		else
		{
			out.print("["+userName+"]不是测试用户");
		}
	}


	
%><html>
<head>
</head>
<body>
<h2>看是否是测试用户</h2>
<center>
<form>
	<input type="hidden" value="true" name="isDo" />
	<input type="text" value="" name="username" />
	<input type="submit" value="提交"  />
</form>
</body>
</html> 
