<%@ page contentType="text/html;charset=utf-8" import="java.util.*"%><%

ResourceManager rm = ResourceManager.getInstance();
String resourceFileForClient = rm.getResourceFileForClient();
String resourceFileForServer = rm.getResourceFileForServer();
String resourceRootPath = rm.getRealResourceRootPath();
String action = request.getParameter("action");
ArrayList<String > errorMessage = null;
MieshiGatewayUpdateResourceServer ur = MieshiGatewayUpdateResourceServer.getInstance();
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
<h2>正式各个资源的情况</h2>
<br><a href="./realResourceFileList.jsp">刷新此页面</a><br>
<br><a href="<%= rm.getRealResourceRootHttpPath() + "/"+rm.getRandomResourcePath()+"/" + rm.getResourceFileForClient() %>">下载客户端使用的资源文件</a><br>
<br><a href="<%= ur.getSkillHttpPath() %>">下载技能二进制文件</a><br>
<br>
<%
LinkedHashMap<String, ResourceData> resourceDataMap = rm.getAllRealResourceData();

if(errorMessage != null && errorMessage.size() > 0){
	out.println("<h2>资源目录有问题，具体错误如下:</h2>");
	for(int i = 0 ; i < errorMessage.size() ; i++){
		out.println(errorMessage.get(i)+"<br>");
	}
}
%>


<table>
<tr><td>版本<%=rm.getRealResourceVersion() %></td><td>服务器端用资源文件列表所在路径<%=resourceRootPath+"/"+rm.getRandomResourcePath()+"/"+resourceFileForServer %></td><td colspan="3">客户端用资源文件列表所在路径<%=resourceRootPath+"/"+rm.getRandomResourcePath()+"/"+resourceFileForClient %></td></tr>
<tr><td></td><td>key</td><td>path</td><td>version</td><td>fileSize</td><td>fileType</td></tr>
<%
int i = 1;
if(resourceDataMap != null){
	for(String str : resourceDataMap.keySet()){
		ResourceData rd = resourceDataMap.get(str);
		if(rd != null){
			%>
			<tr><td><%=(i++) %></td><td><%=str %></td><td><%=rd.getPath() %></td><td><%=rd.getVersion() %></td><td><%=rd.fileSize %></td><td><%=rd.getFileType() %></td></tr>
			<%
		}
	}
}

%>
</table>
</center>
</body>
</html> 
