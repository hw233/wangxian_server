<%@page
	import="com.fy.engineserver.enterlimit.EnterLimitManager.PlayerRecordData"%>
<%@page
	import="com.fy.engineserver.enterlimit.EnterLimitManager.EnterLimitData"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.enterlimit.EnterLimitManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	Map<String, EnterLimitData> likeNeedLimit = EnterLimitManager.getInstance().likeNeedLimit;
	out.print(likeNeedLimit.size());
	for (EnterLimitData e : likeNeedLimit.values()) {
		List<PlayerRecordData> list = e.recordDatas;
		out.print(">>>" + list.size());
		for (PlayerRecordData p : list) {
			p.sealEmptyCellTimes = 10;
			out.print(p.username + "<BR/>");
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