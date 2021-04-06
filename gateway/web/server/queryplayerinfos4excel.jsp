<%@ page contentType="text/html;charset=utf-8" import="java.util.*,
com.xuanzhi.tools.text.*,
com.xuanzhi.tools.transport.*,java.nio.channels.*,com.fy.gamegateway.mieshi.server.*"%>
<%@page import="com.xuanzhi.tools.text.ParamUtils;"%><%

		
//String beanName = "outer_gateway_connection_selector";
MieshiPlayerInfoManager mm = MieshiPlayerInfoManager.getInstance();
	
String usernames = ParamUtils.getParameter(request, "usernames", "");



	
%><html>
<head>
</head>
<body>
<h2>玩家服务器角色关系</h2>
<center>
<h2></h2><br><a href="./queryplayerinfos.jsp">刷新此页面</a><br>
<form method="post">
<input type='hidden' name='submitted' value='true'>
<textarea cols="30"  rows="20"  name='usernames' ><%= usernames %></textarea><br/>
<input type='submit' value='提交'/>
</form>
<br>
<%
	if("true".equalsIgnoreCase(request.getParameter("submitted"))){
		%>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<% 		
		String[] usernameArray = usernames.split("\r*\n");
		for(String username : usernameArray)
		{
			MieshiPlayerInfo pis[] = mm.getMieshiPlayerInfoByUsername(username);
%>
	
	<%
				for(int i = 0 ; i < pis.length ; i++){
					out.println("<tr bgcolor='#FFFFFF' align='center'>");
					out.println("<td>"+pis[i].getPlayerId()+"</td>");
					out.println("<td>"+pis[i].getServerRealName()+"</td>");
					out.println("<td>"+pis[i].getUserName()+"</td>");
					out.println("<td>"+pis[i].getPlayerName()+"</td>");
					out.println("<td>"+pis[i].getLevel()+"</td>");
					out.println("<td>"+pis[i].getCareer()+"</td>");
					out.println("<td>"+DateUtil.formatDate(new Date(pis[i].lastAccessTime),"yyyy-MM-dd HH:mm:ss")+"</td>");
				}
		%>
		
	<%	} 
	}
	%>
	</table><br>
<br/>

</center>
</body>
</html> 
