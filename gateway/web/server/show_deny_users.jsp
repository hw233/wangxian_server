<%@ page contentType="text/html;charset=utf-8" import="java.util.*,
com.xuanzhi.tools.text.*,
com.xuanzhi.tools.transport.*,java.nio.channels.*,com.fy.gamegateway.mieshi.server.*"%>
<%@page import="com.xuanzhi.tools.text.*,com.xuanzhi.tools.page.* "%><%

int pageIndex = 1;
String preURL = "./show_deny_users.jsp";

//String beanName = "outer_gateway_connection_selector";
	MieshiServerInfoManager mm = MieshiServerInfoManager.getInstance();
	
	
	
	int totalDenyUserNum = 0;
	int totalForeverDenyUserNum = 0;
	long now = System.currentTimeMillis();
	ArrayList<DenyUserEntity> duList = mm.getDenyUserList();
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
			}  
		}else{
			if(du.isEnableDeny() && (du.isForoverDeny() ||  (du.isEnableDeny() && du.getDenyStartTime()<= now && now <= du.getDenyEndTime())) ){
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

<h2></h2><br><a href="./show_deny_users.jsp">刷新此页面</a><br>
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
%>


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
		DenyUserEntity du = denyUserList.get(i);
		if(i < (pageIndex-1)*50 || i >= (pageIndex)*50 ) continue;
			
		out.println("<tr bgcolor='#FFFFFF' align='center'>");
		out.println("<td>"+(i+1)+"</td>");
		
		out.println("<td>"+du.getUsername()+"</td>");
		if(du.isForoverDeny()){
			out.println("<td>永久封号</td>");
		}else{
			out.println("<td>临时封号</td>");
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
			out.println("<td></td>");
			
		}else{
			out.println("<td></td>");
		}
		out.println("<td></td>");
		out.println("</tr>");
	}
	
%>	
</table><br>




</center>
</body>
</html> 
