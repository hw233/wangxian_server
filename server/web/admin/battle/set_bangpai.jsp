<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="
java.util.*,
com.fy.engineserver.battlefield.*,
com.fy.engineserver.battlefield.concrete.* " %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.gang.model.Gang,com.fy.engineserver.gang.service.*,com.xuanzhi.boss.game.*"%>
<%@include file="../IPManager.jsp" %><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"> 
<title>帮战战场</title>

<% 
//服务器类型为0代表为可修改的开发服务器
if(GameConstants.getInstance().getServerType() != 0){ 
	return;
}
	GangManager gm = GangManager.getInstance();
	
boolean modify = false;
Gang modifyC = null;
int modifyId = -1;
if(request.getParameter("action") != null && request.getParameter("action").equals("modify")){
	modify = true;
	modifyId = Integer.parseInt(request.getParameter("id"));
	modifyC = gm.getGang(modifyId);
}

if(request.getParameter("action") != null && request.getParameter("action").equals("modified")){
	int id = Integer.parseInt(request.getParameter("id"));
	modifyC = gm.getGang(id);
	modifyC.setGanglevel(Long.parseLong(request.getParameter("level")));
	modifyC.setFunds(Long.parseLong(request.getParameter("fund")));
}
	List list = gm.getGangs();
%>
</head>
<body>
<h2>帮战情况：</h2>
<a href="./set_bangpai.jsp">刷新此页面</a>
<br>
<br>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#C2CAF5" align="center">
<td>编号</td>
<td>帮派名称</td>
<td>帮派等级</td>
<td>帮派资金</td>
<td>操作</td>
</tr>

<%
	String modeName[] = new String[]{"系统匹配","帮派约战"};

	String dayNames[] = new String[]{"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};
	
	for(int i = 0 ; i < list.size() ; i++){
		Gang gang = (Gang)list.get(i);
		out.println("<tr bgcolor='#FFFFFF' align='center'>");
		out.println("<td>"+(i+1)+"</td>");
		
		out.println("<td>"+gang.getName()+"</td>");
		out.println("<td>"+gang.getGanglevel()+"</td>");
		out.println("<td>"+gang.getFunds()+"</td>");
		out.println("<td><a href='./set_bangpai.jsp?action=modify&id="+gang.getId()+"'>修改</a></td>");
		out.println("<tr>");
	}
%>
</table>

<%
	if(modify && modifyC != null){
%>
	<form id='fff'><input type='hidden' name='action' value='modified'>
	<input type='hidden' name='id' value='<%=modifyId %>'>
	<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
	<tr align="center" bgcolor='#ffffff'><td>要修改帮派：</td><td><%= modifyC.getName() %></td></tr>
	<tr align="center" bgcolor='#ffffff'><td>请输入帮派等级</td><td><input type='text' name='level' value='<%= modifyC.getGanglevel() %>'></td></tr>
	<tr align="center" bgcolor='#ffffff'><td>请输入帮派资金</td><td><input type='text' name='fund' value='<%= modifyC.getFunds() %>'></td></tr>
	<tr align="center" bgcolor='#ffffff'><td colspan=2><input type='submit'  value='提交'></td></tr>
	</table>
	</form>
<% 		
	}
%>

<br/>

</body>
</html>
