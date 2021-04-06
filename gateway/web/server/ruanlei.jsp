<%@ page contentType="text/html;charset=utf-8" import="java.util.*,
com.xuanzhi.tools.text.*,com.fy.gamegateway.thirdpartner.ruanlei.*,
com.xuanzhi.tools.transport.*,java.nio.channels.*,org.apache.log4j.Logger,
com.fy.gamegateway.stat.*,com.fy.gamegateway.mieshi.server.*,com.fy.gamegateway.tools.*"%><%

	long now = System.currentTimeMillis();
	
	RuanleiAppstoreMatchService service = RuanleiAppstoreMatchService.getInstance();
	
	String mac = request.getParameter("mac");
	String channel = request.getParameter("channel");
	String appName = request.getParameter("appName");
	
	if(mac == null || mac.length() == 0 || channel == null || appName == null){
		out.println("{\"result\":{\"code\": \"100001\",\"desc\":\"missing parameter\"}}");
	}
	
	if(!appName.equals("wangxian")){
		out.println("{\"result\":{\"code\": \"100001\",\"desc\":\"appName must be wangxian\"}}");
	}else if(!channel.equals("APPSTORE_RUANLIE_MIESHI")){
		out.println("{\"result\":{\"code\": \"100001\",\"desc\":\"channel must be APPSTORE_RUANLIE_MIESHI\"}}");
	}else{
		service.notifyAdvertiseClicked(mac,appName,channel);
		out.println("{\"result\":{\"code\": \"000000\",\"desc\":\"ok\"}}");
	}
%>