<%@ page contentType="text/html;charset=gb2312" 
import="java.io.*,java.util.*,com.xuanzhi.tools.text.*,com.xuanzhi.tools.statistics.depot.*"%><%

	String className = request.getParameter("cl");
	DepotProject sp = DepotProject.getDepotProject(className);		
	
	
%><html>
<head>
</head>
<body>
<% 
	if("true".equals(request.getParameter("addpartition"))){
		String yearMonth = request.getParameter("yearMonth").trim();
		sp.addPartition(yearMonth);
		out.println("<center><h1>分区"+yearMonth+"插入成功</h1></center><br/>");
	}
%>
<br><h2>显示<%=sp.getName()%>统计项目表各个分区</h2>
<br/>
<table border='0' cellpadding='0' cellspacing='1' width='100%' bgcolor='#000000' align='center'>
<tr bgcolor='#00FFFF' align='center'><td>位置</td><td>分区名</td><td>分区截至年月</td><td>已插入行数</td></tr>
<%
	String partitions[][] = sp.getAllPartitionInformation();	

	for(int i = 0 ; i < partitions.length ; i++){
		out.println("<tr bgcolor='#FFFFFF' align='center'>");
		out.println("<td>"+(i+1)+"</td><td>"+partitions[i][0]+"</td><td>"+partitions[i][1]+"</td><td>"+partitions[i][2]+"</td>");
		out.println("</tr>");
	}
%></table>
<br/>
添加新的分区：<br/>
<form id='f'><input type='hidden' name='cl' value='<%=className%>'><input type='hidden' name='addpartition' value='true'>
请输入分区截至年月：<input type='text' name='yearMonth' value=''>&nbsp; 格式为yyyyMM，比如200901，此年月只能是大于最后分区时间或者介于最后分区和倒数第二分区之间。
<br><input type='submit' value='提   交'></form>
</body>
</html> 
