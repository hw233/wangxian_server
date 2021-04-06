<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="com.fy.engineserver.country.manager.CountryManager"%>
<%@page import="com.fy.engineserver.country.data.WangZheTransferPointOnMap"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>王者之印</title>
<link rel="stylesheet" type="text/css" href="../css/conf.css" />
</head>
<body>
<table border=1 cellspacing=0  width=100% bordercolorlight=#333333 bordercolordark=#efefef>

		<thead>
		    <th width="20%">传送位置名字</th>
		    <th width="20%">X值</th>
		    <th width="20%">Y值</th>
		    <th width="20%">半径范围</th>
		    <th width="20%">地图名</th>
  		</thead>
		<%
		HashMap<String, WangZheTransferPointOnMap> hm = CountryManager.getInstance().chinaMapNameTransferPoints;
		Iterator<Entry<String,WangZheTransferPointOnMap>> itor = hm.entrySet().iterator();
		while(itor.hasNext()){
			Entry<String,WangZheTransferPointOnMap>  entry= itor.next();
			WangZheTransferPointOnMap wztp = entry.getValue();
			short[] px = wztp.getPointsX();
			StringBuilder sbx = new StringBuilder();
			for(short s : px){
				sbx.append(s).append(",");
			}
			
			StringBuilder sby = new StringBuilder();
			short[] py = wztp.getPointsY();
			for(short s :py){
				sby.append(s).append(",");
			}
			
			StringBuilder rangesb = new StringBuilder();
			short[] ranges = wztp.getRanges();
			for(short s : ranges){
				rangesb.append(s).append(",");
			}
			
			StringBuilder sbPointNames = new StringBuilder();
			String[] pointNames = wztp.pointNames;
			for(String s :pointNames){
				sbPointNames.append(s).append(",");
			}
			
		
			%>
			<tr>
			<td><%=wztp.getDisplayMapName() %></td>
			<td><%=sbx %></td>
			<td><%=sby %></td>
			<td><%=rangesb  %></td>
			<td><%=wztp.getMapName() %></td>
			</tr>
			<%
		}
		%>
		
	</table>
</body>
</html>