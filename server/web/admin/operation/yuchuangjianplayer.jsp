<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.country.manager.CountryManager"%>
<%@page import="com.fy.engineserver.util.TimeTool"%>
<%@page import="com.fy.engineserver.oper.LocalServerInfo"%>
<%@page import="com.xuanzhi.tools.cache.diskcache.DiskCache"%>
<%@page import="com.fy.engineserver.util.CommonDiskCacheManager"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="com.xuanzhi.tools.text.DateUtil"%>
<%@page import="java.util.Date"%>
<%@page import="com.fy.engineserver.core.CoreSubSystem"%>
<%@page import="com.xuanzhi.tools.text.ParamUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
	boolean isYuchuangjian = ParamUtils.getBooleanParameter(request, "switchYuchuangjian");	
	
	if(isYuchuangjian)
	{
		CoreSubSystem.isJudgeCanEnter = !CoreSubSystem.isJudgeCanEnter;
	}
	DiskCache diskCache  = null;
	//存储到diskcache中
	CommonDiskCacheManager commonDiskCacheManager =  CommonDiskCacheManager.getInstance();
	
	if(commonDiskCacheManager != null)
	{
		diskCache =  commonDiskCacheManager.getDiskCache();
	}

	String key = CoreSubSystem.keyprefix+GameConstants.getInstance().getServerName();
	boolean openservertime = ParamUtils.getBooleanParameter(request, "openservertime");	 
	if(openservertime)
	{
		String opentimestr = ParamUtils.getParameter(request, "opentime");
		
		LocalServerInfo localServerInfo = new LocalServerInfo();
		
		//格式化时间
		localServerInfo.serverOpenTime = DateUtil.parseDate(opentimestr, "yyyy-MM-dd HH:mm:ss").getTime();
		localServerInfo.serverName = GameConstants.getInstance().getServerName();
		localServerInfo.canCreatePlayer = true;
		if(diskCache != null)
		{
			diskCache.put(key, localServerInfo);
			out.println("设置开服信息成功!<br/>");
		}
	
	}
	String option = request.getParameter("option");
	if("beforehandCreate".equals(option)){
		//要设置预创建数据
		String beforehandCreateStartTime = request.getParameter("beforehandCreateStartTime");
		String beforehandCreateEndTime = request.getParameter("beforehandCreateEndTime");
		CoreSubSystem.getInstance().setBeforehandCreateStartTime(TimeTool.formatter.varChar19.parse(beforehandCreateStartTime));
		CoreSubSystem.getInstance().setBeforehandCreateEndTime(TimeTool.formatter.varChar19.parse(beforehandCreateEndTime));
		{
			//解析各个国家的人数限制
			for (int i = 0; i < CoreSubSystem.getInstance().getBeforehandCreateCountryNum().length;i++) {
				String stringNum =  request.getParameter("country_" + i);
				if (stringNum != null) {
					CoreSubSystem.getInstance().getBeforehandCreateCountryNum()[i] = Integer.valueOf(stringNum);
				}
			}
			CoreSubSystem.getInstance().setBeforehandCreateCountryNum(CoreSubSystem.getInstance().getBeforehandCreateCountryNum());
		}
		out.print("<H2>预创建设置完毕</H2>");
	}
	
%>

<table border="2px">
	<tr>
		<td>服务器名称</td>
		<td>开服时间</td>
	</tr>
	<tr>
		<td><%=GameConstants.getInstance().getServerName() %></td>
		<td><%if(diskCache != null && diskCache.get(key) == null) {%>尚未设置<%}else if(diskCache != null && diskCache.get(key) != null){ %><%=((LocalServerInfo)diskCache.get(key)).serverOpenTime%><%} %></td>
	</tr>
</table>

<form method="post">
<input type="hidden" name="switchYuchuangjian" value="true">
	<input type="submit" value="<%if(CoreSubSystem.isJudgeCanEnter){ %>关闭判断预创建角色功能，使得玩家可以直接进入游戏服<% }else{%>开启预创建角色功能<%} %>" >
</form>
<br/>
<br/>
<form method="post">
<input type="hidden" name="openservertime" value="true">
	设置开服时间:<input type="text" name="opentime" value="">(格式:2013-12-25 00:00:00)<br/>
	<input type="submit" value="提交" >
</form>
<hr>
<h1>以下是预创建时间配置,<%=TimeTool.formatter.varChar19.getDes() %></h1>
<hr>
<form action="./yuchuangjianplayer.jsp" method="post">
预创建开始时间:<input name="beforehandCreateStartTime" type="text" value="<%=TimeTool.formatter.varChar19.format(CoreSubSystem.getInstance().getBeforehandCreateStartTime())%>"><BR/>
预创建结束时间:<input name="beforehandCreateEndTime" type="text" value="<%=TimeTool.formatter.varChar19.format(CoreSubSystem.getInstance().getBeforehandCreateEndTime())%>"><BR/>
预创建国家人数:<BR>
<% for (int i = 0; i < CoreSubSystem.getInstance().getBeforehandCreateCountryNum().length;i++) {
	String countryField = "country_" + i;
	int value =  CoreSubSystem.getInstance().getBeforehandCreateCountryNum()[i];
	if (i == 0) {
		%>
		<input name="<%=countryField%>" value="<%=value %>" type="hidden">
		<%
		continue;
	}
%>
	<%=CountryManager.得到国家名(i) %>:<input name="<%=countryField%>" value="<%=value %>" type="text">已有:<%=((GamePlayerManager) GamePlayerManager.getInstance()).em.count(Player.class, "country = ?", new Object[] { i }) %><br/>
	
<%
} %>
<input type="hidden" name="option" value="beforehandCreate">
<input type="submit" value="设置">
</form>
</body>
</html>