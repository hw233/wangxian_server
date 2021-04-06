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
<%@page import="com.fy.engineserver.message.CAN_GET_RES"%>
<%@page import="java.util.concurrent.ConcurrentHashMap"%>
<%@page import="com.fy.engineserver.activity.activeness.ActivityForActiveness"%>
<%@page import="com.fy.engineserver.util.server.TestServerConfigManager"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>手动签到</title>
</head>
<body>

<%
if (TestServerConfigManager.isTestServer()) {
	String playerName = request.getParameter("playerName");
	String days = request.getParameter("days");
	String days2 = request.getParameter("days2");
	if (playerName != null && !"".equals(playerName)) {
		Player player = PlayerManager.getInstance().getPlayer(playerName);
		PlayerActivenessInfo pai = player.getActivenessInfo();
		if (player != null) { 
			if(pai!=null){
				boolean[] hasSign = pai.getHasSign();
				if(days != null && !"".equals(days)){
					String[] day = days.split(",");
					for(int i=0;i<day.length;i++){
						hasSign[Integer.valueOf(day[i])-1] = true;
					}
					pai.setHasSign(hasSign);
					out.print("签到成功<br/>");
				}
				if(days2 != null && !"".equals(days2)){
					String[] day2 = days2.split(",");
					for(int i=0;i<day2.length;i++){
						hasSign[Integer.valueOf(day2[i])-1] = false;
					}
					pai.setHasSign(hasSign);
					out.print("清除签到成功<br/>");
				}
				out.print(Arrays.toString(hasSign)+"本月天数："+hasSign.length);

			}else{
				out.print("该玩家活跃度信息不存在!");
			}
		} else {
			out.print("该玩家不存在!");
		}
	}
} else {
	out.print("只有测试服才可设置");
}
%>

<form action="">角色名：<input type="text" name="playerName" />
签到日期（用英文逗号间隔）：<input type="text" name="days" />
清除签到日期（用英文逗号间隔）：<input type="text" name="days2" />
<input type="submit" value="submit" /></form>
</body>

</html>
