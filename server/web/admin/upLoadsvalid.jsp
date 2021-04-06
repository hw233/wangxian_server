<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8" import="com.xuanzhi.boss.game.*,java.util.*,java.text.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="IPManager.jsp" %><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>careersLoad</title>
</head>
<body>
<%
//可修改标记,true为可以修改
boolean canModify = true;//正式应用改为false
Calendar c = Calendar.getInstance();
Date date = new Date(System.currentTimeMillis());
c.setTime(date);
DateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
String dateFormat = df.format(date);
int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

switch (dayOfWeek) {
case 1:
 out.println(dateFormat+"星期日");
 break;
case 2:
 out.println(dateFormat+"星期一");
 break;
case 3:
 out.println(dateFormat+"星期二");
 break;
case 4:
 out.println(dateFormat+"星期三");
 canModify = true;
 break;
case 5:
 out.println(dateFormat+"星期四");
 canModify = true;
 break;
case 6:
 out.println(dateFormat+"星期五");
 canModify = true;
 break;
case 7:
 out.println(dateFormat+"星期六");
 break;
}
out.println("<br>");
//服务器类型为0代表为可修改的开发服务器
if(GameConstants.getInstance().getServerType() == 0 && canModify){
%>
<form method="post" action="upLoadsFilevalid.jsp" enctype="multipart/form-data">
<input type="hidden" name="text1" value="file"> 
	<table border="0" cellpadding="0" cellspacing="1" bgcolor="#000000">
		<tr bgcolor="#FFFFFF">
			<td style="width:200px">职业数据文件:</td><td><input type="file" name="file1"></td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交"></td>
			<td><input type="reset" name="重置"></td>
		</tr>
</table>
</form>
<form method="post" action="upLoadsFilevalid.jsp" enctype="multipart/form-data">
<input type="hidden" name="text2" value="file"> 
	<table border="0" cellpadding="0" cellspacing="1" bgcolor="#000000">
		<tr bgcolor="#FFFFFF">
			<td style="width:200px">道具数据文件:</td><td><input type="file" name="file2"></td>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交"></td>
			<td><input type="reset" name="重置"></td>
		</tr>
</table>
</form>
<form method="post" action="upLoadsFilevalid.jsp" enctype="multipart/form-data">
<input type="hidden" name="text3" value="file"> 
	<table border="0" cellpadding="0" cellspacing="1" bgcolor="#000000">
		<tr bgcolor="#FFFFFF">
			<td style="width:200px">套装数据文件:</td><td><input type="file" name="file3"></td>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交"></td>
			<td><input type="reset" name="重置"></td>
		</tr>
</table>
</form>
<form method="post" action="upLoadsFilevalid.jsp" enctype="multipart/form-data">
<input type="hidden" name="text6" value="file"> 
	<table border="0" cellpadding="0" cellspacing="1" bgcolor="#000000">
		<tr bgcolor="#FFFFFF">
			<td style="width:200px">buff数据文件:</td><td><input type="file" name="file6"></td>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交"></td>
			<td><input type="reset" name="重置"></td>
		</tr>
</table>
</form>
<form method="post" action="upLoadsFilevalid.jsp" enctype="multipart/form-data">
<input type="hidden" name="text7" value="file"> 
	<table border="0" cellpadding="0" cellspacing="1" bgcolor="#000000">
		<tr bgcolor="#FFFFFF">
			<td style="width:200px">怪物数据文件:</td><td><input type="file" name="file7"></td>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交"></td>
			<td><input type="reset" name="重置"></td>
		</tr>
</table>
</form>
<form method="post" action="upLoadsFilevalid.jsp" enctype="multipart/form-data">
<input type="hidden" name="text8" value="file"> 
	<table border="0" cellpadding="0" cellspacing="1" bgcolor="#000000">
		<tr bgcolor="#FFFFFF">
			<td style="width:200px">怪物出生点数据文件:</td><td><input type="file" name="file8"></td>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交"></td>
			<td><input type="reset" name="重置"></td>
		</tr>
</table>
</form>
<form method="post" action="upLoadsFilevalid.jsp" enctype="multipart/form-data">
<input type="hidden" name="text9" value="file"> 
	<table border="0" cellpadding="0" cellspacing="1" bgcolor="#000000">
		<tr bgcolor="#FFFFFF">
			<td style="width:200px">NPC数据文件:</td><td><input type="file" name="file9"></td>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交"></td>
			<td><input type="reset" name="重置"></td>
		</tr>
</table>
</form>
<form method="post" action="upLoadsFilevalid.jsp" enctype="multipart/form-data">
<input type="hidden" name="text10" value="file"> 
	<table border="0" cellpadding="0" cellspacing="1" bgcolor="#000000">
		<tr bgcolor="#FFFFFF">
			<td style="width:200px">NPC出生点数据文件:</td><td><input type="file" name="file10"></td>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交"></td>
			<td><input type="reset" name="重置"></td>
		</tr>
</table>
</form>
<form method="post" action="upLoadsFilevalid.jsp" enctype="multipart/form-data">
<input type="hidden" name="text11" value="file"> 
	<table border="0" cellpadding="0" cellspacing="1" bgcolor="#000000">
		<tr bgcolor="#FFFFFF">
			<td style="width:200px">任务数据文件:</td><td><input type="file" name="file11"></td>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交"></td>
			<td><input type="reset" name="重置"></td>
		</tr>
</table>
</form>
<form method="post" action="upLoadsFilevalid.jsp" enctype="multipart/form-data">
<input type="hidden" name="text12" value="file"> 
	<table border="0" cellpadding="0" cellspacing="1" bgcolor="#000000">
		<tr bgcolor="#FFFFFF">
			<td style="width:200px">菜单系统配置文件:</td><td><input type="file" name="file12"></td>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交"></td>
			<td><input type="reset" name="重置"></td>
		</tr>
</table>
</form>
<form method="post" action="upLoadsFilevalid.jsp" enctype="multipart/form-data">
<input type="hidden" name="text13" value="file"> 
	<table border="0" cellpadding="0" cellspacing="1" bgcolor="#000000">
		<tr bgcolor="#FFFFFF">
			<td style="width:200px">声望系统配置文件:</td><td><input type="file" name="file13"></td>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交"></td>
			<td><input type="reset" name="重置"></td>
		</tr>
</table>
</form>
<form method="post" action="upLoadsFilevalid.jsp" enctype="multipart/form-data">
<input type="hidden" name="text14" value="file"> 
	<table border="0" cellpadding="0" cellspacing="1" bgcolor="#000000">
		<tr bgcolor="#FFFFFF">
			<td style="width:200px">商店配置文件:</td><td><input type="file" name="file14"></td>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交"></td>
			<td><input type="reset" name="重置"></td>
		</tr>
</table>
</form>
<form method="post" action="upLoadsFilevalid.jsp" enctype="multipart/form-data">
<input type="hidden" name="text15" value="file"> 
	<table border="0" cellpadding="0" cellspacing="1" bgcolor="#000000">
		<tr bgcolor="#FFFFFF">
			<td style="width:200px">副本数据文件:</td><td><input type="file" name="file15"></td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交"></td>
			<td><input type="reset" name="重置"></td>
		</tr>
</table>
</form>
<form method="post" action="upLoadsFilevalid.jsp" enctype="multipart/form-data">
<input type="hidden" name="text16" value="file"> 
	<table border="0" cellpadding="0" cellspacing="1" bgcolor="#000000">
		<tr bgcolor="#FFFFFF">
			<td style="width:200px">问题集数据文件:</td><td><input type="file" name="file16"></td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交"></td>
			<td><input type="reset" name="重置"></td>
		</tr>
</table>
</form>
<form method="post" action="upLoadsFilevalid.jsp" enctype="multipart/form-data">
<input type="hidden" name="text17" value="file"> 
	<table border="0" cellpadding="0" cellspacing="1" bgcolor="#000000">
		<tr bgcolor="#FFFFFF">
			<td style="width:200px">商店打折文件:</td><td><input type="file" name="file17"></td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="提交"></td>
			<td><input type="reset" name="重置"></td>
		</tr>
</table>
</form>
<%}else{
	out.println("此服务器不能修改数据或今天不是周三到周五期间。如果有需求请联系技术。");
}
%>
</body>
</html>
