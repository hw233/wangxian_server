<%@page import="com.fy.engineserver.newtask.ActivityTaskExp"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.newtask.service.TaskManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%

	HashMap<Integer, ActivityTaskExp> map = TaskManager.getInstance().activityPrizeMap;

%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

	<table style="font-size: 12px;" border="1">
		<tr>
			<td></td>
		</tr>
		<%
		
			for (Iterator<Integer> itor = map.keySet().iterator();itor.hasNext();) {
				int id = itor.next();
				%>
					<tr>
						<td><%=id %></td>
					</tr>
				<%
			}
		
		%>
	</table>

</body>
</html>