<%@page import="java.net.URLEncoder"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="com.fy.engineserver.worldmap.WorldMapManager"%>
<%@page import="com.fy.engineserver.worldmap.WorldMapArea"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.*"%>
<%@page import="java.lang.reflect.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>世界地图信息</title>
<link rel="stylesheet" type="text/css" href="../css/conf.css" />
</head>
<body>
<table border=1 cellspacing=0  width=100% bordercolorlight=#333333 bordercolordark=#efefef>

		<thead>
		    <th width="4%">地图名称</th>
		    <th width="7%">按下图片名称</th>
		    <th width="4%">按下图片显示横坐标</th>
		    <th width="4%">按下图片显示纵坐标</th>
		    <th width="4%">世界地图高度</th>
		    <th width="4%">世界地图宽度</th>
		    <th width="4%">世界地图X值</th>
		    <th width="4%">世界地图Y值</th>
		</thead>
		
		<%
		WorldMapArea[] wma = WorldMapManager.getInstance().worldMapAreas;
		for(WorldMapArea w :wma){
			String name = w.getName();
			String pressPng = w.getPressPng();
			short preePngX = w.getPressPngx();
			short preePngY = w.getPressPngy();
			short[] areaH = w.getWorldMapAreaHeight();
			StringBuilder sbHeight = new StringBuilder();
			for(short s:areaH){
				sbHeight.append(s).append(",");
			}
			
			short[] areaW = w.getWorldMapAreaWidth();
			StringBuilder sbAreaWidth = new StringBuilder();
			for(short s:areaW){
				sbAreaWidth.append(s).append(",");
			}
			
			short[] AreaX = w.getWorldMapAreaX();
			StringBuilder sbAreaX = new StringBuilder();
			for(short s:AreaX){
				sbAreaX.append(s).append(",");
			}
			
			
			short[] AreaY = w.getWorldMapAreaY();
			StringBuilder sbAreaY = new StringBuilder();
			for(short s:AreaY){
				sbAreaY.append(s).append(",");
			}
			String varname = URLEncoder.encode(name, "UTF-8");
			String url = request.getContextPath()+"/admin/confMsg/areainfo.jsp?"+"areaname="+varname;
			
			%>
			<tr>
			<td><a href="<%=url %>" >name</a></td>
			<td><%=pressPng %></td>
			<td><%=preePngX %></td>
			<td><%=preePngY %></td>
			<td><%=sbHeight %></td>
			<td><%=sbAreaWidth %></td>
			<td><%=sbAreaX %></td>
			<td><%=sbAreaY %></td>
			</tr>
			<%
		}
		
		%>
		
	</table>
</body>
</html>