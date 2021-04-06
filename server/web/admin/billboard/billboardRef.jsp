<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="./autoInc.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%

	String op = request.getParameter("op");
	if ("run".equals(op)) {
		String contextPath = request.getRealPath("/").replace("webapps","log");
		out.print("contextPath:" + contextPath + "<BR/>");
		String res = run(contextPath);
		out.print("<hr/>");
		out.print("执行结果:" + res);
		out.print("<hr/>");
		 
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>