<%@ page contentType="text/html;charset=utf-8"%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.newtask.Task"%>
<%@page import="com.fy.engineserver.util.CompoundReturn"%>
<%@page import="com.fy.engineserver.activity.ActivityIntroduce"%>
<%@page import="com.fy.engineserver.activity.ActivityManager"%>
<%@page import="com.fy.engineserver.cityfight.CityFightManager"%>
<%@page import="com.fy.engineserver.downcity.downcity3.BossCityManager"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.fy.engineserver.jiazu2.manager.JiazuManager2"%>
<%@page import="com.fy.engineserver.util.server.TestServerConfigManager"%>
<%@page import="com.fy.engineserver.core.JiazuSubSystem"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<META http-equiv=Content-Type content="text/html; charset=utf-8">
<META content="MSHTML 6.00.2900.3157" name=GENERATOR><style type="text/css"><style type="text/css">
</style>
</HEAD>
<%
if(TestServerConfigManager.isTestServer() == false){
	return;
}

JiazuSubSystem.openTimeLimit = false;
String starthour = request.getParameter("starthour");
String startminute = request.getParameter("startminute");
String playerNums = request.getParameter("playerNums");
String startminute2 = request.getParameter("startminute2");
String ll = request.getParameter("ll");
boolean openTimeLimi = JiazuSubSystem.openTimeLimit;

if(ll != null){
	openTimeLimi = ll.equals("true")?false:true;
	JiazuSubSystem.openTimeLimit = openTimeLimi;
	out.print(JiazuSubSystem.openTimeLimit?"<font color='red'>已经开启</font>":"<font color='red'>已经关闭</font>");
}


if(starthour!=null && startminute != null){
	String [] ss = starthour.split(",");
	String [] ss2 = startminute.split(",");
	int [] s1 = new int[2];
	int [] s2 = new int[2];
	for(int i=0;i<ss.length;i++){
		s1[i] = Integer.parseInt(ss[i]);
		s2[i] = Integer.parseInt(ss2[i]);
	}
	
	if(playerNums != null){
		BossCityManager.roomEnterLimit = Integer.parseInt(playerNums);
	}
	BossCityManager.getInstance().setStartHour(s1);
	BossCityManager.getInstance().setStartMinute(s2);
	out.print("设置成功小时:"+Arrays.toString(s1)+"<br>");
	out.print("设置成功分钟:"+Arrays.toString(s2)+"<br>");
	out.print("人数:"+playerNums+"<br>");
	out.print("是否已经通知:"+BossCityManager.getInstance().hasNoticeToday+"<br>");
}


if(startminute2 != null){
	Calendar cl = Calendar.getInstance();
	String [] ss = startminute2.split(",");
	int [] s1 = new int[2];
	for(int i=0;i<ss.length;i++){
		s1[i] = Integer.parseInt(ss[i]);
	}
	 int [] opweek1 = {cl.get(Calendar.DAY_OF_WEEK),cl.get(Calendar.DAY_OF_WEEK)};
	 int openHour = cl.get(Calendar.HOUR_OF_DAY);
	JiazuManager2.instance.opweek1 = opweek1;
	JiazuManager2.instance.openHour = openHour;
	JiazuManager2.instance.openTime = s1;
	out.print("周："+JiazuManager2.instance.opweek1[0] + "-"+JiazuManager2.instance.opweek1[1]+"<br>");
	out.print("小时："+JiazuManager2.instance.openHour+"<br>");
	out.print("分钟："+JiazuManager2.instance.openTime[0] + "-"+JiazuManager2.instance.openTime[1]+"<br>");
	

	int week = cl.get(Calendar.DAY_OF_WEEK);
	if(week == opweek1[0] || week == opweek1[1]){
		out.print("周满足<br>");
		int hour = cl.get(Calendar.HOUR_OF_DAY);
		if(hour == openHour){
			out.print("小时满足<br>");
			int minute = cl.get(Calendar.MINUTE);
			if(minute >= JiazuManager2.instance.openTime[0] && minute < JiazuManager2.instance.openTime[1]){
			out.print("分钟满足<br>");
			out.print("条件都满足，活动开启<br>");
			}
		}
	}
}
%>
<hr><hr>
<form>
<table>
<th>全民boss</th>
<tr><th>开始小时</th><td><input type='text' name='starthour'></td></tr>
<tr><th>开始分钟</th><td><input type='text' name='startminute'></td></tr>
<tr><th>人数</th><td><input type='text' name='playerNums'></td></tr>
<tr><td><input type="submit" name='提交'></td></tr>
</table>
</form>
<hr><hr>
<form>
<table>
<th>家族远征</th>
<tr><th>开始分钟</th><td><input type='text' name='startminute2'></td></tr>
<tr><td><input type="submit" name='提交'></td></tr>
</table>
</form>
<hr><hr>

<th>单人副本</th>
<form action="test.jsp" method="post">
	<input type="hidden" name="ll" value="<%=openTimeLimi %>" /> <input type="submit" value="<%=openTimeLimi?"关闭":"开启" %>" />
</form>
	