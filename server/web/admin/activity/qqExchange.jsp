<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.activity.ActivityManager"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>腾讯兑换活动设置服务器</title>
</head>
<body>

	<%
		String ex = request.getParameter("server");
		if(ex != null && !ex.equals("")){
		
			String[] servers = {"太虚幻境","幽冥山谷","昆仑圣殿","凌霄宝殿","霸气乾坤","烟雨青山"};
			ActivityManager.QQServer = servers;
			out.print("设置成功<br/>");
			return;
		}
	%>

	<form action="">
		设置服务器:<input type="text" name="server" >
	
		<input type="submit" value="submit">
	
	</form>

</body>
</html>