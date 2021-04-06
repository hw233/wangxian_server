<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="../header.jsp"   %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh-cn">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>功能模块管理</title>
<style type="text/css">
body {
	background-color: #c8edcc;
}
</style>
<link rel="stylesheet" href="../css/style.css"/>
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
    function insert(tag,tid1){
       var tab = document.getElementById("tt");
       var index = findpos(tag);
       var tid =tid1;
       if(!tag&&!tid){
         index =-2;
           tid = -1;
         }
       var nextRow = tab.insertRow(index+1);
	   nextRow.onmouseover=function(){ overTag(this);};
	   nextRow.onmouseout=function(){ outTag(this);};
	   var cel1 = nextRow.insertCell(-1);
	   var cel2 = nextRow.insertCell(-1);
	   var cel3 = nextRow.insertCell(-1);
	   cel1.innerHTML = "名称：<input type='text'  id='name' name='name' value = ''/>";
	   cel2.innerHTML = "描述：<input type = 'text'  id='memo' name ='memo' value =''/>";
	   cel3.innerHTML = "<a href='#' onclick='add(this,"+tid+");'>确认</a>|<a href='#' onclick='cancer();'>取消</a>";
    }
    function add(tag,tid){
      var name=document.getElementById("name").value;
      var memo=document.getElementById("memo").value;
      if(name&&memo){
      var ff = document.getElementById("f1");
      ff.action ="?action=add&tid="+tid;
      ff.submit();
      }else{
        alert("输入的内容不能为空");
      }
    }
    function update(tag,tid){
       var trRow = tag.parentNode.parentNode;
      // alert(trRow.nodeName);
       var cells = trRow.cells;
       var name=cells[0].innerHTML;
	   cells[0].innerHTML = "名称：<input type='text'  id='name' name='name' value = '"+name+"'/>";
	   var memo=cells[1].innerHTML;
	   cells[1].innerHTML ="描述：<input type = 'text'  id='memo' name ='memo' value ='"+memo+"'/>";
	   cells[2].innerHTML = "<a href='#' onclick='udcommit(this,"+tid+");'>确认</a>|<a href='#' onclick='cancer();'>取消</a>";
    }
    function udcommit(tag,tid){
      var name=document.getElementById("name").value;
      var memo=document.getElementById("memo").value;
      if(name&&memo){
      var ff = document.getElementById("f1");
      ff.action ="?action=update&tid="+tid;
      ff.submit();}
      else{
       alert("内容不能为空");
      }
    }
    function delete1(tid){
      if(window.confirm("你是否确定要删除此模块")) {
      window.location.replace("types_manager.jsp?delid="+tid);
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
	        
	        //String url = request.getRequestURI();
	        //out.print("**********权限为 ：：：：" + CheckItem.checkResult(gmusername,url));
	        out.print("<input type='button' value='刷新' onclick='window.location.replace(\"types_manager.jsp\")' />");
		    ModuleTypeManager tmanager = ModuleTypeManager.getInstance();
		    String  delid = request.getParameter("delid");
		    if(delid!=null){
		       tmanager.deleteType(Integer.parseInt(delid.trim()));
		       response.sendRedirect("types_manager.jsp");
		    }
		    String action = request.getParameter("action");
		    if(action!=null){
		    if("add".equals(action.trim())){
		       int typeid = Integer.parseInt(request.getParameter("tid").trim());
		       String name = request.getParameter("name");
		       String memo = request.getParameter("memo");
		       ModuleType type =new ModuleType();
		       type.setName(name);
		       type.setMemo(memo);
		       tmanager.insertType(type,typeid);
		      }
		    if("update".equals(action.trim())){
		       int tid = Integer.parseInt(request.getParameter("tid").trim());
		       ModuleType type = tmanager.getType(tid);
		       String name = request.getParameter("name");
		       String memo = request.getParameter("memo");  
		       type.setName(name);
		       type.setMemo(memo);
		       tmanager.updateType(type);
		      }
		    }    
		    List<ModuleType> types = tmanager.getTypes();
		    out.println("<form id ='f1' method='post' action='#'><table width=80% ><tbody id='tt'><tr><th>类型名</th><th>描述</th><th>操作</th></tr>");
		    if(types!=null&&types.size()>0){
		    for(int i=0;i<types.size();i++){
		       ModuleType type1 = types.get(i);
		       out.print("<tr onmouseover='overTag(this);' onmouseout='outTag(this);'><td>"+type1.getName()+"</td><td>"+type1.getMemo()+"</td><td>");
		       out.print("<input type ='button' value='增加' onclick='insert(this,\""+type1.getId()+"\");'/> | ");
		       out.print("<input type ='button' value='删除' onclick='delete1("+type1.getId()+");'/> | ");
		       out.print("<input type ='button' value='管理' onclick='window.location.replace(\"tmodule_manager.jsp?typeid="+type1.getId()+"\")'/> | ");
		       out.print("<input type ='button' value='修改' onclick='update(this,\""+type1.getId()+"\");'/></td></tr>");
		    }}
		    out.print("</tbody></table><p align=\"center\"><input type ='button' value='增加' onclick='insert();'/></p></form>");
		    }catch(Exception e){
		       out.print(StringUtil.getStackTrace(e));
		    }
	%>
  
</body>
</html>
