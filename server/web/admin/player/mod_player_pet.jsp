<%@ page contentType="text/html;charset=gbk" import="java.util.*,com.xuanzhi.tools.text.*,java.lang.reflect.*,
com.fy.engineserver.sprite.*,com.xuanzhi.tools.transport.*,com.google.gson.*,com.xuanzhi.boss.game.*"%><% 

	Gson gson = new Gson();
	PlayerManager sm = PlayerManager.getInstance();
	String action = request.getParameter("action");
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
	Player player = null;
	String errorMessage = null;

	if (action == null) {
		if (errorMessage == null) { 
			if(playerId != -1){
				player = sm.getPlayer(playerId);
				if (player == null) {
					errorMessage = "ID为" + playerId + "的玩家不存在！";
				}
			}else if(playerName != null){
				player = sm.getPlayer(playerName);
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
			player = sm.getPlayer(playerId);
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
			player = sm.getPlayer(playerName);
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
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="java.io.PrintStream"%>

<%@page import="com.fy.engineserver.datasource.article.data.props.Knapsack"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.Cell"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%><%@include file="../IPManager.jsp" %><html><head>
<style type="text/css">
.titlecolor{
background-color:#C2CAF5;
}
.textcenter{
text-align: center;
}
</style>
<link rel="stylesheet" type="text/css" href="../../css/common.css" />
<link rel="stylesheet" type="text/css" href="../../css/table.css" />
</HEAD>
<BODY>
<%
	if(errorMessage != null){
		out.println("<center><table border='0' cellpadding='0' cellspacing='2' width='100%' bgcolor='#FF0000' align='center'>");
		out.println("<tr bgcolor='#FFFFFF' align='center'><td>");
		out.println("<font color='red'><pre>"+errorMessage+"</pre></font>");
		out.println("</td></tr>");
		out.println("</table></center>");
	}
%>
<form id='f1' name='f1' action=""><input type='hidden' name='action' value='select_playerId'>
请输入要用户的ID：<input type='text' name='playerid' id='playerid' value='<%=playerId %>' size='20'><input type='submit' value='提  交'>
</form>
<form id='f01' name='f01' action=""><input type='hidden' name='action' value='select_playerName'>
请输入要角色名：<input type='text' name='playerName' id='playerName' value='<%=playerName %>' size='20'><input type='submit' value='提  交'>
</form>
<br/>
<%
PlayerManager pm = PlayerManager.getInstance();
ArticleManager am = ArticleManager.getInstance();
ArticleEntityManager aem = ArticleEntityManager.getInstance();
if(pm != null && am != null && aem != null){
	Player p = pm.getPlayer(playerId);
	if(p != null){
		%>
		<form>
			<input type="hidden" name='playerid' value=<%= playerId%>>
			<table>
			<%
			Knapsack knapsack = p.getKnapsack_common(Article.KNAP_宠物);
			if(knapsack != null){
				%>
				<tr>
				<td colspan="10" class="titlecolor textcenter"><%=ArticleManager.得到背包名字(Article.KNAP_宠物) %></td>
				</tr>
				<%
				Cell[] cells = knapsack.getCells();
				List<ArticleEntity> aeList = new ArrayList<ArticleEntity>();
				if(cells != null){
                    for(Cell cell : cells){
                    	if(cell != null && cell.entityId > 0 && cell.count > 0){
                    		ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(cell.entityId);
                    		if(ae != null){
                    			aeList.add(ae);
                    		}
                    	}
                    }
				}
				
			}
			%>
			</table>
		</form>
		
		<%
	}
}
%>

</BODY></html>