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
<%@include file="../authority.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=utf-8">
		<title>任务管理</title>
		<link rel="stylesheet" href="../style.css" />
		<style type="text/css">
#a {
	font-size: 0px;
}

#a li {
	font-size: 16px;
	text-align: center;
	width: 110px;
	list-style-type: none;
	display: inline;
	padding: 5px 10px;
}
</style>
		<script type="text/javascript">
	function overTag(tag) {
		tag.style.color = "red";
		tag.style.backgroundColor = "lightcyan";
	}

	function outTag(tag) {
		tag.style.color = "black";
		tag.style.backgroundColor = "white";
	}
	function sub() {
		var name = document.getElementById("buffname").value;
		var shour = document.getElementById("shour").value;
		var smin = document.getElementById("smin").value;
		var ehour = document.getElementById("ehour").value;
		var emin = document.getElementById("emin").value;
		var bufflevel = document.getElementById("bufflevel").value;
		var pll = document.getElementById("pll").value;
		if (name && shour && smin && ehour && emin && pll && bufflevel) {
			document.getElementById("ff").submit();
		} else
			alert("请填入正确的信息！！");
	}
	function geneweek() {
		var ws = [ "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" ];
		var str = "星期:<select name='weekday' id='weekday' >";
		for ( var i = 0; i < 7; i++) {
			str = str + "<option value='" + i + "' >" + ws[i] + "</option>";
		}
		str = str + "</select>";
		return str;
	}
	function change1() {
		var type = document.getElementById("timetype").value;
		var div1 = document.getElementById("tt");
		if (type == 0)
			div1.innerHTML = "";
		if (type == 1)
			div1.innerHTML = geneweek();
		if (type == 2)
			div1.innerHTML = "固定时间(格式如：2010-04-29)：<input type='text' name='fixday' id='fixday' />";
	}
	function chm(mapname) {
		var eml = document.getElementById("eml").value;
		var div1 = document.getElementById("dd");
		if (eml == "true")
			div1.innerHTML = "地图限制：<input type='text' name='maplimit' id='maplimit' value='"
					+ mapname + "' />";
		if (eml == "false")
			div1.innerHTML = "";
	}
