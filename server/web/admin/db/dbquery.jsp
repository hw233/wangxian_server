<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,java.lang.reflect.*,
com.fy.engineserver.sprite.*,com.xuanzhi.tools.transport.*,com.google.gson.*,com.fy.engineserver.datasource.career.*,com.fy.engineserver.datasource.skill.*,com.xuanzhi.tools.dbpool.*,java.sql.Connection,java.sql.*"%>

<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="java.io.PrintStream"%>
<%
try{
String sqlQuery = request.getParameter("sqlQuery");
String tableN = request.getParameter("tableName");
String tableSelect = request.getParameter("tableSelect");
String propertySelect = request.getParameter("propertySelect");
String inputValue = request.getParameter("inputValue");
String currentPageStr = request.getParameter("currentPage");
int currentPage = 1;
int pageRowCount = 100;
int allDataCount = 0;
int allPageCount = 0;
if(currentPageStr != null){
	currentPage = Integer.parseInt(currentPageStr);
}
String strtemp = "select article.* from ( select rownum rn, aa.* from (select * from article order by article.id)aa where rownum <= 10000) a , article where a.rn >=1 and a.id = article.id order by article.id;";
StringBuffer querySB = new StringBuffer();
StringBuffer querySBCount = new StringBuffer();
if(tableSelect != null){
	tableN = tableSelect;
	querySB.append("select "+tableSelect+".* from ( select rownum rn, aa.* from (select * from "+tableSelect+" order by id)aa where rownum <= "+currentPage*pageRowCount+") a , "+tableSelect+" where a.rn >"+(currentPage-1)*pageRowCount+" and a.id = "+tableSelect+".id");
	if(propertySelect != null && inputValue != null && !inputValue.trim().equals("")){
		querySB.append(" and "+tableSelect+"."+propertySelect+" = "+inputValue.trim());
	}
	querySB.append(" order by "+tableSelect+".id");
	
	querySBCount.append("select count(*) from "+tableSelect);
	if(propertySelect != null && inputValue != null && !inputValue.trim().equals("")){
		querySBCount.append(" where "+propertySelect+" = "+inputValue.trim());
	}
	querySBCount.append(" order by id");
}
DataSourceManager dsm = DataSourceManager.getInstance();


//Class.forName("oracle.jdbc.driver.OracleDriver").newInstance(); 
//String url="jdbc:oracle:thin:@211.179.174.50:1521:ora10g"; 

Connection conn  = dsm.getConnection();

if(conn == null){
	throw new Exception("没有连接到数据库");
}
//Connection conn= DriverManager.getConnection(url,user,password);
//Connection conn = dsm.getConnection();
Statement stmt1 = conn.createStatement();
Statement stmt2 = conn.createStatement();
Statement stmt3 = conn.createStatement();
Statement stmt4 = conn.createStatement();
List<String> tableNamesList = new ArrayList<String>();
List<String[]> resultList = new ArrayList<String[]>();
HashMap<String,List<String>> hm = new HashMap<String,List<String>>();
try{
ResultSet rs1 = stmt1.executeQuery("select table_name  from user_tables");
      while(rs1.next()){
    	  String tableName = rs1.getString(1);
    	  tableNamesList.add(tableName);
    	  String sqls = "select COLUMN_NAME from all_TAB_COLS t where TABLE_NAME ='"+tableName+"'";
    	  ResultSet rs2 = stmt2.executeQuery(sqls);
    	  List<String> propertys = new ArrayList<String>();
    	  while(rs2.next()){
    		  String pName = rs2.getString(1);
    		  propertys.add(pName);
    	  }
    	  hm.put(tableName,propertys);
      }
      if(querySB.length() != 0){
    	  ResultSet rs3 = null;
    	  ResultSet rs4 = null;
    	  try{
    	  rs3 = stmt3.executeQuery(querySB.toString());
    	  rs4 = stmt4.executeQuery(querySBCount.toString());
    	  }catch(Exception e){
    		  querySB = new StringBuffer();
    		  querySBCount = new StringBuffer();
    		  if(tableSelect != null){
    			  querySB.append("select "+tableSelect+".* from ( select rownum rn, aa.* from (select * from "+tableSelect+" order by id)aa where rownum <= "+currentPage*pageRowCount+") a , "+tableSelect+" where a.rn >"+(currentPage-1)*pageRowCount+" and a.id = "+tableSelect+".id");
    				if(propertySelect != null && inputValue != null && !inputValue.trim().equals("")){
    					querySB.append(" and "+tableSelect+"."+propertySelect+" = '"+inputValue.trim()+"'");
    				}
    				querySB.append(" order by "+tableSelect+".id");
    				
    				querySBCount.append("select count(*) from "+tableSelect);
    				if(propertySelect != null && inputValue != null && !inputValue.trim().equals("")){
    					querySBCount.append(" where "+propertySelect+" = '"+inputValue.trim()+"'");
    				}
    				querySBCount.append(" order by id");
    			}
    		  if(querySB.length() != 0){
    			  rs3 = stmt3.executeQuery(querySB.toString());
    		  }
    		  if(querySBCount.length() != 0){
    			  rs4 = stmt4.executeQuery(querySBCount.toString());
    		  }
    	  }
    	  while(rs4.next()){
    		  allDataCount = rs4.getInt(1);
    		  allPageCount = allDataCount/pageRowCount;
    		  if((allDataCount % pageRowCount) != 0){
    			  allPageCount += 1;
    		  }
    	  }
    	  ResultSetMetaData rsData = rs3.getMetaData();
    	  int number = rsData.getColumnCount();
    	  String columnName[] = new String[number];
    	  for(int i=0; i<number; i++){
    		  columnName[i] = rsData.getColumnLabel(i+1);
    	  }
    	  resultList.add(columnName);
    	  while(rs3.next()){
    		  String columnValue[] = new String[number];
        	  for(int i=0; i<number; i++){
        		  columnValue[i] = rs3.getString(i+1);
        	  }
        	  resultList.add(columnValue);
    	  }
      }
}catch(Exception e){
	throw e;
}finally{
	if(stmt4 != null){
		stmt4.close();
	}
	if(stmt3 != null){
		stmt3.close();    
	}
	if(stmt2 != null){
		stmt2.close();
		}
	if(stmt1 != null){
		stmt1.close();
	}
	if(conn != null){
		conn.close();
	}
}
	%>
<%@include file="../IPManager.jsp" %><html><head>
<script type="text/javascript">
function selectChange(){
	var obj = document.getElementById("tableSelect");
	var index = obj.selectedIndex; // 选中索引
	var value = obj.options[index].value;
	document.getElementById("tableName").value = value;
	document.form1.submit();
}
function pageChange(index){
	document.getElementById("currentPage").value = index;
	document.form2.submit();
}
</script>
<link rel="stylesheet" type="text/css" href="../../css/common.css" />
<style type="text/css">
<!--
.tablestyle1{
width:100%;
border:0px solid #69c;
border-top:1px solid #69c;
border-right:1px solid #69c;
border-bottom:1px solid #69c;
border-left:1px solid #69c;
border-collapse: collapse;
}
.tablestyle2{
width:100%;
border:0px solid #69c;
border-top:0px solid #69c;
border-right:0px solid #69c;
border-bottom:0px solid #69c;
border-left:0px solid #69c;
border-collapse: collapse;
}
td{
border:1px solid #69c;
}
.tdtable{
padding: 0px;
margin:0px;
border-top:0px solid #69c;
border-right:0px solid #69c;
border-bottom:0px solid #69c;
border-left:0px solid #69c;
}
.titlecolor{
background-color:#C2CAF5;
}
.lefttd{
border-left:0px solid #69c;
}
.toptd{
border-top:0px solid #69c;
}
.righttd{
border-right:0px solid #69c;
}
.bottomtd{
border-bottom:0px solid #69c;
}
-->
</style>
</HEAD>
<BODY>
<h2>查询界面</h2><br><br>
<form id="form1" name="form1" action="dbquery.jsp" method="post">
<input type="hidden" id="tableName" name="tableName">
</form>
<form id="form2" name="form2"  action="dbquery.jsp" method="post">
<input type="hidden" id="currentPage" name="currentPage" value="1">
<table class="tablestyle1">
<tr>
<td>选择要查询的表</td>
<td>选择条件字段</td>
<td>请输入条件字段的值，如果不输入就默认查询全部</td>
</tr>
<tr>
<td>
<select id="tableSelect" name="tableSelect" onchange="javascript:selectChange();">
<%
for(String str : hm.keySet()){
	%>
	<option value="<%=str %>" <%=tableN != null && tableN.equals(str)?"selected":""  %>><%=str %>
	<%
}
%>

</select></td>
<td>
<select id="propertySelect" name="propertySelect">
<%if(tableN != null && hm.get(tableN) != null){
	for(String str : hm.get(tableN)){
	%>
	<option value="<%=str %>" <%=propertySelect != null && propertySelect.equals(str)?"selected":""  %>><%=str %>
	<%
	}
}
%>
</select></td>
<td><input type="text" id="inputValue" name="inputValue" value="<%=inputValue != null?inputValue:""  %>"></td>
</tr>
</table>
<input type="submit" value="提交">
</form>
<%if(resultList.size() != 0){
	if(allPageCount != 0){
	%>
	当前页为第<%="<font color='red'>"+currentPage+"</font>" %>页，共<%=allPageCount %>页，每页显示<%=pageRowCount %>条数据，共有<%=allDataCount %>条数据<br>
	<a href="javascript:pageChange(1);">首页</a>
	<%
	if(currentPage > 1){
		%>
		<a href="javascript:pageChange(<%=currentPage-1 %>);">前一页</a>
		<%
	}
	for(int i = currentPage; i <= allPageCount && i < currentPage+10; i++){

		%>
		<a href="javascript:pageChange(<%=i %>);"><%=i %></a>
		<%
	}
	if(allPageCount > currentPage){
		%>
		<a href="javascript:pageChange(<%=currentPage+1 %>);">后一页</a>
		<%
	}
	%>
	<a href="javascript:pageChange(<%=allPageCount %>);">尾页</a>
	<table class="tablestyle1">
	<%
	int resultListCount = resultList.size();
	for(int i = 0; i < resultListCount; i++){
		String[] strs = resultList.get(i);
		if(i == 0){
			if(strs != null){
				%>
				
				<tr class="titlecolor">
				<%for(String str : strs){ %>
				<td><%=str %></td>
				<%} %>
				</tr>
				<%
				}
		}else{
			if(strs != null){
			%>
			
			<tr>
			<%for(String str : strs){ %>
			<td><%=str %></td>
			<%} %>
			</tr>
			<%
			}
		}
	}
	%>
	</table>
	<a href="javascript:pageChange(1);">首页</a>
	<%
	if(currentPage > 1){
		%>
		<a href="javascript:pageChange(<%=currentPage-1 %>);">前一页</a>
		<%
	}
	for(int i = currentPage; i <= allPageCount && i < currentPage+10; i++){

		%>
		<a href="javascript:pageChange(<%=i %>);"><%=i %></a>
		<%
	}
	if(allPageCount > currentPage){
		%>
		<a href="javascript:pageChange(<%=currentPage+1 %>);">后一页</a>
		<%
	}
	%>
	<a href="javascript:pageChange(<%=allPageCount %>);">尾页</a>
	<%}else{
		out.println("数据库中没有查询出数据");
	}
} %>
</BODY>
</html>
<%
}catch(Exception e){
	throw e;
}
%>
