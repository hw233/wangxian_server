<%@ page contentType="text/html; charset=utf-8"%>
<%@page import="com.xuanzhi.boss.gmuser.model.Gmuser"%>
<%@ include file="header.jsp"   %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh-cn">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>gm登录</title>
<link rel="stylesheet" href="css/lt.css"/>
<script type="text/javascript">
  
</script>
</head>
<body bgcolor="#FFFFFF" >
    <p></p><p></p>
	<h3 align="center">gm登录</h3>
	<p></p>
	<%   GmUserManager gmanager = GmUserManager.getInstance();
	     String username = request.getParameter("username");
         String passport = request.getParameter("passport");
         if("".equals(username)||"".equals(passport)){
           out.println("<span class='redDot'>请输入正确的信息<span>");
         }	
         if(username!=null&&passport!=null){
            try{
            if(gmanager.getAllUser().size()==0&&"admin".equals(username)&&"1234321".equals(passport)){
              session.setAttribute("username",username);
              session.setAttribute("gmid","000000");
              response.sendRedirect("index.jsp");
              return;
              }
            if(gmanager.login(username,passport)){
              Gmuser gu = gmanager.getGmuser(username);
              session.setAttribute("username",username);
              session.setAttribute("gmid",gu.getGmname());
              session.setAttribute("gmuser",gu);
              response.sendRedirect("index.jsp");
              return;
            }else
              out.println("<p align='center' ><font color='red'>登录失败！请检查用户名和密码是否正确</font></p>");
              }catch(Exception e){
               out.println("异常："+e.getMessage());
              }
         }
	 %>
    <form method="post" action="" id="f1">
    <table width='40%' border='1' align='center' cellpadding='0' cellspacing='1' bordercolor='#CCCCCC'>
    <tbody id='t1'>
    <tr><td height='30' colspan='2' align='center' bgcolor='#BADAFE' style='font-style:inherit; font-size:14px; font-weight:bold'>用 户 登 录</td></tr>
    	 <tr> <td width='30%' height='25' bgcolor='#DDEDFF'><div align='right'>用户名：</div></td>    <td bgcolor='#DDEDFF'><input type='text' name='username' id='username' value=''/>    <span class='redDot'>*必填</span></td>  </tr>
    	 <tr> <td height='25' bgcolor='#DDEDFF'><div align='right'>密&nbsp;&nbsp;码：</div></td>    <td bgcolor='#DDEDFF'><input type='password' name='passport' id='passport' />   <span class='redDot'>*必填</span></td>  </tr>
    	 <tr><td height='30' colspan='2' align='center' bgcolor='#BADAFE'><input type='submit' name='button2' id='button2' value='-登录-' />	&nbsp;<input type='reset' name='button' id='button' value='-重置-' /></td>  </tr>
    
    </tbody>
    </table>
      
    </form>
</body>
</html>
