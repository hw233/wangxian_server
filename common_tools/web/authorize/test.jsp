<%@ page contentType="text/html;charset=utf-8" 
import="java.io.*,java.util.*,com.xuanzhi.tools.text.*,
com.xuanzhi.tools.authorize.*"%><%
	AuthorizeManager am = AuthorizeManager.getInstance();	
	String errorMessage = null;
	boolean submitted = "true".equals(request.getParameter("submitted"));
	
	String name = request.getParameter("name");
	if(name == null && session.getAttribute(AuthorizedServletFilter.USER) != null){
		User user = (User)session.getAttribute(AuthorizedServletFilter.USER);
		name = user.getName();
	}
	String url = request.getParameter("url");
	String ip = request.getParameter("ip");
	
	if(submitted){
		User user = am.getUserManager().getUser(name);
		if(user == null){
			errorMessage = "指定的用户不存在！请重新输入用户名";
		}else{
			boolean canAccess = false;
			
			StringBuffer sb = new StringBuffer();
			PlatformManager platformManager = am.getPlatformManager();
			Platform platform = platformManager.getPlatformByUrl(url);
			if(platform == null){
				sb.append("找不到与URL匹配的平台<br>");
			}else{
				sb.append("与URL匹配的平台为『"+platform.getName()+"』<br>");
			
				boolean isOfficeAddress = am.isIpAllows(ip);
				
				if(isOfficeAddress)
					sb.append("IP["+ip+"]为办公室IP<br>");
				else
					sb.append("IP["+ip+"]为外部IP<br>");
				
	    		Role roles[] = user.getRoles();
	    		sb.append("用户配置的角色数目为"+roles.length+"<br>");
	    		
	    		for(int i = 0 ; i < roles.length ; i++){
		    		if(roles[i].isValid() == false){
		    			sb.append("{"+i+"}"+",角色『"+roles[i].getName()+"』当前状态为不可用<br>");
		    			continue;
		    		}
		    		PlatformPermission pp = am.getPermissionManager().getPlatformPermission(platform, roles[i]);
		    		
		    		if(pp != null){
						sb.append("{"+i+"}"+",角色『"+roles[i].getName()+"』与平台『"+platform.getName()+"』之间的权限关系为：" + Platform.PERMISSION_NAMES[pp.getPermission()]+"<br>");
		    			if(pp.isAccessOutOfficeEnable() || isOfficeAddress){
		    				int permission = pp.getPermission();
		    				if(permission == Platform.WHOLE_PERMISSION){
		    					canAccess = true;
		    					break;
		    				}else if(permission == Platform.PART_PERMISSION){
		    					Module module = platformManager.getModuleByUrl(platform,url);
		    					
		    					if(module != null){

		    						ModulePermission mp = am.getPermissionManager().getModulePermission(module, roles[i]);
									if(mp != null){
										if(mp.isAccessOutOfficeEnable() || isOfficeAddress){
											int permission2 = mp.getPermission();
											if(permission2 == Module.WHOLE_PERMISSION){
												sb.append("{"+i+"}"+",角色『"+roles[i].getName()+"』与平台『"+platform.getName()+"』匹配的模块为『" + module.getName()+"』,权限为全部访问<br>");
												canAccess = true;
											}else if(permission2 == Module.PART_PERMISSION){
												sb.append("{"+i+"}"+",角色『"+roles[i].getName()+"』与平台『"+platform.getName()+"』匹配的模块为『" + module.getName()+"』,权限为部分访问，进一步检查页面权限<br>");
												
												WebPage ws[] = module.getWebPages();
												for(int k = 0 ; k < ws.length ; k++){
													WebPagePermission wp = am.getPermissionManager().getWebPagePermission(ws[k], roles[i]);
													if(wp != null){
														if(wp.isAccessOutOfficeEnable() || isOfficeAddress){
															int p2 = wp.getPermission();
															if(p2 == WebPage.WHOLE_PERMISSION){
																if(url.matches(ws[k].getPagePattern())){
																	sb.append("{"+i+"}"+",角色『"+roles[i].getName()+"』与平台『"+platform.getName()+"』匹配的模块为『"  
	+ module.getName()+"』页面为『"+ws[k].getName()+"』,模式匹配成功,pattern="+ws[k].getPagePattern()+"<br>");
																	canAccess = true;
																}else{
																	sb.append("{"+i+"}"+",角色『"+roles[i].getName()+"』与平台『"+platform.getName()+"』匹配的模块为『" 
	+ module.getName()+"』页面为『"+ws[k].getName()+"』,模式匹配不成功,pattern="+ws[k].getPagePattern()+"<br>");
																}
															}else{
																sb.append("{"+i+"}"+",角色『"+roles[i].getName()+"』与平台『"+platform.getName()+"』匹配的模块为『" + module.getName()+"』页面为『"+ws[k].getName()+"』,配置了不可访问权限<br>");
															}
														}else{
															sb.append("{"+i+"}"+",角色『"+roles[i].getName()+"』与平台『"+platform.getName()+"』匹配的模块为『" + module.getName()+"』页面为『"+ws[k].getName()+"』,IP限制不能访问<br>");
														}
													}else{
														sb.append("{"+i+"}"+",角色『"+roles[i].getName()+"』与平台『"+platform.getName()+"』匹配的模块为『" + module.getName()+"』页面为『"+ws[k].getName()+"』,没有配置权限<br>");
													}
												}
											}
										}else{
											sb.append("{"+i+"}"+",角色『"+roles[i].getName()+"』与平台『"+platform.getName()+"』匹配的模块为『" + module.getName()+"』,IP限制不能访问<br>");
										}
									}else{
										sb.append("{"+i+"}"+",角色『"+roles[i].getName()+"』与平台『"+platform.getName()+"』匹配的模块为『" + module.getName()+"』,没有配置权限，不能访问<br>");
									}
		
		    						if(canAccess){
		    							break;
		    						}else{
		    							sb.append("{"+i+"}"+",角色『"+roles[i].getName()+"』与平台『"+platform.getName()+"』匹配的模块为" + module.getName()+",结果为不能访问<br>");
		    						}
		    					}else{
		    						sb.append("{"+i+"}"+",角色『"+roles[i].getName()+"』与平台『"+platform.getName()+"』没有匹配的模块<br>");
		    					}
		    				}
		    			}else{
		    				sb.append("{"+i+"}"+",角色『"+roles[i].getName()+"』与平台『"+platform.getName()+"』之间的权限关系为：由于IP设置禁止访问<br>");
		    			}
		    		}else{
		    			sb.append("{"+i+"}"+",角色『"+roles[i].getName()+"』与平台『"+platform.getName()+"』之间无权限关系<br>");
		    		}
		    	}
			}
			if(canAccess){
				errorMessage = "测试结果：拥有访问权限，日志如下：<br><div align='left'>" + sb.toString()+"</div>";
			}else{
				errorMessage = "测试结果：没有访问权限，日志如下：<br><div align='left'>" + sb.toString()+"</div>";
			}
		}
	}
