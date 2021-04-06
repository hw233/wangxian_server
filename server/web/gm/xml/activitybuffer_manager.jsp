<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page
	import="com.fy.engineserver.operating.activities.ActivityItemManager"%>
<%@page
	import="com.fy.engineserver.operating.activities.ActivityItem"%>
<%@page
	import="com.fy.engineserver.operating.activitybuff.ActivityBuffItemManager"%>
<%@page
	import="com.fy.engineserver.operating.activitybuff.ActivityBuffItem"%>
<%@include file="../header.jsp"%>
<%@include file="../authority.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>buffer任务管理</title>
<link rel="stylesheet" href="../style.css" />
<script type="text/javascript">
	function sub() {
		var name = document.getElementById("buffname").value;
		var desc = document.getElementById("desc").value;
		if (name && desc) {
			// alert("添加成功");
			document.getElementById("ff").submit();
		} else
			alert("请输入正确的信息！！");
	}
</script>
</head>
<body>
	<%
		try {
			String[] ws = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
			String username = session.getAttribute("username").toString();
			ActivityBuffItemManager abmanager = ActivityBuffItemManager.getInstance();
			out.print("<input type='button' value='刷新' onclick='window.location.replace(\"activitybuffer_manager.jsp\")' /><br/>");
			ActionManager amanager = ActionManager.getInstance();
			String action = request.getParameter("action");
			if (action != null) {
				if ("add".equals(action.trim())) {
					String buffname = request.getParameter("buffname");
					String desc = request.getParameter("desc");
					out.print(buffname + "|bfname   " + desc + "| desc");
					abmanager.createActivityBuffItem(buffname, desc);
					abmanager.saveAll();
					amanager.save(username, "添加了一个任务buff：name[" + buffname + "]");
				}
				if ("del".equals(action.trim())) {
					int id = Integer.parseInt(request.getParameter("delid"));
					abmanager.deleteActivityBuffItem(id);
					abmanager.saveAll();
					amanager.save(username, "删除了一个任务buff：id[" + id + "]");
				}
			}
			ActivityBuffItem[] ab = null;
			try {
				ab = abmanager.getActivityBuffItems();
			} catch (Exception e) {
				ab = new ActivityBuffItem[0];
			}
			out.print("<table width='80%'><th>ID</th><th>buffName</th><th>timeType</th><th>时间段</th><th>等级限制</th><th>地图限制</th><th>阵营</th><th>操作</th></tr>");
			if (ab != null && ab.length > 0) {
				for (ActivityBuffItem a : ab) {
					out.print("<tr><td>" + a.getId() + "</td><td>" + a.getBuffName() + "(等级" + a.getBuffLevel() + ")</td><td>");
					if (a.getTimeType() == 0) out.print("每天");
					if (a.getTimeType() == 1) out.print("每周：" + ws[a.getWeekDay()]);
					if (a.getTimeType() == 2) out.print("固定时间：" + a.getFixDay());
					out.print("</td><td>" + a.getStartTimeInDay() + " ~ " + a.getEndTimeInDay() + "</td>");
					out.print("<td>" + a.getPlayerLevelLimit() + "</td><td>" + (a.isEnableMapLimit() ? a.getMapLimit() : "无") + "</td><td>");
					if (a.getPlayerPoliticalCampLimit() == 0) out.print("无限制");
					if (a.getPlayerPoliticalCampLimit() == 1) out.print("紫微宫");
					if (a.getPlayerPoliticalCampLimit() == 2) out.print("日月盟");
					out.print("</td><td>");
					out.print("<input type='button' value='修改' onclick='window.location.replace(\"activitybuffer_update.jsp?id=" + a.getId() + "\")' />");
					out.print("<input type='button' value='删除' onclick='window.location.replace(\"activitybuffer_manager.jsp?action=del&delid=" + a.getId() + "\")' /></td></tr>");
				}
			}
			out.print("<form method='post' action='activitybuffer_manager.jsp?action=add' id='ff' ><table width='40%' align='center'>");
			out.print("<tr><th>活动名：</th><td class='top'><input type='text' name='buffname' id='buffname' value='' /></td></tr>");
			out.print("<tr><th>描述：</th><td ><input type='text' name='desc' id='desc' value='' /></td></tr>");
			out.print("<tr><td colspan ='2'><input type='button' value='增加' onclick='sub();' />");
			out.print("<input type='reset' vlue='重置' /></td></tr></table></form>");
		} catch (Exception e) {
			//e.printStackTrace();
			//out.print("悲剧啦"+e.getMessage());
			//RequestDispatcher rdp = request
			//		.getRequestDispatcher("../gmuser/visitfobiden.jsp");
			//rdp.forward(request, response);
			out.print(StringUtil.getStackTrace(e));
		}
	%>
</body>
</html>
