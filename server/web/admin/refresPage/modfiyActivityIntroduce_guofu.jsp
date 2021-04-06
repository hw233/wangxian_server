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
  	  				if(av.getName().equals("七月令旗兑换道具更新上线")){
  	  					av.setDes(av.getDes().replace("7月1日0:00——7月31日23:59", "8月8日维护后——8月30日23:59"));
  	  					out.print(av.getDes()+"<br>");
  	  					out.print("<font color='red'>注意：只刷新新服</font><br>");
  	  				}
//   	  				else if(av.getName().equals("诸天守望—天降密藏")){
//   	  					av.setDes(av.getDes().replace("河永安城", "和永安城"));
//   	  					av.setDes(av.getDes().replace("不同品质的[诸天秘钥]", "不同品质的[诸天密钥]"));
// 	  					out.print(av.getDes()+"<br>");
//   	  				}
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
