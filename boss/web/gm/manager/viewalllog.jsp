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
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh-cn">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>查看服务器日志</title>
		<link rel="stylesheet" href="../css/style.css" />
		<link rel="stylesheet" href="../css/atalk.css" />
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
	String linenumS = request.getParameter("linenum");
// 	if(logname != null) {
// 		Server server = smanager.getServer(Long.parseLong(sid));
// 		int linenum = Integer.parseInt(linenumS);
// 		String logpath = server.getResinhome() + "/log/" + logname;
// 		logcontent = cmdService.getServerLog(server.getGameipaddr(), logpath, linenum);
// 	}
	%>
	<%!
	static List<String> notShowServers = new ArrayList<String>();
	static {
		notShowServers.add("巍巍昆仑");
		notShowServers.add("百里沃野");
		notShowServers.add("正式发布服");
		notShowServers.add("13测试发布");
		notShowServers.add("幽恋蝶谷");
	}
	%>
		<form action="" method=post name=f1>
			<table id="test1" align="center">
				<tr>
					<td align="center">
					<b>日志名称</b>
						<select name="logname">
							<%for(int i=0; i<logs.length; i++) {%>
							<option value="<%=logs[i] %>" <%if(logname != null && logs[i].equals(logname)) out.print("selected");%>><%=logs[i] %></option>
							<%} %>
						</select>
						行数:<input type="text" name=linenum value="<%=linenumS==null?"20":linenumS %>" size=5>
						<input type="hidden" name=sid value="<%=sid %>">
						<input type=button name=bt2 value=" 查 询 " onclick="view()" />
					</td>
				</tr>
				<tr>
				<%
				int index = 0;
					for(Server ss:servers){
						if(notShowServers.contains(ss.getName())){continue;}
						
						if(logname != null) {
							Server server = smanager.getServer(ss.getId());
							int linenum = Integer.parseInt(linenumS);
							String logpath = server.getResinhome() + "/log/" + logname;
							logcontent = cmdService.getServerLog(server.getGameipaddr(), logpath, linenum);
						}
						%>
						<table align="center">
							<td colspan="20" bgcolor="#add6a6">【<%=index++ %>】<%=ss.getName() %></td>
							<td>
								<textarea name=logtext id=logtext cols="140" rows=20 style="font-size:10px;"><%=logcontent %></textarea>
							</td>
						</table>						
						<%
					}
				%>
					
				</tr>
			</table>
		</form>
		<div id="overlay"></div>
		<div id="lightbox2">
	     <div class="lbody2" id="lbody2" align="center">
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
 
