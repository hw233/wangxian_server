<%@page import="com.fy.engineserver.dajing.DajingGroup"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.engineserver.dajing.DajingStudio"%>
<%@page import="com.fy.engineserver.dajing.DajingStudioManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String username = request.getParameter("username");
	if (username != null && !"".equals(username.trim())) {
		boolean found = false;
		DajingStudioManager dsManager = DajingStudioManager.getInstance();
		if(dsManager.initialized == false){
			dsManager.init();
		}
		DajingStudio[]  dss = dsManager.getDajingStudios();
		for (DajingStudio dd : dss) {
			ArrayList<DajingGroup> dgArr = dd.groupList;
		}
		if (!found) {
			out.print("<h2>" + username + " 不在打金工作室限制中</h2>");
		}
	} else {
		username = "";
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

	<form action="" method="post">
		<input name="username" value="<%=username%>"> <input
			type="submit" value="移除">
	</form>
</body>
</html>