<%@page import="java.lang.reflect.Field"%>
<%@page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache"%>
<%@page
	import="com.fy.engineserver.activity.doomsday.DoomsdayManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
		DoomsdayManager manager = DoomsdayManager.getInstance();
		DefaultDiskCache ddc = (DefaultDiskCache) manager.diskCache;
		try {
			Field f = DefaultDiskCache.class.getDeclaredField("defaultIdleTimeout");
			if (f == null) {
				out.print("字段没取到");
				return;
			} else {
				f.setAccessible(true);
				f.set(ddc, new Long(1000L * 10));
				out.print("设置成功");
				return;
			}
		} catch (Exception e) {
		}
	%>
</body>
</html>