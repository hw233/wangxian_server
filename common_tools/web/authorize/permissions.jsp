<%@ page contentType="text/html;charset=utf-8" 
import="java.io.*,java.util.*,com.xuanzhi.tools.text.*,
com.xuanzhi.tools.authorize.*"%><%
	AuthorizeManager am = AuthorizeManager.getInstance();	
	RoleManager rm = am.getRoleManager();
	Role roles[] = rm.getRoles();
	
	int headerRepeat = 20;
	int rowCount = 0;

	int columnCount = 0;
	String platformName = request.getParameter("platform");	
	String roleName = request.getParameter("role");	
%><html>
<head>
</head>
<body>

<h3>平台权限配置情况汇总</h3>

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
%></select>&nbsp;&nbsp;
请选择角色： <select name='role'><option value=''>-- 请选择角色 --</option>
<%
	for(int i = 0 ; i < roles.length ; i++){
		if(roles[i].getId().equals(roleName)){
                        out.print("<option value='"+roles[i].getId()+"' selected>"+roles[i].getName()+"</option>");
		}else{
                        out.print("<option value='"+roles[i].getId()+"' >"+roles[i].getName()+"</option>");
		}
	}	
%></select><input type='submit' value='提  交'></form><br/>

<table border='0' cellpadding='0' cellspacing='1' bgcolor='#000000' align='center'>
<tr align='center' bgcolor='#00FFFF'>
<td width='80'>平台</td><td width='80'>模块</td><td width='80'>页面</td><td width='2'></td>
<% 
	
	for(int i = 0 ; i < roles.length ; i++){
		if(roleName != null && roleName.length() > 0 && roles[i].getId().equals(roleName) == false)
			continue;
		columnCount++;	
		out.print("<td width='80'>"+roles[i].getName()+"</td>");
	}
