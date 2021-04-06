<%@page import="com.fy.engineserver.menu.activity.nianshou.Option_Bomb"%>
<%@page import="java.util.Iterator"%>
<%@page
	import="com.fy.engineserver.activity.RefreshSpriteManager.RefreshSpriteData"%>
<%@page import="java.util.Map"%>
<%@page import="com.fy.engineserver.activity.RefreshSpriteManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
/**
	Map<String, RefreshSpriteData> data = RefreshSpriteManager.getInstance().getConfigedDatas();
	for (Iterator<String> itor = data.keySet().iterator(); itor.hasNext();) {
		String name = itor.next();
		RefreshSpriteData d = data.get(name);
		if (d.getLastRefreshTime() > 0) {
			d.setLastRefreshTime(System.currentTimeMillis() - d.getConf().getTimeDelay() + 60000);
			out.print("name:" + name);
			out.print("<hr/>");
		}
	}
	*/
	
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>