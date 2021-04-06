<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.newtask.service.TaskManager"%>
<%@page import="com.fy.engineserver.newtask.Task"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%

	String startTaskidStr = request.getParameter("startTaskidStr");
	String option = request.getParameter("option");
	List<List<Task>> list = new ArrayList<List<Task>>();
	
	if ("query".equals(option)) {
		if(startTaskidStr != null ){
			TaskManager manager = TaskManager.getInstance();
			Task task = manager.getTask(Long.valueOf(startTaskidStr));
			if (task != null) {
				List<Task> first = new ArrayList<Task>();
				first.add(task);
				list.add(first);
				List<Task> next = manager.getnextTask(task.getGroupName());
				if (task.getGroupName() != null) {
					while(next != null && next.size() > 0) {
						list.add(next);
						Task temp = next.get(0);
						next = manager.getnextTask(temp.getGroupName());
					}
				}
			} else {
				out.print("<HR>任务不存在,请输入正确的任务id<HR>");
			}
		} else {
			out.print("<HR>请输入任务id<HR>");
		}
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查询后续任务</title>
</head>
<body>
<form action="./nextTasks.jsp" method="post">
	<input type="hidden" value="query" name="option">
	输入要查询人起始任务ID:<input name="startTaskidStr" type="text" value="<%=startTaskidStr==null ? "" : startTaskidStr %>"><input type="submit" value="查询">
</form>
<hr>
<%
	if (list.size() > 0) {
%>
	<table style="font-size: 12px;text-align: center;" width="50%">
		<tr style="background-color: black;color: white;font-weight: bold;font-size: 14px;">
			<td>ID</td>
			<td>名称</td>
			<td>统计名</td>
			<td>等级</td>
		</tr>
	<%
		int index =0;
		for (List<Task> sonList : list) {
			StringBuffer nameStatBuffer = new StringBuffer();
			StringBuffer nameBuffer = new StringBuffer();
			StringBuffer idBuffer = new StringBuffer();
			StringBuffer levelBuffer = new StringBuffer();
			for (Task t : sonList) {
				nameBuffer.append(t.getName() + "<BR>");
				nameStatBuffer.append(t.getName_stat()+"<BR>");
				idBuffer.append(t.getId()+"<BR>");
				levelBuffer.append(t.getGrade()+"<BR>");
			}
		%>
		<tr style="background-color: <%=index++%2==0 ? "#F5FDC1" :"#9CFCC8"%>">
			<td><%=idBuffer.toString() %></td>
			<td><%=nameBuffer.toString() %></td>
			<td><%=nameStatBuffer.toString() %></td>
			<td><%=levelBuffer.toString() %></td>
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