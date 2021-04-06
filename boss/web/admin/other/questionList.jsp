<%@page import="com.fy.boss.gm.gmuser.server.TransferQuestionManager"%>
<%@page import="com.fy.boss.gm.XmlServer"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.boss.gm.XmlServerManager"%>
<%@page import="com.fy.boss.gm.gmuser.TransferQuestion"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link type="text/css" rel="stylesheet" href="style.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>问题列表</title>
<script type="text/javascript">
</script>
</head>
<body bgcolor="#c8edcc">
<h1>问题列表</h1>
<%
	String questionType1 = request.getParameter("questionType1");
	String questionreason = request.getParameter("questionreason");
	String handletodoor = request.getParameter("handletodoor");
	String handledoor = request.getParameter("handledoor");
	String id = request.getParameter("updateid");
	List<TransferQuestion> list = null;
	if(id!=null&&id.trim().length()>0){
		TransferQuestion question = TransferQuestionManager.getInstance().getQuestionById(Long.parseLong(id));	
		if(questionreason!=null&&questionreason.trim().length()>0){
			question.setQuestionMess(questionreason);
		}
		if(handledoor!=null&&handledoor.trim().length()>0){
			list = TransferQuestionManager.getInstance().getQuestionsByBuMen(handledoor);
		}
		if(handletodoor!=null&&handletodoor.trim().length()>0){
			question.setHandlOtherBuMeng(handletodoor);
		}
	}
	
	if(questionType1!=null&&questionType1.trim().length()>0){
		list = TransferQuestionManager.getInstance().getQuestionsByYiJiType(questionType1);
	}	
%>
<form method="post">
	<table>
		<tr><th>事件编号</th><th>游戏名称</th><th>服务器名</th><th>账号</th><th>VIP用户</th><th>一级分类</th><th>二级分类</th><th>提交时间</th><th>问题所在部门</th><th>转交次数</th><th>状态</th><th>操作</th></tr>
		<%
			if(list!=null&&list.size()>0){
				for(TransferQuestion qq:list){
		%>
		<tr><td><%=qq.getEventid() %></td><td><%=qq.getGameName() %></td><td><%=qq.getServerName() %></td><td><%=qq.getUsername() %></td><td><%=qq.getViplevel() %></td><td><%=qq.getQuestionType1() %></td><td><%=qq.getQuestionType2() %></td>
		<td><%=qq.getRecordTime() %></td><td><%=qq.getHandlBuMeng() %></td><td><%=qq.getTransferNum() %></td><td><%=qq.getHandleState() %></td><td><a title="我来处理" href="questionLastHandle.jsp?id=<%=qq.getId() %>">处理</a>||<a title="我来处理" href="questionLastHandle.jsp?idd=chakan&id=<%=qq.getId() %>">查看</a></td></tr>
		<%			
					
				}
			}
		%>
		
		
	</table>
	
	
</form>	
</body>
</html>