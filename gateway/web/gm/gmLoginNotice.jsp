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

<%@page import="com.fy.gamegateway.mieshi.server.MieshiServerInfo"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiServerInfoManager"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="../css/style.css"/>
<title>GM登录公告</title>
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
	
	function sendLoginMessage(){
		var num = 1;
		var name = document.getElementById('name').value;
		var content = document.getElementById('content').value;
		var content1 = encodeURIComponent(content);
		var beginTime = document.getElementById('beginTime').value;
		var endTime = document.getElementById('endTime').value;
		var isbangy = document.getElementById('hava').value;
		var bangynum = document.getElementById('bindSivlerNum').value;
// 		if(document.getElementById("hava").checked){
// 			alert("是否有绑银11:"+isbangy+"---绑银数量11:"+bangynum);
// 		}
		if(name&&content&&beginTime&&endTime){
			servers = document.forms[0].server;
			if(servers.length>0){
				for (i=0;i<servers.length;i++){
					if (servers[i].checked){
						var url = servers[i].value;
						var adminurl = url+"admin/notice/"+"setLoginNotice.jsp";
						var o = {'content':content1,
								'name':name,
								'contentInner':"gm",
								'send':"send",
								'beginTime':beginTime,
								'endTime':endTime,
								'hava':isbangy,
								'bindSivlerNum':bangynum,
								'serverurl':adminurl
								};
						$.ajax({
							  type: 'POST',
							  url: "../../cccc.jsp",
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
		<table>
			<tr>
				<th>公告名称:</th>
				<td><input type="text" name="name" id="name"/></td>
			</tr>
			<tr>
				<th>公告内容:</th>
				<td><textarea rows="17" cols="55" id="content" name="content"></textarea></td>
			</tr>
			<tr>
				<th>公告开启时间:</th>
				<%
					String begin = sdf.format(new Date(System.currentTimeMillis()));
				%>
				<td><input type="text" id="beginTime" name="beginTime" value="<%=begin %>"/></td>
			</tr>
			<tr>
				<%
					String end = sdf.format(new Date(System.currentTimeMillis()+24*60*60*1000));
				%>
				<th>公告失效时间:</th>
				<td><input type="text" id="endTime" name="endTime" value="<%=end %>"/></td>
			</tr>
			<tr>
<!-- 				<td>是否有绑银:</td> -->
<!-- 				<td> -->
<!-- 				没有:<input type="radio" name="hava" id="hava" value="0" /> -->
<!-- 				有:<input type="radio"name="hava"  id="hava" value="1" /> -->
<!-- 				</td> -->
				<th>是否有绑银:</th>
				<td><select id="hava">
					<option value='0'>没有</option>
					<option value='1'>有</option>
				</select></td>
				
			</tr>
			<tr>
				<th>绑银数量:</th>
				<td><input type="text" name="bindSivlerNum" id="bindSivlerNum"/></td>
			</tr>
		</table>
		<input type="button"  onclick="sendLoginMessage()" value="发送" />
 	</form>

</body>
</html>

