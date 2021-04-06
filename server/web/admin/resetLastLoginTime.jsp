<%@ page contentType="text/html;charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">

<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.activity.fateActivity.*"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.sprite.ActivityRecordOnPlayer"%>
<%@page import="com.fy.engineserver.newBillboard.BillboardsManager"%>
<%@page import="com.fy.engineserver.newBillboard.*"%>
<%@page import="com.xuanzhi.tools.simplejpa.*"%>
<%@page import="com.fy.engineserver.util.StringTool"%>
<%@page import="com.xuanzhi.tools.text.StringUtil"%>
<%@page import="com.fy.engineserver.newBillboard.monitorLog.LogRecordManager"%>
<%@page import="com.fy.engineserver.newBillboard.monitorLog.LogRecord"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="com.fy.engineserver.activity.activeness.PlayerActivenessInfo"%>
<%@page import="com.fy.engineserver.activity.activeness.ActivenessManager"%>
<%@page import="com.fy.engineserver.activity.ActivitySubSystem"%>
<%@page import="com.fy.engineserver.util.TimeTool.TimeDistance"%>
<%@page import="com.fy.engineserver.util.TimeTool"%>
<%@page import="java.text.SimpleDateFormat"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>重置玩家上次登录时间</title>
</head>
<body>

<%

	String playerName = request.getParameter("playerName");
	if (playerName != null && !"".equals(playerName)) {
		Player player = PlayerManager.getInstance().getPlayer(playerName);
		if(player!=null){
			long lastLoginTime = player.getPlayerLastLoginTime();
			player.setPlayerLastLoginTime(lastLoginTime - 1000*60*60*24);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm");
			String time = sdf.format(lastLoginTime);
			out.print(time);
			//ActivitySubSystem.logger.warn(player.getLogString()+"重置玩家上次登录时间,置为前一天");
		}
	}
%>

<form action="">角色名：<input type="text" name="playerName" />
 <input type="submit" value="submit" /></form>
</body>

</html>
