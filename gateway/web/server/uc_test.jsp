<%@ page contentType="text/html;charset=utf-8" import="java.util.*,
com.xuanzhi.tools.text.*,
com.xuanzhi.tools.transport.*,java.nio.channels.*,com.fy.gamegateway.mieshi.server.*,com.fy.gamegateway.thirdpartner.uc.*"%>
<%@page import="com.xuanzhi.tools.text.ParamUtils;"%><%

		
//String beanName = "outer_gateway_connection_selector";
	
UcBossClientService uc = UcBossClientService.getInstance();	

	
%><html>
<head>
</head>
<body>
<center>
<h2></h2><br><a href="./servers.jsp">刷新此页面</a><br>
<br>
<% 
	String action = ParamUtils.getParameter(request,"action","");
	String platform = ParamUtils.getParameter(request,"platform");
	String mobile = ParamUtils.getParameter(request,"mobile","");
	String password = ParamUtils.getParameter(request,"password","");
	String username = ParamUtils.getParameter(request,"username","");
	String token = ParamUtils.getParameter(request,"token","");
	
	String actionCode = ParamUtils.getParameter(request,"actionCode","");
	String gameServer = ParamUtils.getParameter(request,"gameServer","");
	String gameArea = ParamUtils.getParameter(request,"gameArea","");
	
	
	if(action.equals("uc_register")){
		RegisterResult rr = uc.register(platform,mobile,password);
		
		out.println("<h2>注册结果:</h2>");
		out.println("status:" + rr.status + "<br>");
		out.println("message:" + rr.message + "<br>");
		out.println("uid:" + rr.uid + "<br>");
		
		username = "" + rr.uid;
	}else if(action.equals("uc_login")){
		LoginResult rr = uc.login(platform,Integer.parseInt(username),password);
		
		out.println("<h2>登录结果:</h2>");
		out.println("status:" + rr.status + "<br>");
		out.println("message:" + rr.message + "<br>");
		out.println("uid:" + rr.uid + "<br>");
		out.println("password:" + rr.password + "<br>");
		out.println("accessToken:" + rr.accessToken + "<br>");
		token = rr.accessToken;
	}else if(action.equals("uc_sendmessage")){
		uc.sendGameInfo(platform,Integer.parseInt(username),actionCode,gameServer,gameArea,token);
		
		out.println("<h2>同步完成</h2>");
	}
	
%>	

<br/>
<h2>UC注册</h2>
<form><input type='hidden' name='submitted' value='true'><input type='hidden' name='action' value='uc_register'>
请输入平台：<input type='text' name='platform' value='<%=platform%>'><br>
请输入手机号码：<input type='text' name='mobile' value='<%=mobile%>'><br>
请输入密码：<input type='text' name='password' value='<%=password%>'><br>
<input type='submit' value='提交'>
</form>

<h2>UC登录</h2>
<form><input type='hidden' name='submitted' value='true'><input type='hidden' name='action' value='uc_login'>
请输入平台：<input type='text' name='platform' value='<%=platform%>'><br>
请输入UC号：<input type='text' name='username' value='<%=username%>'><br>
请输入密码：<input type='text' name='password' value='<%=password%>'><br>
<input type='submit' value='提交'>
</form>

<h2>UC信息同步</h2>
<form><input type='hidden' name='submitted' value='true'>
<input type='hidden' name='action' value='uc_sendmessage'>
请输入平台：<input type='text' name='platform' value='<%=platform%>'><br>
请输入UC号：<input type='text' name='username' value='<%=username%>'><br>
请输入TOKEN：<input type='text' name='token' value='<%=token%>'><br>
请输入行为：<input type='text' name='actionCode' value='<%=actionCode%>'><br>
请输入服务器：<input type='text' name='gameServer' value='<%=gameServer%>'><br>
请输入分区：<input type='text' name='gameArea' value='<%=gameArea%>'><br>
<input type='submit' value='提交'>
</form>


</center>
</body>
</html> 
