<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="java.util.Hashtable"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.fy.engineserver.homestead.cave.Cave"%>
<%@page import="java.util.Collection"%>
<%@page import="com.fy.engineserver.homestead.faery.service.FaeryManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%

	Hashtable<Long, Long> map = FaeryManager.getInstance().getKhatamMap();
	Hashtable<Long, Cave> caves = FaeryManager.getInstance().getPlayerCave();
	Hashtable<Long, Cave> caveIdMap = FaeryManager.getInstance().getCaveIdmap();
	
	String playerIdS = request.getParameter("playerId");
	if (playerIdS != null) {
		Iterator<Long> iterator = caveIdMap.keySet().iterator(); 
		while (iterator.hasNext()) {
			long caveId = iterator.next();
			boolean found = false;
			for (Cave c : caves.values()) {
				if (c.getId() == caveId) {
					found = true;
					break;
				}
			}
			if (!found) {
				Cave cc = caveIdMap.get(caveId);
				cc.setStatus(1);
				FaeryManager.caveEm.flush(cc);
				FaeryManager.getInstance().getKhatamMap().put(cc.getOwnerId(), cc.getId());
				iterator.remove();
			}
		}
		boolean inComm = true;
		long playerId = Long.valueOf(playerIdS);
		Cave  cave = caves.get(playerId);
		if (cave != null) {
			out.print("<H2>" +playerId + "的仙府在有效仙府列表中,仙府状态:" + cave.getStatus() +"</H2>");
		} else {
			inComm = false;
			out.print("<H2>" +playerId + "的仙府bu在有效仙府列表中,</H2>");
		}
		boolean inKhatam = true;
		if (map.containsKey(playerId)) {
			out.print("<H2>" +playerId + "的仙府在封印仙府列表中" + "</H2>");
		} else {
			inKhatam = false;
			out.print("<H2>" +playerId + "的仙府bu在封印仙府列表中" + "</H2>");
		}
		Player player = GamePlayerManager.getInstance().getPlayer(playerId);
		if (caveIdMap.containsKey(player.getCaveId())) {
			out.print("<H2>" +playerId + "在CAVEIDmap" + "</H2>");
		} else {
			out.print("<H2>" +playerId + "bu在CAVEIDmap" + "</H2>");
		}
		Cave DBCave = FaeryManager.caveEm.find(player.getCaveId());
		if (DBCave != null) {
			
			out.print("数据库中 status:" + DBCave.getStatus() + ", ownerId:" + DBCave.getOwnerId() + " version" + DBCave.getVersion() + "<BR>");
			if (!inComm && !inKhatam) {
			}
		} else {
			out.print("数据库中不存在");
		}
	}
	
	out.print("有效数量:" + caves.size());
	out.print("封印数量:" + map.size());
	out.print("IDMAP数量:" + caveIdMap.size());
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>封印所有仙府</title>
</head>
<body style="font-size: 12px;">
	<form action="./seeKhatam.jsp" method="post">
		<input type="text" name="playerId" value="<%=playerIdS%>">
		<input type="submit" value="查询">
	</form>
	<%
		if (true) {
			return;
		}
	%>
	<table border="1">
		<tr>
			<td>玩家ID</td>
			<td>仙府ID</td>
		</tr>
		<%
			for (Iterator<Long> itor = map.keySet().iterator();itor.hasNext();) {
				Long id = itor.next();
				%>
					<tr>
						<td><%=id %></td>
						<td><%=map.get(id) %></td>
					</tr>
				<%
			}
		%>
	</table>
</body>
</html>