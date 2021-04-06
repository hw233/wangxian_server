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
ServerManager smanager = ServerManager.getInstance();
List<Server> servers = smanager.getServers();
ProjectManager pm = ProjectManager.getInstance(); 
boolean showAll = false;
String showAllStr = request.getParameter("showAll");
showAll = "true".equals(showAllStr);
%>
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh-cn">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>发布服务器</title>
		<link rel="stylesheet" href="../css/style.css" />
		<link rel="stylesheet" href="../css/atalk.css" />
		<script type="text/javascript" src="../js/jquery-1.9.1.min.js"></script>
		<script type="text/javascript" src="../js/jquery.chili-2.2.js"></script>
		<script type="text/javascript" src="../js/progress.js"></script>
		<script type="text/javascript" src="/dwr/engine.js"></script>
		<script type="text/javascript" src="/dwr/util.js"></script>
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

var selected;
var myInterval;
var depIndex;
var depType;
var selectedIds;

function deploySelected(name) {
	if(window.confirm("你是否确定要发布所有选中的服务器？？")) {
		if(window.confirm("再次确定要发布所有选中的服务器？？")) {
			var seled = new Array();
			var seledServers = document.getElementById("seledServers");
			var m = 0;
			var el = document.getElementsByTagName('input');
		    var len = el.length;
		    for(var i=0; i<len; i++)
		    {
		        if((el[i].type=="checkbox") && (el[i].name==name))
		        {
		        	if(el[i].checked) {
		            	seled[m++] = el[i].value;
		            }
		        }
		    }
			selected = seled;
			depIndex = 0;
			depType = 0;//发布覆盖
			myInterval = setInterval("checkAndDeployAll()",1000);
		}
	}
}

function deployNoCopySelected(name) {
	if(window.confirm("你是否确定要发布不覆盖所有选中的服务器？？")) {
		if(window.confirm("再次确定要发布不覆盖所有选中的服务器？？")) {
			var seled = new Array();
			var seledServers = document.getElementById("seledServers");
			var m = 0;
			var el = document.getElementsByTagName('input');
		    var len = el.length;
		    for(var i=0; i<len; i++)
		    {
		        if((el[i].type=="checkbox") && (el[i].name==name))
		        {
		        	if(el[i].checked) {
		            	seled[m++] = el[i].value;
		            }
		        }
		    }
			selected = seled;
			depIndex = 0;
			depType = 1;//发布不覆盖
			myInterval = setInterval("checkAndDeployAll()",1000);
		}
	}
}

function deployCopySelected(name) {
	if(window.confirm("你是否确定要拷贝覆盖所有选中的服务器？？")) {
		if(window.confirm("再次确定要拷贝覆盖所有选中的服务器？？")) {
			var seled = new Array();
			var seledServers = document.getElementById("seledServers");
			var m = 0;
			var el = document.getElementsByTagName('input');
		    var len = el.length;
		    for(var i=0; i<len; i++)
		    {
		        if((el[i].type=="checkbox") && (el[i].name==name))
		        {
		        	if(el[i].checked) {
		            	seled[m++] = el[i].value;
		            }
		        }
		    }
			selected = seled;
			depIndex = 0;
			depType = 2;//拷贝覆盖
			myInterval = setInterval("checkAndDeployAll()",3000);
		}
	}
}

function checkAndDeployAll() {
	if(depIndex < selected.length) {
		DeployBoundry.getRunningTaskNum(afterGetRunningTaskNum);
	} else {
		DeployBoundry.getRunningTaskNum(afterCheckAllDone);
	}
}

function afterGetRunningTaskNum(num) {
	if(num == 0) {
		var sid = selected[depIndex++];
		if(depType == 0) {
			DeployBoundry.deploy(sid);
		} else if(depType == 1) {
			DeployBoundry.deployNoCopy(sid);
		} else if(depType == 2) {
			DeployBoundry.deployCopy(sid);
		} 
	}
}

function afterCheckAllDone(num) {
	if(num == 0) {
		clearInterval(myInterval);
		window.alert("完成所有发布!");
	}
}

function confirmSucc(sid) {
	DeployBoundry.confirmSucc(sid);
}

function startServer(sid) {
	if(window.confirm("确认启动服务器？？")) {
		DeployBoundry.startServer(sid);
		document.getElementById("server_"+sid).style.color="green";
	}
}

function removeDeploy(sid) {
	DeployBoundry.removeDeploy(sid);
}

function stopServer(sid) {
	if(window.confirm("确认停止服务器？？")) {
		if(window.confirm("再次确认停止服务器？？")) {
			DeployBoundry.stopServer(sid);
			document.getElementById("server_"+sid).style.color="red";
		}
	}
}

