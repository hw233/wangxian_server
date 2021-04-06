<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="./inc.jsp"%>

<%
	Hashtable<Integer, List<Faery>> countyMap = FaeryManager.getInstance().getCountryMap();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="./css/main.css">
<title>所有仙境列表</title>
</head>
<body>
	<table border="1">
		<tr>
			<td>ID</td>
			<td>名字</td>
			<td>国家</td>
			<td>当前仙府数</td>
			<td>地图</td>
		</tr>
		<%
			for (Iterator<Integer> iter = countyMap.keySet().iterator(); iter.hasNext();) {
				int country = iter.next();
				for (Faery faery : countyMap.get(country)) {
		%>
		<tr class="<%=country % 2 == 1 ? "prize" : "target"%>">
			<td><%=faery.getId()%></td>
			<td><%=CountryManager.国家名称[faery.getCountry() - 1]%></td>
			<td><a title="<%="faery.getCountry()=" +  faery.getCountry()%>"
				href="./faery.jsp?country=<%=country%>&id=<%=faery.getId()%>"><%=faery.getName()%></a>
			</td>
			<td><%=faery.getCaveIds().length%></td>
			<td><%=faery.getGameName() + "<>" + faery.getMemoryMapName()%></td>
		</tr>
		<%
			}
			}
		%>
	</table>
</body>
</html>