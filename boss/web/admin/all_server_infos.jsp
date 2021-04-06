<%@page import="com.xuanzhi.tools.text.JsonUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.fy.boss.game.model.Server"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.boss.game.service.ServerManager"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%
ServerManager smanager = ServerManager.getInstance();
List<Server> servers = smanager.getServers();
Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
List<String> serverNames = new ArrayList<String>();
List<String> serverIps = new ArrayList<String>();
List<String> serverPorts = new ArrayList<String>();
List<String> serverGamePorts = new ArrayList<String>();
for(Server server:servers) {
	serverNames.add(server.getName());
	serverIps.add(server.getGameipaddr());
	String str = server.getSavingNotifyUrl();
	if (str != null && str.contains(":")) {
		String[] ss = str.split(":");
		serverPorts.add(ss[2].split("/")[0]);
	} else {
		serverPorts.add("-1");
	}
	serverGamePorts.add(server.getGameport()+"");
}
resultMap.put("serverNames", serverNames);
resultMap.put("serverIps", serverIps);
resultMap.put("serverPorts", serverPorts);
resultMap.put("serverGamePorts", serverGamePorts);
String json = JsonUtil.jsonFromObject(resultMap);
out.print(json); 
out.flush();

%> 