%>
</tr>
<%
	for(int i = 0 ; i < platforms.length ; i++){
		
		if(platformName != null && platformName.length() > 0 && platforms[i].getId().equals(platformName) == false) continue;

		rowCount++;
		
		if(rowCount % headerRepeat == 0){
				%><tr align='center' bgcolor='#00FFFF'>
			<td width='80'>平台</td><td width='80'>模块</td><td width='80'>页面</td><td width='2'></td>
			<% 
		
			for(int ii = 0 ; ii < roles.length ; ii++){
				if(roleName != null && roleName.length() > 0 && roles[ii].getId().equals(roleName) == false)
                        		continue;
				out.print("<td width='80'>"+roles[ii].getName()+"</td>");
			}
		%>
		</tr><%
			}
		out.print("<tr align='center' bgcolor='#FFFFFF'><td>"+platforms[i].getName()+"</td><td>--</td><td>--</td><td width='2'></td>");
		for(int j = 0 ; j < roles.length ; j++){
			if(roleName != null && roleName.length() > 0 && roles[j].getId().equals(roleName) == false)
                                        continue;
			PlatformPermission pp = am.getPermissionManager().getPlatformPermission(platforms[i],roles[j]);
			
			out.print("<td>");
			if(pp == null){
				out.print("<img src='./delete.png' alt='无权限配置'>");
			}else if(pp.getPermission() == Platform.WHOLE_PERMISSION){
				out.print("<img src='./accept.png' alt='全部权限'>");
			}else if(pp.getPermission() == Platform.PART_PERMISSION){
				out.print("<img src='./add.png' alt='部分权限'>");
			}else{
				out.print("<img src='./delete.png' alt='没有权限'>");
			}
			
			if(pp == null){
				out.print("<img src='./world.png' alt='无IP限制'>");
			}else if(pp.isAccessOutOfficeEnable()){
				out.print("<img src='./world.png' alt='无IP限制'>");
			}else{
				out.print("<img src='./office.png' alt='只限办公室'>");
			}
			out.print("&nbsp;<a href='./addpermission.jsp?step=1&role="+roles[j].getId()+"&action=platform&platform="+platforms[i].getId()+"'><img src='./wrench.png' alt='设置' border='0'></a>");
			out.print("</td>");
			
		}
		out.print("</tr>");
	}
	
	out.println("<tr align='center' bgcolor='#00FFFF' height=2><td colspan='"+(columnCount+4)+"'></td></tr>");
	for(int i = 0 ; i < platforms.length ; i++){
		if(platformName != null && platformName.length() > 0 && platforms[i].getId().equals(platformName) == false) continue;
		Module modules[] = platforms[i].getModules();
		for(int k = 0 ; k < modules.length ; k++){
			
			rowCount++;
		
		if(rowCount % headerRepeat == 0){
			%><tr align='center' bgcolor='#00FFFF'>
			<td width='80'>平台</td><td width='80'>模块</td><td width='80'>页面</td><td width='2'></td>
			<% 
			
			for(int ii = 0 ; ii < roles.length ; ii++){
				if(roleName != null && roleName.length() > 0 && roles[ii].getId().equals(roleName) == false)
                                        continue;
				out.print("<td width='80'>"+roles[ii].getName()+"</td>");
			}
			%>
			</tr><%
		}
			out.print("<tr align='center' bgcolor='#DDFFFF'><td>"+platforms[i].getName()+"</td><td>"+modules[k].getName()+"</td><td>--</td><td width='2'></td>");
			for(int j = 0 ; j < roles.length ; j++){
				if(roleName != null && roleName.length() > 0 && roles[j].getId().equals(roleName) == false)
                                        continue;
				PlatformPermission pp2 = am.getPermissionManager().getPlatformPermission(modules[k].getPlatform(),roles[j]);
				if(pp2 != null){
					if(pp2.getPermission() == Platform.WHOLE_PERMISSION){
						if(pp2.isAccessOutOfficeEnable()){
							out.print("<td><img src='./accept.png' alt='全部权限'><img src='./world.png' alt='无IP限制'>&nbsp;<img src='./bullet_white.png' width=16 height=16></td>");
						}else{
							out.print("<td><img src='./accept.png' alt='全部权限'><img src='./office.png' alt='只限办公室'>&nbsp;<img src='./bullet_white.png' width=16 height=16></td>");
						}
					}else if(pp2.getPermission() == Platform.PART_PERMISSION){
						ModulePermission pp = am.getPermissionManager().getModulePermission(modules[k],roles[j]);
				
						out.print("<td>");
						if(pp == null){
							out.print("<img src='./delete.png' alt='无权限配置'>");
						}else if(pp.getPermission() == Module.WHOLE_PERMISSION){
							out.print("<img src='./accept.png' alt='全部权限'>");
						}else if(pp.getPermission() == Module.PART_PERMISSION){
							out.print("<img src='./add.png' alt='部分权限'>");
						}else{
							out.print("<img src='./delete.png' alt='没有权限'>");
						}
						
						if(pp == null){
							out.print("<img src='./world.png' alt='无IP限制'>");
						}else if(pp.isAccessOutOfficeEnable()){
							out.print("<img src='./world.png' alt='无IP限制'>");
						}else{
							out.print("<img src='./office.png' alt='只限办公室'>");
						}
						out.print("&nbsp;<a href='./addpermission.jsp?step=1&role="+roles[j].getId()+"&action=module&platform="+modules[k].getPlatform().getId()+"&module="+modules[k].getId()+"'><img src='./wrench.png' alt='设置' border='0'></a>");
						out.print("</td>");
						
					}else{
						out.print("<td><img src='./delete.png' alt='没有权限'><img src='./world.png' alt='无IP限制'>&nbsp;<img src='./bullet_white.png' width=16 height=16></td>");
					}
				}else{
					out.print("<td><img src='./delete.png' alt='无权限配置'><img src='./world.png' alt='无IP限制'>&nbsp;<img src='./bullet_white.png' width=16 height=16></td>");
				}
				
				
			}
			out.print("</tr>");
		}
	}
			
	out.println("<tr align='center' bgcolor='#00FFFF' height=2><td colspan='"+(columnCount+4)+"'></td></tr>");		
			
	for(int i = 0 ; i < platforms.length ; i++){
		if(platformName != null && platformName.length() > 0 && platforms[i].getId().equals(platformName) == false) continue;
		Module modules[] = platforms[i].getModules();
		for(int k = 0 ; k < modules.length ; k++){		
			WebPage pages[] = modules[k].getWebPages();
			for(int x = 0 ; x < pages.length ; x++){
				rowCount++;
		
				if(rowCount % headerRepeat == 0){
					%><tr align='center' bgcolor='#00FFFF'>
				<td width='80'>平台</td><td width='80'>模块</td><td width='80'>页面</td><td width='2'></td>
				<% 
					
					for(int ii = 0 ; ii < roles.length ; ii++){
						if(roleName != null && roleName.length() > 0 && roles[ii].getId().equals(roleName) == false)
                                        	continue;
						out.print("<td width='80'>"+roles[ii].getName()+"</td>");
					}
				%>
				</tr><%
				}
		
				out.print("<tr align='center' bgcolor='#DDFFDD'><td>"+platforms[i].getName()+"</td><td>"+modules[k].getName()+"</td><td>"+pages[x].getName()+"</td><td width='2'></td>");
				for(int j = 0 ; j < roles.length ; j++){
					if(roleName != null && roleName.length() > 0 && roles[j].getId().equals(roleName) == false)
                                        continue;
					PlatformPermission pp2 = am.getPermissionManager().getPlatformPermission(pages[x].getModule().getPlatform(),roles[j]);
					if(pp2 != null){
						if(pp2.getPermission() == Platform.WHOLE_PERMISSION){
							if(pp2.isAccessOutOfficeEnable()){
								out.print("<td><img src='./accept.png' alt='全部权限'><img src='./world.png' alt='无IP限制'>&nbsp;<img src='./bullet_white.png' width=16 height=16></td>");
							}else{
								out.print("<td><img src='./accept.png' alt='全部权限'><img src='./office.png' alt='只限办公室'>&nbsp;<img src='./bullet_white.png' width=16 height=16></td>");
							}
						}else if(pp2.getPermission() == Platform.PART_PERMISSION){
							ModulePermission pp = am.getPermissionManager().getModulePermission(pages[x].getModule(),roles[j]);
				
							if(pp == null){
								out.print("<td><img src='./delete.png' alt='无权限配置'><img src='./world.png' alt='无IP限制'>&nbsp;<img src='./bullet_white.png' width=16 height=16></td>");
							}else{
								if(pp.getPermission() == Module.WHOLE_PERMISSION){
									if(pp.isAccessOutOfficeEnable()){
										out.print("<td><img src='./accept.png' alt='全部权限'><img src='./world.png' alt='无IP限制'>&nbsp;<img src='./bullet_white.png' width=16 height=16></td>");
									}else{
										out.print("<td><img src='./accept.png' alt='全部权限'><img src='./office.png' alt='只限办公室'>&nbsp;<img src='./bullet_white.png' width=16 height=16></td>");
									}
								}else if(pp.getPermission() == Module.PART_PERMISSION){
									WebPagePermission pp3 = am.getPermissionManager().getWebPagePermission(pages[x],roles[j]);
									
									out.print("<td>");
									if(pp3 == null){
										out.print("<img src='./delete.png' alt='无权限配置'>");
									}else if(pp3.getPermission() == WebPage.WHOLE_PERMISSION){
										out.print("<img src='./accept.png' alt='全部权限'>");
									}else{
										out.print("<img src='./delete.png' alt='没有权限'>");
									}
									
									if(pp3 == null){
										out.print("<img src='./world.png' alt='无IP限制'>");
									}else if(pp3.isAccessOutOfficeEnable()){
										out.print("<img src='./world.png' alt='无IP限制'>");
									}else{
										out.print("<img src='./office.png' alt='只限办公室'>");
									}
									out.print("&nbsp;<a href='./addpermission.jsp?step=1&role="+roles[j].getId()+"&action=webpage&platform="+pages[x].getModule().getPlatform().getId()+"&module="+pages[x].getModule().getId()+"&page="+pages[x].getId()+"'><img src='./wrench.png' alt='设置' border='0'></a>");
									out.print("</td>");
									
								}else{
									out.print("<td><img src='./delete.png' alt='没有权限'><img src='./world.png' alt='无IP限制'>&nbsp;<img src='./bullet_white.png' width=16 height=16></td>");
								}
							}
						}else{
							out.print("<td><img src='./delete.png' alt='没有权限'><img src='./world.png' alt='无IP限制'>&nbsp;<img src='./bullet_white.png' width=16 height=16></td>");
						}
					}else{
						out.print("<td><img src='./delete.png' alt='无权限配置'><img src='./world.png' alt='无IP限制'>&nbsp;<img src='./bullet_white.png' width=16 height=16></td>");
					}
				
					
					
					
				}
				out.print("</tr>");
			}
		}
	}
%></table><br>

<img src='./delete.png' alt='没有权限'>：代表着没有权限或者没有配置权限<br>
<img src='./add.png' alt='部分权限'>：代表着部分权限，需要进一步查看后续的模块配置或者页面配置<br>
<img src='./accept.png' alt='没有权限'>：代表着全部权限，无论后续的模块配置或者页面配置是什么<br>
<img src='./office.png' alt='只限办公室'>：代表着只能在公司办公室中访问<br>
<img src='./world.png' alt='无IP限制'>：代表着可以在任意地方访问<br>
<img src='./wrench.png' alt='设置' border='0'>：代表着点击可以配置对应的权限<br>
</body>
</html> 
