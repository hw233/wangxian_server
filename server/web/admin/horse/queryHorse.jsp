<%@ page language="java" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.*"%>
<%@page import="com.fy.engineserver.datasource.article.manager.*"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.*"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.*"%>
<%@page import="com.fy.engineserver.datasource.article.data.equipments.*"%>
<%@page import="com.fy.engineserver.sprite.horse.Horse"%>
<%@page import="com.fy.engineserver.sprite.concrete.*"%>
<%@page import="com.fy.engineserver.sprite.*"%>


<%@page import="com.fy.engineserver.sprite.horse.HorseManager"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>查询个人的坐骑</title>
<link rel="stylesheet" type="text/css" href="./style.css" />

<style type="text/css">

</style>

<script type="text/javascript">
	function queryById(obj){
		document.getElementById("horseById").value =obj;
		document.queryHorse.submit();
		
	}

	function horseUse(obj){
		document.getElementById("horseUseId").value =obj;
		document.horseUse.submit();
		

	}
	
	function upOrdown(obj,obj1){

		document.getElementById("upordownHorseId").value=obj;
		document.getElementById("upOrdown").value=obj1;
		document.upOrdownForm.submit();

	}

	

</script>
			<%
			long playerId = -1;
			String playerName = null;
			Object obj = session.getAttribute("playerid");
			Object obj2 = session.getAttribute("playerName");
			if(obj != null){
				playerId = Long.parseLong(obj.toString());
			}
			if(obj2 != null){
				playerName = obj2.toString();
			}
			String action = request.getParameter("action");
			String errorMessage = null;
			PlayerManager pm = PlayerManager.getInstance();
			Player player = null;
			ArticleManager am = ArticleManager.getInstance();
			ArticleEntityManager aem = ArticleEntityManager.getInstance();
			PropsCategory pcs[] = am.getAllPropsCategory();
			HorseManager hm = HorseManager.getInstance();
			Horse horse = null;
			if (action == null) {
				if (errorMessage == null) { 
					if(playerId != -1){
						player = pm.getPlayer(playerId);
						if (player == null) {
							errorMessage = "ID为" + playerId + "的玩家不存在！";
						}
					}else if(playerName != null){
						player = pm.getPlayer(playerName);
						if (player == null) {
							errorMessage = "ID为" + playerId + "的玩家不存在！";
						}
					}
				}
			}else if (action != null && action.equals("select_playerId")) {
				try {
					playerId = Long.parseLong(request
							.getParameter("playerid"));
				} catch (Exception e) {
					errorMessage = "玩家ID必须是数字，不能含有非数字的字符";
				}
				if (errorMessage == null) {
					player = pm.getPlayer(playerId);
					if (player == null) {
						errorMessage = "ID为" + playerId + "的玩家不存在！";
					}else{
						session.setAttribute("playerid",request.getParameter("playerid"));
						playerName = player.getName();
						session.setAttribute("playerName",playerName);
					}
				}
			}else if (action != null && action.equals("select_playerName")) {
				try {
					playerName = request
							.getParameter("playerName");
				} catch (Exception e) {
					errorMessage = "玩家ID必须是数字，不能含有非数字的字符";
				}
				if (errorMessage == null) {
					player = pm.getPlayer(playerName);
					if (player == null) {
						errorMessage = "角色名为" + playerName + "的玩家不存在！";
					}else{
						session.setAttribute("playerName",request.getParameter("playerName"));
						playerId = player.getId();
						session.setAttribute("playerid",playerId);
					}
				}
			}
			%>


</head>

<body>
	<a style="color:#2D20CA" href="queryHorse.jsp">刷新此页面</a>
	<br>
	<br>
	<form id='f1' name='f1' action="">
		<input type='hidden' name='action' value='select_playerId' />
		请输入要用户的ID：<input type='text' name='playerid' id='playerid' value='<%=playerId %>' size='20' />
		<input type='submit' value='提  交' />
	</form>
	<form id='f01' name='f01' action="">
		<input type='hidden' name='action' value='select_playerName'>
		请输入要角色名：<input type='text' name='playerName' id='playerName' value='<%=playerName %>' size='20' />
		<input type='submit' value='提  交' />
	</form>
	
	<br>
	<%
		if(errorMessage != null){
			out.println("<center><table border='0' cellpadding='0' cellspacing='2' width='100%' bgcolor='#FF0000' align='center'>");
			out.println("<tr bgcolor='#FFFFFF' align='center'><td>");
			out.println("<font color='red'><pre>"+errorMessage+"</pre></font>");
			out.println("</td></tr>");
			out.println("</table></center>");
		}
	%>
	
	<form name="horseUse" action="useHorseEntity.jsp"  method="get">
			<input type="hidden" id="horseUseId" name="horseUse" />
	</form>
	
	<h2>坐骑道具</h2>
	<table>
		<tr class="titlecolor" align="center">
			<td>坐骑道具</td><td>使用</td>
		</tr>
		<%
			if(player != null){
				
				Knapsack[] knapsacks = player.getKnapsacks_common();
				
				for(Knapsack knapsack : knapsacks){
					
					Cell[] cells = knapsack.getCells();
					for (int i = 0; i < cells.length; i++) {
						ArticleEntity ae = knapsack.getArticleEntityByCell(i);
						if(ae != null){
							Article a = am.getArticle(ae.getArticleName());
							if(a != null){
								if(a instanceof HorseProps){
									%>
									
									<tr class="titlecolor" align="center">
										<td><%= ae.getArticleName()%></td>
										<td><a href ="javascript:horseUse('<%= ae.getId()%>')">使用</a></td>
									</tr>
									<%
								}
							}
						}
					}
				}
			}
		%>
		
	</table>
	
	<form name="queryHorse" action="horseById.jsp"  method="get">
			<input type="hidden" id="horseById" name="horseById" />
	</form>
	<form name="upOrdownForm" action="upordownHorse.jsp"  method="get">
			<input type="hidden" id="upOrdown" name="upOrdown" />
			<input type="hidden" id="upordownHorseId" name="upordownHorseId" />
	</form>
	
	<h2>坐骑</h2>
	<table>
		<tr class="titlecolor" align="center">
			<td>坐骑名称</td><td>id</td><td>级别</td><td>查看</td><td>上马</td><td>下马</td>
		</tr>
		<%
			if(player != null){
				List<Long> list = player.getHorseIdList();
				if(list.size() == 0){
				%>
				<tr>
					<td align="center" colspan="3"> 此人没有坐骑</td>
				</tr>
				<%
				}else{
					for(long horseid : list){
						
						horse = hm.getHorseById(horseid,player);
						if(horse == null){
							continue;
						}
						String name = horse.getHorseName();
						
						%>
						<tr class="titlecolor" align="center">
							<td><%= name%></td>
							<td><%= horse.getHorseId()%></td>
							<td><a href ="javascript:queryById('<%= horseid%>')">查看</a></td>
							<td><a href ="javascript:upOrdown('<%= horseid%>',1)">上马</a></td>
							<td><a href ="javascript:upOrdown('<%= horseid%>',0)">下马</a></td>
						</tr>
						<%
					}
				}
			} 
		%>
		
	</table>
</body>
</html>
