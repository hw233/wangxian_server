<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.activity.godDown.GodDwonManager"%>
<%@page import="java.util.Calendar"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>设置天神下凡的活动开始时间</title>
</head>
<body>

	<%
	String set = request.getParameter("set");
	if(set != null && !set.equals("")){
		
		GodDwonManager.getInstance().beginTime = System.currentTimeMillis();
		out.print("设置活动开始<br/>");
		
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int munite = cal.get(Calendar.MINUTE);
		
		GodDwonManager.指定开始时间[0][0] = hour;
		GodDwonManager.指定开始时间[0][1] = munite+1;
		
		if((munite +27) >60){
			GodDwonManager.指定结束时间[0][0] = hour+1;
			GodDwonManager.指定结束时间[0][1] = (munite+27-60);
		}else{
			GodDwonManager.指定结束时间[0][0] = hour;
			GodDwonManager.指定结束时间[0][1] = munite+27;
		}

		out.print("npc将会在2分钟后产生，持续4分钟<br/>");
		
		
		GodDwonManager.logger.warn("[后台开启活动]");
		return;
	}
	%>


	<form action="">
		设置天神下凡的开始时间:<input type="text" name="set"/ >
		<input type="submit" value="submit"> 
	</form>

</body>
</html>