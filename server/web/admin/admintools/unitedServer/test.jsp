<%@page import="java.lang.reflect.Field"%>
<%@page import="com.fy.engineserver.uniteserver.UnitedServerThread"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.uniteserver.UnitedServerManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	List<UnitedServerThread> list = UnitedServerManager.getInstance().getCurrentUnitedThreads();
	out.print("<H1>还在运行的线程:" + list.size() + "</H1>");
	Field f = UnitedServerThread.class.getDeclaredField("running");
	f.setAccessible(true);
	int index = 0;
	for (UnitedServerThread ut : list) {

		out.print("[" + (index++) + "] 正在执行的方法:" + ut.getMethodName() + ",是否正在执行:[" + ut.isRunning() + "]");
		for (Object o : ut.getArgs()) {
			out.print("&nbsp;");
			out.print("&nbsp;");
			out.print("&nbsp;");
			out.print("&nbsp;");
			out.print(o.toString());
			out.print(",");
			if (ut.isRunning()) {
				//f.set(ut, false);
				out.print("OK");
			}
		}
		out.print("<BR>");
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