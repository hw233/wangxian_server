<%@page import="com.fy.gamegateway.mieshi.server.MieshiGatewaySubSystem"%>
<%@page import="com.fy.gamegateway.mieshi.server.NeedToHttpVersionManager.Version"%>
<%@page import="java.util.concurrent.ConcurrentHashMap"%>
<%@page import="com.fy.gamegateway.mieshi.server.NeedToHttpVersionManager"%>
<%@ page contentType="text/html;charset=utf-8" import="java.util.*"%>
<%@page import="com.xuanzhi.tools.text.ParamUtils;"%>
<html>
<head>
</head>
<body>
	<%
		String action = request.getParameter("action");
		if (action != null) {
			if (action.equals("addV")) {
				String v = request.getParameter("version");
				String c = request.getParameter("channel");
				String u = request.getParameter("url");
				String g = request.getParameter("gpu");
				Version ver = new Version(v, c, u, g);
				NeedToHttpVersionManager.getInstance().getTestVersions().put(c+g, ver);
			}else if (action.equals("fabu")) {
				String key = request.getParameter("channel");
				Version v = NeedToHttpVersionManager.getInstance().getTestVersions().get(key);
				if (v != null) {
					NeedToHttpVersionManager.getInstance().getVersions().put(key, v);
				}
			}else if (action.equals("testdelete")) {
				String c = request.getParameter("channel");
				NeedToHttpVersionManager.getInstance().getTestVersions().remove(c);
			}else if (action.equals("delete")) {
				String c = request.getParameter("channel");
				NeedToHttpVersionManager.getInstance().getVersions().remove(c);
			}else if (action.equals("setChannel")) {
				String c = request.getParameter("channels");
				c = c.substring(1, c.length()-1);
				NeedToHttpVersionManager.needChannel = c.split(", ");
			}else if (action.equals("win8Open")) {
				MieshiGatewaySubSystem.isWin8Update = !MieshiGatewaySubSystem.isWin8Update;
			}
			NeedToHttpVersionManager.getInstance().saveTo(NeedToHttpVersionManager.getInstance().getConfigFile());
		}
	%>
<center>
<h2></h2><br><a href="./needToHttp_version.jsp">刷新此页面</a><br>
<br>
<h2>测试链接地址</h2>
<br>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center">
	<td>渠道</td>
	<td>版本</td>
	<td>GPU</td>
	<td>地址</td>
	<td>发布成正式</td>
	<td>删除</td>
</tr>
<%
	ConcurrentHashMap<String, Version> testversions = NeedToHttpVersionManager.getInstance().getTestVersions();
	if(testversions == null) {
		testversions = new ConcurrentHashMap<String, Version>();
	}
	
	for(String key : testversions.keySet()){
		Version v = testversions.get(key);
		
		out.println("<tr bgcolor='#FFFFFF' align='center'><td>"+v.versionChannel+"</td><td>"+v.versionString+"</td><td>"+v.versionGpu+"</td><td>"+v.versionUrl+"</td><td><a href='./needToHttp_version.jsp?action=fabu&channel="+key+"'>发布</a></td><td><a href='./needToHttp_version.jsp?action=testdelete&channel="+key+"'>删除</a></td></tr>");
	}
%>	
</table>
<br>

<h2>正式链接地址</h2>
<br>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center">
	<td>渠道</td>
	<td>版本</td>
	<td>GPU</td>
	<td>地址</td>
	<td>删除</td>
</tr>
<%
	ConcurrentHashMap<String, Version> versions = NeedToHttpVersionManager.getInstance().getVersions();
	if(versions == null) {
		versions = new ConcurrentHashMap<String, Version>();
	}
	
	for(String key : versions.keySet()){
		Version v = versions.get(key);
		
		out.println("<tr bgcolor='#FFFFFF' align='center'><td>"+v.versionChannel+"</td><td>"+v.versionString+"</td><td>"+v.versionGpu+"</td><td>"+v.versionUrl+"</td><td><a href='./needToHttp_version.jsp?action=delete&channel="+key+"'>删除</a></td></tr>");
	}
%>	
</table>
<br>


<h2>新加，版本信息</h2>
<form>
	<input type='hidden' name='submitted' value='true'>
	<input type='hidden' name='action' value='addV'>
	版本:<input type="text" name='version' value='' size=30>
	渠道:<input type="text" name='channel' value='' size=20>
	GPU:<input type="text" name='gpu' value='' size=20>
	<br>
	URL<input type="text" name='url' value='' size=50>
	<input type='submit' value='提交'>
</form>
<br>
<h2>参与此版本校验的渠道</h2>
<form>
	<input type='hidden' name='submitted' value='true'>
	渠道:<input type="text" name='channels' value='<%=Arrays.toString(NeedToHttpVersionManager.needChannel) %>' size=70>
	<input type='hidden' name='action' value='setChannel'>
	<input type='submit' value='提交'>
</form>
<br>
<form>
	目前WIN特殊强更是否开启: <%=MieshiGatewaySubSystem.isWin8Update ? "以开启":"测试中" %>
	<input type='hidden' name='action' value='win8Open'>
	<input type='submit' value='提交'>
</form>
</center>
</body>
</html> 
