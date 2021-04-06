<%@page import="com.fy.engineserver.activity.ActivityIntroduce"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.activity.ActivityManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%

	String activityName = request.getParameter("activityName");
	if (activityName != null && !"".equals(activityName.trim())) {
		List<ActivityIntroduce> list =ActivityManager.getInstance().getActivityIntroduces();
		ActivityIntroduce ai = null;
		for (ActivityIntroduce activityIntroduce : list) {
			if (activityIntroduce.getName().equals(activityName)) {
				ai = activityIntroduce;
				break;
			}
		}
		if (ai != null) {
			list.remove(ai);
			out.print("移除:" + ai.toString() + "<BR/>");
		}
	} else {
		activityName = "";
	}

%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

<form action="./removeActivity.jsp">
	<hr/>
	要删除的活动名字:<input name="activityName" value="<%=activityName%>">
	<input type="submit">
</form>
</body>
</html>