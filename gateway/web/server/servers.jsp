<%@ page contentType="text/html;charset=utf-8" import="java.util.*,
com.xuanzhi.tools.text.*,
com.xuanzhi.tools.transport.*,java.nio.channels.*,com.fy.gamegateway.mieshi.server.*,com.fy.gamegateway.thirdpartner.congzhi.* "%>
<%@page import="com.xuanzhi.tools.text.ParamUtils;"%><%



//String beanName = "outer_gateway_connection_selector";
	MieshiServerInfoManager mm = MieshiServerInfoManager.getInstance();
	
	String SERVERTYPE_NAMES[] = MieshiServerInfo.SERVERTYPE_NAMES;
	

	String categories[] = mm.getServerCategories();

	String category = "";
	int priority = 0;
	String name = "";
	String realName = "";
	String ip = "";
	int port = 0;
	int httpPort = 8001;
	int status = 0;
	int serverType = 0;
	String url = "";
	String desp = "";
	int clientId = 0;
	int serverId = 0;
	String action = "";
	action = request.getParameter("action");
	realName = request.getParameter("realName");
	
	if(action != null && action.equals("batchmodify")){
		String ss[] = request.getParameterValues("realName");
		for(int i = 0 ; i < ss.length ; i++){
			MieshiServerInfo si = mm.getServerInfoByName(ss[i]);
			if(si != null){
				si.setDescription(ParamUtils.getParameter(request,"desp",""));
				si.setStatus(ParamUtils.getIntParameter(request,"status",0));
				mm.updateServerInfo(si);
				
				out.println("<h2>服务器【"+ss[i]+"】信息修改成功</h2>");
			}
		}
		
	}else if(action != null && action.equals("modify")){
		MieshiServerInfo si = mm.getServerInfoByName(realName);
		if(si != null){
			category = si.getCategory();
			priority = si.getPriority();
			name = si.getName();
			realName = si.getRealname();
			ip = si.getIp();
			port = si.getPort();
			httpPort = si.getHttpPort();
			status = si.getStatus();
			url = si.getServerUrl();
			desp = si.getDescription();
			clientId = si.getClientid();
			serverId = si.getServerid();
			serverType = si.getServerType();
		}
		
	}else if(action != null && action.equals("set_category")){
		String s = request.getParameter("category");
		String ss[] = s.split(",");
		ArrayList<String> al = new ArrayList<String>();
		for(int i = 0 ; i < ss.length ; i++){
			if(ss[i].trim().length() > 0){
				al.add(ss[i].trim());
			}
		}
		mm.setServerCategories(al);
		
		out.println("<h2>分区信息修改成功</h2>");
	}else if(action != null && action.equals("add_server")){
		MieshiServerInfo si = mm.getServerInfoByName(realName);
		if(si == null){
			si = new MieshiServerInfo();
			si.setRealname(realName);
		}
		si.setCategory(request.getParameter("category"));
		si.setPriority(ParamUtils.getIntParameter(request,"priority",0));
		si.setIp(ParamUtils.getParameter(request,"ip",""));
		si.setClientid((byte) ParamUtils.getIntParameter(request,"clientId",0));
		si.setServerid((byte) ParamUtils.getIntParameter(request,"serverId",0));
		si.setServerUrl(ParamUtils.getParameter(request,"url", ""));
		si.setDescription(ParamUtils.getParameter(request,"desp",""));
		si.setName(ParamUtils.getParameter(request,"name","没有名字"));
		si.setStatus(ParamUtils.getIntParameter(request,"status",0));
		si.setServerType(ParamUtils.getIntParameter(request,"serverType",0));
		si.setPort(ParamUtils.getIntParameter(request,"port",0));
		si.setHttpPort(ParamUtils.getIntParameter(request,"httpport",0));
		
		mm.updateServerInfo(si);
		
		out.println("<h2>服务器信息修改成功</h2>");
	}else if(action != null && action.equals("delete")){
		MieshiServerInfo si = mm.getServerInfoByName(realName);
		if(si != null){
			mm.removeServerInfo(si);
			out.println("<h2>服务器信息删除成功</h2>");
		}
	}else if(action != null && action.equals("add_test_user")){
		String s1 = request.getParameter("username");
		String s2 = request.getParameter("reason");
		if(s1.trim().length() > 0){
			mm.addTestUser(s1,s2,true);
		}
		out.println("<h2>测试人员"+s1+"设置成功</h2>");
	}else if(action != null && action.equals("del_test_user")){
		String s1 = request.getParameter("username");
		mm.removeTestUser(s1);
		out.println("<h2>测试人员"+s1+"设置成功</h2>");
	}else if(action != null && action.equals("enable_test_user")){
		String s1 = request.getParameter("username");
		MieshiServerInfoManager.TestUser tu = mm.getTestUser(s1);
		if(tu != null){
			mm.addTestUser(s1,tu.reason,!tu.enable);
		}
		out.println("<h2>测试人员"+s1+"设置成功</h2>");
	}else if(action != null && action.equals("add_tuijian")){
		realName = request.getParameter("realName");
		mm.addTuijianServerInfo(realName);
		
		out.println("<h2>推荐服务器"+realName+"设置成功</h2>");
	}else if(action != null && action.equals("delete_tuijian")){
		realName = request.getParameter("realName");
		mm.removeTuijianServerInfo(realName);
		
		out.println("<h2>推荐服务器"+realName+"删除成功</h2>");
	}
	
	categories = mm.getServerCategories();
	StringBuffer categorySB = new StringBuffer();
	for(int i = 0 ; i < categories.length ; i++){
		categorySB.append(categories[i]);
		if(i < categories.length -1){
			categorySB.append(",");
		}
	}
	String colors[] = new String[]{"#FFFFFF","#888888","#888888","#ff00ff","#FFFFFF","#FFFFFF",
			"#888888","#FFFFFF","#FFFFFF","#FFFFFF","#FFFFFF","#FFFFFF","#FFFFFF","#FFFFFF","#FFFFFF","#FFFFFF","#FFFFFF"};
	
