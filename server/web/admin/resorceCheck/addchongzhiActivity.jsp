<%@page import="com.fy.engineserver.activity.newChongZhiActivity.NewMoneyActivity"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.engineserver.activity.newChongZhiActivity.NewChongZhiActivityManager"%>
<%@page import="com.fy.engineserver.activity.newChongZhiActivity.TotalTimesChongZhiActivity"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>add充值</title>
</head>
<%
	String[] parameters = new String[]{"","","","",""};
	parameters[0] = "1000000";
	parameters[1] = "逆天大礼包,通天大礼包";
	parameters[2] = "1,1";
	parameters[3] = "-1,-1";
	parameters[4] = "true,true";
	out.print("刷前数量："+NewChongZhiActivityManager.instance.chongZhiActivitys.size()+"<br>");
// 	ArrayList<NewMoneyActivity> chongZhiActivitys = NewChongZhiActivityManager.instance.chongZhiActivitys;
// 	for(int i=0; i<chongZhiActivitys.size();i++){
// 		if(chongZhiActivitys.get(i).getConfigID()==8005){
// 			chongZhiActivitys.remove(i);
// 			break;
// 		}
// 	}
	
	TotalTimesChongZhiActivity totalActivity = new TotalTimesChongZhiActivity(8005, "每充即送", 2, "2013-06-14 00:00:00", "2013-06-20 23:59:00", new String[]{"ALL_SERVER"}, new String[]{}, "恭喜您参与充值送豪礼", "恭喜您参与周年庆典充值送豪礼，每充值20元即可获丰富的大礼包，奖励请在附件中查收，感谢您对飘渺寻仙曲的支持！", parameters);
	totalActivity.creatParameter(parameters);
	totalActivity.loadDiskCache();
	NewChongZhiActivityManager.instance.chongZhiActivitys.add(totalActivity);
	out.print("刷后数量："+NewChongZhiActivityManager.instance.chongZhiActivitys.size());
%>