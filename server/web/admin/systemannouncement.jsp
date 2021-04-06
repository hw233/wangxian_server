<%@page contentType="text/html;charset=utf-8" import="com.fy.engineserver.operating.*"%>
<%@include file="IPManager.jsp" %><html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title></title>
		<%
			SystemAnnouncementManager sam = SystemAnnouncementManager.getInstance();
			String subType = request.getParameter("subtype");
			
			if(subType != null && subType.equals("1")){
				String announcement = request.getParameter("system_announcement");
				if(announcement != null){
					announcement = announcement.replaceAll("\r\n" , "\n");					
					sam.setAnnouncement(announcement);
				}
			}else if(subType != null && subType.equals("3")){
			    sam.clear();
			}
			
		%>
		<script language="JavaScript">
			function update_system_announcement(){
				var subtype = document.getElementById("subtype");
				subtype.value = 1;
				document.system_announcement_form.submit();
			}
			
			function clear_system_announcement_history(){
				var subtype = document.getElementById("subtype");
				subtype.value = 2;
				document.system_announcement_form.submit();
			}
		</script>
	</head>
	<body>
		<form action="" name="system_announcement_form" method="post">
			系统公告:<br/>			
			<textarea name="system_announcement" style="width:300px;height:200px;"><%=sam.getAnnouncement()%></textarea>
			<br />
			<input type="button" name="update_announcement_content" value="更新公告" onclick="javascript:update_system_announcement()">
			<input type="button" name="clear_system_history" value="清除历史" onclick="javascript:clear_system_announcement_history()">
			<input type="hidden" id="subtype" name="subtype" value="" />
		</form>
		
	</body>
</html>
