<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.playerTitles.PlayerTitlesManager"%>
<%@page import="com.fy.engineserver.playerTitles.*"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.datasource.language.Translate"%>
<%@page import="com.fy.engineserver.util.TimeTool"%>
<%@page import="java.util.Date"%><html>
<head>
<title>跨服称号补发</title>

</head>
<body>

	<%
		
		Date date = new Date(116, 9, 16, 21,55);
		
		long ids [] = {1395000000000008787L};
		for(long id : ids){
			try{
				Player player = PlayerManager.getInstance().getPlayer(id);
				boolean result = PlayerTitlesManager.getInstance().addTitle(player, Translate.武神至尊, true);
				PlayerTitle title = null;
				for (PlayerTitle pt : player.getPlayerTitles()) {
					if (pt.getTitleType() == 290) {
						title = pt;
						break;
					}
				}
				
				if(title != null){
					out.print("开始时间："+TimeTool.formatter.varChar23.format(title.getStartTime())+"<br>");
					
					out.print("持续时间："+title.getLastTime()+"<br>");
					
					title.setLastTime((date.getTime()-System.currentTimeMillis()));
					out.print("-------<br>");
					
					out.print("持续时间："+title.getLastTime()+"<br>");
					out.print("[增加称号:"+(result?"成功":"失败")+"] [玩家"+player.getName()+"]");
				}else{
					out.print("失败");
				}
			}catch(Exception e){
				out.print("服务器不对");
			}
		}
	%>

</body>
</html>
