<%@ page contentType="text/html;charset=utf-8" import="java.util.*,
com.xuanzhi.tools.text.*,
com.xuanzhi.tools.transport.*,java.nio.channels.*"%><%

	String beanName = request.getParameter("bean");
	int secondaryIndex = 0;
	if(request.getParameter("si") != null){
		try{
			secondaryIndex = Integer.parseInt(request.getParameter("si"));
		}catch(Exception e){}
	}
	
	DefaultConnectionSelector dcs = null;
	dcs = (DefaultConnectionSelector)org.springframework.web.context.support.WebApplicationContextUtils.getWebApplicationContext(application).getBean(beanName);
	Date date = new Date(org.springframework.web.context.support.WebApplicationContextUtils.getWebApplicationContext(application).getStartupDate());
	//ConnectionSelectorHelper helper = new ConnectionSelectorHelper(dcs);
	boolean clientModel = dcs.isClientModel();
%><html>
<head>
</head>
<body>
<center>
<h2><%=dcs.getName()%>，各个连接的情况</h2><br><a href="./multiconns.jsp?bean=<%=beanName%>&si=<%=secondaryIndex%>">刷新此页面</a><br>
<br>
启动时间：<%=DateUtil.formatDate(date,"yyyy-MM-dd HH:mm:ss")%>&nbsp;<%=  clientModel?"客户端模式":(!dcs.isMultiServerModel()?"单服务器模式运行":"多服务器模式运行")%>&nbsp;
IP/Port <%=dcs.getHost()+":"+dcs.getPort()%>&nbsp;最小/最大链接：<%=dcs.getMinConnectionNum()+"/"+dcs.getMaxConnectionNum()%><br><br>
<%
	ConnectionSelectorHelper helper = null;
	
	if(dcs.isMultiServerModel()){
		DefaultConnectionSelector secondaryServers[] = dcs.getSecondaryServers();
		%><br/>各个副服务器列表：<br/>
		<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
		<tr bgcolor="#00FFFF" align="center"><td>编号</td><td>名称</td><td>管理的连接数</td><td>正在处理消息的连接数</td><td>等待数据的连接数</td></tr>
		<%
		for(int i = 0 ; i < secondaryServers.length ; i++){
			helper = new ConnectionSelectorHelper(secondaryServers[i]);
			%><tr bgcolor="#FFFFFF" align="center"><td><%=(i+1)%></td><td><a href='./multiconns.jsp?bean=<%=beanName%>&si=<%=(i+1)%>'><%=secondaryServers[i].getName() %></a></td>
			<td><%=helper.getConnectionNum() %></td><td><%=helper.getConnectionNum()-helper.getConnectionInSelectorNum() %></td>
			<td><%=helper.getConnectionInSelectorNum() %></td></tr><% 
		}
		%></table><%
	}
	helper = null;
	
	if(!dcs.isMultiServerModel() || dcs.isClientModel())
		helper = new ConnectionSelectorHelper(dcs);
	else{
		DefaultConnectionSelector secondaryServers[] = dcs.getSecondaryServers();
		if(secondaryIndex >= 1 && secondaryIndex <=secondaryServers.length){
			helper = new ConnectionSelectorHelper(secondaryServers[secondaryIndex-1]);
		}
	}
	
	if(helper != null){
%><br>服务器<%=helper.getConnectionSelectorName() %>各个链接的情况</br>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center"><td>名称</td><td><%=dcs.isClientModel()?"服务器":"客户端"%>地址</td><td>Checkout</td><td>状态</td><td>发送-接收</td><td>使用/空闲时间</td><td width="15%">滑动窗口</td><td width="10%">发送缓冲区</td><td width="10%">接收缓冲区</td></tr>
<%

	Connection conns[] = helper.getAllConnections();
	
	String rightColor = "#0000FF";
	String leftColor = "#FF0000";
	for(int i = 0 ; i < conns.length ; i++){
		Connection conn = conns[i];	
		int size = helper.getCurrentSendBufferSize(conn);
		int capacity = helper.getMaxSendBufferSize(conn);
		int leftWidth1 = size * 100 / capacity;
		int rightWidth1 = 100 - leftWidth1;

		int size2 = helper.getCurrentReceiveBufferSize(conn);
		int capacity2 = helper.getMaxReceiveBufferSize(conn);
		int leftWidth2 = size2 * 100 / capacity2;
                int rightWidth2 = 100 - leftWidth2;	
		
%>
<tr bgcolor="#FFFFFF" align="center"><td><%=helper.getName(conn)%></td><td><%=((SocketChannel)conn.getChannel()).socket().getRemoteSocketAddress()%></td><td><%=helper.isCheckout(conn)%></td><td><%=helper.getStatus(conn).substring(11)%></td><td><%=helper.getSendMessageNum(conn)%> - <%=helper.getReceiveMesageNum(conn)%></td><td><%=helper.getIdleOrUsingTime(conn)%></td>
<td>
	<table border="0" cellpadding="0" cellspacing="1" width="95%" bgcolor="#000000">
              <tr height="13" bgcolor="<%=rightColor%>">
	<%
		int cs = helper.getCurrentWindowSize(conn);
		int ms = helper.getMaxWindowSize(conn);
		for(int j = 0 ; j < ms ; j++){
			if(j < cs){
				%><td bgcolor="<%=leftColor%>"></td><%
			}else{
				%><td bgcolor="<%=rightColor%>"></td><%
			}
		}
	%>
        </table>
</td>
<td>
	<table border="0" cellpadding="0" cellspacing="1" width="95%%" bgcolor="<%=rightColor%>">
              <tr height="13" bgcolor="<%=rightColor%>"><td bgcolor="<%=leftColor%>" width="<%=leftWidth1%>%"></td><td bgcolor="<%=rightColor%>" width="<%=rightWidth1%>%"></td></tr>
        </table>	
</td>
<td>
	<table border="0" cellpadding="0" cellspacing="1" width="95%%" bgcolor="<%=rightColor%>">
              <tr height="13" bgcolor="<%=rightColor%>"><td bgcolor="<%=leftColor%>" width="<%=leftWidth2%>%"></td><td bgcolor="<%=rightColor%>" width="<%=rightWidth2%>%"></td></tr>
        </table> 
</td></tr>
<%
	}
%>
</table><br>
<%
	}//end if(helper != null)
%>
</center>
</body>
</html> 
