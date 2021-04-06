<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="java.util.List"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="com.xuanzhi.tools.text.DateUtil"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiServerInfoManager.TestUser"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiServerInfoManager"%>
<%@page import="com.xuanzhi.tools.text.StringUtil"%>
<%@page import="com.xuanzhi.tools.text.ParamUtils"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%
	long time = ParamUtils.getLongParameter(request, "time", -1l);
	String sign = request.getParameter("sign");
	
	if(StringUtils.isEmpty(sign))
	{
		out.print("缺少参数!");
		return;
	}
	
	String mysign = StringUtil.hash(time+"adsaEWRWker5");
	
	if(sign.equals(mysign))
	{
		MieshiServerInfoManager mm = MieshiServerInfoManager.getInstance();
		List<TestUser> lst =  mm.getTestUsers();
		StringBuffer sb = new StringBuffer();
		for(TestUser testUser : lst)
		{
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
			sb.append("\n");
		}
		out.print(URLEncoder.encode(sb.substring(0, sb.length()-1),"utf-8"));
	}
%>