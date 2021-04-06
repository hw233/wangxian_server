<%@ page contentType="text/html;charset=utf-8" import="java.util.*,
com.xuanzhi.tools.text.*,
com.xuanzhi.tools.transport.*,java.nio.channels.*,com.fy.gamegateway.mieshi.server.*"%>
<%@page import="com.xuanzhi.tools.text.*,com.xuanzhi.tools.page.* "%><%

int pageIndex = 1;
String preURL = "./deny_users.jsp";

//String beanName = "outer_gateway_connection_selector";
	MieshiServerInfoManager mm = MieshiServerInfoManager.getInstance();
	
	if("delete_all".equals(request.getParameter("action"))){
		ArrayList<MieshiServerInfoManager.DenyUser> duList = mm.getDenyUserList();
		for(int i = duList.size()-1 ; i >= 0 ; i--){
			MieshiServerInfoManager.DenyUser du = duList.get(i);
			if(du.denyStartTime < System.currentTimeMillis() && du.denyEndTime < System.currentTimeMillis() && du.foroverDeny == false){
				mm.removeDenyUser(du.username);
			}
		}
	}
	
	int totalDenyUserNum = 0;
	int totalForeverDenyUserNum = 0;
	long now = System.currentTimeMillis();
	ArrayList<MieshiServerInfoManager.DenyUser> duList = mm.getDenyUserList();
	for(int i = 0 ; i < duList.size() ; i++){
		MieshiServerInfoManager.DenyUser du = duList.get(i);
		if(du.enableDeny && du.foroverDeny ){
			totalDenyUserNum++;
			totalForeverDenyUserNum++;
		}else if(du.enableDeny && du.denyStartTime<= now && now <= du.denyEndTime){
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
	ArrayList<MieshiServerInfoManager.DenyUser> denyUserList = new ArrayList<MieshiServerInfoManager.DenyUser>();
	for(int i = 0 ; i < duList.size() ; i++){
		MieshiServerInfoManager.DenyUser du = duList.get(i);
		if(queryEnabled){
			if(queryType == 0){
				if(du.username != null && queryName.toLowerCase().contains(du.username.toLowerCase())){
					denyUserList.add(0,du);
				}
			}else if(queryType == 1){
				if(du.deviceId != null && du.deviceId.equalsIgnoreCase(queryName)){
					denyUserList.add(0,du);
				}
			}else if(queryType == 2){
				if(du.clientId != null && du.clientId.equalsIgnoreCase(queryName)){
					denyUserList.add(0,du);
				}
			} else if(queryType == 3){
				if(du.gm != null && du.gm.equalsIgnoreCase(queryName)){
					denyUserList.add(0,du);
				}
			}  
		}else{
			if(du.enableDeny && (du.foroverDeny ||  (du.enableDeny && du.denyStartTime<= now && now <= du.denyEndTime)) ){
				denyUserList.add(0,du);
			}
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

<h2>查询限制登录信息</h2>
<form><input type='hidden' name='action2' value='query'>
请输入信息：<select name='queryType'>
<option value='0' <%= (queryType==0?"selected":"")%> >按帐号查询</option>
<option value='1' <%= (queryType==1?"selected":"")%> >按设备ID查询</option>
<option value='2' <%= (queryType==2?"selected":"")%> >按客户端ID查询</option>
<option value='3' <%= (queryType==3?"selected":"")%> >按GM名字查询</option>
</select><input type='text' name='queryName' value='<%=queryName %>' >
<input type='submit' value='查询'>
</form>

<br/>
<%
String clientId = ParamUtils.getParameter(request,"clientId","");
String deviceId = ParamUtils.getParameter(request,"deviceId","");
String reason = ParamUtils.getParameter(request,"reason","");;
String gm = ParamUtils.getParameter(request,"gm","");;
boolean denyClientId = ParamUtils.getBooleanParameter(request,"denyClientId");
boolean denyDeviceId = ParamUtils.getBooleanParameter(request,"denyDeviceId");
boolean enableDeny = ParamUtils.getBooleanParameter(request,"enableDeny");;
boolean foroverDeny = ParamUtils.getBooleanParameter(request,"foroverDeny");;
int days = ParamUtils.getIntParameter(request,"days",0);
int hours = ParamUtils.getIntParameter(request,"hours",0);
String usernames = request.getParameter("usernames");


String action = request.getParameter("action");

if(action != null && action.equals("modify")){
	String[] userNames = usernames.split("\r*\n");
	for(String username : userNames)
	{
		MieshiServerInfoManager.DenyUser du = mm.getDenyUser(username);
		if(du != null){
			clientId = du.clientId;
			deviceId = du.deviceId;
			denyDeviceId = du.denyDeviceId;
			username = du.username;
			reason = du.reason;
			gm = du.gm;
			denyClientId = du.denyClientId;
			enableDeny = du.enableDeny;
			foroverDeny = du.foroverDeny;
			days = (int)((du.denyEndTime - System.currentTimeMillis())/(24*3600000));
			hours = (int)(((du.denyEndTime - System.currentTimeMillis()) - days * 24*3600000)/3600000 + 1);
		}
	}
}else if(action != null && action.equals("set_modify")){
	String[] userNames = usernames.split("\r*\n");
	for(String username : userNames)
	{
		if(enableDeny)
			mm.addDenyUser(deviceId,clientId,denyDeviceId,denyClientId,username,reason,gm,foroverDeny,days,hours);
		else if(!enableDeny){
			mm.disableDenyUser(username);
			mm.disableDenyUserByClientId(clientId);
		}
		
		out.println("<h2>已修改"+username+"限制信息</h2>");
	}
	
}else if(action != null && action.equals("remove")){
	String[] userNames = usernames.split("\r*\n");
	for(String username : userNames)
	{
		mm.removeDenyUser(username);
		out.println("<h2>已删除"+username+"限制信息</h2>");
	}
}
%>
<h2>添加修改限制用户</h2>
<form><input type='hidden' name='submitted' value='true'><input type='hidden' name='action' value='set_modify'>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor='#FFFFFF' align='center'>
<td>用户名</td>
<td align='left'><textarea rows="10" cols="10" name="usernames" value="<%=usernames%>"></textarea></td>
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
	<td>编号</td><td>用户名</td><td>限制类型</td><td>限制设备</td><td>限制客户端</td>
	<td>开始时间</td><td>结束时间</td>
	<td>原因</td><td>GM</td><td>操作</td><td>操作</td></tr>
<%
	
	int index = 0;
	for(int i = 0 ; i < denyUserList.size() ; i++){
		MieshiServerInfoManager.DenyUser du = denyUserList.get(i);
		if(i < (pageIndex-1)*50 || i >= (pageIndex)*50 ) continue;
			
		out.println("<tr bgcolor='#FFFFFF' align='center'>");
		out.println("<td>"+(i+1)+"</td>");
		
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
		out.println("<td>"+du.reason+"</td>");
		out.println("<td>"+du.gm+"</td>");
		if(queryEnabled){
			out.println("<td><a href='./deny_users.jsp?action2=query&queryType="+queryType+"&queryName="+queryName+"&action=modify&username="+du.username+"'>修改</a></td>");
			
		}else{
			out.println("<td><a href='./deny_users.jsp?action=modify&username="+du.username+"'>修改</a></td>");
		}
		out.println("<td><a href='./deny_users.jsp?action=remove&username="+du.username+"'>删除</a></td>");
		out.println("</tr>");
	}
	
%>	
</table><br>




</center>
</body>
</html> 
