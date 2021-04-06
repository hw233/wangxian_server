<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.fy.boss.gm.newfeedback.service.NewFeedbackStateManager"%>
<%@page import="com.fy.boss.gm.newfeedback.FeedbackState"%>
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
<title>注销反馈</title>
</head>

<script language="javascript">
	function handle(gmid){
		window.location.replace("http://116.213.192.200:8110/game_boss/NewFeedback_handle.jsp?handle="+gmid);
	}
</script>

<body bgcolor="#c8edcc" style="background-image: url('111.jpg');">
	<table width="750px" height="550px">
	<tr>
	<td colspan='11'>
	<table>
		<%
			String gmnum = request.getParameter("handle");
			if(gmnum!=null){
				//
				NewFeedbackStateManager statemanager = NewFeedbackStateManager.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String recordid = sdf.format(new Date());
				if(NewFeedbackStateManager.getInstance().isaddNewState(recordid, gmnum)){
					FeedbackState stat = new FeedbackState();
					stat.setZhuxiaoTime(System.currentTimeMillis());
					stat.setZhuxiaoTimestr(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()));
					stat.setStateid(recordid);
					stat.setGmnum(gmnum);
					statemanager.addNewState(stat);
				}else{
					List<FeedbackState> states = statemanager.getStates();
					for(FeedbackState pp:states){
						if(recordid.equals(pp.getStateid()) && gmnum.equals(pp.getGmnum())){
							pp.setZhuxiaoTime(System.currentTimeMillis());
							if(pp.getZhuxiaoTimestr()!=null){
								pp.setZhuxiaoTimestr(pp.getZhuxiaoTimestr()+"<br>"+(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis())));
							}else{
								pp.setZhuxiaoTimestr((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()))+"<br>");
							}
						}
					}
				}
				//
				List<FeedbackState> fstates = statemanager.getFeedbacks(recordid);
				long 领取问题个数 = 0;
				long 满意评价个数 = 0;
				long 不满意评价数 = 0;
				long 一般评价个数 = 0;
				long 完成关闭次数 = 0;
				long 反馈扭转个数 = 0;
				long 剩余挂起个数 = 0;
				if(fstates!=null && fstates.size()>0){
					for(FeedbackState sbstat:fstates){
						if(sbstat.getGmnum()!=null && sbstat.getGmnum().equals(gmnum)){
							领取问题个数 = sbstat.getGetFeedbackTime();
							满意评价个数 = sbstat.getManyi();
							不满意评价数 = sbstat.getBumanyi();
							一般评价个数 = sbstat.getYiban();
							完成关闭次数 = sbstat.getCloseFeedbackTime();
							反馈扭转个数 = sbstat.getFankuiTime();
							
						}
					}
				}
		%>		<h3><B>注销成功</B></h3>
				<h3><B><%=gmnum %>您好，感谢您一天的辛勤工作，公司全体人员向您表示真诚的谢意！;</B></h3>
				<hr>
				<h4>领取问题个数：<%=领取问题个数 %></h4>
				<h4>满意评价个数：<%=满意评价个数 %></h4>
				<h4>不满意评价数：<%=不满意评价数 %></h4>
				<h4>一般评价个数：<%=一般评价个数 %></h4>
				<h4>直接关闭次数：<%=完成关闭次数 %></h4>
				<h4>反馈扭转个数：<%=反馈扭转个数 %></h4>
				<h4>剩余挂起个数：<%=剩余挂起个数 %></h4>
				<hr>
				<font size='5' color='red'><b>感谢您优异的工作表现，忙了一天好好休息吧，回家注意安全！</b></font>
		<%	
			}
		%>
		
	</table>	
	</td>
	</tr>
	</table>

</body>
</html>

