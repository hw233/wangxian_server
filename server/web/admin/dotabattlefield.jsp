<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,
com.fy.engineserver.sprite.*,
com.fy.engineserver.core.*,
com.xuanzhi.tools.transport.*,
com.fy.engineserver.battlefield.*
"%><% 
	
	DotaBattleFieldManager manager = DotaBattleFieldManager.getInstance();
	



	if(request.getParameter("action") != null &&
			request.getParameter("action").equals("settime")){
		
		String name = request.getParameter("bname");
		DotaBattleField df = manager.getDotaBattleField(name);
		Date startTime =  DateUtil.parseDate(request.getParameter("starttime"),"yyyy-MM-dd HH:mm:ss");
		Date endTime =  DateUtil.parseDate(request.getParameter("endtime"),"yyyy-MM-dd HH:mm:ss");
		
		df.reset(startTime.getTime(),endTime.getTime());
	}else if(request.getParameter("action") != null &&
			request.getParameter("action").equals("create")){
		String name = request.getParameter("ddname");
		Date startTime =  DateUtil.parseDate(request.getParameter("starttime"),"yyyy-MM-dd HH:mm:ss");
		Date endTime =  DateUtil.parseDate(request.getParameter("endtime"),"yyyy-MM-dd HH:mm:ss");
		int type = Integer.parseInt(request.getParameter("type"));
		int minlevel = Integer.parseInt(request.getParameter("minlevel"));
		int maxlevel = Integer.parseInt(request.getParameter("maxlevel"));
		
		manager.createDotaBattleField(name,type,startTime.getTime(),endTime.getTime(),minlevel,maxlevel);
	}
	
	DotaBattleField fp[] = manager.getAllDotaBattleField();
	
	%>
<%@include file="IPManager.jsp" %><html><head>
</HEAD>
<BODY>
<h2>各个战场的情况</h2><br><a href="./dotabattlefield.jsp">刷新此页面</a><br>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center">
<td>战场</td>
<td>战场类型</td>
<td>地图名称</td>
<td>是否开放</td>
<td>禁卫军团</td>
<td>天灾军团</td>
<td>级别限制</td>
<td>开始时间</td>
<td>结束时间</td>
<td>修改</td>
</tr>	
<%
	for(int i = 0 ; i < fp.length ; i++){
		Player ps[] = fp[i].getPlayersBySide(DotaBattleField.BATTLE_SIDE_A);
		int c1 = ps.length;
		ps = fp[i].getPlayersBySide(DotaBattleField.BATTLE_SIDE_B);
		int c2 = ps.length;

		out.println("<form id='"+i+"'><input type='hidden' name='action' value='settime'><input type='hidden' name='bname' value='"+fp[i].getName()+"'>");
		out.println("<tr bgcolor='#FFFFFF' align='center'>");
		out.println("<td>"+fp[i].getName()+"</td><td>"+DotaBattleField.PLAYER_NUM_LIMIT[fp[i].getBattleType()]+"v"+DotaBattleField.PLAYER_NUM_LIMIT[fp[i].getBattleType()]+"</td><td>"+fp[i].getGame().gi.getName()+"</td>");
		out.println("<td>"+fp[i].isOpen()+"</td><td>"+c1+"</td><td>"+c2+"</td>");
		out.println("");
		out.println("<td>"+fp[i].getMinPlayerLevel()+"~"+fp[i].getMaxPlayerLevel()+"</td>");
		out.println("<td><input type='text' name='starttime' value='"+DateUtil.formatDate(new Date(fp[i].getStartTime()),"yyyy-MM-dd HH:mm:ss")+"'></td>");
		out.println("<td><input type='text' name='endtime' value='"+DateUtil.formatDate(new Date(fp[i].getEndTime()),"yyyy-MM-dd HH:mm:ss")+"'></td>");
		out.println("<td><input type='submit' value='修 改'></td>");
		out.println("</tr>");
		out.println("</form>");
	}
%>
</table>
<br>
创建一个新的战场
<br>	
<form id='1234'><input type='hidden' name='action' value='create'>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000">
<tr bgcolor="#FFFFFF" ><td>请输入战场的名称</td><td><input type='text' name='ddname' size='10' value=""></td></tr>
<tr bgcolor="#FFFFFF" ><td>请选择战场的类型</td><td><select name='type'><option value='0'>5v5</option><option value='1'>10v10</option><option value='2'>15v15</option><option value='3'>20v20</option></select></td></tr>
<tr bgcolor="#FFFFFF" ><td>请输入玩家最小等级</td><td><input type='text' name='minlevel' size='10' value='30'></td></tr>
<tr bgcolor="#FFFFFF" ><td>请输入玩家最大等级</td><td><input type='text' name='maxlevel' size='10' value='50'></td></tr>
<tr bgcolor="#FFFFFF" ><td>请输入战场的名称</td><td><input type='text' name='starttime' value='<%= DateUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss")%>'></td></tr>
<tr bgcolor="#FFFFFF" ><td>请输入战场的名称</td><td><input type='text' name='endtime' value='<%= DateUtil.formatDate(new Date(System.currentTimeMillis() + 3600 * 1000),"yyyy-MM-dd HH:mm:ss")%>'></td></tr>
<tr bgcolor="#FFFFFF" ><td></td><td><input type='submit' value='创 建'></td></tr>

</table>
</form>
</BODY></html>
