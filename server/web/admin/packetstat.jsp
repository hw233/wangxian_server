<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,
com.fy.engineserver.sprite.*,
com.fy.engineserver.gateway.*,com.fy.engineserver.observe.*,
com.xuanzhi.tools.transport.*,com.xuanzhi.tools.queue.*"%><%!
	long checkTime = System.currentTimeMillis();
	long rn = 0;
	long sn = 0;
	long rs = 0;
	long ss = 0;
	LinkedList<String> sb = new LinkedList<String>();
%><%@page import="com.fy.engineserver.stat.StatData"%><% 
	
	String beanName = request.getParameter("bean");
	GameNetworkFramework sm = null;
	sm = (GameNetworkFramework)org.springframework.web.context.support.WebApplicationContextUtils.getWebApplicationContext(application).getBean(beanName);

	//ObserveSubSystem om = null;
	//om = (ObserveSubSystem)org.springframework.web.context.support.WebApplicationContextUtils.getWebApplicationContext(application).getBean("observe_sub_system");
	long now = System.currentTimeMillis();
	int packageInQueueSize = 0;
	int packageInQueueSize2 = 0;
	int fullQueue = 0;
	long fullQueueSize = 0;
	int deadplayer = 0;
	Player ps[] = PlayerManager.getInstance().getOnlinePlayers();
	for(int i = 0 ; i < ps.length ; i++){
		Connection conn = ps[i].getConn();
		if(conn != null){
			DefaultSelectableQueue dq = (DefaultSelectableQueue) conn.getAttachment2();
			if(dq != null){
				if(dq.size() >= dq.getMaxSize()){
					fullQueue++;
					fullQueueSize += dq.size();
					//System.out.println("==============="+conn.getName()+"," + dq.size() + "/"+dq.getMaxSize()+" ================");
				}
				
				packageInQueueSize += dq.size();
				if( now - ps[i].getLastRequestTime() < 30000){
					packageInQueueSize2 += dq.size();
					
				}else if(now - ps[i].getLastRequestTime() > 600000){
					deadplayer++;
				}
			}
		}
		
	}
	
	Date date = new Date(org.springframework.web.context.support.WebApplicationContextUtils.getWebApplicationContext(application).getStartupDate());
	if(rn > 0){
		sb.add(0,"<tr bgcolor=\"#FFFFFF\" align=\"center\"><td>"+DateUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss")
				+"</td><td>"+StringUtil.addcommas(sm.getReceivePacketNum())+"</td><td>"
				+StringUtil.addcommas((sm.getReceivePacketNum()- rn)*1000/(System.currentTimeMillis()-checkTime))+"/秒</td><td>"
				+StringUtil.addcommas(sm.getSendPacketNum())+"</td><td>"
				+StringUtil.addcommas(0)+"</td><td>"
				+StringUtil.addcommas((sm.getSendPacketNum()- sn)*1000/(System.currentTimeMillis()-checkTime))+"/秒</td><td>"
				+StringUtil.addcommas( (sm.getReceivePacketTotalSize() - rs)*8000/(System.currentTimeMillis()-checkTime) )+"</td><td>"
				+StringUtil.addcommas( (sm.getSendPacketTotalSize() - ss)*8000/(System.currentTimeMillis()-checkTime) )+"</td><td>"
				+StringUtil.addcommas(sm.getConnectionNum())+"</td><td>"+StringUtil.addcommas(sm.getPacketQueueSize())+"</td><td>"+StringUtil.addcommas(packageInQueueSize)+"</td><td>"+StringUtil.addcommas(packageInQueueSize2)+"</td><td>"+fullQueueSize+"/"+fullQueue+"</td><td>"+StringUtil.addcommas(deadplayer)+"</td></tr>");
	}
	if(sb.size() > 256) sb.removeLast();
	
	if("tichu_10".equals(request.getParameter("action"))){
		for(int i = 0 ; i < ps.length ; i++){
			Connection conn = ps[i].getConn();
			if(conn != null){
				DefaultSelectableQueue dq = (DefaultSelectableQueue) conn.getAttachment2();
				if( now - ps[i].getLastRequestTime() > 10*60000){
					
					System.out.println("[后台踢下线] ["+ps[i].getUsername()+"] ["+conn.getName()+"] ["+((now - ps[i].getLastRequestTime())/1000)+"秒前] [队列："+dq.size()+"]");
					out.println("[后台踢下线] ["+ps[i].getUsername()+"] ["+conn.getName()+"] ["+((now - ps[i].getLastRequestTime())/1000)+"秒前] [队列："+dq.size()+"]<br>");
					
					conn.close();
				}
			}
		}
	}
	
	if("true".equals(request.getParameter("monitor"))){
		if(rn > 0){
			out.println("receive.speed=" + ((sm.getReceivePacketNum()- rn)*1000/(System.currentTimeMillis()-checkTime)));
			out.println("send.speed=" + ((sm.getSendPacketNum()- sn)*1000/(System.currentTimeMillis()-checkTime)));
			out.println("conn.num="+sm.getConnectionNum());
			out.println("queue.size="+sm.getPacketQueueSize());
			out.println("input.flow="+( (sm.getReceivePacketTotalSize() - rs)*1000/(System.currentTimeMillis()-checkTime) ));
			out.println("output.flow="+( (sm.getSendPacketTotalSize() - ss)*1000/(System.currentTimeMillis()-checkTime) ));
		}else{
			out.println("receive.speed=0");
			out.println("send.speed=0");
			out.println("conn.num="+sm.getConnectionNum());
			out.println("queue.size="+sm.getPacketQueueSize());
			out.println("input.flow=0");
			out.println("output.flow=0");
		}
		rn = sm.getReceivePacketNum();
		sn = sm.getSendPacketNum();
		rs = sm.getReceivePacketTotalSize();
		ss = sm.getSendPacketTotalSize() ;
		checkTime = System.currentTimeMillis();
		
		return;
	}else{
		rn = sm.getReceivePacketNum();
		sn = sm.getSendPacketNum() ;
		rs = sm.getReceivePacketTotalSize();
		ss = sm.getSendPacketTotalSize() ;
		checkTime = System.currentTimeMillis();
	%>
<%@include file="IPManager.jsp" %><html><head>
</HEAD>
<BODY>
<h2>Game Server， 数据包统计</h2><br><a href="./packetstat.jsp?bean=<%=beanName%>">刷新此页面</a> | <a href="./packetstat.jsp?bean=<%=beanName%>&action=tichu_10">踢出所有10分钟死在线(10分钟没有消息的)</a><br><br>



<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center"><td>时间</td><td>接收请求总数</td><td>接收速度</td><td>发送请求总数</td><td>观察请求总数</td><td>发送速度</td><td>进流量(BIT/S)</td><td>出流量(BIT/S)</td><td>连接数</td><td>等待发送队列</td><td>全部正在发送</td><td>活跃正在发送</td><td>满队列消息数/队列数</td><td>死在线人数</td></tr>	
<%
	Iterator<String> it = sb.iterator();
	while(it.hasNext()){
		out.println(it.next());
	}
%>
</table>	
</BODY></html>
<% } %>
