<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="./inc.jsp"%>
<%
	int country = ParamUtils.getIntParameter(request, "country", -1);
	long faeryId = Long.valueOf(ParamUtils.getParameter(request, "id"));
	long playerId = Long.valueOf(ParamUtils.getParameter(request, "playerId"));
	int index = ParamUtils.getIntParameter(request, "index", -1);
	String msg = "";
	if (country == -1 || faeryId == -1 || index == -1) {
		msg = "参数异常";
		response.sendRedirect("admin/faery/faeryList.jsp?msg=" + msg);
		return;
	}
	Faery faery = FaeryManager.getInstance().getFaety(country, faeryId);
	if (faery == null) {
		msg = "仙府不存在";
		response.sendRedirect("admin/faery/faeryList.jsp?msg=" + msg);
		return;
	}
	Player player = PlayerManager.getInstance().getPlayer(playerId);

	if (player == null) {
		msg = "角色不存在";
		response.sendRedirect("admin/faery/faeryList.jsp?msg=" + msg);
		return;
	}

	Cave cave = faery.getCave(index);
	if (cave != null) {
		msg = "该位置上有仙府了";
		response.sendRedirect("admin/faery/faeryList.jsp?msg=" + msg);
		return;
	}
	try {
		FaeryManager.logger.info(">>>>>>>>>>>>>>>faeryId = " + faeryId + " player = " + player + " Pcountry = " + player.getCountry() + " index = " + index);
		Cave c = FaeryManager.getInstance().createDefaultCave(player, faeryId, index);
		out.print("OK");
	} catch (AlreadyHasCaveException e1) {
		FaeryManager.logger.error(e1.getMsg(),e1);
	} catch (CountyNotSameException e2) {
		e2.printStackTrace();
		FaeryManager.logger.error(e2.getMsg(),e2);
	} catch (FertyNotFountException e3) {
		e3.printStackTrace();
		FaeryManager.logger.error(e3.getMsg(),e3);
	} catch (PointNotFoundException e4) {
		FaeryManager.logger.error(e4.getMsg(),e4);
		e4.printStackTrace();
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>