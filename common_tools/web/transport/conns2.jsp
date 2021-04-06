<%@ page contentType="text/html;charset=UTF-8" import="java.util.*,
com.xuanzhi.tools.text.*,com.xuanzhi.tools.servlet.*,org.w3c.dom.*,
com.xuanzhi.tools.transport2.*,java.nio.channels.*"%>
<%@page import="com.xuanzhi.tools.servlet.HttpUtils"%>
<%@page import="java.net.InetSocketAddress"%><%! 

	public String getIPInfo(String ip) throws Exception{
		String url = "http://www.youdao.com/smartresult-xml/search.s?type=ip&q="+ip;
		byte content[] = HttpUtils.webGet(new java.net.URL(url),null,1000,1000);
		String s = new String(content,"gbk");
		Document dom = XmlUtil.loadString(s);
		Element root = dom.getDocumentElement();
		Element product = XmlUtil.getChildByName(root,"product");
		Element location = XmlUtil.getChildByName(product,"location");
		return XmlUtil.getText(location,null);
	}
	%><%

	
	
	String beanName = request.getParameter("name");
	
	if(beanName == null){
		String keys[] = ConnectionSelectorHelper2.selectorMap.keySet().toArray(new String[0]);
		for(int i = 0 ; i < keys.length ; i++){
			out.println("<a href='./conns2.jsp?name="+keys[i]+"'>"+keys[i]+"</a>");
			if(i < keys.length -1){
				out.println("&nbsp;&nbsp;|&nbsp;&nbsp;");
			}
		}
		return;
	}
	
	DefaultConnectionSelector2 dcs = ConnectionSelectorHelper2.getSelector(beanName);
	ConnectionSelectorHelper2 helper = new ConnectionSelectorHelper2(dcs);
	Date date = helper.getSelectorCreateTime();
	
	boolean clientModel = dcs.isClientModel();
	
	boolean showAddess = false;
	
	if("true".equalsIgnoreCase(request.getParameter("showaddress"))){
		showAddess = true;
	}
	
	boolean showFilter = false;
	
	if("true".equalsIgnoreCase(request.getParameter("showfilter"))){
		showFilter = true;
	}
	
%><html>
<head>
</head>
<body>
<center>
<h2><%=dcs.getName()%>，各个连接的情况</h2><br><a href="./conns2.jsp?name=<%=beanName%>">刷新此页面</a> | <a href="./conns2.jsp?name=<%=beanName%>&showfilter=true">只显示可能有问题链接</a> <br>
<br>
启动时间：<%=DateUtil.formatDate(date,"yyyy-MM-dd HH:mm:ss")%>&nbsp;<%=dcs.isClientModel()?"客户端模式运行":"服务器模式运行"%>&nbsp;
IP/Port <%=dcs.getHost()+":"+dcs.getPort()%>&nbsp;<%= dcs.isClientModel()?("最小/最大链接："+dcs.getMinConnectionNum()+"/"+dcs.getMaxConnectionNum()):("当前链接数："+ helper.getConnectionInSelectorNum()+"/"+helper.getConnectionNum() ) %> &nbsp;缓冲区内存：<%=StringUtil.addcommas(helper.getTotalReceiveBuffer()) %>/<%=StringUtil.addcommas(helper.getTotalSendBuffer()) %>
正在创建/关闭连接数：<%= helper.getCreatingConecttionNum()+"/"+helper.getClosingConecttionNum() %><br>
等待写入发送缓冲区的消息数目：<%= helper.getWaitingQueueEventNum() %>&nbsp;&nbsp;|&nbsp;&nbsp;等待将发送缓冲区写入网络的事件数目：<%= helper.getWaitingQueueEventNum() %>&nbsp;&nbsp;|&nbsp;&nbsp;发送缓冲区满且队列有消息的链接数目：<%= helper.getConnectionNumOfSendBufferFullAndWaitingQueueNotEmpty() %><br><br>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center">
<td>名称</td><td>标识</td>
<td><%=dcs.isClientModel()?"服务器":"客户端"%>地址</td>

<td>状态</td><td>操作</td><td>发送-接收</td><td>空闲时间</td><td>无数据接受时间</td><td width="10%">等待发送消息</td><td width="10%">发送缓冲区</td><td width="10%">接收缓冲区</td>
<%
	if(helper.getSelectorStatProtocolFlag()){
		out.println("<td>流量详情</td>");
	}
