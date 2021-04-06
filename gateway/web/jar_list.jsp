<%@ page contentType="text/html;charset=utf-8" 
import="java.util.*,java.io.*"%>
<%@page import="com.xuanzhi.tools.text.DateUtil"%>
<%@page import="com.xuanzhi.gameresource.*"%>

<html>
<head>
<title>jar包管理</title>
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
   /*function show(i){
    document.getElementById("th"+i).rowspan=document.getElementById("th"+i+"size").value;
    alert(document.getElementById("th"+i).rowspan);
    var t = document.getElementsByName("tr"+i);
    for(i=0;i<t.length;i++)
    t[i].style.display="";
   }*/
</script>
</head>
<body>
  <%JarVersionManager jmanager = JarVersionManager.getInstance();
    List<JAD>  jads = jmanager.getJadList();
    String action = request.getParameter("action");
    if(action!=null&&"delgroup".equals(action.trim())){
      String delstr[] = request.getParameterValues("delindex");
      if(delstr!=null&&delstr.length>0){
          System.out.print(delstr.length);
          jmanager.delete(delstr); 
      }
    }
    if(action!=null&&"delall".equals(action)){
       jmanager.deleteAll(); 
    }
    out.print("<p></p><p></p><p></p><p></p>");
   // out.print(jads.size()+"==》jar包的长度  ； 根目录为："+jmanager.getFileDir().getAbsolutePath());
  
   out.print("最新更新时间为："+DateUtil.formatDate(new Date(jmanager.getFileDir().lastModified()),"yyyy-MM-dd HH:mm:ss"));
    List<String> fs = new ArrayList<String>();
    out.print("<form action='' method='post' id='f1'><table align='center' width='80%' ><caption>jar包列表("+jads.size()+ ")</caption>");
    out.print("<tr><th >机型名</th><th>jar包</th><th>jad包</th></tr>");
    for(int i=0;i<jads.size();i++){
        String filename = jads.get(i).file.getName();
        if(fs.size()==0||!fs.contains(filename.substring(0,filename.length()-4))){   
        String name = filename.substring(0,filename.length()-4);
        fs.add(filename.substring(0,filename.length()-4));
       // out.print(jmanager.getFileDir().getAbsolutePath()+"/"+name);
        if((new File(jmanager.getFileDir().getAbsolutePath()+"/"+name+".jar")).length()>0){
         out.print("<tr  name='tr"+i+"'onmouseover='overTag(this);' onmouseout='outTag(this);' ><td>"+name+"</td><td><a href='/jars/"+name+".jar' >"+name+":jar包</a>("+(new File(jmanager.getFileDir().getAbsolutePath()+"/"+name+".jar")).length()/1000+"kb)</td>");
         out.print("<td><a href='/jars/"+name+".jad' >"+name+"：jad包</a></td></tr>");        
        }
      }
    }
    out.print("</table>");
 %>


</body>
</html> 
