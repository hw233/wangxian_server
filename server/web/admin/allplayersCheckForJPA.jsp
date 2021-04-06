<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,com.fy.engineserver.core.*,
com.fy.engineserver.sprite.*,com.xuanzhi.tools.transport.*,com.xuanzhi.tools.queue.*
,com.xuanzhi.tools.simplejpa.*,com.xuanzhi.tools.simplejpa.impl.*,com.xuanzhi.tools.page.PageUtil,
java.lang.reflect.*"%><%! 

%><%
	int start = 1;
	int nPage = 1000;
	

	if(request.getParameter("start") != null){
		start = Integer.parseInt(request.getParameter("start"));
	}
	
	SimpleEntityManager<Player> em = SimpleEntityManagerFactory.getSimpleEntityManager(Player.class);
	
	Field field = em.getClass().getDeclaredField("entityModifedMap");
	field.setAccessible(true);
	HashMap<Long,SimpleEntityManagerOracleImpl.EntityEntry<Player>> entityModifedMap = (HashMap<Long,SimpleEntityManagerOracleImpl.EntityEntry<Player>> )field.get(em);
	
	PlayerManager pm = PlayerManager.getInstance();

	int totalCount = entityModifedMap.size();
%>
<html><head>
</HEAD>
<BODY>
<h2>对比PlayerManager中的Player与EM中的Player</h2>

Cache中总数：<%= pm.getCachedPlayers().length %>,SimpleJPA中的数量：<%= entityModifedMap.size() %><br/>


<%
	out.println(PageUtil.makePageHTML("./allplayersCheckForJPA.jsp",15,"start",start,nPage,(int)totalCount));
%>

<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center">
<td>编号</td>
<td>ID</td>
<td>Online</td>
<td>InCache</td>
<td>Name</td>
<td>账户名</td>
<td>地图</td>
<td>级别</td>
<td>总充值</td>
<td>银子</td>
<td>银子高水位</td>
<td>绑银</td>
<td>最后请求时间</td>
</tr>

<%
        //int n = sm.getAmountOfPlayers();
int i = 0;
		Iterator<Long> it = entityModifedMap.keySet().iterator();
        while(it.hasNext()){
        	i++;
        	Long id = it.next();
        	SimpleEntityManagerOracleImpl.EntityEntry<Player> ee = entityModifedMap.get(id);
        	if(ee == null) continue;
        	if(ee.get() == null) continue;
        	Player p = ee.get();
        	Player p2 = pm.getPlayerInCache(id);
        	String color = "#FFFFFF";
        	if(p2 != null) color = "#BBBBEE";
                %><tr bgcolor="<%=color %>" align="center">
                <td><%=i%></td>
                <td><a href='player/mod_player.jsp?action=select_playerId&playerid=<%=p.getId() %>'><%=p.getId() %></a></td>
                <td><%= pm.isOnline(p.getId()) %></td>
                <td><%= pm.getPlayerInCache(id) != null %></td>
                <td><%= p.getName()%></td>
                <td><%= p.getUsername()%></td>
                <td><%= p.getGame() %></td>
                <td><%= p.getLevel() %></td>
                <td><%= p.getRMB()/100 %>元</td>
                <td><%= StringUtil.addcommas(p.getSilver()) %></td>
                <td><%= StringUtil.addcommas(p.getHighWaterOfSilver()) %></td>
           		 <td><%= StringUtil.addcommas(p.getBindSilver()) %></td>
                <td><%= (System.currentTimeMillis() - p.getLastRequestTime())/1000 %>秒前</td>
                </tr><%
        }
%>
</table>
<%
	out.println(PageUtil.makePageHTML("./allplayersCheck.jsp",15,"start",start,nPage,(int)totalCount));
%>
</BODY></html>

