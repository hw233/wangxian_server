<%@ page contentType="text/html;charset=utf-8" 
import="java.util.*,java.io.*,com.xuanzhi.gameresource.* "%>

<html>
<head>
<title>jar包管理</title>
<link rel="stylesheet" href="../css/style.css"/>
</head>
<body>
<h3>潜龙游戏，JAR包管理</h3>
<%
	JarVersionManager jvm = JarVersionManager.getInstance();

	out.println("<table><tr><td>参数名</td><td>值</td></tr>");
	out.println("<tr><td>机型配置文件</td><td>"+jvm.handsetInfoFile+"</td></tr>");
	out.println("<tr><td>HTTP前置路径</td><td>"+jvm.httpPrefix+"</td></tr>");
	out.println("<tr><td>Jar包所在目录</td><td>"+jvm.fileDir+"</td></tr>");
	out.println("</table>");
	
	out.println("<br/>");
	out.println("<br/>");
	
	JarVersionManager.JarMaping ss[] = jvm.handsetInfo.toArray(new JarVersionManager.JarMaping[0]);
	out.println("<table><tr><td>机型</td><td>包标识</td><td>Jad名称</td><td>版本</td><td>jad大小</td><td>包名称</td><td>包真实大小</td><td>MIDLET</td><td>JAD包标识</td></tr>");
	for(int i = 0 ; i < ss.length ; i++){
		out.println("<tr>");
		out.print("<td>");
		for( int j=0;ss[i].机型标识!=null && j< ss[i].机型标识.length;j++){
			out.print(ss[i].机型标识[j]+",");
			if( j%3==0){
			out.println("");
			}
		}
		out.println("</td>");
		
		out.println("<td>"+ss[i].包标识+"</td>");
		
		JAD jad = jvm.getJADBy包标识(ss[i].包标识);
		if(jad != null){
			out.println("<td>"+jad.name +"</td>");				
			out.println("<td>"+jad.version+"</td>");
			out.println("<td>"+jad.size+"</td>");
			
			File f = new File(jvm.fileDir,jad.jarURL);
			out.println("<td><a href ='/jars/"+jad.jarURL+"'>"+jad.jarURL+"</a></td>");
			out.println("<td>"+f.length()+"</td>");
			out.println("<td>"+jad.midLet+"</td>");
			out.println("<td>"+jad.handset+"</td>");
			
		}else{
			
			out.println("<td>--</td>");
			out.println("<td>--</td>");
			out.println("<td>--</td>");

			out.println("<td>--</td>");
			
			out.println("<td>--</td>");
			out.println("<td>--</td>");
			out.println("<td>--</td>");
		}
		out.println("</tr>");
	}
	out.println("</table>");
 %>
</body>
</html> 
