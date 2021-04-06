<%@page import="java.util.Iterator"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="/css/style.css" />
<title>资源更新</title>
<%@include file="gameResourceUpdateTool_config.jsp" %>
</head>
	<body bgcolor='#c0c0c0'>
		<%
		Object o = session.getAttribute("authorize.username");
		//if(o!=null){
			out.print("<font size='5' color='#cd853f'><b>服务器列表</b></font><br><br>");
			Iterator it = addressConfig.keySet().iterator();
			while(it.hasNext()){
				String key = (String)it.next();
			%>
				<a href="<%=addressConfig.get(key) %>?action=" target="targetname"><font color='#2e8b57' size='2'><b><%=key %></b></font></a><br><br>
			<%	
			}
		//}else{
		//	out.print("请重启登陆再操作！");
		//}
		%>
	</body>
</html>