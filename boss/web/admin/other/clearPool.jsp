<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.xuanzhi.tools.dbpool.ConnectionPool"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="com.xuanzhi.tools.simplejpa.impl.SimpleEntityManagerFactoryImpl"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
    
<%
	Field field = SimpleEntityManagerFactoryImpl.class.getDeclaredField("pool");
	if(field != null)
	{
		field.setAccessible(true); 
		ConnectionPool pool = (ConnectionPool)field.get(null);
		if(pool != null)
		{
			out.println("<textarea>清理前数据库连接信息为:"+pool.dumpInfo()+"</textarea>");
		
			pool.reapIdleConnections();
			out.println("<textarea>清理后数据库连接信息为:"+pool.dumpInfo()+"</textarea>");
		}
	}


%>    