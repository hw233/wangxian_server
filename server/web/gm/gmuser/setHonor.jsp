<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.honor.HonorManager"%>
<%@page import="com.fy.engineserver.honor.HonorTemplete"%>
<%@page import="com.fy.engineserver.honor.Honor"%>
<%@include file="../authority.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=utf-8">
		<title>设置称号 </title>
		<%
			String playerId = request.getParameter("playerId");
			String honorName = request.getParameter("honorName");
			String removePlayerId = request.getParameter("removePlayerId");
			String removeHonorName = request.getParameter("removeHonorName");
		%>
		<script type="text/javascript">
</script>
	</head>
	<body>
		<form id="form">
		设置玩家称号：
			玩家ID：
			<input type="text" id="playerId" name="playerId" value="">
			称号名称：
			<input type="text" id="honorName" name="honorName" value="">
			<input type="submit" value="提交">
		</form>
		<br>
		<br>
		<form id="form">
		删除玩家称号：
			玩家ID：
			<input type="text" id="removePlayerId" name="removePlayerId" value="">
			称号名称：
			<input type="text" id="removeHonorName" name="removeHonorName" value="">
			<input type="submit" value="提交">
		</form>
		<%
		try{
			if(playerId!=null&&honorName!=null){
				PlayerManager pm=PlayerManager.getInstance();
				Player player=null;
				try{
					player=pm.getPlayer(Integer.parseInt(playerId));				
				}catch(Exception e){
					e.printStackTrace();
					out.println("ID:"+playerId+"的玩家不存在！");
				}
				if(player!=null){
					if(!honorName.equals("英俊的相公")&&!honorName.equals("美丽的娘子")){
						HonorManager hm=HonorManager.getInstance();
						HonorTemplete ht=hm.getHonorTemplete(honorName);
						if(ht!=null){
							hm.gainHonor(player,ht);
							out.println(honorName+"称号添加成功！");
						}else{
							out.println(honorName+"称号不存在！");
						}
					}else{
						out.println("无法添加"+honorName+"称号！");
					}
				}
			}
			
			if(removePlayerId!=null&&removeHonorName!=null){
				PlayerManager pm=PlayerManager.getInstance();
				Player player=null;
				try{
					player=pm.getPlayer(Integer.parseInt(removePlayerId));				
				}catch(Exception e){
					e.printStackTrace();
					out.println("ID:"+removePlayerId+"的玩家不存在！");
				}
				if(player!=null){
					if(!removeHonorName.equals("英俊的相公")&&!removeHonorName.equals("美丽的娘子")){
						HonorManager hm=HonorManager.getInstance();
						Honor honor=hm.getHonor(player,removeHonorName);
						if(honor!=null){
							hm.loseHonor(player,honor,HonorManager.LOSE_REASON_NOT_MEET_STANDARDS);
							out.println(removeHonorName+"称号删除成功！");
						}else{
							out.println("玩家没有"+removeHonorName+"这个称号！");
						}
					}else{
						out.println("无法删除"+removeHonorName+"称号！");
					}
				}
			}		
		}catch(Exception e){
			e.printStackTrace();
		}
		 %>
	</body>
</html>
