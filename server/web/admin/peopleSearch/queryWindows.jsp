<%@page import="com.fy.engineserver.menu.Option"%>
<%@page import="com.fy.engineserver.menu.MenuWindow"%>
<%@page import="com.fy.engineserver.menu.WindowManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
	String wid = request.getParameter("wid");
	MenuWindow menuWindow = null;
	if (wid == null || "".equals(wid)) {
		wid = "";
	} else {
		int id = Integer.valueOf(wid);
		menuWindow = WindowManager.getInstance().getWindowById(id);
	}
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form action="./queryWindows.jsp" method="post">
		要查询的窗口:<input type="text" name="wid" value="<%=wid%>"> <input
			type="submit" value="查询">
	</form>
	<%
		if (!"".equals(wid)) {
			if (menuWindow != null) {
	%>
		<table border="1" style="font-size: 12px;">
			<tr>
				<td>id</td>
				<td>npcId</td>
				<td>options</td>
			</tr>
			<tr>
				<td><%=menuWindow.getId() %></td>
				<td><%=menuWindow.getNpcId() %></td>
				<td><%
					for (Option option : menuWindow.getOptions()) {
						out.print(option.getClass().getSimpleName() + "/" + option.getText() + "<BR/>");
					}
				%></td>
			</tr>
		</table>
	<%
		} else {
				out.print("窗口不存在:" + wid);
			}
		}
	%>
</body>
</html>