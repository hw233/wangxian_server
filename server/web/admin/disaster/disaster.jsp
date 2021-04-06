<%@page import="com.fy.engineserver.util.server.TestServerConfigManager"%>
<%@page import="com.fy.engineserver.activity.disaster.DisasterConstant"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="com.fy.engineserver.activity.disaster.DisasterManager"%>
<%@page import="com.fy.engineserver.activity.disaster.module.DisasterOpenModule"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
		String playerId = request.getParameter("playerId");
		String action = request.getParameter("action");
		String startHour = request.getParameter("startHour");
		String tempStr = request.getParameter("tempStr");
		String entertimes = request.getParameter("entertimes");
		playerId = playerId == null ? "" : playerId;
		entertimes = entertimes == null ? "" : entertimes;
		action = action == null ? "" : action;
		tempStr = tempStr == null ? "" : tempStr;
		startHour = startHour == null ? "" : startHour;
		if (!"setOpenTime".equals(action)) {
			for (int i=0; i<DisasterManager.getInst().baseModule.getTimeRules().size(); i++) {
				out.println("[开启时间:" + new Timestamp(DisasterManager.getInst().baseModule.getTimeRules().get(i).getOpenTime()[0]) + "]-[结束时间:"+new Timestamp(DisasterManager.getInst().baseModule.getTimeRules().get(i).getOpenTime()[1])+"]<br>");
			}
		}
		out.println("匹配时间:" + DisasterConstant.matchCD + " = 最小开始人数:" + DisasterConstant.MIN_NUM + " =  每场进入次数:" + DisasterConstant.MAX_ENTER_TIMES + "<br>");
		if ("setOpenTime".equals(action)) {
			if (!TestServerConfigManager.isTestServer()) {
				out.println("不是测试服，不允许修改! ");
				return ;
			}
			DisasterOpenModule module = new DisasterOpenModule(startHour);
			DisasterManager.getInst().baseModule.getTimeRules().add(module);
			for (int i=0; i<DisasterManager.getInst().baseModule.getTimeRules().size(); i++) {
				out.println("[开启时间:" + new Timestamp(DisasterManager.getInst().baseModule.getTimeRules().get(i).getOpenTime()[0]) + "]-[结束时间:"+new Timestamp(DisasterManager.getInst().baseModule.getTimeRules().get(i).getOpenTime()[1])+"]<br>");
			}
			out.println("ok！");
		} else if ("setenterrule".equals(action)) {
			if (!TestServerConfigManager.isTestServer()) {
				out.println("不是测试服，不允许修改! ");
				return ;
			}
			out.println("匹配时间:" + DisasterConstant.matchCD + " = 最小开始人数:" + DisasterConstant.MIN_NUM + " =  每场进入次数:" + DisasterConstant.MAX_ENTER_TIMES + "<br>");
		int playerNum = Integer.parseInt(startHour); 
			long matchcd = Long.parseLong(tempStr);
			int ee = Integer.parseInt(entertimes);
			DisasterConstant.matchCD = matchcd;
			DisasterConstant.MIN_NUM = playerNum;
			DisasterConstant.MAX_ENTER_TIMES = ee;
			out.println("ok!"); 
		}
		/* for (int i=0; i<DisasterManager.getInst().baseModule.getTimeRules().size(); i++) {
			out.println("now:" + System.currentTimeMillis() + "<br>");
			out.println("[开启时间:" + Arrays.toString(DisasterManager.getInst().baseModule.getTimeRules().get(i).getOpenTime()) + "]<br>");
			out.println("[开启时间:" + new Timestamp(DisasterManager.getInst().baseModule.getTimeRules().get(i).getOpenTime()[0]) + "]<br>");
		} */
	%>

	<form action="disaster.jsp" method="post">
		<input type="hidden" name="action" value="setOpenTime" />时间:
		<input type="text" name="startHour" value="<%=startHour%>" />
		<input type="submit" value="设置开启时间" />
	</form>
	<br />
	<form action="disaster.jsp" method="post">
		<input type="hidden" name="action" value="setenterrule" />最少人数:
		<input type="text" name="startHour" value="<%=startHour%>" />人数不足多久开启:
		<input type="text" name="tempStr" value="<%=tempStr%>" />每场可进入次数:
		<input type="text" name="entertimes" value="<%=entertimes%>" />
		<input type="submit" value="设置匹配人数时间" />
	</form>
	<br />
</body>
</html>