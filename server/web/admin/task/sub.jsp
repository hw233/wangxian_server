<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="com.fy.engineserver.crossserver.GameServerManager"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.fy.engineserver.gateway.GameSubSystem"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="com.fy.engineserver.gateway.GameNetworkFramework"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	GameServerManager gsm = GameServerManager.getInstance();
	boolean crossServer = false;
	if (gsm != null) {
		if (gsm.isCrossServer(GameConstants.getInstance().getServerName())) {
			crossServer = true;
		}
	}
	out.print("crossServer:" + crossServer);
	GameNetworkFramework gnk = GameNetworkFramework.getInstance();
	Field f = GameNetworkFramework.class.getDeclaredField("message2SubSysMap");
	f.setAccessible(true);
	HashMap<String, GameSubSystem[]> map = (HashMap<String, GameSubSystem[]>) f.get(gnk);
	//System.out.print("map:" + map);
	//out.print("QUERY_PLAYER_REQ:" + map.get("QUERY_PLAYER_REQ"));
	for (Iterator<String> itor = map.keySet().iterator();itor.hasNext();) {
		String pro = itor.next();
		out.print(pro + ":<BR/>");
		for (GameSubSystem gss : map.get(pro)) {
			out.print("&nbsp;&nbsp;&nbsp;&nbsp;" + gss.getName());
		}
		out.print("<hr/>");
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