%><html>
<head>
</head>
<body>
<center>
<h2></h2><br><a href="./servers.jsp">刷新此页面</a> | <a href="./servers.jsp?batch=true">批量修改服务器状态</a><br>
<%
	boolean  batch = false;
	if("true".equals(request.getParameter("batch"))){
		batch = true;
	}
	if(batch){
%>
<form><input type='hidden' name='action' value='batchmodify'>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center">
	<td>标记</td>
	<td>分区</td><td>显示名称</td>
	<td>状态</td><td>类型</td><td>描述</td></tr>
<%
	int index = 0;
	for(int i = 0 ; i < categories.length ; i++){
		MieshiServerInfo si[] = mm.getServerInfoSortedListByCategory(categories[i]);
		for(int j = 0 ; j < si.length ; j++){
			MieshiServerInfo s = si[j];
			index++;
			String color = "#FFFFFF";
			if(s.getServerType() < colors.length) color = colors[s.getServerType()];
			out.println("<tr bgcolor='"+color+"' align='center'>");
			out.println("<td><input type='checkbox' name='realName' value='"+s.getRealname()+"'></td>");
			out.println("<td>"+s.getCategory()+"</td>");
			out.println("<td>"+s.getName()+"</td>");
			out.println("<td>"+MieshiServerInfo.STATUS_DESP[s.getStatus()]+"</td>");
			out.println("<td>"+SERVERTYPE_NAMES[s.getServerType()]+"</td>");
			out.println("<td>"+s.getDescription()+"</td>");
			out.println("</tr>");
		}
	}
%>	
<tr bgcolor='#FFFFFF' align='center'>
<td>状态</td>
<td><input type='text' name='status' value='<%=status %>'></td>
<td colspan='4'><% for(int i = 0 ; i < MieshiServerInfo.STATUS_DESP.length ; i++){
	out.print(i+"="+MieshiServerInfo.STATUS_DESP[i]+"，");
	
} %></td>
</tr>

<tr bgcolor='#FFFFFF' align='center'>
<td>描述</td>
<td><input type='text' name='desp' value='<%=desp %>'></td>
<td colspan='4'>如果填写，用户将看到这个描述，如果不填，有服务器自动产生描述，特别适用于维护</td>
</tr>

<tr bgcolor='#FFFFFF' align='center'>
<td colspan='6'><input type='submit' value='提交'></td>
</tr>
</table></form>

<br>

<%
	}
%>

<br>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center">
	<td>编号</td>
	<td>分区</td>
	<td>优先级</td><td>显示名称</td><td>真实名称</td>
	<td>在线人数</td><td>IP/Port</td><td>http地址</td>
	<td>状态</td><td>类型</td><td>描述</td><td>头标识</td><td>操作</td></tr>
