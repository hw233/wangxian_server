<%@page import="com.fy.engineserver.util.TimeTool"%>
<%@page import="com.fy.engineserver.shop.Goods"%>
<%@page import="com.fy.engineserver.shop.Shop"%>
<%@page import="com.fy.engineserver.shop.ShopManager"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.fy.engineserver.datasource.article.concrete.*,com.google.gson.*,com.xuanzhi.tools.cache.*,com.fy.engineserver.sprite.concrete.*,com.fy.engineserver.sprite.*,java.util.*,java.lang.reflect.*,com.google.gson.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>寶石過期查看</title>
</head>
<body>
<%
	String shopName = "全部道具";
	Shop shop = ShopManager.getInstance().getShops().get(shopName);
	if (shop == null) {
		out.print(shopName + ",不存在");
		return;
	}
	String modifynames[]  = {"寶石竹清(2級)",
			"寶石枳香(2級)",
			"寶石幽橘(2級)",
			"寶石墨輪(2級)",
			"寶石湛天(2級)",
			"寶石炎焚(2級)",
			"寶石混沌(2級)",
			"寶石無相(2級)",
			"寶石魔淵(2級)",
			"雪山冰蠶絲",
			"鑒定符",
			"銘刻符",
			"煉星符",
			"靈獸內丹",
			"初級煉妖石",
			"萬里神識符",
			"紅玫瑰",
			"白玫瑰",
			"藍色妖姬",
			"軟糖",
			"棒棒糖",
			"巧克力",
			"魯班令",
			"高級魯班令",
			"田地令",
			"香火",
			"免罪金牌",
			"棉花糖"
};
	
	List<Goods> oldgs = shop.getGoods();
	int count = 0;
	for(int i=0;i<oldgs.size();i++){
		for(String name :modifynames){
			if(oldgs.get(i).getArticleName().equals(name)){
				Goods g = oldgs.get(i);	
				if(g.getFixTimeBeginLimit()>0){
					count++;
					out.print("《修改前》--物品："+g.getArticleName()+"--時間："+TimeTool.formatter.varChar23.format(g.getFixTimeBeginLimit())+"<br>");
					g.setFixTimeBeginLimit(TimeTool.formatter.varChar19.parse("2013-6-22 00:00:00"));
					out.print("<font color='red'>《修改後》</font>--物品："+g.getArticleName()+"--時間："+TimeTool.formatter.varChar23.format(g.getFixTimeBeginLimit())+"<br>");
				}
			}
		}
	}
	out.print("修改完畢，數量："+count);
	
	
	
	
	
	
	
	
	
	
	
// 	for(int i=0;i<oldgs.size();i++){
// 		if(oldgs.get(i).getArticleName().equals(name) && oldgs.get(i).getId()==38){
// 			oldgs.remove(i);
// 			Goods g = new Goods(shop);
// 			g.setId(100);
// 			g.setArticleName(name);
// 			g.setColor(1);
// 			g.setBundleNum(1);
// 			g.setPayType(1);
// 			g.setOldPrice(125000);
// 			g.setPrice(125000);
// 			g.setZhiwuLimit(0);
// 			g.setGongxunLimit(0);
// 			g.setJiazuGongxianLimit(0);
// 			g.setTotalSellNum(0);
// 			g.setPersonSellNumLimit(0);
// 			oldgs.add(0,g);	
			
// 			out.print("OK.....");
// 		}
// 	}
	

// 	Field field = shop.getClass().getField("goods");
// 	field.setAccessible(true);
// 	List<Goods> list = (List<Goods>)field.get(shop);
	
%>
