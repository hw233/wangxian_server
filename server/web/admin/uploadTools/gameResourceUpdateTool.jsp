<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="/css/style.css" />
<title>资源更新</title>
<%@include file="gameResourceUpdateTool_config.jsp" %>
</head>
<body>
	<%
		String pwd = request.getParameter("pwd");
		boolean istrue =false;
		if(pwd!=null){
			if(powers.contains(pwd)){
				istrue = true;
				session.setAttribute("pwd", pwd);
	%>
			<jsp:forward page="gameResourceUpdateTool_frame.jsp"/>
	<%			
			}else{
				out.print("你好："+pwd+"请您去申请操作权限，带来不便，请原谅");
			}
		}
		
		if(!istrue){
	%>
		<form>
		<table>
			<tr><th>身份验证：</th><td><input type='text' name='pwd'></td></tr>
			<tr><th>操作：</th><td><input type="submit" value='确定'><input type="reset" value='取消'></td></tr>
		</table>
		</form>
	<%		
		}
	%>	
	
</body>
</html>