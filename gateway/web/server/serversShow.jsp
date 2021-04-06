<%@page import="java.net.URLEncoder"%>
<%@page import="java.net.URL"%>
<%@page import="com.xuanzhi.tools.servlet.HttpUtils"%>
<%@ page contentType="text/html;charset=utf-8"
	import="java.util.*,
com.xuanzhi.tools.text.*,
com.xuanzhi.tools.transport.*,java.nio.channels.*,com.fy.gamegateway.mieshi.server.*,com.fy.gamegateway.thirdpartner.congzhi.* "%>
<%@page import="com.xuanzhi.tools.text.ParamUtils;"%>
<%!
	static List<String> notShowServers = new ArrayList<String>();
	static {
		notShowServers.add("巍巍昆仑");
		notShowServers.add("百里沃野");
		notShowServers.add("正式发布服");
		notShowServers.add("13测试发布");
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
		areas.add("一區");
		areas.add("二區");
		areas.add("三區");
		areas.add("四區");
		areas.add("五區");
		areas.add("六區");
		areas.add("七區");
		areas.add("八區");
		areas.add("九區");
		areas.add("十區");
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
%>
<%


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
				serverStatuses.get(s.getStatus()).add(s);
			}
		}
	}
	Collections.sort(showServers,order);
	String url = "";
	String username = "";
	String pwd = "";
	String modifiedUrl = "";
	String title = "";
	if (action != null && "refreshJsp".equals(action)) {
		//要生成刷页面的数据
		url = request.getParameter("url");
		username = request.getParameter("username");
		pwd = request.getParameter("pwd");
		title = request.getParameter("title");
		modifiedUrl = url;
		if (modifiedUrl.indexOf("?") < 0) { //页面本身不带参数
			modifiedUrl += "?";
		} else {
			modifiedUrl += "&";
		}
		modifiedUrl += ("authorize.username=" + URLEncoder.encode(username) + "&authorize.password="+URLEncoder.encode(pwd));
	}
%><html>

<head>
<script type='text/javascript' src='jquery-1.6.2.js'></script>
<script type="text/javascript">
function overTag(tag){
	tag.style.color = "red";	
// 	tag.style.fontWeight = "600";
//		tag.style.backgroundColor = "#E9E4E4";
}
			
function outTag(tag){
	tag.style.color = "black";
// 	tag.style.fontWeight = "300";
//		tag.style.backgroundColor = "white";
}
var id = 0;
function startchecked(tag){
	if(tag){
		if(tag.checked==true){
			checkStart();
			checkStop();
			id=self.setInterval("checkStart()",20000);
		}else{
			window.clearInterval(id);
		}
	}
}
	 function trim(str){ //删除左右两端的空格
	     return str.replace(/(^\s*)|(\s*$)/g, "");
	 }

function checkStart(){
	var gateurl = "http://116.213.192.216:9110/mieshi_game_boss/serverRunCheck.jsp";
	var array = new Array();
	var allservernames = document.getElementById('allservername').value;
	array = allservernames.split("@@@@@");
	for(var i=0;i<array.length;i++){
// 		alert(i+"--"+array[i]);
	 	var o = {'servername':array[i],'gateurl':gateurl};
	$.ajax({
		  type: 'POST',
		  url: "getServerLogMess.jsp",
		  contentType : "application/x-www-form-urlencoded; charset=utf-8",
		  data: o,
//		  complete:function(result){alert("completed"+result)},
		  dataType: "html",
//		  error:function(result){alert("error:"+arguments[0]+":"+arguments[1]+":"+arguments[2])},
		  success: function(result)
		  {
			  if(result){
				  var aa = trim(result);
// 				  document.getElementById(aa).style.backgroundColor  = "green";
// 				  document.getElementById(aa).innerHTML = "<font color='red'>ok</font>";
// 				  if(aa.indexOf("stop")>0){
// 					  var bb = aa.replace("stop","start");
// 					  document.getElementById(bb).style.backgroundColor  = "red";
// 					  document.getElementById(bb).innerHTML = "请检查";
// 				  }
				  if(aa.indexOf("start")>0){
// 					  var cc = aa.replace("start","stop");
					  document.getElementById(aa).style.backgroundColor  = "green";
					  document.getElementById(aa).innerHTML = "已启动";
				  }
			  }
// 			 document.getElementById('uid').innerHTML = "<font color='red'>"+result+"</font>";
		  }
		});
	}
	
	checkStop();

}


