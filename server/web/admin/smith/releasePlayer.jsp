<%@ page contentType="text/html;charset=utf-8"%>
<%@ page import="java.util.*,com.fy.engineserver.smith.*"%>
<%
String playerId = request.getParameter("playerId");
if(playerId != null) {
	long pid = Long.valueOf(playerId);
	MoneyRelationShipManager msm = MoneyRelationShipManager.getInstance();
	msm.getForbidPlayers().remove(pid);
	ArticleRelationShipManager asm = ArticleRelationShipManager.getInstance();
	asm.getForbidPlayers().remove(pid);
	out.println("已经解封了玩家,id为:" + pid);
}
%>