<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@page import="com.fy.engineserver.util.datacheck.DataCheckManager"%>
<%@page import="com.fy.engineserver.util.datacheck.DataCheckHandler"%>
<%@page import="com.fy.engineserver.util.datacheck.SendHtmlToMail"%>
<%@page import="java.util.Arrays"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>物品检查</title>
<!-- <link rel="stylesheet" type="text/css" href="../css/common.css" />-->
<!--  <link rel="stylesheet" type="text/css" href="../css/table.css" />-->
<link rel="stylesheet" type="text/css" href="../css/style.css" />
<script type="text/javascript">

</script>
</head>
<body>
<% 
DataCheckManager check = DataCheckManager.getInstance();	
for (DataCheckHandler handler : check.getCheckHandlers()) {
	if (handler.getCheckResult().getBooleanValue()) { // 有数据录入错误
		SendHtmlToMail[] htmlToMails = (SendHtmlToMail[]) handler.getCheckResult().getObjValue();
		if (htmlToMails != null && htmlToMails.length > 0) {
			out.print("<table title="+handler.getHandlerName()+">");
			out.print("<h1>"+handler.getHandlerName()+"---->错误数量："+htmlToMails.length+"---->表："+Arrays.toString(handler.involveConfigfiles())+""+"</h1>");
			int index = 0;
			for(SendHtmlToMail info : htmlToMails){
				if(index == 0){
					out.print("<tr>");
					String [] titles = info.getTitles();
					for(String n : titles){
						out.print("<th>"+n+"</th>");
					}
					out.print("</tr>");
				index++;
				}
				out.print("<tr>");
				String [] values = info.getValues();
				for(String n : values){
					out.print("<th>"+n+"</th>");
				}
				out.print("</tr>");
			}
			
			out.print("</table>");
		}
	}
}

%>
</body>
</html>
