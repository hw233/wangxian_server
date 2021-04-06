<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.fy.engineserver.gateway.MieshiGatewayClientService,java.lang.reflect.*" %><%
MieshiGatewayClientService cs = MieshiGatewayClientService.getInstance();
Field f = MieshiGatewayClientService.class.getDeclaredField("thread");
f.setAccessible(true);
Thread t = (Thread)f.get(cs);
if(!t.isAlive()) {
        String action = request.getParameter("action");
        if(action != null && action.equals("start")) {
                cs.init();
                t = (Thread)f.get(cs);
        }
}
%>
<center>
<br><br>
当前线程状态：<%=t.isAlive()?"开启":"关闭" %>
<%if(!t.isAlive()) {%>
<input type=button name="bt1" value=" 开 启 " onclick="location.replace('reinitGatewayClientService.jsp?action=start');"/>
<%} %>
</center>