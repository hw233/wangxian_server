<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.fy.engineserver.shop.Goods"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.shop.Shop"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="com.fy.engineserver.shop.ShopManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	List<Gd> gds = new ArrayList<Gd>();
	gds.add(new Gd("红玫瑰", 1, 500));
	gds.add(new Gd("红玫瑰", 9, 4500));
	gds.add(new Gd("红玫瑰", 99, 49500));
	gds.add(new Gd("白玫瑰", 1, 5000));
	gds.add(new Gd("白玫瑰", 9, 45000));
	gds.add(new Gd("白玫瑰", 99, 495000));
	gds.add(new Gd("蓝色妖姬", 1, 10000));
	gds.add(new Gd("蓝色妖姬", 9, 90000));
	gds.add(new Gd("蓝色妖姬", 99, 990000));
	gds.add(new Gd("软糖", 1, 500));
	gds.add(new Gd("软糖", 9, 4500));
	gds.add(new Gd("软糖", 99, 49500));
	gds.add(new Gd("棒棒糖", 1, 5000));
	gds.add(new Gd("棒棒糖", 9, 45000));
	gds.add(new Gd("棒棒糖", 99, 495000));
	gds.add(new Gd("巧克力", 1, 10000));
	gds.add(new Gd("巧克力", 9, 90000));
	gds.add(new Gd("巧克力", 99, 990000));
	LinkedHashMap<String, Shop> shops = ShopManager.getInstance().getShops();
	List<Shop> changeshops = new ArrayList<Shop>();
	for (Shop temp : shops.values()) {
		if (temp.getName().equals("全部道具") || temp.getName().equals("我想传情达意")) {
			changeshops.add(temp);
		}
	}
	if (changeshops.size() == 0) {
		out.print("商店不存在");
		return;
	}
	out.print("商店个数:" + changeshops.size() + "<BR/>");
	for (Shop shop : changeshops) {
		List<Goods> gs = shop.getGoods();
		for (Goods gg : gs) {
			Gd gd = null;
			A: for (Gd gdTemp : gds) {
				if (gdTemp.articleName.equals(gg.getArticleName())) {
					if (gdTemp.num == gg.getBundleNum()) {
						gd = gdTemp;
						break A;
					}
				}
			}
			if (gd != null) {
				int oldPrice = gg.getPrice();
				gg.setPrice(gd.prize);
				gg.setOldPrice(gd.prize);
				boolean change = oldPrice != gg.getPrice();
				out.print("<font color='" + (change ? "red" : "") + "'>[设置价格完毕] [" + shop.getName() + "] [" + gg.getArticleName() + "] [一组个数:" + gg.getBundleNum() + "] [" + oldPrice + "--->" + gg.getPrice() + "]</font> <br/>");
			}
		}
	}
%>
<%!class Gd {
		public String articleName;
		public int num;
		public int prize;

		public Gd(String articleName, int num, int prize) {
			this.articleName = articleName;
			this.num = num;
			this.prize = prize;
		}
	}%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>