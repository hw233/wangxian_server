<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.menu.WindowManager"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.activity.fairyBuddha.FairyBuddhaManager"%>
<%@page import="java.io.Serializable"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>测试修改窗口描述</title>
<%
//	WindowManager.getInstance().getWindowMap().get(131).setDescriptionInUUB("测试一下,不要惊恐");
List<String> l = new ArrayList<String>();
l.add("aa");
l.add("bb");
FairyBuddhaManager fbm = FairyBuddhaManager.getInstance();
fbm.disk.put(1, (Serializable) l);
out.print(fbm.disk.get(1));
l.add("cc");
out.print(fbm.disk.get(1));
l.clear();
out.print(fbm.disk.get(1));
%>
</head>
<body>

</body>
</html>