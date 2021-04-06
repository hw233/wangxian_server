<%@page import="com.xuanzhi.tools.text.LogbackConfigListener"%>
<%@page import="com.fy.engineserver.gametime.SystemTime"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="./autoInc.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%! boolean running = false; %>
<%
	String commandFile = "checkTimeOutArticle.sh";
	long start = SystemTime.currentTimeMillis();
	String logName = request.getParameter("logName");
	String day = request.getParameter("day");
	
	//out.print("日志名字:" + logName + "<BR/>");
	//out.print("系统换行符:[" + System.getProperty("line.separator") + "]<BR/>");
	
	String[][] outShow = null;
	String andFind = ""; 
	String orFind = ""; 
	String andExcept = ""; 
	String orExcept = ""; 
	
	int andFindPRI = -1;
	int orFindPRI = -1;
	int andExceptPRI = -1;
	int orExceptPRI = -1;

	List<QueryCondition> conditions = new ArrayList<QueryCondition>();
	if (isValid(day)) {
		if (running) {
			out.print("<h1>别人正在运行,一会儿在试</h1>");
			return;
		}
		running = true;
		
		andFind = request.getParameter("andFind");
		orFind = request.getParameter("orFind");
		andExcept = request.getParameter("andExcept");
		orExcept = request.getParameter("orExcept");
		andFindPRI = Integer.valueOf(request.getParameter("andFindPRI"));
		orFindPRI = Integer.valueOf(request.getParameter("orFindPRI"));
		andExceptPRI = Integer.valueOf(request.getParameter("andExceptPRI"));
		orExceptPRI = Integer.valueOf(request.getParameter("orExceptPRI"));
		
		if (isValid(andFind)) {
			QueryCondition qc = new QueryCondition(ConditionType.且查找,andFind,andFindPRI);
			conditions.add(qc);
			//out.print(ConditionType.且查找 + ":[" + Arrays.toString(qc.getConditions()) + "] [命令:"+qc.getPickledCommand()+"]<BR>");
		}
		if (isValid(orFind)) {
			QueryCondition qc = new QueryCondition(ConditionType.或查找,orFind,orFindPRI);
			conditions.add(qc);
			//out.print(ConditionType.或查找 + ":[" + Arrays.toString(qc.getConditions()) + "] [命令:"+qc.getPickledCommand()+"]<BR>");
		}
		if (isValid(andExcept)) {
			QueryCondition qc = new QueryCondition(ConditionType.且排除,andExcept,andExceptPRI);
			conditions.add(qc);
			//out.print(ConditionType.且排除 + ":[" + Arrays.toString(qc.getConditions()) + "] [命令:"+qc.getPickledCommand()+"]<BR>");
		}
		if (isValid(orExcept)) {
			QueryCondition qc = new QueryCondition(ConditionType.或排除,orExcept,orExceptPRI);
			conditions.add(qc);
			//out.print(ConditionType.或排除 + ":[" + Arrays.toString(qc.getConditions()) + "] [命令:"+qc.getPickledCommand()+"]<BR>");
		}
		String fileName = logName;
		if (day.equals(today())) {

		} else {
			fileName += ("." + day + ".log");
		}
		
		String contextPath = request.getRealPath("/").replace("webapps","log");//
		String logFileFullName = contextPath + fileName;
		
		//out.print("webContext根路径:[" + contextPath + "]<BR/>");
		//out.print("日志文件路径:[" + logFileFullName + "]<BR/>");
		
		CompoundReturn cr = getLogResult(contextPath,logFileFullName,conditions);
		if (cr.getBooleanValue()) {
			String[] tempArr = cr.getStringValues();
			outShow = new String[tempArr.length][];
			for (int i = 0; i < tempArr.length; i++) {
				String[] values = tempArr[i].split(" ");
				outShow[i] = values;//new String[] { values[1], values[2], values[3], values[9], values[10], values[11], values[12], values[13], values[14], values[17] };
			}
		} else {
			out.print("<h1>" + cr.getStringValue() + "</h1>");
		}
		running = false;		
	} else {
		out.print("<h1>无效的输入 day[" + day + "]</h1>");
	}
	out.print("服务器相应时间:" + (SystemTime.currentTimeMillis() - start) + "ms<BR/>");
%>
<%
	LinkedHashMap<String, String> logMap = new LinkedHashMap<String, String>();
	logMap.put("包裹日志", "knapsack.log");
	logMap.put("角色", "palyerManager.log");
	logMap.put("任务日志", "TaskSubSystem.log");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script language="javascript" type="text/javascript" src="./js/My97DatePicker/WdatePicker.js"></script>
