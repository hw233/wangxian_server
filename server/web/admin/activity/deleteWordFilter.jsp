<%@ page language="java" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%@page import="com.fy.engineserver.core.GameManager"%>
<%@page import="com.fy.engineserver.core.GameInfo"%>
<%@page import="com.fy.engineserver.country.manager.CountryManager"%>
<%@page import="com.xuanzhi.tools.text.WordFilter"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>去掉屏蔽字</title>
</head>

<body>
	
	<%
	String num = request.getParameter("num");
	if(num != null && !num.equals("")){
		WordFilter wf = WordFilter.getInstance();
		Class clazz = Class.forName("com.xuanzhi.tools.text.WordFilter");
		//Field f = clazz.getDeclaredField("");
		//f.setAccessible(true);
		wf.initialize();
		out.print("success");
		
	}
	%>
	
	<form action="">
	
		playerName:<input type="text" name="num"/>
		<input type="submit" value="submit">
	
	</form>
</body>
</html>
