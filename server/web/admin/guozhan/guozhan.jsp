<%@ page contentType="text/html;charset=utf-8"%>
<%@ page import="java.util.*,
				com.fy.engineserver.guozhan.*,
				com.xuanzhi.tools.text.*,
				com.fy.engineserver.sprite.*,
				com.fy.engineserver.country.manager.*,"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
	<title></title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<link rel="stylesheet" href="css/style.css" />
	<link rel="stylesheet" href="css/atalk.css" />
	<script type="text/javascript">
	</script>
	</head>

	<body>
		<%     
		GuozhanOrganizer org = GuozhanOrganizer.getInstance();
		Constants cons = org.getConstants();
		String dwid = request.getParameter("dw");
		if(dwid != null) {
			DeclareWar dw = org.getDeclareWar(Integer.valueOf(dwid));
			if(dw != null) {
				org.testStartGuozhan(dw);
			}
		}
		String gzIndex = request.getParameter("gzIndex");
		if(gzIndex != null) {
			org.getGuozhanList().get(Integer.parseInt(gzIndex)).forceEnd();
		}
		String clearDeclare = request.getParameter("clearDeclare");
		if(clearDeclare != null && clearDeclare.equals("true")) {
			org.getDeclareList().clear();
		}
		String clearGuozhan = request.getParameter("clearGuozhan");
		if(clearGuozhan != null && clearGuozhan.equals("true")) {
			for(Guozhan guozhan : org.getGuozhanList()) {
				if(guozhan.getState() == Guozhan.STATE_开启) {
					guozhan.forceEnd();
				}
			}
			org.getGuozhanList().clear();
		}
		
	    %>     
		<center>
		<%
		List<Guozhan> guozhanList = org.getGuozhanList();
		%>                                         
		    <h2 style="font-weight:bold;">          
		    	国战列表(当前状态:<%=org.getState()==0?"关闭":"开启" %>, 是否开启时间:<%=org.isGuozhanOpenTime(System.currentTimeMillis()) %>)
		    </h2> 
			<table id="test1" align="center" width="100%" cellpadding="0"
				cellspacing="0" border="0"
				class="sortable-onload-5-6r rowstyle-alt colstyle-alt no-arrow">
				<tr>
					<th width=9%>
						<b>国战进攻方</b>
					</th>
					<th width=9%>
						<b>国战防守方</b>
					</th>
					<th width=10%>
						<b>开启时间</b>
					</th>
					<th width=10%>
						<b>结束时间</b>
					</th>
					<th width=12%>
						<b>攻方复活点</b>
					</th>
					<th width=10%>
						<b>龙象卫长</b>
					</th>
					<th width=10%>
						<b>龙象释帝</b>
					</th>
					<th width=8%>
						<b>当前状态</b>
					</th>
					<th width=8%>
						<b>结果</b>
					</th>
					<th width=12%>
						<b>操作</b>
					</th>
				</tr>
				<%
				int index = 0;
				int pnum = 0;
				for(Guozhan guozhan : guozhanList) {
					pnum += guozhan.getAttendPlayers().size();
				%>
				<tr>
					<td><%=guozhan.getAttacker().getName() %></td>
					<td><%=guozhan.getDefender().getName() %></td>
					<td><%=DateUtil.formatDate(new Date(guozhan.getStartTime()),"yyyy-MM-dd HH:mm") %></td>
					<td><%=DateUtil.formatDate(new Date(guozhan.getEndTime()),"yyyy-MM-dd HH:mm") %></td>
					<td>
						<%=(guozhan.getAttackerGuozhanBornPoint()[0].getBelongCountryId()==guozhan.getAttacker().getCountryId()?"占据":"未得") %>
						/
						<%=(guozhan.getAttackerGuozhanBornPoint()[1].getBelongCountryId()==guozhan.getAttacker().getCountryId()?"占据":"未得") %>
						/
						<%=(guozhan.getAttackerGuozhanBornPoint()[2].getBelongCountryId()==guozhan.getAttacker().getCountryId()?"占据":"未得") %>
					</td>
					<td><%=guozhan.getBoss()[1].getHp()+"/"+guozhan.getBoss()[1].getMaxHP() %></td>
					<td><%=guozhan.getBoss()[0].getHp()+"/"+guozhan.getBoss()[0].getMaxHP() %></td>
					<td><%=guozhan.getState()==Guozhan.STATE_开启?"开启":"结束" %></td>
					<td><%=guozhan.getState()==Guozhan.STATE_结束?(guozhan.getResult()==0?"攻方获胜":"守方获胜"):"" %></td>
					<td>
						<input type="button" value="立即关闭" name=bt1 onclick="location.replace('guozhan.jsp?gzIndex=<%=index %>')"> 					
					</td>
				</tr>
				<%
				index++;
				} %>
			</table>国战内存中玩家数量:<%=pnum %><br><br>
			<h2 style="font-weight:bold;">          
		    	宣战列表
		    </h2> 
		    <table id="test1" align="center" width="100%" cellpadding="0"
				cellspacing="0" border="0"
				class="sortable-onload-5-6r rowstyle-alt colstyle-alt no-arrow">
				<tr>
					<th width=15%>
						<b>宣战方</b>
					</th>
					<th width=15%>
						<b>被宣战方</b>
					</th>
					<th width=15%>
						<b>宣战人</b>
					</th>
					<th width=15%>
						<b>宣战日期</b>
					</th>
					<th width=15%>
						<b>开打日期</b>
					</th>
					<th width=15%>
						<b>操作</b>
					</th>
				</tr>
				<%
				List<DeclareWar> dwlist = org.getDeclareList();
				for(DeclareWar dw : dwlist) {
				%>
				<tr>
					<td><%=CountryManager.得到国家名(dw.getDeclareCountryId()) %></td>
					<td><%=CountryManager.得到国家名(dw.getEnemyCountryId()) %></td>
					<td><%=PlayerManager.getInstance().getPlayer(dw.getDeclarePlayerId()).getName() %></td>
					<td><%=DateUtil.formatDate(new Date(dw.getDeclareTime()),"yyyy-MM-dd HH:mm") %></td>
					<td><%=DateUtil.formatDate(new Date(dw.getStartFightTime()),"yyyy-MM-dd HH:mm") %></td>
					<td>
						<input type="button" value="立即开打" name=bt1 onclick="location.replace('guozhan.jsp?dw=<%=dw.getId() %>')"> 					
					</td>
				</tr>
				<%} %>
				
			</table><br><br>
			<input type=button name=bt4 value="清空宣战" onclick="location.replace('guozhan.jsp?clearDeclare=true')">
			<input type=button name=bt3 value="清空国战" onclick="location.replace('guozhan.jsp?clearGuozhan=true')">
			<center>
		<br>
		<br>
	</body>
</html>