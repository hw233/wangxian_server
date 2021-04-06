<%@ page contentType="text/html;charset=utf-8" import="java.util.*,
com.xuanzhi.tools.text.*,
com.xuanzhi.tools.transport.*,java.nio.channels.*,com.fy.gamegateway.mieshi.server.*"%>
<%

		
//String beanName = "outer_gateway_connection_selector";
MieshiPlayerInfoManager mm = MieshiPlayerInfoManager.getInstance();
	
String username = request.getParameter("username");


	
%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache"%><html>
<head>
</head>
<body>
<h2>玩家服务器角色关系</h2>
<center>
<h2></h2><br><a href="./playerinfoXiuZheng.jsp">刷新此页面</a><br>
<form>
<input type='hidden' name='submitted' value='true'>
<input type='text' name='username' value='<%= username %>'><input type='submit' value='提交'>
</form>
<br>
<%
	if("true".equalsIgnoreCase(request.getParameter("submitted"))){
		Class cl = MieshiPlayerInfoManager.class;
		Field f = cl.getDeclaredField("cache");
		f.setAccessible(true);
		DefaultDiskCache cache = (DefaultDiskCache)f.get(mm);
		ArrayList<MieshiPlayerInfo> al = (ArrayList<MieshiPlayerInfo>)cache.get(username);
		if(al != null){
			for(int i = al.size() - 1; i >= 0; i--){
				if(al.get(i) == null){
					al.remove(i);
				}
			}
			cache.put(username,al);
		}
		MieshiPlayerInfo pis[] = mm.getMieshiPlayerInfoByUsername(username);
		out.println("查询结果：<br>");
		%><table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center">
	<td>编号</td>
	<td>服务器</td>
	<td>用户名</td><td>角色名</td>
	<td>等级</td><td>职业</td><td>上一次进入时间</td></tr><%
		for(int i = 0 ; i < pis.length ; i++){
			out.println("<tr bgcolor='#FFFFFF' align='center'>");
			out.println("<td>"+(i+1)+"</td>");
			out.println("<td>"+pis[i].getServerRealName()+"</td>");
			out.println("<td>"+pis[i].getUserName()+"</td>");
			out.println("<td>"+pis[i].getPlayerName()+"</td>");
			out.println("<td>"+pis[i].getLevel()+"</td>");
			out.println("<td>"+pis[i].getCareer()+"</td>");
			out.println("<td>"+DateUtil.formatDate(new Date(pis[i].lastAccessTime),"yyyy-MM-dd HH:mm:ss")+"</td>");
		}
	}
%>
</table><br>
<br/>

</center>
</body>
</html> 
