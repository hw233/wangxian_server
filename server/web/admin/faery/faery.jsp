<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.npc.CaveNPC"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.core.LivingObject"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="./inc.jsp"%>
<%
	int country = ParamUtils.getIntParameter(request, "country", -1);
	long faeryId = Long.valueOf(ParamUtils.getParameter(request, "id"));
	String msg = "";
	if (country == -1 || faeryId == -1) {
		msg = "参数异常";
		response.sendRedirect("admin/faery/faeryList.jsp?msg=" + msg);
		return;
	}
	Faery faery = FaeryManager.getInstance().getFaery(faeryId);
	if (faery == null) {
		msg = "仙府不存在";
		response.sendRedirect("admin/faery/faeryList.jsp?msg=" + msg);
		return;
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="./css/main.css">
<title>仙府<%=faery.getName()%></title>
</head>
<body>
	<form action="./faeryOption.jsp" method="post">
		<input type="hidden" name="country" value="<%=country%>"> <input
			type="hidden" name="id" value="<%=faeryId%>">
		<table>
			<tr class="head">
				<td>ID</td>
				<td>位置索引</td>
				<td>主人ID</td>
				<td>等级</td>
			</tr>
			<%
				out.print("地图资源名字[mapResName]:" + faery.getGameName() + "<BR/>");
				out.print("地图名字[mapName]:" + faery.getMemoryMapName() + "<BR/>");
				out.print("caveIDS:" + Arrays.toString( faery.getCaveIds()) + "<BR/>");
				for (int i = 0; i < faery.getCaveIds().length; i++) {
					Cave cave = faery.getCaves()[i];
					if (cave == null) {
			%>
			<tr>
				<td colspan="4">虚位以待,<%=faery.getCaveIds()[i]%><input
					name="playerId" type="text" value="输入角色ID"> <input
					type="submit" value="创建"> <input type="hidden" name="index"
					value="<%=i%>">
				</td>
			</tr>

			<%
				} else {

						Player p = null;
						try {
							p = PlayerManager.getInstance().getPlayer(cave.getOwnerId());
						} catch (Exception e) {
						}
			%>
			<tr>
				<td><a
					href="./cave.jsp?faeryId=<%=faeryId%>&index=<%=i%>&country=<%=country%>"><%=cave.getId()%></a>
				</td>
				<td><%=i%></td>
				<td><%=cave.getOwnerId()%>/<%=p == null ? "----" : p.getName()%></td>
				<td><%=cave.getMainBuilding().getGrade()%></td>
			</tr>
			<%
				}
				}
			%>
		</table>
	</form>
	<hr />
	<table>
		<%
			for (LivingObject lo : faery.getGame().getLivingObjects()) {
		%>
		<tr>
			<td><%=(lo instanceof Player ? ((Player) lo).getName() : lo.getClass())%>
			</td>
			<td><%=lo%> **** <%=(lo instanceof CaveNPC ? ("NPCID:" + ((CaveNPC) lo).getId() + ",NPCNAME" + ((CaveNPC) lo).getName() + ",所有者ID:" + ((CaveNPC) lo).getCave().getOwnerId() + ",模板ID:" + ((CaveNPC) lo).getNPCCategoryId() + "--- 比例:" + ((CaveNPC) lo).getObjectScale()) : "--- ")%></td>
		</tr>
		<%
			}
		%>
	</table>
</body>
</html>