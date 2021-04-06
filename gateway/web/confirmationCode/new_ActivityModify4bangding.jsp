<%@page import="com.fy.confirm.bean.ConfirmationCode"%>
<%@page import="java.util.List"%>
<%@page import="java.util.regex.Pattern"%>
<%@page import="com.fy.confirm.bean.Prize"%>
<%@page import="com.fy.confirm.service.server.DataManager"%>
<%@page import="com.fy.confirm.bean.GameActivity"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="./inc.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%!SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");%>
<%
	String oldMsg = null;
	String newMsg = null;

	String ip = request.getRemoteAddr();
	String recorder = "";
	Object o = session.getAttribute("authorize.username");
	if (o != null) {
		recorder = o.toString();
	}else{
		out.print("请重新登录");
		return;
	}

	String userInfo = "[" + recorder + "] [ip:" + ip + "] [time:"+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) +"]";
	long id = Long.valueOf(request.getParameter("id"));
	String name = request.getParameter("name");
	String mailNotice = request.getParameter("mailNotice");
	String description = request.getParameter("description");
	String innerDescription = request.getParameter("innerDescription");
	int eachUserExchangeTimes = Integer.valueOf(request.getParameter("eachUserExchangeTimes"));
	boolean isActive = "0".equals(request.getParameter("isActive"));
	long startTime = (sdf.parse(request.getParameter("startTime"))).getTime();
	long endTime = (sdf.parse(request.getParameter("endTime"))).getTime();
	//long gameId = Long.valueOf(request.getParameter("gameId"));
	int functionType = Integer.valueOf(request.getParameter("functionType"));
	
	if(functionType != 1 && functionType!= 2 && functionType != 3 && functionType != 4){
		out.print("无效的级别:" + functionType);
		return;
	}
	
	String[] prizesStr = request.getParameter("prizes").split("&");
	Prize[] prizes = new Prize[prizesStr.length];
	for (int i = 0; i < prizesStr.length; i++) {
		String[] values = prizesStr[i].split(",");
		prizes[i] = new Prize(values[0], Long.valueOf(values[1]), Integer.valueOf(values[2]), true);
	}

	String option = request.getParameter("option");

	DataManager dataManager = DataManager.getInstance();
	GameActivity gameActivity = null;
	if ("add".equalsIgnoreCase(option)) {//增加一个
		gameActivity = new GameActivity();
		gameActivity.setId(GmActionManager.getInstance().em.nextId());
		gameActivity.setCreateTime(System.currentTimeMillis());
		gameActivity.setActive(true);
	} else if ("modify".equalsIgnoreCase(option)) {//更新
		gameActivity = dataManager.getCodeStorer().getGameActivity(id);
		if (gameActivity == null) {
			out.print("无效的活动:" + id);
			return;
		}
		oldMsg = getCodeInfo(gameActivity);
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
	//gameActivity.setGameId(gameId);
	gameActivity.setPrizes(prizes);
	gameActivity.setFunctionLevel(functionType);

	if ("add".equalsIgnoreCase(option)) {//增加一个
		dataManager.getCodeStorer().insertGameActivity(gameActivity);
	} else if ("modify".equalsIgnoreCase(option)) {//更新
		String addCode = request.getParameter("addCode");
		if (addCode != null) {
			int addNum = Integer.valueOf(addCode);
			List<ConfirmationCode> list = dataManager.createCode(gameActivity, addNum);
			if (list != null && list.size() > 0) {
				//给接受者发邮件
				sendToReceiver(list, gameActivity ,mailNotice);	
			}
		}
		dataManager.getCodeStorer().modifyGameActivity(gameActivity);
	}
	newMsg = getCodeInfo(gameActivity);
	String title = "";
	StringBuffer showContent = new StringBuffer();
	if ("add".equalsIgnoreCase(option)) {
		title = "[增加激活码:" + gameActivity.getId() + "]";
	} else {
		title = "[修改激活码:" + gameActivity.getId() + "]";
	}
	showContent.append("<table style='font-size:12px;'>");
	showContent.append("<tr><td>ID</td><td>活动名</td><td>开始时间</td><td>结束时间</td><td>奖励</td><td>账号兑换次数</td><td>激活码数量</td><td>服务器列表</td></tr>");
	showContent.append(oldMsg);
	showContent.append(newMsg);
	showContent.append("</table>");
	showContent.append("<BR/>");
	showContent.append(userInfo);
	sendMail(title, showContent.toString());
	response.sendRedirect("./new_ActivityList4bangding.jsp");
%>
<%!static String getCodeInfo(GameActivity gameActivity) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		StringBuffer sbf = new StringBuffer();
		sbf.append("<tr>");
		sbf.append("<td>");
		sbf.append(gameActivity.getId());
		sbf.append("</td>");
		sbf.append("<td>");
		sbf.append(gameActivity.getName());
		sbf.append("</td>");
		sbf.append("<td>");
		sbf.append(sdf.format(new Date(gameActivity.getStartTime())));
		sbf.append("</td>");
		sbf.append("<td>");
		sbf.append(sdf.format(new Date(gameActivity.getEndTime())));
		sbf.append("</td>");
		sbf.append("<td>");
		Prize[] prizes = gameActivity.getPrizes();
		for (Prize prize : prizes) {
			sbf.append(prize.toString());
			sbf.append("<br/>");
		}
		sbf.append("</td>");
		sbf.append("<td>");
		sbf.append(gameActivity.getEachUserExchangeTimes());
		sbf.append("</td>");
		sbf.append("<td>");
		sbf.append(gameActivity.getAllCodes().size());
		sbf.append("</td>");
		sbf.append("</tr>");
		return sbf.toString();
	}%>
