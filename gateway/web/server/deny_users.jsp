<%@ page contentType="text/html;charset=utf-8" import="java.util.*,
com.xuanzhi.tools.text.*,
com.xuanzhi.tools.transport.*,java.nio.channels.*,com.fy.gamegateway.mieshi.server.*"%>
<%@page import="com.xuanzhi.tools.text.*,com.xuanzhi.tools.page.* "%><%

int pageIndex = 1;
String preURL = "./deny_users.jsp";

//String beanName = "outer_gateway_connection_selector";
	MieshiServerInfoManager mm = MieshiServerInfoManager.getInstance();
	
	int totalDenyUserNum = 0;
	int totalForeverDenyUserNum = 0;
	long now = System.currentTimeMillis();
	ArrayList<DenyUserEntity> duList = mm.getDenyUserList();
	out.println("size:"+duList.size()+"<br/>");
	for(int i = 0 ; i < duList.size() ; i++){
		DenyUserEntity du = duList.get(i);
		if(du.isEnableDeny() && du.isForoverDeny()){
			totalDenyUserNum++;
			totalForeverDenyUserNum++;
		}else if(du.isEnableDeny() && du.getDenyStartTime()<= now && now <= du.getDenyEndTime()){
			totalDenyUserNum++;
		}
	}
	int queryType = 0;
	String queryName = "";
	boolean queryEnabled = false;
	if("query".equalsIgnoreCase(request.getParameter("action2"))){
		queryType = Integer.parseInt(request.getParameter("queryType"));
		queryName = request.getParameter("queryName");
		queryEnabled = true;
		preURL += "?action2=query&queryType="+queryType+"&queryName="+queryName;
	}
	ArrayList<DenyUserEntity> denyUserList = new ArrayList<DenyUserEntity>();
	for(int i = 0 ; i < duList.size() ; i++){
		DenyUserEntity du = duList.get(i);
		if(queryEnabled){
			if(queryType == 0){
				if(du.getUsername() != null && queryName.toLowerCase().contains(du.getUsername().toLowerCase())){
					denyUserList.add(0,du);
				}
			}else if(queryType == 1){
				if(du.getDeviceId() != null && du.getDeviceId().equalsIgnoreCase(queryName)){
					denyUserList.add(0,du);
				}
			}else if(queryType == 2){
				if(du.getClientId() != null && du.getClientId().equalsIgnoreCase(queryName)){
					denyUserList.add(0,du);
				}
			} else if(queryType == 3){
				if(du.getGm() != null && du.getGm().equalsIgnoreCase(queryName)){
					denyUserList.add(0,du);
				}
			} else if (queryType == 4) {
				if (queryName.equals("永久")) {
					if (du.isEnableDeny() && du.isForoverDeny()) {
						denyUserList.add(du);
					}
				}else if (queryName.equals("临时")) {
					if (du.isEnableDeny() && du.getDenyStartTime()<= now && now <= du.getDenyEndTime()) {
						denyUserList.add(du);
					}
				}else {
					if (du.isEnableDeny() && du.isForoverDeny()) {
					}else if (du.isEnableDeny() && du.getDenyStartTime()<= now && now <= du.getDenyEndTime()) {
					}else {
						denyUserList.add(du);
					}
				}
			}else if (queryType == 5) {
				if (queryName.length() > 0 && du.getReason().indexOf(queryName) >= 0) {
					denyUserList.add(du);
				}
			}
		}else{
			//if(du.isEnableDeny() && (du.isForoverDeny() ||  (du.isEnableDeny() && du.getDenyStartTime()<= now && now <= du.getDenyEndTime())) ){
				denyUserList.add(0,du);
			//}
		}
	}
	
	if(request.getParameter("page") != null){
		pageIndex = Integer.parseInt(request.getParameter("page"));
	}
	
	String pageHtml = PageUtil.makePageHTML(preURL,10,"page",pageIndex,50,denyUserList.size());
%><html>
<head>
</head>
<body>
<h1>限制登录页面，一共限制<%=totalDenyUserNum %>个帐号，永久限制<%= totalForeverDenyUserNum %>个帐号</h1>

<h2></h2><br><a href="./deny_users.jsp">刷新此页面</a><br>
<br>

<h2>查询限制登录信息
<%
	if (queryEnabled) {
		out.print("  数目：" + denyUserList.size());
	}
