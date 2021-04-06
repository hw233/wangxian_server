<%@page import="com.fy.boss.gm.dbserver.DbServerManager"%>
<%@page import="com.fy.boss.gm.dbserver.DbServer"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.boss.game.service.ServerManager"%>
<%@page import="com.fy.boss.game.model.Server"%>
<%@ page contentType="text/html;charset=utf-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>服务器列表</title>
		<link rel="stylesheet" href="../../admin/css/style.css" />
		<link rel="stylesheet" href="../../admin/css/atalk.css" />
	<script language="JavaScript">
		function overTag(tag){
			tag.style.color = "red";	
			tag.style.backgroundColor = "#E9E4E4";
		}
					
		function outTag(tag){
			tag.style.color = "black";
			tag.style.backgroundColor = "white";
		}
		
		function addNewJiGui(){
			var recorder = document.getElementById('recorder').value;
			var jiguiname = document.getElementById('jiguiname').value;
			var jiguimess = document.getElementById('jiguimess').value;
			var recordtime = document.getElementById('recordtime').value;
			window.location.replace("?recorder="+recorder+"&jiguiname="+jiguiname+"&jiguimess="+jiguimess+"&recordtime="+recordtime);
		}
		
		function changemess(name,id){
			var obj = document.getElementById(name); //selectid
			var index = obj.selectedIndex; // 选中索引
			var text = obj.options[index].text; // 选中文本
			window.location.replace("?selectservername="+name+"&selectservervalue="+text+"&serverid="+id);
		}
		
		function deleteserver(id){
			window.location.replace("?delid="+id);
		}
	</script>
	</head>
	<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0">
		<h1>
			服务器列表
		</h1>
	<%
		ServerManager smanager = ServerManager.getInstance();
		List<Server> servers = smanager.getServers();
		String recorder = request.getParameter("recorder");
		String jiguiname = request.getParameter("jiguiname");
		String jiguimess = request.getParameter("jiguimess");
		String recordtime = request.getParameter("recordtime");
		
		String selectservername = request.getParameter("selectservername");
		String selectservervalue = request.getParameter("selectservervalue");
		String serverid = request.getParameter("serverid");
		if(selectservername!=null && selectservervalue!=null && serverid!=null){
			Server server = smanager.getServer(Long.parseLong(serverid));
			server.setJiguiname(selectservervalue);
			smanager.updateServer(server);
// 			out.print("name:"+server.getName()+"--Dburi:"+server.getDburi());
		}
		DbServerManager dbmanager = DbServerManager.getInstance();
		
		if(recorder!=null && jiguiname!=null && jiguimess!=null &&recordtime!=null){
			DbServer dbserver = new DbServer();
			dbserver.setDbmess(jiguimess);
			dbserver.setRecorder(recorder);
			dbserver.setDbname(jiguiname);
			dbserver.setRecordtime(recordtime);
			dbmanager.addNewDbServer(dbserver);
		}
		
		String delid = request.getParameter("delid");
		if(delid!=null && delid.trim().length()>0){
			dbmanager.deldbservers(Long.parseLong(delid));
		}		
		List<DbServer> list = dbmanager.getdbservers();
// 		out.print("list:"+list.size());
	%>
	<br>
	<table>
		<%
			if(list!=null && list.size()>0){
				for(DbServer ss:list){
		%>
				<tr onmouseover = "overTag(this);" onmouseout = "outTag(this);" title="<%=ss.getDbmess()%>"><td><%=ss.getDbname() %></td><td colspan="3"><table><tr><th>游戏名称</th><th>服务器名称</th><th>服务器参数</th><th>服务器ip</th></tr>	
				<%
					if(servers!=null && servers.size()>0){
						for(Server sss:servers){
							if(sss.getJiguiname()!=null && sss.getJiguiname().trim().length()>0 && sss.getJiguiname().equals(ss.getDbname())){
								%>
								<tr><td>飘渺寻仙曲</td><td><%=sss.getName() %></td><td>数据库URI:<%=sss.getDburi() %>/<%=sss.getDbusername()%>/<%=sss.getDbpassword()%><br>
						RESIN_HOME:<%=sss.getResinhome() %><br></td><td><%=sss.getGameipaddr() %></td></tr>
								<%
							}
						}
					}
				%>	
				</table></td></tr>
		<% 			
				}
			}
		%>
	</table>
	<hr>
		<h2>没有配置机柜的服务器列表如下：</h2>
		<form action="server_db_list.jsp" method=post name=f1>
			<table id="test1" align="center" class="sortable-onload-5-6r rowstyle-alt colstyle-alt no-arrow">
				<tr>
					<th>
						<b>游戏名称</b>
					</th>
					<th>
						<b>服务器名称</b>
					</th>
					<th>
						<b>服务器参数</b>
					</th>
					<th>服务器ip</th>
					<th>
						<b>操作</b>
					</th>
				</tr>
				<%for(Server server:servers) {
				 %>
				<tr onmouseover = "overTag(this);" onmouseout = "outTag(this);">
					<td align="center">
						飘渺寻仙曲
					</td>
					<td align="center">
						<%=server.getName()%>
					</td>
					<td align="center">
						数据库URI:<%=server.getDburi() %>/<%=server.getDbusername()%>/<%=server.getDbpassword()%><br>
						RESIN_HOME:<%=server.getResinhome() %><br>
					</td>
					<td><%=server.getGameipaddr() %></td>
					<td>
						<select id = '<%=server.getName() %>'name='<%=server.getName() %>'>
						<%
							if(server.getJiguiname()!=null && server.getJiguiname().trim().length()>0){
						%>
							<option><%=server.getJiguiname() %></option>
							<%
							if(list!=null && list.size()>0){
								for(DbServer jigui : list){
							%>
										<option><%=jigui.getDbname() %></option>
							<%	
									}
							}
							%>
						<%		
							}else{
						%>
						<option>--</option>
						<%
							if(list!=null && list.size()>0){
								for(DbServer jigui : list){
						%>
									<option><%=jigui.getDbname() %></option>
						<%	
								}
							}}
						%>
						</select>
						<input type="button" onclick='changemess("<%=server.getName() %>","<%=server.getId() %>")' value='确定'>
					</td>
					
				</tr>
				<%} %>
			</table>
		</form>
		<table>
			<h3>配置新的机柜</h3>
			<hr>
			<%
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			%>
			<tr><th>配置人：</th><td><input type='text' id='recorder'/></td></tr>
			<tr><th>机柜名称：</th><td><input type='text' id='jiguiname'/></td></tr>
			<tr><th>机柜描述：</th><td><input type='text' id='jiguimess'/></td></tr>
			<tr><th>配置时间：</th><td><input type="hidden" id='recordtime' value='<%=sdf.format(System.currentTimeMillis()) %>'/><%=sdf.format(System.currentTimeMillis()) %></td></tr>
			<tr><td colspan='2'><input type='button' value='确定' onclick="addNewJiGui()"/></td></tr>
		</table>
		<hr>
		   <h3>机柜信息</h3>
		   <table>
		   		<tr><th>机柜名称</th><th>添加人</th><th>添加时间</th><th>机柜描述</th><th>操作</th></tr>
		   			<%
		   				if(list!=null && list.size()>0){
							for(DbServer dd:list){
								%>
								<tr><td><%=dd.getDbname() %></td><td><%=dd.getRecorder() %></td><td><%=dd.getRecordtime() %></td><td><%=dd.getDbmess() %></td><td><input type='button' value='删除' onclick='deleteserver("<%=dd.getId()%>")'></td></tr>
								<%
							}		   					
		   				}
		   			%>
		   </table>
	</body>
</html>
 
