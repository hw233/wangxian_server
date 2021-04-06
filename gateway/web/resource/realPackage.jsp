<%@ page contentType="text/html;charset=utf-8" import="java.util.*"%><%

	PackageManager pm = PackageManager.getInstance();
	pm.refreshRealPackage(true);
%>
<%@page import="com.fy.gamegateway.mieshi.resource.manager.ResourceManager"%>
<%@page import="com.fy.gamegateway.mieshi.resource.manager.PackageManager"%>
<%@page import="com.fy.gamegateway.mieshi.resource.manager.PackageManager.Version"%><html>
<head>
<link rel="stylesheet" href="../css/common.css"/>
<link rel="stylesheet" href="../css/table.css"/>

</head>
<body>
<center>
<h2>正式各个包的情况</h2><br><a href="./realPackage.jsp">刷新此页面</a><br>
<br>
<table>
<tr><td>版本</td><td>渠道</td><td>平台</td><td>gpu</td><td>包下载路径</td><td>fullFlag</td><td>packageSize</td><td>lastModifiedTime</td></tr>
<%
Calendar calendar = Calendar.getInstance();
PackageManager.Version[] vs = pm.getRealVersions();
for(PackageManager.Version v : vs){
	if(v != null){
		%>
		<tr><td colspan="20"><%=v.versionString +" 是否强制更新:"+v.forceUpdate %></td></tr>
		<%
		for(PackageManager.Package p : v.packageList){
			if(p != null){
				%>
				<tr><td><%=p.version.versionString %></td><td><%=p.channel %></td><td><%=p.platform %></td><td><%=p.gpuFlag %></td><td><a href=<%=p.httpDownloadURL%>><%=p.httpDownloadURL %></a></td><td><%=p.fullFlag %></td><td><%=p.packageSize %></td><td><%=p.lastModifiedTime %></td></tr>
				<%
			}
		}

	}
}
%>
</table>

</center>
</body>
</html> 
