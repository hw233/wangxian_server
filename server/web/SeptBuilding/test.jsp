<%@page import="com.fy.engineserver.util.ObjectCollection"%>
<%@page import="java.util.*"%>
<%@page
	import="com.fy.engineserver.septstation.SeptStationMapTemplet"%>
<%@page import="com.fy.engineserver.jiazu.service.JiazuManager"%>
<%@page import="com.fy.engineserver.jiazu.Jiazu"%>
<%@page import="com.fy.engineserver.core.GameManager"%>
<%@page import="com.fy.engineserver.sprite.npc.SeptStationNPC"%>
<%@page import="com.fy.engineserver.core.LivingObject"%>
<%@page import="com.fy.engineserver.core.Game"%>
<%@page
	import="com.fy.engineserver.septstation.service.SeptStationManager"%>
<%@page import="com.fy.engineserver.septstation.SeptStation"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
	/**
	 Jiazu jiazu = JiazuManager.getInstance().getJiazu(1001000000000628446L);
	 SeptStation septStation = SeptStationManager.getInstance().getSeptStationById(jiazu.getBaseID());
	 Game game = septStation.getGame();

	 out.print("地图:" + ",," + game.gi.getName() + "<BR/>");
	 for (LivingObject lo : game.getLivingObjects()) {
	 out.print(">>>>" + lo.getClass() +  "<BR/>");
	 }
	 */
	out.print("驻地状态<BR/>");
	SeptStationRunTimeData ssrt = SeptStationManager.getInstance().getRunTimeData();
	for (Iterator<Byte> it = ssrt.getServerSupplyLocation().keySet().iterator(); it.hasNext();) {
		byte country = it.next();

		List<ObjectCollection<Boolean>> list = ssrt.getServerSupplyLocation().get(country);
		out.print("--------------" + country + "---------------<BR/>");
		int index = 0;
		for (ObjectCollection<Boolean> oc : list) {
			out.print("\t\t第" + (index++) + "个<BR/>");
			out.print(oc.toString() + "BR/");
		}
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>