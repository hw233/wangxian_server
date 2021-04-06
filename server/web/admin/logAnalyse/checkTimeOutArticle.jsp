<%@page import="com.fy.engineserver.gametime.SystemTime"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="./inc.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
	String commandFile = "checkTimeOutArticle.sh";
	long start = SystemTime.currentTimeMillis();
	String playerName = request.getParameter("playerName");
	String day = request.getParameter("day");
	String[][] outShow = null;
	if (isValid(playerName) && isValid(day)) {
		String fileName = "knapsack.log";
		//out.print("getContextPath:" + request.getContextPath() + "<BR/>");
		out.print("getRealPath[根路径]:" + request.getRealPath("/") + "<BR/>");

		//		String logPath = "/home/game/resin/log/game_server/";
		String logPath = "/home/game/resin_stable/log/game_server/";
		System.out.println("logPath:" + logPath);

		if (day.equals(today())) {

		} else {
			fileName += ("." + day + ".log");
		}
		fileName = logPath + fileName;
		commandFile = request.getRealPath(commandFile);

		out.print("日志文件:" + logPath + "<BR/>");
		out.print("命令文件:" + commandFile + "<BR/>");

		CompoundReturn cr = getLogResult(fileName, commandFile, fileName, playerName);
		if (cr.getBooleanValue()) {
			String[] tempArr = cr.getStringValues();
			outShow = new String[tempArr.length][];
			for (int i = 0; i < tempArr.length; i++) {
				//	String temp = tempArr[i].replace("[-] ", "");
				String[] values = tempArr[i].split(" ");
				//out.print(values.length + ":" + Arrays.toString(values) + "<br/>");
				outShow[i] = values;//new String[] { values[1], values[2], values[3], values[9], values[10], values[11], values[12], values[13], values[14], values[17] };
			}
		}
		out.print("<h1>" + cr.getStringValue() + "</h1>");
	} else {
		out.print("<h1>无效的输入 playerName:[" + playerName + "],day[" + day + "]</h1>");
	}
	out.print("服务器相应时间:" + (SystemTime.currentTimeMillis() - start) + "ms<BR/>");
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>检查玩家物品</title>
</head>
<body>
	<form action="./checkTimeOutArticle.jsp" method="get">
		<input type="text" name="playerName" value="<%=playerName%>">
		<input type="text" name="day" value="<%=day%>"> <input
			type="submit" value="查询">
	</form>
	<%
		if (outShow != null) {
	%>
	<hr>
	<table style="font-size: 12px">
		<%
			for (int i = 0; i < outShow.length; i++) {
		%>
		<tr style="background-color: <%=(i % 2 == 0) ? "#BCBCBC" : ""%>">
			<%
				for (int k = 0; k < outShow[i].length; k++) {
			%>
			<td style="background-color: <%=(k % 2 == 0) ? "#BCBCBC" : ""%>"><%=outShow[i][k]%></td>
			<%
				}
			%>
		</tr>
		<%
			}
		%>
	</table>
	<%
		}
	%>
	<hr>
</body>
</html>