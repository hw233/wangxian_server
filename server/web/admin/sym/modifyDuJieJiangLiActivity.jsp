<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.menu.activity.exchange.ExchangeActivityManager"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.menu.activity.exchange.DuJieActivity"%>
<%@page import="com.fy.engineserver.menu.WindowManager"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="com.fy.engineserver.menu.MenuWindow"%>
<%@page import="com.fy.engineserver.menu.Option"%>
<%@page import="com.fy.engineserver.menu.activity.exchange.Option_DuJie_Activity"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

<%
	ExchangeActivityManager eam=ExchangeActivityManager.getInstance();
	List<DuJieActivity> dujieActivitys=eam.dujieActivitys;
	for(DuJieActivity da:dujieActivitys){
			Class c = DuJieActivity.class;
			Field f = c.getDeclaredField("activityName");
			f.setAccessible(true);
			f.set(da,"渡劫奖励20150925");
			out.print("刷活动key："+f.get(da)+"<br>");
	}
	WindowManager wm=WindowManager.getInstance();
	MenuWindow wd = wm.getWindowById(55566);	
	Option[] ops = wd.getOptions();
	for(Option op:ops){
			if(op.getText().contains("渡劫奖励")){
				Option_DuJie_Activity oda=(Option_DuJie_Activity)op;
				oda.setActivityName("渡劫奖励20150925");
				out.print("Window表刷新渡劫奖励活动名称<br>");
			}
	}
%>

</body>
</html>