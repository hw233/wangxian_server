<%@page import="com.fy.boss.authorize.model.Passport"%>
<%@page import="com.fy.boss.authorize.service.PassportManager"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="header.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh-cn">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>更新通行证</title>
<link rel="stylesheet" href="../css/style.css"/>
<script language="JavaScript"><!--



--></script>
</head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0">
	<h1>更新通行证</h1>
	<%
	 PassportManager pmanager = PassportManager.getInstance();
	 String idstr = request.getParameter("id");
// 	 Passport passport = pmanager.getPassport(Long.parseLong(idstr));
	 String subed = request.getParameter("subed");
// 	 if(subed!=null) {
// 	    String password=request.getParameter("password");
// 	    String mobile=request.getParameter("mobile");
// 	    String realname=request.getParameter("realname");
// 	    String email=request.getParameter("email");
// 	    if(password != null && !"null".equals(password) && password.trim().length() > 0)
// 	    pmanager.changeUserPassword(passport.getId(), password);
// 	    if(!"null".equals(mobile))
// 	    passport.setMobile(mobile);
// 	    if(!"null".equals(realname))
// 	    passport.setRealname(realname);
// 	    if(!"null".equals(email))
// 	    passport.setEmail(email);
	   // System.out.println(passport.getId()+passport.getUsername());
// 	    pmanager.updatePassport(passport);
// 		response.sendRedirect("passport_search.jsp?username=" + passport.getUsername());
// 		return;
// 	}
	%>
<%-- 	 <form action="passport_update.jsp?a=111&id=<%=passport.getId() %>" method="post"> --%>
	 <table id="test1" align="center" width="70%" cellpadding="0" cellspacing="0" border="0" class="sortable-onload-5-6r rowstyle-alt colstyle-alt no-arrow">
	 <tr>
	  <th align=left width=20%><b>用户名</b><input type="hidden" name="subed" value="true"/></th>
<%-- 	  <td align="left" class="top"><%=passport.getUsername()%><input type="hidden" name="idstr" value="<%=passport.getId()%>"/></td> --%>
	 </tr>
	 <tr>
	  <th align=center width=20%><b>新的密码</b></th>
	  <td align="left"><input type="text" name="password" value=""/></td>
	 </tr>
	 <tr>
	  <th align=center width=20%><b>绑定手机号码</b></th>
<%-- 	  <td align="left"><input type="text" name="mobile" value="<%=passport.getMobile() %>"/></td> --%>
	 </tr>
	 <tr>
	  <th align=center width=20%><b>真实姓名</b></th>
<%-- 	  <td align="left"><input type="text" name="realname" value="<%=passport.getRealname() %>"/></td> --%>
	 </tr>
	 <tr>
	  <th align=center width=20%><b>电子邮件</b></th>
<%-- 	  <td align="left"><input type="text" name="email" value="<%=passport.getEmail() %>"/></td> --%>
	 </tr>
	 <tr > <td colspan="2" align=center><input type="submit" value="确定" />|
	 <input type="button" onclick="window.location.replace('passport_list.jsp')" value="返回上一页" /> 
	 </td> </tr>
    </table>
	</form>
	
</body>
</html>
