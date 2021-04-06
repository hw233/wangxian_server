<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//Dth HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dth">

<%@page import="com.fy.engineserver.notice.NoticeManager"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.notice.Notice"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查询所有临时公告</title>
</head>
<body>

	
	<%
	
		NoticeManager nm = NoticeManager.getInstance();
		List<Notice> list1 =  nm.getOnceList();
		if(list1 != null && list1.size() > 0){
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			Notice effect = nm.getEffectNotice();
			String beginTime = "";
			String endTime = "";
			String effectSt = "没有起作用";
			%>
		<table cellspacing="1" cellpadding="2" border="1" >
			<tr>
 				<th>id</th>
 				<th>公告名称</th>
 				<th>公告描述</th>
 				<th>公告内部描述</th>
 				<th>公告开启时间(2011-01-11 00:00:00)</th>
 				<th>正在起作用</th>
 				<th>操作</th>
 			</tr>
 			
 			<% 
 				for(Notice n:list1){
 					if(n.isDelete()){
 						continue;
 					}
 					try{
	 					beginTime = sdf.format(new Date(n.getBeginTime()));
	 					endTime = sdf.format(new Date(n.getEndTime()));
 					}catch(Exception e){
 						out.print("时间格式错误");
 					}
 					if(n == effect){
 						effectSt = "起作用";
 					}
 					%>
 			<tr>
 				<td><%=n.getId() %></td>
 				<td><%=n.getNoticeName()%></td>
 				
 					<%
 					String content = n.getContent();
 					String contentInside = n.getContentInside();
 					while(content != null  &&content.contains("<")){
 						content = content.replace("<","&lt");
 					}
 					while(content != null  && content.contains(">")){
 						content = content.replace(">","&gt");
 					}
 					
 					while(contentInside != null  &&contentInside.contains("<")){
 						contentInside = content.replace("<","&lt");
 					}
 					while(contentInside != null &&contentInside.contains(">")){
 						contentInside = content.replace(">","&gt");
 					}
 					
 				%>
 				
 				<td><%=content%></td>
 				<td><%= contentInside %></td>
 				<td><%= beginTime%></td>
 				<td><%=effectSt %></td>
 				<td><a href="queryOneTempNotice.jsp?id=<%=n.getId() %>">查看</a><%="||" %><a href="deleteNotice.jsp?id=<%=n.getId() %>">删除</a></td>
 			</tr>
 					
 					<%
 				}
 			
 			%>
 		</table>
			<%
		}else{
			out.print("现在还没有临时公告<br/>");
			out.print("<a href=\"setTempNotice.jsp\">设置临时公告</a>");
		}
	%>
</body>
</html>