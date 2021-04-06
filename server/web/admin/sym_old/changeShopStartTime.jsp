<%@page import="com.fy.engineserver.activity.ActivitySubSystem"%>
<%@page import="com.fy.engineserver.platform.PlatformManager"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="com.fy.engineserver.platform.PlatformManager.Platform"%>
<%@page import="com.fy.engineserver.util.TimeTool"%>
<%@page import="com.fy.engineserver.shop.Goods"%>
<%@page import="com.fy.engineserver.shop.Shop"%>
<%@page import="com.fy.engineserver.shop.ShopManager"%>
<%@page import="com.fy.engineserver.chat.ChatMessageService"%>
<%@page import="com.fy.engineserver.playerTitles.PlayerTitlesManager"%>
<%@page import="com.fy.engineserver.datasource.buff.BuffTemplate"%>
<%@page import="com.fy.engineserver.datasource.buff.Buff"%>
<%@page import="com.fy.engineserver.datasource.buff.BuffTemplateManager"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
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
		<meta http-equiv="Content-Type"
			content="text/html; charset=utf-8">
		<title>修改商店时间</title>
		<link rel="stylesheet" href="gm/style.css" />
		<script type="text/javascript">
	
</script>
	</head>
	<body>
		<br>
		<%
// 			String servernames [] = {"巍巍昆仑","群雄逐鹿","海纳百川","明空梵天","百花深谷","飘渺王城","傲视群雄","春暖花开","一方霸主","洪荒古殿","洪武大帝","程门立雪","上善若水","云游魂境","绿野仙踪","天下无双","半城烟沙","天上人间","盛世永安","天府之国","琴瑟和鸣","龙争虎斗","狂傲天下","天魔神谭","侠骨柔情","天涯海角","修罗转生","一代天骄","仙界至尊","金戈铁马","一战成名","千娇百媚","风雪之巅","菩提众生","奇门辉煌","飘渺仙道","仙山楼阁","一统江湖","笑傲江湖","前尘忆梦","月满西楼","策马江湖","龙隐幽谷","雄霸天下","豪杰聚义","九霄龙吟","再续前缘","九仙揽月","桃源仙境","似水流年","燃烧圣殿","金蛇圣殿","勇者无敌","普陀梵音","问鼎江湖","鱼跃龙门","龙翔凤舞","华夏战神","卧虎藏龙","西方灵山","千秋北斗","浩瀚乾坤","诸神梦境","破晓之穹","傲啸封仙","伏虎降龙"};
// 			String servernam = GameConstants.getInstance().getServerName();
// 			boolean ismodify = false;
// 			for(String name:servernames){
// 				if(name.equals(servernam)){
// 					ismodify = true;
// 				}
// 			}
			
			if(!PlatformManager.getInstance().isPlatformOf(Platform.官方)){
				out.print("<font color='red'>此服务器不在修改范围!!</font>");
				return;
			}
			
			String shopName = "全部道具";
			Shop shop = ShopManager.getInstance().getShops().get(shopName);
			if (shop == null) {
				out.print(shopName + ",不存在");
				return;
			}
			
// 			shop.setTimelimits("2014-01-06-00-00#2014-02-20-23-59");
// 			out.print("【"+GameConstants.getInstance().getServerName()+"】商店时间修改成功，");
			List<Goods> oldgs = shop.getGoods(); 
			for(int i=0;i<oldgs.size();i++){
				if("灵兽内丹".equals(oldgs.get(i).getArticleName())){
					if(oldgs.get(i).getFixTimeEndLimit()>0){
						out.print("灵兽内丹<font color='yellow'>修改之前</font>的结束时间是:"+TimeTool.formatter.varChar23.format(oldgs.get(i).getFixTimeEndLimit())+"<br>");
						oldgs.get(i).setFixTimeEndLimit(TimeTool.formatter.varChar19.parse("2014-03-31 23:59:59"));
						out.print("灵兽内丹 修改之后的结束时间是:"+TimeTool.formatter.varChar23.format(oldgs.get(i).getFixTimeEndLimit())+"<br><hr>");
						ActivitySubSystem.logger.warn("[后台修改全部道具灵兽内丹时间] [ok] [结束时间："+(TimeTool.formatter.varChar23.format(oldgs.get(i).getFixTimeEndLimit()))+"] ");
					}
				}
// 				else if("胡饼锦囊".equals(oldgs.get(i).getArticleName())){
// 					out.print("胡饼锦囊 <font color='yellow'>修改之前</font>的【开始】时间是:"+TimeTool.formatter.varChar23.format(oldgs.get(i).getFixTimeBeginLimit())+"<br>");
// 					out.print("胡饼锦囊 <font color='yellow'>修改之前</font>的结束时间是:"+TimeTool.formatter.varChar23.format(oldgs.get(i).getFixTimeEndLimit())+"<br>");
// 					oldgs.get(i).setFixTimeBeginLimit(TimeTool.formatter.varChar19.parse("2013-09-18 00:00:00"));
// 					oldgs.get(i).setFixTimeEndLimit(TimeTool.formatter.varChar19.parse("2013-09-18 23:59:59"));
// 					out.print("胡饼锦囊 修改之后的【开始】时间是:"+TimeTool.formatter.varChar23.format(oldgs.get(i).getFixTimeBeginLimit())+"<br>");
// 					out.print("胡饼锦囊 修改之后的结束时间是:"+TimeTool.formatter.varChar23.format(oldgs.get(i).getFixTimeEndLimit())+"<br>");
// 				}
			}
		%>

	</body>
</html>
