<%@ page contentType="text/html;charset=utf-8"%>
<%@ page
	import="java.util.*,com.xuanzhi.tools.text.*,com.xuanzhi.tools.mem.* "%>
<%

String name = request.getParameter("name");
HeartBeatTracker2 oct = HeartbeatTrackerServiceHelper2.getTracker(name);

String stepStr = request.getParameter("step");
int step = 5;
if(stepStr != null)
	step = Integer.valueOf(stepStr);

long now = Long.valueOf(request.getParameter("now"));

int index = Integer.parseInt(request.getParameter("index"));

int action = Integer.parseInt(request.getParameter("action"));

int filter = 0;
if(request.getParameter("filter") != null){
	filter =Integer.parseInt(request.getParameter("filter"));
}
%>
<%
	long start = now-HeartbeatTrackerServiceHelper2.getTrackDurationInSeconds()*1000+index*step*1000;
	long end = start + step*1000;
		ArrayList<HeartBeatTracker2.HeartBeatItem> al = oct.getStatOperationItems(start,end);
				
%>
		
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title></title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<link rel="stylesheet" href="../css/style.css" />
		<link rel="stylesheet" href="../css/atalk.css" />
		<script language="javascript" type="text/javascript" src="./flotr/lib/prototype-1.6.0.2.js"></script>
		<script language="javascript" type="text/javascript" src="./flotr/flotr-0.2.0-alpha.js"></script>

<script language="JavaScript">

</script>

	</head>

	<body>
		
		<h2 style="font-weight:bold;">
			跟踪：<%= oct.name %>，跟踪时间：<%= HeartbeatTrackerServiceHelper2.getTrackDurationInSeconds() %>秒，操作数：<%= oct.getTrackerItemNum() %>
		</h2>
		
		<br/>

		<br>
		<br>
		时间区间：<%= DateUtil.formatDate(new Date(start),"yyyy-MM-dd HH:mm:ss") %> ~ <%= DateUtil.formatDate(new Date(end),"yyyy-MM-dd HH:mm:ss") %> 并发的情况：
		
		<form><input type='hidden' name='name' value='<%=name %>'>
		<input type='hidden' name='now' value='<%= now %>'>
		<input type='hidden' name='step' value='<%= step %>'>
		<input type='hidden' name='index' value='<%= index %>'>
		<input type='hidden' name='action' value='<%= action %>'>
		耗时过滤条件（大于显示，单位ms）：<input type='text' name='filter' value='<%= filter %>'> <input type='submit' value='过滤'>
		</form>
		
		<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>
		
		<tr bgcolor='#EEEEBB'><td>时间</td><td>线程名</td><td>对象类</td><td>耗时</td><td>堆栈</td></tr>
		<%
		
		for(int i = 0 ; i < al.size() ; i++){
			HeartBeatTracker2.HeartBeatItem oi = al.get(i);
			if(oi.getEnd() - oi.getStart() + oi.getWaittingTime() >= filter * 1000000L ){
				out.println("<tr bgcolor='#FFFFFF'>");
				out.println("<td>"+ DateUtil.formatDate(new Date(oi.getStart()/1000000L),"yyyy-MM-dd HH:mm:ss")+"</td>");
				out.println("<td>"+oi.getThreadName()+"</td>");
				out.println("<td>"+oi.getClazz()+"</td>");
				out.println("<td>"+ (oi.getWaittingTime()/1000000L)+"ms</td>");
				out.println("<td>"+ ((oi.getEnd() - oi.getStart())/1000000L)+"ms</td>");
				out.println("<td>--</td>");
				out.println("</tr>");
			}
			
			
		}
			
		%>
		</table>

	</body>
</html>
