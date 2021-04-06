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
<title>修改活动介绍</title>

<link rel="stylesheet" type="text/css" href="../css/table.css" />
<style type="text/css">

</style>
</head>
<body><div class="clearfix">
	<form action="">
	<table>
	<%
	String mid = request.getParameter("mid");
	String showType = request.getParameter("showType");
	String weekDay = request.getParameter("weekDay");
	String start = request.getParameter("start");
	String end = request.getParameter("end");
	String name = request.getParameter("name");
	String des = request.getParameter("des");
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
	if(null != mid && !"".equals(mid.trim())){
		for(ActivityIntroduce al:aiList){
			int aid = al.getId();
			if(Integer.valueOf(mid) == aid){
				ai = al;
			}
		}
	}
	String weekDays = (null == ai.getWeekDay()||"".equals(ai.getWeekDay()))? "":Arrays.toString(ai.getWeekDay()).substring(1,Arrays.toString(ai.getWeekDay()).length()-1);
	SimpleDateFormat sdformat1 = new SimpleDateFormat("HH:mm");
	String startstr = sdformat1.format(ai.getStart().getTime());
	String endstr = sdformat1.format(ai.getEnd().getTime());
	SimpleDateFormat sdformat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String tstart = sdformat2.format(ai.getTotalStart());
	String tend = sdformat2.format(ai.getTotalEnd());
	String sServernames = (null == ai.getShowServernames() || "".equals(ai.getShowServernames()))? "":Arrays.toString(ai.getShowServernames()).substring(1,Arrays.toString(ai.getShowServernames()).length()-1);
	String lServernames = (null == ai.getLimitServernames() || "".equals(ai.getLimitServernames()))? "":Arrays.toString(ai.getLimitServernames()).substring(1,Arrays.toString(ai.getLimitServernames()).length()-1);
	
	if(null != showType && !"".equals(showType.trim())){
		ai.setShowType(Integer.valueOf(showType));
	}
	if(null != weekDay && !"".equals(weekDay.trim())){
		String  weekDayStr[] = weekDay.split(",");
		Integer weekDayInt[] = new Integer[weekDay.length()];
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
	
	am.setActivityIntroduces(aiList);
	//out.print("添加成功！");
	%>
	<tr><td>活动ID</td><td><%=ai.getId() %></td></tr>
	<tr><td>类型(在/不在大列表显示)</td><td><input type="text" name="showType" value="<%=ai.getShowType() %>"></input></td></tr>
	<tr><td>在周几出现,逗号分隔</td><td><input type="text" name="weekDay" value="<%=weekDays %>"></input></td></tr>
	<tr><td>开始时间(小时:分钟)</td><td><input type="text" name="start" value="<%=ai.getStart() %>"></input></td></tr>
	<tr><td>结束时间(小时:分钟)</td><td><input type="text" name="end" value="<%=ai.getEnd() %>"></input></td></tr>
	<tr><td>活动名字</td><td><input type="text" name="name" value="<%=ai.getName() %>"></input></td></tr>
	<!--  <tr><td>NPC所在地图</td><td><input type="text" name=""></input></td></tr>
	<tr><td>npc名字</td><td><input type="text" name=""></input></td></tr> -->
	<tr><td>活动描述</td><td><textarea type="textarea" name="des" style="margin: 2px; height: 100px; width: 900px;"><%=ai.getDes() %></textarea></td></tr>
	<tr><td>隶属卷标(分卷标显示,逗号分隔)</td><td><input type="text" name=""></input></td></tr>
	<tr><td>增加活跃值</td><td><input type="text" name="activityAdd" value="<%=ai.getActivityAdd() %>"></input></td></tr>
	<tr><td>对应任务组名</td><td><input type="text" name="lables" value="<%=ai.getLables() %>"></input></td></tr>
	<tr><td>等级限制</td><td><input type="text" name="levelLimit" value="<%=ai.getLevelLimit() %>"></input></td></tr>
	<tr><td>开始时间,必填yyyy-MM-dd HH:mm:ss</td><td><input type="text" name="totalStart" value="<%=tstart %>"></input>*</td></tr>
	<tr><td>结束时间,必填yyyy-MM-dd HH:mm:ss</td><td><input type="text" name="totalEnd" value="<%=tend %>"></input>*</td></tr>
	<tr><td>是否依据开始/结束时间显示</td><td><input type="text" name="isShowAccordTime" value="<%=ai.getIsShowAccordTime() %>"></input></td></tr>
	<tr><td>开放的游戏服务器</td><td><input type="text" name="showServernames" value="<%=sServernames %>"></input></td></tr>
	<tr><td>不开放的游戏服务器</td><td><input type="text" name="limitServernames" value="<%=lServernames %>"></input></td></tr>
	<tr><td></td><td><input type="submit" value="修改"></input></td></tr>
	</table>
	</form>
</div></body>
</html>