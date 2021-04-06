<%@ page contentType="text/html;charset=utf-8" 
import="java.util.*,java.io.*,com.xuanzhi.gameresource.* "%>

<html>
<head>
<title>jar包管理</title>
<link rel="stylesheet" href="../css/style.css"/>
<style>
	body {font-size:14px;}
</style>
</head>
<body>
<br><br>
<h3 align="center">潜龙游戏，JAR包管理</h3>
<%
	JarVersionManager jvm = JarVersionManager.getInstance();

	out.println("<br/>");
	
	JarVersionManager.JarMaping ss[] = jvm.handsetInfo.toArray(new JarVersionManager.JarMaping[0]);
	out.println("<table width='50%'><tr><td>机型</td><td>点击下载</td></tr>");
	for(int i = 2 ; i < ss.length ; i++){
		out.println("<tr>");
		out.print("<td>");
		for( int j=0;ss[i].机型标识!=null && j< ss[i].机型标识.length ;j++){
			if(!ss[i].机型标识[j].equals("default")) 
			out.print(ss[i].机型标识[j]+"<br>");
		}
		out.println("</td>");
		
		JAD jad = jvm.getJADBy包标识(ss[i].包标识);
		if(jad != null){			
			out.println("<td><a href ='/jars/"+jad.jarURL+"'>"+jad.jarURL+"</a></td>");
		}else{
			
			out.println("<td>--</td>");
		}
		out.println("</tr>");
	}
	out.println("</table>");
 %>
</body>
</html> 
