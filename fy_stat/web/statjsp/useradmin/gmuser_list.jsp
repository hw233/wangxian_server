<%@ page contentType="text/html; charset=utf-8"%>
<%@page import="com.xuanzhi.boss.gmuser.model.*"%>
<%@ include file="header.jsp"   %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh-cn">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="../css/style.css"/>
<title>gm用户管理</title>
<script type="text/javascript">
   function overTag(tag){
	  tag.style.color = "red";	
	  tag.style.backgroundColor = "lightcyan";
   }
			
   function outTag(tag){
	  tag.style.color = "black";
	  tag.style.backgroundColor = "white";
   }
</script>
</head>
<body bgcolor="#FFFFFF" >
	<h1 align="center">gm用户列表</h1>
	<%
	   out.print("<input type='button' value='刷新' onclick='window.location.replace(\"gmuser_list.jsp\")' />");
	  GmUserManager gumanager = GmUserManager.getInstance();
	  XmlRoleManager rmanager = XmlRoleManager.getInstance();
	  String delIdstr = request.getParameter("delid");
	  if(delIdstr!=null){
	     gumanager.deleteGmuser(Integer.parseInt(delIdstr));
	     response.sendRedirect("gmuser_list.jsp");
	  }
	  List<Gmuser> gmusers = gumanager.getAllUser();
	  out.print("<table width = 80% ><tbody id = 'tt'><tr><th>id</th><th>用户名</th><th>真实姓名</th><th>GM角色名</th><th>权限角色</th><th>操作</th></tr>");
	  if(gmusers!=null&&gmusers.size()>0){
	      for(Gmuser gmuser:gmusers){//遍历gm用户列表
	        out.print("<tr><td>"+gmuser.getId()+"</td><td>"+gmuser.getUsername()+"</td><td>"+gmuser.getRealname()+"</td><td>"+gmuser.getGmname()+"</td><td>");
	        List<Authority> authorities = gmuser.getAuthos();
	        for(Authority a :authorities){//遍历用户权限
	            out.print(a.getRoleid()+"::"+rmanager.getRole(a.getRoleid()).getName()+"  |  ");
	        }
	        out.print("</td><td>");
	        out.print("<input type='button' value='更新' onclick='window.location.replace(\"gmuser_update.jsp?gid="+gmuser.getId()+"\")' />");
	        out.print("<input type='button' value='删除' onclick='window.location.replace(\"gmuser_list.jsp?delid="+gmuser.getId()+"\")' />");
	        out.print("</td></tr>");    
	      }
	  }
	  out.print("<tr><td colspan=6><input type='button' value='添加' onclick='window.location.replace(\"gmuser_add.jsp\")' /></td></tr></tbody><table>");
	 %>
    
</body>
</html>
