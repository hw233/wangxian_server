<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.economic.BillingCenter"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.activity.monsterDrop.MonsterDropInfo"%>
<%@page import="com.fy.engineserver.activity.monsterDrop.MonsterDropActivityManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.newtask.service.TaskManager"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.newtask.Task"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
		MonsterDropActivityManager manager = MonsterDropActivityManager.getInstance();
	manager.DROP_RANDOM = 500000000;
	{
		List<Task> list = TaskManager.getInstance().getTaskGroupByGroupName("屠魔帖");
		if (list != null) {
			for (Task task : list) {
				if (task != null) {
					task.setDailyTaskMaxNum(15);
					out.print("任务设置每天完成次数OK[任务:"+task.getName()+"][次数:"+task.getDailyTaskMaxNum()+"] <br/>" );
				}
			}
		} else {
			out.print("任务组不存在");
		}
	}
	if (manager.isActivityStart && manager.isOpen) {
	%>
		活动正在开始
		<br>
	<%
	}else {
	%>
		活动未开启
		<br>
	<%
	}
	%>
	<%
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		if (startTime != null && endTime != null) {
			boolean isSet = manager.setStartEndTime(startTime, endTime);
			if (!isSet) {
	%>
	<%
			}
			response.sendRedirect("./MonsterDropActivity.jsp");
		}
		String runNum = request.getParameter("runNum");
		String runPlayer = request.getParameter("runPlayer");
		if (runNum != null && runPlayer != null) {
			Player player = null;
			try{
				player = PlayerManager.getInstance().getPlayer(runPlayer);
			}catch (Exception e) {
				e.printStackTrace();
			}
			if (player != null) {
				int run = Integer.parseInt(runNum);
				for (int i = 0; i < run; i++) {
					manager.doDrop(player);
				}
			}
			response.sendRedirect("./MonsterDropActivity.jsp");
		}
		String action = request.getParameter("action");
		if (action != null && "start_end".equals(action)) {
			manager.isOpen = !manager.isOpen;
			response.sendRedirect("./MonsterDropActivity.jsp");
		}else if (action != null && "flush".equals(action)) {
			manager.flushDropNum();
			response.sendRedirect("./MonsterDropActivity.jsp");
		}
	%>
	当前开始时间:<%=manager.ACTIVITY_START_TIME %> 结束时间:<%=manager.ACTIVITY_END_TIME %>,基数:<%=manager.DROP_RANDOM %>
	
	<table border="1">
		<tr>
			<td>二级分类 </td>
			<td>名字 </td>
			<td>颜色 </td>
			<td>绑定 </td>
			<td>个数 </td>
			<td>掉落几率 </td>
			<td>最小等级 </td>
			<td>最大等级 </td>
			<td>已经掉落个数 </td>
		</tr>
		<%
			for (int i = 0; i < manager.getBaseInfos().size(); i++) {
				int dropNum = manager.getDropNums().get(i);
				MonsterDropInfo info = manager.getBaseInfos().get(i);
		%>
			<tr>
				<td><%=info.getErjiName() %></td>
				<td><%=info.getPropName() %> </td>
				<td><%=info.getColorType() %> </td>
				<td><%=info.isBind() %> </td>
				<td><%=info.getNum() %> </td>
				<td><%=info.getDropRandom() %> </td>
				<td><%=info.getDropLevelMin() %> </td>
				<td><%=info.getDropLevelMax() %> </td>
				<td><%=dropNum %> </td>
			</tr>
		<%
			}
		%>
	</table>
	<%
		for (int i = 0; i < manager.dropLog.size(); i++) {
	%>
		<%=manager.dropLog.get(i) %>
		<br>
	<%
		}
	%>
	<form id="f2" name="f2" method="get">
		开始时间<input name="startTime" value="<%=manager.ACTIVITY_START_TIME %>">结束时间<input name="endTime" value="<%=manager.ACTIVITY_END_TIME %>"><input type="submit" value="修改开始结束时间">
	</form>
	<form id="f3" = name="f3" method="get">
		玩家名字<input name="runPlayer">运行次数<input name="runNum" value="20000"><input type="submit" value="运行">
	</form>
	<form name="f4">
		<input type="hidden" name="action" value="start_end">
		<input type="submit" value=<%=manager.isOpen ? "关闭活动" : "开启活动" %>>
	</form>
	<form name="f5">
		<input type="hidden" name="action" value="flush">
		<input type="submit" value="刷新掉落">
	</form>
</body>
</html>
