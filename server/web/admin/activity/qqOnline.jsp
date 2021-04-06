<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.activity.ActivityManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>腾讯在线设置登陆时间</title>
</head>
<body>

	<%
		String ex = request.getParameter("server");
		if(ex != null && !ex.equals("")){
			Player player = PlayerManager.getInstance().getPlayer(ex);
			player.setEnterServerTime(System.currentTimeMillis() - 29*60*1000);
			out.print("设置成功<br/>");
			return;
		}
	%>

	<form action="">
		设置登录时间提前29分钟:<input type="text" name="server" >
	
		<input type="submit" value="submit">
	
	</form>

</body>
</html>