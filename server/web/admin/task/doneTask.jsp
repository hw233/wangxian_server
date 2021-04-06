<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.sprite.Team"%>
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
	String d = request.getParameter("d");
	String playerIdStr = request.getParameter("playerId");
	String taskName = request.getParameter("taskName");
	if (playerIdStr != null && taskName != null) {
		long pId = Long.valueOf(playerIdStr);
		Player p = GamePlayerManager.getInstance().getPlayer(pId);
		if (p == null) {
			out.print("<H1>角色不存在</H1>");
		} else {
			Team t = p.getTeam();
			if (t == null) {
				out.print("没有队伍");
			} else {
				List<Player> ms = t.getMembers();
				for (Player pp : ms) {
					out.print("队伍成员:" + pp.getId() + "<BR/>");
				}
				out.print("队伍标记" + p.getTeamMark() + "</BR>");
				out.print("有队伍");
				for (Player ppp : p.getTeamMembers()) {
					out.print("队伍成员2:" + ppp.getId() + "<BR/>");
				}
				if (ms.size() == 0) {
					if ("d".equals(d)) {
						p.setTeam(null);
					}
				}
			}
			Task task = TaskManager.getInstance().getTask(taskName);
			if (task == null) {
				out.print("<H1>任务不存在</H1>");
			} else {
				TaskEntity te = p.getTaskEntity(task.getId());
				if (te != null && te.getStatus() == 1) {
					for (int i = 0; i < te.getCompleted().length; i++) {
						te.modifyPercentageCompleted(i, 1);
					}
				}
			}
		}
	} else {
		taskName = "";
		playerIdStr = "";
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form action="./doneTask.jsp">
		角色ID:<input name="playerId" type="text" value="<%=playerIdStr%>">
		任务名字:<input name="taskName" type="text" value="<%=taskName%>">
		<input type="submit" value="提交">
	</form>
</body>
</html>