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
        String shopName = "全部道具";
        String gnames [] = {"羽炼符"};
        int colors [] = {0};
        
        Shop shop = ShopManager.getInstance().getShops().get(shopName);
        if (shop == null) {
             out.print("商店："+shopName + ",不存在");
             return;
        }
        
        List<Goods> relist = new ArrayList<Goods>();
        List<Goods> list = shop.getGoods();
		for(int i=0;i<list.size();i++){
			Goods g = list.get(i);
			for(String name:gnames){
				//防止重复刷页面
				if(name.equals(g.getArticleName())){
					relist.add(g);
					out.print("<br>删除已存在-"+name+"--GoodMaxNum:"+g.getGoodMaxNumLimit()+"--PersonSellNum:"+ g.getPersonSellNumLimit()+"--OverNum:"+g.getOverNum()+ "---"+TimeTool.formatter.varChar23.format(g.getFixTimeBeginLimit())+"---"+TimeTool.formatter.varChar23.format(g.getFixTimeEndLimit()));
				}
			}
		}
        list.removeAll(relist);
        int baseID = 9999999;
		for(int i=0;i<gnames.length;i++){
			String name = gnames[i];
			Goods g = new Goods(shop);
			shop.getGoods().add(0,g);
			g.setId(baseID++);
			g.setArticleName(name);
			g.setColor(colors[i]);
			g.setBundleNum(1);
			g.setPayType(1);
			g.setOldPrice(65000);
			g.setPrice(65000);
			g.setLittleSellIcon("tuijian");
			g.setZhiwuLimit(0);
			g.setGongxunLimit(0);
			g.setJiazuGongxianLimit(0);
// 			g.setTimeLimitType(4);
			g.setGoodMaxNumLimit(99);
			g.setOverNum(-1);
// 			g.setFixTimeBeginLimit(TimeTool.formatter.varChar19.parse("2013-09-03 00:00:00"));
// 			g.setFixTimeEndLimit(TimeTool.formatter.varChar19.parse("2013-11-22 23:59:59"));
			out.print("[添加商品]--[商店："+shopName+"]--[商品："+name+"]---[开始时间："+TimeTool.formatter.varChar23.format(g.getFixTimeBeginLimit())+"]---[结束时间："+TimeTool.formatter.varChar23.format(g.getFixTimeEndLimit())+"]");
		}
		out.print("<hr>");
		
// 		WindowManager wm = WindowManager.getInstance();
// 		MenuWindow[] windowMap = wm.getWindows();
// 		for(MenuWindow menu : windowMap){
// 			if(menu!=null){
// 				if(menu.getId()==153){
// 					Option[] ops = menu.getOptions();
// 					for(Option o:ops){
// 						if(o instanceof Option_ExchangeSliver_Salary){
// 							Option_ExchangeSliver_Salary oe = (Option_ExchangeSliver_Salary)o;
// 							if(oe.getText().equals("50银子兑换工资")){
// 								long oldwage = oe.getWage();
// 								oe.setWage(70000);
// 								out.print("工资转化卡修改成功：[窗口功能:"+oe.getText()+"]--[结束时间:"+oe.getEndTimeStr()+"]--[开始时间:"+ oe.getStartTimeStr()+"]--[消耗物品:"+Arrays.toString(oe.getCostArticleNameArr()) +"]--[工资变化:"+oldwage +"-->"+oe.getWage() +"]<br>");
// 							}else if(oe.getText().equals("10银子兑换工资")){
// 								long oldwage = oe.getWage();
// 								oe.setWage(12000);
// 								out.print("工资转化卡修改成功：[窗口功能:"+oe.getText()+"]--[结束时间:"+oe.getEndTimeStr()+"]--[开始时间:"+ oe.getStartTimeStr()+"]--[消耗物品:"+Arrays.toString(oe.getCostArticleNameArr()) +"]--[工资变化:"+oldwage +"-->"+oe.getWage() +"]<br>");
// 							}
// 						}
// 					}
// 				}
// 			}
// 		}
		
// 		out.print("<hr>");
// 		ActivenessManager am = ActivenessManager.getInstance();
// 		am.setLotteryNames(new String[]{"玉液锦囊(绿色)","绿色屠魔帖锦囊","渡劫回魂丹"});
// 		am.setLotteryNamesFtr(new String[]{"玉液锦囊(绿色)","绿色屠魔帖锦囊","渡劫回魂丹"});
// 		am.setLotteryNums(new Integer[]{3,3,1});
// 		am.setLotteryProbs(new Integer[]{35,35,30});
// 		out.print("lotteryNames:"+Arrays.toString( am.getLotteryNames())+"-lotteryNamesFtr:"+Arrays.toString( am.getLotteryNamesFtr())+"--lotteryNums:"+Arrays.toString( am.getLotteryNums())+"--lotteryProbs:"+Arrays.toString( am.getLotteryProbs()));
		
		
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>添加商品to商店</title>
</head>
<body>

</body>
</html>