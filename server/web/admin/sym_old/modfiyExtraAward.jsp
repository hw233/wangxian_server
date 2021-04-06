<%@page import="com.fy.engineserver.util.TimeTool"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@page import="com.fy.engineserver.menu.activity.exchange.ExchangeActivityManager"%>
<%@page import="com.fy.engineserver.activity.ExtraAwardActivity"%>
  <body>
  	<p>
  		<% 
  		List<ExtraAwardActivity> extraAwaList=ExchangeActivityManager.getInstance().getExtraAwaList();
  		for(ExtraAwardActivity ea:extraAwaList){
  			String name=ea.getName()[0];
  			if(name.equals("国运") && (ea.getEndTime() == 1471795199000L)){
  				ea.setEndTime(1471795199000L + 10*24*60*60*1000L);
  				out.print("["+name+"] [开始时间:"+TimeTool.formatter.varChar23.format(ea.getStartTime())+"] [结束时间:"+TimeTool.formatter.varChar23.format(ea.getEndTime())+"] ["+ea.getStartTime()+"] ["+ea.getEndTime()+"]<br>");
  			}
  		}
  		%>
  	</p>
  </body>
</html>
