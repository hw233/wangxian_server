<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page
	import="com.fy.engineserver.activity.fairyBuddha.FairyBuddhaManager"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Vector"%>
<%@page
	import="com.fy.engineserver.activity.fairyBuddha.FairyBuddhaInfo"%>
<%@page import="java.io.Serializable"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>页面处理仙尊</title>
</head>
<body>
<%
	FairyBuddhaManager fbm = FairyBuddhaManager.getInstance();
	Map<Byte, Vector<FairyBuddhaInfo>> electorMapTemp = (Map<Byte, Vector<FairyBuddhaInfo>>) fbm.disk.get("2015_52" + fbm.KEY_参选者);
	out.print("上期竞选者列表：<br>");
	for (Byte career : electorMapTemp.keySet()) {
		Vector<FairyBuddhaInfo> electorVector = electorMapTemp.get(career);
		if (electorVector.size() > 0) {
			for (FairyBuddhaInfo fbi : electorVector) {
				out.print(fbi.getId() + "--" + fbi.getName() + "<br>");
			}
		}
	}
	fbm.disk.put("2015_0" + fbm.KEY_参选者, (Serializable) electorMapTemp);
	fbm.newFairyBuddha();
	out.print("[仙尊] [后台刷页面选仙尊] [key:" + fbm.getKey(0) + "]<br>");
	if (fbm.logger.isWarnEnabled()) {
		fbm.logger.warn("[仙尊] [后台刷页面选仙尊] [key:" + fbm.getKey(0) + "]");
	}
	fbm.putStatue();
	out.print("[仙尊] [后台刷页面摆放仙尊] [key:" + fbm.getKey(0) + "]<br>");
%>

</body>
</html>