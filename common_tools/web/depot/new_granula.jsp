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
	
	int action = 1;
	if(request.getParameter("select_action") != null){
		action = Integer.parseInt(request.getParameter("select_action"));
	}
	String sql = null;
	try{
		sql = DepotSQLConstructor.constructGranulaSelectSQL(sp,d,granula);
		Map<Map<String,String>,StatData> resultMap = DepotTool.execute_query(sp.getConnection(),sql,ds,sp.getStatData());
		
		sb.append("请选择数值类型：<form id='f3'>\n");
		sb.append("<input type='hidden' name='cl' value='"+className+"'>");
		if(dimensionName != null && dimensionName.trim().length()>0)
			sb.append("<input type='hidden' name='dimension' value='"+dimensionName+"'>");
		sb.append("<input type='hidden' name='granula' value='"+granula+"'>");
		for(int j = 0 ; j < gs.length ; j++){
			if(d.getValue(gs[j]) != null){
				sb.append("<input type='hidden' name='"+gs[j]+"' value='"+d.getValue(gs[j])+"'>");
			}
		}
		sb.append("<select id='select_action' name='select_action' onchange='return f3.submit();'>");
		sb.append("<option value='1' "+(action==1?"selected":"")+" >-- 单选 --</option>");
		if(d.isCanMultiSelect())
			sb.append("<option value='2' "+(action==2?"selected":"")+" >-- 多选 --</option>");
		if(d.isCanRangeSelect())
			sb.append("<option value='3' "+(action==3?"selected":"")+" >-- 范围 --</option>");
		if(d.isCanStartWith())
			sb.append("<option value='4' "+(action==4?"selected":"")+" >-- 前缀 --</option>");
		sb.append("</select>&nbsp;<br/>");
		
		if(action == 1){
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
					if(d.getValue(gs[j]) == null)
						javaScript.append("window.opener.document.getElementById('"+gs[j]+"').value='"+key.get(gs[j])+"';");
					if(gs[j].equals(granula)) break;
				}
				javaScript.append("window.close();");
				
				sb.append("<td><input type='button' value='选  择' onclick=\""+javaScript+"\"></td>");
				
				sb.append("</tr>");
			}
			sb.append("</table>");
		}else if(action == 2){
			sb.append("<table border='0' cellpadding='0' cellspacing='1' width='100%' bgcolor='#000000' align='center'>");
			sb.append("<tr bgcolor='#00FFFF' align='center'>");
			
			for(int i = 0 ; i < gs.length ; i++){
				sb.append("<td>"+d.getGranulaDisplayName(gs[i])+"</td>");
				if(gs[i].equals(granula)) break;
			}
			sb.append("<td>标 记</td>");
			sb.append("</tr>");
			Iterator<Map<String,String>> it = resultMap.keySet().iterator();
			while(it.hasNext()){
				Map<String,String> key = it.next();
				sb.append("<tr bgcolor='#FFFFFF' align='center'>");
				StringBuffer values = new StringBuffer();
				for(int j = 0 ; j < gs.length ; j++){
					sb.append("<td>"+d.getDisplayValue(gs[j],key.get(gs[j]))+"</td>");
					values.append(key.get(gs[j])+";");
					if(gs[j].equals(granula)) break;
				}
				sb.append("<td><input type='checkbox' id='check_box' name='check_box' value='"+values.toString().substring(0,values.length()-1)+"'></td>");
				
				sb.append("</tr>");
			}
			
			sb.append("<tr bgcolor='#FFFFFF' align='center'>");
			for(int i = 0 ; i < gs.length ; i++){
				sb.append("<td></td>");
				if(gs[i].equals(granula)) break;
			}
			StringBuffer javaScript = new StringBuffer();
			javaScript.append("var checkboxs = document.getElementsByName('check_box');\n");
			for(int j = 0 ; j < gs.length ; j++){
				if(d.getValue(gs[j]) == null){
					javaScript.append("for(i=0;i<checkboxs.length;i++){\n");
					javaScript.append("if(checkboxs[i].checked){\n");
					javaScript.append("var ss = checkboxs[i].value.split(';');\n");
					javaScript.append("if(window.opener.document.getElementById('"+gs[j]+"').value=='')\n");
					javaScript.append("\twindow.opener.document.getElementById('"+gs[j]+"').value=ss["+j+"];\n");
					javaScript.append("else{\n");
					javaScript.append("var ss2 = window.opener.document.getElementById('"+gs[j]+"').value.split(',');\n");
					javaScript.append("var bf = false;\n");
					javaScript.append("for(j=0;j<ss2.length;j++){\n");
					javaScript.append("\tif(ss2[j]==ss["+j+"]){bf=true;break;}\n");
					javaScript.append("}\n");
					javaScript.append("if(bf==false)\n");
					javaScript.append("\twindow.opener.document.getElementById('"+gs[j]+"').value+=','+ss["+j+"];");
					javaScript.append("}\n");
					javaScript.append("}\n");
					javaScript.append("}\n");
				}
				if(gs[j].equals(granula)) break;
			}
			javaScript.append("window.close();");
			sb.append("<td><input type='button' value='选  择' onclick=\""+javaScript+"\"></td>");
			sb.append("</tr>");
			sb.append("</table>");
		}else if(action == 3){
			sb.append("<b>请选择范围的开始值：</b>");
			sb.append("<table border='0' cellpadding='0' cellspacing='1' width='100%' bgcolor='#000000' align='center'>");
			sb.append("<tr bgcolor='#00FFFF' align='center'>");
			
			for(int i = 0 ; i < gs.length ; i++){
				sb.append("<td>"+d.getGranulaDisplayName(gs[i])+"</td>");
				if(gs[i].equals(granula)) break;
			}
			sb.append("<td>标 记</td>");
			sb.append("</tr>");
			Iterator<Map<String,String>> it = resultMap.keySet().iterator();
			while(it.hasNext()){
				Map<String,String> key = it.next();
				sb.append("<tr bgcolor='#FFFFFF' align='center'>");
				StringBuffer values = new StringBuffer();
				for(int j = 0 ; j < gs.length ; j++){
					sb.append("<td>"+d.getDisplayValue(gs[j],key.get(gs[j]))+"</td>");
					values.append(key.get(gs[j])+";");
					if(gs[j].equals(granula)) break;
				}
				sb.append("<td><input type='radio' id='start_radio_box' name='start_radio_box' value='"+values.toString().substring(0,values.length()-1)+"'></td>");
				sb.append("</tr>");
			}
			sb.append("</table><br/>");
			
			
			sb.append("<b>请选择范围的结束值：</b>");
			sb.append("<table border='0' cellpadding='0' cellspacing='1' width='100%' bgcolor='#000000' align='center'>");
			sb.append("<tr bgcolor='#00FFFF' align='center'>");
			
			for(int i = 0 ; i < gs.length ; i++){
				sb.append("<td>"+d.getGranulaDisplayName(gs[i])+"</td>");
				if(gs[i].equals(granula)) break;
			}
			sb.append("<td>标 记</td>");
			sb.append("</tr>");
			it = resultMap.keySet().iterator();
			while(it.hasNext()){
				Map<String,String> key = it.next();
				sb.append("<tr bgcolor='#FFFFFF' align='center'>");
				StringBuffer values = new StringBuffer();
				for(int j = 0 ; j < gs.length ; j++){
					sb.append("<td>"+d.getDisplayValue(gs[j],key.get(gs[j]))+"</td>");
					values.append(d.getDisplayValue(gs[j],key.get(gs[j]))+";");
					if(gs[j].equals(granula)) break;
				}
				sb.append("<td><input type='radio' id='end_radio_box' name='end_radio_box' value='"+values.toString().substring(0,values.length()-1)+"'></td>");
				sb.append("</tr>");
			}
			sb.append("</table>");
			
			StringBuffer javaScript = new StringBuffer();
			javaScript.append("var startradioboxs = document.getElementsByName('start_radio_box');\n");
			for(int j = 0 ; j < gs.length ; j++){
				if(d.getValue(gs[j]) == null){
					javaScript.append("for(i=0;i<startradioboxs.length;i++){\n");
					javaScript.append("if(startradioboxs[i].checked){\n");
					javaScript.append("var ss = startradioboxs[i].value.split(';');\n");
					javaScript.append("\twindow.opener.document.getElementById('"+gs[j]+"').value=ss["+j+"];\n");
					javaScript.append("}\n");
					javaScript.append("}\n");
				}
				if(gs[j].equals(granula)) break;
			}
			
			javaScript.append("var end_radio_box = document.getElementsByName('end_radio_box');\n");
			for(int j = 0 ; j < gs.length ; j++){
				if(d.getValue(gs[j]) == null){
					javaScript.append("for(i=0;i<end_radio_box.length;i++){\n");
					javaScript.append("if(end_radio_box[i].checked){\n");
					javaScript.append("var ss = end_radio_box[i].value.split(';');\n");
					javaScript.append("if(window.opener.document.getElementById('"+gs[j]+"').value != ss["+j+"])\n");
					javaScript.append("\twindow.opener.document.getElementById('"+gs[j]+"').value+='~'+ss["+j+"];\n");
					javaScript.append("}\n");
					javaScript.append("}\n");
				}
				if(gs[j].equals(granula)) break;
			}
			
			javaScript.append("window.close();");
			sb.append("\n<SCRIPT LANGUAGE='JavaScript'>\nfunction do_range_select(){\n"+javaScript+"\n}\n</SCRIPT>\n");
			sb.append("<input type='button' value='提交范围选择' onclick='do_range_select()'>");
		}else if(action == 4){
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
					if(d.getValue(gs[j]) == null){
						if(gs[j].equals(granula) == false)
							javaScript.append("window.opener.document.getElementById('"+gs[j]+"').value='"+key.get(gs[j])+"';");
						else
							javaScript.append("window.opener.document.getElementById('"+gs[j]+"').value='"+key.get(gs[j])+"*';");
					}
					if(gs[j].equals(granula)) break;
				}
				javaScript.append("window.close();");
				
				sb.append("<td><input type='button' value='选  择' onclick=\""+javaScript+"\"></td>");
				
				sb.append("</tr>");
			}
			sb.append("</table>");
		}else{
			sb.append("错误，不能识别的数值类型！");
		}
		
	}catch(Exception e){
		sb.append("<h3 color=red>查询出错，详细信息如下，请保留页面联系技术人员解决,SQL is：</h3><br/>");
		sb.append("<i>"+sql+"<i/><br/>");
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
