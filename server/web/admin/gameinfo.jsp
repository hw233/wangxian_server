<%@page import="com.fy.engineserver.sprite.monster.Monster"%>
<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,
com.fy.engineserver.sprite.*,com.fy.engineserver.core.*,com.xuanzhi.tools.transport.*"%><% 
	
	String beanName ="game_manager";
	GameManager sm = null;
	sm = (GameManager)org.springframework.web.context.support.WebApplicationContextUtils.getWebApplicationContext(application).getBean(beanName);
	String gameId = request.getParameter("game");
	Game game = null;
	
	if(request.getParameter("fb") != null && request.getParameter("fb").equals("true")){
		int index = Integer.parseInt(request.getParameter("index"));
		XinShouChunFuBenManager xscfb = XinShouChunFuBenManager.getInstance();
		if(xscfb != null){
			int k = xscfb.indexOf(gameId);
			game = xscfb.getGames()[k][index];
		}
	}else{
		String country = request.getParameter("country");
		game = sm.getGameByName(gameId,Integer.valueOf(country));
	}
	
	
	GameInfo gi = game.getGameInfo();
	StringBuffer basicInfo = new StringBuffer();
	basicInfo.append("地图名(name):"+gi.getName()+"<br>");
	basicInfo.append("版本号:" + gi.getVersion() + "<br/>");
	basicInfo.append("基本信息：CellH="+gi.getCellH()+",CellW="+gi.getCellW()+",Height="+gi.getHeight()+",Width="+gi.getWidth()+",signPostNum="+gi.navigator.getSignPostNum()+",roadNum="+gi.navigator.getRoadNum()+",polygonNum="+gi.navigator.getPolygonNum()+"<br/>");
	basicInfo.append("地图数据：file="+gi.getDataFile()+"<br/>");
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
	
	MapArea[] mas = gi.getMapAreas();
	basicInfo.append("区域数量:"+mas.length+"</br>");
	
	for(MapArea ma : mas){
		basicInfo.append(ma.name + " 区域:x="+ma.x+" y="+ma.y+" width="+ma.width+" height=" + ma.height + " type="+ ma.type +"</br>");	
	}

	MapPolyArea[] mass = gi.mapPolyAreas;
	basicInfo.append("多边型区域数量:"+mass.length+"</br>");
	
	for(MapPolyArea ma : mass){
		basicInfo.append(ma.name + " type="+ ma.type +"</br>");	
	}


	MonsterFlushAgent fa = game.getSpriteFlushAgent();
	
	MonsterFlushAgent.BornPoint[] bps = fa.getBornPoints4SpriteCategoryId(100100009);
	
	java.lang.reflect.Field isAlive = MonsterFlushAgent.BornPoint.class.getDeclaredField("isAlive");
	isAlive.setAccessible(true);
	
	java.lang.reflect.Field lastDisappearTime = MonsterFlushAgent.BornPoint.class.getDeclaredField("lastDisappearTime");
	lastDisappearTime.setAccessible(true);
	
	java.lang.reflect.Field sprite = MonsterFlushAgent.BornPoint.class.getDeclaredField("sprite");
	sprite.setAccessible(true);
	
	
	for(int i = 0 ; i < bps.length ; i++){
		MonsterFlushAgent.BornPoint o = bps[i];
		
		com.fy.engineserver.sprite.monster.Monster mm = (com.fy.engineserver.sprite.monster.Monster)sprite.get(o);
		if(mm != null){
			out.println("isAlive=" + isAlive.getBoolean(o) + ",lastDisappearTime=" + (System.currentTimeMillis() - lastDisappearTime.getLong(o))
					+",sprite={hp="+mm.getHp() +","+mm.getState()+",x="+mm.getX()+",y="+mm.getY()+"}");
			
			if(mm.getState() == 5 && mm.getHp() > 0){
				isAlive.setBoolean(o,false);
			}
		}
	}
	
	
	%>

<%@include file="IPManager.jsp" %>
<%@page import="com.fy.engineserver.core.res.MapArea"%>
<%@page import="com.fy.engineserver.core.res.MapPolyArea"%><html><head>
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
			out.println("<td><a href='./player/query_player.jsp?action=select_player&playerid="+los[i].getId()+"'>"+((Player)los[i]).getName() +"</a></td>");
		}else if(los[i] instanceof com.fy.engineserver.sprite.Sprite){
			out.println("<td>"+((com.fy.engineserver.sprite.Sprite)los[i]).getName()+"</td>");
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
			out.println("<td>"+los[i].getAvataRace() + "/" + los[i].getAvataSex()+"</td>");
			out.println("<td>" + ((los[i] instanceof Monster ? ((Monster)los[i]).getObjectScale() + "-" + ((Monster)los[i]).isObjectOpacity() + "-" + ((Monster)los[i]).getObjectColor()  : "")) +"</td>");
			out.println("<td>---</td>");
		}
		out.println("</tr>");
	}
%>
</table>	
</BODY></html>
