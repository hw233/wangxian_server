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
<title>查询登陆公告</title>
</head>
<body>


	<%
		String idst = request.getParameter("id");
		if(idst == null || idst.equals("")){
			out.print("没有输入id");
		}
		long id = 0;
		try{
			id = Long.parseLong(idst.trim());
		}catch(Exception e){
			out.print("id 错误");
		}
	
		List<Notice> list = NoticeManager.getInstance().getCircleList();
		Notice notice = null;
		for(Notice n:list){
			if(n.getId() == id){
				notice = n;
				break;
			}
		}
		
		if(notice == null){
			out.print("没有这个公告"+id);
			return;
		}
	
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String beginTime = sdf.format(new Date(notice.getBeginTime()));
		String endTime = sdf.format(new Date(notice.getEndTime()));
	%>

	<form action="updateNotice.jsp" method="post">
		<table cellspacing="1" cellpadding="2" border="1" >
			<tr>
				<td>公告id:</td>
				<td><input type="text" name="id" value="<%=notice.getId() %>"/></td>
			</tr>
			<tr>
				<td>公告名称:</td>
				<td><input type="text" name="name" value="<%=notice.getNoticeName() %>"/></td>
			</tr>
			
				<%
 					String content = notice.getContent();
 					String contentInside = notice.getContentInside();
 					while(content != null &&content.contains("<")){
 						content = content.replace("<","&lt");
 					}
 					while(content != null &&content.contains(">")){
 						content = content.replace(">","&gt");
 					}
 					
 					while(contentInside != null &&contentInside.contains("<")){
 						contentInside = content.replace("<","&lt");
 					}
 					while(contentInside != null &&contentInside.contains(">")){
 						contentInside = content.replace(">","&gt");
 					}
 					
 				%>
			<tr>
				<td>公告内容:</td>
				<td><textarea rows="3" cols="20" name="content"><%=content%></textarea></td>
			</tr>
			<tr>
				<td>公告内容(内部看):</td>
				<td><textarea rows="3" cols="20" name="contentInner" > <%=contentInside%></textarea></td>
			</tr>
			<tr>
				<td>公告开启时间:</td>
				<td><input type="text" name="beginTime" value="<%=beginTime%>" /></td>
			</tr>
			<tr>
				<td>公告失效时间:</td>
				<td><input type="text" name="endTime" value="<%=endTime%>" /></td>
			</tr>
			<tr>
				<td>是否有绑银:</td>
				<td>
				<%
					if(notice.isHavaBindSivler()){
						%>
						
				没有:<input type="radio" name="hava" value="0" />
				有:<input type="radio" checked="checked" name="hava" value="1" />
						<%
					}else{
						%>
					没有:<input type="radio" name="hava" checked="checked"  value="0" />
					有:<input type="radio" name="hava" value="1" />
						
						<%
					}
				
				%>

				</td>
			</tr>
			<tr>
				<td>绑银数量:</td>
				<td><input type="text" name="bindSivlerNum" value="<%=notice.getBindSivlerNum() %>" /></td>
			</tr>
		</table>
		<input type="submit" value="更新" />
 	</form>
</body>
</html>