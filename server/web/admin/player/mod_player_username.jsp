<%@ page contentType="text/html;charset=utf-8" %>
<%@page import="java.util.*,com.xuanzhi.tools.text.*,java.lang.reflect.*,
com.fy.engineserver.sprite.*,com.xuanzhi.tools.transport.*,com.google.gson.*,com.xuanzhi.boss.game.*"%>
<% 
	PlayerManager sm = PlayerManager.getInstance();

	Player p = sm.getPlayer(1119000000000023163l);
	
	if(p != null)
	{
		sm.updatePlayerUsername(p, "13935408961");
	}

	
	
%>
