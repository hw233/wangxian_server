<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.sqage.stat.server.DaoJuService"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'countnum.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
    
	<form action="" method="post">
	
		本页面功能查询对服务器压力较大，非终极数据人员，请勿使用。 <br>
	</form>    
	
	                     <a href="statjsp/chongzhivip.jsp">各级VIP充值情况</a>|
		    		     <a href="statjsp/chongzhi_RegistEnter.jsp">注册并进入用户充值情况</a>|
		    		     <a href="statjsp/chongzhi_oldUser.jsp">充值老用户充值情况</a>|
		    		     <a href="statjsp/chongzhi_newUser.jsp">新用户充值情况</a>|
    
  </body>
</html>
