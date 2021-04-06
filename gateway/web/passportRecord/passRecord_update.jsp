<%@page import="com.fy.gamegateway.getbackpassport.PassportRecord"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@page import="java.util.*,java.io.*,java.util.*"%>
<%@page import="com.fy.gamegateway.getbackpassport.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh-cn">
<head>
<title>更新密保记录</title>
<!-- <link rel="stylesheet" href="../../css/style.css"/> -->
<link rel="stylesheet" href="record.css"/>
<style type="text/css">
body {
	background-color: #c8edcc;
}
</style>
<script type="text/javascript">
   function $(tag){
      return document.getElementById(tag);
   }
   function sub(aa){
      $("f1").action='?action=update&trid='+aa;
      $("f1").submit();
   }


</script>
</head>
<body bgcolor="#FFFFFF" >
	<h1 align="d">更新密保记录状态</h1>
	<%
	     try{
	         String action = request.getParameter("action");
	         String trid = request.getParameter("trid");
	         RecordManager rManager = RecordManager.getInstance();	  
	         PassportRecord tr = rManager.getPassportRecordById(Long.parseLong(trid.trim()));
	         if(action!=null&&action.trim().length()>0){
	            if("update".equals(action.trim())){
	                String type=request.getParameter("type");
	                if(type!=null)
	                  tr.setState(type);
	                  rManager.savePassportRecord(tr);
	                out.print("<font color='red' >更新成功！！！更新为-->"+type+"</font>");
	            }
	         }
	         out.print("<input type='button' value='返回' onclick='window.location.replace(\"passportRecords.jsp\")' />");
	         out.print("<form action='' method='post' id='f1'>"); 
		     out.print("<table align='center' width='80%' ><caption>更新状态</caption>");
		     out.print("<tr><th>手机号：</th><td class='top'>"+tr.getTelnumber()+"</td>");
		     out.print("<th>游戏账号：</th><td class='top'>"+tr.getUsername()+"</td></tr>");
		     out.print("<tr><th>服务器：</th><td>"+tr.getServername()+"</td>");
		     out.print("<th>人物昵称：</th><td>"+tr.getName()+"</td></tr> ");
		      out.print("<tr><th>咨询问题内容：</th><td colspan='3'>");
		     out.print(tr.getPassportquestion()+"</td></tr>");
		      out.print("<tr><th>回答内容：</th><td colspan='3'>");
		     out.print(tr.getPassportanswer()+"</td></tr>");
		     
		     out.print("<tr><th >类型：</th><td colspan='3'> <input type='radio' value='已经解答'  name='type' />已经解答  <input type='radio' value='等待核实'  name='type' />等待核实  <input type='radio' value='无效信息'  name='type' />无效信息</td></tr>    ");
		     out.print("<tr><td colspan='4' ><input type='button' value='提交' onclick='sub(\""+tr.getId()+"\");' /></td></tr></table>");
		     out.print("</form>");
		     
		    }catch(Exception e){
		  		out.print(e);
		    }
	%>
  
</body>
</html>
