<%@ page contentType="text/html;charset=utf-8" 
import="java.util.*,java.io.*"%>
<%@page import="com.fy.gamegateway.jar.*"%>

<html>
<head>
<title>jar包管理</title>
<link rel="stylesheet" href="../css/style.css"/>
<script  type="text/javascript">
  function overTag(tag){
	  tag.style.color = "red";	
	  tag.style.backgroundColor = "green";
   }
			
   function outTag(tag){
	  tag.style.color = "black";
	  tag.style.backgroundColor = "white";
   }
</script>
</head>
<body>
<%

    JarManager jmanager = JarManager.getInstance();
    List<Jaro> mps = jmanager.getMps();
    List<String> ns = jmanager.getNs();
    out.print("<p></p><p></p><p></p><p></p>");
    out.print("<table align='center' width='80%' ><caption>jar包列表</caption>");
    for(int i=0;i<ns.size();i++){
      Jaro j = mps.get(i);
      out.print("<tr><th rowspan='"+(j.getJs().size()+1)+"'   >"+j.getName()+"("+j.getJs().size()+"个)</th><th>jar包</th><th>jad包</th></tr>");
      for(int k =0;k<j.getJs().size();k++){
        String name = jmanager.getFileRoot()+"/"+jmanager.getJarhead()+j.getName()+"_"+j.getJs().get(k);
        if((new File(name+".jar")).length()>0){
        out.print("<tr  name='tr"+i+"'onmouseover='overTag(this);' onmouseout='outTag(this);' ><td><a href='"+name+".jar' >"+j.getName()+"_"+j.getJs().get(k)+":jar包</a>("+(new File(name+".jar")).length()/1000+"kb)</td>");
        out.print("<td><a href='"+name+".jad' >"+j.getName()+"_"+j.getJs().get(k)+"：jad包</a></td></tr>");
        }
      }
    }
    out.print("</table>");
 %>


</body>
</html> 
