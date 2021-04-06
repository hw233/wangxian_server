<%@page import="com.fy.engineserver.newtask.Task"%>
<%@page import="com.fy.engineserver.sprite.npc.MemoryNPCManager"%>
<%@page import="com.fy.engineserver.newtask.service.TaskManager"%>
<%@page
	import="com.fy.engineserver.newtask.service.TaskConfig.ModifyType"%>
<%@page import="com.fy.engineserver.core.client.FunctionNPC"%>
<%@page import="java.util.*"%>
<%@page
	import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String playerName = request.getParameter("playerName");
	Player player = null;
	if (playerName != null && !playerName.isEmpty()) {
		player = GamePlayerManager.getInstance().getPlayer(playerName);
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form action="./functionNpcCheck.jsp" method="post">
		要查询的角色名字:<input name="playerName" type="text" value="<%=playerName%>">
		<input name="OK" type="submit" value="查一下">
	</form>
	<%
		if (player != null) {
			List<FunctionNPC> functionNpcList = player.getCurrMapFunctuinNPC();
			if (functionNpcList != null && !functionNpcList.isEmpty()) {
				TaskManager taskManager = TaskManager.getInstance();
				for (FunctionNPC npc : functionNpcList) {
					Map<ModifyType, ArrayList<Long>> modifyMap = npc.getWaitForChange();
					out.print("<HR/>");
					out.print("<font color='red'>" + npc.getName() + npc.toString() + ":</font><BR/>");
					out.print("<HR/>");
					out.print("[已有任务列表]" + Arrays.toString(npc.getAvaiableTaskIds()) + "<BR/>");
					for (Iterator<ModifyType> itor = modifyMap.keySet().iterator(); itor.hasNext();) {
						ModifyType type = itor.next();
						out.print("&nbsp;&nbsp;&nbsp;&nbsp;<font color='green'>" + type.getName() + "</font><BR/>");
						StringBuffer sbf = new StringBuffer("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
						for (Long taskId : modifyMap.get(type)) {
							Task task = taskManager.getTask(taskId);
							sbf.append(task.getName()).append(",");
						}
						out.print(sbf.toString() + "<BR/>");
					}
				}
			} else {
				out.print("暂无数据");
			}
		}
	%>
</body>
</html>