<%
	int totalPlayerNum = 0;

	int index = 0;
	for(int i = 0 ; i < categories.length ; i++){
		MieshiServerInfo si[] = mm.getServerInfoSortedListByCategory(categories[i]);
		for(int j = 0 ; j < si.length ; j++){
			MieshiServerInfo s = si[j];
			index++;
			String color = "#FFFFFF";
			if(s.getServerType() < colors.length) color = colors[s.getServerType()];
			totalPlayerNum += s.getOnlinePlayerNum();
			out.println("<tr bgcolor='"+color+"' align='center'>");
			out.println("<td>"+index+"</td>");
			out.println("<td>"+s.getCategory()+"</td>");
			out.println("<td>"+s.getPriority()+"</td>");
			out.println("<td>"+s.getName()+"</td>");
			out.println("<td>"+s.getRealname()+"</td>");
			out.println("<td>"+s.getOnlinePlayerNum()+"</td>");
			out.println("<td>"+s.getIp()+":"+s.getPort()+"</td>");
			out.println("<td><a href='http://"+s.getIp()+":"+s.getHttpPort()+"/' target='_blank'>"+s.getHttpPort()+"</a></td>");
			out.println("<td>"+MieshiServerInfo.STATUS_DESP[s.getStatus()]+"</td>");
			out.println("<td>"+SERVERTYPE_NAMES[s.getServerType()]+"</td>");
			out.println("<td>"+s.getDescription()+"</td>");
			out.println("<td>"+StringUtil.toHex(new byte[]{s.getClientid(),s.getServerid()})+"</td>");
			out.println("<td><a href='./servers.jsp?action=modify&realName="+s.getRealname()+"'>修改</a> | <a href='./servers.jsp?action=delete&realName="+s.getRealname()+"'>删除</a></td>");
			
			out.println("</tr>");
			
			if(s.getServerUrl() == null || s.getServerUrl().length() == 0) {
				//没有后台地址，进行一次自动设置
				String portStr = String.valueOf(s.getPort());
				s.setServerUrl("http://" + s.getIp() + ":" + "800" + portStr.substring(portStr.length()-1));
				mm.updateServerInfo(s);
			}
		}
	}
	
%>	
</table><br>
<h2>总在线人数:<%=totalPlayerNum %></h2>
<br>
<h2>推荐服务器列表</h2>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center">
	<td>编号</td>
	<td>分区</td>
	<td>优先级</td><td>显示名称</td><td>真实名称</td>
	<td>在线人数</td><td>IP/Port</td>
	<td>状态</td><td>类型</td><td>描述</td><td>头标识</td><td>操作</td></tr>
<% 
MieshiServerInfo si[] = mm.getTuijianServerInfoList();
for(int j = 0 ; j < si.length ; j++){
	MieshiServerInfo s = si[j];
	index++;
	
	out.println("<tr bgcolor='#FFFFFF' align='center'>");
	out.println("<td>"+index+"</td>");
	out.println("<td>"+s.getCategory()+"</td>");
	out.println("<td>"+s.getPriority()+"</td>");
	out.println("<td>"+s.getName()+"</td>");
	out.println("<td>"+s.getRealname()+"</td>");
	out.println("<td>"+s.getOnlinePlayerNum()+"</td>");
	out.println("<td>"+s.getIp()+":"+s.getPort()+"</td>");
	out.println("<td>"+MieshiServerInfo.STATUS_DESP[s.getStatus()]+"</td>");
	out.println("<td>"+SERVERTYPE_NAMES[s.getServerType()]+"</td>");
	out.println("<td>"+s.getDescription()+"</td>");
	out.println("<td>"+StringUtil.toHex(new byte[]{s.getClientid(),s.getServerid()})+"</td>");
	out.println("<td><a href='./servers.jsp?action=delete_tuijian&realName="+s.getRealname()+"'>删除</a></td>");
	
	out.println("</tr>");
}
%>	
</table>

<h2>添加推荐服务器</h2>
<form><input type='hidden' name='submitted' value='true'><input type='hidden' name='action' value='add_tuijian'>
服务器真实名称：<input type='text' name='realName' value=''><input type='submit' value='提交'>
</form>

<br/>
<h2>分区设置</h2>
<form><input type='hidden' name='submitted' value='true'><input type='hidden' name='action' value='set_category'>
设置分区：<input type='text' name='category' value='<%=categorySB.toString()%>'><input type='submit' value='提交'>
</form>


