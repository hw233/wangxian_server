<%@page import="java.util.Arrays"%>
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
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="gm/css/style.css"/>
<title>NPC公告</title>
<%@ include file="korea/translateResouce.jsp"%>
<script type='text/javascript' src='jquery-1.6.2.js'></script>
</head>

<script language="javascript"> 
	
	function allcheaked(){ 
		var a = document.getElementsByTagName("input"); 
		if(a[0].checked==true){ 
			for (var i=0; i<a.length; i++) {
				if (a[i].type == "checkbox") a[i].checked = false; 
			}
			document.getElementById('allche').value = "全选"; 
		}else { 
			for (var i=0; i<a.length; i++) {
				if (a[i].type == "checkbox") a[i].checked = true;
			}
			document.getElementById('allche').value = "取消全选"; 
		} 
	} 
	
	function sendLoginMessage(){
		var num = 1;
		var noticetype = document.getElementById('noticetype').value;
		var name = document.getElementById('noticetitle').value;
		var content = document.getElementById('noticecontent').value;
// 		var content1 = encodeURIComponent(content);
		var beginTime = document.getElementById('beginTime').value;
		var endTime = document.getElementById('endTime').value;
		if(content.length>1200){
			alert("字数太多，别超过1200，请您精简一下！");
			return;
		}
		if(name&&content&&beginTime&&endTime){
			servers = document.forms[0].server;
			if(servers.length>0){
				for (i=0;i<servers.length;i++)
				{
					if (servers[i].checked)
					{
						var url = servers[i].value;
						var o = {'content':content,
								'name':name,
								'contentInner':"gm",
								'beginTime':beginTime,
								'endTime':endTime,
								'serverurl':url,
								'noticetype':noticetype
								};

						$.ajax({
							  type: 'POST',
							  url: "npcnotice.jsp",
							  contentType : "application/x-www-form-urlencoded; charset=utf-8",
							  data: o,
							  dataType: "html",
	 						  success: function(result)
	 						  {
	 							 num++;
	 						  }
							});
						
					}
				}
				 document.getElementById('messid').innerHTML = "<font color='red'>"+(num)+"</font>";
			}else{
				alert("请选择服务器！");
			}
		}else{
			alert("内容不能为空！！");
		}
		
	}
	function koreatranslate(index){
		if(index=="nocommit"){
			window.location.replace("gmNpcNotice.jsp?istran=korea");
		}else if(index=="korea"){
			window.location.replace("gmNpcNotice.jsp?istran=nocommit");
		}
		
	}
	
	function handle(){
		window.location.replace("gmNpcNotice_record.jsp");
	}
</script>

<body bgcolor="#c8edcc">
<%@include file="NewServerConfig__.jsp" %>
	<%
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	%>
	<div id='krose'><input type='button' value='언어전환' onclick='koreatranslate("<%=istran%>")'></input><input type='button' name='record' onclick="handle()" id="record" value="<%=历史记录 %>"/></div>
	<div id='messid'></div>
	<hr>
	<form>
		<table>
			<tr>
				<th><%=类型 %>:</th>
				<td><select id='noticetype' name='noticetype'>
					<option>--</option>
					<option><%=公告 %></option>
					<option><%=活动 %></option>
				</select></td>
			</tr>
			<tr>
				<th><%=公告名称 %>:</th>
				<td><input type="text" name="noticetitle" id="noticetitle"/></td>
			</tr>
			<tr>
				<th><%=公告内容 %>:</th>
				<td><textarea rows="3" cols="20" name="noticecontent" id="noticecontent"></textarea></td>
			</tr>
			<tr>
				<th><%=公告开启时间 %>:</th>
				<%
					String begin = sdf.format(new Date(System.currentTimeMillis()));
				%>
				<td><input type="text" id="beginTime" name="beginTime" value="<%=begin %>"/></td>
			</tr>
			<tr>
				<%
					String end = sdf.format(new Date(System.currentTimeMillis()+24*60*60*1000));
				%>
				<th><%=公告失效时间 %>:</th>
				<td><input type="text" id="endTime" name="endTime" value="<%=end %>"/></td>
			</tr>
		</table>
		<input type="button" onclick="allcheaked()" id="allche" value="<%=全选%>"/><input type="button"  onclick="sendLoginMessage()" value="<%=发送%>" />
 	</form>

</body>
</html>

