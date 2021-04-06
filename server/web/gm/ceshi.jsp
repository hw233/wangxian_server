<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@page import="com.fy.engineserver.chat.ChatMessageService"%>
<%@page import="com.fy.engineserver.chat.ChatMessage"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>潜龙GM邮箱</title>
<link rel="stylesheet" href="../style.css"/>
<script type='text/javascript' src='/game_server/dwr/interface/getmes.js'></script>
<script type='text/javascript' src='/game_server/dwr/engine.js'></script>
<script type="text/javascript" src="/game_server/dwr/interface/DWRManager.js"></script>
<script type="text/javascript" src="/game_server/dwr/util.js"></script>
<script type="text/javascript">
  function $(tag){
    return document.getElementById(tag);
  }
  function change(mid,tag){
	  //将子定义的内容填入回复框
	   var instr = document.getElementById("repcontent"+mid);
	   if(tag.value!=""){
	    instr.value=tag.value;
	   }
  }
  var inc;
  function chat(){
	  var playname = document.getElementById("playname").value;
	  var playid = document.getElementById("playid").value;
	  if(playname!=""){
	       window.location.replace("?playername="+playname);
	    }
	  if(playid!=""){
	      window.location.replace("?playerid="+playid);
	  }
  }
  function ceshi(){
   
    self.setInterval("a1()",1000);
  }
  function a1(){
    var gmname = document.getElementById("f1");
    showmessagename(gmname);
    alert(1);
  }
  function showmessagename(gmname){
    getmes.getMessageByName(gmname,show);
  }
   function showmessageid(gmid){
     getmes.getMessageByName(gmname,show);
  }
  function show(mes){
      var tag = document.getElementById("message");
      var m1 = tag.innerHTML;
      var m2 ="";
      for(i=0;i<mes.length;i++){
        var s= mes[i].split(":");
        if(s[0].indexOf("GM")>=0){
          m2=m2 +"<p color ='red' >"+s[0]+"&nbsp;&nbsp;&nbsp;&nbsp;"+s[1]+"<br/>"+s[3]+"</p>";
        }else{
          m2=m2+"<p color ='green' >"+s[0]+"&nbsp;&nbsp;&nbsp;&nbsp;"+s[1]+"<br/>"+s[3]+"</p>";
        }
      } 
   }
  function shuaxin(){
	  alert("shuaxin");
	    window.location.replace("gm_chat.jsp");
  }
  
  function send(playid){
     var sendms = document.getElementById("sendms").value;
     alert(playid);
     if(sendms!="")
        window.location.replace("?sendms="+sendms+"&pid="+playid);
  }
</script>
</head>
<body>
  <input type='button' value="测试" onclick="ceshi();" />
  <input type="hidden" id='f1' value="GM01" />
  <div id='message' ></div>
</body>
