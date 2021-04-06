<%@ page contentType="text/html;charset=gb2312" 
import="java.io.*,java.util.*,com.xuanzhi.tools.text.*,com.xuanzhi.tools.statistics.*"%>
<%@ page import="com.xuanzhi.tools.statistics.depot.*" %><%!
/**
 * 页面上的输入表单：
 * 		此表单中mark为一个checkbox
 * 
 * @param ds
 * @param attachment
 * @param selectButton
 * @param markButton
 * @param selectDimensionPage
 * @return
 */
public static String getDimensionInputTable(Dimension ds[],boolean attachment,boolean selectButton,boolean markButton,String selectDimensionPage){
	
	int max_column = 6;
	if(selectButton == true && markButton == true){
		max_column = 4;
	}
	
	if(ds.length > max_column){
		Dimension ds2[] = new Dimension[max_column];
		Dimension ds3[] = new Dimension[ds.length - max_column];
		System.arraycopy(ds,0,ds2,0,max_column);
		System.arraycopy(ds,max_column,ds3,0,ds3.length);
		return getDimensionInputTable(ds2,attachment,selectButton,markButton,selectDimensionPage)
		+ "<br/>" + getDimensionInputTable(ds3,attachment,selectButton,markButton,selectDimensionPage);
	}
	int width = 100/max_column;
	StringBuffer sb = new StringBuffer();
	sb.append("<table border='0' cellpadding='0' cellspacing='1' width='100%' bgcolor='#000000' align='center'>");
	sb.append("<tr bgcolor='#00FFFF' align='center'>");
	for(int i = 0 ; i < max_column ; i++){
		if(i < ds.length){
			if(ds[i].isCanRangeSelect()){
				sb.append("<td width='"+width+"%'>"+ds[i].getName()+"维度(支持范围)</td>");
			}else{
				sb.append("<td width='"+width+"%'>"+ds[i].getName()+"维度</td>");
			}
		}
		else
			sb.append("<td  width='"+width+"%'>-</td>");
	}
	sb.append("</tr>");
	for(int index = 0 ; ; index++){
		boolean wouldstop = true;
		for(int i = 0 ; i < ds.length ; i++){
			int an = ds[i].getAttachmentNames().length;
			if(attachment == false){
				if(index < ds[i].getNumOfGranula())
					wouldstop = false;
			}else{
				if(index < ds[i].getNumOfGranula() + an)
					wouldstop = false;
			}
		}
		if(wouldstop) break;
		
		sb.append("<tr bgcolor='#FFFFFF' align='right'>");
		for(int i = 0 ; i < max_column ; i++){
			if(i < ds.length){
				String an[] = ds[i].getAttachmentNames();
				if(index < ds[i].getNumOfGranula()){
					String granula = ds[i].getGranula(index);
					sb.append("<td nowrap  width='"+width+"%'>");
					sb.append(ds[i].getGranulaDisplayName(granula)+":");
					String value = ds[i].getValue(granula);
					
					if(value == null) {
						/*if(granula.equals("day")){
							value = DateUtil.formatDate(new Date(),"yyyyMMdd");
						}else*/
						value = "";
					}
					
					sb.append("<input name='"+granula+"' id='"+granula+"' type='text' size='7' value='"+value+"'>");

					if(markButton){
						sb.append("<input type='checkbox' id='"+granula+"_listmark' name='"+granula+"_listmark' value='true' "+(ds[i].isListMark(granula)?"checked":"")+" >展开");
					}

					if(selectButton && ds[i].isCanListMark()){
						
						if(selectDimensionPage != null){
							String selectPageUrl = selectDimensionPage;
							if(selectDimensionPage.indexOf("?") == -1){
								selectPageUrl += "?granula="+granula;
							}else{
								selectPageUrl += "&granula="+granula;
							}
							//window.open('"+selectPageUrl+"','selectgranula','height=500, width=500, top=150, left=262,scrollbars=yes')\">
							sb.append("<input type='button' value='选择' onclick=\"return open_granula('"+selectPageUrl+"')\">");
						}
						else
							sb.append("<input type='button' value='选择' onclick=\"on_granula_select('"+granula+"')\">");
					}
					sb.append("</td>");
				}else if(attachment && index < ds[i].getNumOfGranula() + an.length){
					String granula = an[index - ds[i].getNumOfGranula()];
					sb.append("<td width='"+width+"%'>");
					sb.append(ds[i].getGranulaDisplayName(granula)+":");
					Object value = ds[i].getAttachment(granula);
					if(value == null) value = "";
					
					sb.append("<input name='"+granula+"' id='"+granula+"' type='text' size='7' value='"+value+"'>");
					sb.append("</td>");
				}else{
					sb.append("<td width='"+width+"%'></td>");
				}
			}else{
				sb.append("<td width='"+width+"%'>&nbsp;</td>");
			}
		}
		sb.append("</tr>");
	}
	sb.append("</table>");
	
	return sb.toString();
}
%><%

	String className = request.getParameter("cl");
	DepotProject sp = DepotProject.getDepotProject(className);

	String yearMonth = request.getParameter("month");
	if(yearMonth == null || yearMonth.equals(Dimension.LISTMARK)) yearMonth = DateUtil.formatDate(new Date(),"yyyyMM");
	
	HashMap<String,String> submitMap = new HashMap<String,String>();	
	
	Enumeration en = request.getParameterNames();
	while(en.hasMoreElements()){
		String name = (String)en.nextElement();
		String value = request.getParameter(name);
		if(name != null && value != null && value.trim().length() > 0)
			submitMap.put(name,value);
	}
	if(submitMap.get("month") == null){
		submitMap.put("month",yearMonth);
	}

	String unAG[] = sp.getUnAccumulateGranulas();
	for(int i = 0 ; unAG != null && i < unAG.length ; i++){
		if(!submitMap.containsKey(unAG[i])){
			submitMap.put(unAG[i],Dimension.LISTMARK);
		}
	}
	
	File tmpFile = sp.getQuickLinkFile();
	if(tmpFile != null)
		WebUtils.updateQuickLinks(tmpFile,request);
	
	int column = 6;
	
	String selectPage = "./new_granula.jsp?cl="+sp.getSelectGranulaProjectClassName();
	
		Properties props = (Properties)session.getAttribute(com.xuanzhi.tools.servlet.AuthorizedServletFilter.PROPERTIES);
		if(props == null) props = new Properties();
		
		Dimension ds[] = sp.getDimensions();
		long startTime = System.currentTimeMillis();
		StringBuffer errorMessage = new StringBuffer();
		
		StringBuffer limitCondition = new StringBuffer();
		
		String filterCondition = "";
		
		for(int i = 0 ; i < ds.length ; i++){
			String gs[] = ds[i].getAllGranula();
			for(int j = 0 ; j < gs.length ; j++){
				String tmp = submitMap.get(gs[j]+"_listmark");
				if(tmp != null && tmp.equals("true")){
					ds[i].setListMark(gs[j]);
				}
				
				String value = submitMap.get(gs[j]);
				if(value != null && value.trim().length() > 0){
					try{
						ds[i].setValue(gs[j],value);
					}catch(IllegalArgumentException e){
						errorMessage.append("设置维度["+ds[i].getName()+"]的粒度[" + ds[i].getGranulaDisplayName(gs[j]) +"]出错，必须先设置大粒度，然后才能设置小粒度\n");
					}
				}

				
				if(props.containsKey(gs[j])){

					String v = props.getProperty(gs[j]);

					limitCondition.append("维度["+ds[i].getName()+"]的粒度[" + ds[i].getGranulaDisplayName(gs[j]) +"]的限制值："+v+"\n");

					String ss[] = v.split(",");
					value = ds[i].getValue(gs[j]);
					if(ss.length == 1 && !ss[0].equals(Dimension.LISTMARK)){
						ds[i].setValue(gs[j],ss[0]);
					}
					else if(value == null){
						String filter = "";
						for(int k = 0; k < ss.length ; k++){
							if(filter.length() == 0) 
								filter = gs[j] + "='"+ss[k]+"'";
							else
								filter += " or " + gs[j] + "='"+ss[k]+"'";
						}
						if(filter.length() > 0) filter = "(" + filter+")";
						
						if(filterCondition.length() == 0)
							filterCondition = filter;
						else
							filterCondition += " and " + filter;

					}else{

						boolean b = false;
						for(int k = 0; k < ss.length ; k++){
							if(ss[k].equals(value)) b = true;
						}
						if(b == false){
						errorMessage.append("设置维度["+ds[i].getName()+"]的粒度[" + ds[i].getGranulaDisplayName(gs[j]) +"]出错，你的权限只能设置["+v+"]，其他值你无权设置");	
						}
					}
				}
			}
		}
		
		
	
		StringBuffer sb  = new StringBuffer();
		sb.append("<form id='form_submit' name='form_submit'>");
		sb.append("<input id='submitted' name='submitted' type='hidden' value='true'>");
		sb.append("<input id='cl' name='cl' type='hidden' value='"+className+"'>");
		sb.append(getDimensionInputTable(ds,false,true,true,selectPage));
		sb.append("\n<br/>");
		sb.append("<input type='submit' value='查    询'></form>");
		sb.append("\n<br/>");
		sb.append("\n<br/>");
		
		boolean submit = false;
		if(String.valueOf("true").equals(submitMap.get("submitted")))
			submit = true;
		
		if(submit && errorMessage.length() == 0){
			String filterExpression = submitMap.get("filterexpression");
			String orderExpression = submitMap.get("orderexpression");
			boolean desc = ("0".equals(submitMap.get("desc")));
			
			if(filterCondition.length() > 0)
			{
				if(filterExpression != null && filterExpression.trim().length() > 0)
					filterExpression = filterCondition + " and (" + filterExpression + ")";
				else
					filterExpression = filterCondition;
			}
			try{
				Map<Map<String,String>,StatData> resultMap = sp.query(yearMonth,ds,sp.getStatData(),filterExpression,orderExpression,desc);
				
				sb.append("<h3>查询结果如下，共"+resultMap.size()+"条记录，耗时 "+((System.currentTimeMillis() - startTime)/1000f)+" 秒：</h3><br/>");
				sb.append(WebUtils.getTableOfQueryResult(ds,sp.getDisplayStatData(),resultMap,true));
				
			}catch(Exception e){
				sb.append("<h3 color=red>查询出错，耗时 "+((System.currentTimeMillis() - startTime)/1000f)+" 秒，详细信息如下，请保留页面联系技术人员解决：</h3><br/>");
				ByteArrayOutputStream bout = new ByteArrayOutputStream();
				e.printStackTrace(new PrintStream(bout));
				sb.append("<pre>");
				sb.append(new String(bout.toByteArray()));
				sb.append("</pre>");
			}
		}else if(errorMessage.length() > 0){
			sb.append("<h3 color=red>输入不完整，请参考错误信息：</h3><br/>");
			sb.append("<pre>");
			sb.append(errorMessage.toString());
			sb.append("</pre>");
		}else if(limitCondition.length() > 0){
			sb.append("<h3 color=red>当前用户为"+session.getAttribute(com.xuanzhi.tools.servlet.AuthorizedServletFilter.USERNAME)+"，查询限制如下</h3><br/>");
			sb.append("<pre>");
                        sb.append(limitCondition.toString());
                        sb.append("</pre>");
		}
	
