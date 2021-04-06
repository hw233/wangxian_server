<%@page import="com.fy.engineserver.activity.loginActivity.ContinueLoginActivity"%>
<%@page import="com.fy.engineserver.activity.loginActivity.Activity"%>
<%@page import="com.fy.engineserver.activity.loginActivity.ActivityManagers"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.fy.engineserver.datasource.article.concrete.*,com.google.gson.*,com.xuanzhi.tools.cache.*,com.fy.engineserver.sprite.concrete.*,com.fy.engineserver.sprite.*,java.util.*,java.lang.reflect.*,com.google.gson.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>台湾连登</title>
<%
	List<Activity> activitys = ActivityManagers.getInstance().getActivitys();
	for(Activity a : activitys){
		if(a instanceof ContinueLoginActivity){
			ContinueLoginActivity cla = (ContinueLoginActivity)a;
			if(cla.getPlatforms().length>0){
				if(cla.getPlatforms()[0].toString().equals("台湾")){
					Set<String> set = new HashSet<String>();
					cla.setOpenServers(set);
					out.print("修改OK:"+cla.getOpenServers().toString()+"<br>");
				}
			}
		}
	}
%>