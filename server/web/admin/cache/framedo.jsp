<%@ page contentType="text/html; charset=utf-8"%>
<%@page import="java.util.*,
		java.net.*,com.linktone.ivr.authorize.service.*,
		java.net.*,com.linktone.ivr.authorize.*,
		org.apache.log4j.*"%>
<%
String username = request.getParameter("username");
UserManager um = UserManager.getInstance();
User u = um.getUser(username);
if(u != null) {
	session.setAttribute("username", username);
	response.sendRedirect("index.jsp");
	return;
}
%>