<h2>添加修改服务器</h2>
<form><input type='hidden' name='submitted' value='true'><input type='hidden' name='action' value='add_server'>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor='#FFFFFF' align='center'>
<td>分区</td>
<td><input type='text' name='category' value='<%=category %>'></td>
<td></td>
</tr>
<tr bgcolor='#FFFFFF' align='center'>
<td>优先级</td>
<td><input type='text' name='priority' value='<%=priority %>'></td>
<td>数值越大，排位靠前</td>
</tr>
<tr bgcolor='#FFFFFF' align='center'>
<td>显示名称</td>
<td><input type='text' name='name' value='<%=name %>'></td>
<td></td>
</tr>
<tr bgcolor='#FFFFFF' align='center'>
<td>真实名称</td>
<td><input type='text' name='realName' value='<%=realName %>'></td>
<td></td>
</tr>
<tr bgcolor='#FFFFFF' align='center'>
<td>IP</td>
<td><input type='text' name='ip' value='<%=ip %>'></td>
<td></td>
</tr>
<tr bgcolor='#FFFFFF' align='center'>
<td>端口</td>
<td><input type='text' name='port' value='<%=port %>'></td>
<td></td>
</tr>

<tr bgcolor='#FFFFFF' align='center'>
<td>后台HTTP端口</td>
<td><input type='text' name='port' value='<%= httpPort %>'></td>
<td></td>
</tr>

<tr bgcolor='#FFFFFF' align='center'>
<td>状态</td>
<td><input type='text' name='status' value='<%=status %>'></td>
<td><% for(int i = 0 ; i < MieshiServerInfo.STATUS_DESP.length ; i++){
	out.print(i+"="+MieshiServerInfo.STATUS_DESP[i]+"，");
	
} %></td>
</tr>

<tr bgcolor='#FFFFFF' align='center'>
<td>类型</td>
<td><input type='text' name='serverType' value='<%=serverType %>'></td>
<td><% for(int i = 0 ; i < SERVERTYPE_NAMES.length ; i++){
	out.print(i+"="+SERVERTYPE_NAMES[i]+"，");
	
} %></td>
</tr>

<tr bgcolor='#FFFFFF' align='center'>
<td>后台URL</td>
<td><input type='text' name='url' value='<%=url %>'></td>
<td>&nbsp;</td>
</tr>

<tr bgcolor='#FFFFFF' align='center'>
<td>描述</td>
<td><input type='text' name='desp' value='<%=desp %>'></td>
<td>如果填写，用户将看到这个描述，如果不填，有服务器自动产生描述，特别适用于维护</td>
</tr>

<tr bgcolor='#FFFFFF' align='center'>
<td>clientId</td>
<td><input type='text' name='clientId' value='<%=clientId %>'></td>
<td>用于协议头，共F5分发用，目前此值可以随意填</td>
</tr>

<tr bgcolor='#FFFFFF' align='center'>
<td>serverId</td>
<td><input type='text' name='serverId' value='<%=serverId %>'></td>
<td>用于协议头，共F5分发用，目前此值可以随意填</td>
</tr>
</table>
<input type='submit' value='提交'>
</form>

<h2>测试帐号信息，这些帐号在服务器是内部测试时，可以进入游戏</h2>

<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center">
	<td>测试帐号</td>
	<td>添加的时间</td>
	<td>最后修改时间</td><td>添加的原因</td><td>是否生效</td>
	<td>操作</td><td>删除</td>
	</tr>
<%
	ArrayList<MieshiServerInfoManager.TestUser> tt = mm.getTestUsers();
	for(int i = 0 ; i < tt.size() ; i++){
		MieshiServerInfoManager.TestUser t = tt.get(i);
		out.println("<tr bgcolor='#FFFFFF' align='center'>");
		out.println("<td>"+t.username+"</td>");
		out.println("<td>"+DateUtil.formatDate(t.createTime,"yyyy-MM-dd")+"</td>");
		out.println("<td>"+DateUtil.formatDate(t.lastModifiedTime,"yyyy-MM-dd")+"</td>");
		out.println("<td>"+t.reason+"</td>");
		out.println("<td>"+t.enable+"</td>");
		out.println("<td><a href='./servers.jsp?action=enable_test_user&username="+t.username+"'>"+(t.enable?"失效":"生效")+"</a></td>");
		out.println("<td><a href='./servers.jsp?action=del_test_user&username="+t.username+"'>删除</a></td>");
		out.println("</tr>");
	}
	%>
</table>
添加测试帐号：
<form><input type='hidden' name='action' value='add_test_user'>
请输入用户名:<input type='text' name='username' value=''><br/>
添加的原因:<input type='text' name='reason' value=''><br/> 	
<input type='submit' value='提交'>
</form>


</center>
</body>
</html> 
