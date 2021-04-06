<%@page import="java.util.List"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.fy.engineserver.sprite.npc.FollowableNPC"%>
<%@page import="com.fy.engineserver.sprite.npc.MemoryNPCManager"%>
<%@page import="com.fy.engineserver.util.TimeTool"%>
<%@page import="com.fy.engineserver.gametime.SystemTime"%>
<%@page import="com.fy.engineserver.newtask.TaskEntity"%>
<%@page import="com.fy.engineserver.newtask.service.TaskManager"%>
<%@page import="com.fy.engineserver.newtask.Task"%>
<%@page
	import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
	String pname = request.getParameter("pname");
	Player player = null;
	if (!(pname == null || "".equals(pname))) {
		player = GamePlayerManager.getInstance().getPlayer(pname);
		if (player != null) {
			List<Task> tasks = TaskManager.getInstance().getTaskBigCollections().get("国运");
			List<Task> tasks1 = TaskManager.getInstance().getTaskBigCollections().get("家族运镖");
			tasks.addAll(tasks1);
			for (Task task : tasks) {

				if (task != null) {
					TaskEntity entity = player.getTaskEntity(task.getId());
					if (entity != null) {
						out.print("<HR>");
						out.print(Arrays.toString(entity.getTaskDemand()));
						out.print(Arrays.toString(entity.getCompleted()));
						out.print("<HR>");
						entity.setCycleDeliverTimes(0);
						entity.setLastDeliverTime(SystemTime.currentTimeMillis() - TimeTool.TimeDistance.DAY.getTimeMillis());
					}
				}
			}
		}
	} else {
		pname = "";
	}
	FollowableNPC followableNPC = (FollowableNPC) MemoryNPCManager.getNPCManager().createNPC(410000001);
	if (followableNPC != null) {
		out.print(followableNPC.getRadius());
	} else {
		out.print("NULL");
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form action="./resetTaskTimes.jsp" method="post">
		角色名:<input name="pname" type="text" value="<%=pname%>"> <input
			type="submit" value="重置">
	</form>
</body>
</html>