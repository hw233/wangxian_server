<%@page import="com.fy.engineserver.activity.base.ExpAddTuMoTie"%>
<%@page import="com.fy.engineserver.activity.base.ExpAddHeJiu"%>
<%@page import="java.util.Date"%>
<%@page import="com.fy.engineserver.activity.base.ExpAddAbstract"%>
<%@page import="com.fy.engineserver.activity.base.ExpAddManager"%>
<%@page import="java.text.SimpleDateFormat"%>
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
		String action = request.getParameter("action");
		if (action != null) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if ("hejiu".equals(action)) {
				try{
					String activityID = request.getParameter("activityID");
					String startTime = request.getParameter("startTime");
					String endTime = request.getParameter("endTime");
					String addParm = request.getParameter("addParm");
					String addColor = request.getParameter("addColor");
					//喝酒活动
					ExpAddHeJiu base1 = new ExpAddHeJiu(Integer.parseInt(activityID), format.parse(startTime).getTime(), 
							format.parse(endTime).getTime(), ExpAddAbstract.EXP_ADD_TYPE1, Integer.parseInt(addParm), Integer.parseInt(addColor));
					ExpAddManager.instance.addActivity(base1);
				}catch(Exception e) {
					System.out.println(e);
				}
				response.sendRedirect("./ExpAddActivity.jsp");
			}else if ("tuMoTie".equals(action)) {
				try{
					String activityID = request.getParameter("activityID");
					String startTime = request.getParameter("startTime");
					String endTime = request.getParameter("endTime");
					String addParm = request.getParameter("addParm");
					String addColor = request.getParameter("addColor");
					//屠魔贴
					ExpAddTuMoTie base1 = new ExpAddTuMoTie(Integer.parseInt(activityID), format.parse(startTime).getTime(), 
							format.parse(endTime).getTime(), ExpAddAbstract.EXP_ADD_TYPE1, Integer.parseInt(addParm), Integer.parseInt(addColor));
					ExpAddManager.instance.addActivity(base1);
				}catch(Exception e) {
					System.out.println(e);
				}
				response.sendRedirect("./ExpAddActivity.jsp");
			}else if ("removeActivity".equals(action)) {
				try{
					String removeIndex = request.getParameter("index");
					ExpAddManager.instance.expAdds.remove(Integer.parseInt(removeIndex));
				}catch(Exception e) {
					System.out.println(e);
				}
			}
		}
	%>
	
	<table border="1">
		<tr>
			<td>活动ID </td>
			<td>开启时间 </td>
			<td>结束时间 </td>
			<td>reason </td>
			<td>addType </td>
			<td>addParm </td>
			<td>参数 </td>
		</tr>
	<%
		for (int i = 0; i < ExpAddManager.instance.expAdds.size(); i++) {
			ExpAddAbstract expAdd = ExpAddManager.instance.expAdds.get(i);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String startTime = format.format(new Date(expAdd.startTime));
			String endTime = format.format(new Date(expAdd.endTime));
			
			%>
			<tr>
				<td><%=expAdd.activityId %></td>
				<td><%=startTime %></td>
				<td><%=endTime %></td>
				<td><%=expAdd.addReason %></td>
				<td><%=expAdd.addType %></td>
				<td><%=expAdd.addParameter %></td>
				<td><%=expAdd.getParmeter() %></td>
			</tr>
			<%
		}
	%>
	</table>
	
	<form name="f1">
		<input type="hidden" name="action" value="hejiu">
		活动ID<input type="text" name="activityID">
		开始时间<input type="text" name="startTime" value="2013-01-28 20:00:00">
		结束时间<input type="text" name="endTime" value="2013-01-28 20:00:00">
		参数<input type="text" name="addParm">
		颜色<input type="text" name="addColor">
		<input type="submit" value="喝酒">
	</form>
	
	<br>
	<br>
	
	<form name="f2">
		<input type="hidden" name="action" value="tuMoTie">
		活动ID<input type="text" name="activityID">
		开始时间<input type="text" name="startTime" value="2013-01-28 20:00:00">
		结束时间<input type="text" name="endTime" value="2013-01-28 20:00:00">
		参数<input type="text" name="addParm">
		颜色<input type="text" name="addColor">
		<input type="submit" value="屠魔贴">
	</form>
	
	<form name="f3">
		<input type="hidden" name="action" value="removeActivity">
		位置Index<input type="text" name="index">
		<input type="submit" value="删除活动">
	</form>
	
</body>
</html>
