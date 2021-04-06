<%@ page contentType="text/html;charset=utf-8"%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">

<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.activity.fateActivity.*"%>
<%@page import="java.util.*"%>

<%@page import="com.fy.engineserver.sprite.ActivityRecordOnPlayer"%>
<%@page import="com.fy.engineserver.newBillboard.BillboardsManager"%>
<%@page import="com.fy.engineserver.newBillboard.*"%>
<%@page import="com.xuanzhi.tools.simplejpa.*"%>



<%@page import="com.fy.engineserver.util.StringTool"%>
<%@page import="com.xuanzhi.tools.text.StringUtil"%>
<%@page import="com.fy.engineserver.newBillboard.monitorLog.LogRecordManager"%>
<%@page import="com.fy.engineserver.newBillboard.monitorLog.LogRecord"%>
<%@page import="com.fy.engineserver.gateway.GameNetworkFramework"%>
<%@page import="com.xuanzhi.tools.transport.ConnectionSelector"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="com.xuanzhi.tools.transport.ConnectionConnectedHandler"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>测试</title>
</head>
<body>

	<%
		
	GameNetworkFramework gf = GameNetworkFramework.getInstance();
	ConnectionSelector cs = gf.getConnectionSelector();
	
	Class clazz = Class.forName("com.xuanzhi.tools.transport.DefaultConnectionSelector");
	
	Field f =  clazz.getDeclaredField("newClientConnectHandler");
	f.setAccessible(true);
	
	
	ConnectionConnectedHandler handler =  (ConnectionConnectedHandler)f.get(cs);
	
	out.print(handler.getClass());
	%>


	

</body>

</html>
