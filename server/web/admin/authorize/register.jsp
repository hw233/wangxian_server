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
String username = request.getParameter("username");
String message = null;
if(username != null) {
	BossClientService boss = BossClientService.getInstance();
	try {
		String passwd = request.getParameter("passwd");
		String email = request.getParameter("email");
		String mobile = request.getParameter("mobile");
		System.out.println("test-:"+username);
//		Passport pass = boss.register(username, passwd, email, mobile);
//		message = "您的通行证账号注册成功";
	} catch(Exception e) {
		e.printStackTrace();
		message = e.getMessage();
	}
}
if(message != null) {
	out.println(message);
}
 %>
<form action="" name=f1>
	账号名:<input type=text name=username size=20><br>
	密 码:<input type=password name=passwd size=20><br>
	邮 件:<input type=text name=email size=20><br>
	手  机:<input type=text name=mobile size=20><br>
	<input type=submit name=sub1 value=" 注 册 ">
</form>
</body>
</html>
