<%@ page contentType="text/html;charset=utf-8"%>
<%@ page
	import="java.util.*,com.xuanzhi.tools.text.*,com.xuanzhi.tools.mem.* "%>
<%

Class clazz = Class.forName(request.getParameter("cl"));
ObjectCreationTracker oct = ObjectTrackerServiceHelper.getObjectCreationTracker(clazz);

String startday = request.getParameter("startday");
String endday = request.getParameter("endday");
if(startday == null) startday = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
if(endday == null) endday = DateUtil.formatDate(new Date(),"yyyy-MM-dd");

ArrayList<Integer> filter = new ArrayList<Integer>();
String ss[] = request.getParameterValues("filterChannel");
if(ss == null) ss = new String[]{"0","1","2"};
for(int i = 0 ;ss != null && i < ss.length ; i++){
	filter.add(Integer.valueOf(ss[i]));
}
String names[] = new String[]{"每分钟新增","内存中的存量","每分钟销毁"};
%>
<%
		
		Date s = DateUtil.parseDate(startday,"yyyy-MM-dd");
		Date e = DateUtil.parseDate(endday,"yyyy-MM-dd");
		
		StringBuffer sb0 = new StringBuffer();
		StringBuffer sb1 = new StringBuffer();
		StringBuffer sb2 = new StringBuffer();
		
		sb0.append("[");
		sb1.append("[");
		sb2.append("[");
		int index = 0;
		
		
		while(s.getTime() < e.getTime() + 3600000){
			String day = DateUtil.formatDate(s,"yyyy-MM-dd");
			
			long nums0[] = oct.getStatNumByeveryMinute(day,0);
			long nums1[] = oct.getStatNumByeveryMinute(day,1);
			long nums2[] = oct.getStatNumByeveryMinute(day,2);
			
			
			
			for(int i = 0 ; i < nums0.length ; i++){
				sb0.append("["+(i + index*24*60)+","+nums0[i]+"]");
				if(i < nums0.length - 1 || !day.equals(endday)){
					sb0.append(",");
				}
			}
			
			String today =  DateUtil.formatDate(new Date(),"yyyy-MM-dd");
			int max = nums1.length;
			if(day.equals(today)){
				max = (int)(System.currentTimeMillis() - DateUtil.parseDate(today+" 00:00:00","yyyy-MM-dd HH:mm:ss").getTime())/60000;
				if(max > 0) max = max -1;
			}
			
			for(int i = 0 ; i < max; i++){
				sb1.append("["+(i + index*24*60)+","+nums1[i]+"]");
				if(i < nums1.length - 1 || !day.equals(endday)){
					sb1.append(",");
				}
			}

			for(int i = 0 ; i < nums2.length ; i++){
				sb2.append("["+(i + index*24*60)+","+nums2[i]+"]");
				if(i < nums2.length - 1 || !day.equals(endday)){
					sb2.append(",");
				}
			}

			index++;
			s.setTime(s.getTime() + 24L * 3600 * 1000);
		}
		sb0.append("]");
		sb1.append("]");
		sb2.append("]");
		
		int hours = 24;
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
		    label: '新增',
		},
		<%}%>
		<% if(filter.contains(1)){%>
		{
		    data: <%=sb1%>,
		    lines: {show: false, fill: false},
		    label: '存量',
		},
		<%}%>
		<% if(filter.contains(2)){%>
		{
		    data: <%=sb2%>,
		    lines: {show: false, fill: false},
		    label: '销毁',
		}
		<%}%>
		],{
			xaxis:{
				noTicks: <%=hours%>,	// Display 7 ticks.	
				tickFormatter: function(n){    var a=Math.floor(n/(60*24)); var b=n-a*60*24; var c=Math.floor(b/60); var d = b - c*60;     if(d<10){return c+':0'+d;}else{return c+':'+d;}}, // =>
				min: 0,
				max: <%= index*24*60 %>
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
			<%=startday %> ~ <%= endday %> 类<%=clazz.getName() %>跟踪<br>存量：<%= StringUtil.addcommas(oct.currentNumInMem) %>，峰值：<%= StringUtil.addcommas(oct.peakNumInMem) %>，创建总量：<%= StringUtil.addcommas(oct.totalCreatedNum) %>
		</h2>
		<form><input type='hidden' name='cl' value='<%=clazz.getName() %>'
		>输入开始日期：<input type='text' name='startday' value='<%=startday %>'>
		结束日期<input type='text' name='endday' value='<%=endday %>'>(格式：2011-01-01) 
		 <br/><%
		 	for(int i = 0 ; i < 3 ; i++){
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
		当前内存中对象创建的调用堆栈分布情况：
		<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>
		<tr bgcolor='#EEEEBB'><td>调用堆栈编号</td><td>内存中的数量</td><td>数量峰值</td><td>已创建数量</td></tr>
		<%
			
			for(int i = 0 ; i < oct.callTraceList.size() ; i++){
				ObjectCreationTracker.CallTrace ct = oct.callTraceList.get(i);
				out.println("<tr bgcolor='#FFFFFF'><td><a href='./object_tracker_call.jsp?cl="+clazz.getName()+"&calltrace="+i+"'>#"+i+"</a></td><td>"+StringUtil.addcommas(ct.currentNumInMem)
						+"</td><td>"+StringUtil.addcommas(ct.peakNumInMem)+"</td><td>"+StringUtil.addcommas(ct.totalCreatedNum)
						+"</td></tr>");
			}
		%>
		</table>
	</body>
</html>
