<%@ page contentType="text/html;charset=utf-8" import="java.util.*"%><%

ResourceManager rm = ResourceManager.getInstance();
Hashtable<String,byte[]>  zipMap = rm.getZipMap();

if("clear".equals(request.getParameter("action"))){
	zipMap.clear();
}

String keys[] = zipMap.keySet().toArray(new String[0]);
long t = 0;
for(int i = 0 ; i < keys.length ; i++){
	byte[] d = zipMap.get(keys[i]);
	t += d.length;
}

%>
<%@page import="com.fy.gamegateway.mieshi.resource.manager.ResourceManager"%>
<%@page import="com.fy.gamegateway.mieshi.resource.manager.PackageManager"%>
<%@page import="com.fy.gamegateway.mieshi.resource.manager.PackageManager.Version"%>
<%@page import="com.fy.gamegateway.mieshi.resource.manager.ResourceData"%>
<%@page import="com.fy.gamegateway.mieshi.server.* "%><html>
<head>
<link rel="stylesheet" href="../css/common.css"/>
<link rel="stylesheet" href="../css/table.css"/>
</head>
<body>
<center>
<h2>内存中缓存资源的情况，一共有<%=keys.length %>个文件,大小为<%= t/1024/1024 %>M</h2>
<br><a href="./zip_resource_list.jsp">刷新此页面</a><br>
<br><a href="./zip_resource_list.jsp?action=clear">清空缓存数据</a><br>
<br>

<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center">
	<td>编号</td>
	<td>文件</td>
	<td>大小</td>
	<td>压缩后</td>
	<td>压缩比</td>
	</tr>
<%
for(int i = 0 ; i < keys.length ; i++){
	byte[] d = zipMap.get(keys[i]);
	out.println("<tr bgcolor='#FFFFFF' align='center'>");
	out.println("<td>"+(i+1)+"</td>");
	out.println("<td>"+keys[i]+"</td>");
	java.io.File file = new java.io.File(keys[i]);
	out.println("<td>"+file.length() +"</td>");
	out.println("<td>"+ d.length +"</td>");
	out.println("<td>"+ (100.0f*d.length/file.length()) +"%</td>");
	out.println("</tr>");
}
%>	
</table>	
</center>
</body>
</html> 
