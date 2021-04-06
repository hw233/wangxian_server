<%@ page contentType="text/html;charset=utf-8" import="java.io.*,java.util.*,com.xuanzhi.tools.text.*,"%><%@ page import="com.xuanzhi.tools.gamestat.*" %>
<%
	String className = request.getParameter("className");
	if(className == null){
		className = RowData.class.getName();
	}
	Class<?> dataClass = Class.forName(className);
	
	GameStatService service = GameStatService.getInstance();
	
	String day = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
	
	if(service == null){
		service = new GameStatService();
		service.setDataRootPath(System.getProperty("resin.home")+"/webapps" + request.getContextPath()+"/gamestat");
		service.init();
		
		TestRowData td = new TestRowData();
		td.init();
		
		service.saveData(className,day,0,td.datas);
		
		td = new TestRowData();
		td.init();
		
		service.saveData(className,day,1,td.datas);
		
		td = new TestRowData();
		td.init();
		
		service.saveData(className,day,2,td.datas);
		
		td = new TestRowData();
		td.init();
		
		service.saveData(className,day,3,td.datas);
		
		td = new TestRowData();
		td.init();
		
		service.saveData(className,day,4,td.datas);
	}

	
	//////////////////////////////////////////////////////////////////////////////////////////////
	
	int start = 1;
	int end = 220;
	int step = 10;
	
	int userType = 0;
	String userTypeNames[] = GameStatService.userTypeNames;
	
	AnalyseTool at = new AnalyseTool(dataClass);
	String hiddenableHeaders[] = at.getHiddenableHeaders();
	String mergeableHeaders[] = at.getMergeableHeaders();
	
	String hiddenHeaders[] = hiddenableHeaders;
	String mergeHeaders[] = new String[]{};
	
	String action = request.getParameter("action");
	if(action != null && action.equals("submit")){
		day = request.getParameter("day");
		start = Integer.parseInt(request.getParameter("start"));
		end = Integer.parseInt(request.getParameter("end"));
		step = Integer.parseInt(request.getParameter("step"));
		
		userType = Integer.parseInt(request.getParameter("userType"));
		
		hiddenHeaders = request.getParameterValues("hiddenheader");
		if(hiddenHeaders == null) hiddenHeaders = new String[0];
		
		mergeHeaders = request.getParameterValues("mergeheader");
		if(mergeHeaders == null) mergeHeaders = new String[0];
		
	}
%>
<html>
<head>
</head>
<body>
<form><input type='hidden' name='action' value='submit'><input type='hidden' name='className' value='<%= dataClass.getName() %>'>
<table border='0' cellpadding='0' cellspacing='1' width='100%' bgcolor='#000000' align='left'>
<tr bgcolor='#FFFFFF'><td>天(yyyy-MM-dd)</td><td><input type='text' name='day' size=10 value='<%= day %>'></td></tr>
<tr bgcolor='#FFFFFF'><td>等级范围</td><td><input type='text' name='start' size=5 value='<%= start %>'> - <input type='text' name='end' size=5 value='<%= end %>'> 间隔 <input type='text' name='step' size=5 value='<%= step %>'></td></tr>
<tr bgcolor='#FFFFFF'><td>用户类型</td><td><%
	for(int i = 0 ; i < userTypeNames.length ; i++){
		out.print("<input type='radio' name='userType' value='"+i+"' "+(i == userType?"checked":"")+" >"+userTypeNames[i]+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
	}
%>
</td></tr>
<tr bgcolor='#FFFFFF'><td>隐藏项</td><td>
<%

	for(int i = 0 ; i < hiddenableHeaders.length ; i++){
		boolean checked = false;
		for(int j = 0 ; j < hiddenHeaders.length ; j++){
			if(hiddenHeaders[j].equals(hiddenableHeaders[i])){
				checked = true;
			}
		}
		out.print("<input type='checkbox' name='hiddenheader' value='"+hiddenableHeaders[i]+"' "+(checked?"checked":"")+" >"+hiddenableHeaders[i]+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
	}
%>
</td></tr>
<tr bgcolor='#FFFFFF'><td>合并项</td><td>
<%

	for(int i = 0 ; i < mergeableHeaders.length ; i++){
		boolean checked = false;
		for(int j = 0 ; j < mergeHeaders.length ; j++){
			if(mergeHeaders[j].equals(mergeableHeaders[i])){
				checked = true;
			}
		}
		out.print("<input type='checkbox' name='mergeheader' value='"+mergeableHeaders[i]+"' "+(checked?"checked":"")+" >"+mergeableHeaders[i]+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
	}
%>
</td></tr>
<tr bgcolor="#FFFFFF"><td ><input type='submit' value='按条件检索'></td><td><a href='./gamestat.jsp?className=<%= dataClass.getName() %>'>刷新本页</a></td></tr>
</table>
<br><br>
<%

	if(action != null && action.equals("submit")){
		Object[] datas = service.loadData(className,day,userType);
		WebTool wt = new WebTool(dataClass,datas);

		out.println(("<table border='0' cellpadding='0' cellspacing='1' width='100%' bgcolor='#000000' align='center'>"));
// 		out.print("hiddenHeaders:"+Arrays.toString(hiddenHeaders)+"--mergeHeaders："+Arrays.toString(mergeHeaders)+"--datas:"+datas.length);
		String s1 = wt.constructHTMLHeader(hiddenHeaders,mergeHeaders);
		
		if(datas.length == 0){
			out.println(s1);
		}else{
			start = start - 1;
			int k = (end - start)/step/50 + 1;
// 			out.print("className:"+className+"--day:"+day+"-userType:"+userType+"--datas:"+datas.length+"--start:"+start+"--k:"+k+"--step:"+step+"<br>");
			for(int i = 0 ; i < k ; i++){
				String s2 = wt.constructHTMLContent(hiddenHeaders,mergeHeaders,start,Math.min(end,start+50*step),step);
				start = Math.min(end,start+50*step);
				out.println(s1);
				out.println(s2);
				
				if(start >= end) break;
			}
		}
		out.println("</table>");
	}

%>
</form>
</body>
</html>