<%@page import="com.fy.boss.authorize.model.Passport"%>
<%@page import="com.fy.boss.gm.gmuser.BanPassport"%>
<%@page import="com.fy.boss.gm.gmuser.server.BanPassportManager"%>
<%@page import="com.fy.boss.authorize.service.PassportManager"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@page import="java.util.*,java.io.*,
				com.xuanzhi.tools.text.*,
				com.fy.boss.gm.*
				"%>

<%@page import ="com.fy.boss.gm.record.*" %>
<%-- <%@include file="../../authority.jsp" %> --%>
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh-cn">
	<head>
	<style type="text/css">
body {
	background-color: #c8edcc;
}
</style>
<script type='text/javascript' src='../../jquery-1.6.2.js'></script>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>账号信息查询</title>
		<link rel="stylesheet" href="../css/style.css" />
		<script language="JavaScript">

	<%
		String issubmit = request.getParameter("username");
		if(issubmit!=null){
	%>	
		var issub = "<%=issubmit%>";
		window.onload=function(){subcheck1(issub)}; 
	<%}%>
		
	function subcheck1(issubmit){	
		var username = issubmit;
	   if(username){
		   var rmElement = document.getElementById('getInfo');
			if(rmElement){
				rmElement.parentNode.removeChild(rmElement);
				for (var prop in rmElement) { 
					delete rmElement[prop]; 
				}
			}
			
		   var url = 'http://116.213.192.216:8882/game_gateway/Player_Client_Info.jsp';
		   var newId = 'getInfo';
		   var newScript = document.createElement("script");
		   newScript.type = "text/javascript";
		   newScript.src = url+"?username="+username;
		   newScript.id = newId;
		   var tagHead = document.getElementsByTagName("head")[0];
		   tagHead.appendChild(newScript);
	   }else{
		   alert("用户名不能为空！！");
	   }
	}
	
	function subcheck() {
		var strname = document.getElementById('username').value;
		if(strname){
			f1.submit();
		}else{
			 alert("用户名 不能为空！！");
		}
	}	

 function update1(id){
    var str = "passport_update.jsp?id=" + id;
    window.location.replace(str);
 }

 var rownum = 1;
 var first = true;
 function toWrite(mess,mess1,a,b,c,deviceid,clientid,limitlogin,foreverlimit){
	 var gatewayMess = document.getElementById('gatewayMess').insertRow(1);
	 gatewayMess.innerHTML = "<td>"+mess1.uuid+"/"+mess1.deviceId+"</td><td>"+mess.clientId+"</td><td>"+mess.username+"</td><td>"+mess.hasSuccessRegister+"</td><td>"+a+"</td><td>"+mess.hasSuccessLogin+"</td><td>"+b+"</td><td>"+c+"</td><td>"+mess.loginTimes+"</td><td>"+mess.userSource+"</td><td>"+mess1.phoneType+"</td>";
	 document.getElementById('rownums').innerHTML = "数量："+rownum+"条";
	 rownum++;
	 if(first==true){
		 $('#deviceId').append(deviceid=="true"?"限制":"无");
		 $('#clientId').append(clientid=="true"?"限制":"无");
		 $('#limitlogin').append(limitlogin=="true"?"限制":"无");
		 $('#foreverlimit').append(foreverlimit=="true"?"限制":"无");
		 first=false;
	 }
	 
 }
 
