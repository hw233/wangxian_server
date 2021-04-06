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
		<form action="activityIntroduce.jsp">
			<input type="submit" value="返回显示全部" />
		</form>
	</div>
	<div><form action="addActivityIntroduceHandle.jsp">
	<table>
	<%
	/*private int id;
	private String icon;
	private int showType;
	private String openTimeDistance;
	private String name;
	private String startGame;
	private String startGameCnName;
	private String startNpc;
	private int startX;
	private int startY;
	private String des;
	private int activityAdd;
	private String[] lables = new String[0];
	private int levelLimit;
	private String taskGroupName;
	private int dailyNum;
	// 周几的活动
	private Integer[] weekDay;
	// 开始时间
	private Calendar start;
	// 结束时间 
	private Calendar end;

	private String isShowAccordTime;

	private String[] showServernames;

	private String[] limitServernames;

	// 活动期限 
	private long totalStart;
	private long totalEnd; */
	/*String id = request.getParameter("id");
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
	
	aiList.add(ai);
	am.setActivityIntroduces(aiList);*/
	//out.print("添加成功！");
	%>
	<tr><td>活动ID</td><td><input type="text" name="id"></input>*</td></tr>
	<tr><td>类型(在/不在大列表显示)</td><td><input type="text" name="showType"></input></td></tr>
	<tr><td>在周几出现,逗号分隔</td><td><input type="text" name="weekDay"></input></td></tr>
	<tr><td>开始时间(小时:分钟)</td><td><input type="text" name="start"></input></td></tr>
	<tr><td>结束时间(小时:分钟)</td><td><input type="text" name="end"></input></td></tr>
	<tr><td>活动名字</td><td><input type="text" name="name"></input></td></tr>
	<!--  <tr><td>NPC所在地图</td><td><input type="text" name=""></input></td></tr>
	<tr><td>npc名字</td><td><input type="text" name=""></input></td></tr> -->
	<tr><td>活动描述</td><td><textarea type="textarea" name="des" style="margin: 2px; height: 100px; width: 900px;"></textarea></td></tr>
	<tr><td>隶属卷标(分卷标显示,逗号分隔)</td><td><input type="text" name=""></input></td></tr>
	<tr><td>增加活跃值</td><td><input type="text" name="activityAdd"></input></td></tr>
	<tr><td>对应任务组名</td><td><input type="text" name="lables"></input></td></tr>
	<tr><td>等级限制</td><td><input type="text" name="levelLimit"></input></td></tr>
	<tr><td>开始时间,必填yyyy-MM-dd HH:mm:ss</td><td><input type="text" name="totalStart"></input>*</td></tr>
	<tr><td>结束时间,必填yyyy-MM-dd HH:mm:ss</td><td><input type="text" name="totalEnd"></input>*</td></tr>
	<tr><td>是否依据开始/结束时间显示</td><td><input type="text" name="isShowAccordTime"></input></td></tr>
	<tr><td>开放的游戏服务器</td><td><input type="text" name="showServernames"></input></td></tr>
	<tr><td>不开放的游戏服务器</td><td><input type="text" name="limitServernames"></input></td></tr>
	<tr><td></td><td><input type="submit" value="添加"></input>添加功能还没完善，先不要用哦</td></tr>
	</table>
	</form></div>
</div></body>
</html>