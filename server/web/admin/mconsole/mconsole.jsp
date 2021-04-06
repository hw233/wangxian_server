<%@page import="com.fy.engineserver.util.TimeTool"%>
<%@page import="com.fy.engineserver.util.console.ChangeAble"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="com.fy.engineserver.util.console.MConsole"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.util.console.MConsoleManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	List<MConsole> mconsoles = MConsoleManager.getConsoles();
	String className = request.getParameter("className");
	MConsole console = MConsoleManager.getMConsole(className);
	if (console == null) {
		out.print("<H1>未配置的class:"+className+"</H1>");
		return;
	}
	Field[] fields = MConsoleManager.getMConsoleFields(console);
	if ("modify".equals(request.getParameter("option"))) {	//要修改数据
		if ("changeable".equals(request.getParameter("pwd"))) {
			out.print("<h2>属性值修改完毕:<br></h2><center><table border='1'>");
			out.print("<tr style='background-color: #6AACD9;font-weight: bold;color: white;text-align: center;'>");
				out.print("<td>");
					out.print("字段");
				out.print("</td>");
				out.print("<td>");
					out.print("原值");
				out.print("</td>");
				out.print("<td>");
					out.print("新值");
				out.print("</td>");
			out.print("</tr>");
			for (Field f : fields) {
				String value = request.getParameter(f.getName());
				try {
					Object oldValue = f.get(console).toString();
					ChangeAble ca = f.getAnnotation(ChangeAble.class);
					if (ca.isTime()) {
						value = String.valueOf(TimeTool.formatter.varChar19.parse(value));
					}
					MConsoleManager.setValue(console, f, value);
					if (!String.valueOf(oldValue).equals(String.valueOf(value))) {
						out.print("<tr>");
							out.print("<td>");
								out.print(f.getName());
							out.print("</td>");
							out.print("<td>");
								out.print(String.valueOf(oldValue));
							out.print("</td>");
							out.print("<td>");
								out.print(String.valueOf(value));
							out.print("</td>");
						out.print("</tr>");
						
					}
				} catch (Throwable t ) {
					t.printStackTrace();
					out.print(t.getLocalizedMessage() + "<BR/>");
				}
				
			}
			out.print("</table></center><hr><hr><hr>");
		} else {
			out.print("<h2>密码输入错误，不能操作!</h2>");
		}
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>后台修改属性</title>
</head>
<body>
<form action="./mconsole.jsp" method="post">
<center>
	<table style="font-size: 14px;width: 60%" border="1">
		<tr style="background-color: #6AACD9;font-weight: bold;color: white;text-align: center;">
			<td>类名</td>
			<td>名字</td>
			<td colspan="2">描述</td>
		</tr>
		<tr >
			<td><%=console.getClass().getSimpleName() %></td>
			<td><%=console.getMConsoleName() %></td>
			<td colspan="2"><%=console.getMConsoleDescription() %></td>
		</tr>
		<tr style="background-color: #6AACD9;font-weight: bold;color: white;text-align: center;">
			<td>字段</td>
			<td>字段类型</td>
			<td>名字</td>
			<td>值</td>
		</tr>
	<%
		for (Field f : fields) {
			f.setAccessible(true);
			boolean isTime = ((ChangeAble) f.getAnnotation(ChangeAble.class)).isTime();
		%>
			<tr>
				<td><%=f.getAnnotation(ChangeAble.class).value()%></td>
				<td><%=f.getType() %></td>
				<td><%=f.getName() %></td>
				<td><input name="<%=f.getName() %>" value="<%=isTime? TimeTool.formatter.varChar19.format((Long)f.get(console)): f.get(console)%>"></td>
			</tr>	
		<%
		}
	%>
	<tr style="background-color: #6AACD9;font-weight: bold;color: white;text-align: center;">
		<td>
			输入修改密码:
		</td>
		<td>
			<input type="password" name="pwd">
		</td>
		<td colspan="2">
			<input type="hidden" value="modify" name="option">
			<input type="hidden" value="<%=className %>" name="className">
			<input type="submit" value="提交">
		</td>
	</tr>
	</table>
</center>
</form>
</body>
</html>