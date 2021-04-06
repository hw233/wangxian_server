<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.activity.explore.ExploreEntity"%>
<%@page import="com.fy.engineserver.activity.explore.ExploreManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.*"%>

<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>使用寻宝道具</title>
</head>
<body>

	<%
		String name = request.getParameter("playerName");
		if(name == null){
			%>
			<form action="" name="useProps">
				用户名：<input type="text" name="playerName" />
				<input type="submit" value="提交"/>
			</form>
			<%
		}else{
			PlayerManager pm = PlayerManager.getInstance();
			try{
				Player player =  pm.getPlayer(name);
				
				ExploreEntity ee = player.getExploreEntity();
				if(ee != null){
					ArticleEntity ae =  ArticleEntityManager.getInstance().getEntity(ee.getPropsId());
					if(ae != null){
						Article a =  ArticleManager.getInstance().getArticle(ae.getArticleName());
						if(a instanceof ExploreProps){
							ExploreProps ep = (ExploreProps)a;
							ep.use(player.getCurrentGame(),player,ae);
						}
					}
				}else{
					out.print("玩家没有寻宝任务");
					
				}
			}catch(Exception e){
				e.printStackTrace();
				out.print("没有这个玩家" + name);
			}
		}
	
	%>
	


</body>
</html>