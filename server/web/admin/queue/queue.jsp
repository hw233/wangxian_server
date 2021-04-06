<%@ page language="java" pageEncoding="utf-8" contentType="text/html;charset=utf-8" 
import="java.util.*,com.xuanzhi.tools.queue.*,
com.xuanzhi.tools.text.*,
com.xuanzhi.stat.client.*"%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@include file="../IPManager.jsp" %><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="/css/style.css"/>
<style>
</style>
<script>
</script>
</head>
<body>
<br><br>
<table border="0" cellpadding="0" cellspacing="1" width="90%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center"><th>队列名称</th><th width="40%">图形示例</th><th>PUSH</th>
<th>POP</th><th>消息个数</th><th>占用空间(K)</th><th>使用比例</th></tr>
<%
String rightColor = "#0000FF";
String leftColor = "#FF0000";

		//需要您来设置此变量
		AdvancedFilePersistentQueue queue = StatClientService.getInstance().getQueue(); 

		String queueName = "统计消息队列";
		
		int size = queue.size();
		int capacity = queue.capacity();
		int num = queue.elementNum();
		int leftWidth = size * 100 / capacity;
		int rightWidth = 100 - leftWidth;
		long pushNum = queue.pushNum();
		long popNum = queue.popNum();
%>
<tr bgcolor="#FFFFFF" align="center"><td><%=queueName%></td>
	<td width="40%">
		<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="<%=rightColor%>">
			<tr height="13" bgcolor="<%=rightColor%>"><td bgcolor="<%=leftColor%>" width="<%=leftWidth%>%"></td><td bgcolor="<%=rightColor%>" width="<%=rightWidth%>%"></td></tr>
		</table>
	</td>
	<td><%=pushNum%></td>
	<td><%=popNum%></td>
	<td><%=num%></td>
	<td><%=StringUtil.addcommas(size)%></td>
	<td><%=(size*10000/capacity)/100f%>%</td>
</tr>
</table>
</body>
</html>

	
