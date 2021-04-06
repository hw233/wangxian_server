<%@page import="com.fy.engineserver.util.StringTool"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="./inc.jsp"%>
<%
	String sid = request.getParameter("id");
	Task task = null;
	if (sid != null && !sid.isEmpty()) {
		task = TaskManager.getInstance().getTask(Long.valueOf(sid));
	}
	if (task == null) {
		out.print("任务不存在");
		return;
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="../task/css/main.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>任务信息--<%=task == null ? "NULL" : task.getName()%></title>
</head>
<body>
	<table border="1">
		<tr>
			<td class="title">ID</td>
			<td><%=task.getId()%></td>
			<td class="title">名字</td>
			<td><%=task.getName()%></td>
			<td class="title">组名</td>
			<td><%=task.getGroupName()%></td>
			<td class="title">显示类型</td>
			<td><%=task.getShowType()%></td>
		</tr>
		<tr>
			<td class="title">任务等级</td>
			<td><%=task.getGrade()%></td>
			<td class="title">接取下限</td>
			<td><%=task.getMinGradeLimit()%></td>
			<td class="title">接取上限</td>
			<td><%=task.getMaxGradeLimit()%></td>
			<td class="title">体力值消耗</td>
			<td><%=task.getThewCost()%></td>
		</tr>
		<tr>
			<td class="title">性别限制</td>
			<td><%=task.getSexLimit()%></td>
			<td class="title">职业限制</td>
			<td><%=task.getWorkLimit()%></td>
			<td class="title">可接职业</td>
			<td><%=(task.getWorkLimits()==null?"null":Arrays.toString(task.getWorkLimits()))%></td>
			<td class="title">国家限制</td>
			<td><%=task.getCountryLimit()%></td>
			<td class="title">国家职务</td>
			<td><%=task.getCountryOfficialLimit()%></td>
		</tr>
		<tr>
			<td class="title">体力限制</td>
			<td><%=task.getThewLimit()%></td>
			<td class="title">境界限制</td>
			<td><%=task.getBourn()%></td>
			<td class="title">家族限制</td>
			<td><%=task.getSeptLimit()%></td>
			<td class="title">家族职务</td>
			<td><%=task.getSeptOfficialLimit()%></td>
		</tr>
		<tr>
			<td class="title">社会关系</td>
			<td><%=task.getSocialRelationsLimit()%></td>
			<td class="title">任务类型</td>
			<td><%=task.getType()%></td>
			<td class="title">前置任务组名</td>
			<td><%=task.getFrontGroupName()%></td>
			<td class="title">前置任务依赖关系</td>
			<td><%=task.getDependType()%></td>
		</tr>
		<tr>
			<td class="title">接取时间限制</td>
			<td><%=task.getTimeLimit() == null ? "无" : task.getTimeLimit().getLimltType()%></td>
			<td class="title">时间限制</td>
			<td><%=task.getTimeLimit() == null ? "无" : task.getTimeLimit().toString()%></td>
			<td class="title">接取后时间限制</td>
			<td><%=task.getDeliverTimeLimit() == null ? "无" : task.getDeliverTimeLimit().getType()%></td>
			<td class="title">时间限制</td>
			<td><%=task.getDeliverTimeLimit() == null ? "无" : task.getDeliverTimeLimit().getTime()%>秒</td>
		</tr>
		<tr>
			<td class="title" title="<%=task.getDailyTaskCycle()%>">日常任务周期</td>
			<td><%=task.getDailyTaskCycle() > 0 ? task.getDailyTaskCycle() + "天" : "不是日常"%></td>
			<td class="title">周期内次数限制</td>
			<td><%=task.getDailyTaskCycle() > 0 ? task.getDailyTaskMaxNum() + "次" : "----"%></td>
			<td class="title">是否计算积分</td>
			<td><%=task.getCountScore() > 0 ? "计算" : "不计算"%></td>
		</tr>
		<tr>
			<td class="title">接取NPC</td>
			<td><%=task.getStartNpc()%></td>
			<td class="title">接取NPC SEX</td>
			<td><%=task.getStartNPCAvataSex()%></td>
			<td class="title">接取NPC RACE</td>
			<td><%=task.getStartNPCAvataRace()%></td>
			<td class="title">接取NPC对话</td>
			<td colspan="5"><%=task.getStartTalk()%></td>
		</tr>
		<tr>
			<td class="title">接取X</td>
			<td><%=task.getStartX()%></td>
			<td class="title">接取Y</td>
			<td><%=task.getStartY()%></td>
			<td class="title">完成X</td>
			<td><%=task.getEndX()%></td>
			<td class="title">完成Y</td>
			<td colspan="5"><%=task.getEndY()%></td>
		</tr>
		<tr>
			<td class="title">交付NPC</td>
			<td><%=task.getEndNpc()%></td>
			<td class="title">交付地图</td>
			<td><%=task.getEndMapResName()%></td>
			<td class="title">接取NPC对话</td>
			<td colspan="3"><%=task.getEndTalk()%></td>
		</tr>

		<tr>
			<td colspan="4" class="title">任务描述</td>
			<td colspan="4"><%=task.getDes()%></td>
		</tr>
		<tr>
			<td colspan="4" class="title">未完成对话</td>
			<td colspan="4"><%=task.getUnDeliverTalk()%></td>
		</tr>
		<%
			if (task.getGivenArticle() != null) {
		%>
		<tr>
			<td colspan="" class="title">任务给予物品</td>
			<td colspan=""><%=StringTool.stringArr2String(task.getGivenArticle().getNames(), ",")%></td>
			<td colspan="" class="title">任务给予物品数量</td>
			<td colspan=""><%=StringTool.intArr2String(task.getGivenArticle().getNums(), ",")%></td>
			<td colspan="" class="title">任务给予物品颜色</td>
			<td colspan=""><%=StringTool.intArr2String(task.getGivenArticle().getColors(), ",")%></td>
		</tr>
		<%
			}
		%>
	</table>
	<table border="1">
		<tr>
			<td colspan="7" class="title">任务目标</td>
		</tr>
		<tr>
			<td>目标类型</td>
			<td>数量</td>
			<td>颜色</td>
			<td>名字</td>
			<td>所在地图</td>
			<td>Xs</td>
			<td>Ys</td>
		</tr>
		<%
			for (TaskTarget tt : task.getTargets()) {
		%>
		<tr>
			<td><%=tt.getTargetType().getName()%></td>
			<td><%=tt.getTargetNum()%></td>
			<td><%=tt.getTargetColor()%></td>
			<td><%=Arrays.toString(tt.getTargetName())%></td>
			<td><%=tt.getMapName() == null ? "--" : Arrays.toString(tt.getMapName())%></td>
			<td><%=tt.getX() == null ? "--" : Arrays.toString(tt.getX())%></td>
			<td><%=tt.getY() == null ? "--" : Arrays.toString(tt.getY())%></td>
		</tr>
		<%
			}
		%>
	</table>
	<table border="1">
		<tr>
			<td colspan="5" class="title">任务奖励</td>
		</tr>
		<tr>
			<td>奖励类型</td>
			<td>总数量</td>
			<td>名字</td>
			<td>颜色</td>
			<td>数量</td>
		</tr>
		<%
			for (TaskPrize tp : task.getPrizes()) {
		%>
		<tr>
			<td><%=tp.getPrizeType().getName()%></td>
			<td><%=tp.getTotalNum()%></td>
			<td><%=tp.getPrizeName() == null ? "--" : Arrays.toString(tp.getPrizeName())%></td>
			<td><%=tp.getPrizeColor() == null ? "--" : Arrays.toString(tp.getPrizeColor())%></td>
			<td><%=tp.getPrizeNum() == null ? "--" : Arrays.toString(tp.getPrizeNum())%></td>
		</tr>
		<%
			}
		%>
	</table>
	<%
		/**
			String cssClass = "";
			int count = 0;
			if (task.getTargets() != null) {
				for (TaskTarget tt : task.getTargets()) {
					if (tt == null) {
						continue;
					}
					count++;
					out.print(tt.toHtmlString(count % 2 == 1 ? "target" : "").replace("<table", "<table border='1'"));
				}
			}
			if (task.getPrizes() != null) {
				out.print("任务奖励个数:" + task.getPrizes().length + "</BR>");
				for (TaskPrize tp : task.getPrizes()) {

					count++;
					out.print(tp.toHtmlString(count % 2 == 1 ? "prize" : "").replace("<table", "<table border='1'"));
				}
			}

			StringBuffer sbf = new StringBuffer();
			sbf.append("<BR/>");
			sbf.append("任务目标所在地图" + task.getTargets().length);
			sbf.append("<BR/>");
			sbf.append("<table>");
			for (TaskTarget target : task.getTargets()) {
				for (int i = 0; i < target.getMapName().length; i++) {
					sbf.append("<tr>").append("<td>").append(target.getTargetName()[i]).append("</td><td>").append(target.getMapName()[i]).append("</td><td>").append("[" + target.getX()[i]).append("," + target.getY()[i] + "]").append("</td></tr>");
				}
			}
			sbf.append("</table>");
			out.print(sbf.toString());
		 */
	%>
</body>
</html>