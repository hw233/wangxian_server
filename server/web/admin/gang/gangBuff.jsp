<%@ page import="java.util.*" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@page import="com.fy.engineserver.gang.service.GangManager"%>
<%@page import="com.fy.engineserver.gang.model.Gang"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%
GangManager gm=GangManager.getInstance();

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
	    	if(weekDay != null && !weekDay.trim().equals("")){
	    		abbpi.setWeekDay(Integer.parseInt(weekDay));	
	    	}
	    	abim.saveAll();
	    	out.println("加帮派buff<br> 帮派id:"+bangPaiIdStr+"<br> buff名:"+buffName+"<br> timeType:"+timeType+"<br> weekDay:"+weekDay+"<br> fixDay:"+fixDay+"<br> fixDayRange:"+fixDayRange+"<br> startTimeInDay:"+startTimeInDay+"<br> endTimeInDay:"+endTimeInDay+"<br>");
	    }
	}catch(Exception ex){
		out.println(StringUtil.getStackTrace(ex));
	}
}
%>



<%@page import="com.fy.engineserver.operating.activitybuff.ActivityBuffItemManager"%>
<%@page import="com.fy.engineserver.operating.activitybuff.ActivityBuffItem"%>
<%@page import="com.fy.engineserver.operating.activitybuff.ActivityBuffBangPaiItem"%>
<%@page import="com.xuanzhi.tools.text.StringUtil"%><%@include file="../IPManager.jsp" %><html>
  <head>
    
    
    <title>Gang List</title>
    
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>

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
      </select><br>
      星期几:<input type="text" name="weekDay"><br>
      固定日期(格式2010-04-29):<input type="text" name="fixDay"><br>
      固定时间段(格式20100429~20100429):<input type="text" name="fixDayRange"><br>
            开始时间(必填格式00:00):<input type="text" name="startTimeInDay"><br>
                  结束时间(必填格式23:59):<input type="text" name="endTimeInDay"><br>
      <input type="submit" value="增加帮派buff">
      </form>
      <%} %>
  </body>
</html>
