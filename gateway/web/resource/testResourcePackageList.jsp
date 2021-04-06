<%@ page contentType="text/html;charset=utf-8" import="java.util.*,java.io.*"%><%

ResourceManager rm = ResourceManager.getInstance();

if("2".equals(request.getParameter("action"))){
	rm.createTestResourcePackageForCurrentVersion();
	out.println("发送重新生成测试资源成功");
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
<h2>各个资源zip的情况</h2>
<br><a href="./testResourcePackageList">刷新此页面</a><br>
<br><br>
<h2>测试资源包</h2>
<form name="f2">
<input type="hidden" name="action" value="2">
<input type="submit" value="重新生成测试资源zip包">
</form>
<br>

<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center">
<td>文件</td><td>下载地址</td><td>大小</td>
</tr>
<%
String testresourceRootPath = rm.getTestResourceRootPath();
File dir = new File(testresourceRootPath);
File zipFiles[] = dir.listFiles(new FileFilter(){
	public boolean accept(File f){
		if(f.getName().endsWith(".zip")){
			return true;
		}
		return false;
	}
});

Arrays.sort(zipFiles,new java.util.Comparator<File>(){
	public int compare(File f1,File f2){
		return - f1.getName().compareTo(f2.getName());
	}
});

for(int i = 0 ; i < zipFiles.length ; i++){
	File f = zipFiles[i];
	out.println("<tr bgcolor='#00FFFF' align='center'>");
	out.println("<td>"+f.getName()+"</td><td>"+rm.getTestResourceRootHttpPath()+f.getName()+"</td><td>"+f.length()+"</td>");
	out.println("</tr>");
}
%>
</table>

<h2>正式资源包</h2>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center">
<td>文件</td><td>下载地址</td><td>大小</td>
</tr>
<%
String realresourceRootPath = rm.getRealResourceRootPath();
dir = new File(realresourceRootPath);
zipFiles = dir.listFiles(new FileFilter(){
	public boolean accept(File f){
		if(f.getName().endsWith(".zip")){
			return true;
		}
		return false;
	}
});

Arrays.sort(zipFiles,new java.util.Comparator<File>(){
	public int compare(File f1,File f2){
		return - f1.getName().compareTo(f2.getName());
	}
});

for(int i = 0 ; i < zipFiles.length ; i++){
	File f = zipFiles[i];
	out.println("<tr bgcolor='#00FFFF' align='center'>");
	out.println("<td>"+f.getName()+"</td><td>"+rm.getRealResourceRootHttpPath()+f.getName()+"</td><td>"+f.length()+"</td>");
	out.println("</tr>");
}
%>
</table>
</center>
</body>
</html> 
