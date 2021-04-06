<%@page import="com.fy.engineserver.core.Game"%>
<%@page import="com.xuanzhi.tools.text.ParamUtils"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.chat.ChatMessageService"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%!
public static List<String> whiteAddress = new ArrayList<String>();
static
{
	whiteAddress.add(Game.网关地址);
	whiteAddress.add("124.248.40.21");
	whiteAddress.add("106.120.222.214");
	whiteAddress.add("116.213.192.216");
}


%>

<%
	String remoteAddr = request.getRemoteAddr();
	boolean validAddress = false;
	if (whiteAddress.contains(remoteAddr)) {
		validAddress = true;
	} else {
			for (String s : whiteAddress) {
				if (s.endsWith("*")) {
					if (remoteAddr.startsWith(s.substring(0, s.length() - 1))) {
						validAddress = true;
						break;
					}
				}
		}
	}

	//	playerId="+playerId+"&hour="+hour+"&reason="+reason+"&level="+sliencelevel
	
	if(validAddress)
	{
		try
		{
			long playerId = ParamUtils.getLongParameter(request, "playerId", 0l);
			int hour = ParamUtils.getIntParameter(request, "hour", 0);
			String reason = ParamUtils.getParameter(request, "reason", "");
			int sliencelevel = ParamUtils.getIntParameter(request, "level", 2);
			
			ChatMessageService.getInstance().unSilencePlayer(playerId);
			out.print("success");
		}
		catch(Exception e)
		{
			out.print("fail,exception");
		}
		
	
	}
	else
	{
		out.print("fail");
	}

%>
