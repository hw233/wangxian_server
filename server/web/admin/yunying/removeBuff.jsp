<%@ page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.datasource.buff.BuffTemplate"%>
<%@page import="com.fy.engineserver.datasource.buff.Buff"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.engineserver.core.Game"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

<%
	String playerName = request.getParameter("playerName");
	String buffNames = request.getParameter("buffNames");

	if(playerName == null || "".equals(playerName)||buffNames == null || "".equals(buffNames)){
		playerName = "";
		buffNames = "";
	}else{
		Player player = PlayerManager.getInstance().getPlayer(playerName);
		List<Buff> removeBuffs = new ArrayList<Buff>();
		boolean remove = false;
		if(player!=null){
			String buffName[] = buffNames.trim().split(",");
			List<Buff> buffs = player.getAllBuffs();
			for(String bName:buffName){
				for(Buff buff:buffs){
					if(buff.getTemplateName().equals(bName)){
						removeBuffs.add(buff);
						remove = true;
					}
				}		
			}
		}
		
		if(remove){
			for(Buff buff:removeBuffs){
				player.removeBuff(buff);
				if(Game.logger.isInfoEnabled()){
					Game.logger.info("[后台移除玩家buff] "+player.getLogString()+" [buff:"+ buff.getTemplateName() +"]");
				}
			}
		}
	}
	
%>
        <form action="" method="post">
	角色名<input type="text" name="playerName" value="<%=playerName%>">
	buff名<input type="text" name="buffNames" value="<%=buffNames%>">
                <input type="submit" value="查询">
        </form>
</body>
</html>