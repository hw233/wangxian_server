<%@ page contentType="text/html;charset=utf-8"%>
<%@ page
	import="java.util.*,com.xuanzhi.tools.text.*,com.xuanzhi.tools.mem.* "%>
<%
	if("true".equals(request.getParameter("config"))){
		 ObjectTrackerService.config(ObjectTrackerServiceHelper.getPath(),ObjectTrackerServiceHelper.getTimeoutDays(),
				 request.getParameter("logback"),request.getParameter("log4j"));
		 ObjectTrackerService.setEnableObjectTrackerService("true".equalsIgnoreCase(request.getParameter("enable_service")));
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
			JVM对象创建，销毁跟踪服务，上一次启动时间：<%= DateUtil.formatDate(new Date(ObjectTrackerService.serviceStartTime),"yyyy-MM-dd HH:mm:ss") %>
		</h2>
		已经跟踪的类型
		<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>
		<tr bgcolor='#EEEEBB'><td>类名</td><td>内存中的数量</td><td>数量峰值</td><td>已创建数量</td><td>调用堆栈追踪数量</td><td>调用堆栈个数</td></tr>
		<%
			Class clazz[] = ObjectTrackerServiceHelper.getTrackedClass();
			for(int i = 0 ; i < clazz.length ; i++){
				ObjectCreationTracker oct = ObjectTrackerServiceHelper.getObjectCreationTracker(clazz[i]);
				out.println("<tr bgcolor='#FFFFFF'><td><a href='./object_tracker.jsp?cl="+clazz[i].getName()+"'>"+clazz[i].getName()+"</a></td><td>"+StringUtil.addcommas(oct.currentNumInMem)
						+"</td><td>"+StringUtil.addcommas(oct.peakNumInMem)+"</td><td>"+StringUtil.addcommas(oct.totalCreatedNum)
						+"</td><td>"+StringUtil.addcommas(oct.map.size())+"</td><td>"+StringUtil.addcommas(oct.callTraceList.size())+"</td></tr>");
			}
		%>
		</table>
		<br/>
		<i>基本配置：cache=<%= ObjectTrackerServiceHelper.getPath() %>,timeoutDays=<%=ObjectTrackerServiceHelper.getTimeoutDays() %></i>
		<form><input type='hidden' name='config' value='true'>服务是否开启：<input type='text' name='enable_service' value='<%=ObjectTrackerService.isEnableObjectTrackerService()  %>'><br>Logback日志名称：<input type='text' name='logback' value='<%=ObjectTrackerServiceHelper.getLogbackName()==null?"":ObjectTrackerServiceHelper.getLogbackName() %>'><br>
			Log4j日志名称：<input type='text' name='log4j' value='<%= ObjectTrackerServiceHelper.getLog4jName() == null?"":ObjectTrackerServiceHelper.getLog4jName() %>'> 
			<input type='submit' value='修改'>
		</form>
		
	</body>
</html>
