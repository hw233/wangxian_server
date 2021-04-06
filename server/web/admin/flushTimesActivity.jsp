<%@page import="java.util.Date"%>
<%@page import="com.fy.engineserver.activity.ActivitySubSystem"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.fy.engineserver.activity.base.TimesActivity"%>
<%@page import="com.fy.engineserver.activity.base.TimesActivityManager"%>
<%@page import="com.fy.engineserver.activity.newChongZhiActivity.WeekAndMonthChongZhiActivity"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.engineserver.activity.newChongZhiActivity.NewChongZhiActivityManager"%>
<%@page import="com.fy.engineserver.smith.ProcessCat"%>
<%@page import="com.fy.engineserver.qiancengta.QianCengTa_Dao"%>
<%@page import="com.fy.engineserver.qiancengta.QianCengTa_Ceng"%>
<%@page import="com.fy.engineserver.authority.AuthorityAgent"%>
<%@page import="com.fy.engineserver.qiancengta.QianCengTa_Ta"%>
<%@page import="com.fy.engineserver.qiancengta.QianCengTaManager"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
int blessActivityID = 30000;
try{
	String startTime = "2013-09-30 00:00:00";
	String endTime = "2013-10-07 23:59:59";
	TimesActivity activity = new TimesActivity(blessActivityID, format.parse(startTime).getTime(), format.parse(endTime).getTime(), 2, TimesActivityManager.HEJIU_ACTIVITY);
	activity.setServerNames(new String[]{TimesActivity.ALL_SERVER_ACTIVITY});
	TimesActivityManager.instacen.addActivity(activity);
	blessActivityID++;
} catch (Exception e) {
	ActivitySubSystem.logger.error("活动创建出错",e );
}
try{
	String startTime = "2013-09-30 00:00:00";
	String endTime = "2013-10-07 23:59:59";
	TimesActivity activity = new TimesActivity(blessActivityID, format.parse(startTime).getTime(), format.parse(endTime).getTime(), 2, TimesActivityManager.TUMOTIE_ACTIVITY);
	activity.setServerNames(new String[]{TimesActivity.ALL_SERVER_ACTIVITY});
	TimesActivityManager.instacen.addActivity(activity);
	blessActivityID++;
} catch (Exception e) {
	ActivitySubSystem.logger.error("活动创建出错",e );
}
try{
	String startTime = "2013-09-30 00:00:00";
	String endTime = "2013-10-07 23:59:59";
	TimesActivity activity = new TimesActivity(blessActivityID, format.parse(startTime).getTime(), format.parse(endTime).getTime(), 1, TimesActivityManager.QIFU_ACTIVITY);
	activity.setServerNames(new String[]{TimesActivity.ALL_SERVER_ACTIVITY});
	TimesActivityManager.instacen.addActivity(activity);
	blessActivityID++;
} catch (Exception e) {
	ActivitySubSystem.logger.error("活动创建出错",e );
}
for (TimesActivity ac: TimesActivityManager.instacen.activitys) {
	out.println("活动ID:"+ac.activityID+" 开始时间:"+format.format(new Date(ac.startTime))+" 结束时间:"+format.format(new Date(ac.endTime))+" 活动类型"+ac.activityType+"<br>");
}
%>
</body>
</html>