<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page
	import="com.fy.engineserver.operating.activities.ActivityItemManager"%>
<%@page
	import="com.fy.engineserver.operating.activities.ActivityItem"%>
<%@page import="com.fy.engineserver.billboard.BillboardManager"%>
<%@page import="com.fy.engineserver.billboard.Billboards"%>
<%@page import="com.fy.engineserver.billboard.BillboardData"%>
<%@include file="../header.jsp"%>
<%@include file="../authority.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=utf-8">
		<title>排行榜</title>
		<link rel="stylesheet" href="../style.css" />
		<style type="text/css">
#a {
	font-size: 0px;
}

#a li {
	font-size: 16px;
	text-align: center;
	width: 110px;
	list-style-type: none;
	display: inline;
	padding: 5px 10px;
}
</style>
		<script type="text/javascript">
	
</script>
	</head>
	<body>
		<%
			try {
				BillboardManager bmanager = BillboardManager.getInstance();
				String[] bnames = bmanager.BILLBOARDS_NAMES;
				String username = session.getAttribute("username").toString();
				String bn = request.getParameter("bn");
				out
						.print("<p></p><p align='center'><font size='16'>排行榜</font></p><hr size=10 color='green' style=\"width:45%\" /><ul id='a'>");
				for (int i = 0; i < bnames.length; i++) {
					if (bn != null && bnames[i].equals(bn))
						out.print("<li style='background-color:gray'>"
								+ bnames[i] + "</li>");
					else
						out
								.print("<li style='background-color:#2080ff'><a href='javascript:window.location.replace(\"ranking_list.jsp?bn="
										+ bnames[i]
										+ "\")'> "
										+ bnames[i]
										+ "</a></li>");
				}
				out
						.print("</ul><hr size=10 color='green' style=\"width:45%\" />");
				if (bn != null) {
					Billboards bs = bmanager.getBillboards(bn);
					String[] sns = bs.getSubmenu();
					String sn = request.getParameter("sn");
					out
							.print(" <ul id='a'><hr size=10 color='#8040ff' style=\"width:80%\" />");
					for (int i = 0; i < sns.length; i++) {
						if (sn != null && sns[i].equals(sn))
							out.print("<li style='background-color:#60ffc0'>"
									+ sns[i] + "</li>");
						else
							out
									.print("<li style='background-color:#60ff20'><a href='javascript:window.location.replace(\"ranking_list.jsp?sn="
											+ sns[i]
											+ "&bn="
											+ bn
											+ "\")'> "
											+ sns[i] + "</a></li>");

					}
					out
							.print("</ul><hr size=10 color='#8040ff' style=\"width:80%\" />");
					if (sn != null) {
						BillboardData[] bbds = bmanager
								.getBillboardData(bn, sn);
						if (bbds != null && bbds.length > 0) {
							int i = 1;
							out
									.print("<table width='80%' align='center' ><catpion>"
											+ sn + "</caption>");
							out
									.print("<tr><th>排名</th><th>Id</th><th>名称</th><th>描述</th><th>角色Id(装备排行是有效)</th><th>数值</th></tr>");
							for (BillboardData bbd : bbds) {
								out.print("<tr><td>" + (i++) + "</td><td>"+bbd.getId()+"</td><td>"
										+ bbd.getRankingObject() + "</td><td>"
										+ bbd.getDescription() + "</td><td>"
										+ bbd.getPlayerId()+"</td><td>"
										+ bbd.getValue() + "</td></tr>");
							}
							out.print("</table>");

						}
					}
				}
			} catch (Exception e) {
				//e.printStackTrace();
				//out.print(e.getMessage());
				//RequestDispatcher rdp = request
				//		.getRequestDispatcher("../gmuser/visitfobiden.jsp");
				//rdp.forward(request, response);
				out.print(StringUtil.getStackTrace(e));
			}
		%>
	</body>
</html>
