<%@ page contentType="text/html;charset=gb2312" import="java.util.*,
com.xuanzhi.tools.text.*,com.xuanzhi.tools.cache.lateral.*,com.xuanzhi.tools.cache.lateral.concrete.*,
com.xuanzhi.tools.transport.*,java.nio.channels.*,com.xuanzhi.tools.queue.*"%><%

	String beanName = request.getParameter("bean");
	DefaultLateralCacheManager dlcm = null;
	dlcm = (DefaultLateralCacheManager)org.springframework.web.context.support.WebApplicationContextUtils.getWebApplicationContext(application).getBean(beanName);
	Date date = new Date(org.springframework.web.context.support.WebApplicationContextUtils.getWebApplicationContext(application).getStartupDate());
	
	HashMap<String,LateralCache> map = dlcm.getCacheMap();
	Iterator<String> it = map.keySet().iterator();
	int numElements = 0;
	int numReferences = 0;
	while(it.hasNext()){
		String key = it.next();
		DefaultLateralCache cache = (DefaultLateralCache)map.get(key);
		numElements += cache.getNumElements();
		numReferences += cache.getNumReferences();
	}
	
%><html>
<head>
</head>
<body>
<center>
<h2>Lateral Cache Manager Information Page</h2><br><a href="?bean=<%=beanName%>">reload this page</a><br>
<br>
<b>Start Time£º</b><%=DateUtil.formatDate(date,"yyyy-MM-dd HH:mm:ss")%>&nbsp;<b>Element Num:</b><%=StringUtil.addcommas(numElements)%>&nbsp;<b>Reference Num:</b><%=StringUtil.addcommas(numReferences)%><br/><br>
<%
		DefaultConnectionSelector dcs = (DefaultConnectionSelector)dlcm.getServerAdapter().getConnectionSelector();
		ConnectionSelectorHelper helper = new ConnectionSelectorHelper(dcs);
%>
<b>Server Model£¬IP/Port </b><%=dcs.getHost()+":"+dcs.getPort()%>&nbsp;<b>Put:</b><%=dlcm.getServerAdapter().getPutNum() %>
&nbsp;<b>Modify:</b><%=dlcm.getServerAdapter().getModifyNum() %>
&nbsp;<b>Remove:</b><%=dlcm.getServerAdapter().getRemoveNum() %>
&nbsp;<b>Get/Hit:</b><%=dlcm.getServerAdapter().getGetNum()+"/"+dlcm.getServerAdapter().getGetHitNum() %>&nbsp;<br>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center"><td>NAME</td><td>IDENTITY</td><td>Client Address</td><td>Checkout</td><td>Status</td><td>Sent-Receive</td><td>Use/Idel Time</td><td width="15%">Slide Window</td><td width="10%">Send Buffer</td><td width="10%">Receive Buffer</td></tr>
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
<tr bgcolor="#FFFFFF" align="center"><td><%=helper.getName(conn)%></td><td><%=conn.getIdentity() %></td><td><%=((SocketChannel)conn.getChannel()).socket().getRemoteSocketAddress()%></td><td><%=helper.isCheckout(conn)%></td><td><%=helper.getStatus(conn).substring(11)%></td><td><%=helper.getSendMessageNum(conn)%> - <%=helper.getReceiveMesageNum(conn)%></td><td><%=helper.getIdleOrUsingTime(conn)%></td>
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
		dcs = (DefaultConnectionSelector)dlcm.getClientAdapter().getConnectionSelector();
		helper = new ConnectionSelectorHelper(dcs);
%>
<b>Client Model£¬&nbsp;Min/Max Connection£º</b><%=dcs.getMinConnectionNum()+"/"+dcs.getMaxConnectionNum()%><br>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center"><td>NAME</td><td>IDENTITY</td><td>Server Address</td><td>Checkout</td><td>Status</td><td>Sent-Receive</td><td>Use/Idel Time</td><td width="15%">Slide Window</td><td width="10%">Send Buffer</td><td width="10%">Receive Buffer</td></tr>
<%

	 conns = helper.getAllConnections();
	

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
<tr bgcolor="#FFFFFF" align="center"><td><%=helper.getName(conn)%></td><td><%=conn.getIdentity() %></td><td><%=((SocketChannel)conn.getChannel()).socket().getRemoteSocketAddress()%></td><td><%=helper.isCheckout(conn)%></td><td><%=helper.getStatus(conn).substring(11)%></td><td><%=helper.getSendMessageNum(conn)%> - <%=helper.getReceiveMesageNum(conn)%></td><td><%=helper.getIdleOrUsingTime(conn)%></td>
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
	ClientAdapter ca = dlcm.getClientAdapter();
