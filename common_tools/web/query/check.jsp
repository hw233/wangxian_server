<%@ page import="com.xuanzhi.tools.query.net.MobileServer" %><%@ page import="com.xuanzhi.tools.query.net.MobileClient" %><%@ page contentType="text/xml;charset=gbk" import="java.io.*"%><%
	MobileServer ms = MobileServer.getInstance();
	byte b = ms.query("13000000000");
	MobileClient.STATUS st = new MobileClient.STATUS(b);
	boolean s = st.isVip();
    out.println("<?xml version=\"1.0\" encoding=\"gbk\" ?>");
    out.println("<MonitorMessage>");
    out.println("<data class=\"String\">"+s+"</data>");
	out.println("<map>");
	out.println("<entry>");
	out.println("<key class=\"String\">desc</key>");
	out.println("<value class=\"String\">"+(s?"系统正常":"查询错误")+"</value>");
	out.println("</entry>");
    out.println("</map>");
    out.println("</MonitorMessage>");
%>