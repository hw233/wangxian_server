<%@page import="com.fy.engineserver.sprite.npc.BiaoCheNpc"%>
<%@page import="com.fy.engineserver.core.LivingObject"%>
<%@page import="com.fy.engineserver.core.Game"%>
<%@page import="com.xuanzhi.tools.text.StringUtil"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.core.TransportData"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%-- <%@include file="../header.jsp"%> --%>
<%-- <%@include file="../authority.jsp" %> --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>解决卡飙车</title>
		<link rel="stylesheet" href="../style.css" />
<script type="text/javascript">
	function followTarget(){
		var tarname = document.getElementById('targetName').value;
		if(tarname){
			window.location.replace("carFollowPlayer.jsp?tarname="+tarname);
		}
	}
</script>
	</head>
	<body>
		<%
			String tarname = request.getParameter("tarname");
			if(tarname!=null&&tarname.trim().length()>0){
				PlayerManager pmanager = PlayerManager.getInstance();
				try{
					Player tarPlayer = pmanager.getPlayer(tarname);
					if(tarPlayer.isOnline()){
						Game game = tarPlayer.getCurrentGame();
						LivingObject[] los = game.getLivingObjects();
						for(LivingObject lo : los){
							if(lo instanceof BiaoCheNpc){
								if(((BiaoCheNpc)lo).getOwnerId() == tarPlayer.getId()){
									lo.setX(tarPlayer.getX());
									lo.setY(tarPlayer.getY());
								}
							}
						}
						out.print("成功的把 "+tarname+" 的镖车移到了他身边！");
					}else{
						out.print("玩家："+tarname+" 不在线！");
					}
					
				}catch(Exception e){
					out.print("异常："+StringUtil.getStackTrace(e));
				}
				
			}else{
				out.print("gm名字或者玩家名字不能为空！！");
			}
		
		%>
		<table>
		<h1>注意：提醒玩家别和镖车在同一视野范围内才可以移动镖车到玩家身边</h1>
		<tr><th>玩家角色名：</th><td><input type='text' id='targetName' name='targetName' value=''/></td></tr>
		<input type="button" onclick="followTarget()" value="确定"/>
		</table>
	</body>
</html>