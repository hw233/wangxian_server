<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>

<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.newBillboard.BillboardsManager"%>
<%@page import="com.fy.engineserver.newBillboard.*"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="java.lang.reflect.Method"%><html>
<head>
<title>查询一个人的排行榜数据</title>
</head>
<body>

	<%
		String name = request.getParameter("name");
		if(name != null && !name.equals("")){
			try{
				Player player = PlayerManager.getInstance().getPlayer(name);
				BillboardStatDate date = BillboardStatDateManager.getInstance().getBillboardStatDate(player.getId());
				
				Class clazz = Class.forName("com.fy.engineserver.newBillboard.BillboardStatDate");
				Field[] fls = clazz.getDeclaredFields();
				Method method = null;
				for(Field f : fls){
					try{
					String fieldName = f.getName();
					 StringBuffer sb = new StringBuffer(fieldName);  
			         sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
			         method = clazz.getMethod("get" + sb.toString(), null);
	                 Object value = method.invoke(date, null);
	                 out.print("属性名:"+fieldName+"*****"+"属性值:"+value+"<br/>");
					}catch(Exception e1){
						out.print(e1);
						out.print("<br/>");
					}
				}
			}catch(Exception e){
				throw e;
			}
			return;	
		}
	%>
	
	<form action="">
		playerName:<input type="text" name="name"></input>
		<input type="submit" value="submit"></input>
	</form>

</body>

</html>
