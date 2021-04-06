
<%@page import="com.fy.gamegateway.mieshi.server.MieshiGatewaySubSystem.ClientRegisterItem"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiGatewaySubSystem"%>
<%
	ClientRegisterItem  clientRegisterItem = (ClientRegisterItem)MieshiGatewaySubSystem.getInstance().clientRegisterCache.get("04404301460527270705");

	out.println(clientRegisterItem.registerUserMap);
	
%>
