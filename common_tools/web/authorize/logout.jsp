<%@ page contentType="text/html;charset=utf-8" %><%@ page import="com.xuanzhi.tools.authorize.AuthorizedServletFilter" %><%
	session.removeAttribute(AuthorizedServletFilter.USERNAME);
	session.removeAttribute(AuthorizedServletFilter.PASSWORD);
	session.removeAttribute(AuthorizedServletFilter.PROPERTIES);
	session.removeAttribute(AuthorizedServletFilter.USER);
	session.removeAttribute(AuthorizedServletFilter.PLATFORM);
	
    out.println("<br><br><h1><center>您已经成功退出，请重新登录使用系统</center></h1>");
    	 
%>