<%@page import="com.fy.engineserver.platform.PlatformManager"%>
<%@page import="com.fy.engineserver.activity.wafen.model.WaFenActivity"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.util.config.ServerFit4Activity"%>
<%@page import="com.fy.engineserver.activity.AllActivityManager"%>
<%@page import="com.fy.engineserver.activity.BaseActivityInstance"%>
<%@page import="com.fy.engineserver.activity.wafen.manager.WaFenManager"%>
<%@page import="com.fy.engineserver.util.TimeTool"%>
<%@page import="com.xuanzhi.tools.text.ParamUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>腾讯挖坟bug</title>
</head>
<body>
<%
	List<BaseActivityInstance> list = AllActivityManager.instance.allActivityMap.get(AllActivityManager.wafenActivity);
	List<String> openServers = new ArrayList<String>();
	for(BaseActivityInstance base : list){
		ServerFit4Activity fit = base.getServerfit();
		if(fit == null){
			out.print("fit == null");
			continue;
		}
		fit.setOpenServerNames(null);//??
		base.setServerfit(fit);
		AllActivityManager.instance.allActivityMap.put(AllActivityManager.wafenActivity,list);
		out.print("[设置成功] [] [当前服务器："+fit.getServerName()+"] [开始时间:"+TimeTool.formatter.varChar23.format(base.getStartTime())+"] [平台:"+Arrays.toString(fit.getPlatForms())+"] ");
	}	
%>
</body>
</html>