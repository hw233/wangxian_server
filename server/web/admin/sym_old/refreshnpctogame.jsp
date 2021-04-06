<%@page import="com.fy.engineserver.core.LivingObject"%>
<%@page import="com.fy.engineserver.core.Game"%>
<%@page import="com.fy.engineserver.core.GameManager"%>
<%@page import="com.fy.engineserver.sprite.npc.MemoryNPCManager"%>
<%@page import="com.fy.engineserver.sprite.npc.NPCManager"%>
<%@page import="com.fy.engineserver.sprite.npc.NPC"%>
<%@page import="com.fy.engineserver.platform.PlatformManager.Platform"%>
<%@page import="com.fy.engineserver.platform.PlatformManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>
<%
	if(PlatformManager.getInstance().isPlatformOf(Platform.台湾)==false){
		out.print("非法操作！");
		return;
	}	
	
	NPC npc = MemoryNPCManager.getNPCManager().createNPC(600184);
	NPC npc2 = MemoryNPCManager.getNPCManager().createNPC(600140);
	NPC npc3 = MemoryNPCManager.getNPCManager().createNPC(600142);
	NPC npc4 = MemoryNPCManager.getNPCManager().createNPC(600173);
	
	if(npc==null||npc2==null||npc3==null||npc4==null){
		out.print("npc不存在！");
		return;
	}
	
	for(int i=1;i<4;i++){
		Game game = GameManager.getInstance().getGameByName("piaomiaowangcheng", i);
		if(game!=null){
			LivingObject livingObjectArray[] = game.getLivingObjects();
			for(LivingObject lo:livingObjectArray){
				if (lo instanceof NPC) {
					if (((NPC) lo).getnPCCategoryId() == 600184) {
						game.removeSprite((NPC) lo);
						out.print("已经存在;piaomiaowangcheng--"+npc.getName()+"<br>");
					} 
				} 
			}
			npc.setX(1551);
			npc.setY(1825);
			game.addSprite(npc);
			out.print("添加npc成功"+game.getGameInfo().getName()+"--"+npc.getName()+"<br>");
		}
	}
	
	for(int i=1;i<4;i++){
		Game game = GameManager.getInstance().getGameByName("piaomiaowangcheng", i);
		if(game!=null){
			LivingObject livingObjectArray[] = game.getLivingObjects();
			for(LivingObject lo:livingObjectArray){
				if (lo instanceof NPC) {
					if (((NPC) lo).getnPCCategoryId() == 600173) {
						game.removeSprite((NPC) lo);
						out.print("已经存在;piaomiaowangcheng--"+npc4.getName()+"<br>");
					} 
				} 
			}
			npc4.setX(3522);
			npc4.setY(586);
			game.addSprite(npc4);
			out.print("添加npc成功"+game.getGameInfo().getName()+"--"+npc4.getName()+"<br>");
		}
	}
	
	
	
	Game game = GameManager.getInstance().getGameByName("yongancheng",0);
	if(game!=null){
		LivingObject livingObjectArray[] = game.getLivingObjects();
		for(LivingObject lo:livingObjectArray){
			if (lo instanceof NPC) {
				if (((NPC) lo).getnPCCategoryId() == 600140) {
					game.removeSprite((NPC) lo);
					out.print("已经存在;piaomiaowangcheng--"+npc2.getName()+"<br>");
				} 
			} 
		}
		npc2.setX(2029);
		npc2.setY(293);
		game.addSprite(npc2);
		out.print("添加npc成功"+game.getGameInfo().getName()+"--"+npc2.getName()+"<br>");
	}
	
	Game game2 = GameManager.getInstance().getGameByName("yongancheng",0);
	if(game2!=null){
		LivingObject livingObjectArray[] = game.getLivingObjects();
		boolean isexit = false;
		for(LivingObject lo:livingObjectArray){
			if (lo instanceof NPC) {
				if (((NPC) lo).getnPCCategoryId() == 600142) {
					game.removeSprite((NPC) lo);
					out.print("已经存在;yongancheng--"+npc3.getName()+"<br>");
				} 
			} 
		}
		npc3.setX(2143);
		npc3.setY(346);
		game2.addSprite(npc3);
		out.print("添加npc成功"+game2.getGameInfo().getName()+"--"+npc3.getName()+"<br>");
	}
	

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>刷新npc</title>
</head>
<body>

</body>
</html>