<%@ page contentType="text/html;charset=utf-8"%>
<%@ page
	import="java.util.*,com.xuanzhi.tools.text.*,com.xuanzhi.tools.mem.* "%>
<%
	if("true".equals(request.getParameter("config"))){
		
		HeartbeatTrackerServiceHelper2.config("true".equalsIgnoreCase(request.getParameter("enable_service")),Integer.parseInt(request.getParameter("seconds")));
	}
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
</head>

	<body>
		
		<h2 style="font-weight:bold;">
			场景心跳跟踪服务，上一次启动时间：<%= DateUtil.formatDate(new Date(ObjectTrackerService.serviceStartTime),"yyyy-MM-dd HH:mm:ss") %>
		</h2>
		已经跟踪的类型
		<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>
		<tr bgcolor='#EEEEBB'><td>名称</td><td>跟踪时间</td><td>调用堆栈个数</td><td>追踪数量</td></tr>
		<%
		String names[] = HeartbeatTrackerServiceHelper2.getTrackerNames();
			for(int i = 0 ; i < names.length ; i++){
				HeartBeatTracker2 oct = HeartbeatTrackerServiceHelper2.getTracker(names[i]);
				out.println("<tr bgcolor='#FFFFFF'><td><a href='./heartbeat_tracker2.jsp?name="+names[i]+"'>"+oct.name+"</a></td><td>"+StringUtil.addcommas(oct.trackDurationInSeconds)
						+"</td><td>--</td><td>"+StringUtil.addcommas(oct.getTrackerItemNum())
						+"</td></tr>");
			}
		%>
		</table>
		
		<br/>
		<i>基本配置：是否开启=<%= HeartbeatTrackerServiceHelper2.isEnabled() %>,跟踪时间（秒）=<%=HeartbeatTrackerServiceHelper2.getTrackDurationInSeconds() %></i>
		<form><input type='hidden' name='config' value='true'>服务是否开启：<input type='text' name='enable_service' value='<%=HeartbeatTrackerServiceHelper2.isEnabled()  %>'>
		<br>跟踪时间(秒)：<input type='text' name='seconds' value='<%= HeartbeatTrackerServiceHelper2.getTrackDurationInSeconds() %>'> 
			<input type='submit' value='修改'>
		</form>
		
	</body>
</html>
