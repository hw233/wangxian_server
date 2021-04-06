<%@ page contentType="text/html;charset=utf-8" import="java.util.*,
com.xuanzhi.tools.text.*,
com.xuanzhi.tools.transport.*,java.nio.channels.*,com.fy.gamegateway.mieshi.server.*"%>
<%

		
StatManager sm = StatManager.getInstance();
SimpleEntityManager<Client> em = sm.em4Client;
String username = request.getParameter("username");

List<Client> clientList = null;
if(username != null && !username.trim().equals("")){
	clientList = em.query(Client.class,"successLoginUsernames like '%"+username+"%'","",1,100);
}

	
%>
<%@page import="com.fy.gamegateway.stat.StatManager"%>
<%@page import="com.xuanzhi.tools.simplejpa.SimpleEntityManager"%>
<%@page import="com.fy.gamegateway.stat.Client"%><html>
<head>
</head>
<body>
<form name="f1">
账号:<input type="text" name="username" id="username" value="<%=(username != null?username:"") %>">
<input type="submit" value="查询">
</form>
<center>
<h2></h2><br><a href="./servers.jsp">刷新此页面</a><br>
<br>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center">
	<td>clientId</td>
	<td>channel</td>
	<td>platform</td>
	<td>resourceName</td>
	<td>phoneType</td>
	<td>gpuType</td>
	<td>packageType</td>
	<td>timeOfFirstConnected</td>
	<td>clientProgramVersionOfFirstConnected</td>
	<td>clientResourceVersionOfFirstConnected</td>
	<td>serverProgramVersionOfFirstConnected</td>
	<td>serverResourceVersionOfFirstConnected</td>
	<td>networkTypeOfFirstConnected</td>
	<td>timeOfLastConnected</td>
	<td>clientProgramVersionOfLastConnected</td>
	<td>clientResourceVersionOfLastConnected</td>
	<td>serverProgramVersionOfLastConnected</td>
	<td>serverResourceVersionOfLastConnected</td>
	<td>networkTypeOfLastConnected</td>
	<td>hasRegister</td>
	<td>firstRegisterTime</td>
	<td>hasLogin</td>
	<td>firstLoginTime</td>
	<td>lastLoginTime</td>
	<td>lastLoginUsername</td>
	<td>loginTimes</td>
	<td>successLoginUserAmount</td>
	<td>successRegisterUserAmount</td>
	<td>successLoginUsernames</td>
	<td>successRegisterUsernames</td>
	</tr>
<%
if(clientList != null){
	for(Client client : clientList){
		%>
		<tr bgcolor="#00FFFF" align="center">
		<td><%= client.getClientId()%></td>
		<td><%= client.getChannel() %></td>
		<td><%= client.getPlatform() %></td>
		<td><%= client.getResourceName() %></td>
		<td><%= client.getPhoneType() %></td>
		<td><%= client.getGpuType() %></td>
		<td><%= client.getPackageType() %></td>
		<td><%= client.getTimeOfFirstConnected() %></td>
		<td><%= client.getClientProgramVersionOfFirstConnected() %></td>
		<td><%= client.getClientResourceVersionOfFirstConnected() %></td>
		<td><%= client.getServerProgramVersionOfFirstConnected() %></td>
		<td><%= client.getServerResourceVersionOfFirstConnected() %></td>
		<td><%= client.getNetworkTypeOfFirstConnected() %></td>
		<td><%= client.getTimeOfLastConnected() %></td>
		<td><%= client.getClientProgramVersionOfLastConnected() %></td>
		<td><%= client.getClientResourceVersionOfLastConnected() %></td>
		<td><%= client.getServerProgramVersionOfLastConnected() %></td>
		<td><%= client.getServerResourceVersionOfLastConnected() %></td>
		<td><%= client.getNetworkTypeOfLastConnected() %></td>
		<td><%= client.isHasRegister() %></td>
		<td><%= client.getFirstRegisterTime() %></td>
		<td><%= client.isHasLogin() %></td>
		<td><%= client.getFirstLoginTime() %></td>
		<td><%= client.getLastLoginTime() %></td>
		<td><%= client.getLastLoginUsername() %></td>
		<td><%= client.getLoginTimes() %></td>
		<td><%= client.getSuccessLoginUserAmount() %></td>
		<td><%= client.getSuccessRegisterUserAmount() %></td>
		<td><%= client.getSuccessLoginUsernames() %></td>
		<td><%= client.getSuccessRegisterUsernames() %></td>
		</tr>
		<%
	}

}
%>
</table>
</center>
</body>
</html> 
