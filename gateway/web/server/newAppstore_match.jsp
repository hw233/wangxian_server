<%@page import="com.fy.gamegateway.mieshi.waigua.ClientAuthorization"%>
<%@page import="com.fy.gamegateway.mieshi.waigua.AuthorizeManager"%>
<%@page import="com.fy.gamegateway.mieshi.waigua.ClientEntity"%>
<%@ page contentType="text/html;charset=utf-8" import="java.util.*,
com.xuanzhi.tools.text.*,
com.xuanzhi.tools.transport.*,java.nio.channels.*,org.apache.log4j.Logger,
com.fy.gamegateway.stat.*,com.fy.gamegateway.mieshi.server.*,com.fy.gamegateway.tools.*"%>
<%

	long now = System.currentTimeMillis();

	String action = request.getParameter("action");
	if(action == null) action = "";
	String channel = request.getParameter("channel");
	
	String startDate = request.getParameter("startDate");
	if(startDate == null) startDate = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
	
	int type = 1;
	
	StatManager stat = StatManager.getInstance();
	
	ArrayList<ClientEntity> al = new ArrayList<ClientEntity>();
	ArrayList<List<ClientAuthorization>> cass = new ArrayList<List<ClientAuthorization>>();
	int total = 0;
	if(action.equals("match")){
		String st = request.getParameter("type");
		type = Integer.parseInt(st);
		
		String data = request.getParameter("data");
		
		String items[] = data.split("\r*\n");
		total = items.length;
		
		long startTime = DateUtil.parseDate(startDate,"yyyy-MM-dd").getTime();
		
		
		for(int i = 0 ; i < items.length ; i++){
			String s = items[i].trim();
			if(s.length() == 0) continue;
			List<ClientEntity> list = AuthorizeManager.getInstance().em_ce.query(ClientEntity.class, "mac=?", new Object[]{s}, "createTime desc", 1, 2);
			ClientEntity entity = null;
			if (list.size() > 0) {
				entity = list.get(0);
			}
			if (entity != null) {
				if (entity.getCreateTime() >= startTime) {
					if (al.contains(entity) == false) {
						al.add(entity);
					}
				}
			}
		}
	}
%>
<html>
<head>
</head>
<body>
<h2>Appstore比对工具</h2>

<%
	if(action.equals("match")){
		int iphone = 0;
		int ipad = 0;
		int other = 0;
		for(int i = 0 ; i < al.size() ;i++){
			ClientEntity c = al.get(i);
			if(c.getPhoneType().toLowerCase().contains("iphone")){
				iphone++;
			}else if(c.getPhoneType().toLowerCase().contains("ipad")){
				ipad++;
			}else{
				other++;
			}
		}
%>
	<h3>匹配结果：输入<%=total %>个设备，成功匹配<%= al.size() %>，转化率为<%= (al.size()*1000/total)/10.0 %>%，iPhone为<%=iphone %>，iPad为<%=ipad %>，其他为<%=other %></h3>
	<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
		<tr bgcolor="#00FFFF" align="center">
		<td>编号</td>
		<td>ClientID</td>
		<td>MAC地址</td>
		<td>第一次联网时间</td>
		<td>最后登录用户</td>
		<td>登录次数</td>
		<td>注册用户</td>
		<td>渠道</td>
		<td>机型</td>
		</tr>
<%
		for(int i = 0 ; i < al.size() ;i++){
			ClientEntity c = al.get(i);
			List<ClientAuthorization> cas = AuthorizeManager.getInstance().em_ca.query(ClientAuthorization.class, "clientID=? and lastLoginTime>? and authorizeType=?", new Object[]{c.getClientID(), c.getCreateTime(), 0}, "", 1, 1000);
			cass.add(cas);
			int loginTimes = 0;
			String userName = "";
			for (ClientAuthorization ca : cas) {
				loginTimes += ca.getLoginNum();
				userName = userName + ca.getUsername() + ",";
			}
			out.println("<tr bgcolor='#FFFFFF' align='center'>");
			out.println("<td>"+(i+1)+"</td>");
			out.println("<td>"+c.getClientID()+"</td>");
			out.println("<td>"+c.getMac()+"</td>");
			out.println("<td>"+DateUtil.formatDate(new Date(c.getCreateTime()),"yyyy-MM-dd HH:mm:ss")+"</td>");
			out.println("<td>"+DateUtil.formatDate(new Date(c.getLastActiveTime()),"yyyy-MM-dd HH:mm:ss")+"</td>");
			out.println("<td>"+loginTimes+"</td>");
			out.println("<td>"+userName+"</td>");
			out.println("<td>"+c.getChannel()+"</td>");
			out.println("<td>"+c.getPhoneType()+"</td>");
		}
	}
%>
	</table>

<h3>请将合作方提供的UDID或者MAC地址拷贝在输入框中</h3>
<form method=post><input type='hidden' name='action' value='match'>
请输入开始日期：<input type='text' name='startDate' value='<%=startDate %>'>(yyyy-MM-dd)<br>
请输入新渠道名称：<input type='text' name='channel' value='<%= channel %>'>(比如APPSTORE_MIESHI_RUANLIE,APPSTORE_MIESHI_ADSAGE,APPSTORE_MIESHI_TAPJOY,APPSTORE_MIESHI_PINGGUOYUAN,APPSTORE_MIESHI_YOUMENG)<br>
请选择比对的类型: <input type='radio' name='type' value='1' <%= (type==1?"checked":"") %>>MACADDRESS<br>
请输入MACADDRESS:<br>
<textarea rows="30" cols="70" name='data'>
<%
	for(int i = 0 ; i < al.size() ;i++){
		ClientEntity c = al.get(i);
		List<ClientAuthorization> cas = cass.get(i);
		for(ClientAuthorization ca : cas){
			out.println("APPSTORE_MIESHI,"+ca.getUsername()+","+channel);
		}
	}
%>
</textarea><br>
<input type='submit' value='提交'>
</form>
</body></html>