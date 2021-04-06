<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.activity.ActivityManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.xuanzhi.tools.cache.diskcache.DiskCache"%>
<%@page import="com.fy.engineserver.activity.PlayerActivityRecord"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>腾讯在线设置时间间隔半小时为1分钟</title>
</head>
<body>

	<%
		String ex = request.getParameter("server");
		out.print(ActivityManager.三十分钟+"<br/>");
		if(ex != null && !ex.equals("")){
	
			ActivityManager.三十分钟  = 60*1000;
			out.print("设置成功<br/>");
			return;
		}else{
			ActivityManager.三十分钟  = 30*60*1000;
		}
	%>

	<form action="">
		设置登录时间提前29分钟:<input type="text" name="server" >
	
		<input type="submit" value="submit">
	
	</form>

</body>
</html>