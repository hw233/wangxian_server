<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.io.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
Process process = Runtime.getRuntime().exec("/home/game/resin/webapps/game_server/admin/logAnalyse/checkTimeOutArticle.sh /home/game/resin/log/game_server/knapsack.log 火");
InputStream in = process.getInputStream();
InputStreamReader inr = new InputStreamReader(in);
BufferedReader br = new BufferedReader(inr);
String line = null;
while ((line = br.readLine()) != null) {
	System.out.println(line);
}
int val = process.waitFor();
System.out.println("process state:" + val);

%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>