<%@ page contentType="text/html;charset=utf-8"%>
<%@ page
	import="java.util.*,com.xuanzhi.tools.text.*,com.xuanzhi.tools.mem.*,com.fy.engineserver.core.*"%>
<%
	
%>
		
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title></title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
</head>

	<body>
		
		<h2 style="font-weight:bold;">
			所有地图信息，上一次启动时间：<%= DateUtil.formatDate(new Date(ObjectTrackerService.serviceStartTime),"yyyy-MM-dd HH:mm:ss") %>
		</h2>
		<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>
		<tr bgcolor='#EEEEBB'><td>在线人数</td><td>国家</td><td>上一次心跳</td><td>显示名称</td>
		<td>名称</td>
		<td>地图名称</td>
		<td>width</td>
		<td>height</td>
		<td>cellw</td>
		<td>cellh</td>
		<td>realSceneName</td>
		<td>--</td>
		<td>--</td>
		</tr>
		<%
		GameManager gm = (GameManager)org.springframework.web.context.support.WebApplicationContextUtils.getWebApplicationContext(application).getBean("game_manager");
		Game games[] = gm.getGames();
		
		for(int i = 0 ; i < games.length ; i++){
			Game g = games[i];
			out.println("<tr bgcolor='#FFFFFF'>");
			out.println("<td>"+g.getNumOfPlayer() +"</td>");
			out.println("<td>"+g.country +"</td>");
			out.println("<td>"+(System.currentTimeMillis() - g.getLastHeartBeatTime())+"ms前</td>");
			out.println("<td>"+g.gi.displayName +"</td>");
			out.println("<td>"+g.gi.name+"</td>");
			out.println("<td>"+g.gi.getMapName()+"</td>");
			out.println("<td>"+g.gi.getWidth()+"</td>");
			out.println("<td>"+g.gi.getHeight()+"</td>");
			out.println("<td>"+g.gi.getCellW()+"</td>");
			out.println("<td>"+g.gi.getCellH()+"</td>");
			out.println("<td>"+g.realSceneName+"</td>");
			out.println("<td><a href='../gameinfo.jsp?game="+g.getGameInfo().getName()+"&country="+g.country+"'>详细信息</a></td>");
			out.println("<td><a href='../bucket.jsp?game="+g.getGameInfo().getName()+"&country="+g.country+"'>详细信息</a></td>");
			
			out.println("</tr>");
		}
		
		
		%>
		</table>
		
		
	</body>
</html>