<title>检查玩家物品</title>
</head>
<body style="font-size: 12px">
	<form action="./autoCheckLog.jsp" method="get">
		要查询的日志 <select name="logName">
			<% for (Iterator<String> itor = logMap.keySet().iterator();itor.hasNext();) {
							String showName = itor.next();
							String value = logMap.get(showName);
							%>
								<option value="<%=value %>" <%=value.equals(logName) ? "selected" :"" %>><%=showName %></option>			
							<%
						} %>
		</select>
	日期:<input type="text" name="day" value="<%=day==null ? today() : day%>" onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})"> 
		<input	type="submit" value="查询">
<div>
	<div style="float: left; border-style:solid; border-width:3px ; border-color:blue;">
		 <%=ConditionType.且查找 %>[查找条件"&"]<br/>
		 <textarea name="andFind" rows="4" cols="20"><%=andFind %></textarea>
		 <br/>执行优先级: 
		 <%
		 	for (int i = 0; i < ConditionType.values().length;i++) {
		 		boolean defaultCheck = andFindPRI == -1 ? (i==0 ? true : false) : (i+1 == andFindPRI);
		 		%>
		 			<%=i %>:<input name="andFindPRI" type="radio" value="<%=(i+1)%>" <%=defaultCheck ? "checked" : "" %>>
		 		<%
		 	}
		 %>
	</div>
	<div style="float: left; background-color: #BCBCBC;border-style:solid; border-width:3px ; border-color:blue;"">
		 <%=ConditionType.或查找 %>[查找条件"|"]<br/>
		 <textarea name="orFind" rows="4" cols="20"><%=orFind %></textarea>
		<br/>  执行优先级: 
		 <%
		 	for (int i = 0; i < ConditionType.values().length;i++) {
		 		boolean defaultCheck = orFindPRI == -1 ? (i==0 ? true : false) : (i+1 == orFindPRI);
		 		%>
		 			<%=i %>:<input name="orFindPRI" type="radio" value="<%=(i+1)%>" <%=defaultCheck ? "checked" : "" %>>
		 		<%
		 	}
		 %>
	</div>
	<div style="float: left;border-style:solid; border-width:3px ; border-color:red;"">
		 <%=ConditionType.且排除 %>[排除条件"&"]<br/>
		 <textarea name="andExcept" rows="4" cols="20"><%=andExcept %></textarea>
		  <br/> 执行优先级: 
		 <%
		 	for (int i = 0; i < ConditionType.values().length;i++) {
		 		boolean defaultCheck = andExceptPRI == -1 ? (i==0 ? true : false) : (i+1 == andExceptPRI);
		 		%>
		 			<%=i %>:<input name="andExceptPRI" type="radio" value="<%=(i+1)%>" <%=defaultCheck ? "checked" : "" %>>
		 		<%
		 	}
		 %>
	 </div>
	<div style="float: left;background-color: #BCBCBC;border-style:solid; border-width:3px ; border-color:red;"">
		 <%=ConditionType.或排除 %>[排除条件"|"]<br/>
		 <textarea name="orExcept" rows="4" cols="20"><%=orExcept %></textarea>
		   <br/> 执行优先级: 
		 <%
		 	for (int i = 0; i < ConditionType.values().length;i++) {
		 		boolean defaultCheck = orExceptPRI == -1 ? (i==0 ? true : false) : (i+1 == orExceptPRI);
		 		%>
		 			<%=i %>:<input name="orExceptPRI" type="radio" value="<%=(i+1)%>" <%=defaultCheck ? "checked" : "" %>>
		 		<%
		 	}
		 %>
	</div>
</div>
	</form>
	<%
		if (outShow != null) {
	%>
	<table style="font-size: 12px">
		<%
			for (int i = 0; i < outShow.length; i++) {
		%>
		<tr style="background-color: <%=(i % 2 == 0) ? "#BCBCBC" : ""%>">
			<%
				for (int k = 0; k < outShow[i].length; k++) {
			%>
			<td style="background-color: <%=(k % 2 == 0) ? "#BCBCBC" : ""%>"><%=outShow[i][k]%></td>
			<%
				}
			%>
		</tr>
		<%
			}
		%>
	</table>
	<%
		}
	%>
	<hr>
	<%out.print("页面响应时间:" + (SystemTime.currentTimeMillis() - start) + "ms<BR/>"); %>
</body>
</html>