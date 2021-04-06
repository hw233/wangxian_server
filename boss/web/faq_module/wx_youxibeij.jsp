<%@page import="com.fy.boss.gm.faq.FaqRecord"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.boss.gm.faq.FaqManager"%>
<%@ page contentType="text/html;charset=utf-8"%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>游戏背景</title>
</head>

<body>
<%
	FaqManager fm = FaqManager.getInstance();
	List<FaqRecord> records = fm.getRecords();
	if(records.size()>0){
		for(FaqRecord record:records){
			if(record.getGamename().equals("飘渺寻仙曲")&&record.getModulename().equals("游戏背景")){
				out.print("<h4>"+record.getTitle()+"</h4>");
				out.print("<li>"+record.getContent()+"</li>");
				out.print("<ul type='disc'>");
				out.print("</ul><br>"+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
						+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
						+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
						+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
				out.print("录入人："+record.getRecorder()+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
				out.print("录入时间："+record.getRecordtime());
				out.print("<HR align=center width=97% color=#000001 SIZE=1> ");
			}
			
		}
	}
%>
 
</body>

</noframes>
</html>

