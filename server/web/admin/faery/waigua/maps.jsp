<%@page import="com.fy.engineserver.enterlimit.EnterLimitManager.PlayerRecordData"%>
<%@page
	import="com.fy.engineserver.enterlimit.EnterLimitManager.EnterLimitData"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.fy.engineserver.enterlimit.EnterLimitManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String lowLevelStr = request.getParameter("lowLevel");
	String lowSizeStr = request.getParameter("lowSize");
	String query = request.getParameter("query");
	int lowSize  = 15;
	int lowLevel = 10;
	if (lowLevelStr != null && lowSizeStr != null) {
		lowSize = Integer.valueOf(lowSizeStr);
		lowLevel = Integer.valueOf(lowLevelStr);
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<form action="" method="post">
	最小条数:<input type="text" name="lowSize" value=<%=lowSize %>><BR/>
	最小等级:<input type="text" name="lowLevel" value=<%=lowLevel %>>
	<input type="hidden" name="query" value="query">
	<input type="submit" value="查询">
</form>
	<table style="font-size: 12px;" border="1">
		<tr>
			<td>data.username</td>
			<td>data.gameName</td>
			<td>data.playerId</td>
			<td>data.playername</td>
			<td>data.level</td>
			<td>data.network</td>
			<td>data.online</td>
			<td>data.ua</td>
			<td>data.channel</td>
			
			<td>摆摊次数</td>
			<td>聊天次数</td>
			<td>境界</td>
			<td>合成绑定物品</td>
			<td>当前体力</td>
			<td>喝酒次数</td>
			<td>快速出售次数</td>
			<td>国战次数</td>
			<td>家族名</td>
			<td>最后更新时间</td>
			<td>主线任务</td>
			<td>最多坐骑</td>
			<td>最多宠物</td>
			
			<td>橙色以上装备</td>
			<td>背包大小</td>
			<td>种植次数</td>
			<td>紫色以上装备</td>
			
			<td>注册时间</td>
			<td>求购次数</td>
			<td>卖空格子</td>
			<td>有物品邮件</td>
		</tr>
		<%
			if (!"query".equals(query)) {
				return;
			}
		EnterLimitData [] datas = EnterLimitManager.getInstance().getLikeNeedLimitArray(lowSize,lowLevel);
			for (EnterLimitData data : datas) {
				String ip = data.ip;
		%>
		<tr>
			<td colspan="31" style="text-align: left;background-color: #70C4F5;font-weight: bolder;"><%=ip+"("+data.size()+")"%></td>
		</tr>

		<%
			for (Long playerId : data.playerIds) {
				PlayerRecordData  prd= EnterLimitManager.getPlayerRecordData(playerId);
				if (prd == null) {
					continue;
				}
		%>
		<tr>
			<td><%=prd.username %></td>
			<td><%=prd.gameName%></td>
			<td><%=prd.playerId%></td>
			<td><%=prd.playername%></td>
			<td><%=prd.level%></td>
			<td><%=prd.network%></td>
			<td><%=prd.online%></td>
			<td><%=prd.ua%></td>
			<td><%=prd.channel%></td>


			<td><%=prd.boothSaleTimes%></td>
			<td><%=prd.chatTimes%></td>
			<td><%=prd.classLevel%></td>
			<td><%=prd.composeBindTimes%></td>
			<td><%=prd.currentTili%></td>
			<td><%=prd.drinkTimes%></td>
			<td><%=prd.fastSellTimes%></td>
			<td><%=prd.guozhanAcceptTimes%></td>
			<td><%=prd.jiazuName%></td>
			<td><%=prd.lastModifyTime%></td>
			<td><%=prd.mainTaskLevel%></td>
			<td><%=prd.maxHorseNum%></td>
			<td><%=prd.maxPetNum%></td>
			
			<td><%=prd.orangeEquipmentNum%></td>
			<td><%=prd.packageSize%></td>
			<td><%=prd.plantNum%></td>
			<td><%=prd.purpleEquipmentNum%></td>
			
			<td><%=prd.registerTime%></td>
			<td><%=prd.requestBuyTimes%></td>
			<td><%=prd.sealEmptyCellTimes%></td>
			<td><%=prd.sendArticleMailTimes%></td>
		</tr>
		<%
			}
		%>
		<%
			}
		%>
	</table>
</body>
</html>