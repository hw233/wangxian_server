<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,
com.fy.engineserver.observe.*,com.xuanzhi.tools.transport.*"%><% 
	
	String beanName = request.getParameter("bean");
	if(beanName == null) beanName = "observe_sub_system";
	ObserveSubSystem sm = null;
	sm = (ObserveSubSystem)org.springframework.web.context.support.WebApplicationContextUtils.getWebApplicationContext(application).getBean(beanName);
	Date date = new Date(org.springframework.web.context.support.WebApplicationContextUtils.getWebApplicationContext(application).getStartupDate());
	
	%>
<%@include file="IPManager.jsp" %><html><head>
</HEAD>
<BODY>
<h2>Game Server，各个观察者的情况</h2><br><a href="./observer.jsp?bean=<%=beanName%>">刷新此页面</a><br><br>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center"><td>编号</td><td>观察者名词</td><td>Online</td><td>Identity</td><td>地图</td><td>位置</td><td>视窗中人数</td></tr>	
<%
	com.fy.engineserver.observe.Observer observers[] = sm.getObservers();
	for(int i = 0 ; i < observers.length ; i++){
		%><tr bgcolor="#FFFFFF" align="center"><td><%=i+1 %></td><td><%=observers[i].getName() %></td><td><%= observers[i].getConn().getState() != Connection.CONN_STATE_CLOSE %></td><td><%=observers[i].getConn().getIdentity() %></td>
		<td><%= observers[i].getGame() %><td><%= "("+observers[i].getX()+","+observers[i].getY()+","+observers[i].getViewWidth()+","+observers[i].getViewHeight()+")"%></td>
		<td><%= observers[i].getViewMap().size() %></td></tr><%
	}
%>
</table>	
</BODY></html>
