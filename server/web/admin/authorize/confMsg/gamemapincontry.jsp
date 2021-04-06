<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="com.fy.engineserver.core.GameManager"%>
<%@page import="java.util.ArrayList"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>中立地图</title>
<link rel="stylesheet" type="text/css" href="../css/conf.css" />
</head>
<body>
<table border=1 cellspacing=0  width=100% bordercolorlight=#333333 bordercolordark=#efefef>

		<thead>
		    <th width="40%">属于某个国家的地图名</th>
  		</thead>
		<%
		GameManager gm = GameManager.getInstance();
		ArrayList<String>  mapName = gm.非中立的国家地图;
		for(String s : mapName){
			%><tr>
			<td><%=s %></td>
		</tr><%
		}
		%>
		
	</table>
</body>
</html>