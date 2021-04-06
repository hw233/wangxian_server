<%@ page contentType="text/html;charset=gb2312" 
import="java.util.*,com.xuanzhi.tools.text.*,
com.xuanzhi.tools.statistics.*,com.xuanzhi.tools.statistics.depot.*"%><%

	String className = request.getParameter("cl");
	DepotProject sp = DepotProject.getDepotProject(className);
	String dimensionName = request.getParameter("dimension");
	String granula = request.getParameter("granula");	
	Dimension ds[] = sp.getDimensions();
	Dimension d = null;
	for(int i = 0 ; i < ds.length ; i++){
		if(dimensionName != null){
			if(ds[i].getDimensionUniqueName().equals(dimensionName)){
				d = ds[i];
				break;
			}
		}else{
			if(ds[i].isGranula(granula)){
				d = ds[i];
				break;
			}
		}
	}
	
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
	
	//content = WebUtils.getDimensionSelectTable(sp.getConnection(),sp.getTableName(yearMonth),ds,sp.getStatData(),granula);
	String gs[] = d.getAllGranula();
	boolean b = false;
	for(int j = 0 ; j < gs.length ; j++){
		if(b == false && d.getValue(gs[j]) == null){
			d.setListMark(gs[j]);
		}
		if(gs[j].equals(granula)) b = true;
		if(b){
			d.setValue(gs[j],null);
		}
	}
	
	StringBuffer sb = new StringBuffer();
	try{
		String sql = DepotSQLConstructor.constructGranulaSelectSQL(sp,d,granula);
		Map<Map<String,String>,StatData> resultMap = DepotTool.execute_query(sp.getConnection(),sql,ds,sp.getStatData());
		
		sb.append("<table border='0' cellpadding='0' cellspacing='1' width='100%' bgcolor='#000000' align='center'>");
		sb.append("<tr bgcolor='#00FFFF' align='center'>");
		
		for(int i = 0 ; i < gs.length ; i++){
			sb.append("<td>"+d.getGranulaDisplayName(gs[i])+"</td>");
			if(gs[i].equals(granula)) break;
		}
		sb.append("<td>操  作</td>");
		sb.append("</tr>");
		Iterator<Map<String,String>> it = resultMap.keySet().iterator();
		while(it.hasNext()){
			Map<String,String> key = it.next();
			sb.append("<tr bgcolor='#FFFFFF' align='center'>");
			StringBuffer javaScript = new StringBuffer();
			javaScript.append("");
			for(int j = 0 ; j < gs.length ; j++){
				sb.append("<td>"+d.getDisplayValue(gs[j],key.get(gs[j]))+"</td>");
				javaScript.append("window.opener.document.getElementById('"+gs[j]+"').value='"+key.get(gs[j])+"';");
				if(gs[j].equals(granula)) break;
			}
			javaScript.append("window.close();");
			
			sb.append("<td><input type='button' value='选  择' onclick=\""+javaScript+"\"></td>");
			
			sb.append("</tr>");
		}
		sb.append("</table>");
	}catch(Exception e){
		sb.append("<h3 color=red>查询出错，详细信息如下，请保留页面联系技术人员解决：</h3><br/>");
		java.io.ByteArrayOutputStream bout = new java.io.ByteArrayOutputStream();
		e.printStackTrace(new java.io.PrintStream(bout));
		sb.append("<pre>");
		sb.append(new String(bout.toByteArray()));
		sb.append("</pre>");
	}
	
	content = sb.toString();
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
