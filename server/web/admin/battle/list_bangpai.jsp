<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="
java.util.*,
com.fy.engineserver.battlefield.*,
com.fy.engineserver.battlefield.concrete.* " %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.gang.model.Gang"%>
<%@include file="../IPManager.jsp" %><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"> 
<title>帮战战场</title>
<% 
	BangPaiBattleManager bpbm = BangPaiBattleManager.getInstance();

	BangPaiBattleManager.BangPaiBattleConfig cs[] = bpbm.getBangPaiBattleConfigs();
	
	boolean modify = false;
	BangPaiBattleManager.BangPaiBattleConfig modifyC = null;
	int modifyId = -1;
	if(request.getParameter("action") != null && request.getParameter("action").equals("modify")){
		modify = true;
		modifyId = Integer.parseInt(request.getParameter("id"));
		modifyC = cs[modifyId];
	}
	
	if(request.getParameter("action") != null && request.getParameter("action").equals("modified")){
		int id = Integer.parseInt(request.getParameter("id"));
		modifyC = cs[id];
		modifyC.dayInWeek = Integer.parseInt(request.getParameter("dayInWeek"));
		modifyC.applyStartMinute = Integer.parseInt(request.getParameter("applyStartHour"));
		modifyC.applyEndMinute = Integer.parseInt(request.getParameter("applyEndHour"));
		modifyC.battleStartMinute = Integer.parseInt(request.getParameter("battleStartHour"));
		modifyC.mode = Integer.parseInt(request.getParameter("mode"));
		bpbm.setLastBattleItemListCreateFlag(false);
		
		modifyC.money = Integer.parseInt(request.getParameter("money"));
		modifyC.mapName = request.getParameter("mapName");
	}
%>
</head>
<body>
<h2>帮战情况：</h2>
<a href="./list_bangpai.jsp">刷新此页面</a>
<br>
<br>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#C2CAF5" align="center">
<td>编号</td>
<td>日期</td>
<td>开始报名时间</td>
<td>结束报名时间</td>
<td>战场开始时间</td>
<td>战场持续时间</td>
<td>模式</td>
<td>报名费用</td>
<td>地图名称</td>
<td>操作</td>
</tr>

