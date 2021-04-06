<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.confirm.bean.Prize"%>
<%@page import="java.util.List"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.fy.confirm.service.server.DataManager"%>
<%@page import="com.fy.confirm.bean.GameActivity"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%!SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");%>
<%
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
<form action="./new_ActivityModify4bangding.jsp?option=<%=option%>" method="post">
	<table border="1">
		<tr class="sortable-onload-5-6r rowstyle-alt colstyle-alt no-arrow">
			<td >id(唯一)</td><td><input name="id" type="text" value="<%=gameActivity.getId()%>" readonly></td>
			<td >名字</td><td><input name="name" type="text" value="<%=gameActivity.getName()%>"></td>
		</tr>
		<tr>
			<td >激活码级别</td>
			<td>免费<input name="functionType" value="1" <%=gameActivity.getFunctionLevel()==1 ? "checked" : "" %> type="radio">
				福利:<input name="functionType" value="2" <%=gameActivity.getFunctionLevel()==2 ? "checked" : "" %> type="radio">
				特权:<input name="functionType" value="3" <%=gameActivity.getFunctionLevel()==3 ? "checked" : "" %> type="radio">
				收费:<input name="functionType" value="4" <%=gameActivity.getFunctionLevel()==4 ? "checked" : "" %> type="radio">
			</td>
			<td bgcolor="red">请每次都确认好级别</td>
		</tr>
		<tr>
			<td >描述(显示给玩家的)</td><td colspan="3"><textarea name="description" rows="5" cols="45"><%=gameActivity.getDescription()%></textarea></td>
		</tr>
		<tr>
			<td >内部描述(玩家看不到)</td><td colspan="3"><textarea name="innerDescription" rows="5" cols="45"><%=gameActivity.getInnerDescription()%></textarea></td>
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
		<!--  
		<tr>
			<td >生效服务器(没有限制不要填)</td>
				<td>
					<input type="text" name="gameId" value="0">
				</td>
		</tr>
		-->
		<tr>
			<td  title="名字,数量,颜色,是否绑定&名字,数量,颜色,是否绑定">奖励<BR/>例子:升级丹,10,1,true&境界丹,12,3,false<br/>[10个颜色是1的绑定升级丹 和 12个颜色是3的不绑定境界丹]</td>
			<td colspan="3"><textarea name="prizes" cols="45" rows="5" onblur="checkInput(this,/^\W+,\d+,\d+,(false|true)(&\W+,\d+,\d+,(false|true))*$/)"><%=prizeSbf.toString()%></textarea></td>
		</tr>
		<tr>
			<td >追加激活码</td><td><input name="addCode" type="text" value="0"></td> 
		</tr>
		<tr>
			<td >激活码</td>
			<td colspan="3">[<%=gameActivity.getAllCodes() == null ? "0" :gameActivity.getAllCodes().size() %>个]</td>
		</tr>
		<tr>
			<td>邮件提示</td><td colspan="2"><input type="text" name="mailNotice"></td>
			<td align="right"><input id="ok" style="display: block;" type="submit" value="提交"></td>
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
