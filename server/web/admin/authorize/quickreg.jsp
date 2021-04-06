<%@ page contentType="text/html;charset=utf-8" import="java.util.*,
											com.xuanzhi.boss.authorize.model.*,
											com.xuanzhi.boss.client.*"%>
<%@include file="../IPManager.jsp" %><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<script language="JavaScript">
<!-- 
-->
</script>
</head>
<body>
<%
Passport pass = null;
String subed = request.getParameter("subed");
if(subed != null) {
	BossClientService boss = BossClientService.getInstance();
	try {
		pass = boss.quickRandomPassport();
	} catch(Exception e) {
		e.printStackTrace();
	}
}
if(pass != null) {
	out.println("您的临时通行证账号为:" + pass.getUsername() + ", 密码为:" + pass.getPasswd());
}
 %>
<form action="" name=f1>
	<input type=hidden name=subed value="true">
	<input type=submit name=sub1 value="快速注册">
</form>
</body>
</html>
