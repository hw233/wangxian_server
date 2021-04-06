<%@page import="com.fy.engineserver.util.TimeTool"%>
<%@page import="com.fy.engineserver.platform.PlatformManager.Platform"%>
<%@page import="com.fy.engineserver.platform.PlatformManager"%>
<%@page import="com.fy.engineserver.activity.ActivityIntroduce"%>
<%@page import="com.fy.engineserver.activity.ActivityManager"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>错误页面</title>
  </head>
  <body>
  	<p>
  		<% 
	  		ActivityManager am = ActivityManager.getInstance();
  			if(PlatformManager.getInstance().isPlatformOf(Platform.官方)){
  				List<ActivityIntroduce> list = am.getActivityIntroduces();
  	  			for(ActivityIntroduce av:list){
  	  				if(av.getName().equals("金秋风暴——墨轮湛天第二件半价")){
  	  					av.setTotalStart(TimeTool.formatter.varChar19.parse("2013-10-20 00:00:00"));
  	  					out.print("修改成功墨轮湛天"+TimeTool.formatter.varChar23.format(av.getTotalStart())+"<br>");
  	  				}else if(av.getName().equals("金秋风暴——竹清第二件半价")){
  	  					av.setTotalStart(TimeTool.formatter.varChar19.parse("2013-10-22 00:00:00"));
	  					out.print("修改成功竹清"+TimeTool.formatter.varChar23.format(av.getTotalStart())+"<br>");
  	  				}
//   	  				else if(av.getName().equals("絕美九尾雪狐清涼登場")){
//   	  					av.setTotalStart(TimeTool.formatter.varChar19.parse("2013-06-11 00:00:00"));
// 	  					out.print("活動<絕美九尾雪狐清涼登場>開始時間修改OK"+"<br>");
//   	  				}else if(av.getName().equals("集“端午勳章”月末驚喜")){
//   	  					av.setTotalStart(TimeTool.formatter.varChar19.parse("2013-06-11 00:00:00"));
// 	  					out.print("活動<集“端午勳章”月末驚喜>開始時間修改OK"+"<br>");
//   	  				}else if(av.getName().equals("回归飘渺寻仙曲特賣會")){
//   	  					av.setTotalStart(TimeTool.formatter.varChar19.parse("2013-06-14 00:00:00"));
// 	  					out.print("活動<回归飘渺寻仙曲特賣會>開始時間修改OK"+"<br>");
//   	  				}
  	  			}
  			}
  		%>
  	</p>
  </body>
</html>