%><html>
<head>
<style type="text/css">
@charset "GBK";
body,td,th {font-size: 14px;}
a:link {color: #006699; text-decoration: none;}
a:visited {color: #006699; text-decoration: none;}
a:hover { color: #0000FF; text-decoration: underline;}
a:active { text-decoration: none; color: #0000FF;}
.redDot {color: #FF0000; font-size: 12px;}
/* Sortable tables */
table.sortable a.sortheader {
    background-color:#eee;
    color:#666666;
    font-weight: bold;
    text-decoration: none;
    display: block;
}
table.sortable span.sortarrow {
    color:black;
    text-decoration: none;
}
</style>
<SCRIPT src="./sorttable.js" type="text/javascript">
</SCRIPT>

<SCRIPT type="text/javascript">
function open_granula(granulapage){
		var value;
<%
	for(int i = 0 ; i < ds.length ; i++){
		String gs[] = ds[i].getAllGranula();
		for(int j = 0 ; j < gs.length ; j++){
			out.println("value = document.getElementById('"+gs[j]+"').value;");
			out.println("if(value != null && value.length > 0 && value != '***'){granulapage+='&"+gs[j]+"='+value;}");
		}
	}
%>
		window.open(granulapage,'selectgranula','height=500, width=500, top=150, left=262,scrollbars=yes');
		return true;
	} 
</SCRIPT>
</head>
<body>
<center>
<b><%=sp.getName()%>统计项目查询界面</b><br/></center>
<%
	if(tmpFile != null){
		out.println("<center>"+WebUtils.getTableOfQuickLinks(className,tmpFile,column,submit,request.getQueryString())+"<a href='./txtEdit.jsp?fn="+tmpFile.getAbsolutePath()+"'>修改快速查询链接</a></center>");
	}
%>
<center><%=sb.toString()%></center>
</body>
</html> 