%>
<html>
<head>
</head>
<body>
<%
	if(errorMessage != null){
		%>
		<table border='0' cellpadding='0' cellspacing='3' width='100%' bgcolor='#FF0000' align='center'>
		<tr bgcolor='#FFFFFF' align='center'><td><font color='red'><%=errorMessage %></font></td></tr>
		</table><br/><br/>
		<%
	}
%><form id='two' name='two'><input type='hidden' name='submitted' value='true'>

<table border='0' width='100%' cellpadding='0' cellspacing='1' bgcolor='#000000' align='center'>
<tr bgcolor='#00FFFF' align='center'><td>属性名称</td><td>属性值</td><td>属性说明</td></tr>
<tr bgcolor='#FFFFFF' align='center'>
<td>请选择要测试的用户名</td>
<td><select id='name' name='name'><%
	User users[] = am.getUserManager().getUsers();
	for(int i = 0 ; i < users.length ; i++){
		if(users[i].getName().equals(name))
			out.println("<option value='"+users[i].getName()+"' selected >"+users[i].getRealName()+"</option>");
		else
			out.println("<option value='"+users[i].getName()+"'>"+users[i].getRealName()+"</option>");
	}
%></select></td>
<td>用户名</td>
</tr>
<tr bgcolor='#FFFFFF' align='center'>
<td>要测试的页面URL</td>
<td><input type='text' id='url' name='url' size='60'  value='<%=url==null?"":url %>'></td>
<td>比如：http://211.99.200.92:8086/ authorize/admin/platforms.jsp ?username=xxxxxx&ysafklasf=sjfklafs</td>
</tr>

<tr bgcolor='#FFFFFF' align='center'>
<td>访问的IP</td>
<td><input type='text' id='ip' name='ip' size='40'  value='<%=ip==null?request.getRemoteAddr():ip %>'></td>
<td>访问的IP地址</td>
</tr>

<tr bgcolor='#00FFFF' align='center'><td colspan=3><input type='submit' value='测   试'></td></tr>
</table>
</form>
</body>
</html> 
