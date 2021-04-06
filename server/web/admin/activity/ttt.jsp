<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.activity.everylogin.LoginActivityManager"%>
<%@page import="com.fy.engineserver.activity.everylogin.LoginActivity"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<% 
	Player p = GamePlayerManager.getInstance().getPlayer("容易莫摧残");
	for(LoginActivity la: LoginActivityManager.getInstance().getActivityMap().values()){
		if (la.getName().equals("台湾活动--0416")) {
			List<String> list = LoginActivityManager.getInstance().getLoginRecord(la,p);
			list.clear();
			list.add("2013-04-15");
			
			HashMap<Long, List<String>> playerRecord = (HashMap<Long, List<String>>) LoginActivityManager.getInstance().ddc.get(la.getName());
			for (String old :playerRecord.get(p.getId())) {
				out.print("old:" + old + "<BR/>");
			}
			playerRecord.put(p.getId(),(list));
			LoginActivityManager.getInstance().ddc.put(la.getName(), playerRecord);

			List<String> aaa = LoginActivityManager.getInstance().getLoginRecord(la,p);
			for (String old :playerRecord.get(p.getId())) {
				out.print("new:" + old + "<BR/>");
			}
			out.print("OK");
			break;
		}
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>