<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,com.fy.engineserver.core.*,
com.fy.engineserver.sprite.*,com.xuanzhi.tools.transport.*,com.xuanzhi.tools.queue.*"%><%

	PlayerManager sm = PlayerManager.getInstance();
     
	Player player = sm.getPlayer(Long.parseLong(request.getParameter("playerid")));


	
%>
<%
	String action = request.getParameter("action");
	if(action != null && action.equals("jingao")){
		HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,"您好，我是GM，我们发现你正在使用加速外挂，特此警告！请立即下线并使用官方客户端。如果你再移动一步，将立即作封号一周处理！");
		player.addMessageToRightBag(req);
		out.println("<br><h3>已发送警告信息：["+req.getHintContent()+"] 给玩家"+player.getName()+"</h3><br>");
	}

	if(action != null && action.equals("jingao2")){
		long playerid = Long.parseLong(request.getParameter("playerid"));
		HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,"您好，我是GM，我们发现你正在使用加速外挂，并且警告后仍然使用加速，将作封号一周处理，10秒后将自动断线！");
		player.addMessageToRightBag(req);
		out.println("<br><h3>已发送警告信息：["+req.getHintContent()+"] 给玩家"+player.getName()+"</h3><br>");
	}

	if(action != null && action.equals("tiren")){
		if(player.getConn() != null){
			player.getConn().close();
		}
		out.println("<br><h3>已经将玩家"+player.getName()+"提下线！</h3><br>");
	}

	if(action != null && action.equals("sendmsg")){
		String msg = request.getParameter("msg");
		HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,msg);
		player.addMessageToRightBag(req);
		out.println("<br><h3>已发送警告信息：["+req.getHintContent()+"] 给玩家"+player.getName()+"</h3><br>");
	}
	
%>

<%@page import="com.fy.engineserver.stat.StatData"%>
<%@page import="com.fy.engineserver.message.HINT_REQ"%>
<%@page import="com.fy.engineserver.message.GameMessageFactory"%>
<html><head>
</HEAD>
<BODY>
<h2>加速外挂：<%= player.getName() + "，  "+(player.isOnline()?"在线":"不在线")%></h2><br><a href="./list_player_jiasu.jsp?playerid=<%= player.getId() %>">更新</a> | <a href="./check_players_jiasu.jsp">返回</a><br><br>
<h2>外挂标准：“路径速度”稳定的远远大于“实际客户端速度”，如果只是偶尔大于，也有很多情况相等，那么就不是外挂。其中9999不能作为判断标准</h2>
<b>前期出现的外挂是双倍移动速度，系统已经帮您标红，发送警告后，请持续关注此用户是否恢复正常，恢复正常的标记是下线，在上线。然后“路径速度”=“实际客户端速度”</b>
<br>
<a href="./list_player_jiasu.jsp?action=jingao&playerid=<%=player.getId() %>">发送警告</a>|<a href="./list_player_jiasu.jsp?action=jingao2&playerid=<%=player.getId() %>">发送封号信息</a>|<a href="./list_player_jiasu.jsp?action=tiren&playerid=<%=player.getId() %>">踢下线</a>
<br>
<br>发送信息给此用户：
<form id='dd'>
<input type='hidden' name='action' value='sendmsg'>
<input type='hidden' name='playerid' value='<%= player.getId() %>'>
<input type='text' name='msg' value='' size='50'>
<input type='submit' value='发送'>
</form>
<br><br>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center">
<td>心跳时间</td>
<td>客户端时间</td>
<td>客户端速度</td>

<td><b>路径速度</b></td>
<td><b>实际客户端速度</b></td>

<td>路径接收时间</td>
<td>路径行走时长</td>
<td>真实行走时长</td>
<td>路径速度</td>
<td>真实速度</td>
<td>路径长度</td>
<td>停止坐标</td>
<td>路径</td>
</tr>
<%
	Iterator<Player.HEARTBEATCHECKREQ> it = player.__checkReqList.iterator();
	while(it.hasNext()){
		Player.HEARTBEATCHECKREQ r = it.next();
		MoveTrace paths[] = r.paths.toArray(new MoveTrace[0]);
		if(paths.length > 0){
			for(int i = 0 ; i < paths.length ; i++){
				MoveTrace p = paths[i];
				String color = "#FFFFFF";
				if(p.pathSpeed != 9999 && p.pathSpeed > p.clientSpeed * 150/100 && p.getTotalLength() > 20){
					color = "#FF0000";
				}
				
				out.println("<tr bgcolor='"+color+"' align='center'>");
				int cc = r.paths.size();
				if(cc == 0) cc = 1;
				
				if(i == 0){
				out.println("<td rowspan='"+cc+"'>"+DateUtil.formatDate(new Date(r.receiveTime),"HH:mm:ss")+"</td>");
				out.println("<td rowspan='"+cc+"'>"+(System.currentTimeMillis() - r.gameTimeOfClient)+"ms</td>");
				out.println("<td rowspan='"+cc+"'>"+(r.speed)+"</td>");
				}
				out.println("<td><b>"+p.pathSpeed+"</b></td>");
				out.println("<td><b>"+p.clientSpeed+"</b></td>");
				out.println("<td>"+DateUtil.formatDate(new Date(p.receiveTimestamp),"HH:mm:ss,S")+"</td>");
				
				out.println("<td>"+ (p.getDestineTimestamp() - p.clientGameTimestamp)+"ms</td>");
				if(p.realEndTime > 0){
					out.println("<td>"+ (p.realEndTime - p.receiveTimestamp)+"ms</td>");
				}else{
					out.println("<td>---</td>");
				}
				out.println("<td>"+p.pathSpeed+"</td>");
				out.println("<td>"+p.realSpeed+"</td>");
				out.println("<td>"+p.getTotalLength()+"</td>");
				out.println("<td>"+p.realEndX + ","+ p.realEndY+"</td>");
				out.println("<td>"+p.toString()+"</td>");
				
			}
		}else{
			out.println("<tr bgcolor='#FFFFFF' align='center'>");
			int cc = r.paths.size();
			if(cc == 0) cc = 1;
			
			out.println("<td rowspan='"+cc+"'>"+DateUtil.formatDate(new Date(r.receiveTime),"HH:mm:ss")+"</td>");
			out.println("<td rowspan='"+cc+"'>"+(System.currentTimeMillis() - r.gameTimeOfClient)+"ms</td>");
			out.println("<td rowspan='"+cc+"'>"+(r.speed)+"</td>");
			
			out.println("<td></td>");
			out.println("<td></td>");
			out.println("<td></td>");
			out.println("<td></td>");
			out.println("<td></td>");
			out.println("<td></td>");
			out.println("<td></td>");
			out.println("<td></td>");
			out.println("<td></td>");
			out.println("<td></td>");
		}
		out.println("</tr>");
	}

%>
</table>
</BODY></html>
