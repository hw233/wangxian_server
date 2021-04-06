<%@ page contentType="text/html;charset=utf-8" import="java.util.*,
com.xuanzhi.tools.text.*,
com.xuanzhi.tools.transport.*,java.nio.channels.*,com.fy.gamegateway.mieshi.server.*
,com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,com.fy.boss.client.BossClientService,
com.fy.boss.authorize.model.Passport"%><%!

	String cacheFile = System.getProperty("resin.home")+"/webapps/game_gateway/WEB-INF/modifyPlayerDeviceCode.ddc";

	DefaultDiskCache cache = new DefaultDiskCache(new java.io.File(cacheFile),"modify-player-devicecode-cache",365L*24*3600*1000L);
	
%><%

	StatManager stat = StatManager.getInstance();
	BossClientService bcs = BossClientService.getInstance();

	String action = request.getParameter("action");
	if(action == null) action = "";
	
	String username = request.getParameter("username");

	Client client = null;
	Passport passport = null;
	ArrayList<String[]> records = new ArrayList<String[]>();
	if(username != null && username.trim().length() != 0){
		username = username.trim();
		
		List<ClientAccount> caList = stat.em4Account.query(ClientAccount.class, "username=? and hasSuccessLogin='T'", new Object[]{username}, "loginTimeForLast desc", 1, 2);
		if(caList.size() > 0){
			ClientAccount ca = caList.get(0);
			List<Client> cList = stat.em4Client.query(Client.class, "clientId=?", new Object[]{ca.getClientId()}, "", 1, 2);
			if(cList.size() > 0){
				client = cList.get(0);
			}
		}
		
		
		try{
			passport = bcs.getPassportByUserName(username);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		if(cache != null)
			records = (ArrayList<String[]>)cache.get(username);
		
		if(records == null) records = new ArrayList<String[]>();
		
	}
	
	if(action.equals("modify")){
		if(passport != null && client != null){
			String deviceCode = "";
			if(client.getPlatform().equals("IOS")){
				deviceCode = "UUID="+client.getUuid()+"MACADDRESS="+client.getMacAddress();
			}else{
				deviceCode = "DEVICEID="+client.getDeviceId()+"IMSI="+client.getImsi()+"MACADDRESS="+client.getMacAddress();
			}
			records.add(0,new String[]{DateUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"),username,client.getClientId(),client.getPlatform(),client.getPhoneType(),client.getChannel(),passport.getLastLoginIp(),deviceCode});

			if(cache != null){
				cache.put(username,records);
			}
			
			bcs.updateAppstoreIdentify(passport,deviceCode);
			
			try{
				passport = bcs.getPassportByUserName(username);
			}catch(Exception e){
				e.printStackTrace();
			}
			
			out.println("<h2>"+username+"的注册设备信息修改成功！</h2>");
		}
	}
	
%>
<%@page import="com.fy.gamegateway.stat.StatManager"%>
<%@page import="com.fy.gamegateway.stat.Client"%>
<%@page import="com.fy.gamegateway.stat.ClientAccount"%><html>
<head>
</head>
<body>
<form name="f1">
账号<input type="text" name="username" id="username" value="<%=(username != null?username:"") %>">
<input type="submit" value="查询">
</form>

<center>
<h2>修改玩家的注册设备信息</h2>
苹果官方版，如果注册信息和当前登录的设备信息不一致，无法充值！
<br>

<%
	if(client == null || passport == null){
		out.println("<h4>帐号["+username+"]对应的信息不存在，请提供正确的帐号</h4>");
	}else{
		String deviceCode = "";
		if(client.getPlatform().equals("IOS")){
			deviceCode = "UUID="+client.getUuid()+"MACADDRESS="+client.getMacAddress();
		}else{
			deviceCode = "DEVICEID="+client.getDeviceId()+"IMSI="+client.getImsi()+"MACADDRESS="+client.getMacAddress();
		}
		
		out.println("<h4>帐号["+username+"]对应的信息如下：</h4>");
		out.println("<form id='f2'><input type='hidden' name='action' value='modify'><input type='hidden' name='username' value='"+username+"'>");
		out.println("<table border='0' cellpadding='0' cellspacing='1' width='100%' bgcolor='#000000' align='center'>");
		out.println("<tr bgcolor='#FFFFFF'><td>注册设备信息</td><td>"+passport.getLastLoginIp()+"</td></tr>");
		out.println("<tr bgcolor='#FFFFFF'><td>注册渠道</td><td>"+passport.getRegisterChannel()+"</td></tr>");
		out.println("<tr bgcolor='#FFFFFF'><td>注册时间</td><td>"+passport.getRegisterDate()+"</td></tr>");
		out.println("<tr bgcolor='#FFFFFF'><td>注册ClientId</td><td>"+passport.getRegisterClientId()+"</td></tr>");
		out.println("<tr bgcolor='#FFFFFF'><td>注册机型</td><td>"+passport.getRegisterMobileType()+"</td></tr>");
		out.println("<tr bgcolor='#FFFFFF'><td>最后一次登录设备信息</td><td>"+deviceCode+"</td></tr>");
		out.println("<tr bgcolor='#FFFFFF'><td>最后一次登录渠道</td><td>"+client.getChannel()+"</td></tr>");
		out.println("<tr bgcolor='#FFFFFF'><td>最后一次登录时间</td><td>"+client.getLastLoginTime()+"</td></tr>");
		out.println("<tr bgcolor='#FFFFFF'><td>最后一次登录ClientId</td><td>"+client.getClientId()+"</td></tr>");
		out.println("<tr bgcolor='#FFFFFF'><td>最后一次登录机型</td><td>"+client.getPhoneType()+"</td></tr>");
		out.println("<tr bgcolor='#FFFFFF'><td><input type='submit' value='用最后一次登录设备信息替换注册信息'></td><td></td></tr>");
		out.println("</table>");
		out.println("</form>");
	}
%>

<h4>此帐号的修改记录</h4>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center">
	<td>修改时间</td>
	<td>帐号名</td>
	<td>上一次修改时的ClientId</td>
	<td>平台</td>
	<td>机型</td>
	<td>渠道</td>
	<td>修改前设备信息</td>
	<td>修改后设备信息</td>
</tr>
<%
	//	records.add(0,new String[]{DateUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"),username,client.getClientId(),client.getPlatform(),client.getPhoneType(),client.getChannel(),passport.getLastLoginIp(),deviceCode});
	for(String ss[] : records){
		out.println("<tr bgcolor='#FFFFFF'>");
		for(int j = 0 ; j < ss.length ; j++){
			out.println("<td>"+ss[j]+"</td>");
		}
		out.println("</tr>");
	}		
%>
</table>
</center>
</body>
</html> 
