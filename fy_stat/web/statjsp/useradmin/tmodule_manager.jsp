<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="header.jsp"   %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh-cn">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>功能模块管理</title>
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
    function insert(tag,moduleid1){
       var tab = document.getElementById("tt");
       var index = findpos(tag);
       var moduleid = moduleid1;
       if(!tag&&!moduleid){
          index =-2;
          moduleid = -1;
          }
       var nextRow = tab.insertRow(index+1);
	   nextRow.onmouseover=function(){ overTag(this);};
	   nextRow.onmouseout=function(){ outTag(this);};
	   var cel1 = nextRow.insertCell(-1);
	   var cel0 = nextRow.insertCell(-1);
	   var cel2 = nextRow.insertCell(-1);
	   var cel3 = nextRow.insertCell(-1);
	   var cel4 = nextRow.insertCell(-1);
	   var cel5 = nextRow.insertCell(-1);
	   cel1.innerHTML = "id：<input type='hidden'  id='id' name='id' value = ''/>";
	   cel0.innerHTML = "本地 <input type='radio' name ='base' value='true' checked />游戏服务器<input type='radio' name='base' value='false' />";
	   cel2.innerHTML = "描述：<input type = 'text'  id='description' name ='description' value =''/>";
	   cel3.innerHTML = "<input type='radio' name='isshow' value='true' checked /> 是  <input type='radio' name='isshow' value='false'  />否";
	   cel4.innerHTML = "页面：<input type = 'text' id='pagename' name ='pagename' value =''/>";
	   cel5.innerHTML = "<a href='#' onclick='add(this,"+moduleid+");'>确认</a>|<a href='#' onclick='cancer();'>取消</a>";
    }
    function add(tag,moduleid){
      var description=document.getElementById("description").value;
      var pagename=document.getElementById("pagename").value;
      if(description&&pagename){
      var ff = document.getElementById("f1");
      ff.action ="?action=add&moduleid="+moduleid;
      ff.submit();
      }else{
        alert("输入的内容不能为空");
      }
    }
    function update(tag,moduleid){
       var trRow = tag.parentNode.parentNode;
      // alert(trRow.nodeName);
       var cells = trRow.cells;
       var id=cells[0].innerHTML;
	   cells[0].innerHTML = "id：<input type='hidden' id='id' name='id' value = '"+id+"'/>";
	   var base=cells[1].innerHTML;
	   var basestr ="本地 <input type='radio' name ='base' value='true' ";
	   if(base=="本地")
	     basestr = basestr+" checked ";
	    basestr=basestr+" />游戏服务器<input type='radio' name='base' value='false' ";
	   if(base=="游戏服务器")
	     basestr = basestr+" checked ";
	    basestr=basestr+" />"
	    cells[1].innerHTML = basestr;
	   var description=cells[2].innerHTML;
	    cells[2].innerHTML = "描述：<input type = 'text' id='description' name ='description' value ='"+description+"'/>";
	   var isshow = cells[3].innerHTML;
	    cells[3].innerHTML = "<input type='radio' name='isshow' value='true' checked /> 是  <input type='radio' name='isshow' value='false'  />否";
	   var pagename=cells[4].innerHTML;
	   cells[4].innerHTML = "页面：<input type = 'text' id='pagename' name ='pagename' value ='"+pagename+"'/>";
	   cells[5].innerHTML = "<a href='#' onclick='udcommit(this,"+moduleid+");'>确认</a>|<a href='#' onclick='cancer();'>取消</a>";
    }
    function udcommit(tag,moduleid){
      var index = findpos(tag);
       var id=document.getElementById("id").value;
      var description=document.getElementById("description").value;
      var pagename=document.getElementById("pagename").value;
      if(id&&description&&pagename){
      var ff = document.getElementById("f1");
      ff.action ="?action=update&moduleid="+moduleid;
      ff.submit();}
      else{
       alert("内容不能为空");
      }
    }
    function delete1(moduleid,typeid){
      if(window.confirm("你是否确定要删除此模块")) {
      window.location.replace("tmodule_manager.jsp?delid="+moduleid+"&typeid="+typeid);
      }
    }
   function cancer(){
  //取消操作 
   location.reload();
  }
