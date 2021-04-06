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
	String jiazuIds = request.getParameter("jiazuId");
	String msg = request.getParameter("msg");
	msg = msg == null ? "" : msg;
	long id = -1;
	if (jiazuIds != null) {
		id = Long.valueOf(jiazuIds);
	}
	SeptStation station = SeptStationManager.getInstance().getSeptStationBySeptId(id);
	if (station == null) {
		out.print("无效的ID：" + jiazuIds);
		return;
	}
	station.getJiazu().setLastCallbossTime(0L);

	out.print("jsp: 家族:" + station.getJiazu().getName() + "[最后一次击杀boss时间设置:" + station.getJiazu().getLastCallbossTime() + "] [" + station.getJiazu().hashCode() + "]");

	station.setInBuilding(false);
	station.setThisBoss(null);
	SeptStationMapTemplet st = SeptStationMapTemplet.getInstance();
	Calendar start = Calendar.getInstance();
	st.setStartTimes(new Calendar[] { start });
	Calendar end = Calendar.getInstance();
	end.add(Calendar.MINUTE, 10);
	st.setEndTimes(new Calendar[] { end });
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>