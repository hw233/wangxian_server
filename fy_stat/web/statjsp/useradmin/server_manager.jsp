<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="header.jsp"   %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh-cn">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>游戏服务器管理</title>
<link rel="stylesheet" href="../../css/style.css"/>
<script type="text/javascript">
   function overTag(tag){
	  tag.style.color = "red";	
	  tag.style.backgroundColor = "lightcyan";
   }
			
   function outTag(tag){
	  tag.style.color = "black";
	  tag.style.backgroundColor = "white";
   }
    function findpos(tag){
     var index = -2;
     if(tag!=null){
     var tab = document.getElementById("tt");
     var trs = tab.rows;
	 var trNode = tag.parentNode.parentNode;
	 for(var i=0;i<tab.rows.length;i++){
	   	   if(trs[i]==trNode){
	   	  	index=i;
	   	  	break;
   	  	   }
	  }
	  }
	  if(index!=-2)
	  index = index-1;
	  //alert(index);
	  return index;
    }
    function insert(tag){
       var tab = document.getElementById("tt");
       var index = findpos(tag);
       var nextRow = tab.insertRow(index+1);
	   nextRow.onmouseover=function(){ overTag(this);};
	   nextRow.onmouseout=function(){ outTag(this);};
	   var cel1 = nextRow.insertCell(-1);
	   var cel2 = nextRow.insertCell(-1);
	   var cel3 = nextRow.insertCell(-1);
	   var cel4 = nextRow.insertCell(-1);
	   cel1.innerHTML = "id：<input type='hidden'  id='id' name='id' value = ''/>";
	   cel2.innerHTML = "描述：<input type = 'text' id='description' name ='description' value =''/>";
	   cel3.innerHTML = "uri：<input type = 'text' id='uri' name ='uri' value =''/>";
	   cel4.innerHTML = "<a href='#' onclick='add(this);'>确认</a>|<a href='#' onclick='cancer();'>取消</a>";
    }
    function add(tag){
      var index = findpos(tag);
     // var id=document.getElementById("id").value;
      var description=document.getElementById("description").value;
      var uri=document.getElementById("uri").value;
      if(description&&uri){
      var ff = document.getElementById("f1");
      ff.action ="?action=add&index="+index;
      ff.submit();}
      else{
         alert("输入的内容不能为空");
      }
    }
    function update(tag){
       var trRow = tag.parentNode.parentNode;
       //alert(trRow.nodeName);
       var cells = trRow.cells;
       var id=cells[0].innerHTML;
       //alert(id);
     //  alert(cells[0].nodeName);
	   cells[0].innerHTML = "id：<input type='hidden'  id='id' name='id' value = '"+id+"'/>";
	   var description=cells[1].innerHTML;
	   cells[1].innerHTML = "描述：<input type = 'text' id='description' name ='description' value ='"+description+"'/>";
	   var uri=cells[2].innerHTML;
	   cells[2].innerHTML = "uri：<input type = 'text' id='uri' name ='uri' value ='"+uri+"'/>";
	   cells[3].innerHTML = "<a href='#' onclick='udcommit(this);'>确认</a>|<a href='#' onclick='cancer();'>取消</a>";
    }
    function udcommit(tag){
      var index = findpos(tag);
      var id=document.getElementById("id").value;
      var description=document.getElementById("description").value;
      var uri=document.getElementById("uri").value;
      if(id&&description&&uri){
      var ff = document.getElementById("f1");
      ff.action ="?action=update&index="+index;
      ff.submit();}
      else{
       alert("输入框中的内容不能为空");
      }
    }
    function delete1(tag){
      var tdNode = tag.parentNode.parentNode.cells[0];
      var idstr = tdNode.innerHTML;
      if(window.confirm("你是否确定要删除此模块")) {
      window.location.replace("server_manager.jsp?delid="+idstr);
      }
    }
   function cancer(){
  //取消操作 
   location.reload();
  }
</script>
</head>
<body bgcolor="#FFFFFF" >
	<h1 align="d">游戏服务器列表</h1>
	<%
	         out.print("<input type='button' value='刷新' onclick='window.location.replace(\"server_manager.jsp\")' />");
		    XmlServerManager smanager = XmlServerManager.getInstance();
		    String  delid = request.getParameter("delid");
		    if(delid!=null){  
		       smanager.delete(delid.trim());
		       response.sendRedirect("server_manager.jsp");
		    }
		    String action = request.getParameter("action");
		    if(action!=null){
		    if("add".equals(action.trim())){
		       int index = Integer.parseInt(request.getParameter("index").trim());
		      // String id = request.getParameter("id");
		       String description = request.getParameter("description");
		       String uri = request.getParameter("uri");
		       XmlServer server =new XmlServer();
		      // server.setId(id);
		       server.setDescription(description);
		       server.setUri(uri);
		       smanager.insert(server,index+"");
		    }
		    if("update".equals(action.trim())){
		       int index = Integer.parseInt(request.getParameter("index").trim());
		       String id = request.getParameter("id");
		       String description = request.getParameter("description");
		       String uri = request.getParameter("uri");
		       XmlServer server =new XmlServer();
		       server.setId(id);
		       server.setDescription(description);
		       server.setUri(uri);
		      smanager.update(server,index+"");
		    }
		    }
		    
		    List<XmlServer> servers = smanager.getServers();
		    out.println("<form id ='f1' method='post' action='#'><table width=80% ><tbody id='tt'><tr><th>id</th><th>描述</th><th>uri(服务器地址)</th><th>操作</th></tr>");
		    if(servers!=null&&servers.size()>0){
		    for(int i=0;i<servers.size();i++){
		       XmlServer server = servers.get(i);
		       out.print("<tr onmouseover='overTag(this);' onmouseout='outTag(this);'><td>"+server.getId()+"</td><td>"+server.getDescription()+"</td><td>"+server.getUri()+"</td><td>");
		       out.print("<input type ='button' value='增加' onclick='insert(this);'/> | ");
		       out.print("<input type ='button' value='删除' onclick='delete1(this);'/> | ");
		       out.print("<input type ='button' value='修改' onclick='update(this);'/></td></tr>");
		    }}
		    out.print("</tbody></table><p align=\"center\"><input type ='button' value='增加' onclick='insert();'/></p></form>");
	%>
  
</body>
</html>