%></h2>
<form><input type='hidden' name='action2' value='query'>
请输入信息：<select name='queryType'>
<option value='0' <%= (queryType==0?"selected":"")%> >按帐号查询</option>
<option value='1' <%= (queryType==1?"selected":"")%> >按设备ID查询</option>
<option value='2' <%= (queryType==2?"selected":"")%> >按客户端ID查询</option>
<option value='3' <%= (queryType==3?"selected":"")%> >按GM名字查询</option>
<option value='4' <%= (queryType==4?"selected":"")%> >按封号情况查询</option>
<option value='5' <%= (queryType==5?"selected":"")%> >按原因模糊查询</option>
</select><input type='text' name='queryName' value='<%=queryName %>' >
<input type='submit' value='查询'>
</form>

<br/>
<%
String clientId = ParamUtils.getParameter(request,"clientId","");
String deviceId = ParamUtils.getParameter(request,"deviceId","");
String username = ParamUtils.getParameter(request,"username","");
String reason = ParamUtils.getParameter(request,"reason","");;
String gm = ParamUtils.getParameter(request,"gm","");;
boolean denyClientId = ParamUtils.getBooleanParameter(request,"denyClientId");
String rmp = ParamUtils.getParameter(request,"rmp","");
boolean denyDeviceId = ParamUtils.getBooleanParameter(request,"denyDeviceId");
boolean enableDeny = ParamUtils.getBooleanParameter(request,"enableDeny");;
boolean foroverDeny = ParamUtils.getBooleanParameter(request,"foroverDeny");;
int days = ParamUtils.getIntParameter(request,"days",0);
int hours = ParamUtils.getIntParameter(request,"hours",0);

String action = request.getParameter("action");

if(action != null && action.equals("modify")){
	DenyUserEntity du = mm.getDenyUser(request.getParameter("username"));
	if(du != null){
		clientId = du.getClientId();
		deviceId = du.getDeviceId();
		denyDeviceId = du.isDenyDeviceId();
		username = du.getUsername();
		reason = du.getReason();
		gm = du.getGm();
		denyClientId = du.isDenyClientId();
		enableDeny = du.isEnableDeny();
		foroverDeny = du.isForoverDeny();
		days = (int)((du.getDenyEndTime() - System.currentTimeMillis())/(24*3600000));
		hours = (int)(((du.getDenyEndTime() - System.currentTimeMillis()) - days * 24*3600000)/3600000 + 1);
	}
}else if(action != null && action.equals("set_modify")){
	if(enableDeny)
		mm.addDenyUser(deviceId,clientId,denyDeviceId,denyClientId,username,reason,gm,foroverDeny,days,hours);
	else if(!enableDeny){
		mm.disableDenyUser(username);
	}
	out.println("<h2>已修改"+request.getParameter("username")+"限制信息</h2>");
}else if(action != null && action.equals("remove")){
	mm.removeDenyUser(request.getParameter("username"));
	out.println("<h2>已删除"+request.getParameter("username")+"限制信息</h2>");
}
%>
<h2>添加修改限制用户</h2>
<form><input type='hidden' name='submitted' value='true'><input type='hidden' name='action' value='set_modify'>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor='#FFFFFF' align='center'>
<td>用户名</td>
<td align='left'><input type='text' name='username' value='<%=username %>'></td>
<td></td>
</tr>

<tr bgcolor='#FFFFFF' align='center'>
<td>设备ID</td>
<td align='left'><input type='text' name='deviceId' value='<%=deviceId %>'>
<select name='denyDeviceId'><option value='false' <%= (denyDeviceId==false?"selected":"")%> >不限制设备登录</option>
<option value='true' <%= (denyDeviceId==true?"selected":"")%>>限制设备登录</option></select>
</td>
<td>IOS的是UDID，Android是DeviceID，不限制可以不填</td>
</tr>

<tr bgcolor='#FFFFFF' align='center'>
<td>客户端ID</td>
<td align='left'><input type='text' name='clientId' value='<%=clientId %>'>
<select name='denyClientId'><option value='false' <%= (denyClientId==false?"selected":"")%> >不限制客户端登录</option>
<option value='true' <%= (denyClientId==true?"selected":"")%>>限制客户端登录</option></select>
</td>
<td></td>
</tr>

