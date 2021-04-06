<%@page import="java.util.*,java.io.*,
				com.xuanzhi.tools.text.*,
				com.xuanzhi.stat.util.*,
				com.sqage.stat.model.*,
				com.xuanzhi.stat.channel.*,
				com.xuanzhi.stat.*,
				com.xuanzhi.stat.operate.*,
				com.sqage.stat.service.*,
				com.xuanzhi.stat.chart.*,
				com.xuanzhi.boss.gmxml.CheckItem,
				com.xuanzhi.tools.queue.AdvancedFilePersistentQueue,
				com.xuanzhi.stat.core.*"%>
<%@page import="com.xuanzhi.boss.gmuser.model.Gmuser"%>
<%
Gmuser user = (Gmuser)session.getAttribute("gmuser");
//if(user == null) {
//	response.sendRedirect("/");
//	return;
//}
String mid = request.getParameter("mid");
String url = request.getRequestURL().toString();
//String result = CheckItem.checkResult(user.getUsername(), url);
//System.out.println("[trace] ["+result+"] ["+mid+"] ["+url+"]");
//if(result == null) {
//	response.sendRedirect("/");
//	return;
//}
%>
