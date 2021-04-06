<%@ page contentType="text/html; charset=utf-8"%>
<%@page import="com.xuanzhi.boss.gmxml.Module"%>
<%@ include file="header.jsp"   %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh-cn">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>角色添加</title>
<link rel="stylesheet" href="../../css/style.css"/>
<script type="text/javascript">
   function sub(){
    // var id = document.getElementById("id").value;
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
	<h1 align="d">角色添加</h1>
	<%  
	     out.print("<input type='button' value='刷新' onclick='window.location.replace(\"trole_add.jsp\")' />");
	    XmlRoleManager rmanager = XmlRoleManager.getInstance();
	    XmlServerManager smanager = XmlServerManager.getInstance();
	    ModuleTypeManager tmanager = ModuleTypeManager.getInstance();
	    List<XmlServer> servers = smanager.getServers();
	    List<XmlModule> modules = tmanager.getModules();
	    int index = rmanager.getRoles().size();
	    String index1 = request.getParameter("index");
	    if(index1!=null)
	    index = Integer.parseInt(index1.trim());
	    String addstr = request.getParameter("add");
	    if(addstr!=null){
	       XmlRole role = new XmlRole();
	       String id = request.getParameter("id");
	       String name = request.getParameter("name");
	       List<String> ses = new ArrayList<String>();
	       String sid[] = request.getParameterValues("sid");
	       if(sid!=null){
	       for(String s:sid){
	       //遍历所有的服务器Id
	         ses.add(s);
	        }
	        }
	       List<Module> mods = new ArrayList<Module>();
	       String mid[] = request.getParameterValues("mid");
	       if(mid!=null){
	         for(int i=0;i<mid.length;i++){
	           //遍历所有选择的模块Id
	           String permission = request.getParameter(mid[i]);
	           Module mo = new Module();
	           mo.setId(mid[i]);
	           mo.setPermission(permission);
	           mods.add(mo);
	         }
	       }
	       role.setId(id);
	       role.setName(name);
	       role.setServers(ses);
	       role.setModules(mods);
	       rmanager.insert(role,index);
	       response.sendRedirect("trole_list.jsp");
	    }
	    out.print("<form action='?add=true&index="+index+"' method='post' id='f1'><table>");
	   	out.print("<tr><th>name</th><td class='top'><input type='text' name='name' id='name' value='' /></td></tr>");
        out.print("<tr><th>服务器列表</th><td ><table ");
      
        for(XmlServer server:servers){
          //偏离服务器列表
          out.print("<tr onmouseover='overTag(this);' onmouseout='outTag(this);' ><td class='top' ><input type='checkbox' name=\"sid\" value='"+server.getId()+"' />"+server.getId()+"</td><td class='top' > ");
          out.print(server.getDescription()+"</td><tr>");
        }
        out.print("</table></td></tr><tr><th>功能模块列表</th><td ><table  ");
        for(XmlModule module:modules){
          //遍历所有的模块
          out.print("<tr onmouseover='overTag(this);' onmouseout='outTag(this);' ><td class='top' ><input type='checkbox' name='mid' value='"+module.getId()+"' />"+module.getId()+"</td><td class='top' >");
          out.print(module.getDescription()+"</td><td class='top' >高级<input type='radio' value='true' name ='"+module.getId()+"' />普通<input type='radio' value='false' name ='"+module.getId()+"' checked /></td><tr>");
        }
        out.print("</table></td></tr>");
        out.print("<tr><td colspan=2><input type='button' onclick='sub();' value='确认' /><input type='button' value='返回列表' onclick='window.location.replace(\"trole_list.jsp\")' /></td></tr></table></form>");
	         
	       
	    %>
  
</body>
</html>
