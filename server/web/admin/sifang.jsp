<%@page import="com.google.gson.Gson"%>
<%@page import="com.fy.engineserver.sifang.SiFangManager"%>
<%@page import="com.fy.engineserver.sifang.info.SiFangInfo"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	SiFangInfo[] infos = SiFangManager.getInstance().infos;

	Gson gson = new Gson();
	String action = request.getParameter("action");
	if (action != null && action.equals("startBaoming")) {
		SiFangManager.getInstance().startBaoMing();
		response.sendRedirect("./sifang.jsp");
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>四方神兽</title>
</head>
<body>
<table border="1">

		<tr>
		<td>rb.getId() </td>
		<td>类型 </td>
		<td>家族ID </td>
		<td>状态 </td>
		<td>报名家族 </td>
		<td>参加人物数 </td>
		<td>状态 </td>
		<td>公告次数 </td>
		<td>进场次数 </td>
		<td>杀人数</td>
	</tr>

	<%
		for (int i = 0; i < infos.length; i++) {

			SiFangInfo rb = infos[i];
			
			String jiazuID = "";
			String jiazuNum = "";
			String kill = "";
			for (int j = 0; j < rb.getEnListID().size(); j++) {
				jiazuID = jiazuID + rb.getEnListID().get(j) + ",";
				jiazuNum = jiazuNum + rb.getEnListPlayerID().get(j).size() + ",";
			}
			if (rb.getKillNum() != null) {
				for (int j = 0; j < rb.getKillNum().length; j++) {
					kill = kill + rb.getKillNum()[j] + ",";
				}
			}
			
	%>
	<tr>
		<td><%=rb.getId() %></td>
		<td><%=rb.getInfoType() %></td>
		<td><%=rb.getJiaZuID() %></td>
		<td><%=rb.getState() %></td>
		<td><%=jiazuID %></td>
		<td><%=jiazuNum %></td>
		<td><%=rb.getState() %></td>
		<td><%=rb.getGonggaoNum() %></td>
		<td><%=rb.getJingchangNum() %></td>
		<td><%=kill %></td>
	</tr>
	<%
		}
	%>
	</table>
	<form name="f1">
		<input type="hidden" name="action" value="startBaoming">
		<input type="submit" value="开始报名">
	</form>
</body>
</html>