<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="java.util.Map"%>
<%@page import="com.fy.engineserver.datasource.article.data.equipments.Equipment"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="sun.awt.image.ImageWatched.Link"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.LevelRandomPackage"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.PackageProps"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.RandomPackageProps"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Collection"%>
<%@page import="java.util.Set"%>
<%@page import="com.fy.gamegateway.mieshi.resource.manager.PackageManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.CareerPackageProps"%>
<%@page import="java.util.LinkedList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.PetEggProps"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.ComposeOnlyChangeColorArticle"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.SingleProps"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.SingleForPetProps"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.PetFoodArticle"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.LastingProps"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.HorseProps"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.HorseFoodArticle"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.KnapsackExpandProps"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.LastingForPetProps"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.ClearSkillPointsProps"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.BookProps"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.AvataProps"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.WangZheZhiYinProps"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.ZhaoJiLingProps"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.FateActivityProps"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.ExchangeArticle"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.ExploreProps"%>
<%@page import="com.fy.engineserver.datasource.article.data.equipments.Weapon"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.BottleProps"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.ExploreResourceMap"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.TaskProps"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.Lasting_For_Compose_Props"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.MoneyProps"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.TiliProps"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.ClearRedProps"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.PickFlowerArticle"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.QiLingArticle"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.InlayArticle"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.datasource.article.data.props.PetProps"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>物品分类</title>

<link rel="stylesheet" type="text/css" href="../css/table.css" />
<style type="text/css">
#tbhead{
background-color:#DCE0EB;
}
.color0{
background-color:#ffffff;
}
.color1{
background-color:#35ca41;
}
.color2{
background-color:#35b2fe;
}
.color3{
background-color:#8b50ed;
}
.color4{
background-color:orange;
}
</style>
</head>
<form action="QueryArticlesByName.jsp" method="get">
 <input type="text" name="articleNames"/><input type="submit" value="搜索"><br> 
