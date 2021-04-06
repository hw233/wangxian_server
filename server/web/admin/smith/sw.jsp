<%@ page contentType="text/html;charset=utf-8"%>
<%@ page import="java.util.*,com.fy.engineserver.util.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
	<title></title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<link rel="stylesheet" href="css/style.css" />
	<link rel="stylesheet" href="css/atalk.css" />
	<script type="text/javascript">
	</script>
	</head>

	<body>
		<%     
		ServerWatchDog swd = (ServerWatchDog)ServerWatchDog.getInstance();
		boolean openning = swd.isOpenning(); 
		if(!openning) {
			String flag = request.getParameter("action");
			if(flag != null && flag.equals("open")) {
				swd.setExpireTime(40 * 1000);
				swd.setClearPeriod(10 * 1000);
				swd.start();
				swd.setOpenning(true);
				openning = swd.isOpenning();
			}
		} else {
			String flag = request.getParameter("action");
			if(flag != null && flag.equals("close")) {
				swd.stop();
				swd.setOpenning(false);
				openning = swd.isOpenning();
			}
		}
		
	    %>                                              
	    <h2 style="font-weight:bold;">          
	    	ServerWatchDog
	    </h2> 
		<center>
			<form action="sw.jsp" name=f0 method=post>
				基础配置:当前状态<%=openning?"开启":"关闭" %>
				<%if(openning) {%>
				<input type=button name=sub1 value=' 关 闭 ' onclick="location.replace('sw.jsp?action=close')">
				<%} else {%>
				<input type=button name=sub1 value=' 开 启 ' onclick="location.replace('sw.jsp?action=open')">				
				<%} %>
				<br>
			</form>
			<center>
		<br>
		<br>
	</body>
</html>