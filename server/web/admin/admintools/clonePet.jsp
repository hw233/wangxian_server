<%@page import="java.util.Arrays"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.fy.engineserver.sprite.pet.PetManager"%>
<%@page import="com.fy.engineserver.sprite.pet.Pet"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String petAId = request.getParameter("petAId");
	if ("".equals(petAId) || petAId == null ) {
		petAId = "";
	} else {
		PetManager pm = PetManager.getInstance();
		Pet petA = pm.getPet(Long.valueOf(petAId));
		if(petA!=null){
			out.print(petA.getName()+"--"+petA.getId()+"--"+petA.getCareer()+"<br>");
		}
	}
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>宠物拷贝</title>
</head>
<body>
	<form action="">
		把宠物A<input type="text" name="petAId" value="<%=petAId%>"/>
		<input type="submit" value="submit" />
	</form>

</body>
</html>