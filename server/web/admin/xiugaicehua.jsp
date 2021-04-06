<%@ page language="java" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.shop.*" %>
<%@page import="java.lang.reflect.Field"%>
<%@page import="java.lang.reflect.Method"%>
<%
	ShopManager sm = ShopManager.getInstance();

Shop shop = sm.getShops().get("全部道具");
{
Goods goods = shop.getGoods("宝石枳香(5级)",3);
Class clazz = Class.forName("com.fy.engineserver.shop.Goods");
Field f = clazz.getDeclaredField("在时间限制下的数量限制");
f.setAccessible(true);
f.set(goods,true);
}
{
Goods goods = shop.getGoods("宝石幽橘(5级)",3);
Class clazz = Class.forName("com.fy.engineserver.shop.Goods");
Field f = clazz.getDeclaredField("在时间限制下的数量限制");
f.setAccessible(true);
f.set(goods,true);
}
out.println("刷新成功");
%>
<%@page import="com.xuanzhi.tools.text.StringUtil"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.Knapsack"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.Cell"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.EquipmentEntity"%>
<%@page import="com.fy.engineserver.mail.service.MailManager"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.equipments.Equipment"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<body>

</body>
</html>
