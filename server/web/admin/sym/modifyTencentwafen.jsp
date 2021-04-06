<%@page import="java.io.File"%>
<%@page import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache"%>
<%@page import="com.fy.engineserver.platform.PlatformManager"%>
<%@page import="com.fy.engineserver.activity.wafen.model.WaFenActivity"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.util.config.ServerFit4Activity"%>
<%@page import="com.fy.engineserver.activity.AllActivityManager"%>
<%@page import="com.fy.engineserver.activity.BaseActivityInstance"%>
<%@page import="com.fy.engineserver.activity.wafen.manager.WaFenManager"%>
<%@page import="com.fy.engineserver.util.TimeTool"%>
<%@page import="com.fy.engineserver.activity.wafen.instacne.*"%>
<%@page import="com.xuanzhi.tools.text.ParamUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>腾讯挖坟</title>
</head>
<body>
<%
	String fileKey = null;
	long openTime = 0;
	List<BaseActivityInstance> list = AllActivityManager.instance.allActivityMap.get(AllActivityManager.wafenActivity);
	long now = System.currentTimeMillis();
	if (list != null && list.size() > 0) {
		for (BaseActivityInstance bi : list) {
			if (bi.getServerfit().thiserverFit() && bi.getEndTime() >= now) {
				fileKey = bi.getEndTime() + "";
				openTime = bi.getStartTime();
				break;
			}
		}
	}
	String df = WaFenManager.instance.getDiskFile() + fileKey + ".ddc";
	File file = new File(df);
	if (!file.exists()) {
		file.createNewFile();
	}
	WaFenManager.instance.disk = new DefaultDiskCache(file, null, "fairybuddha", 100L * 365 * 24 * 3600 * 1000L);
	
	WaFenManager.privateKey = fileKey;
	WaFenManager.instance.privateMaps = (Map<Long, WaFenInstance4Private>) WaFenManager.instance.disk.get(fileKey);
	//if (WaFenManager.instance.privateMaps == null) {
		WaFenManager.instance.privateMaps = new Hashtable<Long, WaFenInstance4Private>();
	//}
	WaFenManager.instance.commonMaps = (Map<String, WaFenInstance4Common>) WaFenManager.instance.disk.get(WaFenManager.instance.key);
	//if (WaFenManager.instance.commonMaps == null) {
		WaFenManager.instance.commonMaps = new Hashtable<String, WaFenInstance4Common>();
	//}
	out.println("==============成功==============<br>");
%>
</body>
</html>