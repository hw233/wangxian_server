<%@page import="java.util.*,java.io.*,
				com.xuanzhi.tools.text.*,
				com.xuanzhi.tools.*,
				com.sqage.stat.service.*"
				
				%>
<%
	if(session.getAttribute("username")==null) {
	   out.println("sfsdfdsf"+request.getContextPath() + "/login.jsp");
		response.sendRedirect(request.getContextPath() + "/login.jsp");
		return;
	}
%>