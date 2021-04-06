<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.LevelRandomPackage"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%
Article a = ArticleManager.getInstance().getArticle("屠魔帖●天罡");
// if(a!=null){
	out.print("a.getArticleLevel() :"+a.getArticleLevel() +"<br>");
// 	if(a instanceof LevelRandomPackage){
		LevelRandomPackage lp = (LevelRandomPackage)a;
		Player p = PlayerManager.getInstance().getPlayer("gggg");
// 		out.print("player.getLevel():"+p.getLevel()+"<br>");	
// 		out.print("this.getLevelLimit():"+lp.getLevelLimit()+"<br>");
// 		List<String> list = new ArrayList<String>();
// 		list = lp.getArticleByPlayerLevel(2, p);
// 		out.print("list:"+list.size()+"<br>");
// 		String	randomArticle = list.get(new Random().nextInt(list.size()));
// 		out.print("randomArticle:"+randomArticle);
		out.print(lp.getLowLimit(p.getLevel())+"<---->");
		out.print(lp.getTopLimit(p.getLevel())+"<br>");
		
// 	}else{
// 		out.print("===not is levelrandonmpackage");
// 	}
// }else{
// 	out.print("a==null");
// }



%>
