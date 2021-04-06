<%@page import="com.fy.gamegateway.mieshi.server.MieshiServerInfo"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiServerInfoManager"%>
<%@page import="java.io.StringReader"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="com.fasterxml.jackson.core.type.TypeReference"%>
<%@page import="java.util.*"%>
<%@page import="com.xuanzhi.tools.text.JsonUtil"%>
<%@page import="com.fy.gamegateway.tools.HttpURLUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
	String queryMode = request.getParameter("queryMode");
	if (queryMode == null || "".equals(queryMode)) {
		queryMode = "默认";
	}
	QueryType queryType = QueryType.valueOf(queryMode);

	//boss上的服务器数据
	String url = "http://116.213.142.183:9110/mieshi_game_boss/admin/yunying/serversForYunying.jsp?authorize.username=overflowman&authorize.password=omygodyaguan";
	String returnString = HttpURLUtils.doPost(url, null);
	StringReader sr = new StringReader(returnString); 
	BufferedReader br = new BufferedReader(sr);
	String line = br.readLine();
	Map<String,ServerInfo> bossServerMap = new HashMap<String,ServerInfo>();//
	//List<ServerInfo> serverList = new ArrayList<ServerInfo>();
	try {
		while (line != null) {
			ServerInfo serverInfo = new ServerInfo(line.trim().split("\\|"));
			//serverList.add(serverInfo);
			bossServerMap.put(serverInfo.serverName, serverInfo);
			line = br.readLine();
		}
	} catch (Exception e) {
		
	} finally {
		try {
			if (br != null) {
				br.close();
			}
		} catch (Exception e) {
			
		}
		try {
			if (sr != null) {
				sr.close();
			}
		} catch (Exception e) {
			
		}
	}
	//网关上的服务器数据
	MieshiServerInfoManager mm = MieshiServerInfoManager.getInstance();
	String categories[] = mm.getServerCategories();
	
	Map<String,MieshiServerInfo> gateWayServerMap = new HashMap<String,MieshiServerInfo>();
	
	for(int i = 0 ; i < categories.length ; i++){
		MieshiServerInfo si[] = mm.getServerInfoSortedListByCategory(categories[i]);
		for(int j = 0 ; j < si.length ; j++){
			MieshiServerInfo s = si[j];
			if (s.getServerType() != 2) {//非测试服务器
				gateWayServerMap.put(s.getName(), s);
			}
		}
	}
	//组建丰富的信息
	List<UseFulData> dataList = new ArrayList<UseFulData>();
	
	for (Iterator<String> itor = gateWayServerMap.keySet().iterator();itor.hasNext();) {
		String serverName = itor.next();
		MieshiServerInfo gateWay = gateWayServerMap.get(serverName);
		ServerInfo boss = bossServerMap.get(serverName);
		if (gateWay != null && boss != null) {
			//nothing
		} else {
			//记录错误次数
		}
		dataList.add(new UseFulData(gateWay,boss));
	}
	
%>
<head>
<style type="text/css">
	
    body {
        font-size: 12px;
        background-color: #21b5fc;
    }
    * {
    	margin: 0px;
    	padding:0px;
    }
    table {
    	margin: auto;
        width: 90%;
    }
	td {
		padding: 5px;
		border-radius: 3px;	
		text-align: center;
	}
	
	tr {
		text-align: center;
		background-color:white;
	}
</style>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>服务器列表</title>
</head>
<body>
<hr>
<div style="float: right;width: 300;px;height: 50px;text-align: center;" >
	<form action="./serversForYunying.jsp" method="post">
		<%for (QueryType query: QueryType.values()) { 
			boolean checked = query.equals(queryType);
			%>
			 <font <%=checked ? "style='font-weight: bold;'": "" %>><%=query.des %></font><input name="queryMode" type="radio" value="<%=query.name()%>" <%=checked ? "checked":"" %>>
		<%} %>
		<input type="submit" value="查询">
	</form>
