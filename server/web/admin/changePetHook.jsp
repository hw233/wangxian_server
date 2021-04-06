<%@page import="com.fy.engineserver.sprite.pet.PetManager"%>
<%@page import="com.fy.engineserver.sprite.pet.Pet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%

	Pet pet = PetManager.getInstance().getPet(1100000000002446421L);
	if (pet != null) {
		pet.setHookInfo(null);
		out.print("设置完成");
	} else {
		out.print("宠物不存在");
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>清除宠物挂机</title>
</head>
<body>

</body>
</html>