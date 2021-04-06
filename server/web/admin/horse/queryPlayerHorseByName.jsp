<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.fy.engineserver.sprite.horse.*"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

%>
<%@page import="com.fy.engineserver.sprite.horse.Horse"%>

<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Soul"%><html>
  <head>
    <title>根据玩家name查询坐骑</title>
  </head>
  
  <body>
    <%
 
    	String name = request.getParameter("name");
    
    	if(name != null && !name.equals("")){
    		
    		Player player = PlayerManager.getInstance().getPlayer(name);
    		
     		Soul ben  = player.getMainSoul();
     		ArrayList<Long> list = ben.getHorseArr();
     		if(list != null && list.size() > 0){
    	 		for(long id : list){
    	 			
    	 			Horse h = HorseManager.getInstance().getHorseById(id,player);
    	 			if(h != null){
    	 				out.print("本尊horseId:"+id+"  name:"+h.getHorseName()+"<br/>");
    	 			}else{
    	 				out.print("本尊horseId:"+id+"  这个坐骑为null<br/>");
    	 			}
    	 		}
     		}else{
     			out.print("本尊没有坐骑<br/>");
     		}
     		
     		
     		Soul soul  = player.getSoul(1);
     		ArrayList<Long> list1 = soul.getHorseArr();
     		if(list1 != null && list1.size() > 0){
    	 		for(long id : list1){
    	 			Horse h = HorseManager.getInstance().getHorseById(id,player);
    	 			if(h != null){
    	 				out.print("元神horseId:"+id+"  name:"+h.getHorseName()+"<br/>");
    	 			}else{
    	 				out.print("元神horseId:"+id+"  这个坐骑为null<br/>");
    	 			}
    	 		}
     		}else{
     			out.print("元神没有坐骑<br/>");
     		}
    		return ;
    	}
    
    
    
 	%>
 	
 	
 	<form action="">
 		玩家name:<input type="text" name="name"/> <br/>
 		<input type="submit" value="submit"> 
 	
 	</form>
  </body>
</html>
