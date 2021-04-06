<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="inc.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<%
	SeptStationManager manager = SeptStationManager.getInstance();
	String sepStringId = request.getParameter("septId");
	long septId = -1;
	if (sepStringId != null) {
		septId = Long.valueOf(sepStringId);
	}
	List<SeptStation> all = new ArrayList<SeptStation>();
	if (septId != -1) {
		all.add(manager.getSeptStationBySeptId(septId));
	} else {
		all = manager.getAllStation();
	}
	out.print("cache.size()=" + manager.ssCache.values().size());
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">

		<title>My JSP 'SeptStationList.jsp' starting page</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" type="text/css" href="../SeptBuilding/css/septStation.css">

	</head>

	<body>
		<table>
			<tr class="head">
				<td>ID</td>
				<td>驻地名称</td>
				<td>等级</td>
				<td>当前繁荣度</td>
				<td>查看</td>
			</tr>
		<%
			if (all.size() == 0) {
				out.print("没有符合条件的驻地信息");
				return;
			}
			for (SeptStation station : all) {
				if (station == null) {
					continue;
				}
				station.setDirty(true);
				station.initInfo();
		%>
			<tr style="background-color: #3EE17B">
				<td><%=station.getId() %></td>
				<td><%=station.getName() %></td>
				<td><%=station.getMainBuildingLevel() %></td>
				<td><%=station.getInfo().getProsper()%></td>
				<td><a href="../SeptBuilding/septStation.jsp?id=<%=station.getId()%>" target="value">详细信息</a></td>
			</tr>
		<%
			}
		%>
		</table>
	</body>
</html>