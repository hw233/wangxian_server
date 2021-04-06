<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.datasource.language.Translate"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

	<%
			String ss = Translate.与你进行XX的XX已经到达情缘树;
			String ss2 = Translate.你已到达情缘树请等待XX到达进行XX;
			out.print(ss + ss2);
	
	
	%>


</body>
</html>