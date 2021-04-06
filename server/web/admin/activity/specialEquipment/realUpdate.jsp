<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@page import="com.fy.engineserver.billboard.special.SpecialEquipmentManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.equipments.Equipment"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.*"%>



<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>


<%@page import="com.fy.engineserver.billboard.special.FlushBossInfo"%>
<%@page import="com.fy.engineserver.billboard.special.SpecialEquipmentManager.SpecialEquipmentAppearMap"%>
<%@page import="com.fy.engineserver.seal.SealManager"%>
<%@page import="com.fy.engineserver.seal.data.Seal"%>
<%@page import="com.fy.engineserver.gametime.SystemTime"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>更新刷新条件</title>
</head>
<body>
	
	<%
		String sealTimeLastSt = request.getParameter("sealTimeLast");
		if(sealTimeLastSt == null || sealTimeLastSt.equals("")){
			
			%>
			
	<form action="">
		封印多长时间后(时间秒):<input type="text" name="sealTimeLast"/><br/>
		第一次通知生成boss信息的时间 (时间分):<input type="text" name="first"/><br/>
		第二次通知生成boss信息的时间:<input type="text" name="second"/><br/>
		第三次通知生成boss信息的时间:<input type="text" name="third"/><br/>
		
		<input type="submit" value="submit"/>
	
	</form>
			
			
			<%
		}else{
			
			SpecialEquipmentManager.生成boss信息间隔 = Integer.parseInt(sealTimeLastSt)*1000;
			SpecialEquipmentManager.第一次通知生成boss信息的时间  = 30000;
			SpecialEquipmentManager.第二次通知生成boss信息的时间  = 15000;
			SpecialEquipmentManager.第三次通知生成boss信息的时间  = 5000;
			
			out.print("设置时间成功");
			
		}
	%>
	
	
</body>
</html>