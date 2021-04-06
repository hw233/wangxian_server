<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.homestead.cave.CaveBuilding"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.fy.engineserver.homestead.cave.CaveSchedule"%>
<%@page import="com.fy.engineserver.homestead.cave.Cave"%>
<%@page
	import="com.fy.engineserver.homestead.faery.service.FaeryManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String playerName = request.getParameter("playerName");
	if (playerName != null && !"".equals(playerName)) {
		Player p = PlayerManager.getInstance().getPlayer(playerName);
		if (p == null) {
			out.print("<H1>角色不存在:" + playerName + "</H1>");
		} else {
			Cave cave = FaeryManager.getInstance().getCave(p);
			if (cave == null) {
				out.print("<H1>角色没有仙府:" + playerName + "</H1>");
			} else {
				out.print("caveid:"+cave.getId()+"<br/>");
				out.print("faeryid:"+cave.getFaery().getId()+"<br/>");
				for (Iterator<Long> it = cave.getBuildings().keySet().iterator(); it.hasNext();) {
					Long id = it.next();
					CaveBuilding cb = cave.getBuildings().get(id);
					if (cb != null) {
						List<CaveSchedule> csList = cb.getSchedules();
						List<CaveSchedule> remove = new ArrayList<CaveSchedule>();
						if (csList != null) {
							for (CaveSchedule cs : csList) {
								if (cs.hasDone()) {
									remove.add(cs);
								}
							}
							if (remove.size() > 0) {
								for (CaveSchedule rm : remove) {
									if (rm != null) {
										cb.removeScheduleForDone(rm.getOptionType());
										cave.notifyFieldChange(cb.getType());
										out.print("移除[" + cb.getNpc().getName() + "]的非法的队列:" + rm.toString());
										out.print("<BR/>");
									}
								}
							} else {
								out.print("[" + cb.getNpc().getName() + "]没有异常数据");
								out.print("<BR/>");
							}
						}
					}
				}
			}
		}
	} else {
		playerName = "";
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修正仙府进度出现错误</title>
</head>
<body>
	<form action="./modifyErrorSchedule.jsp" method="post">
		角色名字:<input name="playerName" value="<%=playerName%>"> <input
			type="submit" value="提交">
	</form>
</body>
</html>