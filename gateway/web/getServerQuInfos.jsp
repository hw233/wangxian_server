<%@page import="com.fy.gamegateway.mieshi.server.MieshiServerInfo"%><%@page import="com.fy.gamegateway.mieshi.server.MieshiServerInfoManager"%><%@ page contentType="text/html;charset=utf-8" 
import="java.util.*,java.io.*"%><%	
	//0,1,2,3,4,5,6
	//绿色一区,一区,二区,三区,四区,五区,新区
	String serverName = request.getParameter("sName");
	if(serverName == null || serverName.isEmpty()){
		out.print("参数错误");
		return;
	}
	MieshiServerInfo info = MieshiServerInfoManager.getInstance().getServerInfoByName(serverName);
	if(info != null && info.getCategory() != null && !info.getCategory().isEmpty()){
		out.print(info.getCategory());
	}else{
		out.print("服务器配置错误");
	}
%>

