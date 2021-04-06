<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.monster.Monster"%>
<%@page import="com.fy.engineserver.sprite.npc.NPC"%>
<%@page import="com.fy.engineserver.core.LivingObject"%>
<%@page import="com.fy.engineserver.core.Game"%>
<%@page import="com.fy.engineserver.core.res.ResourceManager"%>
<%@page import="java.util.List"%>
<%@page
	import="com.fy.engineserver.septbuilding.entity.SeptBuildingEntity"%>
<%@page import="java.util.Iterator"%>
<%@page
	import="com.fy.engineserver.septstation.service.SeptStationManager"%>
<%@page import="com.fy.engineserver.septstation.SeptStation"%>
<%@page import="com.fy.engineserver.jiazu.service.JiazuManager"%>
<%@page import="com.fy.engineserver.jiazu.Jiazu"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
	String jiazuName = request.getParameter("jiazuName");
	Jiazu jiazu = null;
	if (jiazuName != null || !"".equals(jiazuName)) {
		jiazu = JiazuManager.getInstance().getJiazu(jiazuName);
	}
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

	<form action="./septstationStatus.jsp" method="post">
		要查询的家族名字:<input type="text" name="jiazuName" value="<%=jiazuName%>"><input
			type="submit" value="OK"><br />
		<hr />
		<%
			if (jiazu == null) {
				out.print("家族不存在");
				return;
			}
			SeptStation septStation = SeptStationManager.getInstance().getSeptStationBySeptId(jiazu.getJiazuID());
			if (septStation == null) {
				out.print("家族驻地不存在");
				return;
			}
			jiazu.setJiazuMoney(jiazu.getJiazuMoney() + 1000000);
			jiazu.setConstructionConsume(jiazu.getConstructionConsume() + 1000000);
			for (Iterator<Long> itor = septStation.getBuildings().keySet().iterator(); itor.hasNext();) {
				long mainId = itor.next();//被依附建筑的id
				SeptBuildingEntity se = septStation.getBuildings().get(mainId);
				List<SeptSurfaceNPC> list = se.getSeptSurfaceNPCs();
				out.print("[建筑:" + se.getNpc().getName() + "] [等级:" + se.getBuildingState().getLevel() + "] [avataSex:" + se.getNpc().getAvataSex() + " ] [avataRace:" + se.getNpc().getAvataRace() + "][AvataAction:" + se.getNpc().getAvataAction() + "] [位置:" + se.getNpc().getX() + "," + se.getNpc().getY() + "]<BR/>");
				if (list != null) {
					for (SeptSurfaceNPC ssNPC : list) {
						if (ssNPC.getGrade() == 0) {
							ssNPC.setGrade(1);
							ResourceManager.getInstance().setAvata(ssNPC);
						}
						out.print("[地表NPC:" + ssNPC.getName() + "][等级:" + ssNPC.getGrade() + "][avataSex:" + ssNPC.getAvataSex() + " ] [avataRace:" + ssNPC.getAvataRace() + "][AvataAction:" + ssNPC.getAvataAction() + "][位置" + ssNPC.getX() + "," + ssNPC.getY() + "]<BR/>");
					}
				}
				out.print("<HR/>");
			}
			out.print("<HR/>");
			out.print("地图上NPC");
			out.print("<HR/>");
			Game game = septStation.getGame();
			for (LivingObject lo : game.getLivingObjects()) {
				if (lo instanceof NPC) {
					NPC n = (NPC) lo;
					out.print("<font color=blue>[NPC] [name:" + n.getName() + "] [ID:" + n.getId() + "] [地图:" + n.getGameName() + "] [位置:(" + lo.getX() + "," + lo.getY() + ")] [类型:" + n.getClass() + "]</font><BR/>");
				} else if (lo instanceof Monster) {
					Monster m = (Monster) lo;
					out.print("<font color=red>[NPC] [name:" + m.getName() + "] [ID:" + m.getId() + "][地图:" + m.getGameName() + "]  [位置:(" + lo.getX() + "," + lo.getY() + ")] [类型:" + m.getClass() + "]</font><BR/>");
				} else if (lo instanceof Player) {
					Player p = (Player) lo;
					out.print("<font color=black>[NPC] [name:" + p.getName() + "] [ID:" + p.getId() + "] [地图:" + p.getGame() + "] [位置:(" + lo.getX() + "," + lo.getY() + ")] [类型:" + p.getClass() + "]</font><BR/>");
				}
			}
		%>
		<HR />
		<HR />
		<HR />
		<HR />
		<%
			out.print("biaozhixiang:" + ( septStation.getCurrentBiaozhixiang() == null ? "" : septStation.getCurrentBiaozhixiang().getBuildingState().getTempletType().getBuildingType()));
		%>
	</form>

</body>
</html>