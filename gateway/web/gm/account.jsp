<%@ page contentType="text/html;charset=utf-8" import="java.util.*,
com.xuanzhi.tools.text.*,com.fy.boss.authorize.model.*,com.fy.boss.client.*,
com.xuanzhi.tools.transport.*,java.nio.channels.*,com.fy.gamegateway.mieshi.server.*,com.fy.gamegateway.gmaction.*"%>
<%@page import="com.xuanzhi.tools.text.ParamUtils"%>
<%@page import="com.fy.gamegateway.mieshi.waigua.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>账号信息查询</title>
<script src="SpryAssets/SpryTabbedPanels.js" type="text/javascript"></script>
<style type="text/css">
body {
	background-image: url(images/beijing.gif);
}
</style>
<script>
function showModPass() {
	var modpass = document.getElementById("modpass");
	modpass.style.display = "block";
}

function showForbid() {
	var forbid = document.getElementById("forbid");
	forbid.style.display = "block";
}

function showClearSecret() {
	var clearSecret = document.getElementById("clearSecret");
	clearSecret.style.display = "block";
}

function changePassword() {
	var username = document.getElementById("username").value;
	var password = document.getElementById("password").value;
	window.location.replace("account.jsp?action=modpass&password=" + password + "&username=" + username);
}

function forbidUser() {
	var username = document.getElementById("username").value;
	var reason = document.getElementById("reason").value;
	var innerReason = document.getElementById("innerReason").value;
	var hours = document.getElementById("hours").value;
	window.location.replace("account.jsp?action=forbid&hours=" + hours + "&reason=" + reason + "&innerReason=" + innerReason + "&username=" + username);
}

function clearSecret() {
	var username = document.getElementById("username").value;
	window.location.replace("account.jsp?action=clearSecret&&username=" + username);
}