</div>
<hr>
<% int index = 0; %>
	<% switch(queryType) {
		case 默认:
				Map<Integer,List<UseFulData>> channelMap = new HashMap<Integer,List<UseFulData>>();
				for (UseFulData data : dataList) {
					int serverType = data.mieshiServerInfo.getServerType();
					if (!channelMap.containsKey(serverType)) {
						channelMap.put(serverType, new ArrayList<UseFulData>());
					}
					channelMap.get(serverType).add(data);
				}
			%>
			
			<table>
				<tr>
					<th>服务器类型</th>
					<th>索引</th>
					<th>serverId</th>
					<th>服务器名</th>
					<th>所在区</th>
					<th>服务器地址</th>
					<th>数据库地址</th>
					<th>在线人数</th>
					<th>服务状态</th>
					<th>服务器真实状态</th>
				</tr>
				<%
					for (Iterator<Integer> itor = channelMap.keySet().iterator();itor.hasNext();) {
						Integer serverType = itor.next();
						List<UseFulData> list = channelMap.get(serverType);
						for (int i = 0; i < list.size();i++) {
							UseFulData data = list.get(i);
				%>
					<tr>
						<%if (i == 0){ %>
							<td rowspan="<%=list.size()%>">
								<%=MieshiServerInfo.SERVERTYPE_NAMES[serverType] %>[<%=list.size() %>]
							</td>
						<%} %>
						<td><%=++index %></td>
						<td><%=data.serverInfo == null ? "--" : data.serverInfo.serverId %></td>
						<td><a target="_blank" href="<%=data.mieshiServerInfo.getServerUrl()%>"><%=data.mieshiServerInfo.getName() %></a></td>
						<td><%=data.mieshiServerInfo.getCategory()%></td>
						<td><%=data.serverInfo == null ? "--" : data.serverInfo.serverUrl %></td>
						<td><%=data.serverInfo == null ? "--" : data.serverInfo.dbUrl %></td>
						<td><%=data.mieshiServerInfo.getOnlinePlayerNum() %></td>
						<td><%=MieshiServerInfo.STATUS_DESP[data.mieshiServerInfo.getStatus()] %></td>
						<td><%=data.serverInfo == null ? "--" : data.serverInfo.status %></td>
					</tr>
				<%	}
				} %>
			</table>
			
			<%
			break;
		case 数据库:
			Map<String,List<UseFulData>> dbMap = new HashMap<String,List<UseFulData>>();
			for (UseFulData data : dataList) {
				String dbIP = data.serverInfo == null ? "" : data.serverInfo.dbUrl;
				if (!dbMap.containsKey(dbIP)) {
					dbMap.put(dbIP, new ArrayList<UseFulData>());
				}
				dbMap.get(dbIP).add(data);
			}
		%>
		
		<table>
			<tr>
				<th>数据库地址</th>
				<th>索引</th>
				<th>serverId</th>
				<th>服务器名</th>
				<th>服务器类型</th>
				<th>所在区</th>
				<th>服务器地址</th>
				<th>在线人数</th>
				<th>服务状态</th>
				<th>服务器真实状态</th>
			</tr>
			<%
				for (Iterator<String> itor = dbMap.keySet().iterator();itor.hasNext();) {
					String ip = itor.next();
					List<UseFulData> list = dbMap.get(ip);
					for (int i = 0; i < list.size();i++) {
						UseFulData data = list.get(i);
			%>
				<tr>
					<%if (i == 0){ %>
						<td rowspan="<%=list.size()%>">
						<%=data.serverInfo == null ? "--" : data.serverInfo.dbUrl %>[<%=list.size() %>]
						</td>
					<%} %>
					<td><%=++index %></td>
					<td><%=data.serverInfo == null ? "--" : data.serverInfo.serverId %></td>
					<td><a target="_blank" href="<%=data.mieshiServerInfo.getServerUrl()%>"><%=data.mieshiServerInfo.getName() %></a></td>
					<td><%=MieshiServerInfo.SERVERTYPE_NAMES[data.mieshiServerInfo.getServerType()] %></td>
					<td><%=data.mieshiServerInfo.getCategory()%></td>
					<td><%=data.serverInfo == null ? "--" : data.serverInfo.serverUrl %></td>
					<td><%=data.mieshiServerInfo.getOnlinePlayerNum() %></td>
					<td><%=MieshiServerInfo.STATUS_DESP[data.mieshiServerInfo.getStatus()] %></td>
					<td><%=data.serverInfo == null ? "--" : data.serverInfo.status %></td>
				</tr>
			<%	}
			} %>
		</table>
		
		<%
		break;
		case 物理服务:
			Map<String,List<UseFulData>> serverMap = new HashMap<String,List<UseFulData>>();
			for (UseFulData data : dataList) {
				String serverUrl = data.serverInfo == null ? "" : data.serverInfo.serverUrl;
				if (!serverMap.containsKey(serverUrl)) {
					serverMap.put(serverUrl, new ArrayList<UseFulData>());
				}
				serverMap.get(serverUrl).add(data);
			}
		%>
		
		<table>
			<tr>
				<th>服务器地址</th>
				<th>索引</th>
				<th>serverId</th>
				<th>服务器名</th>
				<th>服务器类型</th>
				<th>所在区</th>
				<th>数据库地址</th>
				<th>在线人数</th>
				<th>服务状态</th>
				<th>服务器真实状态</th>
			</tr>
			<%
				for (Iterator<String> itor = serverMap.keySet().iterator();itor.hasNext();) {
					String ip = itor.next();
					List<UseFulData> list = serverMap.get(ip);
					for (int i = 0; i < list.size();i++) {
						UseFulData data = list.get(i);
			%>
				<tr>
					<%if (i == 0){ %>
						<td rowspan="<%=list.size()%>">
						<%=data.serverInfo == null ? "--" : data.serverInfo.serverUrl %>[<%=list.size() %>]
						</td>
					<%} %>
					<td><%=++index %></td>
					<td><%=data.serverInfo == null ? "--" : data.serverInfo.serverId %></td>
					<td><a target="_blank" href="<%=data.mieshiServerInfo.getServerUrl()%>"><%=data.mieshiServerInfo.getName() %></a></td>
					<td><%=MieshiServerInfo.SERVERTYPE_NAMES[data.mieshiServerInfo.getServerType()] %></td>
					<td><%=data.mieshiServerInfo.getCategory()%></td>
					<td><%=data.serverInfo == null ? "--" : data.serverInfo.dbUrl %></td>
					<td><%=data.mieshiServerInfo.getOnlinePlayerNum() %></td>
					<td><%=MieshiServerInfo.STATUS_DESP[data.mieshiServerInfo.getStatus()] %></td>
					<td><%=data.serverInfo == null ? "--" : data.serverInfo.status %></td>
				</tr>
			<%	}
			} %>
		</table>
		
		<%
		break;
	} %>
