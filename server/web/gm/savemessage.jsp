<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="java.util.Enumeration"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=utf-8">
		<title>潜龙GM邮箱</title>
		<link rel="stylesheet" href="../style.css" />
		<script type='text/javascript'
			src='/game_server/dwr/interface/getmes.js'></script>
		<script type='text/javascript' src='/game_server/dwr/engine.js'></script>
		<script type="text/javascript"
			src="/game_server/dwr/interface/DWRManager.js"></script>
		<script type="text/javascript" src="/game_server/dwr/util.js"></script>
		<script type="text/javascript">
	
</script>
	</head>
	<body>
		<%
			String username = request.getParameter("username");
			String gmid = request.getParameter("gmid");
			String server = request.getParameter("server");
			if (username != null && gmid != null) {
				session.setAttribute("username", username);
				session.setAttribute("gmid", gmid);
				out.print("欢迎你 username is " + username + " gmid is " + gmid);
				out.print("<hr/>session id is " + session.getId() + "<br/>");
				/* Cookie cookie = new Cookie("JSESSIONID", session.getId());
				cookie.setMaxAge(3600);
				response.addCookie(cookie);
				Cookie cs[] = request.getCookies();
				if (cs != null) {
					for (Cookie c : cs) {
						out
								.print(c.getName() + "===>" + c.getValue()
										+ "<br/>");
					}
				}
				out.print("设置cookie完毕");*/
				RequestDispatcher dp = request.getRequestDispatcher("a.jsp");
				dp.forward(request, response);
			} else {
				out.print("<h3 color='red'>登录失败请重新登录<h3>");
			}
		%>
	</body>