function batchStartServers(name)
{
	if(window.confirm("确认批量启动选中的服务器？？")) {
		
		var seled = new Array();
		var seledServers = document.getElementById("seledServers");
		var m = 0;
		var el = document.getElementsByTagName('input');
	    var len = el.length;
	    for(var i=0; i<len; i++)
	    {
	        if((el[i].type=="checkbox") && (el[i].name==name))
	        {
	        	if(el[i].checked) {
	            	seled[m++] = el[i].value;
	            }
	        }
	    }
	    selectedIds = seled;
		
		
		DeployBoundry.batchStartServers(selectedIds);
		for(var i=0; i<selectedIds.length; i++)
		{
			document.getElementById("server_"+selectedIds[i]).style.color="green";
		}
	}
}

function batchStopServers(name)
{
	if(window.confirm("确认批量关闭选择的服务器？？")) {
		
		var seled = new Array();
		var seledServers = document.getElementById("seledServers");
		var m = 0;
		var el = document.getElementsByTagName('input');
	    var len = el.length;
	    for(var i=0; i<len; i++)
	    {
	        if((el[i].type=="checkbox") && (el[i].name==name))
	        {
	        	if(el[i].checked) {
	            	seled[m++] = el[i].value;
	            }
	        }
	    }
	    selectedIds = seled;
		
		DeployBoundry.batchStopServers(selectedIds);
		for(var i=0; i<selectedIds.length; i++)
		{
			document.getElementById("server_"+selectedIds[i]).style.color="red";
		}
	}
}


function startServers(name)
{
	if(window.confirm("确认批量启动选中的服务器？？")) {
		
		var seled = new Array();
		var seledServers = document.getElementById("seledServers");
		var m = 0;
		var el = document.getElementsByTagName('input');
	    var len = el.length;
	    for(var i=0; i<len; i++)
	    {
	        if((el[i].type=="checkbox") && (el[i].name==name))
	        {
	        	if(el[i].checked) {
	            	seled[m++] = el[i].value;
	            }
	        }
	    }
	    selectedIds = seled;
		
		
		DeployBoundry.startServers(selectedIds);
		for(var i=0; i<selectedIds.length; i++)
		{
			document.getElementById("server_"+selectedIds[i]).style.color="green";
		}
	}
}

