<%@ page language="java" import="java.util.*,java.sql.*,com.sqage.stat.commonstat.entity.*,
                        com.xuanzhi.tools.text.*,java.io.*,com.sqage.stat.service.*"  pageEncoding="UTF-8"%>
<%

     String regDay = request.getParameter("regday");	
	 String endDay = request.getParameter("endday");
	 	
	boolean submit = "true".equals( request.getParameter("submitted"));
	
%>	

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>插入测试数据</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
  </head>
  
  <body><br>
  <h1>插入测试数据</h1>
  <form>
  		<input type='hidden' name='submitted' value='true'/>
  	     开始日期：<input type='text' name='regday' value='<%= regDay %>'>
  		结束日期：<input type='text' name='endday' value='<%= endDay %>'>
  		<input type='submit' value='提交'/>
  </form>
  <%
  if(submit){
	ChannelStatManager cs= ChannelStatManager.getInstance();
	//String[] strs={"2016-04-26","2016-06-12"};
		String[] strs={regDay,endDay};  //开始日期，结束日期
		cs.execute(strs);
	}
   %>
  <br></body>
</html>
