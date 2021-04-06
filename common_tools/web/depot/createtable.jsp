<%@ page contentType="text/html;charset=gb2312" 
import="java.io.*,java.util.*,com.xuanzhi.tools.text.*,com.xuanzhi.tools.statistics.*,com.xuanzhi.tools.statistics.depot.*"%><%

	String className = request.getParameter("cl");

	DepotProject sp = DepotProject.getDepotProject(className);
	
	String createtable = request.getParameter("createtable");
	boolean showCreateTableButton = false;
	if(createtable == null) showCreateTableButton = true;
	
	String errorMessage = null;
	
	if(createtable != null && createtable.equals("true")){
		try{
			sp.createTableAndIndexs("");
			errorMessage = "创建"+sp.getName()+"项目表和索引成功！";
		}catch(Exception e){
			errorMessage = "创建"+sp.getName()+"项目表和索引失败！错误信息：<br>";
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
				e.printStackTrace(new PrintStream(bout));
				errorMessage += "<pre>"+new String(bout.toByteArray())+"</pre>";
		}
	}
%><html>
<head>
</head>
<body>
<br><h2>创建<%=sp.getName()%>统计项目表及其各个维度的索引脚本</h2>
<br/>

<%
	
	if(errorMessage != null){
		out.println("<h3 color='red'>"+errorMessage+"</h3>");
	}else{
		String month = "";
		
		String sql = sp.constructCreateTableSQL(month);
		%>创建表的SQL：<pre><%= sql %></pre><%
		
		String sqls[] = sp.constructCreateIndexSQL(month);
		sql = "";
		
		for(int i = 0 ; i < sqls.length ;i ++)
			sql += sqls[i] + ";\n";
		%>创建索引的SQL：<pre><%=sql%></pre><%
		
		if(showCreateTableButton){
			%><form id="ffff"><input type="hidden" name="cl" value="<%=className%>">'><input type="hidden" name="createtable" value="true">
				<input type="submit" value="创建<%=month%>的表和索引"></form><br><%
		}
	}
%>
</body>
</html> 
