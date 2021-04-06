<%@ page contentType="text/html;charset=utf-8" import="java.util.*,
com.xuanzhi.tools.text.*,
com.xuanzhi.tools.transport.*,java.nio.channels.*,com.fy.gamegateway.mieshi.server.*,com.fy.gamegateway.thirdpartner.congzhi.* "%>

<%!
	static List<String> notShowServers = new ArrayList<String>();
	static {
		notShowServers.add("巍巍昆仑");
		notShowServers.add("百里沃野");
		notShowServers.add("正式发布服");
		notShowServers.add("13测试发布");
	//	notShowServers.add("幽恋蝶谷");
		notShowServers.add("pan2");
		notShowServers.add("潘多拉星");
		notShowServers.add("台湾测试服");
	}
	static List<String> areas = new ArrayList<String>();//所有区 目前只支持到十区,以后直接加就行
	static {
		areas.add("一区");
		areas.add("二区");
		areas.add("三区");
		areas.add("四区");
		areas.add("五区");
		areas.add("六区");
		areas.add("七区");
		areas.add("八区");
		areas.add("九区");
		areas.add("十区");
	}
	Comparator<MieshiServerInfo> order = new Comparator<MieshiServerInfo>(){
		public int compare(MieshiServerInfo o1, MieshiServerInfo o2) {
			if(o1.getServerType() == o2.getServerType()){//先按类型排序
				if (getAreaIndex(o1.getCategory()) == getAreaIndex(o2.getCategory())) {
					return o2.getPriority() - o1.getPriority();
				}
				return getAreaIndex(o1.getCategory()) - getAreaIndex(o2.getCategory());
			}
			return o1.getServerType() - o2.getServerType();
		}
	};
	public int getAreaIndex(String areaName){
		if (areas.contains(areaName)) {
			return areas.indexOf(areaName);
		}
		return areas.size() + 1;
	}
	public String getServerTypeChangeHTML(int nextServerType){
		StringBuffer sbf = new StringBuffer();
		sbf.append("<tr style='background-color:#AC75EA;font-size: 14px;font-weight: bold;text-align: center; '>");
		sbf.append("<td colspan='12' title='"+nextServerType+"'>"+MieshiServerInfo.SERVERTYPE_NAMES[nextServerType]+"</td>");
		sbf.append("</tr>");
		return sbf.toString();
	}
%><%


