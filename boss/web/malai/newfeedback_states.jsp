<%@page import="com.fy.boss.gm.newfeedback.FeedbackPlayerState"%>
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
	function selectgm(gmid){
		window.open("getGmNewFeedbacks.jsp?gmid="+gmid);
	}
	
</script>
</head>

<body bgcolor="#c8edcc">
<!-- <h1>可能有的数据的准确度不高，我会再一项项的多过几遍</h1> -->
	<%
		Object obj = session.getAttribute("authorize.username");
		NewFeedbackQueueManager nq = NewFeedbackQueueManager.getInstance();
		int queussize = nq.getQueue().size();
		int vipquesusize = nq.getVipqueue().size();
		out.print("<table><caption><font color='red'><B>当前队列统计</B></font></caption><th>普通在线排队用户："+queussize);
		out.print("，VIP在线排队用户："+vipquesusize);
		out.print(",一共还有"+(queussize+vipquesusize)+"个问题等待处理.</th></table><hr color='blue' title='分割线'>");
		if(obj!=null){
			
		}else{
			out.print("请重新登录！");
		}
	%>
	<form method="post" action="?iscommit=true">
	
	
		<table><caption><font color='red'><B>所有玩家统计</B></font></caption>
			<tr><th>提交问题数</th><th>删除问题数</th><th>满意评价数</th><th>一般评价数</th><th>不满意评价数</th></tr>
			<%
				NewFeedbackStateManager stmanager = NewFeedbackStateManager.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String recordid = sdf.format(new Date());
				List<FeedbackPlayerState> list = stmanager.getPlayerStateByRecordId(recordid);
				long 提交问题数 = 0;
				long 删除问题数 = 0;
				long 满意评价数 = 0;
				long 一般评价数 = 0;
				long 不满意评价数 = 0;
				long lingqu_all = 0;
				long close_all = 0;
				long guaqi_all = 0;
				long manyi_all = 0;
				long yiban_all = 0;
				long bumanyi_all = 0;
				long talknum_all = 0;
				if(list.size()>0){
					for(FeedbackPlayerState state : list){
						if(recordid.equals(state.getRecordid())){
							提交问题数 = state.getCommitnums();
							删除问题数 = state.getDeletenums();
							满意评价数 = state.getManyinums();
							一般评价数 = state.getYibannums();
							不满意评价数 = state.getBumanyinums();
						}
					}	
				}
			%>
			<tr><td><%=提交问题数 %></td><td><%=删除问题数 %></td><td><%=满意评价数 %></td><td><%=一般评价数 %></td><td><%=不满意评价数 %></td></tr>
		</table>
		<hr color="blue" title="分割线">
		<table><caption><font color='red'><B>客服统计</B></font></caption>
			<tr><th>GM编号</th><th>领取问题数</th><th>主动关闭数</th><th>挂起数</th><th>暂离次数</th><th>满意比例</th><th>一般比例</th><th>不满意比例</th><th>开始工作时间</th><th>注销时间</th><th>交互条数</th></tr>
			<%
				long 暂离次数 = 0;
				String 注销时间 = "0";
				String gmid = NewFeedbackQueueManager.getInstance().getGmWorkNumByDay(recordid);
				String [] gmids = gmid.split(",");
				String starttime = sdf.format(new Date());
				String endtime = sdf.format(System.currentTimeMillis()+24*60*60*1000);
				long startdate = sdf.parse(starttime).getTime();
				long enddate = sdf.parse(endtime).getTime();
				if(gmids!=null && gmids.length>0){
					for(int i=0;i<gmids.length;i++){
						String id = gmids[i];
						if(!id.equals("null")){
							List<FeedbackState> sts = stmanager.getStates();
							for(FeedbackState st:sts){
								if(st.getStateid()!=null && st.getStateid().equals(recordid) && id.equals(st.getGmnum())){
									String starttimes = "";
									lingqu_all = lingqu_all + st.getGetFeedbackTime();
									close_all = close_all + st.getCloseFeedbackTime();
									guaqi_all = guaqi_all + st.getGuaqiTime();
									manyi_all = manyi_all + st.getManyi();
									yiban_all = yiban_all + st.getYiban();
									bumanyi_all = bumanyi_all + st.getBumanyi();
									talknum_all = talknum_all + st.getTalknum();
									if(st.getStartWorkTime()>0){
										starttimes = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(st.getStartWorkTime());
									}
									String zhuxiaotimes  = "";
// 									out.print(st.getZhuxiaoTimestr());
									if(st.getZhuxiaoTimestr()!=null){
										zhuxiaotimes = st.getZhuxiaoTimestr();
									}
// 									if(st.getZhuxiaoTime()>0){
// 										zhuxiaotimes = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(st.getZhuxiaoTime());
// 									}
									
									
			%>
				<tr><td><a onclick="selectgm('<%=id%>')"><%=id%></a></td><td><%=st.getGetFeedbackTime()%></td><td><%=st.getCloseFeedbackTime() %></td><td><%=st.getGuaqiTime() %></td><td><%=暂离次数 %></td><td><%=st.getManyi() %></td><td><%=st.getYiban() %></td><td><%=st.getBumanyi() %></td><td><%=starttimes %></td><td><%=zhuxiaotimes %></td><td><%=st.getTalknum() %></td></tr>
			<%			
								}
							}
						}
					}
				}
			%>
			<tr bgcolor="#f0e68c"><th><font color='red'>汇总</font></th><td><%=lingqu_all %></td><td><%=close_all %></td><td><%=guaqi_all %></td><td></td><td><%=manyi_all %></td><td><%=yiban_all %></td><td><%=bumanyi_all %></td><td></td><td></td><td><%=talknum_all %></td></tr>
		</table>
	</form>
</body>
</html>

