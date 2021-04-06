<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.activity.shop.ShopActivity"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.activity.shop.ShopActivityManager"%>
<%@page import="com.fy.engineserver.util.config.ServerFit4Activity"%>
<%@page import="com.fy.engineserver.util.config.ServerFit"%>
<%@page import="com.fy.engineserver.activity.shop.ActivityProp"%>
<%@page import="com.fy.engineserver.activity.shop.ShopActivityManager.RepayType"%>
<%@page import="com.fy.engineserver.activity.shop.ShopActivityOfBuyAndGive"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<head>
</head>
<body>
	<%
	ActivityProp needBuyProp = new ActivityProp("宝石竹清(2级)", 1, 1, false);
	ActivityProp giveProp = new ActivityProp("2级竹清5折劵", 3, 1, true);
	ServerFit serverfit = new ServerFit4Activity("sqage", "", "");
	ShopActivityOfBuyAndGive saob = new ShopActivityOfBuyAndGive("2013-11-26 00:00:00", "2013-12-20 23:59:59", getRepayType(0), "全部道具", 
			needBuyProp, giveProp, serverfit, "恭喜您获得年末狂欢购奖励", "请查收附件");
	
	
	ActivityProp needBuyProp2 = new ActivityProp("宝石竹清(3级)", 1, 1, false);
	ActivityProp giveProp2 = new ActivityProp("3级竹清5折劵", 3, 1, true);
	ServerFit serverfit2 = new ServerFit4Activity("sqage", "", "");
	ShopActivityOfBuyAndGive saob2 = new ShopActivityOfBuyAndGive("2013-11-26 00:00:00", "2013-12-20 23:59:59", getRepayType(0), "全部道具", 
			needBuyProp2, giveProp2, serverfit2, "恭喜您获得年末狂欢购奖励", "请查收附件");
	
	
	ActivityProp needBuyProp3 = new ActivityProp("宝石竹清(4级)", 2, 1, false);
	ActivityProp giveProp3 = new ActivityProp("4级竹清5折劵", 3, 1, true);
	ServerFit serverfit3 = new ServerFit4Activity("sqage", "", "");
	ShopActivityOfBuyAndGive saob3 = new ShopActivityOfBuyAndGive("2013-11-26 00:00:00", "2013-12-20 23:59:59", getRepayType(0), "全部道具", 
			needBuyProp3, giveProp3, serverfit3, "恭喜您获得年末狂欢购奖励", "请查收附件");
	List<ShopActivity> list = ShopActivityManager.getInstance().getShopActivities();
	int index = list.size();
	//list.remove(index - 1);
	//list.remove(index - 2);
	//list.remove(index - 3);
	list.add(saob);
	list.add(saob2);
	list.add(saob3);
	ShopActivityManager.getInstance().setShopActivities(list);
	out.println("刷页面成功!!!");
	%>
	<%!
	public RepayType getRepayType(int type) throws Exception{
		for(RepayType r : RepayType.values()) {
			if(r.type == type) {
				return r;
			}
		}
		throw new Exception("买送类型填写错误:" + type);
	}
	%>>
</body>
</html>
