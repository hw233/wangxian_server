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

ArrayList<Integer> filter = new ArrayList<Integer>();
String ss[] = request.getParameterValues("filterChannel");
if(ss == null) ss = new String[]{"0","1"};
for(int i = 0 ;ss != null && i < ss.length ; i++){
	filter.add(Integer.valueOf(ss[i]));
}
String names[] = new String[]{"并发读","并发写"};
%>
<%
		
		StringBuffer sb0 = new StringBuffer();
		StringBuffer sb1 = new StringBuffer();
		
		sb0.append("[");
		sb1.append("[");
		
		float nums0[] = oct.getStatConcurrentNum(0,step);
		float nums1[] = oct.getStatConcurrentNum(1,step);
		float nums2[] = oct.getStatOperateObjectNum(0,step);
		float nums3[] = oct.getStatOperateObjectNum(1,step);
		float nums4[] = oct.getStatOperateDataBytes(0,step);
		float nums5[] = oct.getStatOperateDataBytes(1,step);
		
		long now = System.currentTimeMillis();
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(now);
		int m = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE);
		m = m - OperationTrackerServiceHelper.getTrackDurationInSeconds()/60;
		for(int i = 0 ; i < nums0.length ; i++){
			
			sb0.append("["+(i)+","+nums0[i]+"]");
			if(i < nums0.length - 1 ){
				sb0.append(",");
			}
		}
		
		for(int i = 0 ; i < nums1.length ; i++){
			sb1.append("["+(i)+","+nums1[i]+"]");
			if(i < nums1.length - 1 ){
				sb1.append(",");
			}
		}
		
			
	
		sb0.append("]");
		sb1.append("]");
		
		int hours = OperationTrackerServiceHelper.getTrackDurationInSeconds()/60;
		if(hours > 30) hours = 30;
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
<!--
function drawFlotr1() {
	var f = Flotr.draw(
		$('container1'), [
		<% if(filter.contains(0)){%>
		{
		    data: <%=sb0%>,
		    lines: {show: false, fill: false},
		    label: '并发读',
		},
		<%}%>
		<% if(filter.contains(1)){%>
		{
		    data: <%=sb1%>,
		    lines: {show: false, fill: false},
		    label: '并发写',
		},
		<%}%>
		
		],{
			xaxis:{
				noTicks: <%=hours%>,	// Display 7 ticks.	
				tickFormatter: function(n){var b =<%=m%>; var s =<%=step%>; var t = Math.floor(b+s*n/60); var c=Math.floor(t/60); var d = t - c*60;     if(d<10){return c+':0'+d;}else{return c+':'+d;}}, // =>
				min: 0,
				max: <%=  nums0.length  %>
			},legend:{
				position: 'ne', // => position the legend 'south-east'.
				backgroundColor: '#D2E8FF' // => a light blue background color.
			},
			mouse:{
				track: true,
				color: 'purple',
				sensibility: 1, // => distance to show point get's smaller
				trackDecimals: 2,
				trackFormatter: function(obj){ return 'y = ' + obj.y; }
			}
		}
	);
}
-->
</script>

	</head>

	<body>
		
		<h2 style="font-weight:bold;">
			跟踪：<%= oct.name %>，跟踪时间：<%= OperationTrackerServiceHelper.getTrackDurationInSeconds() %>秒，操作数：<%= oct.getTrackerItemNum() %>
		</h2>
		<form><input type='hidden' name='name' value='<%=oct.name %>'
		>间隔：<input type='text' name='step' value='<%=step %>'>
		 <br/><%
		 	for(int i = 0 ; i < 2 ; i++){
		 		if(filter.contains(i)){
		 			out.print("<input type='checkbox' name='filterChannel' value='"+i+"' checked >"+names[i]+"&nbsp;");
		 		}else{
		 			out.print("<input type='checkbox' name='filterChannel' value='"+i+"' >"+names[i]+"&nbsp;");
		 		}
		 	}
		 %>		
		   
		<input type='submit' value='提交'> </form>
		<br/>

		<center>
			<div id="container1" style="width:100%;height:400px;display:block;"></div>
			<script>
				drawFlotr1();
			</script>
			
		<center>
		<br>
		<br>
		当前并发的情况：
		<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>
		<tr bgcolor='#EEEEBB'><td>时间编号</td><td>时间</td><td>并发读会话数</td><td>并发写会话数</td><td>并发读字节数</td><td>并发写数据字节数</td><td>并发读对象数</td><td>并发写对象数</td></tr>
		<%
		long startTime = now - OperationTrackerServiceHelper.getTrackDurationInSeconds()*1000;
		for(int i = 0 ; i < nums0.length ; i++){
			out.println("<tr bgcolor='#FFFFFF'>");
			out.println("<td>#"+(i+1)+"</td>");
			out.println("<td>"+(DateUtil.formatDate(new Date(startTime+i*step*1000),"yyyy-MM-dd HH:mm:ss"))+"</td>");
			out.println("<td><a href='./operation_tracker_item.jsp?name="+name+"&now="+(now)+"&step="+step+"&index="+i+"&action=0'>"+nums0[i]+"</a></td>");
			out.println("<td><a href='./operation_tracker_item.jsp?name="+name+"&now="+(now)+"&step="+step+"&index="+i+"&action=1'>"+nums1[i]+"</a></td>");
			out.println("<td>"+nums4[i]+"</td>");
			out.println("<td>"+nums5[i]+"</td>");
			out.println("<td>"+nums2[i]+"</td>");
			out.println("<td>"+nums3[i]+"</td>");
		
			out.println("</tr>");
		}
			
		%>
		</table>
	</body>
</html>