function queryUser() {
	var username = document.getElementById("username1").value;
	window.location.replace("account.jsp?&username=" + username);
}
</script>
</head>
<%
int permLevel = -1;
com.xuanzhi.tools.authorize.AuthorizeManager am = com.xuanzhi.tools.authorize.AuthorizeManager.getInstance();
com.xuanzhi.tools.authorize.Role role0 = am.getRoleManager().getRole("role_gm_common");
com.xuanzhi.tools.authorize.Role role1 = am.getRoleManager().getRole("role_gm_captain");
com.xuanzhi.tools.authorize.Role role2 = am.getRoleManager().getRole("role_gm_admin");
String gmusername = (String)session.getAttribute("authorize.username");
com.xuanzhi.tools.authorize.User gmuser = am.getUserManager().getUser(gmusername);
if(gmuser != null) {
	if(gmuser.isRoleExists(role2)) {
		permLevel = 2;
	} else if(gmuser.isRoleExists(role1)) {
		permLevel = 1;
	} else if(gmuser.isRoleExists(role0)) {
		permLevel = 0;
	}
}
if(permLevel < 0) {
	System.out.println("[严重警告: 正在访问没有权限的页面] ["+gmusername+"] ["+request.getRequestURL()+"]");
	return;
}
MieshiServerInfoManager msm = MieshiServerInfoManager.getInstance();
String username = request.getParameter("username");
if(username == null) {
	username = request.getParameter("username1");
}
Passport passport = null;
GmActionManager gam = GmActionManager.getInstance();
if(username != null) {
	try {
		passport = BossClientService.getInstance().getPassportByUserName(username);
	} catch(Exception r) {
		out.println("<script>window.alert('用户不存在!');</script>");
	}
	String action = request.getParameter("action");	
	if(action != null && action.equals("modpass")) {
		if(permLevel >= 0) {
			String password = request.getParameter("password");
			passport.setPassWd(StringUtil.hash(password));
			BossClientService.getInstance().update(passport);
			out.println("<script>window.alert('修改密码成功!');</script>");
			GmAction ga = new GmAction();
			ga.setActionTime(System.currentTimeMillis());
			ga.setActionType(GmAction.ACTION_EDIT_PASSWD);
			ga.setAmount(0);
			ga.setArticleInfo("");
			ga.setOperator(gmusername);
			ga.setOtherInfos(new String[0]);
			ga.setPetInfo("");
			ga.setPlayerId(0L);
			ga.setPlayerName("");
			ga.setReason("");
			ga.setServerName("");
			ga.setUserName(username);
			gam.createGmAction(ga);
		} else {
			out.println("<script>window.alert('无权限!');</script>");
		}
	} else if(action != null && action.equals("forbid")) {
		if(permLevel >= 0) {
			String reason = request.getParameter("reason");
			String innerReason = request.getParameter("innerReason");
			int hours = Integer.valueOf(request.getParameter("hours"));
			msm.addDenyUser("","",false,false,username,reason,innerReason,gmuser.getRealName(),hours==0?true:false,hours/24,hours%24);
			out.println("<script>window.alert('成功封禁了此账号!');</script>");
			GmAction ga = new GmAction();
			ga.setActionTime(System.currentTimeMillis());
			ga.setActionType(GmAction.ACTION_FORBID_ACCOUNT);
			ga.setAmount(0);
			ga.setArticleInfo("");
			ga.setOperator(gmusername);
			ga.setOtherInfos(new String[0]);
			ga.setPetInfo("");
			ga.setPlayerId(0L);
			ga.setPlayerName("");
			ga.setReason(innerReason);
			ga.setServerName("");
			ga.setUserName(username);
			gam.createGmAction(ga);
		} else {
			out.println("<script>window.alert('无权限!');</script>");
		}
	} else if(action != null && action.equals("removeForbid")) {
		if(permLevel >= 0) {
			msm.removeDenyUser(username);
			out.println("<script>window.alert('成功解封了此账号!');</script>");GmAction ga = new GmAction();
			ga.setActionTime(System.currentTimeMillis());
			ga.setActionType(GmAction.ACTION_RELEASE_ACCOUNT);
			ga.setAmount(0);
			ga.setArticleInfo("");
			ga.setOperator(gmusername);
			ga.setOtherInfos(new String[0]);
			ga.setPetInfo("");
			ga.setPlayerId(0L);
			ga.setPlayerName("");
			ga.setReason("");
			ga.setServerName("");
			ga.setUserName(username);
			gam.createGmAction(ga);
		} else {
			out.println("<script>window.alert('无权限!');</script>");
		}
	} else if(action != null && action.equals("clearSecret")) {
		if(permLevel >= 0) {
			passport.setSecretAnswer("");
			passport.setSecretQuestion("");
			BossClientService.getInstance().update(passport);
			out.println("<script>window.alert('已经清除了此账号的密保信息!');</script>");GmAction ga = new GmAction();
			ga.setActionTime(System.currentTimeMillis());
			ga.setActionType(GmAction.ACTION_CLEAR_MIBAO);
			ga.setAmount(0);
			ga.setArticleInfo("");
			ga.setOperator(gmusername);
			ga.setOtherInfos(new String[0]);
			ga.setPetInfo("");
			ga.setPlayerId(0L);
			ga.setPlayerName("");
			ga.setReason("");
			ga.setServerName("");
			ga.setUserName(username);
			gam.createGmAction(ga);
		} else {
			out.println("<script>window.alert('无权限!');</script>");
		}
	} else if(action != null && action.equals("removeCA")) {
		if(permLevel >= 0) {
			int index = Integer.valueOf(request.getParameter("index"));
			String user = request.getParameter("username");
			List<ClientAuthorization> cas = AuthorizeManager.getInstance().getUsernameClientAuthorization(user);
			ClientAuthorization ca = cas.remove(index);
			ca.setLastLoginTime(0);
			AuthorizeManager.getInstance().em_ca.flush(ca);
			AuthorizeManager.logger.warn("[从页面删除一个CA_account.jsp] ["+ca.getId()+"] ["+ca.getClientID()+"] ["+ca.getAuthorizeState()+"] ["+ca.getUsername()+"] ["+ca.getAuthorizeTime()+"]");
			GmAction ga = new GmAction();
			ga.setActionTime(System.currentTimeMillis());
			ga.setActionType(GmAction.ACTION_CLEAR_AUTHORIZATION);
			ga.setAmount(0);
			ga.setArticleInfo("");
			ga.setOperator(gmusername);
			ga.setOtherInfos(new String[0]);
			ga.setPetInfo("");
			ga.setPlayerId(0L);
			ga.setPlayerName("");
			ga.setReason("");
			ga.setServerName("");
			ga.setUserName(username);
			gam.createGmAction(ga);
		} else {
			out.println("<script>window.alert('无权限!');</script>");
		}
	}
}
	
