<%@page import="com.fy.engineserver.carbon.CarbonManager"%>
<%@page import="com.fy.engineserver.carbon.devilSquare.activity.ExtraDevilOpenTimesActivity"%>
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
	CarbonManager.getInst().init();

    AllActivityManager ac = AllActivityManager.instance;
	ExtraDevilOpenTimesActivity ea = new ExtraDevilOpenTimesActivity("2014-03-28 00:00:00","2014-04-30 23:59:59","sqage","","");
	ea.setOtherVar(new int[]{2,15});
	List<BaseActivityInstance> list = new ArrayList<BaseActivityInstance>();
	list.add(ea);
	ac.allActivityMap.put(AllActivityManager.addDevilOpenTimes, list);
	out.println("恶魔刷新成功！！<br>");
	
	
	 String shopName = "限时抢购";
     Shop shop = ShopManager.getInstance().getShops().get(shopName);
     if (shop == null) {
          out.print("商店："+shopName + ",不存在");
          return;
     }
     
     shop.setTimelimits("2014-01-06-00-00#2114-01-25-23-59");
     
     List<Goods> gs =  shop.getGoods();
     for(Goods g:gs){
     	if(g.getArticleName().equals("典藏百年仙酿锦囊")||g.getArticleName().equals("醇香十年陈酿锦囊")||g.getArticleName().equals("万人屠锦囊")||g.getArticleName().equals("千人斩锦囊")){
     		g.setFixTimeEndLimit(TimeTool.formatter.varChar19.parse("2113-09-17 23:59:59"));
     	}
     }
     
		out.print("商店修改成功");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修正活动</title>
</head>
<body>

</body>
</html>