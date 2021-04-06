<%@page import="com.fy.engineserver.datasource.article.data.magicweapon.MagicWeapon"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="java.util.Calendar"%>
<%@page
	import="com.fy.engineserver.activity.activeness.ActivenessManager"%>
<%@page import="java.util.Arrays"%>
<%@page
	import="com.fy.engineserver.menu.Option_ExchangeSliver_Salary"%>
<%@page import="com.fy.engineserver.menu.Option"%>
<%@page import="com.fy.engineserver.menu.MenuWindow"%>
<%@page import="com.fy.engineserver.menu.WindowManager"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.engineserver.util.TimeTool"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.shop.Goods"%>
<%@page import="com.fy.engineserver.shop.Shop"%>
<%@page import="com.fy.engineserver.shop.ShopManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String shopName = "五周年福利商店";
	Shop shop = ShopManager.getInstance().getShops().get(shopName);
	if (shop == null) {
		out.print("商店：" + shopName + ",不存在");
		return;
	}

	List<Goods> gs = shop.getGoods();
	ArticleManager am = ArticleManager.getInstance();
	Article[] articles = am.getAllArticles();
	for(Article a:articles){
		if(a instanceof MagicWeapon && a.getName().equals("5周年纪念法宝")){
			((MagicWeapon)a).setObtainBind(true);
			am.getArticle("5周年纪念法宝").setBindStyle((byte)1);
			am.getArticleByCNname("5周年纪念法宝").setBindStyle((byte)1);
			out.print("ok");
		}
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改限时抢购商店物品时间</title>
</head>
<body>

</body>
</html>