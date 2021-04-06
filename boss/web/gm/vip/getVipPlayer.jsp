
<%@page import="com.xuanzhi.tools.text.JsonUtil"%>
<%@page import="com.fy.boss.vip.platform.VipPlayerInfoManager"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.boss.vip.platform.model.VipPlayerInfoRecord"%>
<%@page import="com.xuanzhi.tools.text.ParamUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
  
 <%
 long createTime  = ParamUtils.getLongParameter(request, "create_time", Long.MAX_VALUE);
	
	 
 %>
<%

	

		Class<VipPlayerInfoRecord> cl = VipPlayerInfoRecord.class;
		String where  = " createTime > ?  "	;
		Object[] os = new Object[]{createTime};
		
		int pernum = 100;
		
		try
		{
				int start = 1;
				List<VipPlayerInfoRecord> lst = VipPlayerInfoManager.getInstance().queryForWhere(where, os, "createTime", start, pernum);
				
				if(lst != null)
				{
					String json = JsonUtil.jsonFromObject(lst);	
					out.print(json);
				}
		}
		catch(Exception e)
		{
			out.println("{}");
		} 
									
	%>
				
