<%@page import="com.fy.engineserver.enterlimit.EnterLimitManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<% 
	String usernames = request.getParameter("usernames");
	String action = request.getParameter("action");
	if ("jiefeng".equals(action)) {
		//要解封
		String [] unameArr = usernames.split("\r\n");
		EnterLimitManager manager = EnterLimitManager.getInstance();
		if (unameArr != null && unameArr.length > 0) {
			for (String uname : unameArr) {
				if (manager.inEnterLimit(uname,null)) {
					out.print("<H4>找到被限制用户:["+uname+"]</H4>");
				} else {
					out.print("<H1><font color=9527'red'>未找到被限制用户:["+uname+"]</font></H1>");
				}
			}
			manager.removeFromlimit(unameArr);
			out.print("<H1>操作完成</H1>");
		} else {
			out.print("<H3>输入要解封的用户名称</H3>");
		}
	}
	
	List<String> us = EnterLimitManager.getInstance().limited;
%>

<%@page import="java.util.List"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>解除进入限制</title>
</head>
<body>
<form action="./jiefeng.jsp" method="post">
	输入要解封的玩家(一行一个):
	<textarea rows="10" cols="10" name="usernames"></textarea>
	<input type="hidden" value="jiefeng" name="action">
	<input type="submit" value="解封">
</form>
		<h2>封号名单</h2>
		<%
		for(String  s: us){
			out.print(s+"<br>");
		}
		%>
	
</body>
</html>