<%@ page import="java.util.*" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@page import="com.fy.engineserver.gang.service.GangManager"%>
<%@page import="com.fy.engineserver.gang.model.Gang"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String pn=request.getParameter("pageNum");
int currentPage=0;
int lines=50;
if(pn!=null&&pn.length()>0){
	currentPage=Integer.parseInt(pn);
}
GangManager gm=GangManager.getInstance();
List<Gang> gangs=gm.getSortedGangs(currentPage*lines,lines);
ActivityBuffItemManager abim = ActivityBuffItemManager.getInstance();
if(abim != null){
	try{
    String bangPaiIdStr = request.getParameter("bangPaiId");
    String buffName = request.getParameter("buffName");
    String des = request.getParameter("des");
    String timeType = request.getParameter("timeType");
    String weekDay = request.getParameter("weekDay");
    String fixDay = request.getParameter("fixDay");
    String fixDayRange = request.getParameter("fixDayRange");
    String startTimeInDay = request.getParameter("startTimeInDay");
    String endTimeInDay = request.getParameter("endTimeInDay");
    if(bangPaiIdStr != null && !bangPaiIdStr.trim().equals("") && buffName != null && !buffName.trim().equals("") && startTimeInDay != null && !startTimeInDay.trim().equals("") && endTimeInDay != null && !endTimeInDay.trim().equals("")){
    	ActivityBuffBangPaiItem abbpi = (ActivityBuffBangPaiItem)abim.createActivityBuffItem(buffName,Long.parseLong(bangPaiIdStr),des);
    	abbpi.setBuffName(buffName);
    	abbpi.setBuffLevel(1);
    	abbpi.setEndTimeInDay(endTimeInDay);
    	abbpi.setStartTimeInDay(startTimeInDay);
    	abbpi.setFixDayRange(fixDayRange);
    	abbpi.setFixDay(fixDay);
    	abbpi.setTimeType(Integer.parseInt(timeType));
    	abbpi.setWeekDay(Integer.parseInt(weekDay));
    }
	}catch(Exception ex){
		
	}
}
%>
<%@page import="com.fy.engineserver.operating.activitybuff.ActivityBuffItemManager"%>
<%@page import="com.fy.engineserver.operating.activitybuff.ActivityBuffItem"%>
<%@page import="com.fy.engineserver.operating.activitybuff.ActivityBuffBangPaiItem"%><%@include file="../IPManager.jsp" %><html>
  <head>
    <title>Gang List</title>
    
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
  <%if(gangs!=null){ %>
	<table border="1">
    <tr>
    	<td align="center" width="10%">帮会名称</td>
		<td align="center" width="10%">级别</td>
		<td align="center" width="10%">创建日期</td>
		<td align="center" width="10%">创建人</td>
		<td align="center" width="10%">帮主</td>
		<td align="center" width="10%">帮会积分</td>
    </tr>
    <%for(Gang g:gangs){
    	out.println("<tr>");
		out.println("<td><a href=gangMembers.jsp?gangName="+g.getName()+">"+g.getName()+"</a></td>");
		out.println("<td>"+g.getGanglevel()+"</td>");
		out.println("<td>"+g.getCreatedate().toString()+"</td>");
				
		Player p = PlayerManager.getInstance().getPlayer(g.getCreator().intValue());
		out.println("<td>"+(p == null ? "无法获得玩家对象" : p.getName() )+"("+g.getCreator()+")</td>");
				
		p = PlayerManager.getInstance().getPlayer(g.getBangzhu().intValue());
		out.println("<td>"+(p == null ? "无法获得玩家对象" : p.getName())+"("+g.getBangzhu()+")</td>");
		out.println("<td>"+g.getGangPoint()+"</td>");
		out.println("</tr>");     
    }%>
    </table>
      
      <table width="100%">
      	<tr>
      		<td align="center">
				<%
				if(currentPage>0){
					out.print("<a href=gangList.jsp?pageNum="+(currentPage-1)+">上一页</a>&nbsp;&nbsp;&nbsp;");    	
    			}
				if(gangs.size()==lines){
    				out.println("<a href=gangList.jsp?pageNum="+(currentPage+1)+">下一页</a>");
    			} %>      		
      		</td>
      	</tr>
      </table>
      <%

      if(abim != null){
      %>
      <form name="f1">
      帮派Id:<input type="text" name="bangPaiId"><br>
      buff名:<input type="text" name="buffName"><br>
      buff描述:<input type="text" name="des"><br>
      时间类型:<select name="timeType">
      <%
      for(int i = 0; i < ActivityBuffItem.TIME_TYPE_NAMES.length; i++){
    	  %>
    	  <option value="<%=i %>"><%=ActivityBuffItem.TIME_TYPE_NAMES[i] %>
    	  <%
      }
      %>
      </select>
      星期几:<input type="text" name="weekDay">
      固定日期(格式2010-04-29):<input type="text" name="fixDay">
      固定时间段(格式2010-04-29~2010-04-29):<input type="text" name="fixDayRange">
            开始时间(必填格式00:00):<input type="text" name="startTimeInDay">
                  结束时间(必填格式23:59):<input type="text" name="endTimeInDay">
      <input type="submit" value="增加帮派buff">
      </form>
      <%} %>
  <%}%>
  </body>
</html>
