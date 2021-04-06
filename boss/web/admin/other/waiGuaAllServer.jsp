<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type='text/javascript' src='jquery-1.6.2.js'></script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="gm/css/style.css"/>
<title>外挂</title>
</head>

<script language="javascript"> 
	window.onload=function(){
// 		self.setInterval("sendSystemMessage()",10000);
		sendSystemMessage();
	}
	
	function sendSystemMessage(){
		$.ajax({
			  type: 'POST',
			  url: "waiguamess.jsp",
			  contentType : "application/x-www-form-urlencoded; charset=utf-8",
			  dataType: "html",
			  success: function(result)
			  {
				 document.getElementById('messid').innerHTML = result;
			  }
			});
		
	}
	
</script>

<body bgcolor="#c8edcc">

<div id='messid'></div>
<div style="display: none"><%@include file="NewServerConfig__.jsp" %></div>
</body>
</html>

