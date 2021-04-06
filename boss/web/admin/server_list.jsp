<%@ page contentType="text/html;charset=utf-8"%><%@page import="java.util.*,java.io.*,
				com.xuanzhi.tools.text.*,
				com.fy.boss.authorize.model.*,
				com.fy.boss.authorize.service.*,
				com.fy.boss.authorize.exception.*,
				com.fy.boss.finance.model.*,
				com.fy.boss.finance.service.*,
				com.fy.boss.game.model.*,
				com.fy.boss.deploy.*,
				com.fy.boss.cmd.*,com.fy.boss.game.service.*"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>服务器列表</title>
		<link rel="stylesheet" href="css/style.css" />
		<link rel="stylesheet" href="css/atalk.css" />
		<script language="JavaScript">
<!--
function selectGame() {
	document.fgame.submit();
}

function openwin(url) {
	window.open(url, '_blank', 'height=700, width=900, top=20, left=60, toolbar=no, menubar=no, scrollbars=auto,resizable=no,location=no, status=no');
}

function $(id){
	return document.getElementById(id);
}

function operate() {
	over(2);
}

function unover(index)
{
  	document.getElementById("overlay").style.display="none";
  	document.getElementById("lightbox"+index).style.display="none";
  	document.getElementById("lbody"+index).innerHTML = lbodyOrig;
}

function over(index)
{
	var obj = $('overlay');
 	obj.style.display="block";
	
	if(document.body.offsetHeight>=document.documentElement.clientHeight){
		obj.style.height = document.body.offsetHeight + 'px'; 
	}else{
		obj.style.height = document.documentElement.clientHeight + 'px';
	}
	var currentOpacity = 0;
	//设置定时器timer1
	var timer1 = setInterval(
		function(){
			if(currentOpacity<=50){
				setOpacity(obj,currentOpacity);
				currentOpacity+=1;	
			}
			else{
				clearInterval(timer1);
			}
		}
	,10);
	
	document.getElementById("lightbox"+index).style.display="block";
}

//设置透明度的函数
function setOpacity(elem,current){
	//如果是ie浏览器
	if(elem.filters){ 
		elem.style.filter = 'alpha(opacity=' + current + ')';
	}else{ //否则w3c浏览器
		elem.style.opacity = current/100;
	}
}

function subcheck() {
	f1.submit();
}
function del(id) {
	if(window.confirm("你是否确定要删除此服务器!!!")) {
		if(window.confirm("再次确定要删除此服务器!!!")) {
			window.location.replace('server_delete.jsp?id=' + id);
		}
	}
}
function change(id){
   window.location.replace('server_update.jsp?id='+id);
}

function overTag(tag){
	tag.style.color = "red";	
	tag.style.backgroundColor = "#E9E4E4";
}
			
function outTag(tag){
	tag.style.color = "black";
	tag.style.backgroundColor = "white";
}

function publish(serverid, project) {
	over(2);
	location.replace("?pub=true&sid=" + serverid + "&project=" + project);
}

function publishCache(serverid) {
	over(2);
	location.replace("?pubcache=true&sid=" + serverid);
}

function redeploy(serverid, project) {
	if(window.confirm("完整部署将清除全部老数据！你是否确认？？")) {
		if(window.confirm("再次确认完整部署，清空服务器老数据？？")) {
			over(2);
			location.replace("?redeploy=true&sid=" + serverid + "&project=" + project);
		}
	}
}

function deploy(serverid, project) {
	if(window.confirm("你是否确认要执行一般部署？？")) {
		over(2);
		location.replace("?deploy=true&sid=" + serverid + "&project=" + project);
	}
}

function closeDeployMod(divid) {
	var dd = document.getElementById(divid);
	dd.style.display = "none";
}

function deployInitConfig(sid) {
	if(window.confirm("发布资源文件！你是否确认？？")) {
		if(window.confirm("再次确认发布？？")) {
			over(2);
			location.replace("?deployInitConfig=true&sid=" + sid);
		}
	}
}

function openDeployMod(divid) {
	var dd = document.getElementById(divid);
	dd.style.display = "block";
}

function deployMod(fmid) {
	if(window.confirm("确认需要部署配置吗？")) {
		over(2);
		document.f1.submit();
	}
}

