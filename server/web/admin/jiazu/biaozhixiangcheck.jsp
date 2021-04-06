<%@page import="com.fy.engineserver.septbuilding.entity.SeptBuildingEntity"%>
<%@page import="com.fy.engineserver.core.JiazuSubSystem"%>
<%@page import="com.fy.engineserver.septstation.service.SeptStationManager"%>
<%@page import="com.fy.engineserver.septstation.SeptStation"%>
<%@page import="com.fy.engineserver.menu.jiazu.Option_Jiazu_Buff"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String playerName = request.getParameter("playerName");
	if (playerName == null) {
		playerName = "";
	} else {
		Player p = GamePlayerManager.getInstance().getPlayer(playerName);
		if (p == null) {
			out.print("<H3>角色不存在:["+playerName+"]</H3>");
		} else {
			boolean canSee = canSee(p,out);
			out.print("[是否能看到:"+canSee+"]");
		}
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查询家族发布buff</title>
</head>
<body>
	<form action="./biaozhixiangcheck.jsp" method="post">
		输入要查询的角色(需要是有权限发布家族buff的)
		<input type="text" name="playerName" value="<%=playerName%>">
		<input type="submit" value="提交">
	</form>
</body>
</html>
<%!
public boolean canSee(Player player,JspWriter out) throws Exception{
	SeptStation septStation = SeptStationManager.getInstance().getSeptStationBySeptId(player.getJiazuId());
	out.print("[septStation:"+septStation+"]<BR/>");
	if (septStation == null) {
		return false;
	}
	SeptBuildingEntity biaozhixiang = septStation.getCurrentBiaozhixiang();
	out.print("[biaozhixiang:"+biaozhixiang+"]<BR/>");
	if (biaozhixiang != null) {
		return true;
	}
	return false;
}
%>