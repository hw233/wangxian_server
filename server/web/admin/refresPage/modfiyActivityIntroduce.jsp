<%@page import="com.fy.engineserver.notice.NoticeManager"%>
<%@page import="com.fy.engineserver.notice.NoticeForever"%>
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
// 	  		ActivityManager am = ActivityManager.getInstance();
//   			if(PlatformManager.getInstance().isPlatformOf(Platform.台湾)){
//   				List<ActivityIntroduce> list = am.getActivityIntroduces();
//   	  			for(ActivityIntroduce av:list){
//   	  				if(av.getName().equals("보주 교환상점 이벤트")){
//   	  					av.setDes("【이벤트 이름】：보주 교환상점 오픈!\n【이벤트 시간】：8월14일 점검 후 -8월20일\n【이벤트 범위】：전 서버\n【참가레벨】：1레벨 이상\n"
//   	  					+"【이벤트 소개】：표묘왕성에 보주 교환 상점이 새롭게 오픈 됩니다. 수행자님들은 온라인 시간에 따라 보주를 획득하실 수 있고,\n 보주는 상점에서 다양한 아이템으로 교환이 가능합니다. 상점 오픈 기념으로 이벤트 기간 동안에는 레벨업, 은화 충전방식으로도 보주를 획득할 수 있습니다만, 이벤트 기간 종료 후에는 온라인 시간으로만 보주 획득이 가능해진다는 점 명심해 주세요!"
//   	  					+"【이벤트 내용】\n"
//   	  					+"이벤트 기간 동안 수행자 님들은 레벨업,온라인 시간, 충전 등 방식으로 보주를 획득할 수 있고 일정 수량의 보주를 모으면 왕성 보주 교환상인으로 부터 여러가지 아이템을 교환할 수 있습니다.\n"

//   	  					+"10레벨 단위로 레벨업 시 보주 5개씩 획득\n연속 온라인 시간 1시간마다 보주 1개씩 획득\n125냥 충전 시 보주 1개 획득\n375냥 충전 시 보주 6개 획득\n"
//   	  					+"625냥 충전 시 보주 15개 획득\n1250냥 충전 시 보주 40개 획득\n2500냥 충전 시 보주 100개 획득\n3750냥 충전 시 보주 180개 획득\n6250냥 충전 시 보주 350개 획득"
//   	  					+"10000냥 충전 시 보주 640개 획득\n12500냥 충전 시 보주 900개 획득");
//   	  					out.print(av.getDes()+"<br>");
//   	  				}
// //   	  				else if(av.getName().equals("絕美九尾雪狐清涼登場")){
// //   	  					av.setTotalStart(TimeTool.formatter.varChar19.parse("2013-06-11 00:00:00"));
// // 	  					out.print("活動<絕美九尾雪狐清涼登場>開始時間修改OK"+"<br>");
// //   	  				}else if(av.getName().equals("集“端午勳章”月末驚喜")){
// //   	  					av.setTotalStart(TimeTool.formatter.varChar19.parse("2013-06-11 00:00:00"));
// // 	  					out.print("活動<集“端午勳章”月末驚喜>開始時間修改OK"+"<br>");
// //   	  				}else if(av.getName().equals("回归飘渺寻仙曲特賣會")){
// //   	  					av.setTotalStart(TimeTool.formatter.varChar19.parse("2013-06-14 00:00:00"));
// // 	  					out.print("活動<回归飘渺寻仙曲特賣會>開始時間修改OK"+"<br>");
// //   	  				}
//   	  			}
//   			}
  			
  			
  			Map<String, List<NoticeForever>> foreverNotices = NoticeManager.getInstance().getForeverNotices();
  			List<NoticeForever> activitys = foreverNotices.get("이벤트");
  			out.print("activitys:"+activitys.size()+"<br>");
  			for(NoticeForever notice:activitys){
  				if(notice.getTitlename().equals("보주 교환상점 이벤트")){
  					out.print("修改前内容："+notice.getNoticeContent()+"<br>");
  					notice.setNoticeContent("【이벤트 이름】：보주 교환상점 오픈!\n【이벤트 시간】：8월14일 점검 후 -8월20일\n【이벤트 범위】：전 서버\n【참가레벨】：1레벨 이상\n"
  	  					+"【이벤트 소개】：표묘왕성에 보주 교환 상점이 새롭게 오픈 됩니다. 수행자님들은 온라인 시간에 따라 보주를 획득하실 수 있고,\n 보주는 상점에서 다양한 아이템으로 교환이 가능합니다. 상점 오픈 기념으로 이벤트 기간 동안에는 레벨업, 은화 충전방식으로도 보주를 획득할 수 있습니다만, 이벤트 기간 종료 후에는 온라인 시간으로만 보주 획득이 가능해진다는 점 명심해 주세요!\n"
  	  					+"【이벤트 내용】\n"
  	  					+"이벤트 기간 동안 수행자 님들은 레벨업,온라인 시간, 충전 등 방식으로 보주를 획득할 수 있고 일정 수량의 보주를 모으면 왕성 보주 교환상인으로 부터 여러가지 아이템을 교환할 수 있습니다.\n"

  	  					+"10레벨 단위로 레벨업 시 보주 5개씩 획득\n연속 온라인 시간 1시간마다 보주 1개씩 획득\n125냥 충전 시 보주 1개 획득\n375냥 충전 시 보주 6개 획득\n"
  	  					+"625냥 충전 시 보주 15개 획득\n1250냥 충전 시 보주 40개 획득\n2500냥 충전 시 보주 100개 획득\n3750냥 충전 시 보주 180개 획득\n6250냥 충전 시 보주 350개 획득"
  	  					+"10000냥 충전 시 보주 640개 획득\n12500냥 충전 시 보주 900개 획득");
  					out.print("修改后内容："+notice.getNoticeContent());
  				}
  			}
  		%>
  	</p>
  </body>
</html>
