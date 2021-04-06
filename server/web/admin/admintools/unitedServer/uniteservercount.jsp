<%@page import="com.fy.engineserver.uniteserver.DataCollect"%>
<%@page import="com.fy.engineserver.uniteserver.UnitedServerManager"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.fy.engineserver.uniteserver.DataUnit"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
</head>
<body>
<%!
	class UniteServerCounter
	{
		public String className;
		public long totalNum;
		public long mergeStarttime;
		public long mergeEndtime;
	
	}

%>
<%
/**
	public static Map<Class<?>, DataCollect<?>> dataCollectMap = new Hashtable<Class<?>, DataCollect<?>>();
	public static Map<Class<?>, DataUnit<?>> dataUnitMap = new Hashtable<Class<?>, DataUnit<?>>();
*/
	//已经合并的 dataUnitMap存在 并且合并线程running为false的
	//正在合并的 dataUnitMap存在 并且合并线程running为true的
	//未在dataUnitMap中的
	
	List<UniteServerCounter> mergedLst = new ArrayList<UniteServerCounter>();
	List<UniteServerCounter> mergingLst = new ArrayList<UniteServerCounter>();
	List<Class<?>> uniteClasses  = UnitedServerManager.uniteClasses;
	Iterator<Class<?>> it = UnitedServerManager.dataUnitMap.keySet().iterator();
	
	while(it.hasNext())
	{
		Class<?> cl = it.next();
		DataUnit<?> du = UnitedServerManager.dataUnitMap.get(cl);
		DataCollect<?> dc =  UnitedServerManager.dataCollectMap.get(cl);
		
		if(!du.running)
		{
			//已经合并了的
			dc.
			du.
			mergedLst.add(e)
		}
	}

%>


</body>
</html>