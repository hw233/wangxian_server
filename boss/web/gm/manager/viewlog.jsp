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

function GetRequest() {
	   var url = location.search; //获取url中"?"符后的字串
	   var theRequest = new Object();
	   if (url.indexOf("?") != -1) {
	      var str = url.substr(1);
	      strs = str.split("&");
	      for(var i = 0; i < strs.length; i ++) {
	         theRequest[strs[i].split("=")[0]]=unescape(strs[i].split("=")[1]);
	      }
	   }
	   return theRequest;
}


function view() {
		
	
		over(2);
		f1.submit();
}

var autoid;

function autoview() {
	var interval;
	
	if(GetRequest()["interval"])
	{
		interval =GetRequest()["interval"];
	}
	
	
	
	if(interval)
	{
		document.getElementById("secs").value = interval/1000;
	}		
	else
	{
		 interval = document.getElementById("secs").value * 1000;
	}
	
	f1.action = "viewlog.jsp?interval="+interval+"&auto=true";
	
	autoid = setTimeout("view()",interval);

}


function stopview() {
	
	
	var interval = document.getElementById("secs").value * 1000;
	f1.action = "viewlog.jsp";
	view();
	if(autoid)
	{
		clearTimeout(autoid);
	}
	
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
	if(logname != null) {
		Server server = smanager.getServer(Long.parseLong(sid));
		int linenum = Integer.parseInt(linenumS);
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
						行数:<input type="text" name=linenum value="<%=linenumS==null?"40":linenumS %>" size=5>
						<input type="hidden" name=sid value="<%=sid %>">
						<input type=button name="bt2"  id="bt2" value=" 开始自动查询 " onclick="autoview()" /><input type=button name="stopbt" id="stopbt" value=" 停止自动查询 " onclick="stopview()" /><input type="range" name="secs"  id="secs"  min="1" max="10" step="1" value="5" />
					</td>
				</tr>
				<tr>
					<td align="left" colspan="2">
						<textarea name=logtext id=logtext cols="140" rows=44 style="font-size:8px;"><%=logcontent %></textarea>
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
			
			var isauto = GetRequest()["auto"];
			if(isauto == "true")
			{
				autoview();
			}

			
		</script>
	</body>
</html>
 
