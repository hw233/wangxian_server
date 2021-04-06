<%@ page contentType="text/html; charset=utf-8"%>
<%@page import="com.xuanzhi.boss.gmxml.Module"%>
<%@ include file="header.jsp"   %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh-cn">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="../../css/style.css"/>
<title>服务器分组管理</title>
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
	<h1 align="center">服务器分组管理列表</h1>
	<%  
	     out.print("<input type='button' value='刷新' onclick='window.location.replace(\"tfenqugroup_list.jsp\")' />");
	    XmlRoleManager rmanager = XmlRoleManager.getInstance();
	    XmlServerManager smanager = XmlServerManager.getInstance();
	    ModuleTypeManager tmanager = ModuleTypeManager.getInstance();
	    String  delid = request.getParameter("delid");
	    if(delid!=null){
	       rmanager.delete(delid.trim());
	       response.sendRedirect("trole_list.jsp");
	    }
	    List<XmlRole> roles = rmanager.getRoles();
	    out.print("<table width=80% border='1'  ><tr style='bgcolor:#00a0ff' ><th>id</th><th>描述</th><th>功能页列表</th><th>操作</th></tr>");
	    if(roles!=null&&roles.size()>0){
	       for(int i=0;i<roles.size();i++){
	         XmlRole role = roles.get(i);
	         out.print("<tr onmouseover='overTag(this);' onmouseout='outTag(this);'><td>"+role.getId()+"</td><td>"+role.getName()+"</td>");
	         out.print("<td>");
	         List<Module> modules = role.getModules();
	         List<Module> ms = new ArrayList<Module>();
	         if(modules!=null&&modules.size()>0){
	           out.print("<div width='100%' height='100%' >"+modules.size()+"/"+tmanager.getModules().size()+"<table width='100%' height='100%'  ><tr><td class='top' >id</td><td class='top'>描述</td><td class='top' >权限</td></tr>");
               for(Module mo :modules){
                    if(tmanager.getModuleids().contains(mo.getId().trim())){
	                   out.print("<tr><td>"+mo.getId()+"</td><td>");
		               out.print(tmanager.getModule(mo.getId()).getDescription()+"</td>");
		               out.print("<td>"+("true".equals(mo.getPermission())?"高级":"普通")+"</td><tr>");
	             	 }else{
	             	  ms.add(mo);
	             	 }
	              }
	            modules.removeAll(ms);
	            out.print("</table><div>");
	         }
	         out.print("<td>");
	         out.print("<input type='button' value='添加' onclick='window.location.replace(\"trole_add.jsp?index="+i+"\")' />");
	         out.print("<input type='button' value='更新' onclick='window.location.replace(\"trole_update.jsp?rid="+role.getId()+"\")' />");
	         out.print("<input type='button' value='删除' onclick='window.location.replace(\"trole_list.jsp?delid="+role.getId()+"\")' />");
	         out.print("</td></tr>");       
	       }
	    }
	      out.print("</table><input type='button' value='添加' onclick='window.location.replace(\"trole_add.jsp?index="+roles.size()+"\")' />");
	    %>
  
</body>
</html>
