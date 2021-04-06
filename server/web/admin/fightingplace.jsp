<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,
com.fy.engineserver.sprite.*,com.fy.engineserver.core.*,com.xuanzhi.tools.transport.*"%><% 
	
	FightingPlaceManager fpm = FightingPlaceManager.getInstance();
	FightingPlace fp[] = fpm.getFightingPlaces();


	if(request.getParameter("action") != null &&
			request.getParameter("action").equals("settime")){
		int k = Integer.parseInt(request.getParameter("k"));
		int l = Integer.parseInt(request.getParameter("level"));
		Date startTime =  DateUtil.parseDate(request.getParameter("starttime"),"yyyy-MM-dd HH:mm:ss");
		Date endTime =  DateUtil.parseDate(request.getParameter("endtime"),"yyyy-MM-dd HH:mm:ss");
		
		fp[k].setMinPlayerLevel(l);
		fp[k].reset(startTime.getTime(),endTime.getTime());
	}
	
	%>
<%@include file="IPManager.jsp" %><html><head>
</HEAD>
<BODY>
<h2>各个战场的情况</h2><br><a href="./fightingplace.jsp">刷新此页面</a><br>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center">
<td>战场</td>
<td>地图名称</td>
<td>是否开放</td>
<td>紫薇宫人数</td>
<td>日月盟人数</td>
<td>紫薇宫复活区域</td>
<td>日月盟复活区域</td>
<td>紫薇宫主将</td>
<td>日月盟主将</td>
<td>级别限制</td>
<td>开始时间</td>
<td>结束时间</td>
<td>修改</td>
</tr>	
<%
	for(int i = 0 ; i < fp.length ; i++){
		Player ps[] = fp[i].getPlayers();
		int c1 = 0;
		int c2 = 0;
		for(int j = 0 ; j < ps.length ; j++){
			
			if(ps[j].getCountry() == 1){
				c1 ++;
			}else{
				c2 ++;
			}
		}
		out.println("<form id='"+i+"'><input type='hidden' name='action' value='settime'><input type='hidden' name='k' value='"+i+"'");
		out.println("<tr bgcolor='#FFFFFF' align='center'>");
		out.println("<td>"+fp[i].getName()+"</td><td>"+fp[i].getGame().gi.getName()+"</td>");
		out.println("<td>"+fp[i].isOpen()+"</td><td>"+c1+"</td><td>"+c2+"</td><td>"+fp[i].getResurrectionRegion_紫薇宫()+"</td>");
		out.println("<td>"+fp[i].getResurrectionRegion_日月盟()+"</td><td>"+fp[i].getGeneral_紫薇宫()+"</td>");
		out.println("<td>"+fp[i].getGeneral_日月盟()+"</td><td><input type='text' name='level' value='"+fp[i].getMinPlayerLevel()+"'></td>");
		out.println("<td><input type='text' name='starttime' value='"+DateUtil.formatDate(new Date(fp[i].getStartTime()),"yyyy-MM-dd HH:mm:ss")+"'></td>");
		out.println("<td><input type='text' name='endtime' value='"+DateUtil.formatDate(new Date(fp[i].getEndTime()),"yyyy-MM-dd HH:mm:ss")+"'></td>");
		out.println("<td><input type='submit' value='修 改'></td>");
		out.println("</tr>");
		out.println("</from>");
	}
%>

</table>	
</BODY></html>
