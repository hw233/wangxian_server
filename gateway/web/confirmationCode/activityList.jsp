<%@page import="com.fy.confirm.bean.Prize"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.fy.confirm.bean.GameActivity"%>
<%@page import="com.fy.confirm.codestore.CodeStorer"%>
<%@page import="com.fy.confirm.service.server.DataManager"%>
<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%!SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");%>
<%
	DataManager dataManager = DataManager.getInstance();
	CodeStorer codeStorer = dataManager.getCodeStorer();
	List<GameActivity> activities = codeStorer.getAllActivity();
	out.print("已有活动数量" + activities.size() + "<BR/>");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="../css/common.css"/>
<link rel="stylesheet" href="../css/confirmationcode.css" />
<title>所有活动列表</title>
</head>
<body>
<div class="used">过期的/未开放的</div>
	<form name="activity" action="">
		<table border="1">
			<tr class="head">
				<td>id</td>
				<td>名字</td>
				<td>描述(玩家看的)</td>
				<td>内部描述</td>
				<td>每个账号可领取次数</td>
				<td>活动开启时间</td>
				<td>活动结束时间</td>
				<td>开放游戏</td>
				<td>开放游戏区</td>
				<td>奖励</td>
				<td>详情</td>
			</tr>
			<%
			long now = System.currentTimeMillis();
			for (int i = 0; i < activities.size(); i++) {
				GameActivity ga = activities.get(i);
				String gameNmae = ""+ga.getGameId();
				//Game game = GameManager.getInstance().getGame(ga.getGameId());
				//if (game != null) {
				//	gameNmae = game.getName();
				//}
				
				boolean isActive = ga.isActive() && (ga.getStartTime() < now && ga.getEndTime() > now);
		%>
			<tr <%=isActive? "" :"class='used'" %>>
				<td><%=ga.getId()%></td>
				<td><%=ga.getName()%></td>
				<td><%=ga.getDescription()%></td>
				<td><%=ga.getInnerDescription()%></td>
				<td><%=ga.getEachUserExchangeTimes()%></td>
				<td><%=sdf.format(ga.getStartTime())%></td>
				<td><%=sdf.format(ga.getEndTime())%></td>
				<td><%=gameNmae%></td>
				<td><%=Arrays.toString(ga.getAreas())%></td>
				<td><span id="prizeButton<%=i%>" style="display: inline;"><a
						onclick="showPrize(<%=i%>)">查看</a>
				</span> <span id="prize<%=i %>" style="display: none;"><a href=""
						onclick="displayPrize(<%=i%>)">关闭</a><BR /> <%
 			for (Prize prize : ga.getPrizes()) {
 			%> <span><%="物品[" + prize.getPropName() + "][颜色:" + prize.getColor() + "][数量:" + prize.getNum() + "][是否绑定:" + (prize.isBind() ? "是" : "否") + "]"%><BR />
					</span> <%
				}
			%> </span></td>
				<td><a
					href="./activity.jsp?activeId=<%=ga.getId() %>&option=modify">详细</a>
				</td>
			</tr>
			<%
			}
		%>
		</table>
		<BR /> <a href="./activity.jsp?option=add">增加新的活动</a>
	</form>
</body>
</html>
<script>
	function showPrize(index){
		document.getElementById("prize"+index).style.display="block";
		document.getElementById("prizeButton"+index).style.display="none";
	}
	function displayPrize(index){
		document.getElementById("prize"+index).style.display="none";
		document.getElementById("prizeButton"+index).style.display="block";
	}
</script>