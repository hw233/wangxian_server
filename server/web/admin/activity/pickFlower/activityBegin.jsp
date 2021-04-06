<%@ page language="java" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>


<%@page import="com.fy.engineserver.activity.pickFlower.PickFlowerManager"%>
<%@page import="com.fy.engineserver.activity.pickFlower.PickFlowerEntity"%><html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>开始采花活动</title>
</head>

<body>
	
	
	<%
	
		String nums = request.getParameter("num");
		if(nums != null && !nums.equals("")){
			
			if(PickFlowerManager.getInstance().pickFlowerEntity == null || !PickFlowerManager.getInstance().pickFlowerEntity.isEffect()){
				int num = Integer.parseInt(nums);
				PickFlowerManager.每次多长时间[0] = 2*60*1000;
				PickFlowerManager.每次多长时间[1] = 30*60*1000;
				PickFlowerEntity pe = new PickFlowerEntity(num);
				PickFlowerManager.getInstance().pickFlowerEntity = pe;
				PickFlowerManager.logger.error("[后台开启采花活动]");
				out.print("采花活动开始");
				PickFlowerManager.getInstance().pickFlowerBegin();
			}else{
				out.print("采花活动正在进行，你不能再次开启");
			}
			return;	
		}
		
	%>
	
	<form action="">
	
		设置0,时间会是俩分钟。进行测试
		第几次(0,1):<input type="text" name="num"/>
		<input type="submit" value="submit">
	
	</form>
</body>
</html>
