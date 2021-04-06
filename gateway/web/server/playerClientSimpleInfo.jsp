<%@ page contentType="text/html;charset=utf-8" import="java.util.*,
com.xuanzhi.tools.text.*,java.lang.reflect.*,com.xuanzhi.tools.queue.DefaultQueue,
com.xuanzhi.tools.transport.*,java.nio.channels.*,com.fy.gamegateway.mieshi.server.*"%>
<%

		
StatManager sm = StatManager.getInstance();
SimpleEntityManager<ClientAccount> em = sm.em4Account;
String username = request.getParameter("username");
String deviceId = request.getParameter("deviceId");

List<ClientAccount> clientList = new ArrayList<ClientAccount>();


Class cl = StatManager.class;

Field field = cl.getDeclaredField("queue");
field.setAccessible(true);
DefaultQueue queue = (DefaultQueue)field.get(sm);

if(username != null && !username.trim().equals("")){
	clientList = em.query(ClientAccount.class,"username = '"+username+"' or clientId='"+username+"'","",1,1000);
}else if(deviceId != null && !deviceId.trim().equals("")){
	List<Client> list = sm.em4Client.query(Client.class,"uuid='"+deviceId+"' or deviceId='"+deviceId+"'","clientId desc",1,200);
	
	for(Client c : list){
		username = c.getClientId();
		if(username != null && !username.trim().equals("")){
			List<ClientAccount> ll = em.query(ClientAccount.class,"username = '"+username+"' or clientId='"+username+"'","",1,1000);
			clientList.addAll(ll);
		}

	}
}

	
%>
<%@page import="com.fy.gamegateway.stat.StatManager"%>
<%@page import="com.xuanzhi.tools.simplejpa.SimpleEntityManager"%>
<%@page import="com.fy.gamegateway.stat.Client"%>
<%@page import="com.fy.gamegateway.stat.ClientAccount"%><html>
<head>
</head>
<body>
StatManager队列中的消息数：size=<%= queue.size()%>,PushNum=<%=queue.getPushNum()%>,PopNum=<%=queue.getPopNum()%><br/>
<form name="f1">
账号或者ClientId:<input type="text" name="username" id="username" value="<%=(username != null?username:"") %>">
<input type="submit" value="查询">
</form>
<form name="f2">
DeviceId:<input type="text" name="deviceId" id="deviceId" value="<%=(deviceId != null?deviceId:"") %>">
<input type="submit" value="查询">
</form>
<center>
<h2></h2><br><a href="./servers.jsp">刷新此页面</a><br>
<br>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center">
	<td>客户端ID</td>
	<td>帐号名</td>
	<td>是否为注册客户端</td>
	<td>注册时间</td>
	<td>是否登录过</td>
	<td>第一次登录时间</td>
	<td>最后一次登录时间</td>
	<td>登录次数</td>
	<td>用户来源</td>
	
	<td>机型/MAC</td>
	<td>此客户端登录过的用户</td>
	<td>对应的服务器</td>
	<td>对应的角色</td>
	<td>对应的等级</td>
	<td>对应的最后升级时间</td>
	<td>此客户端注册过的用户</td>
</tr>
<%
if(clientList != null){
	for(ClientAccount client : clientList){
		System.out.println(""+client.getClientId());
		List<Client> list = sm.em4Client.query(Client.class,"clientId=?",new Object[]{client.getClientId()},"",1,10);
		int rowCount = 1;
		if(list.size() > 0){
			rowCount = 0;
			ArrayList<String> al = list.get(0).getSuccessLoginUsernames();
			MieshiPlayerInfoManager mp = MieshiPlayerInfoManager.getInstance();
			for(String un : al){
				MieshiPlayerInfo pis[] = mp.getMieshiPlayerInfoByUsername(un);
				rowCount += pis.length;
			}
		}
		if(list.size() > 0){
		boolean flag = false;
		ArrayList<String> al = list.get(0).getSuccessLoginUsernames();
		MieshiPlayerInfoManager mp = MieshiPlayerInfoManager.getInstance();
		for(String un : al){
			MieshiPlayerInfo pis[] = mp.getMieshiPlayerInfoByUsername(un);
			for(int j = 0 ; j < pis.length ; j++){
				out.println("<tr bgcolor='#00FFFF' align='center'>");
				if(flag == false){
					
					%>
						<td rowspan='<%=rowCount %>'>CLIENT:<%= client.getClientId()%><br/>DEVICE:<%= list.get(0).getUuid()+"/"+list.get(0).getDeviceId()+"<br>MAC:"+list.get(0).getMacAddress() %></td>
						<td rowspan='<%=rowCount %>'><%= client.getUsername() %></td>
						<td rowspan='<%=rowCount %>'><%= client.isHasSuccessRegister() %></td>
						<td rowspan='<%=rowCount %>'><%= client.getRegisterTime() %></td>
						<td rowspan='<%=rowCount %>'><%= client.isHasSuccessLogin() %></td>
						<td rowspan='<%=rowCount %>'><%= client.getLoginTimeForFirst() %></td>
						<td rowspan='<%=rowCount %>'><%= client.getLoginTimeForLast() %></td>
						<td rowspan='<%=rowCount %>'><%= client.getLoginTimes() %></td>
						<td rowspan='<%=rowCount %>'><%= client.getUserSource() %></td>
						<td rowspan='<%=rowCount %>'><%= list.get(0).getPhoneType() %></td><%
				}
				out.println("<td>"+un+"</td><td>"+pis[j].getServerRealName()+"</td><td>"+pis[j].getPlayerName()+"</td><td>Lv:"+pis[j].getLevel()+"</td><td>"+DateUtil.formatDate(new Date(pis[j].lastAccessTime),"yyyy-MM-dd HH:mm:ss")+"</td>");
				if(flag == false){
					flag = true;
					out.println("<td rowspan='"+rowCount+"'>"+list.get(0).getSuccessRegisterUsernames()+"</td>");
				}
				out.println("</tr>");
			}
		}
		
		}else{
		%><tr bgcolor="#00FFFF" align="center">
						<td rowspan='<%=rowCount %>'><%= client.getClientId()%></td>
						<td rowspan='<%=rowCount %>'><%= client.getUsername() %></td>
						<td rowspan='<%=rowCount %>'><%= client.isHasSuccessRegister() %></td>
						<td rowspan='<%=rowCount %>'><%= client.getRegisterTime() %></td>
						<td rowspan='<%=rowCount %>'><%= client.isHasSuccessLogin() %></td>
						<td rowspan='<%=rowCount %>'><%= client.getLoginTimeForFirst() %></td>
						<td rowspan='<%=rowCount %>'><%= client.getLoginTimeForLast() %></td>
						<td rowspan='<%=rowCount %>'><%= client.getLoginTimes() %></td>
						<td rowspan='<%=rowCount %>'><%= client.getUserSource() %></td>
						<td rowspan='<%=rowCount %>'></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td></tr>
		<%} %>
		
		<%
	}

}
%>
</table>
</center>
</body>
</html> 
