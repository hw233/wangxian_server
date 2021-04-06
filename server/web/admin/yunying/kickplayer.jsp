<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.xuanzhi.tools.text.ParamUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	//踢人
	long playerId = ParamUtils.getLongParameter(request, "roleid", 0);
	Player player = PlayerManager.getInstance().getPlayer(playerId);
	
	try
	{
		if(player != null)
		{
			if(player.isOnline())
			{
				player.getConn().close();
			}
			out.print("success");
			return;
		}
		else
		{
			out.print("fail");
			return;
		}
	}
	catch(Exception e)
	{
		out.print("fail");
	}
%>