//String beanName = "outer_gateway_connection_selector";
	MieshiServerInfoManager mm = MieshiServerInfoManager.getInstance();
	
	String SERVERTYPE_NAMES[] = MieshiServerInfo.SERVERTYPE_NAMES;
	

	String categories[] = mm.getServerCategories();

	Set<String> tuijianServers = new HashSet<String>();//所有推荐服务器列表 realNames
	MieshiServerInfo siArr[] = mm.getTuijianServerInfoList();
	for (MieshiServerInfo msi : siArr) {
		tuijianServers.add(msi.getRealname());
	}
	String category = "";
	int priority = 0;
	String name = "";
	String realName = "";
	String ip = "";
	int port = 0;
	int status = 0;
	String url = "";
	int serverType = 0;
	String desp = "";
	int clientId = 0;
	int serverId = 0;
	String action = "";
	action = request.getParameter("action");
	realName = request.getParameter("realName");
	
	boolean showAllServer = "true".equals(request.getParameter("showAllServer"));
	
	if(action != null && action.equals("batchmodify")){
		String ss[] = request.getParameterValues("choose");
		int index = 0;
		for(int i = 0 ; i < ss.length ; i++){
			MieshiServerInfo si = mm.getServerInfoByName(ss[i]);
			if(si != null){
				si.setDescription(ParamUtils.getParameter(request,"desp",""));
				si.setStatus(ParamUtils.getIntParameter(request,"status",0));
				mm.updateServerInfo(si);
				
				out.print("【"+ss[i]+"】&nbsp;&nbsp;&nbsp;&nbsp;");
				if (index++ % 14 == 13) {
					out.print("<hr>");
				}
			}
		}
		out.print("<h2>批量修改服务器状态完成["+ParamUtils.getParameter(request,"desp","") + "/" + ParamUtils.getIntParameter(request,"status",0)+"]</h2>");
		
	}else if(action != null && action.equals("modify")){
		MieshiServerInfo si = mm.getServerInfoByName(realName);
		if(si != null){
			category = si.getCategory();
			priority = si.getPriority();
			name = si.getName();
			realName = si.getRealname();
			ip = si.getIp();
			port = si.getPort();
			status = si.getStatus();
			desp = si.getDescription();
			clientId = si.getClientid();
			serverId = si.getServerid();
			serverType = si.getServerType();
			url = si.getServerUrl();
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
		si.setDescription(ParamUtils.getParameter(request,"desp",""));
		si.setName(ParamUtils.getParameter(request,"name","没有名字"));
		si.setStatus(ParamUtils.getIntParameter(request,"status",0));
		si.setServerType(ParamUtils.getIntParameter(request,"serverType",0));
		si.setPort(ParamUtils.getIntParameter(request,"port",0));
		si.setServerUrl(ParamUtils.getParameter(request,"url", ""));
		mm.updateServerInfo(si);
		
		out.println("<h2>服务器信息修改成功</h2>");
	}else if(action != null && action.equals("delete")){
		MieshiServerInfo si = mm.getServerInfoByName(realName);
		if(si != null){
			mm.removeServerInfo(si);
			out.println("<h2>服务器信息删除成功</h2>");
		}
	}/* else if(action != null && action.equals("add_test_user")){
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
	} */else if(action != null && action.equals("add_tuijian")){
		realName = request.getParameter("realName");
		mm.addTuijianServerInfo(realName);
		
		out.println("<h2>推荐服务器"+realName+"设置成功</h2>");
	}else if(action != null && action.equals("delete_tuijian")){
		realName = request.getParameter("realName");
		mm.removeTuijianServerInfo(realName);
		
		out.println("<h2>推荐服务器"+realName+"删除成功</h2>");
	}
	else if(action != null && action.equals("add_newserver")){
		realName = request.getParameter("realName");
		mm.addNewServerInfo(realName);
		
		out.println("<h2>最新服务器"+realName+"设置成功</h2>");
	}
	else if(action != null && action.equals("delete_newserver")){
		realName = request.getParameter("realName");
		mm.removeNewServerInfo(realName);
		
		out.println("<h2>最新服务器"+realName+"删除成功</h2>");
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
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	Map<Integer,List<MieshiServerInfo>> serverStatuses = new LinkedHashMap<Integer,List<MieshiServerInfo>>();//每个状态下游戏服的个数
	for (int i = 0; i < MieshiServerInfo.STATUS_DESP.length;i++) {
		serverStatuses.put(i,new ArrayList<MieshiServerInfo>());
	}
	
	List<MieshiServerInfo> showServers = new ArrayList<MieshiServerInfo>();//所有要显示的服务器
	for(int i = 0 ; i < categories.length ; i++){
		MieshiServerInfo si[] = mm.getServerInfoSortedListByCategory(categories[i]);
		for(int j = 0 ; j < si.length ; j++){
			MieshiServerInfo s = si[j];
			String serverName = s.getName();
			boolean needShow = true;
			if (!showAllServer) {
				if (notShowServers.contains(serverName)) {
					needShow = false;
				}
			}
			if (needShow) {
				showServers.add(s);
				if(serverStatuses.get(s.getStatus()) != null)
				{
					serverStatuses.get(s.getStatus()).add(s);
				}
				else
				{
					s.setStatus(6);
					mm.updateServerInfo(s);
				}
			}
		}
	}
	Collections.sort(showServers,order);
%><html>

<head>
</head>
<body  style="font-size: 12px; background-color: #c8edcc">
<center>
<br><a href="./serversNew.jsp">刷新此页面</a> | <a href="./serversNew.jsp?batch=true">批量修改服务器状态</a>
|<a href="./serversNew.jsp?showAllServer=true">显示所有服务器</a>|<a href="./PageJump.jsp">所有服状态</a><br>
<div>
	服务器状态统计:<BR/>
	<%
		for (Iterator<Integer> itor = serverStatuses.keySet().iterator();itor.hasNext();) {
			int serverStatus = itor.next();
			List<MieshiServerInfo> list = serverStatuses.get(serverStatus);
			StringBuffer notice = new StringBuffer();
			for (int i = 0; i < list.size();i++) {
				notice.append("["+i+"]");
				notice.append(list.get(i).getRealname());
				notice.append("&nbsp;&nbsp;");
				if (i % 10 == 9) {
					notice.append("&#10;");
				}
			}
		%>
			<span style="margin-left: 12px;float: left;background-color: #A7F275" title="<%=notice.toString()%>"><%=MieshiServerInfo.STATUS_DESP[serverStatus] %>:<%=serverStatuses.get(serverStatus).size() %></span>
		<%
		}
	%>
</div>
<%
	boolean  batch = false;
	if("true".equals(request.getParameter("batch"))){
		batch = true;
	}
	if(batch){
		//批量修改服务器状态
%>
<br>
<hr>
<form method="post"><input type='hidden' name='action' method='post' value='batchmodify'>
<table style="font-size: 12px;width: 100%;text-align: center;">
<tr bgcolor="#00FFFF" align="center">
	<td>全选<input type="checkbox" id="chooseAll" name="chooseAll" onclick="changeSelect()"></td>
	<td>编号</td>
	<td>分区</td><td>显示名称</td>
	<td>状态</td><td>类型</td><td>描述</td></tr>
<%
		int index = 0;
		int currentServerType = -1;
		for(MieshiServerInfo s : showServers){
			if (s.getServerType() != currentServerType) {
				currentServerType = s.getServerType();
				out.print(getServerTypeChangeHTML(currentServerType));
			}
			
			%>
			<tr align="center" onmouseover = 'overTag(this);' onmouseout = 'outTag(this);'>
				<td><input type="checkbox" name="choose" value="<%=s.getRealname()%>"></td>
				<td><%=index++ %></td>
				<td><%=s.getCategory() %></td>
				<td><%=s.getName() %></td>
				<td><%=MieshiServerInfo.STATUS_DESP[s.getStatus()] %></td>
				<td><%=SERVERTYPE_NAMES[s.getServerType()] %></td>
				<td><%=s.getDescription() %></td>
			</tr>
			<%}%>	
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
<hr>
<%
	}
%>

<br>
<table border="0" cellpadding="0" cellspacing="1" width="100%" align="center" style="font-size: 12px;">
<tr bgcolor="#00FFFF" align="center">
	<td>编号</td>
	<td>分区</td>
	<td>优先级</td>
	<td>显示名称</td>
	<td>真实名称</td>
	<td>在线人数</td>
	<td>IP/Port</td>
	<td>状态</td>
	<td>类型</td>
	<td>描述</td>
	<td>操作</td>
</tr>
<%
		int index = 0;
		int totalPlayerNum = 0;
		int currentServerType = -1;
		for(MieshiServerInfo s : showServers){
			if (s.getServerType() != currentServerType) {
				currentServerType = s.getServerType();
				out.print(getServerTypeChangeHTML(currentServerType));
			}
			totalPlayerNum += s.getOnlinePlayerNum();
			boolean inTuijian = tuijianServers.contains(s.getRealname());
			boolean innerUserOnly = (s.getServerType() == 2 || s.getStatus() == 0 ||s.getStatus() == 5 || s.getStatus() == 6);
			%>
			<tr align="center" style="background-color:  <%=innerUserOnly ? "#B7B7B7" : ""%>" onmouseover = 'overTag(this);' onmouseout = 'outTag(this);'>
				<td title="<%=currentServerType%>"><%=index++ %></td>
				<td><%=s.getCategory() %></td>
				<td><%=s.getPriority() %></td>
				<td style="<%=inTuijian?"color: red;font-weight: bold;":""%>"><%=(inTuijian ? "[荐]" : "" ) +  s.getName() %></td>
				<td><%=s.getRealname() %></td>
				<td><%=s.getOnlinePlayerNum() %></td>
				<td><%=s.getIp()+":"+s.getPort() %></td>
				<td><%=MieshiServerInfo.STATUS_DESP[s.getStatus()] %></td>
				<td><%=SERVERTYPE_NAMES[s.getServerType()] %></td>
				<td><%=s.getDescription() %></td>
				<td><a href="./serversNew.jsp?action=modify&realName=<%=s.getRealname() %>">修改</a>
				 | <a href="./serversNew.jsp?action=delete&realName=<%=s.getRealname() %>">删除</a></td> 
			</tr>
			<%}%>	
</table><br>
<h2>总在线人数:<%=totalPlayerNum %></h2>
<h2>推荐服务器列表</h2>
<hr/>
<table style="font-size: 12px;text-align: center;width: 100%">
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
	
	out.println("<tr bgcolor='#FFFFFF' align='center' onmouseover = 'overTag(this);' onmouseout = 'outTag(this);'>");
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
	out.println("<td><a href='./serversNew.jsp?action=delete_tuijian&realName="+s.getRealname()+"'>删除</a></td>");
	
	out.println("</tr>");
}
%>	
</table>

<h2>添加推荐服务器</h2>
<form><input type='hidden' name='submitted' value='true'><input type='hidden' name='action' value='add_tuijian'>
服务器真实名称：<input type='text' name='realName' value=''><input type='submit' value='提交'>
</form>
<hr/>
<h2>新服务器列表</h2>
<hr/>
<table style="font-size: 12px;text-align: center;width: 100%">
<tr bgcolor="#00FFFF" align="center">
	<td>编号</td>
	<td>分区</td>
	<td>优先级</td><td>显示名称</td><td>真实名称</td>
	<td>在线人数</td><td>IP/Port</td>
	<td>状态</td><td>类型</td><td>描述</td><td>头标识</td><td>操作</td></tr>
<% 
MieshiServerInfo msi[] = mm.getNewServerInfoList();
for(int j = 0 ; j < msi.length ; j++){
	MieshiServerInfo s = msi[j];
	index++;
	
	out.println("<tr bgcolor='#FFFFFF' align='center' onmouseover = 'overTag(this);' onmouseout = 'outTag(this);'>");
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
	out.println("<td><a href='./serversNew.jsp?action=delete_newserver&realName="+s.getRealname()+"'>删除</a></td>");
	
	out.println("</tr>");
}
%>	
</table>

<h2>添加新服务器</h2>
<form><input type='hidden' name='submitted' value='true'><input type='hidden' name='action' value='add_newserver'>
服务器真实名称：<input type='text' name='realName' value=''><input type='submit' value='提交'>
</form>
<hr/>


<h2>分区设置</h2>
<form><input type='hidden' name='submitted' value='true'><input type='hidden' name='action' value='set_category'>
设置分区：<input type='text' name='category' value='<%=categorySB.toString()%>'><input type='submit' value='提交'>
</form>


<h2>添加修改服务器</h2>
<hr/>
<form><input type='hidden' name='submitted' value='true'><input type='hidden' name='action' value='add_server'>
<table style="text-align: center;font-size: 12px;width: 100%;border-spacing: 1;">
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
<hr/>

</center>
</body>
<script type="text/javascript">
	function changeSelect(){
		var chooseAll = document.getElementById("chooseAll");
		var subs = document.getElementsByName("choose");
			for (var i = 0; i < subs.length;i++) {
				if(chooseAll.checked){
					subs[i].checked = true;
				} else {
					subs[i].checked = false;
				}
			}
	}
	function overTag(tag){
		tag.style.color = "red";	
// 		tag.style.fontWeight = "600";
// 		tag.style.backgroundColor = "#E9E4E4";
	}
				
	function outTag(tag){
		tag.style.color = "black";
// 		tag.style.fontWeight = "300";
// 		tag.style.backgroundColor = "white";
	}
</script>
</html> 


