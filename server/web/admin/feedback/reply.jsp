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
				
				String feedbackId = request.getParameter("feedbackId1");
				String replyContent = request.getParameter("reply");
				
				FeedbackManager fbm = FeedbackManager.getInstance();
				Feedback fb =  fbm.getFeedBack(Long.parseLong(feedbackId));
				if(fb != null){
					fb.setLastGmId((String)session.getAttribute("gm"));
				}
				
				String result = fbm.GmAnserFeedBack((String)session.getAttribute("gm"),fb.getId(),replyContent);
				FeedbackManager.logger.warn("[GM恢复反馈] [成功] [gmid:"+fb.getLastGmId()+"] [内容："+fb.getContent()+"] [反馈id:"+fb.getFid()+"] [in reply .....]");
				if(result != null && result.equals("yes")){
					out.print("回复完成<br/>");
					out.print("<a href=\"javascript:window.history.back()\">继续操作</a>");
				}else if(result != null){
					out.print(result);
					out.print("<a href=\"javascript:window.history.back()\">继续操作</a>");
				}
				
			}
		
		
		%>
		
				
</body>

</html>