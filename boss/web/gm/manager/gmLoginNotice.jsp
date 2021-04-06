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
<title>GM登录公告</title>
<script type='text/javascript' src='../../jquery-1.6.2.js'></script>

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
		var name = document.getElementById('name').value;
		var content = document.getElementById('content').value;
		var content1 = encodeURIComponent(content);
		var beginTime = document.getElementById('beginTime').value;
		var endTime = document.getElementById('endTime').value;
		var isbangy = document.getElementById('hava').value;
		var bangynum = document.getElementById('bindSivlerNum').value;
		var qudao = document.getElementById('qudao').value;
		var qudao1 = document.getElementById('qudao1').value;
// 		if(document.getElementById("hava").checked){
// 			alert("是否有绑银11:"+isbangy+"---绑银数量11:"+bangynum);
// 		}
		if(name&&content&&beginTime&&endTime){
			servers = document.forms[0].server;
			if(servers.length>0){
				for (i=0;i<servers.length;i++)
				{
					if (servers[i].checked)
					{
							var url = servers[i].value;
							var adminurl = url.substring(0,url.indexOf("gm"))+"admin/notice/"+"setLoginNotice.jsp";
							var o = {'content':content1,
									'name':name,
									'contentInner':"gm",
									'send':"send",
									'beginTime':beginTime,
									'endTime':endTime,
									'hava':isbangy,
									'bindSivlerNum':bangynum,
									'serverurl':adminurl,
									'qudao':qudao,
									'qudao1':qudao1
									};
							alert("1");

							$.ajax({
								  type: 'POST',
								  url: "../../cccc.jsp",
								  contentType : "application/x-www-form-urlencoded; charset=utf-8",
								  data: o,
//		 						  complete:function(result){alert("completed"+result)},
								  dataType: "html",
//		 						  error:function(result){alert("error:"+arguments[0]+":"+arguments[1]+":"+arguments[2])},
		 						  success: function(result)
		 						  {
// 		 							 document.getElementById('messid').innerHTML = "<font color='red'>OK</font>";
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
	function koreatranslate(index){
		if(index=="nocommit"){
			window.location.replace("gmLoginNotice.jsp?istran=korea");
		}else if(index=="korea"){
			window.location.replace("gmLoginNotice.jsp?istran=nocommit");
		}
		
	}
</script>

<body bgcolor="#c8edcc">
<%@include file="../../admin/other/NewServerConfig__.jsp" %>
	<%
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	%>
<!--	<div id='krose'><input type='button' value='언어전환' onclick='koreatranslate("<%=istran%>")'></input></div>-->
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
				<td><textarea rows="3" cols="20" id="content" name="content"></textarea></td>
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
		<input type="button" onclick="allcheaked()" id="allche" value="全选"/><input type="button"  onclick="sendLoginMessage()" value="发送" />
 	</form>

</body>
</html>

