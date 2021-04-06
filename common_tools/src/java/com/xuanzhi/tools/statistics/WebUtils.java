package com.xuanzhi.tools.statistics;

import java.util.*;
import java.util.Date;
import java.io.*;
import java.lang.reflect.Constructor;
import java.sql.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import com.xuanzhi.tools.text.DateUtil;

public class WebUtils {

	public static String updatePage(Connection conn,String tableName,Dimension ds[],StatData template,boolean accumulate,HashMap<String,String> submitMap){
		
		StringBuffer errorMessage = new StringBuffer();
		
		boolean submit = false;
		if(String.valueOf("true").equals(submitMap.get("submitted")))
			submit = true;
		
		for(int i = 0 ; submit && i < ds.length ; i++){
			
			String gs[] = ds[i].getAllGranula();
			for(int j = 0 ; j < gs.length ; j++){
				String value = submitMap.get(gs[j]);
				if(value != null && value.trim().length() > 0){
					try{
						ds[i].setValue(gs[j],value);
					}catch(IllegalArgumentException e){
						errorMessage.append("设置维度["+ds[i].getName()+"]的粒度[" + ds[i].getGranulaDisplayName(gs[j]) +"]出错，必须先设置大粒度，然后才能设置小粒度\n");
					}
				}else{
					errorMessage.append("请设置维度["+ds[i].getName()+"]的粒度[" + ds[i].getGranulaDisplayName(gs[j]) +"]的值\n");
				}
			}
			String as[] = ds[i].getAttachmentNames();
			for(int j = 0 ; submit && j < as.length ; j++){
				String value = submitMap.get(as[j]);
				Object object = ds[i].getAttachment(as[j]);
				if(object == null) object = "null";
				if(value != null && value.trim().length() > 0){
					
					try{
						Constructor c = object.getClass().getConstructor(new Class[]{String.class});
						object = c.newInstance(new Object[]{value});
						ds[i].setAttachment(as[j],object);
					}catch(Exception e){
						errorMessage.append("维度["+ds[i].getName()+"]的属性[" + ds[i].getGranulaDisplayName(as[j]) +"]的值格式不对，必须为["+object.getClass()+"]类型\n");
					}
				}else{
					errorMessage.append("请维度["+ds[i].getName()+"]的属性[" + ds[i].getGranulaDisplayName(as[j]) +"]的值,其类型为["+object.getClass()+"]\n");
				}
			}
			
		}
		
		String fs[] = template.getAllDataField();
		for(int i = 0 ; submit && i < fs.length ; i++){
			String value = submitMap.get(fs[i]);
			Number number = template.getValue(fs[i]);
			if(value != null && value.trim().length() > 0){
				try{
					Constructor c = number.getClass().getConstructor(new Class[]{String.class});
					Number n = (Number)c.newInstance(new Object[]{value});
					template.setValue(fs[i],n);
				}catch(Exception e){
					errorMessage.append("统计项[" + template.getFieldDisplayName(fs[i]) +"]的值格式不对，必须为["+number.getClass()+"]类型\n");
				}
			}else{
				errorMessage.append("请设置统计项[" + template.getFieldDisplayName(fs[i]) +"]的值，其类型为["+number.getClass()+"]\n");
			}
		}
		
		StringBuffer sb  = new StringBuffer();
		sb.append("<h3>请输入所有粒度的值：</h3><br/>");
		sb.append("<form id='form_submit' name='form_submit'>");
		sb.append("<input id='submitted' name='submitted' type='hidden' value='true'>");
		sb.append(getDimensionInputTable(ds,true,false,false));
		sb.append("\n<br/>");
		sb.append("<h3>请输入所有统计项的值：</h3><br/>");
		sb.append("<table border='0' cellpadding='0' cellspacing='0' bgcolor='#FFFFFF' align='left'>");
		
		for(int i = 0 ; i < fs.length ; i++){
			sb.append("<tr><td align='right' nowrap>"+template.getFieldDisplayName(fs[i])+"</td><td width='10'>:</td><td align='left'><input type='text' name='"+fs[i]+"' id='"+fs[i]+"' size='8' value='"+template.getValue(fs[i])+"'></td><td width='100%'></td></tr>");
		}
		sb.append("<tr><td></td><td><td></td><td></td></tr>");
		sb.append("<tr><td><input type='submit' value='提    交'></td><td><td></td><td></td></tr>");
		sb.append("</table>");
		sb.append("\n<br/><br/>");
		sb.append("\n<br/>");
		
		if(submit && errorMessage.length() == 0){
			try{
				StatisticsTool.update(conn,tableName,ds,template,accumulate);
				Map<Map<String,String>,StatData> resultMap = StatisticsTool.query(conn,tableName,ds,template,null,null,false);
				
				sb.append("<h3>查询结果如下，共"+resultMap.size()+"条记录：</h3><br/>");
				sb.append(getTableOfQueryResult(ds,template,resultMap));
				
				
			}catch(Exception e){
				sb.append("<h3 color=red>更新出错，详细信息如下，请保留页面联系技术人员解决：</h3><br/>");
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				e.printStackTrace(new PrintStream(out));
				sb.append("<pre>");
				sb.append(new String(out.toByteArray()));
				sb.append("</pre>");
			}
		}else if(errorMessage.length() > 0){
			sb.append("<h3 color=red>输入不完整，请参考错误信息：</h3><br/>");
			sb.append("<pre>");
			sb.append(errorMessage.toString());
			sb.append("</pre>");
			try{
				conn.close();
			}catch(Exception e){}
		}else{
			try{
				conn.close();
			}catch(Exception e){}
		}
	
		return sb.toString();
		
	}
	public static String queryPage(Connection conn,String tableName,Dimension ds[],StatData template,HashMap<String,String> submitMap){
		return queryPage(conn,tableName,ds,ds,template,submitMap,null);
	}
	
