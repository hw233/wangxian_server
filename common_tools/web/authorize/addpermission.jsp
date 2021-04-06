<%@ page contentType="text/html;charset=utf-8" 
import="java.io.*,java.util.*,com.xuanzhi.tools.text.*,
com.xuanzhi.tools.authorize.*"%><%
	AuthorizeManager am = AuthorizeManager.getInstance();	

	int step = 0;
	try{
		step = Integer.parseInt(request.getParameter("step"));
	}catch(Exception e){}
	Role role = null;
	String action = request.getParameter("action");
	
	String platform = request.getParameter("platform");
	String moduleName = request.getParameter("module");
	String webPageName= request.getParameter("page");
	
	String rid = request.getParameter("role");
	if(rid != null)
		role = am.getRoleManager().getRole(rid);
	boolean submitted = "true".equals(request.getParameter("submitted"));
	String errorMessage = null;
	
	if(step > 0 && role == null){
		errorMessage = "制定的角色["+rid+"]不存在，请重新进入此页面选择角色！";
	}
	if(step > 0 && !action.equals("platform") && !action.equals("module")&&!action.equals("webpage")){
		errorMessage = "制定的权限类型["+action+"]不存在，请重新进入此页面选择角色！";
	}
	
	if(errorMessage == null && step == 2 && submitted){
		if(action.equals("platform")){
			Platform ps[] = am.getPlatformManager().getPlatforms();
			for(int i = 0 ;i < ps.length ; i++){
				String s = request.getParameter("perm_"+ps[i].getId());
				if(s != null && s.length() > 0){
					PlatformPermission pp = am.getPermissionManager().getPlatformPermission(ps[i],role);
					if(pp == null){
						pp = am.getPermissionManager().addPlatformPermission(ps[i],role,Integer.parseInt(s));
					}else{
						pp.setPermission(Integer.parseInt(s));
					}
					s = request.getParameter("office_"+ps[i].getId());
					if(s != null){
						if(s.equals("true")){
							pp.setAccessOutOfficeEnable(true);
						}else{
							pp.setAccessOutOfficeEnable(false);
						}
					}
				}
			}
			am.saveTo(am.getConfigFile());
			errorMessage = "平台权限设置成功！";
		}else if(action.equals("module")){
			Platform ps[] = am.getPlatformManager().getPlatforms();
			for(int i = 0 ;i < ps.length ; i++){
				Module ms[] = ps[i].getModules();
				for(int j = 0 ; j < ms.length ; j++){
					String s = request.getParameter("perm_"+ps[i].getId()+"_"+ms[j].getId());
					if(s != null && s.length() > 0){
						ModulePermission pp = am.getPermissionManager().getModulePermission(ms[j],role);
						if(pp == null){
							pp = am.getPermissionManager().addModulePermission(ms[j],role,Integer.parseInt(s));
						}else{
							pp.setPermission(Integer.parseInt(s));
						}
						s = request.getParameter("office_"+ps[i].getId()+"_"+ms[j].getId());
						if(s != null){
							if(s.equals("true")){
								pp.setAccessOutOfficeEnable(true);
							}else{
								pp.setAccessOutOfficeEnable(false);
							}
						}
					}
				}
			}
			am.saveTo(am.getConfigFile());
			errorMessage = "模块权限设置成功！";
		}else if(action.equals("webpage")){
			Platform ps[] = am.getPlatformManager().getPlatforms();
			for(int i = 0 ;i < ps.length ; i++){
				Module ms[] = ps[i].getModules();
				for(int j = 0 ; j < ms.length ; j++){
					WebPage[] pages = ms[j].getWebPages();
					for(int k = 0 ; k < pages.length ; k++){
						String s = request.getParameter("perm_"+ps[i].getId()+"_"+ms[j].getId()+"_"+pages[k].getId());
						if(s != null && s.length() > 0){
							WebPagePermission pp = am.getPermissionManager().getWebPagePermission(pages[k],role);
							if(pp == null){
								pp = am.getPermissionManager().addWebPagePermission(pages[k],role,Integer.parseInt(s));
							}else{
								pp.setPermission(Integer.parseInt(s));
							}
							s = request.getParameter("office_"+ps[i].getId()+"_"+ms[j].getId()+"_"+pages[k].getId());
							if(s != null){
								if(s.equals("true")){
									pp.setAccessOutOfficeEnable(true);
								}else{
									pp.setAccessOutOfficeEnable(false);
								}
							}
						}
					}
				}
			}
			am.saveTo(am.getConfigFile());
			errorMessage = "页面权限设置成功！";
		}
	}
