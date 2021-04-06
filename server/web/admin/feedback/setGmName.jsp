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
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.gm.feedback.FeedBackState"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.gm.feedback.Reply"%>



<%@page import="com.fy.engineserver.gm.feedback.GMRecord"%>
<%@page import="com.xuanzhi.tools.cache.CacheObject"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>设置gmName</title>

</head>
<body>
		<%
		
			String  query = request.getParameter("query");
			if(query != null && !query.equals("")){
				
				Collection collection =  FeedbackManager.getInstance().gmRecordCache.values();
				
				Iterator it = collection.iterator();
				GMRecord gr = null;
				while(it.hasNext()){
					gr = (GMRecord)(((CacheObject)it.next()).object);
					if(gr != null){
						if(gr.getGmName() == null){
							gr.setGmName("");
							out.print("设置成功"+"<br/>");
						}
					}else{
						out.print("gr null"+"<br/>");
					}
				}
				return;
			}
		%>
		
		<form action="">
			
			查询:<input type="text" name="query"/>
			<input type="submit" value="submit"/>
		
		</form>
		
				
</body>

</html>