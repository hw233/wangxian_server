<%@page import="java.util.HashSet"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="com.fy.engineserver.smith.ProcessCat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<%!
Thread thread = null;
int count = 0;
%>
<%
if(thread == null){
	count = 0;
	thread = new Thread(new Runnable(){
		public void run(){
			while(count >= 0){
				count++;
				try{
					Thread.sleep(10000);
				}catch(Exception ex){
					
				}
				try {
					Field field = ProcessCat.getInstance().getClass().getDeclaredField("kickMap");
					field.setAccessible(true);
					HashMap<Long,HashSet<String>> kMap = (HashMap<Long,HashSet<String>>)field.get(ProcessCat.getInstance());
					int oldSzie = kMap.size();
					kMap.clear();
					System.out.println(count+"  原有"+oldSzie+"  kMap.size--"+kMap.size());
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
			thread = null;
		}
	},"processCat");
	thread.start();
	out.println("processCat");
}
%>
</head>
<body>
</body>
</html>