</script>
</head>
<body bgcolor="#FFFFFF" >
	<h1 align="d">功能模块列表</h1>
	<%
	         try{
	         String gmusername = session.getAttribute("username").toString();
	         out.print("<input type='button' value='刷新' onclick='window.location.replace(\"tmodule_manager.jsp\")' />");
		    ModuleTypeManager tmanager = ModuleTypeManager.getInstance();
		    String typeid =request.getParameter("typeid");
		    String  delid = request.getParameter("delid");
		    
		    if(delid!=null){
		    
		       tmanager.deleteModule(delid.trim(),Integer.parseInt(typeid));
		       response.sendRedirect("tmodule_manager.jsp?typeid="+typeid);
		    }
		    String action = request.getParameter("action");
		    if(action!=null){
		    if("add".equals(action.trim())){
		       int moduleid = Integer.parseInt(request.getParameter("moduleid").trim());
		      // String id = request.getParameter("id");
		       String base = request.getParameter("base");
		       String description = request.getParameter("description");
		       String pagename = request.getParameter("pagename");
		       String isshow = request.getParameter("isshow");
		       XmlModule module =new XmlModule();
		       module.setBase(base);
		       module.setDescription(description);
		       module.setJspName(pagename);
		       module.setIsShow(isshow);
		      tmanager.insertModule(module,moduleid,Integer.parseInt(typeid));
		    }
		    if("update".equals(action.trim())){
		       String moduleid = request.getParameter("moduleid").trim();
		       String base = request.getParameter("base");
		       String description = request.getParameter("description");
		       String pagename = request.getParameter("pagename");
		       String isshow = request.getParameter("isshow");
		       XmlModule module = tmanager.getModule(moduleid);
		       module.setBase(base);
		       module.setDescription(description);
		       module.setIsShow(isshow);
		       module.setJspName(pagename);
		       tmanager.updateModule(module,Integer.parseInt(typeid));
		    }
		    }
		    
		    List<XmlModule> modules = tmanager.getType(Integer.parseInt(typeid)).getModules();
		    out.println("<form id ='f1' method='post' action='#'><table width=80% ><tbody id='tt'><tr><th>id</th><th>位置</th><th>描述</th><th>是否显示</th><th>jsp页面<input type=\"hidden\" name=\"typeid\" value='"+typeid+"'/></th><th>操作</th></tr>");
		    if(modules!=null&&modules.size()>0){
		    for(int i=0;i<modules.size();i++){
		       XmlModule module = modules.get(i);
		       out.print("<tr onmouseover='overTag(this);' onmouseout='outTag(this);'><td>"+module.getId()+"</td><td>"+(module.getBase().equals("true")?"本地":"游戏服务器")+"</td><td>"+module.getDescription()+"</td><td>"+("true".equals(module.getIsShow())?"是":"否")+"</td><td>"+module.getJspName()+"</td><td>");
		       out.print("<input type ='button' value='增加' onclick='insert(this,\""+module.getId()+"\");'/> | ");
		       out.print("<input type ='button' value='删除' onclick='delete1("+module.getId()+",\""+typeid+"\");'/> | ");
		       out.print("<input type ='button' value='修改' onclick='update(this,\""+module.getId()+"\");'/></td></tr>");
		    }}
		    out.print("</tbody></table><p align=\"center\"><input type ='button' value='增加' onclick='insert();'/><input type ='button' value='返回类型表' onclick='window.location.replace(\"types_manager.jsp\")'/></p></form>");
		    }catch(Exception e){
		    out.print(StringUtil.getStackTrace(e));
		    }
	%>
  
</body>
</html>
