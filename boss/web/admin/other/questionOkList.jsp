<%@page import="com.fy.boss.gm.gmuser.QuestionQuery"%>
<%@page import="com.fy.boss.gm.gmuser.server.TransferQuestionManager"%>
<%@page import="com.fy.boss.gm.XmlServer"%>
<%@page import="java.util.*"%>
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

</head>
<body bgcolor="#c8edcc">
<h1>已处理问题列表</h1>
<%
// 	Object obj = session.getAttribute("authorize.username");
	String gmname = request.getParameter("gmname");
	String begintime = request.getParameter("begintime");
	String endtime = request.getParameter("endtime");
	List<TransferQuestion> list = new ArrayList<TransferQuestion>();
	if(gmname!=null&&gmname.trim().length()>0&&begintime!=null&&begintime.trim().length()>0&&endtime!=null&&endtime.trim().length()>0){
		list = TransferQuestionManager.getInstance().getQueriesByGmName(gmname,begintime,endtime);
	}else{
		out.print("<font red='color'>名称不能为空!</font>");
	}
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String begin = sdf.format(new Date());
	String end = sdf.format(System.currentTimeMillis()+24*60*60*1000);
%>
<form method="post" name='gogo'>
	<table>
		<tr><th>GM名称：</th><td><input type='text' name='gmname' id='gmname'></td></tr>
		<tr><th>时间段：</th><td><input type='text' name='begintime' id='begintime' value='<%=begin%>'>--<input type='text' name='endtime' id='endtime' value='<%=end%>'></td></tr>
		<tr><input type='submit' value='查询'></tr>
	</table>
</form>	
	<table id="addmess"><caption>数量：<div><%=list.size() %> 条 </div></caption>
		<tr><th>编号</th><th>游戏名称</th><th>服务器名</th><th>账号</th><th>VIP用户</th><th>一级分类</th><th>二级分类</th><th>提交时间</th><th>问题所在部门</th><th>当前状态</th><th>操作</th></tr>
		<%
			if(list!=null&&list.size()>0){
				for(TransferQuestion qq:list){
		%>
					<tr id='<%=qq.getId() %>del'><td><%=qq.getEventid() %></td><td><%=qq.getGameName() %></td><td><%=qq.getServerName() %></td><td><%=qq.getUsername() %></td><td><%=qq.getViplevel() %></td><td><%=qq.getQuestionType1() %></td><td><%=qq.getQuestionType2() %></td>
					<td><%=qq.getRecordTime() %></td><td><%=qq.getHandlOtherBuMeng() %></td><td id='<%=qq.getId() %>'><%=qq.getCurrHadler() %></td><td><a title="查看问题" href="gm/manager/questionLastHandle.jsp?idd=chakan&id=<%=qq.getId() %>">查看</a></td></tr>
		<%			
				}
			}else{
				out.print("没有问题处理~");
			}
		%>
	  
		
	</table>
	
	

</body>
</html>