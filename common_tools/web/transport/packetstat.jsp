<%@page import="com.xuanzhi.tools.transport2.*"%>
<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,
com.xuanzhi.tools.transport.*,com.xuanzhi.tools.queue.*"%><%!

	HashMap<String,long[]> map2 =new HashMap<String,long[]>();
	//long checkTime = System.currentTimeMillis();
	//long rn = 0;
	//long sn = 0;
	//long rs = 0;
	//long ss = 0;
	HashMap<String,LinkedList<String>> map =new HashMap<String,LinkedList<String>>();
	
%><% 
	
	
	String beanName = request.getParameter("name");
	
	if(beanName == null){
		String keys[] = ConnectionSelectorHelper2.selectorMap.keySet().toArray(new String[0]);
		for(int i = 0 ; i < keys.length ; i++){
			out.println("<a href='./packetstat.jsp?name="+keys[i]+"'>"+keys[i]+"</a>");
			if(i < keys.length -1){
				out.println("&nbsp;&nbsp;|&nbsp;&nbsp;");
			}
		}
		return;
	}

	LinkedList<String> sb = map.get(beanName);
	if(sb == null){
		sb = new LinkedList<String>();
		map.put(beanName,sb);
	}

	long[] data = map2.get(beanName);
	if(data == null){
		data = new long[5];
		data[0] = System.currentTimeMillis();
		map2.put(beanName,data);
	}
	long rn = data[1];
	long sn = data[2];
	long rs = data[3];
	long ss = data[4];
	long checkTime = data[0];
	
	DefaultConnectionSelector2 selector = ConnectionSelectorHelper2.getSelector(beanName);
	ConnectionSelectorHelper2 helper2 = new ConnectionSelectorHelper2(selector);

	if(request.getParameter("statprotocol") != null){
		if("true".equals(request.getParameter("statprotocol"))){
			helper2.setSelectorStatProtocolFlag(true);
		}else if("false".equals(request.getParameter("statprotocol"))){
			helper2.setSelectorStatProtocolFlag(false);
		}
	}
	
	long now = System.currentTimeMillis();
	int packageInQueueSize = 0;
	int packageInQueueSize2 = 0;
	int fullQueue = 0;
	long fullQueueSize = 0;
	int deadplayer = 0;
	
	long getReceivePacketNum = helper2.getTotalReceiveMessageNum();
	long getSendPacketNum  = helper2.getTotalSendMesageNum();
	long getReceivePacketTotalSize = helper2.getTotalReceiveMessagePacketSize();
	long getSendPacketTotalSize = helper2.getTotalSendMessagePacketSize();
	long nowCheckTime = helper2.getLastStatNumAndPacketTime();
	
	long getWaitingQueueSize = 0;
	
	Connection2 conns[] = helper2.getAllConnections();
	for(int i = 0 ; i < conns.length ; i++){
		Connection2 conn = conns[i];
		if(conn != null && conn.getState() == Connection2.CONN_STATE_CONNECT){
			int qs = conn.getWaitingQueueSize();
			if(qs >= 1024){
				fullQueue++;
				fullQueueSize += qs;
			}
				
			packageInQueueSize += qs;
			if( now - conn.getLastReceiveDataTime() < 30000){
				packageInQueueSize2 += qs;
			}else if(now -conn.getLastReceiveDataTime() > 600000){
				deadplayer++;
			}
			
			getWaitingQueueSize += qs;
		}
		
	}

	Date date = helper2.getSelectorCreateTime();
	
	
	if(rn > 0 && nowCheckTime > checkTime){
		
		 
		sb.add(0,"<tr bgcolor=\"#FFFFFF\" align=\"center\"><td>"+DateUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss")
				+"</td><td>"+StringUtil.addcommas(getReceivePacketNum)+"</td><td>"
				+StringUtil.addcommas((getReceivePacketNum- rn)*1000/(nowCheckTime-checkTime))+"/秒</td><td>"
				+StringUtil.addcommas(getSendPacketNum)+"</td><td>"	
				+StringUtil.addcommas((getSendPacketNum- sn)*1000/(nowCheckTime-checkTime))+"/秒</td><td>"
				+StringUtil.addcommas( (getReceivePacketTotalSize - rs)*8000/(nowCheckTime-checkTime) )+"</td><td>"
				+StringUtil.addcommas( (getSendPacketTotalSize - ss)*8000/(nowCheckTime-checkTime) )+"</td><td>"
				+StringUtil.addcommas(helper2.getConnectionNum())+"</td><td>"+StringUtil.addcommas(getWaitingQueueSize)+"</td><td>"
				+StringUtil.addcommas(packageInQueueSize)+"</td><td>"+StringUtil.addcommas(packageInQueueSize2)+"</td><td>"+fullQueueSize+"/"+fullQueue+"</td><td>"+StringUtil.addcommas(deadplayer)+"</td></tr>");
	}
	if(sb.size() > 256) sb.removeLast();
	
	
	if("true".equals(request.getParameter("monitor"))){
		
		rn = getReceivePacketNum;
		sn = getSendPacketNum ;
		rs = getReceivePacketTotalSize;
		ss = getSendPacketTotalSize ;
		checkTime = nowCheckTime;
		
		data[0] = checkTime;
		data[1] = rn;
		data[2] = sn;
		data[3] = rs;
		data[4] = ss;
		return;
	}else{
		rn = getReceivePacketNum;
		sn = getSendPacketNum ;
		rs = getReceivePacketTotalSize;
		ss = getSendPacketTotalSize ;
		checkTime = nowCheckTime;
		
		data[0] = checkTime;
		data[1] = rn;
		data[2] = sn;
		data[3] = rs;
		data[4] = ss;
	%>
<html><head>
</HEAD>
<BODY>
<h2><%=beanName %>， 数据包统计</h2>
<br><a href="./packetstat.jsp?name=<%=beanName%>">刷新此页面</a> &nbsp;&nbsp;|&nbsp;&nbsp;<a href="./packetstat.jsp?name=<%=beanName%>&statprotocol=<%= helper2.getSelectorStatProtocolFlag()?"false":"true" %>"><%= helper2.getSelectorStatProtocolFlag()?"关闭协议流量分布统计":"开启协议流量分布统计" %></a> <%= helper2.getSelectorStatProtocolFlag()?("&nbsp;&nbsp;|&nbsp;&nbsp;<a href='./protocolstat.jsp?name="+beanName+"'>查看协议流量分布</a>"):"" %><br><br>



<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center"><td>时间</td><td>接收请求总数</td><td>接收速度</td><td>发送请求总数</td><td>发送速度</td><td>进流量(BIT/S)</td><td>出流量(BIT/S)</td><td>连接数</td><td>等待发送队列</td><td>全部正在发送</td><td>活跃正在发送</td><td>满队列消息数/队列数</td><td>死在线人数</td></tr>	
<%
	Iterator<String> it = sb.iterator();
	while(it.hasNext()){
		out.println(it.next());
	}
%>
</table>	
</BODY></html>
<% } %>
