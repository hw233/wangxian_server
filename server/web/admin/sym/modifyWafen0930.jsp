<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.activity.wafen.manager.WaFenManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>
<%
//紫色魂石礼包(开出魂石为绑定)
//紫色坐骑魂石锦囊
String s = "昆天圣域，开启时间：9月30日00:00——10月30日23:59 共计200次寻宝机会，必产出“剑奴惊喜锦囊（逆天技能带给你不一样的飘渺寻仙曲）”、“羽化5级保底符*3(不怕装备掉级啦)”、“紫色坐骑魂石锦囊” 挖取每次消耗国庆金铲*10（挖取物品非绑定）或国庆银铲*10(挖取物品绑定) 可以挖到各级宝石，各色深渊器灵、天璇丸、天权丹、大额积分卡、羽化石、紫色法宝、超级天赋书等物品！ 全部挖取昆天圣域地图宝物后，可进入终极地图———琉璃幻境，抢挖全服限量宝物(注：琉璃幻境宝物为全服共有，数量有限手快有，手慢没有)！";
WaFenManager.instance.fenmuMap.get("昆天圣域").setHelpInfo(s);
out.println(WaFenManager.instance.fenmuMap.get("昆天圣域").getHelpInfo());
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