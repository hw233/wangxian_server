<%@ page contentType="text/html;charset=utf-8" import="java.util.*,
com.xuanzhi.tools.text.*,
com.xuanzhi.tools.transport.*,java.nio.channels.*"%><%

	//String beanName = "outer_gateway_connection_selector";
	
	String beanName = request.getParameter("bean");
	DefaultConnectionSelector dcs = null;
	dcs = (DefaultConnectionSelector)org.springframework.web.context.support.WebApplicationContextUtils.getWebApplicationContext(application).getBean(beanName);
	Date date = new Date(org.springframework.web.context.support.WebApplicationContextUtils.getWebApplicationContext(application).getStartupDate());
	ConnectionSelectorHelper helper = new ConnectionSelectorHelper(dcs);
	boolean clientModel = dcs.isClientModel();
	
%><%@include file="IPManager.jsp" %><html>
<head>
</head>
<body>
<center>
<h2><%=dcs.getName()%>，各个连接的情况</h2><br><a href="./conns.jsp?bean=<%=beanName%>">刷新此页面</a><br>
<br>
启动时间：<%=DateUtil.formatDate(date,"yyyy-MM-dd HH:mm:ss")%>&nbsp;<%=dcs.isClientModel()?"客户端模式运行":"服务器模式运行"%>&nbsp;
IP/Port <%=dcs.getHost()+":"+dcs.getPort()%>&nbsp;<%= dcs.isClientModel()?("最小/最大链接："+dcs.getMinConnectionNum()+"/"+dcs.getMaxConnectionNum()):("当前链接数："+ helper.getConnectionInSelectorNum()+"/"+helper.getConnectionNum() ) %> &nbsp;缓冲区内存：<%=StringUtil.addcommas(helper.getTotalReceiveBuffer()) %>/<%=StringUtil.addcommas(helper.getTotalSendBuffer()) %>
正在创建/关闭连接数：<%= helper.getCreatingConecttionNum()+"/"+helper.getClosingConecttionNum() %><br><br>
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
              <tr height="13" bgcolor="<%=rightColor%>"><td bgcolor="<%=leftColor%>" width="<%=leftWidth1%>%"></td><td bgcolor="<%=rightColor%>" width="<%=rightWidth1%>%"><%= size %></td></tr>
        </table>	
</td>
<td>
	<table border="0" cellpadding="0" cellspacing="1" width="95%%" bgcolor="<%=rightColor%>">
              <tr height="13" bgcolor="<%=rightColor%>"><td bgcolor="<%=leftColor%>" width="<%=leftWidth2%>%"></td><td bgcolor="<%=rightColor%>" width="<%=rightWidth2%>%"><%= size2 %></td></tr>
        </table> 
</td></tr>
<%
	}
%>
</table><br>

</center>
</body>
</html> 
