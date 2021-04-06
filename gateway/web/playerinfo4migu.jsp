
<%@page import="com.fy.gamegateway.mieshi.waigua.AuthorizeManager"%><%@ page contentType="text/html;charset=utf-8" import="java.util.*,
com.xuanzhi.tools.text.*,
com.xuanzhi.tools.transport.*,java.nio.channels.*,com.fy.gamegateway.mieshi.server.*"%>
<%
AuthorizeManager authorizeManager = AuthorizeManager.getInstance();
		
out.print(authorizeManager);

	
%>

