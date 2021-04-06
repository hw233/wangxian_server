<%@page import="java.net.URL"%>
<%@page import="com.xuanzhi.tools.servlet.HttpUtils"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.xuanzhi.tools.text.StringUtil"%>
<%@page import="java.util.Date"%>
<%@page import="com.xuanzhi.tools.text.DateUtil"%>
<%@page import="com.fy.boss.gm.record.TelRecord"%>
<%@page import="com.fy.boss.gm.record.TelRecordManager"%>
<%@page import="com.fy.boss.gm.record.ActionManager"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.boss.gm.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
     
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.gamegateway.mieshi.server.MieshiServerInfoManager"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiServerInfo"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="../css/style.css"/>
<title>GM活动公告</title>
<script type='text/javascript' src='../js/jquery-1.6.2.js'></script>
</head>

<script language="javascript"> 

	function selectServerType(serverType){
		var a = document.getElementsByTagName("input"); 
		if(serverType == "全选择"){
			for (var i=0; i<a.length; i++) {
				if (a[i].type == "checkbox") a[i].checked = true; 
			}
		}else {
			for (var i=0; i<a.length; i++) {
				if (a[i].type == "checkbox") a[i].checked = false; 
			}
   			for (var i=0; i<a.length; i++) {
   				if (a[i].type == "checkbox") {
   					if(a[i].id == serverType){
   						if(a[i].checked==true){ 
   							a[i].checked = false;
   	   					}else{
   	   						a[i].checked = true;
   	   	   				}
   	   				}
   				}
   			}
		}
	}
	
	function sendActivityMessage(){
		var num = 1;
		var recorder = document.getElementById('recorder').value;
		var name = document.getElementById('name').value;
		var content1 = document.getElementById('content').value;
		var content = encodeURIComponent(content1);
		var beginTime = document.getElementById('beginTime').value;
		var endTime = document.getElementById('endTime').value;
		
		if(recorder&&name&&content&&beginTime&&endTime){
			servers = document.forms[0].server;
			if(servers.length>0){
				for (i=0;i<servers.length;i++){
					if (servers[i].checked){
						var adminurl = servers[i].value+"admin/notice/"+"setActivityNotice.jsp";
						//alert("recorder:"+recorder+"--adminurl:"+adminurl);
						var o = {'content':content,
								'name':name,
								'contentInner':recorder,
								'send':"send",
								'beginTime':beginTime,
								'endTime':endTime,
								'serverurl':adminurl
								};
						$.ajax({
							  type: 'POST',
							  url: "../../aaaa.jsp",
							  contentType : "application/x-www-form-urlencoded; charset=utf-8",
							  data: o,
	 						 // complete:function(result){alert("completed"+result)},
							  dataType: "html",
	 						 // error:function(result){alert("error:"+arguments[0]+":"+arguments[1]+":"+arguments[2])},
							  success: function(result)
							  {
								  document.getElementById('messid').innerHTML = "<font color='red'>发送成功</font>:"+num++;
							  }
							});
					}
				}
			}
		}else{
			alert("内容不能为空！！");
		}
		
		
	}
	
</script>

<body bgcolor="#c8edcc">

<form>
	<table border="0" cellpadding="0" cellspacing="1" width="85%" bgcolor="#000000" align="center">
	<tr bgcolor="#00FFFF" align="center">
		<td>
		<select onchange="selectServerType(this.options[this.options.selectedIndex].value)">
			<option>渠道选择</option>
			<option>全选择</option>
		<%	
			String SERVERTYPE_NAMES[] = MieshiServerInfo.SERVERTYPE_NAMES;
			for(String t : SERVERTYPE_NAMES){
			%>	
				<option><%=t %></option>
			<%
			}
		%>
		</select>
		</td><td>编号</td><td>服务器类型</td><td>分区</td><td>优先级</td><td>服务器名</td><td>在线人数</td><td>状态</td>
		<td>描述</td>
	</tr>
		<%
			MieshiServerInfoManager mm = MieshiServerInfoManager.getInstance();
			String categories[] = mm.getServerCategories();
			String colors[] = new String[]{"#FFFFFF","#888888","#888888","#ff00ff","#FFFFFF","#FFFFFF",
					"#888888","#FFFFFF","#FFFFFF","#FFFFFF","#FFFFFF","#FFFFFF","#FFFFFF","#FFFFFF","#FFFFFF","#FFFFFF","#FFFFFF"};
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
					out.println("<td><input type='checkbox' id='"+SERVERTYPE_NAMES[s.getServerType()]+"' name='server' value="+s.getServerUrl()+"></td>");
					out.println("<td>"+index+"</td>");
					out.println("<td>"+s.getCategory()+"</td>");
					out.println("<td>"+s.getPriority()+"</td>");
					out.println("<td>"+s.getName()+"</td>");
					out.println("<td>"+s.getOnlinePlayerNum()+"</td>");
					out.println("<td>"+MieshiServerInfo.STATUS_DESP[s.getStatus()]+"</td>");
					out.println("<td>"+SERVERTYPE_NAMES[s.getServerType()]+"</td>");
					out.println("<td>"+s.getDescription()+"</td>");
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
	</table>
</form>
<h2>总在线人数:<%=totalPlayerNum %></h2>
	<%
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	%>
	<div id='messid'></div>
	<hr>
	<form>
			<table align="center">
				<tr>
					<td align="center">公告填写人:</td>
					<td><input type="text"  id="recorder" name="recorder"/></td>
				</tr>
				<tr>
					<td align="center">公告名称:</td>
					<td><input type="text" id="name" name="name" /></td>
				</tr>
				<tr>
					<td align="center">公告内容:</td>
					<td><textarea rows="17" cols="55" id="content" name="content"></textarea></td>
				</tr>
				
				<tr>
					<td align="center">公告开启时间:</td>
					<%
						String begin = sdf.format(new Date(System.currentTimeMillis()));
					%>
					<td><input type="text" id="beginTime" name="beginTime" value="<%=begin %>"/></td>
				</tr>
				<tr>
					<%
						String end = sdf.format(new Date(System.currentTimeMillis()+24*60*60*1000));
					%>
					<td align="center">公告失效时间:</td>
					<td><input type="text" id="endTime" name="endTime" value="<%=end %>"/></td>
				</tr>
			</table>
			<input type="button"  onclick="sendActivityMessage()" value="发送" />
	</form>

</body>
</html>