<tr bgcolor='#FFFFFF' align='center'>
<td>限制原因，用户登录时会显示给用户看</td>
<td  align='left'><input type='text' name='reason' value='<%=reason %>'></td>
<td></td>
</tr>
<tr bgcolor='#FFFFFF' align='center'>
<td>操作人员</td>
<td align='left'><input type='text' name='gm' value='<%=gm %>'></td>
<td></td>
</tr>

<tr bgcolor='#FFFFFF' align='center'>
<td>是否限制</td>
<td align='left'><select name='enableDeny'><option value='true' <%= (enableDeny==true?"selected":"")%> >限制登录</option>
<option value='false' <%= (enableDeny==false?"selected":"")%>>不限制登录</option></select>
<select name='foroverDeny'><option value='false' <%= (foroverDeny==false?"selected":"")%> >临时限制登录</option>
<option value='true' <%= (foroverDeny==true?"selected":"")%>>永久限制登录</option></select>
</td>
<td></td>
</tr>


<tr bgcolor='#FFFFFF' align='center'>
<td >限制时间</td>
<td align='left'><input type='text' name='days' size='5' value='<%=days %>'>天<input type='text' name='hours' size='5' value='<%=hours %>'>小时</td>
<td></td>
</tr>

</table>
<input type='submit' value='提交'>
</form>

<center>
<%= pageHtml %><br>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center">
	<td>ID</td><td>用户名</td><td>限制类型</td><td>限制设备</td><td>限制客户端</td>
	<td>开始时间</td><td>结束时间</td>
	<td>原因</td><td>GM</td><td>操作</td>
	<% 
		if(rmp != null && rmp.equals("ko")){
			out.print("<td>操作</td>");
		}
	%>
	</tr>
<%
	
	int index = 0;
	for(int i = 0 ; i < denyUserList.size() ; i++){
		DenyUserEntity du = denyUserList.get(i);
		if(i < (pageIndex-1)*50 || i >= (pageIndex)*50 ) continue;
		if (du.isEnableDeny() && du.isForoverDeny()) {
			out.println("<tr bgcolor='#FFFFFF' align='center'>");
		}else if (du.isEnableDeny() && du.getDenyStartTime()<= now && now <= du.getDenyEndTime()) {
			out.println("<tr bgcolor='#FFFFFF' align='center'>");
		}else {
			out.println("<tr bgcolor='#adb1fe' align='center'>");
		}
		out.println("<td>"+du.getId()+"</td>");
		
		out.println("<td>"+du.getUsername()+"</td>");
		if (du.isEnableDeny() && du.isForoverDeny()) {
			out.println("<td>永久封号</td>");
		}else if (du.isEnableDeny() && du.getDenyStartTime()<= now && now <= du.getDenyEndTime()) {
			out.println("<td>临时封号</td>");
		}else {
			out.println("<td>已解封</td>");
		}
		if(du.isDenyDeviceId()){
			out.println("<td>"+du.getDeviceId()+"</td>");
		}else{
			out.println("<td>不限制设备</td>");
		}
		if(du.isDenyClientId()){
			out.println("<td>"+du.getClientId()+"</td>");
		}else{
			out.println("<td>不限制客户端ID</td>");
		}
		out.println("<td>"+DateUtil.formatDate(new Date(du.getDenyStartTime()),"yyyy-MM-dd HH:mm:ss")+"</td>");
		out.println("<td>"+DateUtil.formatDate(new Date(du.getDenyEndTime()),"yyyy-MM-dd HH:mm:ss")+"</td>");
		out.println("<td>"+du.getReason()+"</td>");
		out.println("<td>"+du.getGm()+"</td>");
		if(queryEnabled){
			out.println("<td><a href='./deny_users.jsp?action2=query&queryType="+queryType+"&queryName="+queryName+"&action=modify&username="+du.getUsername()+"'>修改</a></td>");
			
		}else{
			out.println("<td><a href='./deny_users.jsp?action=modify&username="+du.getUsername()+"'>修改</a></td>");
		}
		if(rmp != null && rmp.equals("ko")){
			out.println("<td><a href='./deny_users.jsp?action=remove&username="+du.getUsername()+"'>删除</a></td>");
		}
		out.println("</tr>");
	}
	
%>	
</table><br>




</center>
</body>
</html> 
