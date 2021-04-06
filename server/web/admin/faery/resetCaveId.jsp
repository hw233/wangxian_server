<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.homestead.faery.service.FaeryConfig"%>
<%@page import="com.fy.engineserver.homestead.cave.Cave"%>
<%@page import="com.fy.engineserver.homestead.faery.service.FaeryManager"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>


<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.PetEggPropsEntity"%>
<%@page import="com.fy.engineserver.sprite.pet.PetManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.PetEggProps"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.PetProps"%>
<%@page import="com.fy.engineserver.sprite.pet.Pet"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.PetPropsEntity"%><html>
<head>
<title>清除无效仙府id</title>

</head>
<body>

<%
	String playerId = request.getParameter("playerId");
	String pwd = request.getParameter("pwd");
	
	if(playerId != null && !playerId.equals("")){
		if (!"buzhidaomimajiubiefa".equals(pwd)) {
			out.print("<H1>不知道密码别乱改</H1>");
			return;
		}
		Player player = GamePlayerManager.getInstance().getPlayer(playerId);
		long caveId = player.getCaveId();
		if (caveId > 0) {
			Cave cave = FaeryManager.caveEm.find(caveId);
			if (cave == null) {
				player.setCaveId(0);
				out.println("<H1>清除无效仙府id成功！</H1>");
			} else {
				if (FaeryManager.getInstance().getCave(player) == null) {
					cave.setStatus(FaeryConfig.CAVE_STATUS_KHATAM);
					FaeryManager.getInstance().getKhatamMap().put(cave.getOwnerId(), cave.getId());
					out.println("<H1>封印玩家仙府，成功！</H1>");
					return ;
				}
				out.println("<H1>玩家仙府存在，不能清除！</H1>");
			}
			
		} else {
			Cave cv = FaeryManager.getInstance().getCave(player);
			if (cv != null && cv.getOwnerId() == player.getId()) {
				player.setCaveId(cv.getId());
				out.println("设置成功！！");
			} else {
				out.println("<H1>玩家本来就木有仙府</H1>");
			}
		}
	}
%>

<form action="">
	玩家名:<input type="text" name="playerId"/>
	密码<input type="password" name="pwd"/>
	<input type="submit" value="submit"/>
</form>


</body>
</html>
