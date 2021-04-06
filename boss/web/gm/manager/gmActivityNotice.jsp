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
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="../css/style.css"/>
<title>GM活动公告</title>
<script type='text/javascript' src='../../jquery-1.6.2.js'></script>
</head>

<script language="javascript"> 
function koreatranslate(index){
	if(index=="nocommit"){
		window.location.replace("gmActivityNotice.jsp?istran=korea");
	}else if(index=="korea"){
		window.location.replace("gmActivityNotice.jsp?istran=nocommit");
	}
}
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
				for (i=0;i<servers.length;i++)
				{
					if (servers[i].checked)
					{
					var url = servers[i].value;
					var adminurl = url.substring(0,url.indexOf("gm"))+"admin/notice/"+"setActivityNotice.jsp";
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
// 						  complete:function(result){alert("completed"+result)},
						  dataType: "html",
// 						  error:function(result){alert("error:"+arguments[0]+":"+arguments[1]+":"+arguments[2])},
						  success: function(result)
						  {
// 							  document.getElementById('messid').innerHTML = "<font color='red'>OK</font>";
							  document.getElementById('messid').innerHTML = "<font color='red'>发送成功</font>:"+num++;
						  }
						});
					
					///document.getElementById("messid").innerHTML =document.getElementById("messid").innerHTML + "发送成功-"+serverName+",";
					}
				}
				
			}
		}else{
			alert("内容不能为空！！");
		}
		
		
	}
	
</script>

<body bgcolor="#c8edcc">

<table>
	<form>
	
	<%@include file="../../admin/other/NewServerConfig__.jsp" %>
	 </form>
</table>
	<%
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	%>
	<div id='messid'></div>
	<hr>
	<form>
			<table >
				<tr>
					<td>公告填写人:</td>
					<td><input type="text"  id="recorder" name="recorder"/></td>
				</tr>
				<tr>
					<td>公告名称:</td>
					<td><input type="text" id="name" name="name" /></td>
				</tr>
				<tr>
					<td>公告内容:</td>
					<td><textarea rows="3" cols="30" id="content" name="content"></textarea></td>
				</tr>
				
				<tr>
					<td>公告开启时间(2011-01-11 00:00:00):</td>
					<%
						String begin = sdf.format(new Date(System.currentTimeMillis()));
					%>
					<td><input type="text" id="beginTime" name="beginTime" value="<%=begin %>"/></td>
				</tr>
				<tr>
					<%
						String end = sdf.format(new Date(System.currentTimeMillis()+24*60*60*1000));
					%>
					<td>公告失效时间(2011-01-11 00:00:00):</td>
					<td><input type="text" id="endTime" name="endTime" value="<%=end %>"/></td>
				</tr>
			</table>
			<input type="button" onclick="allcheaked()" id="allche" value="全选"/><input type="button"  onclick="sendActivityMessage()" value="发送" />
	</form>

</body>
</html>

