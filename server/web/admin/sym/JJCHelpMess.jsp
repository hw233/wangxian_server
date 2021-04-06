<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.battlefield.jjc.JJCManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.battlefield.jjc.JJCBillboardTeamInfo"%>
<%@page import="java.util.Arrays"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
</head>
<body>
<%
	
	long ids[] = JJCManager.em_billboardInfo.queryIds(JJCBillboardTeamInfo.class,"");
	for(long id : ids){
		JJCBillboardTeamInfo info2 = JJCManager.em_billboardInfo.find(id);
		if(info2.getTeamid() == 1504000000000040962L){
			JJCBillboardTeamInfo info = JJCManager.em_billboardInfo.find(id);
			info.setTeamName("qqqq@晨提夕命");
			out.print(info+"<br>");
		}else if(info2.getTeamid() == 1507000000000081922L){
			JJCBillboardTeamInfo info = JJCManager.em_billboardInfo.find(id);
			info.setTeamName("qqqq@冲云破雾");
			out.print(info+"<br>");
		}else if(info2.getTeamid() == 1502000000000061443L){
			JJCBillboardTeamInfo info = JJCManager.em_billboardInfo.find(id);
			info.setTeamName("qqqq@不舞之鹤");
			out.print(info+"<br>");
		}
		
	}
	//List<JJCBillboardTeamInfo> list =  JJCManager.em_billboardInfo.queryFields(JJCBillboardTeamInfo.class,ids);
	//for(JJCBillboardTeamInfo info : list){
		//out.print(info+"<br>");
	//}
%>
</body>
</html>