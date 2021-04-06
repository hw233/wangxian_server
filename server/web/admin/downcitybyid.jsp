<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,
com.fy.engineserver.sprite.*,com.fy.engineserver.sprite.concrete.*,com.fy.engineserver.core.*,java.lang.reflect.*,com.google.gson.*,com.fy.engineserver.sprite.monster.*,com.fy.engineserver.downcity.*"%><%! 
	
	String aaa(String s,int len){
		StringBuffer sb = new StringBuffer();
		char chars[] = s.toCharArray();
		int c = 0;
		for(int i = 0 ; i < chars.length ; i++){
			sb.append(chars[i]);
			c++;
			if( c >= len && (chars[i] == ',' || chars[i] == '{' || chars[i] == '}' || chars[i] == ':')){
				sb.append("<br/>");
				c = 0;
			}
		}
		return sb.toString();
	}
%>
<%@include file="IPManager.jsp" %><html><head>
<link rel="stylesheet" type="text/css" href="../css/common.css" />
<link rel="stylesheet" type="text/css" href="../css/table.css" />
<style type="text/css">
.titlecolor{
background-color:#C2CAF5;
}
</style>
</HEAD>
<BODY>
<h2></h2><br><br>
<%
DownCityManager dcm = DownCityManager.getInstance();
String downCityId = request.getParameter("downcityid");
if(dcm != null && downCityId != null){
	DownCity dciList = dcm.getDownCityById(downCityId);
	if(dciList != null){
		%>
		<table>
		<tr class="titlecolor">
		<td>副本ID</td><td>副本配置信息</td><td>副本对应的地图</td><td>创建时间</td><td>结束时间</td><td>保留此副本进度的玩家</td><td>threadIndex</td>
		</tr>
		<tr>
			<td><%=dciList.getId() %></td><td><%=dciList.getDi() != null ? dciList.getDi().getName() : "" %></td><td><%=dciList.getGame() != null ? dciList.getGame().getGameInfo().getName()+"("+dciList.getGame().getNumOfPlayer()+")" : "无副本地图" %></td><td><%=dciList.getCreateTime() %></td><td><%=dciList.getEndTime() %></td>
			<td>
			<%
			StringBuffer sbb = new StringBuffer();
			Player keepProcessPlayerList[] = dciList.getPlayersKeepingProcess();
			if(keepProcessPlayerList != null){
				for(Player player : keepProcessPlayerList){
					if(player != null){
						sbb.append("<a href='player/mod_player_commondata.jsp?action=select_playerId&playerid="+player.getId()+"'>"+player.getName() +"</a> ");
					}
				}
			}
			out.println(sbb.toString());
			%>
			</td>
			<td><%=dciList.getThreadIndex() %></td>
		</tr>
		</table>
		<%
		out.println("副本地图信息:<br>");
		Game game = dciList.getGame();
		GameInfo gi = game.getGameInfo();
		StringBuffer basicInfo = new StringBuffer();
		
		basicInfo.append("基本信息：CellH="+gi.getCellH()+",CellW="+gi.getCellW()+",Height="+gi.getHeight()+",Width="+gi.getWidth()+",signPostNum="+gi.navigator.getSignPostNum()+",roadNum="+gi.navigator.getRoadNum()+",polygonNum="+gi.navigator.getPolygonNum()+"<br/>");
		basicInfo.append("地图数据：file="+gi.getDataFile()+"<br/>");
		basicInfo.append("动态数据：player="+game.getNumOfPlayer()+",queue.eleNum="+(game.getQueue().getPushNum()-game.getQueue().getPopNum())+",queue.push="+game.getQueue().getPushNum()+",queue.pop="+game.getQueue().getPopNum()+"<br/>");
		basicInfo.append("最后一次心跳："+(System.currentTimeMillis() - game.getLastHeartBeatTime())+"毫秒前<br/>");
		%>
<%= basicInfo.toString() %>
<br>
<table>
<tr class="titlecolor">
<td>编号</td>
<td>名字</td>
<td>ID</td>
<td>精灵类型</td>
<td>上一次心跳时间</td>
<td>X坐标</td>
<td>Y坐标</td>
<td>移动路径</td>
<td>目标时间</td>
<td>链接状态</td>
<td>链接标识</td>
<td>链接地址</td>
</tr>	
<%
	LivingObject[] los = game.getLivingObjects();
	for(int i = 0 ; i < los.length ; i++){
		out.println("<tr>");
		out.println("<td>"+(i+1)+"</td>");
		if(los[i] instanceof Player){
			out.println("<td><a href='player/mod_player_commondata.jsp?action=select_playerId&playerid="+los[i].getId()+"'>"+((Player)los[i]).getName() +"</a></td>");
		}else if(los[i] instanceof Monster){
			Monster monster = (Monster)los[i];
			out.println("<td><a href='spriteauto.jsp?sid="+monster.getId()+"'>"+monster.getName()+"</a></td>");
		}else if(los[i] instanceof Sprite){
			Sprite monster = (Sprite)los[i];
			out.println("<td><a href='npcauto.jsp?sid="+monster.getId()+"'>"+monster.getName()+"</a></td>");
		}else{
			out.println("<td>---</td>");
		}
		out.println("<td>"+los[i].getId()+"</td>");
		out.println("<td>"+los[i].getClassType()+"</td>");
		out.println("<td>"+los[i].getInterval()+"毫秒前</td>");
		out.println("<td>"+los[i].getX() +"</td>");
		out.println("<td>"+los[i].getY() +"</td>");
		if(los[i].getMoveTrace() != null){
			MoveTrace mt = los[i].getMoveTrace();
			StringBuffer sb = new StringBuffer();
			sb.append("<td>");
			for(int j = 0 ; j < mt.getRoadLen().length ; j++){
				if(mt.getCurrentRoad() == j){
					sb.append("<b>("+mt.getRoadPoints()[j].x+","+mt.getRoadPoints()[j].y+")--></b>");
				}else if(mt.getCurrentRoad() +1 == j){
					sb.append("<b>("+mt.getRoadPoints()[j].x+","+mt.getRoadPoints()[j].y+")</b>->");
				}else{
					sb.append("("+mt.getRoadPoints()[j].x+","+mt.getRoadPoints()[j].y+")->");
				}
				
				if(j == mt.getRoadLen().length-1){
					sb.append("("+mt.getRoadPoints()[mt.getRoadLen().length].x+","+mt.getRoadPoints()[mt.getRoadLen().length].y+")");
				}
			}
			sb.append("</td>");
			out.println(sb.toString());
			out.println("<td>"+((mt.getDestineTimestamp() - los[i].getHeartBeatStartTime())/1000)+"秒后</td>");
			
		}else{
			out.println("<td>-</td>");
			out.println("<td>-</td>");
			
		}
		if(los[i] instanceof Player){
			Player p = (Player)los[i];
			out.println("<td>"+p.getConn().getStateString(p.getConn().getState())+"</td>");
			out.println("<td>"+p.getConn().getIdentity()+"</td>");
			out.println("<td>"+p.getConn().getRemoteAddress()+"</td>");
		}else{
			out.println("<td>---</td>");
			out.println("<td>---</td>");
			out.println("<td>---</td>");
		}
		out.println("</tr>");
	}
%>
</table>
		<%
	}
}else{
	out.println("副本管理器为空或副本ID为空");
}
%>
</BODY></html>
