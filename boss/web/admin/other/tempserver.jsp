<%@ page import="com.fy.boss.game.model.Server"%>
<%@ page import="com.fy.boss.game.service.ServerManager"%>
<%@ page import="java.util.*,java.io.*,com.xuanzhi.tools.text.*"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="admin/header.jsp"%>
<%
	ServerManager smanager = ServerManager.getInstance();
	String message = null;
	String name = "化外之境";
	String gameipaddr = "117.135.129.57";
	String gameport = "9201";
	String dburi = "xxx";
	String dbusername = "aaa";
	String dbpassword = "bbb";
	String resinhome = "resin_server";
	String savingNotifyUrl = "http://117.135.129.57:8801/saving_notifier?authorize.username=innerapp&authorize.password=innerapp123";
	String serverId = "1000";
	try {
		Server server = new Server();
		server.setName(name);
		server.setGameipaddr(gameipaddr);
		server.setGameport(Integer.parseInt(gameport));
		server.setSavingNotifyUrl(savingNotifyUrl);
		server.setDburi(dburi);
		server.setDbusername(dbusername);
		server.setDbpassword(dbpassword);
		server.setServerid(serverId);
		server.setResinhome(resinhome);
		server = smanager.createServer(server);
		if (server != null) {
			message = "创建成功!";
		} else {
			message = "创建失败，插入库失败";
		}
	} catch (Exception e) {
		e.printStackTrace();
		message = "失败:" + e.getMessage();
	}
%>
<%=message %>
