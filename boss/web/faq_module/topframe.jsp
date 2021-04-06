<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>后台</title>
  <script type='text/javascript' src='/game_boss/dwr/util.js'></script>
  <script type='text/javascript' src='/game_boss/dwr/interface/checknew.js'></script>
  <script type='text/javascript' src='/game_boss/dwr/engine.js'></script>
<style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}
.s36 {font-size: 18px; font-weight: bold;color:white;font-family:"黑体";}
-->
</style>
<script type='text/javascript'>
  function $(id){
     return document.getElementById(id);
  }
  window.onload=function(){
     <%  String gmusername = session.getAttribute("username").toString();
//          if(gmusername!=null){
//             out.print("self.setInterval(\"a()\",1000);");  
            
//          }
    %> 
  }
<%--   var gmusername =<%="\""+gmusername.trim()+"\"" %>; --%>
  function a(){
      checknew.check(gmusername,show);
  }
  function show(count){
  //  alert(count);
    if(count==1||count=="1"){
       $("d1").style.display="";
       $("d1").innerHTML="<marquee  loop=5 align=center height=17 width=90% vspace=2 bgcolor=red >FAQ有所更新啦，赶紧去查看吧！！！！</marquee>";
       self.setTimeout("hiddendiv()",60000);     
    }
  }
  function hiddendiv(){
    $("d1").style.display="none";
  }
</script> 
</head>

<body bgcolor="#BADAFE">
<div id ='d1' style='display:none;position:absolute;width:90%;height:90%;margin:4px 80px;'>
  
</div>
<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#0A6689">
  <tr>
    <td height="5" align="center">
    </td>

  </tr>
  <tr>
    <td height="18" align="center"><span class="s36">游戏FAQ手册</span>
    </td>
  </tr>
  <tr>
    <td height="2" align="center">&nbsp;
    </td>

  </tr>
</table>
</body>
</html>