function stopServers(name)
{
	if(window.confirm("确认批量关闭选择的服务器？？")) {
		
		var seled = new Array();
		var seledServers = document.getElementById("seledServers");
		var m = 0;
		var el = document.getElementsByTagName('input');
	    var len = el.length;
	    for(var i=0; i<len; i++)
	    {
	        if((el[i].type=="checkbox") && (el[i].name==name))
	        {
	        	if(el[i].checked) {
	            	seled[m++] = el[i].value;
	            }
	        }
	    }
	    selectedIds = seled;
		
		DeployBoundry.stopServers(selectedIds);
		for(var i=0; i<selectedIds.length; i++)
		{
			document.getElementById("server_"+selectedIds[i]).style.color="red";
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

function showHideSelected() {
	var selectedDiv = document.getElementById("selectedFiles");
	if(selectedDiv.innerHTML != "") {
		selectedDiv.innerHTML = "";
		selectedDiv.style.display = "none";
	} else{
		DeployBoundry.getSelectedFiles(afterGetSelected);
	}
}

function afterGetSelected(files) {
	var ff = files.split(";;");
	var html = "";
	for(var i=0; i<ff.length; i++) {
		html = html + ff[i] + "<br>";
	}
	var selectedDiv = document.getElementById("selectedFiles");
	selectedDiv.innerHTML = html;
	selectedDiv.style.display = "block";
}

function judgeAll(name) {
	var call = document.getElementById("checkall");
	if(call.checked) {
		checkAll(name);
	} else {
		clearAll(name);
	}
}

function checkAll(name)
{
    var el = document.getElementsByTagName('input');
    var len = el.length;
    for(var i=0; i<len; i++)
    {
        if((el[i].type=="checkbox") && (el[i].name==name))
        {
            el[i].checked = true;
            tr_bgcolor(el[i]);
        }
    }
}

function clearAll(name)
{
    var el = document.getElementsByTagName('input');
    var len = el.length;
    for(var i=0; i<len; i++)
    {
        if((el[i].type=="checkbox") && (el[i].name==name))
        {
            el[i].checked = false;
            tr_bgcolor(el[i]);
        }
    }
}
function changeBgColor(selectname){
	var el = document.getElementsByTagName('input');
    var len = el.length;
    for(var i=0; i<len; i++)
    {
        if((el[i].type=="checkbox") && (el[i].selectname==selectname))
        {
            el[i].checked = true;
        }
        tr_bgcolor(el[i]);
    }
}
function tr_bgcolor(c){
var tr = c.parentNode.parentNode;
tr.style.backgroundColor = c.checked ? '#add6a6' : 'white';
}

function getErrInfos()
{
	DeployBoundry.getErrMesses(afterGetErrMesses);
}

function afterGetErrMesses(messages)
{
	$("#errinfo").innerHTML = "";
	var errMess = "";
	if(messages)
	{
		for(var i=0; i<messages.length; i++) {
			errMess += messages[i]+"<br/>";
		}
		$("#errinfo").innerHTML = errMess;
	}
	else
	{
		$("#errinfo").innerHTML = "";
	}
}


CheckStatus();




-->
</script>
<style>
.bg{position:absolute;z-index:999;filter:alpha(opacity=50);background:#666;opacity: 0.5;-moz-opacity: 0.5;left:0;top:0;height:99%;width:100%;}
.beian_winBG {
MARGIN-TOP: -100px; LEFT: 50%; MARGIN-LEFT: -190px; WIDTH: 376px; POSITION: absolute; TOP: 50%; HEIGHT:200px;border:#666666 1px solid;z-index: 1000;
}
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
	
	<span>
		<input type=button name=bt1 value="查看选中文件(<%=ProjectManager.getInstance().getFiles()!=null?ProjectManager.getInstance().getFiles().length:0 %>)" onclick="showHideSelected()">
		<input type=button name=bt1 value="重新选择文件" onclick="openwin('publishfiles.jsp')">
	</span>
	
	<div id="selectedFiles" style="width:500px;text-align:left;padding:5px;line-height:20px;"></div>
	
		<form action="server_list.jsp" method=post name=f1>
		<table align="center" width="99%" cellpadding="0"
                                cellspacing="0" border="0"
                                class="sortable-onload-5-6r rowstyle-alt colstyle-alt no-arrow">
                 <tr >
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
                                        <th align=center width=20%>
                                                <b><input type="checkbox" style="zoom:300%;" name="checkall" id="checkall" value="" onclick="judgeAll('seledServers');">全选</b>
                                        </th>
                                </tr>
                                
                 <%
                 int serverIndex = 0;
                 for(Server server:servers) {
				 %>
				<tr>
					<td align="center">
						<span id="server_<%=server.getId()%>" title="server_<%=server.getId()%>">[<%=serverIndex++ %>]<%=server.getName()%>(<%=server.getResinhome() %>)</span>
					</td>
					<td align="center">
					<%
					ServerStatus status = pm.getServerStatus(server);
					if(status == null) {
						out.println("Agent无响应");
					} else 
					if(!status.isAgenton()) {
						out.println("Agent无响应");
					} else {
						if(!status.isInstalled()) {
							out.println("未部署服务器");
						} else {
							if(!status.isRunning()) {
								out.println("未启动服务器<br><img src='../images/start.png' title='启动' style='cursor:pointer;' onclick=\"startServer('"+server.getId()+"')\">");
							} else {
								out.println("服务器运行中<br><img src='../images/stop.png' title='关闭' style='cursor:pointer;' onclick=\"stopServer('"+server.getId()+"')\">");
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
					<td align="center">
						<input type="checkbox" style="zoom:300%;" name="seledServers" value="<%=server.getId() %>" id="seledServers" onclick="changeBgColor('seledServers')"  servername="<%=server.getName()%>" >
					</td>
				</tr>
				<%} %>
          </table>                      
			<center><input type=button name=bt44 value="发布选中的 " onclick="deploySelected('seledServers')">
			<input type=button name=bt44 value=" 发布不覆盖选中的 " onclick="deployNoCopySelected('seledServers')">
			<input type=button name=bt44 value=" 拷贝覆盖选中的 " onclick="deployCopySelected('seledServers')">
			<input type=button name=bt44 value="启动选中的服务器" onclick="batchStartServers('seledServers')">
			<input type=button name=bt44 value="关闭选中的服务器" onclick="batchStopServers('seledServers')">
			<input type=button name=bt44 value="启动选中的服务器(同时启动)" onclick="startServers('seledServers')">
			<input type=button name=bt44 value="关闭选中的服务器(同时关闭)" onclick="stopServers('seledServers')">
			<input type=button name=bt44 value="查看错误信息" onclick="errwin.style.display='block';"></center>
			<br/>
		</form>
		<br><br>
		<div id="overlay"></div>
		<div id="lightbox2">
	     <div class="lbody2" id="lbody2">
	       		正在处理，请耐心等候...<br>
	       		<img src='../images/process.gif'> 
	     </div>
		</div>
		
		<div id="errwin" style="display:none;">
		<div id="mask" style="top:0;left:0;position: absolute;z-index:1000;" class="bg"></div>
		<DIV class=beian_winBG id=beian_popup>
		<div id="divOneStep" style="z-index:1002;width:100%;height:auto;background: #fff;position:absolute;">
		<div style="width:100%;background:#f1f1f1;height:30px;light-height:30px;border-bottom:#666666 1px solid;text-align:right;"><a href="javascript:;" onClick="errwin.style.display='none';">点此关闭</a> </div>
		<div id="errinfo"></div>
		</div>
		</div>
		
	</body>
</html>
 
