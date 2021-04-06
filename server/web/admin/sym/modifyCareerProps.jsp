<%@page import="com.fy.engineserver.datasource.article.data.props.CareerPackageProps"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.ArticleProperty"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.PackageProps"%>
<%@page import="com.fy.engineserver.activity.loginActivity.ActivityManagers"%>
<%@page import="com.fy.engineserver.activity.ActivityShowInfo"%>
<%@page import="java.util.Hashtable"%>
<%@page import="java.util.Map"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.activity.shop.ActivityProp"%>
<%@page import="com.fy.engineserver.activity.taskDeliver.TaskDeliverAct"%>
<%@page import="com.fy.engineserver.activity.BaseActivityInstance"%>
<%@page import="com.fy.engineserver.activity.AllActivityManager"%>
<%@page import="com.fy.engineserver.activity.activeness.ActivenessManager"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.fy.engineserver.menu.Option_ExchangeSliver_Salary"%>
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
	String[] wrong = new String[]{"天枢兽血骨盔","天枢兽血皮铠","天枢兽血皮肩","天枢兽血骨腕","天枢兽血长靴","天枢兽血腰带","天枢兽血晶石","天枢兽血兽魂","天枢兽血骨勋"};
	String[] right = new String[]{"天璇兽血骨盔","天璇兽血皮铠","天璇兽血皮肩","天璇兽血骨腕","天璇兽血长靴","天璇兽血腰带","天璇兽血晶石","天璇兽血兽魂","天璇兽血骨勋"};
	
	String wrongWeapon = "天枢怨灵之镰";
	String rightWeapon = "天璇破天权杖";

	Map<String, Article> articleNameMaps = new Hashtable<String, Article>();
	Map<String, Article> articleNameCnnMaps = new Hashtable<String, Article>();
	for (Article a : ArticleManager.getInstance().getAllArticles()) {
		if (a.getName().equals("斗神装备袋")) {
			CareerPackageProps pp = ((CareerPackageProps)a);
			for (ArticleProperty[] aps : pp.getArticleNames()) {
				for (ArticleProperty ap : aps) {
					for (int i=0; i<wrong.length; i++) {
						if (ap.getArticleName().equals(wrong[i])) {
							ap.setArticleName(right[i]);
							ap.setArticleName_stat(right[i]);
							out.println("[" + a.getName() + "] [" + wrong[i] + " = " + ap.getArticleName() + "]<br>");
						}
					}
				}
			}
		}
		if (a.getName().equals("白金武器锦囊")) {
			CareerPackageProps pp = ((CareerPackageProps)a);
			for (ArticleProperty[] aps : pp.getArticleNames()) {
				for (ArticleProperty ap : aps) {
					if (ap.getArticleName().equals(wrongWeapon)) {
						ap.setArticleName(rightWeapon);
						ap.setArticleName_stat(rightWeapon);
						out.println("[" + a.getName() + "] [" + wrongWeapon + " = " + rightWeapon + "]<br>");
					}
				}
			}
		}
		articleNameMaps.put(a.getName(), a);
		articleNameCnnMaps.put(a.getName_stat(), a);
	}
	
	Class<?> c = ArticleManager.class;
	Field f_allArticleNameMap = c.getDeclaredField("allArticleNameMap");
	f_allArticleNameMap.setAccessible(true);
	Field f_allArticleCNNameMap = c.getDeclaredField("allArticleCNNameMap");
	f_allArticleCNNameMap.setAccessible(true);
	f_allArticleNameMap.set(ArticleManager.getInstance(), articleNameMaps);
	f_allArticleCNNameMap.set(ArticleManager.getInstance(), articleNameCnnMaps); 
	
	
	
	out.println("ok!!");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修正白格</title>
</head>
<body>

</body>
</html>