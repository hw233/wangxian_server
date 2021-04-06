<%@ page contentType="text/html;charset=utf-8"%>
<%@ page
	import="java.util.*,com.xuanzhi.tools.text.*,com.xuanzhi.tools.simplejpa.impl.*,com.xuanzhi.tools.simplejpa.* "%>
<%

	String clazz = request.getParameter("cl");
	SimpleEntityManagerOracleImpl<?> o = SimpleEntityManagerFactoryHelper.getSimpleEntityManagerImpl(clazz);
	
	ArrayList<String[]> al = o.checkAllObjectsFieldModify();
%>
		
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title></title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
</head>

	<body>
		
		<h2 style="font-weight:bold;">
			【<%= SimpleEntityManagerFactoryHelper.getServerName() %>】数据存储服务，查看<%=clazz %>修改但忘记通知的对象
		</h2>
	
		<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>
		<tr bgcolor='#EEEEBB'><td>编号</td><td>类名</td><td>ID</td><td>属性</td><td>属性类型</td><td>数据库中的值</td><td>内存中的值</td></tr>
		<%
			for(int i = 0 ; i < al.size() ; i++){
				String s[] = al.get(i);
				out.println("<tr bgcolor='#FFFFFF'><td>"+(i+1)+"</td>");
				for(int j = 0 ; j < s.length ; j++){
					out.print("<td>"+s[j]+"</td>");
				}
				out.println("</tr>");
				
			}
		%>
		</table>
		</body>
</html>
