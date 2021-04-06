<%@ page contentType="text/html;charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@page import="com.fy.engineserver.activity.dig.DigTemplate"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="java.util.Map"%>
<%@page import="com.fy.engineserver.activity.dig.DigManager"%>
<%@page import="com.fy.engineserver.activity.ActivitySubSystem"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>移除挖宝信息</title>
</head>
<body>

<%
	Object [] players= ((GamePlayerManager)GamePlayerManager.getInstance()).getAllPlayerInCache();
	for(Object o:players){
		if (o instanceof Player) {
			Player player = (Player)o;
			Map<Long, DigTemplate>  map = player.getDigInfo();
			if (map != null && map.size() >= 10) {
				String notice = "移除玩家上身上的DIGINFO" + player.getLogString() + "，数量:" + map.size() ;
				map.clear();
				player.setDigInfo(map);
				out.print(notice + "<BR/>");
				ActivitySubSystem.logger.error(notice);
			}
		}
	}
		%>

<form action="">
	角色名：<input type="text" name="playerName" />
	 <input	type="submit" value="submit" /></form>
</body>

</html>
