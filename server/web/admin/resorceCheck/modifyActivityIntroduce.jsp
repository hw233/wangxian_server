<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.props.PetProps"%>
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
<body>
<div class="clearfix">
<form action="activityIntroduce.jsp" method="post">
<table>
	<%
		String mid = request.getParameter("mid");

		ActivityManager am = ActivityManager.getInstance();
		List<ActivityIntroduce> aiList = am.getActivityIntroduces();
		ActivityIntroduce ai = new ActivityIntroduce();
		if (null != mid && !"".equals(mid.trim())) {
			for (ActivityIntroduce al : aiList) {
				int aid = al.getId();
				if (Integer.valueOf(mid) == aid) {
					ai = al;
				}
			}
		}
		String startstr="";
		String endstr="";
		String tstart="";
		String tend="";
		String weekDays = (null == ai.getWeekDay() || "".equals(ai.getWeekDay())) ? "" : Arrays.toString(ai.getWeekDay()).substring(1, Arrays.toString(ai.getWeekDay()).length() - 1).replace(" ","");
		SimpleDateFormat sdformat1 = new SimpleDateFormat("HH:mm");
		if (ai.getStart() != null) {
			startstr = sdformat1.format(ai.getStart().getTime());
			endstr = sdformat1.format(ai.getEnd().getTime());
		}
		SimpleDateFormat sdformat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (ai.getTotalStart() != 0l) {
			tstart = sdformat2.format(ai.getTotalStart());  
			tend = sdformat2.format(ai.getTotalEnd());
		}
		String sServernames = (null == ai.getShowServernames() || "".equals(ai.getShowServernames())) ? "" : Arrays.toString(ai.getShowServernames()).substring(1, Arrays.toString(ai.getShowServernames()).length() - 1).replace(" ","");
		String lServernames = (null == ai.getLimitServernames() || "".equals(ai.getLimitServernames())) ? "" : Arrays.toString(ai.getLimitServernames()).substring(1, Arrays.toString(ai.getLimitServernames()).length() - 1).replace(" ","");
	%>
	<tr>
		<input type="hidden" name="modify" value="modify">
		<td>活动ID</td>
		<td><input type="hidden" name="mid"
			value="<%=ai.getId()%>"><%=ai.getId()%></td> 
	</tr>
	<tr>
		<td>类型(在/不在大列表显示)</td>
		<td><input type="text" name="showType"
			value="<%=ai.getShowType()%>"></input></td>
	</tr>
	<tr>
		<td>在周几出现,逗号分隔</td>
		<td><input type="text" name="weekDay" value="<%=weekDays%>"></input></td>
	</tr>
	<tr>
		<td>开始时间(小时:分钟)</td>
		<td><input type="text" name="start" value="<%="".equals(startstr)?"":startstr%>"></input></td>
	</tr>
	<tr>
		<td>结束时间(小时:分钟)</td>
		<td><input type="text" name="end" value="<%="".equals(endstr)?"":endstr%>"></input></td>
	</tr>
	<tr>
		<td>活动名字</td>
		<td><input type="text" name="name" value="<%=ai.getName()==null?"":ai.getName()%>"></input>*</td>
	</tr>
	<!--  <tr><td>NPC所在地图</td><td><input type="text" name=""></input></td></tr>
	<tr><td>npc名字</td><td><input type="text" name=""></input></td></tr> -->
	<tr>
		<td>活动描述</td>
		<td><textarea type="textarea" name="des"
			style="margin: 2px; height: 100px; width: 900px;"><%=ai.getDes()==null?"":ai.getDes()%></textarea>*</td>
	</tr>
	<!--<tr>
		<td>隶属卷标(分卷标显示,逗号分隔)</td>
		<td><input type="text" name=""></input></td>
	</tr>
	 <tr>
		<td>增加活跃值</td>
		<td><input type="text" name="activityAdd"
			value=""></input></td>
	</tr> 
	<tr>
		<td>对应任务组名</td>
		<td><input type="text" name="lables" value="<%=Arrays.toString(ai.getLables())%>"></input></td>
	</tr>-->
	<tr>
		<td>等级限制</td>
		<td><input type="text" name="levelLimit"
			value="<%=ai.getLevelLimit()%>"></input></td>
	</tr>
	<tr>
		<td>开始时间,必填yyyy-MM-dd HH:mm:ss</td>
		<td><input type="text" name="totalStart" value="<%=tstart%>"></input>*</td>
	</tr>
	<tr>
		<td>结束时间,必填yyyy-MM-dd HH:mm:ss</td>
		<td><input type="text" name="totalEnd" value="<%=tend%>"></input>*</td>
	</tr>
	<tr>
		<td>是否依据开始/结束时间显示</td>
		<td><input type="text" name="isShowAccordTime"
			value="<%=ai.getIsShowAccordTime()==null?"":ai.getIsShowAccordTime()%>"></input></td>
	</tr>
	<tr>
		<td>开放的游戏服务器</td>
		<td><input type="text" name="showServernames"
			value="<%=sServernames%>"></input></td>
	</tr>
	<tr>
		<td>不开放的游戏服务器</td>
		<td><input type="text" name="limitServernames"
			value="<%=lServernames%>"></input></td>
	</tr>
	<tr>
		<td></td>
		<td><input type="submit" value="修改"></input></td>
	</tr>
</table>
</form>
</div>
</body>
</html>