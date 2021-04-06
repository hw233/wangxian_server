<%@page import="java.util.Date"%>
<%@page import="com.fy.engineserver.menu.activity.Option_Get_Reward_Only_One_Time"%>
<%@page import="com.fy.engineserver.menu.Option"%>
<%@page import="com.fy.engineserver.menu.MenuWindow"%>
<%@page import="com.fy.engineserver.menu.WindowManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
WindowManager wm = WindowManager.getInstance();
for(MenuWindow menu : wm.getWindows()){
	if(menu!=null){
		if(menu.getId()==30000){
			Option[] ops = menu.getOptions();
			for(Option o:ops){
				if(o.getText().equals("更端庆贺大礼")){
					if(o instanceof Option_Get_Reward_Only_One_Time){
						Option_Get_Reward_Only_One_Time op = (Option_Get_Reward_Only_One_Time)o;
						op.setEndTimeStr("2016-09-24 23:59:59");
						Date date = new Date("2016-09-24 23:59:59");
						op.setEndTime(date.getTime());
						out.print("设置完成<br>");
					}
				}
			}
			
			menu.setOptions(ops);
			wm.getWindowMap().put(new Integer(30000), menu);
			out.print("[修改成功] ["+menu.getId()+"] [title:"+menu.getTitle()+"] [desc:"+menu.getDescriptionInUUB()+"] [optionNum:"+ops.length+"]<br>");
		}
	}
}
%>
</body>
</html>