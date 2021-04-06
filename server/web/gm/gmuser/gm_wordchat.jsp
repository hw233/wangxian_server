	<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@page import="com.fy.engineserver.gm.custom.WordFilter"%>
<%@include file="../header.jsp" %>
<%-- <%@include file="../authority.jsp" %> --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style type="text/css">
body {
	background-color: #c8edcc;
}
</style>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>灭世GM世界频道发言 </title>
<link rel="stylesheet" href="../style.css"/>
 <script type='text/javascript' src='/dwr/interface/chatborder.js'></script>
  <script type='text/javascript' src='/dwr/engine.js'></script>
<!-- <script type="text/javascript" src="/game_server/dwr/interface/DWRManager.js"></script> -->
<script type="text/javascript" src="/dwr/util.js"></script>
<script type="text/javascript">
  function $(tag){
    return document.getElementById(tag);
  }
  var inc;
  var status=0;
    window.onload=function(){
     inc=self.setInterval("showmessage()",2000);
   }
   var a = true;
   <%
      out.print("var ws = [");
      WordFilter wf = WordFilter.getInstance();
      List<String> ws = wf.getWords();
      for(int i=0;i<ws.size();i++){
         out.print("\""+ws.get(i)+"\"");
         if(i!=ws.size()-1){
           out.print(",");
         }
      }
      out.print("];");
   %>
   
   var ms = ["你确定?","are you sure?","继续弹框吗?","if you want continue?","有人有意见了，快点去看看吧?","there are somebody you mast have a see?"];
   var lastdate;
   var sleeptime = 10;
  function showmessage(){
       chatborder.getMessageWord(show);
  }
  function change(){
     a = true;
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
	        	var b = true;
	        	for(var j in ws){
	        	  if(s[2].indexOf(ws[j])>=0){
	        	     b = false;
	        	     break;
	        	  }
	        	}
		        if(s[0].indexOf("gm01")==0){
		          m2= "<p style=\"color:red\" ><a href='user_action.jsp?playerid=" +s1+"'>"+s[0]+"</a>&nbsp;&nbsp;&nbsp;&nbsp;"+s[1]+"<br/>"+s[2]+"</p>" + m2;
		        }else{	
		          m2=m2+"<p style=\"color:green;"+((!b)?"background-color:#20a0ff;":"")+"\" ><a href='user_action.jsp?playerid=" +s1+"'>"+s[0]+"</a>&nbsp;&nbsp;&nbsp;&nbsp;"+s[1]+"<br/>"+s[2]+"</p>" + m2;
		          if(a&&!b){
		            if(window.confirm(ms[Math.round(Math.random()*10)%6])){
		              a = false;
		              status =1;
		              lastdate = new Date();
		              self.setTimeout("change()",sleeptime*60000);
		              alert(sleeptime);
		            }
		          }
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
  function showlasttime(){
    alert("上次弹框时间："+lastdate);
    alert("停留时间："+sleeptime+"分钟");
  }
  function setSleepTime(){
     sleeptime = window.prompt("请填写停留的时间",10);
  }
  function set(){
     status =1;
  }
  function unset(){
     status =0;
  }
  function send(gmname){
     var sendms = document.getElementById("sendms").value;
     document.getElementById("sendms").value="";
     if(sendms){
       chatborder.sendMessageWord(gmname,sendms);
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

  <%  try{
	  
		  String gmname = "gm01";	
	      out.print("<input type='button' value='锁定' onclick='set();' />");
	      out.print("<input type='button' value='查看聊天记录' onclick='window.open(\"channel_record.jsp?sname=wordchat\")' /><br/>");
	      out.print("<p></p><p></p><p align='center' ><h1>与世界频道聊天</h1></p>");
	      out.print("<div align='center' id='message'  style=\"width:80%; height: 450px; border: solid green 1px; overflow: scroll\" ></div>");
	      out.print("<div align='left'><input type='text' name='sendms' style=\"width:75%; height: 30px; border: solid green 1px;\" id ='sendms' value=''/>" );
	      out.print("<input type='button' value='发送' onclick ='send(\""+gmname+"\");' /></div>");
	      }catch(Exception e){
	        out.print(StringUtil.getStackTrace(e));
	      }
  %>
</body>
</html>