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
<%@page
	import="com.fy.engineserver.newBillboard.monitorLog.LogRecordManager"%>
<%@page
	import="com.fy.engineserver.newBillboard.monitorLog.LogRecord"%>
<%@page import="java.util.Map.Entry"%>
<%@page
	import="com.fy.engineserver.activity.activeness.PlayerActivenessInfo"%>
<%@page
	import="com.fy.engineserver.activity.activeness.ActivenessManager"%>
<%@page import="com.fy.engineserver.activity.ActivitySubSystem"%>
<%@page import="com.fy.engineserver.util.TimeTool.TimeDistance"%>
<%@page import="com.fy.engineserver.util.TimeTool"%>
<%@page import="com.fy.engineserver.message.CAN_GET_RES"%>
<%@page import="com.fy.engineserver.activity.activeness.ActivityForActiveness"%>
<%@page import="com.fy.engineserver.newtask.service.TaskManager"%>
<%@page import="com.fy.engineserver.newtask.Task"%>
<%@page import="com.fy.engineserver.datasource.language.Translate"%>
<%@page import="com.fy.engineserver.activity.dig.DigManager"%>
<%@page import="com.fy.engineserver.activity.dig.DigTemplate"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>查看玩家活跃度</title>
</head>
<body>

<%
	String playerName = request.getParameter("playerName");
	if (playerName != null && !"".equals(playerName)) {
		Player player = PlayerManager.getInstance().getPlayer(playerName);
		Map<Long,DigTemplate> map = player.getDigInfo();
			for(Iterator<Long> itor = map.keySet().iterator();itor.hasNext();) {
				Long articleId  = itor.next();
				DigTemplate dt = map.get(articleId);
				out.print("articleId:" + articleId);
				out.print("<BR/>");
				out.print(dt.toString());
				out.print("<HR/>");
				out.print("<HR/>");
			}
	}
		%>

<form action="">
	角色名：<input type="text" name="playerName" />
	 <input	type="submit" value="submit" /></form>
</body>

</html>
