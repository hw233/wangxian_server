<%@page import="com.fy.engineserver.core.JiazuSubSystem"%>
<%@page import="com.fy.engineserver.septbuilding.entity.SeptBuildingEntity"%>
<%@page import="com.fy.engineserver.jiazu.service.JiazuManager"%>
<%@page import="com.fy.engineserver.jiazu.*"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%

	String playerName = request.getParameter("playerName");
	String numS = request.getParameter("num");

	if (playerName != null && !"".equals(playerName) && numS != null && !"".equals(numS)) {
		int num = Integer.valueOf(numS);
		Player player = GamePlayerManager.getInstance().getPlayer(playerName);
		Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
		if (jiazu != null) {
			SeptBuildingEntity sbe = jiazu.getInbuildingEntity();
			if (sbe != null) {
				for (int i = 0; i < num; i++) {
					sbe.onDeliverTask(player);
				}
			}
			out.print("<H1>完成家族任务:"+numS+"次</H1>");
		} else {
			out.print("<H1>没有家族</H1>");
		}
	} else {
		out.print("<H1>please输入角色名字和次数</H1>");
	}

%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form action="./doneTask.jsp" method="post">
		角色<input name="playerName" type="text" value="<%=playerName%>">
		数量<input name="num" type="text" value="<%=numS%>">
		<input type="submit" value="提交">
		
	</form>
</body>
</html>