</script>
	</head>
	<body>
		<%
			try {
				ActivityBuffItemManager abmanager = ActivityBuffItemManager
						.getInstance();
				/**
				 *根据ID获取一个BUFF
				 */
				int id = Integer.parseInt(request.getParameter("id"));
				ActivityBuffItem a = abmanager.getActivityBuffItem(id);
				/**
				 *验证是否登录
				 */
				String username = session.getAttribute("username").toString();
				ActionManager amanager = ActionManager.getInstance();
				String action = request.getParameter("action");
				if (action != null && "update".equals(action)) {
					String buffname = request.getParameter("buffname");
					boolean res = true;
					int timetype = Integer.parseInt(request.getParameter(
							"timetype").trim());

					if (timetype == 1)
						a.setWeekDay(Integer.parseInt(request
								.getParameter("weekday")));
					if (timetype == 2
							&& request.getParameter("fixday").split("-").length == 3)
						a.setFixDay(request.getParameter("fixday"));
					String stime = request.getParameter("shour") + ":"
							+ request.getParameter("smin");
					long stimel = DateUtil.parseDate(stime, "HH:mm").getTime();
					String etime = request.getParameter("ehour") + ":"
							+ request.getParameter("emin");
					long etimel = DateUtil.parseDate(etime, "HH:mm").getTime();
					if (res && abmanager.getActivityBuffItems() != null
							&& abmanager.getActivityBuffItems().length > 0) {
						for (ActivityBuffItem as : abmanager
								.getActivityBuffItems()) {
							long astimel = DateUtil.parseDate(
									as.getStartTimeInDay(), "HH:mm").getTime();
							long aetimel = DateUtil.parseDate(
									as.getEndTimeInDay(), "HH:mm").getTime();
							if (as.getBuffName() != null&&as.getId()!=a.getId()
									&& as.getBuffName().equals(buffname)
									&& ((stimel > astimel && stimel < aetimel)
											|| (etimel > astimel && etimel < aetimel)
											|| (astimel > stimel && astimel < etimel) || (aetimel > stimel && aetimel < etimel))) {
								res = false;
								out.print("时间限制"+as.getId()+"["+as.getStartTimeInDay()+"]["+as.getEndTimeInDay()+"]["+as.getId()+"]["+a.getId()+"]");
								break;
							}
						}
					}
					String bufflevel = request.getParameter("bufflevel");
					if (Integer.parseInt(bufflevel.trim()) < 1) {
						res = false;
					}
					int pll = Integer.parseInt(request.getParameter("pll")
							.trim());
					boolean eml = Boolean.parseBoolean(request.getParameter(
							"eml").trim());
					a.setEnableMapLimit(eml);
					if (eml)
						a.setMapLimit(request.getParameter("maplimit"));
					int ppcl = Integer.parseInt(request.getParameter("ppcl"));
					a.setPlayerPoliticalCampLimit(ppcl);
					if (res) {
						a.setBuffName(buffname);
						a.setTimeType(timetype);
						a.setStartTimeInDay(stime);
						a.setPlayerLevelLimit(pll);
						a.setEndTimeInDay(etime);
						a.setBuffLevel(Integer.parseInt(bufflevel.trim()));
						amanager.save(username, "更新了一个任务buff：id[" + id + "]");
						abmanager.saveAll();
						out.print("修改成功");
						out.print(a.getBuffLevel() + "|" + a.getBuffName()
								+ "|" + a.getEndTimeInDay() + "|"
								+ a.getFixDay() + "|");
						out.print(a.getMapLimit() + "|"
								+ a.getPlayerLevelLimit() + "|"
								+ a.getPlayerPoliticalCampLimit() + "|"
								+ a.getStartTimeInDay() + "|");
						out.print(a.getTimeType() + "|" + a.getWeekDay() + "|"
								+ a.getId());
					} else if (!res) {
						out.print("修改失败，请检验有没有不合格数值");
					}

				}
				out
						.print("<form method='post' action='?action=update' id='ff' ><table width='40%' align='center'>");
				out
						.print("<tr><th>buffer名：</th><input type='hidden' name='id' value='"
								+ a.getId()
								+ "' /><td class='top'><input type='text' name='buffname' id='buffname' value='"
								+ a.getBuffName() + "' /></td></tr>");
				out
						.print("<tr><th>任务类型:</th><td><select id='timetype' name='timetype' onchange='change1();' >");
				out
						.print("<option value='0'>每天</option><option value='1' >每周</option><option value='2' >固定时间</option></select><div id='tt' ></div></td></tr>");
				String ssh1 = " : ";
				if (a.getStartTimeInDay() != null
						|| !"".equals(a.getStartTimeInDay().trim()))
					ssh1 = a.getStartTimeInDay();
				out
						.print("<tr><th>开始时间</th><td><input type='text' size='8' name='shour' id='shour' value='"
								+ ssh1.split(":")[0] + "' />时");
				out.print("<input type='text' name='smin' id='smin' value='"
						+ ssh1.split(":")[1] + "' />分</td></tr>");
				String eesh1 = " : ";
				if (a.getEndTimeInDay() != null
						|| !"".equals(a.getEndTimeInDay().trim()))
					eesh1 = a.getEndTimeInDay();
				out
						.print("<tr><th>结束时间</th><td><input type='text' size='8' name='ehour' id='ehour' value='"
								+ eesh1.split(":")[0] + "' />时");
				out.print("<input type='text' name='emin' id='emin' value='"
						+ eesh1.split(":")[1] + "' />分</td></tr>");
				out
						.print("<tr><th>级别限制</th><td><input type='text' name='pll' id='pll' value='"
								+ a.getPlayerLevelLimit() + "' /></td></tr>");
				out
						.print("<tr><th>地图限制</th><td><select id='eml' name='eml' onchange='chm(\""
								+ a.getMapLimit() + "\")' > ");
				out.print("<option value='false' selected >否</option>");
				out
						.print("<option value='true' >是</option></select><div id='dd' ></div></select></td></tr>");
				out
						.print("<tr><th>阵营限制：</th><td><select id='ppcl' name='ppcl'>");
				out
						.print("<option value='0'>无阵营</option><option value='1' >紫微宫</option><option value='2'>日月盟</option></select></td><tr>");
				out
						.print("<tr><th>buff等级</th><td>(buff等级大于0)<input type='text' value='"
								+ a.getBuffLevel()
								+ "' id='bufflevel' name='bufflevel' /> </td></tr>");
				out
						.print("<tr><td colspan ='2'><input type='button' value='更新' onclick='sub();' />");
				out
						.print("<input type='reset' vlue='重置' /></td></tr></table></form>");
				out
						.print("<input type='button' value='返回任务BUFF列表' onclick='window.location.replace(\"activitybuffer_manager.jsp\")'/> ");
			} catch (Exception e) {
				//e.printStackTrace();
				//out.print(e.getMessage());
				//RequestDispatcher rdp = request
				//		.getRequestDispatcher("../gmuser/visitfobiden.jsp");
				//rdp.forward(request, response);
				out.print(StringUtil.getStackTrace(e));
			}
		%>
	</body>
</html>
