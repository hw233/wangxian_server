<%@ page language="java" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>


<%@page import="com.fy.engineserver.activity.quiz.QuizManager"%>
<%@page import="com.fy.engineserver.activity.quiz.*"%>
<%@page import="com.fy.engineserver.gametime.*"%>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>设置答题自动开始</title>
	<link rel="stylesheet" type="text/css" href="../css/common.css" />
</head>

<body>
	<%
	
		out.print("0 能自动开始，1不能自动开始<br/>");
	
		String st = request.getParameter("auto");
		if(st != null){
			int auto = Integer.parseInt(st.trim());
			QuizManager qm = QuizManager.getInstance();
			if(auto == 1){
				qm.open = false;
				out.print("以后不能自动开始");
			}else if(auto == 0){
				qm.open = true;
				out.print("以后能自动开始");
			}else{
				out.print("auto != 0  1");
			}
			
		}else{
			out.print("auto == null");
		}
	
		

	%>
	
</body>
</html>
