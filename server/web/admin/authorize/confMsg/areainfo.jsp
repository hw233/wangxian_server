<%@page import="com.fy.engineserver.worldmap.MapSingleMonsterInfo"%>
<%@page import="com.fy.engineserver.worldmap.WorldMapManager"%>
<%@page import="java.net.URLDecoder"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="com.fy.engineserver.playerTitles.PlayerTitlesManager"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.*"%>
<%@page import="java.lang.reflect.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>区域信息</title>
<link rel="stylesheet" type="text/css" href="../css/conf.css" />
</head>
<body>
	<%
		String name = request.getParameter("areaname");
		String areaname = URLDecoder.decode(name, "UTF-8");
	%>
			<h2 style="font-weight:bold;">          
		    	<%=areaname %>
		    </h2> 
<table border=1 cellspacing=0  width=100% bordercolorlight=#333333 bordercolordark=#efefef>

		<thead>
		    <th width="20%">地图名字</th>
		    <th width="20%">怪物名字</th>
		    <th width="20%">怪物级别</th>
		    <th width="20%">怪物横坐标</th>
		    <th width="20%">怪物纵坐标</th>
		</thead>
		
		<%
		WorldMapManager wmm = WorldMapManager.getInstance();
		HashMap<String, String[]> maphm = wmm.mapCHINANamesInArea;
		HashMap<String, MapSingleMonsterInfo> monsterInfo = wmm.mapEnglishSingleMonsterInfos;
		String[] mapNames = maphm.get(areaname);
		for(String mapName : mapNames){
			MapSingleMonsterInfo info = monsterInfo.get(mapName);
			String[]  monsterNames  = info.getMonsterNames();
			
			StringBuilder sbMonsterNames = new StringBuilder();
			for(String s :monsterNames){
				sbMonsterNames.append(s).append(",");
			}
			
			StringBuilder sbLevel = new StringBuilder();
			int[] levels = info.getMonsterLv();
			for(int level : levels){
				sbLevel.append(level).append(",");
			}
			
			StringBuilder sbMonsterX = new StringBuilder();
			short[] monsterX = info.getMonsterx();
			for(short level : monsterX){
				sbMonsterX.append(level).append(",");
			}
			
			StringBuilder sbMonsterY = new StringBuilder();
			short[] monsterYs = info.getMonstery();
			for(short monsterY : monsterYs){
				sbMonsterY.append(monsterY).append(",");
			}
			%>
			<tr>
			<td><%=mapName %></td>
			<td><%=sbMonsterNames %></td>
			<td><%=sbLevel %></td>
			<td><%=sbMonsterX %></td>
			<td><%=sbMonsterY %></td>
			</tr>
			<%
		}
		%>
		
	</table>
</body>
</html>