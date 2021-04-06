<%@page
	import="com.fy.engineserver.septbuilding.entity.SeptBuildingEntity"%>
<%@page import="com.fy.engineserver.jiazu.JiazuTitle"%>
<%@page
	import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.jiazu.service.JiazuManager"%>
<%@page import="com.fy.engineserver.jiazu.Jiazu"%>
<%@page
	import="com.fy.engineserver.septstation.service.SeptStationManager"%>
<%@page import="com.fy.engineserver.septstation.SeptStation"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改家族更新繁荣度时间</title>
</head>
<body>

<%
	String jiazuName = request.getParameter("jiazuName");
	String timest = request.getParameter("time");
	out.print(jiazuName +"  ||" + timest);
	if(jiazuName == null){
		
	}else{
		Jiazu jiazu = JiazuManager.getInstance().getJiazu(jiazuName);
		if(jiazu != null){
			
			if(timest == null || timest.equals("")){
				
				//更新为现在时间
				out.print(jiazu.getUpdateProsperityTime());
				JiazuManager.logger.error("[修补更新繁荣度时间] [家族:"+jiazu.getJiazuID()+"] [家族name:"+jiazu.getName()+"] [原来时间:"+jiazu.getUpdateProsperityTime()+"] [现在时间:"+System.currentTimeMillis()+"]");
				jiazu.setUpdateProsperityTime(System.currentTimeMillis());
			}else{
				JiazuManager.logger.error("[还原更新繁荣度时间] [家族:"+jiazu.getJiazuID()+"] [家族name:"+jiazu.getName()+"] [原来时间:"+jiazu.getUpdateProsperityTime()+"] [现在时间:"+Long.parseLong(timest)+"]");
				jiazu.setUpdateProsperityTime(Long.parseLong(timest));
			}
			
		}else{
			out.print("没有此家族"+jiazuName);
		}
		out.print("更新完毕");
		return;
	}

%>

	<form action="">
			家族名:<input type="text" name="jiazuName"/><br/>
			更新时间:<input type="text" name="time"/><br/>
		<input type="submit" value="submit"/>
	</form>

</body>
</html>