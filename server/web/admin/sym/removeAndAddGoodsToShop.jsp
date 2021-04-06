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
		/**
		*	要刷的商店名和商品名
		*/
        String shopName = "限时抢购";
        String gnames [] = {"精华羽化石","紫色玉液礼包(绑)"};
        
        Shop shop = ShopManager.getInstance().getShops().get(shopName);
        if (shop == null) {
             out.print("商店："+shopName + ",不存在");
             return;
        }
        
        List<Goods> relist = new ArrayList<Goods>();
        List<Goods> list = shop.getGoods();
        out.print("删掉前的数据长度:"+list.size()+"<br>");
		for(int i=0;i<list.size();i++){
			Goods g = list.get(i);
			for(String name:gnames){
				//防止重复刷页面
				if(name.equals(g.getArticleName())){
					if (g.getPrice() != 6000000) {
						relist.add(g);
						out.print("<br>删除已存在-" + name + "--GoodMaxNum:" + g.getGoodMaxNumLimit() + "--PersonSellNum:" + g.getPersonSellNumLimit() + "--OverNum:" + g.getOverNum() + "---" + TimeTool.formatter.varChar23.format(g.getFixTimeBeginLimit()) + "---" + TimeTool.formatter.varChar23.format(g.getFixTimeEndLimit()));
					}
				}
			}
		}
        list.removeAll(relist);
        out.print("删掉后的数据长度:"+list.size()+"<br>");
        Goods g1=new Goods(shop);
        g1.setArticleName("精华羽化石");
        g1.setArticleName_stat("精华羽化石");
        g1.setId(2222222);
        g1.setColor(3);
        g1.setBundleNum(1);
        g1.setPayType(1);
        g1.setOldPrice(2000000);
        g1.setPrice(2000000);
        g1.setLittleSellIcon("tuijian");
        g1.setTotalSellNumLimit(10);
        g1.setOverNum((int)g1.getTotalSellNumLimit());
        g1.在时间限制下的数量限制=true;
        g1.setTimeLimitType(1);
        g1.setEveryDayBeginLimit(0);
        g1.setEveryDayEndLimit(23);
        g1.setGoodMaxNumLimit(99);
        out.print("overnum1 :"+g1.getOverNum()+"<br>");
        shop.getGoods().add(g1);
        out.print("添加"+g1.getArticleName()+"数量:"+shop.getGoods().size()+"<br>");
        Goods g2=new Goods(shop);
        g2.setArticleName("紫色玉液礼包(绑)");
        g2.setArticleName_stat("紫色玉液礼包(绑)");
        g2.setId(2333333);
        g2.setColor(3);
        g2.setBundleNum(1);
        g2.setPayType(1);
        g2.setOldPrice(300000);
        g2.setPrice(300000);
        g2.setTotalSellNumLimit(10);
        g2.setOverNum((int)g2.getTotalSellNumLimit());
        g2.setLittleSellIcon("tuijian");
        g2.在时间限制下的数量限制=true;
        g2.setTimeLimitType(1);
        g2.setEveryDayBeginLimit(0);
        g2.setEveryDayEndLimit(23);
        g2.setGoodMaxNumLimit(99);
        shop.getGoods().add(g2);
        out.print("overnum2 :"+g2.getOverNum()+"<br>");
        out.print("添加"+g2.getArticleName()+"数量:"+shop.getGoods().size()+"<br>");
		out.print("<hr>");
		
		Calendar cal = Calendar.getInstance();
		Player player=PlayerManager.getInstance().getPlayer("aaa");
		shop.flushGoods(System.currentTimeMillis(), cal, g1, player);
		shop.flushGoods(System.currentTimeMillis(), cal, g2, player);
		
		
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.Calendar"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>添加and删除商店中商品</title>
</head>
<body>

</body>
</html>