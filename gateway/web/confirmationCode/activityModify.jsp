<%@page import="com.fy.confirm.bean.Prize"%>
<%@page import="com.fy.confirm.service.server.DataManager"%>
<%@page import="com.fy.confirm.bean.GameActivity"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%!SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");%>
<%
	long id = Long.valueOf(request.getParameter("id"));
	String name = request.getParameter("name");
	String description = request.getParameter("description");
	String innerDescription = request.getParameter("innerDescription");
	int eachUserExchangeTimes = Integer.valueOf(request.getParameter("eachUserExchangeTimes"));
	int functionType = Integer.valueOf(request.getParameter("functionType"));
	boolean isActive = "0".equals(request.getParameter("isActive"));
	long startTime = (sdf.parse(request.getParameter("startTime"))).getTime();
	long endTime = (sdf.parse(request.getParameter("endTime"))).getTime();
	long gameId = Long.valueOf(request.getParameter("gameId"));
	String[] areasStr = request.getParameter("areas").split(",");
	String[] prizesStr = request.getParameter("prizes").split("&");
	Prize[] prizes = new Prize[prizesStr.length];
	for (int i = 0; i < prizesStr.length; i++) {
		String[] values = prizesStr[i].split(",");
		prizes[i] = new Prize(values[0], Long.valueOf(values[1]), Integer.valueOf(values[2]), "true".equals(values[3]));
	}

	String option = request.getParameter("option");

	DataManager dataManager = DataManager.getInstance();
	GameActivity gameActivity = null;
	if ("add".equalsIgnoreCase(option)) {//增加一个
		gameActivity = new GameActivity();
		gameActivity.setId(id);
		gameActivity.setCreateTime(System.currentTimeMillis());
		gameActivity.setActive(true);
	} else if ("modify".equalsIgnoreCase(option)) {//更新
		gameActivity = dataManager.getCodeStorer().getGameActivity(id);
		if (gameActivity == null) {
			out.print("无效的活动:" + id);
			return;
		}
	} else {
		out.print("无效的参数:" + option);
		return;
	}
	gameActivity.setName(name);
	gameActivity.setDescription(description);
	gameActivity.setInnerDescription(innerDescription);
	gameActivity.setEachUserExchangeTimes(eachUserExchangeTimes);
	gameActivity.setActive(isActive);
	gameActivity.setStartTime(startTime);
	gameActivity.setEndTime(endTime);
	gameActivity.setGameId(gameId);
	gameActivity.setAreas(areasStr);
	gameActivity.setPrizes(prizes);
	gameActivity.setFunctionLevel(functionType);
	if ("add".equalsIgnoreCase(option)) {//增加一个
		dataManager.getCodeStorer().insertGameActivity(gameActivity);
	} else if ("modify".equalsIgnoreCase(option)) {//更新
		String addCode = request.getParameter("addCode");
		if (addCode != null) {
			int addNum = Integer.valueOf(addCode);
			dataManager.createCode(gameActivity, addNum);
		}

		dataManager.getCodeStorer().modifyGameActivity(gameActivity);
	}
	response.sendRedirect("./activityList.jsp");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>