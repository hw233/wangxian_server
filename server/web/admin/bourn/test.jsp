<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
try {
	Player player = GamePlayerManager.getInstance().getPlayer("yyy");
	out.print("测试前:" + player.getLastCountBournTime() + "," + player.getLeftZazenTime());
	/*player.modifyBournTaskAndTime();*/
	out.print("测试后:" + player.getLastCountBournTime() + "," + player.getLeftZazenTime());
} catch (Exception e) {
	
}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

	


</body>
</html>