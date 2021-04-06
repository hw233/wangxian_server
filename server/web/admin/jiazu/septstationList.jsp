<%@page
	import="com.fy.engineserver.septbuilding.entity.SeptBuildingEntity"%>
<%@page import="com.fy.engineserver.septstation.SeptStation"%>
<%@page import="com.xuanzhi.tools.cache.CacheObject"%>
<%@page import="java.util.Iterator"%>
<%@page
	import="com.fy.engineserver.septstation.service.SeptStationManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form action="./setstationAction.jsp" method="post" name="f1">
		<table>
			<tr>
				<td>建筑名字</td>
				<td>建筑ID</td>
				<td>建筑等级</td>
				<td>状态</td>
				<td>次数</td>
				<td>族长完成任务</td>
			</tr>
			<%
				for (Iterator it = SeptStationManager.ssCache.keySet().iterator(); it.hasNext();) {
					Object o = it.next();
					SeptStation station = (SeptStation) value;
					if (station != null) {
						for (Iterator<Long> itor = station.getBuildings(); itor.hasNext();) {
							long npcId = itor.next();
							SeptBuildingEntity sbe = station.getBuildings().get(npcId);
			%>
			<tr>
				<td colspan="4"
					style="text-align: center; font-weight: 300; color: #76B4EB">家族:<%=station.getJiazu().getName()%></td>
			</tr>
			<tr>
				<td><%=sbe.getNpc().getName()%></td>
				<td><%=sbe.getNpc().getId()%></td>
				<td><%=sbe.getBuildingState().getLevel()%></td>
				<td><%=sbe.isInBuild() ? "建设中[" + sbe.getCurrTaskNum() + "/" + sbe.getLvUpTaskNum() + "]" : "闲置"%></td>
				<td><input name="times" type="text" value="1"></td>
				<td><input type="button" value="搞几次"
					onclick="dotask('<%=station.getId() %>','<%=npcId%>')"> <input
					type="hidden" id="sid" name="sid" value=""> <input
					type="hidden" id="npcid" name="npcid" value=""></td>
			</tr>
			<%
				}
					}
				}
			%>
		</table>
	</form>
</body>
</html>
<script type="text/javascript">
	function dotask(sid,npcid){
		document.getElementById("sid").value=sid;
		document.getElementById("npcid").value=npcid;
		document.f1.submit();
	}
</script>