</form>
<div class="clearfix">
	<div style="float:left;width:200px" id="menuDiv">
	<%
	ArticleManager am = ArticleManager.getInstance();
	Article[] articles = am.getAllArticles();
	
	Map<String,String> map =  new HashMap<String,String>();
	map.put("装备",Equipment.class.getName());
	map.put("宠物蛋",PetEggProps.class.getName());
	map.put("宠物",PetProps.class.getName());
	map.put("一般物品",Article.class.getName());
	map.put("宝石类",InlayArticle.class.getName());
	map.put("可合成类仅仅改变颜色物品",ComposeOnlyChangeColorArticle.class.getName());
	map.put("给玩家用的瞬间恢复药品",SingleProps.class.getName());
	map.put("给宠物用的瞬间恢复药品",SingleForPetProps.class.getName());
	map.put("宠物食物",PetFoodArticle.class.getName());
	map.put("buff类道具",LastingProps.class.getName());
	map.put("坐骑道具",HorseProps.class.getName());
	map.put("喂养坐骑物品",HorseFoodArticle.class.getName());
	map.put("防爆包",KnapsackExpandProps.class.getName());
	map.put("宠物buff类道具",LastingForPetProps.class.getName());
	map.put("洗髓丹",ClearSkillPointsProps.class.getName());
	map.put("技能书",BookProps.class.getName());
	map.put("时装类道具",AvataProps.class.getName());
	map.put("天尊圣印",WangZheZhiYinProps.class.getName());
	map.put("召集令",ZhaoJiLingProps.class.getName());
	map.put("宝箱类道具",PackageProps.class.getName());
	map.put("随机宝箱类道具",RandomPackageProps.class.getName());
	map.put("情缘活动道具",FateActivityProps.class.getName());
	map.put("宝物",ExchangeArticle.class.getName());
	map.put("宝物道具",ExploreProps.class.getName());
	map.put("武器",Weapon.class.getName());
	map.put("古董",BottleProps.class.getName());
	map.put("藏宝图",ExploreResourceMap.class.getName());
	map.put("触发任务道具",TaskProps.class.getName());
	map.put("酒",Lasting_For_Compose_Props.class.getName());
	map.put("银子",MoneyProps.class.getName());
	map.put("职业包裹",CareerPackageProps.class.getName());
	map.put("洗红名",ClearRedProps.class.getName());
	map.put("体力丹",TiliProps.class.getName());
	map.put("采花",PickFlowerArticle.class.getName());
	map.put("器灵",QiLingArticle.class.getName());
	map.put("等级随机礼包",LevelRandomPackage.class.getName());
	
	
	Map<String,List<Article>> mm = new HashMap<String,List<Article>>();
	for(int i=0;i<articles.length;i++){
		/*if(articles[i].getName().equals("黑风熊精蛋")){
			out.print(articles[i].getClass().getName());
		}*/
		if(!mm.containsKey(articles[i].getClass().getName())){
			List<Article> ll = new ArrayList<Article>();
			ll.add(articles[i]);
			mm.put(articles[i].getClass().getName(),ll);
		}else{
			mm.get(articles[i].getClass().getName()).add(articles[i]);
		}
	}
	
	out.print("<table style='font-size: 12px; text-align: center;' border='1'><tr id='tbhead'><td>物品分类</td></tr>");
	out.print("<tr><td><a href='?type=all'>显示全部</a></td><tr>");
	Set<String> ss = map.keySet();
	Iterator<String> it = ss.iterator();
	while(it.hasNext()){
		String aType = it.next();
		//out.print("<li class='li'><a href='?type="+aType+"'>"+aType+"</a></li><br>");
		out.print("<tr><td><a href='?type="+aType+"'>"+aType+"</a></td><tr>");
	}
	out.print("</table>");
	%>
	</div>
	<div style="float:left;width:800px;" id="showDetail">
	<%
	String type = request.getParameter("type");
	if(null==type||"".equals(type)){
		out.print("请选择物品类型O(∩_∩)O~");
	}else if(type.equals("all")){
		out.print("<table style='font-size: 12px; text-align:left;width:600px;position:relative;left:2px' border='1'><tr id='tbhead'><td>物品名</td><td>物品统计名</td><td>等级</td><td>颜色</td><td>描述</td></tr>");
		for(int i=0;i<articles.length;i++){
			out.print("<tr class='color"+articles[i].getColorType()+"'><td><a href='ArticleByName.jsp?articleName="+articles[i].getName()+"'>"+articles[i].getName()+"</a></td><td><a href='ArticleByName.jsp?articleCNName="+articles[i].getName()+"'>"+articles[i].getName_stat()+"</a></td><td>"+articles[i].getArticleLevel()+"</td><td>"+articles[i].getColorType()+"</td><td>"+articles[i].getDescription()+"</td>");
		}
	}else if(map.containsKey(type)){
		Iterator<Article> iter = mm.get(map.get(type)).iterator();
		out.print("<font css='bold'>"+type+"</font><br>");
		out.print("<table style='font-size: 12px; text-align:left;width:600px;position:relative;left:2px' border='1'><tr id='tbhead'><td>物品名</td><td>物品统计名</td><td>等级</td><td>颜色</td><td>描述</td></tr>");
		while(iter.hasNext()){
			Article aa = iter.next();
			out.print("<tr class='color"+aa.getColorType()+"'><td><a href='ArticleByName.jsp?articleName="+aa.getName()+"'>"+aa.getName()+"</td><td><a href='ArticleByName.jsp?articleCNName="+aa.getName_stat()+"'>"+aa.getName_stat()+"</td><td>"+aa.getArticleLevel()+"</td><td>"+aa.getColorType()+"</td><td>"+aa.getDescription()+"</td>");
		}
	}
	
	out.print("</table>");
	%>
	</div>
</div>
</body>
</html>