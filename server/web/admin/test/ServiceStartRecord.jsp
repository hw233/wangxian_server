<%@page import="java.util.List"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.NumberFormat"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.fy.engineserver.util.ServiceStartTime"%>
<%@page import="java.util.Map"%>
<%@page import="com.fy.engineserver.util.ServiceStartRecord"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%

ServiceStartRecord.print();
Map<Class<?>, ServiceStartTime>  map = ServiceStartRecord.getStartTimeMap();
long totalCost = 0L;
NumberFormat formatter = new DecimalFormat("0.0000%");
for (Iterator<Class<?>> itor = map.keySet().iterator();itor.hasNext();) {
	Class<?> clazz = itor.next();
	ServiceStartTime time = map.get(clazz);
	long cost = time.getEndTime() - time.getStartTime();
	if (cost > 0) {
		totalCost+=cost;
	}
}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<h2>总耗时:<%=totalCost %></h2>
<table style="font-size: 12px;text-align: center;float: left;" border="1" width="40%">
	<tr style="color: white;background-color: black;">
		<td>启动顺序</td>
		<td>类</td>
		<td>平均耗时</td>
		<td>最高耗时</td>
		<td>最低耗时</td>
		<td>本次耗时</td>
		<td>耗时占比</td>
	</tr>
<%
	int index = 0; 
	Map<String, Long[]> computedRecoedMessage = ServiceStartRecord.computedRecoedMessage;
	for (Iterator<Class<?>> itor = map.keySet().iterator();itor.hasNext();) {
		Class<?> clazz = itor.next();
		ServiceStartTime time = map.get(clazz);
		long cost = time.getEndTime() - time.getStartTime();
		boolean startOk = cost >= 0;
		Long [] historyTime = computedRecoedMessage.containsKey(clazz.toString()) ? computedRecoedMessage.get(clazz.toString()) : new Long[]{0L,0L,0L};
		%>
		<tr>
			<td><%=++index %></td>
			<td><%=clazz.getSimpleName() %></td>
			<td><%=historyTime[0] %> </td>
			<td><%=historyTime[1] %> </td>
			<td><%=historyTime[2] %> </td>
			<td style="color: <%=cost>historyTime[0] ? "red" :"green"%>"><%=startOk ? cost : "--" %></td>
			<td><%=startOk ? formatter.format((double) cost/totalCost) : "--" %></td>
		</tr>
		<%
	}	
%>
</table>
</body>
</html>