	public static String queryPage(Connection conn,String tableName,Dimension ds[],StatData template,HashMap<String,String> submitMap,String selectPage){
		return queryPage(conn,tableName,ds,ds,template,submitMap,selectPage);
	}
	
	public static String queryPage(Connection conn,String tableName,Dimension ds[],Dimension[] displayDs,StatData template,HashMap<String,String> submitMap,String selectPage){
		long startTime = System.currentTimeMillis();
		StringBuffer errorMessage = new StringBuffer();
		for(int i = 0 ; i < ds.length ; i++){
			String gs[] = ds[i].getAllGranula();
			for(int j = 0 ; j < gs.length ; j++){
				String value = submitMap.get(gs[j]);
				if(value != null && value.trim().length() > 0){
					try{
						ds[i].setValue(gs[j],value);
					}catch(IllegalArgumentException e){
						errorMessage.append("设置维度["+ds[i].getName()+"]的粒度[" + ds[i].getGranulaDisplayName(gs[j]) +"]出错，必须先设置大粒度，然后才能设置小粒度\n");
					}
				}
			}
		}
		
		StringBuffer sb  = new StringBuffer();
		sb.append("<form id='form_submit' name='form_submit'>");
		sb.append("<input id='submitted' name='submitted' type='hidden' value='true'>");
		sb.append(getDimensionInputTable(displayDs,false,true,true,selectPage));
		sb.append("\n<br/>");
		sb.append("过滤条件表达式：<input name='filterexpression' id='filterexpression' size='40' value=\""+(submitMap.get("filterexpression")==null?"":submitMap.get("filterexpression"))+"\"><input type='button' value='选择过滤条件' onclick=\"on_filter_select('filterexpression')\"> 可以为复杂表达式");
		sb.append("\n<br/>");
		sb.append("排序规则表达式：<input name='orderexpression' id='orderexpression' size='30' value=\""+(submitMap.get("orderexpression")==null?"":submitMap.get("orderexpression"))+"\"><input type='button' value='选择排序规则' onclick=\"on_order_select('orderexpression')\">");
		sb.append("正序<input type='radio' name='desc' id='desc' "+("1" == submitMap.get("desc")?"selected":"")+" value=1>&nbsp;反序<input type='radio' name='desc' id='desc' "+("0" == submitMap.get("desc")?"selected":"")+" value=0> 可以为复杂表达式");
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
			boolean desc = ("0" == submitMap.get("desc"));
			try{
				Map<Map<String,String>,StatData> resultMap = StatisticsTool.query(conn,tableName,ds,template,filterExpression,orderExpression,desc);
				sb.append("<h3>查询结果如下，共"+resultMap.size()+"条记录，耗时 "+((System.currentTimeMillis() - startTime)/1000f)+" 秒：</h3><br/>");
				sb.append(getTableOfQueryResult(ds,template,resultMap));
				
			}catch(Exception e){
				sb.append("<h3 color=red>查询出错，耗时 "+((System.currentTimeMillis() - startTime)/1000f)+" 秒，详细信息如下，请保留页面联系技术人员解决：</h3><br/>");
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				e.printStackTrace(new PrintStream(out));
				sb.append("<pre>");
				sb.append(new String(out.toByteArray()));
				sb.append("</pre>");
			}
		}else if(errorMessage.length() > 0){
			sb.append("<h3 color=red>输入不完整，请参考错误信息：</h3><br/>");
			sb.append("<pre>");
			sb.append(errorMessage.toString());
			sb.append("</pre>");
			
			try{
				conn.close();
			}catch(Exception e){}
		}else{
			try{
				conn.close();
			}catch(Exception e){}
		}
	
