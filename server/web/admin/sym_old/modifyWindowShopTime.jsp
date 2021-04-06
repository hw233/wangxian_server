<%@page
	import="com.fy.engineserver.platform.PlatformManager.Platform"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.engineserver.util.TimeTool"%>
<%@page
	import="com.fy.engineserver.menu.activity.Option_Get_Reward_Only_One_Time"%>
<%@page import="com.fy.engineserver.menu.Option"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="com.fy.engineserver.menu.Option_ExchangeArticle"%>
<%@page import="com.fy.engineserver.menu.MenuWindow"%>
<%@page import="com.fy.engineserver.menu.WindowManager"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	WindowManager wm = WindowManager.getInstance();
	MenuWindow[] windowMap = wm.getWindows();
	List<Option> addlist = new ArrayList<Option>();
	long now = System.currentTimeMillis();
	java.io.FileInputStream input = new java.io.FileInputStream(wm.getConfigFile());
	wm.loadExcel(input);
	input.close();
	out.print("[加载完成] [耗时：" + (System.currentTimeMillis() - now) + "ms]<br>");
	//修改平台
	for (MenuWindow menu : wm.getWindows()) {
		if (menu != null) {
			if (menu.getId() == 51031) {
				Option[] ops = menu.getOptions();
				for (Option o : ops) {
					if (o instanceof Option_Shop_Get_Display) {
						if (((Option_Shop_Get_Display) o).getShopName().equals("年獸拓拓兌換商店")) {
							((Option_Shop_Get_Display) o).setEndTimeStr("2014-02-24 23:59:59");
							((Option_Shop_Get_Display) o).init();
							addlist.add(o);
							out.print("[是否可见:" + (((Option_Shop_Get_Display) o).getStartTime() <= now && now <= ((Option_Shop_Get_Display) o).getEndTime()) + "]<br>");
						}
					} else {
						addlist.add(o);
					}
				}

				menu.setOptions(addlist.toArray(new Option[] {}));
				wm.getWindowMap().put(new Integer(51031), menu);
				out.print("[修改成功] [" + menu.getId() + "] [title:" + menu.getTitle() + "] [desc:" + menu.getDescriptionInUUB() + "] [菜单数：" + addlist.size() + "]<br>");
			}
		}
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.menu.Option_Shop_Get_Display"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改windows时间</title>
</head>
<body>

</body>
</html>