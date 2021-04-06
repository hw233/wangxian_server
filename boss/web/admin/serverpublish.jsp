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
<%
String game = request.getParameter("game");
if(game == null) {
	game = "寻仙曲";
}
ServerManager smanager = ServerManager.getInstance();
List<Server> servers = smanager.getServers();
ProjectManager pm = ProjectManager.getInstance(); 
%>
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh-cn">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>发布服务器</title>
		<link rel="stylesheet" href="css/style.css" />
		<link rel="stylesheet" href="css/atalk.css" />
		<script type='text/javascript' src='js/jquery-1.6.2.js'></script>
		<script type="text/javascript" src="js/progress.js"></script>
		<script type="text/javascript" src="/dwr/engine.js"></script>
		<script type='text/javascript' src='/dwr/interface/DeployBoundry.js'></script>
<script language="JavaScript">		
<!--

function selectGame() {
	document.fgame.submit();
}
	
var progress_key = '4d462e9bb01a9';

$(document).ready(function() {
<%
for(int i=0; i<servers.size(); i++) {
%>
	$("#pb<%=servers.get(i).getId()%>").progressBar();
<%
}%>
});

function overTag(tag){
	tag.style.color = "red";	
	tag.style.backgroundColor = "#E9E4E4";
}
			
function outTag(tag){
	tag.style.color = "black";
	tag.style.backgroundColor = "white";
}

function openwin(url) {
	window.open(url, '_blank', 'height=700, width=900, top=20, left=60, toolbar=no, menubar=no, scrollbars=auto,resizable=no,location=no, status=no');
}

function deployServer(sid, name) {
	if(window.confirm("发布服务器《"+name+"》！你是否确认？？")) {
		if(window.confirm("再次确认发布《"+name+"》？？")) {
			DeployBoundry.deploy(sid);
		}
	}
}

function confirmSucc(sid) {
	DeployBoundry.confirmSucc(sid);
}

function startServer(sid) {
	if(window.confirm("确认启动服务器？？")) {
		DeployBoundry.startServer(sid);
	}
}

function removeDeploy(sid) {
	DeployBoundry.removeDeploy(sid);
}

function stopServer(sid) {
	if(window.confirm("确认停止服务器？？")) {
		if(window.confirm("再次确认停止服务器？？")) {
			DeployBoundry.stopServer(sid);
		}
	}
}

var time = 1000;
window.setInterval('CheckStatus()',time); 

function CheckStatus() {
	//alert("checking");
	DeployBoundry.getAllServerStatus(afterCheck);
}
	
function afterCheck(status) {
	for(var i=0; i<status.length; i++) {
		var str = status[i].split("__");
		if(str.length == 2) {
			var id = str[0];
			var perc = str[1];
			//alert("id:"+id+"===perc:"+perc);
			if(perc == "-2") {
				$("#deploy-" + id).css('display','block');
				$("#pb" + id).css('display','none');
				$("#failed-" + id).css('display','none');
				$("#succ-" + id).css('display','none');
			} else if(perc == "-1") {
				$("#deploy-" + id).css('display','none');
				$("#pb" + id).css('display','none');
				$("#failed-" + id).css('display','block');
				$("#succ-" + id).css('display','none');
			} else if(perc == "100") {
				$("#deploy-" + id).css('display','none');
				$("#pb" + id).css('display','none');
				$("#failed-" + id).css('display','none');
				$("#succ-" + id).css('display','block');
			} else {
				$("#deploy-" + id).css('display','none');
				$("#pb" + id).css('display','block');
				$("#failed-" + id).css('display','none');
				$("#succ-" + id).css('display','none');
				var old = $("#pb" + id).innerHTML;
				if(old != (perc+"%")) {
					$("#pb" + id).progressBar(perc);
				}
			}
		}
	}
}	

CheckStatus();

-->
</script>
<style>
</style>
	</head>
	<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0">
		<h1>
			发布游戏服务器
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
	<br>
		<form name="fgame" action="server_list.jsp" method="get">
			<select name="game" onchange="selectGame()">
				
				<option value="">...</option>
			</select>
		</form>
	<br>
		<form action="server_list.jsp" method=post name=f1>
		<table align="center" width="99%" cellpadding="0"
                                cellspacing="0" border="0"
                                class="sortable-onload-5-6r rowstyle-alt colstyle-alt no-arrow">
                 <tr >
                                        <th align=center width=15%>
                                                <b>游戏名称</b>
                                        </th>
                                        <th align=center width=15%>
                                                <b>服务器名称</b>
                                        </th>
                                        <th align=center width=20%>
                                                <b>服务器状态</b>
                                        </th>
                                        <th align=center width=20%>
                                                <b>日志</b>
                                        </th>
                                        <th align=center width=20%>
                                                <b>发布部署</b>
                                        </th>
                                </tr>
                                
                 <%for(Server server:servers) {
// 				 	Game Ogame = gmanager.getGame(server.getGameid());
				 %>
				<tr onmouseover = "overTag(this);" onmouseout = "outTag(this);">
					<td align="center">
						寻仙曲
					</td>
					<td align="center">
						<%=server.getName()%>(<%=server.getResinhome() %>)
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
								out.println("未启动服务器<br><img src='/images/start.png' title='启动' style='cursor:pointer;' onclick=\"startServer('"+server.getId()+"')\">");
							} else {
								out.println("服务器运行中<br><img src='/images/stop.png' title='关闭' style='cursor:pointer;' onclick=\"stopServer('"+server.getId()+"')\">");
							}
						}
					}
					 %>
					</td>
					<td align="center">
						<input type="button" name=bt10 value="查看日志" onclick="openwin('viewlog.jsp?sid=<%=server.getId() %>')">
					</td>
					<td align="center" style="padding-left:10px;">
						<div id="deploy-<%=server.getId() %>">
							<input type=button name=bt1 value="发布此服务器" onclick="deployServer('<%=server.getId() %>','<%=server.getName() %>')"/><br>
						</div>
						<span class="progressBar" id="pb<%=server.getId() %>">0%</span>
						<div id="failed-<%=server.getId() %>">发布失败<input type=button name=bt1 value="清除此发布任务" onclick="removeDeploy('<%=server.getId() %>')"/></div>
						<div id="succ-<%=server.getId() %>">发布成功<input type=button name=bt1 value="确认" onclick="confirmSucc('<%=server.getId() %>')"/></div>
					</td>
				</tr>
				<%} %>
          </table>                      
				
		</form>
		<br><br>
		<div id="overlay"></div>
		<div id="lightbox2">
	     <div class="lbody2" id="lbody2">
	       		正在处理，请耐心等候...<br>
	       		<img src='images/process.gif'> 
	     </div>
		</div>
	</body>
</html>
 
