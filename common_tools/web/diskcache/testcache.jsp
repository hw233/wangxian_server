<%@ page language="java" pageEncoding="GBK" contentType="text/html;charset=GBK" import="java.util.*,com.xuanzhi.tools.cache.diskcache.concrete.*,com.xuanzhi.tools.cache.diskcache.concrete.testcase.*"%><%@ page import="com.xuanzhi.tools.text.StringUtil" %><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>Show All List Cache</title>
<style>
td{ font-size:12px; color:#FF33FF; font-family:Arial; font-weight:bold;}
th{ background-color:#EEEEEE; border:1px solid #0059CC; align:center;}
.l{ text-align:right; padding-right:0px; vertical-align:top;}
.r{ text-align:left; padding-left:0px; vertical-align:bottom;}
.m{ text-align:center; height:20px; vertical-align:middle; color:#0059CC; font-weight:bold; font-size:16px;}
</style>
</head>
<%
	String name = request.getParameter("name") + StringUtil.randomIntegerString(5);
	String filePath = "/home/sms/testcache"+StringUtil.randomIntegerString(10)+".ddc";
	int n = 1024;
	int size = 1024;
	int timeout = 30;
	String message = null;
	if("true".equals(request.getParameter("submitted"))){
		name = request.getParameter("name");
		filePath = request.getParameter("filepath");
		n = Integer.parseInt(request.getParameter("num"));
		size = Integer.parseInt(request.getParameter("size"));
		timeout = Integer.parseInt(request.getParameter("timeout"));
		
		DefaultDiskCache ddc = new DefaultDiskCache(new java.io.File(filePath));
		ddc.setName(name);
		
		Random r = new Random();
		r.setSeed(System.currentTimeMillis());
		
		for(int i = 0 ; i < n ; i++){
			byte bytes[] = new byte[(int)(size * (0 + r.nextDouble()))];
			ddc.put(name+"-"+(i+1),new DiskCachePresureTestCase.Data(""+(i+1),bytes),0,(long)(5000+timeout*1000*r.nextDouble()));
		}
		
		for(int i = 0 ; i < n ; i++){
			if(r.nextDouble() > 0.5){
				ddc.remove(name+"-"+(i+1));
			}
		}
		
		for(int i = 0 ; i < n ; i++){
			if(r.nextDouble() > 0.1){
				ddc.get(name+"-"+(i+1));
			}
		}
		
		name = name.substring(0,name.length()-5) + StringUtil.randomIntegerString(5);
		filePath = "/home/sms/testcache"+StringUtil.randomIntegerString(10)+".ddc";
	
		message = "Cache ["+name+","+filePath+"] 创建成功！";
	}
%>
<body>
<br/><center>
<%
	if(message != null){
		out.println("<b>"+message+"</b><br/><a href='./showalldiskcache.jsp'>查看所有DiskCache情况</a><br>");
	}
%>
<table border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#FFFFFF" width="100%">
<form id='f'><input type='hidden' name='submitted' value='true'>
<tr><td align='right'>请输入Cache名称</td><td><input type='text' name='name' size=15 value='<%=name%>'></td></tr>
<tr><td align='right'>请输入数据文件地址</td><td><input type='text' name='filepath' size=30 value='<%=filePath%>'></td></tr>
<tr><td align='right'>请输入加入元素</td><td><input type='text' name='num' size=15 value='<%=n%>'></td></tr>
<tr><td align='right'>请输入元素大小</td><td><input type='text' name='size' size=15 value='<%=size%>'></td></tr>
<tr><td align='right'>请输入最大Idle时间（s）</td><td><input type='text' name='timeout' size=15 value='<%=timeout%>'></td></tr>
<tr><td align='right'><input type='submit' value='提交'></td><td></td></tr>
</form>
</table></center>	
</body>
</html>
