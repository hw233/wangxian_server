<%@ page contentType="text/html; charset=utf-8"%>
<%@page import="com.xuanzhi.boss.gmxml.Module"%>
<%@ include file="header.jsp"   %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh-cn">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>角色更新</title>
<link rel="stylesheet" href="../../css/style.css"/>
<script type="text/javascript">
   function sub(){
     var id = document.getElementById("rid").value;
     var name = document.getElementById("name").value;
     if(name){
       var f1 = document.getElementById("f1");
       f1.submit();
     }else{
      alert("name不能为空");
     }
   }
   
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
	<h1 align="d">角色更新</h1>
	<%  XmlRoleManager rmanager = XmlRoleManager.getInstance();
	    XmlServerManager smanager = XmlServerManager.getInstance();
	    ModuleTypeManager tmanager = ModuleTypeManager.getInstance();
	    List<XmlServer> servers = smanager.getServers();
	    List<XmlModule> modules = tmanager.getModules();   
	    String rid = request.getParameter("rid");
	    XmlRole role =null;
	    if(rid!=null)
	     role = rmanager.getRole(rid.trim());
	    String update = request.getParameter("update");
	    if(update!=null){
	       String name = request.getParameter("name");
	       List<String> ses = new ArrayList<String>();
	       String sid[] = request.getParameterValues("sid");
	       if(sid!=null){
	       for(String s:sid){
	         ses.add(s);
	        }}
	       List<Module> mods = new ArrayList<Module>();
	       String mid[] = request.getParameterValues("mid");
	       if(mid!=null){
		       for(int i=0;i<mid.length;i++){
		           String permission = request.getParameter(mid[i]);
		           Module mo = new Module();
		           mo.setId(mid[i]);
		           mo.setPermission(permission);
		           mods.add(mo);
		       }
	       }
	       role.setName(name);
	       role.setServers(ses);
	       role.setModules(mods); 
	       rmanager.update(role);
	       response.sendRedirect("trole_list.jsp");
	    }
	    out.print("<form action='?update=true' method='post' id='f1'><table><tr><th>id</th><td class='top'><input type='hidden' name='rid' id='rid' value='"+role.getId()+"' />"+role.getId()+"</td></tr>");
	   	out.print("<tr><th>name</th><td ><input type='text' name='name' id='name' value='"+role.getName()+"' /></td></tr>");
        out.print("<tr><th>服务器列表</th><td ><table ");
        
        for(XmlServer server:servers){
          out.print("<tr onmouseover='overTag(this);' onmouseout='outTag(this);' ><td class='top' ><input type='checkbox' name=\"sid\" value='"+server.getId()+"'" );
          if(rmanager.serverIsIn(server.getId(),role))
          out.println("checked");
          out.print("/>"+server.getId()+"</td><td class='top' >"+server.getDescription()+"</td><tr>");
        }
        out.print("</table></td></tr><tr><th>功能模块列表</th><td ><table  ");
        for(XmlModule module:modules){
          out.print("<tr onmouseover='overTag(this);' onmouseout='outTag(this);' ><td class='top' ><input type='checkbox' name='mid' value='"+module.getId()+"'");
          String permission ="";
          if(rmanager.moduleIsIn(module.getId(),role)){
          out.print("checked");
          permission = rmanager.modulePermission(module.getId(),role);
           }
          out.print(" />"+module.getId()+"</td><td class='top' >"+module.getDescription()+"</td><td class='top' >高级<input type='radio' value='true' name ='"+module.getId()+"'" );
          if("true".equals(permission))
          out.print("checked");
          out.print("/>普通<input type='radio' value='false' name ='"+module.getId()+"'");
          if("false".equals(permission))
          out.print("checked");
          if("".equals(permission))
          out.print("checked");
          out.print(" /></td><tr>");
        }
        out.print("</table></td></tr>");
        out.print("<tr><td colspan=2><input type='button' onclick='sub();' value='确认' /><input type='button' value='返回列表' onclick='window.location.replace(\"trole_list.jsp\")' /></td></tr></table></form>");      
	    %>
  
</body>
</html>
