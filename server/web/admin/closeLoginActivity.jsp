<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.fy.engineserver.activity.loginActivity.LoginStatDate"%>
<%@page import="com.fy.engineserver.activity.loginActivity.LoginActivityManager"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=utf-8">
		<title>closeloginActivity</title>
	</head>
	<body>
	<%
		LoginActivityManager.getInstance().setOpen(false);	
		String pid = request.getParameter("pid");
		if(pid!=null){
			LoginStatDate lsd = (LoginStatDate) LoginActivityManager.getInstance().getDdc().get(Long.parseLong(pid)+"login");
			if(lsd!=null){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				out.print("上次发奖时间："+sdf.format(lsd.getLasttime()));
				out.print("连续几天登录："+lsd.getValue());
			}else{
				out.print("没有符合的记录！可能是该玩家这几天没有登录，请去邮件里面详细查询！");
			}
		}
	%>
	<form method="post">
		<table>
			<tr><td><input type='text' name = 'pid'></td><td><input type="submit" value='GO'></td></tr>
		</table>
	</form>
	</body>
</html>
