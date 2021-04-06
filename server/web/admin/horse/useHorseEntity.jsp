<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>



<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.concrete.*"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.core.Game"%>

<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.HorseProps"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.Knapsack"%><html>
  <head>
    <base href="<%=basePath%>">
	
	<title>使用坐骑道具</title>
  </head>
  
  <body>
	<% 
		try{
			String horseId =  request.getParameter("horseUse");
			if(horseId != null){
				long id = Long.parseLong(horseId.trim()) ;
				ArticleEntityManager aem = ArticleEntityManager.getInstance();
				ArticleEntity ae = aem.getEntity(id);
				Player player = null;
				
				long playerId = Long.parseLong(session.getAttribute("playerid").toString().trim());
				PlayerManager pm = PlayerManager.getInstance();
				player = pm.getPlayer(playerId);
				if(player != null){
					Game game = player.getCurrentGame();
					Article a =  ArticleManager.getInstance().getArticle(ae.getArticleName()) ;
					if(a instanceof HorseProps){
						HorseProps pp = (HorseProps)a;
						boolean success = pp.use(game, player, ae);
						if (success) {
							// 使用后就消失
							if (!pp.isUsedUndisappear()) {
								player.removeArticleEntityFromKnapsackByArticleId(id);
								if (aem != null) {
									aem.recycleEntity(ae, ArticleEntityManager.DELETE_USE, player);
								}
							}
						}
					}
				}
			}
		
	%>
	<h3>成功</h3>
	<%
		} catch (Exception e) {
			e.printStackTrace();
			
			%>
			<h3>失败</h3>
			
			<%
		}
	%>
	<a href="<%=path %>/admin/horse/queryHorse.jsp">返回</a>
	
  </body>

</html>