%>
<b>Client Send Queue£¬GetNum:</b><%=StringUtil.addcommas(ca.getGetNum()) %>
&nbsp;<b>RemoteGetHitNum:</b><%=StringUtil.addcommas(ca.getGetHitNum())%>
&nbsp;<b>RemoteGetHitRate:</b><%=(100.0*ca.getGetHitNum())/(ca.getGetNum()==0?1:ca.getGetNum()) %>%
<br>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center">
	<td>NAME</td><td>Image</td><td>PUSH</td><td>POP</td><td>Element</td><td>MaxNum</td><td>Rate</td>
</tr>
<%
	DefaultQueue queues[] = new DefaultQueue[]{ca.getPutQueue(),ca.getModifyQueue(),ca.getRemoveQueue()};
	String queuenames[] = new String[]{"PUTQUEUE","MODIFYQUEUE","REMOVEQUEUE"};
	for(int i = 0 ; i < queues.length ; i++){ 
		DefaultQueue queue = queues[i];
		int leftWidth2 = queue.size() * 100 / queue.getMaxSize();
		int rightWidth2 = 100 - leftWidth2;
%>
<tr bgcolor="#FFFFFF" align="center">
	<td><%=queuenames[i]%></td>
	<td>
		<table border="0" cellpadding="0" cellspacing="1" width="95%%" bgcolor="<%=rightColor%>">
              <tr height="13" bgcolor="<%=rightColor%>"><td bgcolor="<%=leftColor%>" width="<%=leftWidth2%>%"></td><td bgcolor="<%=rightColor%>" width="<%=rightWidth2%>%"></td></tr>
        </table> 
	</td>
	<td><%=StringUtil.addcommas(queue.getPushNum()) %></td>
	<td><%=StringUtil.addcommas(queue.getPopNum()) %></td>
	<td><%=StringUtil.addcommas(queue.size()) %></td>
	<td><%=StringUtil.addcommas(queue.getMaxSize()) %></td>
	<td><%=(queue.size()*10000L/queue.getMaxSize())/100.0f %>%</td>
</tr>
<%
	}
%>
</table><br>
<b>All Cache Info£º</b>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center">
	<td>NAME</td>
	<td>MODEL</td>
	<td>ElementNum</td>
	<td>RefenceNum</td>
	<td>HitRate</td>
	<td>PutNum</td>
	<td>ModifyNum</td>
	<td>RemoveNum</td>
	<td>RemoteGetNum</td>
	<td>RemoteHitRate</td>
</tr>
<%
	it = map.keySet().iterator();
	while(it.hasNext()){
		String key = it.next();
		DefaultLateralCache cache = (DefaultLateralCache)map.get(key);
		long hitmiss = cache.getCacheHits() + cache.getCacheMisses();
		if(hitmiss == 0) hitmiss = 1;
		long rhitmiss = cache.getRemoteGetHitNum() + cache.getRemoteGetMissNum();
		if(rhitmiss == 0) rhitmiss = 1;
		
		%>
		<tr bgcolor="#FFFFFF" align="center">
			<td><%=cache.getName() %></td>
			<td><%=DefaultLateralCache.getStringOfModel(cache.getHandleModel()) %></td>
			<td><%=StringUtil.addcommas(cache.getNumElements()) %></td>
			<td><%=StringUtil.addcommas(cache.getNumReferences()) %></td>
			<td><%=cache.getCacheHits()*100.0/hitmiss %>%</td>
			<td><%=StringUtil.addcommas(cache.getPutNum()) %></td>
			<td><%=StringUtil.addcommas(cache.getModifyNum()) %></td>
			<td><%=StringUtil.addcommas(cache.getRemoveNum()) %></td>
			<td><%=StringUtil.addcommas(cache.getRemoteGetHitNum() + cache.getRemoteGetMissNum()) %></td>
			<td><%=(cache.getRemoteGetHitNum()*10000L/rhitmiss)/100.0f %>%</td>
		</tr>
		<%
	}
%>
</table>
</center>
<div align='right' style='font-size:10px'>Author£º<a href="mailto:myzdf.bj@gmail.com">myzdf</a><br/>Version: 1.0</div>
</body>
</html> 
