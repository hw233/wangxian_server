<%@ page contentType="text/html;charset=gb2312" import="java.util.*,com.xuanzhi.tools.text.*,com.xuanzhi.tools.statistics.*"%><%@ page import="java.util.Calendar" %><%

	String className = request.getParameter("cl");
	boolean usingOracle = String.valueOf("true").equals(request.getParameter("uo")); 
	
	final StatProject sp = StatProject.getStatProject(className);
	if(usingOracle)
		sp.setUsingOracle(usingOracle);
	
	Date d = new Date(System.currentTimeMillis() - 24 * 3600 * 1000L);
	String yearMonth = DateUtil.formatDate(d,"yyyy_MM");
	String dayStr = DateUtil.formatDate(d,"yyyyMMdd");
	
	if(request.getParameter("yearMonth") != null){
		yearMonth = request.getParameter("yearMonth").trim();
	}
	
	if(request.getParameter("dayStr") != null){
		dayStr = request.getParameter("dayStr").trim();
	}
		
%><html>
<head>
</head>
<body>
<br><h2><%=sp.getName()%>项目表的生成(<%=yearMonth+","+dayStr%>)</h2>
<br/>
<form id="f"><input type='hidden' name='cl' value='<%=className%>'>
<input type='hidden' name='uo' value='<%=usingOracle%>'>
请输入月份：<input type="type" size="20" name="yearMonth" value="<%=yearMonth%>">比如2007_11<br/>
请输入日期：<input type="type" size="20" name="dayStr" value="<%=dayStr%>">比如20071105<br/>
请输入口令：<input type="password" size="20" name="passwd" value="">&nbsp;<input type="submit" value="提  交">&nbsp;<br/>
</form><br>
<%
	String passwd = request.getParameter("passwd");
	
	String errorMessage = null;
	if(passwd != null && passwd.equals("@xuanzhi")){

		if(!dayStr.startsWith(yearMonth.replace("_",""))){
			throw new IllegalArgumentException("dayStr["+dayStr+"] not match the yearMonthStr["+yearMonth+"]");
		}
		long startTime = System.currentTimeMillis();
		out.println("<center>快照已经启动，正在执行，请查看相关日志</center>");
		out.flush();
		final String _yearMonth = yearMonth;
		final String _dayStr = dayStr;
		Thread thread = new Thread(new Runnable(){
			public void run(){
				sp.dataConstruct(_yearMonth,_dayStr);
				//System.out.println("[snapshot] ["+sp.getName()+"] ["+yearMonth+"] ["+dayStr+"] [finished]");
			}
		},sp.getName() + "-SnapShot-Thread");
		thread.start();
	}else if(passwd != null){
		out.println("<center>口令错误</center>");
	}
%>
</body>
</html> 
