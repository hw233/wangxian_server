<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- <link rel="stylesheet" type="text/css" href="/css/style.css" /> -->
<title>资源更新</title>
</head>
	<body bgcolor='#c1c1c0' style="azimuth: center;">
		<%
			String servername = GameConstants.getInstance().getServerName();
			String recorder = request.getParameter("action");
			if(servername!=null){
				out.print("<h2>"+recorder+"你好，当前服务器："+servername+"</h2>");
			}
		%>
		
		<form method="post" action="gameResourceUpdateTool_handle.jsp?action=upload" enctype="multipart/form-data">
		<input type="hidden" name="text1" value="file"> 
			<table border="1" cellpadding="1"  bgcolor="#c5c5c5">
				<tr bgcolor="#FFFFFF">
					<td style="width:200px">请选择要上传的表</td><td><input type="file" name="file1"></td>
					<td><input type="submit" value="上传"></td>
				</tr>
		</table>
		</form>
		<br>
		<hr>
		<form method="post" action="gameResourceUpdateTool_downlist.jsp">
			<table border="1" cellpadding="1"  bgcolor="#c5c5c5">
				<tr bgcolor="#FFFFFF">
					<td style="width:200px">请选择要下载的表</td>
					<td><input type="submit" value="选择文件"></td>
				</tr>
		</table>
		</form>
	</body>
</html>