<%
	String modeName[] = new String[]{"系统匹配","帮派约战"};

	String dayNames[] = new String[]{"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};
	for(int i = 0 ; i < cs.length ; i++){
		BangPaiBattleManager.BangPaiBattleConfig c = cs[i];
		Calendar cal = Calendar.getInstance();
		out.println("<tr bgcolor='#FFFFFF' align='center'>");
		out.println("<td>"+(i+1)+"</td>");
		
		out.println("<td>"+dayNames[c.dayInWeek-1]+"</td>");
		out.println("<td>"+(c.applyStartMinute/60)+":"+(c.applyStartMinute % 60)+"</td>");
		out.println("<td>"+(c.applyEndMinute/60)+":"+(c.applyEndMinute % 60)+"</td>");
		out.println("<td>"+(c.battleStartMinute/60)+":"+(c.battleStartMinute % 60)+"</td>");
		out.println("<td>2小时</td>");
		out.println("<td>"+modeName[c.mode]+"</td>");
		out.println("<td>"+c.money+"</td>");
		out.println("<td>"+c.mapName+"</td>");
		out.println("<td><a href='./list_bangpai.jsp?action=modify&id="+i+"'>修改</a></td>");
		out.println("<tr>");
	}
%>
</table>

<%
	if(modify && modifyC != null){
%>
	<form id='fff'><input type='hidden' name='action' value='modified'>
	<input type='hidden' name='id' value='<%=modifyId %>'>
	<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
	<tr align="center" bgcolor='#ffffff'><td>请选择日期</td><td><select name='dayInWeek'>
	<%
		for(int i = 0 ; i < dayNames.length ; i++){
			out.println("<option value='"+(i+1)+"' "+(modifyC.dayInWeek==i+1?"selected":"")+">"+dayNames[i]+"</option>");
		}
	%>
	</select></td></tr>
	
	<tr align="center" bgcolor='#ffffff'><td>请输入报名开始时间(分钟)</td><td><input type='text' name='applyStartHour' value='<%= modifyC.applyStartMinute %>'></td></tr>
	<tr align="center" bgcolor='#ffffff'><td>请输入报名结束时间(分钟)</td><td><input type='text' name='applyEndHour' value='<%= modifyC.applyEndMinute %>'></td></tr>
	<tr align="center" bgcolor='#ffffff'><td>请输入帮战开始时间(分钟)</td><td><input type='text' name='battleStartHour' value='<%= modifyC.battleStartMinute %>'></td></tr>
	<tr align="center" bgcolor='#ffffff'><td>请输入报名资金</td><td><input type='text' name='money' value='<%= modifyC.money %>'></td></tr>
	<tr align="center" bgcolor='#ffffff'><td>请输入帮战地图</td><td><input type='text' name='mapName' value='<%= modifyC.mapName %>'></td></tr>
	<tr align="center" bgcolor='#ffffff'><td>请输入模式（0标识系统匹配，1标识帮派约战）</td><td><input type='text' name='mode' value='<%= modifyC.mode %>'></td></tr>
	<tr align="center" bgcolor='#ffffff'><td colspan=2><input type='submit'  value='提交'></td></tr>
	</table>
	</form>
<% 		
	}
%>

<br/>
<h3>报名情况</h3>
<br/>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#C2CAF5" align="center">
<td>编号</td>
<td>帮派名称</td>
<td>帮派等级</td>
<td>帮派人数</td>
</tr>
<%

Gang gangs[] = bpbm.getApplyGangs();

for(int i = 0 ; i < gangs.length ; i++){
	out.println("<tr bgcolor='#FFFFFF' align='center'>");
	out.println("<td>"+(i+1)+"</td>");
	out.println("<td>"+gangs[i].getName()+"</td>");
	out.println("<td>"+gangs[i].getGanglevel()+"</td>");
	out.println("<td>"+gangs[i].getMemberCount()+"</td>");
	out.println("</tr>");
}
%>
</table>


<h3>约战情况</h3>
<br/>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#C2CAF5" align="center">
<td>帮派A</td>
<td>帮派A等级</td>
<td>帮派A人数</td>
<td>帮派B</td>
<td>帮派B等级</td>
<td>帮派B人数</td>
<td>赌注</td>
</tr>
<%

ArrayList<BangPaiBattleManager.BangPaiYueZhanItem> list = bpbm.getYueZhanList();

for( BangPaiBattleManager.BangPaiYueZhanItem item :list){
	out.println("<tr bgcolor='#FFFFFF' align='center'>");
	out.println("<td>"+item.gangA.getName()+"</td>");
	out.println("<td>"+item.gangA.getGanglevel()+"</td>");
	out.println("<td>"+item.gangA.getMemberCount()+"</td>");
	out.println("<td>"+item.gangB.getName()+"</td>");
	out.println("<td>"+item.gangB.getGanglevel()+"</td>");
	out.println("<td>"+item.gangB.getMemberCount()+"</td>");
	out.println("<td>"+item.money+"</td>");
	out.println("</tr>");
}
%>
</table>

<h3>配对情况</h3>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#C2CAF5" align="center">

<td>编号</td>

<td>帮派A</td>
<td>帮派B</td>
<td>模式</td>
<td>赌注</td>
<td>战场名</td>
<td>战场情况</td>
</tr>
<%
BangPaiBattleManager.BangPaiBattleItem items[] = bpbm.getBangPaiBattleItems();
for(int i = 0 ; i < items.length ; i++){
	out.println("<tr bgcolor='#FFFFFF' align='center'>");
	out.println("<td>"+(i+1)+"</td>");
	out.println("<td>"+items[i].gangA.getName()+"</td>");
	out.println("<td>"+items[i].gangB.getName()+"</td>");
	out.println("<td>"+modeName[items[i].mode]+"</td>");
	out.println("<td>"+items[i].money+"</td>");
	out.println("<td>"+items[i].bi.getName() +"</td>");
	out.println("<td>"+items[i].battleField.getBattleFieldSituation() +"</td>");
	
	out.println("</tr>");
}


%></table>
</body>
</html>
