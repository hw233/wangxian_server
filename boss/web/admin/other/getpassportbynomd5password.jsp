<%@page import="com.xuanzhi.tools.text.ParamUtils"%>
<%@page import="com.xuanzhi.tools.text.StringUtil"%>
<%@page import="com.fy.boss.authorize.model.Passport"%>
<%@page import="com.fy.boss.authorize.service.PassportManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<% 
		long pid = ParamUtils.getLongParameter(request, "pid", 0);
		String ps = ParamUtils.getParameter(request, "ps","");
		PassportManager passportManager = PassportManager.getInstance();
		Passport p = passportManager.getPassport(pid);
		if(p != null)
		{
			p.setPassWd(StringUtil.hash(ps));
			
			passportManager.update(p);
		
			out.println("[更新passport密码] [id:"+p.getId()+"] [username:"+p.getUserName()+"] [密码:"+p.getPassWd()+"]");
		}
		else
		{
			out.println("[更新passport密码] [没有查到对应的passport] [id:"+pid+"]");
		}
%>