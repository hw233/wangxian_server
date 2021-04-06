<%@ page contentType="text/html;charset=utf-8"%>
<%@ page
	import="java.util.*,com.xuanzhi.tools.text.*,com.xuanzhi.tools.mem.* "%>
<%

String name = request.getParameter("name");
ReadAndWriteOperationTracker oct = OperationTrackerServiceHelper.getTracker(name);

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
	long start = now-OperationTrackerServiceHelper.getTrackDurationInSeconds()*1000+index*step*1000;
	long end = start + step*1000;
		ArrayList<ReadAndWriteOperationTracker.OperationItem> al = oct.getStatOperationItems(action,start,end);
				
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
			跟踪：<%= oct.name %>，跟踪时间：<%= OperationTrackerServiceHelper.getTrackDurationInSeconds() %>秒，操作数：<%= oct.getTrackerItemNum() %>
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
		
		<tr bgcolor='#EEEEBB'><td>时间</td><td>线程名</td><td>对象类</td><td>操作</td><td>耗时</td><td>大小</td><td>数量</td><td>堆栈</td></tr>
		<%
		
		for(int i = 0 ; i < al.size() ; i++){
			ReadAndWriteOperationTracker.OperationItem oi = al.get(i);
			ReadAndWriteOperationTracker.CallTrace ct = oi.getCt();
			int ctIndex = oct.callTraceList.indexOf(ct);
			if(oi.getEnd() - oi.getStart() >= filter){
				out.println("<tr bgcolor='#FFFFFF'>");
				out.println("<td>"+ DateUtil.formatDate(new Date(oi.getStart()),"yyyy-MM-dd HH:mm:ss")+"</td>");
				out.println("<td>"+oi.getThreadName()+"</td>");
				out.println("<td>"+oi.getClazz()+"</td>");
				out.println("<td>"+ (oi.getAction() == 0 ?"读":"写")+"</td>");
				out.println("<td>"+ (oi.getEnd() - oi.getStart())+"ms</td>");
				out.println("<td>"+ oi.getBytes() + "</td>");
				out.println("<td>"+ oi.getNum() + "</td>");
				out.println("<td><a href='./operation_tracker_item.jsp?name="+name+"&now="+(now)+"&step="+step+"&index="+index+"&action="+action+"&ctIndex="+ctIndex+"'>#"+ctIndex+"</a></td>");
				out.println("</tr>");
			}
			
			
		}
			
		%>
		</table>
<%
	int ctIndex = -1;
	if(request.getParameter("ctIndex") != null){
		ctIndex = Integer.valueOf(request.getParameter("ctIndex"));
	}
	if(ctIndex >= 0 && ctIndex < oct.callTraceList.size()){
		ReadAndWriteOperationTracker.CallTrace ct = oct.callTraceList.get(ctIndex);
		
		ArrayList<ReadAndWriteOperationTracker.OperationItem> al2 = oct.getStatOperationItems(action,now-OperationTrackerServiceHelper.getTrackDurationInSeconds()*1000,now);
		
		
%><h2>#<%=ctIndex %>堆栈全部执行情况：</h2>
<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>
		
		<tr bgcolor='#EEEEBB'><td>时间</td><td>线程名</td><td>对象类</td><td>操作</td><td>耗时</td><td>大小</td><td>数量</td><td>堆栈</td></tr>
		<%
		
		for(int i = 0 ; i < al2.size() ; i++){
			ReadAndWriteOperationTracker.OperationItem oi = al2.get(i);
			if(ct == oi.getCt()){
					out.println("<tr bgcolor='#FFFFFF'>");
					out.println("<td>"+ DateUtil.formatDate(new Date(oi.getStart()),"yyyy-MM-dd HH:mm:ss")+"</td>");
					out.println("<td>"+oi.getThreadName()+"</td>");
					out.println("<td>"+oi.getClazz()+"</td>");
					out.println("<td>"+ (oi.getAction() == 0 ?"读":"写")+"</td>");
					out.println("<td>"+ (oi.getEnd() - oi.getStart())+"ms</td>");
					out.println("<td>"+ oi.getBytes() + "</td>");
					out.println("<td>"+ oi.getNum() + "</td>");
					out.println("<td>--</td>");
					out.println("</tr>");
			}
			
		}
			
		%>
		</table>
		
	<table id='test2' align='left' width='90%' cellpadding='0' bgcolor='#FFFFFF' cellspacing='0' border='0'>
		<%
			for(int i = 0 ; i < ct.elements.length ; i++){
				out.println("<tr><td>"+ct.elements[i].toString()+"</td></tr>");
			}
		%>
		</table>
<% 	
		
	}
%>		
	</body>
</html>
