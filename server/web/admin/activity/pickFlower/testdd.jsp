<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.xuanzhi.tools.simplejpa.SimpleEntityManager"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="com.xuanzhi.tools.simplejpa.impl.MetaDataEntity"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="com.xuanzhi.tools.text.StringUtil"%>
<%@page import="com.xuanzhi.tools.simplejpa.impl.SimpleEntityManagerOracleImpl"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查看</title>
</head>
<body>

	<%
		try{
			SimpleEntityManager sem =  SimpleEntityManagerFactory.getSimpleEntityManager(ArticleEntity.class);
			out.print(sem+"     className:"+sem.getClass().getName()+"<br/>");
			
			Class clazz = Class.forName("com.xuanzhi.tools.simplejpa.impl.SimpleEntityManagerOracleImpl");
			
			Field[] fs = clazz.getDeclaredFields();
			
			for(Field f : fs){
				out.print(f.getName()+"<br/>");
			}
			out.print("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx<br/>");
			Field  classInfo = clazz.getDeclaredField("classInfo");
			classInfo.setAccessible(true);
			Map<Class<?>,Integer> map = (Map<Class<?>,Integer> )classInfo.get((SimpleEntityManagerOracleImpl)sem);
			
			for(Map.Entry<Class<?>,Integer> en : map.entrySet()){
				out.print("key:"+en.getKey().getName()+"    value:"+en.getValue()+"<br/>");
			}
			out.print("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx<br/>");
			Field  mde = clazz.getDeclaredField("mde");
			mde.setAccessible(true);
			MetaDataEntity  me = (MetaDataEntity)mde.get(sem);
			
			out.print("完成");
		}catch(Exception e){
			out.print(StringUtil.getStackTrace(e));
		}
	%>


</body>
</html>