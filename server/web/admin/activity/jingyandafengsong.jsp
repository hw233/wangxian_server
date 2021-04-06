<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<
<%@page import="java.util.Calendar"%>
<%@page import="com.fy.engineserver.activity.ActivityManager"%>
<%@page import="com.fy.engineserver.gametime.SystemTime"%>
<%@page import="com.fy.engineserver.activity.fireActivity.FireManager"%>
<%@page import="com.fy.engineserver.activity.ActivityThread"%>
<%@page import="com.fy.engineserver.newBillboard.monitorLog.LogRecordManager"%>
<%@page import="java.util.Date"%><html>
	<head>
		<title>经验金钱在线大奉送 </title>
	</head>
	
	<body>
	
	
		<%
			String st = request.getParameter("id");
		
			if(st != null && !st.equals("")){
				
				Calendar cal = Calendar.getInstance();
				
				int year = cal.get(Calendar.YEAR);
				int month = cal.get(Calendar.MONTH);
				int day = cal.get(Calendar.DAY_OF_MONTH);
				
				int hour = cal.get(Calendar.HOUR_OF_DAY);
				int minute = cal.get(Calendar.MINUTE);
				
				//在线活动
				ActivityManager am = ActivityManager.getInstance();
				am.在线特殊日期[0] = year;
				am.在线特殊日期[1] = month;
				am.在线特殊日期[2] = day;
				
				//大奉送活动
				am.start_stopTime[0] = SystemTime.currentTimeMillis();
				
				//家族经验活动
				FireManager.家族经验特殊日期 [0] = year;
				FireManager.家族经验特殊日期 [1] = month;
				FireManager.家族经验特殊日期 [2] = day;
				
				//飞行坐骑活动
				String dateString = LogRecordManager.sdf.format(new Date(SystemTime.currentTimeMillis()+2*60*1000));
				out.print("设置在2分钟后打印活动日志"+dateString);
				
				ActivityThread at = ActivityThread.getInstance();
				at.dateString[0] = dateString;
				
				
				at.坐骑特殊日期[0] = year;
				at.坐骑特殊日期[1] = month;
				at.坐骑特殊日期[2] = day;
				
				at.指定日期[0] = at.坐骑特殊日期;
				int[][] ints = at.指定时间;
				
				int[][] intss = new int[ints.length+1][];
				
				if(minute+ 5 >= 59){
					minute = 59;
				}else{
					minute += 5;
				}
				int[] intt = new int[]{hour,minute};
				
				intss[0] = intt;
				for(int i=1;i<intss.length;i++){
					intss[i] = ints[i-1];
				}
				at.指定时间 = intss;
				at.init();
				
				out.print("设置成功,排行榜那个在5分钟后会有效");
				return;
			}
		%>
		
		<form action="">
			设置(在线活动，大奉送活动，家族经验活动，飞行坐骑活动):<input type="text" name="id" />
			<input type="submit" value="submit" />
		
		</form>
		
	</body>
</html>