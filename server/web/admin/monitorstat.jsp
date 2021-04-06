<%@ page contentType="text/html;charset=utf-8" import="java.util.*,java.io.*,com.xuanzhi.tools.text.*,
com.fy.engineserver.gateway.*,com.xuanzhi.tools.transport.*"%><% 
	
	String beanName = request.getParameter("bean");
	GameNetworkFramework sm = null;
	sm = (GameNetworkFramework)org.springframework.web.context.support.WebApplicationContextUtils.getWebApplicationContext(application).getBean(beanName);
	
	out.println("receive.packets="+sm.getReceivePacketNum());
	out.println("send.packets="+sm.getSendPacketNum());
	out.println("conn.num="+sm.getConnectionNum());
	out.println("queue.size="+sm.getPacketQueueSize());
	out.println("input.size="+sm.getReceivePacketTotalSize());
	out.println("output.size="+sm.getSendPacketTotalSize());
	
	try{
		String filename = "/proc/loadavg";
		BufferedReader reader = new BufferedReader(new FileReader(new File(filename)));
		String loadavg = reader.readLine(); //0.19 0.13 0.10 1/406 21272
		reader.close();
		String loadCols[] = loadavg.split(" ");
		out.println("loadavg=" + (Integer.parseInt(loadCols[0])));
	}catch(Exception e){}
	
%>
