<%@ page contentType="text/html;charset=utf-8" import="java.io.*,java.util.*,
com.xuanzhi.tools.resource.*,com.xuanzhi.tools.text.*,"%><%

	boolean bPackage = false;
	if(request.getParameter("bPackage") != null && request.getParameter("bPackage").equals("true")){
		bPackage = true;
	}
	
	//TODO 需要应用层设置
	DefaultResourceManager drm = null;
	
	if(drm == null){
		out.println("这只是一个模板页面，请用此页面的项目拷贝此页面，并且设置DefaultResourceManager实例！");
		return;
	}
	
	if(request.getParameter("reload") != null && request.getParameter("reload").equals("true")){
		drm.load();
	}
	
	if(request.getParameter("action") != null && request.getParameter("action").equals("settag")){
		String identity = request.getParameter("identity");
		ResourcePackage rp = drm.getResourcePackageByIdentity(identity);
		String tag = request.getParameter("tag");
		if(rp != null){
			drm.setResourcePackageValidFlag(rp,tag);
		}
	}
	

	ArrayList<ResourcePackage> al = null;
	if(bPackage){
		al = drm.brpList;
	}else{
		al = drm.arpList;
	}
	
%><html>
<head>
	<title>资源管理</title>
</head>
<body>
<center>
<h2>资源<%= bPackage?"B":"A" %>包管理</h2><br/>
<a href="<%= request.getRequestURI()+"?bPackage=false" %>">资源A包管理</a>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;<a href="<%= request.getRequestURI()+"?bPackage=true" %>">资源B包管理</a>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;<a href='<%= request.getRequestURI()+"?reload=true" %>'>重新加载对应的配置文件</a>
<table border='0' cellpadding='0' cellspacing='1' width='100%' bgcolor='#000000' align='left'>
<tr bgcolor='#88FF65'>
	<td>编号</td>
	<td>编号</td>
	<td><input type='text' name='day' size=10 value='<%= day %>'></td>
</tr>
<%
	for(int i = 0 ; i < al.size() ; i++){
		ResourcePackage rp = al.get(i);
		out.println("<form><input type='hidden' name='action' value='settag'><input type='hidden' name='identity' value='"+rp.getIdentity()+"'>");
		out.println("<tr bgcolor='#FFFFFF'>");
		out.println("<td>"+(i+1)+"</td>");
		out.println("<td>"+rp.masterVersion+"."+rp.slaveVersion +"</td>");
		out.println("<td>"+(rp.aPackageFlag?"A包":"B包")+"</td>");
		out.println("<td>"+(rp.fullPackage?"全包":"半包")+"</td>");
		out.println("<td>"+rp.fileNum+"</td>");
		out.println("<td>"+rp.fileSize+"</td>");
		String s = "";
		for(int j = 0 ; j < rp.downloadUrls.length ;j++){
			s += "<a href='"+rp.downloadUrls[j]+"'>rp.downloadUrls[j]</a>";
			if(j < rp.downloadUrls.length-1) s += "<br/>";
		}
		out.println("<td>"+s+"</td>");
		
		String tag = drm.getResourcePackageValidFlag(rp);
		if(tag ==  null) tag = "未生效";
		out.println("<td><input type='text' name='tag' value='"+tag+"' size='15'><input type='submit' value='提交'></td>");
		out.println("</tr></form>");
	}
%>
</table>
<i>tag为*标识对任何tag生效，否则可以是以逗号分隔的字符串，tag可以是渠道串，或者某种特殊的标识，比如whitelist</i>
</center>

</body>
</html>
