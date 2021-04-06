<%@ page contentType="text/html;charset=utf-8" import="java.util.*,
com.xuanzhi.tools.text.*,
com.xuanzhi.tools.transport.*,java.nio.channels.*,com.fy.gamegateway.mieshi.server.*"%>
<%@page import="com.xuanzhi.tools.text.ParamUtils;"%><%

		

AppstoreVersionManager mm = AppstoreVersionManager.getInstance();

	
	String action = "";
	action = request.getParameter("action");
	
	if(action != null && action.equals("delete")){
		int index = Integer.parseInt(request.getParameter("index"));
		
		mm.removeVersion(index);
		mm.saveTo(mm.getConfigFile());
		
		out.println("<h2>删除版本成功</h2>");
	}else if(action != null && action.equals("add_app")){
		String url = request.getParameter("url");
		String version = request.getParameter("version");
		String channel = request.getParameter("channel");
		if(url.trim().length() > 0){
			mm.addVersion(version, url, channel);
			mm.saveTo(mm.getConfigFile());
		}
		out.println("<h2>设置下载地址成功</h2>");
	}
	
	
%><html>
<head>
</head>
<body>
<center>
<h2></h2><br><a href="./appstore_version.jsp">刷新此页面</a><br>
<br>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center">
	<td>版本</td>
	<td>地址</td>
	<td>操作</td>
	<td>渠道</td>
</tr>
<%
	AppstoreVersionManager.Version versions[] = mm.getVersions();
	if(versions == null) versions = new AppstoreVersionManager.Version [0];

	for(int i = 0 ; i < versions.length ; i++){
		AppstoreVersionManager.Version v = versions[i];
		
		out.println("<tr bgcolor='#FFFFFF' align='center'><td>"+v.versionString+"</td><td>"+v.downUrl+"</td><td><a href='./appstore_version.jsp?action=delete&index="+i+"'>删除</a></td><td>"+v.appChannel+"</td></tr>");
	}
%>	
</table><br>


<h2>设置AppStore飘渺寻仙曲的下载地址</h2>
<form>
	<input type='hidden' name='submitted' value='true'>
	<input type='hidden' name='action' value='add_app'>
版本<input type='text' name='version' size=20>
AppStore飘渺寻仙曲的下载地址：<input type='text' name='url' size=50>
渠道<input type='text' name='channel' size=20>
	<input type='submit' value='提交'>
</form>


</center>
</body>
</html> 
