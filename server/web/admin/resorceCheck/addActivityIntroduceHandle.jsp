<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.PetProps"%>
<%@page import="com.fy.engineserver.activity.ActivityManager"%>
<%@page import="com.fy.engineserver.activity.ActivityIntroduce"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Arrays"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@page import="com.fy.engineserver.util.TimeTool"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>添加活动介绍</title>

<link rel="stylesheet" type="text/css" href="../css/table.css" />
<style type="text/css">

</style>
</head>
<body><div class="clearfix">
	<div>
	<%
	
	String id = request.getParameter("id");
	String showType = request.getParameter("showType");
	String weekDay = request.getParameter("weekDay");
	String start = request.getParameter("start");
	String end = request.getParameter("end");
	String name = request.getParameter("name");
	//String end = request.getParameter("end");
	//String end = request.getParameter("end");
	String des = request.getParameter("des");
	//String end = request.getParameter("end");
	String activityAdd = request.getParameter("activityAdd");
	String lables = request.getParameter("lables");
	String levelLimit = request.getParameter("levelLimit");
	String totalStart = request.getParameter("totalStart");
	String totalEnd = request.getParameter("totalEnd");
	String isShowAccordTime = request.getParameter("isShowAccordTime");
	String showServernames = request.getParameter("showServernames");
	String limitServernames = request.getParameter("limitServernames");
	
	ActivityManager am = ActivityManager.getInstance();
	List<ActivityIntroduce> aiList = am.getActivityIntroduces();
	ActivityIntroduce ai = new ActivityIntroduce();
	if(null != id && !"".equals(id.trim())){
		for(ActivityIntroduce list:aiList){
			int aid = list.getId();
			if(Integer.valueOf(id) == aid){
				out.print("该id已经存在<br>");
				return;
			}
		}
		ai.setId(Integer.valueOf(id));
	}else{
		out.print("id不能为空！<br>");
	}
	if(null != showType && !"".equals(showType.trim())){
		ai.setShowType(Integer.valueOf(showType));
	}
	if(null != weekDay && !"".equals(weekDay.trim())){
		String  weekDayStr[] = weekDay.split(",");
		Integer weekDayInt[] = new Integer[weekDayStr.length];
		for(int i=0;i<weekDayStr.length;i++){
			weekDayInt[i] = Integer.valueOf(weekDayStr[i]);
		}
		ai.setWeekDay(weekDayInt);
	}
	if(null != start && !"".equals(start.trim())){
		SimpleDateFormat sdf= new SimpleDateFormat("hh:mm");
		Date date =sdf.parse(start);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		ai.setStart(calendar);
	}
	if(null != end && !"".equals(end.trim())){
		SimpleDateFormat sdf= new SimpleDateFormat("hh:mm");
		Date date =sdf.parse(end);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		ai.setEnd(calendar);
	}
	if(null != name && !"".equals(name.trim())){
		ai.setName(name);
	}
	if(null != des && !"".equals(des.trim())){
		ai.setDes(des);
	}
	if(null != activityAdd && !"".equals(activityAdd.trim())){
		ai.setActivityAdd(Integer.valueOf(activityAdd));
	}
	if(null != lables && !"".equals(lables.trim())){
		ai.setLables(lables.split(","));
	}
	if(null != levelLimit && !"".equals(levelLimit.trim())){
		ai.setLevelLimit(Integer.valueOf(levelLimit));
	}
	if(null != totalStart && !"".equals(totalStart.trim())){
		ai.setTotalStart(TimeTool.formatter.varChar19.parse(totalStart));
	}
	if(null != totalEnd && !"".equals(totalEnd.trim())){
		ai.setTotalEnd(TimeTool.formatter.varChar19.parse(totalEnd));
	}
	if(null != isShowAccordTime && !"".equals(isShowAccordTime.trim())){
		ai.setIsShowAccordTime(isShowAccordTime);
	}
	if(null != showServernames && !"".equals(showServernames.trim())){
		ai.setShowServernames(showServernames.split(","));
	}
	if(null != limitServernames && !"".equals(limitServernames.trim())){
		ai.setLimitServernames(limitServernames.split(","));
	}
	
	aiList.add(ai);
	am.setActivityIntroduces(aiList);
	response.sendRedirect("activityIntroduce.jsp");
	//out.print("添加成功！");
	%>
	</div>
</div></body>
</html>