%>
<body>
<form action="account.jsp" method=post>
<table width="98%" cellpadding="2" cellspacing="2">
  <tr>
    <td width="30%"><label for="textfield">账号：</label>
      <input type="text" name="username1" id="username1" value="<%=username!=null?username:"" %>"/>
      <input type="submit" name="button" id="button" value="查询"/>
    </td>
    <td width="70%">
    	<input type=button name=bt1 value="通过角色名反查账号名" onclick="window.open('p2u.jsp','_blank');">
    	<input type=button name=bt1 value="查看GM操作记录" onclick="window.open('gmAction.jsp','_blank');">
    </td>
  </tr>
</table></form>
<hr />
<%if(passport != null) { 
MieshiServerInfoManager.DenyUser du = msm.getDenyUser(username);
%>
<div style="font-weight:bold;width:100px;margin-top:20px;">基本信息：</div>
<input type=hidden name=username id='username' value="<%=username %>">
<table width="98%" border="1" align="left">
  <tr>
    <td width="30%" align="center" valign="middle">用户名：</td>
    <td width="70%">&nbsp;<%=passport.getUserName() %></td>
  </tr>
  <tr>
    <td align="center" valign="middle">注册时间：</td>
    <td>&nbsp;<%=DateUtil.formatDate(passport.getRegisterDate(),"yyyy-MM-dd HH:mm:ss") %></td>
  </tr>
  <tr>
    <td align="center" valign="middle">最后一次登录时间：</td>
    <td>&nbsp;<%=DateUtil.formatDate(passport.getLastLoginDate(),"yyyy-MM-dd HH:mm:ss") %></td>
  </tr>
  <tr>
    <td align="center" valign="middle">密保问题：</td>
    <td>&nbsp;<%=passport.getSecretQuestion() %></td>
  </tr>
  <tr>
    <td align="center" valign="middle">密保答案：</td>
    <td>&nbsp;<%=passport.getSecretAnswer() %></td>
  </tr>
  <tr>
    <td align="center" valign="middle">最后一次充值时间：</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td align="center" valign="middle">注册机型：</td>
    <td>&nbsp;<%=passport.getRegisterMobileType() %></td>
  </tr>
  <tr>
    <td align="center" valign="middle">账号状态：</td>
    <td>是否为封停状态：<%=du!=null?"<b>是</b>":"<b>否</b>" %><br>
    	<%if(du!=null) {%>
    	<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
			<tr bgcolor="#00FFFF" align="center">
				<tr bgcolor="#00FFFF" align="center">
					<td>用户名</td>
					<td>限制类型</td>
					<td>限制设备</td>
					<td>限制客户端</td>
					<td>开始时间</td>
					<td>结束时间</td>
					<td>操作人</td>
					<td>对外原因</td>
					<td>对内原因</td>
					<td>操作</td>
				</tr>
				<%
				out.println("<tr bgcolor='#FFFFFF' align='center'>");				
				out.println("<td>"+du.username+"</td>");
				if(du.foroverDeny){
					out.println("<td>永久封号</td>");
				}else{
					out.println("<td>临时封号</td>");
				}
				if(du.denyDeviceId){
					out.println("<td>"+du.deviceId+"</td>");
				}else{
					out.println("<td>不限制设备</td>");
				}
				if(du.denyClientId){
					out.println("<td>"+du.clientId+"</td>");
				}else{
					out.println("<td>不限制客户端ID</td>");
				}
				out.println("<td>"+DateUtil.formatDate(new Date(du.denyStartTime),"yyyy-MM-dd HH:mm:ss")+"</td>");
				out.println("<td>"+DateUtil.formatDate(new Date(du.denyEndTime),"yyyy-MM-dd HH:mm:ss")+"</td>");
				out.println("<td>"+du.gm+"</td>");
				out.println("<td>"+du.reason+"</td>");
				out.println("<td>"+du.innerReason+"</td>");
				if(permLevel >= 0) {
					out.println("<td><a href='./account.jsp?action=removeForbid&username="+du.username+"'>解封</a></td>");
				} else {
					out.println("<td>&nbsp;无解封权限</td>");
				}
				out.println("</tr></table>");
    		}
			%>
    </td>
  </tr>
  <tr>
    <td rowspan="4" align="center" valign="middle">高级操作：</td>
    <td>&nbsp;
    	<%if(permLevel >= 0) { %>
    	<!--   input type="button" name="button6" id="button6" value="修改密码" onclick="showModPass();"-->
    	<div id="modpass" style="display:none;">新密码:<input type=text name=password id='password' size=12><input type=button name=sub1 value="确定" onclick="changePassword();"></div>
   		<%} %>
    </td>
  </tr>
  <%if(du == null){ %>
  <tr>
    <td>&nbsp;
    	<%if(permLevel >= 0) { %>
    	<!-- input type="submit" name="button3" id="button3" value="封停" onclick="showForbid();"/--> 
      	<div id="forbid" style="display:none;text-align:left;"><br>
      		对外原因:<input type=text name=reason id='reason' size=20><br>
      		对内原因:<input type=text name=innerReason id='innerReason' size=20><br>
      		限制时间:<input type=text name=hours id='hours' size=6 value="0">填0为永久封禁<br>
      		<input type=button name=sub1 value="确定" onclick="forbidUser();">
      	</div>
      	<%} %>
    </td>
  </tr>
  <%} %>
  <tr>
    <td>&nbsp;
    	<%if(permLevel >= 0) { %>
    	<!--  input type="submit" name="button5" id="button5" value="清空密保信息" onclick="showClearSecret();"/-->
    	<div id="clearSecret" style="display:none;">是否确定要清空密保信息和答案？<input type=button name=sub1 value="确定" onclick="clearSecret();"></div>
   		<%} %>
    </td>
  </tr>
</table>
<table border="0" width="200"><tr><td height="20">&nbsp;</td></tr></table>
<div style="font-weight:bold;width:100px;padding-bottom:5px;">授权信息：</div>
<%
List<ClientAuthorization> cas = AuthorizeManager.getInstance().getUsernameClientAuthorization(username);
%>
<table width="98%" height="73" border="1" align="left" style="text-align:center;">
  <tr>
    <td height="21">用户名</td>
	<td>授权状态</td>
	<td>授权方式</td>
	<td>授权时间</td>
	<td>最后登录时间</td>
	<td>登录次数</td>
	<td>请求授权次数</td>
	<td>客户端ID</td>
	<td>IP地址</td>
	<td>MAC地址</td>
	<td>平台</td>
	<td>机型</td>
	<td>GPU型号</td>
	<td>客户端创建时间</td>
	<td>授权客户端ID</td>
	<td>删除</td>
  </tr>
  <%
			String aa[] = new String[]{"等待", "正常", "被替代", "异常", "拒绝", "等待过期"};
			String bb[] = new String[]{"自动授权", "手动授权或拒绝"};
			for (int i = 0 ; i< cas.size(); i++) {
				ClientAuthorization ca = cas.get(i);
				
				ClientEntity ce = AuthorizeManager.getInstance().getClientEntity(ca.getClientID());
								
				String mac = "-";							//mac地址
				
				String platform = "-";					//平台
				
				String phoneType = "-";					//机型
				
				String gpuType = "-";						//GPU类型
				
				long createTime = 0;		//创建时间
				
				if(ce != null){
					mac = ce.getMac();
					platform = ce.getPlatform();
					phoneType = ce.getPhoneType();
					gpuType = ce.getGpuType();
					createTime = ce.getCreateTime();
				}
		%>
			<tr>
				<td><%=ca.getUsername() %></td>
				<td><%=aa[ca.getAuthorizeState()] %></td>
				<td><%=bb[ca.getAuthorizeType()] %></td>
				<td><%=DateUtil.formatDate(new Date(ca.getAuthorizeTime()), "yyyy-MM-dd HH:mm:ss") %></td>
				<td><%=DateUtil.formatDate(new Date(ca.getLastLoginTime()), "yyyy-MM-dd HH:mm:ss") %></td>
				<td><%=ca.getLoginNum() %></td>
				<td><%=ca.getQueryNum() %></td>
				<td><%=ca.getClientID() %></td>				
				<td><%=ca.getIp() %></td>
				<td><%=mac %></td>
				<td><%=platform %></td>
				<td><%=phoneType %></td>
				<td><%=gpuType %></td>
				<td><%= DateUtil.formatDate(new Date(createTime), "yyyy-MM-dd HH:mm:ss")  %></td>
				<td><%=ca.getAuthorizeClientId() %></td>
				<%if(permLevel >= 0) { %>
				<td><!-- <a href="account.jsp?action=removeCA&username=<%=username %>&index=<%=i %>">删除</a> --></td>
				<%} else {%>
				<td>无删除权限</td>
				<%} %>
			</tr>
		<%
			}
		%>
</table>
<table border="0" width="200"><tr><td height="20">&nbsp;</td></tr></table>
<%
MieshiPlayerInfoManager mpm = MieshiPlayerInfoManager.getInstance();
MieshiPlayerInfo[] mps = mpm.getMieshiPlayerInfoByUsername(username);
//整理到一个map中
HashMap<String,List<MieshiPlayerInfo>> serverPlayerMap = new HashMap<String,List<MieshiPlayerInfo>>();
for(MieshiPlayerInfo mp : mps) {
	List<MieshiPlayerInfo> list = serverPlayerMap.get(mp.getServerRealName());
	if(list == null) {
		list = new ArrayList<MieshiPlayerInfo>();
		serverPlayerMap.put(mp.getServerRealName(), list);
	}
	list.add(mp);
}
String servers[] = serverPlayerMap.keySet().toArray(new String[0]);
%>
<div style="font-weight:bold;width:100px;margin:20px 0 5px; 0;"><span>角色列表：</span></div>
  <table width="98%" height="73" border="1" align="left" style="text-align:center;">  	
	  <tr>
	    <td width=10%>服务器</td>
	    <td width=15%>角色ID</td>
	    <td width=15%>角色名</td>
		<td width=10%>职业</td>
		<td width=10%>等级</td>
		<td width=15%>充值额度</td>
		<td width=10%>VIP等级</td>
		<td width=15%>最后登录时间</td>
	  </tr>
<%for(String server : servers) {
List<MieshiPlayerInfo> list = serverPlayerMap.get(server);
%>
<!--<div style="width:90px;text-align:center;font-weight:bold;color:blue;margin:5px;"><%=server %></div>-->

	  
	  <%
	  long pid = 0;
	  for(MieshiPlayerInfo info : list) {
		  if(pid!=info.getPlayerId()){
	  %>
			  <tr>
				  <td><%=info.getServerRealName() %></td>
			  	<td><%=info.getPlayerId() %></td>
			  	<td><a href="javascript:window.open('<%=getServerPlatformUrl(info.getServerRealName()) %>/admin/gm/playerinfo.jsp?username=<%=username %>&playername=<%=info.getPlayerName() %>')"><%=info.getPlayerName() %></a></td>
			  	<td><%=getCareerName(info.getCareer()) %></td>
			  	<td><%=info.getLevel() %></td>
			  	<td><%=info.getPlayerRMB() %></td>
			  	<td><%=info.getPlayerVIP() %></td>
			  	<td><%=DateUtil.formatDate(new Date(info.lastAccessTime),"yyyy-MM-dd HH:mm:ss") %></td>
			  	<td><a href="javascript:window.open('<%=getServerPlatformUrl(info.getServerRealName()) %>/admin/smith/iforbid.jsp?username=<%=username %>&playername=<%=info.getPlayerName() %>')">是否封禁</a></td>
			  </tr>
	   <%
			  	pid =  info.getPlayerId();
		  }
	  } %>
	  
  
<%} %>
 </table>
<p>&nbsp;</p>
<script type="text/javascript">
var TabbedPanels1 = new Spry.Widget.TabbedPanels("TabbedPanels1");
</script>
<%} %>
</body>
</html>
<%!
String[] careerNames = new String[]{"通用","斗罗","鬼煞","灵尊","巫皇","兽"};
public String getCareerName(int career) {
	return careerNames[career];
}

public String getServerPlatformUrl(String realName) {
	MieshiServerInfo server = MieshiServerInfoManager.getInstance().getServerInfoByName(realName);
	if(server != null) {
		return server.getServerUrl();
	}
	return "unknown";
}
%>