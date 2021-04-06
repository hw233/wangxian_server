<%@ page contentType="text/html;charset=utf-8"%><%@page import="java.util.*,java.io.*,
				com.xuanzhi.tools.text.*,
				com.fy.boss.authorize.model.*,
				com.fy.boss.authorize.service.*,
				com.fy.boss.authorize.exception.*,
				com.fy.boss.finance.model.*,
				com.fy.boss.finance.service.*,
				com.fy.boss.game.model.*,
				com.fy.boss.deploy.*,
				com.fy.boss.cmd.*,com.fy.boss.game.service.*"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%
ServerManager smanager = ServerManager.getInstance();
ProjectManager pm = ProjectManager.getInstance(); 

String subed = request.getParameter("subed");
boolean updated = false;
if(subed != null && subed.equals("true")) {
	String seledFiles[] = request.getParameterValues("seledFiles");
	updated = true;
	if(seledFiles != null && seledFiles.length > 0) {
		pm.setFiles(seledFiles);
		for(int i=0; i<seledFiles.length; i++) {
			pm.logger.info("[选择目标发布文件] ["+seledFiles[i]+"]");
		}
	} else {
		pm.setFiles(null);
	}
}
%>
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh-cn">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>发布服务器</title>
		<link rel="stylesheet" href="../css/style.css" />
		<link rel="stylesheet" href="../css/atalk.css" />
<script language="JavaScript">		
<!--
<%if(updated) {%>
window.opener.location.reload();
<%}%>
-->
</script>
<style>
</style>
	</head>
	<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0">
		<h1>
			选择发布文件或目录
		</h1>
		<%
		if(subed == null) {
		String files = new DeployBoundry().getAllFiles();
		String ffs[] = files.split(";;");
		%>
		<form action="publishfiles.jsp" method=post name=f1>
		<table align="center" width="99%" cellpadding="0"
                                cellspacing="0" border="0"
                                class="sortable-onload-5-6r rowstyle-alt colstyle-alt no-arrow">                 
           	<tr>
				<td align="center">
                 <%for(String ff : ffs) {
                	 String str[] = ff.split("##");
                	 int depth = Integer.valueOf(str[2]);
                	 StringBuffer sb = new StringBuffer();
                	 for(int i=0; i<depth; i++) {
                		 sb.append("&nbsp;&nbsp;&nbsp;&nbsp;");
                	 }
                	 int type = Integer.valueOf(str[1]);
                	 if(type == 0) {
                		 out.println("<input type='checkbox' name='seledFiles' value='"+str[0]+"'>" + sb.toString() + "<span style='font-size:14px;font-weight:bold;'>"+str[0]+"</span><br>");
                	 } else {
                		 if(str[0].indexOf("xuanzhi") != -1 || str[0].indexOf("mieshi") != -1) {
                 		 	out.println("<input type='checkbox' name='seledFiles' value='"+str[0]+"'>" + sb.toString() + "<span style='font-size:14px;color:red;'>" + str[0] + "</span><br>");
                		 } else {
                		 	out.println("<input type='checkbox' name='seledFiles' value='"+str[0]+"'>" + sb.toString() + str[0] + "<br>");
                		 }
                	 }
				 %>
			<%} %>
				</td>
			</tr>
          </table>
          <input type=hidden name=subed value="true">       
          <input type=submit name=sub1 value=" 提交选中 ">               
		</form>
		<%} else {%>
		<input type=button name=bt2 value="关闭" onclick="window.close();">
		<%} %>
	</body>
</html>
 
