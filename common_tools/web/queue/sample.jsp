<%@ page language="java" pageEncoding="GBK" contentType="text/html;charset=GBK" 
import="java.util.*,com.xuanzhi.tools.queue.*,
com.xuanzhi.tools.text.*"%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<style>
td{ font-size:12px; color:#FF33FF; font-family:Arial; font-weight:bold;}
th{ background-color:#EEEEEE; border:1px solid #0059CC; align:center;}
.l{ text-align:right; padding-right:0px; vertical-align:top;}
.r{ text-align:left; padding-left:0px; vertical-align:bottom;}
.m{ text-align:center; height:20px; vertical-align:middle; color:#0059CC; font-weight:bold; font-size:16px;}
</style>
<script>
	function clear_queue(name){
		if(confirm("此操作将清除 "+name+" 优先级 "+priority+" 队列中所有的消息，并且不可恢复，你确认此操作吗？")){
			document.location="./sample.jsp?action=clear&name="+name;
		}
	}
	function clear_queue_all(name){
		if(confirm("此操作将清除 "+name+" 所有优先级队列中所有的消息，并且不可恢复，你确认此操作吗？")){
			document.location="./sample.jsp?action=clear_all&name="+name;
		}
	}

	//setTimeout("document.location='./queues_info.jsp'",30000);
</script>
</head>
<body>

<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center"><td>队列名称</td><td width="40%">图形示例</td><td>PUSH</td><td>POP</td><td>消息个数</td><td>占用空间(K)</td><td>使用比例</td><td>操作</td><td>查看</td></tr>
<%
String rightColor = "#0000FF";
String leftColor = "#FF0000";

		//需要您来设置此变量
		AdvancedFilePersistentQueue queue = null;

		String queueName = "需要您来设置";
		
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
	</td><td><%=pushNum%></td><td><%=popNum%></td><td><%=num%></td><td><%=StringUtil.addcommas(size)%></td><td><%=(size*10000/capacity)/100f%>%</td><td><a href="#" onclick="clear_queue('<%=queueName%>')">清  空</a></td>
<td><a href="./list_queue.jsp?product=<%=queueName%>&num=<%=Math.min(1000,num)%>">参看队列</a></td></tr>
</table>
</body>
</html>

	
