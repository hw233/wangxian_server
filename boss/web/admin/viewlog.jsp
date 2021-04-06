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
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh-cn">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>查看服务器日志</title>
		<link rel="stylesheet" href="css/style.css" />
		<link rel="stylesheet" href="css/atalk.css" />
		<script language="JavaScript">
<!--

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
	document.getElementById("overlay").style.height = document.body.clientHeight;
	document.getElementById("overlay").style.width = document.body.clientWidth;	
	document.getElementById("lightbox"+index).style.display="block";
 	document.getElementById("overlay").style.display="block";
}

function subcheck() {
	f1.submit();
}

function view() {
	over(2);
	f1.submit();
}
-->
</script>
	</head>
	<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0">
		<%
	String message = "";
	ServerManager smanager = ServerManager.getInstance();
	List<Server> servers = smanager.getServers();
	CMDService cmdService = CMDService.getInstance(); 
	ProjectManager pm = ProjectManager.getInstance(); 
	Project pj = pm.getProject("game_server"); 
	String sid = request.getParameter("sid");
	String logs[] = new String[]{"stdout.log","stderr.log","game_server/gateway.log"
								,"game_server/game.log","game_server/core.log","game_server/connection.log"
								,"game_server/selector.log"};
	String logname = request.getParameter("logname");
	String logcontent = "";
	if(logname != null) {
		Server server = smanager.getServer(Long.parseLong(sid));
		int linenum = Integer.parseInt(request.getParameter("linenum"));
		String logpath = server.getResinhome() + "/log/" + logname;
		logcontent = cmdService.getServerLog(server.getGameipaddr(), logpath, linenum);
	}
	%>
		<form action="" method=post name=f1>
			<table id="test1" align="center" width="100%" cellpadding="0"
				cellspacing="0" border="0"
				class="sortable-onload-5-6r rowstyle-alt colstyle-alt no-arrow">
				<tr >
					<th align=center width=10%>
						<b>日志名称</b>
					</th>
					<td align="left">
						<select name="logname">
							<%for(int i=0; i<logs.length; i++) {%>
							<option value="<%=logs[i] %>" <%if(logname != null && logs[i].equals(logname)) out.print("selected");%>><%=logs[i] %></option>
							<%} %>
						</select>
						行数:<input type="text" name=linenum value="200" size=5>
						<input type="hidden" name=sid value="<%=sid %>">
						<input type=button name=bt2 value=" 查 询 " onclick="view()" />
					</td>
				</tr>
				<tr>
					<td align="left" colspan="2">
						<textarea name=logtext id=logtext cols="140" rows=44 style="font-size:12px;"><%=logcontent %></textarea>
					</td>
				</tr>
			</table>
		</form>
		<div id="overlay"></div>
		<div id="lightbox2">
	     <div class="lbody2" id="lbody2">
	       		正在处理，请耐心等候...<br>
	       		<img src='images/process.gif'> 
	     </div>
		</div>
		<script>
			var obj = document.getElementById("logtext");
			obj.scrollHeight = obj.height;
		</script>
	</body>
</html>
 
