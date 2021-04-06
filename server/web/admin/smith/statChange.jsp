<%@page import="com.fy.engineserver.smith.MoneyRelationShipManager"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%
	String currstat = request.getParameter("currstat");	
	boolean openning = false;
	MoneyRelationShipManager msm = MoneyRelationShipManager.getInstance();
	if(currstat!=null && msm!=null){
		if(currstat.equals("开启")) {
				msm.start();
				out.print("start");
		} else if(currstat.equals("关闭")){
				msm.stop();
				out.print("stop");
		}
	}
%>
