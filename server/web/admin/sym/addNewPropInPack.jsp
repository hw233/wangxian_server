<%@page import="com.fy.engineserver.activity.ActivitySubSystem"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.ArticleProperty"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.platform.PlatformManager.Platform"%>
<%@page import="com.fy.engineserver.platform.PlatformManager"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.PackageProps"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>
<%
	if(!PlatformManager.getInstance().isPlatformOf(Platform.官方)){
		out.print("平台不符！");
		return;
	}
	
	Article article = null;
	for(Article a : ArticleManager.getInstance().getAllArticles()){
		if(a.getName().equals("紫色法宝兑换券")){
			article = a;
		}
	}
	
	if(article == null){
		out.print("获取道具失败");
		return;
	}
	
	PackageProps[] allPackageProps = ArticleManager.getInstance().getAllPackageProps();
	PackageProps pp = null;
	
	for(PackageProps props : allPackageProps){
		if(props.getName().equals("超级黑暗大礼包")){
			pp = props;
		}
	}
	
	
	
	if(pp == null){
		out.print("pp == null");
		return;
	}

	String oldDesc = pp.getDescription();
	pp.setDescription("内含：7级宝石随机袋*1，深渊长生之灵(鞋)*1,紫色法宝兑换券*1");
	out.print("[礼包描述修改] [礼包:"+pp.getName()+"] [old:"+oldDesc+"] [new:"+pp.getDescription()+"]<br>");
	ArticleProperty[] articleNames = pp.getArticleNames();
	
	ArticleProperty ap = new ArticleProperty();
	ap.setArticleName(article.getName());
	ap.setCount(1);
	ap.setColor(article.getColorType());
	
	String oldArticleMesss = "";
	List<ArticleProperty> list = new ArrayList<ArticleProperty>();
	for(ArticleProperty app : articleNames){
		list.add(app);
		oldArticleMesss = oldArticleMesss + "[老的道具:"+app.getArticleName()+"] [颜色:"+app.getColor()+"] [数量:"+app.getCount()+"]<br>";
	}
	
	list.add(ap);
	pp.setArticleNames(list.toArray(new ArticleProperty[]{}));
	articleNames = pp.getArticleNames();
	
	String newArticleMesss = "";
	for(ArticleProperty app : articleNames){
		newArticleMesss = newArticleMesss + "[新的道具:"+app.getArticleName()+"] [颜色:"+app.getColor()+"] [数量:"+app.getCount()+"]<br>";
	}
	out.print(oldArticleMesss);
	out.print(newArticleMesss);
	ActivitySubSystem.logger.warn(oldArticleMesss);
	ActivitySubSystem.logger.warn(newArticleMesss);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>台湾添加新的物品类型</title>
</head>
<body>

</body>
</html>