</body>
</html>
<%!

	class UseFulData {
		MieshiServerInfo mieshiServerInfo;
		ServerInfo serverInfo;
		
		public UseFulData (MieshiServerInfo mieshiServerInfo,ServerInfo serverInfo) {
			this.mieshiServerInfo = mieshiServerInfo;
			this.serverInfo = serverInfo;
		}
	}
	class ServerInfo{
		String serverName;
		String serverId;
		String serverUrl;
		String dbUrl;
		Status status;
		String port;
		public ServerInfo (String serverName,String serverId,String serverUrl,String dbUrl,Status status,String port) {
			this.serverName = serverName;
			this.serverId = serverId; 
			this.serverUrl = serverUrl;
			this.dbUrl = dbUrl;
			this.status = status;
			this.port = port;
		}
		public ServerInfo(String[] transferValues ){
			this(transferValues[0],transferValues[1],transferValues[2],transferValues[3],getStatus(transferValues[4]),transferValues[5]);
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
	
	enum QueryType {
		默认("按照服务器类型"),
		物理服务("按照物理服务器"),
		数据库("按照数据库服务器"),
		;
		QueryType (String des){ this.des = des;}
		String des;
	}
	
	Status getStatus(String name) {
		return Status.valueOf(name);
	}
	
	class TransferObject {
		ArrayList<ServerInfo> serverList ;
		public TransferObject(){}
		public TransferObject (	ArrayList<ServerInfo> serverList) {
			this.serverList = serverList;
		}
		
		public void setServerList(ArrayList<ServerInfo> serverList) {
			this.serverList = serverList;
		}
		
		public ArrayList<ServerInfo> getServerList(){
			return this.serverList;
		}
	}
%>