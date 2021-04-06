<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.gm.feedback.Feedback"%>
<%@page import="java.text.*"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.gm.feedback.service.FeedbackManager"%>
<%@page import="com.xuanzhi.stat.model.PlayerMakeDealFlow"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.gm.feedback.FeedBackState"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.gm.feedback.Reply"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

</head>
<body>
		
		<%
			String[] states = {"未处理","等待","新","关闭"};
			String id = request.getParameter("updateId");
			
			
			Feedback f = FeedbackManager.getInstance().getFeedBack(Long.parseLong(id));
			if(f.noticeGm){
				f.noticeGm = false;
				StringBuffer sb = new StringBuffer();
				sb.append("####");
				sb.append(id+"|");
				sb.append(states[f.getGmState()]);
				sb.append("####");
				out.print(sb.toString());
			}
			
			
		
		%>
		
		
				
</body>

</html>