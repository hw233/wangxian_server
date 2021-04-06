<%@page import="com.xuanzhi.tools.authorize.Role"%>
<%@page import="com.xuanzhi.tools.authorize.User"%>
<%@page import="com.xuanzhi.tools.authorize.UserManager"%>
<%@page import="com.xuanzhi.tools.authorize.RoleManager"%>
<%@page import="com.xuanzhi.tools.authorize.AuthorizeManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%
	//如果是高级经理则vip目录下所有页面可见，否则有选择的可见
	String curUser = (String)session.getAttribute("authorize.username");

	AuthorizeManager authorizeManager = AuthorizeManager.getInstance();
	RoleManager roleManager = authorizeManager.getRoleManager();
	UserManager userManager = authorizeManager.getUserManager();
	User user = userManager.getUser(curUser);
	Role r = roleManager.getRole("role_vip_common");
	int level = 2; //普通经理 可以看只属于自己的信息
	if(user != null)
	{
		if(user.isRoleExists(r))
		{
			
		}
		else 
		{
			r = roleManager.getRole("role_vip_manager");
			
			if(user.isRoleExists(r))
			{
				level = 1; //高级经理 可以看属于所有人的信息
			}
		}
	}
	else
	{
		out.println("<script>window.alert('无权限!');</script>");
		return;
	}

%>   