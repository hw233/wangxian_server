<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@include file="../header.jsp" %>
<%@include file="../authority.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>潜龙GM紫薇频道发言 </title>
<link rel="stylesheet" href="../style.css"/>
<script type='text/javascript' src='/game_server/dwr/interface/chatborder.js'></script>
<script type='text/javascript' src='/game_server/dwr/engine.js'></script>
<script type="text/javascript" src="/game_server/dwr/interface/DWRManager.js"></script>
<script type="text/javascript" src="/game_server/dwr/util.js"></script>
<script type="text/javascript">
  function $(tag){
    return document.getElementById(tag);
  }
  var status = 0;
  var inc;
  window.onload=function(){
       inc=self.setInterval("showmessage()",2000);
  }
  
  function showmessage(){
          chatborder.getMessageZiWei(show);
  }
  function set(){
     status =1;
  }
  function unset(){
     status =0;
  }
  function show(mes){
      if(mes.length>0){
	      var tag = document.getElementById("message");
	      var m1 = tag.innerHTML;
	      var m2 ="";
	      for(i=0;i<mes.length;i++){
	        var s= mes[i].split("#@#");
	        var s1 = s[0].split("(")[1].substring(0,s[0].split("(")[1].length-1);
	        if(s[0].indexOf("GM")==0){
	          m2= "<p style=\"color:red\" align='left'><a href='user_action.jsp?playerid=" +s1+"'>"+s[0]+"</a>&nbsp;&nbsp;&nbsp;&nbsp;"+s[1]+"<br/>"+s[2]+"</p>" + m2;
	        }else{	
	          m2=m2+"<p style=\"color:green\" align='left'><a href='user_action.jsp?playerid=" +s1+"'>"+s[0]+"</a>&nbsp;&nbsp;&nbsp;&nbsp;"+s[1]+"<br/>"+s[2]+"</p>" + m2;
	        }
	      } 
	      if(m2!=""){ 
		      var cobj = document.createElement("div");
		      var t1 = tag.scrollTop;
		      cobj.innerHTML= m2;
		      tag.appendChild(cobj);
		      if(status==0){
		      tag.scrollTop = tag.scrollHeight;
		      }
		      if(status==1){
		      tag.scrollTop = t1;
		      }
	      }
    }
  }

  function send(gmname){
     
     var sendms = document.getElementById("sendms").value;
     document.getElementById("sendms").value="";
     if(sendms){
       chatborder.sendMessageZiWei(gmname,sendms);
     }
  }
  function delay(){
      clearInterval(inc);
	  inc = self.setInterval("showmessage()",30000);
  }
  function review(){
      clearInterval(inc);
	  inc = self.setInterval("showmessage()",2000);
  }
</script>
</head>
<body>

  <%  
      try{
      String gmname = session.getAttribute("gmid").toString();
      String username = session.getAttribute("username").toString();
      ActionManager amanager = ActionManager.getInstance();
      amanager.save(username,"参与了紫薇频道聊天");
     // String gmname = "GM04";
     out.print("<br/><input type='button' value='查看聊天记录' onclick='window.open(\"channel_record.jsp?sname=ziweichat\")' />");
      out.print("<br/><input type='button' value='锁定' onclick='set();' />");
      out.print("<input type='button' value='取消' onclick='unset();' />");
      out.print("<br/><input type='button' value='减速' onclick='delay();' />");
      out.print("<input type='button' value='恢复' onclick='review();' /><br/>");
      out.print("gmname:"+gmname);
      out.print("<p></p><p></p><p align='center' ><h2>与紫薇宫频道聊天</h2></p>");
      out.print("<div id='message'  style=\"width:100%; height: 500px; border: solid green 1px; overflow: scroll\" ></div>");
      out.print("<br/><input type='text' name='sendms' id ='sendms' value='' size='100' />  " );
      out.print("<input type='button' value='发送' onclick ='send(\""+gmname+"\");' />");
      out.print("</td></tr></table>");
      }catch(Exception e){
        out.print(StringUtil.getStackTrace(e));
       // RequestDispatcher rdp = request.getRequestDispatcher("visitfobiden.jsp");
       // rdp.forward(request,response);
      }
  %>
</body>