function startServer(sid) {
	over(2);
	location.replace("?start=true&sid=" + sid);
}

function stopServer(sid) {
	if(window.confirm("确认停止服务器？？")) {
		if(window.confirm("再次确认停止服务器？？")) {
			over(2);
			location.replace("?stop=true&sid=" + sid);
		}
	}
}
-->
</script>
	</head>
	<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0">
		<h1>
			服务器列表
		</h1>
		<%!
		public  byte[] hexStringToBytes(String hexString) {  
		    if (hexString == null || hexString.equals("")) {  
		        return null;  
		    }  
		    hexString = hexString.toUpperCase();  
		    int length = hexString.length() / 2;  
		    char[] hexChars = hexString.toCharArray();  
		    byte[] d = new byte[length];  
		    for (int i = 0; i < length; i++) {  
		        int pos = i * 2;  
		        d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));  
		    }  
	     return d;  
		 }  
	  private  byte charToByte(char c) {  
		      return (byte) "0123456789ABCDEF".indexOf(c);  
		  }  
		 %>
		
		<%
	String message = "";
	ServerManager smanager = ServerManager.getInstance();
	List<Server> servers = smanager.getServers();
	CMDService cmdService = CMDService.getInstance(); 
	ProjectManager pm = ProjectManager.getInstance(); 
	List<Project> pjs = new ArrayList<Project>();
	if(pm != null) {
		pjs = pm.getProjects("飘渺寻仙曲"); 
		for(Project p:pjs){
			out.print(p.getName()+"<br>");
		}
	}
	String pub = request.getParameter("pub");
	if(pub != null && pub.equals("true") ) {
		String sid = request.getParameter("sid");
		Server server = smanager.getServer(Long.parseLong(sid));
		Project pj = pm.getProject(request.getParameter("project"));
		boolean succ = pm.publish(pj, server);
		if(succ) {
			message = "发布《"+pj.getName()+"》成功!";
		} else {
			message = "发布《"+pj.getName()+"》失败，请联系相关负责人!";
		}
	}
	
	String redeploy = request.getParameter("redeploy");
	if(redeploy != null && redeploy.equals("true") ) {
		String sid = request.getParameter("sid");
		Server server = smanager.getServer(Long.parseLong(sid));
		Project pj = pm.getProject(request.getParameter("project"));
		boolean succ = pm.publish(pj, server);
		if(succ) {
			succ = pm.deploy(pj, server);
			if(succ) {
				message = "完整部署《"+pj.getName()+"》成功!";
			} else {
				message = "完整部署《"+pj.getName()+"》失败，请联系相关负责人!";
			}
		} else {
			message = "完整部署《"+pj.getName()+"》失败，请联系相关负责人!";
		}
	}
	
	String deploy = request.getParameter("deploy");
	if(deploy != null && deploy.equals("true") ) {
		String sid = request.getParameter("sid");
		Server server = smanager.getServer(Long.parseLong(sid));
		Project pj = pm.getProject(request.getParameter("project"));
		boolean succ = pm.publish(pj, server);
		if(succ) {
			List<Module> ms = pj.getCommonModules();
			StringBuffer sb = new StringBuffer();
			Module mods[] = ms.toArray(new Module[0]);
			succ = pm.deploy(pj, mods, server, new DeployTask());
			if(!succ) {
				sb.append("1一般部署《"+pj.getName()+"》失败，请联系相关负责人#");
			}
			if(sb.toString().length() == 0) {
				message = "一般部署《"+pj.getName()+"》部署成功!";
			} else {
				message = sb.toString();
			}
		} else {
			message = "2一般部署《"+pj.getName()+"》失败，请联系相关负责人!";
		}
	}
	
	String start = request.getParameter("start");
	if(start != null && start.equals("true")) {
		String sid = request.getParameter("sid");
		Server server = smanager.getServer(Long.parseLong(sid));
		boolean succ = cmdService.startServer(server);
		if(succ) {
			message = "服务器启动成功!";
		} else {
			message = "服务器启动失败，请联系相关负责人!!!!";
		}
	}
	
	String stop = request.getParameter("stop");
	if(stop != null && stop.equals("true")) {
		String sid = request.getParameter("sid");
		Server server = smanager.getServer(Long.parseLong(sid));
		boolean succ = cmdService.stopServer(server);
		if(succ) {
			message = "服务器关闭成功!";
		} else {
			message = "服务器关闭失败，请联系相关负责人!!!!";
		}
	}
	
	if(message.length() > 0) {
		out.println("<script>var a='"+message+"'; window.alert(a.replace(/#/g,'\\n'));</script>");
	}
	%>
	<br>
		<form action="server_list.jsp" method=post name=f1>
			<table id="test1" align="center" width="99%" cellpadding="0"
				cellspacing="0" border="0"
				class="sortable-onload-5-6r rowstyle-alt colstyle-alt no-arrow">
				<tr >
					<th align=center width=10%>
						<b>游戏名称</b>
					</th>
					<th align=center width=10%>
						<b>服务器名称</b>
					</th>
					<th align=center width=30%>
						<b>服务器参数</b>
					</th>
					<th align=center width=10%>
						<b>服务器状态</b>
					</th>
					<th align=center width=7%>
						<b>编辑</b>
					</th>
					<th align=center width=8%>
						<b>发布部署</b>
					</th>
					<th align=center width=25%>
						<b>高级选项</b>
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
						服务器地址:<%=server.getGameipaddr()+":"+server.getGameport() %><br>
						数据库URI:<%=server.getDburi() %>/<%=server.getDbusername()%>/<%=server.getDbpassword()%><br>
						RESIN_HOME:<%=server.getResinhome() %><br>
						充值通知地址:<%=server.getSavingNotifyUrl() %><br>
						服务器serverId:<%=server.getServerid() %>
					</td>
					<td align="center">
					<%
					ServerStatus status = pm.getServerStatus(server);
					if(!status.isAgenton()) {
						out.println("Agent无响应");
					} else {
						if(!status.isInstalled()) {
							out.println("未部署服务器");
						} else {
							if(!status.isRunning()) {
								out.println("未启动服务器<br><img src='images/start.png' title='启动' style='cursor:pointer;' onclick=\"startServer('"+server.getId()+"')\">");
							} else {
								out.println("服务器运行中<br><img src='images/stop.png' title='关闭' style='cursor:pointer;' onclick=\"stopServer('"+server.getId()+"')\">");
							}
						}
					}
					 %>
					 <input type="button" name=bt10 value="查看日志" onclick="openwin('viewlog.jsp?sid=<%=server.getId() %>')">
					</td>
					<td align="center" style="padding-left:10px;">
						<input type=button name=bt1 value=" 更新 " onclick="change('<%=server.getId() %>')"><br>
						<input type=button name=bt1 value=" 删除 " onclick="del('<%=server.getId() %>');" >
					</td>
					<td align="center" style="padding-left:10px;">
						<input type=button name=bt1 value="发布资源" onclick="deployInitConfig('<%=server.getId() %>')"/><br>
					</td>
					<td align="center" style="padding-left:10px;background:#E9F8CC;">
						<%
						for(Project pj : pjs) {
							String pjName=pj.getName();
							pjName=pjName.replaceAll("&","&amp;");
							ProjectManager.logger.info(pj.getName()+"----------->"+pjName);
						%>
						<input type=button name=bt1 value="发布项目<%=pjName %>" onclick="publish('<%=server.getId() %>', '<%=pjName %>')"/><br>
						<%if(status.isInstalled()) {%>
						<input type=button name=bt1 value="一般部署<%=pjName %>" onclick="deploy('<%=server.getId() %>', '<%=pjName %>')"/><br>
						<%} else { %>
						<input type=button name=bt2 value="完整部署<%=pjName %>" onclick="redeploy('<%=server.getId() %>', '<%=pjName %>')"/><br><br>
						<%} %>
						<hr><br>
						<%} %>
					</td>
				</tr>
				<%} %>
			</table>
			<input type=button name=bt2 value="新增服务器"
				onclick="location.replace('server_new.jsp')" />
		</form>
		<div id="overlay"></div>
		<div id="lightbox2">
	     <div class="lbody2" id="lbody2">
	       		正在处理，请耐心等候...<br>
	       		<img src='images/process.gif'> 
	     </div>
		</div>
	</body>
</html>
 
