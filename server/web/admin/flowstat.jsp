<%@ page contentType="text/html;charset=utf-8" import="java.util.*,java.io.*,com.xuanzhi.tools.text.*,
com.fy.engineserver.gateway.*"%><% 

	String beanName = "game_gateway";
	GameNetworkFramework sm = null;
	sm = (GameNetworkFramework)org.springframework.web.context.support.WebApplicationContextUtils.getWebApplicationContext(application).getBean(beanName);

	int packets = sm.getConnectionNum() * 10;
	if(packets < 100) packets = 100;
	
	long startTime = System.currentTimeMillis();

	String outputFile = "/usr/local/projects/game_engine_server/."+StringUtil.randomIntegerString(10)+".cap";
	String cmdLine = "/usr/sbin/tcpdump -s0 -w "+outputFile+" -c " + packets;
	
	Process p = Runtime.getRuntime().exec(cmdLine);
	p.waitFor();
	long costTime = System.currentTimeMillis() - startTime;
	
	java.io.File f = new java.io.File(outputFile);
	long flowSize = f.length();
	f.delete();
	
	out.println("costTime="+costTime);
	out.println("flowSize="+flowSize);
	out.println("packets="+packets);
	
	/*
	tcpdump: listening on eth0, link-type EN10MB (Ethernet), capture size 65535 bytes
	6419 packets captured
	6439 packets received by filter
	20 packets dropped by kernel
	*/
%>
