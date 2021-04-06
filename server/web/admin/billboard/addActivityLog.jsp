<%@ page contentType="text/html;charset=utf-8"%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">

<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.activity.fateActivity.*"%>
<%@page import="java.util.*"%>

<%@page import="com.fy.engineserver.sprite.ActivityRecordOnPlayer"%>
<%@page import="com.fy.engineserver.newBillboard.BillboardsManager"%>
<%@page import="com.fy.engineserver.newBillboard.*"%>
<%@page import="com.xuanzhi.tools.simplejpa.*"%>



<%@page import="com.fy.engineserver.util.StringTool"%>
<%@page import="com.xuanzhi.tools.text.StringUtil"%>

<%@page import="com.fy.engineserver.newBillboard.monitorLog.LogRecordManager"%>
<%@page import="com.fy.engineserver.newBillboard.monitorLog.LogRecord"%>
<%@page import="java.util.Map.Entry"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>添加打印活动日志</title>
</head>
<body>

	<%
		String typeSt = request.getParameter("typeS");
		String platSt = request.getParameter("platS");
		if(typeSt != null && platSt != null){
		
			LogRecordManager manager = LogRecordManager.getInstance();
		
			Map<String, LogRecord> map = manager.map;
			
			if(map.get(typeSt) != null){
				
				String dateSt = manager.sdf.format(new Date(System.currentTimeMillis()+60*1000));
				LogRecord lr = map.get(typeSt);
				
				String[] srcDate = lr.dateString;
				String[] srcPlat = lr.platform;
				
				String[] destDate = new String[srcDate.length+1];
				String[] destPlat = new String[srcDate.length+1];
				destDate[0] = dateSt;
				destPlat[0] = platSt;
				System.arraycopy(srcDate,0,destDate,1,srcDate.length);
				System.arraycopy(destPlat,0,destPlat,1,srcPlat.length);
				
				lr.dateString = destDate;
				lr.platform = destPlat;
				
				lr.init();
				out.print("over");
				
			}else{
				out.print("类型错误");
			}
			return ;
		}
		
	%>
	
	<form action="">
	
		类别(家族,仙府,等级,酒仙,宠物):<input type="text" name="typeS"/>
		平台:<input type="text" name="platS"/>
		<input type="submit" value="submit"/>
	</form>
</body>

</html>
