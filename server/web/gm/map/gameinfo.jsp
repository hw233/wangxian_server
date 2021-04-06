<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,
com.fy.engineserver.sprite.*,com.fy.engineserver.core.*,com.xuanzhi.tools.transport.*"%>
<%@include file="../authority.jsp" %>
<% 
  try{
   String gmname = session.getAttribute("username").toString();
  out.print("<input type='button' value='返回地图列表' onclick='window.location.replace(\"map_list.jsp\")' />");	
	String beanName ="game_manager";
	GameManager sm = null;
	sm = (GameManager)org.springframework.web.context.support.WebApplicationContextUtils.getWebApplicationContext(application).getBean(beanName);
	String gameId = request.getParameter("game");
	Game game = null;
	out.print("<input type='button' value='刷新' onclick='window.location.replace(\"gameinfo.jsp?game="+gameId+"&fb="+request.getParameter("fb")+"&index="+request.getParameter("index")+"\");' />");
	if(request.getParameter("fb") != null && request.getParameter("fb").equals("true")){
		int index = Integer.parseInt(request.getParameter("index"));
		XinShouChunFuBenManager xscfb = XinShouChunFuBenManager.getInstance();
		if(xscfb != null){
			int k = xscfb.indexOf(gameId);
			game = xscfb.getGames()[k][index];
		}
	}else{
		game = sm.getGameByName(gameId);
	}
	
	
	GameInfo gi = game.getGameInfo();
	StringBuffer basicInfo = new StringBuffer();
	
	basicInfo.append("基本信息 ：a="+gi.getA()+",b="+gi.getB()+",rows="+gi.getRows()+",columns="+gi.getColumns()+",signPostNum="+gi.navigator.getSignPostNum()+",roadNum="+gi.navigator.getRoadNum()+",polygonNum="+gi.navigator.getPolygonNum()+"<br/>");
	basicInfo.append("地图数据：size="+gi.getData().length+",file="+gi.getDataFile()+"<br/>");
	basicInfo.append("动态数据：player="+game.getNumOfPlayer()+",queue.eleNum="+(game.getQueue().getPushNum()-game.getQueue().getPopNum())+",queue.push="+game.getQueue().getPushNum()+",queue.pop="+game.getQueue().getPopNum()+"<br/>");
	basicInfo.append("最后一次心跳："+(System.currentTimeMillis() - game.getLastHeartBeatTime())+"毫秒前<br/>");
	StringBuffer sb2 = new StringBuffer();
	sb2.append("</br>");
	TransportData[] tds = gi.getTransports();
	for(int i=0 ;i < tds.length; i++){
		TransportData td = tds[i];
		sb2.append("传送到地图 " + td.getTargetMapName() + " X坐标 " + td.getTargetMapX() + " Y坐标 " + td.getTargetMapY() + "</br>");
	}
	
	basicInfo.append("传送点信息："+ sb2.toString());
	%>
<html><head>
</HEAD>
<BODY>
<h2><%=gameId %>，各个生物的情况</h2><br><a href="./gameinfo.jsp?game=<%=gameId %>">刷新此页面</a><br>
<%= basicInfo.toString() %>
<br>

<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center">
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
		out.println("<tr bgcolor='#FFFFFF' align='center'>");
		out.println("<td>"+(i+1)+"</td>");
		if(los[i] instanceof Player){
			out.println("<td><a href='../gmuser/user_action.jsp?playerid="+((Player)los[i]).getId()+"'>"+((Player)los[i]).getName() +"</a></td>");
		}else{
			out.println("<td>----</td>");
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
	}catch(Exception e){
	        //  RequestDispatcher rdp = request
			//				.getRequestDispatcher("../gmuser/visitfobiden.jsp");
				//	rdp.forward(request, response);
				  out.print(StringUtil.getStackTrace(e));
	}
%>
</table>	
</BODY></html>
