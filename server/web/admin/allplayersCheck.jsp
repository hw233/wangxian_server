<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,com.fy.engineserver.core.*,
com.fy.engineserver.sprite.*,com.xuanzhi.tools.transport.*,com.xuanzhi.tools.queue.*
,com.xuanzhi.tools.simplejpa.*,com.xuanzhi.tools.simplejpa.impl.*,com.xuanzhi.tools.page.PageUtil "%><%! 
	
	public static interface SimplePlayerInfo{
	
		public long getId();
		public String getName();
		public String getUsername();
		public String getGame();
		public int getLevel();
		public long getSilver();
		public long getHighWaterOfSilver();
		public long getBindSilver();
		public int getX();
		public int getY();
		
	}
%><%
	int start = 1;
	int nPage = 50;
	int orderType = 0;
	
	String orderBy = "";
	String selectIndex = request.getParameter("selectIndex");
	if(selectIndex == null) selectIndex = "1";
	if(selectIndex != null){
		if(selectIndex.equals("1")){//按照银子排序
			orderBy = "silver desc";
		}else if(selectIndex.equals("2")){//按照绑银排序
			orderBy = "highWaterOfSilver desc";
		}else if(selectIndex.equals("3")){//按照身上的东西排序，东西名称为下面指定的名称
			orderBy = "bindSilver desc";
		}else if(selectIndex.equals("4")){//按照身上的东西排序，东西名称为下面指定的名称
			orderBy = "level desc";
		}else if(selectIndex.equals("5")){
			orderBy = "highWaterOfSilver desc";
		}
		else{
			out.println("请输入的要查找的东西");
		}
	}
	
	if(request.getParameter("start") != null){
		start = Integer.parseInt(request.getParameter("start"));
	}
	
	SimpleEntityManager<Player> em = SimpleEntityManagerFactory.getSimpleEntityManager(Player.class);
	long totalCount = em.count();
	
	long ids[] = null;
	if(selectIndex.equals("5")){
		ids = em.queryIds(Player.class,"RMB < 1000 and level < 50 and highWaterOfSilver > 10000000",orderBy,(start-1)*nPage+1,start*nPage+1);
		
	}else{
		ids = em.queryIds(Player.class,"",orderBy,(start-1)*nPage+1,start*nPage+1);
	}
	List<SimplePlayerInfo> pList = em.queryFields(SimplePlayerInfo.class,ids);

	
	
	HashMap<Long,Long> map = new HashMap<Long,Long> ();
	java.sql.Connection conn = null;
	
	String sql = "";
	if(SimpleEntityManagerFactory.getDbType().equals("oracle")){
		SimpleEntityManagerOracleImpl<Player> emO = (SimpleEntityManagerOracleImpl<Player>)em;
		conn = emO.getConnection();		
		sql = "select id,rmb from PLAYER where id in (";
	}else{
		SimpleEntityManagerMysqlImpl<Player> emO = (SimpleEntityManagerMysqlImpl<Player>)em;
		conn = emO.getConnection();
		sql = "select id,rmb from PLAYER_S1 where id in (";
	}
	for(int i = 0 ; i < ids.length ; i++){
		sql += ids[i];
		if(i < ids.length -1){
			sql +=",";
		}
	}
	sql += ")";
	
	java.sql.Statement stmt = conn.createStatement();
	java.sql.ResultSet rs = stmt.executeQuery(sql);
	while(rs.next()){
		long id = rs.getLong(1);
		long rmb = rs.getLong(2);
		map.put(id,rmb);
	}
	rs.close();

	
	if(SimpleEntityManagerFactory.getDbType().equals("oracle")){
		sql = "select sum(silver),sum(bindSilver) from player";
	}else{
		sql = "select sum(silver),sum(bindSilver) from PLAYER_S1";
	}
	rs = stmt.executeQuery(sql);
	long totalSilver = 0;
	long totalBindSilver = 0;
       	if(rs.next()){
                totalSilver = rs.getLong(1);
                totalBindSilver = rs.getLong(2);
        }
        rs.close();

	stmt.close();
	conn.close();
	
	PlayerManager pm = PlayerManager.getInstance();
	
	if("removermb".equals(request.getParameter("action"))){
		long pid = Long.parseLong(request.getParameter("pid"));
		Player p = pm.getPlayer(pid);
		long ssss = p.getSilver();
		System.out.println("["+(new Date())+"] [抹去刷出来的影子] ["+p.getUsername()+"] ["+p.getName()+"] [银子:"+StringUtil.addcommas(ssss)+"]");
		p.setSilver(0L);
		out.println("["+(new Date())+"] [抹去刷出来的影子] ["+p.getUsername()+"] ["+p.getName()+"] [银子:"+StringUtil.addcommas(ssss)+"]<br>");		
	}

%>
<html><head>
</HEAD>
<BODY>
<h2>检查玩家身上的属性，从数据库中直接查询，速度很慢，请耐心等待。。。</h2>

角色总数：<%=totalCount %>，存量银子：<%=StringUtil.addcommas(totalSilver)%>，存量绑银：<%=StringUtil.addcommas(totalBindSilver)%><br/>
<form id="f2" name="f2" method="get">
<select name="selectIndex">
<option value="1">按照银子排序
<option value="2">按照银子的高水位排序
<option value="3">按照绑银排序
<option value="4">按照等级排序
<option value="5">按刷钱排序</select>
<input type="submit" value="排序">
</form>

<%
	out.println(PageUtil.makePageHTML("./allplayersCheck.jsp?selectIndex="+selectIndex+"",15,"start",start,nPage,(int)totalCount));
%>

<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center">
<td>编号</td>
<td>ID</td>
<td>Online</td>
<td>Name</td>
<td>账户名</td>
<td>地图</td>
<td>级别</td>
<td>总充值</td>
<td>银子</td>
<td>银子高水位</td>
<td>绑银</td>
<td>X坐标</td>
<td>Y坐标</td><td>操作</td>
</tr>

<%
        //int n = sm.getAmountOfPlayers();

        for(int i = 0 ; i < pList.size()        ; i++){
        	SimplePlayerInfo p = pList.get(i);
                %><tr bgcolor="#FFFFFF" align="center">
                <td><%=i+1 %></td>
                <td><a href='player/mod_player.jsp?action=select_playerId&playerid=<%=p.getId() %>'><%=p.getId() %></a></td>
                <td><%= pm.isOnline(p.getId()) %></td>
                <td><%= p.getName()%></td>
                <td><%= p.getUsername()%></td>
                <td><%= p.getGame() %></td>
                <td><%= p.getLevel() %></td>
                <td><%= map.get(p.getId())/100 %>元</td>
                <td><%= StringUtil.addcommas(p.getSilver()) %></td>
                <td><%= StringUtil.addcommas(p.getHighWaterOfSilver()) %></td>
           		 <td><%= StringUtil.addcommas(p.getBindSilver()) %></td>
                <td><%= p.getX() %></td>
                <td><%= p.getY() %></td>
                <td><a href='./allplayersCheck.jsp?selectIndex=<%=selectIndex%>&action=<%= (selectIndex.equals("5")?"removermb":"") %>&pid=<%= p.getId() %>'><%= (selectIndex.equals("5")?"抹去银子":"") %></a><td>
                </tr><%
        }
%>
</table>
<%
	out.println(PageUtil.makePageHTML("./allplayersCheck.jsp?selectIndex="+selectIndex+"",15,"start",start,nPage,(int)totalCount));
%>
</BODY></html>