%>
</tr>
<%
Connection2 conns[] = new Connection2[0];
	try{
		 conns = helper.getAllConnections();
	}catch(Exception e){
		conns = new Connection2[0];
		out.println("<tr bgcolor='#FFFFFF' align='center'><td colspan='10'><h2>出现访问冲突，请稍后重试！</h2></td></td>");
	}	
	String rightColor = "#0000FF";
	String leftColor = "#FF0000";
	for(int i = 0 ; i < conns.length ; i++){
		Connection2 conn = conns[i];	

		int size = helper.getCurrentSendBufferSize(conn);
		int capacity = helper.getMaxSendBufferSize(conn);
		int leftWidth1 = size * 100 / capacity;
		int rightWidth1 = 100 - leftWidth1;

		int size2 = helper.getCurrentReceiveBufferSize(conn);
		int capacity2 = helper.getMaxReceiveBufferSize(conn);
		int leftWidth2 = size2 * 100 / capacity2;
                int rightWidth2 = 100 - leftWidth2;	
		
        if(showFilter && size == 0 && size2 == 0 && helper.getWaitingMessageNum(conn) == 0 && helper.getIdleTime(conn) < 60000L ){
        	continue;
        }      
%>
<tr bgcolor="#FFFFFF" align="center">
<td><%=helper.getName(conn)%></td><td><%=(conn.getIdentity()==null?"":conn.getIdentity()) %></td>
<%
	out.println("<td>"+conn.getRemoteAddress()+"</td>");
	
%>
<td><%=helper.getStatus(conn).substring(11)%></td><td><%= helper.getConnectionOperation(conn) %></td>
<td><%=helper.getSendMessageNum(conn)%> - <%=helper.getReceiveMesageNum(conn)%></td>
<td><%=helper.getIdleTime(conn)%></td>
<td><%= System.currentTimeMillis() - helper.getLastReceiveDataTime(conn).getTime() %></td>
<td>
	<table border="0" cellpadding="0" cellspacing="1" width="95%" bgcolor="#000000">
              <tr height="13" bgcolor="<%=rightColor%>">
	<%
		int cs = helper.getWaitingMessageNum(conn);
		int ms = 1024;
		int leftWidth3 = cs/ms;
		if(leftWidth3 > 100) leftWidth3 = 99;
		int rightWidth3 = 100-leftWidth3;
		
	%>	
	<td bgcolor="<%=leftColor%>" width="<%=leftWidth3%>%"></td>
	<td bgcolor="<%=rightColor%>" width="<%=rightWidth3%>%"><%= cs %></td>
    </tr></table>
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
</td>
<%
	if(helper.getSelectorStatProtocolFlag()){
		out.println("<td><a href='./protocolstat.jsp?name="+beanName+"&conn="+conn.getName()+"'>流量详情</a></td>");
	}
%>
</tr>
<%
	}
%>
</table><br>

<h3>正在创建，还没有返回给Selector的链接</h3>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center"><td>名称</td><td><%=dcs.isClientModel()?"服务器":"客户端"%>地址</td><td>状态</td><td>发送-接收</td><td>使用/空闲时间</td><td width="15%">滑动窗口</td><td width="10%">发送缓冲区</td><td width="10%">接收缓冲区</td></tr>
<%

 conns = helper.getCreatingConnections();
	
	 rightColor = "#0000FF";
	 leftColor = "#FF0000";
	for(int i = 0 ; i < conns.length ; i++){
		Connection2 conn = conns[i];	
		int size = helper.getCurrentSendBufferSize(conn);
		int capacity = helper.getMaxSendBufferSize(conn);
		int leftWidth1 = size * 100 / capacity;
		int rightWidth1 = 100 - leftWidth1;

		int size2 = helper.getCurrentReceiveBufferSize(conn);
		int capacity2 = helper.getMaxReceiveBufferSize(conn);
		int leftWidth2 = size2 * 100 / capacity2;
                int rightWidth2 = 100 - leftWidth2;	
		
%>
<tr bgcolor="#FFFFFF" align="center"><td><%=helper.getName(conn)%></td><td><%=((SocketChannel)conn.getChannel()).socket().getRemoteSocketAddress()%></td><td><%=helper.getStatus(conn).substring(11)%></td><td><%=helper.getSendMessageNum(conn)%> - <%=helper.getReceiveMesageNum(conn)%></td><td><%=helper.getIdleTime(conn)%></td>
<td>
	<table border="0" cellpadding="0" cellspacing="1" width="95%" bgcolor="#000000">
              <tr height="13" bgcolor="<%=rightColor%>">
	<%
		int cs = helper.getWaitingMessageNum(conn);
		int ms = 1024;
		int leftWidth3 = cs/ms;
		if(leftWidth3 > 100) leftWidth3 = 99;
		int rightWidth3 = 100-leftWidth3;
		
	%>	
	<td bgcolor="<%=leftColor%>" width="<%=leftWidth3%>%"></td>
	<td bgcolor="<%=rightColor%>" width="<%=rightWidth3%>%"><%= cs %></td>
    </tr></table>
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
