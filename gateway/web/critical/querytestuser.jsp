<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="com.xuanzhi.tools.text.DateUtil"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiServerInfoManager.TestUser"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiServerInfoManager"%>
<%@page import="com.xuanzhi.tools.text.StringUtil"%>
<%@page import="com.xuanzhi.tools.text.ParamUtils"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%
	String username = request.getParameter("username");
	long time = ParamUtils.getLongParameter(request, "time", -1l);
	String sign = request.getParameter("sign");
	
	if(StringUtils.isEmpty(sign) || StringUtils.isEmpty(username))
	{
		out.print("缺少参数!");
		return;
	}
	
	
	String mysign = StringUtil.hash(username+time+"adsaEWRWker5");
	
	if(sign.equals(mysign))
	{
		MieshiServerInfoManager mm = MieshiServerInfoManager.getInstance();
		TestUser testUser = mm.getTestUser(username);
		
		if(testUser == null)
		{
			out.print("false");
		}
		else
		{
			StringBuffer sb = new StringBuffer();
			sb.append("true");
			sb.append("&&");
			sb.append(testUser.username);
			sb.append(",");
			sb.append((testUser.enable == true ? "有效":"失效"));
			sb.append(",");
			sb.append((testUser.reason == null ? "" : testUser.reason));
			sb.append(",");
			sb.append((testUser.addUser == null ? "" : testUser.addUser));
			sb.append(",");
			sb.append((testUser.bumen == null ? "" : testUser.bumen));
			sb.append(",");
			sb.append((testUser.createTime == null ? "" : DateUtil.formatDate(testUser.createTime, "yyyyMMdd")));
			sb.append(",");
			sb.append((testUser.lastModifiedTime == null ? "" : DateUtil.formatDate(testUser.lastModifiedTime, "yyyyMMdd")));
			out.print(URLEncoder.encode(sb.toString(), "utf-8"));
		}
		
		
	}
%>