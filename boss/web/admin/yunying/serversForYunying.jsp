<%@page import="com.xuanzhi.tools.text.JsonUtil"%><%@page import="com.fy.boss.deploy.*"%><%@page import="com.fy.boss.deploy.ProjectManager"%><%@page import="com.fy.boss.game.model.Server"%><%@page import="java.util.*"%><%@page import="com.fy.boss.game.service.ServerManager"%><%@ page language="java" contentType="text/html; charset=UTF-8"   pageEncoding="UTF-8"%><%
	ServerManager smanager = ServerManager.getInstance();
	List<Server> servers = smanager.getServers();
	ProjectManager pm = ProjectManager.getInstance(); 
	ArrayList<ServerInfo> resultList = new ArrayList<ServerInfo>();
	for (Server server : servers) {
		ServerStatus status = pm.getServerStatus(server);
		Status serverStatus = null;
		if(!status.isAgenton()) {
			serverStatus = Status.未响应;
		} else {
			if(!status.isInstalled()) {
				serverStatus = Status.未部署;
			} else {
				if(!status.isRunning()) {
					serverStatus = Status.未启动;
				} else {
					serverStatus = Status.运行中;
				}
			}
		}
		resultList.add(new ServerInfo(server,serverStatus));
	}
//	out.print(JsonUtil.jsonFromObject((resultList)));	
//	out.print(JsonUtil.jsonFromObject(new TransferObject(resultList)));
	for (ServerInfo serverInfo : resultList) {
		out.print(serverInfo.toTransferString());
	}
%><%!
	class ServerInfo{
		private String serverName;
		private String serverId;
		private String serverUrl;
		private String dbUrl;
		private Status status;
		private String port;
		
		ServerInfo (Server server,Status status){
			this.serverName = server.getName();
			this.serverId = server.getServerid();
			String tempUrl = server.getSavingNotifyUrl() != null && server.getSavingNotifyUrl().indexOf("saving_notifier") >0 ? server.getSavingNotifyUrl().substring(0,server.getSavingNotifyUrl().indexOf("saving_notifier")) : "";
			if (tempUrl.indexOf("http://") > -1) {
				try {
					this.port = tempUrl.substring(tempUrl.lastIndexOf(":") + 1 , tempUrl.length() - 1);
					this.serverUrl = tempUrl.substring(tempUrl.indexOf("http://") + 7  , tempUrl.lastIndexOf(":"));
				} catch(Exception e) {
					this.port = "error";
					this.serverUrl = "error";
				}
			}
			String []tempDBURL = server.getDburi() != null && server.getDburi().indexOf("@") > 0 ? server.getDburi().substring(server.getDburi().indexOf("@") + 1, server.getDburi().length()).split(":") : null;
			this.dbUrl = tempDBURL == null ? "--" : tempDBURL[0];
			this.status = status;
		}
		
		public String toTransferString(){
			StringBuffer sbf = new StringBuffer();
			sbf.append(serverName);
			sbf.append("|");
			sbf.append(serverId);
			sbf.append("|");
			sbf.append(serverUrl);
			sbf.append("|");
			sbf.append(dbUrl);
			sbf.append("|");
			sbf.append(status);
			sbf.append("|");
			sbf.append(port);
			sbf.append("\n");
			return sbf.toString();
		}
	}
	enum Status {
		运行中,未启动,未部署,未响应;
	}
	class TransferObject {
		ArrayList<ServerInfo> serverList ;
		public TransferObject(){}
		public TransferObject (ArrayList<ServerInfo> serverList) {
			this.serverList = serverList;
		}
		
	}
%>