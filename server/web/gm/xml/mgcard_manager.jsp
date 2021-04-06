<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page
	import="com.fy.engineserver.operating.activities.SerialNumberAndMagicCardManager"%>
<%@include file="../header.jsp"%>
<%@include file="../authority.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=utf-8">
		<title>神奇卡操作</title>
		<link rel="stylesheet" href="../style.css" />
		<script type="text/javascript">
	
</script>
	</head>
	<body>
		<%
			try {
				SerialNumberAndMagicCardManager cnam = SerialNumberAndMagicCardManager
						.getInstance();
				ActionManager amanager = ActionManager.getInstance();
				String filename = request.getParameter("upload");
				String username = session.getAttribute("username").toString();
				out
						.print("<input type='button' value='刷新' onclick='window.location.replace(\"mgcard_manager.jsp\")' />");
				if (filename != null && "success".equals(filename.trim())) {
					out.print("上传成功");
					amanager.save(username, "成功上传了一批系列号！");
				}
				if (filename != null && "fail".equals(filename.trim()))
					out.print("上传失败");
				out.print("<p></p><p></p><p align='center' >10元卡的数量："
						+ cnam.getCurrentAvailableMagicCards10Count() + "</p>");
				out.print("<p></p><p></p><p align='center' >50元卡的数量:"
						+ cnam.getCurrentAvailableMagicCards50Count() + "</p>");
				out
						.print("<form METHOD=\"POST\" ACTION=\"upload.jsp\" NAME=\"PW\" ENCTYPE=\"multipart/form-data\">");
				out
						.print("<div style='margin-left:auto;margin-right:auto' ><table border=\"0\" cellspacing=\"1\" align='center' cellpadding=\"5\" width=\"500\" align=left bgcolor=\"#3A6EA5\" >");
				out.print("<caption>注意！先将检查上传文件格式</caption></tr>");
				out.print("<th style=\"color:white\" colspan=2>上传文件</th></tr>");
				out.print("<tr bgcolor=\"#ffffff\">");
				out.print("<td bgcolor=\"#C4DFFB\">文件</td>");
				out
						.print("<td bgcolor=\"#E9F4FF\"><input TYPE=\"file\" name=\"FILE1\" size=40></td>");
				out
						.print("</tr><tr bgcolor=\"#ffffff\"><td bgcolor=\"#C4DFFB\"><input type=\"hidden\" name=\"fpath\" value=\"\" />");
				out
						.print("</td><td bgcolor=\"#E9F4FF\"><input TYPE=\"submit\" value=\"确定上传\"></td></tr></table></div></form>");
			} catch (Exception e) {
				// e.printStackTrace();
				//RequestDispatcher rd = request.getRequestDispatcher("../gmuser/visitfobiden.jsp");
				// rd.forward(request,response);
				out.print(StringUtil.getStackTrace(e));
			}
		%>
	</body>
</html>
