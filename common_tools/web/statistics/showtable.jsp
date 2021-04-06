<%@ page contentType="text/html;charset=gb2312" 
import="java.io.*,java.util.*,com.xuanzhi.tools.text.*,com.xuanzhi.tools.statistics.*"%><%

	String className = request.getParameter("cl");
	boolean usingOracle = String.valueOf("true").equals(request.getParameter("uo")); 
	
	StatProject sp = StatProject.getStatProject(className);
	if(usingOracle)
		sp.setUsingOracle(usingOracle);
		
%><html>
<head>
</head>
<body>
<br><h2>显示<%=sp.getName()%>统计项目表及其各个维度的索引</h2>
<br/>
<table border='0' cellpadding='0' cellspacing='1' width='100%' bgcolor='#000000' align='center'>
<tr bgcolor='#00FFFF' align='center'><td>表名</td><td>索引名</td><td>索引字段</td></tr>
<%
	String tables[] = sp.getAllTables();
	for(int i = 0 ; i < tables.length ; i++){
		String[][] indexs = sp.getIndexs(tables[i]);
		for(int j = 0 ; j < indexs.length ; j++){
			out.println("<tr bgcolor='#FFFFFF' align='center'>");
			if(j == 0){
				out.println("<td rowspan='"+indexs.length+"'>"+tables[i]+"</td>");
			}
			out.println("<td>"+indexs[j][0]+"</td><td>"+indexs[j][1]+"</td>");
			out.println("</tr>");
		}
	}
%></table>
</body>
</html> 