%><html>
<head>
</head>
<body>

<h3>设置权限配置</h3>
<% 
	if(errorMessage != null){
		%>
		<table border='0' cellpadding='0' cellspacing='3' width='100%' bgcolor='#FF0000' align='center'>
		<tr bgcolor='#FFFFFF' align='center'><td><font color='red'><%=errorMessage %></font></td></tr>
		</table><br/><br/>
		<%
	}
	if(step == 0 && errorMessage == null){
%>
<h4>第一步:请选择角色和权限类型</h4>
<form id='first' name='first'><input type='hidden' name='submitted' value='true'>
<input type='hidden' name='step' value='1'>
<table border='0' width='100%' cellpadding='0' cellspacing='1' bgcolor='#000000' align='center'>
<tr bgcolor='#FFFFFF' align='center'>
<td>请选择要添加权限的角色</td>
<td><select id='role' name='role'><option value=''>-- 请选择角色 --</option><%
	Role roles[] = am.getRoleManager().getRoles();
	for(int i = 0 ; i < roles.length ; i++){
		out.print("<option value='"+roles[i].getId()+"'>"+roles[i].getName()+"</option>");
	}
%></select></td>
<td></td>
</tr>
<tr bgcolor='#FFFFFF' align='center'>
<td>请选择权限类型</td>
<td><select id='action' name='action'>
<option value=''>-- 请选择权限类型 --</option>
<option value='platform'>平台</option>
<option value='module'>模块</option>
<option value='webpage'>页面</option></select></td>
<td></td>
</tr>
<tr bgcolor='#00FFFF' align='center'><td colspan=3><input type='submit' value='提交'></td></tr>
</table>
</form>
<%
	}else{
		out.print("您选择了为角色『"+role.getName()+"』添加『"+action+"』权限！<br>");
	}

	if(step == 1 && errorMessage == null){
	%>
	<h4>第二步:设置角色与<%=action%>的权限</h4>
	<form id='first' name='first'><input type='hidden' name='submitted' value='true'>
	<input type='hidden' name='step' value='2'>
	<input type='hidden' name='action' value='<%=action %>'>
	<input type='hidden' name='platform' value='<%=platform %>'>
	<input type='hidden' name='role' value='<%=role.getId() %>'>
	<%	
		if(action.equals("platform")){
%>
<table border='0' width='100%' cellpadding='0' cellspacing='1' bgcolor='#000000' align='center'>
	<tr align='center' bgcolor='#00FFFF'>
	<td>平台</td><td>权限</td><td>访问IP</td>
	</tr>
	<%
	Platform ps[] = am.getPlatformManager().getPlatforms();
	for(int i = 0 ; i < ps.length ; i++){
		if(platform != null && platform.length()>0 && ps[i].getId().equals(platform) ==false) continue;
		PlatformPermission pp = am.getPermissionManager().getPlatformPermission(ps[i],role);
		out.print("<tr align='center' bgcolor='#00FFFF'>");
		out.print("<td>"+ps[i].getName()+"</td>");
		out.print("<td>");
		for(int j = 0 ; j < Platform.PERMISSION_NAMES.length ; j++){
			if(pp != null && pp.getPermission() == j)
				out.print("<input type='radio' name='perm_"+ps[i].getId()+"' value='"+j+"' checked>"+Platform.PERMISSION_NAMES[j]+"&nbsp;");
			else
				out.print("<input type='radio' name='perm_"+ps[i].getId()+"' value='"+j+"'>"+Platform.PERMISSION_NAMES[j]+"&nbsp;");
		}
		out.print("</td>");
		out.print("<td>");
		if(pp != null && pp.isAccessOutOfficeEnable()){
			out.print("<input type='radio' name='office_"+ps[i].getId()+"' value='true' checked>办公室外可访问&nbsp;");
		}else{
			out.print("<input type='radio' name='office_"+ps[i].getId()+"' value='true'>办公室外可访问&nbsp;");
		}
		if(pp != null && pp.isAccessOutOfficeEnable()){
			out.print("<input type='radio' name='office_"+ps[i].getId()+"' value='false'>办公室外不可访问&nbsp;");
		}else{
			out.print("<input type='radio' name='office_"+ps[i].getId()+"' value='false' checked>办公室外不可访问&nbsp;");
		}
		out.print("</td>");
		out.print("</tr>");
	}
	%>
<tr bgcolor='#00FFFF' align='center'><td colspan=3><input type='submit' value='修改平台权限'></td></tr>
</table>
<% 			
		}else if(action.equals("module")){
%>
<table border='0' width='100%' cellpadding='0' cellspacing='1' bgcolor='#000000' align='center'>
	<tr align='center' bgcolor='#00FFFF'>
	<td>平台</td><td>模块</td><td>权限</td><td>访问IP</td>
	</tr>
	<%
	Platform ps[] = am.getPlatformManager().getPlatforms();
	for(int i = 0 ; i < ps.length ; i++){
		if(platform != null && ps[i].getId().equals(platform) ==false) continue;
		Module modules[] = ps[i].getModules();
		for(int k = 0 ; k < modules.length ; k++){
			if(moduleName!=null&&moduleName.length()>0&&modules[k].getId().equals(moduleName) ==false) continue;
			ModulePermission pp = am.getPermissionManager().getModulePermission(modules[k],role);
			out.print("<tr align='center' bgcolor='#00FFFF'>");
			out.print("<td>"+ps[i].getName()+"</td>");
			out.print("<td>"+modules[k].getName()+"</td>");
			out.print("<td>");
			for(int j = 0 ; j < Module.PERMISSION_NAMES.length ; j++){
				if(pp != null && pp.getPermission() == j)
					out.print("<input type='radio' name='perm_"+ps[i].getId()+"_"+modules[k].getId()+"' value='"+j+"' checked>"+Module.PERMISSION_NAMES[j]+"&nbsp;");
				else
					out.print("<input type='radio' name='perm_"+ps[i].getId()+"_"+modules[k].getId()+"' value='"+j+"'>"+Module.PERMISSION_NAMES[j]+"&nbsp;");
			}
			out.print("</td>");
			out.print("<td>");
			if(pp != null && pp.isAccessOutOfficeEnable()){
				out.print("<input type='radio' name='office_"+ps[i].getId()+"_"+modules[k].getId()+"' value='true' checked>办公室外可访问&nbsp;");
			}else{
				out.print("<input type='radio' name='office_"+ps[i].getId()+"_"+modules[k].getId()+"' value='true'>办公室外可访问&nbsp;");
			}
			if(pp != null && pp.isAccessOutOfficeEnable()){
				out.print("<input type='radio' name='office_"+ps[i].getId()+"_"+modules[k].getId()+"' value='false'>办公室外不可访问&nbsp;");
			}else{
				out.print("<input type='radio' name='office_"+ps[i].getId()+"_"+modules[k].getId()+"' value='false' checked>办公室外不可访问&nbsp;");
			}
			out.print("</td>");
			out.print("</tr>");
		}
	}
	%>
	<tr bgcolor='#00FFFF' align='center'><td colspan=4><input type='submit' value='修改模块权限'></td></tr>
</table>
<% 			
			
		}else if(action.equals("webpage")){
%>
<table border='0' width='100%' cellpadding='0' cellspacing='1' bgcolor='#000000' align='center'>
	<tr align='center' bgcolor='#00FFFF'>
	<td>平台</td><td>模块</td><td>页面</td><td>权限</td><td>访问IP</td>
	</tr>
	<%
	Platform ps[] = am.getPlatformManager().getPlatforms();
	for(int i = 0 ; i < ps.length ; i++){
		if(platform != null && ps[i].getId().equals(platform) ==false) continue;
		Module modules[] = ps[i].getModules();
		for(int k = 0 ; k < modules.length ; k++){
			if(moduleName!=null&&moduleName.length()>0&&modules[k].getId().equals(moduleName) ==false) continue;
			WebPage pages[] = modules[k].getWebPages();
			for(int x = 0 ; x < pages.length ; x++){
				if(webPageName!=null&&webPageName.length()>0&&pages[x].getId().equals(webPageName) ==false) continue;
				WebPagePermission pp = am.getPermissionManager().getWebPagePermission(pages[x],role);
				out.print("<tr align='center' bgcolor='#00FFFF'>");
				out.print("<td>"+ps[i].getName()+"</td>");
				out.print("<td>"+modules[k].getName()+"</td>");
				out.print("<td>"+pages[x].getName()+"</td>");
				out.print("<td>");
				for(int j = 0 ; j < WebPage.PERMISSION_NAMES.length ; j++){
					if(pp != null && pp.getPermission() == j)
						out.print("<input type='radio' name='perm_"+ps[i].getId()+"_"+modules[k].getId()+"_"+pages[x].getId()+"' value='"+j+"' checked>"+WebPage.PERMISSION_NAMES[j]+"&nbsp;");
					else
						out.print("<input type='radio' name='perm_"+ps[i].getId()+"_"+modules[k].getId()+"_"+pages[x].getId()+"' value='"+j+"'>"+WebPage.PERMISSION_NAMES[j]+"&nbsp;");
				}
				out.print("</td>");
				out.print("<td>");
				if(pp != null && pp.isAccessOutOfficeEnable()){
					out.print("<input type='radio' name='office_"+ps[i].getId()+"_"+modules[k].getId()+"_"+pages[x].getId()+"' value='true' checked>办公室外可访问&nbsp;");
				}else{
					out.print("<input type='radio' name='office_"+ps[i].getId()+"_"+modules[k].getId()+"_"+pages[x].getId()+"' value='true'>办公室外可访问&nbsp;");
				}
				if(pp != null && pp.isAccessOutOfficeEnable()){
					out.print("<input type='radio' name='office_"+ps[i].getId()+"_"+modules[k].getId()+"_"+pages[x].getId()+"' value='false'>办公室外不可访问&nbsp;");
				}else{
					out.print("<input type='radio' name='office_"+ps[i].getId()+"_"+modules[k].getId()+"_"+pages[x].getId()+"' value='false' checked>办公室外不可访问&nbsp;");
				}
				out.print("</td>");
				out.print("</tr>");	
			}
			
		}
	}
	%>
	<tr bgcolor='#00FFFF' align='center'><td colspan=5><input type='submit' value='修改页面权限'></td></tr>
</table>
<% 			
			
		}//end of action == webpage
	}//step == 1

	out.print("<a href='./permissions.jsp'>返回权限列表页面</a><br/>");
	if(role != null){
		if(platform != null)
			out.print("<a href='./permissions.jsp?role="+rid
	+"&platform="+platform+"'>返回当前角色『"+role.getName()+"』与平台『"+platform+"』的权限列表页面</a><br/>");
			out.print("<a href='./permissions.jsp?role="+rid+"'>返回当前角色『"+role.getName()+"』的权限列表页面</a>");
	}
%>
</body>
</html> 
