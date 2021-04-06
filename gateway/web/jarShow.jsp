<%@ page contentType="text/html;charset=utf-8" 
import="java.util.*,java.io.*"%>

<html>
<head>
<title>jar包管理</title>
<link rel="stylesheet" href="../css/style.css"/>
<script  type="text/javascript">
  function overTag(tag){
	  tag.style.color = "red";	
	  tag.style.backgroundColor = "lightcyan";
   }
			
   function outTag(tag){
	  tag.style.color = "black";
	  tag.style.backgroundColor = "white";
   }
</script>
</head>
<body>


<%
	class JAD{
		String version;
		String jad,jar;
		JAD(String v){
			version =v;
		}		
	};
	class Family{
		String name;
		HashMap<String,JAD> jads = new HashMap<String,JAD>();
		Family(String n){
			name = n;
		}
	};
    File dir = new File("/home/game/resin/webapps/game_gateway_mod/jars");
   // out.println(file.getName()+"::"+file.getPath()+"::"+file.getParentFile()+"::"+file.isDirectory());
    File fs[] = dir.listFiles(new FileFilter(){
		public boolean accept(File pathname) {
			if(pathname.isFile() &&
			( pathname.getName().toLowerCase().endsWith(".jad")||pathname.getName().toLowerCase().endsWith(".jar"))  ){
				return true;
			}
			return false;
		}			
	});
	
	HashMap<String,Family> familys = new HashMap<String,Family>();	
    for(File f:fs){
      //out.println("<br/>"+f.getName().substring(0,f.getName().length()-4)+"::");
      //QL_bjnet_e72_056.jad
      int i = f.getName().lastIndexOf('_');
      if( i>0){
	      	String family = f.getName().substring(0,i);
	      	String version = "";
	      	if( i+1<f.getName().length()-4){
	      		version = f.getName().substring(i+1,f.getName().length()-4);      		
	      	}
	      	Family pf =(Family) familys.get(family);
	      	if( pf==null){
	      		pf = new Family(family);
				familys.put(family,pf);
	      	}
	      	JAD pj = pf.jads.get(version);
      		if( pj==null){
      			pj = new JAD(version);
      			pf.jads.put(version,pj);
      		}
      		if( f.getName().toLowerCase().endsWith(".jad") ){
      			pj.jad = f.getName();
      		}
      		else{
      			pj.jar = f.getName();
      		}
    	}
    }    
    out.print("<p></p><p></p><p></p><p></p>");
    out.print("<table align='center' width='80%' >");
//    out.print("<caption>JAR包管理</caption>");
//    out.print("<tr><th>JAR包</th><th>JAD</th></tr>");
    

	Iterator iter = familys.entrySet().iterator(); 
	while (iter.hasNext()) { 
	    Map.Entry entry = (Map.Entry) iter.next(); 
	    Family val = (Family)entry.getValue(); 
	    out.print("<tr><th>系列: "+val.name+"</th></tr>");
	    Iterator iterJad = val.jads.entrySet().iterator(); 
		while (iterJad.hasNext()) { 
		    Map.Entry entryJad = (Map.Entry) iterJad.next(); 
		    JAD jad = (JAD)entryJad.getValue(); 
		    out.print("<tr onmouseover='overTag(this);' onmouseout='outTag(this);'><td><a href='/jars/"+jad.jar+"' >"+jad.jar+"</a></td><td>");
     		out.print("<a href='/jars/"+jad.jad+"'>"+jad.jad+"</a></td></tr>");
		} 
	} 
    
   
    out.print("</table>");
 %>


</body>
</html> 
