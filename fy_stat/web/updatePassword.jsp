<%@ page contentType="text/html; charset=utf-8"%>
<%@page import="com.xuanzhi.boss.gmuser.model.Gmuser"%>
<%@ include file="statjsp/useradmin/header.jsp"   %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh-cn">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>统计平台</title>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<link rel="stylesheet" href="css/lt.css"/>
    <script type="text/javascript">  
     function changeCode()  
      {  
       window.location.reload();
       document.getElementById("rc").src="imgcode";  
      }  
     </script>  
     
     <script type="text/javascript">
             if(self!=top){
                 top.location="<%=basePath%>/updatePassword.jsp";
             }else if(window.parent!=null&&self==top){
                 var _v=new String(window.parent.location);
                 if(_v.indexOf(".jsp")>0&&_v.indexOf("/updatePassword.jsp")<0){
			         //window.close();
			         window.location = "<%=basePath%>/updatePassword.jsp";
			     }
             }
</script>
</head>
<body bgcolor="#FFFFFF" >
    <p></p><p></p>
	<h3 align="center">修改密码</h3>
	<h5 align="center"> 请注意密码安全，经常修改密码，以免造成不必要的麻烦！ </h5>
	<p></p>
	<%   GmUserManager gmanager = GmUserManager.getInstance();
	     String username = request.getParameter("username");
         String passport = request.getParameter("passport");
         
         String passport1 = request.getParameter("passport1");
         String passport2 = request.getParameter("passport2");
         
         String randomcode=request.getParameter("randomcode");
         
         if(randomcode!=null){ randomcode=randomcode.toLowerCase();}
         
         if(randomcode!=null&&session.getAttribute("randomCode")!=null){
         if("".equals(username)||"".equals(passport)||"".equals(passport1)||"".equals(passport2)||"".equals(randomcode)){
           out.println("<span align='center'><font color='red'>请输入正确的信息</font><span>");
         }else{	
         if(!randomcode.equals(session.getAttribute("randomCode"))){
         System.out.println(new Date()+"   验证码错误：["+username+"] ["+passport+"]访问来自：["+request.getRequestURL()+"] ["+request.getRemoteAddr()+"]");
         out.println("<span class='redDot'><font color='red'>验证码错误</font><span>");
        
         }else if(!passport1.equals(passport2)){
          out.println("<span class='redDot'><font color='red'>两次输入新密码不同,请确认新密码!</font><span>");
         }else{
         
         if(username!=null&&passport!=null&&passport1!=null&&passport2!=null){
            try{
            if(gmanager.getAllUser().size()==0&&"admin".equals(username)&&"12343211123321".equals(passport)){
              session.setAttribute("username",username);
              session.setAttribute("gmid","000000");
              response.sendRedirect("index.jsp");
              }
             
              
            if(gmanager.login(username,passport)){
            Gmuser gu = gmanager.getGmuser(username);
              gu.setPassword(passport1);
              gmanager.updateGmuser(gu);
             
              session.removeAttribute("username");
              out.println("<script>window.top.location.replace('index.jsp');</script>"); 
              //session.setAttribute("username",username);
             // session.setAttribute("gmid",gu.getGmname());
              //session.setAttribute("gmuser",gu);
              //response.sendRedirect("index.jsp");
            }else
              out.println("<p align='center' ><font color='red'>更新失败！请检查用户名和密码是否正确</font></p>");
              }catch(Exception e){
               out.println("异常："+e.getMessage());
              }
         }
         }
         }
         }
	 %>
    <form method="post" action="" id="f1">
    <table width='40%' border='1' align='center' cellpadding='0' cellspacing='1' bordercolor='#CCCCCC'>
    <tbody id='t1'>
        <tr><td height='30' colspan='2' align='center' bgcolor='#BADAFE' style='font-style:inherit; font-size:14px; font-weight:bold'>用 户 登 录</td></tr>
    	 <tr> <td width='30%' height='25' bgcolor='#DDEDFF'><div align='right'>用户名：</div></td>    <td bgcolor='#DDEDFF'><input type='text' name='username' id='username' value=''/>    <span class='redDot'>*必填</span></td>  </tr>
    	 <tr> <td height='25' bgcolor='#DDEDFF'><div align='right'>老密码：</div></td>    <td bgcolor='#DDEDFF'><input type='password' name='passport' id='passport' />   <span class='redDot'>*必填</span></td>  </tr>
    	
    	 <tr> <td height='25' bgcolor='#DDEDFF'><div align='right'>新密码：</div></td>    <td bgcolor='#DDEDFF'><input type='password' name='passport1' id='passport1' />   <span class='redDot'>*必填</span></td>  </tr>
    	 <tr> <td height='25' bgcolor='#DDEDFF'><div align='right'>再次确认新密码：</div></td>    <td bgcolor='#DDEDFF'><input type='password' name='passport2' id='passport2' />   <span class='redDot'>*必填</span></td>  </tr>
    	
    	
    	 <tr> <td height='25' bgcolor='#DDEDFF'><div align='right'>验证码：</div></td>    <td bgcolor='#DDEDFF'><input type='text' name='randomcode' id='randomcode' /> 
    	      <img src="imgcode" id="rc"/> <a href="javascript:changeCode();">看不清?换一张</a>  </td>  </tr>
    	    
    	 <tr><td height='30' colspan='2' align='center' bgcolor='#BADAFE'><input type='submit' name='button2' id='button2' value='-提交-' />	&nbsp;<input type='reset' name='button' id='button' value='-重置-' /></td>  </tr>
    
    </tbody>
    </table>
      
    </form>
</body>
</html>
