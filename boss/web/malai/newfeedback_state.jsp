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
<link rel="stylesheet" href="../gm/css/style.css" />
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
		String starttime = request.getParameter("starttime");
		String endtime = request.getParameter("endtime");
		String gmid = request.getParameter("gmid");
// 		String username = request.getParameter("username");
		long 领取问题数 = 0;
		long 关闭问题数 = 0;
		long 挂起问题数 = 0;
		int 满意比例 = 0;
		int 一般比例 = 0;
		int 不满意比例 = 0;
		String 开始工作的时间 = "";
		String 注销时间 = "";
		int 交互条数 = 0;
		NewFeedbackQueueManager manager = NewFeedbackQueueManager.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(starttime==null)starttime = sdf.format(new Date());
		if(endtime==null)endtime = sdf.format(System.currentTimeMillis()+24*60*60*1000);
		List<FeedbackState> alllist = new ArrayList<FeedbackState>();
		if (obj != null) {
			if(iscommit!=null && iscommit.equals("true")){
				if(starttime!=null &&starttime.trim().length()>0 &&endtime!=null &&endtime.trim().length()>0){
					NewFeedbackStateManager stmanager = NewFeedbackStateManager.getInstance();
					if(gmid!=null && gmid.trim().length()>0){
						alllist = stmanager.getStatesByStateidAndGmNum(starttime, endtime, gmid);
					}
				}else{
					out.print("必填项不能为空！");
				}				
			}
		}else{
			out.print("请重启登录");
		}
	%>
	<form method="post" action="?iscommit=true">
		<table>
<!-- 			<tr><th><font color='red'></font>玩家帐号：</th><td><input type='text' value='' name='username'></td></tr> -->
			<tr><th><font color='red'></font>GM编号：</th><td><input type='text' value='' name='gmid'></td></tr>
			<tr><th><font color='red'>*</font>开始时间：</th><td><input type='text' value='<%=starttime %>' name='starttime'></td></tr>
			<tr><th><font color='red'>*</font>结束时间：</th><td><input type='text' value='<%=endtime %>' name='endtime'></td></tr>
			<tr><td colspan="2" align="center"><input type="submit" value='确定'></td></tr>
		</table>
		<table>
			<tr><th>GM编号</th><th>领取问题数</th><th>关闭问题数</th><th>挂起问题数</th><th>满意比例</th><th>一般比例</th><th>不满意比例</th><th>开始工作的时间</th><th>注销时间</th><th>交互条数</th></tr>
			<%
				if(alllist.size()>0){
					for(FeedbackState st:alllist){
						领取问题数 = 领取问题数 + st.getGetFeedbackTime();
						关闭问题数 = 关闭问题数 + st.getCloseFeedbackTime();
						挂起问题数 = 挂起问题数 + st.getGuaqiTime();
						满意比例 = 满意比例 + st.getManyi();
						一般比例 = 一般比例 + st.getYiban();
						不满意比例  = 不满意比例 + st.getBumanyi();
						开始工作的时间 = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm").format(st.getStartWorkTime())+"<br>"+开始工作的时间;
						注销时间 = st.getZhuxiaoTimestr();
						交互条数 = 交互条数 + st.getTalknum();
// 						gmid = st.getGmnum()+"<br>"+gmid;
					}						
				}
			%>
			<tr><td><a onclick="selectgm('<%=gmid%>','<%=starttime%>','<%=endtime%>')"><%=gmid %></a></td><td><%=领取问题数 %></td><td><%=关闭问题数 %></td><td><%=挂起问题数 %></td><td><%=满意比例 %></td><td><%=一般比例 %></td><td><%=不满意比例 %></td><td><%=开始工作的时间 %></td><td><%=注销时间 %></td><td><%=交互条数 %></td></tr>			
		</table>
	</form>
</body>
</html>

