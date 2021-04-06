<%@ page language="java" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>


<%@page import="com.fy.engineserver.activity.pickFlower.PickFlowerManager"%>
<%@page import="com.fy.engineserver.activity.pickFlower.PickFlowerEntity"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.fy.engineserver.activity.pickFlower.FlushPoint"%>
<%@page import="com.fy.engineserver.activity.pickFlower.Flower"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="com.fy.engineserver.activity.pickFlower.FlowerNpc"%><html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>设置同意采花的人</title>
</head>

<body>
	
	<%
	
		String name = request.getParameter("name");
		if(name != null && !name.equals("") ){
			
			int num = Integer.parseInt(name);
			PickFlowerEntity pfe = PickFlowerManager.getInstance().pickFlowerEntity;
			if(pfe != null && pfe.isEffect()){
				for(int i=0;i<num;i++){
					pfe.agreePickFlowerPlayer.add(100l+i);
				}
				PickFlowerManager.logger.error("[后台设置采花人数] ["+num+"]");
			}else{
				out.print("活动还没开启");
			}
			
		}
		
	%>
	
	<form action="">

		个数:<input type="text" name="name"/>
		<input type="submit" value="submit">
	
	</form>
</body>
</html>
