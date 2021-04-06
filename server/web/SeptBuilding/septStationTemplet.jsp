<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="inc.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%
	SeptStationMapTemplet templet = SeptStationMapTemplet.getInstance();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>驻地建筑位置信息</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" type="text/css" href="../SeptBuilding/css/septStation.css">
  </head>
  
  <body>
  	<table border="1">
  		<tr class="head">
  			<td>位置索引</td>
  			<td>X坐标</td>
  			<td>Y坐标</td>
  		</tr>
  		<%
  			int i = 0;
  			for (Iterator<Integer> it = templet.getMap().keySet().iterator(); it.hasNext();) {
  				i++;
  				int index = it.next();
  				NpcStation nStation = templet.getMap().get(index);
  		 %>
  		 	<tr style="background-color : <%=(i%2==0)? "#A5EDDE" : ""%>">
  		 		<td><%=nStation.getIndex() %></td>
  		 		<td><%=nStation.getX() %></td>
  		 		<td><%=nStation.getY() %></td>
  		 	</tr>
  		 <%
  		 	}
  		  %>
  	</table>
  </body>
</html>
