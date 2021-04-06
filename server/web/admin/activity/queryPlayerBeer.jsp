<%@ page language="java" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.activity.fateActivity.FateManager"%>
<%@page import="com.fy.engineserver.activity.fateActivity.base.FateActivity"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.fy.engineserver.sprite.PropsUseRecord"%><html>
<head>
	<title>查询玩家喝酒次数</title>
</head>

<body>
	<%
	
		String name = request.getParameter("name");
		if(name != null){
			
			Player player = PlayerManager.getInstance().getPlayer(name);
			
			Map<String, PropsUseRecord> map = player.getPropsUseRecordMap();
			PropsUseRecord pr = map.get("酒");
			
			out.print(pr.getLastUseTime()+"<br/>");
			out.print(pr.getUseNum()+"<br/>");
			out.print(pr.getPropsName()+"<br/>");
			
			return;
		}
	
	%>
	
		<form action="">
		
			玩家名:<input type="text" name="name"/><br/>
			<input type="submit" value="submit"/>
	
		</form>
	
</body>
</html>
