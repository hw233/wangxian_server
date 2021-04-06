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
<script type='text/javascript' src='jquery-1.6.2.js'></script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="gm/css/style.css"/>
<title>GM系统公告</title>
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
	
	function sendSystemMessage(){
		var msgContent = document.getElementById('msgContent').value;
		var type = document.getElementById('selected').value;
		if(msgContent&&type){
			servers = document.forms[0].server;
			if(servers.length>0){
				for (i=0;i<servers.length;i++)
				{
					if (servers[i].checked)
					{
					var url = servers[i].value;
					var adminurl = url.substring(0,url.indexOf("gm"))+"admin/"+"system_chatmessage.jsp";
					var o = {'msgContent':msgContent,
							'selected':type,
							'send':"send",
							'serverurl':adminurl
							};

					$.ajax({
						  type: 'POST',
						  url: "bbb_tw2.jsp",
						  contentType : "application/x-www-form-urlencoded; charset=utf-8",
						  data: o,
// 						  complete:function(result){alert("completed"+result)},
						  dataType: "html",
// 						  error:function(result){alert("error:"+arguments[0]+":"+arguments[1]+":"+arguments[2])},
						  success: function(result)
						  {
							 document.getElementById('messid').innerHTML = "<font color='red'>OK</font>";
						  }
						});
					
					///document.getElementById("messid").innerHTML =document.getElementById("messid").innerHTML + "发送成功-"+serverName+",";
					}
				}
				
			}
		}else{
			alert("内容不能为空！");
		}
		
		
	}
	
</script>

<body bgcolor="#c8edcc">

<table>
	<%@include file="NewServerConfig_tw.jsp" %>
</table>
<div id='messid'></div>
<hr>

<table>
   	<tr><th>类型：</th><td><select id='selected' name="selected"><option value="0">系统滚动消息<option value="1">系统提示消息<option value="2">系统电视消息<option value="3">世界频道<option value="4">彩色世界频道</select></td></tr>
	<tr><th>消息：</th><td><textarea id='msgContent' name=msgContent style="width:500px;height:200px;"></textarea></td></tr>
	<tr><input type="button" onclick="allcheaked()" id="allche" value="全选"/><input type='button' name='sub1' onclick="sendSystemMessage()" value="发送 "/></tr>
	
</table>

</body>
</html>

