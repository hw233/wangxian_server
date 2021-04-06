<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,com.fy.engineserver.core.*,
com.fy.engineserver.sprite.*,com.fy.engineserver.lineup.*,com.xuanzhi.tools.transport.*,com.xuanzhi.tools.queue.*"%><%!
%><%!
public String listToRoles(List<LineupPlayer> ps) {
	StringBuffer sb = new StringBuffer();
	for(LineupPlayer pp : ps) {
		if(pp.player.isOnline())
			sb.append(TeamRole.getRoleDesp(pp.teamrole ) + "<br>\n");
	}
	return sb.toString();
}

public String listToPlayerNames(List<LineupPlayer> ps) {
	StringBuffer sb = new StringBuffer();
	for(LineupPlayer pp : ps) {
		if(pp.player.isOnline())
			sb.append(pp.player.getId() + "/" + pp.player.getName() + "<br>\n");
	}
	return sb.toString();
}
 %>
<%@include file="IPManager.jsp" %><html><head>
<!--[if IE]><script language="javascript" type="text/javascript" src="/game_server/js/flotr/lib/excanvas.js"></script><![endif]-->
<script language="javascript" type="text/javascript" src="/game_server/js/flotr/lib/prototype-1.6.0.2.js"></script>
<script language="javascript" type="text/javascript" src="/game_server/js/flotr/flotr-0.2.0-alpha.js"></script>
</HEAD>
<BODY onload='draw_Flotr();'>
<h2>副本排队情况</h2>
<a href='./lineup.jsp' >刷新此页面</a>
<br><br>
<table border='0' cellpadding='0' cellspacing='1' width='100%' bgcolor='#000000' align='center'>
<tr align='center' bgcolor='#efefef'>
<td>副本</td>
<td>排队玩家数量</td>
<td>玩家</td>
<td>角色</td>
</tr>
<%
Hashtable<String, List<LineupPlayer>> lpmap = LineupManager.getInstance().lineupMap;
String keys[] = lpmap.keySet().toArray(new String[0]);
for(String city : keys) {
	List<LineupPlayer> clist = lpmap.get(city);
%>
<tr align='center' bgcolor='#FFFFFF'>
	<td><%=city %></td>
	<td><%=clist.size() %></td>
	<td><%=listToPlayerNames(clist)%></td>
	<td><%=listToRoles(clist)%></td>
</tr>
<%} %>
</table>
</BODY></html>
