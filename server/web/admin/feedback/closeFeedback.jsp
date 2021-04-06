<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>


<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.gm.feedback.Feedback"%>
<%@page import="java.text.*"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.gm.feedback.service.FeedbackManager"%>
<%@page import="com.xuanzhi.stat.model.PlayerMakeDealFlow"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.gm.feedback.FeedBackState"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.gm.feedback.Reply"%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

</head>
<body>
		<%
			if(session.getAttribute("gm") == null){
				
				out.print("<a href='showQueryFeedback.jsp'>输入gm账号</a>");
			}else{
				
				String feedbackId = request.getParameter("feedbackId3");
				
				FeedbackManager fbm = FeedbackManager.getInstance();
				
				String result = fbm.closeFeedBack(Long.parseLong(feedbackId),(String)session.getAttribute("gm"));
				
				if(result != null || result.equals("")){
					out.print("gm关闭了这个反馈<br/>");
					out.print("<a href=\"javascript:window.history.back()\">继续操作</a>");
				}else{
					if(result != null){
						out.print(result);
						out.print("<a href=\"javascript:window.history.back()\">继续操作</a>");
					}
				}
				
			}
		
		
		%>
		
				
</body>

</html>