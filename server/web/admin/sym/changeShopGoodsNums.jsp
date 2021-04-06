<%@page import="com.fy.engineserver.activity.ActivitySubSystem"%>
<%@page import="com.fy.engineserver.platform.PlatformManager"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page
	import="com.fy.engineserver.platform.PlatformManager.Platform"
%>
<%@page import="com.fy.engineserver.util.TimeTool"%>
<%@page import="com.fy.engineserver.shop.Goods"%>
<%@page import="com.fy.engineserver.shop.Shop"%>
<%@page import="com.fy.engineserver.shop.ShopManager"%>
<%@page import="com.fy.engineserver.chat.ChatMessageService"%>
<%@page
	import="com.fy.engineserver.playerTitles.PlayerTitlesManager"
%>
<%@page import="com.fy.engineserver.datasource.buff.BuffTemplate"%>
<%@page import="com.fy.engineserver.datasource.buff.Buff"%>
<%@page
	import="com.fy.engineserver.datasource.buff.BuffTemplateManager"
%>
<%@ page
	language="java"
	contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"
%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.xuanzhi.tools.text.StringUtil"%>
<%@page import="com.fy.engineserver.stat.StatData"%>
<%@page import="com.xuanzhi.tools.text.DateUtil"%>
<%@page import="org.apache.log4j.Logger"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta
	http-equiv="Content-Type"
	content="text/html; charset=utf-8"
>
<title>修改商店商品兑换数量</title>
<link
	rel="stylesheet"
	href="gm/style.css"
/>
<script type="text/javascript">
	
</script>
</head>
<body>
	<br>
	<%
		if (!PlatformManager.getInstance().isPlatformOf(Platform.官方)) {
			out.print("<font color='red'>此服务器不在修改范围!!</font>");
			return;
		}
		String names[] = { "后天乾坤界", "先天阴阳界", "太极混元界", "无极混沌界" };
		String anames [] = {"鬼头令","鬼头令(元神)","卷轴","卷轴(元神)","小精灵","小精灵(元神)","玲珑塔","玲珑塔(元神)"};
		for (String name : names) {
			Shop shop = ShopManager.getInstance().getShops().get(name);
			if (shop == null) {
				out.print(name + ",不存在<br>");
				continue;
			}
			out.print("========================="+name+"=======================<br>");
			List<Goods> oldgs = shop.getGoods();
			Iterator<Goods> it = oldgs.iterator();
			while(it.hasNext()){
				Goods g = it.next();
				if(Arrays.asList(anames).contains(g.getArticleName())){
					it.remove();
					out.print("干掉的物品："+g.getArticleName()+"--颜色："+g.getColor()+"<br>");
				}
			}
		}
		
	%>
</body>
</html>
