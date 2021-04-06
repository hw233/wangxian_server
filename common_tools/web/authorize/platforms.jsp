<%@ page contentType="text/html;charset=utf-8" 
import="java.io.*,java.util.*,com.xuanzhi.tools.text.*,
com.xuanzhi.tools.authorize.*"%><%!
	
	public String formatA(String s,int len){
		if(s == null || s.length() <= len) return s;
		StringBuffer sb = new StringBuffer();
		char ch[] = s.toCharArray();
		for(int i = 0 ; i < ch.length ; i++){
			sb.append(ch[i]);
			if(i > 0 && (i % len)==0){
				sb.append("<spacer size='0'/>");
			}
		}
		return sb.toString();
	}
%><%
	AuthorizeManager am = AuthorizeManager.getInstance();	
%><html>
<head>
</head>
<body>

<%
	String platformName = request.getParameter("platform");	
	
%>
<h3>平台配置情况汇总</h3>
<form id='f' name='f'>
请选择平台：<select name='platform'><option value=''>-- 请选择平台 -- </option>
<%
	
        PlatformManager pm = am.getPlatformManager();
        Platform platforms[] = pm.getPlatforms();
	for(int i = 0 ; i < platforms.length ; i++){
		if(platforms[i].getId().equals(platformName)){
			out.print("<option value='"+platforms[i].getId()+"' selected>"+platforms[i].getName()+"</option>");
		}else{
			out.print("<option value='"+platforms[i].getId()+"' >"+platforms[i].getName()+"</option>");
		}
	}
