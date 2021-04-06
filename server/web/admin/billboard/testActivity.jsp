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
<title>测试打印活动日志</title>
</head>
<body>

	<%
	
		
		LogRecordManager manager = LogRecordManager.getInstance();
	
		Map<String, LogRecord> map = manager.map;

		for(Map.Entry<String, LogRecord> en : map.entrySet()){
				
			out.print("类别:"+en.getKey()+"  " + "完全过期:"+en.getValue().effected +"  ");
			
			
			if(en.getValue().effected){
				
				String[] st1 = en.getValue().platform;
				String[] st2 = en.getValue().dateString;
				boolean[] bln1 = en.getValue().state;
				for(int i=0;i<st1.length;i++){
					out.print("平台:"+st1[i] +"  "+"日期:"+st2[i]+"  "+"过期:"+bln1[i]);
				}
			}
			out.print("<br/>");
			
		}
	%>
</body>

</html>
