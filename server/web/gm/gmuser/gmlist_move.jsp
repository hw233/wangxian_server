<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%-- 	<%@include file="../authority.jsp" %> --%>
<%-- <%@page import="com.fy.engineserver.datasource.props.Knapsack.Cell"%> --%>
<%@page import="com.fy.engineserver.chat.ChatMessageService"%>
<%@page import="com.fy.engineserver.core.TransportData"%>
<%@include file="../header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>潜龙玩家管理 </title>
		<link rel="stylesheet" href="../style.css" />
		<script type="text/javascript">
	function $(tag) {
		return document.getElementById(tag);
	}
	function query(){
	    var pname = $("pname").value;
	    var pid = $("pid").value;
	    if(pname||pid){
	      window.location.replace("gmlist_move.jsp?pname="+pname+"&pid="+pid);
	      }
	    else
	      alert("角色名不能为空？？");
	}
	function move(){
	  var mapname = $("mapname").value;
	  var xsit = $("xsit").value;
	  var ysit = $("ysit").value;
	  if(mapname&&xsit&&ysit){
	    $("f1").submit();
	  }else{
	   alert("请输入正确的信息！！");
	  }
	}
</script>
	</head>
	<body>
	    
			<%
				try {
					PlayerManager pmanager = PlayerManager.getInstance();
					ActionManager amanager = ActionManager.getInstance();
					String pname = request.getParameter("pname");
					String pid = request.getParameter("pid");
					out.print("<p></p><p></p><p></p><p></p><form action ='?action=move' method='post' id='f1'>");
					out.print("<font color='red'>请输入角色名(请先登录后再移动！)</font>：<input type='text' name='pname' id='pname' value='"+(pname==null?"":pname)+"' />");
					out.print("或角色ID:<input type='text' name='pid' id='pid' value='"+(pid==null?"":pid)+"' />");
					out.print("<input type='button' value='查询' onclick='query();' /></br>");
					out.print("<br/><font color='red'>(由于信息变化需要一个过程，所以想获取目标信息，请手动刷新！！)</font><br/><input type='button' value='刷新' onclick='window.location.replace(\"gmlist_move.jsp?pname="+(pname==null?"":pname)+"&pid="+(pid==null?"":pid)+"\")' />");
					if((pname!=null&&!"".equals(pname.trim()))||(pid!=null&&pid.trim().length()>0)){
					   try{
					   Player p =null;
					     if(pname!=null&&!"".equals(pname.trim()))
					      p = pmanager.getPlayer(pname.trim());
					     if(pid!=null&&pid.trim().length()>0)
					      p = pmanager.getPlayer(Integer.parseInt(pid.trim()));
					      out.print("<br/>角色名："+p.getName()+"(id:"+p.getId()+")<br/>");
					     String action = request.getParameter("action");
					     if(action!=null&&"move".equals(action)){
					       String mapname = request.getParameter("mapname");
					       String xsit  = request.getParameter("xsit");
					       String ysit  = request.getParameter("ysit");
					       if(!"".equals(mapname.trim())&&!"".equals(xsit.trim())&&!"".equals(ysit.trim())){
					          TransportData td = new TransportData(0,0,0,0,mapname.trim(),Integer.parseInt(xsit.trim()),Integer.parseInt(ysit.trim()));
					           p.getCurrentGame().transferGame(p,td);
					           amanager.save("","移动["+p.getName()+"]至  地图"+mapname+":X:"+xsit+" y:"+ysit);
					           out.print("移动成功！！！");
					          // response.sendRedirect("gmlist_move.jsp?pname="+(pname==null?"":pname));
					          // Thread.sleep(1000l);
					          // out.print("<script type='text/javascript' >window.location.replace(\"gmlist_move.jsp?pname="+(pname==null?"":pname)+"\");</script>");
					         //  return;
					       }else
					         out.print("移动失败！！");
					     }
					     if(!p.isOnline()){
					        out.print("请先上线在执行此操作！！！！");
					     }else{
					       out.print("<table width='50%' ><tr><th>地图名</th><td class='top'>");
					       out.print("<input type='text' id='mapname' name='mapname' value='"+p.getMapName()+"' /></td></tr>");
					       out.print("<tr><th>横坐标</th><td><input type='text' name='xsit' id='xsit' value='"+p.getX()+"' /></td></tr>");
					       out.print("<tr><th>纵坐标</th><td><input type='text' name='ysit' id='ysit' value='"+p.getY()+"' /></td></tr>");
					       out.print("<tr><td colspan='2'><input type='button' onclick='move();' value='移动' /></td></tr> ");
					       out.print("</table></form>");
					     }
					   }catch(Exception e){
					      e.printStackTrace();
					      out.print("请输入正确的信息 ！！！！");
					   }
					}
				  }catch(Exception e){
				    out.print(StringUtil.getStackTrace(e));
					   //e.printStackTrace();
					  // RequestDispatcher rdp = request
					//		.getRequestDispatcher("visitfobiden.jsp");
					  //rdp.forward(request, response);
					 }
			%>



	</body>
