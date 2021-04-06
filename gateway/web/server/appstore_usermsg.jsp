<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.fy.gamegateway.mieshi.waigua.ClientAuthorization"%>
<%@page import="com.fy.gamegateway.mieshi.waigua.AuthorizeManager"%>
<%@page import="com.fy.gamegateway.mieshi.waigua.ClientEntity"%>
<%@ page contentType="text/html;charset=utf-8" import="java.util.*,
com.xuanzhi.tools.text.*,
com.xuanzhi.tools.transport.*,java.nio.channels.*,org.apache.log4j.Logger,
com.fy.gamegateway.stat.*,com.fy.gamegateway.mieshi.server.*,com.fy.gamegateway.tools.*"%>
<%
	String action = request.getParameter("action");
	ArrayList<String> dataS = new ArrayList<String>();
	ArrayList<long[]> dataMsgs = new ArrayList<long[]>();
	String[] phoneType = new String[]{"iPod touch", "iPad", "iPhone"};
	if (action != null) {
		if (action.equals("create")) {
			String startTimeS = request.getParameter("startTime");
			String endTimeS = request.getParameter("endTime");
			long startTime = DateUtil.parseDate(startTimeS,"yyyy-MM-dd").getTime();
			long endTime = DateUtil.parseDate(endTimeS,"yyyy-MM-dd").getTime() + 60*60*24*1000L;
			long spaceTime = endTime - startTime;
			long dayNum = spaceTime / (60*60*24*1000L);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			for (int i = 0; i < dayNum; i++) {
				long[] nums = new long[phoneType.length + 1];
				dataS.add(format.format(new Date(startTime + i * (60*60*24*1000L))));
				dataMsgs.add(nums);
				int j = 0;
				for (; j < phoneType.length; j++) {
					long num = AuthorizeManager.getInstance().em_ce.count(ClientEntity.class, "channel=? and createTime>? and createTime<? and phoneType=?", new Object[]{"APPSTORE_MIESHI", startTime + i * (60*60*24*1000L), startTime + (i+1) * (60*60*24*1000L), phoneType[j]});
					nums[j] = num;
				}
				if (j == phoneType.length) {
					nums[nums.length-1] = AuthorizeManager.getInstance().em_ce.count(ClientEntity.class, "channel=? and createTime>? and createTime<?", new Object[]{"APPSTORE_MIESHI", startTime + i * (60*60*24*1000L), startTime + (i+1) * (60*60*24*1000L)});
				}
			}
		}
	}
%>
<html>
<head>
</head>
<body>
<h2>Appstore信息查询</h2>
<br>
查询这个时间段，每天激活客户端数目
<br>
<form action="">
	<input type="hidden" name="action" value="create">
	起始时间<input type="text" name="startTime" value="2013-06-25">
	结束时间<input type="text" name="endTime" value="2013-06-25">
	<input type="submit" name="确定">
</form>
<br>
<br>
<%
	if (dataMsgs.size() > 0) {
%>
<h2>APPSTORE_MIESHI渠道，ClientID创建数目</h2>
<table border="1">
	<tr>
		<td>日期</td>
		<% 
			for (int i = 0;i < phoneType.length; i++) {
		%>
		<td>"<%=phoneType[i] %>"</td>
		<% 
			}
		%>
		<td>总数</td>
	</tr>
	<%
		for (int i = 0; i<dataMsgs.size(); i++) {
			out.println("<tr>");
			out.println("<td>"+dataS.get(i)+"</td>");
			long[] nums = dataMsgs.get(i);
			for (int j = 0; j < nums.length; j++) {
				out.println("<td>"+nums[j]+"</td>");
			}
			out.println("</tr>");
		}
	%>
</table>
<%
	}
%>
<br>
</body></html>