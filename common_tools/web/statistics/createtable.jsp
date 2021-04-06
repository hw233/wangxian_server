<%@ page contentType="text/html;charset=gb2312" 
import="java.io.*,java.util.*,com.xuanzhi.tools.text.*,com.xuanzhi.tools.statistics.*"%><%

	String className = request.getParameter("cl");

	boolean usingOracle = String.valueOf("true").equals(request.getParameter("uo")); 
	
	StatProject sp = StatProject.getStatProject(className);
	if(usingOracle)
		sp.setUsingOracle(usingOracle);
	
	String month = request.getParameter("month");
	if(month != null && month.trim().length() == 0) month = null;
	
	String createtable = request.getParameter("createtable");
	boolean showCreateTableButton = false;
	if(createtable == null && month != null) showCreateTableButton = true;
	
	String errorMessage = null;
	
	if(createtable != null && createtable.equals("true") && month != null){
		try{
			sp.createTableAndIndexs(month);
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
<form id="f"><input type="hidden" name="cl" value="<%=className%>"><input id='uo' name='uo' type='hidden' value='<%=usingOracle%>'>
请输入月份：<input type="text" size="20" name="month" value="<%=month==null?"":month%>">&nbsp;<input type="submit" value="提  交">&nbsp;格式yyyy_MM，比如2007_09，2007_10<br/>
</form><br>
<%
	
	if(errorMessage == null && month != null){
		month = month.trim();
		int k = month.indexOf("_");
		try{
		if(k > 0){
			int y = Integer.parseInt(month.substring(0,k));
			int m = Integer.parseInt(month.substring(k+1));
			if(y < 2007 || y > 2020) errorMessage = "年份不合情理，应该在2007到2020年之间";
			if(m < 1 || m > 12) errorMessage = "月份不对，月份应该在01到12之间";
		}else{
			errorMessage = "格式错误，应该是yyyy_MM";
		}
		}catch(Exception e){
			errorMessage = "格式错误，应该是yyyy_MM，其中yyyy和MM都表示数字";
		}
	}
	
	if(errorMessage != null){
		out.println("<h3 color='red'>"+errorMessage+"</h3>");
	}else{
		if(month == null) month = "%yyyy_MM%";
		
		String sql = sp.constructCreateTableSQL(month);
		%>创建表的SQL：<pre><%= sql %></pre><%
		
		String sqls[] = sp.constructCreateIndexSQL(month);
		sql = "";
		
		for(int i = 0 ; i < sqls.length ;i ++)
			sql += sqls[i] + ";\n";
		%>创建索引的SQL：<pre><%=sql%></pre><%
		
		if(showCreateTableButton){
			%><form id="ffff"><input type="hidden" name="cl" value="<%=className%>"><input id='uo' name='uo' type='hidden' value='<%=usingOracle%>'><input type="hidden" name="month" value="<%=month%>"><input type="hidden" name="createtable" value="true">
				<input type="submit" value="创建<%=month%>的表和索引"></form><br><%
		}
	}
%>
</body>
</html> 
