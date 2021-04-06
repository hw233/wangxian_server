<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,
com.fy.engineserver.sprite.*,com.fy.engineserver.sprite.concrete.*,
com.fy.engineserver.core.*,
com.xuanzhi.tools.transport.*"%><% 
	
	GamePlayerManager sm = (GamePlayerManager)PlayerManager.getInstance();
	CoreSubSystem core = CoreSubSystem.getInstance();
	
	Player sprites[] = sm.getCachedPlayers();
	
	Player players[] = sm.getOnlinePlayers();
	
	GameManager gm = GameManager.getInstance();
	Game games[] = gm.getGames();
	HashMap<Long,Player> map = new HashMap<Long,Player>();
	ArrayList<Player> al = new ArrayList<Player>();
	for(int i = 0 ; i < games.length ; i++){
		LivingObject los[] = games[i].getLivingObjects();
		for(int j = 0 ; j < los.length ; j++){
			if(los[j] instanceof Player){
				Player p = (Player)los[j];
				map.put(p.getId(),p);
			}
		}
	}
	
	XinShouChunFuBenManager xxx = XinShouChunFuBenManager.getInstance();
	if(xxx != null){
		Game gamess[][] = xxx.getGames();
		for(int i = 0 ; i < gamess.length ; i++){
			for(int k = 0 ;k < gamess[i].length ; k++){
				LivingObject los[] = gamess[i][k].getLivingObjects();
				for(int j = 0 ; j < los.length ; j++){
					if(los[j] instanceof Player){
						Player p = (Player)los[j];
						map.put(p.getId(),p);
					}
				}
			}
		}
	}
	
	
	
	%>
<%@include file="IPManager.jsp" %><html><head>
</HEAD>
<BODY>
<h2>Game Server，所有玩家的情况，在线用户：<%= players.length %>，正在等待的用户：<%= core.waitingEnterGameConnections.size() %>，加载的用户：<%= sprites.length %></h2>
<br><a href="./players.jsp">刷新此页面</a><br><br>

<br>存储所有用户的数据，系统重启前必须提交，否则会有玩家数据丢失: <form id='f'>
<input type='hidden' name='action' value='saveallplayer'>
<input type='submit' value='提交'>
</form>
<%
	if(request.getParameter("action") != null && request.getParameter("action").equals("saveallplayer")){
		long startTime = System.currentTimeMillis();
		//sm.saveAllPlayers();
		out.println("<br>所有用户数据存储完毕，耗时："+(System.currentTimeMillis() - startTime)+"ms<br>");
	}
%>
<br>
内存中存在多份实例的用户列表：<br/>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center"><td>编号</td><td>ID</td><td>Online</td><td>Identity</td><td>Name</td><td>账户名</td><td>地图</td>
<td>级别</td>
<td>血</td>
<td>蓝</td>
<td>经验值</td>

<td>最后一次接收数据的时间</td><td>X坐标</td><td>Y坐标</td></tr>	

<%
	for(int i = 0 ; i < sprites.length ; i++){
		Player p2 = map.get(sprites[i].getId());
		if(sprites[i]!= p2 && p2 != null){
			try{
				%><tr bgcolor="#FFFFFF" align="center">
				<td><%=i+1 %></td>
				<td><a href='player/mod_player.jsp?action=select_playerId&playerid=<%=sprites[i].getId() %>'><%=sprites[i].getId() %></a></td>
				<td><%= (sprites[i].getConn() != null && sprites[i].getConn().getState() != Connection.CONN_STATE_CLOSE) %></td>
				<td><%= (sprites[i].getConn()!=null?sprites[i].getConn().getIdentity():"无连接") %></td>
				<td><%= sprites[i].getName()%></td>
				<td><%= sprites[i].getUsername()%></td>
				<td><%= sprites[i].getGame() %></td>
				<td><%= sprites[i].getLevel() %></td>
				<td><%= sprites[i].getHp()+"/" + sprites[i].getMaxHP() %></td>
				<td><%= sprites[i].getMp()+"/" + sprites[i].getMaxMP() %></td>
				<td><%= sprites[i].getExp()+"/" + sprites[i].getNextLevelExp() %></td>
				<td><%= (System.currentTimeMillis() - sprites[i].getLastRequestTime())/1000 %>秒前</td>
				<td><%=sprites[i].getX() %></td>
				<td><%=sprites[i].getY() %></td></tr><%
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}
%>
</BODY></html>
