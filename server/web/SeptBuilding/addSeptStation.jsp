<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="inc.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
	//增加驻地
	System.out.println("JSP...增加驻地");
	String sSeptId = request.getParameter("septId");
	long septId = -1;
	if (sSeptId != null) {
		septId = Long.valueOf(sSeptId);
	}
	if (septId > 0) {
		String mapName = request.getParameter("mapName");
		String name = request.getParameter("name");
		String info = "创建驻地[" + name + "成功";
		SeptStation station = null;
		try {
			station =SeptStationManager.getInstance().createDefaultSeptStation(septId, mapName, name, 0, 0l);
		} catch (NameExistException e) {
			info = name + "已经被注册了";
		} catch (StationExistException e) {
			info = TextTranslate.translate.text_6315;
		} catch (SeptNotExistException e) {
			info = "家族不存在";
		}
		if (station != null) {
			response.sendRedirect("../SeptBuilding/septStation.jsp?id="+station.getId() +"&msg="+java.net.URLEncoder.encode(info,"utf-8"));
		} else {
			out.print(info);
		}
	} else {
		out.print("ID错误:" + septId);
		return;
	}
%>