<%!
	void sendToReceiver(List<ConfirmationCode> codeList, GameActivity gameActivity ,String mailNotice) {
		StringBuffer showContent = new StringBuffer();
		int conls = 15;
		boolean mailNoticeIsEmpty = mailNotice == null || "".equals(mailNotice.trim());
		String title = (mailNoticeIsEmpty ? "" : mailNotice + ":") +gameActivity.getName() + "[" + codeList.size() + "个]";
		
		showContent.append("<hr>");
		if (!mailNoticeIsEmpty) {
			showContent.append("<h2 style='color=red;'>"+mailNotice+"<h2>");
		}
		showContent.append("<h2>" + gameActivity.getName() + "</h2>");
		showContent.append("<table border=1 style='text-align:center;font-size:12px;'>");
		showContent.append("<tr style='background-color:red;color:#ffffff;'>");
			showContent.append("<th>");
			showContent.append("物品名称");
			showContent.append("</th>");
			showContent.append("<th>");
			showContent.append("物品颜色");
			showContent.append("</th>");
			showContent.append("<th>");
			showContent.append("物品数量");
			showContent.append("</th>");
		showContent.append("</tr>");
		
		for (Prize prize : gameActivity.getPrizes()) {
			showContent.append("<tr>");
				showContent.append("<td>");
				showContent.append(prize.getPropName());
				showContent.append("</td>");
				showContent.append("<td>");
				showContent.append(getColor(prize.getColor()));
				showContent.append("</td>");
				showContent.append("<td>");
				showContent.append(prize.getNum());
				showContent.append("</td>");
			showContent.append("</tr>");
		}
		showContent.append("</table>");
		showContent.append("<hr>");
		showContent.append("<table border=1 style='text-align:center;font-size:6px;'>");
		for (int i = 0 ; i < codeList.size(); i++) {
			showContent.append("<tr>");
			showContent.append("<td>");
			showContent.append(codeList.get(i).getCode());
			showContent.append("</td>");
			showContent.append("</tr>");
		
		}
		
		showContent.append("</table>");
		sendMail(title, showContent.toString());
}
%>
<%!
	String getColor(int colorType){
		String color = "未知";
		switch(colorType){
			case 0:
				color = "白色";
				break;
			case 1:
				color = "绿色";
				break;
			case 2:
				color = "蓝色";
				break;
			case 3:
				color = "紫色";
				break;
			case 4:
				color = "橙色";
				break;
		}
		return color;
	}
%>

<%@page import="com.fy.gamegateway.message.GameMessageFactory"%>
<%@page import="com.fy.gamegateway.gmaction.GmActionManager"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>