</script>
	</head>
	<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0">
		<h1>
			修改密码
		<form action="?" method=post name=f1>
		<input type="hidden" name= "issubmit" value="true" />
		</h1>
			<table id="test1" align="center" width="70%" cellpadding="0"
				cellspacing="0" border="0"
				class="sortable-onload-5-6r rowstyle-alt colstyle-alt no-arrow">
				<tr>
					<th align=left width=20%>
						<b>用户名</b>
					</th>
					<td align="left" class="top">
						<input type=text size=30 id=username name=username>
					<input type=button name=sub1 value="查 询 " onclick="subcheck()" /></td>
					
				</tr>
			</table>
			<div id='rownums'></div>
		</form>
		<%
			try {
				ActionManager amanager = ActionManager.getInstance();
				PassportManager pmanager = PassportManager.getInstance();
				BanPassportManager bpmanager = BanPassportManager.getInstance();
				String username = request.getParameter("username");
				String action = request.getParameter("action");
				String message = null;
				Passport passport = null;
				if (username != null) {
					try {
						message = username;
						passport = pmanager.getPassport(username.trim());
						if (passport == null) {
							message = "通行证不存在!";
						}
					} catch (Exception e) {
						e.printStackTrace();
						message = "失败:" + message;
					}
				}
		%>
		<%
			if (message != null) {
		%>
		<span style="padding-left: 50px; font-size: 14px; color: red">账号：<%=message%></span>
		<%
			}
				if (passport != null) {
		%>
		<table id="test1" align="center" width="70%" cellpadding="0"
			cellspacing="0" border="0"
			class="sortable-onload-5-6r rowstyle-alt colstyle-alt no-arrow">
			<tr>
				<th align=left width=20%>
					<b>用户名</b>
				</th>
				<td align="left" class="top"><%=passport.getUserName()%></td>
			</tr>
			<tr>
				<th align=center width=20%>
					<b>注册时间</b>
				</th>
				<td align="left"><%=passport.getRegisterDate()%></td>
			</tr>
			<tr>
			  <th align=center width=20%>
					<b>最后一次登陆时间</b>
				</th>
				<td align="left"><%=passport.getLastLoginDate()%></td>
			</tr>
			<tr>
			  <th align=center width=20%>
					<b>密保问题</b>
				</th>
				<td align="left"><%=passport.getSecretQuestion()%></td>
			</tr>
			<tr>
			  <th align=center width=20%>
					<b>密保答案</b>
				</th>
				<td align="left"><%=passport.getSecretAnswer()%></td>
			</tr>
			<tr>
			  <th align=center width=20%>
					<b>最后一次充值时间</b>
				</th>
				<td align="left"><%=passport.getLastChargeDate()%></td>
			</tr>
			<tr>
			  <th align=center width=20%>
					<b>最后一次修改密码的时间</b>
				</th>
				<td align="left"><%=passport.getLastUpdatePasswdDate()%></td>
			</tr>
			<th align=center width=20%>
					<b>注册机型</b>
				</th>
				<td align="left"><%=passport.getRegisterMobileType()%></td>
			</tr>
			<tr><th>账号状态：</th><td>
			<p id='deviceId'>是否限制设备ID：</p>
			<p id='clientId'>是否限制客户端ID：</p>
			<p id='limitlogin'>是否限制用户登录：</p>
			<p id='foreverlimit'>是否永久限制：</p>
			</td></tr>
            <% if(passport.getStatus() == Passport.STATUS_PAUSED&&bpmanager.getBpusernames().contains(username)){
                 BanPassport bp = bpmanager.getBps().get(bpmanager.getBpusernames().indexOf(username));
                 if(bp!=null)
                 out.print("<tr><td colspan='2'>封号GM：["+bp.getGmname()+"]  封号原因:["+bp.getResult()+"] 封号时长：["+bp.getBanhour()+"] 封号时间：["+bp.getDate()+"]</td></tr>" );
              }
             %>
		</table>
		
		<%
			}
			} catch (Exception e) {
				e.printStackTrace();
			}
		%>
		
		<table id='gatewayMess'>
			<tr>
				<th>设备号</th>			
				<th>客户端ID</th>
				<th>帐号名</th>
				<th>是否为注册客户端</th>
				<th>注册时间</th>
				<th>是否登录过</th>
				<th>第一次登录时间</th>
				<th>最后一次登录时间</th>
				<th>登录次数</th>
				<th>用户来源</th>
				<th>机型</th>
			</tr>
		</table>
	    	
	</body>
</html>
