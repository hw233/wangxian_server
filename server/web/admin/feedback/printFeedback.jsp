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
<%@page import="com.fy.engineserver.gametime.*"%>
<%@page import="com.fy.engineserver.gm.feedback.GMRecord"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>打印反馈</title>
</head>
<body >
	
	<%
		String begin = request.getParameter("beginTime");
		String end = request.getParameter("endTime");

		if(begin == null || end == null || begin.equals("") || end.equals("")){
			
			SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd");
			String startTime = sdf.format(SystemTime.currentTimeMillis() - 24*60*60*1000);
			String endTime = sdf.format(SystemTime.currentTimeMillis() + 24*60*60*1000);
			
			
			%>
			<form action="">
				开始时间:(2011-01-11)<input name="beginTime" type="text" value=<%=startTime%>>
				截止时间:(2011-01-11)<input name="endTime" type="text" value=<%=endTime%>>
				
				<input type="submit" value="打印" />
			</form>
			
			<%
		}else{
			
			FeedbackManager fbm = FeedbackManager.getInstance();
			try{
			String result = fbm.writeToTxt(begin,end);
			if(result.equals("")){
				out.print("打印完成");
			}else{
				out.print(result);
			}
			}catch(Exception e){
				StackTraceElement[] ses = e.getStackTrace();
				for(StackTraceElement se :ses){
					out.print(se.toString()+"<br/>");
				}
				
			}
			
		}
	
	%>
	
	
			
</body>
</html>