<%@page import="com.fy.engineserver.homestead.cave.ResourceCollection"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.menu.cave.Option_Cave_Create_Cave"%>
<%@page import="com.fy.engineserver.menu.Option"%>
<%@page import="com.fy.engineserver.menu.MenuWindow"%>
<%@page import="com.fy.engineserver.menu.WindowManager"%>
<%@page import="com.fy.engineserver.homestead.cave.Cave"%>
<%@page import="com.fy.engineserver.homestead.faery.Faery"%>
<%@page import="java.util.*"%>
<%@page
	import="com.fy.engineserver.homestead.faery.service.FaeryManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	if ("add".equals(request.getParameter("option"))) {
		if (!"add".equals(request.getParameter("pwd"))) {
			out.print("error");
			return;
		}
		Player p = GamePlayerManager.getInstance().getPlayer(request.getParameter("pname"));
		int res = Integer.valueOf(request.getParameter("res"));
		Cave cave = FaeryManager.getInstance().getCave(p);
		if (res > 0) {
			cave.getCurrRes().unite(new ResourceCollection(res,res,res));
		} else {
			cave.getCurrRes().deduct(new ResourceCollection(Math.abs(res),Math.abs(res),Math.abs(res)));
		}
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body style="font-size: 12px;">
<form action="./test.jsp" method="post">
	角色名:<input type="text" name="pname">
	资源:<input type="text" name="res">
	密码:<input type="password" name="pwd">
	<input type="hidden" name="option" value="add">
	<input type="submit">
</form>
</body>
</html>