<%@page import="com.xuanzhi.tools.servlet.CommonServletFilter"%>
<%
        String s = request.getParameter("dump_times");

	int iTimes = 5;

	if(s != null)
		try{
		iTimes = Integer.parseInt(s);
		}catch(Exception e){}

        out.println(CommonServletFilter.dumpThreadOn(iTimes));
%>

