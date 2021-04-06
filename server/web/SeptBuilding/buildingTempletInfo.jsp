<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="inc.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
//	out.print(SeptBuildingManager.type_templet_map.size() + "<BR/>");
//	for ( SeptBuildingTemplet t : SeptBuildingManager.type_templet_map.values()) {
//		out.print(t.toString()+ "<BR/>");
//	}
//	for (BuildingType t : BuildingType.values()) {
//			out.print(t.getName() + "----[" + t.hashCode() + "]" + t.isBase() + ",,," + t.isDefault() + "<BR/>");
//	}
 %>
<head>
	<link rel="stylesheet" type="text/css" href="../SeptBuilding/css/septStation.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>家族驻地模板数据</title>
</head>
<body>
	<table border="1">
		<tr class="head">
			<td>名字</td>
			<td>信息</td>
		</tr>
	<%
		int i = 0;
		for (Iterator<BuildingType> it =  SeptBuildingManager.type_templet_map.keySet().iterator();it.hasNext();)  {
			i++;
			BuildingType type = it.next();
			SeptBuildingTemplet templet = SeptBuildingManager.type_templet_map.get(type);
	 %>
	 	<tr style="background-color: <%=type.isBase()? baseColor : otherColor%>">
			<td><%=type.getName() %></td>
			<td><%=templet.toString()%></td>
	 	</tr>
	 <%
	 	}
	  %>
	</table>
</body>
</html>