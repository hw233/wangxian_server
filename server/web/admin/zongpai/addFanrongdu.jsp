<%@page import="com.fy.engineserver.zongzu.data.ZongPai"%>
<%@page import="com.fy.engineserver.zongzu.manager.ZongPaiManager"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>增加宗派繁荣度</title>
</head>
<body>
	<form action="">
		宗主id:<input type="text" name="zongzhuId" /> 添加繁荣度:<input type="text"
			name="fanrongdu" />
	</form>
	<%
		String zongzhuId = request.getParameter("zongzhuId");
		String fanrongdu = request.getParameter("fanrongdu");
		if (zongzhuId != null && !"".equals(zongzhuId) && fanrongdu != null && !"".equals(fanrongdu)) {
			ZongPaiManager zpm = ZongPaiManager.getInstance();
			ZongPai zp = zpm.getZongPaiByPlayerId(Long.valueOf(zongzhuId));
			if (zp != null) {
				zp.setFanrongdu(zp.getFanrongdu() - Integer.valueOf(fanrongdu));
			} else {
				out.print("宗派不存在");
			}
		}
	%>
</body>
</html>