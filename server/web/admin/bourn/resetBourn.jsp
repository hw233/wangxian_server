<%@page import="com.fy.engineserver.bourn.BournCfg"%>
<%@page import="com.fy.engineserver.bourn.BournManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
	BournCfg level5 = BournManager.getInstance().getBournCfg(5);
	level5.setStartTask(2333L);
	level5.setEndTask(2337L);
	
	BournCfg level6 = BournManager.getInstance().getBournCfg(6);
	level6.setStartTask(2338L);
	level6.setEndTask(2342L);
	
	BournCfg level7 = BournManager.getInstance().getBournCfg(7);
	level7.setStartTask(2343L);
	level7.setEndTask(2347L);
	
	BournCfg level8 = BournManager.getInstance().getBournCfg(8);
	level8.setStartTask(2348L);
	level8.setEndTask(2352L);
	
	out.print("level5>>>" + level5.toString() + "</BR>");
	out.print("level6>>>" + level6.toString() + "</BR>");
	out.print("level7>>>" + level7.toString() + "</BR>");
	out.print("level8>>>" + level8.toString() + "</BR>");
	
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>