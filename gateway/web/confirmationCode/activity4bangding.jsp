<%@page import="com.xuanzhi.confirmation.bean.Prize"%>
<%@page import="java.util.List"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.xuanzhi.confirmation.service.server.DataManager"%>
<%@page import="com.xuanzhi.confirmation.bean.GameActivity"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%!SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");%>
<%1
	String option = request.getParameter("option");
	GameActivity gameActivity = null;
	if ("add".equals(option)) {
		gameActivity = new GameActivity();
	} else if ("modify".equals(option)) {
		long activeId = Long.valueOf(request.getParameter("activeId"));
		gameActivity = DataManager.getInstance().getCodeStorer().getGameActivity(activeId);
		if (gameActivity == null) {
			out.print("无效的活动ID:" + activeId);
			return;
		}
	} else {
		out.print("无效的参数!!!!");
		return;
	}
	StringBuffer areasSbf = new StringBuffer();
	for (int i = 0; i < gameActivity.getAreas().length - 1; i ++ ) {
		areasSbf.append(gameActivity.getAreas()[i]).append(",");
	}
	if (gameActivity.getAreas().length > 0) {
		areasSbf.append(gameActivity.getAreas()[gameActivity.getAreas().length - 1]);
	}
	StringBuffer prizeSbf = new StringBuffer();
	for (int i = 0; i < gameActivity.getPrizes().length - 1;i ++) {
		prizeSbf.append(gameActivity.getPrizes()[i].toShowString()).append("&");
		
	}
	if (gameActivity.getPrizes().length > 0) {
		prizeSbf.append(gameActivity.getPrizes()[gameActivity.getPrizes().length - 1].toShowString());
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="../css/common.css"/>
<link rel="stylesheet" href="../css/confirmationcode.css"/>
<script language="javascript" type="text/javascript" src="../js/My97DatePicker/WdatePicker.js"></script>
<title>Insert title here</title>
</head>
<body>
<form action="./activityModify4bangding.jsp?option=<%=option%>" method="post">
	<table border="1">
		<tr class="sortable-onload-5-6r rowstyle-alt colstyle-alt no-arrow">
			<td >id</td><td><input name="id" type="text" value="<%=gameActivity.getId()%>"></td>
			<td >名字</td><td><input name="name" type="text" value="<%=gameActivity.getName()%>"></td>
		</tr>
		<tr>
			<td >描述(显示给玩家的)</td><td colspan="3"><textarea name="description" rows="5" cols="30"><%=gameActivity.getDescription()%></textarea></td>
		</tr>
		<tr>
			<td >内部描述(玩家看不到)</td><td colspan="3"><textarea name="innerDescription" rows="5" cols="30"><%=gameActivity.getInnerDescription()%></textarea></td>
		</tr>
		<tr>
			<td >每个账号可领取次数</td><td><input name="eachUserExchangeTimes" type="text" value="<%=gameActivity.getEachUserExchangeTimes()%>"></td>
			<td >是否有效</td><td>有效<input name="isActive" value="0" <%=gameActivity.isActive() ? "checked" : "" %> type="radio">
								 无效:<input name="isActive" value="1" <%=!gameActivity.isActive() ? "checked" : "" %> type="radio">
							</td>
		</tr>
		<tr>
			<td >活动开启时间</td><td><input name="startTime" type="text" value="<%=sdf.format(gameActivity.getStartTime()) %>" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"></td>
			<td >活动结束时间</td><td><input name="endTime" type="text" value="<%=sdf.format(gameActivity.getEndTime()) %>" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"></td>
		</tr>
		<tr>
			<td >开放游戏(没有用不要填)</td>
				<td>
					<input type="text" name="gameId" value="0">
				</td>
			<td >开放游戏区[逗号分隔]</td><td><input name="areas" value="<%=areasSbf.toString()%>" onmouseout="onblur(this,'xxxxx')"></td>
		</tr>
		<tr>
			<td  title="只能发绑定物品">奖励<BR/>例子:升级丹,10,1<br/>[10个颜色是1的绑定升级丹]</td>
			<td colspan="3"><textarea name="prizes" cols="25" rows="5" onblur="checkInput(this,/^\W+,\d+,\d+,(false|true)(&\W+,\d+,\d+,(false|true))*$/)"><%=prizeSbf.toString()%></textarea></td>
		</tr>
		<%if (!"add".equals(option)){%>
		<tr>
			<td >追加激活码</td><td><input name="addCode" type="text" value="0"></td> 
			<td  colspan="2"><a href="./seeCode.jsp?activityId=<%=gameActivity.getId()%>">查看激活码领取详情</a></td> 
		</tr>
			<tr>
				<td >激活码[<%=gameActivity.getAllCodes() == null ? "0" :gameActivity.getAllCodes().size() %>个]</td>
				<td colspan="3">
				<% if(gameActivity.getAllCodes() != null){%>
					<textarea rows="10" cols="80" readonly="readonly">
						<%for (int i = 0;i < gameActivity.getAllCodes().size();i++) {
								out.print(gameActivity.getAllCodes().get(i));
								out.print("\t");
							}%>
					</textarea>
					<%} %>
				</td>
				</tr>
			<%} %>
		<tr>
			<td colspan="4" align="right"><input id="ok" style="display: block;" type="submit" value="提交"></td>
		</tr>
	</table>
</form>
</body>
</html>
<script>
	function checkInput(obj,regular){
		if (true) {
			return;
		}
		var value = obj.value;
		var mach = regular.exec(value);
		if (mach == null) {
			alert("录入格式错误:" + value);
			document.getElementById("ok").style.display="none";
		} else {
			document.getElementById("ok").style.display="block";
		}
		
	}
</script>