		return sb.toString();
	}
	
	public static String getDimensionSelectTable(Connection conn,String tableName,Dimension ds[],StatData template,String selectedGranula){
		
		Dimension d = null;
		for(int i = 0 ; i < ds.length ; i++){
			if(ds[i].isGranula(selectedGranula)){
				d = ds[i];
			}
		}
		if(d == null){
			return "粒度["+selectedGranula+"]在给定的维度中不存在";
		}
		String gs[] = d.getAllGranula();
		boolean b = false;
		for(int j = 0 ; j < gs.length ; j++){
			if(b == false && d.getValue(gs[j]) == null){
				d.setListMark(gs[j]);
			}
			if(gs[j].equals(selectedGranula)) b = true;
			if(b){
				d.setValue(gs[j],null);
			}
		}
		
		
		StringBuffer sb = new StringBuffer();
		try{
			String sql = StatisticsSQLConstructor.constructGranulaSelectSQL(tableName,d,selectedGranula,ds);
			
			Map<Map<String,String>,StatData> resultMap = StatisticsTool.execute_query(conn,sql,ds,template);
			
			sb.append("<table border='0' cellpadding='0' cellspacing='1' width='100%' bgcolor='#000000' align='center'>");
			sb.append("<tr bgcolor='#00FFFF' align='center'>");
			
			for(int i = 0 ; i < gs.length ; i++){
				sb.append("<td>"+d.getGranulaDisplayName(gs[i])+"</td>");
				if(gs[i].equals(selectedGranula)) break;
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
					if(gs[j].equals(selectedGranula)) break;
				}
				javaScript.append("window.close();");
				
				sb.append("<td><input type='button' value='选  择' onclick=\""+javaScript+"\"></td>");
				
				sb.append("</tr>");
			}
			sb.append("</table>");
		}catch(Exception e){
			sb.append("<h3 color=red>查询出错，详细信息如下，请保留页面联系技术人员解决：</h3><br/>");
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			e.printStackTrace(new PrintStream(out));
			sb.append("<pre>");
			sb.append(new String(out.toByteArray()));
			sb.append("</pre>");
		}
		return sb.toString();
	}
	
	public static String getDimensionInputTable(Dimension ds[],boolean attachment,boolean selectButton,boolean markButton){
		return getDimensionInputTable(ds,attachment,selectButton,markButton,null);
	}
	
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
			if(i < ds.length)
				sb.append("<td width='"+width+"%'>"+ds[i].getName()+"维度"+(ds[i].isCanStartWith()?"(支持前缀)":"")+(ds[i].isCanRangeSelect()?"(支持范围,如2~5)":"")+"</td>");
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
						if(ds[i].isListMark(granula)) 
							value = Dimension.LISTMARK;
						if(value == null) {
							/*if(granula.equals("day")){
								value = DateUtil.formatDate(new Date(),"yyyyMMdd");
							}else*/
							value = "";
						}
						
						sb.append("<input name='"+granula+"' id='"+granula+"' type='text' size='7' value='"+value+"'>");
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
						if(markButton)
							sb.append("<input type='button' value='标记' onclick=\"{document.getElementById('"+granula+"').value='"+Dimension.LISTMARK+"'}\">");
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
	
	
	/**
	 * <pre>
	 * 查询，各个查询的粒度都设置了值，可能有粒度标记为列表.
	 * 查询的结果为一组记录，记录存放在一个map中，map的key值为所有标记为列表值的粒度，对应的具体值。
	 * 比如：
	 *     各个维度的信息如下：
	 *     Operator:   operator='cmcc' node=*
	 *     Product:    product='abc'   
	 *     Channel:    type=*
	 *     Province:   province=*
	 * 
	 * 那么结果Map中的key也应该为一个map，如下：
	 *     node='xxxx' type='yyyy' province='zzzz'    
	 * 
	 * </pre>
	 *  
	 * @throws Exception 出现错误，比如表达式错误
	 */
	public static String getTableOfQueryResult(Dimension ds[],StatData template,Map<Map<String,String>,StatData> resultMap){
		return getTableOfQueryResult(ds,template,resultMap,false);
	}
	
	public static String getTableOfQueryResult(Dimension ds[],StatData template,Map<Map<String,String>,StatData> resultMap,boolean showTotal){
		
		StringBuffer sb = new StringBuffer();
		sb.append("<table class='sortable' id='resulttable' border='0' cellpadding='0' cellspacing='1' width='100%' bgcolor='#000000' align='center'>");
		sb.append("<tr bgcolor='#00FFFF' align='center'>");
		
		String dataFields[] = template.getAllDataField();
		
		StatData totalSD = null;
		
		for(int i = 0 ; i < ds.length ; i++){
			String gs[] = ds[i].getGranulas();
//			if(gs.length > 0 && ds[i].isListMark(gs[gs.length-1])){
				for(int j = 0 ; j < gs.length ; j++){
					sb.append("<td>"+ds[i].getGranulaDisplayName(gs[j])+"</td>");
				}
//			}
		}
		
		for(int i = 0 ; i < dataFields.length ; i++){
			sb.append("<td>"+template.getFieldDisplayName(dataFields[i])+"</td>");
		}
		sb.append("</tr>");
		
		Iterator<Map<String,String>> it = resultMap.keySet().iterator();
		while(it.hasNext()){
			
			Map<String,String> key = it.next();
			StatData data = resultMap.get(key);
			
			if(showTotal && totalSD == null){
				totalSD = data.clone();
			}else if(showTotal){
				totalSD.add(data);
			}
			
			sb.append("<tr bgcolor='#FFFFFF' align='center'>");
			for(int i = 0 ; i < ds.length ; i++){
				String gs[] = ds[i].getGranulas();
				//if(gs.length > 0 && ds[i].isListMark(gs[gs.length-1])){
					for(int j = 0 ; j < gs.length ; j++){
						String value = key.get(gs[j]);
						if(value != null)
							ds[i].setValue(gs[j],value);
						else
							value = ds[i].getValue(gs[j]);
						sb.append("<td bgcolor='#80FFFF'>"+ds[i].getDisplayValue(gs[j],value)+"</td>");
					}
				//}
			}
			
			for(int i = 0 ; i < dataFields.length ; i++){
				try {
					Number nf = data.getValue(dataFields[i]);
					if(nf instanceof Integer || nf instanceof Long || nf instanceof Short || nf instanceof Byte){
						sb.append("<td>"+data.getValue(dataFields[i])+"</td>");
					}else{
						long k = (long)(nf.floatValue() * 100 + 0.5);
						sb.append("<td>"+(k/100.0f)+"</td>");
					}
				} catch(NullPointerException e) {
					System.err.println("[trace1] [null] ["+dataFields[i]+"] ["+data+"]");
					throw e;
				} 
			}
			sb.append("</tr>");
		}
		if(showTotal && totalSD != null){
			sb.append("<tr bgcolor='#00FFFF' align='center'>");
			
			for(int i = 0 ; i < ds.length ; i++){
				String gs[] = ds[i].getGranulas();
					for(int j = 0 ; j < gs.length ; j++){
						if(j == 0 && i == 0)
							sb.append("<td>汇  总 </td>");
						else
							sb.append("<td>-</td>");
							
					}
			}
			
			
			
			for(int i = 0 ; i < dataFields.length ; i++){
				Number nf = totalSD.getValue(dataFields[i]);
				if(nf instanceof Integer || nf instanceof Long || nf instanceof Short || nf instanceof Byte){
					sb.append("<td>"+totalSD.getValue(dataFields[i])+"</td>");
				}else{
					long k = (long)(nf.floatValue() * 100 + 0.5);
					sb.append("<td>"+(k/100.0f)+"</td>");
				}
			}
			sb.append("</tr>");
		}
		sb.append("</table>");
		return sb.toString();
		
	}
	
	public static String showResultSetOnPage(ResultSet rs,Map<String,String> directory) throws Exception{
		if(directory == null) directory = new HashMap<String,String>();
		StringBuffer sb = new StringBuffer();
		sb.append("<table class='sortable' id='resulttable' border='0' cellpadding='0' cellspacing='1' width='100%' bgcolor='#000000' align='center'>");
		sb.append("<tr bgcolor='#00FFFF' align='center'>");
		ResultSetMetaData md = rs.getMetaData();
		int count = md.getColumnCount();
		String dataFields[] = new String[count];
		for(int i = 0 ; i < dataFields.length ; i++){
			dataFields[i] = md.getColumnName(i+1);
		}
		
		for(int i = 0 ; i < dataFields.length ; i++){
			if(directory.containsKey(dataFields[i]))
				sb.append("<td>"+directory.get(dataFields[i])+"</td>");
			else
				sb.append("<td>"+dataFields[i]+"</td>");
		}
		sb.append("</tr>");
		
		while(rs.next()){
			
			sb.append("<tr bgcolor='#FFFFFF' align='center'>");
			for(int i = 0 ; i < dataFields.length ; i++){
				String value = rs.getString(i+1);
				if(value == null) value="";
				sb.append("<td bgcolor='#80FFFF'>"+value+"</td>");
			}
			sb.append("</tr>");
		}
		sb.append("</table>");
		return sb.toString();
	}
	
	public static void updateQuickLinks(File file,HttpServletRequest request) throws IOException{
		String linkDesp = request.getParameter("link_desp");
		String linkQuery = request.getParameter("query_str");
		
		if(linkDesp != null && linkQuery != null){
			linkQuery = linkQuery.replaceAll(";AMP;","&");
			BufferedWriter writer = new BufferedWriter(new FileWriter(file,true));
			writer.write("BEGIN{"+linkDesp+"====="+request.getRequestURI() + "?" + linkQuery+"}END\n");
			writer.close();
		}
		
	}
	
	public static String getTableOfQuickLinks(File file,int column,boolean showForm,String linkQueryStr) throws IOException{
		return getTableOfQuickLinks("",file,column,showForm,linkQueryStr);
	}
	
	public static String getTableOfQuickLinks(String className,File file,int column,boolean showForm,String linkQueryStr) throws IOException{
		ArrayList<String> links = new ArrayList<String>();

		if(file.exists()){
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String s = null;
			while((s = reader.readLine()) != null){
				if(s.trim().startsWith("#")) continue;
				s = s.trim();
				if(s.startsWith("BEGIN{") && s.endsWith("}END") && s.indexOf("=====") > 0){
					s = s.substring(6);
					s = s.substring(0,s.length()-4);
					links.add(s);
				}
			}
			reader.close();
		}
		StringBuffer sb = new StringBuffer();
		
		if(links.size() > 0){
			sb.append("<b>快速查询链接，每次查询后可以添加</b>");
			sb.append("<table border='0' cellpadding='0' cellspacing='1' bgcolor='#000000' align='center'>");
			sb.append("<tr bgcolor='#FFFFFF' align='center'>");
			
			int kkk = 0;
			if(links.size() % column == 0) kkk = links.size();
			else kkk = column * (links.size()/column +1);
			
			for(int i = 0 ; i < kkk ; i++){
				if(i < links.size()){
					String s = links.get(i);
					String ss[] = s.split("=====");
					String href = ss[1];
					
					href = href.replaceAll("current_day",DateUtil.formatDate(new Date(),"yyyyMMdd"));
					
					sb.append("<td width='"+(100/column)+"%'><a href='"+href+"'>"+ss[0]+"</a></td>");
				}else{
					sb.append("<td width='"+(100/column)+"%'>-</td>");
				}
				if(i < kkk-1 && (i+1) % column == 0){
					sb.append("</tr><tr bgcolor='#FFFFFF' align='center'>");
				}
			}
			sb.append("</tr></table><br>");
		}
			
		if(showForm){
			sb.append("<form id=\"linkform\">请输入此次查询的描述：<input type=\"text\" size=\"20\" name=\"link_desp\" value=\"\"><input type=\"hidden\" name=\"cl\" value=\""+className+"\"><input type=\"hidden\" name=\"query_str\" value='"+linkQueryStr.replaceAll("&",";AMP;")+"'>&nbsp;<input type=\"submit\" value=\"添加快速查询\">&nbsp;</form>");
		}
		
		return sb.toString();
	}
}
