<%@ page contentType="text/html;charset=gb2312" 
import="java.util.*,com.xuanzhi.tools.text.*,com.xuanzhi.tools.statistics.*"%><%

	String className = request.getParameter("cl");

	boolean usingOracle = String.valueOf("true").equals(request.getParameter("uo")); 
	
	StatProject sp = StatProject.getStatProject(className);
	if(usingOracle)
		sp.setUsingOracle(usingOracle);
		
	String yearMonth = request.getParameter("year_month");
	if(yearMonth == null ) 
		yearMonth = (String)session.getAttribute("year_month");
	else
		session.setAttribute("year_month",yearMonth);	
	if(yearMonth == null) yearMonth = DateUtil.formatDate(new Date(),"yyyy_MM");
	
	HashMap<String,String> map = new HashMap<String,String>();	
	
	Enumeration en = request.getParameterNames();
	while(en.hasMoreElements()){
		String name = (String)en.nextElement();
		String value = request.getParameter(name);
		if(name != null && value != null && value.trim().length() > 0)
			map.put(name,value);
	}
	
	String content = WebUtils.updatePage(sp.getConnection(),sp.getTableName(yearMonth),sp.getDimensions(),sp.getStatData(),true,map);
%><html>
<head>
</head>
<body>
<center>
<b><%=sp.getName()%>统计项目更新界面</b><br/></center>
<form id="f"><input id='cl' name='cl' type='hidden' value='<%=className%>"'><input id='uo' name='uo' type='hidden' value='<%=usingOracle%>'>
请输入月份：<input type="text" size="20" name="year_month" value="<%=yearMonth==null?"":yearMonth%>">&nbsp;<input type="submit" value="提  交">&nbsp;格式yyyy_MM，比如2007_09，2007_10<br/>
</form><br>
<%=content%>
</body>
</html> 
