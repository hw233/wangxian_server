<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,com.fy.engineserver.core.*,
com.fy.engineserver.sprite.*,com.xuanzhi.tools.transport.*,com.xuanzhi.tools.queue.*"%><%
        PlayerManager sm = PlayerManager.getInstance();
     
	Player.JIASHU_INFO infos[] = Player.getAllJIASHU_INFO();

	
%>
<%@page import="com.fy.engineserver.message.GameMessageFactory"%>
<%@page import="com.fy.engineserver.message.HINT_REQ"%>
<html><head>
</HEAD>
<BODY>
<h2>24小时内使用加速外挂的情况</h2><br><a href="./check_players_jiasu2.jsp">刷新此页面</a><br><br>
<h2>一定要查看详情，才能确认是否为加速外挂</h2>
<%
	String action = request.getParameter("action");
	if(action != null && action.equals("jingao")){
		long playerid = Long.parseLong(request.getParameter("playerid"));
		Player player = sm.getPlayer(playerid);
		HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,"您好，我是GM，我们发现你正在使用加速外挂，特此警告，请不要再使用！否则将作封号一周处理！");
		player.addMessageToRightBag(req);
		out.println("<br><h3>已发送警告信息：["+req.getHintContent()+"] 给玩家"+player.getName()+"</h3><br>");
	}
	if(action != null && action.equals("tiren")){
		long playerid = Long.parseLong(request.getParameter("playerid"));
		Player player = sm.getPlayer(playerid);
		if(player.getConn() != null){
			player.getConn().close();
		}
		out.println("<br><h3>已经将玩家"+player.getName()+"提下线！</h3><br>");
	}

%>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center">
<td>编号</td>
<td>ID</td>
<td>查看详情</td>
<td>是否在线</td>
<td>最后一次检测加速时间</td>
<td>5分钟内</td>
<td>1小时内</td>
<td>24小时内</td>
<td>帐号</td>
<td>角色</td>
<td>级别</td>
<td>系统强制中断次数</td>
<td>操作</td>
<td>操作</td>


</tr>
<%
        //int n = sm.getAmountOfPlayers();

        for(int i = 0 ; i < infos.length ; i++){
        		String color = "#FFFFFF";
        		if(sm.isOnline(infos[i].playerId) == false){
        			color = "#888888";
        		} 
        		
                %><tr bgcolor="<%=color %>" align="center">
                <td><%=i+1 %></td>
                <td><a href='mod_player.jsp?action=select_playerId&playerid=<%=infos[i].playerId %>'><%=infos[i].playerId %></a></td>
                 <td><a href='list_player_jiasu.jsp?playerid=<%=infos[i].playerId%>'>详情</a></td>
                 <td><%= sm.isOnline(infos[i].playerId)?"在线":"不在线" %></td>
                <td><%= DateUtil.formatDate(new Date(infos[i].lastJiaShuTime),"HH:mm:ss") %></td>
                <td><%= infos[i].jiaShuCountInFiveMinutes %></td>
                <td><%= infos[i].jiaShuCountInOneHour %></td>
                <td><%= infos[i].jiaShuAmount %></td>
                <td><%= infos[i].userName %></td>
                <td><%= infos[i].playerName %></td>
                <td><%= infos[i].playerLevel %></td>
                <td><%= infos[i].forceToTerminateConnectionTimes %></td>
                <td>---</td>
           		<td>---</td>     
				</tr><%
        }
%>
</table>
</BODY></html>
