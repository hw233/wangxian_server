<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	DigManager dm= DigManager.getInstance();
	for(DigRefMapInfo dmi:dm.getDigRefInfoList()){
		Game game=GameManager.getInstance().getGameByName(dmi.getMapName(),0);
		if(game==null){
			out.print("地图不存在,地图名:"+dmi.getMapName());
		}
	}
%>

<%@page import="com.fy.engineserver.activity.dig.DigManager"%>
<%@page import="com.fy.engineserver.activity.dig.DigRefMapInfo"%>
<%@page import="com.fy.engineserver.core.GameManager"%>
<%@page import="com.fy.engineserver.core.Game"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>检查地图</title>
</head>
<body>

</body>
</html>