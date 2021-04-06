<%@page import="java.util.ArrayList"%>
<%@page import="java.io.Serializable"%>
<%@page import="java.io.File"%>
<%@page import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiGatewaySubSystem"%>
<%@page import="java.util.Calendar"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
		Calendar calendar = Calendar.getInstance();
		int today = calendar.get(Calendar.DAY_OF_YEAR);
		MieshiGatewaySubSystem instance = MieshiGatewaySubSystem.getInstance();
		if (instance.errorMsgDiskCache == null) {
			instance.errorMsgDiskCache = new DefaultDiskCache(new File(instance.getErrorMsgPath()), "崩溃日志", 1000L*60*60*24*MieshiGatewaySubSystem.CACHE_DISTIME);
			Serializable a = instance.errorMsgDiskCache.get(MieshiGatewaySubSystem.ERROR_DAY_CACHE);
			if (a != null) {
				instance.errorToday = Integer.parseInt(a.toString());
			}else {
				instance.errorToday = today;
				instance.errorMsgDiskCache.put(MieshiGatewaySubSystem.ERROR_DAY_CACHE, instance.errorToday);
			}
			Serializable b = instance.errorMsgDiskCache.get(MieshiGatewaySubSystem.ERROR_MSG_CACHE + instance.errorToday);
			if (b != null) {
				instance.errorMsgs = (ArrayList<String[]>)b;
			}else {
				instance.errorMsgs.clear();
				instance.errorMsgDiskCache.put(MieshiGatewaySubSystem.ERROR_MSG_CACHE + instance.errorToday, instance.errorMsgs);
			}
		}
		ArrayList<String[]> oneDayMsg = null;
		int dd = -1;
		String action = request.getParameter("action");
		if (action != null) {
			if (action.equals("getDayError")) {
				String day = request.getParameter("day");
				dd = Integer.parseInt(day);
				Serializable b = instance.errorMsgDiskCache.get(MieshiGatewaySubSystem.ERROR_MSG_CACHE + day);
				if (b != null) {
					oneDayMsg = (ArrayList<String[]>)b;
				}
			}
		}
	%>
	<form name="f1">
		输入查询日期，今天<%=today %>
		<input type="hidden" name="action" value="getDayError">
		日期<input name="day" type="text">
		<input type="submit" value=<%="确定"%>>
	</form>
	<br>
	<br>
	<%="今天的数据:~~~~~~~~~~~" %>
	<br>
	<%
		for (int i = 0; i < instance.errorMsgs.size(); i++) {
			%>
			<%="~~~~~~~~~~~~~~~~第几条"+i + "~~~~~~~~~~~" %>
			<br>
			<%
			String[] msgs = instance.errorMsgs.get(i);
			for (int j = 0; j < msgs.length; j++) {
			%>
				<%=msgs[j] %>
				<br>
			<%
			}
		}
	%>
	<br>
	<br>
	<%
		if (oneDayMsg != null) {
			%>
			<br>
			<%="查询的数据:~~~~~~~~~~~"+dd+"~~~~~~~~~~" %>
			<%
			for (int i = 0; i < oneDayMsg.size(); i++) {
				%>
				<%="~~~~~~~~~~~~~~~~第几条"+i + "~~~~~~~~~~~" %>
				<br>
				<%
				String[] msgs = oneDayMsg.get(i);
				for (int j = 0; j < msgs.length; j++) {
				%>
					<%=msgs[j] %>
					<br>
				<%
				}
			}
		}
	%>
</body>
</html>
