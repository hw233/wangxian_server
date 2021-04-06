<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	BillboardsManager bm = BillboardsManager.getInstance();
	List<String> heads = (List<String>) bm.getBillboardDisk().get(BillboardsManager.THKEY);
	List<String> dates = (List<String>) bm.getBillboardDisk().get(BillboardsManager.ALLDATEKEY);
	String startTimeStr = request.getParameter("startTime");
	String endTimeStr = request.getParameter("endTime");
	String type = request.getParameter("type");
%>

<%@page import="com.fy.engineserver.newBillboard.BillboardsManager"%>
<%@page import="java.util.List"%>
<%@page
	import="com.fy.engineserver.newBillboard.monitorLog.NewServerBillboard"%>
<%@page import="java.io.Serializable"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.fy.engineserver.util.TimeTool"%>
<%@page import="java.util.LinkedList"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新服排行</title>
</head>
<body>
<form action="">时间段(格式:2014-01-01 00:00:00)<input type="text"
	name="startTime" value="<%=startTimeStr == null ? "" : startTimeStr%>" />&nbsp;-&nbsp;
<input type="text" name="endTime"
	value="<%=endTimeStr == null ? "" : endTimeStr%>" /><br>
排行类型<input type="text" name="type" value="<%=type == null ? "" : type%>" /><input
	type="submit" /></form>
<table>
	<tr>
		<%
			List<String> showKey = new LinkedList<String>();
			if (!"".equals(startTimeStr) && startTimeStr != null && !"".equals(endTimeStr) && endTimeStr != null) {
				long startTime = TimeTool.formatter.varChar19.parse(startTimeStr);
				long endTime = TimeTool.formatter.varChar19.parse(endTimeStr);
				for (String keyTime : dates) {
					long key = TimeTool.formatter.varChar19.parse(keyTime + ":00");
					if (startTime <= key && key <= endTime) {
						showKey.add(keyTime);
					}
				}
			}
			if (showKey.size() > 0) {
				for (String th : heads) {
					out.print("<th>" + th + "</th>");
				}
		%>
	</tr>
	<%
		String temp = "";
		String classStyle="color-1";
			for (String date : showKey) {
				List<NewServerBillboard> billboards = (List<NewServerBillboard>) bm.getBillboardDisk().get(date);
				if (billboards != null) {
					for (NewServerBillboard billboard : billboards) {
						boolean change = false;
						if (billboard != null) {
							if (type != null && !"".equals(type)) {
								if (type.equals(billboard.getSubMenu())) {
									if (!temp.equals(billboard.getSubMenu())) {
										temp = billboard.getSubMenu();
										change = true;
										classStyle=(classStyle.endsWith("color-1"))?"color-2":"color-1";
									}
	%>
	<tr class='<%=classStyle%>'>
		<td><%=date%></td>
		<td><%=billboard.getMenu()%></td>
		<td><%=billboard.getSubMenu()%></td>
		<td><%=billboard.getName()%></td>
		<td><%=billboard.getUserName()%></td>
		<td><%=billboard.getId()%></td>
	</tr>
	<%
		}
							} else if (type == null || "".equals(type)) {
								if (!temp.equals(billboard.getSubMenu())) {
									temp = billboard.getSubMenu();
									change = true;
									classStyle=(classStyle.endsWith("color-1"))?"color-2":"color-1";
								}
	%>
	<tr class='<%=classStyle%>'>
		<td><%=date%></td>
		<td><%=billboard.getMenu()%></td>
		<td><%=billboard.getSubMenu()%></td>
		<td><%=billboard.getName()%></td>
		<td><%=billboard.getUserName()%></td>
		<td><%=billboard.getId()%></td>
	</tr>
	<%
		}
						}
					}
				}
			}
		}
	%>
</table>

<style type="text/css">
body {
	font-size: 12px;
}

table {
	border-spacing: 1px;
}

th {
	background-color: #9ED18E;
}

td {
	text-align: center;
	font-size: 12px;
}

td::selection {
	background-color: blue;
	font-size: 20px;
}

/*tr:nth-child(2n+1) {
		background-color: #D3FAC7;
	}
	tr:nth-child(2n) {
		background-color: #E8F2FE;
	}*/
.color-1 {
	background-color: #D3FAC7;
}

.color-2 {
	background-color: #E8F2FE;
}
</style>
</body>
</html>
