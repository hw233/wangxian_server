<%@page import="com.fy.engineserver.platform.PlatformManager.Platform"%>
<%@page import="com.fy.engineserver.platform.PlatformManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.DiscountCard"%>
<%@page import="com.fy.engineserver.datasource.article.manager.DiscountCardAgent"%>
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
	if(!PlatformManager.getInstance().isPlatformOf(Platform.台湾)){
		out.print("无权操作");
		return;
	}

	List<DiscountCard> list = DiscountCardAgent.getInstance().getList();
	List<DiscountCard> adds = new ArrayList<DiscountCard>();
	boolean isadd = false;
	for(DiscountCard card : list){
		if(card.getShopName().equals("限時搶購")){
			isadd = true;
			break;
		}
		if(card.getName().equals("綠色煉星符半價券") || card.getName().equals("藍色煉星符半價券") || card.getName().equals("紫色煉星符半價券")){
			adds.add(card);	
		}
	}
	
	if(isadd){
		out.print("您已经点过了，别再点了");
		return;
	}

	for(DiscountCard card : adds){
		card.setShopName("限時搶購");
		out.print("[打折卡添加成功] [打折卡："+card.getName()+"] [打折的道具名："+card.getDiscountArticleName()+"] [打折的商店名:"+card.getShopName()+"]<br>");
	}
	list.addAll(adds);
	out.print("<hr>[添加成功] [总数："+list.size()+"] [addnums:"+adds.size()+"]");
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改商店打折卡</title>
</head>
<body>

</body>
</html>