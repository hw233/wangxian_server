<%@page import="com.fy.engineserver.validate.ValidateAsk"%>
<%@page import="com.fy.engineserver.util.TimeTool"%>
<%@page
	import="com.fy.engineserver.validate.DefaultValidateManager.UserData"%>
<%@page import="com.fy.engineserver.validate.ValidateManager"%>
<%@page import="com.fy.engineserver.validate.DefaultValidateManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String username = request.getParameter("username");
	if (username == null) {
		username = "";
	}
	String query = request.getParameter("query");
	UserData userData = null;
	if ("query".equals(query)) { //查询
		if (username == null || "".equals(username.trim())) {
			username = "";
		} else {
			ValidateManager validateManager = DefaultValidateManager.getInstance();
			userData = validateManager.getUserData(username);
		}
	}
	if ("modify".equals(query)) { //修改数据
		String chargeMoneyLessStr = request.getParameter("chargeMoneyLess");
		if (chargeMoneyLessStr != null) {
			int chargeMoneyLes = Integer.valueOf(chargeMoneyLessStr);
			if (chargeMoneyLes > 0) {
				DefaultValidateManager.chargeMoneyLess = chargeMoneyLes;
			}
		}
		String maxWrongTimesStr = request.getParameter("maxWrongTimes");
		if (maxWrongTimesStr != null) {
			int maxWrongTimes = Integer.valueOf(maxWrongTimesStr);
			if (maxWrongTimes > 0) {
				DefaultValidateManager.maxWrongTimes = maxWrongTimes;
			}
		}
		String maxTakeTaskNumForOneIpStr = request.getParameter("maxTakeTaskNumForOneIp");
		if (maxTakeTaskNumForOneIpStr != null) {
			int maxTakeTaskNumForOneIp = Integer.valueOf(maxTakeTaskNumForOneIpStr);
			if (maxTakeTaskNumForOneIp > 0) {
				DefaultValidateManager.maxTakeTaskNumForOneIp = maxTakeTaskNumForOneIp;
			}
		}
		String maxLoginUserNumForOneIpStr = request.getParameter("maxLoginUserNumForOneIp");
		if (maxLoginUserNumForOneIpStr != null) {
			int maxLoginUserNumForOneIp = Integer.valueOf(maxLoginUserNumForOneIpStr);
			if (maxLoginUserNumForOneIp > 0) {
				DefaultValidateManager.maxLoginUserNumForOneIp = maxLoginUserNumForOneIp;
			}
		}
		String maxRightTimesStr = request.getParameter("maxRightTimes");
		if (maxRightTimesStr != null) {
			int maxRightTimes = Integer.valueOf(maxRightTimesStr);
			if (maxRightTimes > 0) {
				DefaultValidateManager.maxRightTimes = maxRightTimes;
			}
		}
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户任务数据校验</title>
</head>
<body>
	<form name="us" action="./validateCheck.jsp" method="post">
		输入要查询的用户名:<input type="text" name="username" value="<%=username%>">
		<input type="hidden" name="query" value="query"> <input
			type="submit" value="查询">
	</form>
	<%
		if ("query".equals(query)) {
			if (userData != null) {
				out.print("角色名称:" + userData.playerName + "<BR/>");
				out.print("角色ID:" + userData.playerId + "<BR/>");
				out.print("接取任务数量:" + userData.takeTaskCount + "<BR/>");
				out.print("回答正确数量:" + userData.answerRightTimes + "<BR/>");
				out.print("回答错误数量:" + userData.answerWrongTimes + "<BR/>");
				out.print("当前reason:" + userData.reason + "<BR/>");
				ValidateAsk va = userData.currentAsk;
				out.print("当前题目:" + va == null ? "--" : va + "<BR/>");
				out.print("最后一次修改时间:" + TimeTool.formatter.varChar19.format(userData.lastModifyTime) + "<BR/>");
			} else {
				out.print("<H3>暂无数据</H3>");
			}
		} else {
			out.print("<H3>输入用户名查询</H3>");
		}
	%>
	<hr>
	<hr>
	<form name="sys" action="./validateCheck.jsp" method="post">
		<input type="hidden" name="query" value="modify"> 
		充值金额限制:<input type="text" name="chargeMoneyLess"	value="<%=DefaultValidateManager.chargeMoneyLess%>">
		<br/>
		单人错误次数限制:<input type="text" name="maxWrongTimes"	value="<%=DefaultValidateManager.maxWrongTimes%>">
		<br/>
		同IP任务数量限制:<input type="text" name="maxTakeTaskNumForOneIp"	value="<%=DefaultValidateManager.maxTakeTaskNumForOneIp%>">
		<br/>
		同IP用户数量限制:<input type="text" name="maxLoginUserNumForOneIp"	value="<%=DefaultValidateManager.maxLoginUserNumForOneIp%>">
		<br/>
		正确数量(超过就直接通过):<input type="text" name="maxRightTimes"	value="<%=DefaultValidateManager.maxRightTimes%>">
		<br/>
		<input type="submit" value="提交" style="background-color: red;">
	</form>
</body>
</html>