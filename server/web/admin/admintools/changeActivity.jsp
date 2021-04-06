<%@page
	import="com.fy.engineserver.activity.newChongZhiActivity.NewMoneyActivity"%>
<%@page
	import="com.fy.engineserver.activity.newChongZhiActivity.NewChongZhiActivityManager"%>
<%@page import="com.fy.engineserver.activity.ActivityIntroduce"%>
<%@page import="com.fy.engineserver.activity.ActivityManager"%>
<%@page import="java.util.*"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String time = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	List<Integer> ids = new ArrayList<Integer>();
	
	ids.add(8024);
	ids.add(8025);
	ids.add(8026);
	ids.add(8027);
	ids.add(8028);
	ids.add(8029);
	ids.add(8030);
	ids.add(8031);
	ids.add(8032);
	ids.add(8033);
	ids.add(8034);
	ids.add(8035);
	ids.add(8036);
	ids.add(8037);
	ids.add(8038);
	ids.add(8039);
	ids.add(8040);
	ids.add(8041);
	ids.add(8042);
	ids.add(8043);
	
	int num = 0;
	if ("2013-07-23".equals(time)) {
		for (NewMoneyActivity na : NewChongZhiActivityManager.instance.chongZhiActivitys) {
			if (ids.contains(na.getConfigID())) {
				na.setEndTime("2013-07-25 23:59:59");
				na.createLongTime();
				out.print("(" + (++num) + ")活动刷新完毕:" + na.getName() + ",结束时间:" + na.getEndTime() + "<BR/>");
			}
		}
		List<ActivityIntroduce> list = ActivityManager.getInstance().getActivityIntroduces();
		ActivityIntroduce activityIntroduce = null;
		for (ActivityIntroduce ai : list ) {
			if (ai.getId() == 1) {
				activityIntroduce = ai;
				break;
			}
		}
		if (activityIntroduce != null) {
			String des = activityIntroduce.getDes().replace("7月24日23:59", "25日维护");
			activityIntroduce.setDes(des);
			out.print("<BR/>");
			out.print(activityIntroduce.getDes());
		}
		
	} else {
		out.print("<H1>页面已失效</H1>");
		return;
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>2-13-07-21刷新活动</title>
</head>
<body>

</body>
</html>