<%@ page contentType="text/html;charset=utf-8"%>
<%@ page import="java.util.*,com.fy.engineserver.smith.*"%>
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
		ArticleRelationShipManager msm = ArticleRelationShipManager.getInstance();
		msm.recreate();
		%>
		<br>
		<br>
		<H2>完成重建！</H2>
	</body>
</html>