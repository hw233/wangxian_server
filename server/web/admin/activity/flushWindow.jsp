<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.menu.WindowManager"%>
<%@page import="java.io.File"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>临时刷新窗口</title>
</head>
<body>

	<%
		String ss = request.getParameter("name");
		if(ss != null && !ss.equals("")){
			WindowManager wm = WindowManager.getInstance();
			File configFile = wm.getConfigFile();
			java.io.FileInputStream input = new java.io.FileInputStream(configFile);
			try{
			wm.loadExcel(input);
			}finally{
				input.close();
			}
			
			out.print("加载成功");
			return;
		}
	
	%>
		<form action="">
			随便:<input type="text" name="name" />
			<input type="submit" value="submit" />
		
		</form>


</body>
</html>