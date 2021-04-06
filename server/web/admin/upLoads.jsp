<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8" import="com.xuanzhi.boss.game.*,java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="IPManager.jsp" %><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>careersLoad</title>
</head>
<body>
<%
//可修改标记
boolean canModify = false;
canModify = true;
Calendar c = Calendar.getInstance();
c.setTime(new Date(System.currentTimeMillis()));
int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
switch (dayOfWeek) {
case 1:
 out.println("今天是星期日");
 break;
case 2:
 out.println("今天是星期一");
 break;
case 3:
 out.println("今天是星期二");
 break;
case 4:
 out.println("今天是星期三");
 canModify = true;
 break;
case 5:
 out.println("今天是星期四");
 canModify = true;
 break;
case 6:
 out.println("今天是星期五");
 canModify = true;
 break;
case 7:
 out.println("今天是星期六");
 break;
}
//服务器类型为0代表为可修改的开发服务器
if(GameConstants.getInstance().getServerType() == 0 && canModify){
%>
<form method="post" action="jspmaster.jsp" enctype="multipart/form-data">
<input type="hidden" name="text1" value="file"> 
	<table border="0" cellpadding="0" cellspacing="1" bgcolor="#000000">
		<tr bgcolor="#FFFFFF">
			<td style="width:200px">职业数据文件:</td><td><input type="file" name="file1"></td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交"></td>
			<td><input type="reset" name="重置"></td>
		</tr>
</table>
</form>
<form method="post" action="jspmaster.jsp" enctype="multipart/form-data">
<input type="hidden" name="text2" value="file"> 
	<table border="0" cellpadding="0" cellspacing="1" bgcolor="#000000">
		<tr bgcolor="#FFFFFF">
			<td style="width:200px">道具数据文件:</td><td><input type="file" name="file2"></td>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交"></td>
			<td><input type="reset" name="重置"></td>
		</tr>
</table>
</form>
<form method="post" action="jspmaster.jsp" enctype="multipart/form-data">
<input type="hidden" name="text3" value="file"> 
	<table border="0" cellpadding="0" cellspacing="1" bgcolor="#000000">
		<tr bgcolor="#FFFFFF">
			<td style="width:200px">套装数据文件:</td><td><input type="file" name="file3"></td>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交"></td>
			<td><input type="reset" name="重置"></td>
		</tr>
</table>
</form>
<form method="post" action="upLoadsFilevalid_download.jsp" enctype="multipart/form-data">
<input type="hidden" name="text6" value="file"> 
	<table border="0" cellpadding="0" cellspacing="1" bgcolor="#000000">
		<tr bgcolor="#FFFFFF">
			<td style="width:200px">buff数据文件:</td><td><input type="file" name="file6"></td>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交"></td>
			<td><input type="reset" name="重置"></td>
		</tr>
</table>
</form>
<form method="post" action="jspmaster.jsp" enctype="multipart/form-data">
<input type="hidden" name="text7" value="file"> 
	<table border="0" cellpadding="0" cellspacing="1" bgcolor="#000000">
		<tr bgcolor="#FFFFFF">
			<td style="width:200px">怪物数据文件:</td><td><input type="file" name="file7"></td>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交"></td>
			<td><input type="reset" name="重置"></td>
		</tr>
</table>
</form>
<form method="post" action="jspmaster.jsp" enctype="multipart/form-data">
<input type="hidden" name="text8" value="file"> 
	<table border="0" cellpadding="0" cellspacing="1" bgcolor="#000000">
		<tr bgcolor="#FFFFFF">
			<td style="width:200px">怪物出生点数据文件:</td><td><input type="file" name="file8"></td>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交"></td>
			<td><input type="reset" name="重置"></td>
		</tr>
</table>
</form>
<form method="post" action="jspmaster.jsp" enctype="multipart/form-data">
<input type="hidden" name="text9" value="file"> 
	<table border="0" cellpadding="0" cellspacing="1" bgcolor="#000000">
		<tr bgcolor="#FFFFFF">
			<td style="width:200px">NPC数据文件:</td><td><input type="file" name="file9"></td>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交"></td>
			<td><input type="reset" name="重置"></td>
		</tr>
</table>
</form>
<form method="post" action="jspmaster.jsp" enctype="multipart/form-data">
<input type="hidden" name="text10" value="file"> 
	<table border="0" cellpadding="0" cellspacing="1" bgcolor="#000000">
		<tr bgcolor="#FFFFFF">
			<td style="width:200px">NPC出生点数据文件:</td><td><input type="file" name="file10"></td>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交"></td>
			<td><input type="reset" name="重置"></td>
		</tr>
</table>
</form>
<form method="post" action="jspmaster.jsp" enctype="multipart/form-data">
<input type="hidden" name="text11" value="file"> 
	<table border="0" cellpadding="0" cellspacing="1" bgcolor="#000000">
		<tr bgcolor="#FFFFFF">
			<td style="width:200px">任务数据文件:</td><td><input type="file" name="file11"></td>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交"></td>
			<td><input type="reset" name="重置"></td>
		</tr>
</table>
</form>
<form method="post" action="jspmaster.jsp" enctype="multipart/form-data">
<input type="hidden" name="text12" value="file"> 
	<table border="0" cellpadding="0" cellspacing="1" bgcolor="#000000">
		<tr bgcolor="#FFFFFF">
			<td style="width:200px">菜单系统配置文件:</td><td><input type="file" name="file12"></td>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交"></td>
			<td><input type="reset" name="重置"></td>
		</tr>
</table>
</form>
<form method="post" action="jspmaster.jsp" enctype="multipart/form-data">
<input type="hidden" name="text13" value="file"> 
	<table border="0" cellpadding="0" cellspacing="1" bgcolor="#000000">
		<tr bgcolor="#FFFFFF">
			<td style="width:200px">声望系统配置文件:</td><td><input type="file" name="file13"></td>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交"></td>
			<td><input type="reset" name="重置"></td>
		</tr>
</table>
</form>
<form method="post" action="jspmaster.jsp" enctype="multipart/form-data">
<input type="hidden" name="text14" value="file"> 
	<table border="0" cellpadding="0" cellspacing="1" bgcolor="#000000">
		<tr bgcolor="#FFFFFF">
			<td style="width:200px">商店配置文件:</td><td><input type="file" name="file14"></td>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交"></td>
			<td><input type="reset" name="重置"></td>
		</tr>
</table>
</form>
<form method="post" action="jspmaster.jsp" enctype="multipart/form-data">
<input type="hidden" name="text15" value="file"> 
	<table border="0" cellpadding="0" cellspacing="1" bgcolor="#000000">
		<tr bgcolor="#FFFFFF">
			<td style="width:200px">副本数据文件:</td><td><input type="file" name="file15"></td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交"></td>
			<td><input type="reset" name="重置"></td>
		</tr>
</table>
</form>
<form method="post" action="jspmaster.jsp" enctype="multipart/form-data">
<input type="hidden" name="text16" value="file"> 
	<table border="0" cellpadding="0" cellspacing="1" bgcolor="#000000">
		<tr bgcolor="#FFFFFF">
			<td style="width:200px">问题集数据文件:</td><td><input type="file" name="file16"></td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交"></td>
			<td><input type="reset" name="重置"></td>
		</tr>
</table>
</form>
<%}else{
	out.println("此服务器不能修改数据，或今天不是周三到周五期间。如果有修改需要请联系技术。");
}
%>
</body>
</html>