function checkStop(){
	var gateurl = "http://116.213.192.200:8110/game_boss/viewlog.jsp";
	var array = new Array();
	var allservernames = document.getElementById('allservername').value;
	array = allservernames.split("@@@@@");
	for(var i=0;i<array.length;i++){
	 	var o = {'servername':array[i],'gateurl':gateurl
			};
	$.ajax({
		  type: 'POST',
		  url: "getServerLogMess.jsp",
		  contentType : "application/x-www-form-urlencoded; charset=utf-8",
		  data: o,
		  dataType: "html",
		  success: function(result)
		  {
			  if(result){
				  var aa = trim(result);
				  if(aa.indexOf("stop")>0){
					  var bb = aa.replace("stop","start");
					  document.getElementById(bb).style.backgroundColor  = "red";
					  document.getElementById(bb).innerHTML = "请检查";
				  }
			  }
		  }
		});
	}

}
</script>
</head>
<body style="font-size: 12px; background-color: #c8edcc">
	<center>
		<br> <a href="serversShow.jsp">刷新此页面</a><br>
		<%
	boolean  batch = false;
	if("true".equals(request.getParameter("batch"))){
		batch = true;
	}
%>

		<br>
		<table border="0" cellpadding="0" cellspacing="1" width="80%"
			align="center" style="font-size: 12px;">
			<tr bgcolor="#00FFFF" align="center">
				<td>编号</td>
				<td>分区</td>
				<td>优先级</td>
				<td>服务器类型</td>
				<td>真实名称</td>
				<td>单服地址</td><td>启动检查</td><td><input type="checkbox" onclick='startchecked(this)'>--20秒刷新</td>
			</tr>
			<%
		Map<String,String> map = new TreeMap<String,String>();
		String serverurl = "http://116.213.192.216:9110/mieshi_game_boss/getServerUrl.jsp";//"http://116.213.192.200:8110/game_boss/getServerUrl.jsp";
		HashMap headers = new HashMap();
		String contentP = "content=1";
		byte[] b = HttpUtils.webPost(new URL(serverurl), contentP.getBytes(), headers, 60000, 60000);
		String mess = new String(b);
		String serverurles[] = mess.split("@@##");
		String names [] = null;
		if(serverurles!=null && serverurles.length>0){
			for(int i=0;i<serverurles.length;i++){
				names = serverurles[i].split("mmmm");
				if(names.length>1){
					map.put(names[0].trim(), names[1]);
				}
			}
		}
		if (!"".equals(modifiedUrl)) {
			//要获得刷新页面的数据
			StringBuffer sbf = new StringBuffer();
			sbf.append("<hr>");
			sbf.append("<h2>" + title + "</h2>");
			sbf.append("<hr>");
			sbf.append("<div style='font-size:12px;'>");
			for (MieshiServerInfo s : showServers) {
				sbf.append("[" +SERVERTYPE_NAMES[s.getServerType()] + "]").append("<a href='"+map.get(s.getName()) + modifiedUrl +"' target='_blank' style='font-weight: bold;'>").append(s.getName()).append("</a><br><br>\n");
			}
			out.print("<textarea rows='"+showServers.size()+"' cols='120'>"+sbf.toString()+"</textarea>");
			sbf.append("<div>");
			return;
		}
		int index = 0;
		int currentServerType = -1;
		String serverss = "";
		for(MieshiServerInfo s : showServers){
			if (s.getServerType() != currentServerType) {
				currentServerType = s.getServerType();
				//out.print(getServerTypeChangeHTML(currentServerType));
			}
			serverss = serverss+s.getRealname()+"@@@@@";
			boolean inTuijian = tuijianServers.contains(s.getRealname());
			boolean innerUserOnly = (s.getServerType() == 2 || s.getStatus() == 0 ||s.getStatus() == 5 || s.getStatus() == 6);
			%>
			<tr align="center"
				style="background-color:  <%=innerUserOnly ? "#B7B7B7" : ""%>;" onmouseover = "overTag(this);" onmouseout = "outTag(this);">
				<td title="<%=currentServerType%>"><%=index++ %></td>
				<td><%=s.getCategory() %></td>
				<td><%=s.getPriority() %></td>
				<td><%=SERVERTYPE_NAMES[s.getServerType()] %></td>
				<td style="<%=inTuijian?"color: red;font-weight: bold;":""%>"><%=inTuijian ? "[荐]":""%>
					<a href="<%=map.get(s.getName()) %>" target="_blank"><%= s.getRealname() %></a>
				</td>
				<td><%=map.get(s.getName()) %></td>
				<td style="background-color:" id='<%=s.getRealname() %>start'></td>
			</tr>
			<%}%>
			<input type="hidden" name='allservername' id='allservername' value='<%=serverss%>'>
		</table>
	</center>
	<HR/>
	<form action="" method="post">
		页面相对路径:<input size="80" name="url">
		链接的标题:<input type="text" name="title"><BR/>
		用户名:<input type="text" size="20" name="username">
		密&nbsp;&nbsp;码:<input size="20" type="password" name="pwd" value="<%=url%>">
		<input type="hidden" name="action" value="refreshJsp">
		<input type="submit" name="提交">
	</form>
	<HR/>
	
</body>
<script type="text/javascript">
	function changeSelect() {
		var chooseAll = document.getElementById("chooseAll");
		var subs = document.getElementsByName("choose");
		for ( var i = 0; i < subs.length; i++) {
			if (chooseAll.checked) {
				subs[i].checked = true;
			} else {
				subs[i].checked = false;
			}
		}
	}
</script>
</html>

