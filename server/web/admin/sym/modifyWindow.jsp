<%@page import="com.fy.engineserver.menu.activity.exchange.Option_Exchange_Activity"%>
<%@page import="com.fy.engineserver.menu.Option"%>
<%@page import="com.fy.engineserver.menu.MenuWindow"%>
<%@page import="com.fy.engineserver.menu.WindowManager"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
</head>
<body>
<%
WindowManager wm = WindowManager.getInstance();
for(MenuWindow menu : wm.getWindows()){
	if(menu!=null){
		if(menu.getId()==51165){
			Option[] ops = menu.getOptions();
			for(Option o:ops){
				if(o.getText().equals("五级竹清鸿运包兑换")){
					o.setText("竹清宝石鸿运包兑换");
					Option_Exchange_Activity opt = (Option_Exchange_Activity)o;
					opt.setActivityName("竹清宝石鸿运包兑换");
					out.print(o.getText()+"<br>");
				}
			}
			
			menu.setOptions(ops);
			wm.getWindowMap().put(new Integer(51165), menu);
			out.print("[修改成功] ["+menu.getId()+"] [title:"+menu.getTitle()+"] [desc:"+menu.getDescriptionInUUB()+"]<br>");
		}
	}
}
%>
</body>
</html>