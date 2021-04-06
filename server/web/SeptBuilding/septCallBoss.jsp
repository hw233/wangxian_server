<%@page import="java.util.Calendar"%>
<%@page
	import="com.fy.engineserver.septstation.SeptStationMapTemplet"%>
<%@page
	import="com.fy.engineserver.septstation.service.SeptStationManager"%>
<%@page import="com.fy.engineserver.septstation.SeptStation"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
	String sId = request.getParameter("id");
	String msg = request.getParameter("msg");
	msg = msg == null ? "" : msg;
	long id = -1;
	if (sId != null) {
		id = Long.valueOf(sId);
	}
	SeptStation station = SeptStationManager.getInstance().getSeptStationById(id);
	if (station == null) {
		out.print("无效的ID：" + sId);
		return;
	}
	station.setInBuilding(false);
	station.setThisBoss(null);
	station.getJiazu().setLastCallbossTime(0L);
	SeptStationMapTemplet st = SeptStationMapTemplet.getInstance();
	Calendar start = Calendar.getInstance();
	st.setStartTimes(new Calendar[] { start });
	Calendar end = Calendar.getInstance();
	end.add(Calendar.MINUTE, 10);
	st.setEndTimes(new Calendar[] { end });
	response.sendRedirect("./septStation.jsp?id=" + id);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>