<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.google.gson.*,java.util.*,java.io.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">



<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.*"%>
<%@page import="java.text.*"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>查看道具使用记录</title>
</head>
<body>

	<%
		String name = request.getParameter("name");
		if(name == null || name.equals("")){
			%>
			
			<form action="">
				用户名 ：<input type="text" name="name" />
				<input type="submit" value="submit" />
			</form>
			
			<%
			
			
		}else{
			
			Player player = PlayerManager.getInstance().getPlayer(name);
			
			Map<String, PropsUseRecord> map = player.getPropsUseRecordMap();
			
			if(map != null && map.size() > 0){
				
				 Set<Map.Entry<String,PropsUseRecord>> entry = map.entrySet();
				 %>
				 
					<table cellspacing="1" cellpadding="2" border="1" >
					<tr>
		  				<td>name</td>
		  				<td>次数</td>
		  				<td>上次使用时间</td>
		  			</tr>				 
				 
				 
				 
				 <%
				 for(Map.Entry<String,PropsUseRecord> en : entry){
					 
					SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					 long time = en.getValue().getLastUseTime();
					 String timeS = format1.format(new Date(time));
					 %>
					<tr>
		  				<td><%=en.getValue().getPropsName() %></td>
		  				<td><%= en.getValue().getUseNum() %></td>
		  				<td><%= timeS %></td>
		  			</tr>
					 
					 <%
					 
				 }
				
				 %>
				 </table>
				 <%
				
				
			}else{
				out.print("没有使用记录");
			}
			
		}
	
	
	%>




</body>
</html>
