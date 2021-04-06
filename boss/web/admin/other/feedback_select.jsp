<%@page import="com.fy.boss.gm.newfeedback.NewFeedback"%>
<%@page import="com.fy.boss.gm.newfeedback.GmTalk"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.boss.gm.newfeedback.FeedbackState"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.boss.gm.newfeedback.service.NewFeedbackStateManager"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.fy.boss.gm.newfeedback.service.NewFeedbackQueueManager"%>
<%@page import="com.xuanzhi.tools.text.StringUtil"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="gm/css/style.css" />
<title>统计</title>
<script type="text/javascript">
function selectgm(gmid,starttime,endtime){
// 	alert("gmid:"+gmid+"--starttime:"+starttime+"--endtime:"+endtime);
	window.open("getGmNewFeedbacks.jsp?gmid="+gmid+"&starttime="+starttime+"&endtime="+endtime);
}
</script>

</head>

<body bgcolor="#c8edcc">
	<h2>统计</h2>
	<hr>
	<%
		Object obj = session.getAttribute("authorize.username");
		String iscommit = request.getParameter("iscommit");
		String username = request.getParameter("username");
		String playername = request.getParameter("playername");
		NewFeedbackQueueManager manager = NewFeedbackQueueManager.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<NewFeedback> alllist2 = new ArrayList<NewFeedback>();
		if (obj != null) {
			if(iscommit!=null && iscommit.equals("true")){
				if(username!=null && username.trim().length()>0){
					alllist2 = manager.getFeedbackByUserName(username);
				}else if(playername!=null && playername.trim().length()>0){
					alllist2 = manager.getFeedbackByPlayername(playername);
				}
			}
		}else{
			out.print("请重启登录");
		}
	%>
	<form method="post" action="?iscommit=true">
		<table>
			<tr><th><font color='red'></font>玩家帐号：</th><td><input type='text' value='' name='username'></td></tr>
			<tr><th><font color='red'></font>玩家角色：</th><td><input type='text' value='' name='playername'></td></tr>
			<tr><td colspan="2" align="center"><input type="submit" value='确定'></td></tr>
		</table>
		<table>
			<tr><th>帐号</th><th>角色名</th><th>问题</th><th>内容</th><th>提问时间</th><th>VIP等级</th><th>服务器</th></tr>
			<%
				for(NewFeedback nf:alllist2){
					if(nf!=null){
						GmTalk [] talks = NewFeedbackQueueManager.getInstance().getGmTalkById(nf.getId());
						StringBuffer sb = new StringBuffer();
						for(GmTalk t:talks){
							if(t.getName().contains("GM")){
								sb.append("<font color='green'>"+t.getName()+"</font>:"+t.getTalkcontent()+"<br>");
							}else{
								sb.append("<font color='red'>"+nf.getPlayername()+"</font>:"+t.getTalkcontent()+"<br>");
							}
							
						}
				%>
					<tr><td><%=nf.getUsername() %></td><td><%=nf.getPlayername() %></td><td><%=nf.getTitle() %></td><td><%=sb.toString() %></td><td><%=nf.getSendtime() %></td><td><%=nf.getViplevel() %></td><td><%=nf.getServername() %></td></tr>	
				<%						
					}
				}
			%>
		</table>
	</form>
</body>
</html>

