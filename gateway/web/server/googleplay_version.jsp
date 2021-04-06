<%@ page contentType="text/html;charset=utf-8" import="java.util.*,
com.xuanzhi.tools.text.*,
com.xuanzhi.tools.transport.*,java.nio.channels.*,com.fy.gamegateway.mieshi.server.*"%>
<%@page import="com.xuanzhi.tools.text.ParamUtils;"%>
<%
GooglePlayVersionManager mm = GooglePlayVersionManager.getInstance();

	
	String action = "";
	action = request.getParameter("action");
	
	if(action != null && action.equals("delete")){
		int index = Integer.parseInt(request.getParameter("index"));
		
		mm.removeVersion(index);
		mm.saveTo(mm.getConfigFile());
		
		out.println("<h2>删除版本成功</h2>");
	}else if(action != null && action.equals("set_downloadurl")){
		String s = request.getParameter("url");
		if(s.trim().length() > 0){
			mm.setAppstoreDownloadURL(s.trim());
			mm.saveTo(mm.getConfigFile());
		}
		out.println("<h2>设置下载地址成功</h2>");
	}else if(action != null && action.equals("add_version")){
		String s = request.getParameter("version");
		if(s.trim().length() > 0){
			mm.addVersion(s.trim());
			mm.saveTo(mm.getConfigFile());
		}
		out.println("<h2>添加版本成功</h2>");
	}
	
	
%>
<html>
<head>
</head>
<body>
<center>
<h2></h2><br><a href="./googleplay_version.jsp">刷新此页面</a><br>
<br>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center">
	<td>版本</td>
	<td>地址</td>
	<td>操作</td></tr>
<%
GooglePlayVersionManager.Version versions[] = GooglePlayVersionManager.getInstance().getVersions();
	if(versions == null) {
		versions = new GooglePlayVersionManager.Version [0];
	}

	for(int i = 0 ; i < versions.length ; i++){
		GooglePlayVersionManager.Version v = versions[i];
		
		out.println("<tr bgcolor='#FFFFFF' align='center'><td>"+v.versionString+"</td><td>"+mm.getAppstoreDownloadURL()+"</td><td><a href='./googleplay_version.jsp?action=delete&index="+i+"'>删除</a></td></tr>");
	}
%>	
</table>
<br>


<h2>设置GooglePlay飘渺寻仙曲的下载地址</h2>
<form><input type='hidden' name='submitted' value='true'><input type='hidden' name='action' value='set_downloadurl'>
GooglePlay飘渺寻仙曲的下载地址：<input type='text' name='url' value='<%= mm.getAppstoreDownloadURL() %>' size=50><input type='submit' value='提交'>
</form>


<h2>添加飘渺寻仙曲在GooglePlay上新的版本号</h2>
<form><input type='hidden' name='submitted' value='true'><input type='hidden' name='action' value='add_version'>
飘渺寻仙曲在GooglePlay上新的版本号：<input type='text' name='version' value=''><input type='submit' value='提交'>
</form>



</center>
</body>
</html> 
