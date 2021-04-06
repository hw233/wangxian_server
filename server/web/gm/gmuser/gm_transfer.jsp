<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="com.fy.engineserver.datasource.props.Knapsack.Cell"%>
<%@page import="com.fy.engineserver.chat.ChatMessageService"%>
<%@page import="com.fy.engineserver.core.TransportData"%>
<%@include file="../header.jsp"%>
<%@include file="../authority.jsp" %>
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
	    if(pname){
	      window.location.replace("gmlist_move.jsp?pname="+pname);
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
	function jump(){
	  var playername = $("playername").value;
	  var playerId = $("playerid").value;
	  if(playerId)
	   window.location.replace("gm_transfer.jsp?action=move&playerid="+playerId);
	  if(playername)
	   window.location.replace("gm_transfer.jsp?action=move&playername="+playername);
	
	}
</script>
	</head>
	<body>
	    
			<%
				try {
					String uname = session.getAttribute("username").toString();//判断是否登录
					PlayerManager pmanager = PlayerManager.getInstance();
					ActionManager amanager = ActionManager.getInstance();
					String pname = session.getAttribute("gmid").toString();
					out.print("<p></p><p></p><p></p><p></p><form action ='?action=move' method='post' id='f1'>");
					out.print("<font color='red'>角色名："+pname+"(注意：请上线后再移动！！！)</font>");
					out.print("<input type='hidden' name='pname' id='pname' value='"+(pname==null?"":pname)+"' />");
					out.print("<br/><input type='button' value='刷新' onclick='window.location.replace(\"gm_transfer.jsp\")' /><br/>");
					out.print("<font color='red' >由于移动需要一定时间，所以下边信息变化需要手动刷新才能保持更新！</font>");
					if(pname!=null&&!"".equals(pname.trim())){
					   try{
					     Player p = pmanager.getPlayer(pname.trim());
					     String action = request.getParameter("action");
					     if(action!=null&&"move".equals(action)){
					       String mapname = request.getParameter("mapname");
					       String xsit  = request.getParameter("xsit");
					       String ysit  = request.getParameter("ysit");
					       String playername = request.getParameter("playername");
					       String playerid = request.getParameter("playerid");
					       if(mapname!=null&&!"".equals(mapname.trim())&&xsit!=null&&!"".equals(xsit.trim())&&ysit!=null&&!"".equals(ysit.trim())){
					          TransportData td = new TransportData(0,0,0,0,mapname.trim(),Integer.parseInt(xsit.trim()),Integer.parseInt(ysit.trim()));
					           p.getCurrentGame().transferGame(p,td);
					           amanager.save(uname,"移动["+pname+"]至"+mapname+":X:"+xsit+" y:"+ysit);
					           out.print("移动成功！！！");
					          
					          // response.sendRedirect("gmlist_move.jsp?pname="+(pname==null?"":pname));
					          // Thread.sleep(1000l);
					          // out.print("<script type='text/javascript' >window.location.replace(\"gmlist_move.jsp?pname="+(pname==null?"":pname)+"\");</script>");
					         //  return;
					       }
					       if((playerid!=null&&playerid.trim().length()>0)||(playername!=null&&playername.trim().length()>0)){
					          String mn = "";
					          int xs =-1;
					          int ys =-1;
					          Player ps = null;
					          try{
					          if(playername!=null){
					              ps = pmanager.getPlayer(playername);
					          } 
					          if(playerid!=null){
					              ps = pmanager.getPlayer(Integer.parseInt(playerid));
					          }
					           if(ps!=null&&ps.isOnline()){
					           TransportData td = new TransportData(0,0,0,0,ps.getMapName(),ps.getX(),ps.getY());
					           p.getCurrentGame().transferGame(p,td);
					           amanager.save(uname,"移动["+pname+"]至"+ps.getName()+"("+ps.getId()+")处");
					           out.print("成功移动至玩家处！");
					            }else{
					             out.print("<br/>该用户不存在或未上线，移动失败!<br/>");
					           }
					           }catch(Exception e){
					            out.print("请再检查一下角色信息或者出错");
					           }
					       }
					     }
					     if(!p.isOnline()){
					        out.print("<br/><font color='red' >请先上线在执行此操作！！！！</font>");
					     }else{
					       out.print("<table width='50%' ><tr><th>地图名</th><td class='top'>");
					       out.print("<input type='text' id='mapname' name='mapname' value='"+p.getMapName()+"' /></td></tr>");
					       out.print("<tr><th>横坐标</th><td><input type='text' name='xsit' id='xsit' value='"+p.getX()+"' /></td></tr>");
					       out.print("<tr><th>纵坐标</th><td><input type='text' name='ysit' id='ysit' value='"+p.getY()+"' /></td></tr>");
					       out.print("<tr><td colspan='2'><input type='button' onclick='move();' value='移动' /></td></tr> ");
					       out.print("</table></form>");
					       out.print("<table width='50' align='center'><caption>移动到某个玩家处</caption>");
					       out.print("<tr><td class='top'>角色名<input type='text' id='playername' name='playername' value='' />");
					       out.print("或：角色ID<input type='text' id='playerid' name='playerid' value='' /><input type='button' value='移动至该玩家处' onclick='jump();' /></td></tr></table>");
					     }
					   }catch(Exception e){
					      e.printStackTrace();
					      out.print("请输入正确的角色名！！！！");
					   }
					}
				  }catch(Exception e){
				    out.print(StringUtil.getStackTrace(e));
					  // e.printStackTrace();
					  // RequestDispatcher rdp = request
						//	.getRequestDispatcher("visitfobiden.jsp");
					  //rdp.forward(request, response);
					 }
			%>



	</body>
