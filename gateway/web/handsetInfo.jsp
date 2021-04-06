<%@ page contentType="text/html;charset=utf-8" 
import="java.util.*,java.io.*"%>
<%@page import="com.xuanzhi.tools.text.DateUtil"%>
<%@page import="com.fy.gamegateway.jar.HandsetInfoManager"%>
<%@page import="com.fy.gamegateway.jar.HandsetInfo"%>
<%@page import="com.fy.gamegateway.jar.JarManager"%>

<html>
<head>
<title>jar参考详情</title>
<link rel="stylesheet" href="../css/style.css"/>
<script  type="text/javascript">
  function overTag(tag){
	  tag.style.color = "red";	
	  tag.style.backgroundColor = "gray";
   }
			
   function outTag(tag){
	  tag.style.color = "black";
	  tag.style.backgroundColor = "white";
   }
 
</script>
<link rel="stylesheet" href="../css/style.css"/>
</head>
<body>
 

<%
   HandsetInfoManager hmanager = HandsetInfoManager.getInstance();
   JarManager jmanager = JarManager.getInstance();
   List<HandsetInfo> hs = hmanager.getHeis();
   out.print("<input type='button' value='刷新' onclick='window.location.replace(\"handsetInfo.jsp\")' />");
   out.print("<table width='80%' align='center' >");
   out.print("<tr><th>机型号</th><th>推荐下载jar包类型</th><th>详细情况</th>");
   if(hs!=null&&hs.size()>0){
    for(HandsetInfo hi:hs){
     out.print("<tr onmouseover='overTag(this)' onmouseout='outTag(this)'><td>"+hi.getHandset()+"</td><td>"+hi.getJar());
     if(jmanager.getJarName(hi.getJar()) != null){
     String name = jmanager.getFileRoot() + "/"+jmanager.getJarhead()+ jmanager.getJarName(hi.getJar());
     out.print("<a href ='/jars/"+jmanager.getJarhead()
								+ jmanager.getJarName(hi.getJar())
								+ ".jad' >马上下载</a>("+((new File(name+".jar")).length()/1000+"kb")+")");
								}
     out.print("</td><td>"+hi.getComments()+"</td></tr>");
    
    }
   
   }
 %>


</body>
</html> 
