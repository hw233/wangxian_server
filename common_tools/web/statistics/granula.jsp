<%@ page contentType="text/html;charset=gb2312" 
import="java.util.*,com.xuanzhi.tools.text.*,com.xuanzhi.tools.statistics.*"%><%@ page import="sun.util.calendar.CalendarDate" %><%

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
	
	String granula = request.getParameter("granula");	
	Dimension ds[] = sp.getDimensions();
	HashMap<String,String> submitMap = new HashMap<String,String>();	
	
	Enumeration en = request.getParameterNames();
	while(en.hasMoreElements()){
		String name = (String)en.nextElement();
		String value = request.getParameter(name);
		if(name != null && value != null && value.trim().length() > 0)
			submitMap.put(name,value);
	}
	for(int i = 0 ; i < ds.length ; i++){
		String gs[] = ds[i].getAllGranula();
		for(int j = 0 ; j < gs.length ; j++){
			String value = submitMap.get(gs[j]);
			if(value != null && value.trim().length() > 0 && !value.equals(Dimension.LISTMARK)){
				try{
					ds[i].setValue(gs[j],value);
				}catch(IllegalArgumentException e){
				}
			}
		}
	}	
	String content = null;
	if(granula.equalsIgnoreCase("day")){
			StringBuffer sb = new StringBuffer();
			sb.append("<table border='0' cellpadding='0' cellspacing='1' width='100%' bgcolor='#000000' align='center'>");
			sb.append("<tr bgcolor='#00FFFF' align='center'>");
			sb.append("<td>天</td>");
			sb.append("<td>操  作</td>");
			sb.append("</tr>");
			
			Date date = DateUtil.parseDateSafely(yearMonth+"_01","yyyy_MM_dd");
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			
			int month = cal.get(Calendar.MONTH);
			while(cal.get(Calendar.MONTH) == month){
				sb.append("<tr bgcolor='#FFFFFF' align='center'>");
				StringBuffer javaScript = new StringBuffer();
				javaScript.append("");
				
				sb.append("<td>"+DateUtil.formatDate(cal.getTime(),"yyyyMMdd")+"</td>");
				javaScript.append("window.opener.document.getElementById('"+granula+"').value='"+DateUtil.formatDate(cal.getTime(),"yyyyMMdd")+"';");
					
				
				javaScript.append("window.close();");
				
				sb.append("<td><input type='button' value='选  择' onclick=\""+javaScript+"\"></td>");
				
				sb.append("</tr>");
				
				cal.add(Calendar.SECOND,24*3600+1);
			}
			sb.append("</table>");
			
			content = sb.toString();	
	}
	else
		content = WebUtils.getDimensionSelectTable(sp.getConnection(),sp.getTableName(yearMonth),ds,sp.getStatData(),granula);
%><html>
<head>
<SCRIPT   LANGUAGE="JavaScript">  
  var   isNav4,   isIE4;  
  if   (parseInt(navigator.appVersion.charAt(0))   >=   4)   {  
  isNav4   =   (navigator.appName   ==   "Netscape")   ?   1   :   0;  
  isIE4   =   (navigator.appName.indexOf("Microsoft")   !=   -1)   ?   1   :   0;  
  }  
  function   fitWindowSize()   {  
	  if   (isNav4)   {  
	  		window.innerWidth   =   document.layers[0].document.images[0].width;  
	  		window.innerHeight   =   document.layers[0].document.images[0].height;  
	  }  
	  if   (isIE4)   {  
		  window.resizeTo(500,   500);  
		  width   =   500   -   (document.body.clientWidth   -     document.images[0].width);  
		  height   =   500   -   (document.body.clientHeight   -     document.images[0].height);  
		  window.resizeTo(width,   height);  
	  }  
  }  
  </script></head>
<body>
<h2>请选择您希望的维度，粒度</h2><br/></center>
<br/>
<center><%=content%></center>
</body>
</html> 
