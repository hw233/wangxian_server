<%@ page contentType="text/html;charset=utf-8" 
import="java.io.*,java.util.*,com.xuanzhi.tools.text.*,
com.xuanzhi.tools.dbpool.*"%><%
	ArrayList<ConnectionPool> al = ConnectionPool.M_POOLLIST;

%><%@include file="../IPManager.jsp" %><html>
<head>
</head>
<body>
<br>
<br/>
<%
	if(String.valueOf("true").equals(request.getParameter("detail"))){
		int id = Integer.parseInt(request.getParameter("id"));
		ConnectionPool pool = al.get(id);
		out.println("<pre>"+pool.dumpInfo()+"</pre>");
	}else{
	
%>
<h3>链接池的汇总情况</h3>
<table border='0' width='100%' cellpadding='0' cellspacing='1' bgcolor='#000000' align='center'>
<tr align='center' bgcolor='#00FFFF'>
<td>名称</td>
<td>创建时间</td>
<td>空闲数</td>
<td>链接数</td>
<td>峰值</td>
<td>最大值</td>
<td>语句缓存数量</td>
<td>请求连接次数</td>
<td>等待连接次数</td>
<td>使用超时次数</td>
<td>操作</td></tr>
<%
	for(int i = 0 ; i < al.size() ; i++){
		ConnectionPool pool = al.get(i);
		int c = pool.getNumOfCachedStatement();
		String color = "#FFFFFF";
		if(c > 1000) color = "#FF0000";
		else if(c > 500) color = "#888888";
		else if(c > 400) color = "#999999";
		else if(c > 300) color = "#aaaaaa";
		else if(c > 200) color = "#bbbbbb";
		else if(c > 100) color = "#cccccc";
		else if(c > 50) color = "#dddddd";
		if(c > 25) color = "#eeeeee";
		
		out.println("<tr align='center' bgcolor='"+color+"'>");
		out.println("<td>"+pool.getAlias()+"</td>");
		out.println("<td>"+DateUtil.formatDate(pool.getCreateTime(),"yyyy-MM-dd HH:mm:ss")+"</td>");
		out.println("<td>"+pool.getIdleSize()+"</td>");
		out.println("<td>"+pool.size()+"</td>");
		out.println("<td>"+pool.getPeakSize()+"</td>");		
		out.println("<td>"+pool.getMaxConn()+"</td>");
		out.println("<td>"+pool.getNumOfCachedStatement()+"</td>");
		out.println("<td>"+pool.getNumRequests()+"</td>");
		out.println("<td>"+pool.getNumWaits()+"</td>");
		out.println("<td>"+pool.getNumCheckoutTimeouts()+"</td>");
		
		out.println("<td><a href='./poolinfo.jsp?id="+i+"&detail=true'>详细情况</a></td>");
		out.println("</tr>");
	}
%></table>
<%
}
%>
</body>
</html> 
