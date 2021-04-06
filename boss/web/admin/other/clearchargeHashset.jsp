<%@page import="com.xuanzhi.tools.text.ParamUtils"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="com.fy.boss.finance.service.NotifySavingManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%
	NotifySavingManager notifier = NotifySavingManager.getInstance();
	Field field = NotifySavingManager.class.getDeclaredField("notifyingSet");
	
	if(field != null)
	{
		field.setAccessible(true);
		HashSet<Long> set = (HashSet)field.get(null);
		String html = "<textarea>";
		if(set != null)
		{
			Iterator<Long> it = set.iterator();
			while(it.hasNext())
			{
				Long id = it.next();
				html += "订单id:"+id.longValue();
				html += "\n";
			}
			
			html += "</textarea>";
		}
		
		out.println(html);
	}
	
	boolean isClear = ParamUtils.getBooleanParameter(request, "isClear");
	if(isClear)
	{
		String ids = ParamUtils.getParameter(request, "ids", "");
		ids = ids.trim();
		String[] idss = ids.split("\r*\n");
		if(idss != null)
		{
			for(String id : idss)
			{
				field = NotifySavingManager.class.getDeclaredField("notifyingSet");
				
				if(field != null)
				{
					field.setAccessible(true);
					HashSet<Long> set = (HashSet)field.get(null);
					
					if(set != null)
					{
						Iterator<Long> it = set.iterator();
						while(it.hasNext())
						{
							Long lid = it.next();
							if((lid.longValue()+"").equals(id))
							{
								it.remove();
								out.println("<h2>从hashset中清除订单id:"+id+"</h2>");
								break;
							}
						}
						
						
					}
					
				}
			}
		}
	}

%>
<form  method="post">
	<input type="hidden" name="isClear" value="true">
	<textarea rows="100" cols="100" name="ids" value=""></textarea><br>
	<input type="submit" value="提交">
</form>
    