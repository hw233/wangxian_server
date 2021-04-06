<%@page import="com.fy.engineserver.menu.activity.Option_MergeServer_Config"%>
<%@page import="com.fy.engineserver.activity.base.TimesActivity"%>
<%@page import="java.util.Calendar"%>
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
	String op = "铁血武圣,瀚海缘阁,凝霜啸岭,千娇百媚,神舞凤凰,御龙在天,鸿蒙之始,武尊天下,众仙之门,群神荣耀,神龙北斗,碎玉盘龙,四面楚歌,猎明江湖,奇门辉煌,揽星水月,儒战天下,烽火情缘,西岭飘雪,月影豪情";
	TimesActivity temp2 = new TimesActivity("2014-12-12 00:00:00", "2014-12-13 23:59:59", "sqage", op, "");       
	temp2.setOtherVar(20001, 2, 2, 20);
	out.println("增加喝酒次数成功！<br>");
	
	WindowManager wm = WindowManager.getInstance();
	MenuWindow[] windowMap = wm.getWindows();
	List<Option> addlist = new ArrayList<Option>();
	for (MenuWindow menu : wm.getWindows()) {
		if (menu != null) {
			if (menu.getId() == 50007) {
				Option[] ops = menu.getOptions();
				for (Option o : ops) {
					addlist.add(o);
				}
			}
			Option_MergeServer_Config op2 = new Option_MergeServer_Config();
			op2.setText("领取合服前礼包");
			op2.setIconId("");
			op2.setColor(0xffffff);
			op2.setActivitykey("20141211合服前领取合服锦囊");
			op2.setIsOneTimeRewardstr("false");
			op2.setArticleName("合服锦囊");
			op2.init();
			addlist.add(op2);
			
			menu.setOptions(addlist.toArray(new Option[] {}));
			wm.getWindowMap().put(new Integer(50007), menu);
			out.println("[新增成功]");
			out.print("[修改成功] [" + menu.getId() + "] [title:" + menu.getTitle() + "] [desc:" + menu.getDescriptionInUUB() + "] [菜单数：" + addlist.size() + "]<br>");
			break;
		}
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改国服合服活动</title>
</head>
<body>

</body>
</html>