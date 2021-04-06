	<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@page import="com.fy.engineserver.gm.custom.WordFilter"%>
<%@include file="../header.jsp" %>
<%//include file="../authority.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>潜龙GM世界频道发言 </title>
<link rel="stylesheet" href="../style.css"/>
<script type='text/javascript' src='/game_server/dwr/interface/mct.js'></script>
<script type='text/javascript' src='/game_server/dwr/engine.js'></script>
<script type="text/javascript" src="/game_server/dwr/interface/DWRManager.js"></script>
<script type="text/javascript" src="/game_server/dwr/util.js"></script>
<% GmMailReplay gmr = GmMailReplay.getInstance(); %>
<script type="text/javascript">
   var totalcount = <%= gmr.getGmMailSize("GM01") %>;
   var lock = false;
   var inc;
   var t1 =0;
   function check(){
     if(lock){
        mct.getGMMailsize(show);
     }
   }
   function show(co){
     t1 = co;
     
     if(t1!=0&&totalcount!=t1){
       alert("你有新的邮件啦！赶紧查收吧！！！！");
       totalcount = t1;
     }
   }
   function suspend(){
     lock = true;
     inc = self.setInterval("check()",10000);
   }
   function unsuspend(){
    clearInterval(inc);
    lock = false;
   }
   function nowstate(){
     alert("总数为："+totalcount);
     alert("当前为："+t1);
     if(lock){
       alert("当前为挂起！！");
     }else{
       alert("当前为解挂！！");
     }
   }
</script>
</head>
<body>&nbsp; 
 
  <%  try{ 
       
        out.print("<br/><br/><input type='button' value='挂起' onclick='suspend();' />");
        out.print("<br/><br/><input type='button' value='解挂' onclick='unsuspnd()' />");
        out.print("<br/><br/><input type='button' value='查看状态' onclick='nowstate();' />");
       
      }catch(Exception e){ 
        out.print(StringUtil.getStackTrace(e)); 
      //  RequestDispatcher rdp = request.getRequestDispatcher(&quot;visitfobiden.jsp&quot;); 
       // rdp.forward(request,response); 
      } 
  %>
</body>
