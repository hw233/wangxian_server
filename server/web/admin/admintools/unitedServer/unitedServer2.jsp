<%@page import="com.fy.engineserver.uniteserver.UnitedServerManager2"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.engineserver.uniteserver.UnitedServer"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	List<UnitedServer> serverList = new ArrayList<UnitedServer>();
	
	String mainServerName = request.getParameter("mainServerName");
	String mainServerConf = request.getParameter("mainServerConf");
	
	String otherServerName = request.getParameter("otherServerName");
	String otherServerConf = request.getParameter("otherServerConf");
	
	String action = request.getParameter("action");
	String pwd = request.getParameter("pwd");
	String [] otherServerNameArr = null;
	String [] otherServerConfArr = null;
	if (otherServerName != null && !"".equals(otherServerName) && otherServerConf != null && !"".equals(otherServerConf)) {
			otherServerNameArr = otherServerName.split("\r\n");
		 	otherServerConfArr = otherServerConf.split("\r\n");
	}
	if ("execute".equals(action)) {
		if ("woyaohefu&*(".equals(pwd)) {
			UnitedServer main = new UnitedServer(mainServerName, mainServerConf, true);
			serverList.add(main);
			if (otherServerNameArr.length == otherServerConfArr.length) {
				for (int i = 0; i < otherServerNameArr.length; i++) {
					String serverName = otherServerNameArr[i];
					String otherConf = otherServerConfArr[i];
					UnitedServer temp = new UnitedServer(serverName,otherConf, false);
					serverList.add(temp);
				}			
				long startTime = System.currentTimeMillis();
				out.print("<HR>合服开始<HR>");
				
				UnitedServerManager2.getInstance().uniteServerBegin(serverList.toArray(new UnitedServer[0]));
				out.print("<HR>合服完成,耗时:" + (System.currentTimeMillis() - startTime) + "ms<HR>");
			} else {
				out.print("<HR>合服配置错误<HR>");
			}
		} else {
			out.print("<HR>密码输入错误<HR>");
		}
	} else if ("query".equals(action)) {
		if ("woyaohefu&*(".equals(pwd)) {
			out.print("<h1>以下是配置的数据:</h1>");
			out.print("<table border='1'>");
			out.print("<tr style='font-weight: bold;font-size: 14px; color: white;background-color: red;'>");
				out.print("<td>");
					out.print("伺服器名称");
				out.print("</td>");
				out.print("<td>");
					out.print("伺服器配置");
				out.print("</td>");
			out.print("</tr>");
			
			out.print("<tr bgcolor=green>");
				out.print("<td>");
					out.print(mainServerName);
				out.print("</td>");
				out.print("<td>");
					out.print(mainServerConf);
			out.print("</td>");
			out.print("</tr>");
			for (int i = 0; i < otherServerNameArr.length; i++) {
				String serverName = otherServerNameArr[i];
				String otherConf = otherServerConfArr[i];
				out.print("<tr>");
					out.print("<td>");
						out.print(serverName);
					out.print("</td>");
					out.print("<td>");
						out.print(otherConf);
					out.print("</td>");
				out.print("</tr>");				
			}
				out.print("</table>");
			out.print("<HR/>");
			out.print("<HR/>");
			
			out.print("<table border='1'>");
				out.print("<tr>");
					out.print("<td>");
						out.print("是否合并数据");
					out.print("</td>");
					out.print("<td>");
						out.print("是否改名字");
					out.print("</td>");
					out.print("<td>");
						out.print("removeData");
					out.print("</td>");
				out.print("</tr>");
				out.print("<tr>");
					out.print("<td>");
						out.print(UnitedServerManager2.unitedData);
					out.print("</td>");
					out.print("<td>");
						out.print(UnitedServerManager2.unitedChanagerName);
					out.print("</td>");
					out.print("<td>");
						out.print(UnitedServerManager2.removeData);
					out.print("</td>");
				out.print("</tr>");
			out.print("</table>");
		} else {
			out.print("<HR>密码输入错误<HR>");
		}
	} else {
		mainServerConf = "/home/game/resin_jinfengyulu/webapps/game_server/WEB-INF/spring_config/simpleEMF.xml";
		otherServerConf = "/home/game/resin_youyunjinglong/webapps/game_server/WEB-INF/spring_config/simpleEMF.xml";
		mainServerName= GameConstants.getInstance().getServerName();
		otherServerName= "otherServers";
	}

%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body style="font-size: 12px;">
	<%
		if (!"execute".equals(action)) {
			out.print("<H2>请仔细确认合服的各项配置是否正确</H2>");
		}
	%>
	<% if (!"query".equals(action)) { %>
		<form action="./unitedServer2.jsp" method="post">
			<table border="1">
				<tr style="font-weight: bold;font-size: 14px; color: white;background-color: black;">
					<td>服务器类型</td>
					<td>服务器名字</td>
					<td>服务器配置</td>
				</tr>
				<tr>
					<td>主服务器/当前</td>
					<td><input type="text" name="mainServerName" value="<%=mainServerName%>"></td>
					<td><input type="text" name="mainServerConf" value="<%=mainServerConf%>" size="100"></td>
				</tr>
				<tr>
					<td>被合并的服务器<BR>换行分隔</td>
					<td><textarea rows="5" cols="20" name="otherServerName"><%=otherServerName%></textarea></td>
					<td><textarea rows="5" cols="100" name="otherServerConf"><%=otherServerConf%></textarea></td>
				</tr>
				<tr>
					<td>输入合服密码</td>
					<td><input name="pwd" type="password"><input type="hidden" value="query" name="action"></td>
					<td><input type="submit" value="数据校验"></td>
				</tr>
			</table>
		</form>
	<%} %>
	<hr>
	
	<% if("query".equals(action)) {//验证配置后才能合服
	%>	
		<form action="./unitedServer2.jsp">
			<table border="1">
				<tr style="font-weight: bold;font-size: 14px; color: white;background-color: black;">
					<td>服务器类型</td>
					<td>服务器名字</td>
					<td>服务器配置</td>
				</tr>
				<tr>
					<td>主服务器/当前</td>
					<td><input type="text" name="mainServerName" value="<%=mainServerName%>"></td>
					<td><input type="text" name="mainServerConf" value="<%=mainServerConf%>" size="100"></td>
				</tr>
				<tr>
					<td>被合并的服务器<BR><B><font color=red>换行分隔</font></B></td>
					<td><textarea rows="5" cols="20" name="otherServerName"><%=otherServerName%></textarea></td>
					<td><textarea rows="5" cols="100" name="otherServerConf"><%=otherServerConf%></textarea></td>
				</tr>
				<tr>
					<td>输入合服密码</td>
					<td><input name="pwd" type="password"><input type="hidden" value="execute" name="action"></td>
					<td><input type="submit" value="合服开始"></td>
				</tr>
			</table>
		</form>
	<%
		}
	%>
	<hr>
		要合并的class:<BR/>
		<%for(Class<?> c :UnitedServerManager2.uniteClasses){
			out.print(c.getName());
			out.print("<BR/>");
		} %>
	<hr>
	要删除的class:<BR/>
	<%for(Class<?> c :UnitedServerManager2.clearClasses){
		out.print(c.getName());
		out.print("<BR/>");
	} %>
</body>
</html>