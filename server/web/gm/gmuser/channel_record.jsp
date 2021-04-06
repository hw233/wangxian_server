<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@include file="../header.jsp"%>
<%-- <%@include file="../authority.jsp"%> --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=utf-8">
		<title>聊天日志 </title>
		<link rel="stylesheet" href="../style.css" />
		<script type='text/javascript'
			src='/game_server/dwr/interface/chatborder.js'></script>
		<script type='text/javascript' src='/game_server/dwr/engine.js'></script>
		<script type="text/javascript"
			src="/game_server/dwr/interface/DWRManager.js"></script>
		<script type="text/javascript" src="/game_server/dwr/util.js"></script>
		<script type="text/javascript">
	function $(tag) {
		return document.getElementById(tag);
	}
	function queryBygm(start, sname) {
		var gmname = $("gmname").value;
		if (gmname)
			window.location.replace("?gmname=" + gmname + "&start=" + start
					+ "&sname=" + sname);
		else
			alert("请输入正确的信息");
	}
</script>
	</head>
	<body>

		<%
			try {

// 				String gmusername = session.getAttribute("username").toString();
				String sname = request.getParameter("sname");
				out
						.print("<input type='button' value='刷新' onclick='window.location.replace(\"channel_record.jsp?sname="
								+ sname + "\")' /><br/>");
				RecordManager rmanager = RecordManager.getInstance();
				List<String> sss = rmanager.getSrcdate();
				List<String> ss1 = new ArrayList<String>();
				for (String sss1 : sss) {
					if (sss1.startsWith(sname))
						ss1.add(sss1);
				}
				sss = ss1;
				int start = sss.size() - 1;
				if (request.getParameter("start") != null)
					start = Integer.parseInt(request.getParameter("start"));
				String td = DateUtil.formatDate(new Date(), "yyyy-MM-dd");
				int size = 8;
				out
						.print("发送人(过滤查询):<input type='text' name ='gmname' id='gmname' value='' /> <input type='button' value='查询' onclick='queryBygm("
								+ start + ",\"" + sname + "\")' /><br/>");
				if (start >= 0) {
					out.print("<a href ='?sname=" + sname + "&start="
							+ (sss.size() - 1)
							+ "' alt='首页' >首页</a>&nbsp;&nbsp;");
					if (start < sss.size() - 1)
						out.print("<a href='?sname=" + sname + "&start="
								+ (start + 1) + "' alt='" + sss.get(start + 1)
								+ "' />" + sss.get(start + 1)
								+ "</a>&nbsp;&nbsp;");
					if (start - 8 >= 0) {
						for (int i = start; i > start - 8; i--) {
							String date = sss.get(i);
							out.print("<a href='?sname=" + sname + "&start="
									+ i + "' alt='" + date + "' />" + date
									+ "</a>&nbsp;&nbsp;");
						}
					}
					if (start - 8 < 0) {
						for (int i = start; i > -1; i--) {
							String date = sss.get(i);
							out.print("<a href='?sname=" + sname + "&start="
									+ i + "' alt='" + date + "' />" + date
									+ "</a>&nbsp;&nbsp;");
						}
					}
					if (start - 8 > 0)
						out.print("<a href='?sname=" + sname + "&start="
								+ (start - 8) + "' alt='" + sss.get(start - 8)
								+ "' />" + sss.get(start - 8)
								+ "</a>&nbsp;&nbsp;");
					if (start >= 0)
						out.print("<a href ='?sname=" + sname + "&start=" + 0
								+ "' alt='末页' >末页</a>");
				}
				List<RecordMessage> rms = new ArrayList<RecordMessage>();
				if (start != -1) {
					rms = rmanager.getSDayMessage(sss.get(start), "");
					if (request.getParameter("gmname") != null) {
						rms = rmanager.getSDayMessage(sss.get(start), request
								.getParameter("gmname"), "");
						if (request.getParameter("player") != null)
							rms = rmanager.getSDayMessage(sss.get(start),
									request.getParameter("gmname"), request
											.getParameter("player"), "");
					}
				}
				out
						.print("<table width='80%' ><caption>聊天记录<caption><tr><th width=20% >发送人</th><th width=50% >信息</th><th width=30% >发生时间</th></tr>");
				out.print("消息的总长度为" + rms.size());
				if (rms != null && rms.size() > 0) {
					for (int i = rms.size() - 1; i > -1; i--) {
						RecordMessage r = rms.get(i);
						out.print("<tr><td>" + r.getFname() + "</td><td>"
								+ r.getMessage() + "</td><td>"
								+ r.getSendDate() + "</td></tr>");
					}
				}
			} catch (Exception e) {
				out.print(StringUtil.getStackTrace(e));
				//RequestDispatcher rdp = request
				//		.getRequestDispatcher("visitfobiden.jsp");
				//rdp.forward(request, response);
			}
		%>
	</body>
