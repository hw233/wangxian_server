<%@page import="com.fy.engineserver.platform.PlatformManager.Platform"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.engineserver.util.TimeTool"%>
<%@page import="com.fy.engineserver.menu.activity.Option_Get_Reward_Only_One_Time"%>
<%@page import="com.fy.engineserver.menu.Option"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="com.fy.engineserver.menu.Option_ExchangeArticle"%>
<%@page import="com.fy.engineserver.menu.MenuWindow"%>
<%@page import="com.fy.engineserver.menu.WindowManager"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	WindowManager wm = WindowManager.getInstance();
	MenuWindow[] windowMap = wm.getWindows();
	List<Option> addlist = new ArrayList<Option>();
//桃源仙境
	long now = System.currentTimeMillis();
	java.io.FileInputStream input = new java.io.FileInputStream("/home/game/resin_server/webapps/game_server/WEB-INF/game_init_config/windows.xls");
	wm.loadExcel(input);
	input.close();
	out.print("[加载完成] [耗时："+(System.currentTimeMillis()-now)+"ms]<br>");
//修改平台
	for(MenuWindow menu : wm.getWindows()){
		if(menu!=null){
			if(menu.getId()==153){
				Option[] ops = menu.getOptions();
				for(Option o:ops){
					if(!o.getText().equals("同庆九游四周年") && !o.getText().equals("飘渺寻仙曲感谢您")){
						addlist.add(o);
					}
				}
				
				menu.setOptions(addlist.toArray(new Option[]{}));
				wm.getWindowMap().put(new Integer(153), menu);
				out.print("[修改成功] ["+menu.getId()+"] [title:"+menu.getTitle()+"] [desc:"+menu.getDescriptionInUUB()+"] [菜单数："+addlist.size()+"]<br>");
			}
		}
	}
	
//删除没用的
// 	for(MenuWindow menu : windowMap){
// 		if(menu!=null){
// 			if(menu.getId()==153){
// 				Option[] ops = menu.getOptions();
// 				out.print("删除成功"+ops.length+"-->"+ addlist.size()+"<br>");
// 				menu.setOptions(addlist.toArray(new Option[]{}));
// 				wm.getWindowMap().put(new Integer(153), menu);
// 			}
// 		}
// 	}
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改windows时间</title>
</head>
<body>

</body>
</html>