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
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.engineserver.util.StringTool"%>
<%@page import="com.fy.engineserver.util.TimeTool"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>活动介绍</title>

<link rel="stylesheet" type="text/css" href="../css/table.css" />
<style type="text/css">
</style>
<script language="JavaScript">             
function delete_confirm(e) 
{
    if (event.srcElement.outerText == "删除") 
    {
        event.returnValue = confirm("删除是不可恢复的，你确认要删除吗？");
    }
}
document.onclick = delete_confirm;
</script>
</head>
<body>
<div class="clearfix">
<div>
<form action="addActivityIntroduce.jsp"><input type="submit"
	value="添加" />添加功能还没完善，先不要用哦</form>
</div>
<div>
<table>
	<%
		ActivityManager am = ActivityManager.getInstance();
		List<ActivityIntroduce> aiList = am.getActivityIntroduces();
		String action = request.getParameter("modify");
		if ("modify".equals(action)) {
			String mid = request.getParameter("mid");
			String showType = request.getParameter("showType");
			String weekDay = request.getParameter("weekDay");

			String startS = request.getParameter("start");
			String endS = request.getParameter("end");

			String name = request.getParameter("name");
			String des = request.getParameter("des");
			String levelLimit = request.getParameter("levelLimit");
			String totalStartTime = request.getParameter("totalStart");
			String totalEndTime = request.getParameter("totalEnd");
			String isShowAccordTime = request.getParameter("isShowAccordTime");
			String showServernames = request.getParameter("showServernames");
			String limitServernames = request.getParameter("limitServernames");
			if (null != mid && !"".equals(mid.trim())) {
				for (ActivityIntroduce al : aiList) {
					int aid = al.getId();
					if (Integer.valueOf(mid) == aid) {
						if (!"".equals(showType) && showType != null) {
							al.setShowType(Integer.valueOf(showType));
						}
						if (!"".equals(weekDay) && weekDay != null) {
							al.setWeekDay(StringTool.string2Array(weekDay, ",", Integer.class));
						}
						if (!"".equals(startS) && startS != null) {
							Calendar end = Calendar.getInstance();
							Integer[] startTimes = StringTool.string2Array(startS, ":", Integer.class);
							Calendar start = Calendar.getInstance();
							start.set(Calendar.HOUR_OF_DAY, startTimes[0]);
							start.set(Calendar.MINUTE, startTimes[1]);
							Integer[] endTimes = StringTool.string2Array(endS, ":", Integer.class);
							end.set(Calendar.HOUR_OF_DAY, endTimes[0]);
							end.set(Calendar.MINUTE, endTimes[1]);
							String openTimeDistance = startS + "-" + endS;
							al.setStart(start);
							al.setEnd(end);
						}
						al.setName(name);
						al.setDes(des);
						if (!"".equals(levelLimit) && levelLimit != null) {
							al.setLevelLimit(Integer.valueOf(levelLimit));
						}
						if (!"".equals(totalStartTime) && totalEndTime != null) {
							al.setTotalStart(TimeTool.formatter.varChar19.parse(totalStartTime));
							al.setTotalEnd(TimeTool.formatter.varChar19.parse(totalEndTime));
						}
						if (!"".equals(isShowAccordTime) && isShowAccordTime != null) {
							al.setIsShowAccordTime(isShowAccordTime);
						}
						if (!"".equals(showServernames) && showServernames != null) {
							al.setShowServernames(StringTool.string2Array(showServernames, ",", String.class));
						}
						if (!"".equals(limitServernames) && limitServernames != null) {
							al.setLimitServernames(StringTool.string2Array(limitServernames, ",", String.class));
						}
					}
				}
				am.setActivityIntroduces(aiList);
				out.print("<H1>修改成功</H1>");
			}
		}
		String id = request.getParameter("id");
		List<ActivityIntroduce> removeList = new ArrayList<ActivityIntroduce>();
		if (null != id && !"".equals(id.trim())) {
			for (ActivityIntroduce ai : aiList) {
				int aid = ai.getId();
				if (Integer.valueOf(id) == aid) {
					removeList.add(ai);
				}
			}
			for (ActivityIntroduce rml : removeList) {
				aiList.remove(rml);
			}
			am.setActivityIntroduces(aiList);
		}
	%>
	<tr>
		<td>活动ID</td>
		<td>活动名字</td>
		<td>活动描述</td>
		<td>开始时间</td>
		<td>结束时间</td>
		<td>是否依据开始/结束时间显示</td>
		<td>开放的游戏服务器</td>
		<td>不开放的游戏服务器</td>
		<td>操作</td>
	</tr>
	<%
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (ActivityIntroduce ai : aiList) {
			String start = sdf.format(ai.getTotalStart());
			//out.print(ai.getStart().get(Calendar.YEAR)+"年"+ai.getStart().get(Calendar.MONTH)+"月"+ai.getStart().get(Calendar.DAY_OF_MONTH)+"日"+ai.getStart().get(Calendar.HOUR_OF_DAY)+"时"+ai.getStart().get(Calendar.MINUTE)+"分"+ai.getStart().get(Calendar.MILLISECOND)+"秒<br>");
			String end = sdf.format(ai.getTotalEnd());

			String showSerernames = Arrays.toString(ai.getShowServernames());
			String limitServernames = Arrays.toString(ai.getLimitServernames());
	%>
	<tr>
		<td><%=ai.getId()%></td>
		<td style="width: 80px"><%=ai.getName()%></td>
		<td><%=ai.getDes()%></td>
		<td style="width: 80px"><%=start%></td>
		<td style="width: 80px"><%=end%></td>
		<td><%=ai.getIsShowAccordTime()%></td>
		<td style="width: 80px"><%=showSerernames%></td>
		<td style="width: 80px"><%=limitServernames%></td>
		<td style="width: 80px"><a
			href="modifyActivityIntroduce.jsp?mid=<%=ai.getId()%>">修改</a>&nbsp;<a
			href="activityIntroduce.jsp?id=<%=ai.getId()%>" onClick="delete_confirm">删除</a></td>
	</tr>
	<%
		}
	%>
</table>
</div>
</div>
</body>
</html>