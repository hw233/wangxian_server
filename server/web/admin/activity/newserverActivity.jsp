<%@page import="java.util.Iterator"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.fy.engineserver.util.CompoundReturn"%>
<%@page import="com.fy.engineserver.activity.newserver.NewServerActivityManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
CompoundReturn cr =	NewServerActivityManager.getInstance().inNewserverActivity();
if (cr != null) {
	out.print(cr.getBooleanValue() + "<BR/>");
	if (cr.getBooleanValue()) {
		HashMap map = (HashMap) cr.getObjValue();
		for (Iterator<Integer> itor = map.keySet().iterator();itor.hasNext();) {
			int key = itor.next();
			out.print(key + ":" + map.get(key) + "<BR/>");
		}
		out.print("<HR>");
		out.print(map.get(20));
	}
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