%></select><input type='submit' value='提  交'></form><br/>
<table border='0' width='100%' cellpadding='0' cellspacing='1' bgcolor='#000000' align='center'>
<tr align='center' bgcolor='#00FFFF'>
<td>平台名称</td>
<td>平台URL</td>
<td>模块名称</td>
<td>模块URI</td>
<td>页面名称</td>
<td>页面匹配规则</td>
<td>页面操作</td>
<td>模块操作</td>
<td>平台操作</td>
</tr>
<%
	for(int i = 0 ; i < platforms.length ; i++){
		if(platformName != null && platformName.length() > 0 && platforms[i].getId().equals(platformName) == false)	
			continue;

		Module modules[] = platforms[i].getModules();
		if(modules.length > 0){
			for(int j = 0 ; j < modules.length ; j++){
				
				WebPage pages[] = modules[j].getWebPages();
				if(pages.length > 0){
					for(int k = 0 ; k < pages.length ; k++){
						out.print("<tr align='center' bgcolor='#FFFFFF'>");
						
						if(j == 0 && k == 0){
							int rowSpan = 0;
							Module tmpModules[] = platforms[i].getModules();
							for(int x = 0; x < tmpModules.length ; x++){
								if(tmpModules[x].getWebPages().length > 0)
									rowSpan += tmpModules[x].getWebPages().length;
								else
									rowSpan += 1;
							}
							if(rowSpan == 0) rowSpan = 1;
							out.print("<td rowspan='"+rowSpan+"'>"+platforms[i].getName()
									+"</td><td rowspan='"+rowSpan+"'>"+formatA(platforms[i].getUrl(),4)+"</td>");
						}
						
						if(k == 0){
							int rowSpan = modules[j].getWebPages().length;
							if(rowSpan == 0) rowSpan = 1;
							out.print("<td rowspan='"+rowSpan+"'>"+modules[j].getName()
									+"</td><td rowspan='"+rowSpan+"'>"+formatA(modules[j].getUri(),4)+"</td>");
						}
						
						out.print("<td>"+formatA(pages[k].getName(),4)+"</td><td>"+formatA(pages[k].getPagePattern(),4)+"</td>");
						
						out.print("<td><a href='./webpage.jsp?action=modify&pid="+platforms[i].getId()+"&mid="+modules[j].getId()+"&gid="+pages[k].getId()+"'>修改页面</a> "
								+"| <a href='./webpage.jsp?action=remove&pid="+platforms[i].getId()+"&mid="+modules[j].getId()+"&gid="+pages[k].getId()+"'>删除页面</a></td>");
						
						if(k == 0){
							int rowSpan = modules[j].getWebPages().length;
							if(rowSpan == 0) rowSpan = 1;
							out.print("<td rowspan='"+rowSpan+"'> <a href='./module.jsp?action=modify&pid="+platforms[i].getId()+"&mid="+modules[j].getId()+"'>修改模块</a>"
									+"|<a href='./module.jsp?action=remove&pid="+platforms[i].getId()+"&mid="+modules[j].getId()+"'>删除模块</a>"
									+"|<a href='./webpage.jsp?action=add&pid="+platforms[i].getId()+"&mid="+modules[j].getId()+"'>添加页面</a></td>");
						}
						
						if(j == 0 && k == 0){
							int rowSpan = 0;
							Module tmpModules[] = platforms[i].getModules();
							for(int x = 0; x < tmpModules.length ; x++){
								if(tmpModules[x].getWebPages().length > 0)
									rowSpan += tmpModules[x].getWebPages().length;
								else
									rowSpan += 1;
							}
							if(rowSpan == 0) rowSpan = 1;
							
							out.print("<td rowspan='"+rowSpan+"'><a href='./platform.jsp?action=modify&pid="+platforms[i].getId()+"'>修改平台</a>|"
									+"<a href='./platform.jsp?action=remove&pid="+platforms[i].getId()+"'>删除平台</a>|"
									+"<a href='./module.jsp?action=add&pid="+platforms[i].getId()+"'>添加模块</a></td>");
						}
						out.print("</tr>\n");
					}
				}else{
					out.print("<tr align='center' bgcolor='#FFFFFF'>");
						
					if(j == 0){
						int rowSpan = 0;
						Module tmpModules[] = platforms[i].getModules();
						for(int x = 0; x < tmpModules.length ; x++){
							if(tmpModules[x].getWebPages().length > 0)
									rowSpan += tmpModules[x].getWebPages().length;
								else
									rowSpan += 1;
						}
						if(rowSpan == 0) rowSpan = 1;
						out.print("<td rowspan='"+rowSpan+"'>"+platforms[i].getName()
								+"</td><td rowspan='"+rowSpan+"'>"+formatA(platforms[i].getUrl(),4)+"</td>");
					}
					
					out.print("<td >"+modules[j].getName()
								+"</td><td>"+formatA(modules[j].getUri(),4)+"</td>");
					
					out.print("<td>--</td><td>--</td>");
					
					out.print("<td>--</td>");
					
					out.print("<td> <a href='./module.jsp?action=modify&pid="+platforms[i].getId()+"&mid="+modules[j].getId()+"'>修改模块</a>"
									+"|<a href='./module.jsp?action=remove&pid="+platforms[i].getId()+"&mid="+modules[j].getId()+"'>删除模块</a>"
									+"|<a href='./webpage.jsp?action=add&pid="+platforms[i].getId()+"&mid="+modules[j].getId()+"'>添加页面</a></td>");
					
					
					if(j == 0){
						int rowSpan = 0;
						Module tmpModules[] = platforms[i].getModules();
						for(int x = 0; x < tmpModules.length ; x++){
							if(tmpModules[x].getWebPages().length > 0)
									rowSpan += tmpModules[x].getWebPages().length;
								else
									rowSpan += 1;
						}
						if(rowSpan == 0) rowSpan = 1;
						out.print("<td rowspan='"+rowSpan+"'> <a href='./platform.jsp?action=modify&pid="+platforms[i].getId()+"'>修改平台</a>|"
									+"<a href='./platform.jsp?action=remove&pid="+platforms[i].getId()+"'>删除平台</a>|"
									+"<a href='./module.jsp?action=add&pid="+platforms[i].getId()+"'>添加模块</a></td>");
					}
					out.print("</tr>\n");
				}
			}
		}else{
			out.print("<tr align='center' bgcolor='#FFFFFF'>");
			out.print("<td>"+platforms[i].getName()	+"</td><td>"+formatA(platforms[i].getUrl(),4)+"</td>");
			out.print("<td>--</td><td>--</td>");
			out.print("<td>--</td><td>--</td>");
			out.print("<td>--</td>");
			out.print("<td>--</td>");
			out.print("<td><a href='./module.jsp?action=add&pid="+platforms[i].getId()+"'>添加模块</a></td>");
			out.print("</tr>\n");
			
		}
	}
%></table><br>
<a href='./platform.jsp?action=add'>添加新的平台</a>

</body>
</html> 
