<%@page import="java.util.Map"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.uniteserver.UnitedServerManager"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>合服统计</title>
</head>
<body>
	<%!
		class UniteServerCounter
		{
			public String className;
			public long totalNum;
			public long mergeBeginTime;
			public long mergeEndTime;
			public long costedTime;
		}
	%>
	<%

		UnitedServerManager unitedServerManager = UnitedServerManager.getInstance();
	
		List<Class<?>> uniteClasses = UnitedServerManager.uniteClasses;
		Map<Class<?>, DataCollect<?>> collectMap = UnitedServerManager.dataCollectMap;
		Map<Class<?>, DataUnit<?>> dataUnitMap = UnitedServerManager.dataUnitMap;
		//合并的
		List<UniteServerCounter> mergedList = new ArrayList<UniteServerCounter>();
		//正在合并的
		List<UniteServerCounter> mergingList = new ArrayList<UniteServerCounter>();
		//未合并的
		List<UniteServerCounter> unMergeList = new ArrayList<UniteServerCounter>();
		
		
	
		
	%>
</body>
</html>