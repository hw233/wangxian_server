<%@page import="java.io.IOException"%>
<%@page import="java.net.MalformedURLException"%>
<%@page import="com.fy.engineserver.core.Game"%>
<%@page import="com.xuanzhi.tools.servlet.HttpUtils"%>
<%@page import="java.util.*"%>
<%@page
	import="com.fy.engineserver.activity.across.AcrossServerManager"%>
<%@page
	import="com.fy.engineserver.gm.newfeedback.NewFeedbackSubSystem"%>
<%@page import="com.fy.engineserver.util.TimeTool.TimeDistance"%>
<%@page import="com.fy.engineserver.homestead.cave.Cave"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	out.print(testServlet("http://116.213.192.203:8002/acrossServerCommunication", "test...jsp"));
%>
<%!public String testServlet(String url, String sayWhat) {
		Map headers = new HashMap();
		try {
			headers.put("requestType", "test");
			headers.put("content", sayWhat);
			headers.put("authorize.username", "wx_across_user");
			headers.put("authorize.password", "111111");
			byte bytes[] = HttpUtils.webPost(new java.net.URL(url), "requestType=test".getBytes(), headers, 60000, 60000);
			String result = new String(bytes, "UTF-8");
			Game.logger.warn("[跨服消息] [发送] [" + url + "] [" + sayWhat + "] [result:" + result + "]");
			return new String(bytes,"UTF-8");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>