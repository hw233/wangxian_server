<%@ page contentType="text/html;charset=utf-8" import="java.util.*,
com.xuanzhi.tools.text.*,
com.xuanzhi.tools.transport.*,java.nio.channels.*,com.fy.gamegateway.mieshi.server.*"%>
<%@page import="com.xuanzhi.tools.text.ParamUtils;"%><%
	
    InnerTesterManager itm = InnerTesterManager.getInstance();
	String action = request.getParameter("action");
	if("create_test_user".equals(action)){
		String clientId = request.getParameter("clientId");
		String username = request.getParameter("username");
		if(clientId == null || clientId.trim().equals("")){
			out.println("clientId不能为空");
			return;
		}
		if(username == null || username.trim().equals("")){
			username = "测试";
		}
		itm.updateMieshiInnerTesterInfo(clientId,username,(byte)0,0);
	}else if("delete_test_user".equals(action)){
		String clientId = request.getParameter("clientId");
		if(clientId == null || clientId.trim().equals("")){
			out.println("clientId不能为空");
			return;
		}
		itm.updateMieshiInnerTesterInfo(clientId,"",(byte)1,1);
	}else if("valid".equals(action)){
		itm.valid = !itm.valid;
	}else if("kaiqi".equals(action)){
		String clientId = request.getParameter("clientId1").trim();
		itm.updateMieshiInnerTesterInfo(clientId,(byte)0,0);
	}else if("guanbi".equals(action)){
		String clientId = request.getParameter("clientId2").trim();
		itm.updateMieshiInnerTesterInfo(clientId,(byte)1,0);
	}else if("delete".equals(action)){
		String clientId = request.getParameter("clientId3").trim();
		itm.updateMieshiInnerTesterInfo(clientId,(byte)1,1);
	}
	InnerTesterInfo[] infos = itm.getAllInnerTesterInfos();
%><html>
<head>
<link rel="stylesheet" href="../css/common.css"/>
<link rel="stylesheet" href="../css/table.css"/>

<script type="text/javascript">
function kaiqi(clientId){
	document.getElementById("clientId1").value = clientId;
	document.f4.submit();
}
function guanbi(clientId){
	document.getElementById("clientId2").value = clientId;
	document.f5.submit();
}
function shanchu(clientId){
	document.getElementById("clientId3").value = clientId;
	document.f6.submit();
}
</script>
</head>
<body>
<center>
<h2></h2><br><a href="./innerTester.jsp">刷新此页面</a><br>
<br>

<h2>内部测试人员clientId信息，这些帐号在服务器是内部测试时，可以进入测试服测试游戏包和资源下载</h2>
<form name="f1">
<input type='hidden' name='action' value='valid'>
服务器内部测试人员状态，总开关，开启状态可以使用测试号(<%=(itm.valid?"开启":"关闭") %>)<br/>
<input type="submit" value="<%=(itm.valid?"点击关闭":"点击开启") %>">
</form>
<table>
<tr>
<td>内部测试人员clientId</td><td>内部测试人员姓名</td><td>状态0测试，1正式</td><td>操作</td>
</tr>
<%
for(InnerTesterInfo info : infos){
	%>
	<tr>
<td><%=info.getClientId() %></td><td><%=info.getTesterName() %></td><td><%=info.getState() %></td><td><input type="button" name="button<%=info.getClientId() %>" value="开启测试状态" onclick='javascript:kaiqi("<%=info.getClientId() %>")'><input type="button" name="button2" value="开启正式状态" onclick='guanbi("<%=info.getClientId() %>")'></td>
</tr>
	<%
}
%>
</table>
</center>
<form name="f2">
<input type='hidden' name='action' value='create_test_user'>
clientId:<input type="text" name="clientId">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;姓名<input type="text" name="username" value="测试">
<input type="submit" value="新建或更新内部测试人员">
</form>
<br/>
<form name="f3">
<input type='hidden' name='action' value='delete_test_user'>
clientId:<input type="text" name="clientId">
<input type="submit" value="删除内部测试人员，注意不要删除别人的clientId">
</form>

<form name="f4">
<input type='hidden' name='action' value='kaiqi'>
<input type='hidden' name='clientId1' id="clientId1" value='clientId1'>
</form>

<form name="f5">
<input type='hidden' name='action' value='guanbi'>
<input type='hidden' name='clientId2' id="clientId2" value='clientId2'>
<input type="submit" name="ff2" value="ff" style="display:none">
</form>

<form name="f6">
<input type='hidden' name='action' value='delete'>
<input type='hidden' name='clientId3' id="clientId3" value='clientId3'>
<input type="submit" name="ff3" value="ff" style="display:none">
</form>
</body>
</html> 
