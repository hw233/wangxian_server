<%@page import="com.fy.engineserver.mail.service.concrete.DefaultMailManager"%>
<%@page import="java.util.Calendar"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.newtask.service.TaskManager"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.newtask.Task"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
		int nowTaday = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
		DefaultMailManager defaultMailManager = (DefaultMailManager)DefaultMailManager.getInstance();
		if (nowTaday != defaultMailManager.sendMailToday) {
			out.println("时间修改成功   现在天:" + nowTaday + "  原来天:" + defaultMailManager.sendMailToday);
			defaultMailManager.sendMailToday = nowTaday;
		}else {
			out.println("时间未有修改   当前天：" + defaultMailManager.sendMailToday);
		}
	%>
</body>
</html>
