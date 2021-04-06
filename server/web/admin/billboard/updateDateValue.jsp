<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>

<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.newBillboard.BillboardsManager"%>
<%@page import="com.fy.engineserver.newBillboard.*"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="java.lang.reflect.Method"%>
<%@page import="com.fy.engineserver.util.StringTool"%>
<%@page import="com.xuanzhi.tools.text.StringUtil"%><html>
<head>
<title>更新玩家指定的属性</title>
</head>
<body>

	<!-- <a href=\"updateDateValue.jsp?value="+sb.toString()+"&&id="+player.getId()+" -->
	<%
	
	
		String name = request.getParameter("name");
		String fileName = request.getParameter("fileName");
		String values = request.getParameter("value");
		if(name != null && !name.equals("") && values != null && !values.equals("") && fileName != null && !fileName.equals("")){
			try{
				Player player = PlayerManager.getInstance().getPlayer(name);
				BillboardStatDate date = BillboardStatDateManager.getInstance().getBillboardStatDate(player.getId());
				Class clazz = Class.forName("com.fy.engineserver.newBillboard.BillboardStatDate");
				
				Field[] fls = clazz.getDeclaredFields();
				boolean have = false;
				for(Field f : fls){
					if(f.getName().equals(fileName)){
						have = true;
						break;
					}
				}
				if(!have){
					out.print("输入属性错误，没有指定属性");
					return;
				}
				StringBuffer sb = new StringBuffer(fileName);  
		        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
		        Method method = null;
		        
		        method = clazz.getMethod("get" + sb.toString(), null);
		        Object value = method.invoke(date, null);
				
		        method = clazz.getMethod("set" + sb.toString(), int.class);
		        method.invoke(date,Integer.parseInt(values));
		        
			}catch(Exception e){
				out.print(StringUtil.getStackTrace(e));
			}
			out.print("修改完毕");
			out.print("<a href=\"javascript:window.history.back()\">返回</a>");
			return;	
		}
	%>
	
	<form action="">
		playerName:<input type="text" name="name"/><br/>
		属性名:<input type="text" name="fileName"/><br/>
		想要设置的值(数字):<input type="text" name="value"/><br/>
		<input type="submit" value="submit"/><br/>
	
	
	</form>
	
</body>

</html>
